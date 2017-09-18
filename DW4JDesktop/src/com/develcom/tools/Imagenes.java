/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;



/**
 * Clase utilitaria para
 * los archivos escaneados
 * @author develcom
 */
public abstract class Imagenes {


    /**
     * Rota la imagen
     * @param stripImage
     * Bufer de la imagen
     * @param angle
     * El angulo de la imagen
     * @return
     * Bufer de la imagen rotada
     */
    public static BufferedImage rotate(BufferedImage stripImage, double angle) {


        /*int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage dimg = new BufferedImage(h, w, img.getType());

        Graphics2D g = dimg.createGraphics();

        g.rotate(Math.toRadians(angle), img.getWidth()/2, img.getHeight()/2);

        g.drawImage(img, null, 0, 0);
        return dimg;
        */
//        int oldW = 0;
//        int oldH = 0;
//        int newW = 0;
//        int newH = 0;
//        angle=Math.toRadians(angle);
//        AffineTransform anAffTr = null;
//        GraphicsEnvironment aGrEnv = null;
//        GraphicsDevice aGrDev = null;
//        GraphicsConfiguration aGrConf = null;
//        BufferedImage rotatedImage = null;
//        Graphics2D aG2D = null;
        
        int oldW;
        int oldH;
        int newW;
        int newH;
        angle=Math.toRadians(angle);
        AffineTransform anAffTr;
        GraphicsEnvironment aGrEnv;
        GraphicsDevice aGrDev;
        GraphicsConfiguration aGrConf;
        BufferedImage rotatedImage;
        Graphics2D aG2D;

        oldW = stripImage.getWidth();
        oldH = stripImage.getHeight();
        newW = (int) (Math.round(oldH * Math.abs(Math.sin(angle)) + oldW * Math.abs(Math.cos(angle))));
        newH = (int) (Math.round(oldH * Math.abs(Math.cos(angle)) + oldW * Math.abs(Math.sin(angle))));
        anAffTr = AffineTransform.getTranslateInstance((newW - oldW) / 2, (newH - oldH) / 2);
        anAffTr.rotate(angle, oldW / 2, oldH / 2);
        aGrEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        aGrDev = aGrEnv.getDefaultScreenDevice();
        aGrConf = aGrDev.getDefaultConfiguration();
        rotatedImage = aGrConf.createCompatibleImage(newW, newH);
        aG2D = rotatedImage.createGraphics();
        aG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        aG2D.setPaint(Color.WHITE);
        aG2D.fillRect(0, 0, newW, newH);
        aG2D.drawRenderedImage(stripImage, anAffTr);
        aG2D.dispose();
        return rotatedImage;
    }


    public static BufferedImage scale(double scale, BufferedImage srcImg){

        if (scale == 1 ){
            return srcImg;
        }

        AffineTransformOp op = new AffineTransformOp
        (AffineTransform.getScaleInstance(scale, scale), null);
        return op.filter(srcImg, null);
    }

    /**
     * Aleja la imagen
     * @param bi
     * Bufer de la imagen
     * @param scale
     * Escala de la imagen
     * @return
     * Bufer de la image alejada
     */
    public static BufferedImage zoomOut(BufferedImage bi, int scale){
        int width = bi.getWidth() / scale;
        int height = bi.getHeight() / scale;

        BufferedImage biScale = new BufferedImage(width, height, bi.getType());
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                biScale.setRGB(i, j, bi.getRGB(i*scale, j*scale));
            }
        }

        return biScale;
    }

    /**
     * Acerca la imagen
     * @param bi
     * Bufer de la imagen
     * @param scale
     * Escala de la imagen
     * @return
     * Bufer de la image acercada
     */
    public static  BufferedImage zoomIn(BufferedImage bi, int scale){
        int width = scale * bi.getWidth();
        int height = scale * bi.getHeight();

        BufferedImage biScale = new BufferedImage(width, height, bi.getType());

        // Cicla dando un valore medio al pixel corrispondente
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                biScale.setRGB(i, j, bi.getRGB(i/scale, j/scale));
            }
        }

        return biScale;
    }
}
