/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.captura;

import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.gui.tools.ObjetosDinamicosDatosAdicionales;
import com.develcom.tools.UtilidadPalabras;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class DatoAdicional extends javax.swing.JDialog {

    private static final long serialVersionUID = 1792185644920856373L;

    private Traza traza = new Traza(DatoAdicional.class);
    private List<com.develcom.administracion.DatoAdicional> lsIndDatosAdicionales;
    private Expediente expediente;
    private DigitalizaDocumento digitalizaDocumento;
    private boolean mostrar;
    private int version;

    /**
     * Creates new form DatoAdicional
     *
     * @param expediente
     * @param valor
     * @param digitalizaDocumento
     * @param numeroDocumento
     * @param version
     */
    public DatoAdicional(Expediente expediente, boolean valor, DigitalizaDocumento digitalizaDocumento, int numeroDocumento, int version) {
        initComponents();
        setModal(true);
        this.expediente = expediente;
        this.digitalizaDocumento = digitalizaDocumento;
        this.version = version;

        jlExpediente.setText("Expediente: " + expediente.getIdExpediente());
        jlMensajeLibreria.setText("Libreria: " + expediente.getLibreria());
        jlMensajeCategoria.setText("Categoria: " + expediente.getCategoria());
        jlMensajeSubCategoria.setText("SubCategoria: " + expediente.getSubCategoria());
        jlMensajeTipoDocumento.setText("Tipo de Documento: " + expediente.getTipoDocumento());

        CentraVentanas.centerDialog(this);
        mostrar = false;
        objetos(valor, numeroDocumento, version);
        setVisible(true);
    }

    /**
     *
     * @param numeroDocumento
     * @param version
     */
    public DatoAdicional(int numeroDocumento, int version) {
        initComponents();
        setModal(true);
        this.expediente = ManejoSesion.getExpediente();

        jlExpediente.setText("Expediente: " + expediente.getIdExpediente());
        jlMensajeLibreria.setText("Libreria: " + expediente.getLibreria());
        jlMensajeCategoria.setText("Categoria: " + expediente.getCategoria());
        jlMensajeSubCategoria.setText("SubCategoria: " + expediente.getSubCategoria());
        jlMensajeTipoDocumento.setText("Tipo de Documento: " + expediente.getTipoDocumento());

        CentraVentanas.centerDialog(this);
        mostrar = true;
        objetos(false, numeroDocumento, version);
        setVisible(true);
    }

    public DatoAdicional(List<com.develcom.documento.DatoAdicional> lsDatosAdicionales) {
        initComponents();
        setModal(true);
        this.expediente = ManejoSesion.getExpediente();

        jlExpediente.setText("Expediente: " + expediente.getIdExpediente());
        jlMensajeLibreria.setText("Libreria: " + expediente.getLibreria());
        jlMensajeCategoria.setText("Categoria: " + expediente.getCategoria());
        jlMensajeSubCategoria.setText("SubCategoria: " + expediente.getSubCategoria());
        jlMensajeTipoDocumento.setText("Tipo de Documento: " + expediente.getTipoDocumento());

        CentraVentanas.centerDialog(this);

        objetos(lsDatosAdicionales);
        setVisible(true);
    }

    private void objetos(final List<com.develcom.documento.DatoAdicional> lsDatosAdicionales) {
        final MostrarProceso proceso = new MostrarProceso("Iniciando datos adicionales");
        final ObjetosDinamicosDatosAdicionales uv = new ObjetosDinamicosDatosAdicionales(expediente, this);
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel panel;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        panelIndices.setLayout(new FlowLayout(FlowLayout.CENTER));
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                jbGuardar.setVisible(false);
                panelIndices.add(uv.mostrarIndicesGuardar(lsDatosAdicionales));
                panelIndices.updateUI();
                proceso.detener();
            }
        }).start();
    }

    private void objetos(final boolean valor, final int numeroDocumento, final int version) {
        final MostrarProceso proceso = new MostrarProceso("Iniciando datos adicionales");
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                crearObjetos(valor, numeroDocumento, version);
                proceso.detener();
            }
        }).start();
    }

    /**
     * Crea los indice dinamicos en el formulario
     */
    private void crearObjetos(boolean valor, int numeroDocumento, int version) {
        GridBagConstraints constraints = new GridBagConstraints();
        ObjetosDinamicosDatosAdicionales uv = new ObjetosDinamicosDatosAdicionales(expediente, this);
        JPanel panel;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        panelIndices.setLayout(new FlowLayout(FlowLayout.CENTER));

        try {
            traza.trace("numero del documento " + numeroDocumento, Level.INFO);
            traza.trace("version del documento " + version, Level.INFO);
            if (valor) {
                panel = uv.llenarValorDatosAdicional(numeroDocumento);
                if (panel != null) {
                    panelIndices.add(panel);
                } else {
                    JOptionPane.showMessageDialog(this, "El tipo de documento no posee datos adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                if (mostrar) {
                    jbGuardar.setVisible(false);
                    panel = uv.crearObjetos(expediente, true, numeroDocumento, version);
                } else {
                    panel = uv.crearObjetos(expediente, false, 0, version);
                }
                if (panel != null) {
                    panelIndices.add(panel);
                } else {
                    JOptionPane.showMessageDialog(this, "El tipo de documento no posee datos adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
            panelIndices.updateUI();
        } catch (SOAPException e) {
            traza.trace("problemas al crear los objetos de datos adicionales", Level.ERROR, e);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();
        jbGuardar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jlMensajeLibreria = new javax.swing.JLabel();
        jlMensajeCategoria = new javax.swing.JLabel();
        jlMensajeSubCategoria = new javax.swing.JLabel();
        jlMensajeTipoDocumento = new javax.swing.JLabel();
        jlExpediente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Datos Adicionales");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));
        panelIndices.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Adicionales"));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 936, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelIndices);

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/dato2.png"))); // NOI18N
        jbGuardar.setMnemonic('e');
        jbGuardar.setText("Guardar");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        jbCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jbCancelar.setMnemonic('c');
        jbCancelar.setText("Cancelar");
        jbCancelar.setToolTipText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jScrollPane2.setBackground(new java.awt.Color(224, 239, 255));

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Información"));

        jlMensajeLibreria.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jlMensajeLibreria.setText("Libreria");
        jlMensajeLibreria.setAutoscrolls(true);

        jlMensajeCategoria.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jlMensajeCategoria.setText("Categoria");
        jlMensajeCategoria.setAutoscrolls(true);

        jlMensajeSubCategoria.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jlMensajeSubCategoria.setText("SubCategoria");
        jlMensajeSubCategoria.setAutoscrolls(true);

        jlMensajeTipoDocumento.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jlMensajeTipoDocumento.setText("Tipo de Documento");
        jlMensajeTipoDocumento.setAutoscrolls(true);

        jlExpediente.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jlExpediente.setText("Expediente");
        jlExpediente.setAutoscrolls(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlExpediente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlMensajeLibreria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlMensajeCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlMensajeSubCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlMensajeTipoDocumento, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlExpediente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensajeLibreria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensajeCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensajeSubCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensajeTipoDocumento)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbCancelar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbCancelar)
                    .addComponent(jbGuardar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed

        int n = JOptionPane.showOptionDialog(this,
                "¿Desea Guardar la Información Adicional?",
                "Alerta...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"SI", "NO"}, "NO");

        if (n == JOptionPane.YES_OPTION) {
            guardar();
        }
    }//GEN-LAST:event_jbGuardarActionPerformed

    private void guardar() {

        List<com.develcom.documento.DatoAdicional> lsDatosAdicionales = new ArrayList<>();
        com.develcom.documento.DatoAdicional datoAdicionalValor;
//        boolean exito;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        int contador = 1;
//        int idCategoria = expediente.getIdCategoria();
//        int idLibreria = expediente.getIdLibreria();

        JDateChooser jDateChooser;
        JComboBox jComboBox;
        JTextField jTextField;
        JTextArea jTextArea;

        try {

            for (com.develcom.administracion.DatoAdicional da : lsIndDatosAdicionales) {
                datoAdicionalValor = new com.develcom.documento.DatoAdicional();

                if (da.getTipo().equalsIgnoreCase("texto")) {

                    jTextField = (JTextField) da.getValor();
                    String data = jTextField.getText();

                    traza.trace("dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo() + " valor " + data, Level.INFO);

                    if (!data.equalsIgnoreCase("")) {
                        datoAdicionalValor.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                        datoAdicionalValor.setTipo(da.getTipo());
                        datoAdicionalValor.setValor(data);
                        datoAdicionalValor.setIdTipoDocumento(da.getIdTipoDocumento());
                        datoAdicionalValor.setIdDatoAdicional(da.getIdDatoAdicional());
                        datoAdicionalValor.setVersion(version);
                        datoAdicionalValor.setIdValor(da.getIdValor());
                    } else {
                        String arg = da.getIndiceDatoAdicional().replace("_", " ");
                        arg = arg.toLowerCase();
                        char[] cs = arg.toCharArray();
                        char ch = cs[0];
                        cs[0] = Character.toUpperCase(ch);
                        arg = String.valueOf(cs);

                        throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                    }

                } else if (da.getTipo().equalsIgnoreCase("combo")) {

                    jComboBox = (JComboBox) da.getValor();
                    String data = jComboBox.getSelectedItem().toString();

                    traza.trace("dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo() + " valor " + data, Level.INFO);

                    if (!data.equalsIgnoreCase("")) {
                        datoAdicionalValor.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                        datoAdicionalValor.setTipo(da.getTipo());
                        datoAdicionalValor.setValor(data);
                        datoAdicionalValor.setIdTipoDocumento(da.getIdTipoDocumento());
                        datoAdicionalValor.setIdDatoAdicional(da.getIdDatoAdicional());
                        datoAdicionalValor.setVersion(version);
                        datoAdicionalValor.setIdValor(da.getIdValor());
                    } else {
                        String arg = da.getIndiceDatoAdicional().replace("_", " ");
                        arg = arg.toLowerCase();
                        char[] cs = arg.toCharArray();
                        char ch = cs[0];
                        cs[0] = Character.toUpperCase(ch);
                        arg = String.valueOf(cs);

                        throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                    }

                } else if (da.getTipo().equalsIgnoreCase("fecha")) {

                    try {

                        jDateChooser = (JDateChooser) da.getValor();
                        Calendar calendar = jDateChooser.getCalendar();

                        if (calendar != null) {

                            traza.trace("dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo() + " valor " + sdf.format(calendar.getTime()), Level.INFO);

                            datoAdicionalValor.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                            datoAdicionalValor.setTipo(da.getTipo());
                            datoAdicionalValor.setValor(sdf.format(calendar.getTime()));
                            datoAdicionalValor.setIdTipoDocumento(da.getIdTipoDocumento());
                            datoAdicionalValor.setIdDatoAdicional(da.getIdDatoAdicional());
                            datoAdicionalValor.setVersion(version);
                            datoAdicionalValor.setIdValor(da.getIdValor());
                        } else {
                            String arg = da.getIndiceDatoAdicional().replace("_", " ");
                            arg = arg.toLowerCase();
                            char[] cs = arg.toCharArray();
                            char ch = cs[0];
                            cs[0] = Character.toUpperCase(ch);
                            arg = String.valueOf(cs);

                            throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                        }
                    } catch (NullPointerException e) {
                    }

                } else if (da.getTipo().equalsIgnoreCase("numero")) {

                    jTextField = (JTextField) da.getValor();

                    if (jTextField.getText().matches(UtilidadPalabras.VALIDAR_NUMEROS)) {

                        String data = jTextField.getText();

                        traza.trace("dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo() + " valor " + data, Level.INFO);

                        if (!data.equalsIgnoreCase("")) {
                            datoAdicionalValor.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                            datoAdicionalValor.setTipo(da.getTipo());
                            datoAdicionalValor.setValor(data);
                            datoAdicionalValor.setIdTipoDocumento(da.getIdTipoDocumento());
                            datoAdicionalValor.setIdDatoAdicional(da.getIdDatoAdicional());
                            datoAdicionalValor.setVersion(version);
                            datoAdicionalValor.setIdValor(da.getIdValor());
                        } else {
                            String arg = da.getIndiceDatoAdicional().replace("_", " ");
                            arg = arg.toLowerCase();
                            char[] cs = arg.toCharArray();
                            char ch = cs[0];
                            cs[0] = Character.toUpperCase(ch);
                            arg = String.valueOf(cs);

                            throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                        }

                    } else {
                        String arg = da.getIndiceDatoAdicional().replace("_", " ");
                        arg = arg.toLowerCase();
                        char[] cs = arg.toCharArray();
                        char ch = cs[0];
                        cs[0] = Character.toUpperCase(ch);
                        arg = String.valueOf(cs);

                        throw new DW4JDesktopExcepcion("el " + arg + " debe ser solo numeros");
                    }

                } else if (da.getTipo().equalsIgnoreCase("area")) {

                    jTextArea = (JTextArea) da.getValor();

                    String data = jTextArea.getText();

                    traza.trace("dato adicional " + da.getIndiceDatoAdicional() + " tipo " + da.getTipo() + " valor " + data, Level.INFO);

                    if (!data.equalsIgnoreCase("")) {
                        datoAdicionalValor.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                        datoAdicionalValor.setTipo(da.getTipo());
                        datoAdicionalValor.setValor(data);
                        datoAdicionalValor.setIdTipoDocumento(da.getIdTipoDocumento());
                        datoAdicionalValor.setIdDatoAdicional(da.getIdDatoAdicional());
                        datoAdicionalValor.setVersion(version);
                        datoAdicionalValor.setIdValor(da.getIdValor());
                    } else {
                        String arg = da.getIndiceDatoAdicional().replace("_", " ");
                        arg = arg.toLowerCase();
                        char[] cs = arg.toCharArray();
                        char ch = cs[0];
                        cs[0] = Character.toUpperCase(ch);
                        arg = String.valueOf(cs);

                        throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                    }
                }
                lsDatosAdicionales.add(datoAdicionalValor);
                traza.trace("tamaño de la lista de datos adicionales " + lsDatosAdicionales.size(), Level.INFO);
            }
            digitalizaDocumento.setLsDatosAdicionales(lsDatosAdicionales);
            dispose();

        } catch (DW4JDesktopExcepcion ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            traza.trace(ex.getMessage(), Level.ERROR, ex);
        }
    }

    public void setLsIndDatosAdicionales(List<com.develcom.administracion.DatoAdicional> lsIndDatosAdicionales) {
        if (!lsIndDatosAdicionales.isEmpty()) {
            this.lsIndDatosAdicionales = lsIndDatosAdicionales;
        } else {
            JOptionPane.showMessageDialog(this, "El tipo de documento no posee datos adicionales", "Advertencia", JOptionPane.WARNING_MESSAGE);
            dispose();
        }
    }

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed

        if (digitalizaDocumento != null) {
            digitalizaDocumento.setLsDatosAdicionales(new ArrayList<com.develcom.documento.DatoAdicional>());
        }
        dispose();
    }//GEN-LAST:event_jbCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JLabel jlExpediente;
    private javax.swing.JLabel jlMensajeCategoria;
    private javax.swing.JLabel jlMensajeLibreria;
    private javax.swing.JLabel jlMensajeSubCategoria;
    private javax.swing.JLabel jlMensajeTipoDocumento;
    private javax.swing.JPanel panelIndices;
    // End of variables declaration//GEN-END:variables

}
