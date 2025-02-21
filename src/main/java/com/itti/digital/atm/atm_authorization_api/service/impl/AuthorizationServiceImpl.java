package com.itti.digital.atm.atm_authorization_api.service.impl;


import com.google.gson.Gson;
import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import com.itti.digital.atm.atm_authorization_api.dao.AtmAuthorizationRepository;
import com.itti.digital.atm.atm_authorization_api.dao.DataBaseOracle;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsTypeException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import com.itti.digital.atm.atm_authorization_api.models.authorization.RequestAuthorization;
import com.itti.digital.atm.atm_authorization_api.models.authorization.ResponseAuthorization;
import com.itti.digital.atm.atm_authorization_api.service.AuthorizationService;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Service
public class AuthorizationServiceImpl implements AuthorizationService {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private RestTemplate restTemplate;

    GlobalProperties gp;

    private JdbcTemplate jdbcTemplate;

    private DataBaseOracle dataBaseOracle;

    private AtmAuthorizationRepository repository;

    int intentosSleep = 0;




    @Autowired
    public AuthorizationServiceImpl(@Qualifier("restTemplate")RestTemplate restTemplate,
                                    GlobalProperties gp,
                                    @Qualifier("oracleJdbcTemplate")JdbcTemplate jdbcTemplate,
                                    AtmAuthorizationRepository repository,
                                    DataBaseOracle dataBaseOracle) {
        this.restTemplate = restTemplate;
        this.gp = gp;
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
        this.dataBaseOracle=dataBaseOracle;

    }

