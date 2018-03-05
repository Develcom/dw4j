/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.reportes.tools;

import com.develcom.administracion.Indice;
import com.develcom.dao.Campos;
import com.develcom.dao.Expediente;
import com.develcom.tools.trazas.Traza;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.expediente.BuscaIndice;

/**
 *
 * @author develcom
 */
public class ConstruyeConsultas {

    private Traza traza = new Traza(ConstruyeConsultas.class);

    public final static int DOCUMENTO_A_VENCERSE = 0;
    public final static int DOCUMENTO_VENCIDOS = 1;
    public final static int CANTIDAD_DOCUMENTO_INDEXADOS_POR_INDEXADOR = 2;
    public final static int CRECIMIENTO_INTERMENSUAL_DE_DOCUMENTOS = 3;
    public final static int DOCUMENTOS_RECHAZADOS = 4;
    public final static int DOCUMENTO_PENDIENTE_POR_APROBAR = 5;
    public final static int CAUSA_DE_RECHAZO = 6;
    public final static int DOCUMENTO_INDEXADOS_RECHAZADO_APROBADO_PENDIENTE = 7;
    public final static int TIPO_PERSONAL = 8;
    public final static int EXPEDIENTE_ESTATUS = 9;
    public final static int DOCUMENTO_ELIMINADO = 10;

    private String indice1;
    private String indice2;
    private String indice3;
    private String indice4;
    private List<Campos> listaCampos = new ArrayList<Campos>();
    private int reporteSeleccionado;

