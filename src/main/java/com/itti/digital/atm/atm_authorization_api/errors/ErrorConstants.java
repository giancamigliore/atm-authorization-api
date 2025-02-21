package com.itti.digital.atm.atm_authorization_api.errors;

/**
 * @author Marcelo Lopez
 */

public class ErrorConstants {




    private ErrorConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String HTTP_401 = "401";
    public static final String HTTP_UNAUTHORIZED = "Unauthorized";

    public static final String HTTP_400 = "400";
    public static final String HTTP_BAD_REQUEST = "Bad Request";

    public static final String HTTP_500 = "500";
    public static final String HTTP_INTERNAL = "Internal Server Error";

    public static final String ERROR_CODE_ENCRYPTION_FAIL = "03";
    public static final String ERROR_MESSAGE_ENCRYPTION_FAIL = "ERROR EN LA ENCRIPCION";

    public static final String DB_1000 = "06";
    public static final String OCURRIO_UN_ERROR_CON_LA_BD = "Ocurrio un error con la BD";

    public static final String ERROR_CODE_RESPONSE_INVALID_FORMAT = "93";
    public static final String ERROR_MESSAGE_RESPONSE_INVALID_FORMAT = "FORMATO INCORRECTO EN RESPUESTA";

    public static final String ERROR_CODE_DATABASE_TIMEOUT = "94";
    public static final String ERROR_MESSAGE_DATABASE_TIMEOUT = "TIEMPO DE ESPERA AGOTADO PARA LA TRANSACCION";


}
