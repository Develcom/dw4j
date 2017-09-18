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
public class Libreria  implements Serializable{
    private static final long serialVersionUID = 2428823359479157938L;
    
    private String descripcion;
    private int idLibreria;
    private String estatus;


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
