/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.visor.tool;

import com.develcom.gui.visor.VerDocumentoPDF;
import com.sun.pdfview.PDFPage;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import org.apache.log4j.Level;
import com.develcom.tools.trazas.Traza;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PagePanel;

/**
 *
 * @author develcom
 */
//public class PreparaPagina {
public class PreparaPagina extends Thread {
//public class PreparaPagina implements Runnable {

    private Traza traza = new Traza(PreparaPagina.class);
    private int waitforPage;
    private int prepPage;
    private VerDocumentoPDF verDocPDF;
    private PagePanel panelPagina;
    private PDFFile archivoPDF;
    private int paginaActual = -1;

    /**
     * Creates a new PreparaPagina to prepare the page after the current one.
     *
     * @param waitforPage the current page number, 0 based
     * @param verDocPDF
     */
    public PreparaPagina(int waitforPage, VerDocumentoPDF verDocPDF) {
        this.verDocPDF = verDocPDF;
        panelPagina = verDocPDF.getPanelPagina();
        archivoPDF = verDocPDF.getArchivoPDF();
        this.paginaActual = verDocPDF.getPaginaActual();
        traza.trace("verDocPDF " + verDocPDF, Level.INFO);
        setDaemon(true);
        setName(getClass().getName());

        this.waitforPage = waitforPage;
        this.prepPage = waitforPage + 1;
    }

    public void quit() {
        waitforPage = -1;
    }

    @Override
    public void run() {
        Dimension size = null;
        Rectangle2D clip = null;

        traza.trace("panel pagina " + panelPagina, Level.INFO);
        traza.trace("waitforPage " + waitforPage, Level.INFO);
        traza.trace("Pagina Actual " + paginaActual, Level.INFO);

        if (verDocPDF.getAcciones().getFspp() != null) {
            verDocPDF.getAcciones().getFspp().waitForCurrentPage();
            size = verDocPDF.getAcciones().getFspp().getCurSize();
            clip = verDocPDF.getAcciones().getFspp().getCurClip();
        } else if (panelPagina != null) {
            panelPagina.waitForCurrentPage();
            size = panelPagina.getCurSize();
            clip = panelPagina.getCurClip();
        }

        traza.trace("size " + size, Level.INFO);
        traza.trace("clip " + clip, Level.INFO);

        if (waitforPage == paginaActual) {
            // don't go any further if the user changed pages.
            traza.trace("Preparer generating page " + (prepPage + 2), Level.INFO);

            PDFPage pdfPage = archivoPDF.getPage(prepPage + 1, true);

            traza.trace("pdfPage " + pdfPage, Level.INFO);

            if (pdfPage != null && waitforPage == paginaActual) {
                traza.trace("numero de pagina " + pdfPage.getPageNumber(), Level.INFO);
                traza.trace("Generating image for page " + (prepPage + 2), Level.INFO);

                // don't go any further if the user changed pages
//                                        System.out.println("Generating image for page " + (prepPage + 2));
                pdfPage.getImage(size.width, size.height, clip, null, true, true);
//                		    System.out.println("Generated image for page "+ (prepPage+2));
            }
        }
    }
}
