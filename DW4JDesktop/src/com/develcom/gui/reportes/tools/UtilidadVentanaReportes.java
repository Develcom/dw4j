/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.reportes.tools;

import com.develcom.autentica.Usuario;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.sesion.IniciaSesion;

/**
 *
 * @author develcom
 */
public class UtilidadVentanaReportes {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(UtilidadVentanaReportes.class);


    private JPanel panel;
    private JComboBox cmboMes = new JComboBox();
    private JComboBox cmboAnioDesde = new JComboBox();
    private JComboBox cmboAnioHasta = new JComboBox();
    private JComboBox cmboMesDesde = new JComboBox();
    private JComboBox cmboMesHasta = new JComboBox();
    private JComboBox cmboTipoPersonal = new JComboBox();
    private JComboBox cmboUsuarios = new JComboBox();
    private DefaultComboBoxModel modelo;
    private DefaultComboBoxModel modeloAnioDesde;
    private DefaultComboBoxModel modeloAnioHasta;
    private DefaultComboBoxModel modeloDesde;
    private DefaultComboBoxModel modeloHasta;
    private Map<String, String> mapMeses = Collections.unmodifiableMap(new HashMap<String, String>() {
                {
                    put("Enero", "01");
                    put("Febrero", "02");
                    put("Marzo", "03");
                    put("Abril", "04");
                    put("Mayo", "05");
                    put("Junio", "06");
                    put("Julio", "07");
                    put("Agosto", "08");
                    put("Septiembre", "09");
                    put("Octubre", "10");
                    put("Noviembre", "11");
                    put("Diciembre", "12");
                }
            });
    private String [] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    private List<String> listaTipoPersonal = new ArrayList<String>();
    private boolean tipoPersonal = false;
    private boolean usuario = false;

    private JRadioButton jrb30dias = new JRadioButton();
    private JRadioButton jrb60dias = new JRadioButton();
    private JRadioButton jrb90dias = new JRadioButton();
    private String dias;
    private ButtonGroup grupoBotones = new ButtonGroup();

    private JDateChooser fechaDesde = new JDateChooser();
    private JDateChooser fechaHasta = new JDateChooser();

    private GridBagConstraints constraints = new GridBagConstraints();



    public JPanel docVencerse(){
        
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new java.awt.Color(224, 239, 255));


        jrb30dias.setText("30 días");
        jrb30dias.setBackground(new java.awt.Color(224, 239, 255));
        jrb60dias.setText("60 días");
        jrb60dias.setBackground(new java.awt.Color(224, 239, 255));
        jrb90dias.setText("90 días");
        jrb90dias.setBackground(new java.awt.Color(224, 239, 255));

        grupoBotones.add(jrb30dias);
        grupoBotones.add(jrb60dias);
        grupoBotones.add(jrb90dias);

        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(jrb30dias, constraints);
        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(jrb60dias, constraints);
        constraints.gridx=0;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(jrb90dias, constraints);
        return panel;
    }

    public JPanel mesAnio(){

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new java.awt.Color(224, 239, 255));

        llenarComboMes();
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Mes: "), constraints);
        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboMes, constraints);


        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("  "), constraints);

        llenarComboAnio();
        constraints.gridx=0;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Año: "), constraints);
        constraints.gridx=1;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboAnioDesde, constraints);


        return panel;
    }

    private void llenarComboUsuarios() {
        modelo = new DefaultComboBoxModel();
        modelo.addElement("");
        List<Usuario> lstUsuarios;
        try {

            lstUsuarios = new IniciaSesion().autocomplete();

            for (Usuario user : lstUsuarios) {
                if (user.getIdEstatus() == 1) {
                    modelo.addElement(user.getIdUsuario());
                }
            }
            
            cmboUsuarios.setModel(modelo);

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los usuarios", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("problemas al buscar los usuarios", Level.ERROR, ex);
        } catch (ConnectException ex) {
            traza.trace("error de coneccion", Level.ERROR, ex);
        }

    }


    private void llenarComboTipoPersonal(){
        modelo = new DefaultComboBoxModel();
        modelo.addElement("");
        buscarTiposPersonal();
        
        for(String tp : listaTipoPersonal){
            modelo.addElement(tp);
        }
        cmboTipoPersonal.setModel(modelo);
    }
    
    private void llenarComboMes(){
        modelo = new DefaultComboBoxModel();
        modeloDesde = new DefaultComboBoxModel();
        modeloHasta = new DefaultComboBoxModel();
        modelo.addElement("");
        modeloDesde.addElement("");
        modeloHasta.addElement("");

        for(String mes : meses){
            modelo.addElement(mes);
            modeloDesde.addElement(mes);
            modeloHasta.addElement(mes);
        }

        cmboMes.setModel(modelo);
        cmboMesDesde.setModel(modeloDesde);
        cmboMesHasta.setModel(modeloHasta);
    }

    private void llenarComboAnio(){
        Calendar calendar;// = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        modeloAnioDesde = new DefaultComboBoxModel();
        modeloAnioHasta = new DefaultComboBoxModel();
//        modeloDesde = new DefaultComboBoxModel();
//        modeloHasta = new DefaultComboBoxModel();
        modeloAnioDesde.addElement("");
        modeloAnioHasta.addElement("");
//        modeloDesde.addElement("");
//        modeloHasta.addElement("");
        
        for(int i=0;i<4;i++){
            calendar = new GregorianCalendar();

            calendar.add(Calendar.YEAR, -i);
            modeloAnioDesde.addElement(sdf.format(calendar.getTime()));
            modeloAnioHasta.addElement(sdf.format(calendar.getTime()));
            //modeloDesde.addElement(sdf.format(calendar.getTime()));
            //modeloHasta.addElement(sdf.format(calendar.getTime()));
            traza.trace("año "+calendar.get(Calendar.YEAR), Level.INFO);
        }
        cmboAnioDesde.setModel(modeloAnioDesde);
        cmboAnioHasta.setModel(modeloAnioHasta);
    }

    public JPanel rangosMesAnio(){

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new java.awt.Color(224, 239, 255));

        llenarComboMes();
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Mes desde: "), constraints);
        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboMesDesde, constraints);

        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("  "), constraints);

        constraints.gridx=0;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Mes hasta: "), constraints);
        constraints.gridx=1;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboMesHasta, constraints);


        constraints.gridx=0;
        constraints.gridy=3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("  "), constraints);

        llenarComboAnio();
        constraints.gridx=0;
        constraints.gridy=4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Año desde: "), constraints);
        constraints.gridx=1;
        constraints.gridy=4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboAnioDesde, constraints);

        constraints.gridx=0;
        constraints.gridy=5;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("  "), constraints);

        llenarComboAnio();
        constraints.gridx=0;
        constraints.gridy=6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Año hasta: "), constraints);        
        constraints.gridx=1;
        constraints.gridy=6;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(cmboAnioHasta, constraints);





