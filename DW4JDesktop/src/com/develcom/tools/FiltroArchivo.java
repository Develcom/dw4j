/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools;

import java.io.File;
import java.util.Locale;
import javax.swing.filechooser.FileFilter;

/**
 * Clase para filtrar
 * los tipos de archivos
 * que se selecionaran
 * @author develcom
 */
public class FiltroArchivo extends FileFilter{


    private String descripcion;
    private String[] extencion = {"tif","xls","doc","ppt","pdf","ods","odp","odt"};

    public FiltroArchivo(String descripcion) {
        this.descripcion=descripcion;
    }



    
    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            // NOTE: we tested implementations using Maps, binary search
            // on a sorted list and this implementation. All implementations
            // provided roughly the same speed, most likely because of
            // overhead associated with java.io.File. Therefor we've stuck
            // with the simple lightweight approach.
            String fileName = f.getName();
	    int i = fileName.lastIndexOf('.');
	    if (i > 0 && i < fileName.length() - 1) {
                String desiredExtension = fileName.substring(i+1).toLowerCase(Locale.ENGLISH);
                if(desiredExtension!=null){
                    return true;
                }
//                for (String extension : lowerCaseExtensions) {
//                    if (desiredExtension.equals(extension)) {
//                        return true;
//                    }
//                }
	    }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return descripcion;
    }

}
