/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.visor.tool;

import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.trazas.Traza;
import com.sun.pdfview.FullScreenWindow;
import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPrintPage;
import com.sun.pdfview.PageChangeListener;
import com.sun.pdfview.PagePanel;
import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Acciones implements PageChangeListener, ActionListener, TreeSelectionListener, KeyListener {

    private FullScreenWindow fullScreen;
    /**
     * The full screen page display, or null if not in full screen mode
     */
    private PagePanel fspp;

    private Traza traza = new Traza(Acciones.class);

    private PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
    private PageBuilder pb = new PageBuilder(this);
    private VerDocumentoPDF verDocPDF;

    public Acciones(VerDocumentoPDF verDocPDF) {
        this.verDocPDF = verDocPDF;
        traza.trace("verDocPDF " + verDocPDF, Level.INFO);
    }

    /**
     * Print the current document.
     */
    public void doPrint() {

        ToolsFiles toolsFile = new ToolsFiles();

        traza.trace("imprimir el pdf ", Level.INFO);
        PrinterJob pjob = PrinterJob.getPrinterJob();
        File archivo;
        String root, fileName, file;

        try {

            pjob.setJobName(verDocPDF.getNombreDocumento());
            Book book = new Book();
            PDFPrintPage pages = new PDFPrintPage(verDocPDF.getArchivoPDF());

//            pages.printFile(toolsFile.getTempPath() + verDocPDF.getFilePDF().getName(), false);
            archivo = verDocPDF.getFilePDF();
            root = toolsFile.getTempPath();
            fileName = archivo.getName();
            file = root + fileName;
            traza.trace("ruta " + root, Level.INFO);
            traza.trace("nombre del archivo " + fileName, Level.INFO);
            traza.trace("archivo " + file, Level.INFO);

//            pages.printFile(file, false);
            book.append(pages, pformat, verDocPDF.getArchivoPDF().getNumPages());

            pjob.setPageable(book);
            if (pjob.printDialog()) {
                new PrintThread(pages, pjob).start();
            }
        } catch (Exception ex) {
            traza.trace("error al imprimir", Level.ERROR, ex);
        }
    }

//    public Action firstAction = new AbstractAction("", getIcon("../gfx/first.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doFirst();
//        }
//    };
//    public Action prevAction = new AbstractAction("", getIcon("../gfx/prev.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doPrev();
//        }
//    };
//    public Action nextAction = new AbstractAction("", getIcon("../gfx/next.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doNext();
//        }
//    };
//    public Action lastAction = new AbstractAction("", getIcon("../gfx/last.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doLast();
//        }
//    };
//    public Action zoomToolAction = new AbstractAction("", getIcon("../gfx/zoom.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doZoomTool();
//        }
//    };
//    public Action fitInWindowAction = new AbstractAction("Fit in window", getIcon("../gfx/fit.gif")) {
//
//                @Override
//        public void actionPerformed(ActionEvent evt) {
//            doFitInWindow();
//        }
//    };
    /**
     * Turns on zooming
     */
    public void doZoomTool() {
//        if (verDocPDF.fspp == null) {
        verDocPDF.getPanelPagina().useZoomTool(true);
//        }
    }

    /**
     * Turns off zooming; makes the page fit in the window
     */
    public void doFitInWindow() {
//        if (verDocPDF.fspp == null) {
        verDocPDF.getPanelPagina().useZoomTool(false);
        verDocPDF.getPanelPagina().setClip(null);
//        }
    }

//    public Action printAction = new AbstractAction("", getIcon("../gfx/print.gif")) {
//
//        @Override
//        public void actionPerformed(ActionEvent evt) {
//            doPrint();
//        }
//    };
    /**
     * utility method to get an icon from the resources of this class
     *
     * @param name the name of the icon
     * @return the icon, or null if the icon wasn't found.
     */
    public Icon getIcon(String name) {
        Icon icon = null;
        URL url;
        try {
            url = getClass().getResource(name);

            icon = new ImageIcon(url);
            if (icon == null) {
                System.out.println("Couldn't find " + url);
            }
        } catch (Exception e) {
            traza.trace("icono no encontrado", Level.ERROR, e);
//            System.out.println("Couldn't find " + getClass().getName() + "/" + name);
//            e.printStackTrace();
        }
        return icon;
    }

    @Override
    public void gotoPage(int pagenum) {
        traza.trace("gotoPage pagenum " + pagenum, Level.INFO);

        if (pagenum < 0) {
            pagenum = 0;
        } else if (pagenum >= verDocPDF.getArchivoPDF().getNumPages()) {
            pagenum = verDocPDF.getArchivoPDF().getNumPages() - 1;
        }

        traza.trace("gotoPage pagenum " + pagenum, Level.INFO);
        verDocPDF.forceGotoPage(pagenum);
    }

    /**
     * Goes to the next page
     */
    public void doNext() {
        gotoPage(verDocPDF.getPaginaActual() + 1);
    }

    /**
     * Goes to the previous page
     */
    public void doPrev() {
        gotoPage(verDocPDF.getPaginaActual() - 1);
    }

    /**
     * Goes to the first page
     */
    public void doFirst() {
        gotoPage(0);
    }

    /**
     * Goes to the last page
     */
    public void doLast() {
        gotoPage(verDocPDF.getArchivoPDF().getNumPages() - 1);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        doPageTyped();
    }

    /**
     * Goes to the page that was typed in the page number text field
     */
    public void doPageTyped() {
        int pagenum = -1;
        try {
            pagenum = Integer.parseInt(verDocPDF.getPagina().getText()) - 1;
            traza.trace("doPageTyped pagenum " + pagenum, Level.INFO);
        } catch (NumberFormatException nfe) {
        }
        if (pagenum >= verDocPDF.getArchivoPDF().getNumPages()) {
            pagenum = verDocPDF.getArchivoPDF().getNumPages() - 1;
            traza.trace("doPageTyped obteniendo el numero de paginas " + pagenum, Level.INFO);
        }
        if (pagenum >= 0) {
            if (pagenum != verDocPDF.getPaginaActual()) {

                traza.trace("doPageTyped vamos a la pagina " + pagenum, Level.INFO);
                gotoPage(pagenum);
            }
        } else {
            verDocPDF.getPagina().setText(String.valueOf(verDocPDF.getPaginaActual()));
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.isAddedPath()) {
            OutlineNode node = (OutlineNode) e.getPath().getLastPathComponent();
            if (node == null) {
                return;
            }

            try {
                PDFAction action = node.getAction();
                if (action == null) {
                    return;
                }

                if (action instanceof GoToAction) {
                    PDFDestination dest = ((GoToAction) action).getDestination();
                    if (dest == null) {
                        return;
                    }

                    PDFObject page1;
                    page1 = dest.getPage();
                    if (page1 == null) {
                        return;
                    }

                    int pageNum = verDocPDF.getArchivoPDF().getPageNumber(page1);
                    if (pageNum >= 0) {
                        gotoPage(pageNum);
                    }
                }
            } catch (IOException ioe) {
                traza.trace("Error al leer el archivo ", Level.ERROR, ioe);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key >= '0' && key <= '9') {
            int val = key - '0';
            pb.keyTyped(val);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT) {
            doPrev();
        } else if (code == KeyEvent.VK_RIGHT) {
            doNext();
        } else if (code == KeyEvent.VK_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_HOME) {
            doFirst();
        } else if (code == KeyEvent.VK_END) {
            doLast();
        } else if (code == KeyEvent.VK_PAGE_UP) {
            doPrev();
        } else if (code == KeyEvent.VK_PAGE_DOWN) {
            doNext();
        } else if (code == KeyEvent.VK_SPACE) {
            doNext();
        } else if (code == KeyEvent.VK_ESCAPE) {
            setFullScreenMode(false, false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doFullScreen(boolean force) {
        setFullScreenMode(fullScreen == null, force);
    }

    public void setFullScreenMode(boolean full, boolean force) {
        //	curpage= -1;
        if (full && fullScreen == null) {
            fullScreenAction.setEnabled(false);
            new Thread(new PerformFullScreenMode(force),
                    getClass().getName() + ".setFullScreenMode").start();
            verDocPDF.getFullScreem().setSelected(true);
        } else if (!full && fullScreen != null) {
            fullScreen.close();
            fullScreen = null;
//            verDocPDF.setPanelPagina(null);
            gotoPage(verDocPDF.getPaginaActual());
            verDocPDF.getFullScreem().setSelected(false);
        }
    }

    public Action fullScreenAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            doFullScreen((evt.getModifiers() & ActionEvent.SHIFT_MASK) != 0);
        }
    };

    /**
     * Runs the FullScreenMode change in another thread
     */
    class PerformFullScreenMode implements Runnable {

        boolean force;

        public PerformFullScreenMode(boolean forcechoice) {
            force = forcechoice;
        }

        @Override
        public void run() {
            fspp = new PagePanel();
            fspp.setBackground(Color.black);
            verDocPDF.getPanelPagina().showPage(null);
            fullScreen = new FullScreenWindow(fspp, force);
            fspp.addKeyListener(Acciones.this);
            gotoPage(verDocPDF.getPaginaActual());
            fullScreenAction.setEnabled(true);
        }
    }

    public PagePanel getFspp() {
        return fspp;
    }
    
}
