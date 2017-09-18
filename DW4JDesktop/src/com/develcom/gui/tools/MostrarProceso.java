/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.tools;

import com.develcom.gui.DialogoEspera;
import com.develcom.tools.trazas.Traza;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class MostrarProceso extends Thread{
//public class MostrarProceso implements Runnable{

    private Traza traza = new Traza(MostrarProceso.class);
    private DialogoEspera dialogoEspera;
    private String mensaje;
    private Progreso progreso;

    public MostrarProceso(String mensaje) {
        this.mensaje = mensaje;
        traza.trace("recibiendo el mensaje "+mensaje, Level.INFO);
//        dialogoEspera = new DialogoEspera();
        dialogoEspera = new DialogoEspera(mensaje);
    }

    public MostrarProceso() {
        traza.trace("constructor", Level.INFO);
        dialogoEspera = new DialogoEspera();
    }


    public synchronized void mensaje(String mensaje){
        this.mensaje = mensaje;
        traza.trace("recibiendo el mensaje "+mensaje, Level.INFO);
        SwingUtilities.invokeLater(new Progreso(mensaje, dialogoEspera));
    }



    @Override
    public void run() {
        traza.trace("iniciando el dialogo de espera", Level.INFO);
//        dialogoEspera.mostrar(mensaje);
        progreso = new Progreso(mensaje, dialogoEspera);
        SwingUtilities.invokeLater(progreso);

//        new Thread(new Runnable() {
//
//            public void run() {
//                dialogoEspera.mostrar(mensaje);
////                SwingUtilities.invokeLater(new Runnable() {
////
////                    public void run() {
////                        dialogoEspera.mostrar(mensaje);
////                    }
////                });
//                //dialogoEspera.mostrar();
//            }
//        }).start();

        

//        SwingUtilities.invokeLater(new Progreso(mensaje, dialogoEspera));

//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                dialogoEspera.mostrar();
//            }
//        });

//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                dialogoEspera.mostrar();
//            }
//        });


//        dialogoEspera.mostrar(mensaje);
    }

    public DialogoEspera getDialogoEspera() {
        return dialogoEspera;
    }


    public void detener(){
        dialogoEspera.dispose();
        this.interrupt();
    }

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DialogoEspera dialog = new DialogoEspera();
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }


    
}
