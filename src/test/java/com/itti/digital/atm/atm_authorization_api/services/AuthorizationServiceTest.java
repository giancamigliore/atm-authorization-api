package com.itti.digital.atm.atm_authorization_api.services;


import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import com.itti.digital.atm.atm_authorization_api.dao.AtmAuthorizationRepository;
import com.itti.digital.atm.atm_authorization_api.dao.DataBaseOracle;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsTypeException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import com.itti.digital.atm.atm_authorization_api.models.authorization.RequestAuthorization;
import com.itti.digital.atm.atm_authorization_api.models.authorization.ResponseAuthorization;
import com.itti.digital.atm.atm_authorization_api.service.AuthorizationService;
import com.itti.digital.atm.atm_authorization_api.service.impl.AuthorizationServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuthorizationServiceTest {

    AtmAuthorizationRepository repository = mock(AtmAuthorizationRepository.class);
    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    GlobalProperties gp = mock(GlobalProperties.class);
    DataSource datasource = mock(DataSource.class);
    Connection connection = mock(Connection.class);
    CallableStatement cs = mock(CallableStatement.class);
    DataBaseOracle dataBaseOracle = mock(DataBaseOracle.class);
    AuthorizationService service = new AuthorizationServiceImpl(gp, jdbcTemplate, repository,dataBaseOracle);


    @AfterEach
    public void cleanup() {
        Mockito.clearAllCaches();
    }

    @Test
    void processAtmTransaction_validDataPresented_transactionAproved()throws Exception{
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        ResponseAuthorization responseAuthorization = new ResponseAuthorization();
        responseAuthorization.setCode("00");
        responseAuthorization.setMessage("transacción procesada");
        responseAuthorization.setTransactionId("000504515251386");
        responseAuthorization.setIsReversal(false);
        responseAuthorization.setTransactionType("FCOCashDepositWU");
        responseAuthorization.setSaldo("000000000000000");
        responseAuthorization.setSigno("+");
        responseAuthorization.setSaldoContable("000000000000000");
        responseAuthorization.setSignoSaldoContable("+");
        responseAuthorization.setNroTrxRed("000504515251386");
        responseAuthorization.setNroTrxEntidad("000000020251386");
        responseAuthorization.setCantPaquetesExtracto("000");
        responseAuthorization.setImpresionPieTicketAtm("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");
        responseAuthorization.setImpresionPantallaAtm("                                                                                                                                                                                                                                                                                                                                   ");
        responseAuthorization.setImpresionPantallaCostoTrx("    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ");

        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");


        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(responseAuthorization.toString());
        //WHEN
        ResponseAuthorization  response = service.processAtmTransaction(authorization);
        //THEN
        assertThat(response).extracting("code").isEqualTo(responseAuthorization.getCode());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));

    }

    @Test
    void processAtmTransaction_validDataPresented_transactionNotAproved()throws Exception{
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        ResponseAuthorization responseAuthorization = new ResponseAuthorization();
        responseAuthorization.setCode("14");
        responseAuthorization.setMessage("transacción no procesada");
        responseAuthorization.setTransactionId("000000000000000");
        responseAuthorization.setIsReversal(false);
        responseAuthorization.setTransactionType("FCOCashDepositWU");
        responseAuthorization.setSaldo("000000000000000");
        responseAuthorization.setSigno("+");
        responseAuthorization.setSaldoContable("000000000000000");
        responseAuthorization.setSignoSaldoContable("+");
        responseAuthorization.setNroTrxRed("000000000000000");
        responseAuthorization.setNroTrxEntidad("000000000000000");
        responseAuthorization.setCantPaquetesExtracto("000");
        responseAuthorization.setImpresionPieTicketAtm("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");
        responseAuthorization.setImpresionPantallaAtm("                                                                                                                                                                                                                                                                                                                                   ");
        responseAuthorization.setImpresionPantallaCostoTrx("    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ");

        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");


        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(responseAuthorization.toString());
        //WHEN
        ResponseAuthorization  response = service.processAtmTransaction(authorization);
        //THEN
        assertThat(response).extracting("code").isEqualTo(responseAuthorization.getCode());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    void processAtmTransaction_validDataPresented_throwsSQLException()throws Exception{
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        ResponseAuthorization responseAuthorization = new ResponseAuthorization();
        responseAuthorization.setCode("00");
        responseAuthorization.setMessage("transacción procesada");
        responseAuthorization.setTransactionId("000504515251386");
        responseAuthorization.setIsReversal(false);
        responseAuthorization.setTransactionType("FCOCashDepositWU");
        responseAuthorization.setSaldo("000000000000000");
        responseAuthorization.setSigno("+");
        responseAuthorization.setSaldoContable("000000000000000");
        responseAuthorization.setSignoSaldoContable("+");
        responseAuthorization.setNroTrxRed("000504515251386");
        responseAuthorization.setNroTrxEntidad("000000020251386");
        responseAuthorization.setCantPaquetesExtracto("000");
        responseAuthorization.setImpresionPieTicketAtm("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");
        responseAuthorization.setImpresionPantallaAtm("                                                                                                                                                                                                                                                                                                                                   ");
        responseAuthorization.setImpresionPantallaCostoTrx("    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ");

        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");

        SQLException sqlException = mock(SQLException.class);


        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenThrow(sqlException);
        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.processAtmTransaction(authorization);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000, ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));

    }

    @Test
    void processAtmTransaction_validDataPresented_throwsException()throws Exception{
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        ResponseAuthorization responseAuthorization = new ResponseAuthorization();
        responseAuthorization.setCode("00");
        responseAuthorization.setMessage("transacción procesada");
        responseAuthorization.setTransactionId("000504515251386");
        responseAuthorization.setIsReversal(false);
        responseAuthorization.setTransactionType("FCOCashDepositWU");
        responseAuthorization.setSaldo("000000000000000");
        responseAuthorization.setSigno("+");
        responseAuthorization.setSaldoContable("000000000000000");
        responseAuthorization.setSignoSaldoContable("+");
        responseAuthorization.setNroTrxRed("000504515251386");
        responseAuthorization.setNroTrxEntidad("000000020251386");
        responseAuthorization.setCantPaquetesExtracto("000");
        responseAuthorization.setImpresionPieTicketAtm("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");
        responseAuthorization.setImpresionPantallaAtm("                                                                                                                                                                                                                                                                                                                                   ");
        responseAuthorization.setImpresionPantallaCostoTrx("    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ");

        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");

        RuntimeException exception = mock(RuntimeException.class);


        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenThrow(exception);
        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.processAtmTransaction(authorization);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000, ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    void processAtmTransaction_validDataPresented_throwsExceptionOnConnClose()throws Exception{
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        ResponseAuthorization responseAuthorization = new ResponseAuthorization();
        responseAuthorization.setCode("00");
        responseAuthorization.setMessage("transacción procesada");
        responseAuthorization.setTransactionId("000504515251386");
        responseAuthorization.setIsReversal(false);
        responseAuthorization.setTransactionType("FCOCashDepositWU");
        responseAuthorization.setSaldo("000000000000000");
        responseAuthorization.setSigno("+");
        responseAuthorization.setSaldoContable("000000000000000");
        responseAuthorization.setSignoSaldoContable("+");
        responseAuthorization.setNroTrxRed("000504515251386");
        responseAuthorization.setNroTrxEntidad("000000020251386");
        responseAuthorization.setCantPaquetesExtracto("000");
        responseAuthorization.setImpresionPieTicketAtm("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ");
        responseAuthorization.setImpresionPantallaAtm("                                                                                                                                                                                                                                                                                                                                   ");
        responseAuthorization.setImpresionPantallaCostoTrx("    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ");

        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).close();
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");

        RuntimeException exception = mock(RuntimeException.class);


        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(responseAuthorization.toString());
        //WHEN

        ThrowableAssert.ThrowingCallable throwingCallable = () ->service.processAtmTransaction(authorization);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000, ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    public void processAtmTransaction_Extorno99() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("99");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "7600000106965000+000000106965000+0005093162530270000000202530280000";
        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        // Act
        ResponseAuthorization response = service.processAtmTransaction(authorization );

        // Assert
        assertEquals("00", response.getCode());
        assertEquals("Transacción procesada", response.getMessage());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    public void processAtmTransaction_ResponseWithErrorCodeFromCore() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "61000000106965000+000000106965000+000509316253027000000020253028000";

        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");

        // Act
        ResponseAuthorization response = service.processAtmTransaction(authorization);

        // Assert
        assertEquals("61", response.getCode());
        assertEquals("SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE", response.getMessage());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    public void processAtmTransaction_InvalidResponseFormat() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "FORMATO INCORRECTO EN RESPUESTA";
        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");
        // Act
        ResponseAuthorization response = service.processAtmTransaction(authorization);

        // Assert
        assertEquals(ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT, response.getCode());
        assertEquals(ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT, response.getMessage());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }

    @Test
    public void processAtmTransaction_SQLExceptionORA01013() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        when(dataBaseOracle.procesar(anyString(),any(Connection.class),any(GlobalProperties.class))).thenThrow(new SQLException("ORA-01013: no se puede conectar a la base de datos"));
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        when(jdbcTemplate.getDataSource()).thenReturn(datasource);
        when(jdbcTemplate.getDataSource().getConnection()).thenReturn(connection);
        when(gp.getProcedure()).thenReturn("fuj_pro_tra_cajeros");
        when(gp.getClientAdditionalData()).thenReturn("ATM001");
        when(gp.getClientForceCrypto()).thenReturn("false");


        ResponseAuthorization response = service.processAtmTransaction(authorization);

        // Assert
        assertEquals(ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT, response.getCode());
        assertEquals(ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT, response.getMessage());
        verify(dataBaseOracle).procesar(anyString(),any(Connection.class),any(GlobalProperties.class));
    }


    @Test
    public void testProcessAtmTransactionV2_Success() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "00000000106965000+000000106965000+000509316253027000000020253028000";
        when(repository.processAtmTransaction(authorization.toString())).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);

        // Act
        ResponseAuthorization response = service.processAtmTransactionV2(authorization);

        // Assert
        assertEquals("00", response.getCode());
        assertEquals("Transacción procesada", response.getMessage());
        verify(repository).processAtmTransaction(anyString());
    }

    @Test
    public void testProcessAtmTransactionV2_ResponseWithErrorCodeFromCore() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "61000000106965000+000000106965000+000509316253027000000020253028000";
        when(repository.processAtmTransaction(authorization.toString())).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);

        // Act
        ResponseAuthorization response = service.processAtmTransactionV2(authorization);

        // Assert
        assertEquals("61", response.getCode());
        assertEquals("SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE", response.getMessage());
        verify(repository).processAtmTransaction(anyString());
    }

    @Test
    public void testProcessAtmTransactionV2_InvalidResponseFormat() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "FORMATO INCORRECTO EN RESPUESTA";
        when(repository.processAtmTransaction(authorization.toString())).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        // Act
        ResponseAuthorization response = service.processAtmTransactionV2(authorization);

        // Assert
        assertEquals(ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT, response.getCode());
        assertEquals(ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT, response.getMessage());
        verify(repository).processAtmTransaction(anyString());
    }

    @Test
    public void testProcessAtmTransactionV2_SQLException() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        when(repository.processAtmTransaction(authorization.toString())).thenThrow(SQLException.class);
        when(gp.getLongitudMensajeria()).thenReturn(1277);

        //WHEN
        ThrowableAssert.ThrowingCallable throwingCallable = () -> service.processAtmTransactionV2(authorization);
        //THEN
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(AlfaMsException.class)
                .extracting("type", "errorCode", "errorMessage")
                .containsExactly(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000, ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        verify(repository).processAtmTransaction(anyString());
    }

    @Test
    public void testProcessAtmTransactionV2_SQLExceptionORA01013() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("00");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        when(repository.processAtmTransaction(authorization.toString())).thenThrow(new SQLException("ORA-01013: no se puede conectar a la base de datos"));
        when(gp.getLongitudMensajeria()).thenReturn(1277);


        ResponseAuthorization response = service.processAtmTransactionV2(authorization);

        // Assert
        assertEquals(ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT, response.getCode());
        assertEquals(ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT, response.getMessage());
        verify(repository).processAtmTransaction(anyString());
    }

    @Test
    public void testProcessAtmTransactionV2_Extorno99() throws Exception {
        // Arrange
        RequestAuthorization authorization = new RequestAuthorization();
        authorization.setCta1("0096912217");
        authorization.setBanco1("107");
        authorization.setCta2("0000000000");
        authorization.setBanco2("000");
        authorization.setCodTrx("03");
        authorization.setExtorno("99");
        authorization.setLocal("000000020251386");
        authorization.setDocum("000504515251386");
        authorization.setImpor("000000002000000");
        authorization.setImpob("000000000000000");
        authorization.setImpoC("000000000000000");
        authorization.setCanHq("0000");
        authorization.setMoneda("00");
        authorization.setLunar("001");
        authorization.setCaja("01");
        authorization.setNumtrcj("1386");
        authorization.setHora14("14D");
        authorization.setNumneg("CI45632172                              ");
        authorization.setLocale("000000000000000");
        authorization.setAtmbco("107");
        authorization.setDbfec("14022025");
        authorization.setDbhor("154814");
        authorization.setTransactionId("000504515251386");
        authorization.setTransactionType("FCOCashDepositWU");
        authorization.setIsReversal(false);

        String respuesta = "7600000106965000+000000106965000+0005093162530270000000202530280000";
        when(repository.processAtmTransaction(authorization.toString())).thenReturn(respuesta);
        when(gp.getLongitudMensajeria()).thenReturn(1277);
        // Act
        ResponseAuthorization response = service.processAtmTransactionV2(authorization );

        // Assert
        assertEquals("00", response.getCode());
        assertEquals("Transacción procesada", response.getMessage());
        verify(repository).processAtmTransaction(anyString());
    }


}
