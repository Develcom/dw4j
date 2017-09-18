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
public class FileFilterPDF extends FileFilter {


    @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".pdf");
        }

        @Override
        public String getDescription() {
            return "Archivos PDF (*.pdf)";
        }
}
