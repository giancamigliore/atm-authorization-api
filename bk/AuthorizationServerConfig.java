package com.itti.digital.atm.atm_authorization_api.configs.springsecurity;


import com.itti.digital.atm.atm_authorization_api.configs.CryptoConfigs;
import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    final static Logger log = org.slf4j.LoggerFactory.getLogger(AuthorizationServerConfig.class);

    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private TokenAdditionalInfo tokenAdditionalInfo;

    private CryptoConfigs cryptoConfigs;

    private GlobalProperties gp;

    public AuthorizationServerConfig(BCryptPasswordEncoder passwordEncoder,@Qualifier("authenticationManager") AuthenticationManager authenticationManager, TokenAdditionalInfo tokenAdditionalInfo, CryptoConfigs cryptoConfigs, GlobalProperties gp) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenAdditionalInfo = tokenAdditionalInfo;
        this.cryptoConfigs = cryptoConfigs;
        this.gp = gp;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("CONFIGURANDO AuthorizationServer");
        log.info("Encrypted Secret:"+gp.getAuthorizationServerConfigEncryptedPass());
        log.info("Additional Data:"+gp.getAuthorizationServerConfigEncryptedPassAdditionalData());
        log.info("Client Id:"+gp.getAuthorizationServerConfigEncryptedPassClientId());
        log.info("Access Token Validity Seconds:"+gp.getAccessTokenValiditySeconds());
        log.info("Refresh Token Validity Seconds:"+gp.getRefreshTokenValiditySeconds());
       String decryptedPass =cryptoConfigs.decrypt(gp.getAuthorizationServerConfigEncryptedPass(),gp.getAuthorizationServerConfigEncryptedPassAdditionalData());
        clients.inMemory().withClient(gp.getAuthorizationServerConfigEncryptedPassClientId())
                .secret(passwordEncoder.encode(decryptedPass))
                .scopes("read","write")
                .authorizedGrantTypes("password","refresh_token")
                .accessTokenValiditySeconds(Integer.parseInt(gp.getAccessTokenValiditySeconds()))
                .refreshTokenValiditySeconds(Integer.parseInt(gp.getRefreshTokenValiditySeconds()));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenAdditionalInfo,accessTokenConverter()));
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(JwtConfig.RSA_PRIVATE);
        jwtAccessTokenConverter.setVerifierKey(JwtConfig.RSA_PUBLIC);

        return jwtAccessTokenConverter;
    }
}
