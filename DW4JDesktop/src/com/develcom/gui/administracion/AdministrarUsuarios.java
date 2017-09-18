/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * AdministrarUsuarios.java
 *
 * Created on 12/03/2012, 02:11:32 PM
 */
package com.develcom.gui.administracion;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.Fabrica;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.Perfil;
import com.develcom.administracion.Rol;
import com.develcom.administracion.Sesion;
import com.develcom.dao.ManejoSesion;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionAgregar;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.administracion.AdministracionModifica;

/**
 *
 * @author develcom
 */
public class AdministrarUsuarios extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -3089120392013769922L;
    /**
     * Lista del Librerias y Categorias
     */
    private List<Perfil> listaPerfil;
    /**
     * Modelo para la tabla
     */
    private ModeloTabla modelTable;// = new DefaultTableModel();
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(AdministrarUsuarios.class);
    /**
     * Para modificacion de usuarios
     */
    private boolean existe = false;
    private boolean fabrica;
    private boolean elimina;
    /**
     * Usuario que inicio sesion
     */
    private String usuario;
    /**
     * estatus del usuario buscado
     */
    private String estatusActualUsuario = "";
    /**
     * Perfil del usuario buscado
     */
    private List<Sesion> perfilActual;
    /**
     * El usuario buscado
     */
    private String usuarioBuscado;
    private List<Categoria> listaCategorias = new ArrayList<Categoria>();

    /**
     * Construtor, inicia los componetes
     */
    public AdministrarUsuarios() {

        usuario = ManejoSesion.getLogin();
        fabrica = ManejoSesion.getConfiguracion().isFabrica();
        elimina = ManejoSesion.getConfiguracion().isElimina();

        traza.trace("usuario que inicio sesion " + usuario, Level.INFO);
        traza.trace("fabrica activa " + fabrica, Level.INFO);

        initComponents();

        grupoBotonesEstatus.add(jrbActivo);
        grupoBotonesEstatus.add(jrbInactivo);

        jbEliminarFilas.setVisible(false);
        tablaPerfil.setModel(armarTabla());
        jlMensaje.setText("");
        jlMensajeUser.setText("");
        jlEstatus.setText("");

        if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
            jchkConsultarExpediente.setVisible(true);
        } else {
            jchkConsultarExpediente.setVisible(false);
        }

        if (fabrica) {
            jchkFabrica.setVisible(true);
        } else {
            jchkFabrica.setVisible(false);
        }

        if (elimina) {
            chkEliminar.setVisible(true);
        } else {
            chkEliminar.setVisible(false);
        }

        llenarLibreria();
        inactivarComponentes();
        this.setTitle("Administrar Usuarios");
        CentraVentanas.centrar(this, Principal.desktop);
        this.setVisible(true);
    }

    /**
     * Crea los titulos de la tabla
     *
     * @return devuelve un DefaultTableModel
     */
    private DefaultTableModel armarTabla() {

        if (modelTable == null) {
            modelTable = new ModeloTabla();

            //modelTable.addColumn("N°");
            modelTable.addColumn("Usuario");
            modelTable.addColumn("Perfil");
            modelTable.addColumn("Libreria");
            modelTable.addColumn("Categorias");
//            modelTable.addColumn("Estatus Categoria");
        } else {
            modelTable.eliminarFilas();
        }

        return modelTable;
    }

    /**
     * Llena la lista desplegable con las Librerias
     */
    private void llenarLibreria() {
        String tmp = "";
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        try {
            modelo.addElement("");

            listaPerfil = new AdministracionBusqueda().buscarLibCat();

            for (Perfil lc : listaPerfil) {
                if (!tmp.equalsIgnoreCase(lc.getLibreria().getDescripcion())) {
                    tmp = lc.getLibreria().getDescripcion();
                    if (lc.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        modelo.addElement(lc.getLibreria().getDescripcion());
                    }
                }
            }
            cmbLibreria.setModel(modelo);
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
    /**
     * Inicio de componentes
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoBotonesEstatus = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbAgregar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbLibreria = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlCategoria = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jchkAdministrador = new javax.swing.JCheckBox();
        jchkConsultarCategoria = new javax.swing.JCheckBox();
        chkDigitalizar = new javax.swing.JCheckBox();
        jchkConsultarExpediente = new javax.swing.JCheckBox();
        chkReportes = new javax.swing.JCheckBox();
        jchkImprimir = new javax.swing.JCheckBox();
        chkEliminar = new javax.swing.JCheckBox();
        chkAprobar = new javax.swing.JCheckBox();
        jbComprobarUser = new javax.swing.JButton();
        jlMensaje = new javax.swing.JLabel();
        jlMensajeUser = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jrbActivo = new javax.swing.JRadioButton();
        jrbInactivo = new javax.swing.JRadioButton();
        jchkFabrica = new javax.swing.JCheckBox();
        panelTabla = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaPerfil = new javax.swing.JTable();
        jbSalvar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jbEliminarFilas = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jlEstatus = new javax.swing.JLabel();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Administrar Usuarios");

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));

        jbAgregar.setText("Agregar");
        jbAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAgregarActionPerformed(evt);
            }
        });

        jLabel2.setText("Categorias");

        jLabel1.setText("Libreria");

        cmbLibreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLibreriaActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jlCategoria);

        jLabel4.setText("Usuario");

        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(224, 239, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Perfil Usuario"));

        jchkAdministrador.setBackground(new java.awt.Color(224, 239, 255));
        jchkAdministrador.setText("Administrador");
        jchkAdministrador.setToolTipText("Administrador");

        jchkConsultarCategoria.setBackground(new java.awt.Color(224, 239, 255));
        jchkConsultarCategoria.setText("Consultar por Categoria");
        jchkConsultarCategoria.setToolTipText("Consultar por Categoria");

        chkDigitalizar.setBackground(new java.awt.Color(224, 239, 255));
        chkDigitalizar.setText("Digitalizador");

        jchkConsultarExpediente.setBackground(new java.awt.Color(224, 239, 255));
        jchkConsultarExpediente.setText("Consultar por Expediente");
        jchkConsultarExpediente.setToolTipText("Consultar por Expediente");

        chkReportes.setBackground(new java.awt.Color(224, 239, 255));
        chkReportes.setText("Reportes");

        jchkImprimir.setBackground(new java.awt.Color(224, 239, 255));
        jchkImprimir.setText("Imprimir");
        jchkImprimir.setToolTipText("Imprimir");

        chkEliminar.setBackground(new java.awt.Color(224, 239, 255));
        chkEliminar.setText("Eliminar");

        chkAprobar.setBackground(new java.awt.Color(224, 239, 255));
        chkAprobar.setText("Aprobador");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkDigitalizar)
                    .addComponent(chkReportes)
                    .addComponent(jchkImprimir)
                    .addComponent(chkEliminar))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkAprobar)
                    .addComponent(jchkAdministrador)
                    .addComponent(jchkConsultarCategoria)
                    .addComponent(jchkConsultarExpediente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jchkAdministrador)
                    .addComponent(chkDigitalizar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jchkConsultarCategoria)
                    .addComponent(jchkImprimir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkReportes)
                    .addComponent(jchkConsultarExpediente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkEliminar)
                    .addComponent(chkAprobar)))
        );

        jbComprobarUser.setText("Comprobar Usuario");
        jbComprobarUser.setToolTipText("Comprobar Usuario");
        jbComprobarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbComprobarUserActionPerformed(evt);
            }
        });

        jlMensaje.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jlMensaje.setText("jLabel5");

        jlMensajeUser.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jlMensajeUser.setText("jLabel5");

        jPanel4.setBackground(new java.awt.Color(224, 239, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Estatus Usuario"));

        jrbActivo.setBackground(new java.awt.Color(224, 239, 255));
        jrbActivo.setSelected(true);
        jrbActivo.setText("Activo");

        jrbInactivo.setBackground(new java.awt.Color(224, 239, 255));
        jrbInactivo.setText("Inactivo");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbActivo)
                    .addComponent(jrbInactivo))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jrbActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrbInactivo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jchkFabrica.setBackground(new java.awt.Color(224, 239, 255));
        jchkFabrica.setText("¿Pertenece a la fabrica?");

        panelTabla.setBackground(new java.awt.Color(224, 239, 255));

        tablaPerfil.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaPerfil);

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

        jbEliminarFilas.setText("Eliminar filas");
        jbEliminarFilas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarFilasActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel5.setText("Nuevo Estatus del Usuario");

        jlEstatus.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jlEstatus.setText("jLabel6");

        javax.swing.GroupLayout panelTablaLayout = new javax.swing.GroupLayout(panelTabla);
        panelTabla.setLayout(panelTablaLayout);
        panelTablaLayout.setHorizontalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaLayout.createSequentialGroup()
                .addGroup(panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTablaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE))
                    .addGroup(panelTablaLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jbSalvar)
                        .addGap(123, 123, 123)
                        .addComponent(jbEliminarFilas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                        .addComponent(jbCancelar)
                        .addGap(180, 180, 180)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlEstatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTablaLayout.setVerticalGroup(
            panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTablaLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbCancelar)
                            .addGroup(panelTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbSalvar)
                                .addComponent(jbEliminarFilas)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelTablaLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlEstatus)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(jbComprobarUser, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jchkFabrica)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jbAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlMensajeUser, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(jlMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(panelTabla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jbComprobarUser))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addComponent(jlMensaje, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jlMensajeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jchkFabrica)
                                .addGap(22, 22, 22)
                                .addComponent(jbAgregar))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7)
                .addComponent(panelTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Agrega el usuario con su perfil a la tabla
     *
     * @param evt Evento click
     */
    private void jbAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAgregarActionPerformed

        agregar();

    }//GEN-LAST:event_jbAgregarActionPerformed

    /**
     * Agrega el usuario con su perfil a la tabla
     */
    private void agregar() {

        //int contador=1;
        int i = 0, j = 0, l = 0, contAd = 0;
        Vector filas, ver;
        String libreria, status = "", perf, cat, rol, tiene = "";
        Object[] categoria;
        Vector perfiles = new Vector();
        String user;
        boolean verPerfil = true, agrega = true, admin = true, other = false;

        libreria = cmbLibreria.getSelectedItem().toString();
        categoria = jlCategoria.getSelectedValues();
        user = txtUsuario.getText();

        traza.trace("libreria seleccionada " + libreria, Level.INFO);
        traza.trace("usuario a configurar " + libreria, Level.INFO);

        if (jrbActivo.isSelected()) {
            status = jrbActivo.getText();
        } else if (jrbInactivo.isSelected()) {
            status = jrbInactivo.getText();
        }

        traza.trace("actual estatus del usuario " + estatusActualUsuario, Level.INFO);
        traza.trace("nuevo estatus del usuario " + status, Level.INFO);

        if (!estatusActualUsuario.equalsIgnoreCase(status) || estatusActualUsuario.equalsIgnoreCase("")) {
            jlEstatus.setText(status);
            agrega = false;
        } else {
            if (!jlEstatus.getText().equalsIgnoreCase(status) && !jlEstatus.getText().equalsIgnoreCase("")) {
                jlEstatus.setText("");
                agrega = false;
            }
        }

        if (jchkAdministrador.isSelected()) {
            perfiles.add(jchkAdministrador.getText());
            admin = false;
        }

        if (jchkConsultarExpediente.isSelected()) {
            perfiles.add(jchkConsultarExpediente.getText());
            other = true;
        }

        if (jchkConsultarCategoria.isSelected()) {
            perfiles.add(jchkConsultarCategoria.getText());
            other = true;
        }

        if (jchkImprimir.isSelected()) {
            perfiles.add(jchkImprimir.getText());
            other = true;
        }

        if (chkDigitalizar.isSelected()) {
            perfiles.add(chkDigitalizar.getText());
            other = true;
        }

        if (chkReportes.isSelected()) {
            perfiles.add(chkReportes.getText());
            other = true;
        }

        if (chkEliminar.isSelected()) {
            perfiles.add(chkEliminar.getText());
            other = true;
        }

        if (chkAprobar.isSelected()) {
            perfiles.add(chkAprobar.getText());
            other = true;
        }

        agrega = !perfiles.isEmpty();

        if (agrega) {

            if (admin && other) {

                if (!user.equalsIgnoreCase("")) {
                    if (!libreria.equalsIgnoreCase("")) {
                        if (categoria.length > 0) {

                            if (modelTable != null) {

                                try {
                                    ver = modelTable.getDataVector();
                                    for (i = 0; i < ver.size(); i++) {

                                        Vector tabla = (Vector) ver.get(i);
                                        cat = tabla.get(2).toString();
                                        rol = tabla.get(3).toString();

                                        if (verPerfil) {
                                            for (l = 0; l < perfiles.size(); l++) {
                                                String per = (String) perfiles.get(l);
                                                for (j = 0; j < categoria.length; j++) {
                                                    String cate = categoria[j].toString();
                                                    if ((cate.equalsIgnoreCase(cat))
                                                            && (per.equalsIgnoreCase(rol))) {
                                                        verPerfil = false;
                                                        tiene = cat + " y " + rol;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (NullPointerException e) {
                                }
                            }

                            if (verPerfil) {
                                for (i = 0; i < perfiles.size(); i++) {
                                    String per = (String) perfiles.get(i);

                                    for (j = 0; j < categoria.length; j++) {

                                        if (per.equalsIgnoreCase(jchkAdministrador.getText())) {
                                            if (contAd == 0) {
                                                filas = new Vector();
                                                filas.add(user);
                                                filas.add(per.toUpperCase());
                                                modelTable.addRow(filas);
                                            }
                                            contAd++;
                                            break;
                                        } else {
                                            filas = new Vector();
                                            filas.add(user);
                                            filas.add(per.toUpperCase());
                                            filas.add(libreria);
                                            filas.add(categoria[j].toString());

                                            for (Categoria cate : listaCategorias) {
                                                if (cate.getCategoria().equalsIgnoreCase(categoria[j].toString())) {
                                                    filas.add(cate.getEstatus());
                                                }
                                            }

                                            modelTable.addRow(filas);
                                        }
                                    }
                                }

                                tablaPerfil.setModel(modelTable);
                                limpiar();

                            } else {
                                JOptionPane.showMessageDialog(this, "Ya el usuario tiene ese perfil " + tiene, "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Debe seleccionar categorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Debe seleccionar una libreria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe colocar un usuario", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                filas = new Vector();
                filas.add(user);
                filas.add(jchkAdministrador.getText().toUpperCase());
                modelTable.addRow(filas);
                tablaPerfil.setModel(modelTable);
                limpiar();
            }
        } else {
            if (estatusActualUsuario.equalsIgnoreCase(status) || jlEstatus.getText().equalsIgnoreCase(estatusActualUsuario)) {
                JOptionPane.showMessageDialog(this, "Nada que modificar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }

    }

    private void limpiar() {

        grupoBotonesEstatus.clearSelection();
        jrbActivo.setSelected(true);

        tablaPerfil.clearSelection();
        cmbLibreria.setSelectedItem("");

        jchkAdministrador.setSelected(false);
        jchkConsultarCategoria.setSelected(false);
        jchkImprimir.setSelected(false);
        jchkConsultarExpediente.setSelected(false);
        chkDigitalizar.setSelected(false);
        chkReportes.setSelected(false);
        chkEliminar.setSelected(false);
        chkAprobar.setSelected(false);
    }

    private void jbSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarActionPerformed
        guardarPerfil();
    }//GEN-LAST:event_jbSalvarActionPerformed

    /**
     * Guarda o modifuca el usuario con su perfil
     */
    private void guardarPerfil() {

        Vector dataTotal;
        int f = 0;
        List<Perfil> perfiles = new ArrayList<Perfil>();
        Perfil perfil;
        Categoria categoria;
        Libreria libreria;
        Fabrica fabric;
        Rol rol;
//        List<Integer> idPerfil;
        boolean idPerfil;

        try {

            dataTotal = modelTable.getDataVector();

            for (f = 0; f < dataTotal.size(); f++) {
                Vector data = (Vector) dataTotal.get(f);

                perfil = new Perfil();
                libreria = new Libreria();
                fabric = new Fabrica();
                categoria = new Categoria();
                rol = new Rol();

                perfil.setUsuario(usuarioBuscado);

                try {
                    rol.setRol(data.get(1).toString().toUpperCase());
                    for (Perfil p1 : listaPerfil) {
                        if (rol.getRol().equalsIgnoreCase(p1.getRol().getRol())) {
                            rol.setIdRol(p1.getRol().getIdRol());
                            perfil.setRol(rol);
                            traza.trace("rol " + rol.getRol(), Level.INFO);
                            traza.trace("id rol " + rol.getIdRol(), Level.INFO);
                            break;
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    libreria.setDescripcion(data.get(2).toString());
                    for (Perfil p1 : listaPerfil) {
                        if (p1.getLibreria().getDescripcion().equalsIgnoreCase(libreria.getDescripcion())) {
                            libreria.setIdLibreria(p1.getLibreria().getIdLibreria());
                            perfil.setLibreria(libreria);
                            traza.trace("libreria " + libreria.getDescripcion(), Level.INFO);
                            traza.trace("id libreria " + libreria.getIdLibreria(), Level.INFO);
                            break;
                        }
                    }
                } catch (NullPointerException e) {
                }

                try {
                    categoria.setCategoria(data.get(3).toString().toUpperCase());
                    for (Perfil p1 : listaPerfil) {
                        if (p1.getCategoria().getCategoria().equalsIgnoreCase(categoria.getCategoria())) {
                            categoria.setIdCategoria(p1.getCategoria().getIdCategoria());
                            perfil.setCategoria(categoria);
                            traza.trace("categoria " + categoria.getCategoria(), Level.INFO);
                            traza.trace("id categoria " + categoria.getIdCategoria(), Level.INFO);
                            break;
                        }
                    }
                } catch (Exception e) {
                }

                if (jlEstatus.getText().equalsIgnoreCase(jrbActivo.getText())) {
                    perfil.setEstatus(Constantes.ESTATUS_ACTIVO);
                } else if (jlEstatus.getText().equalsIgnoreCase(jrbInactivo.getText())) {
                    perfil.setEstatus(Constantes.ESTATUS_INACTIVO);
                }
                traza.trace("id estatus " + perfil.getEstatus(), Level.INFO);

                if (this.fabrica) {
                    if (jchkFabrica.isSelected()) {
                        fabric.setPertenece(true);
                        fabric.setUsuario(usuarioBuscado);
                        perfil.setFabrica(fabric);
                    } else {
                        fabric.setPertenece(false);
                        fabric.setUsuario(usuarioBuscado);
                        perfil.setFabrica(fabric);
                    }
                } else {
                    fabric.setPertenece(false);
                    fabric.setUsuario(usuarioBuscado);
                    perfil.setFabrica(fabric);
                }

                perfiles.add(perfil);
            }

            if (existe) {
                idPerfil = new AdministracionModifica().modificandoPerfil(perfiles);
            } else {
                idPerfil = new AdministracionAgregar().agregandoPerfil(perfiles);

            }

//            if (idPerfil.size() > 0) {
            if (idPerfil) {
                JOptionPane.showMessageDialog(this, "Usuario " + usuarioBuscado + " configurado con exito", "Indormacion", JOptionPane.INFORMATION_MESSAGE);
                limpiarComponentes();
                inactivarComponentes();
            } else {
                JOptionPane.showMessageDialog(this, "Problemas al configurar el usuario " + usuarioBuscado, "Error", JOptionPane.ERROR_MESSAGE);
                limpiarComponentes();
                inactivarComponentes();
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
    }

    /**
     * Cierra la ventana
     *
     * @param evt Evento click
     */
    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed

        this.dispose();
    }//GEN-LAST:event_jbCancelarActionPerformed

    /**
     * Comprueba si el usuario existe o no en LDAP y en la Base de Datos
     *
     * @param evt Evento click
     */
    private void jbComprobarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbComprobarUserActionPerformed

        buscarUsuario(txtUsuario.getText());
    }//GEN-LAST:event_jbComprobarUserActionPerformed

    private void buscarUsuario(final String usuario) {

        final MostrarProceso proceso = new MostrarProceso("Comprobando el Usuario: " + usuario);
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                comprobarUsuario(usuario);
                proceso.detener();
            }
        }).start();

    }

    /**
     * Comprueba si el usuario existe o no en LDAP y en la Base de Datos
     */
    private void comprobarUsuario(String usuario) {
        try {
            usuarioBuscado = usuario;
            Vector data = modelTable.getDataVector();
            String fa = "No";

            Sesion sesion = new AdministracionBusqueda().compruebaUsuario(usuarioBuscado);

            if (sesion.getFabrica().isPertenece()) {
                fa = "Si";
            }

            limpiarComponentes();
            txtUsuario.setText(usuarioBuscado);
            estatusActualUsuario = sesion.getEstatusUsuario();
            traza.trace("estatus usuario " + estatusActualUsuario, Level.INFO);

            if (estatusActualUsuario == null) {
                estatusActualUsuario = "";
            }

            if (!data.isEmpty()) {

                int n = JOptionPane.showOptionDialog(this,
                        "Configuracion actual no se guardo\n¿Desea continuar?", "Alerta",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"SI", "NO"}, "NO");

                if (n == JOptionPane.YES_OPTION) {

                    if (!usuarioBuscado.equalsIgnoreCase("dw4jconf")) {

                        traza.trace("comprobar usuario: " + sesion.getRespuesta(), Level.INFO);

                        if (sesion.getVerificar().equalsIgnoreCase("exito")) {
                            if (llenarTabla()) {
                                if (sesion.getIdUsuario().equalsIgnoreCase(usuario)) {
                                    jlMensaje.setText("<html>Usuario: " + txtUsuario.getText() + " ya registrado <br/>su estatus es: " + estatusActualUsuario + " y pertenece a la fabrica: " + fa);
                                    jbSalvar.setText("Modificar");
                                    jbEliminarFilas.setVisible(true);
                                    existe = true;
                                    activarComponentes();
                                } else {
                                    jlMensajeUser.setForeground(Color.black);
                                    jlMensajeUser.setText("<html>El usuario " + sesion.getIdUsuario() + " ya inicio sesion, <br/>los cambios son <br/> efectivo de inmediato </br> excepto con el perfil de administrador </html>");
                                    jbSalvar.setText("Modificar");
                                    jbEliminarFilas.setVisible(true);
                                    existe = true;
                                    activarComponentes();
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Problemas al llenar la tabla perfil del usuario " + usuarioBuscado, "Advertencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } else if (sesion.getVerificar().equalsIgnoreCase("basedato")) {

                            existe = false;
                            traza.trace(sesion.getRespuesta(), Level.INFO);
                            jlMensajeUser.setForeground(Color.black);
                            jlMensajeUser.setText(sesion.getRespuesta() + "<br/>Proceda a configurarlo para<br/> registrarlo en el sistema<html/>");
                            activarComponentes();
                            jbSalvar.setText("Guardar");
                            jbEliminarFilas.setVisible(false);

                        } else if (sesion.getVerificar().equalsIgnoreCase("ldap")) {

                            existe = false;
                            jlMensajeUser.setForeground(Color.red);
                            jlMensajeUser.setText(sesion.getRespuesta());
                            inactivarComponentes();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El usuario " + usuarioBuscado + " no se puede configurar", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }

            } else {
                if (!usuarioBuscado.equalsIgnoreCase("dw4jconf")) {

                    traza.trace("comprobar usuario: " + sesion.getRespuesta(), Level.INFO);

                    if (sesion.getVerificar().equalsIgnoreCase("exito")) {
                        if (llenarTabla()) {
                            if (sesion.getIdUsuario().equalsIgnoreCase(usuario)) {
                                jlMensaje.setText("<html>Usuario: " + txtUsuario.getText() + " ya registrado <br/>su estatus es: " + estatusActualUsuario + " y pertenece a la fabrica: " + fa);
                                jbSalvar.setText("Modificar");
                                jbEliminarFilas.setVisible(true);
                                existe = true;
                                activarComponentes();
                            } else {
                                jlMensajeUser.setForeground(Color.black);
                                jlMensajeUser.setText("<html>El usuario " + sesion.getIdUsuario() + " ya inicio sesion, <br/>los cambios son <br/> efectivo de inmediato </br> excepto con el perfil de administrador </html>");
                                jbSalvar.setText("Modificar");
                                jbEliminarFilas.setVisible(true);
                                existe = true;
                                activarComponentes();
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Problemas al llenar la tabla perfil del usuario " + usuarioBuscado, "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }

                    } else if (sesion.getVerificar().equalsIgnoreCase("basedato")) {

                        existe = false;
                        traza.trace(sesion.getRespuesta(), Level.INFO);
                        jlMensajeUser.setForeground(Color.black);
                        jlMensajeUser.setText(sesion.getRespuesta() + "<br/>Proceda a configurarlo para<br/> registrarlo en el sistema<html/>");
                        activarComponentes();
                        jbSalvar.setText("Guardar");
                        jbEliminarFilas.setVisible(false);

                    } else if (sesion.getVerificar().equalsIgnoreCase("ldap")) {

                        existe = false;
                        jlMensajeUser.setForeground(Color.red);
                        jlMensajeUser.setText(sesion.getRespuesta());
                        inactivarComponentes();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "El usuario " + usuarioBuscado + " no se puede configurar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }

    /**
     * Llena la tabla con el perfil del usuario de base de datos para su
     * modificación
     */
    private boolean llenarTabla() {
        int cont = 0;
        Vector filas;
        String fabric = null;
        boolean resp = false;
        try {

            perfilActual = new AdministracionBusqueda().buscandoPerfil(txtUsuario.getText());

            traza.trace("tamaño lista perfil del usuario " + usuarioBuscado + " " + perfilActual.size(), Level.INFO);

            if (!perfilActual.isEmpty()) {
                resp = true;

                if (fabrica) {
                    if (perfilActual.get(0).getFabrica().isPertenece()) {
                        jchkFabrica.setSelected(true);
                        fabric = "SI";
                    } else {
                        jchkFabrica.setSelected(false);
                        fabric = "NO";
                    }
                }

                for (Sesion ses : perfilActual) {
                    filas = new Vector();

                    try {

                        if (fabrica) {
                            jchkFabrica.setSelected(ses.getFabrica().isPertenece());
                        }

                        if (ses.getLibreria() != null && ses.getCategoria() != null) {
                            filas.add(ses.getIdUsuario());
                            filas.add(ses.getRolUsuario().getRol());
                            filas.add(ses.getLibreria().getDescripcion());
                            filas.add(ses.getCategoria().getCategoria());
//                            filas.add(ses.getCategoria().getEstatus());
                            modelTable.addRow(filas);
                            traza.trace("filas " + filas, Level.INFO);
                        } else {
                            filas.add(ses.getIdUsuario());
                            filas.add(ses.getRolUsuario().getRol());
                            modelTable.addRow(filas);
                            traza.trace("filas " + filas, Level.INFO);
                        }
                    } catch (NullPointerException e) {
                        filas.add(ses.getIdUsuario());
                        filas.add(ses.getRolUsuario().getRol());
                        modelTable.addRow(filas);
                        traza.trace("filas " + filas, Level.INFO);
                    }

                    tablaPerfil.setModel(modelTable);
                }
                if (fabrica) {
                    jlMensaje.setText("<html>Usuario: " + txtUsuario.getText() + " ya registrado <br/>su estatus es: " + estatusActualUsuario + " y pertenece a la FABRICA: " + fabric.toUpperCase());
                } else {
                    jlMensaje.setText("<html>Usuario: " + txtUsuario.getText() + " ya registrado <br/>su estatus es: " + estatusActualUsuario);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Problemas al buscar los perfiles\ndel usuario " + usuarioBuscado, "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }
        return resp;
    }

    private void jbEliminarFilasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarFilasActionPerformed
        eliminarFila();
    }//GEN-LAST:event_jbEliminarFilasActionPerformed

    /**
     * Elimina un registro de la tabla
     */
    private void eliminarFila() {
        int indexFila[];

        String id_exp = ((String) tablaPerfil.getValueAt(tablaPerfil.getSelectedRow(), tablaPerfil.getSelectedColumn()));

        indexFila = tablaPerfil.getSelectedRows();

        //for(int i=0;i<indexFila.length;i++){
        for (int i = indexFila.length - 1; i >= 0; i--) {
            traza.trace("quitando el indice " + indexFila[i], Level.INFO);
            modelTable.removeRow(indexFila[i]);
//            TableModelEvent event = new TableModelEvent(modelTable, i);
//            modelTable.rowsRemoved(event);
        }

        TableModelEvent event = new TableModelEvent(modelTable, indexFila[0], indexFila[indexFila.length - 1], TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        modelTable.rowsRemoved(event);
    }

    private void cmbLibreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLibreriaActionPerformed
        llenarListaCategoria();
    }//GEN-LAST:event_cmbLibreriaActionPerformed

    /**
     * Llena la lista desplegable de las Categorias desde el evento
     * ItemStateChanged de la lista desplegable de las Librerias
     */
    private void llenarListaCategoria() {
        DefaultListModel lm = new DefaultListModel();
        String lib = cmbLibreria.getSelectedItem().toString();
        String cat = "";
        for (Perfil lc : listaPerfil) {
            if (lib.equalsIgnoreCase(lc.getLibreria().getDescripcion())) {
                if (!cat.equalsIgnoreCase(lc.getCategoria().getCategoria())) {
                    cat = lc.getCategoria().getCategoria();
                    lm.addElement(cat.trim());
                    listaCategorias.add(lc.getCategoria());
                }
            }
        }
        jlCategoria.setModel(lm);
    }

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            comprobarUsuario(txtUsuario.getText());
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    /**
     * Inactiva los Componentes
     */
    private void inactivarComponentes() {
        cmbLibreria.setEnabled(false);
        chkDigitalizar.setEnabled(false);
        jchkConsultarExpediente.setEnabled(false);
        chkReportes.setEnabled(false);
        chkAprobar.setEnabled(false);
        jchkAdministrador.setEnabled(false);
        jchkConsultarCategoria.setEnabled(false);
        jchkImprimir.setEnabled(false);
        jlCategoria.setEnabled(false);
        tablaPerfil.setEnabled(false);
        jbAgregar.setEnabled(false);
        jbSalvar.setEnabled(false);
        jrbActivo.setEnabled(false);
        jrbInactivo.setEnabled(false);
        jchkFabrica.setEnabled(false);
        chkEliminar.setEnabled(false);
    }

    /**
     * Activa los Componentes
     */
    private void activarComponentes() {
        cmbLibreria.setEnabled(true);
        chkDigitalizar.setEnabled(true);
        jchkConsultarExpediente.setEnabled(true);
        chkReportes.setEnabled(true);
        jchkAdministrador.setEnabled(true);
        jchkConsultarCategoria.setEnabled(true);
        jchkImprimir.setEnabled(true);
        jlCategoria.setEnabled(true);
        tablaPerfil.setEnabled(true);
        jbAgregar.setEnabled(true);
        jbSalvar.setEnabled(true);
        jrbActivo.setEnabled(true);
        jrbInactivo.setEnabled(true);
        jchkFabrica.setEnabled(true);
        chkEliminar.setEnabled(true);
        chkAprobar.setEnabled(true);
    }

    /**
     * Limpia los Componentes
     */
    private void limpiarComponentes() {

        //cmbLibreria.setSelectedItem("");
        llenarLibreria();
        txtUsuario.setText("");

        grupoBotonesEstatus.clearSelection();
        jrbActivo.setSelected(true);

        jchkAdministrador.setSelected(false);
        jchkConsultarCategoria.setSelected(false);
        jchkImprimir.setSelected(false);
        jchkFabrica.setSelected(false);
        chkDigitalizar.setSelected(false);
        jchkConsultarExpediente.setSelected(false);
        chkReportes.setSelected(false);
        chkEliminar.setSelected(false);
        chkAprobar.setSelected(false);

        jlCategoria.setModel(new DefaultListModel());

        tablaPerfil.setModel(armarTabla());

        jlMensaje.setText("");
        jlMensajeUser.setText("");
        jlEstatus.setText("");
        jbSalvar.setText("Guardar");
        jbEliminarFilas.setVisible(false);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkAprobar;
    private javax.swing.JCheckBox chkDigitalizar;
    private javax.swing.JCheckBox chkEliminar;
    private javax.swing.JCheckBox chkReportes;
    private javax.swing.JComboBox cmbLibreria;
    private javax.swing.ButtonGroup grupoBotonesEstatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbAgregar;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbComprobarUser;
    private javax.swing.JButton jbEliminarFilas;
    private javax.swing.JButton jbSalvar;
    private javax.swing.JCheckBox jchkAdministrador;
    private javax.swing.JCheckBox jchkConsultarCategoria;
    private javax.swing.JCheckBox jchkConsultarExpediente;
    private javax.swing.JCheckBox jchkFabrica;
    private javax.swing.JCheckBox jchkImprimir;
    private javax.swing.JList jlCategoria;
    private javax.swing.JLabel jlEstatus;
    private javax.swing.JLabel jlMensaje;
    private javax.swing.JLabel jlMensajeUser;
    private javax.swing.JRadioButton jrbActivo;
    private javax.swing.JRadioButton jrbInactivo;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JTable tablaPerfil;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
