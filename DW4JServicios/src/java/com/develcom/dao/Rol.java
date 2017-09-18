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
public class Rol  implements Serializable{
    private static final long serialVersionUID = -4880432965694793601L;

    private int idRol;
    private String rol;

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
