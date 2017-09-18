/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.dao;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author develcom
 */
public class EliminaDocumento  implements Serializable{
    private static final long serialVersionUID = -3342378609471095154L;
    private Date fechaEliminado;
    private String usuarioElimino;
    private String idExpediente;
    private Libreria libreria;
    private Categoria categoria;
    private SubCategoria subCategoria;
    private InfoDocumento infoDocumento;
    private String causaElimino;
    private String motivoElimino;

    public Date getFechaEliminado() {
        return fechaEliminado;
    }

    public void setFechaEliminado(Date fechaEliminado) {
        this.fechaEliminado = fechaEliminado;
    }

    public String getUsuarioElimino() {
        return usuarioElimino;
    }

    public void setUsuarioElimino(String usuarioElimino) {
        this.usuarioElimino = usuarioElimino;
    }

    public String getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(String idExpediente) {
        this.idExpediente = idExpediente;
    }

    public Libreria getLibreria() {
        return libreria;
    }

    public void setLibreria(Libreria libreria) {
        this.libreria = libreria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public SubCategoria getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }

    public InfoDocumento getInfoDocumento() {
        return infoDocumento;
    }

    public void setInfoDocumento(InfoDocumento infoDocumento) {
        this.infoDocumento = infoDocumento;
    }

    public String getCausaElimino() {
        return causaElimino;
    }

    public void setCausaElimino(String causaElimino) {
        this.causaElimino = causaElimino;
    }

    public String getMotivoElimino() {
        return motivoElimino;
    }

    public void setMotivoElimino(String motivoElimino) {
        this.motivoElimino = motivoElimino;
    }
}
