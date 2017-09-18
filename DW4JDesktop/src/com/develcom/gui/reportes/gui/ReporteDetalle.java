/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReporteDetalle.java
 *
 * Created on 12-sep-2012, 13:15:28
 */
package com.develcom.gui.reportes.gui;

import com.develcom.autentica.Usuario;
import com.develcom.dao.Mensajes;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.Principal;
import com.develcom.gui.reportes.tools.ProcesaReporte;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.trazas.Traza;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.sesion.IniciaSesion;

/**
 *
 * @author develcom
 */
public class ReporteDetalle extends javax.swing.JInternalFrame {
    
    private static final long serialVersionUID = -967298811973764677L;

    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(ReporteDetalle.class);
    private List<Usuario> autocompletarusuarios;

    
    public ReporteDetalle(String titulo) {
        try {
            autocompletarusuarios = new IniciaSesion().autocomplete();
            if (!autocompletarusuarios.isEmpty()) {
                initComponents();
                setTitle(titulo);
                llenarListaUsuarios();
                CentraVentanas.centrar(this, Principal.desktop);
                setVisible(true);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap", Level.INFO, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soap fault ", Level.ERROR, ex);
        } catch (ConnectException ex) {
            traza.trace("error de coneccion", Level.ERROR, ex);
        }
    }

    private void llenarListaUsuarios() {
        DefaultListModel model = new DefaultListModel();

        for (Usuario user : autocompletarusuarios) {
            if (!user.getIdUsuario().equalsIgnoreCase("dw4jconf")) {
                model.addElement(user.getNombre().trim() + " " + user.getApellido().trim());
            }
        }
        usuarios.setModel(model);
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
        fechaDesde = new com.toedter.calendar.JDateChooser();
        fechaHasta = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usuarios = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jbtCancelar = new javax.swing.JButton();
        jbtAceptar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));

        jLabel1.setText("Fecha desde");

        jLabel2.setText("Fecha hasta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechaHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(fechaDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(fechaHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(224, 239, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione uno o varios indexadores"));

        usuarios.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(usuarios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(224, 239, 255));

        jbtCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jbtCancelar.setMnemonic('c');
        jbtCancelar.setText("Cerrar");
        jbtCancelar.setToolTipText("Cancelar");
        jbtCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCancelarActionPerformed(evt);
            }
        });

        jbtAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Valid Green.png"))); // NOI18N
        jbtAceptar.setMnemonic('a');
        jbtAceptar.setText("Aceptar");
        jbtAceptar.setToolTipText("Aceptar");
        jbtAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtAceptar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtAceptar)
                    .addComponent(jbtCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAceptarActionPerformed
        //aceptar();
        try {
            List<String>  usuariosSelecionados = usuarios.getSelectedValuesList();
//            Object[] usuariosSelecionados = usuarios.getSelectedValues();
            List<String> selectUser = new ArrayList<String>();
            Calendar fechaDesd = fechaDesde.getCalendar();
            Calendar fechaHast = fechaHasta.getCalendar();

//            if (usuariosSelecionados.length > 0) {
            if (!usuariosSelecionados.isEmpty()) {
                if (fechaDesd != null) {
                    if (fechaHast != null) {
                        if (fechaHast.after(fechaDesd)) {

                            for (Usuario user : autocompletarusuarios) {
                                String usuario = user.getNombre().trim() + " " + user.getApellido().trim();

                                for (String obj : usuariosSelecionados) {
                                    if (usuario.equals(obj)) {
                                        selectUser.add(user.getIdUsuario());
                                    }
                                }
                            }

                            reportes(selectUser, fechaDesd, fechaHast);

                        } else {
                            throw new DW4JDesktopExcepcion("La fecha desde debe ser menor que la fecha hasta");
                        }
                    } else {
                        throw new DW4JDesktopExcepcion("Debe seleccionar la fecha hasta");
                    }
                } else {
                    throw new DW4JDesktopExcepcion("Debe seleccionar la fecha desde");
                }
            } else {
                throw new DW4JDesktopExcepcion("Debe seleccionar por lo menos un indexador");
            }

        } catch (DW4JDesktopExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);
        }
}//GEN-LAST:event_jbtAceptarActionPerformed

    private void reportes(final List<String> usuariosSelecionados, final Calendar fechaDesd, final Calendar fechaHast) {
        final String titulo = getTitle();
        final MostrarProceso proceso = new MostrarProceso("<html>Generando el reporte<br/>" + titulo + "</html>");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {

                aceptar(titulo, usuariosSelecionados, fechaDesd, fechaHast);
                proceso.detener();
            }
        }).start();
    }

    private void aceptar(String tituloReporte, List<String> usuariosSelecionados, Calendar fechaDesd, Calendar fechaHast) {

        ProcesaReporte pr = new ProcesaReporte();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        HashMap param = new HashMap();
        String users = "";
        int sel, cont = 0;

        try {

            Mensajes.setMensaje("");

            sel = usuariosSelecionados.size();
            traza.trace("tamaño de la lista de usuarios seleccionado " + sel, Level.INFO);
            
            for (String obj : usuariosSelecionados) {
                
                if (cont == (sel - 1)) {
                    users = users + "'" + obj+ "'";
                } else {
                    users = users + "'" + obj + "',";
                }
                traza.trace("contador " + cont, Level.INFO);
                cont++;
            }

            param.put("usuarios", users);
            param.put("fechaDesde", fechaDesd.getTime());
            param.put("fechaHasta", fechaHast.getTime());

            traza.trace("usuarios seleccionados " + users, Level.INFO);
            traza.trace("fecha desde " + sdf.format(fechaDesd.getTime()), Level.INFO);
            traza.trace("fecha hasta " + sdf.format(fechaHast.getTime()), Level.INFO);

            pr.crearReporte("detalleIndexador.jrxml", param, tituloReporte);

        } catch (Exception e) {
            traza.trace("error ene proceso del reporte", Level.ERROR, e);
        }

    }

    private void jbtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCancelarActionPerformed
        //traza.trace("cancelando", Level.INFO);
        //this.removeAll();
        dispose();
}//GEN-LAST:event_jbtCancelarActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fechaDesde;
    private com.toedter.calendar.JDateChooser fechaHasta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtAceptar;
    private javax.swing.JButton jbtCancelar;
    private javax.swing.JList usuarios;
    // End of variables declaration//GEN-END:variables
}