    public String crearQuery(int reporte, UtilidadVentanaReportes uvr, Expediente expediente) {

        boolean report;
        String query = null;
        ve.com.develcom.foliatura.Foliatura foliatura = new ve.com.develcom.foliatura.Foliatura();

        traza.trace("libreria " + expediente.getLibreria() + " categoria " + expediente.getCategoria(), Level.INFO);
        reporteSeleccionado = reporte;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            buscarCamposPrincipales(expediente);

            if (reporte == DOCUMENTO_A_VENCERSE) {
                traza.trace("construyendo el reporte " + reporte + " dias " + uvr.getDias(), Level.INFO);

                query = " SELECT distinct d.tipo_documento documento, i.FECHA_VENCIMIENTO, t.expediente, t.valor, t.fecha_indice, a.*"
                        + " FROM TIPODOCUMENTO d INNER JOIN INFODOCUMENTO i"
                        + " ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                        + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                        + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                        + " INNER JOIN EXPEDIENTES t"
                        + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                        + " INNER JOIN INDICES a ON a.ID_INDICE=t.ID_INDICE"
                        + " WHERE i.FECHA_VENCIMIENTO IS NOT null"
                        + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                        + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                        + " AND i.FECHA_VENCIMIENTO BETWEEN current_date and current_date + " + uvr.getDias() + ""
                        + " ORDER BY d.tipo_documento, a.id_indice ASC";

                report = foliatura.generarReporteDinamico(expediente.getIdCategoria(), query);

                if (report) {
                    query = " SELECT distinct d.tipo_documento documento, i.FECHA_VENCIMIENTO, a.*"
                            + " FROM TIPODOCUMENTO d INNER JOIN INFODOCUMENTO i"
                            + " ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                            + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                            + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                            + " INNER JOIN EXPEDIENTES t"
                            + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                            + " INNER JOIN reporte a ON a.expediente=t.EXPEDIENTE"
                            + " WHERE i.FECHA_VENCIMIENTO IS NOT null"
                            + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                            + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                            + " AND i.FECHA_VENCIMIENTO BETWEEN current_date and current_date + " + uvr.getDias() + ""
                            + " ORDER BY d.tipo_documento ASC";
                }

            } else if (reporte == DOCUMENTO_VENCIDOS) {

                Calendar desde = uvr.getFechaDesde().getCalendar();
                Calendar hasta = uvr.getFechaHasta().getCalendar();

                traza.trace("construyendo el reporte " + reporte, Level.INFO);
                traza.trace("fecha desde " + sdf.format(desde.getTime()), Level.INFO);
                traza.trace("fecha hasta " + sdf.format(hasta.getTime()), Level.INFO);

                query = " SELECT distinct d.tipo_documento documento, i.FECHA_VENCIMIENTO, t.expediente, t.valor, t.fecha_indice, a.*"
                        + " FROM TIPODOCUMENTO d"
                        + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                        + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                        + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                        + " INNER JOIN EXPEDIENTES t"
                        + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                        + " INNER JOIN INDICES a ON a.ID_INDICE=t.ID_INDICE"
                        + " WHERE i.FECHA_VENCIMIENTO IS NOT null"
                        + " AND i.ESTATUS_DOCUMENTO=1"
                        + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                        + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                        + " AND i.FECHA_VENCIMIENTO  BETWEEN TO_DATE('" + sdf.format(desde.getTime()) + "', 'dd/MM/yyyy')"
                        + " AND TO_DATE('" + sdf.format(hasta.getTime()) + "', 'dd/MM/yyyy')"
                        + " ORDER BY d.tipo_documento ASC ";

                report = foliatura.generarReporteDinamico(expediente.getIdCategoria(), query);

                if (report) {
                    query = " SELECT distinct d.tipo_documento documento, i.FECHA_VENCIMIENTO, a.*"
                            + " FROM TIPODOCUMENTO d"
                            + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                            + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                            + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                            + " INNER JOIN EXPEDIENTES t"
                            + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                            + " INNER JOIN reporte a ON a.expediente=t.EXPEDIENTE"
                            + " WHERE i.FECHA_VENCIMIENTO IS NOT null"
                            + " AND i.ESTATUS_DOCUMENTO=1"
                            + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                            + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                            + " AND i.FECHA_VENCIMIENTO  BETWEEN TO_DATE('" + sdf.format(desde.getTime()) + "', 'dd/MM/yyyy')"
                            + " AND TO_DATE('" + sdf.format(hasta.getTime()) + "', 'dd/MM/yyyy')"
                            + " ORDER BY d.tipo_documento ASC ";
                }

            } else if (reporte == DOCUMENTOS_RECHAZADOS) {

                Calendar desde = uvr.getFechaDesde().getCalendar();
                Calendar hasta = uvr.getFechaHasta().getCalendar();

                traza.trace("construyendo el reporte " + reporte, Level.INFO);
                traza.trace("fecha desde " + sdf.format(desde.getTime()), Level.INFO);
                traza.trace("fecha hasta " + sdf.format(hasta.getTime()), Level.INFO);

                query = " SELECT distinct d.tipo_documento documento, di.FECHA_RECHAZO,"
                        + " di.USUARIO_RECHAZO, di.CAUSA_RECHAZO, di.FECHA_DIGITALIZACION,"
                        + " di.USUARIO_DIGITALIZO,  t.expediente, t.valor, t.fecha_indice, a.*"
                        + " FROM TIPODOCUMENTO d"
                        + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                        + " INNER JOIN DATOS_INFODOCUMENTO di ON di.ID_INFODOCUMENTO=i.ID_INFODOCUMENTO"
                        + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                        + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                        + " INNER JOIN EXPEDIENTES t"
                        + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                        + " INNER JOIN INDICES a ON a.ID_INDICE=t.ID_INDICE"
                        + " WHERE di.FECHA_RECHAZO BETWEEN TO_DATE('" + sdf.format(desde.getTime()) + "', 'dd/MM/yyyy')"
                        + " AND TO_DATE('" + sdf.format(hasta.getTime()) + "', 'dd/MM/yyyy')"
                        + " AND i.RE_DIGITALIZADO = '0'"
                        + " AND i.ESTATUS_DOCUMENTO = 2"
                        + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                        + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                        + " ORDER BY d.tipo_documento ASC ";

                report = foliatura.generarReporteDinamico(expediente.getIdCategoria(), query);

                if (report) {
                    query = " SELECT distinct d.tipo_documento documento, di.FECHA_RECHAZO,"
                            + " di.USUARIO_RECHAZO, di.CAUSA_RECHAZO, di.FECHA_DIGITALIZACION,"
                            + " di.USUARIO_DIGITALIZO, a.*"
                            + " FROM TIPODOCUMENTO d"
                            + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                            + " INNER JOIN DATOS_INFODOCUMENTO di ON di.ID_INFODOCUMENTO=i.ID_INFODOCUMENTO"
                            + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                            + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                            + " INNER JOIN EXPEDIENTES t"
                            + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                            + " INNER JOIN reporte a ON a.expediente=t.EXPEDIENTE"
                            + " WHERE di.FECHA_RECHAZO BETWEEN TO_DATE('" + sdf.format(desde.getTime()) + "', 'dd/MM/yyyy')"
                            + " AND TO_DATE('" + sdf.format(hasta.getTime()) + "', 'dd/MM/yyyy')"
                            + " AND i.RE_DIGITALIZADO = '0'"
                            + " AND i.ESTATUS_DOCUMENTO = 2"
                            + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                            + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                            + " ORDER BY d.tipo_documento ASC ";
                }

            } else if (reporte == DOCUMENTO_PENDIENTE_POR_APROBAR) {

                query = " SELECT distinct d.tipo_documento documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION,"
                        + " di.USUARIO_DIGITALIZO, t.expediente, t.valor, t.fecha_indice, a.*,"
                        + " count(d.tipo_documento) AS cantidad_documentos"
                        + " FROM TIPODOCUMENTO d"
                        + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                        + " INNER JOIN DATOS_INFODOCUMENTO di ON di.ID_INFODOCUMENTO=i.ID_INFODOCUMENTO"
                        + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                        + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                        + " INNER JOIN EXPEDIENTES t"
                        + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                        + " INNER JOIN INDICES a ON a.ID_INDICE=t.ID_INDICE"
                        + " WHERE i.ESTATUS_DOCUMENTO = 0"
                        + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                        + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                        + " GROUP BY d.tipo_documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION, a.id_categoria, t.expediente,"
                        //+ " a." + indice1.toLowerCase().replace(" ", "_") + ", a." + indice2.toLowerCase().replace(" ", "_") + ","
                        //+ " a." + indice3.toLowerCase().replace(" ", "_") + ", a." + indice4.toLowerCase().replace(" ", "_") + ","
                        + " t.valor, t.fecha_indice, a.id_indice, di.USUARIO_DIGITALIZO"
                        + " ORDER BY d.tipo_documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION, a.id_indice ASC";

                report = foliatura.generarReporteDinamico(expediente.getIdCategoria(), query);

                if (report) {
                    query = " SELECT distinct d.tipo_documento documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION,"
                            + " di.USUARIO_DIGITALIZO, a.*,"
                            + " count(d.tipo_documento) AS cantidad_documentos"
                            + " FROM TIPODOCUMENTO d"
                            + " INNER JOIN INFODOCUMENTO i ON d.ID_DOCUMENTO = i.ID_DOCUMENTO"
                            + " INNER JOIN DATOS_INFODOCUMENTO di ON di.ID_INFODOCUMENTO=i.ID_INFODOCUMENTO"
                            + " INNER JOIN CATEGORIA c ON d.ID_CATEGORIA = c.ID_CATEGORIA"
                            + " INNER JOIN LIBRERIA l ON c.ID_LIBRERIA = l.ID_LIBRERIA"
                            + " INNER JOIN EXPEDIENTES t"
                            + " ON (c.ID_CATEGORIA = t.ID_CATEGORIA AND t.EXPEDIENTE = i.ID_EXPEDIENTE)"
                            + " INNER JOIN reporte a ON a.expediente=t.EXPEDIENTE"
                            + " WHERE i.ESTATUS_DOCUMENTO = 0"
                            + " AND d.ID_CATEGORIA=" + expediente.getIdCategoria() + ""
                            + " AND c.ID_LIBRERIA=" + expediente.getIdLibreria() + ""
                            + " GROUP BY d.tipo_documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION, a.id_categoria, a.expediente,"
                            + " a." + indice1.toLowerCase().replace(" ", "_") + ", a." + indice2.toLowerCase().replace(" ", "_") + ","
                            + " a." + indice3.toLowerCase().replace(" ", "_") + ", a." + indice4.toLowerCase().replace(" ", "_") + ", di.USUARIO_DIGITALIZO"
                            + " ORDER BY d.tipo_documento, i.NUMERO_DOCUMENTO, di.FECHA_DIGITALIZACION ASC";
                }

            }
        } catch (SOAPException ex) {
            traza.trace("probelmas al general el reporte dinamico", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("problemas al general el reporte dicnamico", Level.ERROR, ex);
        }

        traza.trace("query \n " + query, Level.INFO);
        return query;
    }

