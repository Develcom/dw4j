/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.captura;

import com.develcom.documento.InfoDocumento;
import com.develcom.administracion.TipoDocumento;
import com.develcom.administracion.SubCategoria;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.Expedientes;
import com.develcom.expediente.ListaIndice;
import com.develcom.gui.Libreria;
import com.develcom.gui.Principal;
import com.develcom.gui.visor.VerDocumento;
import com.develcom.gui.visor.VerImagenes;
import com.develcom.gui.captura.scan.EscaneaDocumento;
import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.gui.reportes.tools.Foliatura;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.expediente.GestionDocumentos;
import ve.com.develcom.expediente.GestionExpediente;

/**
 *
 * @author develcom
 */
public class DigitalizaDocumento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 7449883847673060273L;
    /**
     * Contiene informacion del expediente
     */
    private Expediente expediente;
    /**
     * Lista con las SubCategorias
     */
    private List<SubCategoria> subCategorias = null;
    /**
     * un hashmap de los documentos disponibles
     */
    private HashMap<String, Integer> mapTipoDocumentoDisponible = new HashMap();
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(DigitalizaDocumento.class);
    /**
     * Lista con documentos digitalizados
     */
    private List<InfoDocumento> documentosDigitalizado = new ArrayList<>();
    /**
     * Lista con documentos digitalizados rechazados
     */
    private List<InfoDocumento> docDigitalizadoRecha = new ArrayList<>();
    /**
     * Lista con los documentos disponibles
     */
    private List<TipoDocumento> tipoDocumentosDisponibles = new ArrayList<>();
    /**
     * Variable que comprueba si se debe crear un nuevo expediente o no
     */
    private boolean crearExpediente;
    private List<com.develcom.documento.DatoAdicional> lsDatosAdicionales;
    private List<ListaIndice> listaIndices;

    /**
     * Creates new form DigitalizaDocumento
     */
    public DigitalizaDocumento() {
        initComponents();
        iniciar();
    }

    private void iniciar() {
        this.expediente = ManejoSesion.getExpediente();

        DefaultTreeModel modelDisp = new DefaultTreeModel(null);
        //TreePath tpDisp = new TreePath(arbolDisponibles.getPath());
        jtrDocumentosDisponibles.setModel(modelDisp);

        DefaultTreeModel modelDigit = new DefaultTreeModel(null);
        jtrDocumentosDigitalizados.setModel(modelDigit);

        DefaultTreeModel modelRecha = new DefaultTreeModel(null);
        jtrDocumentosRechazados.setModel(modelRecha);

        jlVencimientoDocDisp.setVisible(false);
        jdDocumentoDisponible.setVisible(false);

        jlVencimientoDocRech.setVisible(false);
        jdDocumentoRechazado.setVisible(false);

        panelRechazados.setVisible(false);

        setTitle(ManejoSesion.getExpediente().getLibreria() + " - " + ManejoSesion.getExpediente().getCategoria());

        if (!ManejoSesion.getConfiguracion().isFoliatura()) {
            jbtFoliatura.setVisible(false);
        }

        if (llenarSubCategorias()) {
            CentraVentanas.centrar(this, Principal.desktop);
            setVisible(true);
        } else {
            this.dispose();
        }
    }

    /**
     * LLena el combo con las subCategorias
     *
     * @param idCategoria
     */
    private boolean llenarSubCategorias() {
        boolean resp = false;
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("");
        try {
            traza.trace("llenado listado de subCategorias", Level.INFO);

            subCategorias = new AdministracionBusqueda().buscarSubCategoria(null, expediente.getIdCategoria(), 0);

            if (subCategorias != null && !subCategorias.isEmpty()) {

                for (SubCategoria sc : subCategorias) {
                    if (sc.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        modelo.addElement(sc.getSubCategoria().trim());
                    }
                }
                cmbSubCategorias.setModel(modelo);
                resp = true;
            } else {
                JOptionPane.showMessageDialog(this, "La Categoria " + expediente.getCategoria() + "\nno tiene subCategorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
                jbtCerrarActionPerformed(null);
            }
        } catch (SOAPException | SOAPFaultException | HeadlessException e) {
            traza.trace("error al llenar el listado de subCategorias", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error al llenar el listado de subCategorias\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return resp;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSeleccionar = new javax.swing.JButton();
        jbtCerrar = new javax.swing.JButton();
        jbtFoliatura = new javax.swing.JButton();
        panelDigitalizado = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnReemplazar = new javax.swing.JButton();
        btnVisualizar = new javax.swing.JButton();
        btnVersionar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtrDocumentosDigitalizados = new javax.swing.JTree();
        jPanel3 = new javax.swing.JPanel();
        jbBuscarExpediente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        cmbSubCategorias = new javax.swing.JComboBox();
        panelDisponibles = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlVencimientoDocDisp = new javax.swing.JLabel();
        jdDocumentoDisponible = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        btnEscanearDocDisp = new javax.swing.JButton();
        btnExplorarDocDisp = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtrDocumentosDisponibles = new javax.swing.JTree();
        panelRechazados = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jlVencimientoDocRech = new javax.swing.JLabel();
        jdDocumentoRechazado = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        btnEscanearDocRech = new javax.swing.JButton();
        btnExplorarDocRech = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtrDocumentosRechazados = new javax.swing.JTree();
        jScrollPane4 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();

        setBackground(new java.awt.Color(224, 239, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        btnSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/list.png"))); // NOI18N
        btnSeleccionar.setText("Seleccionar Escaner");
        btnSeleccionar.setToolTipText("Seleccionar el scanner a usar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        jbtCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jbtCerrar.setText("Cancelar");
        jbtCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCerrarActionPerformed(evt);
            }
        });

        jbtFoliatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/reporte/1344612993_document.png"))); // NOI18N
        jbtFoliatura.setText("Foliatura");
        jbtFoliatura.setToolTipText("Genera la Foliatura del Expediente");
        jbtFoliatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFoliaturaActionPerformed(evt);
            }
        });

        panelDigitalizado.setBackground(new java.awt.Color(224, 239, 255));
        panelDigitalizado.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos Digitalizados"));

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));

        btnReemplazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/New24.gif"))); // NOI18N
        btnReemplazar.setText("Reemplazar");
        btnReemplazar.setToolTipText("Reemplazar el documento");
        btnReemplazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReemplazarActionPerformed(evt);
            }
        });

        btnVisualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Open24.gif"))); // NOI18N
        btnVisualizar.setText("Visualizar");
        btnVisualizar.setToolTipText("Abrir un documento");
        btnVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisualizarActionPerformed(evt);
            }
        });

        btnVersionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Add24.gif"))); // NOI18N
        btnVersionar.setText("Versionar");
        btnVersionar.setToolTipText("Crear una nueva version del documento");
        btnVersionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVersionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnVisualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVersionar)
                .addGap(64, 64, 64)
                .addComponent(btnReemplazar))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReemplazar)
                    .addComponent(btnVisualizar)
                    .addComponent(btnVersionar)))
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtrDocumentosDigitalizados.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtrDocumentosDigitalizados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtrDocumentosDigitalizadosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtrDocumentosDigitalizados);

        javax.swing.GroupLayout panelDigitalizadoLayout = new javax.swing.GroupLayout(panelDigitalizado);
        panelDigitalizado.setLayout(panelDigitalizadoLayout);
        panelDigitalizadoLayout.setHorizontalGroup(
            panelDigitalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDigitalizadoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDigitalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelDigitalizadoLayout.setVerticalGroup(
            panelDigitalizadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelDigitalizadoLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(224, 239, 255));

        jbBuscarExpediente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/search_b.png"))); // NOI18N
        jbBuscarExpediente.setText("Buscar");
        jbBuscarExpediente.setToolTipText("Busca los Tipos de Documentos de la SubCategoria Seleccionada");
        jbBuscarExpediente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBuscarExpedienteActionPerformed(evt);
            }
        });

        jLabel1.setText("Indique el Id Identificador del Expediente y Seleccione una SubCategoria");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbSubCategorias, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtExpediente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbBuscarExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(txtExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbSubCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jbBuscarExpediente, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelDisponibles.setBackground(new java.awt.Color(224, 239, 255));
        panelDisponibles.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipos de Documentos a Digitalizar"));

        jPanel2.setBackground(new java.awt.Color(224, 239, 255));

        jlVencimientoDocDisp.setText("Fecha de Vencimiento");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jlVencimientoDocDisp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdDocumentoDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdDocumentoDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jlVencimientoDocDisp)
        );

        btnEscanearDocDisp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/scanner16.png"))); // NOI18N
        btnEscanearDocDisp.setText("Escanear");
        btnEscanearDocDisp.setToolTipText("Digitalizar desde Scanner");
        btnEscanearDocDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscanearDocDispActionPerformed(evt);
            }
        });

        btnExplorarDocDisp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/open18.png"))); // NOI18N
        btnExplorarDocDisp.setText("Explorar");
        btnExplorarDocDisp.setToolTipText("Digitalizar desde un Archivo");
        btnExplorarDocDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExplorarDocDispActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(btnEscanearDocDisp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExplorarDocDisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEscanearDocDisp)
                    .addComponent(btnExplorarDocDisp)))
        );

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtrDocumentosDisponibles.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtrDocumentosDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtrDocumentosDisponiblesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jtrDocumentosDisponibles);

        javax.swing.GroupLayout panelDisponiblesLayout = new javax.swing.GroupLayout(panelDisponibles);
        panelDisponibles.setLayout(panelDisponiblesLayout);
        panelDisponiblesLayout.setHorizontalGroup(
            panelDisponiblesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDisponiblesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDisponiblesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(panelDisponiblesLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelDisponiblesLayout.setVerticalGroup(
            panelDisponiblesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDisponiblesLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelDisponiblesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelRechazados.setBackground(new java.awt.Color(224, 239, 255));
        panelRechazados.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentos Rechazados"));

        jPanel5.setBackground(new java.awt.Color(224, 239, 255));

        jlVencimientoDocRech.setText("Fecha de Vencimiento");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jlVencimientoDocRech)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdDocumentoRechazado, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlVencimientoDocRech)
                    .addComponent(jdDocumentoRechazado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnEscanearDocRech.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/scanner16.png"))); // NOI18N
        btnEscanearDocRech.setText("Escanear");
        btnEscanearDocRech.setToolTipText("Digitalizar desde Scanner");
        btnEscanearDocRech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscanearDocRechActionPerformed(evt);
            }
        });

        btnExplorarDocRech.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/open18.png"))); // NOI18N
        btnExplorarDocRech.setText("Explorar");
        btnExplorarDocRech.setToolTipText("Digitalizar desde un Archivo");
        btnExplorarDocRech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExplorarDocRechActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(btnEscanearDocRech, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExplorarDocRech, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnExplorarDocRech)
                .addComponent(btnEscanearDocRech))
        );

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtrDocumentosRechazados.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtrDocumentosRechazados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtrDocumentosRechazadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtrDocumentosRechazados);

        javax.swing.GroupLayout panelRechazadosLayout = new javax.swing.GroupLayout(panelRechazados);
        panelRechazados.setLayout(panelRechazadosLayout);
        panelRechazadosLayout.setHorizontalGroup(
            panelRechazadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRechazadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRechazadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRechazadosLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelRechazadosLayout.setVerticalGroup(
            panelRechazadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRechazadosLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRechazadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane4.setBackground(new java.awt.Color(224, 239, 255));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 199, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(panelIndices);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDisponibles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSeleccionar)
                        .addGap(62, 62, 62)
                        .addComponent(jbtFoliatura, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtCerrar))
                    .addComponent(panelDigitalizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelRechazados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtFoliatura, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSeleccionar)
                            .addComponent(jbtCerrar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDigitalizado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelRechazados, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCerrarActionPerformed

        this.dispose();
        Principal.desktop.removeAll();
        Libreria libreria = new Libreria(ManejoSesion.getSesion(), "Seleccionar Libreria - Digitalizar");
        Principal.desktop.add(libreria);

    }//GEN-LAST:event_jbtCerrarActionPerformed


    private void btnExplorarDocDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExplorarDocDispActionPerformed
        buscarArchivo();
    }//GEN-LAST:event_btnExplorarDocDispActionPerformed

    private void buscarArchivo() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        InfoDocumento infoDocumento = new InfoDocumento();
        GestionDocumentos gd = new GestionDocumentos();
        com.develcom.dao.Expediente digExpe = ManejoSesion.getExpediente();// new com.develcom.dto.Expediente();
        XMLGregorianCalendar xmlFechaVencimiento = null;
        GregorianCalendar calendar;
        DefaultMutableTreeNode verificador;
        TreeNode categoria;
        TreeNode subCategoria;
        String tipoDocumentoDigitalizar = "";
        int idDoc = 0, numDoc, cont = 0, numDocActual;
        boolean flag = false, tiene = true;

        try {
            Constantes.ACCION = "GUARDAR";
            if (!jtrDocumentosDisponibles.isSelectionEmpty()) {

                verificador = (DefaultMutableTreeNode) jtrDocumentosDisponibles.getLastSelectedPathComponent();

                if (verificador.isLeaf()) {

                    categoria = verificador.getRoot();
                    subCategoria = verificador.getParent();
                    tipoDocumentoDigitalizar = verificador.toString().trim();
                    traza.trace("tipo de documento seleccionado " + tipoDocumentoDigitalizar, Level.INFO);

                    int n = JOptionPane.showOptionDialog(this,
                            "Desea  crear un nuevo documento \n(" + tipoDocumentoDigitalizar + ") del expediente \n" + ManejoSesion.getExpediente().getIdExpediente(),
                            "¿?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, new Object[]{"SI", "NO"}, "NO");

                    if (n == JOptionPane.YES_OPTION) {

                        traza.trace("se creara el tipo de documento " + tipoDocumentoDigitalizar, Level.INFO);

                        if (!mapTipoDocumentoDisponible.isEmpty()) {
                            idDoc = mapTipoDocumentoDisponible.get(tipoDocumentoDigitalizar);
                        } else {
                            throw new DW4JDesktopExcepcion("No se puede digitalizar \n no hay tipos de documentos");
                        }

                        try {
                            infoDocumento = new InfoDocumento();
                            for (TipoDocumento td : tipoDocumentosDisponibles) {
                                if (tipoDocumentoDigitalizar.equalsIgnoreCase(td.getTipoDocumento().trim())) {
                                    if (td.getVencimiento().equalsIgnoreCase("1")) {
                                        if (jdDocumentoDisponible.getCalendar() != null) {
                                            jdDocumentoDisponible.setEnabled(false);
                                            calendar = (GregorianCalendar) jdDocumentoDisponible.getCalendar();
                                            DatatypeFactory dtf = DatatypeFactory.newInstance();
                                            xmlFechaVencimiento = dtf.newXMLGregorianCalendar(calendar);
                                            GregorianCalendar fecha = xmlFechaVencimiento.toGregorianCalendar();
                                            traza.trace("fecha de vencimiento " + sdf.format(fecha.getTime()), Level.INFO);
                                            traza.trace("fecha de vencimiento " + xmlFechaVencimiento, Level.INFO);
                                            infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
                                            break;
                                        } else {
                                            throw new DW4JDesktopExcepcion("Debe colocar una fecha de vencimiento");
                                        }
                                    }
                                }
                            }

                        } catch (NullPointerException e) {
                        }

                        try {
                            for (TipoDocumento td : tipoDocumentosDisponibles) {
                                if (tipoDocumentoDigitalizar.equalsIgnoreCase(td.getTipoDocumento().trim())) {
                                    if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                                        infoDocumento.setTipoDocDatoAdicional(true);
                                        tiene = true;
                                        expediente.setTipoDocumento(tipoDocumentoDigitalizar);
                                        expediente.setIdTipoDocumento(td.getIdTipoDocumento());
//                                        datosAdicionales(expediente, this);
                                        DatoAdicional dag = new DatoAdicional(expediente, false, this, 0, 0);
                                        for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
                                            infoDocumento.getLsDatosAdicionales().add(da);
                                            flag = true;
                                        }
                                        break;
                                    } else {
                                        tiene = false;
                                    }
                                }
                            }

                            if (!tiene) {
                                flag = true;
                            }
                        } catch (NullPointerException e) {
                        }

                        digExpe.setTipoDocumento(tipoDocumentoDigitalizar);
                        digExpe.setIdTipoDocumento(idDoc);
                        ManejoSesion.setExpediente(digExpe);
                        infoDocumento.setTipoDocumento(tipoDocumentoDigitalizar);
                        infoDocumento.setIdDocumento(idDoc);
                        infoDocumento.setIdExpediente(ManejoSesion.getExpediente().getIdExpediente());
                        infoDocumento.setReDigitalizo(false);

                        if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                            infoDocumento.setEstatus(0);
                        } else {
                            infoDocumento.setEstatus(1);
                        }

                        traza.trace("digitalizando el documento " + tipoDocumentoDigitalizar, Level.INFO);
                        if (flag) {
                            new EscaneaDocumento("Guardar", infoDocumento, true).buscarArchivo();
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Por favor seleccione el documento del arbol digitalizados,\n y luego use los botones segun su preferencia", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser digitalizado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (!jtrDocumentosRechazados.isSelectionEmpty()) {

                verificador = (DefaultMutableTreeNode) jtrDocumentosRechazados.getLastSelectedPathComponent();

                if (verificador.isLeaf()) {

                    categoria = verificador.getRoot();
                    subCategoria = verificador.getParent();
                    tipoDocumentoDigitalizar = verificador.toString().trim();
                    traza.trace("tipo de documento rechazado seleccionado " + tipoDocumentoDigitalizar, Level.INFO);

                    try {

                        for (InfoDocumento id : docDigitalizadoRecha) {
                            String nombre = id.getTipoDocumento().trim();
                            nombre = nombre.trim();
                            traza.trace("buscando tipo de documento rechazado para la fecha de vencimiento " + nombre, Level.INFO);
                            if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                for (TipoDocumento td : tipoDocumentosDisponibles) {
                                    String tiDo = td.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                                    tiDo = tiDo.trim();
                                    if (tipoDocumentoDigitalizar.contains(tiDo)) {
                                        if (td.getVencimiento().equalsIgnoreCase("1")) {
                                            if (jdDocumentoRechazado.getCalendar() != null) {
                                                jdDocumentoRechazado.setEnabled(false);
                                                calendar = (GregorianCalendar) jdDocumentoRechazado.getCalendar();
                                                DatatypeFactory dtf = DatatypeFactory.newInstance();
                                                xmlFechaVencimiento = dtf.newXMLGregorianCalendar(calendar);
                                                GregorianCalendar fecha = xmlFechaVencimiento.toGregorianCalendar();
                                                traza.trace("fecha de vencimiento " + sdf.format(fecha.getTime()), Level.INFO);
                                                traza.trace("fecha de vencimiento " + xmlFechaVencimiento, Level.INFO);

                                                break;
                                            } else {
                                                throw new DW4JDesktopExcepcion("Debe colocar una fecha de vencimiento");
                                            }
                                        }
                                    }
                                }

                            }
                        }

                    } catch (NullPointerException e) {
                    }

                    try {

                        for (InfoDocumento id : docDigitalizadoRecha) {
                            String nombre = id.getTipoDocumento().trim();
                            nombre = nombre.trim();
                            traza.trace("buscando tipo de documento rechazado para el dato adicional " + nombre, Level.INFO);
                            if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                for (TipoDocumento td : tipoDocumentosDisponibles) {
                                    String tiDo = td.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                                    tiDo = tiDo.trim();
                                    if (tipoDocumentoDigitalizar.contains(tiDo)) {
                                        if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                                            expediente.setTipoDocumento(tipoDocumentoDigitalizar);
                                            expediente.setIdTipoDocumento(td.getIdTipoDocumento());
                                            DatoAdicional dag = new DatoAdicional(expediente, true, this, id.getNumeroDocumento(), id.getVersion());
                                            flag = true;
                                            break;
                                        } else {
                                            tiene = false;
                                        }
                                    }
                                }
                            }
                        }

                        if (!tiene) {
                            flag = true;
                        }

                    } catch (NullPointerException e) {
                    }

                    if (!docDigitalizadoRecha.isEmpty()) {

                        for (InfoDocumento infoDoc : docDigitalizadoRecha) {
                            String nombre = infoDoc.getTipoDocumento().trim();
                            nombre = nombre.trim();

                            if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                idDoc = infoDoc.getIdDocumento();
                                digExpe.setTipoDocumento(tipoDocumentoDigitalizar);
                                digExpe.setIdTipoDocumento(idDoc);
                                ManejoSesion.setExpediente(digExpe);

                                if (!infoDoc.getLsDatosAdicionales().isEmpty()) {
                                    infoDoc.getLsDatosAdicionales().clear();
                                }
                                infoDocumento = infoDoc;
                                infoDocumento.setIdExpediente(ManejoSesion.getExpediente().getIdExpediente());
                                infoDocumento.setReDigitalizo(true);
                                if (lsDatosAdicionales != null) {
                                    for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
                                        da.setVersion(infoDocumento.getVersion());
                                        infoDocumento.getLsDatosAdicionales().add(da);
                                    }
                                }
                                if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                                    infoDocumento.setEstatus(0);
                                } else {
                                    infoDocumento.setEstatus(1);
                                }

                                if (xmlFechaVencimiento != null) {
                                    infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
                                }

                                traza.trace("re-digitalizando el documento " + infoDoc.getTipoDocumento(), Level.INFO);

                                new EscaneaDocumento("Guardar", infoDocumento, true).buscarArchivo();

                                //DigitalizarDoc jdDigitalizarDoc = new DigitalizarDoc(true, "Guardar", infoDocumento);
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay documento rechazado", "Alerta...", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser digitalizado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Debe elejir un tipo de documento", "Informacion", JOptionPane.INFORMATION_MESSAGE);
//                if (jtrDocRecha.isSelectionEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Debe elejir uno de los documentos rechazados", "Informacion", JOptionPane.INFORMATION_MESSAGE);
//                } else if (jtrDocDisp.isSelectionEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Elija uno de los documentos disponibles", "Informacion", JOptionPane.INFORMATION_MESSAGE);
//                }
            }

        } catch (DatatypeConfigurationException ex) {
            traza.trace("problemas al convertir la fecha de vencimiento", Level.ERROR, ex);
        } catch (DW4JDesktopExcepcion ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            traza.trace("problemas general al tratar de digitalizar un tipo de documento " + tipoDocumentoDigitalizar, Level.ERROR, e);
        }
    }

    private void btnEscanearDocDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscanearDocDispActionPerformed
        digitalizar("Guardar");
    }//GEN-LAST:event_btnEscanearDocDispActionPerformed

    /**
     * digitaliza un documento
     */
    private void digitalizar(String accion) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        InfoDocumento infoDocumento = new InfoDocumento();
        GestionDocumentos gd = new GestionDocumentos();
        com.develcom.dao.Expediente digExpe = ManejoSesion.getExpediente();// new com.develcom.dto.Expediente();
        XMLGregorianCalendar xmlFechaVencimiento;
        GregorianCalendar calendar;
        DefaultMutableTreeNode verificador;
        TreeNode categoria;
        TreeNode subCategoria;
        String tipoDocumentoDigitalizar = "";
        int idDoc = 0;//, numDoc, numDocActual;
        boolean flag = false, tiene = true;

        try {
            Constantes.ACCION = "GUARDAR";
            if (accion.equalsIgnoreCase("reDigitalizar")) {

                if (!jtrDocumentosRechazados.isSelectionEmpty()) {

                    verificador = (DefaultMutableTreeNode) jtrDocumentosRechazados.getLastSelectedPathComponent();

                    if (!verificador.isLeaf()) {
                        JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser digitalizado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        categoria = verificador.getRoot();
                        subCategoria = verificador.getParent();
                        tipoDocumentoDigitalizar = verificador.toString().trim();
                        traza.trace("tipo de documento rechazado seleccionado " + tipoDocumentoDigitalizar, Level.INFO);

                        try {

                            for (InfoDocumento id : docDigitalizadoRecha) {
                                String nombre = id.getTipoDocumento().trim();
                                nombre = nombre.trim();
                                traza.trace("buscando tipo de documento rechazado para la fecha de vencimiento " + nombre, Level.INFO);
                                if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                    for (TipoDocumento td : tipoDocumentosDisponibles) {
                                        String tiDo = td.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                                        tiDo = tiDo.trim();
                                        if (tipoDocumentoDigitalizar.contains(tiDo)) {
                                            if (td.getVencimiento().equalsIgnoreCase("1")) {
                                                if (jdDocumentoRechazado.getCalendar() != null) {
                                                    jdDocumentoRechazado.setEnabled(false);
                                                    calendar = (GregorianCalendar) jdDocumentoRechazado.getCalendar();
                                                    DatatypeFactory dtf = DatatypeFactory.newInstance();
                                                    xmlFechaVencimiento = dtf.newXMLGregorianCalendar(calendar);
                                                    GregorianCalendar fecha = xmlFechaVencimiento.toGregorianCalendar();
                                                    infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
                                                    traza.trace("fecha de vencimiento " + sdf.format(fecha.getTime()), Level.INFO);
                                                    traza.trace("fecha de vencimiento " + xmlFechaVencimiento, Level.INFO);
                                                    break;
                                                } else {
                                                    throw new DW4JDesktopExcepcion("Debe colocar una fecha de vencimiento valida");
                                                }
                                            } else {
                                                tiene = false;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                        }

                        try {

                            for (InfoDocumento id : docDigitalizadoRecha) {
                                String nombre = id.getTipoDocumento().trim();
                                nombre = nombre.trim();
                                traza.trace("buscando tipo de documento rechazado para el dato adicional " + nombre, Level.INFO);
                                if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                    for (TipoDocumento td : tipoDocumentosDisponibles) {
                                        String tiDo = td.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                                        tiDo = tiDo.trim();
                                        if (tipoDocumentoDigitalizar.contains(tiDo)) {
                                            if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                                                expediente.setTipoDocumento(tipoDocumentoDigitalizar);
                                                expediente.setIdTipoDocumento(td.getIdTipoDocumento());
                                                DatoAdicional dag = new DatoAdicional(expediente, true, this, id.getNumeroDocumento(), id.getVersion());
                                                flag = true;
                                                break;
                                            } else {
                                                tiene = false;
                                            }
                                        }
                                    }
                                }
                            }

                            if (!tiene) {
                                flag = true;
                            }
                        } catch (NullPointerException e) {
                        }

                        if (!docDigitalizadoRecha.isEmpty()) {

                            for (InfoDocumento infoDoc : docDigitalizadoRecha) {

                                String nombre = infoDoc.getTipoDocumento().trim();
                                nombre = nombre.trim();

                                if (tipoDocumentoDigitalizar.equalsIgnoreCase(nombre)) {

                                    idDoc = infoDoc.getIdDocumento();
                                    digExpe.setTipoDocumento(tipoDocumentoDigitalizar);
                                    digExpe.setIdTipoDocumento(idDoc);
                                    ManejoSesion.setExpediente(digExpe);

                                    infoDoc.setDatoAdicional(infoDocumento.getDatoAdicional());
                                    infoDocumento = infoDoc;
                                    infoDocumento.setIdExpediente(ManejoSesion.getExpediente().getIdExpediente());
                                    infoDocumento.setReDigitalizo(true);
                                    if (lsDatosAdicionales != null) {
                                        for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
                                            da.setVersion(infoDocumento.getVersion());
                                            infoDocumento.getLsDatosAdicionales().add(da);
                                        }
                                    }

                                    if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                                        infoDocumento.setEstatus(0);
                                    } else {
                                        infoDocumento.setEstatus(1);
                                    }

//                                    if (xmlFechaVencimiento != null) {
//                                        infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
//                                    }
                                    traza.trace("re-digitalizando el documento " + infoDoc.getTipoDocumento(), Level.INFO);
                                    traza.trace("version del documento " + infoDocumento.getVersion(), Level.INFO);
                                    traza.trace("numero del documento " + infoDocumento.getNumeroDocumento(), Level.INFO);
                                    if (flag) {
                                        new EscaneaDocumento("Guardar", infoDocumento, false).escanearDocumento();
                                        break;
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No hay documento rechazado para " + accion.toUpperCase(), "Alerta...", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Elija uno de los documentos rechazados", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (!jtrDocumentosDisponibles.isSelectionEmpty()) {

                verificador = (DefaultMutableTreeNode) jtrDocumentosDisponibles.getLastSelectedPathComponent();

                if (!verificador.isLeaf()) {

                    JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser digitalizado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
                } else {

                    categoria = verificador.getRoot();
                    subCategoria = verificador.getParent();
                    tipoDocumentoDigitalizar = verificador.toString();

                    int n = JOptionPane.showOptionDialog(this,
                            "Desea  crear un nuevo documento \n(" + tipoDocumentoDigitalizar + ") del expediente \n" + ManejoSesion.getExpediente().getIdExpediente(),
                            "¿?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, new Object[]{"SI", "NO"}, "NO");

                    if (n == JOptionPane.YES_OPTION) {

                        traza.trace("se creara el tipo de documento " + tipoDocumentoDigitalizar, Level.INFO);

                        if (!mapTipoDocumentoDisponible.isEmpty()) {
                            idDoc = mapTipoDocumentoDisponible.get(tipoDocumentoDigitalizar);
                        } else {
                            throw new DW4JDesktopExcepcion("No se puede digitalizar \n no hay tipos de documentos");
                        }

                        try {
                            infoDocumento = new InfoDocumento();
                            for (TipoDocumento td : tipoDocumentosDisponibles) {
                                if (tipoDocumentoDigitalizar.contains(td.getTipoDocumento().trim())) {
                                    if (td.getVencimiento().equalsIgnoreCase("1")) {
                                        if (jdDocumentoDisponible.getCalendar() != null) {
                                            jdDocumentoDisponible.setEnabled(false);
                                            calendar = (GregorianCalendar) jdDocumentoDisponible.getCalendar();
                                            DatatypeFactory dtf = DatatypeFactory.newInstance();
                                            xmlFechaVencimiento = dtf.newXMLGregorianCalendar(calendar);
                                            GregorianCalendar fecha = xmlFechaVencimiento.toGregorianCalendar();
                                            traza.trace("fecha de vencimiento " + sdf.format(fecha.getTime()), Level.INFO);
                                            traza.trace("fecha de vencimiento " + xmlFechaVencimiento, Level.INFO);
                                            infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
                                            break;
                                        } else {
                                            throw new DW4JDesktopExcepcion("Debe colocar una fecha de vencimiento valida");
                                        }
                                    }
                                }
                            }

                        } catch (NullPointerException e) {
                        }

                        try {
                            for (TipoDocumento td : tipoDocumentosDisponibles) {
                                if (tipoDocumentoDigitalizar.contains(td.getTipoDocumento().trim())) {
                                    if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                                        infoDocumento.setTipoDocDatoAdicional(true);
                                        tiene = true;
                                        expediente.setTipoDocumento(tipoDocumentoDigitalizar);
                                        expediente.setIdTipoDocumento(td.getIdTipoDocumento());
                                        DatoAdicional dag = new DatoAdicional(expediente, false, this, 0, 0);
                                        for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
                                            infoDocumento.getLsDatosAdicionales().add(da);
                                            flag = true;
                                        }
                                        break;
                                    } else {
                                        tiene = false;
                                    }
                                }
                            }

                            if (!tiene) {
                                flag = true;
                            }
                        } catch (NullPointerException e) {
                        }

                        digExpe.setTipoDocumento(tipoDocumentoDigitalizar);
                        digExpe.setIdTipoDocumento(idDoc);
                        ManejoSesion.setExpediente(digExpe);
                        infoDocumento.setTipoDocumento(tipoDocumentoDigitalizar);
                        infoDocumento.setIdDocumento(idDoc);
                        infoDocumento.setIdExpediente(ManejoSesion.getExpediente().getIdExpediente());
                        infoDocumento.setReDigitalizo(false);

                        if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                            infoDocumento.setEstatus(0);
                        } else {
                            infoDocumento.setEstatus(1);
                        }

                        traza.trace("digitalizando el documento " + tipoDocumentoDigitalizar, Level.INFO);
                        traza.trace("version del documento " + infoDocumento.getVersion(), Level.INFO);
                        traza.trace("numero del documento " + infoDocumento.getNumeroDocumento(), Level.INFO);
                        if (flag) {
                            new EscaneaDocumento(accion, infoDocumento, false).escanearDocumento();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Por favor seleccione el documento del arbol digitalizados,\n y luego use los botones segun su preferencia", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Elija uno de los documentos disponibles", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DatatypeConfigurationException ex) {
            JOptionPane.showMessageDialog(this, "Problemas al convertir la fecha de vencimiento", "Error", JOptionPane.ERROR_MESSAGE);
            traza.trace("problemas al convertir la fecha de vencimiento", Level.ERROR, ex);
        } catch (DW4JDesktopExcepcion ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            traza.trace(ex.getMessage(), Level.ERROR, ex);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "problemas general al tratar de digitalizar un tipo de documento " + tipoDocumentoDigitalizar, "Error", JOptionPane.ERROR_MESSAGE);
            traza.trace("problemas general al tratar de digitalizar un tipo de documento " + tipoDocumentoDigitalizar, Level.ERROR, e);
        }

    }

    private void jbBuscarExpedienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBuscarExpedienteActionPerformed

        String exped, subCat;
        try {
            if (!txtExpediente.getText().equals("")) {
                if (!cmbSubCategorias.getSelectedItem().equals("")) {
//                    if (txtExpediente.getText().matches(UtilidadPalabras.VALIDAR_NUMEROS)) {

                    jdDocumentoDisponible.setCalendar(null);
                    jdDocumentoDisponible.setDate(null);
                    jdDocumentoDisponible.setVisible(false);
                    jdDocumentoDisponible.setEnabled(false);
                    jlVencimientoDocDisp.setVisible(false);

//                        int tmp = Integer.parseInt(txtExpediente.getText());
//                        exped = String.valueOf(tmp);
//                        txtExpediente.setText(exped);
                    subCat = cmbSubCategorias.getSelectedItem().toString();
                    buscarExpediente(txtExpediente.getText(), subCat);

//                    } else {
//                        JOptionPane.showMessageDialog(this, "El Identificador del Expediente debe ser solo numeros", "Advertencia", JOptionPane.WARNING_MESSAGE);
//                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar alguna SubCategoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe introducir un identificador de expediente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
        }

    }//GEN-LAST:event_jbBuscarExpedienteActionPerformed

    public void find() {
        System.gc();
        txtExpediente.setText(ManejoSesion.getExpediente().getIdExpediente());
        cmbSubCategorias.setSelectedItem(ManejoSesion.getExpediente().getSubCategoria());
        buscarExpediente(txtExpediente.getText(), cmbSubCategorias.getSelectedItem().toString());
    }

    private void crearExpediente() {

        ListaExpediente listaExpediente = new ListaExpediente(listaIndices, this);
        Principal.desktop.add(listaExpediente);
        listaExpediente.toFront();
    }

    private synchronized void buscarExpediente(final String expediente, final String subCategoria) {

        traza.trace("expediente a digitalizar " + expediente, Level.INFO);
        //String exped = null;
        final MostrarProceso proceso = new MostrarProceso("Verificando el Expediente " + expediente);

        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (comprobarExpediente(expediente, subCategoria)) {
                    crearObjetos();
                    construirArboles();
                    proceso.detener();
                } else {
                    proceso.detener();
                    if (crearExpediente) {
                        crearExpediente();
                    }
                }

            }
        }).start();
    }

    /**
     * Captura toda la información del expediente para ir a los documentos
     * disponibles y digitalizados
     */
    private synchronized boolean comprobarExpediente(String exped, String subCategoria) {
        //String subCat, expedient, subCateg;
        Expedientes expedienteBuscado;
        int idCat, idLib, idSubCat = 0, tmp;
//        List<TipoDocumento> tipoDocumentosDisp;// = new ArrayList<TipoDocumento>();
        boolean resp;

        try {
            traza.trace("expediente a digitalizar " + txtExpediente.getText(), Level.INFO);

            idCat = expediente.getIdCategoria();
            idLib = expediente.getIdLibreria();

            traza.trace("verificando que el expediente " + exped + " exista", Level.INFO);
            expedienteBuscado = new GestionExpediente().encuentraExpediente(exped, idCat, idLib);

            traza.trace("seleccion la subCategoria " + subCategoria, Level.INFO);

            for (SubCategoria sc : subCategorias) {
                if (subCategoria.equalsIgnoreCase(sc.getSubCategoria())) {
                    idSubCat = sc.getIdSubCategoria();
                    expediente.setIdSubCategoria(idSubCat);
                }
            }

            if (expedienteBuscado.isExiste()) {

                traza.trace("seleccion la categoria " + expediente.getCategoria(), Level.INFO);

                expediente.setIdExpediente(exped);
                expediente.setSubCategoria(subCategoria);

                traza.trace("libreria " + idLib + " - " + expediente.getLibreria(), Level.INFO);
                traza.trace("categoria " + idCat + " - " + expediente.getCategoria(), Level.INFO);
                traza.trace("subCategoria " + idSubCat + " - " + subCategoria, Level.INFO);
                traza.trace("expediente " + exped, Level.INFO);
                ManejoSesion.setExpediente(expediente);
                resp = true;

            } else {
                int n = JOptionPane.showOptionDialog(this, "El expediente \"" + exped + "\" no existe, \n¿desea crearlo?\n" + expedienteBuscado.getMensaje(), "Alerta...", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"SI", "NO"}, "NO");
                if (n == JOptionPane.YES_OPTION) {

                    traza.trace("el expediente " + exped + " no existe se creara", Level.INFO);

                    expediente.setIdExpediente(exped);
                    expediente.setSubCategoria(subCategoria);
                    expediente.setIndices(expedienteBuscado.getIndices());

                    traza.trace("libreria " + idLib + " - " + expediente.getLibreria(), Level.INFO);
                    traza.trace("categoria " + idCat + " - " + expediente.getCategoria(), Level.INFO);
                    traza.trace("subCategoria " + idSubCat + " - " + subCategoria, Level.INFO);

                    this.toBack();
                    listaIndices = expedienteBuscado.getListaIndices();
                    ManejoSesion.setExpediente(expediente);
                    crearExpediente = true;
                    resp = false;

                } else {
                    resp = false;
                    txtExpediente.setText("");
                    cmbSubCategorias.setSelectedItem("");
                    crearExpediente = false;
                    show();
                    repaint();
                }
            }
        } catch (SOAPException e) {
            JOptionPane.showMessageDialog(this, "Problemas buscando el expediente " + txtExpediente, "Error", JOptionPane.ERROR_MESSAGE);
            traza.trace("problemas buscando el expediente " + txtExpediente, Level.ERROR, e);
            resp = false;
        }

        return resp;
    }

    /**
     * Arma los indice dinamicos en el formulario
     */
    private void crearObjetos() {
        try {
            traza.trace("creando indices dinamicos", Level.INFO);
            GridBagConstraints constraints = new GridBagConstraints();
            CreaObjetosDinamicos uv = new CreaObjetosDinamicos();
            JPanel panelIndice;

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            panelIndices.removeAll();
            panelIndices.setLayout(new FlowLayout(FlowLayout.LEFT));
            panelIndice = uv.mostrarIndices(expediente);
            panelIndices.add(panelIndice);
            panelIndices.updateUI();

        } catch (Exception e) {
            traza.trace("error al crear indices dinamicos", Level.INFO, e);
        }
    }

    /**
     * metodo que crea los modelos de los arboles documento digitalizados
     * basicamente lo que hace es lo siguiente llama al servicio recupera
     * categoria subcategoria tipo de documentos este se cumple siempre en el
     * arbol documento disponibles en el arbol documentos digitalizados solo se
     * creara la categoria si hay por lo menos un tipo de documento digitalizado
     * la subcategoria si hay un tipo de documento digitalizado asociado a esa
     * subcategoria tipo de documento todos los tipos de documetos digitalizados
     * asociados a la subcategoria
     */
    private synchronized boolean construirArboles() {

        DefaultMutableTreeNode arbolDocDisponibles;
        DefaultMutableTreeNode ramaDocDisponibles;
        DefaultMutableTreeNode arbolDocDigitalizados;
        DefaultMutableTreeNode ramaDocDigitalizados;
        DefaultMutableTreeNode arbolRechazados = null;
        DefaultMutableTreeNode ramaRechazados;
        DefaultMutableTreeNode hijo;

        boolean resp = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<InfoDocumento> infoDocumentos;
        List<InfoDocumento> infoDocRechazados;
//        List<com.develcom.documento.DatoAdicional> datosAdicionales = new ArrayList<com.develcom.documento.DatoAdicional>();
        GestionExpediente btd = new GestionExpediente();
        List<Integer> idsDocumento = new ArrayList<>();
        String cat, subCat, nombreTD, tmp[], fullName;
        int idCat, idSubCat, totalDocDig, cont = 0, j = 0;

        try {

            traza.trace("armando los arboles de documentos", Level.INFO);
            traza.trace("llenando los arboles con los tipos de documentos", Level.INFO);

            idCat = expediente.getIdCategoria();
            idSubCat = expediente.getIdSubCategoria();

            cat = expediente.getCategoria();
            subCat = expediente.getSubCategoria();

            traza.trace("id categoria: " + idCat + " Categoria: " + cat + " idSubCategoria: " + idSubCat + " SubCategoria: " + subCat, Level.INFO);

            traza.trace("buscando los tipos de documentos disponibles", Level.INFO);

            //busca los tipos de documentos disponibles
            tipoDocumentosDisponibles.clear();
            tipoDocumentosDisponibles = new AdministracionBusqueda().buscarTipoDocumento(null, idCat, idSubCat);

            if (tipoDocumentosDisponibles.isEmpty()) {
                throw new DW4JDesktopExcepcion("La SubCategoria: \"" + subCat + "\"\n no posee tipos de documentos");
            }

            arbolDocDisponibles = new DefaultMutableTreeNode(cat);
            ramaDocDisponibles = new DefaultMutableTreeNode(subCat);

            //llena el arbol con los tipos de documentos disponibles
            mapTipoDocumentoDisponible.clear();
            for (TipoDocumento td : tipoDocumentosDisponibles) {

                if (td.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                    traza.trace("agregando la hoja " + td.getTipoDocumento(), Level.INFO);
                    ramaDocDisponibles.add(new DefaultMutableTreeNode(td.getTipoDocumento().trim()));

                    mapTipoDocumentoDisponible.put(td.getTipoDocumento().trim(), td.getIdTipoDocumento());
                    idsDocumento.add(td.getIdTipoDocumento());
                }
            }

            if (idsDocumento.isEmpty()) {
                throw new DW4JDesktopExcepcion("La SubCategoria: \"" + subCat + "\"\n tiene tipos de documentos inactivos");
            } else {

                arbolDocDisponibles.add(ramaDocDisponibles);

                traza.trace("buscando los tipos de documentos digitalizados", Level.INFO);

                //busca los tipos de documentos digitalizados
                documentosDigitalizado.clear();
                if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                    documentosDigitalizado = new GestionDocumentos().encontrarInformacionDoc(idsDocumento, expediente.getIdExpediente(), idCat, idSubCat, 0, 1, false);
                } else {
                    documentosDigitalizado = new GestionDocumentos().encontrarInformacionDoc(idsDocumento, expediente.getIdExpediente(), idCat, idSubCat, 1, 1, false);
                }

                //llena el arbol con los tipos de documentos digitalizados
                arbolDocDigitalizados = new DefaultMutableTreeNode(cat);
                ramaDocDigitalizados = new DefaultMutableTreeNode(subCat);

                totalDocDig = documentosDigitalizado.size();
                tmp = new String[totalDocDig];

                for (j = 0; j < totalDocDig; j++) {

                    InfoDocumento id = documentosDigitalizado.get(j);
                    nombreTD = id.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();

                    tmp[cont] = nombreTD;
                    cont++;
                    int h = 0;
                    for (String s : tmp) {
                        try {
                            if (s.equalsIgnoreCase(nombreTD)) {
                                h++;
                            }
                        } catch (NullPointerException e) {
                        }

                    }
                    if (h == 1) {
                        String da = "";
                        if (id.isTipoDocDatoAdicional()) {
                            List<com.develcom.documento.DatoAdicional> datosAdicionales = id.getLsDatosAdicionales();
                            int size = datosAdicionales.size(), i = 1;
                            for (com.develcom.documento.DatoAdicional datAd : datosAdicionales) {

                                if (datAd.getTipo().equalsIgnoreCase("fecha")) {
                                    if (i == size) {
                                        da = da + " " + datAd.getValor();
                                    } else {
                                        da = da + " " + datAd.getValor() + ",";
                                    }
                                } else if (i == size) {
                                    da = da + " " + datAd.getValor();
                                } else {
                                    da = da + " " + datAd.getValor() + ",";
                                }

                                i++;
                            }
                        }
                        da = da.trim();
                        if (ManejoSesion.getConfiguracion().isCalidadActivo()) {

                            if (id.isTipoDocDatoAdicional()) {
                                fullName = nombreTD + " " + id.getEstatusDocumento() + " (" + da + ")";
                                hijo = new DefaultMutableTreeNode(fullName);
                            } else {
                                fullName = nombreTD + " " + id.getEstatusDocumento();
                                hijo = new DefaultMutableTreeNode(fullName);
                            }

                            traza.trace("nombre y numero del documento encontrado " + fullName, Level.INFO);
                            id.setTipoDocumento(fullName);
                            documentosDigitalizado.get(j).setTipoDocumento(fullName);
                            ramaDocDigitalizados.add(hijo);
                        } else {
                            if (id.isTipoDocDatoAdicional()) {
                                fullName = nombreTD + " (" + da + ")";
                                hijo = new DefaultMutableTreeNode(fullName);
                            } else {
                                fullName = nombreTD;
                                hijo = new DefaultMutableTreeNode(fullName);
                            }

                            traza.trace("nombre y numero del documento encontrado " + fullName, Level.INFO);
                            id.setTipoDocumento(fullName);
                            documentosDigitalizado.get(j).setTipoDocumento(fullName);
                            ramaDocDigitalizados.add(hijo);
                        }
                    } else {
                        documentosDigitalizado.remove(j);
                        j--;
                        totalDocDig = totalDocDig - 1;
                    }
                }

                arbolDocDigitalizados.add(ramaDocDigitalizados);

                traza.trace("buscando los tipos de documentos rechazados", Level.INFO);

                docDigitalizadoRecha.clear();
                infoDocRechazados = new GestionDocumentos().encontrarInformacionDoc(idsDocumento, expediente.getIdExpediente(), idCat, idSubCat, 2, 0, false);

                if (arbolRechazados == null) {
                    arbolRechazados = new DefaultMutableTreeNode(cat);
                }

                //llena el arbol con los tipos de documentos digitalizados rechazados
                ramaRechazados = new DefaultMutableTreeNode(subCat);

                totalDocDig = infoDocRechazados.size();

                tmp = new String[totalDocDig];
                cont = 0;

                for (InfoDocumento id : infoDocRechazados) {

                    nombreTD = id.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                    tmp[cont] = nombreTD;
                    cont++;
                    int h = 0;
                    for (String s : tmp) {
                        try {
                            if (s.equalsIgnoreCase(nombreTD)) {
                                h++;
                            }
                        } catch (NullPointerException e) {
                        }

                    }
                    if (h == 1) {
                        String da = "";
                        if (id.isTipoDocDatoAdicional()) {
                            List<com.develcom.documento.DatoAdicional> datosAdicionales = id.getLsDatosAdicionales();
                            int size = datosAdicionales.size(), i = 1;
                            for (com.develcom.documento.DatoAdicional datAd : datosAdicionales) {

                                if (datAd.getTipo().equalsIgnoreCase("fecha")) {
//                                XMLGregorianCalendar calendar = (XMLGregorianCalendar) datAd.getValor();
//                                Calendar cal = calendar.toGregorianCalendar();
                                    if (i == size) {
                                        da = da + " " + datAd.getValor();
                                    } else {
                                        da = da + " " + datAd.getValor() + ",";
                                    }
                                } else if (i == size) {
                                    da = da + " " + datAd.getValor();
                                } else {
                                    da = da + " " + datAd.getValor() + ",";
                                }

                                i++;
                            }
                        }
                        da = da.trim();
                        if (id.isTipoDocDatoAdicional()) {
                            fullName = nombreTD + " (" + da + ")";
                            hijo = new DefaultMutableTreeNode(fullName);
                        } else {
                            fullName = nombreTD;
                            hijo = new DefaultMutableTreeNode(fullName);
                        }
                        id.setTipoDocumento(fullName);
                        docDigitalizadoRecha.add(id);
                        ramaRechazados.add(hijo);
                    }
                }

                arbolRechazados.add(ramaRechazados);

                idsDocumento.clear();

                traza.trace("terminando de construir los arboles", Level.INFO);

                DefaultTreeModel modelDisp = new DefaultTreeModel(arbolDocDisponibles);
                jtrDocumentosDisponibles.setModel(modelDisp);

                for (Enumeration e = ((TreeNode) jtrDocumentosDisponibles.getModel().getRoot()).children(); e.hasMoreElements();) {
                    TreeNode tn = (TreeNode) e.nextElement();
                    jtrDocumentosDisponibles.expandPath(new TreePath(((DefaultTreeModel) jtrDocumentosDisponibles.getModel()).getPathToRoot(tn)));
                }

                DefaultTreeModel modelDigit = new DefaultTreeModel(arbolDocDigitalizados);
                jtrDocumentosDigitalizados.setModel(modelDigit);

                for (Enumeration e = ((TreeNode) jtrDocumentosDigitalizados.getModel().getRoot()).children(); e.hasMoreElements();) {
                    TreeNode tn = (TreeNode) e.nextElement();
                    jtrDocumentosDigitalizados.expandPath(new TreePath(((DefaultTreeModel) jtrDocumentosDigitalizados.getModel()).getPathToRoot(tn)));
                }

                DefaultTreeModel modelRecha = new DefaultTreeModel(arbolRechazados);
                jtrDocumentosRechazados.setModel(modelRecha);

                for (Enumeration e = ((TreeNode) jtrDocumentosRechazados.getModel().getRoot()).children(); e.hasMoreElements();) {
                    TreeNode tn = (TreeNode) e.nextElement();
                    jtrDocumentosRechazados.expandPath(new TreePath(((DefaultTreeModel) jtrDocumentosRechazados.getModel()).getPathToRoot(tn)));
                }

                if (!infoDocRechazados.isEmpty()) {

                    if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                        panelRechazados.setVisible(true);
                    } else {
                        panelRechazados.setVisible(false);
                    }
                } else {
                    panelRechazados.setVisible(false);
                }

                if (!infoDocRechazados.isEmpty()) {
                    infoDocRechazados.clear();
                }

                resp = true;
            }
        } catch (DW4JDesktopExcepcion e) {

            JOptionPane.showMessageDialog(this, e.getMessage(), "Adverntencia", JOptionPane.WARNING_MESSAGE);
            jbtCerrarActionPerformed(new ActionEvent(e, 0, ""));
            this.dispose();
            Principal.desktop.removeAll();
            Libreria libreria = new Libreria(ManejoSesion.getSesion(), "Seleccionar Libreria - Digitalizar");
            Principal.desktop.add(libreria);
        } catch (Exception e) {
            traza.trace("Error al crear el arbol de documentos", Level.ERROR, e);
        }
        return resp;

    }

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        new EscaneaDocumento(null, null, false).seleccionarEscaner();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void btnExplorarDocRechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExplorarDocRechActionPerformed
        buscarArchivo();
    }//GEN-LAST:event_btnExplorarDocRechActionPerformed

    private void btnEscanearDocRechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscanearDocRechActionPerformed
        digitalizar("reDigitalizar");
    }//GEN-LAST:event_btnEscanearDocRechActionPerformed

    private void btnReemplazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReemplazarActionPerformed
        gestionDocumento("reemplazar");
    }//GEN-LAST:event_btnReemplazarActionPerformed

    private void btnVersionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVersionarActionPerformed
        gestionDocumento("versionar");
    }//GEN-LAST:event_btnVersionarActionPerformed

    /**
     * Gestiona los documento si se reemplazará o versionará
     *
     * @param accion
     */
    private void gestionDocumento(String accion) {

        InfoDocumento infoDocumento;// = new InfoDocumento();
        GestionDocumentos gd = new GestionDocumentos();
        com.develcom.dao.Expediente expe = ManejoSesion.getExpediente(); //new com.develcom.dto.Expediente();
        XMLGregorianCalendar xmlFechaVencimiento = null;
        GregorianCalendar calendar;
        DefaultMutableTreeNode verificador;
        TreeNode categoria;
        TreeNode subCategoria;
        String tipoDocumento = null, tiDo;
        int idDoc, version = 0, numDoc;
        boolean flag = false, tiene = true, explora;

        traza.trace("accion a realizar " + accion, Level.INFO);

        try {
            Constantes.ACCION = "GUARDAR";

            if (!jtrDocumentosDigitalizados.isSelectionEmpty()) {

                verificador = (DefaultMutableTreeNode) jtrDocumentosDigitalizados.getLastSelectedPathComponent();
                if (verificador.isLeaf()) {
                    categoria = verificador.getRoot();
                    subCategoria = verificador.getParent();
                    tipoDocumento = verificador.toString().trim();
                    //idDoc = mapTipodocumento.get(tipoDocuemnto);
                    traza.trace("tipo de documento digitalizado seleccionado " + tipoDocumento, Level.INFO);

                    if (accion.equalsIgnoreCase("versionar")) {
                        if (tipoDocumento.contains("Pendiente")) {
                            throw new DW4JDesktopExcepcion("Documento con estatus pendiente no se puede versionar");
                        }
                    }

                    int n = JOptionPane.showOptionDialog(this,
                            "Desea digitalizar el tipo de documento\n" + tipoDocumento.trim() + "\n desde el escaner",
                            "¿?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, new Object[]{"SI", "NO"}, "SI");

                    if (n == JOptionPane.YES_OPTION) {
                        explora = false;
                        flag = true;
                    } else {
                        explora = true;
                    }

                    try {

                        for (InfoDocumento id : documentosDigitalizado) {
                            traza.trace("buscando tipo de documento para la fecha de vencimiento " + id.getTipoDocumento().trim(), Level.INFO);

                            if (tipoDocumento.contains(id.getTipoDocumento().trim())) {

                                for (TipoDocumento td : tipoDocumentosDisponibles) {
                                    if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                                        tiDo = td.getTipoDocumento() + " - " + id.getNumeroDocumento() + " " + id.getEstatusDocumento();
                                    } else {
                                        tiDo = td.getTipoDocumento() + " - " + id.getNumeroDocumento();
                                    }
                                    tiDo = tiDo.trim();

                                    if (tipoDocumento.contains(tiDo)) {
                                        if (td.getVencimiento().equalsIgnoreCase("1")) {
                                            if (jdDocumentoDisponible.getCalendar() != null) {
                                                calendar = (GregorianCalendar) jdDocumentoDisponible.getCalendar();
                                                DatatypeFactory dtf = DatatypeFactory.newInstance();
                                                xmlFechaVencimiento = dtf.newXMLGregorianCalendar(calendar);
                                                jdDocumentoDisponible.setEnabled(false);

                                                break;
                                            } else {
                                                throw new DW4JDesktopExcepcion("Debe colocar una fecha de vencimiento valida");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (NullPointerException e) {
                    }

                    try {
                        for (InfoDocumento id : documentosDigitalizado) {
                            traza.trace("buscando tipo de documento para el dato adicional " + id.getTipoDocumento().trim(), Level.INFO);

                            if (tipoDocumento.contains(id.getTipoDocumento().trim())) {

                                for (TipoDocumento td : tipoDocumentosDisponibles) {
                                    if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                                        tiDo = td.getTipoDocumento() + " - " + id.getNumeroDocumento() + " " + id.getEstatusDocumento();
                                    } else {
                                        tiDo = td.getTipoDocumento() + " - " + id.getNumeroDocumento();
                                    }
                                    tiDo = tiDo.trim();

                                    if (tipoDocumento.contains(tiDo)) {
                                        if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                                            tiene = true;
                                            expediente.setTipoDocumento(tipoDocumento);
                                            expediente.setIdTipoDocumento(td.getIdTipoDocumento());
                                            if (accion.equalsIgnoreCase("versionar")) {
                                                DatoAdicional dag = new DatoAdicional(expediente, false, this, id.getNumeroDocumento(), id.getVersion());
                                                flag = !lsDatosAdicionales.isEmpty();
                                            } else {
                                                DatoAdicional dag = new DatoAdicional(expediente, true, this, id.getNumeroDocumento(), id.getVersion());
                                                flag = !lsDatosAdicionales.isEmpty();
                                            }
                                            break;
                                        } else {
                                            tiene = false;
                                        }
                                    }
                                }
                                if (!tiene) {
                                    flag = true;
                                }
                            }
                        }

                    } catch (NullPointerException e) {
                    }

                    if (!documentosDigitalizado.isEmpty()) {
                        for (InfoDocumento infoDoc : documentosDigitalizado) {

                            if (tipoDocumento.contains(infoDoc.getTipoDocumento().trim())) {

                                idDoc = infoDoc.getIdDocumento();
                                numDoc = infoDoc.getNumeroDocumento();

                                infoDocumento = infoDoc;
                                if (xmlFechaVencimiento != null) {
                                    infoDocumento.setFechaVencimiento(xmlFechaVencimiento);
                                }

                                expe.setTipoDocumento(infoDoc.getTipoDocumento().trim());
                                expe.setIdTipoDocumento(idDoc);
                                expe.setIdInfoDocumento(infoDoc.getIdInfoDocumento());

                                infoDocumento.setIdExpediente(ManejoSesion.getExpediente().getIdExpediente());

                                if (accion.equalsIgnoreCase("versionar")) {
                                    traza.trace("actual version del documento " + infoDoc.getVersion(), Level.INFO);
                                    version = gd.buscarVersionUltima(idDoc, ManejoSesion.getExpediente().getIdExpediente(), numDoc) + 1;
                                    traza.trace("version nueva del documento " + version, Level.INFO);
                                    infoDocumento.setVersion(version);
                                }

                                if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                                    infoDocumento.setEstatus(0);
                                } else {
                                    infoDocumento.setEstatus(1);
                                }

                                if (lsDatosAdicionales != null) {
                                    infoDocumento.getLsDatosAdicionales().clear();
                                    for (com.develcom.documento.DatoAdicional da : lsDatosAdicionales) {
                                        da.setVersion(infoDocumento.getVersion());
                                        infoDocumento.getLsDatosAdicionales().add(da);
                                    }
                                }

                                ManejoSesion.setExpediente(expe);
                                traza.trace("version del documento " + infoDocumento.getVersion(), Level.INFO);
                                traza.trace("numero del documento " + infoDocumento.getNumeroDocumento(), Level.INFO);
                                if (flag) {
                                    if (explora) {
                                        new EscaneaDocumento(accion, infoDocumento, true).buscarArchivo();
                                    } else {
                                        new EscaneaDocumento(accion, infoDocumento, false).escanearDocumento();
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay tipo de documento a " + accion.toUpperCase(), "Alerta...", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Elija uno de los documentos digitalizados", "Información...", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (DatatypeConfigurationException ex) {
            traza.trace("problemas al convertir la fecha de vencimiento", Level.ERROR, ex);
        } catch (DW4JDesktopExcepcion ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            traza.trace("problemas general al tratar de digitalizar un tipo de documento " + tipoDocumento, Level.ERROR, e);
        }
    }

    private void btnVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisualizarActionPerformed

        final MostrarProceso proceso = new MostrarProceso("Comprobando datos");
        proceso.start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                abrir();
                proceso.detener();
            }

        }).start();
    }//GEN-LAST:event_btnVisualizarActionPerformed

    /**
     * muestra un documento digitalizado
     */
    private void abrir() {

        InfoDocumento infoDocumento = null;
        DefaultMutableTreeNode verificador;
        TreeNode categoria;
        TreeNode subCategoria;
        String tipoDocumento;
//        TreePath path;
        int index;

        if (!jtrDocumentosDigitalizados.isSelectionEmpty()) {
            Constantes.ACCION = "CAPTURAR";
            verificador = (DefaultMutableTreeNode) jtrDocumentosDigitalizados.getLastSelectedPathComponent();

            if (!verificador.isLeaf()) {
                JOptionPane.showMessageDialog(new JFrame(), "Debe elegir un tipo de documento para ser consultado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
            } else {
                categoria = verificador.getRoot();
                subCategoria = verificador.getParent();
                tipoDocumento = verificador.toString();

                traza.trace("tipo de documento seleccionado " + tipoDocumento, Level.INFO);

                for (InfoDocumento infDoc : documentosDigitalizado) {
                    traza.trace("tipo de documento digitalizado " + infDoc.getTipoDocumento().trim(), Level.INFO);

                    if (infDoc.getTipoDocumento().trim().contains(tipoDocumento)) {
                        ManejoSesion.getExpediente().setTipoDocumento(tipoDocumento);
                        //if (tipoDocumento.startsWith(infDoc.getTipoDocumento().trim())) {

                        traza.trace("tipo de documento a mostrar " + tipoDocumento, Level.INFO);
                        traza.trace("nombre del archivo " + infDoc.getNombreArchivo(), Level.INFO);
                        traza.trace("ruta del archivo " + infDoc.getRutaArchivo(), Level.INFO);
                        traza.trace("version del archivo " + infDoc.getVersion(), Level.INFO);
                        traza.trace("id del archivo " + infDoc.getIdInfoDocumento(), Level.INFO);
                        traza.trace("numero del documento (archivo) " + infDoc.getNumeroDocumento(), Level.INFO);
                        traza.trace("id del documento " + infDoc.getIdDocumento(), Level.INFO);
                        traza.trace("fecha vencimiento del archivo " + infDoc.getFechaVencimiento(), Level.INFO);
                        traza.trace("estatus documento " + infDoc.getEstatusDocumento(), Level.INFO);
                        traza.trace("formato del documento " + infDoc.getFormato(), Level.INFO);

                        infoDocumento = infDoc;
                        break;
                    }
                }

                try {
                    if (infoDocumento != null) {
                        dispose();

                        if (infoDocumento.getFormato().equalsIgnoreCase("PDF")) {
                            VerDocumentoPDF vdpdf = new VerDocumentoPDF("", infoDocumento, null, false, false, 0);
                            Principal.desktop.add(vdpdf);
                        } else if (infoDocumento.getFormato().equalsIgnoreCase("TIF") || infoDocumento.getFormato().equalsIgnoreCase("TIFF")) {
                            VerDocumento vd = new VerDocumento(infoDocumento);
                            Principal.desktop.add(vd);
                        } else {
                            VerImagenes vi = new VerImagenes("", infoDocumento, null, false, 0, false);
                            Principal.desktop.add(vi);
                        }
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "No hay tipo de documente a visualizar", "Alerta...", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    traza.trace("error al obtener informacion del documento digitalizado", Level.ERROR, ex);
                    JOptionPane.showMessageDialog(new JFrame(), "error al obtener informacion del documento digitalizado\n" + ex.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);

                }
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Elija uno de los documentos digitalizados", "Información...", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void jtrDocumentosDigitalizadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtrDocumentosDigitalizadosMouseClicked

        DefaultMutableTreeNode verificador;
        TreeNode categoria;
        TreeNode subCategoria;
        String tipoDocumento;
        GregorianCalendar fechaActual;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            jtrDocumentosDisponibles.clearSelection();
            jtrDocumentosRechazados.clearSelection();

            verificador = (DefaultMutableTreeNode) jtrDocumentosDigitalizados.getLastSelectedPathComponent();
            categoria = verificador.getRoot();
            subCategoria = verificador.getParent();
            tipoDocumento = verificador.toString().trim();

            traza.trace("seleciono tipo de documento " + tipoDocumento, Level.INFO);

            jdDocumentoDisponible.setCalendar(null);
            jdDocumentoDisponible.setDate(null);
            jdDocumentoDisponible.setVisible(false);
            jdDocumentoDisponible.setEnabled(false);
            jlVencimientoDocDisp.setVisible(false);

            //fecha de vencimiento
            for (InfoDocumento id : documentosDigitalizado) {
                try {
                    if (tipoDocumento.contains(id.getTipoDocumento().trim())) {
                        XMLGregorianCalendar xmlCalendarActual = id.getFechaActual();
                        XMLGregorianCalendar xmlCalendarVencimiento = id.getFechaVencimiento();
                        GregorianCalendar fechVencimiento = xmlCalendarVencimiento.toGregorianCalendar();

                        //fechaActual = Fecha.tomarFecha();
                        fechaActual = xmlCalendarActual.toGregorianCalendar();

                        traza.trace("fecha de vencimiento del documento " + tipoDocumento + " " + new SimpleDateFormat("dd/MM/yyyy").format(fechVencimiento.getTime()), Level.INFO);
                        traza.trace("fecha del sistema " + new SimpleDateFormat("dd/MM/yyyy").format(fechaActual.getTime()), Level.INFO);

                        if (fechaActual.after(fechVencimiento)) {
                            JOptionPane.showMessageDialog(this, "Documento Vencido", "Alerta...", JOptionPane.WARNING_MESSAGE);
                        }

                        jdDocumentoDisponible.setCalendar(fechVencimiento);
                        jdDocumentoDisponible.setVisible(true);
                        jdDocumentoDisponible.setEnabled(true);
                        jlVencimientoDocDisp.setVisible(true);
                        break;
                    } else {
                        jdDocumentoDisponible.setVisible(false);
                        jdDocumentoDisponible.setCalendar(null);
                        jdDocumentoDisponible.setEnabled(false);
                        jlVencimientoDocDisp.setVisible(false);
                    }
                } catch (NullPointerException e) {
                    jdDocumentoDisponible.setVisible(false);
                    jdDocumentoDisponible.setCalendar(null);
                    jdDocumentoDisponible.setEnabled(false);
                    jlVencimientoDocDisp.setVisible(false);
                }

            }

        } catch (NullPointerException e) {

            jdDocumentoDisponible.setVisible(false);
            jdDocumentoDisponible.setCalendar(null);
            jdDocumentoDisponible.setEnabled(false);
            jlVencimientoDocDisp.setVisible(false);
        }
    }//GEN-LAST:event_jtrDocumentosDigitalizadosMouseClicked

    private void jbtFoliaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFoliaturaActionPerformed

        if (!txtExpediente.getText().equals("")) {

            //Expediente expediente = ManejoSesion.getExpediente();
            int idLib = expediente.getIdLibreria();
            int idCat = expediente.getIdCategoria();
            String idExpediente = txtExpediente.getText();

            traza.trace("imprimiendo la foliatura", Level.INFO);
            traza.trace("idLibreria: " + idLib, Level.INFO);
            traza.trace("idCategoria: " + idCat, Level.INFO);
            traza.trace("idExpediente: " + idExpediente, Level.INFO);

            foliaturas(idLib, idCat, idExpediente);

        } else {
            JOptionPane.showMessageDialog(this, "Debe introducir un identificador de expediente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jbtFoliaturaActionPerformed

    private void foliaturas(final int idLib, final int idCat, final String idExpediente) {
        final MostrarProceso proceso = new MostrarProceso("Generando la Foliatura");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                traza.trace("mostrando la ventana", Level.INFO);
                if (!new Foliatura().generarFoliatura(idLib, idCat, idExpediente)) {
                    JOptionPane.showMessageDialog(new JFrame(), "Problemas para crear la foliatura.\nProbablemente no hay documentos aprobados", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                proceso.detener();

            }
        }).start();
    }

    private void jtrDocumentosDisponiblesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtrDocumentosDisponiblesMouseClicked

        String tipoDocumento;

        try {
            this.jtrDocumentosDigitalizados.clearSelection();
            this.jtrDocumentosRechazados.clearSelection();

            DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) this.jtrDocumentosDisponibles.getLastSelectedPathComponent();
            TreeNode categoria = verificador.getRoot();
            TreeNode subCategoria = verificador.getParent();
            tipoDocumento = verificador.toString().trim();

            this.traza.trace("seleciono tipo de documento " + tipoDocumento, Level.INFO);

            this.jdDocumentoDisponible.setCalendar(null);
            this.jdDocumentoDisponible.setDate(null);
            this.jdDocumentoDisponible.setVisible(false);
            this.jdDocumentoDisponible.setEnabled(false);
            this.jlVencimientoDocDisp.setVisible(false);

            for (TipoDocumento td : this.tipoDocumentosDisponibles) {
                if (tipoDocumento.equalsIgnoreCase(td.getTipoDocumento().trim())) {
                    try {
                        if (td.getVencimiento().equalsIgnoreCase("1")) {
                            this.jlVencimientoDocDisp.setVisible(true);
                            this.jdDocumentoDisponible.setVisible(true);
                            this.jdDocumentoDisponible.setDateFormatString("dd/MM/yyyy");
                            this.jdDocumentoDisponible.setEnabled(true);
                            break;
                        }

                        this.jlVencimientoDocDisp.setVisible(false);
                        this.jdDocumentoDisponible.setCalendar(null);
                        this.jdDocumentoDisponible.setVisible(false);
                        this.jdDocumentoDisponible.setEnabled(false);
                    } catch (Exception e) {
                        this.jlVencimientoDocDisp.setVisible(false);
                        this.jdDocumentoDisponible.setCalendar(null);
                        this.jdDocumentoDisponible.setVisible(false);
                        this.jdDocumentoDisponible.setEnabled(false);
                    }
                }
            }
        } catch (NullPointerException e) {
            this.jlVencimientoDocDisp.setVisible(false);
            this.jdDocumentoDisponible.setCalendar(null);
            this.jdDocumentoDisponible.setVisible(false);
            this.jdDocumentoDisponible.setEnabled(false);
        }

    }//GEN-LAST:event_jtrDocumentosDisponiblesMouseClicked

    private void jtrDocumentosRechazadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtrDocumentosRechazadosMouseClicked

        String tipoDocumento;
        try {
            this.jtrDocumentosDisponibles.clearSelection();
            this.jtrDocumentosDigitalizados.clearSelection();

            DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) this.jtrDocumentosRechazados.getLastSelectedPathComponent();
            TreeNode categoria = verificador.getRoot();
            TreeNode subCategoria = verificador.getParent();
            tipoDocumento = verificador.toString().trim();

            this.jdDocumentoRechazado.setCalendar(null);
            this.jdDocumentoRechazado.setDate(null);
            this.jdDocumentoRechazado.setVisible(false);
            this.jdDocumentoRechazado.setEnabled(false);
            this.jlVencimientoDocRech.setEnabled(false);
            this.jlVencimientoDocRech.setVisible(false);

            for (InfoDocumento id : this.docDigitalizadoRecha) {
                try {
                    String nombre = id.getTipoDocumento().trim();
                    nombre = nombre.trim();
                    if (tipoDocumento.equalsIgnoreCase(nombre)) {
                        for (TipoDocumento td : this.tipoDocumentosDisponibles) {
                            String tiDo = td.getTipoDocumento().trim() + " - " + id.getNumeroDocumento();
                            tiDo = tiDo.trim();
                            if (tipoDocumento.contains(tiDo)) {
                                if (td.getVencimiento().equalsIgnoreCase("1")) {
                                    XMLGregorianCalendar xmlCalendarActual = id.getFechaActual();
                                    XMLGregorianCalendar xmlCalendarVencimiento = id.getFechaVencimiento();
                                    GregorianCalendar fechVencimiento = xmlCalendarVencimiento.toGregorianCalendar();

                                    GregorianCalendar fechaActual = xmlCalendarActual.toGregorianCalendar();

                                    this.traza.trace("fecha de vencimiento del documento " + tipoDocumento + " " + new SimpleDateFormat("dd/MM/yyyy").format(fechVencimiento.getTime()), Level.INFO);
                                    this.traza.trace("fecha del sistema " + new SimpleDateFormat("dd/MM/yyyy").format(fechaActual.getTime()), Level.INFO);

                                    if (fechaActual.after(fechVencimiento)) {
                                        JOptionPane.showMessageDialog(this, "Documento Vencido", "Alerta...", 2);
                                    }

                                    this.jdDocumentoRechazado.setCalendar(fechVencimiento);
                                    this.jdDocumentoRechazado.setVisible(true);
                                    this.jdDocumentoRechazado.setEnabled(true);
                                    this.jlVencimientoDocRech.setEnabled(true);
                                    this.jlVencimientoDocRech.setVisible(true);
                                    break;
                                }
                                this.jdDocumentoRechazado.setCalendar(null);
                                this.jdDocumentoRechazado.setVisible(false);
                                this.jdDocumentoRechazado.setEnabled(false);
                                this.jlVencimientoDocRech.setEnabled(false);
                                this.jlVencimientoDocRech.setVisible(false);
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    this.jdDocumentoRechazado.setCalendar(null);
                    this.jdDocumentoRechazado.setVisible(false);
                    this.jdDocumentoRechazado.setEnabled(false);
                    this.jlVencimientoDocRech.setEnabled(false);
                    this.jlVencimientoDocRech.setVisible(false);
                }
            }
        } catch (NullPointerException e) {
            this.jdDocumentoRechazado.setCalendar(null);
            this.jdDocumentoRechazado.setVisible(false);
            this.jdDocumentoRechazado.setEnabled(false);
            this.jlVencimientoDocRech.setEnabled(false);
            this.jlVencimientoDocRech.setVisible(false);
        }

    }//GEN-LAST:event_jtrDocumentosRechazadosMouseClicked

    public void setLsDatosAdicionales(List<com.develcom.documento.DatoAdicional> lsDatosAdicionales) {
        this.lsDatosAdicionales = lsDatosAdicionales;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEscanearDocDisp;
    private javax.swing.JButton btnEscanearDocRech;
    private javax.swing.JButton btnExplorarDocDisp;
    private javax.swing.JButton btnExplorarDocRech;
    private javax.swing.JButton btnReemplazar;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JButton btnVersionar;
    private javax.swing.JButton btnVisualizar;
    private javax.swing.JComboBox cmbSubCategorias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton jbBuscarExpediente;
    private javax.swing.JButton jbtCerrar;
    private javax.swing.JButton jbtFoliatura;
    private com.toedter.calendar.JDateChooser jdDocumentoDisponible;
    private com.toedter.calendar.JDateChooser jdDocumentoRechazado;
    private javax.swing.JLabel jlVencimientoDocDisp;
    private javax.swing.JLabel jlVencimientoDocRech;
    private javax.swing.JTree jtrDocumentosDigitalizados;
    private javax.swing.JTree jtrDocumentosDisponibles;
    private javax.swing.JTree jtrDocumentosRechazados;
    private javax.swing.JPanel panelDigitalizado;
    private javax.swing.JPanel panelDisponibles;
    private javax.swing.JPanel panelIndices;
    private javax.swing.JPanel panelRechazados;
    private javax.swing.JTextField txtExpediente;
    // End of variables declaration//GEN-END:variables

}
