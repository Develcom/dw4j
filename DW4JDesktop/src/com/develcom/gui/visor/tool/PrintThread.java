/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.visor.tool;

import com.sun.pdfview.PDFPrintPage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author develcom
 */
public class PrintThread extends Thread {

    PDFPrintPage ptPages;
    PrinterJob ptPjob;

    public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
        ptPages = pages;
        ptPjob = pjob;
        setName(getClass().getName());
    }

    @Override
    public void run() {
        try {
            ptPages.show(ptPjob);
            ptPjob.print();
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Printing Error: " + pe.getMessage(),
                    "Print Aborted",
                    JOptionPane.ERROR_MESSAGE);
        }
        ptPages.hide();
    }
}
