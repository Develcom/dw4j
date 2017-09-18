/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.consulta;

import com.develcom.expediente.Categoria;
import com.develcom.administracion.Combo;
import com.develcom.autentica.Perfil;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.autentica.Indice;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.SubCategoria;
import com.develcom.expediente.TipoDocumento;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;
import ve.com.develcom.expediente.LLenarListaDesplegable;
import ve.com.develcom.sesion.IniciaSesion;

/**
 *
 * @author familia
 */
public class ConsultaTodos extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 1L;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(ConsultaTodos.class);
    private List<Perfil> perfiles;
    private List<Categoria> categorias = new ArrayList<>();
    private List<com.develcom.expediente.SubCategoria> subCategorias = new ArrayList<>();
    private List<TipoDocumento> tiposDocumentos;
    private List<Indice> indices = new ArrayList<>();
    private Expediente expe;

    /**
     * Creates new form ConsultaTodos
     */
    public ConsultaTodos() {
        initComponents();
        iniciar();
    }

    private void iniciar() {

        this.expe = new Expediente();
        ManejoSesion.setExpediente(expe);
        llenarcboLibreria();
        CentraVentanas.centrar(this, Principal.desktop);
        setTitle("Busqueda de todos los Expediente");
        this.setVisible(true);
    }

    /**
     * Llena la lista desplegables con las Librerias
     */
    private void llenarcboLibreria() {
        String lib = "";
        try {
            traza.trace("llenando lista de libreria", Level.INFO);
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            modelo.addElement("");

            traza.trace("buscando las libreria del usuario " + ManejoSesion.getLogin() + " con el perfil " + Constantes.ROL, Level.INFO);
            perfiles = new IniciaSesion().buscarLibCatIndicePerfil(ManejoSesion.getLogin(), Constantes.ROL);

            traza.trace("tamaño perfiles " + perfiles.size(), Level.INFO);

            for (Perfil perfil : perfiles) {
                String des = perfil.getLibreria().getDescripcion();
                if (!lib.equalsIgnoreCase(des)) {
                    lib = perfil.getLibreria().getDescripcion();
                    if (perfil.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        modelo.addElement(perfil.getLibreria().getDescripcion());
                    }
                }
            }

            cboLibreria.setModel(modelo);
            panelIndices.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelIndices.add(crearFormulario());

        } catch (SOAPException | SOAPFaultException | ConnectException e) {
            traza.trace("error al llenar lista de libreria", Level.INFO, e);
            JOptionPane.showMessageDialog(this, "Error al llenar lista de libreria\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearFormulario() {

        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel[] labels;
        JTextField[] textos;
        JTextArea areaTexto[];
        JComboBox[] combos;
        JDateChooser fecha[];
        String cat, ind = "";
        int banderaColumna = 0, filas = 0, columnas = 0, idCategoria, sizeList;

        for (int i = 0; i <= perfiles.size(); i++) {

            cat = perfiles.get(i).getCategoria().getCategoria();

            if ((!ind.equalsIgnoreCase(perfiles.get(i).getIndice().getIndice()))
                    && (cat.equalsIgnoreCase(perfiles.get(i + 1).getCategoria().getCategoria()))) {

                ind = perfiles.get(i).getIndice().getIndice();

                indices.add(perfiles.get(i).getIndice());
            } else {
                indices.add(perfiles.get(i).getIndice());
                break;
            }
        }

        sizeList = indices.size();
        textos = new JTextField[sizeList];
        labels = new JLabel[sizeList + 1];
        combos = new JComboBox[sizeList];
        areaTexto = new JTextArea[sizeList];
        fecha = new JDateChooser[sizeList + 1];

        panel.setLayout(gridBagLayout);
            panel.setBackground(new java.awt.Color(224, 239, 255));

        for (int i = 0; i < indices.size(); i++) {

            String arg = indices.get(i).getIndice().replace("_", " ");
            String tipo = indices.get(i).getTipo();

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

                indices.get(i).setValor(textos[i]);

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

                indices.get(i).setValor(fecha[i]);

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

                indices.get(i).setValor(fecha[i]);

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

                indices.get(i).setValor(combos[i]);

                combos[i].setModel(llenarCombo(i, indices.get(i).getCodigo(), false));

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

                indices.get(i).setValor(textos[i]);

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

                indices.get(i).setValor(areaTexto[i]);

                banderaColumna++;
            }

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
        return panel;
    }

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboLibreria = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCategorias = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSubCategorias = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstTipoDocumento = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAceptar = new javax.swing.JButton();
        btnRegresar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();

        setBackground(new java.awt.Color(224, 239, 255));

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));

        jLabel1.setText("Seleccione la Libreria");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLibreria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnBuscar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscar))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(224, 239, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione la Categoria, SubCategoria y el Tipo de Documento"));

        lstCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstCategoriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstCategorias);

        lstSubCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSubCategoriasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstSubCategorias);

        jScrollPane3.setViewportView(lstTipoDocumento);

        jLabel2.setText("Categorias");

        jLabel3.setText("SubCategorias");

        jLabel4.setText("Tipos de Documentos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Valid Green.png"))); // NOI18N
        btnAceptar.setMnemonic('a');
        btnAceptar.setText("Aceptar");
        btnAceptar.setToolTipText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        btnRegresar.setMnemonic('c');
        btnRegresar.setText("Cancelar");
        btnRegresar.setToolTipText("Cancelar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        jScrollPane4.setBackground(new java.awt.Color(224, 239, 255));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 723, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(panelIndices);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRegresar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("Consultar Todos Los Expedientes");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        consultar();
    }//GEN-LAST:event_btnAceptarActionPerformed

    /**
     * Obtiene los datos necesario para realizar la consulta dinamica
     */
    private void consultar() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<ConsultaDinamica> consultaDinamicas;

        HashMap<String, GregorianCalendar> fechas;// = new HashMap<String, GregorianCalendar>();
        List<HashMap<String, GregorianCalendar>> lstFechas = new ArrayList<>();

        Object[] subCateSel, tipoDocSel;

        List<TipoDocumento> listaTipoDoc = new ArrayList<>();
        List<SubCategoria> listaSubCat = new ArrayList<>();
        List<Categoria> listaCat = new ArrayList<>();
        com.develcom.expediente.Indice indice;
        List<com.develcom.expediente.Indice> listaIndices = new ArrayList<>();

        JDateChooser jDateChooser;
        JComboBox jComboBox;
        JTextField jTextField;
        JTextArea jTextArea;

        int size, contNull = 0, contDato;
        boolean vacio;

        try {

            for (Indice ind : indices) {
                indice = new com.develcom.expediente.Indice();
                indice.setUpdateIndices(false);

                if (ind.getTipo().equalsIgnoreCase("texto")) {

                    jTextField = (JTextField) ind.getValor();

                    indice.setIdCategoria(ind.getIdCategoria());
                    indice.setIdIndice(ind.getIdIndice());
                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(jTextField.getText());
                    listaIndices.add(indice);

                } else if (ind.getTipo().equalsIgnoreCase("combo")) {

                    jComboBox = (JComboBox) ind.getValor();

                    indice.setIdCategoria(ind.getIdCategoria());
                    indice.setIdIndice(ind.getIdIndice());
                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(jComboBox.getSelectedItem().toString());
                    listaIndices.add(indice);

                } else if (ind.getTipo().equalsIgnoreCase("fecha")) {

                    try {

                        fechas = new HashMap<>();

                        jDateChooser = (JDateChooser) ind.getValor();
                        GregorianCalendar fecha = (GregorianCalendar) jDateChooser.getCalendar();

                        fechas.put(ind.getIndice(), fecha);
                        lstFechas.add(fechas);

                        indice.setIdCategoria(ind.getIdCategoria());
                        indice.setIdIndice(ind.getIdIndice());
                        indice.setIndice(ind.getIndice());
                        indice.setClave(ind.getClave());
                        indice.setTipo(ind.getTipo());
                        indice.setValor(sdf.format(fecha.getTime()));
                        listaIndices.add(indice);

                    } catch (NullPointerException e) {

                        indice.setIdCategoria(ind.getIdCategoria());
                        indice.setIdIndice(ind.getIdIndice());
                        indice.setIndice(ind.getIndice());
                        indice.setClave(ind.getClave());
                        indice.setTipo(ind.getTipo());
                        listaIndices.add(indice);

                    }

                } else if (ind.getTipo().equalsIgnoreCase("numero")) {

                    jTextField = (JTextField) ind.getValor();

                    indice.setIdCategoria(ind.getIdCategoria());
                    indice.setIdIndice(ind.getIdIndice());
                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(jTextField.getText());
                    listaIndices.add(indice);

                } else if (ind.getTipo().equalsIgnoreCase("area")) {

                    jTextArea = (JTextArea) ind.getValor();

                    indice.setIdCategoria(ind.getIdCategoria());
                    indice.setIdIndice(ind.getIdIndice());
                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(jTextArea.getText());
                    listaIndices.add(indice);
                }
            }

            if (!lstCategorias.isSelectionEmpty()) {
                subCateSel = lstCategorias.getSelectedValues();

                for (Categoria cate : categorias) {
                    for (Object obj : subCateSel) {

                        if (cate.getCategoria().equalsIgnoreCase(obj.toString())) {
                            Categoria sc1 = new Categoria();
                            sc1.setEstatus(cate.getEstatus());
                            sc1.setIdCategoria(cate.getIdCategoria());
                            sc1.setIdCategoria(cate.getIdCategoria());
                            sc1.setCategoria(cate.getCategoria());
                            listaCat.add(sc1);
                        }
                    }
                }
            }

            if (!lstSubCategorias.isSelectionEmpty()) {
                subCateSel = lstSubCategorias.getSelectedValues();

                for (SubCategoria sc : subCategorias) {
                    for (Object obj : subCateSel) {

                        if (sc.getSubCategoria().equalsIgnoreCase(obj.toString())) {
                            SubCategoria sc1 = new SubCategoria();
                            sc1.setEstatus(sc.getEstatus());
                            sc1.setIdCategoria(sc.getIdCategoria());
                            sc1.setIdSubCategoria(sc.getIdSubCategoria());
                            sc1.setSubCategoria(sc.getSubCategoria());
                            listaSubCat.add(sc1);
                        }
                    }
                }
            }

            if (!lstTipoDocumento.isSelectionEmpty()) {
                tipoDocSel = lstTipoDocumento.getSelectedValues();

                for (TipoDocumento td : tiposDocumentos) {
                    for (Object obj : tipoDocSel) {
                        String tipoDoc = td.getTipoDocumento().trim();
                        String tipoDocObj = obj.toString().trim();
                        if (tipoDoc.equalsIgnoreCase(tipoDocObj)) {
                            listaTipoDoc.add(td);
                        }
                    }
                }
            }

            size = listaIndices.size();

            for (com.develcom.expediente.Indice ind : listaIndices) {
                if ((ind.getValor() == null) || (ind.getValor().equals(""))) {
                    contNull++;
                }
            }

            vacio = contNull != size;
//            if(contNull == size){
//                vacio=false;
//            }else{
//                vacio=true;
//            }

            if (vacio) {
                if (comprobarFechas(lstFechas, listaIndices)) {

                    consultaDinamicas = new BuscaExpedienteDinamico().consultarExpedienteDinamico(listaIndices, listaCat, listaSubCat, listaTipoDoc, 1, expe.getIdLibreria());

//                    if (!consultaDinamicas.isEmpty()) {
                    if (consultaDinamicas.get(0).isExiste()) {
                        expe.setSubCategorias(convertir(listaSubCat));
                        expe.setTipoDocumentos(listaTipoDoc);
                        ManejoSesion.setExpediente(expe);

                        ManejoSesion.setSizeSearch(consultaDinamicas.size());

                        traza.trace("exito al en la consulta dinamica ", Level.INFO);

                        this.dispose();

                        ResultadoExpediente re = new ResultadoExpediente(consultaDinamicas);

                        Principal.desktop.add(re);
                    } else {
                        throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
                        //throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
                    }
//                    } else {
//                        //throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
//                        throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
//                    }

                } else {
                    throw new DW4JDesktopExcepcion("Problemas con los rango de fechas");
                }
            } else {
                throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
            }

        } catch (SOAPException e) {
            traza.trace("error en la busqueda dinamica ", Level.INFO, e);
        } catch (DW4JDesktopExcepcion e) {
            traza.trace("error en la busqueda dinamica ", Level.INFO, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Verifica los rangos de las fechas
     *
     * @param lstFechas Lista con datos de fechas a consultar
     * @param listaIndices Lista de indices para la busqueda y comparacion
     * @return Siempre devuelve verdadero, solo devuelva falso cuando los rango
     * de fechas estan errados
     */
    private boolean comprobarFechas(List<HashMap<String, GregorianCalendar>> lstFechas, List<com.develcom.expediente.Indice> listaIndices) {

        boolean resp = true;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lstFechas.size(); i++) {

            HashMap<String, GregorianCalendar> fecha = lstFechas.get(i);

            for (com.develcom.expediente.Indice ind : listaIndices) {

                String etiDesde = ind.getIndice();
                int desde = etiDesde.indexOf("DESDE");

                if (desde != -1) {

                    String lblDesde = etiDesde.substring(0, desde - 1);
                    GregorianCalendar fechDesde = fecha.get(etiDesde);

                    for (int j = 0; j < lstFechas.size(); j++) {
                        HashMap<String, GregorianCalendar> fech = lstFechas.get(j);

                        for (com.develcom.expediente.Indice ind1 : listaIndices) {

                            String etiHasta = ind1.getIndice();
                            int hasta = etiHasta.indexOf("HASTA");

                            if (hasta != -1) {
                                String lblHasta = etiHasta.substring(0, hasta - 1);
                                GregorianCalendar fechHasta = fech.get(etiHasta);

                                if (lblHasta.equalsIgnoreCase(lblDesde)) {
                                    if (fechHasta != null) {
                                        if (fechDesde != null) {
                                            if (fechDesde.after(fechHasta)) {
                                                resp = false;
                                                traza.trace("problema de fechas", Level.INFO);
                                                traza.trace(etiDesde + " - " + sdf.format(fechDesde.getTime()), Level.INFO);
                                                traza.trace(etiHasta + " - " + sdf.format(fechHasta.getTime()), Level.INFO);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return resp;
    }
    
    private List<com.develcom.administracion.SubCategoria> convertir(List<SubCategoria> listaSubCat) {
        com.develcom.administracion.SubCategoria sc = new com.develcom.administracion.SubCategoria();
        List<com.develcom.administracion.SubCategoria> scs = new ArrayList<>();

        for (SubCategoria subCat : listaSubCat) {
            sc.setEstatus(subCat.getEstatus());
            sc.setIdCategoria(subCat.getIdCategoria());
            sc.setIdSubCategoria(subCat.getIdSubCategoria());
            sc.setSubCategoria(subCat.getSubCategoria());
            scs.add(sc);
        }

        return scs;
    }
    
    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed

        dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        Expediente expediente = new Expediente();
        String lib;
        int idLib;
        DefaultListModel lm = new DefaultListModel();

        try {
            lib = cboLibreria.getSelectedItem().toString();

            traza.trace("buscando las categorias de la libreria " + lib, Level.INFO);

            for (Perfil perfil : perfiles) {

                if (perfil.getLibreria().getDescripcion().equalsIgnoreCase(lib)) {
                    idLib = perfil.getLibreria().getIdLibreria();
                    expe.setIdLibreria(idLib);
                    expediente.setIdLibreria(idLib);
                    expediente.setLibreria(lib);
                    categorias =  convertirCategoria(new AdministracionBusqueda().buscarCategoria(null, 0, idLib));
                    break;
                }
            }

            for (Categoria sc : categorias) {

                if (sc.getEstatus().equalsIgnoreCase("activo")) {
                    lm.addElement(sc.getCategoria().trim());
                }

            }
            lstCategorias.setModel(lm);
            lstSubCategorias.setModel(new DefaultListModel());
            lstTipoDocumento.setModel(new DefaultListModel());

        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al buscar las categorias ", Level.ERROR, ex);
        }

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void lstSubCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstSubCategoriasMouseClicked

        LlenarTipoDocumentos();
    }//GEN-LAST:event_lstSubCategoriasMouseClicked

    private void lstCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCategoriasMouseClicked

        LlenarSubCategorias();
    }//GEN-LAST:event_lstCategoriasMouseClicked

    private List<Categoria> convertirCategoria(List<com.develcom.administracion.Categoria> cs){
        
        List<Categoria> cats = new ArrayList<>();
        Categoria categoria;
        
        for (com.develcom.administracion.Categoria c : cs) {
            categoria = new Categoria();
            categoria.setCategoria(c.getCategoria());
            categoria.setEstatus(c.getEstatus());
            categoria.setIdCategoria(c.getIdCategoria());
            cats.add(categoria);
        }
        
        return cats;
    }
    
    
    private void LlenarSubCategorias() {

        DefaultListModel lm = new DefaultListModel();
        Object[] catSel;
//        int i = 0;
        List<Integer> idSubCate = new ArrayList<>();

        try {
            if (lstCategorias.getSelectedValue() != null) {
                catSel = lstCategorias.getSelectedValues();

                for (Categoria sc : categorias) {
                    for (Object obj : catSel) {

                        if (sc.getCategoria().equalsIgnoreCase(obj.toString())) {
                            idSubCate.add(sc.getIdCategoria());
                        }
                    }
                }

                subCategorias = new BuscaExpedienteDinamico().encontrarSubCategorias(idSubCate);
                for (com.develcom.expediente.SubCategoria td : subCategorias) {
                    lm.addElement(td.getSubCategoria().trim());
                }
                lstSubCategorias.setModel(lm);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }

    private void LlenarTipoDocumentos() {

        DefaultListModel lm = new DefaultListModel();
        Object[] subCatSel;
//        int i = 0;
        List<Integer> idSubCate = new ArrayList<>();

        try {
            if (lstSubCategorias.getSelectedValue() != null) {
                subCatSel = lstSubCategorias.getSelectedValues();

                for (com.develcom.expediente.SubCategoria sc : subCategorias) {
                    for (Object obj : subCatSel) {

                        if (sc.getSubCategoria().equalsIgnoreCase(obj.toString())) {
                            idSubCate.add(sc.getIdSubCategoria());
                        }
                    }
                }

                tiposDocumentos = new BuscaExpedienteDinamico().buscarTiposDocumentos(idSubCate);
                for (com.develcom.expediente.TipoDocumento td : tiposDocumentos) {
                    lm.addElement(td.getTipoDocumento().trim());
                }
                lstTipoDocumento.setModel(lm);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cboLibreria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> lstCategorias;
    private javax.swing.JList<String> lstSubCategorias;
    private javax.swing.JList<String> lstTipoDocumento;
    private javax.swing.JPanel panelIndices;
    // End of variables declaration//GEN-END:variables
}
