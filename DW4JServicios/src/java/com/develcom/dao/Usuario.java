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
public class Usuario implements Serializable {
    private static final long serialVersionUID = -3636418295630586647L;

    private String idUsuario;
    private String nombre;
    private String apellido;
    private String cedula;
    private int idEstatus;
    private String estatus;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(int idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
