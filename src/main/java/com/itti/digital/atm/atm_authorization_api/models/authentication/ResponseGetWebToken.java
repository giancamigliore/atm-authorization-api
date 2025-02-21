package com.itti.digital.atm.atm_authorization_api.models.authentication;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Giancarlo Migliore
 */


@Schema(name = "ResponseCheckDocumentOrientation", description = "Respuesta de verificacion de orientacion de documento")
public class ResponseGetWebToken implements Serializable {

    @Schema(description = "Token de sesion", example = "fdsdkjashfjkdshfkjsdahfdsbfndsgfldhsuiofhdisufbdssnbfdsnmberwoiighdsfd5sf4d6s54f65sdfsdfsdsd")
    private String access_token;
    @Schema(description = "Tipo de token", example = "Bearer")
    private String token_type;
    @Schema(description = "Token de sesion", example = "fdsdkjashfjkdshfkjsdahfdsbfndsgfldhsuiofhdisufbdssnbfdsnmberwoiighdsfd5sf4d6s54f65sdfsdfsdsd")
    private String refresh_token;
    @Schema(description = "Tiempo de expiración", example = "3599")
    private int expires_in;
    @Schema(description = "Timestamp de expiración", example = "3599")
    private String expires_at;
    @Schema(description = "Alcance", example = "read write")
    private String scope;
    @Schema(description = "Información adicional", example = "Hola Ted")
    private String additional_info;
    @Schema(description = "Nombre", example = "ted")
    private String name;
    @Schema(description = "JTI", example = "cd62fa77-466e-4ff2-82d4-2ea905c24baf")
    private String jti;

    public ResponseGetWebToken(String access_token, String token_type, String refresh_token, int expires_in, String expires_at, String scope, String additional_info, String name, String jti) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.expires_at = expires_at;
        this.scope = scope;
        this.additional_info = additional_info;
        this.name = name;
        this.jti = jti;
    }

    public ResponseGetWebToken() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }
}
