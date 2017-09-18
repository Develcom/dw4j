/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * guardarDoc.java
 *
 * Created on 06/12/2011, 10:36:28 AM
 */
package com.develcom.gui.captura;

import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.Indice;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.gui.tools.ObjetosDinamicosDatosAdicionales;
import com.develcom.tools.Imagenes;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.cryto.EncriptaDesencripta;
import com.develcom.tools.trazas.Traza;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Level;
import uk.co.mmscomputing.application.imageviewer.ImagePanel;
import ve.com.develcom.archivo.GestionArchivos;

/**
 *
 * @author develcom
 */
public class GuardarDoc extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -3599088826691508411L;

    /**
     * Lista de bufer de imagenes
     */
    private ArrayList<BufferedImage> imagenesTiff;
    private double scalaImagen = 1;
    /**
     * Información del expediente
     */
    private Expediente expediente;
    /**
     * Total de paginas digitalizadas
     */
    private int cantidadPaginas = 0;
    /**
     * escribe trazas en el log
     */
    private Traza traza = new Traza(GuardarDoc.class);
    /**
     * lista de indices dinamicos
     */
    private List<Indice> indices = new ArrayList<Indice>();
    /**
     * accion a tomar (versionar o reemplazar)
     */
    private String accion;
    /**
     * objecto con la informacion del documento
     */
    private InfoDocumento infoDocumento;

    /**
     * Constructor Revisa si el documento tiene fecha de vencimiento, que accion
     * se realizará (guardar, versionar, reeemplazar), muestra la informacion de
     * los indices
     *
     * @param accion La accion a realizar (guardar, versionar, reeemplazar)
     * @param infoDocumento Objecto con la informacion del tipo de documento
     * @param ruta
     */
    public GuardarDoc(String accion, InfoDocumento infoDocumento, String ruta) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        initComponents();

        this.expediente = ManejoSesion.getExpediente();
        this.infoDocumento = infoDocumento;
        this.accion = accion;
        setTitle("Guardar Documento en el Expediente " + expediente.getIdExpediente() + " - " + expediente.getLibreria() + " - " + expediente.getCategoria() + " - " + expediente.getSubCategoria());

        jlTipoDocumento.setText(expediente.getTipoDocumento());
        jlLibreria.setText(expediente.getLibreria());
        jlCategoria.setText(expediente.getCategoria());
        jlSubCategoria.setText(expediente.getSubCategoria());
        traza.trace("accion " + accion, Level.INFO);

        if (infoDocumento.getFechaVencimiento() != null) {
            XMLGregorianCalendar xmlCalendar = infoDocumento.getFechaVencimiento();
            GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
            jlFechaVencimiento.setText("Fecha de vencimiento: " + sdf.format(rechaVencimiento.getTime()));

        } else {
            jlFechaVencimiento.setVisible(false);

        }

        if (accion.equalsIgnoreCase("versionar")) {
            jbGuadar.setText("Versionar");
            jbGuadar.setToolTipText("Versionar");
        } else if (accion.equalsIgnoreCase("reemplazar")) {
            jbGuadar.setText("Reemplazar");
            jbGuadar.setToolTipText("Reemplazar");
        }

        //crearObjetos();
        CentraVentanas.centrar(this, Principal.desktop);
        //mostrar(ruta);
        mostrarDocumentos(ruta);
        datosAdicionales();
    }

