package com.itti.digital.atm.atm_authorization_api.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;



@RestControllerAdvice
public class EncodeBodyAdvice implements ResponseBodyAdvice <Object>{

    final static Logger log = org.slf4j.LoggerFactory.getLogger(EncodeBodyAdvice.class);

    private CryptoConfigs cryptoConfigs;


    private Boolean forceCrypto;

    public EncodeBodyAdvice(CryptoConfigs cryptoConfigs,@Value("${forceCrypto}") Boolean forceCrypto) {
        this.cryptoConfigs = cryptoConfigs;
        this.forceCrypto = forceCrypto;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String className = returnType.getContainingClass().toString();

        String methodName = returnType.getMethod().toString();

        if (className.contains("Controller") || className.contains("MsBaseExceptionHandler")) {

            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            List<String> cryptoEnabledHeader =request.getHeaders().get("Crypto-Enabled");
            String cryptoEnabled = cryptoEnabledHeader!=null?cryptoEnabledHeader.get(0):null;

            List<String> tedIdHeader =request.getHeaders().get("Ted-Id");
            String tedId = tedIdHeader!=null?tedIdHeader.get(0):null;

            if((cryptoEnabled!=null && cryptoEnabled.equals("true") )|| forceCrypto){
                ObjectMapper om = new ObjectMapper();
                String stringBody = om.writeValueAsString(body);

                 log.info("raw response: {}", stringBody);

            // Base64 Decode
                String encodedBody = cryptoConfigs.encrypt(stringBody,tedId);

                log.info("encoded response: {}", encodedBody);
                response.getHeaders().setContentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE));
                return encodedBody;
            }else{
                response.getHeaders().setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
                return body;
            }
        }catch(Exception e){
            log.error("Error while encoding body", e);
            return null;
        }
    }





}


