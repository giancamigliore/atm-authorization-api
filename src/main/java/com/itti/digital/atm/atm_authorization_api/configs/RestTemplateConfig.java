package com.itti.digital.atm.atm_authorization_api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate encryptionRestTemplate(GlobalProperties gp, CryptoConfigs cryptoConfigs){
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(gp.getConnTimeout(),gp.getReadTimeOut())));
        if(gp.getClientForceCrypto().equalsIgnoreCase("true")) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new EncryptionInterceptor(cryptoConfigs, gp));
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;

    }

    @Bean
    public RestTemplate restTemplate(GlobalProperties gp, CryptoConfigs cryptoConfigs){
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(getClientHttpRequestFactory(gp.getConnTimeout(),gp.getReadTimeOut())));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;

    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory(int connTimeOutWepa, int readTimeOutWepa) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
                = new SimpleClientHttpRequestFactory();
        //Connect timeout

        clientHttpRequestFactory.setConnectTimeout((connTimeOutWepa));

        //Read timeout
        clientHttpRequestFactory.setReadTimeout((readTimeOutWepa));
        return clientHttpRequestFactory;
    }

}
