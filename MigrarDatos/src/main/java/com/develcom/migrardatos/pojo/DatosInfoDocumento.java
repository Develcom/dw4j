/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Soaint210TQF
 */
public class DatosInfoDocumento implements Serializable{

    private Date fechaDigitalizacion;
    private String usuarioDigitalizo;
    private Date fechaAprobacion;
    private String usuarioAprobo;
    private Date fechaRechazo;
    private String usuarioRechazo;
    private String motivoRechazo;
    private String causaRechazo;
    private Date fechaElimino;
    private String usuarioElimino;
    private String motivoElimino;
    private String causaElimino;

    /**
     * @return the fechaDigitalizacion
     */
    public Date getFechaDigitalizacion() {
        return fechaDigitalizacion;
    }

    /**
     * @param fechaDigitalizacion the fechaDigitalizacion to set
     */
    public void setFechaDigitalizacion(Date fechaDigitalizacion) {
        this.fechaDigitalizacion = fechaDigitalizacion;
    }

    /**
     * @return the usuarioDigitalizo
     */
    public String getUsuarioDigitalizo() {
        return usuarioDigitalizo;
    }

    /**
     * @param usuarioDigitalizo the usuarioDigitalizo to set
     */
    public void setUsuarioDigitalizo(String usuarioDigitalizo) {
        this.usuarioDigitalizo = usuarioDigitalizo;
    }

    /**
     * @return the fechaRechazo
     */
    public Date getFechaRechazo() {
        return fechaRechazo;
    }

    /**
     * @param fechaRechazo the fechaRechazo to set
     */
    public void setFechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    /**
     * @return the usuarioRechazo
     */
    public String getUsuarioRechazo() {
        return usuarioRechazo;
    }

    /**
     * @param usuarioRechazo the usuarioRechazo to set
     */
    public void setUsuarioRechazo(String usuarioRechazo) {
        this.usuarioRechazo = usuarioRechazo;
    }

    /**
     * @return the motivoRechazo
     */
    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    /**
     * @param motivoRechazo the motivoRechazo to set
     */
    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    /**
     * @return the causaRechazo
     */
    public String getCausaRechazo() {
        return causaRechazo;
    }

    /**
     * @param causaRechazo the causaRechazo to set
     */
    public void setCausaRechazo(String causaRechazo) {
        this.causaRechazo = causaRechazo;
    }

    /**
     * @return the fechaElimino
     */
    public Date getFechaElimino() {
        return fechaElimino;
    }

    /**
     * @param fechaElimino the fechaElimino to set
     */
    public void setFechaElimino(Date fechaElimino) {
        this.fechaElimino = fechaElimino;
    }

    /**
     * @return the usuarioElimino
     */
    public String getUsuarioElimino() {
        return usuarioElimino;
    }

    /**
     * @param usuarioElimino the usuarioElimino to set
     */
    public void setUsuarioElimino(String usuarioElimino) {
        this.usuarioElimino = usuarioElimino;
    }

    /**
     * @return the motivoElimino
     */
    public String getMotivoElimino() {
        return motivoElimino;
    }

    /**
     * @param motivoElimino the motivoElimino to set
     */
    public void setMotivoElimino(String motivoElimino) {
        this.motivoElimino = motivoElimino;
    }

    /**
     * @return the causaElimino
     */
    public String getCausaElimino() {
        return causaElimino;
    }

    /**
     * @param causaElimino the causaElimino to set
     */
    public void setCausaElimino(String causaElimino) {
        this.causaElimino = causaElimino;
    }

    /**
     * @return the fechaAprobacion
     */
    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    /**
     * @param fechaAprobacion the fechaAprobacion to set
     */
    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    /**
     * @return the usuarioAprobo
     */
    public String getUsuarioAprobo() {
        return usuarioAprobo;
    }

    /**
     * @param usuarioAprobo the usuarioAprobo to set
     */
    public void setUsuarioAprobo(String usuarioAprobo) {
        this.usuarioAprobo = usuarioAprobo;
    }

}
