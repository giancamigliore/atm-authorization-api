package com.itti.digital.atm.atm_authorization_api.dao;

import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsException;
import com.itti.digital.atm.atm_authorization_api.errors.AlfaMsTypeException;
import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class AtmAuthorizationRepository extends JdbcDaoSupport {

    final static Logger logger = org.slf4j.LoggerFactory.getLogger(AtmAuthorizationRepository.class);
    GlobalProperties gp;
    JdbcTemplate jdbcTemplate;

    public AtmAuthorizationRepository(GlobalProperties gp, @Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate) {
        setDataSource(jdbcTemplate.getDataSource());
        this.gp = gp;
        this.jdbcTemplate = jdbcTemplate;
    }


    public String processAtmTransaction(String request) throws Exception {
        String respuesta = null;
        logger.info("createWorkFlow - SE LLAMARA AL SP: " + gp.getProcedure());
        logger.info("REQUEST ENVIADO A "+gp.getProcedure()+": " + request);
        List<SqlParameter> parameters = new ArrayList<SqlParameter>();

        parameters.add(new SqlOutParameter("p_msj_respuesta", Types.CLOB));

        Map<String, Object> result = getJdbcTemplate().call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                CallableStatement cs = con.prepareCall("{? = call " + gp.getProcedure() + "('" + request + "')}");

                cs.registerOutParameter(1, Types.CLOB);
                return cs;
            }
        }, parameters);


        if (result.get("p_msj_respuesta") != null) {
            Clob dato;
            dato = (Clob) result.get("p_msj_respuesta");
            int iLength = (int) (dato.length());
            respuesta = dato.getSubString(1, iLength);
            // convierto el resultado a UTF-8
            respuesta = new String(respuesta.getBytes("UTF-8"), "UTF-8");
            logger.info("RESPUESTA OBTENIDA DE "+gp.getProcedure()+": " + respuesta);
        }

        return respuesta;
    }
}



