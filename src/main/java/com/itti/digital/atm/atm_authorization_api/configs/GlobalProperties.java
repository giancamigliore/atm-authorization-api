package com.itti.digital.atm.atm_authorization_api.configs;


import org.springframework.beans.factory.annotation.Value;
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
    @Value("${ATM_ROUTE_ACTIVATED}")
    private Boolean atmRouteActivated;
    @Value("${SHOW_HIKARI_POOL_STATUS}")
    private Boolean showHikariPoolStatus;
    @Value("${SHOW_REQUEST_FIELDS_LOGS}")
    private Boolean showRequestFieldsLogs;
    @Value("${SHOW_RESPONSE_FIELDS_LOGS}")
    private Boolean showResponseFieldsLogs;
    @Value("${ADD_MSG_ATM_RESPONSE}")
    private Boolean addMsgAtmResponse;
    @Value("${SET_BALANCE_TO_ZERO_ON_DEPOSIT}")
    private Boolean setBalanceToZeroOnDeposit;
    @Value("${ACTIVATE_WAIT}")
    private Boolean activateWait;
    @Value("${ERROR_RESPONSE}")
    private Boolean errorResponse;
    @Value("${DUMMY_MODE}")
    private Boolean dummyMode;
    @Value("${DUMMY_ATM_NUMBER}")
    private String dummyAtmNumber;
    @Value("${DUMMY_TRX_CODE}")
    private String dummyTrxCode;
    @Value("${WAIT_TIME}")
    private int waitTime;
    @Value("${BANKITTI_TED_IDS}")
    private String bankittiTedIds;
    @Value("${BANKITTI_URL}")
    private String bankittiUrl;
    @Value("${BANKITTI_USERNAME}")
    private String bankittiUsername;
    @Value("${BANKITTI_PASSWORD}")
    private String bankittiPassword;
    @Value("${CANT_INTENTOS}")
    private Integer cantIntentos;
    @Value("${atm.transaction.datasource.route}")
    private Integer atmTransactionDatasourceRoute;
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

    public Boolean getAtmRouteActivated() {
        return atmRouteActivated;
    }

    public void setAtmRouteActivated(Boolean atmRouteActivated) {
        this.atmRouteActivated = atmRouteActivated;
    }

    public Boolean getShowHikariPoolStatus() {
        return showHikariPoolStatus;
    }

    public void setShowHikariPoolStatus(Boolean showHikariPoolStatus) {
        this.showHikariPoolStatus = showHikariPoolStatus;
    }

    public Boolean getShowRequestFieldsLogs() {
        return showRequestFieldsLogs;
    }

    public void setShowRequestFieldsLogs(Boolean showRequestFieldsLogs) {
        this.showRequestFieldsLogs = showRequestFieldsLogs;
    }

    public Boolean getShowResponseFieldsLogs() {
        return showResponseFieldsLogs;
    }

    public void setShowResponseFieldsLogs(Boolean showResponseFieldsLogs) {
        this.showResponseFieldsLogs = showResponseFieldsLogs;
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

    public Boolean getActivateWait() {
        return activateWait;
    }

    public void setActivateWait(Boolean activateWait) {
        this.activateWait = activateWait;
    }

    public Boolean getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(Boolean errorResponse) {
        this.errorResponse = errorResponse;
    }

    public Boolean getDummyMode() {
        return dummyMode;
    }

    public void setDummyMode(Boolean dummyMode) {
        this.dummyMode = dummyMode;
    }

    public String getDummyAtmNumber() {
        return dummyAtmNumber;
    }

    public void setDummyAtmNumber(String dummyAtmNumber) {
        this.dummyAtmNumber = dummyAtmNumber;
    }

    public String getDummyTrxCode() {
        return dummyTrxCode;
    }

    public void setDummyTrxCode(String dummyTrxCode) {
        this.dummyTrxCode = dummyTrxCode;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public String getBankittiTedIds() {
        return bankittiTedIds;
    }

    public void setBankittiTedIds(String bankittiTedIds) {
        this.bankittiTedIds = bankittiTedIds;
    }

    public String getBankittiUrl() {
        return bankittiUrl;
    }

    public void setBankittiUrl(String bankittiUrl) {
        this.bankittiUrl = bankittiUrl;
    }

    public String getBankittiUsername() {
        return bankittiUsername;
    }

    public void setBankittiUsername(String bankittiUsername) {
        this.bankittiUsername = bankittiUsername;
    }

    public String getBankittiPassword() {
        return bankittiPassword;
    }

    public void setBankittiPassword(String bankittiPassword) {
        this.bankittiPassword = bankittiPassword;
    }

    public Integer getCantIntentos() {
        return cantIntentos;
    }

    public void setCantIntentos(Integer cantIntentos) {
        this.cantIntentos = cantIntentos;
    }

    public Integer getAtmTransactionDatasourceRoute() {
        return atmTransactionDatasourceRoute;
    }

    public void setAtmTransactionDatasourceRoute(Integer atmTransactionDatasourceRoute) {
        this.atmTransactionDatasourceRoute = atmTransactionDatasourceRoute;
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
