/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.tools;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author develcom
 */
public class PanelImagen  extends JPanel{



    private Image imagen;
    private String ruta;

    public PanelImagen() {
    }

    public PanelImagen(String nombreImagen) {
        if (nombreImagen != null) {
            imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
        }
    }

    public PanelImagen(Image imagenInicial) {
        if (imagenInicial != null) {
            imagen = imagenInicial;
        }
    }

    public void setImagen(String nombreImagen) {
        if (nombreImagen != null) {
            imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
        } else {
            imagen = null;
        }

        repaint();
    }

    public void setImagen(Image nuevaImagen) {
        imagen = nuevaImagen;

        repaint();
    }

    @Override
    public void paint(Graphics g) {

//        BufferedImage img = null;
//        int posx=0, posy=0;
//        try
//        {
//            img = ImageIO.read(new File("src/recursos/ejemplo.png"));
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);

            setOpaque(false);
        } else {
            setOpaque(true);
        }

        super.paint(g);
    }

}
