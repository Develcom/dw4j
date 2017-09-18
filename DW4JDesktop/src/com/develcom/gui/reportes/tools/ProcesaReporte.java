/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.reportes.tools;

import ar.com.fdvs.dj.core.registration.EntitiesRegistrationException;
import com.develcom.dao.Campos;
import com.develcom.dao.Mensajes;
import com.develcom.tools.trazas.Traza;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class ProcesaReporte {

    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(ProcesaReporte.class);

    public void crearReporte(String tituloReporte, String query, String lib, String cat, List<Campos> listaCampos, String fechas) {

        try {

            ReporteDinamico rd = new ReporteDinamico();

            JasperPrint jasperPrint = rd.crearReporteDinamico(tituloReporte, query, listaCampos, lib, cat, fechas);

            JasperViewer frame = new JasperViewer(jasperPrint, false);
            frame.setTitle(tituloReporte + " - " + Mensajes.getMensaje());
//            frame.setAlwaysOnTop(true);
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.toFront();

        } catch (EntitiesRegistrationException e) {
            traza.trace("error general al construir el reporte dinamico", Level.ERROR, e);
        }
        //return jasperPrint;
    }

    public void crearReporte(String plantilla, Map params, String tituloReporte) {

        JasperReport jasperReport;
        JasperPrint jasperPrint;

        try {

            traza.trace("plantilla " + plantilla, Level.INFO);
            Collection valores = params.values();
            Iterator it = valores.iterator();

            while (it.hasNext()) {
                traza.trace("valor del parametro " + it.next().toString(), Level.INFO);
            }

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream fileReport = cl.getResourceAsStream("com/develcom/gui/reportes/plantillas/" + plantilla);

            jasperReport = JasperCompileManager.compileReport(fileReport);
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, new BaseDato().conectar());

            JasperViewer frame = new JasperViewer(jasperPrint, false);
            frame.setTitle(tituloReporte + " - " + Mensajes.getMensaje());
            //frame.setAlwaysOnTop(true);
//            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.toFront();

        } catch (JRException e) {
            traza.trace("error al construir el reporte", Level.ERROR, e);
        } catch (SQLException ex) {
            traza.trace("error de coneccion con la base de datos", Level.ERROR, ex);
        } catch (ClassNotFoundException ex) {
            traza.trace("error el en driver oracle", Level.ERROR, ex);
        }
    }

}
