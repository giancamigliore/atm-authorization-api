package com.itti.digital.atm.atm_authorization_api.service.dummies;


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
import com.itti.digital.atm.atm_authorization_api.utils.ModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

@org.springframework.stereotype.Service
@Profile("hmldummy")
public class AuthorizationServiceDummyImpl implements AuthorizationService {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(AuthorizationServiceDummyImpl.class);

    GlobalProperties gp;

    private JdbcTemplate jdbcTemplate;

    private DataBaseOracle dataBaseOracle;

    private AtmAuthorizationRepository repository;

    int intentosSleep = 0;


    private Boolean activateWait;

    private Boolean errorResponse;

    private Boolean dummyMode;

    private String dummyAtmNumber;

    private String dummyTrxCode;

    private int waitTime;

    private Integer cantIntentos;


    private Boolean activateWaitBeforeTransaction;




    @Autowired
    public AuthorizationServiceDummyImpl(GlobalProperties gp,
                                         @Qualifier("oracleJdbcTemplate")JdbcTemplate jdbcTemplate,
                                         AtmAuthorizationRepository repository,
                                         DataBaseOracle dataBaseOracle,
                                         @Value("${ACTIVATE_WAIT}") Boolean activateWait,
                                         @Value("${ERROR_RESPONSE}") Boolean errorResponse,
                                         @Value("${DUMMY_MODE}") Boolean dummyMode,
                                         @Value("${DUMMY_ATM_NUMBER}") String dummyAtmNumber,
                                         @Value("${DUMMY_TRX_CODE}") String dummyTrxCode,
                                         @Value("${WAIT_TIME}") int waitTime,
                                         @Value("${CANT_INTENTOS}") Integer cantIntentos,
                                         @Value("${ACTIVATE_WAIT_BEFORE_TRANSACTION}") Boolean activateWaitBeforeTransaction) {
        this.gp = gp;
        this.jdbcTemplate = jdbcTemplate;
        this.repository = repository;
        this.dataBaseOracle=dataBaseOracle;
        this.activateWait=activateWait;
        this.dummyAtmNumber=dummyAtmNumber;
        this.dummyMode=dummyMode;
        this.dummyTrxCode=dummyTrxCode;
        this.errorResponse=errorResponse;
        this.waitTime=waitTime;
        this.cantIntentos=cantIntentos;
        this.activateWaitBeforeTransaction=activateWaitBeforeTransaction;

    }

