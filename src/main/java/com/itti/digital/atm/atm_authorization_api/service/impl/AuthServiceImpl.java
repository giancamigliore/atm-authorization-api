package com.itti.digital.atm.atm_authorization_api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itti.digital.atm.atm_authorization_api.configs.CryptoConfigs;
import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsTypeException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import com.itti.digital.atm.atm_authorization_api.models.authentication.OauthRequest;
import com.itti.digital.atm.atm_authorization_api.models.authentication.RequestGetWebToken;
import com.itti.digital.atm.atm_authorization_api.models.authentication.ResponseGetWebToken;
import com.itti.digital.atm.atm_authorization_api.service.AuthService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Giancarlo Migliore
 */

@Service
public class AuthServiceImpl implements AuthService {
    final static Logger log = org.slf4j.LoggerFactory.getLogger(AuthServiceImpl.class);
    private RestTemplate restTemplate;
    private GlobalProperties gp;
    private CryptoConfigs cryptoConfigs;

    @Autowired
    public AuthServiceImpl( @Qualifier("restTemplate")RestTemplate restTemplate,
                           CryptoConfigs cryptoConfigs,
                           GlobalProperties gp
    ) {

        this.restTemplate = restTemplate;
        this.cryptoConfigs = cryptoConfigs;
        this.gp = gp;

    }


    @Override
    public ResponseGetWebToken getWebToken(RequestGetWebToken getWebToken) throws AlfaMsException {
        Thread.currentThread().setName(Calendar.getInstance().getTimeInMillis() + "");
        ResponseGetWebToken responseGetWebToken = null;
        if(gp.getSecurityEnabled()) {
            log.info("************** INICIA GET WEB TOKEN **************");

            try {
                responseGetWebToken = authenticateToSpringSecurity(getWebToken);

            } catch (Exception e) {
                log.error("ERROR AL ENCRIPTAR EL PASSWORD: " + e.getMessage());
                throw new AlfaMsException(AlfaMsTypeException.SECURITY, ErrorConstants.ERROR_CODE_ENCRYPTION_FAIL, ErrorConstants.ERROR_MESSAGE_ENCRYPTION_FAIL);
            }

            Gson gson = new Gson();
            log.info("RESPUESTA DEL SERVICIO:" + gson.toJson(responseGetWebToken));
            log.info("************** FINALIZA GET WEB TOKEN *****************");
        }else{
            log.info("************** GET WEB TOKEN DESACTIVADO**************");
        }
        return responseGetWebToken;
    }


    private ResponseGetWebToken authenticateToSpringSecurity(RequestGetWebToken request) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(request.getAppUsername(), request.getAppPassword());
        log.info("URL:http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login");

        OauthRequest oauthRequest = new OauthRequest();
        oauthRequest.setPassword(request.getTedPassword());
        oauthRequest.setUsername(request.getTedUsername());
        Gson json = new Gson();
        HttpEntity<String> httpEntity = new HttpEntity<>(json.toJson(oauthRequest), headers);
        try {
            ResponseGetWebToken response = restTemplate.postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity, ResponseGetWebToken.class);
            return response;
        } catch (HttpClientErrorException exception) {
            log.error("ERROR HTTP:" + exception.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(exception.getResponseBodyAsString(), ResponseGetWebToken.class);
        } catch (ResourceAccessException exception) {
            log.error("ERROR AL ACCEDER:" + exception.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> errorMap = new HashMap<>();
            Gson gsonError = new Gson();
            errorMap.put("message", "SE PRODUJO UN ERROR AL OBTENER EL TOKEN " + exception.getMessage());
            errorMap.put("code", "98");
            return mapper.readValue(gsonError.toJson(errorMap), ResponseGetWebToken.class);
        }
    }


}
