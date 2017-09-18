/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.tools;

import org.apache.log4j.Level;
import com.develcom.tools.trazas.Traza;
import com.develcom.gui.DialogoEspera;

/**
 *
 * @author develcom
 */
public class Progreso implements Runnable{


    private Traza traza = new Traza(Progreso.class);
    private DialogoEspera dialogoEspera;
    private String mensaje;

    public Progreso(String mensaje, DialogoEspera dialogoEspera) {
        this.dialogoEspera = dialogoEspera;
        this.mensaje = mensaje;
        traza.trace("mensaje nuevo "+mensaje, Level.INFO);
    }

    @Override
    public void run() {
        traza.trace("agregando nuevo mensaje", Level.INFO);
//        dialogoEspera.mostrar();
        dialogoEspera.setVisible(true);
    }

}
