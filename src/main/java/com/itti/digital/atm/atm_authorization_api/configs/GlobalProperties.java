package com.itti.digital.atm.atm_authorization_api.configs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Bean que obtiene las propiedades del proyecto  *
 *  @author Giancarlo Migliore
 *  @version 1.00 2022/02/20
 *  @since JDK11
 */
@Component

public class GlobalProperties {


    @Value("${PUERTO}")
    private String puerto;
    @Value("${CALL_PROCEDURE}")
    private String procedure;
    @Value("${TIMEOUT_PROCESO}")
    private int timeOutProceso;
    @Value("${LONGITUD_MENSAJERIA}")
    private int longitudMensajeria;
    @Value("${SHOW_HIKARI_POOL_STATUS}")
    private Boolean showHikariPoolStatus;
    @Value("${ADD_MSG_ATM_RESPONSE}")
    private Boolean addMsgAtmResponse;
    @Value("${SET_BALANCE_TO_ZERO_ON_DEPOSIT}")
    private Boolean setBalanceToZeroOnDeposit;
    @Value("${accessToken.validityMiliSeconds}")
    private String accessTokenValidityMiliSeconds;
    @Value("${refreshToken.validityMiliSeconds}")
    private String refreshTokenValidityMiliSeconds;
    @Value("${authorizationServerConfig.encryptedPass}")
    private String authorizationServerConfigEncryptedPass;
    @Value("${authorizationServerConfig.encryptedPass.additionalData}")
    private String authorizationServerConfigEncryptedPassAdditionalData;
    @Value("${authorizationServerConfig.encryptedPass.clientId}")
    private String authorizationServerConfigEncryptedPassClientId;
    @Value("${clientForceCrypto}")
    private String clientForceCrypto;
    @Value("${clientAdditionalData}")
    private String clientAdditionalData;
    @Value("${security.basic.enable}")
    private Boolean securityEnabled;
    @Value("${server.port}")
    private String serverPort;
    @Value("${connTimeOut}")
    private int connTimeout;
    @Value("${readTimeOut}")
    private int readTimeOut;


    public String getAccessTokenValidityMiliSeconds() {
        return accessTokenValidityMiliSeconds;
    }

    public void setAccessTokenValidityMiliSeconds(String accessTokenValidityMiliSeconds) {
        this.accessTokenValidityMiliSeconds = accessTokenValidityMiliSeconds;
    }

    public String getRefreshTokenValidityMiliSeconds() {
        return refreshTokenValidityMiliSeconds;
    }

    public void setRefreshTokenValidityMiliSeconds(String refreshTokenValidityMiliSeconds) {
        this.refreshTokenValidityMiliSeconds = refreshTokenValidityMiliSeconds;
    }

    public String getAuthorizationServerConfigEncryptedPass() {
        return authorizationServerConfigEncryptedPass;
    }

    public void setAuthorizationServerConfigEncryptedPass(String authorizationServerConfigEncryptedPass) {
        this.authorizationServerConfigEncryptedPass = authorizationServerConfigEncryptedPass;
    }

    public String getAuthorizationServerConfigEncryptedPassAdditionalData() {
        return authorizationServerConfigEncryptedPassAdditionalData;
    }

    public void setAuthorizationServerConfigEncryptedPassAdditionalData(String authorizationServerConfigEncryptedPassAdditionalData) {
        this.authorizationServerConfigEncryptedPassAdditionalData = authorizationServerConfigEncryptedPassAdditionalData;
    }

    public String getAuthorizationServerConfigEncryptedPassClientId() {
        return authorizationServerConfigEncryptedPassClientId;
    }

    public void setAuthorizationServerConfigEncryptedPassClientId(String authorizationServerConfigEncryptedPassClientId) {
        this.authorizationServerConfigEncryptedPassClientId = authorizationServerConfigEncryptedPassClientId;
    }

    public String getClientForceCrypto() {
        return clientForceCrypto;
    }

    public void setClientForceCrypto(String clientForceCrypto) {
        this.clientForceCrypto = clientForceCrypto;
    }

    public String getClientAdditionalData() {
        return clientAdditionalData;
    }

    public void setClientAdditionalData(String clientAdditionalData) {
        this.clientAdditionalData = clientAdditionalData;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public int getTimeOutProceso() {
        return timeOutProceso;
    }

    public void setTimeOutProceso(int timeOutProceso) {
        this.timeOutProceso = timeOutProceso;
    }

    public int getLongitudMensajeria() {
        return longitudMensajeria;
    }

    public void setLongitudMensajeria(int longitudMensajeria) {
        this.longitudMensajeria = longitudMensajeria;
    }

    public Boolean getShowHikariPoolStatus() {
        return showHikariPoolStatus;
    }

    public void setShowHikariPoolStatus(Boolean showHikariPoolStatus) {
        this.showHikariPoolStatus = showHikariPoolStatus;
    }

    public Boolean getAddMsgAtmResponse() {
        return addMsgAtmResponse;
    }

    public void setAddMsgAtmResponse(Boolean addMsgAtmResponse) {
        this.addMsgAtmResponse = addMsgAtmResponse;
    }

    public Boolean getSetBalanceToZeroOnDeposit() {
        return setBalanceToZeroOnDeposit;
    }

    public void setSetBalanceToZeroOnDeposit(Boolean setBalanceToZeroOnDeposit) {
        this.setBalanceToZeroOnDeposit = setBalanceToZeroOnDeposit;
    }

    public Boolean getSecurityEnabled() {
        return securityEnabled;
    }

    public void setSecurityEnabled(Boolean securityEnabled) {
        this.securityEnabled = securityEnabled;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public int getConnTimeout() {
        return connTimeout;
    }
    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

}
