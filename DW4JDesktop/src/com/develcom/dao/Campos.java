/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.util.Date;

/**
 *
 * @author develcom
 */
public class Campos {

    public static final String CADENA = "java.lang.String";
    public static final String FECHA = "java.sql.Date";
    public static final String NUMERO = "java.lang.Integer";


    public static final String STRING = String.class.getName();
    public static final String INTEGER = Integer.class.getName();
    public static final String DATE = Date.class.getName();


    private String nombre;
    private Object tipoDato;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Object getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(Object tipoDato) {
        this.tipoDato = tipoDato;
    }

    

}
