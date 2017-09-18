/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.controlador.expediente;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.documento.InfoDocumento;
import java.util.List;

/**
 *
 * @author familia
 */
public class Expedientes {
    
    private String expediente;
    private List<Libreria> libreria;
    private List<Categoria> categoria;
    private List<SubCategoria> subCategoria;
    private List<TipoDocumento> tiposDocumentos;
    private List<InfoDocumento> infoDocumentos;
    private List<Indices> indices;

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public List<Libreria> getLibreria() {
        return libreria;
    }

    public void setLibreria(List<Libreria> libreria) {
        this.libreria = libreria;
    }

    public List<Categoria> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<Categoria> categoria) {
        this.categoria = categoria;
    }

    public List<SubCategoria> getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(List<SubCategoria> subCategoria) {
        this.subCategoria = subCategoria;
    }

    public List<TipoDocumento> getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<TipoDocumento> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public List<InfoDocumento> getInfoDocumentos() {
        return infoDocumentos;
    }

    public void setInfoDocumentos(List<InfoDocumento> infoDocumentos) {
        this.infoDocumentos = infoDocumentos;
    }

    public List<Indices> getIndices() {
        return indices;
    }

    public void setIndices(List<Indices> indices) {
        this.indices = indices;
    }
}