//    private void mostrar(final String ruta) {
//        final MostrarProceso proceso = new MostrarProceso("Abriendo el documento <br>" + expediente.getTipoDocumento());
//        proceso.start();
//        //proceso.mensaje("Armando los arboles");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (mostrarDocumentos(ruta)) {
//                    //crearObjetos();
//                    proceso.detener();
//                    setVisible(true);
//                }
//            }
//        }).start();
//    }
    /**
     * Carga la imagen para mostrarla
     *
     * @param ruta Ruta donde esta la imagen
     */
    private boolean mostrarDocumentos(String ruta) {
        boolean resp = false;
        try {

            traza.trace("cargando la imagen al panel de imagenes", Level.INFO);
            imagenesTiff = new ToolsFiles().open(ruta);
            cantidadPaginas = imagenesTiff.size();
            for (int i = 0; i < imagenesTiff.size(); i++) {

                ImagePanel imageTab = new ImagePanel();
                JScrollPane sp = new JScrollPane(imageTab);

                sp.getVerticalScrollBar().setUnitIncrement(100);
                sp.getHorizontalScrollBar().setUnitIncrement(100);
                imageTab.setImage(imagenesTiff.get(i));
                jtPaneImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
            }
            resp = true;
            crearObjetos();
            setVisible(true);
        } catch (IOException ex) {
            traza.trace("error al cargar la imagen", Level.INFO, ex);
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen\n" + ex.getMessage(), "Alerta", JOptionPane.ERROR_MESSAGE);
        }
        return resp;
    }

    /**
     * Crea los indices dinamicos en el formulario
     */
    private void crearObjetos() {
        traza.trace("creando indices dinamicos", Level.INFO);
        GridBagConstraints constraints = new GridBagConstraints();
        CreaObjetosDinamicos uv = new CreaObjetosDinamicos(this);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        try {
            panelIndices.setLayout(new FlowLayout(FlowLayout.LEFT));
            //panelIndices.add(uv.crearObjetos(expediente));
            panelIndices.add(uv.mostrarIndices(expediente));

        } catch (Exception e) {
            traza.trace("error al crear los indices dinamicos", Level.ERROR, e);
        }

    }

    private void datosAdicionales() {

        traza.trace("creando datos adicionales", Level.INFO);
        ObjetosDinamicosDatosAdicionales uv = new ObjetosDinamicosDatosAdicionales(expediente, null);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        panelDA.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        try {
            if (!infoDocumento.isTipoDocDatoAdicional()) {
                scrollDA.setVisible(false);
            } else {
//                JPanel panel = uv.mostrarIndicesGuardar(infoDocumento.getLsDatosAdicionales());
                panelDA.add(uv.mostrarIndicesGuardar(infoDocumento.getLsDatosAdicionales()));

            }
        } catch (NullPointerException ex) {
            traza.trace("error al armar los datos adicionales", Level.ERROR, ex);
            JOptionPane.showMessageDialog(new JFrame(), "Problemas al colocar los Datos Adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

        scrollIndices = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();
        jtPaneImagen = new javax.swing.JTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jButtonRotIzq = new javax.swing.JButton();
        jButtonRotDer = new javax.swing.JButton();
        jButtonZommOut = new javax.swing.JButton();
        jButtonRotar = new javax.swing.JButton();
        jButtonZommIn = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jbGuadar = new javax.swing.JButton();
        scrollDA = new javax.swing.JScrollPane();
        panelDA = new javax.swing.JPanel();
        scrollInfo = new javax.swing.JScrollPane();
        panelInfo = new javax.swing.JPanel();
        jlTituloTD = new javax.swing.JLabel();
        jlTipoDocumento = new javax.swing.JLabel();
        jlTituloTD1 = new javax.swing.JLabel();
        jlLibreria = new javax.swing.JLabel();
        jlTituloTD2 = new javax.swing.JLabel();
        jlCategoria = new javax.swing.JLabel();
        jlTituloTD3 = new javax.swing.JLabel();
        jlSubCategoria = new javax.swing.JLabel();
        jlFechaVencimiento = new javax.swing.JLabel();

        setBackground(new java.awt.Color(224, 239, 255));

        scrollIndices.setBackground(new java.awt.Color(224, 239, 255));
        scrollIndices.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 905, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );

        scrollIndices.setViewportView(panelIndices);

        jtPaneImagen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jtPaneImagen.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jtPaneImagen.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jtPaneImagen.setAutoscrolls(true);

        jToolBar1.setFloatable(false);

        jButtonRotIzq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Left.png"))); // NOI18N
        jButtonRotIzq.setMnemonic('b');
        jButtonRotIzq.setToolTipText("Pag.Anterior");
        jButtonRotIzq.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotIzq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotIzqActionPerformed(evt);
            }
        });

        jButtonRotDer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Forward.png"))); // NOI18N
        jButtonRotDer.setMnemonic('n');
        jButtonRotDer.setToolTipText("Pag. Siguiente");
        jButtonRotDer.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotDer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotDerActionPerformed(evt);
            }
        });

        jButtonZommOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomOut24.gif"))); // NOI18N
        jButtonZommOut.setMnemonic('-');
        jButtonZommOut.setToolTipText("Reducir Imagen");
        jButtonZommOut.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommOutActionPerformed(evt);
            }
        });

        jButtonRotar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Refresh.png"))); // NOI18N
        jButtonRotar.setMnemonic('r');
        jButtonRotar.setToolTipText("rotar la página");
        jButtonRotar.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotarActionPerformed(evt);
            }
        });

        jButtonZommIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomIn24.gif"))); // NOI18N
        jButtonZommIn.setMnemonic('+');
        jButtonZommIn.setToolTipText("Aumentar Imagen");
        jButtonZommIn.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommInActionPerformed(evt);
            }
        });

        jbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jbCancelar.setMnemonic('c');
        jbCancelar.setText("Cancelar");
        jbCancelar.setToolTipText("Cancelar");
        jbCancelar.setPreferredSize(new java.awt.Dimension(140, 40));
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jbGuadar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/save.png"))); // NOI18N
        jbGuadar.setMnemonic('g');
        jbGuadar.setText("Guardar");
        jbGuadar.setPreferredSize(new java.awt.Dimension(140, 40));
        jbGuadar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuadarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115)
                .addComponent(jbGuadar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jbGuadar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jToolBar1.add(jPanel1);

        scrollDA.setBackground(new java.awt.Color(224, 239, 255));
        scrollDA.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Adicionales"));

        panelDA.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelDALayout = new javax.swing.GroupLayout(panelDA);
        panelDA.setLayout(panelDALayout);
        panelDALayout.setHorizontalGroup(
            panelDALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );
        panelDALayout.setVerticalGroup(
            panelDALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        scrollDA.setViewportView(panelDA);

        scrollInfo.setBackground(new java.awt.Color(224, 239, 255));

        panelInfo.setBackground(new java.awt.Color(224, 239, 255));

        jlTituloTD.setText("Tipo de Documento");

        jlTipoDocumento.setText("jLabel1");
        jlTipoDocumento.setAutoscrolls(true);

        jlTituloTD1.setText("Libreria");

        jlLibreria.setText("jLabel2");
        jlLibreria.setAutoscrolls(true);

        jlTituloTD2.setText("Categoria");

        jlCategoria.setText("jLabel2");
        jlCategoria.setAutoscrolls(true);

        jlTituloTD3.setText("SubCategoria");

        jlSubCategoria.setText("jLabel2");
        jlSubCategoria.setAutoscrolls(true);

        jlFechaVencimiento.setText("Fecha Vencimiento");

        javax.swing.GroupLayout panelInfoLayout = new javax.swing.GroupLayout(panelInfo);
        panelInfo.setLayout(panelInfoLayout);
        panelInfoLayout.setHorizontalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(jlTituloTD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(103, 103, 103))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(jlTipoDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jlTituloTD1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlLibreria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlTituloTD2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlTituloTD3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlSubCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlFechaVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelInfoLayout.setVerticalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlTituloTD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTipoDocumento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTituloTD1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTituloTD2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlTituloTD3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlSubCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        scrollInfo.setViewportView(panelInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtPaneImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollIndices, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(scrollDA)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollInfo)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollIndices, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollDA, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jtPaneImagen))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cancela el guardar el documento digitalizado
     *
     * @param evt
     */
    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        traza.trace("cancelando guardar documento", Level.INFO);

        dispose();
        Principal.desktop.removeAll();
        DigitalizaDocumento dd = new DigitalizaDocumento();
        dd.find();
        Principal.desktop.add(dd);
        Principal.desktop.repaint();
    }//GEN-LAST:event_jbCancelarActionPerformed

    /**
     * Guarda el documento digitalizado
     *
     * @param evt
     */
    private void jbGuadarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuadarActionPerformed

        String acc = accion, msg;

        acc = acc.toLowerCase();
        char[] cs = acc.toCharArray();
        char ch = cs[0];
        cs[0] = Character.toUpperCase(ch);
        acc = String.valueOf(cs);

        msg = "Desea " + acc + " \n" + expediente.getTipoDocumento() + " \nen el expediente " + expediente.getIdExpediente();
        if (accion.equalsIgnoreCase("versionar")) {
            msg = "Desea " + acc + " \n" + expediente.getTipoDocumento() + "\n version " + infoDocumento.getVersion() + " \nen el expediente " + expediente.getIdExpediente();
        }

        int n = JOptionPane.showOptionDialog(this,
                msg,
                "¿?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"SI", "NO"}, "NO");

        if (n == JOptionPane.YES_OPTION) {

            traza.trace("guardando el documento", Level.INFO);

            enviar();
        }

    }//GEN-LAST:event_jbGuadarActionPerformed

    private void enviar() {
        final MostrarProceso proceso = new MostrarProceso("Guardando el documento");
        proceso.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (enviarArchivo()) {
                    dispose();
                    System.gc();
                    proceso.detener();
                    Principal.desktop.removeAll();
                    Principal.desktop.updateUI();
                    DigitalizaDocumento dd = new DigitalizaDocumento();
                    dd.find();
                    Principal.desktop.add(dd);

                } else {
                    proceso.detener();
                }
//                proceso.detener();
            }
        }).start();
    }

    /**
     * Guarda la informacion del documento y envia el archivo al servidor
     */
    private boolean enviarArchivo() {
        boolean resp = false;
        traza.trace("enviando el archivo", Level.INFO);
//        InfoDocumento infoDocumento, infoDoc;
        InfoDocumento infoDoc;
        //Zip zip = new Zip();
        ToolsFiles toolsTiff = new ToolsFiles();
        String dirTipoDoc, dirCat, dirSubCat, ruta, lib, idExpEncriptado, fileName, exito;
        Matcher mat;
        Pattern pat;
        int idDoc;
        //String archivoSerializado = "";
        InputStream leyendo = null;
        byte[] bu;
        Bufer buffer = new Bufer();
        pat = Pattern.compile(" ");

        //zip.comprimirArchivos(new File(toolsTiff.getArchivoCodificado()));
        try {

            lib = expediente.getLibreria();
            dirCat = expediente.getCategoria();
            dirSubCat = expediente.getSubCategoria();
            dirTipoDoc = expediente.getTipoDocumento();
            idDoc = expediente.getIdTipoDocumento();
            idExpEncriptado = expediente.getIdExpediente();

            mat = pat.matcher(lib);
            lib = mat.replaceAll("");

            mat = pat.matcher(dirCat);
            dirCat = mat.replaceAll("");

            mat = pat.matcher(dirSubCat);
            dirSubCat = mat.replaceAll("");

            mat = pat.matcher(dirTipoDoc);
            dirTipoDoc = mat.replaceAll("");

            //mat = pat.matcher(idExpEncriptado);
            //idExpEncriptado = mat.replaceAll("");
            lib = EncriptaDesencripta.encripta(lib);
            dirCat = EncriptaDesencripta.encripta(dirCat);
            dirSubCat = EncriptaDesencripta.encripta(dirSubCat);
            dirTipoDoc = EncriptaDesencripta.encripta(dirTipoDoc);
            //idExpEncriptado = EncriptaDesencripta.encripta(idExpEncriptado);
            ruta = lib + "/" + dirCat + "/" + dirSubCat;

            leyendo = new FileInputStream(toolsTiff.getArchivoCodificado());
            traza.trace("posible tamaño del archivo a transferir " + (leyendo.available() / 1024000), Level.INFO);
            bu = new byte[leyendo.available()];
            leyendo.read(bu);
            leyendo.close();
            buffer.setBufer(bu);

            infoDocumento.setNombreArchivo(dirTipoDoc);
            infoDocumento.setRutaArchivo(ruta);
            infoDocumento.setCantPaginas(cantidadPaginas);

            traza.trace("fecha vencimiento " + infoDocumento.getFechaVencimiento(), Level.INFO);
            exito = new GestionArchivos().almacenaArchivo(buffer, accion, infoDocumento, expediente.getIdExpediente(), ManejoSesion.getLogin());
            
            if (exito.equalsIgnoreCase("exito")) {
                traza.trace("se guardo el documento con exito", Level.INFO);
                JOptionPane.showMessageDialog(this, "Documento generado satisfactoriamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                imagenesTiff.clear();
                imagenesTiff = null;
                resp = true;
            } else {
                traza.trace("problemas al guardar el archivo objecto \n respuesta del webservices " + exito, Level.INFO);
                JOptionPane.showMessageDialog(this, "Problemas al guardar el documento, contacte al administrador\n" + exito, "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
                throw new DW4JDesktopExcepcion("problemas al guardar el archivo objecto");
            }

        } catch (OutOfMemoryError ex) {
            traza.trace("Error de memoria ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error\n" + ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

        } catch (FileNotFoundException ex) {
            traza.trace("Error archivo: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error archivo: \n" + ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            traza.trace("Error lectura archivo: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error lectura archivo: \n" + ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex) {
            traza.trace("Error puntero nulo ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo\nReintente de nuevo\n" + ex.getMessage(), "Error al guardar el archivo", JOptionPane.WARNING_MESSAGE);
        } catch (DW4JDesktopExcepcion ex) {
            traza.trace("Error ", Level.ERROR, ex);
//            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error General", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            traza.trace("Error general: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error General", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                leyendo.close();
            } catch (IOException ex) {
                traza.trace("error al cerrar el archivo", Level.ERROR, ex);
            }
        }
        return resp;
    }

    

    /**
     * Aleja la imagen
     *
     * @param evt
     */
    private void jButtonZommOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZommOutActionPerformed
        scalaImagen = scalaImagen * 0.8;
        ImagePanel imageTab = new ImagePanel();
        imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(jtPaneImagen.getSelectedIndex())));
        JScrollPane sp = new JScrollPane(imageTab);
        sp.getVerticalScrollBar().setUnitIncrement(100);
        sp.getHorizontalScrollBar().setUnitIncrement(100);
        jtPaneImagen.setComponentAt(jtPaneImagen.getSelectedIndex(), sp);
        jtPaneImagen.repaint();
    }//GEN-LAST:event_jButtonZommOutActionPerformed

    /**
     * Acerca la imagen
     *
     * @param evt
     */
    private void jButtonZommInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZommInActionPerformed
        scalaImagen = scalaImagen * 1.2;
        ImagePanel imageTab = new ImagePanel();
        imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(jtPaneImagen.getSelectedIndex())));
        JScrollPane sp = new JScrollPane(imageTab);
        sp.getVerticalScrollBar().setUnitIncrement(100);
        sp.getHorizontalScrollBar().setUnitIncrement(100);
        jtPaneImagen.setComponentAt(jtPaneImagen.getSelectedIndex(), sp);
        jtPaneImagen.repaint();
    }//GEN-LAST:event_jButtonZommInActionPerformed

    /**
     * Rota la imagen
     *
     * @param evt
     */
    private void jButtonRotarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotarActionPerformed
        ImagePanel imageTab = new ImagePanel();
        imagenesTiff.set(jtPaneImagen.getSelectedIndex(), Imagenes.rotate((BufferedImage) imagenesTiff.get(jtPaneImagen.getSelectedIndex()), 90));
        imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(jtPaneImagen.getSelectedIndex())));
        JScrollPane sp = new JScrollPane(imageTab);
        sp.getVerticalScrollBar().setUnitIncrement(100);
        sp.getHorizontalScrollBar().setUnitIncrement(100);
        jtPaneImagen.setComponentAt(jtPaneImagen.getSelectedIndex(), sp);
        jtPaneImagen.repaint();
    }//GEN-LAST:event_jButtonRotarActionPerformed

    /**
     * Va al izquierda de documento
     *
     * @param evt
     */
    private void jButtonRotIzqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotIzqActionPerformed
        try {
            if (jtPaneImagen.getSelectedIndex() - 1 >= 0) {
                jtPaneImagen.setSelectedIndex(jtPaneImagen.getSelectedIndex() - 1);
            }
        } catch (IndexOutOfBoundsException ex) {
            traza.trace("error al rotar la imagen", Level.ERROR, ex);
        }
    }//GEN-LAST:event_jButtonRotIzqActionPerformed

    /**
     * Va la derecha del documento
     *
     * @param evt
     */
    private void jButtonRotDerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotDerActionPerformed
        try {
            jtPaneImagen.setSelectedIndex(jtPaneImagen.getSelectedIndex() + 1);
        } catch (IndexOutOfBoundsException ex) {
            traza.trace("error al rotar la imagen", Level.ERROR, ex);
        }
    }//GEN-LAST:event_jButtonRotDerActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRotDer;
    private javax.swing.JButton jButtonRotIzq;
    private javax.swing.JButton jButtonRotar;
    private javax.swing.JButton jButtonZommIn;
    private javax.swing.JButton jButtonZommOut;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbGuadar;
    private javax.swing.JLabel jlCategoria;
    private javax.swing.JLabel jlFechaVencimiento;
    private javax.swing.JLabel jlLibreria;
    private javax.swing.JLabel jlSubCategoria;
    private javax.swing.JLabel jlTipoDocumento;
    private javax.swing.JLabel jlTituloTD;
    private javax.swing.JLabel jlTituloTD1;
    private javax.swing.JLabel jlTituloTD2;
    private javax.swing.JLabel jlTituloTD3;
    private javax.swing.JTabbedPane jtPaneImagen;
    private javax.swing.JPanel panelDA;
    private javax.swing.JPanel panelIndices;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JScrollPane scrollDA;
    private javax.swing.JScrollPane scrollIndices;
    private javax.swing.JScrollPane scrollInfo;
    // End of variables declaration//GEN-END:variables

    /**
     * Estable la informacion de los indices de la categoria seleccionada
     *
     * @param indices
     */
    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }
}
