

package com.develcom.tool.cryto;

import com.develcom.tool.log.Traza;
import com.develcom.tools.cryto.base64api.Base64API;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Level;

public class CodDecodArchivos   {

    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(CodDecodArchivos.class);

    /**
     * Decodifica un archivo
     *
     * @param rutaArchivoCod
     * Ruta del archivo codificado
     * @param rutaArchivoDeco
     * Ruta del archivo decodificado
     */
    public void decodificar(String rutaArchivoCod, String rutaArchivoDeco) {

        traza.trace("descodificar "+rutaArchivoCod+" en  "+rutaArchivoDeco, Level.INFO);

        try {
            long startTime2 = (new java.util.Date()).getTime();
            InputStream in = new Base64API.InputStream(new FileInputStream(rutaArchivoCod));
            File archivoDeco = new File(rutaArchivoDeco);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(archivoDeco));
            traza.trace("Decodificando...", Level.INFO);
            byte[] buffer2 = new byte[3];
            for (int n = in.read(buffer2); n > 0; n = in.read(buffer2)) {
                bos.write(buffer2);
            }
            bos.close();
            in.close();
            long endTime2 = (new java.util.Date()).getTime();
            long diffTime2 = endTime2 - startTime2;
            traza.trace("Tiempo de conversión total de decodificación fue " + (diffTime2 * 0.001) / 60 + " min", Level.INFO);

        } catch (FileNotFoundException e) {
            traza.trace("error archivo no encontrado en la decodificacion", Level.ERROR, e);
        } catch (IOException e) {
            traza.trace("error de entrada salida en la decodificacion", Level.ERROR, e);
        }
    }

    /**
     * Codifica un archivo
     * @param rutaArchivoDeco
     * Ruta del archivo decodificado
     * @param rutaArchivoCod
     * Ruta del archivo codificado
     */
    public void codificar(String rutaArchivoDeco, String rutaArchivoCod) {
        
        traza.trace("codificando el archivo "+rutaArchivoDeco+" en el archivo "+rutaArchivoCod, Level.INFO);

        OutputStream out = null;
        File fileDeco = null;
        try {
            long startTime = (new java.util.Date()).getTime();
            out = new Base64API.OutputStream(new FileOutputStream(rutaArchivoCod));
            //BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/prueba.txt"));
            fileDeco = new File(rutaArchivoDeco);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileDeco));
            int bytes = (int) fileDeco.length();
            byte[] buffer = new byte[bytes];
            bis.read(buffer);
            try {
                traza.trace("Codificando: "+ fileDeco.getName(), Level.INFO);
                out.write(buffer);
                out.close();
                bis.close();
            } catch (IOException e) {
                traza.trace("error de entrada salida en la codificacion", Level.ERROR, e);
            }
            long endTime = (new java.util.Date()).getTime();
            long diffTime = endTime - startTime;
            traza.trace("Tiempo de conversión total de decodificación fue " + (diffTime * 0.001) / 60 + " min", Level.INFO);
        } catch (IOException ex) {
            traza.trace("Error al codificar el archivo "+fileDeco.getName(), Level.ERROR, ex);
        } 
    }
}
