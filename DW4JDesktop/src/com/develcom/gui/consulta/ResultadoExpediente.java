package com.develcom.gui.consulta;

import com.develcom.administracion.Categoria;
import com.develcom.documento.InfoDocumento;
import com.develcom.administracion.TipoDocumento;
import com.develcom.dao.DatosTabla;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.dao.OtrosDatos;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.administracion.SubCategoria;
import com.develcom.gui.Principal;
import com.develcom.gui.visor.VerDocumento;
import com.develcom.gui.visor.VerImagenes;
import com.develcom.gui.calidad.BuscaExpediente;
import com.develcom.gui.consulta.ficha.Ficha;
import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.gui.reportes.tools.Foliatura;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.ModeloTabla;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.trazas.Traza;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;
import ve.com.develcom.expediente.GestionDocumentos;

public class ResultadoExpediente extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -8972073117537325443L;

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(ResultadoExpediente.class);
    /**
     * Resultado de las claves primarias y segunda de la consulta dinamica
     */
    private List<DatosTabla> datosTablas = new ArrayList<>();
    /**
     * Modelo para la tabla
     */
    private ModeloTabla modeloTabla;
    /**
     * Listado de subCategorias
     */
    private List<SubCategoria> listaSubCategorias = new ArrayList<>();
    /**
     * Datos del expediente
     */
    private Expediente expe;
    /**
     * Listado de tipos de docuemtos digitalizados
     */
    private List<InfoDocumento> documentosDigitalizado = new ArrayList<>();
    /**
     * Listado del resultado de la consulta dinamica
     */
    private static List<ConsultaDinamica> consultaDinamicas;
    private static List<com.develcom.calidad.ConsultaDinamica> consultaDinamicasAprobar;
    private List<Indice> listaIndice = null;
    private boolean generico;

    /**
     * Constructor que se usa cuando se aprueba o se rechaza un documento
     */
    public ResultadoExpediente() {
        initComponents();

        jtrDocDigit.setModel(new DefaultTreeModel(null));

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            setTitle("Aprobar o Rechazar Documentos Digitalizados");
            jbVerFicha.setVisible(false);
        }

        if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
            jbtFoliatura.setVisible(false);
        }
        jbEstatusExpediente.setVisible(false);

        jbVerFicha.setVisible(false);

        CentraVentanas.centrar(this, Principal.desktop);
        jbtVisualizar.setEnabled(false);
        expe = ManejoSesion.getExpediente();
        this.setVisible(true);
    }

    /**
     * Constructor
     *
     * @param consultaDinamicas
     * @param generico
     */
    public ResultadoExpediente(List<ConsultaDinamica> consultaDinamicas, boolean generico) {

        ResultadoExpediente.consultaDinamicas = consultaDinamicas;
        this.expe = ManejoSesion.getExpediente();
        this.generico = generico;
        initComponents();
        CentraVentanas.centrar(this, Principal.desktop);

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            setTitle("Aprobar o Rechazar Documentos Digitalizados");
        }

        llenarDatosTabla();
        llenarTabla();
        jtrDocDigit.setExpandsSelectedPaths(true);
        if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
            jbtFoliatura.setVisible(false);
        }
        jbEstatusExpediente.setVisible(false);

        jbVerFicha.setVisible(false);

        jbtVisualizar.setEnabled(false);
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

    public void convertir() {

        ConsultaDinamica conDinamica;
        Indice indice;
        List<com.develcom.calidad.Indice> indices;
//        Object idExpediente, tmp = "";

        if (consultaDinamicasAprobar != null) {
            consultaDinamicas = new ArrayList<>();
            for (com.develcom.calidad.ConsultaDinamica cd : consultaDinamicasAprobar) {

                conDinamica = new ConsultaDinamica();
                indices = cd.getIndices();

//                idExpediente = buscarIdExpediente(indices);
//                if(!idExpediente.equals(tmp)){
//                    tmp=idExpediente;
                for (com.develcom.calidad.Indice ind : indices) {
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
        } else {
            JOptionPane.showMessageDialog(this, "Problemas en la convercion para aprobacion de documentos", "Advertencia", JOptionPane.WARNING_MESSAGE);
            this.dispose();
            System.gc();

        }
        if (!consultaDinamicas.isEmpty()) {
            llenarDatosTabla();
            llenarTabla();
        }
    }

    private String buscarCategoria(int idCategoria) {

        String categoria = "";

        for (Categoria cat : expe.getCategorias()) {
            if (cat.getIdCategoria() == idCategoria) {
                categoria = cat.getCategoria();
            }
        }

        return categoria;
    }

    /**
     * Crea el objeto con los indices primarios y segundos, del resultado de la
     * consulta dinamica
     */
    private void llenarDatosTabla() {

        traza.trace("armando el data table", Level.INFO);
        DatosTabla dt = null;
        OtrosDatos od;
        List<OtrosDatos> listOtrosDatos = new ArrayList<>();
        DatosTabla dtCabezeras = new DatosTabla();
        boolean sec1 = true, sec2 = true, sec3 = true, ban = true, bus = true;
        int cont = 0;
        List<Indice> listIndice;

        for (ConsultaDinamica cd : consultaDinamicas) {
            listIndice = cd.getIndices();

            for (Indice ind : listIndice) {

                if (ban) {
                    dt = new DatosTabla();
                    sec1 = true;
                    sec2 = true;
                    sec3 = true;
                }

                if (ind.getClave().equalsIgnoreCase("y")) {

                    if (generico) {
                        dtCabezeras.setCategoria("Categoria");
                        dt.setCategoria(buscarCategoria(ind.getIdCategoria()));
                        cont++;
                    }
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
                    //traza.trace(dt.getClavePrimaria()+" - "+dt.getDatoPrimario()+" "+dt.getSubCategoria(), Level.INFO);

                } else if (ind.getClave().equalsIgnoreCase("s")) {

                    if (sec1) {

                        //if (ind.getIndice().regionMatches(true, 0, "fecha", 0, 4)) {
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

                        //if (ind.getIndice().regionMatches(true, 0, "fecha", 0, 4)) {
                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {
                            try {
//                                XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                Calendar c = fechaIngreso.toGregorianCalendar();

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
                        //traza.trace(dt.getClaveSec2()+" - "+dt.getDatoSec2(), Level.INFO);

                    } else if (sec3) {

                        //if (ind.getIndice().regionMatches(true, 0, "fecha", 0, 4)) {
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

                            dtCabezeras.setClaveSec3(crearEtiqueta(ind.getIndice()));
                            dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                            dt.setDatoSec3(ind.getValor().toString());
                        }

                        sec3 = false;
                        cont++;
                        //traza.trace(dt.getClaveSec3()+" - "+dt.getDatoSec3(), Level.INFO);
                        //ban=true;

                    }
                } else {
                    od = new OtrosDatos();
                    od.setNombre(crearEtiqueta(ind.getIndice()));
                    od.setValor(ind.getValor().toString());
                    listOtrosDatos.add(od);
                    dt.setOtrosDatos(listOtrosDatos);
                    dtCabezeras.setOtrosDatos(listOtrosDatos);
//                    cont++;
                }

                if (generico) {
                    if (cont == 5) {
                        datosTablas.add(dt);
                        ban = true;
                        bus = true;
                        cont = 0;
                    }
                } else if (!generico) {
                    if (cont == 4) {
                        datosTablas.add(dt);
                        ban = true;
                        bus = true;
                        cont = 0;
                    }
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

        if (dt.getOtrosDatos() == null) {
            modeloTabla.addColumn(dt.getClavePrimaria());
            modeloTabla.addColumn(dt.getClaveSec1());
            modeloTabla.addColumn(dt.getClaveSec2());
            modeloTabla.addColumn(dt.getClaveSec3());
            //modeloTabla.addColumn("SubCategoria");
        } else {
            try {
                if (!dt.getCategoria().equalsIgnoreCase("") || dt.getCategoria() != null) {
                    modeloTabla.addColumn(dt.getCategoria());
                    cont++;
                }
            } catch (NullPointerException e) {
            }
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

            if (cont != 4 && !generico) {
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

            if (dt.getOtrosDatos() == null) {
                if (generico) {
                    filas.add(dt.getCategoria());
                }
                filas.add(dt.getDatoPrimario());
                filas.add(dt.getDatoSec1());
                filas.add(dt.getDatoSec2());
                filas.add(dt.getDatoSec3());
                //filas.add(dt.getSubCategoria());
                modeloTabla.addRow(filas);
            } else {
                try {
                    if (!dt.getCategoria().equalsIgnoreCase("") || dt.getCategoria() != null) {
                        filas.add(dt.getCategoria());
                        cont++;
                    }
                } catch (NullPointerException e) {
                }
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

                if (cont != 4 && !generico) {
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

        jtResultadoConsulta.setModel(modeloTabla);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelResultado = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtResultadoConsulta = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        panelIndices = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtrDocDigit = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jButtonRegresar = new javax.swing.JButton();
        jbVerFicha = new javax.swing.JButton();
        jbtFoliatura = new javax.swing.JButton();
        jbtVisualizar = new javax.swing.JButton();
        jbEstatusExpediente = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setTitle("Resultado Consulta");

        panelResultado.setBackground(new java.awt.Color(224, 239, 255));
        panelResultado.setBorder(javax.swing.BorderFactory.createTitledBorder("Seleccione el Expediente"));

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
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelResultadoLayout.setVerticalGroup(
            panelResultadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane3.setBackground(new java.awt.Color(224, 239, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Indices"));

        panelIndices.setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelIndicesLayout = new javax.swing.GroupLayout(panelIndices);
        panelIndices.setLayout(panelIndicesLayout);
        panelIndicesLayout.setHorizontalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 854, Short.MAX_VALUE)
        );
        panelIndicesLayout.setVerticalGroup(
            panelIndicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(panelIndices);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jtrDocDigit.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jtrDocDigit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtrDocDigitMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtrDocDigit);

        jPanel1.setBackground(new java.awt.Color(224, 239, 255));

        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel.png"))); // NOI18N
        jButtonRegresar.setMnemonic('c');
        jButtonRegresar.setText("Cancelar");
        jButtonRegresar.setToolTipText("Cancelar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jbVerFicha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/folder_users.png"))); // NOI18N
        jbVerFicha.setText("Ver Ficha");
        jbVerFicha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbVerFichaActionPerformed(evt);
            }
        });

        jbtFoliatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/reporte/1344612993_document.png"))); // NOI18N
        jbtFoliatura.setText("Foliatura");
        jbtFoliatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFoliaturaActionPerformed(evt);
            }
        });

        jbtVisualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Open24.gif"))); // NOI18N
        jbtVisualizar.setMnemonic('a');
        jbtVisualizar.setText("Ver documento");
        jbtVisualizar.setToolTipText("Abrir un documento");
        jbtVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtVisualizarActionPerformed(evt);
            }
        });

        jbEstatusExpediente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Status.png"))); // NOI18N
        jbEstatusExpediente.setText("Cambio de Estatus");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(jbtVisualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(jbtFoliatura)
                .addGap(18, 18, 18)
                .addComponent(jbEstatusExpediente)
                .addGap(18, 18, 18)
                .addComponent(jbVerFicha)
                .addGap(18, 18, 18)
                .addComponent(jButtonRegresar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegresar)
                    .addComponent(jbVerFicha)
                    .addComponent(jbtFoliatura, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtVisualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEstatusExpediente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelResultado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
            if (generico) {
                ConsultaTodos ct = new ConsultaTodos();
                Principal.desktop.add(ct);
            } else {
                ConsultaExpediente ce = new ConsultaExpediente();
                Principal.desktop.add(ce);
            }
        } else if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            BuscaExpediente be = new BuscaExpediente();
            Principal.desktop.add(be);
        } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR TODOS")) {
            ConsultaTodos ce = new ConsultaTodos();
            Principal.desktop.add(ce);
        }

        //Principal.desktop.add(new ConsultarExpediente());
        //Principal.desktop.add(new ConsultarExpediente(this.expe));
        //  LibreriaConsulta jl = new LibreriaConsulta(Principal.thisFrame, ManejoSesion.getSesion());
}//GEN-LAST:event_jButtonRegresarActionPerformed

    private void jbtVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtVisualizarActionPerformed
        abrir();
}//GEN-LAST:event_jbtVisualizarActionPerformed

    /**
     * Muestra el documento digitalizado
     */
    private void abrir() {
        //List<InfoDocumento> infoDocumentos = new ArrayList<InfoDocumento>();
        InfoDocumento infoDocumento = null;
        DefaultMutableTreeNode verificador;
        TreeNode categori;
        TreeNode subCategor;
        String tipoDocumento, formato = "";

        if (!jtrDocDigit.isSelectionEmpty()) {
            verificador = (DefaultMutableTreeNode) jtrDocDigit.getLastSelectedPathComponent();

            if (!verificador.isLeaf()) {
                JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser consultado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
            } else {
                categori = verificador.getRoot();
                subCategor = verificador.getParent();
                tipoDocumento = verificador.toString().trim();
                traza.trace("tipo de documento a abrir " + tipoDocumento, Level.INFO);
                ManejoSesion.getExpediente().setSubCategoria(subCategor.toString());

                if (listaSubCategorias != null) {
                    for (SubCategoria sc : listaSubCategorias) {
                        if (sc.getSubCategoria().equalsIgnoreCase(subCategor.toString())) {
                            ManejoSesion.getExpediente().setIdSubCategoria(sc.getIdSubCategoria());
                            break;
                        }
                    }
                }

                traza.trace("buscando la extencion del tipo de documento " + tipoDocumento, Level.INFO);
                for (InfoDocumento infDoc : documentosDigitalizado) {
                    //if (tipoDoc.startsWith(tipoDocuemnto)) {
                    if (tipoDocumento.contains(infDoc.getTipoDocumento())) {
                        //if(infDoc.getIdDocumento()==mapTipodocumento.get(tipoDocuemnto)){

                        formato = infDoc.getFormato();

                        traza.trace("tipo de documento a mostrar " + tipoDocumento, Level.INFO);
                        traza.trace("nombre del archivo " + formato, Level.INFO);
                        traza.trace("formato del archivo " + infDoc.getFormato(), Level.INFO);
                        traza.trace("ruta del archivo " + infDoc.getRutaArchivo(), Level.INFO);
                        traza.trace("version del archivo " + infDoc.getVersion(), Level.INFO);
                        traza.trace("id del archivo " + infDoc.getIdInfoDocumento(), Level.INFO);
                        traza.trace("numero del documento (archivo) " + infDoc.getNumeroDocumento(), Level.INFO);
                        traza.trace("id del documento " + infDoc.getIdDocumento(), Level.INFO);
                        traza.trace("fecha vencimiento del archivo " + infDoc.getFechaVencimiento(), Level.INFO);
                        infoDocumento = infDoc;
                        expe.setTipoDocumento(tipoDocumento);
                        ManejoSesion.setExpediente(expe);
                    }
                }

                repaint();
                Principal.desktop.repaint();
                if (formato.equalsIgnoreCase("tif") || formato.equalsIgnoreCase("tiff")) {
                    VerDocumento vd = new VerDocumento(infoDocumento);
                    Principal.desktop.add(vd);
                    vd.toFront();
                    this.toBack();
                } else if (formato.equalsIgnoreCase("pdf")) {
                    VerDocumentoPDF vdpdf = new VerDocumentoPDF("", infoDocumento, null, false, false, 0);
                    Principal.desktop.add(vdpdf);
                    vdpdf.toFront();
                    this.toBack();
                } else if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {
                    VerImagenes vi = new VerImagenes("", infoDocumento, null, false, 0, false);
                    Principal.desktop.add(vi);
                    vi.toFront();
                    this.toBack();
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Elija uno de los documentos digitalizados", "Información...", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void refrescar() {
//         jtResultadoConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
//             @Override
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                jtResultadoConsultaMouseClicked(evt);
//            }
//        });
        traza.trace("refrescando el expediente " + ManejoSesion.getExpediente().getIdExpediente(), Level.INFO);
        jtResultadoConsultaMouseClicked(null);
    }

    /**
     * Muestra el arbol con los tipos de documentos digitalizados y el valor de
     * los indices
     *
     * @param evt
     */
    private void jtResultadoConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtResultadoConsultaMouseClicked

        buscar(evt);

}//GEN-LAST:event_jtResultadoConsultaMouseClicked

    private void buscar(final java.awt.event.MouseEvent evt) {

        final MostrarProceso proceso = new MostrarProceso("<html>Generando el expediente <br/>"
                + " esto puede tardar segun<br/> la cantidad de documentos</html>");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {

                buscarExpediente(evt);
                proceso.detener();

            }
        }).start();
    }

    private void buscarExpediente(java.awt.event.MouseEvent evt) {

        boolean ban = true;
        List<String> idSubCatBuscadas = new ArrayList<>();
        String idExpediente, categoria = "";
        int row = jtResultadoConsulta.getSelectedRow();
        int column = jtResultadoConsulta.getSelectedColumn();
        List<SubCategoria> listaSubCat = expe.getSubCategorias();
        
        traza.trace("expediente sesion "+expe.getIdExpediente(), Level.INFO);

        if (evt != null) {
            if (generico) {
                categoria = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
                idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 1));
            } else {
                idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
                //String id_exp = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), jtResultadoConsulta.getSelectedColumn()));
                //            String nombre1 = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), jtResultadoConsulta.getSelectedColumn() + 1));
                //            String nombre3 = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), jtResultadoConsulta.getSelectedColumn() + 2));
                //            String nombre4 = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), jtResultadoConsulta.getSelectedColumn() + 3));
                //  String nombre2 = ((String) tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn() + 4));

                //panelArbol.setBorder(javax.swing.BorderFactory.createTitledBorder("Expediente "+idExpediente));
            }
        } else if (ManejoSesion.getExpediente().getIdExpediente() == null) {
            if (generico) {
                categoria = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
                idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 1));
            }
            idExpediente = ((String) jtResultadoConsulta.getValueAt(jtResultadoConsulta.getSelectedRow(), 0));
        } else {
            idExpediente = ManejoSesion.getExpediente().getIdExpediente();
        }

        traza.trace("selecciono el expediente " + idExpediente, Level.INFO);

        expe.setIdExpediente(idExpediente);
        ManejoSesion.getExpediente().setIdExpediente(idExpediente);

        if (!idSubCatBuscadas.isEmpty()) {
            idSubCatBuscadas.clear();
        }

        for (ConsultaDinamica cd : consultaDinamicas) {
            if (ban) {
                listaIndice = cd.getIndices();
                for (Indice ind : listaIndice) {
                    try {
                        if (ind.getClave().equalsIgnoreCase("y")) {
                            if (ind.getValor().toString().equalsIgnoreCase(idExpediente)) {
                                traza.trace("idExpediente seleccionado " + idExpediente, Level.INFO);

                                if (generico) {
                                    List<com.develcom.expediente.SubCategoria> listSubCat = new BuscaExpedienteDinamico().encontrarSubCategorias(expe.getIdCategorias());
                                    for (com.develcom.expediente.SubCategoria sc : listSubCat) {

                                        idSubCatBuscadas.add(String.valueOf(sc.getIdSubCategoria()));

                                        SubCategoria subCate = new SubCategoria();
                                        subCate.setEstatus(sc.getEstatus());
                                        subCate.setIdCategoria(sc.getIdCategoria());
                                        subCate.setIdSubCategoria(sc.getIdSubCategoria());
                                        subCate.setSubCategoria(sc.getSubCategoria());
                                        listaSubCategorias.add(subCate);
                                    }
                                } else if ((!listaSubCat.isEmpty()) && listaSubCat != null) {
                                    for (SubCategoria sc : listaSubCat) {
                                        idSubCatBuscadas.add(String.valueOf(sc.getIdSubCategoria()));
                                        listaSubCategorias.add(sc);
                                    }
                                } else {
                                    listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria(null, expe.getIdCategoria(), 0);
                                    for (SubCategoria sc : listaSubCategorias) {
                                        idSubCatBuscadas.add(String.valueOf(sc.getIdSubCategoria()));
                                    }
                                }
                                ban = false;
                                break;
                            }
                        }
                    } catch (NullPointerException e) {

                    } catch (SOAPException | SOAPFaultException e) {
                        traza.trace("error ", Level.ERROR, e);
                    }
                }
            }
        }

        panelIndices.removeAll();
        panelIndices.setLayout(new FlowLayout());
        panelIndices.add(new CreaObjetosDinamicos().mostrarIndices(listaIndice));
        panelIndices.updateUI();

        mostrarArbol(idExpediente, idSubCatBuscadas, categoria);

    }

    /**
     * Construye el arbol con los tipos de documentos digitalizados
     *
     * @param idExpediente el id del expediente selccionado
     */
    private synchronized void mostrarArbol(String idExpediente, List<String> idSubCatBuscadas, String categoria) {

//        MostrarProceso mp = new MostrarProceso("Construyendo el arbol, por favor espere");
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        List<Integer> idDocumento = new ArrayList<>();
        List<com.develcom.expediente.TipoDocumento> listaTipoDoc = expe.getTipoDocumentos();
        List<Categoria> categorias = expe.getCategorias();
        String cat = "", nombreTD, tmp[], fullName;
        int idCat = 0, totalDocDig, cont = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        DefaultMutableTreeNode arbolDigitalizados = new DefaultMutableTreeNode();
        DefaultMutableTreeNode ramaDigitalizados;// = new DefaultMutableTreeNode();
        DefaultMutableTreeNode hijo;

        traza.trace("armando el arbol con el expediente " + idExpediente, Level.INFO);

        try {

            jtrDocDigit.setModel(null);

            if (generico) {
                for (Categoria cate : categorias) {
                    if (cate.getCategoria().equals(categoria)) {
                        idCat = cate.getIdCategoria();
                        cat = cate.getCategoria();
                        break;
                    }
                }
            } else {
                idCat = expe.getIdCategoria();
                cat = expe.getCategoria();
            }

            traza.trace("id categoria: " + idCat + " - Categoria: " + cat, Level.INFO);
            traza.trace("buscando los tipos de documentos disponibles", Level.INFO);

            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria(null, idCat, 0);

            arbolDigitalizados.setUserObject(cat + " - " + expe.getIdExpediente());

            for (SubCategoria sc : listaSubCategorias) {
                for (String idSuc : idSubCatBuscadas) {
                    int idSubCat = Integer.parseInt(idSuc);
                    if (sc.getIdSubCategoria() == idSubCat) {

                        if (listaTipoDoc.isEmpty() || listaTipoDoc == null) {
                            tipoDocumentos = new AdministracionBusqueda().buscarTipoDocumento(null, sc.getIdCategoria(), sc.getIdSubCategoria());
                        } else {
                            tipoDocumentos = convertidor(listaTipoDoc);
                        }

                        //llena el arbol con los tipos de documentos disponibles
                        for (TipoDocumento td : tipoDocumentos) {
                            if (td.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                                idDocumento.add(td.getIdTipoDocumento());
                            }
                        }

                        if (!idDocumento.isEmpty()) {
                            //busca los tipos de documentos digitalizados
                            traza.trace("buscar infodocumento del idSubCategoria " + idSuc, Level.INFO);
                            infoDocumentos = new GestionDocumentos().encontrarInformacionDoc(idDocumento, idExpediente, idCat, Integer.parseInt(idSuc), 1, 0, false);

                            if (!infoDocumentos.isEmpty()) {
                                traza.trace("rama subCategoria " + sc.getSubCategoria(), Level.INFO);
                                ramaDigitalizados = new DefaultMutableTreeNode(sc.getSubCategoria());

                                totalDocDig = infoDocumentos.size();

                                tmp = new String[totalDocDig];

                                for (InfoDocumento infoDoc : infoDocumentos) {

                                    nombreTD = infoDoc.getTipoDocumento() + " - " + infoDoc.getNumeroDocumento();
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
                                        if (infoDoc.isTipoDocDatoAdicional()) {
                                            List<com.develcom.documento.DatoAdicional> datosAdicionales = infoDoc.getLsDatosAdicionales();
                                            int size = datosAdicionales.size(), i = 1;
                                            for (com.develcom.documento.DatoAdicional datAd : datosAdicionales) {

                                                if (datAd.getTipo().equalsIgnoreCase("fecha")) {
//                                                    XMLGregorianCalendar calendar = (XMLGregorianCalendar) datAd.getValor();
//                                                    Calendar cal = calendar.toGregorianCalendar();
                                                    if (i == size) {
                                                        da = da + " " + datAd.getValor().toString();
                                                    } else {
                                                        da = da + " " + datAd.getValor().toString() + ",";
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

                                        if (infoDoc.isTipoDocDatoAdicional()) {
                                            fullName = nombreTD + " (" + da + ")";
                                            infoDoc.setTipoDocumento(fullName);
                                            hijo = new DefaultMutableTreeNode(fullName);
                                        } else {
                                            fullName = nombreTD;
                                            infoDoc.setTipoDocumento(fullName);
                                            hijo = new DefaultMutableTreeNode(fullName);

                                        }

                                        documentosDigitalizado.add(infoDoc);
                                        ramaDigitalizados.add(hijo);
                                        traza.trace("tipo documento " + fullName + " de la subCategoria " + sc.getSubCategoria(), Level.INFO);
                                    }
                                }
                                arbolDigitalizados.add(ramaDigitalizados);
                            }
                        }
                    }
                }
                cont = 0;

                if (!idDocumento.isEmpty()) {
                    idDocumento.clear();
                }
                if (!tipoDocumentos.isEmpty()) {
                    tipoDocumentos.clear();
                }
                if (!infoDocumentos.isEmpty()) {
                    infoDocumentos.clear();
                }
            }

            jtrDocDigit.setModel(new DefaultTreeModel(arbolDigitalizados));

        } catch (SOAPException | SOAPFaultException | NumberFormatException e) {
            traza.trace("Error al crear el arbol de documentos", Level.ERROR, e);
//            JOptionPane.showMessageDialog(this, e.getMessage(),  "Adverntencia", JOptionPane.WARNING_MESSAGE);
        }

    }

    private List<TipoDocumento> convertidor(List<com.develcom.expediente.TipoDocumento> listaTipoDoc) {
        List<TipoDocumento> tipoDocumentos = new ArrayList<TipoDocumento>();
        TipoDocumento tipoDocumento = new TipoDocumento();

        for (com.develcom.expediente.TipoDocumento tds : listaTipoDoc) {

            tipoDocumento.setEstatus(tds.getEstatus());
            tipoDocumento.setIdCategoria(tds.getIdCategoria());
            tipoDocumento.setIdSubCategoria(tds.getIdSubCategoria());
            tipoDocumento.setIdTipoDocumento(tds.getIdTipoDocumento());
            tipoDocumento.setTipoDocumento(tds.getTipoDocumento());
            tipoDocumento.setVencimiento(tds.getVencimiento());
            tipoDocumentos.add(tipoDocumento);
        }

        return tipoDocumentos;
    }

    /**
     * Activa el boton de mostrar el documento digitalizado
     *
     * @param evt
     */
    private void jtrDocDigitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtrDocDigitMouseClicked

        DefaultMutableTreeNode verificador;

        if (!jtrDocDigit.isSelectionEmpty()) {
            verificador = (DefaultMutableTreeNode) jtrDocDigit.getLastSelectedPathComponent();

            if (verificador.isLeaf() && !verificador.isRoot()) {
                jbtVisualizar.setEnabled(true);
            } else {
                jbtVisualizar.setEnabled(false);
            }
        } else {
            jbtVisualizar.setEnabled(false);
        }

    }//GEN-LAST:event_jtrDocDigitMouseClicked

    private void jbtFoliaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFoliaturaActionPerformed

        Expediente expediente = ManejoSesion.getExpediente();
        int idLib = expediente.getIdLibreria();
        int idCat = expediente.getIdCategoria();
        String idExpediente = expediente.getIdExpediente();

        traza.trace("imprimiendo la foliatura", Level.INFO);
        traza.trace("idLibreria: " + idLib, Level.INFO);
        traza.trace("idCategoria: " + idCat, Level.INFO);
        traza.trace("idExpediente: " + idExpediente, Level.INFO);

        foliaturas(idLib, idCat, idExpediente);

//                if(!new Foliatura().generarFoliatura(idLib, idCat, idExpediente)){
//                    JOptionPane.showMessageDialog(this, "Problemas para crear la foliatura.\nProbablemente no hay documentos aprobados", "Advertencia", JOptionPane.WARNING_MESSAGE);
//                }

    }//GEN-LAST:event_jbtFoliaturaActionPerformed

    private void jbVerFichaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbVerFichaActionPerformed

        try {
            if (listaIndice != null || !listaIndice.isEmpty()) {
                Ficha ficha = new Ficha(listaIndice);
                Principal.desktop.add(ficha);
                ficha.toFront();
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Por favor seleccione un Expediente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Por favor seleccione un Expediente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jbVerFichaActionPerformed

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbEstatusExpediente;
    private javax.swing.JButton jbVerFicha;
    private javax.swing.JButton jbtFoliatura;
    private javax.swing.JButton jbtVisualizar;
    private javax.swing.JTable jtResultadoConsulta;
    private javax.swing.JTree jtrDocDigit;
    private javax.swing.JPanel panelIndices;
    private javax.swing.JPanel panelResultado;
    // End of variables declaration//GEN-END:variables

    public void setConsultaDinamicasAprobar(List<com.develcom.calidad.ConsultaDinamica> consultaDinamicasAprobar) {
        ResultadoExpediente.consultaDinamicasAprobar = consultaDinamicasAprobar;
    }
}
