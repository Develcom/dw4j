/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.administracion;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.DatoAdicional;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.tools.UtilidadPalabras;
import com.develcom.tools.trazas.Traza;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionAgregar;
import ve.com.develcom.administracion.AdministracionBusqueda;

/**
 *
 * @author develcom
 */
public class AdministraDatosAdicionales extends javax.swing.JInternalFrame {

    /**
     * Modelo para la tabla
     */
    private ModeloTabla modelTableDatos;
    /**
     * Modelo para la tabla de tipos de documentos existentes
     */
    private ModeloTabla modelTableDataExistentes;
    /**
     * Escribe las trazas en el log
     */
    private Traza traza = new Traza(AdministraDatosAdicionales.class);
    /**
     * Lista de Librerias
     */
    private List<Libreria> librerias;
    /**
     * Lista de Categorias
     */
    private List<Categoria> categorias;
    /**
     * *
     * Lista de SubCategorias
     */
    private List<SubCategoria> subCategorias;
    /**
     * Lista Tipos Documrntos
     */
    private List<TipoDocumento> listaTipoDocumentos;
    /**
     * Para modificacion de tipos de documentos
     */
    private boolean existe = false;

    private List<DatoAdicional> lsDatosAdicionales;
    private int idTipoDocumento;

    /**
     * Creates new form DatosAdicionales
     */
    public AdministraDatosAdicionales() {
        initComponents();

        tblDatosExistentes.setModel(armarTablaDatosExistentes());
        tblDatosAdicionales.setModel(armarTabla());
        llenarComboTipoDato();
        llenarComboLibreria();
        setTitle("Administrar Datos Adicionales");
        CentraVentanas.centrar(this, Principal.desktop);
        setVisible(true);
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
     * Llena la lista desplegables con las Librerias
     */
    private void llenarComboLibreria() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            model.addElement("");

            librerias = new AdministracionBusqueda().buscarLibreria("", 0);

            traza.trace("buscando las libreria ", Level.INFO);
            if (!librerias.isEmpty()) {
                for (Libreria l : librerias) {
                    model.addElement(l.getDescripcion());
                }
                cmbLibreria.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "Problemas al llenar la lista desplegable de las Librerias", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }

    private DefaultTableModel armarTabla() {

        if (modelTableDatos == null) {
            modelTableDatos = new ModeloTabla();

            modelTableDatos.addColumn("Nombre");
            modelTableDatos.addColumn("Tipo de dato");
        } else {
            modelTableDatos.eliminarFilas();
        }

        return modelTableDatos;
    }

    /**
     * Coloca los titulos a la tabala
     *
     * @return retorna un DefaultTableModel
     */
    private DefaultTableModel armarTablaDatosExistentes() {

        modelTableDataExistentes = new ModeloTabla();

        modelTableDataExistentes.addColumn("Nombre");
        modelTableDataExistentes.addColumn("Tipo de Dato");

        return modelTableDataExistentes;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        cmbLibreria = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox();
        cmbSubCategoria = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbTipoDoc = new javax.swing.JComboBox();
        jpDatosExistente = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDatosExistentes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtNombreDato = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbTipoDato = new javax.swing.JComboBox();
        jbAgregar = new javax.swing.JButton();
        panelInferior = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDatosAdicionales = new javax.swing.JTable();
        jbSalvar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Datos Adicionales de los Tipos de Documentos");

        jPanel2.setBackground(new java.awt.Color(224, 239, 255));

        cmbLibreria.setPreferredSize(new java.awt.Dimension(400, 20));
        cmbLibreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLibreriaActionPerformed(evt);
            }
        });

        jLabel2.setText("Librerias");

        jLabel3.setText("Categorias");

        cmbCategoria.setPreferredSize(new java.awt.Dimension(400, 20));
        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        cmbSubCategoria.setPreferredSize(new java.awt.Dimension(400, 20));
        cmbSubCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubCategoriaActionPerformed(evt);
            }
        });

        jLabel4.setText("SubCategorias");

        jLabel1.setText("Tipo de Documento");

        cmbTipoDoc.setPreferredSize(new java.awt.Dimension(400, 20));
        cmbTipoDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoDocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbLibreria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSubCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoDoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSubCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(cmbTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpDatosExistente.setBackground(new java.awt.Color(224, 239, 255));

        tblDatosExistentes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane3.setViewportView(tblDatosExistentes);

        javax.swing.GroupLayout jpDatosExistenteLayout = new javax.swing.GroupLayout(jpDatosExistente);
        jpDatosExistente.setLayout(jpDatosExistenteLayout);
        jpDatosExistenteLayout.setHorizontalGroup(
            jpDatosExistenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatosExistenteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jpDatosExistenteLayout.setVerticalGroup(
            jpDatosExistenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatosExistenteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(224, 239, 255));

        jLabel6.setText("Nombre del Dato Adiccional");

        jLabel5.setText("Tipo de Dato");

        jbAgregar.setText("Agregar");
        jbAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTipoDato, 0, 237, Short.MAX_VALUE)
                    .addComponent(txtNombreDato))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreDato, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipoDato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jbAgregar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInferior.setBackground(new java.awt.Color(224, 239, 255));

        tblDatosAdicionales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane2.setViewportView(tblDatosAdicionales);

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
                .addGap(130, 130, 130)
                .addComponent(jbSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbCancelar)
                .addGap(187, 187, 187))
            .addGroup(panelInferiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelInferiorLayout.setVerticalGroup(
            panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInferiorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbSalvar)
                    .addComponent(jbCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDatosExistente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panelInferior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpDatosExistente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbLibreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLibreriaActionPerformed

        String lib = cmbLibreria.getSelectedItem().toString();
        limpiarComponentes();
        cmbLibreria.setSelectedItem(lib);

        for (Libreria li : librerias) {
            if (lib.equalsIgnoreCase(li.getDescripcion())) {
                traza.trace("libreria selecionada " + li.getDescripcion() + " su id es " + li.getIdLibreria(), Level.INFO);
                llenarComboCategoria(li.getDescripcion(), li.getIdLibreria());
                break;
            }
        }
    }//GEN-LAST:event_cmbLibreriaActionPerformed

    /**
     * Llena la lista desplegable con las categorias desde el evento
     * ItemStateChanged de la lista desplegable de las Librerias
     *
     * @param lib el nombre de la Libreria
     * @param idLib el id de la Libreria
     */
    private void llenarComboCategoria(String lib, int idLib) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            model.addElement("");

            traza.trace("buscando las Categorias de la libreria: " + lib, Level.INFO);
            traza.trace("id de la Libreria: " + idLib, Level.INFO);
            cmbCategoria.setSelectedItem("");

            cmbSubCategoria.setModel(new DefaultComboBoxModel());

            categorias = new AdministracionBusqueda().buscarCategoria("", idLib, 0);

            if (!categorias.isEmpty()) {
                for (Categoria ca : categorias) {
                    if (ca.getEstatus().equalsIgnoreCase("Activo")) {
                        model.addElement(ca.getCategoria());
                    }
                }
                cmbCategoria.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "La libreria " + lib + " no posee categorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }


    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed

        String categoria = cmbCategoria.getSelectedItem().toString();

        traza.trace("Categoria seleccionada " + categoria, Level.INFO);

        for (Categoria cat : categorias) {
            if (categoria.equalsIgnoreCase(cat.getCategoria())) {
                llenarComboSubCategoria(cat.getCategoria(), cat.getIdCategoria());
                break;
            }
        }
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    /**
     *
     *
     * @param cat el nombre de la Categoria
     * @param idCat el id de la categoria
     */
    private void llenarComboSubCategoria(String cat, int idCat) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        try {
            model.addElement("");

            traza.trace("buscando las SubCategorias de la Categorias: " + cat, Level.INFO);
            traza.trace("id de la categoria: " + idCat, Level.INFO);
            cmbSubCategoria.setSelectedItem("");

            subCategorias = new AdministracionBusqueda().buscarSubCategoria("", idCat, 0);

            if (!subCategorias.isEmpty()) {
                for (SubCategoria subca : subCategorias) {
                    if (subca.getEstatus().equalsIgnoreCase("Activo")) {
                        model.addElement(subca.getSubCategoria());
                    }
                }
                cmbSubCategoria.setModel(model);
            } else {
                JOptionPane.showMessageDialog(this, "La categoria " + cat + " no posee subcategorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }


    private void cmbSubCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubCategoriaActionPerformed

        String subCategoria = cmbSubCategoria.getSelectedItem().toString();

        traza.trace("Categoria seleccionada " + subCategoria, Level.INFO);

        for (SubCategoria sc : subCategorias) {
            if (subCategoria.equalsIgnoreCase(sc.getSubCategoria())) {
                llenarComboTipoDocumento(sc.getSubCategoria(), sc.getIdCategoria(), sc.getIdSubCategoria());
                break;
            }
        }

    }//GEN-LAST:event_cmbSubCategoriaActionPerformed

    /**
     * Llena la lista con los Tipos Documentos desde el evento ItemStateChanged
     * de la lista desplegable de las Categorias
     */
    private void llenarComboTipoDocumento(String subCat, int idCat, int idSubCat) {

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        boolean flag = false;

        try {

//            cmbTipoDoc.removeAllItems();
            cmbTipoDoc.setModel(new DefaultComboBoxModel());
            tblDatosExistentes.setModel(armarTablaDatosExistentes());
            tblDatosAdicionales.setModel(armarTabla());

            model.addElement("");

            traza.trace("buscando los Tipos de Documentos de la SubCategorias: " + subCat, Level.INFO);
            traza.trace("id de la categoria: " + idCat, Level.INFO);
            traza.trace("id de la subCategoria: " + idSubCat, Level.INFO);
//            cmbSubCategoria.setSelectedItem("");

            listaTipoDocumentos = new AdministracionBusqueda().buscarTipoDocumento("", idCat, idSubCat);

            if (!listaTipoDocumentos.isEmpty()) {
                for (TipoDocumento td : listaTipoDocumentos) {
                    if (td.getEstatus().equalsIgnoreCase("Activo")) {
                        if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                            model.addElement(td.getTipoDocumento());
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    cmbTipoDoc.setModel(model);
                } else {
                    JOptionPane.showMessageDialog(this, "La subCategoria " + subCat + " no posee Tipos de Documentos \ncon datos adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "La subCategoria " + subCat + " no posee Tipos de Documentos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }


    private void cmbTipoDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoDocActionPerformed
        llenarTablaExistente();
    }//GEN-LAST:event_cmbTipoDocActionPerformed

    /**
     * Llena la tabla con los Argumentos existentes de la Categoria seleccionada
     */
    private void llenarTablaExistente() {

        Vector filas;
        int cont = 1;

        try {

            tblDatosExistentes.setModel(armarTablaDatosExistentes());
            tblDatosAdicionales.setModel(armarTabla());

            String tipoDocu = cmbTipoDoc.getSelectedItem().toString();

            //jlCategoria.setText("Indices existentes para la categoria: "+cate);
//            jpDatosExistente.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Adicionales existentes para: " + tipoDocu));
            for (TipoDocumento td : listaTipoDocumentos) {
                if (td.getTipoDocumento().equalsIgnoreCase(tipoDocu)) {
                    traza.trace("buscando los datos adicionales de tipo de documento " + tipoDocu + " id " + td.getIdCategoria(), Level.INFO);
                    idTipoDocumento = td.getIdTipoDocumento();
                    lsDatosAdicionales = new AdministracionBusqueda().buscarIndDatoAdicional(idTipoDocumento);
                    break;
                }
            }
            traza.trace("tamaño de la lista de argumentos " + lsDatosAdicionales.size(), Level.INFO);

            if (!lsDatosAdicionales.isEmpty()) {
                for (DatoAdicional arg : lsDatosAdicionales) {
                    filas = new Vector();

                    filas.add(arg.getIndiceDatoAdicional());
                    filas.add(arg.getTipo());

                    modelTableDataExistentes.addRow(filas);
                    tblDatosExistentes.setModel(modelTableDataExistentes);
                }
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }


    private void jbSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarActionPerformed
        guardarDatosAdicionales();
    }//GEN-LAST:event_jbSalvarActionPerformed

    private void guardarDatosAdicionales() {
        List<DatoAdicional> lsDatoAdicional = new ArrayList<DatoAdicional>();
        DatoAdicional da;
        Vector datosIndice;
        boolean resp;

        try {

            datosIndice = modelTableDatos.getDataVector();

            traza.trace("tamaño de la lista de indices para datos adicioanles " + datosIndice.size(), Level.INFO);

            for (int i = 0; i < datosIndice.size(); i++) {
                Vector dato = (Vector) datosIndice.get(i);

                da = new DatoAdicional();
                da.setIdTipoDocumento(idTipoDocumento);
                da.setIndiceDatoAdicional(dato.get(0).toString());
                da.setTipo(dato.get(1).toString());

                traza.trace("agregando dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo(), Level.INFO);

                lsDatoAdicional.add(da);
            }

            resp = new AdministracionAgregar().insertarIndiceDatoAdicional(lsDatoAdicional);

            if (resp) {
                JOptionPane.showMessageDialog(this, "Datos Adicionales guardados con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                limpiarComponentes();
            } else {
                JOptionPane.showMessageDialog(this, "Problemas al guardar los Datos adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                limpiarComponentes();
            }
        } catch (SOAPException ex) {
            traza.trace("problemas al agregar los datos adicioanles", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("problemas al agregar los datos adicioanles", Level.ERROR, ex);
        }
    }


    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed

        this.dispose();
    }//GEN-LAST:event_jbCancelarActionPerformed

    private void jbAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAgregarActionPerformed

        agregarDatoAdicional();
    }//GEN-LAST:event_jbAgregarActionPerformed

    /**
     * Agrega el nuevo indice a la tabla
     */
    private void agregarDatoAdicional() {

        Vector filas = new Vector();
        String argument, tipo;
        String[] tmp, blanco;
        boolean banLista = true;
        String libreria, categoria;

        try {

            libreria = cmbLibreria.getSelectedItem().toString();

            if (!libreria.equalsIgnoreCase("")) {

                categoria = cmbCategoria.getSelectedItem().toString();

                if (!categoria.equalsIgnoreCase("")) {

                    tipo = cmbTipoDato.getSelectedItem().toString();

                    if (!tipo.equalsIgnoreCase("")) {

                        argument = txtNombreDato.getText();

                        if (!argument.equalsIgnoreCase("")) {

                            filas.add(argument);

                            filas.add(tipo);

                            for (DatoAdicional arg : lsDatosAdicionales) {
                                String argu = arg.getIndiceDatoAdicional();
                                if (argu.equalsIgnoreCase(argument)) {
                                    banLista = false;
                                    break;
                                } else {
                                    banLista = true;
                                }
                            }

                            if (banLista) {
                                modelTableDatos.addRow(filas);
                                tblDatosAdicionales.setModel(modelTableDatos);
                                txtNombreDato.setText("");
                                cmbTipoDato.setSelectedIndex(0);
                            } else {
                                JOptionPane.showMessageDialog(this, "El Dato Adicional ya existe", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
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
     * Limpia los componentes
     */
    private void limpiarComponentes() {
//        cmbLibreria.removeAllItems();
        llenarComboLibreria();

//        cmbCategoria.removeAllItems();
        cmbCategoria.setModel(new DefaultComboBoxModel());

//        cmbSubCategoria.removeAllItems();
        cmbSubCategoria.setModel(new DefaultComboBoxModel());

//        cmbTipoDato.removeAllItems();
        cmbTipoDoc.setModel(new DefaultComboBoxModel());

        cmbTipoDato.setSelectedItem("");

        txtNombreDato.setText("");
        tblDatosExistentes.setModel(armarTablaDatosExistentes());
        tblDatosAdicionales.setModel(armarTabla());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbCategoria;
    private javax.swing.JComboBox cmbLibreria;
    private javax.swing.JComboBox cmbSubCategoria;
    private javax.swing.JComboBox cmbTipoDato;
    private javax.swing.JComboBox cmbTipoDoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbAgregar;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbSalvar;
    private javax.swing.JPanel jpDatosExistente;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JTable tblDatosAdicionales;
    private javax.swing.JTable tblDatosExistentes;
    private javax.swing.JTextField txtNombreDato;
    // End of variables declaration//GEN-END:variables
}
