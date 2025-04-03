package com.itti.digital.atm.atm_authorization_api.utils;



import com.itti.digital.atm.atm_authorization_api.errors.ErrorConstants;
import com.itti.digital.atm.atm_authorization_api.models.authorization.RequestAuthorization;
import com.itti.digital.atm.atm_authorization_api.models.authorization.ResponseAuthorization;
import com.itti.digital.atm.atm_authorization_api.service.impl.AuthorizationServiceImpl;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class ModelUtils {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(ModelUtils.class);
    private static final long serialVersionUID = 1L;

    private static final Integer MAX_CUENTA_SIF = 100000;

    private static final String CUENTA_NULA = "0000000000";



    private Map<String, String> cuentas;



    private String convertCuentas(String traza) {
        StringBuilder sBuilder = new StringBuilder(traza);
        String cuenta1 = this.cuentas.get("cuenta1");
        String cuenta2 = this.cuentas.get("cuenta2");
        cuenta1 = String.format("%010d", Integer.parseInt(cuenta1));
        cuenta2 = String.format("%010d", Integer.parseInt(cuenta2));
        sBuilder = sBuilder.replace(0, 10, cuenta1);
        sBuilder = sBuilder.replace(13, 23, cuenta2);
        return sBuilder.toString();
    }

    private void setCuentasTraza(String traza) {
        String cuenta1 = traza.substring(0, 10);
        String cuenta2 = traza.substring(13, 23);
        this.cuentas = new HashMap<>();
        this.cuentas.put("cuenta1", cuenta1);
        this.cuentas.put("cuenta2", cuenta2);
    }

    private String cuentaCNB(String cuenta) {
        StringBuilder sBuilder = new StringBuilder(cuenta);
        Integer dv = Character.getNumericValue(sBuilder.charAt(7));
	/*	if (dv == 9) {
			cuenta = sBuilder.replace(7, 8, "0").toString();
		}*/
        return cuenta;
    }



    public static String formatMessage (String response, int longitudMensaje){
        int longitud = longitudMensaje-67;
        if (longitud > 0){
            for (int i = 0; i < longitud; i++) {
                response += " ";
            }
        }
        return response;
    }

    public static ResponseAuthorization parseResponse(String traza,Integer longitudMensajeria, RequestAuthorization request,String errorCode, String errorMessage,Boolean setZeroBalance,Boolean addMsgAtmResponse) {
        ResponseAuthorization response = new ResponseAuthorization();
        traza = ModelUtils.formatMessage(traza, longitudMensajeria);
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

        if(errorCode!=null && !errorCode.isEmpty()){
            response.setCode(errorCode);
            response.setMessage(errorMessage);
            response.setTransactionId(request.getDocum());
            response.setTransactionType(request.getTransactionType());
            response.setIsReversal(request.getIsReversal());
            return response;
        }
        String codRtrn = traza.substring(0, 2);
        if (setZeroBalance) {
            log.info("SE ENCUENTRA HABILITADO EL SETEO DE LOS SALDOS A 0 EN LA RESPUESTA DEL SOCKET PARA OPERACIONES DE DEPOSITO.");
            if (request.getCodTrx() != null && request.getCodTrx().equalsIgnoreCase("03")) {
                saldo ="000000000000000";
                saldoContable="000000000000000";

            }else{
                saldo = traza.substring(2, 17);
                saldoContable = traza.substring(18, 33);
            }
        }else{
            saldo = traza.substring(2, 17);
            saldoContable = traza.substring(18, 33);
        }


        signo = traza.substring(17, 18);
        signoSaldoContable = traza.substring(33, 34);
        nroTrxInfonet = traza.substring(34, 49);
        nroTrxEntidad = traza.substring(49, 64);
        cantPaquetesExtracto = traza.substring(64, 67);
        impresionPieTicketAtm = traza.substring(67, 547);
        impresionPantallaAtm = traza.substring(547, 869);
        impresionPantallaCostoTrx = traza.substring(869, 1277);
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

        if (addMsgAtmResponse) {
            log.info("AGREGANDO TEXTO DE IMPRESION EN RESPUESTA...");
            String doceli = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ";
            String scrdta = "                                                                                                                                                                                                                                                                                                                                  ";
            String bncdta = "   ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                              ";
            response.setImpresionPieTicketAtm(doceli);
            response.setImpresionPantallaAtm(scrdta);
            response.setImpresionPantallaCostoTrx(bncdta);
        }

        return response;
    }
}
