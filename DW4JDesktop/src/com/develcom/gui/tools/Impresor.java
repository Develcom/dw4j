/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.tools;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import org.apache.log4j.Level;
import uk.co.mmscomputing.application.imageviewer.ImagePanel;
import com.develcom.tools.trazas.Traza;

/**
 *
 * @author develcom
 */
public class Impresor {

    private Traza traza = new Traza(Impresor.class);

    private PrinterJob    printerJob;
    private PageFormat    pageFormat;
    private Book          book;
    

    public Impresor(){
      printerJob=PrinterJob.getPrinterJob();

      pageFormat = printerJob.defaultPage();
      Paper paper=pageFormat.getPaper();
      paper.setImageableArea(0.0,0.0,paper.getWidth(),paper.getHeight());
      pageFormat.setPaper(paper);
      pageFormat = printerJob.validatePage(pageFormat);

      book=new Book();
    }

    public void append(ImagePanel image){
      book.append(image, pageFormat);
    }

    public void print(){
      printerJob.setPageable(book);
      if(printerJob.printDialog()){
        try{
          printerJob.print();
        }catch (Exception e){
          traza.trace("error durante la impresion", Level.ERROR, e);
          traza.trace("9\b"+getClass().getName()+".print:\n\t"+e.getMessage(), Level.ERROR);
        }
      }
    }

}
