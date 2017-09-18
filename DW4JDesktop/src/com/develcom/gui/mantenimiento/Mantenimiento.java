/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * Mantenimiento.java
 *
 * Created on 20-ago-2012, 13:03:10
 */
package com.develcom.gui.mantenimiento;

import biz.source_code.base64Coder.Base64Coder;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.mantenimiento.Configuracion;
import com.develcom.tools.trazas.Traza;
import javax.swing.JOptionPane;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.mantenimiento.MantenimientoVarios;

/**
 *
 * @author develcom
 */
public class Mantenimiento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -3497755663704183360L;

    private Traza traza = new Traza(Mantenimiento.class);
//    private Configuracion configuracion;

    /**
     * Creates new form Mantenimiento
     */
    public Mantenimiento() {
        initComponents();
        iniciar();
    }

    private void iniciar() {

        try {

            Configuracion configuracion = new MantenimientoVarios().buscandoMantenimiento();

            txtServidor.setText(configuracion.getServerName());
            txtBaseDatos.setText(configuracion.getDatabaseName());
            txtPuerto.setText(String.valueOf(configuracion.getPort()));
            txtUsuario.setText(Base64Coder.decodeString(configuracion.getUser()));
            txtContrasenia.setText(Base64Coder.decodeString(configuracion.getPassword()));
            chkCalidad.setSelected(configuracion.isCalidadActivo());
            chkFoliatura.setSelected(configuracion.isFoliatura());
            chkFicha.setSelected(configuracion.isFicha());
            jchkFabrica.setSelected(configuracion.isFabrica());
            chkEliminar.setSelected(configuracion.isElimina());
            chkLDPA.setSelected(configuracion.isLdap());
            
            CentraVentanas.centrar(this, Principal.desktop);
            setVisible(true);
            
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtServidor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtBaseDatos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtPuerto = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtContrasenia = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        chkCalidad = new javax.swing.JCheckBox();
        chkFoliatura = new javax.swing.JCheckBox();
        chkFicha = new javax.swing.JCheckBox();
        jchkFabrica = new javax.swing.JCheckBox();
        chkEliminar = new javax.swing.JCheckBox();
        chkLDPA = new javax.swing.JCheckBox();
        jbCancelar = new javax.swing.JButton();
        jbAceptar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Mantenimiento del DW4J");
        setToolTipText("");

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuracion de Base de Datos"));

        jLabel1.setText("Direción IP");

        jLabel2.setText("Nombre");

        jLabel3.setText("Puerto");

        jLabel4.setText("Usuario");

        jLabel5.setText("Contraseña");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBaseDatos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(txtPuerto, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtServidor))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBaseDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(224, 239, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        chkCalidad.setBackground(new java.awt.Color(224, 239, 255));
        chkCalidad.setText("Activar o Desactivar Calidad");

        chkFoliatura.setBackground(new java.awt.Color(224, 239, 255));
        chkFoliatura.setText("Activar o Desactivar Foliatura");

        chkFicha.setBackground(new java.awt.Color(224, 239, 255));
        chkFicha.setText("Activar o Descativar Ficha");

        jchkFabrica.setBackground(new java.awt.Color(224, 239, 255));
        jchkFabrica.setText("Activar o Desactivar Separación de Cola");

        chkEliminar.setBackground(new java.awt.Color(224, 239, 255));
        chkEliminar.setText("Activar o Desactivar Eliminación de Documentos");

        chkLDPA.setBackground(new java.awt.Color(224, 239, 255));
        chkLDPA.setText("Activar o Desactivar verificar LDPA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkCalidad)
                    .addComponent(chkFoliatura)
                    .addComponent(chkFicha)
                    .addComponent(jchkFabrica)
                    .addComponent(chkEliminar)
                    .addComponent(chkLDPA))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkCalidad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkFoliatura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkFicha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchkFabrica)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkLDPA)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jbCancelar.setText("Cerrar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });

        jbAceptar.setText("Aceptar");
        jbAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbCancelar))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAceptar)
                    .addComponent(jbCancelar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAceptarActionPerformed
        aceptar();
    }//GEN-LAST:event_jbAceptarActionPerformed

    private void aceptar() {

        Configuracion config = new Configuracion();
        String servidor;
        String baseDatos;
        String usuario;
        String contrasenia;
        int puerto;
        char[] pass;
        boolean resp, calidad, foliatura, ficha, fabrica, eliminar, ldpa;

        try {

            servidor = txtServidor.getText();
            baseDatos = txtBaseDatos.getText();
            usuario = txtUsuario.getText();
            pass = txtContrasenia.getPassword();
            contrasenia = String.valueOf(pass);
            puerto = Integer.parseInt(txtPuerto.getText());

            calidad = chkCalidad.isSelected();
            foliatura = chkFoliatura.isSelected();
            ficha = chkFicha.isSelected();
            fabrica = jchkFabrica.isSelected();
            eliminar = chkEliminar.isSelected();
            ldpa = chkLDPA.isSelected();

            if (!servidor.equalsIgnoreCase("")) {
                if (!baseDatos.equalsIgnoreCase("")) {
                    if (!usuario.equalsIgnoreCase("")) {
                        if (!contrasenia.equalsIgnoreCase("")) {
                            if (puerto != 0) {

                                config.setServerName(servidor);
                                config.setDatabaseName(baseDatos);
                                config.setPort(puerto);
                                config.setUser(usuario);
                                config.setPassword(contrasenia);
                                config.setCalidadActivo(calidad);
                                config.setFoliatura(foliatura);
                                config.setFicha(ficha);
                                config.setFabrica(fabrica);
                                config.setElimina(eliminar);
                                config.setLdap(ldpa);

                                resp = new MantenimientoVarios().mantenimientoBaseDatos(config);

                                traza.trace("respuesta al realizar el mantenimiento " + resp, Level.INFO);

                                if (resp) {
                                    JOptionPane.showMessageDialog(this, "Exito en el mantenimiento", "Información", JOptionPane.INFORMATION_MESSAGE);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Problemas en el mantenimiento", "Adverencia", JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "Debe colocar un numero de puerto", "Adverencia", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Debe colocar una contraseña", "Adverencia", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Debe colocar un usuario", "Adverencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe colocar un nombre de base de datos", "Adverencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe colocar un nombre de servidor o direccion ip", "Adverencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mantener la base de datos", "Error", JOptionPane.WARNING_MESSAGE);
            traza.trace("Error en el mantgenimiento", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error en el mantgenimiento\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_jbCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkCalidad;
    private javax.swing.JCheckBox chkEliminar;
    private javax.swing.JCheckBox chkFicha;
    private javax.swing.JCheckBox chkFoliatura;
    private javax.swing.JCheckBox chkLDPA;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbAceptar;
    private javax.swing.JButton jbCancelar;
    private javax.swing.JCheckBox jchkFabrica;
    private javax.swing.JTextField txtBaseDatos;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtPuerto;
    private javax.swing.JTextField txtServidor;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

}
