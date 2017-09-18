/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

import com.develcom.administracion.Indices;
import com.develcom.administracion.Combo;
import com.develcom.dao.Expediente;
import com.develcom.expediente.Indice;
import com.develcom.gui.visor.VerImagenes;
import com.develcom.gui.captura.CreaExpediente;
import com.develcom.gui.captura.GuardarDoc;
import com.develcom.gui.consulta.ConsultaExpediente;
import com.develcom.gui.indice.ConsultaIndices;
import com.develcom.gui.indice.ResultadoConsulta;
import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.gui.reportes.gui.LibreriaReportes;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Level;
import ve.com.develcom.expediente.BuscaIndice;
import ve.com.develcom.expediente.LLenarListaDesplegable;

/**
 * Clase para armar los indices dinamicos en diferentes ventanas
 *
 * @author develcom
 */
public class CreaObjetosDinamicos {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(CreaObjetosDinamicos.class);
    /**
     * Ventana interna
     */
    private GuardarDoc guardarDoc;
    /**
     * Ventana interna
     */
    private CreaExpediente creaExpediente;
    /**
     * Ventana interna
     */
    private ConsultaExpediente consultarExpediente;
    /**
     * Ventana interna
     */
    private ConsultaIndices consultaIndices;
    /**
     * Ventana interna
     */
    private ResultadoConsulta resultadoConsulta;
    /**
     * Ventana interna
     */
    private LibreriaReportes libreriaReportes;
    /**
     * Ventana interna
     */
    private VerDocumentoPDF verDocumentoPDF;
    /**
     * Ventana interna
     */
    private VerImagenes verImagenes;
    /**
     * Informacion del expediente
     */
    private Expediente expediente;
    /**
     * Objeto con los indices dinamicos
     */
    private Indice indice;

    /**
     * Lista de argumentos
     */
    private List<com.develcom.administracion.Indice> listaIndices;
    /**
     * Lista de indices dinamicos
     */
    private List<Indice> indicess;
    /**
     * Panel contenedor
     */
    private JPanel panel = null;

    public CreaObjetosDinamicos() {
    }

    public CreaObjetosDinamicos(CreaExpediente creaExpediente) {
        this.creaExpediente = creaExpediente;
    }

    public CreaObjetosDinamicos(GuardarDoc guardarDoc) {
        this.guardarDoc = guardarDoc;
    }

    public CreaObjetosDinamicos(ConsultaExpediente consultarExpediente) {
        this.consultarExpediente = consultarExpediente;
    }

    public CreaObjetosDinamicos(ConsultaIndices consultaIndices) {
        this.consultaIndices = consultaIndices;
    }

    public CreaObjetosDinamicos(ResultadoConsulta resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    public CreaObjetosDinamicos(VerDocumentoPDF verDocumentoPDF) {
        this.verDocumentoPDF = verDocumentoPDF;
    }

    public CreaObjetosDinamicos(VerImagenes verImagenes) {
        this.verImagenes = verImagenes;
    }

    /**
     * Crea los objetos en el panel contenedor segun los indices dinamicos
     *
     * @param expediente Informacion del expediente
     * @return Un contenedor con los obejtos armados dinamicamente
     */
    public JPanel crearObjetos(Expediente expediente) {
       
        this.expediente = expediente;

        if (creaExpediente != null) {
            armarObjetos();
        } else if (guardarDoc != null) {
            llenarDatos();
        } else if (consultarExpediente != null) {
            armarObjetosConsulta();
        } else if (consultaIndices != null) {
            armarObjetosConsulta();
        } else if (resultadoConsulta != null) {
            llenarDatos();
        } else if (libreriaReportes != null) {
            llenarDatos();
        }

        return panel;
    }

    /**
     * Construye un panel con los indices y sus datos de la categoria
     * selecionada
     *
     * @param expediente Informacion del Expediente
     * @return Un panel con todos los indices
     */
    public JPanel mostrarIndices(Expediente expediente) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.expediente = expediente;
        JPanel panelIndices = null;
        try {
            listaIndices = new BuscaIndice().buscarIndice(expediente.getIdCategoria());

            buscarDatosIndices();

            panelIndices = new JPanel(gridBagLayout);
            panelIndices.setBackground(new java.awt.Color(224, 239, 255));

            panelIndices = mostrarIndices(indicess);

            if (creaExpediente != null) {
                creaExpediente.setIndices(indicess);
            } else if (guardarDoc != null) {
                guardarDoc.setIndices(indicess);
            } else if (verDocumentoPDF != null) {
                verDocumentoPDF.setIndices(indicess);
            } else if (verImagenes != null) {
                verImagenes.setIndices(indicess);
            }
        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }
        return panelIndices;
    }

