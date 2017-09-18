package com.develcom.gui.toolfile;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;



/* ImageFileView.java is used by FileChooserDemo2.java. */
public class ImageFileView extends FileView {

//    ImageIcon jpgIcon = Utils.createImageIcon("images/jpgIcon.gif");
//    ImageIcon gifIcon = Utils.createImageIcon("images/gifIcon.gif");
//    ImageIcon tiffIcon = Utils.createImageIcon("images/tiffIcon.gif");
//    ImageIcon pngIcon = Utils.createImageIcon("images/pngIcon.png");

    //ImageIcon tiffIcon;// = Utils.createImageIcon("/com/develcom/gui/imagenes/develcom/tif.gif");

    @Override
    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    @Override
    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    @Override
    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    @Override
    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(Utils.gif)){
                type = "GIF Image";
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(Utils.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    @Override
    public Icon getIcon(File f) {
        String extension = Utils.getExtension(f);
        Icon tiffIcon = null;
//
        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                tiffIcon = Utils.createImageIcon("/com/develcom/gui/imagenes/develcom/ext/jpg.png");
            } else if (extension.equals(Utils.gif)) {
                tiffIcon = Utils.createImageIcon("/com/develcom/gui/imagenes/develcom/ext/gif.png");
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                tiffIcon = Utils.createImageIcon("/com/develcom/gui/imagenes/develcom/ext/tif.png");
            } else if (extension.equals(Utils.png)) {
                //icon = pngIcon;
            } else if (extension.equals(Utils.pdf)) {
                tiffIcon = Utils.createImageIcon("/com/develcom/gui/imagenes/develcom/ext/pdf.png");
            }
        }
//        return icon;

        return tiffIcon;
    }
}
