package com.itti.digital.atm.atm_authorization_api.errors;


public enum AlfaErrorType {
  ERROR_ACCOUNT_HAVE_CARD("4051", "La cuenta ya tiene asociada una tarjeta."),
  ERROR_SECURITY_DECRYPT_ENCRYPT_DATA("4052", "No se pudo encriptar/desencriptar datos sensibles."),
  ERROR_SECURITY_CARD_NUMBER_NOT_VALID("4054", "Formato de numero de tarjeta no valido."),
  ERROR_SECURITY_PRIVATE_PUBLIC_KEY_NOT_VALID("4055", "Key invalido."),
  DB_ERROR_CALL_FUNC_PROC("4053", "Error al ejecutar procedimiento externo."),
  DB_ERROR_EXEC_QUERY("4063", "Error al ejecutar/obtener datos de la consulta."),

  GP_ERROR_ACCOUNT_NUMBER_INVALID("4056", "Numero de cuenta invalido."),
  GP_ERROR_UPDATE_STATE("4057", "Estado no valido para el cambio."),
  GP_ERROR_ACCOUNT_NUMBER_ALREADY_EXIST(
      "4058", "El numero de cuenta ya se encuentra registrado en GP."),
  GP_ERROR_DOCUMENT_PRODUCT_ALREADY_EXIST(
      "4059", "Ya existe cliente y producto con esos datos asociados."),
  GP_ERROR_500("4060", "Error interno del Servidor GP."),
  GP_ERROR_CARD_NUMBER_NOT_EXIST("4064", "El numero de tarjeta no existe en GP."),
  GP_ERROR_ENVELOP_NOT_EXIST("4066", "El numero de sobre no existe en GP."),

  ERROR_RESPONSE_SERIALIZE("4061", "Error en deserializacion de respuesta de Error."),
  ERROR_GLOBAL_CODE_NOT_FOUND("4062", "El codigo de Error de GP no reconocido."),
  ERROR_CHANGE_STATE("4065", "No se pudo realizar el cambio de estado."),
  ERROR_RESPONSE_DESERIALIZE("4066", "Error en deserializacion de respuesta de GP."),
  GP_ERROR_SERVICE_NOT_AVAILABLE("4067", "Servicio No Disponible GP."),
  ERROR_CURRENCY_EXCHANGE_BID_PRICE_HIGHER("4068", "El precio de compra de la cotizacion es mayor al precio de venta"),
  ERROR_DATE_HIGHER("4069", "La fecha de la cotizacion es mayor a la fecha actual"),
  ERROR_DATE_LOWER("4070", "La fecha de la cotizacion es menor a la fecha actual"),
  ERROR_DATE_FORMAT("4071", "Error al procesar formato de fecha"),
  GP_WRONG_PIN("4072","El pin ingresado es incorrecto"),
  ERROR_CARD_TYPE_REPRINT("4073", "Tipo de tarjeta no valido para reimpresion"),
  ERROR_PRODUCT_CODE_NOT_FOUND("4074", "No se encontro codigo de producto"),
  IDL_SERVICE_NOT_AVAILABLE("4075", "Servicio de IDL para envio de SMS no disponible"),
  ERROR_PHONE_NUMBER_FORMAT_INVALID("4076", "El formato de numero de telefono es invalido"),
  ERROR_OTP_NOT_SENT("4077", "No se pudo enviar OTP al destino"),
  ERROR_OTP_SERVICE_IDL_DESERIALIZE("4078", "Error al deserializar respuesta del servicio de IDL"),
  ERROR_ACCOUNT_NOT_CARDS("4079", "La cuenta no posee tarjetas de credito en estado Normal"),
  RECK_AWS_SERVICE_ERROR("E01", "Error al consultar servicio AWS Rekognition"),
  RECK_IMAGE_NOT_VALID_ERROR("E02", "Imagen no valida. "),
  RECK_INVALID_FIELD_ERROR("E03", "No se pudo extraer campo"),
  RECK_INVALID_DOCUMENT_BACK("E04", "El dorso de la CI no corresponde con el frente especifado en el parametro por documentNumber."),
  UNUM_BIOMETRIC_SERVICE_NOT_AVAILABLE("4075", "Servicio de Biometria no disponible"),
  UNUM_PERSON_FIND_SERVICE_NOT_AVAILABLE("4076", "Servicio de IDL  de personas no disponible"),
  DB_CONNECTION_ERROR("C000", "No se pudo crear la conexión a la base de datos."),
  GP_ERROR_RANGO_EDAD("4080", "La persona no posee la edad necesaria."),
  FACTURY_RUC_FIND_SERVICE_NOT_AVAILABLE("4081", "Servicio de IDL de registros unicos de contribuyentes (RUC) no disponible");


  private String alfaCode;
  private String description;

  AlfaErrorType(String alfaCode, String description) {
    this.alfaCode = alfaCode;
    this.description = description;
  }

  public String getAlfaCode() {
    return alfaCode;
  }

  public String getDescription() {
    return description;
  }
}
