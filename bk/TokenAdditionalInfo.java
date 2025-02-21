package com.itti.digital.atm.atm_authorization_api.configs.springsecurity;

import com.itti.digital.atm.atm_authorization_api.entities.User;
import com.itti.digital.atm.atm_authorization_api.service.IUserService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenAdditionalInfo implements TokenEnhancer {

    private IUserService userService;

    public TokenAdditionalInfo(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String,Object> info = new HashMap<String,Object>();
        info.put("addiotional_info","Hola ".concat(oAuth2Authentication.getName()));
        User user = userService.findByUsername(oAuth2Authentication.getName());
        info.put("name",user.getUsername());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
