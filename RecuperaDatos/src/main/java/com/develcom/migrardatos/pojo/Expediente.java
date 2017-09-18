/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.pojo;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author familia
 */
@Component
public class Expediente implements Serializable {

    private static final long serialVersionUID = 4329357767452376518L;

    private String expediente;
    private String libreria;
    private String categoria;
    private List<Indice> indices;

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
     * @return the idLibreria
     */
    public String getLibreria() {
        return libreria;
    }

    /**
     * @param libreria the idLibreria to set
     */
    public void setLibreria(String libreria) {
        this.libreria = libreria;
    }

    /**
     * @return the idCategoria
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the idCategoria to set
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the indices
     */
    public List<Indice> getIndices() {
        return indices;
    }

    /**
     * @param indices the indices to set
     */
    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }

}
