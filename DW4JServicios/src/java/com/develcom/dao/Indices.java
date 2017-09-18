/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;

/**
 *
 * @author develcom
 */
public class Indices implements Serializable{
    private static final long serialVersionUID = 8531069384626004245L;

    private int idIndice;
    private int idCategoria;
    private String indice;
    private String tipo;
    private int codigo;
    private String clave;

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
