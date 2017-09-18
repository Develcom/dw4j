/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LibreriaReportes.java
 *
 * Created on 29/02/2012, 01:46:44 PM
 */
package com.develcom.gui.reportes.gui;

import com.develcom.autentica.Perfil;
import com.develcom.autentica.Sesion;
import com.develcom.dao.Campos;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.Mensajes;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.Principal;
import com.develcom.gui.reportes.tools.ConstruyeConsultas;
import com.develcom.gui.reportes.tools.ProcesaReporte;
import com.develcom.gui.reportes.tools.UtilidadVentanaReportes;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;
import ve.com.develcom.sesion.IniciaSesion;

/**
 * Muestra las Librerias y Categorias
 *
 * @author develcom
 */
public class LibreriaReportes extends javax.swing.JInternalFrame {

    private UtilidadVentanaReportes uvr = new UtilidadVentanaReportes();
    private int reporte;
    //private MostrarProceso proceso;// = new MostrarProceso("<html>Generando el reporte<br/>"+titulo+"</html>");
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(LibreriaReportes.class);
    private List<Perfil> perfiles;

    
    public LibreriaReportes(List<Sesion> sesion, String titulo, int reporte) {

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
//        constraints.gridwidth = 1;
//        constraints.gridheight = 1;
//        constraints.fill = GridBagConstraints.BOTH;

        this.reporte = reporte;
        this.setResizable(false);
        initComponents();
        cboCategoria.setEnabled(false);
        jbtAceptar.setEnabled(false);
        panelGenerico.setLayout(new FlowLayout(FlowLayout.LEFT));

        if (reporte == ConstruyeConsultas.DOCUMENTO_A_VENCERSE) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el periodo"));
            panelGenerico.add(uvr.docVencerse());
            
        } else if (reporte == ConstruyeConsultas.CANTIDAD_DOCUMENTO_INDEXADOS_POR_INDEXADOR) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el Mes y el Año"));
            panelGenerico.add(uvr.mesAnio());
            uvr.getCmboAnioDesde().setEnabled(false);
            uvr.getCmboMes().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.DOCUMENTO_VENCIDOS) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));
            panelGenerico.add(uvr.rangosFechas());
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.DOCUMENTOS_RECHAZADOS) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));
            panelGenerico.add(uvr.rangosFechas());
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.DOCUMENTO_PENDIENTE_POR_APROBAR) {
            
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.CAUSA_DE_RECHAZO) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));
            panelGenerico.add(uvr.rangosFechas());
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.DOCUMENTO_INDEXADOS_RECHAZADO_APROBADO_PENDIENTE) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el Mes y el Año"));
            panelGenerico.add(uvr.mesAnio());
            uvr.getCmboAnioDesde().setEnabled(false);
            uvr.getCmboMes().setEnabled(false);
            
        } else if (reporte == ConstruyeConsultas.CRECIMIENTO_INTERMENSUAL_DE_DOCUMENTOS) {
            
            jpLibreria.setVisible(false);
            jpCategoria.setVisible(false);
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione rangos de Mes y Año"));
            panelGenerico.add(uvr.rangosMesAnio());
            jbtAceptar.setEnabled(true);

        } else if (reporte == ConstruyeConsultas.TIPO_PERSONAL) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el tipo de personal y un rango de fechas"));
            uvr.setTipoPersonal(true);
            panelGenerico.add(uvr.rangosFechas());
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            uvr.getCmboTipoPersonal().setEnabled(false);

        } else if (reporte == ConstruyeConsultas.EXPEDIENTE_ESTATUS) {

            jpLibreria.setVisible(false);
            jpCategoria.setVisible(false);
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));
            panelGenerico.add(uvr.rangosFechas());
            jbtAceptar.setEnabled(true);

        } else if (reporte == ConstruyeConsultas.DOCUMENTO_ELIMINADO) {
            
            panelGenerico.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione un rango de fechas"));
            uvr.setUsuario(true);
            panelGenerico.add(uvr.rangosFechas());
            uvr.getFechaDesde().setEnabled(false);
            uvr.getFechaHasta().setEnabled(false);
            uvr.getCmboUsuarios().setEnabled(false);
            
        }

        llenarcboLibreria();
        CentraVentanas.centrar(this, Principal.desktop);
        setTitle(titulo);
        this.setVisible(true);

    }

    /**
     * Llena la lista desplegables con las Librerias
     */
    private void llenarcboLibreria() {
        String lib = "";
        try {
            traza.trace("llenando lista de libreria", Level.INFO);
            DefaultComboBoxModel modelo = new DefaultComboBoxModel();
            //mapLibreria = new HashMap();
            modelo.addElement("");

            traza.trace("buscando las libreria del usuario " + ManejoSesion.getLogin() + " con el perfil " + Constantes.ROL, Level.INFO);
            perfiles = new IniciaSesion().buscarLibCatPerfil(ManejoSesion.getLogin(), Constantes.ROL);

            traza.trace("tamaño perfiles " + perfiles.size(), Level.INFO);

            for (Perfil perfil : perfiles) {
                String des = perfil.getLibreria().getDescripcion();
                //traza.trace("libreria "+des, Level.INFO);
                if (!lib.equalsIgnoreCase(des)) {
                    lib = perfil.getLibreria().getDescripcion();
                    if (perfil.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        modelo.addElement(perfil.getLibreria().getDescripcion());
                    }
                }
            }

//            for (Sesion sess : sesion) {
//                String des = sess.getLibreria().getDescripcion();
//                if(!lib.equalsIgnoreCase(des)){
//                    lib=sess.getLibreria().getDescripcion();
//                    if(sess.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)){
//                        modelo.addElement(sess.getLibreria().getDescripcion());
//                    }
//                }
//            }
            cboLibreria.setModel(modelo);
        } catch (Exception e) {
            traza.trace("error al llenar lista de libreria", Level.INFO, e);
            JOptionPane.showMessageDialog(this, "Error al llenar lista de libreria\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jpLibreria = new javax.swing.JPanel();
        cboLibreria = new javax.swing.JComboBox();
        jlbLibreria = new javax.swing.JLabel();
        jpCategoria = new javax.swing.JPanel();
        cboCategoria = new javax.swing.JComboBox();
        jlbTipoDocumento = new javax.swing.JLabel();
        panelGenerico = new javax.swing.JPanel();
        jbtAceptar = new javax.swing.JButton();
        jbtCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Seleccionar Libreria y Categoria");

        jpLibreria.setBackground(new java.awt.Color(224, 239, 255));

        cboLibreria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLibreriaActionPerformed(evt);
            }
        });

        jlbLibreria.setText("Libreria");

        javax.swing.GroupLayout jpLibreriaLayout = new javax.swing.GroupLayout(jpLibreria);
        jpLibreria.setLayout(jpLibreriaLayout);
        jpLibreriaLayout.setHorizontalGroup(
            jpLibreriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLibreriaLayout.createSequentialGroup()
                .addComponent(jlbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
            .addComponent(cboLibreria, 0, 388, Short.MAX_VALUE)
        );
        jpLibreriaLayout.setVerticalGroup(
            jpLibreriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpLibreriaLayout.createSequentialGroup()
                .addComponent(jlbLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpCategoria.setBackground(new java.awt.Color(224, 239, 255));
        jpCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jlbTipoDocumento.setText("Categoria");

        javax.swing.GroupLayout jpCategoriaLayout = new javax.swing.GroupLayout(jpCategoria);
        jpCategoria.setLayout(jpCategoriaLayout);
        jpCategoriaLayout.setHorizontalGroup(
            jpCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCategoriaLayout.createSequentialGroup()
                .addComponent(jlbTipoDocumento)
                .addContainerGap(341, Short.MAX_VALUE))
            .addComponent(cboCategoria, 0, 388, Short.MAX_VALUE)
        );
        jpCategoriaLayout.setVerticalGroup(
            jpCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCategoriaLayout.createSequentialGroup()
                .addComponent(jlbTipoDocumento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGenerico.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelGenericoLayout = new javax.swing.GroupLayout(panelGenerico);
        panelGenerico.setLayout(panelGenericoLayout);
        panelGenericoLayout.setHorizontalGroup(
            panelGenericoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        panelGenericoLayout.setVerticalGroup(
            panelGenericoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        jbtAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Valid Green.png"))); // NOI18N
        jbtAceptar.setMnemonic('a');
        jbtAceptar.setText("Aceptar");
        jbtAceptar.setToolTipText("Aceptar");
        jbtAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtAceptarActionPerformed(evt);
            }
        });

        jbtCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jbtCancelar.setMnemonic('c');
        jbtCancelar.setText("Cerrar");
        jbtCancelar.setToolTipText("Cancelar");
        jbtCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGenerico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpLibreria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtAceptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addComponent(jbtCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpLibreria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGenerico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cierra esta ventana para ir a las subcategorias con la informacion de la
     * libreria y la categoria
     */
    private void jbtAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtAceptarActionPerformed
        String titulo = getTitle().substring(getTitle().indexOf("-") + 1);
        aceptar(titulo);
        //reportes();
}//GEN-LAST:event_jbtAceptarActionPerformed


    private void reportesPlantilla(final String plantilla, final HashMap map, final String titulo) {
        final ProcesaReporte pr = new ProcesaReporte();
        //final String titulo = getTitle().substring(getTitle().indexOf("-")+1);
        final MostrarProceso proceso = new MostrarProceso("<html>Generando el reporte<br/>" + titulo + "</html>");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                pr.crearReporte(plantilla, map, titulo);
                proceso.detener();
            }
        }).start();
    }

    private void reportesDinamico(final String titulo, final String query, final String lib, final String cat, final List<Campos> campos, final String fechas) {
        final ProcesaReporte pr = new ProcesaReporte();
        //final String titulo = getTitle().substring(getTitle().indexOf("-")+1);
        final MostrarProceso proceso = new MostrarProceso("<html>Generando el reporte<br/>" + titulo + "</html>");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {

                pr.crearReporte(titulo, query, lib, cat, campos, fechas);
                proceso.detener();
            }
        }).start();
    }

    private void aceptar(final String tituloReporte) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Expediente expediente = new Expediente();
        //ProcesaReporte pr = new ProcesaReporte();
        ConstruyeConsultas consultas = new ConstruyeConsultas();
        String lib = null, cat = null, query = null, fechas, strFechaHasta;
        int idLib = 0, idCat = 0;


        try {
            traza.trace("armando informacion del reporte", Level.INFO);

            if (cboLibreria.getSelectedItem() != null && cboCategoria.getSelectedItem() != null) {
                lib = cboLibreria.getSelectedItem().toString();
                cat = cboCategoria.getSelectedItem().toString();
            }
            //cboLibreria.setEnabled(false);
            //cboCategoria.setEnabled(false);

            Mensajes.setMensaje(lib + " - " + cat);

            traza.trace("libreria seleccionada " + lib, Level.INFO);
            traza.trace("categoria seleccionada " + cat, Level.INFO);

            for (Perfil perfil : perfiles) {

                if (perfil.getLibreria().getDescripcion().equalsIgnoreCase(lib)) {
                    idLib = perfil.getLibreria().getIdLibreria();
                    expediente.setIdLibreria(idLib);
                    expediente.setLibreria(lib);
                }

                if (perfil.getCategoria().getCategoria().equalsIgnoreCase(cat)) {
                    idCat = perfil.getCategoria().getIdCategoria();
                    expediente.setIdCategoria(idCat);
                    expediente.setCategoria(cat);
                }
            }

            if (reporte == ConstruyeConsultas.EXPEDIENTE_ESTATUS) {

                HashMap map = new HashMap();
                traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                Calendar fechaHasta = uvr.getFechaHasta().getCalendar();


                if ((fechaDesde != null) && (fechaHasta != null)) {
                    if (fechaHasta.after(fechaDesde)) {
                        
                        fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                        
                        map.put("fechaDesde", fechaDesde.getTime());
                        map.put("fechaHasta", fechaHasta.getTime());

                        traza.trace("rango de fechas " + fechas, Level.INFO);
                        reportesPlantilla("reporteEstatusExpediente.jrxml", map, tituloReporte);

                    } else {
                        throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fecha desde");
                    }
                } else {
                    throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                }

            } else if (reporte == ConstruyeConsultas.CRECIMIENTO_INTERMENSUAL_DE_DOCUMENTOS) {

                traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                String mesDesde, mesHasta, anioDesde, anioHasta;
                int mDesde, mHasta, aDesde, aHasta;
                HashMap mesAnio = new HashMap();

                mesDesde = uvr.getCmboMesDesde().getSelectedItem().toString();
                mesHasta = uvr.getCmboMesHasta().getSelectedItem().toString();
                anioDesde = uvr.getCmboAnioDesde().getSelectedItem().toString();
                anioHasta = uvr.getCmboAnioHasta().getSelectedItem().toString();

                if ((mesDesde != null) && (mesHasta != null) && ((!mesDesde.equalsIgnoreCase("")) && (!mesHasta.equalsIgnoreCase("")))
                        && (anioDesde != null) && (anioHasta != null) && (!anioDesde.equalsIgnoreCase("") && !anioHasta.equalsIgnoreCase(""))) {

                    mDesde = Integer.parseInt(uvr.getMapMeses().get(mesDesde));
                    mHasta = Integer.parseInt(uvr.getMapMeses().get(mesHasta));
                    aDesde = Integer.parseInt(uvr.getCmboAnioDesde().getSelectedItem().toString());
                    aHasta = Integer.parseInt(uvr.getCmboAnioHasta().getSelectedItem().toString());

                    if (mDesde == 12 && mHasta >= 1) {
                        if (aHasta == (aDesde + 1)) {

                            traza.trace("mes desde " + mesDesde, Level.INFO);
                            traza.trace("mes hasta " + mesHasta, Level.INFO);
                            traza.trace("año desde " + anioDesde, Level.INFO);
                            traza.trace("año hasta " + anioHasta, Level.INFO);

                            mesAnio.put("mesAnioDesde", anioDesde + uvr.getMapMeses().get(mesDesde));
                            mesAnio.put("mesAnioHasta", anioHasta + uvr.getMapMeses().get(mesHasta));
                            mesAnio.put("mesAnioDesdeTitulo", uvr.getMapMeses().get(mesDesde) + " / " + anioDesde);
                            mesAnio.put("mesAnioHastaTitulo", uvr.getMapMeses().get(mesHasta) + " / " + anioHasta);


                            reportesPlantilla("crecimientoIntermensual.jrxml", mesAnio, tituloReporte);

                        }
                    }


                    if (mDesde <= mHasta) {
                        if (aDesde <= aHasta) {

                            traza.trace("mes desde " + mesDesde, Level.INFO);
                            traza.trace("mes hasta " + mesHasta, Level.INFO);
                            traza.trace("año desde " + anioDesde, Level.INFO);
                            traza.trace("año hasta " + anioHasta, Level.INFO);

                            mesAnio.put("mesAnioDesde", anioDesde + uvr.getMapMeses().get(mesDesde));
                            mesAnio.put("mesAnioHasta", anioHasta + uvr.getMapMeses().get(mesHasta));
                            mesAnio.put("mesAnioDesdeTitulo", uvr.getMapMeses().get(mesDesde) + " / " + anioDesde);
                            mesAnio.put("mesAnioHastaTitulo", uvr.getMapMeses().get(mesHasta) + " / " + anioHasta);


                            reportesPlantilla("crecimientoIntermensual.jrxml", mesAnio, tituloReporte);

                        } else {
                            throw new DW4JDesktopExcepcion("El año desde debe ser menor que el año hasta");
                        }
                    } else {
                        throw new DW4JDesktopExcepcion("El mes desde debe ser menor que el mes hasta");
                    }
                } else {
                    throw new DW4JDesktopExcepcion("Debe selecionar el Mes y el Año");
                }

            } else if ((lib != null) && (!lib.equalsIgnoreCase(""))) {
                if ((cat != null) && (!cat.equalsIgnoreCase(""))) {

                    traza.trace("seleccion la libreria " + lib + " id " + idLib, Level.INFO);
                    traza.trace("seleccion la categoria " + cat + " id " + idCat, Level.INFO);

                    ManejoSesion.setExpediente(expediente);


                    if (reporte == ConstruyeConsultas.DOCUMENTO_A_VENCERSE) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        if (uvr.getJrb30dias().isSelected()) {

                            traza.trace("seleciono 30 dias", Level.INFO);
                            uvr.setDias("30");
                            query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTO_A_VENCERSE, uvr, expediente);


                        } else if (uvr.getJrb60dias().isSelected()) {

                            traza.trace("seleciono 60 dias", Level.INFO);
                            uvr.setDias("60");
                            query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTO_A_VENCERSE, uvr, expediente);

                        } else if (uvr.getJrb90dias().isSelected()) {

                            traza.trace("seleciono 90 dias", Level.INFO);
                            uvr.setDias("90");
                            query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTO_A_VENCERSE, uvr, expediente);

                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar un periodo");
                        }

                        if (query != null) {
                            reportesDinamico("Documentos a Vencerse", query, lib, cat, consultas.getListaCampos(), "Período de " + uvr.getDias() + " días");
                            //pr.crearReporte("Documentos a Vencerse", query, lib, cat, consultas.getListaCampos(), "Período de "+uvr.getDias()+" días");
                        }

                    } else if (reporte == ConstruyeConsultas.CANTIDAD_DOCUMENTO_INDEXADOS_POR_INDEXADOR) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        String mes, anio;
                        HashMap mesAnio = new HashMap();

                        mes = uvr.getCmboMes().getSelectedItem().toString();
                        anio = uvr.getCmboAnioDesde().getSelectedItem().toString();

                        if ((mes != null) && (anio != null) && ((!mes.equalsIgnoreCase("")) && (!anio.equalsIgnoreCase("")))) {

                            mesAnio.put("idLib", expediente.getIdLibreria());
                            mesAnio.put("idCat", expediente.getIdCategoria());
                            mesAnio.put("mesAnio", anio + uvr.getMapMeses().get(mes));
                            mesAnio.put("mesAnioTitulo", uvr.getMapMeses().get(mes) + " / " + anio);

                            traza.trace("mes " + mes, Level.INFO);
                            traza.trace("año " + anio, Level.INFO);

                            reportesPlantilla("CantDocIndexados.jrxml", mesAnio, tituloReporte);

                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar el Mes y el Año");
                        }

                    } else if (reporte == ConstruyeConsultas.DOCUMENTO_VENCIDOS) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                        Calendar fechaHasta = uvr.getFechaHasta().getCalendar();
                        fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                        //strFechaHasta = "Fecha hasta: "+sdf.format(fechaHasta.getTime());

                        if ((fechaDesde != null) && (fechaHasta != null)) {
                            if (fechaHasta.after(fechaDesde)) {
                                traza.trace("rango de fechas " + fechas, Level.INFO);
                                //traza.trace("fecha hasta "+strFechaHasta, Level.INFO);
                                query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTO_VENCIDOS, uvr, expediente);

                                reportesDinamico("Documentos Vencidos", query, lib, cat, consultas.getListaCampos(), fechas);
                                //pr.crearReporte("Documentos Vencidos", query, lib, cat, consultas.getListaCampos(), fechas);
                            } else {
                                throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fehca desde");
                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                        }

                    } else if (reporte == ConstruyeConsultas.DOCUMENTOS_RECHAZADOS) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                        Calendar fechaHasta = uvr.getFechaHasta().getCalendar();
                        fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                        //strFechaHasta = "Fecha hasta: "+sdf.format(fechaHasta.getTime());

                        if ((fechaDesde != null) && (fechaHasta != null)) {
                            if (fechaHasta.after(fechaDesde)) {
                                traza.trace("rango de fechas " + fechas, Level.INFO);
                                //traza.trace("fecha hasta "+strFechaHasta, Level.INFO);
                                query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTOS_RECHAZADOS, uvr, expediente);

                                reportesDinamico("Documentos Rechazados", query, lib, cat, consultas.getListaCampos(), fechas);
                                //pr.crearReporte("Documentos Rechazados", query, lib, cat, consultas.getListaCampos(), fechas);
                            } else {
                                throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fecha desde");
                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                        }

                    } else if (reporte == ConstruyeConsultas.DOCUMENTO_PENDIENTE_POR_APROBAR) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        query = consultas.crearQuery(ConstruyeConsultas.DOCUMENTO_PENDIENTE_POR_APROBAR, uvr, expediente);

                        reportesDinamico("Documentos Pendientes por Aprobar", query, lib, cat, consultas.getListaCampos(), "");
                        //pr.crearReporte("Documentos Pendientes por Aprobar", query, lib, cat, consultas.getListaCampos(), "");

                    } else if (reporte == ConstruyeConsultas.CAUSA_DE_RECHAZO) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        HashMap param = new HashMap();

                        Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                        Calendar fechaHasta = uvr.getFechaHasta().getCalendar();
                        fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                        //strFechaHasta = "Fecha hasta: "+sdf.format(fechaHasta.getTime());

                        if ((fechaDesde != null) && (fechaHasta != null)) {
                            if (fechaHasta.after(fechaDesde)) {

                                traza.trace("rango de fechas " + fechas, Level.INFO);
                                //traza.trace("fecha hasta "+strFechaHasta, Level.INFO);

                                param.put("idLib", expediente.getIdLibreria());
                                param.put("idCat", expediente.getIdCategoria());
                                param.put("fechaDesde", fechaDesde.getTime());
                                param.put("fechaHasta", fechaHasta.getTime());

                                reportesPlantilla("causaRechazo.jrxml", param, tituloReporte);
                                
                            } else {
                                throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fehca desde");
                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                        }

                    } else if (reporte == ConstruyeConsultas.DOCUMENTO_INDEXADOS_RECHAZADO_APROBADO_PENDIENTE) {

                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        String mes, anio;
                        HashMap mesAnio = new HashMap();

                        mes = uvr.getCmboMes().getSelectedItem().toString();
                        anio = uvr.getCmboAnioDesde().getSelectedItem().toString();

                        if ((mes != null) && (anio != null) && ((!mes.equalsIgnoreCase("")) && (!anio.equalsIgnoreCase("")))) {

                            mesAnio.put("idLib", expediente.getIdLibreria());
                            mesAnio.put("idCat", expediente.getIdCategoria());
                            mesAnio.put("mesAnio", anio + uvr.getMapMeses().get(mes));
                            mesAnio.put("mesAnioTitulo", uvr.getMapMeses().get(mes) + " / " + anio);

                            traza.trace("mes " + mes, Level.INFO);
                            traza.trace("año " + anio, Level.INFO);

                            reportesPlantilla("docIndexRechAprobPend.jrxml", mesAnio, tituloReporte);

                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar el Mes y el Año");
                        }
                    } else if (reporte == ConstruyeConsultas.TIPO_PERSONAL) {

                        HashMap map = new HashMap();
                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        String tipoPersonal = uvr.getCmboTipoPersonal().getSelectedItem().toString();
                        Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                        Calendar fechaHasta = uvr.getFechaHasta().getCalendar();
                        
                        //strFechaHasta = "Fecha hasta: "+sdf.format(fechaHasta.getTime());

                        if ((fechaDesde != null) && (fechaHasta != null)) {
                            if (fechaHasta.after(fechaDesde)) {
                                if (!tipoPersonal.equalsIgnoreCase("")) {
                                    
                                    fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                                    map.put("idLib", expediente.getIdLibreria());
                                    map.put("idCat", expediente.getIdCategoria());
                                    map.put("tipoPersonal", tipoPersonal);
                                    map.put("fechaDesde", fechaDesde.getTime());
                                    map.put("fechaHasta", fechaHasta.getTime());

                                    traza.trace("rango de fechas " + fechas, Level.INFO);
                                    reportesPlantilla("tipoPersonal.jrxml", map, tituloReporte);

                                } else {
                                    throw new DW4JDesktopExcepcion("Debe selecionar el tipo de personal");
                                }
                            } else {
                                throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fecha desde");
                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                        }
                    }else if (reporte == ConstruyeConsultas.DOCUMENTO_ELIMINADO) {

                        HashMap map = new HashMap();
                        traza.trace("generando el reporte " + tituloReporte, Level.INFO);

                        String usuario = uvr.getCmboUsuarios().getSelectedItem().toString();
                        Calendar fechaDesde = uvr.getFechaDesde().getCalendar();
                        Calendar fechaHasta = uvr.getFechaHasta().getCalendar();
                        
                        //strFechaHasta = "Fecha hasta: "+sdf.format(fechaHasta.getTime());

                        if ((fechaDesde != null) && (fechaHasta != null)) {
                            if (fechaHasta.after(fechaDesde)) {
                                if (!usuario.equalsIgnoreCase("")) {
                                    
                                    fechas = "Fecha desde: " + sdf.format(fechaDesde.getTime()) + " - Fecha hasta: " + sdf.format(fechaHasta.getTime());
                                    map.put("idLib", expediente.getIdLibreria());
                                    map.put("idCat", expediente.getIdCategoria());
                                    map.put("usuario", usuario);
                                    map.put("fechaDesde", fechaDesde.getTime());
                                    map.put("fechaHasta", fechaHasta.getTime());

                                    traza.trace("rango de fechas " + fechas, Level.INFO);
                                    reportesPlantilla("documentosEliminados.jrxml", map, tituloReporte);

                                } else {
                                    throw new DW4JDesktopExcepcion("Debe selecionar el tipo de personal");
                                }
                            } else {
                                throw new DW4JDesktopExcepcion("La fecha hasta debe ser menor que la fecha desde");
                            }
                        } else {
                            throw new DW4JDesktopExcepcion("Debe selecionar una fecha desde y una fecha hasta");
                        }
                        
                    }
                } else {
                    throw new DW4JDesktopExcepcion("Debe selecionar la categoria");
                }
            } else {
                throw new DW4JDesktopExcepcion("Debe selecionar la libreria");
            }
            //this.dispose();

        } catch (DW4JDesktopExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Alerta", JOptionPane.WARNING_MESSAGE);

            //return;
        } catch (Exception e) {
            traza.trace("error", Level.ERROR, e);
        }
    }

    /**
     * Cierra esta ventana
     *
     * @param evt
     */
    private void jbtCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCancelarActionPerformed
        //traza.trace("cancelando", Level.INFO);
        //this.removeAll();
        dispose();
}//GEN-LAST:event_jbtCancelarActionPerformed

    /**
     * Llena la lista desplegable categorias
     *
     * @param evt
     */
    private void cboLibreriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLibreriaActionPerformed
        if (!cboLibreria.getSelectedItem().equals("")) {
            llenarcboCategoria();
            jbtAceptar.setEnabled(true);
        } else {
            cboCategoria.setEnabled(false);
            jbtAceptar.setEnabled(false);
        }
    }//GEN-LAST:event_cboLibreriaActionPerformed

    /**
     * Llena la lista desplegable con las Categorias
     */
    private void llenarcboCategoria() {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        String lib, cat = "";

        try {
            traza.trace("llenando lista de categorias", Level.INFO);
            lib = cboLibreria.getSelectedItem().toString();

            for (Perfil catego : perfiles) {
                if (catego.getLibreria().getDescripcion().equalsIgnoreCase(lib)) {
                    String categ = catego.getCategoria().getCategoria();
                    //traza.trace("categoria "+categ, Level.INFO);
                    if (!cat.equalsIgnoreCase(categ)) {
                        cat = catego.getCategoria().getCategoria();
                        if (catego.getCategoria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                            modelo.addElement(categ);
                            //traza.trace("agregando categoria a la lista desplegable: "+categ, Level.INFO);
                        }
                    }
                }
            }

            cboCategoria.setModel(modelo);

            cboCategoria.setEnabled(true);
            uvr.getCmboAnioDesde().setEnabled(true);
            uvr.getCmboMes().setEnabled(true);
            uvr.getFechaDesde().setEnabled(true);
            uvr.getFechaHasta().setEnabled(true);
            uvr.getCmboTipoPersonal().setEnabled(true);
            uvr.getCmboUsuarios().setEnabled(true);


        } catch (Exception e) {
            traza.trace("error al llenar lista de categoria", Level.INFO, e);
            JOptionPane.showMessageDialog(this, "Error al llenar lista de categoria\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboCategoria;
    private javax.swing.JComboBox cboLibreria;
    private javax.swing.JButton jbtAceptar;
    private javax.swing.JButton jbtCancelar;
    private javax.swing.JLabel jlbLibreria;
    private javax.swing.JLabel jlbTipoDocumento;
    private javax.swing.JPanel jpCategoria;
    private javax.swing.JPanel jpLibreria;
    private javax.swing.JPanel panelGenerico;
    // End of variables declaration//GEN-END:variables
}
