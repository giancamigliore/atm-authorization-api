package com.itti.digital.atm.atm_authorization_api.models.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class OauthRequest {

    @NotBlank
    @Schema(description = "Usuario de la app", example = "k3app")
    private String username;

    @NotBlank
    @Schema(description = "Password encriptado de la app", example = "fdsfdsfdsnbfbvkjasdfbdasjvnbaslfhsBHFDAOIADVBADFF4B56FDAGDSFHJLDShSFHDS==")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
