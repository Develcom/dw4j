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
public class Fabrica implements Serializable{
    private static final long serialVersionUID = -3919673669390398297L;
    
    
    private boolean pertenece;
    private String usuario;

    public boolean isPertenece() {
        return pertenece;
    }

    public void setPertenece(boolean pertenece) {
        this.pertenece = pertenece;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    
    
}
