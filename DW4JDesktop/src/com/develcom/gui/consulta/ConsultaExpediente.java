package com.develcom.gui.consulta;

import com.develcom.expediente.SubCategoria;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.Mensajes;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
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
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;

/**
 * Consulta y busca expedientes
 *
 * @author develcom
 */
public class ConsultaExpediente extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 3375082133093418163L;
    /**
     * Lista de los indices dinamicos
     */
    private List<Indice> indices = new ArrayList<>();
    /**
     * Datos del expediente
     */
    private Expediente expe;
    /**
     * Lista de subcategorias
     */
    private List<com.develcom.administracion.SubCategoria> listaSubCategorias;
    /**
     * Lista de tipos de documentos
     */
    private List<TipoDocumento> listaTipoDocumentos;
    /**
     * Lista de los argumentos
     */
    private List<com.develcom.expediente.Indices> argum;
    /**
     * escribe traza en el log
     */
    private Traza traza = new Traza(ConsultaExpediente.class);

    public ConsultaExpediente() {
        setTitle(Mensajes.getMensaje());
        this.expe = ManejoSesion.getExpediente();
        initComponents();
        buscarDatos();
        jlMensaje1.setText("Libreria: " + expe.getLibreria());
        jlMensaje2.setText("Categoria: " + expe.getCategoria());
        CentraVentanas.centrar(this, Principal.desktop);
    }

    private void buscarDatos() {

        final MostrarProceso proceso = new MostrarProceso("Armando el formulario");
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                llenarListaSubCategoria();
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
        panelIndices.add(uv.crearObjetos(expe));
        this.setVisible(true);
    }

    /**
     * Busca y llena la lista con las subCategoria
     *
     * @param idCategoria El id de la categoria
     */
    private void llenarListaSubCategoria() {

        DefaultListModel lm = new DefaultListModel();

        try {
            traza.trace("llenado listado de subCategorias", Level.INFO);

            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria(null, expe.getIdCategoria(), 0);

            for (com.develcom.administracion.SubCategoria sc : listaSubCategorias) {
                
                if (sc.getEstatus().equalsIgnoreCase("activo")) {
                    lm.addElement(sc.getSubCategoria().trim());
                }
                
            }
            jlSubCategoria.setModel(lm);
            jlTipoDocumento.setEnabled(false);

        } catch (SOAPException | SOAPFaultException e) {
            traza.trace("error al llenar el listado de subCategorias", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error al llenar el listado de subCategorias\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonAceptar1 = new javax.swing.JButton();
        jButtonRegresar1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlSubCategoria = new javax.swing.JList();
        jlSelecionar = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlTipoDocumento = new javax.swing.JList();
        jlMensaje1 = new javax.swing.JLabel();
        jlMensaje2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Consulta de Expedientes");

        jButtonAceptar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Valid Green.png"))); // NOI18N
        jButtonAceptar1.setMnemonic('a');
        jButtonAceptar1.setText("Aceptar");
        jButtonAceptar1.setToolTipText("Aceptar");
        jButtonAceptar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAceptar1ActionPerformed(evt);
            }
        });

        jButtonRegresar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar1.setMnemonic('c');
        jButtonRegresar1.setText("Cancelar");
        jButtonRegresar1.setToolTipText("Cancelar");
        jButtonRegresar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresar1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));

        jlSubCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlSubCategoriaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jlSubCategoria);

        jlSelecionar.setText("SubCategoria");

        jLabel1.setText("TipoDocumento");

        jlTipoDocumento.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jlTipoDocumento);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlSelecionar)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlSelecionar)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

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
                        .addContainerGap()
                        .addComponent(jlMensaje1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlMensaje2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(jButtonAceptar1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRegresar1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlMensaje1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlMensaje2, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegresar1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAceptar1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Obtiene los datos necesario para realizar la consulta dinamica
     */
    private void jButtonAceptar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAceptar1ActionPerformed
        consultar();
}//GEN-LAST:event_jButtonAceptar1ActionPerformed

    /**
     * Obtiene los datos necesario para realizar la consulta dinamica
     */
    private void consultar() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<ConsultaDinamica> consultaDinamicas;

        HashMap<String, GregorianCalendar> fechas;// = new HashMap<String, GregorianCalendar>();
        List<HashMap<String, GregorianCalendar>> lstFechas = new ArrayList<>();

        Object[] subCateSel, tipoDocSel;

        List<TipoDocumento> listaTipoDoc = new ArrayList<>();
        List<SubCategoria> listaSubCat = new ArrayList<>();
        Indice indice;
        List<Indice> listaIndices = new ArrayList<>();

        JDateChooser jDateChooser;
        JComboBox jComboBox;
        JTextField jTextField;
        JTextArea jTextArea;

        int size, contNull = 0, contDato;
        boolean vacio;

        try {

            for (Indice ind : indices) {
                indice = new Indice();
                indice.setUpdateIndices(false);

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

                        fechas = new HashMap<>();

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

            if (!jlSubCategoria.isSelectionEmpty()) {
                subCateSel = jlSubCategoria.getSelectedValues();

                for (com.develcom.administracion.SubCategoria sc : listaSubCategorias) {
                    for (Object obj : subCateSel) {

                        if (sc.getSubCategoria().equalsIgnoreCase(obj.toString())) {
                            SubCategoria sc1 = new SubCategoria();
                            sc1.setEstatus(sc.getEstatus());
                            sc1.setIdCategoria(sc.getIdCategoria());
                            sc1.setIdSubCategoria(sc.getIdSubCategoria());
                            sc1.setSubCategoria(sc.getSubCategoria());
                            listaSubCat.add(sc1);
                        }
                    }
                }
            }

            if (!jlTipoDocumento.isSelectionEmpty()) {
                tipoDocSel = jlTipoDocumento.getSelectedValues();

                for (TipoDocumento td : listaTipoDocumentos) {
                    for (Object obj : tipoDocSel) {
                        String tipoDoc = td.getTipoDocumento().trim();
                        String tipoDocObj = obj.toString().trim();
                        if (tipoDoc.equalsIgnoreCase(tipoDocObj)) {
                            listaTipoDoc.add(td);
                        }
                    }
                }
            }

            size = listaIndices.size();

            for (Indice ind : listaIndices) {
                if ((ind.getValor() == null) || (ind.getValor().equals(""))) {
                    contNull++;
                }
            }

            vacio = contNull != size;
//            if(contNull == size){
//                vacio=false;
//            }else{
//                vacio=true;
//            }

            if (vacio) {
                if (comprobarFechas(lstFechas, listaIndices)) {

                    consultaDinamicas = new BuscaExpedienteDinamico().consultarExpedienteDinamico(listaIndices, null, listaSubCat, listaTipoDoc, 1, expe.getIdLibreria());

//                    if (!consultaDinamicas.isEmpty()) {
                    if (consultaDinamicas.get(0).isExiste()) {
                        expe.setSubCategorias(convertir(listaSubCat));
                        expe.setTipoDocumentos(listaTipoDoc);
                        expe.setCategorias(new AdministracionBusqueda().buscarCategoria(null, 0, expe.getIdCategoria()));
                        ManejoSesion.setExpediente(expe);

                        ManejoSesion.setSizeSearch(consultaDinamicas.size());

                        traza.trace("exito al en la consulta dinamica ", Level.INFO);

                        this.dispose();

                        ResultadoExpediente re = new ResultadoExpediente(consultaDinamicas, false);

                        Principal.desktop.add(re);
                    } else {
                        throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
                        //throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
                    }
//                    } else {
//                        //throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
//                        throw new DW4JDesktopExcepcion("No se encontraron expedientes asociados a la búsqueda ingresada");
//                    }

                } else {
                    throw new DW4JDesktopExcepcion("Problemas con los rango de fechas");
                }
            } else {
                throw new DW4JDesktopExcepcion("Debe indicar algún Índice de Búsqueda");
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

//    private String crearEtiqueta(String arg){
//
//        arg = arg.toString().replace("_", " ");
//        arg = arg.toLowerCase();
//        char[] cs = arg.toCharArray();
//        char ch = cs[0];
//        cs[0] = Character.toUpperCase(ch);
//        arg = String.valueOf(cs);
//
//
//        return arg;
//    }
    /**
     * Regresa al frame anterior
     *
     * @param evt
     */
    private void jButtonRegresar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresar1ActionPerformed

        Principal.desktop.add(new Libreria(ManejoSesion.getSesion(), "Seleccionar Libreria - Busqueda"));
        dispose();
}//GEN-LAST:event_jButtonRegresar1ActionPerformed

    private void jlSubCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlSubCategoriaMouseClicked
        LlenarTipoDocumentos();
    }//GEN-LAST:event_jlSubCategoriaMouseClicked

    /**
     * Llena el listado con los tipos de documentos segun la subCategoria
     * seleccionada
     */
    private void LlenarTipoDocumentos() {

        DefaultListModel lm = new DefaultListModel();
        Object[] subCatSel;
//        int i = 0;
        List<Integer> idSubCate = new ArrayList<>();

        try {
            if (jlSubCategoria.getSelectedValue() != null) {
                subCatSel = jlSubCategoria.getSelectedValues();

                for (com.develcom.administracion.SubCategoria sc : listaSubCategorias) {
                    for (Object obj : subCatSel) {

                        if (sc.getSubCategoria().equalsIgnoreCase(obj.toString())) {
                            idSubCate.add(sc.getIdSubCategoria());
//                            i++;
                        }
                    }
                }

                listaTipoDocumentos = new BuscaExpedienteDinamico().buscarTiposDocumentos(idSubCate);
                for (com.develcom.expediente.TipoDocumento td : listaTipoDocumentos) {
                    lm.addElement(td.getTipoDocumento().trim());
                }
                jlTipoDocumento.setModel(lm);
                jlTipoDocumento.setEnabled(true);
            }
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar1;
    private javax.swing.JButton jButtonRegresar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel jlMensaje1;
    private javax.swing.JLabel jlMensaje2;
    private javax.swing.JLabel jlSelecionar;
    private javax.swing.JList jlSubCategoria;
    private javax.swing.JList jlTipoDocumento;
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
