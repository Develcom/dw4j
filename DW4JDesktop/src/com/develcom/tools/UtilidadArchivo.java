/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import com.develcom.tools.trazas.Traza;

/**
 * Clase utilitaria
 * para copiar archivos y
 * escribir contenidos en el
 * archivo
 * @author develcom
 */
public class UtilidadArchivo {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(UtilidadArchivo.class);


    /**
     * Copia un archivo
     * @param origen
     * Ruta del archivo origen
     * @param destino
     * Ruta del archivo destino
     */
    public void copiarArchivo(File origen, File destino) {
        String fileName = origen.getName();
        //byte[] buf = new byte[10485760];
        byte[] buf = new byte[1024];
        int i = 0;
        int x = 0;
        int fileSize = (int)origen.length();
        int len;
        
        try{
            InputStream leyendo = new FileInputStream(origen);
            OutputStream escribiendo = new FileOutputStream(destino);

            int vueltas = fileSize / 10485760 + 1;

            x = 100 / vueltas;
            if (vueltas == 1){
                i = 100;
            }

            while ((len = leyendo.read(buf)) > 0) {
                i += x;
                escribiendo.write(buf, 0, len);
            }

            leyendo.close();
            escribiendo.flush();
            escribiendo.close();
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "ERROR: \n" + ex, "Error", 0);
            traza.trace("Archivo no encontrado para copiar", Level.ERROR, ex);
        }
        catch (IOException ex) {
            traza.trace("Error en el proceso de copiado ", Level.ERROR, ex);
        }
        catch (Exception ex) {
            traza.trace("Error general en el proceso de copiado", Level.ERROR, ex);
        }
    }

    /**
     * Escribe un continido
     * en un archivo
     * @param nombreArchivo
     * Ruta del archiv
     * @param contenidoArchivo
     * Contenido del archivo
     */
    public void escribirArchivo(File nombreArchivo, String contenidoArchivo){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(nombreArchivo);
            pw = new PrintWriter(fichero);
            pw.print(contenidoArchivo);
            pw.close();
            fichero.close();
        } catch (IOException ex) {
            traza.trace("error al escribir el contenido", Level.ERROR, ex);
        }



    }

}
