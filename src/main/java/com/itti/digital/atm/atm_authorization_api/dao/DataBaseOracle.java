package com.itti.digital.atm.atm_authorization_api.dao;


import com.itti.digital.atm.atm_authorization_api.configs.GlobalProperties;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.sql.*;

/** Manejador las transacciones con la Base de Datos ORACLE  *
 *  @author Giancarlo Migliore
 *  @version 1.00 2022/02/25
 *  @since JDK23
 */

@Component
public class DataBaseOracle {
    final static Logger log = org.slf4j.LoggerFactory.getLogger(DataBaseOracle.class);
    /** Método que realiza la call al autorizador. Esta versión Utiliza JdbcTemplate y SimpleJdbcCall.
     * La anotación @Transactional permite que el DataSourceTransactionManager haga el seguimiento de las operaciones.
     * */
    @Transactional
    public String procesar(String pedido, JdbcTemplate jdbcTemplate, GlobalProperties gp) throws Exception {

        // contenedor del resultado
        log.info("LLAMANDO AL PROCEDURE:" + gp.getProcedure());
        String resultado = "";
        Clob dato;
        String monto = pedido.substring(60, 73);
        String local = pedido.substring(30, 45);
        String docum = pedido.substring(45, 60);
        String codTrx = pedido.substring(26, 28);
        String moneda = pedido.substring(109, 111);
        CallableStatement callableStatement = null;
        if (Double.parseDouble(monto) > 25000000 && pedido.substring(26, 28).equalsIgnoreCase("05")) {
            resultado = "13000000000000000+000000000000000+" + local + docum + "000";
            return resultado;
        }
        try {

            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withFunctionName(gp.getProcedure())
                    .withoutProcedureColumnMetaDataAccess()
                    .useInParameterNames("p_pedido")
                    .declareParameters(
                            new SqlOutParameter("result", Types.CLOB),
                            new SqlParameter("p_pedido", Types.VARCHAR)
                    );

            SqlParameterSource parameterMap = new MapSqlParameterSource("p_pedido", pedido);

            dato = simpleJdbcCall.executeFunction(Clob.class, parameterMap);

            int iLength = (int) (dato.length());
            resultado = dato.getSubString(1, iLength);

            // convierto el resultado a UTF-8
            resultado = new String(resultado.getBytes("UTF-8"), "UTF-8");

        } catch (Exception e) {
            log.error("Error: error al procesar trx: " + e);
            throw e;
        }
        return resultado;
    }

    /** Método que realiza la call al autorizador. Esta versión Utiliza CallableStatement y Connection.
     * La anotación @Transactional permite que el DataSourceTransactionManager haga el seguimiento de las operaciones.
     * */
    @Transactional
    public String procesar(String pedido, Connection conn, GlobalProperties gp) throws SQLException, UnsupportedEncodingException {
        String psentencia = "{? = call " + gp.getProcedure() + "('" + pedido + "')}";
        // contenedor del resultado
        log.info("LLAMANDO AL PROCEDURE:" + gp.getProcedure());
        String resultado = new String();
        Clob dato;
        String monto = pedido.substring(60, 73);
        String local = pedido.substring(30, 45);
        String docum = pedido.substring(45, 60);
        String codTrx = pedido.substring(26, 28);
        String moneda = pedido.substring(109, 111);

        if (Double.parseDouble(monto) > 25000000 && pedido.substring(26, 28).equalsIgnoreCase("05")) {
            resultado = "13000000000000000+000000000000000+" + local + docum + "000";
            return resultado;
        }
        // preparo la sentencia
        CallableStatement cs = conn.prepareCall(psentencia,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        //System.out.println("registro la variable como clob");
        cs.registerOutParameter(1, Types.CLOB);
        // ejecuto la funcion
        //System.out.println("ejecuto la sentencia: " + psentencia);
        cs.execute();
        // dato de retorno de la funcion

        //System.out.println("leo el valor leido");
        dato = cs.getClob(1);
        int iLength = (int) (dato.length());
        resultado = dato.getSubString(1, iLength);
        // convierto el resultado a UTF-8
        resultado = new String(resultado.getBytes("UTF-8"), "UTF-8");

        cs.close();
        return (resultado);


    }

    private String respError = "06000000000000000+000000000000000+000000000000000000000000000000000";



}
