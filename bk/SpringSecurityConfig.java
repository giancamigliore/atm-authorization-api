package com.itti.digital.atm.atm_authorization_api.configs.springsecurity;


import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SpringSecurityConfig  {
    private final UserDetailsService userService;
    private GlobalProperties gp;

    public SpringSecurityConfig(UserDetailsService userService, GlobalProperties gp) {
        this.userService = userService;
        this.gp = gp;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean("authenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf)->csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authz)->authz
                .anyRequest().authenticated()
                );

        return http.build();

    }



    @Bean
    public WebSecurityCustomizer configure() throws Exception {

        if (!gp.getSecurityEnabled()){
            return (web) -> web.ignoring().requestMatchers("/**");

        }
        return null;
    }
}
