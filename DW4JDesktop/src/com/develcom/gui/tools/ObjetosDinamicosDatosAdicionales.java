/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

import com.develcom.dao.Expediente;
import com.develcom.gui.captura.DatoAdicional;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.expediente.GestionDocumentos;

/**
 *
 * @author develcom
 */
public class ObjetosDinamicosDatosAdicionales extends CreaObjetosDinamicos {

    /**
     * Ventana interna
     */
    private DatoAdicional datoAdicional;
    private Expediente expediente;
    private int numeroDocumento;
    private int version;
    private Traza traza = new Traza(ObjetosDinamicosDatosAdicionales.class);

    public ObjetosDinamicosDatosAdicionales(Expediente expediente, DatoAdicional datoAdicional) {
        super();
        this.expediente = expediente;
        this.datoAdicional = datoAdicional;
    }

    /**
     * Crea los objetos en el panel contenedor segun los indices dinamicos
     *
     * @param expediente Informacion del expediente
     * @param mostrar
     * @param numeroDocumento
     * @return Un contenedor con los obejtos armados dinamicamente
     * @throws javax.xml.soap.SOAPException
     */
    public JPanel crearObjetos(Expediente expediente, boolean mostrar, int numeroDocumento, int version) throws SOAPException {
        JPanel panel = null;
        this.expediente = expediente;
        this.numeroDocumento = numeroDocumento;
        this.version = version;
        if (mostrar) {
            panel = mostrarIndices();
        } else {
            panel = armarObjetosDatoAdicional();
        }

        return panel;
    }

    private Object buscarValorDatoAdicional(String datoAdicional, List<com.develcom.documento.DatoAdicional> lsIndDatosAdicionales) {
        Object valor = null;
        for (com.develcom.documento.DatoAdicional da : lsIndDatosAdicionales) {
            if (da.getIndiceDatoAdicional().equalsIgnoreCase(datoAdicional)) {
                valor = da.getValor();
            }
        }
        return valor;
    }

