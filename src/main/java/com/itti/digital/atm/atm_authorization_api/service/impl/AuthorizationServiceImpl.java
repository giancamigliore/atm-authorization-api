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
import com.itti.digital.atm.atm_authorization_api.utils.ModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@org.springframework.stereotype.Service
@Profile({"hml","prd"})
public class AuthorizationServiceImpl implements AuthorizationService {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    GlobalProperties gp;

    private JdbcTemplate jdbcTemplate;

    private DataBaseOracle dataBaseOracle;

    private AtmAuthorizationRepository repository;

    @Autowired
    public AuthorizationServiceImpl(
                                    GlobalProperties gp,
                                    @Qualifier("oracleJdbcTemplate")JdbcTemplate jdbcTemplate,
                                    AtmAuthorizationRepository repository,
                                    DataBaseOracle dataBaseOracle) {

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
                        response= ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_RESPONSE_INVALID_FORMAT,ErrorConstants.ERROR_MESSAGE_RESPONSE_INVALID_FORMAT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;

                        log.info("ASIGNANDO CODIGO DE ERROR GENERICO PARA RESPUESTA AL TED....");
                        return response;
                    }
                    if (request.getExtorno().equalsIgnoreCase("99") && codRtrn!=null && !codRtrn.isEmpty() && codRtrn.equalsIgnoreCase("76")) {
                        respuesta = "00" + respuesta.substring(2);
                        codRtrn="00";
                        log.info("RESPONSE RESVERSA PARA TED: " + respuesta);
                    }

                    if (codRtrn != null && !codRtrn.isEmpty() && (codRtrn.equalsIgnoreCase("00") || codRtrn.equalsIgnoreCase("99"))) {
                        response= ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,null,null,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse());
                    }else{
                        return ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,codRtrn,"SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE",gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
                    }
                }
            } catch (Exception e) {
                log.error("Error: Problemas al procesar transacción. Exception: " + e.getMessage());
                if(errorCode!=null && !errorCode.equalsIgnoreCase("") && errorCode.contains("ORA-01013")) {
                    return  ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT,ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;

                }
                throw new AlfaMsException(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000,ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
            }

        log.info("RESPONSE:" +  gson.toJson(response));
        log.info("************* FINALIZA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        return response;

    }

    @Override
    public ResponseAuthorization processAtmTransactionV2(RequestAuthorization request) throws AlfaMsException {
        Thread.currentThread().setName(Calendar.getInstance().getTimeInMillis() + "");
        log.info("************* INICIA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        ResponseAuthorization response = new ResponseAuthorization();
        Gson gson = new Gson();
        String errorCode=null;
        log.info("REQUEST:" + gson.toJson(request));
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
                    respuesta = "00" + respuesta.substring(2);
                    codRtrn ="00";
                    log.info("RESPONSE RESVERSA PARA TED: " + respuesta);
                }

                if (codRtrn != null && !codRtrn.isEmpty() && (codRtrn.equalsIgnoreCase("00") || codRtrn.equalsIgnoreCase("99"))) {
                    response= ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,null,null,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse());
                }else{
                    return ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,codRtrn,"SE HA OBTENIDO UN CODIGO DE RECHAZO DEL CORE",gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
                }
            }
        } catch (Exception e) {
            log.error("Error: Problemas al procesar transacción. Exception: " + e.getMessage());
            if(errorCode!=null && !errorCode.equalsIgnoreCase("") && errorCode.contains("ORA-01013")) {
                return  ModelUtils.parseResponse(respuesta,gp.getLongitudMensajeria(),request,ErrorConstants.ERROR_CODE_DATABASE_TIMEOUT,ErrorConstants.ERROR_MESSAGE_DATABASE_TIMEOUT,gp.getSetBalanceToZeroOnDeposit(),gp.getAddMsgAtmResponse()) ;
            }
            throw new AlfaMsException(AlfaMsTypeException.APPLICATION, ErrorConstants.DB_1000,ErrorConstants.OCURRIO_UN_ERROR_CON_LA_BD);
        }

        log.info("RESPONSE:" +  gson.toJson(response));

        log.info("************* FINALIZA PROCESAMIENTO DE TRANSACCION CONTRA BANKITTI ***************");
        return response;
    }

}
