/*
 * utility.java
 *
 * Created on 21 de agosto de 2007, 03:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import com.develcom.tools.trazas.Traza;
import org.apache.log4j.Level;

/**
 * Clase para centrar ventanas
 *
 * @author develcom
 */
public class CentraVentanas {

    /**
     * Escribe trazas en el log
     */
    private static Traza traza = new Traza(CentraVentanas.class);

    //@Deprecated
    public static void centerDialog(javax.swing.JDialog dialogo) {
//        try {
//
//            LookAndFeelInfo[] lista = UIManager.getInstalledLookAndFeels();
//            for (int i = 0; i < lista.length; i++) {
//                System.out.println(lista[i].getClassName());
//            }
//
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension ventana = dialogo.getSize();
            dialogo.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
            //dialogo.setVisible(true);

            //        if (dialogDimension.width > screenDimension.width) {
            //            dialogDimension.width = screenDimension.width;
            //        }
            //        if (dialogDimension.height > screenDimension.height) {
            //            dialogDimension.height = screenDimension.height;
            //        }
            //
            //        dialogo.setLocation((screenDimension.width - dialogDimension.width) / 2,
            //                (screenDimension.height - dialogDimension.height) / 2);
            //        dialogo.setVisible(true);
//        } catch (ClassNotFoundException ex) {
//            traza.trace("clase no encontrada", Level.ERROR, ex);
//        } catch (InstantiationException ex) {
//            traza.trace("error al instanciar", Level.ERROR, ex);
//        } catch (IllegalAccessException ex) {
//            traza.trace("error: acceso ilegal", Level.ERROR, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            traza.trace("look anf feel no soportado", Level.ERROR, ex);
//        }

    }

//    @Deprecated
//    public static void centerFrame(javax.swing.JFrame ventana) {
//        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension dialogDimension = ventana.getSize();
//        if (dialogDimension.width > screenDimension.width) {
//            dialogDimension.width = screenDimension.width;
//        }
//        if (dialogDimension.height > screenDimension.height) {
//            dialogDimension.height = screenDimension.height;
//        }
//
//        ventana.setLocation((screenDimension.width - dialogDimension.width) / 2,
//                (screenDimension.height - dialogDimension.height) / 2);
//        ventana.setVisible(true);
//
//    }
//
//    @Deprecated
//    public static void centerFrame(javax.swing.JInternalFrame ventana) {
//        Dimension screenDimension = Principal.desktop.getSize();//Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension dialogDimension = ventana.getSize();
//        if (dialogDimension.width > screenDimension.width) {
//            dialogDimension.width = screenDimension.width;
//        }
//        if (dialogDimension.height > screenDimension.height) {
//            dialogDimension.height = screenDimension.height;
//        }
//
//        ventana.setLocation((screenDimension.width - dialogDimension.width) / 2,
//                (screenDimension.height - dialogDimension.height) / 2);
//        //ventana.setVisible(true);
//
//    }
//
    /**
     * Centra la ventana principal
     *
     * @param frame La ventana principal
     */
    public static void centerMaxFrame(JFrame frame) {
//        try {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point center = ge.getCenterPoint();
        Rectangle bounds = ge.getMaximumWindowBounds();

//            frame.setDefaultLookAndFeelDecorated(true);
//            JDialog.setDefaultLookAndFeelDecorated(true);
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        /*int w = Math.max(bounds.width/2, Math.min(frame.getWidth(), bounds.width));
         int h = Math.max(bounds.height/2, Math.min(frame.getHeight(), bounds.height));
         int x = center.x - w/2, y = center.y - h/2;
         frame.setBounds(x, y, w, h);*/
        frame.setBounds(bounds);
        //if (w == bounds.width && h == bounds.height)frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.validate();
//        } catch (ClassNotFoundException ex) {
//            traza.trace("clase no encontrada", Level.ERROR, ex);
//        } catch (InstantiationException ex) {
//            traza.trace("error al instanciar", Level.ERROR, ex);
//        } catch (IllegalAccessException ex) {
//            traza.trace("error: acceso ilegal", Level.ERROR, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            traza.trace("look anf feel no soportado", Level.ERROR, ex);
//        }
    }
//
//    @Deprecated
//    public static void centerMaxFrame(javax.swing.JInternalFrame frame, JDesktopPane panel) {
//                //frame.setSize(panel.getMaximumSize());
//                frame.setBounds(0, 0, panel.getWidth(), panel.getHeight());
//                System.out.println(panel.getMaximumSize());
//            /*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            Point center = ge.getCenterPoint();
//            Rectangle bounds = ge.getMaximumWindowBounds();
//
//            int w = Math.max(bounds.width/2, Math.min(frame.getWidth(), bounds.width));
//            int h = Math.max(bounds.height/2, Math.min(frame.getHeight(), bounds.height));
//            int x = center.x - w/2, y = center.y - h/2;
//            frame.setBounds(x, y, w, h);
//            frame.setBounds(bounds);
//            //if (w == bounds.width && h == bounds.height)frame.setExtendedState(Frame.MAXIMIZED_BOTH);
//            frame.validate();
//            */
//
//    }

    /**
     * Centra una ventana interna en su ventana principal
     *
     * @param frame CentraVentanas interna
     * @param panel JDesktopPane de la ventana principal
     */
    public static void centrar(javax.swing.JInternalFrame frame, JDesktopPane panel) {
        String so = System.getProperty("os.name");
        int index = so.indexOf("windows");
        boolean ok = so.startsWith("Windows");
//        Toolkit tk = Toolkit.getDefaultToolkit();  
//        Dimension screenSize = tk.getScreenSize();  
//        int screenHeight = screenSize.height;  
//        int screenWidth = screenSize.width; 
//        frame.setLocation((int) (screenWidth / 2.8), (int) (screenHeight / 4));

        traza.trace("sistema operativo " + so, Level.INFO);
        Container padre = frame.getParent();
        Container pa = panel.getParent();
        int ancho = pa.getWidth();
        int alto = pa.getHeight();
        if (ok) {
            frame.setLocation((ancho / 2) - (frame.getWidth() / 2), (alto / 2) - (frame.getHeight() / 2) - 5);
        } else {
            frame.setLocation((ancho / 2) - (frame.getWidth() / 2), (alto / 2) - (frame.getHeight() / 2) - 20);
        }
//        Dimension desktopSize = panel.getSize();
//        Dimension jInternalFrameSize = frame.getSize();
//        //frame.setLocation((desktopSize.width - jInternalFrameSize.width) / 2, (desktopSize.height - jInternalFrameSize.height) / 2);
//        //frame.setLocation((desktopSize.width / 2) - (jInternalFrameSize.width / 2), (desktopSize.height / 2 ) - (jInternalFrameSize.height / 2));
//        frame.setLocation( (int) ((desktopSize.getWidth() / 2) - (jInternalFrameSize.getWidth() / 2)), (int ) ((desktopSize.getHeight() / 2 ) - (jInternalFrameSize.getHeight() / 2)));


    }
//    public static void centrar(javax.swing.JInternalFrame frame) {
//
//        frame.setLocation(frame.getParent().getWidth() / 2 - frame.getWidth() / 2, frame.getParent().getHeight() / 2 - frame.getHeight() / 2 - 20);
//
//        //este metodo devuelve el tamaño de la pantalla
//        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
//
//        //obtenemos el tamaño de la ventana
//        Dimension ventana = frame.getSize();
//
//        //para centrar la ventana lo hacemos con el siguiente calculo
//        //frame.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
//        frame.setLocation((pantalla.width - ventana.width) / 2, 0);
//    }
}
