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
 *
 * @author develcom
 */
public class UtilitarioFecha {

    private SimpleDateFormat sdf;
    private Date date;

    public String tomarFecha(String formato) {
        Calendar calendar = new GregorianCalendar();

        sdf = new SimpleDateFormat(formato);
        String fecha = sdf.format(calendar.getTime());
        return fecha;
    }

    public String convertLongDate(long time){
        String resp=null;

        date = new Date(time);
        //sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        sdf = new SimpleDateFormat("dd/MM/yyyy - kk:mm:ss");
        resp = sdf.format(date.getTime());
        return resp;
    }

}
