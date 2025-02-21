package com.itti.digital.atm.atm_authorization_api.models.authentication;

import io.swagger.v3.oas.annotations.media.Schema;


import javax.validation.constraints.NotBlank;

/**
 * @author Giancarlo Migliore
 */


@Schema(name = "RequestGetWebToken", description = "Request para obtener JWT")
public class RequestGetWebToken {

    @NotBlank
    @Schema(description = "Usuario de la app", example = "k3app")
    private String appUsername;

    @NotBlank
    @Schema(description = "Password encriptado de la app", example = "fdsfdsfdsnbfbvkjasdfbdasjvnbaslfhsBHFDAOIADVBADFF4B56FDAGDSFHJLDShSFHDS==")
    private String appPassword;

    @NotBlank
    @Schema(description = "Usuario del usuario generico del ted", example = "k3app")
    private String tedUsername;

    @NotBlank
    @Schema(description = "Password encriptado del usuario generico del ted", example = "fdsfdsfdsnbfbvkjasdfbdasjvnbaslfhsBHFDAOIADVBADFF4B56FDAGDSFHJLDShSFHDS==")
    private String tedPassword;

    public RequestGetWebToken(String appUsername, String appPassword, String tedUsername, String tedPassword) {
        this.appUsername = appUsername;
        this.appPassword = appPassword;
        this.tedUsername = tedUsername;
        this.tedPassword = tedPassword;
    }

    public RequestGetWebToken() {
    }

    public String getAppUsername() {
        return appUsername;
    }

    public void setAppUsername(String appUsername) {
        this.appUsername = appUsername;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getTedUsername() {
        return tedUsername;
    }

    public void setTedUsername(String tedUsername) {
        this.tedUsername = tedUsername;
    }

    public String getTedPassword() {
        return tedPassword;
    }

    public void setTedPassword(String tedPassword) {
        this.tedPassword = tedPassword;
    }
}
