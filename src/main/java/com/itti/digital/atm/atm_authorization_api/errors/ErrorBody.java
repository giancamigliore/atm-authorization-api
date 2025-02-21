package com.itti.digital.atm.atm_authorization_api.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.Objects;

/**
 * @author Marcelo Lopez
 */


@Schema(name = "ErrorBody", description = "Respuesta en caso de error")
public class ErrorBody {

    @Schema(description = "Mensaje de error", example = "Ocurrio un error con la BD")
    private String message;

    @Schema(description = "Mensaje útil para el desarrollador", example = "Could not find a property named 'Customer_id' on Object 'getaccountsview'.")
    private String debugMessage;

    @Schema(description = "Código de error", example = "DB1000")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @Schema(description = "Tipo de excepción", example = "APPLICATION")
    private AlfaMsTypeException type;

    public ErrorBody(String message, String debugMessage, String code, AlfaMsTypeException type) {
        this.message = message;
        this.debugMessage = debugMessage;
        this.code = code;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AlfaMsTypeException getType() {
        return type;
    }

    public void setType(AlfaMsTypeException type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorBody errorBody = (ErrorBody) o;
        return Objects.equals(message, errorBody.message) && Objects.equals(debugMessage, errorBody.debugMessage) && Objects.equals(code, errorBody.code) && type == errorBody.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, debugMessage, code, type);
    }

    public ErrorBody() {
    }
}
