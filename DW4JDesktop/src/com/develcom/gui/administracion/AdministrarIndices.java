/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AdministrarIndices.java
 *
 * Created on 14/03/2012, 04:12:50 PM
 */
package com.develcom.gui.administracion;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionAgregar;
import ve.com.develcom.administracion.AdministracionBusqueda;
import com.develcom.administracion.Indice;
import com.develcom.administracion.Categoria;
import com.develcom.administracion.Libreria;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.tools.UtilidadPalabras;
import com.develcom.tools.trazas.Traza;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author develcom
 */
public class AdministrarIndices extends javax.swing.JInternalFrame {

    /**
     * modelo para la tabla
     */
    private ModeloTabla modelTable;
    /**
     * modelo para la tabla
     */
    private ModeloTabla modelTableIndicesExistentes;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(AdministrarIndices.class);
    /**
     * Lista de Librerias
     */
    private List<Libreria> librerias;
    /**
     * Lista de Categorias
     */
    private List<Categoria> categorias;
    /**
     * Captura la Libreria y la Categoria
     */
    private static String libreria, categoria;
    /**
     * Lista de Argumentos
     */
    private List<Indice> indices;
    /**
     * Para agregar nuevos Argumentos
     */
    private boolean existe = false;

    /**
     * Creates new form AdministrarIndices
     */
    public AdministrarIndices() {
        initComponents();
        grupoBotonesClaves.add(jrbPrimario);
        grupoBotonesClaves.add(jrbSecundario);
        grupoBotonesClaves.add(jrbTerciario);
        grupoBotonesClaves.add(jrbCuaternario);
        //jlInformacion.setText("El nombre del indice debe estar separado \n por underscore y maximo 2 palabras");

        tablaIndice.setModel(armarTabla());
        tablaIndicesExistentes.setModel(armarTablaExistentes());

        llenarComboTipoDato();
        llenarComboLibreria();

        setTitle("Administrar Indices");
        CentraVentanas.centrar(this, Principal.desktop);
        this.setVisible(true);
    }

    /**
     * Coloca los titulos a la tablas
     *
     * @return DefaultTableModel
     */
    private DefaultTableModel armarTabla() {

        if (modelTable == null) {
            modelTable = new ModeloTabla();

            modelTable.addColumn("Nombre");
            modelTable.addColumn("ID");
            modelTable.addColumn("Obligatorio");
            modelTable.addColumn("Tipo de dato");
        } else {
            modelTable.eliminarFilas();
        }

        return modelTable;
    }

    private DefaultTableModel armarTablaExistentes() {

        if (modelTableIndicesExistentes == null) {
            modelTableIndicesExistentes = new ModeloTabla();
            modelTableIndicesExistentes.addColumn("Nombre");
            modelTableIndicesExistentes.addColumn("ID");
            modelTableIndicesExistentes.addColumn("Obligatorio");
            modelTableIndicesExistentes.addColumn("Tipo de dato");
        } else {
            modelTableIndicesExistentes.eliminarFilas();
        }

        return modelTableIndicesExistentes;
    }

    /**
     * Llena la lista desplegables de Tipo de Dato
     */
    private void llenarComboTipoDato() {

        Vector<String> tipos = new Vector<String>();
        tipos.add("");
        tipos.add("TEXTO");
        tipos.add("NUMERO");
        tipos.add("FECHA");
        tipos.add("COMBO");
        tipos.add("AREA");

        DefaultComboBoxModel model = new DefaultComboBoxModel(tipos);
        cmbTipoDato.setModel(model);
    }

    /**
     * Llena la lista desplegable de las Librerias
     */
    private void llenarComboLibreria() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            model.addElement("");

            categorias = null;

            librerias = new AdministracionBusqueda().buscarLibreria("", 0);

            traza.trace("buscando las libreria ", Level.INFO);

