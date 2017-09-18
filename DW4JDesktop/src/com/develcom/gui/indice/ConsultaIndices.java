package com.develcom.gui.indice;

import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.Mensajes;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.expediente.SubCategoria;
import com.develcom.expediente.TipoDocumento;
import com.develcom.gui.Libreria;
import com.develcom.gui.Principal;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Level;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;

/**
 * Consulta y busca expedientes
 *
 * @author develcom
 */
public class ConsultaIndices extends javax.swing.JInternalFrame {

    /**
     * Lista de los indices dinamicos
     */
    private List<Indice> indices = new ArrayList<Indice>();
    /**
     * Datos del expediente
     */
    private Expediente expediente;
    /**
     * escribe traza en el log
     */
    private Traza traza = new Traza(ConsultaIndices.class);

    /**
     * Constructor inicializa el atributo expediente y agrega los indices de la
     * categoria seleccionada
     */
    public ConsultaIndices() {
        setTitle(Mensajes.getMensaje());
        this.expediente = ManejoSesion.getExpediente();
        initComponents();

        objetos();
        jlMensaje1.setText("Libreria: " + expediente.getLibreria());
        jlMensaje2.setText("Categoria: " + expediente.getCategoria());
        CentraVentanas.centrar(this, Principal.desktop);
        this.setVisible(true);
    }

    private void objetos() {
        final MostrarProceso proceso = new MostrarProceso("Generando el formulario");
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                crearObjetos();
                proceso.detener();
            }
        }).start();
    }

    /**
     * Crea y coloca los indices dinamicos en el frame
     */
    private void crearObjetos() {
        traza.trace("Creando objetos", Level.INFO);
        GridBagConstraints constraints = new GridBagConstraints();
        CreaObjetosDinamicos uv = new CreaObjetosDinamicos(this);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        panelIndices.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelIndices.add(uv.crearObjetos(expediente));
        panelIndices.updateUI();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aceptar = new javax.swing.JButton();
        jButtonRegresar = new javax.swing.JButton();
        jlMensaje1 = new javax.swing.JLabel();
        jlMensaje2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Consulta de Expedientes");

        aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Valid Green.png"))); // NOI18N
        aceptar.setMnemonic('a');
        aceptar.setText("Consultar");
        aceptar.setToolTipText("Consultar");
        aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarActionPerformed(evt);
            }
        });

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar.setMnemonic('c');
        jButtonRegresar.setText("Cancelar");
        jButtonRegresar.setToolTipText("Cancelar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jlMensaje1.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        jlMensaje1.setText("jLabel1");
        jlMensaje1.setAutoscrolls(true);

        jlMensaje2.setFont(new java.awt.Font("DejaVu Sans", 1, 10)); // NOI18N
        jlMensaje2.setText("jLabel1");
        jlMensaje2.setAutoscrolls(true);

        jScrollPane3.setBackground(new java.awt.Color(224, 239, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 905, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(panelIndices);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(aceptar)
                        .addGap(205, 205, 205)
                        .addComponent(jButtonRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlMensaje1, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlMensaje2, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlMensaje1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensaje2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aceptar)
                    .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Verifica los rangos de las fechas
     *
     * @param lstFechas Lista con datos de fechas a consultar
     * @param listaIndices Lista de indices para la busqueda y comparacion
     * @return Siempre devuelve verdadero, solo devuelva falso cuando los rango
     * de fechas estan errados
     */
    private boolean comprobarFechas(List<HashMap<String, GregorianCalendar>> lstFechas, List<Indice> listaIndices) {

        boolean resp = true;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lstFechas.size(); i++) {

            HashMap<String, GregorianCalendar> fecha = lstFechas.get(i);

            for (Indice ind : listaIndices) {

                String etiDesde = ind.getIndice();
                int desde = etiDesde.indexOf("DESDE");

                if (desde != -1) {

                    String lblDesde = etiDesde.substring(0, desde - 1);
                    GregorianCalendar fechDesde = fecha.get(etiDesde);

                    for (int j = 0; j < lstFechas.size(); j++) {
                        HashMap<String, GregorianCalendar> fech = lstFechas.get(j);

                        for (Indice ind1 : listaIndices) {

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

    /**
     * Regresa al frame anterior
     *
     * @param evt
     */
    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed

        Principal.desktop.add(new Libreria(ManejoSesion.getSesion(), "Seleccionar Libreria - Busqueda"));
        dispose();
}//GEN-LAST:event_jButtonRegresarActionPerformed

    private void aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarActionPerformed
        buscar();
    }//GEN-LAST:event_aceptarActionPerformed

    private void buscar() {
        final MostrarProceso proceso = new MostrarProceso("Buscando Expedente(s) ");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                consultar();
                proceso.detener();
            }
        }).start();
    }

    /**
     * Recoge los datos en una lista tipo objecto (Indice) para enviarlo al
     * servicio y generar la consulta
     */
    private void consultar() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<ConsultaDinamica> consultaDinamicas;

        HashMap<String, GregorianCalendar> fechas;// = new HashMap<String, GregorianCalendar>();
        List<HashMap<String, GregorianCalendar>> lstFechas = new ArrayList<>();

        List<TipoDocumento> listaTipoDoc = new ArrayList<>();
        List<SubCategoria> listaSubCat = new ArrayList<>();
        Indice indice;
        List<Indice> listaIndices = new ArrayList<>();

        JDateChooser jDateChooser;
        JComboBox jComboBox;
        JTextField jTextField;
        JTextArea jTextArea;

        try {

            for (Indice ind : indices) {
                indice = new Indice();
                indice.setUpdateIndices(true);

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

                        fechas = new HashMap<String, GregorianCalendar>();

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

            if (comprobarFechas(lstFechas, listaIndices)) {

                consultaDinamicas = new BuscaExpedienteDinamico().consultarExpedienteDinamico(listaIndices, null, listaSubCat, listaTipoDoc, 1, expediente.getIdLibreria());

                if (!consultaDinamicas.isEmpty()) {
                    if (consultaDinamicas.get(0).isExiste()) {
                        expediente.setSubCategorias(convertir(listaSubCat));
                        ManejoSesion.setExpediente(expediente);

                        ManejoSesion.setSizeSearch(consultaDinamicas.size());

                        traza.trace("exito al en la consulta dinamica ", Level.INFO);

                        this.dispose();
                        ResultadoConsulta rc = new ResultadoConsulta(consultaDinamicas);
                        Principal.desktop.add(rc);
                    } else {
                        //throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
                        throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
                    }
                } else {
                    //throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
                    throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
                }

            } else {
                throw new DW4JDesktopExcepcion("Problemas con los rango de fechas");
            }

        } catch (SOAPException e) {
            traza.trace("error en la busqueda dinamica ", Level.INFO, e);
        } catch (DW4JDesktopExcepcion e) {
            traza.trace("error en la busqueda dinamica ", Level.INFO, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptar;
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel jlMensaje1;
    private javax.swing.JLabel jlMensaje2;
    private javax.swing.JPanel panelIndices;
    // End of variables declaration//GEN-END:variables
//metodo que valida el formulario dinamico de la clase consultar expediente

    /**
     * Fija los indices buscados segun la categoria selccionada
     *
     * @param indices Listado de indices
     */
    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }
}