//        constraints.gridx=0;
//        constraints.gridy=5;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        panel.add(new JLabel("  "), constraints);
//
//        constraints.gridx=0;
//        constraints.gridy=6;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        panel.add(new JLabel("Año hasta: "), constraints);
//        constraints.gridx=1;
//        constraints.gridy=6;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;
//        panel.add(cmboAnioHasta, constraints);


        return panel;
    }


    public JPanel rangosFechas(){

        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new java.awt.Color(224, 239, 255));


        fechaDesde.setCalendar(null);
        fechaDesde.setDateFormatString("dd/MM/yyyy");
        fechaHasta.setCalendar(null);
        fechaHasta.setDateFormatString("dd/MM/yyyy");

        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel(" "), constraints);

        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("                                "), constraints);

        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Fecha desde:   "), constraints);

        constraints.gridx=1;
        constraints.gridy=1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 2.0;
        panel.add(fechaDesde, constraints);
        constraints.weighty = 0.0;


        constraints.gridx=4;
        constraints.gridy=2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("  "), constraints);
        

        constraints.gridx=0;
        constraints.gridy=3;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("Fecha hasta:   "), constraints);

        constraints.gridx=1;
        constraints.gridy=3;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 2.0;
        panel.add(fechaHasta, constraints);
        constraints.weighty = 0.0;

        if(tipoPersonal){
            
            constraints.gridx=0;
            constraints.gridy=4;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(new JLabel("  "), constraints);
            
            constraints.gridx=0;
            constraints.gridy=5;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(new JLabel("Tipo Personal:   "), constraints);
            
            llenarComboTipoPersonal();
            constraints.gridx=1;
            constraints.gridy=5;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(cmboTipoPersonal, constraints);
        } else if (usuario) {

            constraints.gridx = 0;
            constraints.gridy = 4;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(new JLabel("  "), constraints);

            constraints.gridx = 0;
            constraints.gridy = 5;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(new JLabel("Usuario:   "), constraints);

            llenarComboUsuarios();
            constraints.gridx = 1;
            constraints.gridy = 5;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.fill = GridBagConstraints.BOTH;
            panel.add(cmboUsuarios, constraints);

        }
        



        return panel;
    }

    private void buscarTiposPersonal(){
        String appPath = System.getProperties().getProperty("user.dir");
        File tipoPer = new File(appPath+"/lib/tipoPersonal");
        
        try {
            
//            listaTipoPersonal = new LLenarListaDesplegable().buscarTiposEmpleados();
            
            Scanner lectura=new Scanner(tipoPer);
            while (lectura.hasNextLine()){
                String tp = lectura.nextLine();
                traza.trace("personal "+tp, Level.INFO);
                listaTipoPersonal.add(tp);
                //System.out.println(lectura.nextLine());
            }

            lectura.close();
        } catch (Exception  e) {
            traza.trace("archivo de tipo de personal no encontrado", Level.ERROR, e);
        }
        
    }


    public Map<String, String> getMapMeses() {
        return mapMeses;
    }    

    public JRadioButton getJrb30dias() {
        return jrb30dias;
    }

    public JRadioButton getJrb60dias() {
        return jrb60dias;
    }

    public JRadioButton getJrb90dias() {
        return jrb90dias;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public JComboBox getCmboAnioDesde() {
        return cmboAnioDesde;
    }

    public JComboBox getCmboAnioHasta() {
        return cmboAnioHasta;
    }

    public JComboBox getCmboMes() {
        return cmboMes;
    }

    public JDateChooser getFechaDesde() {
        return fechaDesde;
    }

    public JDateChooser getFechaHasta() {
        return fechaHasta;
    }

    public JComboBox getCmboMesDesde() {
        return cmboMesDesde;
    }

    public JComboBox getCmboMesHasta() {
        return cmboMesHasta;
    }

    public void setTipoPersonal(boolean tipoPersonal) {
        this.tipoPersonal = tipoPersonal;
    }

    public JComboBox getCmboTipoPersonal() {
        return cmboTipoPersonal;
    }

    public void setUsuario(boolean usuario) {
        this.usuario = usuario;
    }

    public JComboBox getCmboUsuarios() {
        return cmboUsuarios;
    }
}
