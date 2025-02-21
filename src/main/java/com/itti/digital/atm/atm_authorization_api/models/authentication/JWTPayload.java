package com.itti.digital.atm.atm_authorization_api.models.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class JWTPayload {

    @NotBlank
    @Schema(description = "sub", example = "k3app")
    private String sub;

    @NotBlank
    @Schema(description = "authorities", example = "[{\\\"authority\\\":\\\"ROLE_ADMIN\\\"},{\\\"authority\\\":\\\"ROLE_USER\\\"}]")
    private String authorities;

    @NotBlank
    @Schema(description = "Usuario de la app", example = "k3app")
    private String username;

    @NotBlank
    @Schema(description = "isAdmin", example = "true")
    private Boolean isAdmin;

    @NotBlank
    @Schema(description = "Issued at time", example = "1739542757")
    private Long iat;

    @NotBlank
    @Schema(description = "Expires_in", example = "1739546357")
    private Long exp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Long getIat() {
        return iat;
    }

    public void setIat(Long iat) {
        this.iat = iat;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