    @Override
    public ResponseAuthorization processAtmTransaction(RequestAuthorization request) throws AlfaMsException {
        Thread.currentThread().setName(Calendar.getInstance().getTimeInMillis() + "");
        log.info("************* INICIA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        ResponseAuthorization response = new ResponseAuthorization();
        Gson gson = new Gson();
        String errorCode=null;
        String saldo = null;
        String signo = null;
        String saldoContable = null;
        String signoSaldoContable = null;
        String nroTrxInfonet = null;
        String nroTrxEntidad = null;
        String cantPaquetesExtracto = null;
        String impresionPieTicketAtm = null;
        String impresionPantallaAtm = null;
        String impresionPantallaCostoTrx = null;
        log.info("REQUEST:" + gson.toJson(request));


        String respuesta=null;

            try (Connection conn = jdbcTemplate.getDataSource().getConnection();) {
                conn.setAutoCommit(false);
                try {
                    respuesta = dataBaseOracle.procesar(request.toString(), conn, gp);
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    log.error("ERROR AL PROCESAR TRANSACCION: "+e.getMessage());
                    if(e.getMessage()!=null && e.getMessage().contains("ORA")) {
                        errorCode = e.getMessage().split(":")[0];
                    }
                    throw new Exception(e.getMessage());
                }finally {
                    try{
                        log.info("Desconectando de la bd...");
                        conn.close();
                    }catch(SQLException e){
                        log.error("ERROR AL DESCONECTAR DE LA BD: "+e.getMessage());
                        throw new Exception(e.getMessage());
                    }
                }

                if(respuesta!=null && !respuesta.isEmpty()) {
                    String codRtrn = respuesta.substring(0, 2);
                    log.info("código retorno: " + codRtrn);
                    if (!StringUtils.isNumeric(codRtrn)) {
                        log.info("RESPUESTA CORE: " + respuesta);
                        log.error("------LA RESPUESTA DEL CORE BANKITTI NO TIENE EL FORMATO CORRECTO------");
                        response.setCode(ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT);
                        response.setMessage(ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT);
                        response.setTransactionId(request.getDocum());
                        response.setTransactionType(request.getTransactionType());
                        response.setIsReversal(request.getIsReversal());
                        log.info("ASIGNANDO CODIGO DE ERROR GENERICO PARA RESPUESTA AL TED....");
                        return response;
                    }


                    if (codRtrn != null && !codRtrn.isEmpty() && (codRtrn.equalsIgnoreCase("00") || codRtrn.equalsIgnoreCase("99"))) {
                        if (gp.getSetBalanceToZeroOnDeposit()) {
                            log.info("SE ENCUENTRA HABILITADO EL SETEO DE LOS SALDOS A 0 EN LA RESPUESTA DEL SOCKET PARA OPERACIONES DE DEPOSITO.");
                            if (request.getCodTrx() != null && request.getCodTrx().equalsIgnoreCase("03")) {
                                saldo ="000000000000000";
                                saldoContable="000000000000000";

                            }else{
                                saldo = respuesta.substring(2, 17);
                                saldoContable = respuesta.substring(18, 33);
                            }
                        }else{
                            saldo = respuesta.substring(2, 17);
                            saldoContable = respuesta.substring(18, 33);
                        }


                        signo = respuesta.substring(17, 18);
                        signoSaldoContable = respuesta.substring(33, 34);
                        nroTrxInfonet = respuesta.substring(34, 49);
                        nroTrxEntidad = respuesta.substring(49, 64);
                        cantPaquetesExtracto = respuesta.substring(64, 67);
                        impresionPieTicketAtm = respuesta.substring(67, 547);
                        impresionPantallaAtm = respuesta.substring(547, 869);
                        impresionPantallaCostoTrx = respuesta.substring(869, 1277);

                        response.setCode(codRtrn);
                        response.setTransactionId(nroTrxInfonet);
                        response.setMessage("Transacción procesada");
                        response.setTransactionType(request.getTransactionType());
                        response.setIsReversal(request.getIsReversal());
                        response.setSaldo(saldo);
                        response.setSigno(signo);
                        response.setSaldoContable(saldoContable);
                        response.setSignoSaldoContable(signoSaldoContable);
                        response.setNroTrxRed(nroTrxInfonet);
                        response.setNroTrxEntidad(nroTrxEntidad);
                        response.setCantPaquetesExtracto(cantPaquetesExtracto);
                        response.setImpresionPieTicketAtm(impresionPieTicketAtm);
                        response.setImpresionPantallaAtm(impresionPantallaAtm);
                        response.setImpresionPantallaCostoTrx(impresionPantallaCostoTrx);

                        if (gp.getAddMsgAtmResponse()) {
                            log.info("AGREGANDO TEXTO DE IMPRESION EN RESPUESTA...");
                            String doceli = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
                            String scrdta = "                                                                                                                                                                                                                                                                                                                                  ";
                            String bncdta = "   ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                              ";
                            response.setImpresionPieTicketAtm(doceli);
                            response.setImpresionPantallaAtm(scrdta);
                            response.setImpresionPantallaCostoTrx(bncdta);
                        }
                        if (gp.getActivateWait() != null && gp.getActivateWait() && intentosSleep < gp.getCantIntentos()) {
                            log.info("INICIANDO WAIT " + gp.getWaitTime() + " MS");
                            //Thread.currentThread().stop();//MATA EL KTH
                            intentosSleep++;
                            Thread.sleep(gp.getWaitTime());

                            log.info("FINALIZANDO WAIT " + gp.getWaitTime() + " MS");
                        }
                    }else{
                        response.setMessage("SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE");
                        response.setCode(codRtrn);
                        response.setTransactionId(request.getDocum());
                        response.setTransactionType(request.getTransactionType());
                        response.setIsReversal(request.getIsReversal());
                        return response;
                    }
                }
            } catch (Exception e) {
                log.error("Error: Problemas con la conexión. Exception: " + e.getMessage());
                if(errorCode!=null && !errorCode.equalsIgnoreCase("") && errorCode.contains("ORA-01013")) {
                    response.setCode(ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT);
                    response.setMessage(ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT);
                    response.setTransactionId(request.getDocum());
                    response.setTransactionType(request.getTransactionType());
                    response.setIsReversal(request.getIsReversal());
                    return response;
                }
                throw new AlfaMsException(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000,ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
            }

        log.info("RESPONSE:" +  gson.toJson(response));

        log.info("************* FINALIZA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        return response;

    }

}
