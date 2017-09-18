/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.reportes.tools;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
//import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
//import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
//import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
//import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
//import net.sf.jasperreports.engine.type.LineStyleEnum;
//import net.sf.jasperreports.engine.type.ModeEnum;
//import net.sf.jasperreports.engine.type.PositionTypeEnum;
//import net.sf.jasperreports.engine.type.SplitTypeEnum;
//import net.sf.jasperreports.engine.type.StretchTypeEnum;
//import net.sf.jasperreports.engine.type.VerticalAlignEnum;
//import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.SplitTypeEnum;
import net.sf.jasperreports.engine.type.StretchTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import org.apache.log4j.Level;
import com.develcom.dao.Campos;
import com.develcom.tools.trazas.Traza;

/**
 *
 * @author develcom
 */
public class CreadorReporte {


    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(CreadorReporte.class);



    public JasperDesign crearReport(String nombreReporte, String query, String tituloReporte, List<Campos> listaCampos, List<String> titulos, List<String> detalles){

        //estructura basica
        JasperDesign jasperDesign = estruturaBasica(nombreReporte);

        //estilos
        for (JRDesignStyle jrDesignStyle : creandoEstilos()) {
            try {
                jasperDesign.addStyle(jrDesignStyle);
            } catch (JRException e) {
                traza.trace("Error in adding a style to the report... ", Level.ERROR, e);
            }
        }

        //agrega query
        jasperDesign.setQuery(creandoQuery(query));

        //los campos (fields)
        for (JRDesignField field : getFieldList(listaCampos)) {
            try {
                jasperDesign.addField(field);
            } catch (JRException e) {
                traza.trace("Error in adding a field to the reportdesign ", Level.ERROR, e);
            }
        }

        //la band del titulo
        jasperDesign.setTitle(creandoBandTitulo((JRDesignStyle) jasperDesign.getStylesMap().get("titleStyle"), tituloReporte));
        
        //la band de las cabezeras
        jasperDesign.setColumnHeader(crearBandCabezeras((JRDesignStyle) jasperDesign.getStylesMap().get("headerStyle"), titulos));


        //la band de detalles
        ((JRDesignSection)jasperDesign.getDetailSection()).addBand(createDetailBand((JRDesignStyle) jasperDesign.getStylesMap().get("detailStyle"), detalles));

        jasperDesign.preprocess();

        return jasperDesign;
    }



    private JasperDesign estruturaBasica(String nombreReporte){
        JasperDesign jasperDesign=new JasperDesign();

        //net.sf.jasperreports.engine.JasperReport c;

        jasperDesign.setName(nombreReporte);
        jasperDesign.setColumnCount(1);
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(555);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);
        //jasperDesign.setWhenNoDataType(JasperReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL);
//        jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
//        jasperDesign.setTitleNewPage(false);
//        jasperDesign.setSummaryNewPage(false);


