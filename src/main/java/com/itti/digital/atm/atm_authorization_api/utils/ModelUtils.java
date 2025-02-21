package com.itti.digital.atm.atm_authorization_api.utils;



import java.util.HashMap;
import java.util.Map;


public class ModelUtils {


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



    public static String formatErrorMessage (String response, int longitudMensaje){
        int longitud = longitudMensaje-67;
        if (longitud > 0){
            for (int i = 0; i < longitud; i++) {
                response += " ";
            }
        }
        return response;
    }
}
