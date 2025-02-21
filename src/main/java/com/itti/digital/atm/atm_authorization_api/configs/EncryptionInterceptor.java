package com.itti.digital.atm.atm_authorization_api.configs;


import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsTypeException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@Component
public class EncryptionInterceptor implements ClientHttpRequestInterceptor {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(EncryptionInterceptor.class);
    GlobalProperties gp;
    CryptoConfigs cryptoConfigs;

    public EncryptionInterceptor(CryptoConfigs cryptoConfigs, GlobalProperties gp) {
        this.cryptoConfigs = cryptoConfigs;
        this.gp = gp;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse decryptedRes =null;

        Boolean clientCryptoEnabled = gp.getClientForceCrypto().equalsIgnoreCase("true")?true:false;
       try {
           if(!request.getURI().toURL().getPath().contains("/login")&& clientCryptoEnabled) {
               String reqBody = new String(body);
               if (!(reqBody.contains("pass") || reqBody.contains("password"))) {
                   log.info("REQUEST BODY: " + reqBody);
               } else {
                   log.info("REQUEST BODY CONTAINS SENSIBLE DATA");
               }
               String encBodyReq = cryptoConfigs.encrypt(reqBody, gp.getClientAdditionalData());
               log.info("ENCRYPTED REQUEST BODY: " + encBodyReq);
               // log.info("deCRYPTED REQUEST BODY: " + cryptoConfigs.decrypt(encBodyReq, "ATM000"));
               byte[] encryptedRequestBody = encBodyReq.getBytes();
               byte[] decryptedResponseBodyBytes = null;
               request.getHeaders().add("Crypto-Enabled", clientCryptoEnabled.toString());
               request.getHeaders().add("Ted-Id", gp.getClientAdditionalData());
               ClientHttpResponse response = execution.execute(request, encryptedRequestBody);
               String responseBody=null;

               if(response.getStatusCode()==HttpStatus.OK || response.getStatusCode()==HttpStatus.CREATED
               || response.getStatusCode()==HttpStatus.ACCEPTED || response.getStatusCode()==HttpStatus.NON_AUTHORITATIVE_INFORMATION
               || response.getStatusCode()==HttpStatus.NO_CONTENT || response.getStatusCode()==HttpStatus.RESET_CONTENT
               || response.getStatusCode()==HttpStatus.PARTIAL_CONTENT || response.getStatusCode()==HttpStatus.MULTI_STATUS
               || response.getStatusCode()==HttpStatus.ALREADY_REPORTED || response.getStatusCode()==HttpStatus.IM_USED ){
                   responseBody = new String(response.getBody().readAllBytes());
                   decryptedResponseBodyBytes = cryptoConfigs.interceptorDecrypt(responseBody, gp.getClientAdditionalData()).getBytes();
               }else{
                   if(response.getStatusCode()==HttpStatus.NOT_FOUND){

                       decryptedResponseBodyBytes =null;
                   }else {
                       responseBody = "";
                       decryptedResponseBodyBytes = responseBody.getBytes();
                   }
               }

               String decryptedResponseBody = decryptedResponseBodyBytes!=null?new String(decryptedResponseBodyBytes):null;
                   log.info("RESPONSE BODY: " + decryptedResponseBody);

               // prepare modified response
               decryptedRes = new ClientHttpResponse() {
                   @Override
                   public HttpHeaders getHeaders() {
                       if(response!=null){
                            return response.getHeaders();
                       }else{

                           return new HttpHeaders();
                       }
                   }

                   @Override
                   public InputStream getBody() throws IOException {
                       // The expected modified response body to be populated here
                       return decryptedResponseBody!=null?new ByteArrayInputStream(decryptedResponseBody.getBytes()):null;
                   }

                   @Override
                   public HttpStatus getStatusCode() throws IOException {
                       if(response!=null){
                           return HttpStatus.resolve(response.getStatusCode().value());
                       }else{
                            return HttpStatus.NOT_FOUND;
                       }
                   }


                   @Override
                   public String getStatusText() throws IOException {
                       return response.getStatusText();
                   }

                   @Override
                   public void close() {

                   }
               };
           }else{


               ClientHttpResponse response = execution.execute(request, body);
               String responseBody = new String(response.getBody().readAllBytes());

               log.info("RESPONSE BODY: " + responseBody);
               // prepare modified response
               decryptedRes = new ClientHttpResponse() {
                   @Override
                   public HttpHeaders getHeaders() {
                       return response.getHeaders();
                   }

                   @Override
                   public InputStream getBody() throws IOException {
                       // The expected modified response body to be populated here
                       return responseBody!=null?new ByteArrayInputStream(responseBody.getBytes()):null;
                   }

                   @Override
                   public HttpStatus getStatusCode() throws IOException {
                       return HttpStatus.resolve(response.getStatusCode().value());
                   }

                   @Override
                   public String getStatusText() throws IOException {
                       return response.getStatusText();
                   }

                   @Override
                   public void close() {

                   }
               };
           }
       }catch(Exception e){
            log.error("ERROR EN ENCRYPTION INTERCEPTOR: ", e);

            throw new AlfaMsException(AlfaMsTypeException.INTERNAL, ErrorConstants.HTTP_500,ErrorConstants.HTTP_INTERNAL+": "+e.getMessage());
       }finally {
           if(decryptedRes!=null) {
               decryptedRes.getHeaders().setContentType(MediaType.APPLICATION_JSON);
           }
           return decryptedRes;
       }

    }
}
