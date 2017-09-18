/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.toolfile;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 *
 * @author dflores
 */
public class FileFilterImagenes extends FileFilter {

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (//extension.equals(Utils.tiff) ||
                    //extension.equals(Utils.tif) //||extension.equals(Utils.gif) ||
                    extension.equals(Utils.jpeg)
                    || extension.equals(Utils.jpg)
                    || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;

    }

    public String getDescription() {
        return "Archivos Imagenes";
    }
}