    /**
     * Construye un panel con los indices y sus datos de la categoria
     * selecionada
     *
     * @param indices Listado de indices
     * @return Un panel con todos los indices
     */
    public JPanel mostrarIndices(List<Indice> indices) {

        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JPanel panelIndices;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int i, banderaColumna, filas, columnas;
        String fech = "";
        columnas = 0;
        filas = 0;
        banderaColumna = 0;

        labels = new JLabel[indices.size()];
        panelIndices = new JPanel(gridBagLayout);
        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        for (i = 0; i < indices.size(); i++) {
            Indice ind = indices.get(i);

            if (banderaColumna != 0) {
                columnas = 0;
            }

            try {

                if (ind.getTipo().equalsIgnoreCase("fecha")) {
//                    String arg = crearEtiqueta(ind.getIndice()) + ": ";
                    String arg = ind.getIndice() + ": ";
                    try {
                        banderaColumna = 0;
                        if (!arg.equalsIgnoreCase(fech)) {
                            fech = arg;
//                            XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) ind.getValor();
//                            Calendar cal = xmlCal.toGregorianCalendar();

//                            String dato = sdf.format(cal.getTime());
                            String dato = ind.getValor().toString();

                            traza.trace(arg + dato, Level.INFO);

                            labels[i] = new JLabel(arg + dato + " ");
                            labels[i].setHorizontalAlignment(JLabel.LEFT);

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

                    } catch (NullPointerException e) {

                        labels[i] = new JLabel(arg);
                        labels[i].setHorizontalAlignment(JLabel.LEFT);

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
//                        String arg = crearEtiqueta(ind.getIndice()) + ": ";
                        String arg = ind.getIndice() + ": ";
                        String dato = ind.getValor().toString().trim() + " ";

                        traza.trace(arg + dato, Level.INFO);

                        labels[i] = new JLabel(arg + dato);
                        labels[i].setHorizontalAlignment(JLabel.LEFT);

                        constraints.gridx = columnas;
                        constraints.gridy = filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panelIndices.add(labels[i], constraints);

                    } catch (NullPointerException e) {

//                        labels[i] = new JLabel(crearEtiqueta(ind.getIndice()) + ": ");
                        labels[i] = new JLabel(ind.getIndice() + ": ");
                        labels[i].setHorizontalAlignment(JLabel.LEFT);

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

        return panelIndices;
    }

    /**
     * Arma los objectos segun los indices dinamicos
     */
    private void armarObjetos() {
        List<Indice> indices = new ArrayList<>();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JTextField[] textos;
        JTextArea areaTexto[];
        JComboBox[] combos;
        JDateChooser fecha[];
        int i, banderaColumna = 0, filas = 0, columnas = 0, idCategoria, sizeList;
        List<Indice> listaIndicesLocal;

        try {

            idCategoria = expediente.getIdCategoria();
//            listaIndices = new BuscaIndice().buscarIndice(idCategoria);
            listaIndicesLocal = expediente.getIndices();
            sizeList = listaIndicesLocal.size();

            textos = new JTextField[sizeList];
            labels = new JLabel[sizeList];
            combos = new JComboBox[sizeList];
            areaTexto = new JTextArea[sizeList];
            fecha = new JDateChooser[sizeList];

            panel = new JPanel(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

            //buscarDatosIndices();
            for (i = 0; i < sizeList; i++) {

                indice = new Indice();

                Indice indiceLocal = listaIndicesLocal.get(i);
                String arg = indiceLocal.getIndice().replace("_", " ");
                String tipo = indiceLocal.getTipo();
                indice.setClave("");

//                labels[i] = new JLabel(" " + crearEtiqueta(arg.trim()) + ": ");
                labels[i] = new JLabel(" " + arg.trim() + ": ");
                labels[i].setHorizontalAlignment(JLabel.RIGHT);
                textos[i] = new JTextField(30);
                combos[i] = new JComboBox();
                areaTexto[i] = new JTextArea();
                fecha[i] = new JDateChooser();
                fecha[i].setCalendar(null);
                fecha[i].setDateFormatString("dd/MM/yyyy");

                if (banderaColumna != 0) {
                    columnas = 0;
                }

                try {
                    if (indiceLocal.getClave().equalsIgnoreCase("y") || indiceLocal.getClave().equalsIgnoreCase("s")
                            || indiceLocal.getClave().equalsIgnoreCase("o")) {
                        indice.setClave(indiceLocal.getClave());
                    }
                } catch (NullPointerException e) {
                }

                if (tipo.equalsIgnoreCase("numero")) {

                    if (indiceLocal.getValor() != null) {
                        textos[i].setText(indiceLocal.getValor().toString());
                        textos[i].setEditable(false);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("fecha")) {

                    if (indiceLocal.getValor() != null) {
                        XMLGregorianCalendar calendar = (XMLGregorianCalendar) indiceLocal.getValor();
                        Calendar cal = calendar.toGregorianCalendar();

                        Date d = cal.getTime();

                        fecha[i].setCalendar(cal);
                        fecha[i].setEnabled(false);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(fecha[i]);

                } else if (tipo.equalsIgnoreCase("combo")) {

                    if (indiceLocal.getValor() != null) {
                        combos[i].setSelectedItem(indiceLocal.getValor().toString());
                        combos[i].setEditable(false);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(combos[i]);

                    combos[i].setModel(llenarCombo(i, indiceLocal.getCodigo(), false));

                } else if (tipo.equalsIgnoreCase("texto")) {

                    if (indiceLocal.getValor() != null) {
                        textos[i].setText(indiceLocal.getValor().toString());
                        textos[i].setEditable(false);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("area")) {

                    if (indiceLocal.getValor() != null) {
                        areaTexto[i].setText(indiceLocal.getValor().toString());
                        areaTexto[i].setEditable(false);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(areaTexto[i]);
                }

                indices.add(indice);
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

            if (creaExpediente != null) {
                creaExpediente.setIndices(indices);
            } else if (guardarDoc != null) {
                guardarDoc.setIndices(indices);
            } else if (consultarExpediente != null) {
                consultarExpediente.setIndices(indices);
            }
        } catch (Exception ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }

    }

    /**
     * Arma los objectos segun los indices dinamicos
     */
    private void armarObjetosConsulta() {
        List<Indice> indices = new ArrayList<>();
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
            listaIndices = new BuscaIndice().buscarIndice(idCategoria);
            sizeList = listaIndices.size();

            textos = new JTextField[sizeList];
            labels = new JLabel[sizeList+1];
            combos = new JComboBox[sizeList];
            areaTexto = new JTextArea[sizeList];
            fecha = new JDateChooser[sizeList+1];

            panel = new JPanel(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < sizeList; i++) {

                indice = new Indice();
                
                Indices indiceLocal = listaIndices.get(i);
                String arg = indiceLocal.getIndice().replace("_", " ");
                String tipo = indiceLocal.getTipo();
                indice.setClave("");

                labels[i] = new JLabel(" " + arg + " ");
                labels[i].setHorizontalAlignment(JLabel.RIGHT);
                textos[i] = new JTextField(30);
                combos[i] = new JComboBox();
                areaTexto[i] = new JTextArea();
                fecha[i] = new JDateChooser();
                fecha[i].setCalendar(null);
                fecha[i].setDateFormatString("dd/MM/yyyy");

                if (banderaColumna != 0) {
                    columnas = 0;
                }

                try {
                    if (indiceLocal.getClave().equalsIgnoreCase("y") || indiceLocal.getClave().equalsIgnoreCase("s")) {
                        indice.setClave(indiceLocal.getClave());
                    }
                } catch (NullPointerException e) {
                }

                if (tipo.equalsIgnoreCase("numero")) {

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("fecha")) {

                    String label = labels[i].getText();

                    labels[i].setText(label + "desde ");

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice() + "_DESDE");
                    indice.setTipo(tipo);
                    indice.setValor(fecha[i]);

                    indices.add(indice);

                    if (columnas >= 1) {
                        constraints.gridx = 0;
                        constraints.gridy = ++filas;
                        constraints.gridwidth = 1;
                        constraints.gridheight = 1;
                        constraints.fill = GridBagConstraints.BOTH;
                        panel.add(new JLabel(" "), constraints);
                        columnas = 0;
                        filas++;
                    } else {
                        columnas++;
                    }

                    i++;
                    indice = new Indice();
                    labels[i] = new JLabel(label + "hasta ");
                    labels[i].setHorizontalAlignment(JLabel.RIGHT);
                    fecha[i] = new JDateChooser();
                    fecha[i].setCalendar(null);

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice() + "_HASTA");
                    indice.setTipo(tipo);
                    indice.setValor(fecha[i]);
                    indice.setClave("");

                    i--;

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(combos[i]);

                    combos[i].setModel(llenarCombo(i, indiceLocal.getCodigo(), false));

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("area")) {

                    constraints.gridx = 0;
                    constraints.gridy = ++filas;
                    constraints.gridwidth = 1;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.BOTH;
                    panel.add(new JLabel(" "), constraints);

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(areaTexto[i]);

                    banderaColumna++;
                }

                indices.add(indice);
                banderaColumna++;

                if (columnas >= 1) {
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

            if (consultarExpediente != null) {
                consultarExpediente.setIndices(indices);
            } else if (consultaIndices != null) {
                consultaIndices.setIndices(indices);
            }

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }

    }

    /**
     * Busca todos los datos de los indices dinamicos correspondintes
     */
    private void buscarDatosIndices() {

        traza.trace("buscando argumentos del expediente " + expediente.getIdExpediente() + " idCategoria " + expediente.getIdCategoria(), Level.INFO);

        for (Indices a : listaIndices) {
            try {
                if (a.getClave().equalsIgnoreCase("y")) {
                    indicess = new BuscaIndice().buscaDatosIndice(expediente.getIdExpediente(), expediente.getIdLibreria(), expediente.getIdCategoria());
                    break;
                }
            } catch (SOAPException ex) {
                traza.trace("error al buscar los datos de los indices", Level.ERROR, ex);
            } catch (NullPointerException e) {
            }
        }

    }

    /**
     * Busca los datos de cada indice dinamico
     *
     * @param argumento El argumento el cual se buscara su informacion
     * @return Un objeto con la informacion del indice dinamico
     */
    private Object buscarDato(String argumento) {
        Object resp = new Object();
        traza.trace("buscando dato del argumento: " + argumento, Level.INFO);
        for (Indice ind : indicess) {
            String arg = ind.getIndice().replace("_", " ");
            if (arg.equalsIgnoreCase(argumento)) {
                resp = ind.getValor();
                traza.trace("valor del argumento: " + argumento + " es: " + resp, Level.INFO);
                break;
            }
        }
        return resp;
    }

    /**
     * Arma los indices dinamicos con su informacion correspondiente
     */
    private void llenarDatos() {
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Indice> indices = new ArrayList<>();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JTextField[] textos;
        JTextArea areaTexto[];
        JComboBox[] combos;
        JDateChooser fecha[];
        int i, banderaColumna = 0, filas = 0, columnas = 0, idCategoria, sizeList;

        try {

            idCategoria = expediente.getIdCategoria();
            listaIndices = new BuscaIndice().buscarIndice(idCategoria);
            sizeList = listaIndices.size();

            buscarDatosIndices();

            textos = new JTextField[sizeList];
            labels = new JLabel[sizeList];
            combos = new JComboBox[sizeList];
            areaTexto = new JTextArea[sizeList];
            fecha = new JDateChooser[sizeList];

            panel = new JPanel(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

            for (i = 0; i < sizeList; i++) {

                indice = new Indice();

                Indices indiceLocal = listaIndices.get(i);
                String arg = indiceLocal.getIndice().replace("_", " ");
                String tipo = indiceLocal.getTipo();
                indice.setClave("");

//                labels[i] = new JLabel(" " + crearEtiqueta(arg) + ": ");
                labels[i] = new JLabel(" " + arg + ": ");
                textos[i] = new JTextField(30);
                combos[i] = new JComboBox();
                areaTexto[i] = new JTextArea();
                fecha[i] = new JDateChooser();

                fecha[i].setCalendar(null);
                fecha[i].setDateFormatString("dd/MM/yyyy");

                labels[i].setHorizontalAlignment(JLabel.RIGHT);

                if (banderaColumna != 0) {
                    columnas = 0;
                }

                try {
                    if (indiceLocal.getClave().equalsIgnoreCase("y") || indiceLocal.getClave().equalsIgnoreCase("s")) {
                        indice.setClave(indiceLocal.getClave());
                    }
                } catch (NullPointerException e) {
                }

                if (tipo.equalsIgnoreCase("numero")) {

                    banderaColumna = 0;

                    try {
                        textos[i].setText(buscarDato(arg).toString());
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("fecha")) {

                    try {
//                        XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) buscarDato(arg);
//                        Calendar cal = xmlCal.toGregorianCalendar();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(buscarDato(arg).toString()));
                        
                        if (cal != null) {
                            fecha[i].setCalendar(cal);
                        } else {
                            fecha[i].setCalendar(null);
                        }
                    } catch (NullPointerException e) {
                        fecha[i].setCalendar(null);
                    } catch (ParseException ex) {
                            traza.trace("problemas en el parse de la fecha del indice del expediente", Level.ERROR, ex);
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(fecha[i]);

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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(combos[i]);

                    combos[i].setModel(llenarCombo(i, indiceLocal.getCodigo(), false));

                    combos[i].setSelectedItem(buscarDato(arg));

                } else if (tipo.equalsIgnoreCase("texto")) {

                    try {
                        textos[i].setText(buscarDato(arg).toString());
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(textos[i]);

                } else if (tipo.equalsIgnoreCase("area")) {

                    try {
                        areaTexto[i].setText(buscarDato(arg).toString());
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

                    indice.setIdCategoria(indiceLocal.getIdCategoria());
                    indice.setIdIndice(indiceLocal.getIdIndice());
                    indice.setIndice(indiceLocal.getIndice());
                    indice.setTipo(tipo);
                    indice.setValor(areaTexto[i]);
                }

                indices.add(indice);
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

            if (creaExpediente != null) {
                creaExpediente.setIndices(indices);
            } else if (guardarDoc != null) {
                guardarDoc.setIndices(indices);
            } else if (resultadoConsulta != null) {
                resultadoConsulta.setIndices(indices);
            }

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar los argumentos", Level.ERROR, ex);
        }

    }

    /**
     * LLena la lista desplegable con su informacion segun el codigo y la
     * posicion
     *
     * @param pos El indice de la lista del objeto JComboBox
     * @param codigo Codigo del Indice de la Categoria seleccionada en la base
     * de dato
     * @param bandera
     * @return
     */
    protected DefaultComboBoxModel llenarCombo(int pos, int codigo, boolean bandera) {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        try {
            List<Combo> datosCombo = new LLenarListaDesplegable().buscarData(codigo, bandera);
            modelo.addElement("");
            for (Combo combo : datosCombo) {
                modelo.addElement(combo.getDatoCombo());
            }

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar la informacion de la lista desplegable " + codigo, Level.INFO, ex);
        }
        return modelo;
    }

    /**
     * Crea la etiqueta del indice de la categoria, le elimina los underscore
     *
     * @param argu El nombre del indice
     * @return El indice en forma de etiqueta
     */
//    String crearEtiqueta(String argu) {
//
//        String arg = argu.replace("_", " ");
//        arg = arg.toLowerCase();
//        char[] cs = arg.toCharArray();
//        char ch = cs[0];
//        cs[0] = Character.toUpperCase(ch);
//        arg = String.valueOf(cs);
//
//        arg = arg.trim();
//        return arg;
//    }

    public String crearTituloExpediente() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String titulo = "", tmp = "";
        int cont = 0;

        for (Indice ind : indicess) {
            
            traza.trace("indice "+ind.getIndice()+" valor "+ind.getValor(), Level.INFO);
            
            if (ind.getClave().equalsIgnoreCase("y")) {
                
                if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                    
                    tmp = "Expediente: " + ind.getValor().toString() + " -";

                } else {
                    tmp = "Expediente: " + ind.getValor().toString() + " -";
                }
                
            } else if (ind.getClave().equalsIgnoreCase("s")) {
                
                if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                    
                    tmp = tmp + " " + ind.getValor().toString();

                } else {
                    tmp = tmp + " " + ind.getValor().toString();
                }
                
                cont++;
            }
            if (cont == 2) {
                titulo = tmp;
                break;
            }
        }
        traza.trace("titulo " + titulo, Level.INFO);
        return titulo;
    }
}
