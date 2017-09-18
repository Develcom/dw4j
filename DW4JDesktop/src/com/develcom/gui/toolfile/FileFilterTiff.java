/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.toolfile;

import java.io.File;
import javax.swing.filechooser.FileFilter;



/**
 *
 * @author dflores
 */
public class FileFilterTiff extends FileFilter {


    @Override
    public boolean accept(File f) {
        String name = new String();

        if (f.isDirectory()) {
            return true;
        }
        name = f.getName();
        return name.endsWith(".tiff");

//        String extension = Utils.getExtension(f);
//        if (extension != null) {
//            if (extension.equals(Utils.tiff) ||
//                    extension.equals(Utils.tif) //||extension.equals(Utils.gif) ||
//                    //extension.equals(Utils.jpeg) ||
//                    // extension.equals(Utils.jpg) ||
//                    //extension.equals(Utils.png))
//                    ) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        return false;

    }

    @Override
    public String getDescription() {
        return "Archivos tiff (*.tiff)";
    }
}
