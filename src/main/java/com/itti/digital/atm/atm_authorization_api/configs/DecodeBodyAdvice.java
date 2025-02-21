package com.itti.digital.atm.atm_authorization_api.configs;


import com.itti.digital.atm.atm_authorization_api.service.impl.AuthorizationServiceImpl;
import com.itti.digital.atm.atm_authorization_api.utils.DecodeHttpInputMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestControllerAdvice // Don't forget the @RestControllerAdvice annotation. It will take effect for all RestControllers.
@Component
public class DecodeBodyAdvice extends RequestBodyAdviceAdapter {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(DecodeBodyAdvice.class);
    private CryptoConfigs cryptoConfigs;
    private Boolean forceCrypto;

    public DecodeBodyAdvice(CryptoConfigs cryptoConfigs,@Value("${forceCrypto}") Boolean forceCrypto) {
        this.cryptoConfigs = cryptoConfigs;
        this.forceCrypto = forceCrypto;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * This method will be executed before spring mvc reads the request body. We can do some pre-processing of the request body here.
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        try (InputStream inputStream = inputMessage.getBody()) {

            List<String> cryptoEnabledHeader =inputMessage.getHeaders().get("Crypto-Enabled");

            String cryptoEnabled = cryptoEnabledHeader!=null?cryptoEnabledHeader.get(0):null;


            List<String> tedIdHeader =inputMessage.getHeaders().get("Ted-Id");
            String tedId = tedIdHeader!=null?tedIdHeader.get(0):null;


            if((cryptoEnabled!=null && cryptoEnabled.equals("true")) || forceCrypto) {

                // Read request body
                byte[] body = StreamUtils.copyToByteArray(inputStream);

                log.info("raw request: {}", new String(body));

                // Base64 Decode
                String decodedBody = cryptoConfigs.decrypt(new String(body),tedId);

                if(!(decodedBody.contains("pass")||decodedBody.contains("password"))) {
                    log.info("decoded request: {}", decodedBody);
                }else{
                    log.info("decoded request contains sensible data");
                }

                // Return the decoded body
                return new DecodeHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(decodedBody.getBytes(StandardCharsets.UTF_8)));
            } else {
                byte[] body = StreamUtils.copyToByteArray(inputStream);

                return new DecodeHttpInputMessage(inputMessage.getHeaders(), new ByteArrayInputStream(body));
            }
        } catch (Exception e) {
            log.error("Error while decoding body", e);
            return null;
        }
    }


}
