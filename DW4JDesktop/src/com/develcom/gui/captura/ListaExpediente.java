/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.captura;

import com.develcom.dao.DatosTabla;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.OtrosDatos;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.expediente.ListaIndice;
import com.develcom.gui.Principal;
import com.develcom.gui.consulta.ResultadoExpediente;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.tools.trazas.Traza;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;

/**
 *
 * @author familia
 */
public class ListaExpediente extends javax.swing.JInternalFrame {

    private Traza traza = new Traza(ListaExpediente.class);
    private ModeloTabla modeloTabla;
    private List<ListaIndice> listaIndices;
    private List<DatosTabla> datosTablas = new ArrayList<>();
    private DigitalizaDocumento digitalizaDocumento;
    /**
     * Contiene informacion del expediente
     */
    private Expediente expediente;

    public ListaExpediente(List<ListaIndice> listaIndices, DigitalizaDocumento digitalizaDocumento) {

        this.digitalizaDocumento = digitalizaDocumento;
        this.listaIndices = listaIndices;
        initComponents();
        iniciar();
    }

    private void iniciar() {
        expediente = ManejoSesion.getExpediente();
        llenarDatosTabla();
        llenarTabla();
        CentraVentanas.centrar(this, Principal.desktop);
        setVisible(true);
    }

    /**
     * Crea las etiquetas para las cabezera de la tabla
     *
     * @param arg Argumento con el nombre de la etiqueta
     * @return La etiqueta creada
     */
    private String crearEtiqueta(String arg) {

        arg = arg.replace("_", " ");
        arg = arg.toLowerCase();
        char[] cs = arg.toCharArray();
        char ch = cs[0];
        cs[0] = Character.toUpperCase(ch);
        arg = String.valueOf(cs);

        return arg;
    }

    /**
     * Crea el objeto con los indices primarios y segundos, del resultado de la
     * consulta dinamica
     */
    private void llenarDatosTabla() {

        traza.trace("armando el data table", Level.INFO);
        DatosTabla dt = null;
        DatosTabla dtCabezeras = new DatosTabla();
        boolean sec1 = true, sec2 = true, sec3 = true, ban = true;
        int cont = 0, registro = 1;
        List<Indice> listIndice;

        for (ListaIndice cd : listaIndices) {
            listIndice = cd.getIndices();

            for (Indice ind : listIndice) {

                if (ban) {
                    dt = new DatosTabla();
                    sec1 = true;
                    sec2 = true;
                    sec3 = true;
                }

                if (ind.getClave().equalsIgnoreCase("y")) {

                    //if (ind.getIndice().regionMatches(true, 0, "fecha", 0, 4)) {
                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                        Calendar c = fechaIngreso.toGregorianCalendar();
                        dtCabezeras.setClavePrimaria(crearEtiqueta(ind.getIndice()));
                        dt.setClavePrimaria(crearEtiqueta(ind.getIndice()));
                        dt.setDatoPrimario(ind.getValor().toString());

                    } else {

                        dtCabezeras.setClavePrimaria(crearEtiqueta(ind.getIndice()));
                        dt.setClavePrimaria(crearEtiqueta(ind.getIndice()));
                        dt.setDatoPrimario(ind.getValor().toString());

                    }

                    ban = false;
                    cont++;

                } else if (ind.getClave().equalsIgnoreCase("s")) {

                    if (sec1) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {

                                dtCabezeras.setClaveSec1(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec1(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec1(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec1(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec1(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec1("");
                            }
                        } else {

                            dtCabezeras.setClaveSec1(crearEtiqueta(ind.getIndice()));
                            dt.setClaveSec1(crearEtiqueta(ind.getIndice()));
                            dt.setDatoSec1(ind.getValor().toString());
                        }

                        sec1 = false;
                        cont++;

                    } else if (sec2) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {

                                dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec2(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec2("");
                            }
                        } else {

                            dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                            dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                            dt.setDatoSec2(ind.getValor().toString());
                        }

                        sec2 = false;
                        cont++;

                    } else if (sec3) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {

                                dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec3(ind.getValor().toString());
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec3("");
                            }
                        } else {

                            dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                            dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                            dt.setDatoSec3(ind.getValor().toString());
                        }

                        sec3 = false;
                        cont++;

                    }
                }

                if (cont == 4) {
                    dt.setRegistro(registro);
                    datosTablas.add(dt);
                    registro++;
                    ban = true;
                    cont = 0;
                }
            }
        }

        for (DatosTabla dt1 : datosTablas) {

            traza.trace("clave primaria " + dt1.getClavePrimaria() + " dato " + dt1.getDatoPrimario(), Level.INFO);
            traza.trace("clave Sec1 " + dt1.getClaveSec1() + " dato " + dt1.getDatoSec1(), Level.INFO);
            traza.trace("clave Sec2 " + dt1.getClaveSec2() + " dato " + dt1.getDatoSec2(), Level.INFO);
            traza.trace("clave Sec3 " + dt1.getClaveSec3() + " dato " + dt1.getDatoSec3(), Level.INFO);
        }

