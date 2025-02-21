package com.itti.digital.atm.atm_authorization_api.errors;


import java.util.Objects;


public class AlfaMsException extends Exception {

    private AlfaMsTypeException type;
    private String errorCode;
    private String errorMessage;

    public AlfaMsException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public AlfaMsException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }


    public AlfaMsException(String errorCode, String errorMessage, Exception e) {
        super(errorMessage,e);
        this.errorMessage = errorMessage;
    }


    public AlfaMsException(String errorCode, Exception e) {
        super(errorCode,e);
    }

    public AlfaMsException(AlfaMsTypeException type, String errorCode, String errorMessage) {
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AlfaMsException(String message, AlfaMsTypeException type, String errorCode, String errorMessage) {
        super(message);
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AlfaMsException(String message, Throwable cause, AlfaMsTypeException type, String errorCode, String errorMessage) {
        super(message, cause);
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AlfaMsException(Throwable cause, AlfaMsTypeException type, String errorCode, String errorMessage) {
        super(cause);
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AlfaMsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, AlfaMsTypeException type, String errorCode, String errorMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AlfaMsException() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlfaMsException that = (AlfaMsException) o;
        return type == that.type && Objects.equals(errorCode, that.errorCode) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, errorCode, errorMessage);
    }

    public AlfaMsTypeException getType() {
        return type;
    }

    public void setType(AlfaMsTypeException type) {
        this.type = type;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
