/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.visor.tool;

import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.tools.trazas.Traza;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class ThumbAction extends AbstractAction implements PropertyChangeListener {

    private Traza traza = new Traza(ThumbAction.class);
    private VerDocumentoPDF verDocPDF;
    private boolean isOpen = true;

    public ThumbAction(VerDocumentoPDF verDocPDF) {
        super("Hide thumbnails");
        this.verDocPDF = verDocPDF;
        traza.trace("verDocPDF " + verDocPDF, Level.INFO);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        int v = ((Integer) evt.getNewValue()).intValue();

        traza.trace("valor PropertyChangeEvent "+v, Level.INFO);

        if (v <= 1) {
            isOpen = false;
            putValue(ACTION_COMMAND_KEY, "Muestra thumbnails");
            putValue(NAME, "Muestra thumbnails");
        } else {
            isOpen = true;
            putValue(ACTION_COMMAND_KEY, "Oculta thumbnails");
            putValue(NAME, "Oculta thumbnails");
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        traza.trace("isOpen "+isOpen, Level.INFO);

        if (!isOpen) {
            verDocPDF.getSplit().setDividerLocation((int) verDocPDF.getThumbs().getPreferredSize().width + (int) verDocPDF.getThumbsScroll().getVerticalScrollBar().getWidth() + 4);
        } else {
            verDocPDF.getSplit().setDividerLocation(0);
        }


        doThumbs(!isOpen);
    }

    /**
     * Shows or hides the thumbnails by moving the split pane divider
     */
    public void doThumbs(boolean show) {
        traza.trace("mostrar thumb " + show, Level.INFO);
        if (show) {
            verDocPDF.getSplit().setDividerLocation((int) verDocPDF.getThumbs().getPreferredSize().width + (int) verDocPDF.getThumbsScroll().getVerticalScrollBar().getWidth() + 4);
        } else {
            verDocPDF.getSplit().setDividerLocation(0);
        }
    }
}
