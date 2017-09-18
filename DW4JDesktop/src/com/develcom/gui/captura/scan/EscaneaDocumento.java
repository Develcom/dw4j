/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.captura.scan;

import com.develcom.documento.InfoDocumento;
import com.develcom.gui.Principal;
import com.develcom.gui.visor.VerImagenes;
import com.develcom.gui.captura.GuardarDoc;
import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.gui.toolfile.ImagePreview;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.FiltroArchivo;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.trazas.Traza;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import uk.co.mmscomputing.device.sane.SaneIOException;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerDevice;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;

/**
 *
 * @author develcom
 */
public class EscaneaDocumento implements ScannerListener {

    private Traza traza = new Traza(EscaneaDocumento.class);

    /**
     * bufer de la imagen
     */
    private List<BufferedImage> imageBuffer = new ArrayList<BufferedImage>();

    /**
     * Herramienta para la imagen y el archivo
     */
    private ToolsFiles toolsTiff = new ToolsFiles();

    /**
     * Objeto para el escaner
     */
    private Scanner scanner;

    /**
     * Accion a tomar (reemplazar, versionar, etc)
     */
    private String accion;
    //private static String edoScaner;
    private boolean versionar = false;
    /**
     * Informacion del documento
     */
    private InfoDocumento infoDocumento;
    private ScannerDevice scannerDevice;

