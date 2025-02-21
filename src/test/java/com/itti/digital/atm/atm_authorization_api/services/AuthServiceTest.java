package com.itti.digital.atm.atm_authorization_api.services;


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
import com.itti.digital.atm.atm_authorization_api.service.impl.AuthServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    RestTemplate restTemplate = mock(RestTemplate.class);
    CryptoConfigs cryptoConfigs = mock(CryptoConfigs.class);
    String endpoint = "url";
    GlobalProperties gp = mock(GlobalProperties.class);
    AuthService service = new AuthServiceImpl(restTemplate, cryptoConfigs, gp);

    @AfterEach
    public void cleanup() {
        Mockito.clearAllCaches();
    }

    @Test
    void getWebToken_validCredentialsPresented_tokenGenerated()throws Exception{
        RequestGetWebToken requestGetWebToken = new RequestGetWebToken();
        requestGetWebToken.setAppPassword("12345");
        requestGetWebToken.setAppUsername("k3app");
        requestGetWebToken.setTedPassword("12345");
        requestGetWebToken.setTedUsername("k3app");

        ResponseGetWebToken responseGetWebToken = new ResponseGetWebToken();
        responseGetWebToken.setAccess_token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrM2FwcCIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV9BRE1JTlwifSx7XCJhdXRob3JpdHlcIjpcIlJPTEVfVVNFUlwifV0iLCJ1c2VybmFtZSI6ImszYXBwIiwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzM5ODE0OTM4LCJleHAiOjE3Mzk4MTg1Mzh9.COmLajufd6bYa6y7Z7JK3HESW5iQbf8A6yS_6IO0ALU");
        responseGetWebToken.setToken_type("JWT");
        responseGetWebToken.setRefresh_token(null);
        responseGetWebToken.setExpires_in(3600);
        responseGetWebToken.setExpires_at("Mon Feb 17 15:55:38 PYST 2025");
        responseGetWebToken.setScope("read write");
        responseGetWebToken.setAdditional_info("Hola k3app has iniciado sesion con exito");
        responseGetWebToken.setName("k3app");
        responseGetWebToken.setJti(null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestGetWebToken.getAppUsername(), requestGetWebToken.getAppPassword());
        OauthRequest oauthRequest = new OauthRequest();
        oauthRequest.setPassword(requestGetWebToken.getTedPassword());
        oauthRequest.setUsername(requestGetWebToken.getTedUsername());
        Gson json = new Gson();
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(json.toJson(oauthRequest), headers);

        when(gp.getSecurityEnabled()).thenReturn(true);
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        //restTemplate.postForObject(,httpEntity, ResponseGetWebToken.class)
        when(restTemplate.postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class)).thenReturn(responseGetWebToken);
        //WHEN
        ResponseGetWebToken  response = service.getWebToken(requestGetWebToken);
        //THEN
        assertThat(response).extracting("name").isEqualTo(responseGetWebToken.getName());
        verify(restTemplate).postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class);

    }

    @Test
    void getWebToken_validCredentialsPresented_tokenGeneratedException()throws Exception{
        RequestGetWebToken requestGetWebToken = new RequestGetWebToken();
        requestGetWebToken.setAppPassword("12345");
        requestGetWebToken.setAppUsername("k3app");
        requestGetWebToken.setTedPassword("12345");
        requestGetWebToken.setTedUsername("k3app");

        ResponseGetWebToken responseGetWebToken = new ResponseGetWebToken();
        responseGetWebToken.setAccess_token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrM2FwcCIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV9BRE1JTlwifSx7XCJhdXRob3JpdHlcIjpcIlJPTEVfVVNFUlwifV0iLCJ1c2VybmFtZSI6ImszYXBwIiwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzM5ODE0OTM4LCJleHAiOjE3Mzk4MTg1Mzh9.COmLajufd6bYa6y7Z7JK3HESW5iQbf8A6yS_6IO0ALU");
        responseGetWebToken.setToken_type("JWT");
        responseGetWebToken.setRefresh_token(null);
        responseGetWebToken.setExpires_in(3600);
        responseGetWebToken.setExpires_at("Mon Feb 17 15:55:38 PYST 2025");
        responseGetWebToken.setScope("read write");
        responseGetWebToken.setAdditional_info("Hola k3app has iniciado sesion con exito");
        responseGetWebToken.setName("k3app");
        responseGetWebToken.setJti(null);

        HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestGetWebToken.getAppUsername(), requestGetWebToken.getAppPassword());
        OauthRequest oauthRequest = new OauthRequest();
        oauthRequest.setPassword(requestGetWebToken.getTedPassword());
        oauthRequest.setUsername(requestGetWebToken.getTedUsername());
        Gson json = new Gson();
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(json.toJson(oauthRequest), headers);

        when(gp.getSecurityEnabled()).thenReturn(true);
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        //restTemplate.postForObject(,httpEntity, ResponseGetWebToken.class)
        when(restTemplate.postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class)).thenThrow(httpClientErrorException);
        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.getWebToken(requestGetWebToken);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.SECURITY, ErrorConstants.ERROR_CODE_ENCRYPTION_FAIL, ErrorConstants.ERROR_MESSAGE_ENCRYPTION_FAIL);
        verify(restTemplate).postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class);

    }

    @Test
    void getWebToken_validCredentialsPresented_tokenGeneratedResourceAccessException()throws Exception{
        RequestGetWebToken requestGetWebToken = new RequestGetWebToken();
        requestGetWebToken.setAppPassword("12345");
        requestGetWebToken.setAppUsername("k3app");
        requestGetWebToken.setTedPassword("12345");
        requestGetWebToken.setTedUsername("k3app");

        ResponseGetWebToken responseGetWebToken = new ResponseGetWebToken();
        responseGetWebToken.setAccess_token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrM2FwcCIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV9BRE1JTlwifSx7XCJhdXRob3JpdHlcIjpcIlJPTEVfVVNFUlwifV0iLCJ1c2VybmFtZSI6ImszYXBwIiwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNzM5ODE0OTM4LCJleHAiOjE3Mzk4MTg1Mzh9.COmLajufd6bYa6y7Z7JK3HESW5iQbf8A6yS_6IO0ALU");
        responseGetWebToken.setToken_type("JWT");
        responseGetWebToken.setRefresh_token(null);
        responseGetWebToken.setExpires_in(3600);
        responseGetWebToken.setExpires_at("Mon Feb 17 15:55:38 PYST 2025");
        responseGetWebToken.setScope("read write");
        responseGetWebToken.setAdditional_info("Hola k3app has iniciado sesion con exito");
        responseGetWebToken.setName("k3app");
        responseGetWebToken.setJti(null);

        ResourceAccessException resourceAccessException = mock(ResourceAccessException.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestGetWebToken.getAppUsername(), requestGetWebToken.getAppPassword());
        OauthRequest oauthRequest = new OauthRequest();
        oauthRequest.setPassword(requestGetWebToken.getTedPassword());
        oauthRequest.setUsername(requestGetWebToken.getTedUsername());
        Gson json = new Gson();
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(json.toJson(oauthRequest), headers);

        when(gp.getSecurityEnabled()).thenReturn(true);
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        //restTemplate.postForObject(,httpEntity, ResponseGetWebToken.class)
        when(restTemplate.postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class)).thenThrow(resourceAccessException);
        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.getWebToken(requestGetWebToken);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.SECURITY, ErrorConstants.ERROR_CODE_ENCRYPTION_FAIL, ErrorConstants.ERROR_MESSAGE_ENCRYPTION_FAIL);
        verify(restTemplate).postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class);

    }

    @Test
    void getWebToken_withDisabledSecurityAndValidCredentialsPresented_nullResponse()throws Exception{
        RequestGetWebToken requestGetWebToken = new RequestGetWebToken();
        requestGetWebToken.setAppPassword("12345");
        requestGetWebToken.setAppUsername("k3app");
        requestGetWebToken.setTedPassword("12345");
        requestGetWebToken.setTedUsername("k3app");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestGetWebToken.getAppUsername(), requestGetWebToken.getAppPassword());
        OauthRequest oauthRequest = new OauthRequest();
        oauthRequest.setPassword(requestGetWebToken.getTedPassword());
        oauthRequest.setUsername(requestGetWebToken.getTedUsername());
        Gson json = new Gson();
        org.springframework.http.HttpEntity<String> httpEntity = new org.springframework.http.HttpEntity<>(json.toJson(oauthRequest), headers);

        when(gp.getSecurityEnabled()).thenReturn(false);
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        //NO DEBERIA LLAMARSE A NINGUN ENDPOINT CON EL SECURE DISABLED
        when(restTemplate.postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class)).thenReturn(null);
        //WHEN
        ResponseGetWebToken  response = service.getWebToken(requestGetWebToken);
        //THEN
        assertThat(response).isEqualTo(null);
        verify(restTemplate,never()).postForObject("http://localhost:"+gp.getServerPort()+"/atm-authorization-api/login",httpEntity,ResponseGetWebToken.class);

    }





}
