package com.itti.digital.atm.atm_authorization_api.configs;


import com.itti.digital.atm.atm_authorization_api.utils.CryptoUtils;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;





@Component
public class CryptoConfigs {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(CryptoConfigs.class);
    @Value("${serverp12Pass}")
    private String serverp12Pass;
    @Value("${interceptor.response.serverp12}")
    private String interceptorResponseServerp12;
    @Value("${interceptor.response.serverp12Pass}")
    private String interceptorResponseServerp12Pass;
    @Value("${interceptor.response.clientcert}")
    private String interceptorResponseClientcert;

    @Value("${serverp12.base64}")
    private String serverBase64;
    @Value("${clientcert.base64}")
    private String clientBase64;
    @Value("${interceptor.response.serverp12.base64}")
    private String interceptorServerBase64;
    @Value("${interceptor.response.clientcert.base64}")
    private String interceptorClientBase64;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private PublicKey publicKeyClientResponse;

    private PrivateKey privateKeyClientResponse;

    @Bean
    public String configureCrypto() {
        log.info("Configuring Crypto");

        try {

            Security.addProvider(new BouncyCastleProvider());

            byte[] associatedData = null;

            X509Certificate cer = null;

            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
           if(clientBase64 != null && !clientBase64.isEmpty()) {
               InputStream is = new ByteArrayInputStream( Base64.decodeBase64(clientBase64));
               cer = (X509Certificate) certFactory.generateCertificate(is);

           }

            publicKey = cer.getPublicKey();
            KeyStore p12 = KeyStore.getInstance("pkcs12");

            if(serverBase64 != null && !serverBase64.isEmpty()) {
                InputStream is = new ByteArrayInputStream( Base64.decodeBase64(serverBase64));
                p12.load(is, serverp12Pass.toCharArray());

            }

            privateKey = (PrivateKey) p12.getKey("server", serverp12Pass.toCharArray());

            //CONFIG PARA INTERCEPTOR
            CertificateFactory certFactory2 = CertificateFactory.getInstance("X.509");
            X509Certificate cer2;
            InputStream is2 = new ByteArrayInputStream( Base64.decodeBase64(interceptorClientBase64));
            cer2 = (X509Certificate) certFactory.generateCertificate(is2);
            publicKeyClientResponse = cer2.getPublicKey();
            KeyStore p12ClientResponse = KeyStore.getInstance("pkcs12");

            InputStream is3 = new ByteArrayInputStream( Base64.decodeBase64(interceptorServerBase64));
            p12ClientResponse.load(is3, interceptorResponseServerp12Pass.toCharArray());
            privateKeyClientResponse = (PrivateKey) p12ClientResponse.getKey("client", interceptorResponseServerp12Pass.toCharArray());
        } catch (Exception e) {
            log.error("ERROR CONFIGURING CRYPTO: ", e);
        }finally {
            return "done";
        }
    }

    public String encrypt(String message,String additionalData) throws Exception{
        byte[] textoPlanoBytes = message.getBytes();
        CryptoUtils c = new CryptoUtils();

        byte[] resEnc = c.signAndEncryptSign(textoPlanoBytes, additionalData!=null?additionalData.getBytes():null, privateKey, publicKey);

        //String resBase64 = Base64.getEncoder().encodeToString(resEnc);
        String resBase64 = Base64.encodeBase64String(resEnc);
        return resBase64;
    }

    public String decrypt(String message,String additionalData)throws Exception{
        CryptoUtils c = new CryptoUtils();
       // byte[] data = Base64.getDecoder().decode(message);
        byte[] data = Base64.decodeBase64(message);
        byte[] res = c.verifyDecryptAndVerify(data, additionalData!=null?additionalData.getBytes():null, privateKey, publicKey);
        return new String(res);
    }


    public String interceptorDecrypt(String message,String additionalData)throws Exception{
        CryptoUtils c = new CryptoUtils();
        // byte[] data = Base64.getDecoder().decode(message);
        byte[] data = Base64.decodeBase64(message);
        byte[] res = c.verifyDecryptAndVerify(data, additionalData!=null?additionalData.getBytes():null, privateKeyClientResponse, publicKeyClientResponse);
        return new String(res);
    }
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }


}