    private void buscarCamposPrincipales(Expediente expediente) {

        List<Indice> listaIndices;

        Campos campos;

        try {

            listaIndices = new BuscaIndice().buscarIndice(expediente.getIdCategoria());

            if (reporteSeleccionado == DOCUMENTOS_RECHAZADOS) {
                campos = new Campos();
                campos.setNombre("documento");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("FECHA_RECHAZO");
                campos.setTipoDato(Campos.FECHA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("USUARIO_RECHAZO");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("CAUSA_RECHAZO");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("FECHA_DIGITALIZACION");
                campos.setTipoDato(Campos.FECHA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("USUARIO_DIGITALIZO");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
            } else if (reporteSeleccionado == DOCUMENTO_PENDIENTE_POR_APROBAR) {
                campos = new Campos();
                campos.setNombre("documento");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("NUMERO_DOCUMENTO");
                campos.setTipoDato(Campos.NUMERO);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("FECHA_DIGITALIZACION");
                campos.setTipoDato(Campos.FECHA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("USUARIO_DIGITALIZO");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
            } else {
                campos = new Campos();
                campos.setNombre("documento");
                campos.setTipoDato(Campos.CADENA);
                listaCampos.add(campos);
                campos = new Campos();
                campos.setNombre("FECHA_VENCIMIENTO");
                campos.setTipoDato(Campos.FECHA);
                listaCampos.add(campos);
            }

            for (Indice indices : listaIndices) {

                if (indices.getTipo().equalsIgnoreCase("fecha")) {
                    if (indices.getClave().equalsIgnoreCase("y")) {
                        campos = new Campos();
                        indice1 = indices.getIndice().replace(" ", "_");
                        campos.setNombre(indice1);
                        campos.setTipoDato(Campos.FECHA);
                        traza.trace("indice 1 " + indice1, Level.INFO);
                        listaCampos.add(campos);
                    } else if (indices.getClave().equalsIgnoreCase("s")) {
                        if (indice2 == null) {
                            campos = new Campos();
                            indice2 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice2);
                            campos.setTipoDato(Campos.FECHA);
                            traza.trace("indice 2 " + indice2, Level.INFO);
                            listaCampos.add(campos);
                        } else if (indice3 == null) {
                            campos = new Campos();
                            indice3 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice3);
                            campos.setTipoDato(Campos.FECHA);
                            traza.trace("indice 3 " + indice3, Level.INFO);
                            listaCampos.add(campos);
                        } else if (indice4 == null) {
                            campos = new Campos();
                            indice4 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice4);
                            campos.setTipoDato(Campos.FECHA);
                            traza.trace("indice 4 " + indice4, Level.INFO);
                            listaCampos.add(campos);
                        }
                    }
                } else {
                    if (indices.getClave().equalsIgnoreCase("y")) {
                        campos = new Campos();
                        indice1 = indices.getIndice().replace(" ", "_");
                        campos.setNombre(indice1);
                        campos.setTipoDato(Campos.CADENA);
                        traza.trace("indice 1 " + indice1, Level.INFO);
                        listaCampos.add(campos);
                    } else if (indices.getClave().equalsIgnoreCase("s")) {
                        if (indice2 == null) {
                            campos = new Campos();
                            indice2 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice2);
                            campos.setTipoDato(Campos.CADENA);
                            traza.trace("indice 2 " + indice2, Level.INFO);
                            listaCampos.add(campos);
                        } else if (indice3 == null) {
                            campos = new Campos();
                            indice3 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice3);
                            campos.setTipoDato(Campos.CADENA);
                            traza.trace("indice 3 " + indice3, Level.INFO);
                            listaCampos.add(campos);
                        } else if (indice4 == null) {
                            campos = new Campos();
                            indice4 = indices.getIndice().replace(" ", "_");
                            campos.setNombre(indice4);
                            campos.setTipoDato(Campos.CADENA);
                            traza.trace("indice 4 " + indice4, Level.INFO);
                            listaCampos.add(campos);
                        }
                    }
                }
            }

            if (reporteSeleccionado == DOCUMENTO_PENDIENTE_POR_APROBAR) {

                campos = new Campos();
                campos.setNombre("cantidad_documentos");
                campos.setTipoDato(Campos.NUMERO);
                listaCampos.add(campos);

            }

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }
    }

//    public String getQuery() {
//        return query;
//    }
    public List<Campos> getListaCampos() {
        return listaCampos;
    }

}
