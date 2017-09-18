package com.develcom.tools.cryto;

import com.develcom.tools.cryto.base64api.Base64API;
import com.develcom.tools.trazas.Traza;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.log4j.Level;

public class CodDecodArchivos {

    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(CodDecodArchivos.class);

    /**
     * Decodifica un archivo
     *
     * @param rutaArchivoCodificado Ruta del archivo codificado
     * @param rutaArchivoDeco Ruta del archivo decodificado
     */
    public void decodificar(String rutaArchivoCodificado, String rutaArchivoDeco) {

//        traza.trace("descodificar " + rutaArchivoCod + " en  " + rutaArchivoDeco, Level.INFO);
        byte[] buffer;
        InputStream in;
        BufferedOutputStream bos;
        File archivoCod = new File(rutaArchivoCodificado);
        File archivoDecodificado = new File(rutaArchivoDeco);
        int len;
        long t1, t2, dif;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Calendar tiempoInicio = Calendar.getInstance();
        //Calendar tiempoDiferencia = Calendar.getInstance();
        Calendar tiempoFinal;
        GregorianCalendar tiempoF = new GregorianCalendar();
        GregorianCalendar tiempoI = new GregorianCalendar();

        try {
            t1 = tiempoInicio.getTimeInMillis();
            tiempoI.setTimeInMillis(t1);
            
            in = new Base64API.InputStream(new FileInputStream(rutaArchivoCodificado));
            bos = new BufferedOutputStream(new FileOutputStream(archivoDecodificado));
            
            traza.trace("tamaño del archivo " + archivoCod.getName() + " " + archivoCod.length() / 1024 + " kilobytes", Level.INFO);
            traza.trace("Decodificando...  " + sdf.format(tiempoI.getTime()), Level.INFO);
            
//             buffer = new byte[3];
             buffer = new byte[10485760];
//            for (int n = in.read(buffer); n > 0; n = in.read(buffer)) {
//                bos.write(buffer);
//            }
            while ((len = in.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            in.close();

            tiempoFinal = Calendar.getInstance();
            t2 = tiempoFinal.getTimeInMillis();
            dif = t2 - t1;
            tiempoF.setTimeInMillis(dif);

            traza.trace("Tiempo de conversión total de decodificación fue " + sdf.format(tiempoF.getTime()), Level.INFO);

            traza.trace("tamaño del archivo " + archivoDecodificado.getName() + " " + archivoDecodificado.length() + " bytes", Level.INFO);
        } catch (FileNotFoundException e) {
            traza.trace("error archivo no encontrado en la decodificacion", Level.ERROR, e);
        } catch (IOException e) {
            traza.trace("error de entrada salida en la decodificacion", Level.ERROR, e);
        }
    }

    /**
     * Codifica un archivo
     *
     * @param rutaArchivoDeco Ruta del archivo decodificado
     * @param rutaArchivoCod Ruta del archivo codificado
     */
    public void codificar(String rutaArchivoDeco, String rutaArchivoCod) {

        traza.trace("codificando el archivo " + rutaArchivoDeco + " en el archivo " + rutaArchivoCod, Level.INFO);

        OutputStream out;
        BufferedInputStream bis;
        File fileDeco = new File(rutaArchivoDeco);
        File fileCod = new File(rutaArchivoCod);

        long t1, t2, dif;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Calendar tiempoInicio = Calendar.getInstance();
        //Calendar tiempoDiferencia = Calendar.getInstance();
        Calendar tiempoFinal;
        GregorianCalendar tiempo = new GregorianCalendar();

        try {
            t1 = tiempoInicio.getTimeInMillis();

            out = new Base64API.OutputStream(new FileOutputStream(rutaArchivoCod));
            //BufferedWriter out = new BufferedWriter(new FileWriter(System.getProperty("user.home")+"/prueba.txt"));

            traza.trace("tamaño del archivo " + fileDeco.getName() + " " + fileDeco.length() / 1024 + " kilobytes", Level.INFO);
            bis = new BufferedInputStream(new FileInputStream(fileDeco));
            int bytes = (int) fileDeco.length();
            //byte[] buffer = new byte[8192];
            byte[] buffer = new byte[bytes];
            bis.read(buffer);

            traza.trace("Codificando: " + fileDeco.getName(), Level.INFO);
            out.write(buffer);
            out.close();
            bis.close();

            tiempoFinal = Calendar.getInstance();
            t2 = tiempoFinal.getTimeInMillis();
            dif = t2 - t1;
            tiempo.setTimeInMillis(dif);

            traza.trace("Tiempo de conversión total de decodificación fue " + sdf.format(tiempo.getTime()), Level.INFO);

            traza.trace("tamaño del archivo " + fileCod.getName() + " " + fileCod.length() + " bytes", Level.INFO);
        } catch (IOException ex) {
            traza.trace("Error al codificar el archivo " + fileDeco.getName(), Level.ERROR, ex);
        }
    }
}
