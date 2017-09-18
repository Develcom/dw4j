/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

/**
 *
 * @author develcom
 */
public class Mensajes {

    private static String mensaje;

    public static String getMensaje() {
        return mensaje;
    }

    public static void setMensaje(String mensaje) {
        Mensajes.mensaje = mensaje;
    }

}
