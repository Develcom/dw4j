/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.pojo;

import java.io.Serializable;

/**
 *
 * @author familia
 */
public class Combo implements Serializable{

    private static final long serialVersionUID = 7287894264879864530L;

    private int cogigoIndice;
    private String descripcion;

    /**
     * @return the cogigoIndice
     */
    public int getCogigoIndice() {
        return cogigoIndice;
    }

    /**
     * @param cogigoIndice the cogigoIndice to set
     */
    public void setCogigoIndice(int cogigoIndice) {
        this.cogigoIndice = cogigoIndice;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
