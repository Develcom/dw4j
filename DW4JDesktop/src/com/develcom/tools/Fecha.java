/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Calse utilitaria
 * para tomar la fecha
 * del sistema
 * @author develcom
 */
public class Fecha {




    /**
     * Toma la fecha del
     * sistema
     * @param formato
     * Formato de la fecha
     * @return
     * La fecha como una cadena
     * con el formato dado
     */
    public static String tomarFecha(String formato){
        Calendar date = new GregorianCalendar();

        Date hoy = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        String fecha = sdf.format(date.getTime());
        return fecha;
    }

    /**
     * Toma la fecha del sistema
     * @return
     * GregorianCalendar
     */
    public static GregorianCalendar tomarFecha(){

        GregorianCalendar date = new GregorianCalendar();
//        GregorianCalendar date1 = new GregorianCalendar(2012,02,01);
//
//        System.out.println(" fecha "+date.after(date1));

        return date;
    }

//    public static void main(String[] arg){
//        new Fecha().tomarFecha();
//    }

}
