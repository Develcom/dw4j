package com.develcom.gui.indice;

import com.develcom.dao.DatosTabla;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.documento.InfoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Expedientes;
import com.develcom.expediente.Indice;
import com.develcom.expediente.SubCategoria;
import com.develcom.gui.Principal;
import com.develcom.gui.calidad.BuscaExpediente;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.UtilidadPalabras;
import com.develcom.tools.trazas.Traza;
import com.toedter.calendar.JDateChooser;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.ActualizaIndice;

public class ResultadoConsulta extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 4545733785864884133L;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(ResultadoConsulta.class);
    /**
     * Resultado de las claves primarias y segunda de la consulta dinamica
     */
    private List<DatosTabla> datosTablas = new ArrayList<DatosTabla>();
    /**
     * Modelo para la tabla
     */
    private ModeloTabla modeloTabla;
    /**
     * Datos del expediente
     */
    private Expediente expediente;
    /**
     * Listado de tipos de docuemtos digitalizados
     */
    private List<InfoDocumento> documentosDigitalizado = new ArrayList<InfoDocumento>();
    /**
     * Listado de id de subCategorias
     */
    private List<String> idSubCatBuscadas = new ArrayList<String>();
    /**
     * Listado del resultado de la consulta dinamica
     */
    private static List<ConsultaDinamica> consultaDinamicas;
    private static List<com.develcom.calidad.ConsultaDinamica> consultaDinamicasAprobar;
    /**
     * Lista de los indices dinamicos
     */
    private List<Indice> indices = new ArrayList<Indice>();
    private static Object DatoNuevoCampoPrincipal = null;
    private static Object DatoNuevoCampoSec1 = null;
    private static Object DatoNuevoCampoSec2 = null;
    private static Object DatoNuevoCampoSec3 = null;
    private Object DatoViejoCampoPrincipal = null;
    private int filaSeleccionada;
    private DatosTabla dtCabezeras = new DatosTabla();

    /**
     * Constructor que se usa cuando se aprueba o se rechaza un documento
     */
    public ResultadoConsulta() {

        if (DatoNuevoCampoPrincipal != null) {
            DatoNuevoCampoPrincipal = null;
        }
        if (DatoNuevoCampoSec1 != null) {
            DatoNuevoCampoSec1 = null;
        }
        if (DatoNuevoCampoSec2 != null) {
            DatoNuevoCampoSec2 = null;
        }
        if (DatoNuevoCampoSec3 != null) {
            DatoNuevoCampoSec3 = null;
        }

        initComponents();
        CentraVentanas.centrar(this, Principal.desktop);

        llenarDatosTabla();
        llenarTabla();
        expediente = ManejoSesion.getExpediente();
        this.setVisible(true);
    }

    /**
     * Constructor
     *
     * @param consultaDinamicas Resultado de la consulta dinamica
     */
    public ResultadoConsulta(List<ConsultaDinamica> consultaDinamicas) {
        ResultadoConsulta.consultaDinamicas = consultaDinamicas;
        this.expediente = ManejoSesion.getExpediente();

        if (DatoNuevoCampoPrincipal != null) {
            DatoNuevoCampoPrincipal = null;
        }
        if (DatoNuevoCampoSec1 != null) {
            DatoNuevoCampoSec1 = null;
        }
        if (DatoNuevoCampoSec2 != null) {
            DatoNuevoCampoSec2 = null;
        }
        if (DatoNuevoCampoSec3 != null) {
            DatoNuevoCampoSec3 = null;
        }

        initComponents();
        CentraVentanas.centrar(this, Principal.desktop);
        llenarDatosTabla();
        llenarTabla();

        this.setVisible(true);
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
     * Permite convertir la clase Indice y ConsultaDinamica del paquete calidad
     * al paquete expediente
     */
    public void convertir() {

        ConsultaDinamica conDinamica;
        Indice indice;
        List<com.develcom.calidad.Indice> indicess;
//        Object idExpediente, tmp = "";

        if (consultaDinamicasAprobar != null) {
            consultaDinamicas = new ArrayList<ConsultaDinamica>();
            for (com.develcom.calidad.ConsultaDinamica cd : consultaDinamicasAprobar) {

                conDinamica = new ConsultaDinamica();
                indicess = cd.getIndices();

//                idExpediente = buscarIdExpediente(indices);
//                if(!idExpediente.equals(tmp)){
//                    tmp=idExpediente;
                for (com.develcom.calidad.Indice ind : indicess) {
                    indice = new Indice();

                    indice.setIndice(ind.getIndice());
                    indice.setClave(ind.getClave());
                    indice.setTipo(ind.getTipo());
                    indice.setValor(ind.getValor());
                    conDinamica.getIndices().add(indice);
                }
                consultaDinamicas.add(conDinamica);
//                }
            }
        }
        if (!consultaDinamicas.isEmpty()) {
            llenarDatosTabla();
            llenarTabla();
        }
    }

    /**
     * Crea el objeto con los indices primarios y segundos, del resultado de la
     * consulta dinamica
     */
    private void llenarDatosTabla() {

        traza.trace("armando el data table", Level.INFO);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosTabla dt = null;
        boolean sec1 = true, sec2 = true, sec3 = true, ban = true, bus = true;
        int cont = 0;
        List<Indice> listaIndice;

        for (ConsultaDinamica cd : consultaDinamicas) {
            listaIndice = cd.getIndices();

            for (Indice ind : listaIndice) {

                if (ban) {
                    dt = new DatosTabla();
                    sec1 = true;
                    sec2 = true;
                    sec3 = true;
                }

                if (ind.getClave().equalsIgnoreCase("y")) {

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
                    //traza.trace(dt.getClavePrimaria()+" - "+dt.getDatoPrimario()+" "+dt.getSubCategoria(), Level.INFO);

                } else if (ind.getClave().equalsIgnoreCase("s")) {

                    if (sec1) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {
//                                XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                Calendar c = fechaIngreso.toGregorianCalendar();

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
                        //traza.trace(dt.getClaveSec1()+" - "+dt.getDatoSec1(), Level.INFO);

                    } else if (sec2) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {
//                                XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                Calendar c = fechaIngreso.toGregorianCalendar();

                                dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec2(ind.getValor().toString());

//                                if(DatoNuevoCampoSec2!=null){
//                                    c = (Calendar) DatoNuevoCampoSec2;
//                                    dt.setDatoSec2(sdf.format(c.getTime()));
//                                }else{
//                                    dt.setDatoSec2(sdf.format(c.getTime()));
//                                }
                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec2("");
                            }
                        } else {

                            dtCabezeras.setClaveSec2(crearEtiqueta(ind.getIndice()));
                            dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                            dt.setDatoSec2(ind.getValor().toString());

//                            if(DatoNuevoCampoSec2!=null){
//                                dt.setDatoSec2(DatoNuevoCampoSec2.toString());
//                            }else{
//                                dt.setDatoSec2(ind.getValor().toString());
//                            }
                        }

                        sec2 = false;
                        cont++;
                        //traza.trace(dt.getClaveSec2()+" - "+dt.getDatoSec2(), Level.INFO);

                    } else if (sec3) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {
//                                XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                Calendar c = fechaIngreso.toGregorianCalendar();

                                dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec3(ind.getValor().toString());

                            } catch (NullPointerException e) {
                                dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                dt.setDatoSec3("");
                            }
                        } else {

                            dtCabezeras.setClaveSec3(ind.getIndice());
                            dt.setClaveSec3(ind.getIndice());
                            dt.setDatoSec3(ind.getValor().toString());

                        }

                        sec3 = false;
                        cont++;
                        //traza.trace(dt.getClaveSec3()+" - "+dt.getDatoSec3(), Level.INFO);
                        //ban=true;

                    }
                }

                //traza.trace("cont "+cont, Level.INFO);
                if (cont == 4) {
                    datosTablas.add(dt);
                    ban = true;
                    bus = true;
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

        modeloTabla = new ModeloTabla();

        modeloTabla.addColumn(dt.getClavePrimaria());
        modeloTabla.addColumn(dt.getClaveSec1());
        modeloTabla.addColumn(dt.getClaveSec2());
        modeloTabla.addColumn(dt.getClaveSec3());
        //modeloTabla.addColumn("SubCategoria");

    }

    /**
     * Crea la tabla con los datos de la consulta dinamica
     */
    private void llenarTabla() {

        Vector filas;
        //int cont=1;

        for (DatosTabla dt : datosTablas) {

            filas = new Vector();

            filas.add(dt.getDatoPrimario());
            filas.add(dt.getDatoSec1());
            filas.add(dt.getDatoSec2());
            filas.add(dt.getDatoSec3());
            //filas.add(dt.getSubCategoria());
            modeloTabla.addRow(filas);
        }

        jtResultadoConsulta.setModel(modeloTabla);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelResultado = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtResultadoConsulta = new javax.swing.JTable();
        jButtonRegresar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();
        jbGuardar = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Resultado Consulta");

        panelResultado.setBackground(new java.awt.Color(224, 239, 255));
        panelResultado.setBorder(javax.swing.BorderFactory.createTitledBorder("Indique el Expediente"));

        jtResultadoConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jtResultadoConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtResultadoConsultaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtResultadoConsulta);

        javax.swing.GroupLayout panelResultadoLayout = new javax.swing.GroupLayout(panelResultado);
        panelResultado.setLayout(panelResultadoLayout);
        panelResultadoLayout.setHorizontalGroup(
            panelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelResultadoLayout.setVerticalGroup(
            panelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar.setMnemonic('c');
        jButtonRegresar.setText("Cancelar");
        jButtonRegresar.setToolTipText("Cancelar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jScrollPane3.setBackground(new java.awt.Color(224, 239, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 858, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(panelIndices);

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/save.png"))); // NOI18N
        jbGuardar.setMnemonic('g');
        jbGuardar.setText("Guardar Indices");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelResultado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jbGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRegresar)
                        .addGap(102, 102, 102))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbGuardar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Regresa al frame anterior
     *
     * @param evt
     */
    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed
        dispose();

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            BuscaExpediente be = new BuscaExpediente();
            Principal.desktop.add(be);
        } else if (Constantes.ACCION.equalsIgnoreCase("ACTUALIZAR_INDICES")) {
            ConsultaIndices ci = new ConsultaIndices();
            Principal.desktop.add(ci);
        }
}//GEN-LAST:event_jButtonRegresarActionPerformed

    /**
     * Muestra el arbol con los tipos de documentos digitalizados y el valor de
     * los indices
     *
     * @param evt
     */
    private void jtResultadoConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtResultadoConsultaMouseClicked

        List<Indice> listaIndice = null;
        boolean ban = true;

        int x = evt.getX();
        int y = evt.getY();
        int row = jtResultadoConsulta.getSelectedRow();
        int column = jtResultadoConsulta.getSelectedColumn();

        filaSeleccionada = jtResultadoConsulta.getSelectedRow();

        String idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
        ManejoSesion.getExpediente().setIdExpediente(idExpediente);

        expediente.setIdExpediente(idExpediente);

        if (DatoNuevoCampoPrincipal != null) {
            expediente.setIdExpediente(DatoNuevoCampoPrincipal.toString());
        }

        indices();

}//GEN-LAST:event_jtResultadoConsultaMouseClicked

    private void indices() {
        final CreaObjetosDinamicos uv = new CreaObjetosDinamicos(this);
        final MostrarProceso proceso = new MostrarProceso("Buscando los datos del expediente " + expediente.getIdExpediente());
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    panelIndices.removeAll();
                    panelIndices.setLayout(new FlowLayout(FlowLayout.CENTER));
                    panelIndices.add(uv.crearObjetos(expediente));
                    panelIndices.updateUI();
                    proceso.detener();
                } catch (Exception e) {
                    traza.trace("error ", Level.ERROR, e);
                }
            }
        }).start();
    }

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed

        int n = JOptionPane.showOptionDialog(this,
                "¿Desea actualizar los Indices \ndel expediente " + expediente.getIdExpediente() + "?",
                "Alerta...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"SI", "NO"}, "NO");

        if (n == JOptionPane.YES_OPTION) {
            guardar();
        }

    }//GEN-LAST:event_jbGuardarActionPerformed

    /**
     * Guarda la informacion del expediente y sus indices
     */
    private void guardar() {
        //SistemaRRHH rh = ManejoSesion.getSistemaRRHH();
        Expedientes expeWS = new Expedientes();
        boolean exito;
        boolean sec1 = true, sec2 = true, sec3 = true;
        Indice indice;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        int idCategoria = expediente.getIdCategoria();
        int idLibreria = expediente.getIdLibreria();

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
                    if ((ind.getClave().equalsIgnoreCase("y")) || (ind.getClave().equalsIgnoreCase("s")) || (ind.getClave().equalsIgnoreCase("o"))) {
                        String data = jTextField.getText();

                        if (!data.equalsIgnoreCase("")) {

                            if (ind.getClave().equalsIgnoreCase("y")) {
                                DatoNuevoCampoPrincipal = data;
                            }

                            if (ind.getClave().equalsIgnoreCase("s")) {
                                if (sec1) {
                                    DatoNuevoCampoSec1 = data;
                                    sec1 = false;
                                } else if (sec2) {
                                    DatoNuevoCampoSec2 = data;
                                    sec2 = false;
                                } else if (sec3) {
                                    DatoNuevoCampoSec3 = data;
                                    sec3 = false;
                                }
                            }

                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(data);
                        } else {
                            String arg = ind.getIndice().replace("_", " ");
                            arg = arg.toLowerCase();
                            char[] cs = arg.toCharArray();
                            char ch = cs[0];
                            cs[0] = Character.toUpperCase(ch);
                            arg = String.valueOf(cs);

                            throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                        }
                    } else {
                        indice.setIdCategoria(ind.getIdCategoria());
                        indice.setIdIndice(ind.getIdIndice());
                        indice.setIndice(ind.getIndice());
                        indice.setClave(ind.getClave());
                        indice.setTipo(ind.getTipo());
                        indice.setValor(jTextField.getText());
                    }

                } else if (ind.getTipo().equalsIgnoreCase("combo")) {

                    jComboBox = (JComboBox) ind.getValor();
                    if ((ind.getClave().equalsIgnoreCase("y")) || (ind.getClave().equalsIgnoreCase("s")) || (ind.getClave().equalsIgnoreCase("o"))) {
                        String data = jComboBox.getSelectedItem().toString();

                        if (!data.equalsIgnoreCase("")) {

                            if (ind.getClave().equalsIgnoreCase("y")) {
                                DatoNuevoCampoPrincipal = data;
                            }

                            if (ind.getClave().equalsIgnoreCase("s")) {
                                if (sec1) {
                                    DatoNuevoCampoSec1 = data;
                                    sec1 = false;
                                } else if (sec2) {
                                    DatoNuevoCampoSec2 = data;
                                    sec2 = false;
                                } else if (sec3) {
                                    DatoNuevoCampoSec3 = data;
                                    sec3 = false;
                                }
                            }

                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(data);
                        } else {
                            String arg = ind.getIndice().replace("_", " ");
                            arg = arg.toLowerCase();
                            char[] cs = arg.toCharArray();
                            char ch = cs[0];
                            cs[0] = Character.toUpperCase(ch);
                            arg = String.valueOf(cs);

                            throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                        }
                    } else {
                        indice.setIdCategoria(ind.getIdCategoria());
                        indice.setIdIndice(ind.getIdIndice());
                        indice.setIndice(ind.getIndice());
                        indice.setClave(ind.getClave());
                        indice.setTipo(ind.getTipo());
                        indice.setValor(jComboBox.getSelectedItem().toString());
                    }

                } else if (ind.getTipo().equalsIgnoreCase("fecha")) {

                    try {

                        jDateChooser = (JDateChooser) ind.getValor();
                        Calendar calendar = jDateChooser.getCalendar();

                        if ((ind.getClave().equalsIgnoreCase("y")) || (ind.getClave().equalsIgnoreCase("s")) || (ind.getClave().equalsIgnoreCase("o"))) {

                            if (calendar != null) {

                                if (ind.getClave().equalsIgnoreCase("y")) {
                                    DatoNuevoCampoPrincipal = sdf.format(calendar.getTime());
                                }

                                if (ind.getClave().equalsIgnoreCase("s")) {
                                    if (sec1) {
                                        DatoNuevoCampoSec1 = sdf.format(calendar.getTime());
                                        sec1 = false;
                                    } else if (sec2) {
                                        DatoNuevoCampoSec2 = sdf.format(calendar.getTime());
                                        sec2 = false;
                                    } else if (sec3) {
                                        DatoNuevoCampoSec3 = sdf.format(calendar.getTime());
                                        sec3 = false;
                                    }
                                }

                                indice.setIdCategoria(ind.getIdCategoria());
                                indice.setIdIndice(ind.getIdIndice());
                                indice.setIndice(ind.getIndice());
                                indice.setClave(ind.getClave());
                                indice.setTipo(ind.getTipo());
                                indice.setValor(sdf.format(calendar.getTime()));
                            } else {
                                String arg = ind.getIndice().replace("_", " ");
                                arg = arg.toLowerCase();
                                char[] cs = arg.toCharArray();
                                char ch = cs[0];
                                cs[0] = Character.toUpperCase(ch);
                                arg = String.valueOf(cs);

                                throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                            }
                        } else {
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(sdf.format(calendar.getTime()));
                        }

                    } catch (NullPointerException e) {
                    }

                } else if (ind.getTipo().equalsIgnoreCase("numero")) {

                    jTextField = (JTextField) ind.getValor();

                    if (jTextField.getText().matches(UtilidadPalabras.VALIDAR_NUMEROS)) {

                        if ((ind.getClave().equalsIgnoreCase("y")) || (ind.getClave().equalsIgnoreCase("s")) || (ind.getClave().equalsIgnoreCase("o"))) {
                            String data = jTextField.getText();

                            if (!data.equalsIgnoreCase("")) {

                                if (ind.getClave().equalsIgnoreCase("y")) {
                                    DatoNuevoCampoPrincipal = data;
                                }

                                if (ind.getClave().equalsIgnoreCase("s")) {
                                    if (sec1) {
                                        DatoNuevoCampoSec1 = data;
                                        sec1 = false;
                                    } else if (sec2) {
                                        DatoNuevoCampoSec2 = data;
                                        sec2 = false;
                                    } else if (sec3) {
                                        DatoNuevoCampoSec3 = data;
                                        sec3 = false;
                                    }
                                }

                                indice.setIdCategoria(ind.getIdCategoria());
                                indice.setIdIndice(ind.getIdIndice());
                                indice.setIndice(ind.getIndice());
                                indice.setClave(ind.getClave());
                                indice.setTipo(ind.getTipo());
                                indice.setValor(data);
                            } else {
                                String arg = ind.getIndice().replace("_", " ");
                                arg = arg.toLowerCase();
                                char[] cs = arg.toCharArray();
                                char ch = cs[0];
                                cs[0] = Character.toUpperCase(ch);
                                arg = String.valueOf(cs);

                                throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                            }
                        } else {
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(jTextField.getText());
                        }
                    } else {
                        String arg = ind.getIndice().replace("_", " ");
                        arg = arg.toLowerCase();
                        char[] cs = arg.toCharArray();
                        char ch = cs[0];
                        cs[0] = Character.toUpperCase(ch);
                        arg = String.valueOf(cs);

                        throw new DW4JDesktopExcepcion("el " + arg + " debe ser solo numeros");
                    }

                } else if (ind.getTipo().equalsIgnoreCase("area")) {

                    jTextArea = (JTextArea) ind.getValor();
                    if ((ind.getClave().equalsIgnoreCase("y")) || (ind.getClave().equalsIgnoreCase("s")) || (ind.getClave().equalsIgnoreCase("o"))) {
                        String data = jTextArea.getText();

                        if (!data.equalsIgnoreCase("")) {

                            if (ind.getClave().equalsIgnoreCase("y")) {
                                DatoNuevoCampoPrincipal = data;
                            }

                            if (ind.getClave().equalsIgnoreCase("s")) {
                                if (sec1) {
                                    DatoNuevoCampoSec1 = data;
                                    sec1 = false;
                                } else if (sec2) {
                                    DatoNuevoCampoSec2 = data;
                                    sec2 = false;
                                } else if (sec3) {
                                    DatoNuevoCampoSec3 = data;
                                    sec3 = false;
                                }
                            }

                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(data);
                        } else {
                            String arg = ind.getIndice().replace("_", " ");
                            arg = arg.toLowerCase();
                            char[] cs = arg.toCharArray();
                            char ch = cs[0];
                            cs[0] = Character.toUpperCase(ch);
                            arg = String.valueOf(cs);

                            throw new DW4JDesktopExcepcion("el " + arg + " no debe estar vacio");
                        }
                    } else {
                        indice.setIdCategoria(ind.getIdCategoria());
                        indice.setIdIndice(ind.getIdIndice());
                        indice.setIndice(ind.getIndice());
                        indice.setClave(ind.getClave());
                        indice.setTipo(ind.getTipo());
                        indice.setValor(jTextArea.getText());
                    }

                }
                expeWS.getIndices().add(indice);

            }

            //expeWS.setRrhh(rh);
            expeWS.setExpediente(expediente.getIdExpediente());
            expeWS.setIdLibreria(idLibreria);
            expeWS.setIdCategoria(idCategoria);

            if (!DatoViejoCampoPrincipal.equals(DatoNuevoCampoPrincipal)) {
                int n = JOptionPane.showOptionDialog(this,
                        "Se procedera a cambiar el dato del indice principal\n"
                        + "¿Esta seguro de hacerlo?",
                        "Advertencia",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, new Object[]{"SI", "NO"}, "NO");

                if (n == JOptionPane.YES_OPTION) {
                    exito = new ActualizaIndice().updateIndices(expeWS);
                } else {
                    return;
                }
            } else {
                exito = new ActualizaIndice().updateIndices(expeWS);
            }

            if (exito) {
                actualizaTabla();
                repaint();
                JOptionPane.showMessageDialog(this, "Indices actualizados con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                DatoNuevoCampoPrincipal = null;
                DatoNuevoCampoSec1 = null;
                DatoNuevoCampoSec2 = null;
                DatoNuevoCampoSec3 = null;
                
            } else {
                traza.trace("Problema al actualizar los indices respuesta del servicio " + exito, Level.INFO);
                JOptionPane.showMessageDialog(this, "Problema al actualizar los indices", "No se puede actualizar", JOptionPane.ERROR_MESSAGE);
            }

        } catch (DW4JDesktopExcepcion ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            traza.trace(ex.getMessage(), Level.ERROR, ex);
        } catch (SOAPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            traza.trace("Error del servicio web", Level.ERROR, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la actualizacion de los indices \n" + ex.getMessage(), "No se puede actualizar.", JOptionPane.ERROR_MESSAGE);
            traza.trace("Error en la actualizacion de los indices", Level.ERROR, ex);
        }
    }

    private void actualizaTabla() {

        ModeloTabla modelo = new ModeloTabla();
        Vector datas = modeloTabla.getDataVector();
        int size = datas.size();

        try {

            modelo.addColumn(dtCabezeras.getClavePrimaria());
            modelo.addColumn(dtCabezeras.getClaveSec1());
            modelo.addColumn(dtCabezeras.getClaveSec2());
            modelo.addColumn(dtCabezeras.getClaveSec3());

//            for(int i=0;i<size;i++){
//                modeloTabla.removeRow(i);
//            }
            for (int i = 0; i < size; i++) {
                Vector datos = (Vector) datas.get(i);

                if (filaSeleccionada == i) {
                    datos.clear();
                    if (DatoNuevoCampoPrincipal != null) {
                        datos.add(DatoNuevoCampoPrincipal);
                    }
                    if (DatoNuevoCampoSec1 != null) {
                        datos.add(DatoNuevoCampoSec1);
                    }
                    if (DatoNuevoCampoSec2 != null) {
                        datos.add(DatoNuevoCampoSec2);
                    }
                    if (DatoNuevoCampoSec3 != null) {
                        datos.add(DatoNuevoCampoSec3);
                    }

                    modelo.addRow(datos);
                } else {
                    modelo.addRow(datos);
                }
            }

            modeloTabla.getDataVector().clear();
            modeloTabla = modelo;
            jtResultadoConsulta.setModel(modeloTabla);

        } catch (Exception e) {
            traza.trace("Error en la actualizacion de la tabla ", Level.ERROR, e);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JTable jtResultadoConsulta;
    private javax.swing.JPanel panelIndices;
    private javax.swing.JPanel panelResultado;
    // End of variables declaration//GEN-END:variables

    public void setIndices(List<Indice> indices) {

        JDateChooser jDateChooser;
        JComboBox jComboBox;
        JTextField jTextField;
        JTextArea jTextArea;

        for (Indice indice : indices) {
            if (indice.getClave().equalsIgnoreCase("y")) {

                if (indice.getTipo().equalsIgnoreCase("texto")) {

                    jTextField = (JTextField) indice.getValor();
                    DatoViejoCampoPrincipal = jTextField.getText();

                } else if (indice.getTipo().equalsIgnoreCase("area")) {

                    jTextArea = (JTextArea) indice.getValor();
                    DatoViejoCampoPrincipal = jTextArea.getText();

                } else if (indice.getTipo().equalsIgnoreCase("numero")) {

                    jTextField = (JTextField) indice.getValor();
                    DatoViejoCampoPrincipal = jTextField.getText();

                } else if (indice.getTipo().equalsIgnoreCase("fecha")) {

                    jDateChooser = (JDateChooser) indice.getValor();
                    Calendar calendar = jDateChooser.getCalendar();
                    DatoViejoCampoPrincipal = calendar;

                } else if (indice.getTipo().equalsIgnoreCase("combo")) {

                    jComboBox = (JComboBox) indice.getValor();
                    DatoViejoCampoPrincipal = jComboBox.getSelectedItem();

                }
            }
        }

        this.indices = indices;
    }
    
}
