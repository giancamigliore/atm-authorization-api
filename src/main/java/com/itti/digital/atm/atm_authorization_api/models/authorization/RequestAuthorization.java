package com.itti.digital.atm.atm_authorization_api.models.authorization;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;



/**
 * @author Giancarlo Migliore
 */


@Schema(name = "RequestAuthorization", description = "Request para solicitud de autorización contra bankitti")
public class RequestAuthorization {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "cta1", example = "0096912217")
    private String cta1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "banco1", example = "107")
    private String banco1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "cta2", example = "0000000000")
    private String cta2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "banco2", example = "000")
    private String banco2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "codTrx", example = "03")
    private String codTrx;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "extorno", example = "00")
    private String extorno;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "local", example = "000000020251386")
    private String local;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "docum", example = "000504515251386")
    private String docum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impor", example = "000000002000000")
    private String impor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impob", example = "000000000000000")
    private String impob;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "impoC", example = "000000000000000")
    private String impoC;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "canHq", example = "0000")
    private String canHq;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "moneda", example = "00")
    private String moneda;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "lunar", example = "005")
    private String lunar;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "caja", example = "01")
    private String caja;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "numtrcj", example = "1386")
    private String numtrcj;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "hora14", example = "14D")
    private String hora14;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "numneg", example = "CI45632172                              ")
    private String numneg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "locale", example = "000000000000000")
    private String locale;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "atmbco", example = "107")
    private String atmbco;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "dbfec", example = "14022025")
    private String dbfec;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "dbhor", example = "154814")
    private String dbhor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "transactionType", example = "03")
    private String transactionType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "isReversal", example = "false")
    private Boolean isReversal;

    @JsonInclude(JsonInclude.Include.NON_NULL)//docum
    @Schema(description = "transactionId", example = "000504515251386")
    private String transactionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)//docum
    @Schema(description = "gf1", example = "generic field 1")
    private String gf1;

    @JsonInclude(JsonInclude.Include.NON_NULL)//docum
    @Schema(description = "gf2", example = "generic field 2")
    private String gf2;

    @JsonInclude(JsonInclude.Include.NON_NULL)//docum
    @Schema(description = "gf3", example = "generic field 3")
    private String gf3;

    public RequestAuthorization(String cta1, String banco1, String cta2, String banco2, String codTrx, String extorno, String local, String docum, String impor, String impob, String impoC, String canHq, String moneda, String lunar, String caja, String numtrcj, String hora14, String numneg, String locale, String atmbco, String dbfec, String dbhor, String transactionType, Boolean isReversal, String transactionId, String gf1, String gf2, String gf3) {
        this.cta1 = cta1;
        this.banco1 = banco1;
        this.cta2 = cta2;
        this.banco2 = banco2;
        this.codTrx = codTrx;
        this.extorno = extorno;
        this.local = local;
        this.docum = docum;
        this.impor = impor;
        this.impob = impob;
        this.impoC = impoC;
        this.canHq = canHq;
        this.moneda = moneda;
        this.lunar = lunar;
        this.caja = caja;
        this.numtrcj = numtrcj;
        this.hora14 = hora14;
        this.numneg = numneg;
        this.locale = locale;
        this.atmbco = atmbco;
        this.dbfec = dbfec;
        this.dbhor = dbhor;
        this.transactionType = transactionType;
        this.isReversal = isReversal;
        this.transactionId = transactionId;
        this.gf1 = gf1;
        this.gf2 = gf2;
        this.gf3 = gf3;
    }

    public Boolean getReversal() {
        return isReversal;
    }

    public void setReversal(Boolean reversal) {
        isReversal = reversal;
    }

    public String getGf1() {
        return gf1;
    }

    public void setGf1(String gf1) {
        this.gf1 = gf1;
    }

    public String getGf2() {
        return gf2;
    }

    public void setGf2(String gf2) {
        this.gf2 = gf2;
    }

    public String getGf3() {
        return gf3;
    }

    public void setGf3(String gf3) {
        this.gf3 = gf3;
    }

    public RequestAuthorization() {
    }

    public String getCta1() {
        return cta1;
    }

    public void setCta1(String cta1) {
        this.cta1 = cta1;
    }

    public String getBanco1() {
        return banco1;
    }

    public void setBanco1(String banco1) {
        this.banco1 = banco1;
    }

    public String getCta2() {
        return cta2;
    }

    public void setCta2(String cta2) {
        this.cta2 = cta2;
    }

    public String getBanco2() {
        return banco2;
    }

    public void setBanco2(String banco2) {
        this.banco2 = banco2;
    }

    public String getCodTrx() {
        return codTrx;
    }

    public void setCodTrx(String codTrx) {
        this.codTrx = codTrx;
    }

    public String getExtorno() {
        return extorno;
    }

    public void setExtorno(String extorno) {
        this.extorno = extorno;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDocum() {
        return docum;
    }

    public void setDocum(String docum) {
        this.docum = docum;
    }

    public String getImpor() {
        return impor;
    }

    public void setImpor(String impor) {
        this.impor = impor;
    }

    public String getImpob() {
        return impob;
    }

    public void setImpob(String impob) {
        this.impob = impob;
    }

    public String getImpoC() {
        return impoC;
    }

    public void setImpoC(String impoC) {
        this.impoC = impoC;
    }

    public String getCanHq() {
        return canHq;
    }

    public void setCanHq(String canHq) {
        this.canHq = canHq;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getNumtrcj() {
        return numtrcj;
    }

    public void setNumtrcj(String numtrcj) {
        this.numtrcj = numtrcj;
    }

    public String getHora14() {
        return hora14;
    }

    public void setHora14(String hora14) {
        this.hora14 = hora14;
    }

    public String getNumneg() {
        return numneg;
    }

    public void setNumneg(String numneg) {
        this.numneg = numneg;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAtmbco() {
        return atmbco;
    }

    public void setAtmbco(String atmbco) {
        this.atmbco = atmbco;
    }

    public String getDbfec() {
        return dbfec;
    }

    public void setDbfec(String dbfec) {
        this.dbfec = dbfec;
    }

    public String getDbhor() {
        return dbhor;
    }

    public void setDbhor(String dbhor) {
        this.dbhor = dbhor;
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
        return cta1 + banco1 + cta2 + banco2 + codTrx + extorno + local + docum + impor + impob + impoC + canHq + moneda + lunar + caja + numtrcj + hora14 + numneg + locale + atmbco + dbfec + dbhor;
    }
}