    public JPanel llenarValorDatosAdicional(int numeroDocumento) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JPanel panel = null;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JTextField[] textos;
        JTextArea areaTexto[];
        JComboBox[] combos;
        JDateChooser fecha[];
        int i = 0, banderaColumna = 0, filas = 0, columnas = 0, idCategoria, sizeList;
        try {

            idCategoria = expediente.getIdCategoria();
            List<com.develcom.documento.DatoAdicional> lsDatosAdicionales = new GestionDocumentos().encontrarValorDatoAdicional(expediente.getIdTipoDocumento(), expediente.getIdExpediente(), numeroDocumento, version);

            sizeList = lsDatosAdicionales.size();

            textos = new JTextField[sizeList];
            labels = new JLabel[sizeList];
            combos = new JComboBox[sizeList];
            areaTexto = new JTextArea[sizeList];
            fecha = new JDateChooser[sizeList];

            panel = new JPanel(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < sizeList; i++) {

                String datoAdic = lsDatosAdicionales.get(i).getIndiceDatoAdicional().replace("_", " ");
                String tipo = lsDatosAdicionales.get(i).getTipo();

//                labels[i] = new JLabel(" " + crearEtiqueta(datoAdic.trim()) + ": ");
                labels[i] = new JLabel(" " + datoAdic.trim() + ": ");
                labels[i].setHorizontalAlignment(JLabel.RIGHT);

                textos[i] = new JTextField(30);

                combos[i] = new JComboBox();

                areaTexto[i] = new JTextArea();

                fecha[i] = new JDateChooser();
                fecha[i].setCalendar(null);
                fecha[i].setDateFormatString("dd/MM/yyyy");
                fecha[i].setPreferredSize(new Dimension(100, 20));

                if (banderaColumna != 0) {
                    columnas = 0;
                }

                if (tipo.equalsIgnoreCase("numero")) {

                    try {
                        textos[i].setText(buscarValorDatoAdicional(datoAdic, lsDatosAdicionales).toString());
                    } catch (NullPointerException e) {
                        textos[i].setText("");
                    }

                    banderaColumna = 0;
                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(textos[i], constraints);

                    lsDatosAdicionales.get(i).setValor(textos[i]);

                } else {
                    banderaColumna = 0;

                    if (tipo.equalsIgnoreCase("fecha")) {

                        try {
//                            XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) buscarValorDatoAdicional(datoAdic, lsDatosAdicionales);
//                            Calendar cal = xmlCal.toGregorianCalendar();

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(sdf.parse(buscarValorDatoAdicional(datoAdic, lsDatosAdicionales).toString()));

                            if (cal != null) {
                                fecha[i].setCalendar(cal);
                            } else {
                                fecha[i].setCalendar(null);
                            }
                        } catch (NullPointerException e) {
                            fecha[i].setCalendar(null);
                        } catch (ParseException ex) {
                            traza.trace("problemas en el parse de la fecha del dato adicional", Level.ERROR, ex);
                        }

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(labels[i], constraints);

                        constraints.gridx = ++columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(fecha[i], constraints);

                        lsDatosAdicionales.get(i).setValor(fecha[i]);

                    } else if (tipo.equalsIgnoreCase("combo")) {

                        combos[i].setModel(llenarCombo(i, lsDatosAdicionales.get(i).getCodigo(), true));

                        try {
                            combos[i].setSelectedItem(buscarValorDatoAdicional(datoAdic, lsDatosAdicionales).toString());
                        } catch (NullPointerException e) {
                            combos[i].setSelectedItem("");
                        }

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(labels[i], constraints);

                        constraints.gridx = ++columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(combos[i], constraints);

                        lsDatosAdicionales.get(i).setValor(combos[i]);

                    } else if (tipo.equalsIgnoreCase("texto")) {

                        try {
                            textos[i].setText(buscarValorDatoAdicional(datoAdic, lsDatosAdicionales).toString());
                        } catch (NullPointerException e) {
                            textos[i].setText("");
                        }

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(labels[i], constraints);

                        constraints.gridx = ++columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(textos[i], constraints);

                        lsDatosAdicionales.get(i).setValor(textos[i]);

                    } else if (tipo.equalsIgnoreCase("area")) {

                        try {
                            areaTexto[i].setText(buscarValorDatoAdicional(datoAdic, lsDatosAdicionales).toString());
                        } catch (NullPointerException e) {
                            areaTexto[i].setText("");
                        }

                        areaTexto[i].setColumns(10);
                        areaTexto[i].setRows(3);
                        JScrollPane jsp = new JScrollPane();
                        jsp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                        jsp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        jsp.setAutoscrolls(true);
                        jsp.setViewportView(areaTexto[i]);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(labels[i], constraints);

                        constraints.gridx = ++columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 6;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(jsp, constraints);

                        lsDatosAdicionales.get(i).setValor(areaTexto[i]);
                    }
                }

                banderaColumna++;

                if (columnas == 1) {
                    constraints.gridx = 0;
                    constraints.gridy = ++filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(new JLabel(" "), constraints);
                    columnas = 0;
                    banderaColumna++;
                    filas++;
                }
            }

            datoAdicional.setLsIndDatosAdicionales(convertir(lsDatosAdicionales));

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }
        return panel;
    }

    private List<com.develcom.administracion.DatoAdicional> convertir(List<com.develcom.documento.DatoAdicional> lsDatosAdicionales) {
        List<com.develcom.administracion.DatoAdicional> datosAdicionales = new ArrayList<com.develcom.administracion.DatoAdicional>();
        com.develcom.administracion.DatoAdicional datoAdicional;

        for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
            datoAdicional = new com.develcom.administracion.DatoAdicional();

            datoAdicional.setCodigo(da.getCodigo());
            datoAdicional.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
            datoAdicional.setIdDatoAdicional(da.getIdDatoAdicional());
            datoAdicional.setIdTipoDocumento(da.getIdTipoDocumento());
            datoAdicional.setIdValor(da.getIdValor());
            datoAdicional.setNumeroDocumento(da.getNumeroDocumento());
            datoAdicional.setTipo(da.getTipo());
            datoAdicional.setValor(da.getValor());

            datosAdicionales.add(datoAdicional);
        }

        return datosAdicionales;
    }

    private JPanel armarObjetosDatoAdicional() {
        JPanel panel = null;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JTextField[] textos;
        JTextArea areaTexto[];
        JComboBox[] combos;
        JDateChooser fecha[];
        int i = 0, filas = 0, columnas = 0, sizeList;
        List<com.develcom.administracion.DatoAdicional> lsIndDatosAdicionales;
        try {

            lsIndDatosAdicionales = new AdministracionBusqueda().buscarIndDatoAdicional(expediente.getIdTipoDocumento());
            sizeList = lsIndDatosAdicionales.size();

            textos = new JTextField[sizeList];
            labels = new JLabel[sizeList];
            combos = new JComboBox[sizeList];
            areaTexto = new JTextArea[sizeList];
            fecha = new JDateChooser[sizeList];
            

            panel = new JPanel(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < sizeList; i++) {

                String arg = lsIndDatosAdicionales.get(i).getIndiceDatoAdicional().replace("_", " ");
                String tipo = lsIndDatosAdicionales.get(i).getTipo();

//                labels[i] = new JLabel(" " + crearEtiqueta(arg.trim()) + ": ");
                labels[i] = new JLabel(" " + arg.trim() + ": ");
                labels[i].setHorizontalAlignment(JLabel.RIGHT);

                textos[i] = new JTextField(30);

                combos[i] = new JComboBox();

                areaTexto[i] = new JTextArea();

                fecha[i] = new JDateChooser();
                fecha[i].setCalendar(null);
                fecha[i].setDateFormatString("dd/MM/yyyy");
                fecha[i].setPreferredSize(new Dimension(100, 20));

                if (tipo.equalsIgnoreCase("numero")) {

                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(textos[i], constraints);

                    lsIndDatosAdicionales.get(i).setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("fecha")) {

                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(fecha[i], constraints);

                    lsIndDatosAdicionales.get(i).setValor(fecha[i]);

                } else if (tipo.equalsIgnoreCase("combo")) {

                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(combos[i], constraints);

                    lsIndDatosAdicionales.get(i).setValor(combos[i]);

                    combos[i].setModel(llenarCombo(i, lsIndDatosAdicionales.get(i).getCodigo(), true));

                } else if (tipo.equalsIgnoreCase("texto")) {

                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(textos[i], constraints);

                    lsIndDatosAdicionales.get(i).setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("area")) {

                    areaTexto[i].setColumns(10);
                    areaTexto[i].setRows(3);
                    JScrollPane jsp = new JScrollPane();
                    jsp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    jsp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    jsp.setAutoscrolls(true);
                    jsp.setViewportView(areaTexto[i]);

                    constraints.gridx = columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(labels[i], constraints);

                    constraints.gridx = ++columnas;
                    constraints.gridy = filas;
                    constraints.gridwidth = 6;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(jsp, constraints);

                    lsIndDatosAdicionales.get(i).setValor(areaTexto[i]);
                }

                if (columnas == 1) {
                    constraints.gridx = 0;
                    constraints.gridy = ++filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(new JLabel(" "), constraints);
                    columnas = 0;
                    filas++;
                }
            }

            datoAdicional.setLsIndDatosAdicionales(lsIndDatosAdicionales);

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }
        return panel;
    }

    private JPanel mostrarIndices() throws SOAPException, SOAPFaultException {
        List<com.develcom.documento.DatoAdicional> lsDatosAdicionales;
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
//        Font font = new Font("Dialog", Font.PLAIN, 16);
        JPanel panelIndices = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int i = 0, banderaColumna = 0, filas, columnas;
        //boolean fechaIngreso=true, fechaEgreso=true;
        String fech = "";
        columnas = 0;
        filas = 0;
        banderaColumna = 0;

        lsDatosAdicionales = new GestionDocumentos().encontrarValorDatoAdicional(expediente.getIdTipoDocumento(), expediente.getIdExpediente(), numeroDocumento, version);

        if (!lsDatosAdicionales.isEmpty()) {

            labels = new JLabel[lsDatosAdicionales.size()];
            panelIndices = new JPanel(gridBagLayout);
            panelIndices.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < lsDatosAdicionales.size(); i++) {
                com.develcom.documento.DatoAdicional da = lsDatosAdicionales.get(i);

                if (banderaColumna != 0) {
                    columnas = 0;
                }

                try {
                    if (da.getTipo().equalsIgnoreCase("fecha")) {
//                        String arg = crearEtiqueta(da.getIndiceDatoAdicional()) + ": ";
                        String arg = da.getIndiceDatoAdicional() + ": ";
                        try {
                            banderaColumna = 0;
                            if (!arg.equalsIgnoreCase(fech)) {
                                fech = arg;
//                                    XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) da.getValor();
//                                    Calendar cal = xmlCal.toGregorianCalendar();

                                String dato = da.getValor().toString();

                                traza.trace(arg + dato, Level.INFO);

                                labels[i] = new JLabel(arg + dato + " ");
                                labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                    labels[i].setFont(font);

                                constraints.gridx = columnas;
                                constraints.gridy = filas;
                                constraints.gridwidth = 1;
                                constraints.gridheight = 1;
                                constraints.fill = GridBagConstraints.BOTH;
                                panelIndices.add(labels[i], constraints);

                                constraints.gridx = columnas;
                                constraints.gridy = filas;
                                constraints.gridwidth = 1;
                                constraints.gridheight = 1;
                                constraints.fill = GridBagConstraints.BOTH;
                                panelIndices.add(new JLabel(" "), constraints);
                                //fechaIngreso=false;
                            }

                        } catch (NullPointerException e) {

                            labels[i] = new JLabel(arg);
                            labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(labels[i], constraints);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(new JLabel(" "), constraints);

                        }

                    } else {
                        try {
                            banderaColumna = 0;
//                            String arg = crearEtiqueta(da.getIndiceDatoAdicional()) + ": ";
                            String arg = da.getIndiceDatoAdicional() + ": ";
                            String dato = da.getValor().toString().trim() + " ";

                            traza.trace(arg + dato, Level.INFO);

                            labels[i] = new JLabel(arg + dato);
                            labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(labels[i], constraints);

                        } catch (NullPointerException e) {

//                            labels[i] = new JLabel(crearEtiqueta(da.getIndiceDatoAdicional()) + ": ");
                            labels[i] = new JLabel(da.getIndiceDatoAdicional() + ": ");
                            labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(labels[i], constraints);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(new JLabel(" "), constraints);

                        }
                    }

                    filas++;
                } catch (NullPointerException e) {
                }
            }
        } else {

        }

        return panelIndices;
    }

    /**
     *
     * @param lsDatosAdicionales
     * @return
     * @throws SOAPFaultException
     */
    public JPanel mostrarIndicesGuardar(List<com.develcom.documento.DatoAdicional> lsDatosAdicionales) throws NullPointerException {

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
//        Font font = new Font("Dialog", Font.PLAIN, 20);
        JPanel panelIndices = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int i, banderaColumna = 0, filas, columnas;
        //boolean fechaIngreso=true, fechaEgreso=true;
        String fech = "";
        columnas = 0;
        filas = 0;
        banderaColumna = 0;

        if (!lsDatosAdicionales.isEmpty()) {

            labels = new JLabel[lsDatosAdicionales.size()];
            panelIndices = new JPanel(gridBagLayout);
            panelIndices.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < lsDatosAdicionales.size(); i++) {
                com.develcom.documento.DatoAdicional da = lsDatosAdicionales.get(i);

                if (banderaColumna != 0) {
                    columnas = 0;
                }

//                try {
                if (da.getTipo().equalsIgnoreCase("fecha")) {
//                    String arg = crearEtiqueta(da.getIndiceDatoAdicional()) + ": ";
                    String arg = da.getIndiceDatoAdicional() + ": ";
                    try {
                        banderaColumna = 0;
                        if (!arg.equalsIgnoreCase(fech)) {
                            fech = arg;
                            String dato = da.getValor().toString();

                            traza.trace(arg + dato, Level.INFO);

                            labels[i] = new JLabel(arg + dato + " ");
                            labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                    labels[i].setFont(font);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(labels[i], constraints);

                            constraints.gridx = columnas;
                            constraints.gridy = filas;
                            constraints.gridwidth = 1;
                            constraints.gridheight = 1;
                            constraints.fill = GridBagConstraints.BOTH;
                            panelIndices.add(new JLabel(" "), constraints);
                            //fechaIngreso=false;
                        }

                    } catch (NullPointerException e) {

                        labels[i] = new JLabel(arg);
                        labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(labels[i], constraints);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(new JLabel(" "), constraints);

                    }

                } else {
                    try {
                        banderaColumna = 0;
//                        String arg = crearEtiqueta(da.getIndiceDatoAdicional()) + ": ";
                        String arg = da.getIndiceDatoAdicional() + ": ";
                        String dato = da.getValor().toString().trim() + " ";

                        traza.trace(arg + dato, Level.INFO);

                        labels[i] = new JLabel(arg + dato);
                        labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(labels[i], constraints);

                    } catch (NullPointerException e) {

//                        labels[i] = new JLabel(crearEtiqueta(da.getIndiceDatoAdicional()) + ": ");
                        labels[i] = new JLabel(da.getIndiceDatoAdicional() + ": ");
                        labels[i].setHorizontalAlignment(JLabel.LEFT);
//                                labels[i].setFont(font);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(labels[i], constraints);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(new JLabel(" "), constraints);

                    }
                }

                filas++;
//                } catch (NullPointerException e) {
//                }
            }
        } else {
            throw new NullPointerException();
        }

        return panelIndices;
    }

}