            for (Libreria l : librerias) {
                model.addElement(l.getDescripcion());
            }
            cmbLibreria.setModel(model);
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoBotonesClaves = new javax.swing.ButtonGroup();
        panelInferior = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaIndice = new javax.swing.JTable();
        jbSalvar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        panelSuperior = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbLibreria = new javax.swing.JComboBox();
        cmbCategoria = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        panelArgExistentes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaIndicesExistentes = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jlNota = new javax.swing.JLabel();
        panelOpciones = new javax.swing.JPanel();
        jrbPrimario = new javax.swing.JRadioButton();
        jrbSecundario = new javax.swing.JRadioButton();
        jrbTerciario = new javax.swing.JRadioButton();
        jrbCuaternario = new javax.swing.JRadioButton();
        jbAgregar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtIndice = new javax.swing.JTextField();
        chkObligatorio = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        cmbTipoDato = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Administrar Indices");

        panelInferior.setBackground(new java.awt.Color(224, 239, 255));

        tablaIndice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane2.setViewportView(tablaIndice);

        jbSalvar.setText("Guardar");
        jbSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarActionPerformed(evt);
            }
        });

        jbCancelar.setText("Cerrar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInferiorLayout = new javax.swing.GroupLayout(panelInferior);
        panelInferior.setLayout(panelInferiorLayout);
        panelInferiorLayout.setHorizontalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInferiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(panelInferiorLayout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jbSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbCancelar)
                .addGap(112, 112, 112))
        );
        panelInferiorLayout.setVerticalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInferiorLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSalvar)
                    .addComponent(jbCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSuperior.setBackground(new java.awt.Color(224, 239, 255));

        jLabel1.setText("Libreria");

        cmbLibreria.setPreferredSize(new java.awt.Dimension(500, 20));
        cmbLibreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLibreriaActionPerformed(evt);
            }
        });

        cmbCategoria.setPreferredSize(new java.awt.Dimension(500, 20));
        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        jLabel2.setText("Categoria");

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmbLibreria, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(cmbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSuperiorLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelArgExistentes.setBackground(new java.awt.Color(224, 239, 255));

        tablaIndicesExistentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaIndicesExistentes);

        javax.swing.GroupLayout panelArgExistentesLayout = new javax.swing.GroupLayout(panelArgExistentes);
        panelArgExistentes.setLayout(panelArgExistentesLayout);
        panelArgExistentesLayout.setHorizontalGroup(
            panelArgExistentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelArgExistentesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelArgExistentesLayout.setVerticalGroup(
            panelArgExistentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelArgExistentesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(224, 239, 255));

        jlNota.setText("Recuerde el orden de los Indices al crearlos, desde el principal hasta el cuarto");

        panelOpciones.setBackground(new java.awt.Color(222, 230, 238));
        panelOpciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Orden de despliege de los Indices"));

        jrbPrimario.setBackground(new java.awt.Color(222, 230, 238));
        jrbPrimario.setText("Principal");

        jrbSecundario.setBackground(new java.awt.Color(222, 230, 238));
        jrbSecundario.setText("2do");

        jrbTerciario.setBackground(new java.awt.Color(222, 230, 238));
        jrbTerciario.setText("3ro");

        jrbCuaternario.setBackground(new java.awt.Color(222, 230, 238));
        jrbCuaternario.setText("4to");

        javax.swing.GroupLayout panelOpcionesLayout = new javax.swing.GroupLayout(panelOpciones);
        panelOpciones.setLayout(panelOpcionesLayout);
        panelOpcionesLayout.setHorizontalGroup(
            panelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpcionesLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jrbPrimario)
                .addGap(18, 18, 18)
                .addComponent(jrbSecundario)
                .addGap(18, 18, 18)
                .addComponent(jrbTerciario)
                .addGap(18, 18, 18)
                .addComponent(jrbCuaternario)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        panelOpcionesLayout.setVerticalGroup(
            panelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOpcionesLayout.createSequentialGroup()
                .addGroup(panelOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrbPrimario)
                    .addComponent(jrbSecundario)
                    .addComponent(jrbTerciario)
                    .addComponent(jrbCuaternario))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jbAgregar.setText("Agregar");
        jbAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlNota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(panelOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlNota)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jbAgregar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(224, 239, 255));

        jLabel6.setText("Nombre del Indice");

        chkObligatorio.setBackground(new java.awt.Color(224, 239, 255));
        chkObligatorio.setText("Obligatorio");
        chkObligatorio.setToolTipText("Obligatorio");

        jLabel4.setText("Tipo de Dato");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIndice)
                    .addComponent(cmbTipoDato, 0, 375, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkObligatorio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIndice, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkObligatorio)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbTipoDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelArgExistentes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelArgExistentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed

        this.dispose();
}//GEN-LAST:event_jbCancelarActionPerformed

    private void jbAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAgregarActionPerformed

        agregarIndice();
    }//GEN-LAST:event_jbAgregarActionPerformed

    /**
     * Agrega el nuevo indice a la tabla
     */
    private void agregarIndice() {

        Vector ind;
        Vector filas = new Vector();
        Vector ver;
        String argument, tipo;
        boolean banLista = true, banTabla = true;

        try {

            libreria = cmbLibreria.getSelectedItem().toString();

            if (!libreria.equalsIgnoreCase("")) {

                categoria = cmbCategoria.getSelectedItem().toString();

                if (!categoria.equalsIgnoreCase("")) {

                    tipo = cmbTipoDato.getSelectedItem().toString();

                    if (!tipo.equalsIgnoreCase("")) {

                        argument = txtIndice.getText();

                        if (!argument.equalsIgnoreCase("")) {

                            filas.add(argument);

                            if (!existe) {
                                ind = modelTable.getDataVector();
                                if (!ind.isEmpty()) {
                                    if (ind.size() < 4) {
                                        for (int i = 0; i < ind.size(); i++) {
                                            Vector v = (Vector) ind.get(i);
                                            String idIndices = v.get(1).toString();
                                            if (!idIndices.equalsIgnoreCase(jrbPrimario.getText())) {
                                                if (!jrbPrimario.isSelected()) {
                                                    throw new DW4JDesktopExcepcion("Debe seleccionar el id principal primero");
                                                } else {
                                                    filas.add(jrbPrimario.getText());
                                                    jrbPrimario.setEnabled(false);
                                                    chkObligatorio.setSelected(true);
                                                }
                                            } else if (idIndices.equalsIgnoreCase(jrbPrimario.getText())) {

                                                if (ind.size() != 1) {
                                                    v = (Vector) ind.get(++i);
                                                    idIndices = v.get(1).toString();
                                                }

                                                if (!idIndices.equalsIgnoreCase(jrbSecundario.getText())) {
                                                    if (!jrbSecundario.isSelected()) {
                                                        throw new DW4JDesktopExcepcion("Debe seleccionar el segundo id despues del principal");
                                                    } else {
                                                        filas.add(jrbSecundario.getText());
                                                        jrbSecundario.setEnabled(false);
                                                        chkObligatorio.setSelected(true);
                                                    }
                                                } else if (idIndices.equalsIgnoreCase(jrbSecundario.getText())) {

                                                    if (ind.size() != 2) {
                                                        v = (Vector) ind.get(++i);
                                                        idIndices = v.get(1).toString();
                                                    }

                                                    if (!idIndices.equalsIgnoreCase(jrbTerciario.getText())) {
                                                        if (!jrbTerciario.isSelected()) {
                                                            throw new DW4JDesktopExcepcion("Debe seleccionar el tercero id despues del segundo");
                                                        } else {
                                                            filas.add(jrbTerciario.getText());
                                                            jrbTerciario.setEnabled(false);
                                                            chkObligatorio.setSelected(true);
                                                        }
                                                    } else if (idIndices.equalsIgnoreCase(jrbTerciario.getText())) {
                                                        if (ind.size() != 3) {
                                                            v = (Vector) ind.get(++i);
                                                            idIndices = v.get(1).toString();
                                                        }

                                                        if (!idIndices.equalsIgnoreCase(jrbCuaternario.getText())) {
                                                            if (!jrbCuaternario.isSelected()) {
                                                                throw new DW4JDesktopExcepcion("Debe seleccionar el cuarto id despues del tercero");
                                                            } else {
                                                                filas.add(jrbCuaternario.getText());
                                                                jrbCuaternario.setEnabled(false);
                                                                chkObligatorio.setSelected(true);
                                                            }
                                                        } else if (idIndices.equalsIgnoreCase(jrbCuaternario.getText())) {
                                                            break;
                                                        }
                                                    }
                                                }

                                            } else {
                                                break;
                                            }
                                        }
                                    } else {
                                        filas.add("");
                                    }

                                } else {

                                    if (!jrbPrimario.isSelected()) {
                                        throw new DW4JDesktopExcepcion("Debe seleccionar el id principal primero");
                                    } else {
                                        filas.add(jrbPrimario.getText());
                                        jrbPrimario.setEnabled(false);
                                        chkObligatorio.setSelected(true);
                                    }
                                }
                            } else {
                                filas.add("adcional");
                            }

                            if (chkObligatorio.isSelected()) {
                                filas.add("si");
                                chkObligatorio.setSelected(false);
                            } else {
                                filas.add("no");
                                chkObligatorio.setSelected(false);
                            }

                            filas.add(tipo);

                            for (Indice arg : indices) {
                                String argu = arg.getIndice();
                                if (argu.equalsIgnoreCase(argument)) {
                                    banLista = false;
                                    break;
                                } else {
                                    banLista = true;
                                }
                            }

                            if (modelTable != null) {
                                ver = modelTable.getDataVector();
                                for (int i = 0; i < ver.size(); i++) {
                                    Vector d = (Vector) ver.get(i);
                                    String subCat = d.get(0).toString();
                                    if (subCat.equalsIgnoreCase(argument)) {
                                        banTabla = false;
                                        break;
                                    }
                                }
                            }

                            if (banTabla) {

                                if (banLista) {
                                    modelTable.addRow(filas);
                                    tablaIndice.setModel(modelTable);
                                    txtIndice.setText("");
                                    cmbTipoDato.setSelectedIndex(0);
                                } else {
                                    JOptionPane.showMessageDialog(this, "El indice ya existe", "Advertencia", JOptionPane.WARNING_MESSAGE);
                                }

                            } else {
                                JOptionPane.showMessageDialog(this, "Ya el indice se encuentra en la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE);

                            }
//                            } else {
//                                throw new DW4JDesktopExcepcion("Caracteres invalidos en el indice \nsolo letras, numeros y un underscore");
//                            }
//                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe colocar un indice \n Recuerde al menos 2 palabras separadas por underscore");
                        }
                    } else {
                        throw new DW4JDesktopExcepcion("Debe seleccionar un tipo de dato");
                    }

                } else {
                    throw new DW4JDesktopExcepcion("Debe seleccionar una categoria");
                }

            } else {
                throw new DW4JDesktopExcepcion("Debe seleccionar una libreria");
            }

        } catch (DW4JDesktopExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            //limpiarComponentes();
            //inactivarComponentes();
        } catch (Exception e) {
            traza.trace("Error general", Level.ERROR, e);
        }

    }

    /**
     * Procesa la categoria para crear el nombre de la tabla
     *
     * @param categoria Nombre de la Categoria
     * @return Nombre de la Categoria modificada
     */
    private String procesarCaregoria(String categoria) {

        categoria = categoria.replace("de ", "");
        categoria = categoria.replace("la ", "");
        categoria = categoria.replace("y ", "");
        categoria = categoria.replace("ñ", "n");
        categoria = categoria.replace("Ñ", "N");
        categoria = categoria.replace(" ", "_");
        categoria = UtilidadPalabras.buscarAcentos(categoria);

        String tmp[] = categoria.split("_");
        if (tmp.length == 1) {
            categoria = tmp[0];
        } else if (tmp.length == 2) {
            categoria = tmp[0] + "_" + tmp[1];
        } else if (tmp.length == 3) {
            categoria = tmp[0] + "_" + tmp[1] + "_" + tmp[2];
        } else if (tmp.length >= 4) {
            categoria = tmp[0] + "_" + tmp[1] + "_" + tmp[2] + "_" + tmp[3];
        }
        categoria = categoria.toUpperCase();

        return categoria;
    }

    private void jbSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarActionPerformed
        guardarIndices();
    }//GEN-LAST:event_jbSalvarActionPerformed

    /**
     * Guarda los nuevos Argumentos
     */
    private void guardarIndices() {

        int idCategoria = 0, idLibreria = 0;
        List<Indice> listaIndices = new ArrayList<>();
        Indice argumento;
        boolean resp = false;
        String principal = "";

        try {
            Vector datos = modelTable.getDataVector();

            if (modelTableIndicesExistentes != null) {
                Vector data = modelTableIndicesExistentes.getDataVector();
                for (int i = 0; i < data.size(); i++) {
                    Vector dato = (Vector) data.get(i);
                    if (dato.get(1).toString().equalsIgnoreCase("principal")) {
                        principal = jrbPrimario.getText();
                        break;
                    }
                }
            }

            int n = JOptionPane.showOptionDialog(this, "Seguro que desea guardar los Indices",
                    "¿?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"SI", "NO"}, "NO");

            if (n == JOptionPane.YES_OPTION) {

                if (!principal.equalsIgnoreCase(jrbPrimario.getText())) {
                    Vector v = (Vector) datos.get(0);
                    principal = v.get(1).toString();
                }

                if (!datos.isEmpty()) {

                    if (principal.equalsIgnoreCase(jrbPrimario.getText())) {

                        for (Categoria c : categorias) {
                            if (categoria.equalsIgnoreCase(c.getCategoria())) {
                                idCategoria = c.getIdCategoria();
                                break;
                            }
                        }

                        for (Libreria l : librerias) {
                            if (libreria.equalsIgnoreCase(l.getDescripcion())) {
                                idLibreria = l.getIdLibreria();
                                break;
                            }
                        }

                        for (int i = 0; i < datos.size(); i++) {

                            Vector vArg = (Vector) datos.get(i);
                            argumento = new Indice();

                            argumento.setIndice(vArg.get(0).toString());
                            argumento.setIdCategoria(idCategoria);

                            if (vArg.get(1).toString().equalsIgnoreCase(jrbPrimario.getText())) {
                                argumento.setClave("Y");
                            } else if (vArg.get(1).toString().equalsIgnoreCase(jrbSecundario.getText())) {
                                argumento.setClave("S");
                            } else if (vArg.get(1).toString().equalsIgnoreCase(jrbTerciario.getText())) {
                                argumento.setClave("S");
                            } else if (vArg.get(1).toString().equalsIgnoreCase(jrbCuaternario.getText())) {
                                argumento.setClave("S");
                            } else if (vArg.get(2).toString().equalsIgnoreCase("si")) {
                                argumento.setClave("O");
                            } else {
                                argumento.setClave("");
                            }

                            argumento.setTipo(vArg.get(3).toString());

                            listaIndices.add(argumento);
                        }

                        resp = new AdministracionAgregar().guardarIndices(listaIndices);

                        if (resp) {
                            JOptionPane.showMessageDialog(this, "Argumentos agregados con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                            limpiarComponentes();
                        } else {
                            JOptionPane.showMessageDialog(this, "Problemas al agregar los Argumentos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            limpiarComponentes();
                        }
//                        } else {
//
//                            for (int i = 0; i < datos.size(); i++) {
//                                Vector vArg = (Vector) datos.get(i);
//                                argumento = new Argumentos();
//
//                                argumento.setArgumento(vArg.get(0).toString());
//                                argumento.setIdCategoria(idCategoria);
//                                
//                                if (vArg.get(1).toString().equalsIgnoreCase(jrbPrimario.getText())) {
//                                    argumento.setClave("y");
//                                } else if (vArg.get(1).toString().equalsIgnoreCase(jrbSecundario.getText())) {
//                                    argumento.setClave("s");
//                                } else if (vArg.get(1).toString().equalsIgnoreCase(jrbTerciario.getText())) {
//                                    argumento.setClave("s");
//                                } else if (vArg.get(1).toString().equalsIgnoreCase(jrbCuaternario.getText())) {
//                                    argumento.setClave("s");
//                                } else if (vArg.get(2).toString().equalsIgnoreCase("si")) {
//                                    argumento.setClave("o");
//                                } else {
//                                    argumento.setClave("");
//                                }
//
//                                argumento.setTipo(vArg.get(3).toString());
//
//                                listaArgumentos.add(argumento);
//                            }
//
//                            resp = new AdministracionAgregar().guardarIndices(listaArgumentos);
//
//                            if (resp) {
//                                JOptionPane.showMessageDialog(this, "Argumentos guardados con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
//                                limpiarComponentes();
//                            } else {
//                                JOptionPane.showMessageDialog(this, "Problemas al guardar los Argumentos", "Advertencia", JOptionPane.WARNING_MESSAGE);
//                                limpiarComponentes();
//                            }
//                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "La tabla debe tener el indice principal", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "La tabla de Indices esta vacia \n no se pueder guardar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }

    private void cmbLibreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLibreriaActionPerformed

        boolean llenar = false;
        String lib = cmbLibreria.getSelectedItem().toString();
        tablaIndicesExistentes.setModel(armarTablaExistentes());

        for (Libreria l : librerias) {
            if (lib.equalsIgnoreCase(l.getDescripcion())) {
                traza.trace("libreria selecionada " + l.getDescripcion() + " su id es " + l.getIdLibreria(), Level.INFO);
                llenar = llenarComboCategoria(l.getDescripcion(), l.getIdLibreria());
                break;
            }
        }

        if (!llenar) {
            JOptionPane.showMessageDialog(this, "La Libreria " + lib + "\n no posee categorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_cmbLibreriaActionPerformed

    /**
     * Llena la lista desplegables de Categorias
     *
     * @param lib Nombre de la Libreria
     * @param idLibreria El id de la Libreria
     * @return Verdadero si tiene categorias, falso en caso contrario
     */
    private boolean llenarComboCategoria(String lib, int idLibreria) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        boolean resp = false;
        try {

            model.addElement("");

            traza.trace("buscando las categorias de la libreria: " + lib, Level.INFO);
            traza.trace("id de la libreria: " + idLibreria, Level.INFO);
            modelTable = null;
            tablaIndice.setModel(armarTabla());

            categorias = new AdministracionBusqueda().buscarCategoria("", idLibreria, 0);

            if (!categorias.isEmpty()) {
                resp = true;
                for (Categoria c : categorias) {
                    if (c.getEstatus().equalsIgnoreCase("Activo")) {
                        model.addElement(c.getCategoria());
                    }
                }
                cmbCategoria.setModel(model);
                activarComponentes();
            } else {
                resp = false;
                //JOptionPane.showMessageDialog(this, "La Libreria "+lib +"\n no posee categorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
        return resp;
    }

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed

        traza.trace("llena la tabla de categorias", Level.INFO);

        llenarTablaIndices();

    }//GEN-LAST:event_cmbCategoriaActionPerformed

    /**
     * Llena la tabla con los Argumentos existentes de la Categoria seleccionada
     */
    private void llenarTablaIndices() {

        Vector filas;
        int cont = 1;

        try {

            String cate = cmbCategoria.getSelectedItem().toString();

            tablaIndicesExistentes.setModel(armarTablaExistentes());
            modelTable = null;
            tablaIndice.setModel(armarTabla());

            //jlCategoria.setText("Indices existentes para la categoria: "+cate);
            panelArgExistentes.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices existentes " + cate));
            for (Categoria cat : categorias) {
                if (cat.getCategoria().equalsIgnoreCase(cate)) {
                    traza.trace("buscando los argumentos de la categoria " + cate + " id " + cat.getIdCategoria(), Level.INFO);
                    indices = new AdministracionBusqueda().buscandoIndices(cat.getIdCategoria());
                    break;
                }
            }
            traza.trace("tamaño de la lista de argumentos " + indices.size(), Level.INFO);

            if (!indices.isEmpty()) {

                jbSalvar.setText("Modificar");
                existe = true;
                jrbPrimario.setEnabled(false);
                jrbSecundario.setEnabled(false);
                jrbTerciario.setEnabled(false);
                jrbCuaternario.setEnabled(false);
                chkObligatorio.setEnabled(false);

                for (Indice arg : indices) {
                    filas = new Vector();

                    filas.add(arg.getIndice());

                    try {
                        if (arg.getClave().equalsIgnoreCase("y")) {
                            filas.add("principal");
                            filas.add("si");
                            traza.trace("ID principal, obligatorio ", Level.INFO);
                            filas.add(arg.getTipo());
                            traza.trace("tipo de dato " + arg.getTipo(), Level.INFO);
                        }
                    } catch (NullPointerException e) {
                    }

                    try {
                        if (arg.getClave().equalsIgnoreCase("s")) {
                            filas.add("clave " + ++cont);
                            filas.add("si");
                            traza.trace("ID " + cont + ", obligatorio ", Level.INFO);
                            filas.add(arg.getTipo());
                            traza.trace("tipo de dato " + arg.getTipo(), Level.INFO);
                        }
                    } catch (NullPointerException e) {
                    }

                    try {
                        if (arg.getClave().equalsIgnoreCase("o")) {
                            filas.add("");
                            filas.add("si");
                            traza.trace("campo obligatorio ", Level.INFO);
                            filas.add(arg.getTipo());
                            traza.trace("tipo de dato " + arg.getTipo(), Level.INFO);
                        } else {
                            filas.add("");
                            filas.add("no");
                            traza.trace("campo no obligatorio ", Level.INFO);
                            filas.add(arg.getTipo());
                            traza.trace("tipo de dato " + arg.getTipo(), Level.INFO);
                        }
                    } catch (NullPointerException e) {
                    }

                    modelTableIndicesExistentes.addRow(filas);
                    tablaIndicesExistentes.setModel(modelTableIndicesExistentes);

                }
            } else {
                existe = false;
                jrbPrimario.setEnabled(true);
                jrbSecundario.setEnabled(true);
                jrbTerciario.setEnabled(true);
                jrbCuaternario.setEnabled(true);
                chkObligatorio.setEnabled(true);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }

    /**
     * limpia componentes
     */
    private void limpiarComponentes() {
        llenarComboLibreria();
        cmbCategoria.setModel(new DefaultComboBoxModel());

        txtIndice.setText("");
        chkObligatorio.setSelected(false);
        grupoBotonesClaves.clearSelection();

        tablaIndice.setModel(armarTabla());
        tablaIndicesExistentes.setModel(armarTablaExistentes());

    }

    /**
     * inactiva componentes
     */
    private void inactivarComponentes() {

        jbAgregar.setEnabled(false);
        cmbCategoria.setEnabled(false);
        cmbTipoDato.setEnabled(false);
        txtIndice.setEnabled(false);
        chkObligatorio.setEnabled(false);
        jrbPrimario.setEnabled(false);
        jrbSecundario.setEnabled(false);
        jrbTerciario.setEnabled(false);
        jrbCuaternario.setEnabled(false);
    }

    /**
     * activa componentes
     */
    private void activarComponentes() {

        jbAgregar.setEnabled(true);
        cmbCategoria.setEnabled(true);
        cmbTipoDato.setEnabled(true);
        txtIndice.setEnabled(true);
        chkObligatorio.setEnabled(true);
        jrbPrimario.setEnabled(true);
        jrbSecundario.setEnabled(true);
        jrbTerciario.setEnabled(true);
        jrbCuaternario.setEnabled(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkObligatorio;
    private javax.swing.JComboBox cmbCategoria;
    private javax.swing.JComboBox cmbLibreria;
    private javax.swing.JComboBox cmbTipoDato;
    private javax.swing.ButtonGroup grupoBotonesClaves;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbAgregar;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbSalvar;
    private javax.swing.JLabel jlNota;
    private javax.swing.JRadioButton jrbCuaternario;
    private javax.swing.JRadioButton jrbPrimario;
    private javax.swing.JRadioButton jrbSecundario;
    private javax.swing.JRadioButton jrbTerciario;
    private javax.swing.JPanel panelArgExistentes;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelOpciones;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JTable tablaIndice;
    private javax.swing.JTable tablaIndicesExistentes;
    private javax.swing.JTextField txtIndice;
    // End of variables declaration//GEN-END:variables
}
