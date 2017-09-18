package com.develcom.migrardatos.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Soaint210TQF
 */
public class InfoDocumento implements Serializable{

    private static final long serialVersionUID = 7231187536270839168L;

    private String rutaRaiz;
    private String tipoDocumento;
    private String subCategoria;
    private String expediente;
    private String nombreArchivo;
    private String rutaArchivo;
    private String formato;
    private int numeroDocumento;
    private int version;
    private int paginas;
    private Date fechaVencimiento;
    private int estatusDocumento;
    private String reDigitalizo;
    private DatosInfoDocumento datosInfoDocumento;

    /**
     * @return the tipoDocuemto
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocuemto to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the subCategoria
     */
    public String getSubCategoria() {
        return subCategoria;
    }

    /**
     * @param subCategoria the subCategoria to set
     */
    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }

    /**
     * @return the expediente
     */
    public String getExpediente() {
        return expediente;
    }

    /**
     * @param expediente the expediente to set
     */
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the rutaArchivo
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }

    /**
     * @param rutaArchivo the rutaArchivo to set
     */
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * @return the formato
     */
    public String getFormato() {
        return formato;
    }

    /**
     * @param formato the formato to set
     */
    public void setFormato(String formato) {
        this.formato = formato;
    }

    /**
     * @return the numeroDocumento
     */
    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the paginas
     */
    public int getPaginas() {
        return paginas;
    }

    /**
     * @param paginas the paginas to set
     */
    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the estatusDocumento
     */
    public int getEstatusDocumento() {
        return estatusDocumento;
    }

    /**
     * @param estatusDocumento the estatusDocumento to set
     */
    public void setEstatusDocumento(int estatusDocumento) {
        this.estatusDocumento = estatusDocumento;
    }

    /**
     * @return the datosInfoDocumento
     */
    public DatosInfoDocumento getDatosInfoDocumento() {
        return datosInfoDocumento;
    }

    /**
     * @param datosInfoDocumento the datosInfoDocumento to set
     */
    public void setDatosInfoDocumento(DatosInfoDocumento datosInfoDocumento) {
        this.datosInfoDocumento = datosInfoDocumento;
    }

    /**
     * @return the rutaRaiz
     */
    public String getRutaRaiz() {
        return rutaRaiz;
    }

    /**
     * @param rutaRaiz the rutaRaiz to set
     */
    public void setRutaRaiz(String rutaRaiz) {
        this.rutaRaiz = rutaRaiz;
    }

    /**
     * @return the reDigitalizo
     */
    public String getReDigitalizo() {
        return reDigitalizo;
    }

    /**
     * @param reDigitalizo the reDigitalizo to set
     */
    public void setReDigitalizo(String reDigitalizo) {
        this.reDigitalizo = reDigitalizo;
    }

}