        return  jasperDesign;
    }

    private List<JRDesignStyle> creandoEstilos(){


        List<JRDesignStyle> stylesList=new ArrayList<JRDesignStyle>();
        JRDesignStyle estilo;//=new JRDesignStyle();

        //estilo del titulo
        estilo=new JRDesignStyle();
        estilo.setName("titleStyle");
        estilo.setDefault(true);
        estilo.setFontName("SansSerif");
        estilo.setForecolor(Color.BLACK);
        estilo.setFontSize(30);
//        estilo.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
//        estilo.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        estilo.setBold(true);
        estilo.setItalic(false);
        estilo.setUnderline(false);
        estilo.setStrikeThrough(false);
        estilo.setPdfEmbedded(true);
        stylesList.add(estilo);

        //estilo del deatlle
        estilo=new JRDesignStyle();
        estilo.setName("detailStyle");
        estilo.setDefault(true);
        estilo.setFontName("SansSerif");
        estilo.setForecolor(Color.BLACK);
        estilo.setFontSize(9);
//        estilo.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
//        estilo.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        estilo.setBold(false);
        estilo.setItalic(false);
        estilo.setUnderline(false);
        estilo.setStrikeThrough(false);
        estilo.setPdfEmbedded(true);
        stylesList.add(estilo);

        //estilo cabezeras
        estilo=new JRDesignStyle();
        estilo.setName("headerStyle");
        estilo.setDefault(true);
        estilo.setFontName("SansSerif");
        estilo.setForecolor(Color.WHITE);
        estilo.setBackcolor(Color.GRAY);
        estilo.setFontSize(9);
//        estilo.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
//        estilo.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        estilo.setBold(true);
        estilo.setItalic(false);
        estilo.setUnderline(false);
        estilo.setStrikeThrough(false);
        estilo.setPdfEmbedded(true);
        stylesList.add(estilo);


        return stylesList;
    }


    private JRDesignQuery creandoQuery(String query){

        JRDesignQuery designQuery = new JRDesignQuery();
        designQuery.setLanguage("sql");
        designQuery.setText(query);

        return designQuery;
    }

    private List<JRDesignField> getFieldList(List<Campos> listaCampos){

        List<JRDesignField> fieldsList=new ArrayList<JRDesignField>();
        JRDesignField field;//=new JRDesignField();

        for(Campos campos : listaCampos){
            field=new JRDesignField();
            field.setName(campos.getNombre());
            field.setValueClassName(campos.getTipoDato().toString());
            fieldsList.add(field);
        }

        return fieldsList;
    }

    private JRDesignBand creandoBandTitulo(JRDesignStyle jrStyle, String tituloReporte){
        
        JRDesignBand designBand=new JRDesignBand();
        designBand.setHeight(79);
//        designBand.setSplitAllowed(true);
        designBand.setSplitType(SplitTypeEnum.STRETCH);
        //designBand.setSplitAllowed(true);

        //Create a TextField (textFieldTag attributes)
        JRDesignTextField textField=new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(true);
//        textField.setEvaluationTime(JRExpression.EVALUATION_TIME_NOW);
//        textField.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_NONE);
//        textField.setHyperlinkTarget(JRHyperlink.HYPERLINK_TARGET_SELF);
        textField.setEvaluationTime(EvaluationTimeEnum.NOW);
        textField.setHyperlinkType(HyperlinkTypeEnum.NONE);
        textField.setHyperlinkTarget(HyperlinkTargetEnum.SELF);

        textField.setStyle(jrStyle);
        textField.setX(0);
        textField.setY(0);
        textField.setWidth(434);
        textField.setHeight(79);
        textField.setKey("titleTextField");
//        textField.setHorizontalAlignment(JRTextField.HORIZONTAL_ALIGN_CENTER);
//        textField.setStretchType(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT);
//        textField.setPositionType(JRElement.POSITION_TYPE_FLOAT);
//        textField.setMode(JRTextField.MODE_OPAQUE);
        textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        textField.setVerticalAlignment(VerticalAlignEnum.MIDDLE);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setPositionType(PositionTypeEnum.FLOAT);
        textField.setMode(ModeEnum.OPAQUE);

        //box tag properties
//        textField.getLineBox().setPadding(1);
//        textField.getLineBox().getPen().setLineWidth(1);
//        textField.getLineBox().getPen().setLineColor(Color.BLACK);
//        textField.getLineBox().getPen().setLineStyle(LineStyleEnum.SOLID);

        //TextField expression
        JRDesignExpression designExpression=new JRDesignExpression();
        designExpression.setValueClass(String.class);
        designExpression.setText(tituloReporte);

        //Setting the expression
        textField.setExpression(designExpression);

        //Add the textField to the band
        designBand.addElement(textField);
        return(designBand);
    }

    private JRDesignBand crearBandCabezeras(JRDesignStyle style, List<String> titulos){
        JRDesignBand header=new JRDesignBand();
//        header.setSplitAllowed(true);
        header.setSplitType(SplitTypeEnum.STRETCH);
        header.setHeight(41);
        JRDesignTextField titleHeader;
        int x=0, y=0, width=0, height=0, cont=0;

        height=13;
        width=100;
        //y=41;
        for(String titulo : titulos){
            titleHeader = new JRDesignTextField();
//            titleHeader = crearCampos(x, y, width, height, "etiqueta"+cont, Campos.CADENA, titulo, JRTextField.HORIZONTAL_ALIGN_CENTER, style);
            titleHeader = crearCampos(x, y, width, height, "etiqueta"+cont, titulo, HorizontalAlignEnum.CENTER, style);
            header.addElement(titleHeader);
            x=x+100;
            cont++;
        }


        return header;
    }

    private JRDesignBand createDetailBand(JRDesignStyle style, List<String> detalles){
        JRDesignBand detail=new JRDesignBand();
//        detail.setSplitAllowed(true);
        detail.setSplitType(SplitTypeEnum.STRETCH);
        detail.setHeight(41);
        JRDesignTextField titleHeader;
        int x=0, y=0, width=0, height=0, cont=0;

        height=12;
        width=100;
        //y=41;
        for(String titulo : detalles){
            titleHeader = new JRDesignTextField();
//            titleHeader = crearCampos(x, y, width, height, "detalle"+cont, Campos.CADENA, titulo, JRTextField.HORIZONTAL_ALIGN_CENTER, style);
            titleHeader = crearCampos(x, y, width, height, "detalle"+cont, titulo, HorizontalAlignEnum.CENTER, style);
            detail.addElement(titleHeader);
            x=x+100;
            cont++;
        }


        return detail;
    }


    private JRDesignTextField crearCampos(int x, int y, int width, int height, String fieldId,
                                              String expressionString, HorizontalAlignEnum alignment,
                                              JRDesignStyle detailStyle){
//    private JRDesignTextField crearCampos(int x, int y, int width, int height, String fieldId,
//                                              String fieldClass, String expressionString, byte alignment,
//                                              JRDesignStyle detailStyle){

        //Create a TextField (textFieldTag attributes)
        JRDesignTextField textField=new JRDesignTextField();
        textField.setStretchWithOverflow(true);
        textField.setBlankWhenNull(true);
//        textField.setEvaluationTime(JRExpression.EVALUATION_TIME_NOW);
//        textField.setHyperlinkType(JRHyperlink.HYPERLINK_TYPE_NONE);
//        textField.setHyperlinkTarget(JRHyperlink.HYPERLINK_TARGET_SELF);
        textField.setEvaluationTime(EvaluationTimeEnum.NOW);
        textField.setHyperlinkType(HyperlinkTypeEnum.NONE);
        textField.setHyperlinkTarget(HyperlinkTargetEnum.SELF);

        textField.setStyle(detailStyle);
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(width);
        textField.setHeight(height);
        textField.setKey(fieldId);
        textField.setHorizontalAlignment(alignment);
//        textField.setStretchType(JRElement.STRETCH_TYPE_RELATIVE_TO_TALLEST_OBJECT);
//        textField.setPositionType(JRElement.POSITION_TYPE_FLOAT);
//        textField.setMode(JRTextField.MODE_OPAQUE);
        textField.setStretchType(StretchTypeEnum.RELATIVE_TO_TALLEST_OBJECT);
        textField.setPositionType(PositionTypeEnum.FLOAT);
        textField.setMode(ModeEnum.OPAQUE);

        //box tag properties
//        textField.getLineBox().setPadding(1);
//        textField.getLineBox().getPen().setLineWidth(1);
//        textField.getLineBox().getPen().setLineColor(Color.BLACK);
//        textField.getLineBox().getPen().setLineStyle(LineStyleEnum.SOLID);

        //TextField expression
        JRDesignExpression designExpression=new JRDesignExpression();
//        designExpression.setValueClassName(fieldClass);
        designExpression.setText(expressionString);

        //Setting the expression
        textField.setExpression(designExpression);

        return textField;
    }

}
