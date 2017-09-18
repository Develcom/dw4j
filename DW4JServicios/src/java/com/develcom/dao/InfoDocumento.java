/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author develcom
 */
public class InfoDocumento  implements Serializable{
    private static final long serialVersionUID = -247361190927599766L;


    private int idInfoDocumento;
    private int idDocumento;
    private String nombreArchivo;
    private String rutaArchivo;
    private int cantPaginas;
    private int version;
    private String formato;
    private String idExpediente;
    private int numeroDocumento;
    private Date fechaVencimiento;
    private String datoAdicional;
    private Date fechaDigitalizacion;
    private String estatusDocumento;
    private int estatus;
    private String usuarioDigitalizo;
    private Date fechaAprobacion;
    private String usuarioAprobacion;
    private Date fechaRechazo;
    private String usuarioRechazo;
    private String motivoRechazo;
    private String causaRechazo;
    private String tipoDocumento;
    private boolean tipoDocDatoAdicional;
    private boolean reDigitalizo;
    private int folio;
    private boolean nuevo;
    private GregorianCalendar fechaActual;
    private List<DatoAdicional> lsDatosAdicionales;

    public int getIdInfoDocumento() {
        return idInfoDocumento;
    }

    public void setIdInfoDocumento(int idInfoDocumento) {
        this.idInfoDocumento = idInfoDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public int getCantPaginas() {
        return cantPaginas;
    }

    public void setCantPaginas(int cantPaginas) {
        this.cantPaginas = cantPaginas;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(String idExpediente) {
        this.idExpediente = idExpediente;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDatoAdicional() {
        return datoAdicional;
    }

    public void setDatoAdicional(String datoAdicional) {
        this.datoAdicional = datoAdicional;
    }

    public Date getFechaDigitalizacion() {
        return fechaDigitalizacion;
    }

    public void setFechaDigitalizacion(Date fechaDigitalizacion) {
        this.fechaDigitalizacion = fechaDigitalizacion;
    }

    public String getEstatusDocumento() {
        return estatusDocumento;
    }

    public void setEstatusDocumento(String estatusDocumento) {
        this.estatusDocumento = estatusDocumento;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getUsuarioDigitalizo() {
        return usuarioDigitalizo;
    }

    public void setUsuarioDigitalizo(String usuarioDigitalizo) {
        this.usuarioDigitalizo = usuarioDigitalizo;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getUsuarioAprobacion() {
        return usuarioAprobacion;
    }

    public void setUsuarioAprobacion(String usuarioAprobacion) {
        this.usuarioAprobacion = usuarioAprobacion;
    }

    public Date getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public String getUsuarioRechazo() {
        return usuarioRechazo;
    }

    public void setUsuarioRechazo(String usuarioRechazo) {
        this.usuarioRechazo = usuarioRechazo;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getCausaRechazo() {
        return causaRechazo;
    }

    public void setCausaRechazo(String causaRechazo) {
        this.causaRechazo = causaRechazo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public boolean isTipoDocDatoAdicional() {
        return tipoDocDatoAdicional;
    }

    public void setTipoDocDatoAdicional(boolean tipoDocDatoAdicional) {
        this.tipoDocDatoAdicional = tipoDocDatoAdicional;
    }

    public boolean isReDigitalizo() {
        return reDigitalizo;
    }

    public void setReDigitalizo(boolean reDigitalizo) {
        this.reDigitalizo = reDigitalizo;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public GregorianCalendar getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(GregorianCalendar fechaActual) {
        this.fechaActual = fechaActual;
    }

    public List<DatoAdicional> getLsDatosAdicionales() {
        return lsDatosAdicionales;
    }

    public void setLsDatosAdicionales(List<DatoAdicional> lsDatosAdicionales) {
        this.lsDatosAdicionales = lsDatosAdicionales;
    }
}