    @Override
    public ResponseAuthorization processAtmTransaction(RequestAuthorization request) throws AlfaMsException {
        Thread.currentThread().setName(Calendar.getInstance().getTimeInMillis() + "");
        log.info("************* INICIA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        log.info("############DUMMY MODE###########");
        ResponseAuthorization response = new ResponseAuthorization();
        Gson gson = new Gson();
        String errorCode=null;

        log.info("REQUEST:" + gson.toJson(request));
        if (activateWaitBeforeTransaction != null && activateWaitBeforeTransaction && intentosSleep < cantIntentos) {
            log.info("INICIANDO WAIT PREVIO A LA TRANSACCION " + waitTime + " MS");
            //Thread.currentThread().stop();//MATA EL KTH
            intentosSleep++;
            try {
                Thread.sleep(waitTime);
            }catch(Exception e){
                log.error("error durmiendo el thread: "+e.getMessage());
            }
            log.info("FINALIZANDO WAIT PREVIO A LA TRANSACCION" + waitTime + " MS");
            return null;
        }

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

                if (dummyAtmNumber.contains(request.getLunar())) {

                    if (dummyMode && dummyTrxCode!= null && !dummyTrxCode.isEmpty() && dummyTrxCode.contains(request.getCodTrx())) {
                        log.info("--------------------------DUMMY MODE--------------------------");
                        log.info("DUMMY TRX CODE: " + request.getCodTrx());
                        respuesta = "00115000771070000000000000630000000002023659200031851723659200000001100000000000000000000000000000000000000000000101659204DATM" + request.getLunar() + "                                PY00000000000000010704072023171008";
                        log.info("DUMMY MODE RESPONSE : " + respuesta);
                        log.info("--------------------------DUMMY MODE--------------------------");
                    }
                }

                if(respuesta!=null && !respuesta.isEmpty()) {
                    String codRtrn = respuesta.substring(0, 2);
                    log.info("código retorno: " + codRtrn);
                    if (!StringUtils.isNumeric(codRtrn)) {
                        log.info("RESPUESTA CORE: " + respuesta);
                        log.error("------LA RESPUESTA DEL CORE BANKITTI NO TIENE EL FORMATO CORRECTO------");
                        response= ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT,ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;

                        log.info("ASIGNANDO CODIGO DE ERROR GENERICO PARA RESPUESTA AL TED....");
                        return response;
                    }
                    if (request.getExtorno().equalsIgnoreCase("99") && codRtrn!=null && !codRtrn.isEmpty() && codRtrn.equalsIgnoreCase("76")) {
                        respuesta ="00" + respuesta.substring(2);

                        log.info("RESPONSE RESVERSA PARA TED: " + respuesta);

                    }

                    if (codRtrn != null && !codRtrn.isEmpty() && (codRtrn.equalsIgnoreCase("00") || codRtrn.equalsIgnoreCase("99"))) {
                        if (errorResponse) {
                            codRtrn = "61";
                            respuesta = codRtrn + respuesta.substring(2);
                            response = ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(), request, codRtrn, "SE SETEA CODIGO DE ERROR DUMMY", gp.getSetBalanceToZeroOnDeposit(), gp.getAddMsgAtmResponse());
                        }else {
                            response = ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(), request, null, null, gp.getSetBalanceToZeroOnDeposit(), gp.getAddMsgAtmResponse());
                        }


                        if (activateWait != null && activateWait && intentosSleep < cantIntentos) {
                            log.info("INICIANDO WAIT " + waitTime + " MS");
                            //Thread.currentThread().stop();//MATA EL KTH
                            intentosSleep++;
                            Thread.sleep(waitTime);

                            log.info("FINALIZANDO WAIT " + waitTime + " MS");
                        }

                    }else{
                        return ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,codRtrn,"SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE",gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
                    }
                }
            } catch (Exception e) {
                log.error("Error: Problemas con la conexión. Exception: " + e.getMessage());
                if(errorCode!=null && !errorCode.equalsIgnoreCase("") && errorCode.contains("ORA-01013")) {
                    return  ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT,ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;

                }
                throw new AlfaMsException(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000,ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
            }

        log.info("RESPONSE:" +  gson.toJson(response));
        log.info("############DUMMY MODE###########");
        log.info("************* FINALIZA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        return response;

    }

    @Override
    public ResponseAuthorization processAtmTransactionV2(RequestAuthorization request) throws AlfaMsException {
        Thread.currentThread().setName(Calendar.getInstance().getTimeInMillis() + "");
        log.info("************* INICIA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        log.info("############DUMMY MODE###########");
        ResponseAuthorization response = new ResponseAuthorization();
        Gson gson = new Gson();
        String errorCode=null;
        log.info("REQUEST:" + gson.toJson(request));
        if (activateWaitBeforeTransaction != null && activateWaitBeforeTransaction && intentosSleep < cantIntentos) {
            log.info("INICIANDO WAIT PREVIO A LA TRANSACCION " + waitTime + " MS");
            //Thread.currentThread().stop();//MATA EL KTH
            intentosSleep++;
            try {
                Thread.sleep(waitTime);
            }catch(Exception e){
                log.error("error durmiendo el thread: "+e.getMessage());
            }
            log.info("FINALIZANDO WAIT PREVIO A LA TRANSACCION" + waitTime + " MS");
            return null;
        }
        String respuesta=null;

        try  {

            try {
                respuesta = repository.processAtmTransaction(request.toString());

            } catch (SQLException e) {

                log.error("ERROR AL PROCESAR TRANSACCION: "+e.getMessage());
                if(e.getMessage()!=null && e.getMessage().contains("ORA")) {
                    errorCode = e.getMessage().split(":")[0];
                }
                throw new Exception(e.getMessage());
            }finally {

                    log.info("Desconectando de la bd...");


            }
            if (dummyAtmNumber.contains(request.getLunar())) {

                if (dummyMode && dummyTrxCode != null && !dummyTrxCode.isEmpty() && dummyTrxCode.contains(request.getCodTrx())) {
                    log.info("--------------------------DUMMY MODE--------------------------");
                    log.info("DUMMY TRX CODE: " + request.getCodTrx());
                    respuesta = "00000000106965000+000000106965000+000509316253027000000020253028000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                     ";
                    log.info("DUMMY MODE RESPONSE : " + respuesta);
                    log.info("--------------------------DUMMY MODE--------------------------");
                }
            }
            if(respuesta!=null && !respuesta.isEmpty()) {
                String codRtrn = respuesta.substring(0, 2);
                log.info("código retorno: " + codRtrn);
                if (!StringUtils.isNumeric(codRtrn)) {
                    log.info("RESPUESTA CORE: " + respuesta);
                    log.error("------LA RESPUESTA DEL CORE BANKITTI NO TIENE EL FORMATO CORRECTO------");
                    response= ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT,ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
                    log.info("ASIGNANDO CODIGO DE ERROR GENERICO PARA RESPUESTA AL TED....");
                    return response;
                }

                if (request.getExtorno().equalsIgnoreCase("99") && codRtrn!=null && !codRtrn.isEmpty() && codRtrn.equalsIgnoreCase("76")) {
                    respuesta ="00" + respuesta.substring(2);

                    log.info("RESPONSE RESVERSA PARA TED: " + respuesta);

                }

                if (codRtrn != null && !codRtrn.isEmpty() && (codRtrn.equalsIgnoreCase("00") || codRtrn.equalsIgnoreCase("99"))) {


                    if (errorResponse) {
                        codRtrn = "61";
                        respuesta = codRtrn + respuesta.substring(2);
                        response = ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(), request, codRtrn, "SE SETEA CODIGO DE ERROR DUMMY", gp.getSetBalanceToZeroOnDeposit(), gp.getAddMsgAtmResponse());
                    }else {
                        response = ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(), request, null, null, gp.getSetBalanceToZeroOnDeposit(), gp.getAddMsgAtmResponse());
                    }

                    if (activateWait != null && activateWait && intentosSleep < cantIntentos) {
                        log.info("INICIANDO WAIT " + cantIntentos + " MS");
                        //Thread.currentThread().stop();//MATA EL KTH
                        intentosSleep++;
                        Thread.sleep(cantIntentos);

                        log.info("FINALIZANDO WAIT " + cantIntentos + " MS");
                    }
                }else{
                    return ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,codRtrn,"SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE",gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
                }
            }
        } catch (Exception e) {
            log.error("Error obteniendo respuesta del core. Exception: " + e.getMessage());
            if(errorCode!=null && !errorCode.equalsIgnoreCase("") && errorCode.contains("ORA-01013")) {
                return  ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT,ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
            }
            throw new AlfaMsException(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000,ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        }

        log.info("RESPONSE:" +  gson.toJson(response));
        log.info("############DUMMY MODE###########");
        log.info("************* FINALIZA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        return response;
    }

}
