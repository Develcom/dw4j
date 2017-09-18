/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author develcom
 */
public class Categoria implements Serializable{
    private static final long serialVersionUID = -2822420755000006811L;

    private int idCategoria;
    private String categoria;
    private String estatus;
    private List<Indice> lstIndices;

    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public List<Indice> getLstIndices() {
        return lstIndices;
    }

    public void setLstIndices(List<Indice> lstIndices) {
        this.lstIndices = lstIndices;
    }
}
