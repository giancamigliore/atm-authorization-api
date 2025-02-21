package com.itti.digital.atm.atm_authorization_api.models.authorization;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;


/**
 * @author Giancarlo Migliore
 */

@Schema(name = "ResponseAuthorization", description = "Respuesta de autorización de bankitti")
public class ResponseAuthorization implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "code", example = "00")
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "message", example = "aprobado")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "saldo", example = "000000000000000")
    private String saldo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "signo", example = "+")
    private String signo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "saldoContable", example = "000000000000000")
    private String saldoContable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "signoSaldoContable", example = "+")
    private String signoSaldoContable;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "nroTrxRed", example = "000504515251386")
    private String nroTrxRed;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "nroTrxEntidad", example = "000000020251386")
    private String nroTrxEntidad;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "cantPaquetesExtracto", example = "000")
    private String cantPaquetesExtracto;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impresionPieTicketAtm", example = "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ")
    private String impresionPieTicketAtm;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impresionPantallaAtm", example = "                                                                                                                                                                                                                                                                                                                                   ")
    private String impresionPantallaAtm;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impresionPantallaCostoTrx", example = "    ESTA TRANSACCION ESTA SUJETA       A CARGOS ADICIONALES POR          PARTE DEL BANCO EMISOR                                                     GS. 7.000                                                                                                                                                                                                                                                            ")
    private String impresionPantallaCostoTrx;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "transactionType", example = "03")
    private String transactionType;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "isReversal", example = "false")
    private Boolean isReversal;

    @JsonInclude(JsonInclude.Include.NON_NULL)//docum
    @Schema(description = "transactionId", example = "06455555522994")
    private String transactionId;

    public ResponseAuthorization(String code, String message, String saldo, String signo, String saldoContable, String signoSaldoContable, String nroTrxRed, String nroTrxEntidad, String cantPaquetesExtracto, String impresionPieTicketAtm, String impresionPantallaAtm, String impresionPantallaCostoTrx, String transactionType, Boolean isReversal, String transactionId) {
        this.code = code;
        this.message = message;
        this.saldo = saldo;
        this.signo = signo;
        this.saldoContable = saldoContable;
        this.signoSaldoContable = signoSaldoContable;
        this.nroTrxRed = nroTrxRed;
        this.nroTrxEntidad = nroTrxEntidad;
        this.cantPaquetesExtracto = cantPaquetesExtracto;
        this.impresionPieTicketAtm = impresionPieTicketAtm;
        this.impresionPantallaAtm = impresionPantallaAtm;
        this.impresionPantallaCostoTrx = impresionPantallaCostoTrx;
        this.transactionType = transactionType;
        this.isReversal = isReversal;
        this.transactionId = transactionId;
    }

    public ResponseAuthorization() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public String getSaldoContable() {
        return saldoContable;
    }

    public void setSaldoContable(String saldoContable) {
        this.saldoContable = saldoContable;
    }

    public String getSignoSaldoContable() {
        return signoSaldoContable;
    }

    public void setSignoSaldoContable(String signoSaldoContable) {
        this.signoSaldoContable = signoSaldoContable;
    }

    public String getNroTrxRed() {
        return nroTrxRed;
    }

    public void setNroTrxRed(String nroTrxRed) {
        this.nroTrxRed = nroTrxRed;
    }

    public String getNroTrxEntidad() {
        return nroTrxEntidad;
    }

    public void setNroTrxEntidad(String nroTrxEntidad) {
        this.nroTrxEntidad = nroTrxEntidad;
    }

    public String getCantPaquetesExtracto() {
        return cantPaquetesExtracto;
    }

    public void setCantPaquetesExtracto(String cantPaquetesExtracto) {
        this.cantPaquetesExtracto = cantPaquetesExtracto;
    }

    public String getImpresionPieTicketAtm() {
        return impresionPieTicketAtm;
    }

    public void setImpresionPieTicketAtm(String impresionPieTicketAtm) {
        this.impresionPieTicketAtm = impresionPieTicketAtm;
    }

    public String getImpresionPantallaAtm() {
        return impresionPantallaAtm;
    }

    public void setImpresionPantallaAtm(String impresionPantallaAtm) {
        this.impresionPantallaAtm = impresionPantallaAtm;
    }

    public String getImpresionPantallaCostoTrx() {
        return impresionPantallaCostoTrx;
    }

    public void setImpresionPantallaCostoTrx(String impresionPantallaCostoTrx) {
        this.impresionPantallaCostoTrx = impresionPantallaCostoTrx;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Boolean getIsReversal() {
        return isReversal;
    }

    public void setIsReversal(Boolean reversal) {
        isReversal = reversal;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return  code  + saldo  + signo + saldoContable  + signoSaldoContable + nroTrxRed +  nroTrxEntidad + cantPaquetesExtracto + impresionPieTicketAtm + impresionPantallaAtm + impresionPantallaCostoTrx;
    }
}
