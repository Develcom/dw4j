/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.reportes.tools;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import java.awt.Color;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.log4j.Level;
import com.develcom.dao.Campos;
import com.develcom.tools.trazas.Traza;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author develcom
 */
public class ReporteDinamico {

    private Traza traza = new Traza(ReporteDinamico.class);

    public JasperPrint crearReporteDinamico(String titulo, String query, List<Campos> listaCampos, String lib, String cat, String fechas) {

        Map params = new HashMap();
        //FastReportBuilder drb = new FastReportBuilder();
        DynamicReportBuilder drb = new DynamicReportBuilder();
        JasperPrint jp = null;
        JasperReport jr;
        String subTitulo;

        AbstractColumn column;
        AbstractColumn colTotal = null;
        AbstractColumn colDoc = null;

        traza.trace("construyendo el reporte " + titulo, Level.INFO);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(20, "SansSerif", true));
        titleStyle.setBorderBottom(Border.NO_BORDER());
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        titleStyle.setBackgroundColor(Color.WHITE);
        titleStyle.setTextColor(Color.BLACK);
        titleStyle.setTransparency(Transparency.OPAQUE);

        Style subtitleStyle = new Style();
        subtitleStyle.setFont(new Font(11, "SansSerif", false));

        Style footer = new Style();
        subtitleStyle.setFont(new Font(10, "SansSerif", true));

        Style columnHeader = new Style();
        columnHeader.setFont(new Font(10, "SansSerif", false));
        columnHeader.setBorderBottom(Border.THIN());
        columnHeader.setHorizontalAlign(HorizontalAlign.CENTER);
        columnHeader.setVerticalAlign(VerticalAlign.MIDDLE);
        columnHeader.setBackgroundColor(Color.LIGHT_GRAY);
        columnHeader.setTextColor(Color.BLACK);
        columnHeader.setTransparency(Transparency.OPAQUE);

        Style detailStyle = new Style();
        detailStyle.setFont(new Font(10, "SansSerif", false));
        //detailStyle.setBorderBottom(Border.PEN_2_POINT());
        //detailStyle.setBorderBottom(Border.PEN_1_POINT());
        detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        detailStyle.setBackgroundColor(Color.WHITE);
        detailStyle.setTextColor(Color.BLACK);
        detailStyle.setTransparency(Transparency.OPAQUE);

