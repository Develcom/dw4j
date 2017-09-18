/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Level;
import com.develcom.tools.trazas.Traza;

/**
 *
 * @author develcom
 */
public class Zip {

    private Traza traza =  new Traza(Zip.class);
    private final int BUFFER = 2048;
    private byte buffer[] = new byte[BUFFER];
//    private String userdir=System.getProperty("user.dir");
//    private String fileZip;

//    public Zip() {
        //String userdir=System.getProperty("user.home");
//        String rutaTemp =  ManejoSesion.getPropedades().getProperty("pathTmp");
//        fileZip=userdir+"/"+rutaTemp+"/"+ManejoSesion.getPropedades().getProperty("fileZip");
//    }



    
    
    public void descomprimirArchivo(File archivo){
         BufferedOutputStream dest = null;
         ZipEntry entry;
        
        try {

         FileInputStream fis = new FileInputStream(archivo);
         ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

         while((entry = zis.getNextEntry()) != null) {

            traza.trace("Extracting: " +entry, Level.INFO);
            int count;

            // write the files to the disk
            if(!entry.isDirectory()){
                
                String rutaarchivo=entry.getName();
                int index=rutaarchivo.lastIndexOf("/");
                rutaarchivo=rutaarchivo.substring(index+1); 
                
                
                FileOutputStream fos = new FileOutputStream(rutaarchivo);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(buffer, 0, BUFFER)) != -1) {
                    dest.write(buffer, 0, count);
                }
            }
            //FileOutputStream fos = new 	      FileOutputStream(entry.getName());
            
            dest.flush();
            dest.close();
         }
         zis.close();
      } catch(Exception e) {
         traza.trace("error al descomprimir el archivo "+archivo, Level.ERROR, e);
      }
        
    }
    
    
    public void comprimirArchivos(File archivo){
        String nombreArchivo;
        
        //copyFile(archivo);
        nombreArchivo=archivo.getName().substring(0, archivo.getName().indexOf("."));
        
        try {
            FileInputStream in = new FileInputStream(archivo);
            FileOutputStream out = new FileOutputStream(nombreArchivo+".zip");

            
            ZipOutputStream zipOut = new ZipOutputStream(out);
            ZipEntry entry = new ZipEntry(archivo.toString());
            
            zipOut.putNextEntry(entry);
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
            zipOut.closeEntry();
            zipOut.close();
            
            
        } catch (IOException ex) {
            traza.trace("error al comprimir el archivo "+archivo, Level.ERROR, ex);
        }
    }
    
    
//    public void copyFile(File archivo) {
//
//        try{
//            File dir = new File(userdir+"/tmp");
//            if(!dir.exists()){
//                dir.mkdir();
//            }
//            FileChannel in = (new FileInputStream(archivo)).getChannel();
//            FileChannel out = (new FileOutputStream(dir+"/"+archivo.getName())).getChannel();
//            in.transferTo(0, archivo.length(), out);
//            in.close();
//            out.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    public void copyFile(File s, File t) {
        try{
            FileChannel in = (new FileInputStream(s)).getChannel();
            FileChannel out = (new FileOutputStream(t)).getChannel();
            in.transferTo(0, s.length(), out);
            in.close();
            out.close();
        } catch(Exception e) {
            traza.trace("error durante la copia del archivo", Level.ERROR, e);
        }
    }

}