    public EscaneaDocumento(String accion, InfoDocumento infoDocumento, boolean buscarArchivo) {

        this.infoDocumento = infoDocumento;
        if (accion != null) {
            this.accion = accion;
            if (accion.equalsIgnoreCase("versionar")) {
                versionar = true;
            }
        }
        if (!buscarArchivo) {
        //ClassLoader cl = Thread.currentThread().getContextClassLoader();
            //JarLib.load(uk.co.mmscomputing.device.sane.jsane.class, "jsane");
            scanner = Scanner.getDevice();
            scanner.addListener(this);
        }
    }

//    public EscaneaDocumento() {
//        
//        scanner = Scanner.getDevice();
//        scanner.addListener(this);
//        
//    }
    @Override
    public void update(ScannerIOMetadata.Type type, ScannerIOMetadata metadata) {

        boolean cancel = false;
        //ScannerDevice scannerDevice = null;

        if (type.equals(ScannerIOMetadata.ACQUIRED)) {

            try {
                BufferedImage bufImg = metadata.getImage();

                traza.trace("buffer de la imagen " + bufImg, Level.INFO);
                imageBuffer.add(bufImg);

            } catch (Exception e) {
                traza.trace("Error al agregar la imagen al bufer de imagen", Level.ERROR, e);
            }

        } else if (type.equals(ScannerIOMetadata.NEGOTIATE)) {

            traza.trace("negociando con el escaner", Level.INFO);

            try {

                scannerDevice = metadata.getDevice();
                scannerDevice.setShowUserInterface(true);
                scannerDevice.setShowProgressBar(true);

            } catch (Exception e) {
                traza.trace("Error durante el escaneo", Level.ERROR, e);
                JOptionPane.showMessageDialog(new JFrame(), "Error en el escaneo\n" + e.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
            }

        } else if (type.equals(ScannerIOMetadata.STATECHANGE)) {

            traza.trace("Estado del escaner " + metadata.getState() + " - " + metadata.getStateStr(), Level.INFO);

            if (scannerDevice != null) {
                cancel = scannerDevice.getCancel();
                traza.trace("scannerDevice.getCancel() " + scannerDevice.getCancel(), Level.INFO);
            }

            if (metadata.isFinished()) {

                if (!cancel) {
                    traza.trace("tamaño del bufer " + imageBuffer.size(), Level.INFO);
                    if (toolsTiff.save(imageBuffer)) {

                        if (!imageBuffer.isEmpty()) {
                            codificar(null, true);
                        }
                    }
                }
            }

        } else if (type.equals(ScannerIOMetadata.EXCEPTION)) {
            traza.trace("Error al escanear el documento", Level.ERROR, metadata.getException());
            JOptionPane.showMessageDialog(new JFrame(), "Verifique si el escanner se encuentra encedido\no conectado a la computadora...",
                    "Alerta... " + metadata.getException().getMessage(), JOptionPane.WARNING_MESSAGE);

        }
    }

    private void codificar(final File archivo, final boolean scan) {
        final MostrarProceso proceso = new MostrarProceso("Codificando el documento");
        proceso.start();
        new Thread(new Runnable() {
            @Override
            public void run() {

                codificando(archivo, scan);
                proceso.detener();
            }
        }).start();
    }

    private void codificando(File archivo, boolean scan) {
        String ext = "", file;

        Principal.desktop.removeAll();

        traza.trace("documento escaneado " + scan, Level.INFO);
        traza.trace("versionar el documento " + versionar, Level.INFO);
        traza.trace("accion a realizar en el documento " + accion, Level.INFO);

        try {

//            if(new File(new ToolsFiles().getArchivoTif()).exists()){
            if (archivo != null) {

                file = archivo.getName();
                traza.trace("archivo seleccionado " + file, Level.INFO);
                int index = file.indexOf(".");
                if (index != -1) {
                    ext = file.substring(file.lastIndexOf('.') + 1);
                    //ext = file.substring(index+1);
                    infoDocumento.setFormato(ext);
                    traza.trace("extencion del archivo " + ext, Level.INFO);
                }

                toolsTiff.codificar(archivo);
            } else {
                toolsTiff.codificar();
            }

            if ((ext.equalsIgnoreCase("tiff") || ext.equalsIgnoreCase("tif")) || scan) {

                if (scan) {
                    infoDocumento.setFormato("tif");
                }

                if (versionar) {
                    if (scan) {
                        GuardarDoc gd = new GuardarDoc(accion, infoDocumento, new ToolsFiles().getArchivoTif());
                        Principal.desktop.add(gd);
                        gd.toFront();
                    } else {
                        GuardarDoc gd = new GuardarDoc(accion, infoDocumento, archivo.getPath());
                        Principal.desktop.add(gd);
                        gd.toFront();
                    }
                } else {
                    if (scan) {
                        GuardarDoc gd = new GuardarDoc(accion, infoDocumento, new ToolsFiles().getArchivoTif());
                        Principal.desktop.add(gd);
                        gd.toFront();
                    } else {
                        GuardarDoc gd = new GuardarDoc(accion, infoDocumento, archivo.getPath());
                        Principal.desktop.add(gd);
                        gd.toFront();
                    }
                }
            } else if (ext.equalsIgnoreCase("pdf")) {
                if (versionar) {
                    VerDocumentoPDF vdpdf = new VerDocumentoPDF(accion, infoDocumento, archivo, true, false, 0);
                    Principal.desktop.add(vdpdf);
                    vdpdf.toFront();
                } else {
                    VerDocumentoPDF vdpdf = new VerDocumentoPDF(accion, infoDocumento, archivo, true, false, 0);
                    Principal.desktop.add(vdpdf);
                    vdpdf.toFront();
                }
            } else {

                if (versionar) {
                    VerImagenes vi = new VerImagenes(accion, infoDocumento, archivo, versionar, 0, false);
                    Principal.desktop.add(vi);
                    vi.toFront();
                } else {
                    VerImagenes vi = new VerImagenes(accion, infoDocumento, archivo, true, 0, false);
                    Principal.desktop.add(vi);
                    vi.toFront();
                }
            }
//            }else{
//                dispose();
//            }

        } catch (Exception e) {
            traza.trace("error codificando el archivo", Level.ERROR, e);
            JOptionPane.showMessageDialog(new JFrame(), "Error en el proceso de codificación del documento", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        }

    }

    public void buscarArchivo() {

        JFileChooser fc = new JFileChooser();
        String ext = "", file;
        traza.trace("buscando un archivo digitalizado", Level.INFO);

        fc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //fc.addChoosableFileFilter(new FileFilterImagenes());
        //fc.addChoosableFileFilter(new FileFilterTiff());
        fc.addChoosableFileFilter(new FiltroArchivo("Documentos"));
        fc.setAcceptAllFileFilterUsed(false);
        //fc.setFileView(new ImageFileView());
        fc.setAccessory(new ImagePreview(fc));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnVal = fc.showDialog(new JFrame(), "Aceptar");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            codificar(fc.getSelectedFile(), false);
        }
    }

    public void escanearDocumento() {

        try {

            if (scanner.isAPIInstalled()) {
                scanner.acquire();
            }

        } catch (ScannerIOException ex) {
            traza.trace("Problemas con el Escaner", Level.ERROR, ex);
            JOptionPane.showMessageDialog(new JFrame(), "Problemas con el Escaner\n" + ex.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);

        }

    }

    public void seleccionarEscaner() {

        try {
            scanner.select();
        } catch (SaneIOException ex) {

            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(),
                    "Alerta...", JOptionPane.ERROR_MESSAGE);
        } catch (ScannerIOException ex) {

            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(),
                    "Alerta...", JOptionPane.ERROR_MESSAGE);
        }

    }

}