        try {
            drb.setTitle(titulo);
            subTitulo = "Libreria: " + lib + " \\nCategoria: " + cat + "\\n" + fechas;
            drb.setSubtitle(subTitulo);

            drb.setTitleHeight(35);
            drb.setSubtitleHeight(50);

            if (titulo.equalsIgnoreCase("Documentos Rechazados") || titulo.equalsIgnoreCase("Documentos Pendientes por Aprobar")) {
                drb.setPageSizeAndOrientation(Page.Page_Letter_Landscape());
                //drb.setPageSizeAndOrientation(Page.Page_A4_Landscape());
            } else {
                drb.setPageSizeAndOrientation(Page.Page_Letter_Portrait());
                //drb.setPageSizeAndOrientation(Page.Page_A4_Portrait());
            }

            drb.setMargins(20, 20, 20, 20);
            drb.setDetailHeight(15);
            drb.setColumnsPerPage(1);
            drb.setDefaultStyles(titleStyle, subtitleStyle, columnHeader, detailStyle);
            drb.setUseFullPageWidth(true);
            drb.setWhenNoDataBlankPage();
            drb.setWhenNoDataNoPages();
            drb.setWhenNoDataType(DJConstants.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
            //drb.addImageBanner(System.getProperty("user.dir") +"/logos/DevelcomLogo.png", new Integer(121), new Integer(79), ImageBanner.ALIGN_RIGHT);

            drb.setQuery(query, DJConstants.QUERY_LANGUAGE_SQL);

            //drb.addAutoText(AutoText.AUTOTEXT_PAGE_X, AutoText.POSITION_HEADER, AutoText.ALIGMENT_RIGHT);
            drb.addAutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y, AutoText.POSITION_HEADER, AutoText.ALIGMENT_RIGHT);

            for (Campos campos : listaCampos) {

                traza.trace("campos del reporte dinamico " + campos.getNombre() + " tipo de dato " + campos.getTipoDato(), Level.INFO);

                if (campos.getTipoDato().equals(Campos.FECHA)) {

                    column = ColumnBuilder.getNew()
                            .setColumnProperty(campos.getNombre(), campos.getTipoDato().toString())
                            .setTitle(crearEtiqueta(campos.getNombre()))
                            //.setWidth(120) //85
                            .setWidth(130) //85
                            .setPattern("dd/MM/yyyy")
                            .build();
                    drb.addColumn(column);
                } else {
                    if (campos.getNombre().equalsIgnoreCase("cantidad_documentos")) {
                        colTotal = ColumnBuilder.getNew()
                                //                                .setCustomExpression(getCustomExpression())
                                //                                .setCustomExpressionForCalculation(getCustomExpression2())
                                //                                .setStyle(subtitleStyle)
                                .setColumnProperty(campos.getNombre(), campos.getTipoDato().toString())
                                .setTitle(crearEtiqueta(campos.getNombre()))
                                .setWidth(120)
                                .build();
                        drb.addColumn(colTotal);
                    } else if (campos.getNombre().equalsIgnoreCase("documento")) {
                        colDoc = ColumnBuilder.getNew()
                                .setColumnProperty(campos.getNombre(), campos.getTipoDato().toString())
                                .setTitle(crearEtiqueta(campos.getNombre()))
                                .setWidth(120)
                                .build();
                        drb.addColumn(colDoc);
                    } else {
                        column = ColumnBuilder.getNew()
                                .setColumnProperty(campos.getNombre(), campos.getTipoDato().toString())
                                .setTitle(crearEtiqueta(campos.getNombre()))
                                .setWidth(120)
                                .build();
                        drb.addColumn(column);

                    }
                }
            }

            if (titulo.equalsIgnoreCase("Documentos Pendientes por Aprobar")) {

                //drb.addGlobalFooterVariable(colTotal, DJCalculation.SUM, subtitleStyle, getValueFormatter());
                //drb.setGrandTotalLegend("");
//                drb.addGlobalHeaderVariable(colTotal, DJCalculation.SUM, subtitleStyle);
                drb.addGlobalFooterVariable(colTotal, DJCalculation.SUM);
// 		drb.setGlobalHeaderVariableHeight(new Integer(25));
                drb.setGlobalFooterVariableHeight(25);
                drb.setGrandTotalLegend("Total Documentos Pendientes por Aprobar");

//                GroupBuilder gb1 = new GroupBuilder();
//                DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) colTotal)
//                        .addFooterVariable(colTotal,DJCalculation.SUM,subtitleStyle) // idem for the columnaQuantity column
////                        .addFooterVariable(columnAmount,DJCalculation.SUM,groupVariables) // tell the group place a variable footer of the column "columnAmount" with the SUM of allvalues of the columnAmount in this group.
////                        .addHeaderVariable(colTotal,DJCalculation.SUM,subtitleStyle) // idem for the columnaQuantity column
////                        .addHeaderVariable(columnAmount,DJCalculation.SUM,groupVariables) // tell the group place a variable footer of the column "columnAmount" with the SUM of allvalues of the columnAmount in this group.
////                        .setGroupLayout(GroupLayout.VALUE_IN_HEADER) // tells the group how to be shown, there are manyposibilities, see the GroupLayout for more.
//                        .setFooterVariablesHeight(new Integer(20))
//                        .setFooterHeight(new Integer(50),true)
////                        .setHeaderVariablesHeight(new Integer(35))
//                        .build();
//
//                drb.addGroup(g1);
            }
            
            DynamicReport dr = drb.build();
            
            //jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), params);
            //JasperDesignViewer.viewReportDesign(jr);
            //jp = JasperFillManager.fillReport(jr, params, createConnection());
            jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), new BaseDato().conectar(), params);
            //jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), createConnection(), params);
        } catch (JRException e) {
            traza.trace("error al construir el reporte", Level.ERROR, e);
        } catch (SQLException ex) {
            traza.trace("error de coneccion con la base de datos", Level.ERROR, ex);
        } catch (ClassNotFoundException ex) {
            traza.trace("error el en driver oracle", Level.ERROR, ex);
        }
        return jp;
    }

    

    /**
     * Crea la etiqueta del indice de la categoria, le elimina los underscore
     *
     * @param argu El nombre del indice
     * @return El indice en forma de etiqueta
     */
    private String crearEtiqueta(String argu) {

        String arg = argu.replace("_", " ");
        arg = arg.toUpperCase();
//        char[] cs = arg.toCharArray();
//        char ch = cs[0];
//        cs[0] = Character.toUpperCase(ch);
//        arg = String.valueOf(cs);

        return arg;
    }

//    private CustomExpression getCustomExpression() {
//        return new CustomExpression() {
//
//            @Override
//            public Object evaluate(Map fields, Map variables, Map parameters) {
//                //Long amount = (Long) fields.get("cantidad_documentos");
//                Integer amount = (Integer) fields.get("cantidad_documentos");
//                traza.trace("valor campo " + amount, Level.INFO);
//                return amount;
//            }
//
//            @Override
//            public String getClassName() {
//                return String.class.getName();
//            }
//
//        };
//    }
//
//    private CustomExpression getCustomExpression2() {
//        return new CustomExpression() {
//
//            @Override
//            public Object evaluate(Map fields, Map variables, Map parameters) {
//                return fields.get("cantidad_documentos");
//            }
//
//            @Override
//            public String getClassName() {
//                return Integer.class.getName();
//                //return Long.class.getName();
//            }
//
//        };
//    }
//
//    private DJValueFormatter getValueFormatter() {
//        return new DJValueFormatter() {
//
//            @Override
//            public Object evaluate(Object value, Map fields, Map variables, Map parameters) {
//                //return "Total time: " + getAsMinutes((Long) value);
//                Integer amount = (Integer) fields.get("cantidad_documentos");
//                return "Total time: " + amount;
//            }
//
//            @Override
//            public String getClassName() {
//                return String.class.getName();
//            }
//        };
//    }
}