        crearCabezeras(dtCabezeras);
    }

    /**
     * Crea la cabezera de la tabla resultado consulta dinamica
     *
     * @param dt Las etiquetas con los nombres de las cabezeras
     */
    private void crearCabezeras(DatosTabla dt) {
        int cont = 0;
        modeloTabla = new ModeloTabla();
        modeloTabla.addColumn("Registro");

        if (dt.getOtrosDatos() == null) {
            modeloTabla.addColumn(dt.getClavePrimaria());
            modeloTabla.addColumn(dt.getClaveSec1());
            modeloTabla.addColumn(dt.getClaveSec2());
            modeloTabla.addColumn(dt.getClaveSec3());
            //modeloTabla.addColumn("SubCategoria");
        } else {
            try {
                if (!dt.getClavePrimaria().equalsIgnoreCase("") || dt.getClavePrimaria() != null) {
                    modeloTabla.addColumn(dt.getClavePrimaria());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec1().equalsIgnoreCase("") || dt.getClaveSec1() != null) {
                    modeloTabla.addColumn(dt.getClaveSec1());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec2().equalsIgnoreCase("") || dt.getClaveSec2() != null) {
                    modeloTabla.addColumn(dt.getClaveSec2());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            try {
                if (!dt.getClaveSec3().equalsIgnoreCase("") || dt.getClaveSec3() != null) {
                    modeloTabla.addColumn(dt.getClaveSec3());
                    cont++;
                }
            } catch (NullPointerException e) {
            }

            if (cont != 4) {
                for (OtrosDatos od : dt.getOtrosDatos()) {
                    modeloTabla.addColumn(od.getNombre());
                    cont++;
                    if (cont == 4) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Crea la tabla con los datos de la consulta dinamica
     */
    private void llenarTabla() {

        Vector filas;

        for (DatosTabla dt : datosTablas) {
            int cont = 0;
            filas = new Vector();
            filas.add(dt.getRegistro());

            if (dt.getOtrosDatos() == null) {
                filas.add(dt.getDatoPrimario());
                filas.add(dt.getDatoSec1());
                filas.add(dt.getDatoSec2());
                filas.add(dt.getDatoSec3());
                //filas.add(dt.getSubCategoria());
                modeloTabla.addRow(filas);
            } else {
                try {
                    if (!dt.getDatoPrimario().equalsIgnoreCase("") || dt.getDatoPrimario() != null) {
                        filas.add(dt.getDatoPrimario());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec1().equalsIgnoreCase("") || dt.getDatoSec1() != null) {
                        filas.add(dt.getDatoSec1());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec2().equalsIgnoreCase("") || dt.getDatoSec2() != null) {
                        filas.add(dt.getDatoSec2());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                try {
                    if (!dt.getDatoSec3().equalsIgnoreCase("") || dt.getDatoSec3() != null) {
                        filas.add(dt.getDatoSec3());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }

                if (cont != 4) {
                    for (OtrosDatos od : dt.getOtrosDatos()) {

                        filas.add(od.getValor());
                        cont++;
                        if (cont == 4) {
                            break;
                        }

                    }
                }
                modeloTabla.addRow(filas);
            }
        }

        jtLstadoExpediente.setModel(modeloTabla);
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
        jtLstadoExpediente = new javax.swing.JTable();
        jButtonRegresar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Seleccione el expediente que desea crear");

        jtLstadoExpediente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtLstadoExpediente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtLstadoExpedienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtLstadoExpediente);

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar.setMnemonic('c');
        jButtonRegresar.setText("Cancelar");
        jButtonRegresar.setToolTipText("Cancelar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1176, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRegresar)
                .addGap(460, 460, 460))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRegresar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtLstadoExpedienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtLstadoExpedienteMouseClicked

        crearExpediente(evt);
    }//GEN-LAST:event_jtLstadoExpedienteMouseClicked

    private void crearExpediente(java.awt.event.MouseEvent evt) {

        String idExpediente, solicitud = "";
        int registro = 0;
        List<Indice> listIndice;
        boolean flag = true;

        if (evt != null) {
            registro = (int) jtLstadoExpediente.getValueAt(jtLstadoExpediente.getSelectedRow(), 0);
            solicitud = (String) jtLstadoExpediente.getValueAt(jtLstadoExpediente.getSelectedRow(), 1);
            idExpediente = (String) jtLstadoExpediente.getValueAt(jtLstadoExpediente.getSelectedRow(), 2);
        }

        for (int i = 0; i < listaIndices.size(); i++) {
            if (i == registro - 1) {
                listIndice = listaIndices.get(i).getIndices();
                expediente.setIndices(listIndice);
                ManejoSesion.setExpediente(expediente);
                break;
            }
        }

        for (ListaIndice cd : listaIndices) {
            listIndice = cd.getIndices();
            if (flag) {
                for (Indice indice : listIndice) {
                    try {
                        if (indice.getValor().toString().equalsIgnoreCase(solicitud)) {
                            expediente.setIndices(listIndice);
                            ManejoSesion.setExpediente(expediente);
                            flag = false;
                            break;
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }
        }

        dispose();

        CreaExpediente jdlCreaExpediente = new CreaExpediente(digitalizaDocumento);
        Principal.desktop.add(jdlCreaExpediente);
        jdlCreaExpediente.toFront();

    }


    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed

        dispose();

    }//GEN-LAST:event_jButtonRegresarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtLstadoExpediente;
    // End of variables declaration//GEN-END:variables
}
