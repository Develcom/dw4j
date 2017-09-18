/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * VerDocumentoPDF.java
 *
 * Created on 06-sep-2012, 9:14:30
 */
package com.develcom.gui.visor;

import com.develcom.autentica.Perfil;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.Indice;
import com.develcom.gui.Principal;
import com.develcom.gui.calidad.CalidadDocumento;
import com.develcom.gui.calidad.RechazarDocumento;
import com.develcom.gui.captura.DatoAdicional;
import com.develcom.gui.captura.DigitalizaDocumento;
import com.develcom.gui.consulta.ResultadoExpediente;
import com.develcom.gui.elimina.EliminaDocumento;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.gui.visor.tool.Acciones;
import com.develcom.gui.visor.tool.PreparaPagina;
import com.develcom.gui.visor.tool.ThumbAction;
import com.develcom.tools.Constantes;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.cryto.EncriptaDesencripta;
import com.develcom.tools.trazas.Traza;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import com.sun.pdfview.ThumbPanel;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.elimina.QuitaDocumento;
import ve.com.develcom.sesion.IniciaSesion;

/**
 *
 * @author develcom
 */
public class VerDocumentoPDF extends JInternalFrame {

    private static final long serialVersionUID = -1821197781616889328L;
//public class VerDocumentoPDF extends JFrame {
    /**
     * Total de paginas digitalizadas
     */
    private int cantidadPaginas = 0;
    /**
     * lista de indices dinamicos
     */
    private List<Indice> indices = new ArrayList<>();
    /**
     * accion a tomar (versionar o reemplazar)
     */
    private String accion;
    /**
     * objecto con la informacion del documento
     */
    private InfoDocumento infoDocumento;
    private com.develcom.elimina.InfoDocumento infoDocElimina;
    private int idInfoDocumento;
    private List<InfoDocumento> infoDocumentos;
    private Expediente expediente;
    private Traza traza = new Traza(VerDocumentoPDF.class);
    private Acciones acciones = new Acciones(this);
    private String nombreDocumento;
    private int paginaActual = -1;
    private PagePanel panelPagina;
    private PDFFile archivoPDF;
    private ThumbPanel thumbs;
    private JScrollPane thumbsScroll;
    private JScrollPane pageScroll;
    private PreparaPagina pagePrep;
    private int versionSeleccionada;
    private int versionMostrar;
    private boolean flagSave;
    private ToolsFiles toolsFile = new ToolsFiles();
    private File filePDF;
    private CalidadDocumento calidadDocumento;
    private boolean vers;
    private EliminaDocumento eliminaDocumento;

    public VerDocumentoPDF(String accion, InfoDocumento infoDocumento, File file, boolean flagSave, boolean vers, int version) {
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocumento = infoDocumento;
        this.accion = accion;
        this.flagSave = flagSave;
        this.filePDF = file;
        this.vers = vers;
        versionMostrar = version;
        iniciar(file, flagSave);
    }

    public VerDocumentoPDF(InfoDocumento infoDocumento, CalidadDocumento calidadDocumento) {
        this.calidadDocumento = calidadDocumento;
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocumento = infoDocumento;
        iniciar(null, false);
    }

    public VerDocumentoPDF(com.develcom.elimina.InfoDocumento infoDocumento, EliminaDocumento eliminaDocumento) {
        this.eliminaDocumento = eliminaDocumento;
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocElimina = infoDocumento;
        iniciar(null, false);
    }

    private void iniciar(File file, boolean flag) {

        initComponents();

        fullScreem.setAction(acciones.fullScreenAction);
        acciones.fullScreenAction.setEnabled(true);

        panelPagina = new PagePanel();
        panelPagina.addKeyListener(acciones);

        if (infoDocumento != null) {
            nombreDocumento = infoDocumento.getTipoDocumento().trim();
        } else {
            nombreDocumento = infoDocElimina.getTipoDocumento().trim();
        }
//        zommIn.setVisible(false);
//        zommOut.setVisible(false);

        try {
            List<Perfil> perfiles = new IniciaSesion().buscarLibCatPerfil(ManejoSesion.getLogin(), Constantes.IMPRIMIR);
            String cat = ManejoSesion.getExpediente().getCategoria();

            jbImprimir.setEnabled(false);
            for (Perfil perfil : perfiles) {
                String cate = perfil.getCategoria().getCategoria();
                if (cate.equalsIgnoreCase(cat)) {
                    jbImprimir.setEnabled(true);
                    break;
                }
            }

        } catch (SOAPException ex) {
            traza.trace("problemas al buscar las libreria y categoria segun el perfil", Level.ERROR, ex);
        }

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                jbRechazar.setVisible(true);
                jbAprobar.setVisible(true);
                btnAbrir.setVisible(false);
                cboVersion.setVisible(false);
            } else {
                jbRechazar.setVisible(false);
                jbAprobar.setVisible(false);
            }
        } else if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
            jbRechazar.setVisible(true);
            jbAprobar.setVisible(false);
            jbGuardar.setVisible(false);
            jbImprimir.setVisible(false);
            cboVersion.setVisible(false);
            btnAbrir.setVisible(false);
            btnDatosAdicionales.setVisible(false);
        } else if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
        } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
        }

        traza.trace("nombre del documento " + nombreDocumento, Level.INFO);
        traza.trace("accion " + accion, Level.INFO);

        if (Constantes.ACCION.equalsIgnoreCase("GUARDAR")) {
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
            btnAbrir.setVisible(false);
            cboVersion.setVisible(false);
            if (infoDocumento.isTipoDocDatoAdicional()) {
                btnDatosAdicionales.setVisible(true);
            } else {
                btnDatosAdicionales.setVisible(false);
            }
        } else if (infoDocumento != null) {
            if (infoDocumento.isTipoDocDatoAdicional()) {
                btnDatosAdicionales.setVisible(true);
            } else {
                btnDatosAdicionales.setVisible(false);
            }
        } //            else{
        //                if(infoDocElimina.isTipoDocDatoAdicional()){
        //                    btnDatosAdicionales.setVisible(true);
        //                } else {
        //                    btnDatosAdicionales.setVisible(false);
        //                }
        //            }
        jlFechaVencimiento.setVisible(false);

        
        split.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new ThumbAction(this));
        split.setOneTouchExpandable(true);
        thumbs = new ThumbPanel(null);
        thumbsScroll = new JScrollPane(thumbs, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        pageScroll = new JScrollPane(panelPagina, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //pageScroll = new JScrollPane(panelPagina);
        pageScroll.getVerticalScrollBar().setUnitIncrement(100);
        pageScroll.getHorizontalScrollBar().setUnitIncrement(100);

        //panel.add(panelPagina);
//        pageScroll.setAutoscrolls(true);
//        pageScroll.getVerticalScrollBar().setUnitIncrement(100);
//        pageScroll.getHorizontalScrollBar().setUnitIncrement(100);
        split.setDividerLocation((int) thumbs.getPreferredSize().width + (int) thumbsScroll.getVerticalScrollBar().getWidth() + 4);
        split.setRightComponent(pageScroll);
        split.setLeftComponent(thumbsScroll);

        CentraVentanas.centrar(this, Principal.desktop);

        crearObjetos();

        if (flag) {
            guardar(file);
        } else {
            mostrar();
        }
    }

    private void guardar(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        jbGuardar.setVisible(true);

        if (infoDocumento.getFechaVencimiento() != null) {
            XMLGregorianCalendar xmlCalendar = infoDocumento.getFechaVencimiento();
            GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
            jlFechaVencimiento.setText(sdf.format(rechaVencimiento.getTime()));
        }
        openFile(file);
        setVisible(true);
    }

    private void mostrar() {
        final MostrarProceso proceso = new MostrarProceso("Buscando y Decodificando el documento");
        proceso.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
                    mostrarDocumentos();
                } else {
                    visualizar();
                }
                traza.trace("mostrando la ventana", Level.INFO);
                proceso.detener();
            }
        }).start();
    }

    private void visualizar() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String ruta = "", archivo = "";//, buffer;
//        BufferedReader leer;
//        PrintWriter escribir;

        OutputStream escribiendoPDF;
        //byte[] buffer;
        Bufer bufferPDF;

        int contDocAprob = 0, contDocPend = 0;

        idInfoDocumento = infoDocumento.getIdInfoDocumento();
        int idDoc = infoDocumento.getIdDocumento();
        int version = infoDocumento.getVersion();
        int numDoc = infoDocumento.getNumeroDocumento();
        int idCat = ManejoSesion.getExpediente().getIdCategoria();
        int idSubCat = ManejoSesion.getExpediente().getIdSubCategoria();
        String idExpediente = ManejoSesion.getExpediente().getIdExpediente();
        traza.trace("buscando el documento idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);

        try {
            System.gc();
            infoDocumentos = new GestionArchivos().buscarImagenDocumentos(idInfoDocumento, idDoc, idCat, idSubCat, version, numDoc, idExpediente);

            if (infoDocumentos.size() > 0) {

                File fileCod = new File(toolsFile.getArchivoCodificado());
                if (toolsFile.getDirTemporal().exists()) {

                    File[] files = toolsFile.getDirTemporal().listFiles();
                    for (File f : files) {
                        if (f.delete()) {
                            traza.trace("eliminado archivo " + f.getName(), Level.INFO);
                        } else {
                            traza.trace("problemas al eliminar el archivo " + f.getName(), Level.WARN);
                            f.deleteOnExit();
                        }
                    }
                }

                for (InfoDocumento id : infoDocumentos) {

                    if (vers) {
                        if (id.getVersion() != versionMostrar) {
                            continue;
                        }
                    }

                    if (version > 0) {
                        if (id.getVersion() == version) {
                            if (id.getFechaVencimiento() != null) {

                                XMLGregorianCalendar xmlCalendar = id.getFechaVencimiento();
                                GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
                                jlFechaVencimiento.setText(sdf.format(rechaVencimiento.getTime()));
                                jlFechaVencimiento.setVisible(true);

                                ruta = id.getRutaArchivo();
                                archivo = id.getNombreArchivo();
                                version = id.getVersion();
                                break;

                            } else {
                                ruta = id.getRutaArchivo();
                                archivo = id.getNombreArchivo();
                                version = id.getVersion();
                                break;
                            }
                        }
                    } else if (id.getFechaVencimiento() != null) {

                        XMLGregorianCalendar xmlCalendar = id.getFechaVencimiento();
                        GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
                        jlFechaVencimiento.setText(sdf.format(rechaVencimiento.getTime()));
                        jlFechaVencimiento.setVisible(true);

                        ruta = id.getRutaArchivo();
                        archivo = id.getNombreArchivo();
                        version = id.getVersion();
                        break;

                    } else {
                        ruta = id.getRutaArchivo();
                        archivo = id.getNombreArchivo();
                        version = id.getVersion();
                        break;
                    }

                }

                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + version, Level.INFO);

                //jlMensaje4.setText("Tipo de Documento: "+infoDocumento.getTipoDocumento()+" \"ULTIMA VERSION: "+version+"\"");
                if (archivo == null) {
                    throw new DW4JDesktopExcepcion("Falta información del documento");
                }
                //busca el archivo fisico del tipo de documento
                bufferPDF = new GestionArchivos().buscandoArchivo(ruta, archivo);

                if (!bufferPDF.isExiste()) {
                    throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                }
                escribiendoPDF = new FileOutputStream(fileCod);
                escribiendoPDF.write(bufferPDF.getBufer());
                escribiendoPDF.flush();
                escribiendoPDF.close();

                filePDF = new File("documento" + Constantes.CONTADOR++ + "." + infoDocumento.getFormato());

                toolsFile.decodificar(filePDF.getName());

                openFile(new File(toolsFile.getTempPath() + filePDF));
//                toolsFile.clean();

                for (InfoDocumento infoDoc : infoDocumentos) {

                    if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                        if (infoDoc.getEstatusDocumento().equalsIgnoreCase("aprobado")) {
                            cboVersion.addItem(infoDoc.getVersion());
                            contDocAprob++;
                        }

                    } else if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {

                        if (infoDoc.getEstatusDocumento().equalsIgnoreCase("pendiente")) {
                            cboVersion.addItem(infoDoc.getVersion());
                            contDocPend++;
                        }

                    } else if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {
                        cboVersion.addItem(infoDoc.getVersion());
                    }
                }

                if (infoDocumentos.size() == 1 || contDocAprob == 1 || contDocPend == 1) {
                    cboVersion.setEnabled(false);
                    btnAbrir.setEnabled(false);
                }
                if (vers) {
                    cboVersion.setSelectedItem(versionMostrar);
                    cboVersion.setEnabled(true);
                    btnAbrir.setEnabled(true);
                }
                setVisible(true);

            } else {
                throw new IOException("Documento o Archivo no encontrado");
            }
        } catch (DW4JDesktopExcepcion ex) {
            traza.trace("Error general al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
//                ResultadoExpediente re = new ResultadoExpediente();
//                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }

        } catch (SOAPException ex) {
            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
//                ResultadoExpediente re = new ResultadoExpediente();
//                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }
        } catch (FileNotFoundException ex) {

            traza.trace("Error al buscar el archivo", Level.ERROR, ex);
            this.dispose();
            JOptionPane.showMessageDialog(this, "Error al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
//                ResultadoExpediente re = new ResultadoExpediente();
//                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }
        } catch (SOAPFaultException e) {
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
//                ResultadoExpediente re = new ResultadoExpediente();
//                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }
        } catch (Exception ex) {
            traza.trace("Error general al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
//                ResultadoExpediente re = new ResultadoExpediente();
//                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }
        }

    }

    private void mostrarDocumentos() {

        String ruta = "", archivo = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Bufer buffer;
        FileOutputStream escribiendo;
        com.develcom.elimina.InfoDocumento infoDocBuscado;

        try {

            idInfoDocumento = infoDocElimina.getIdInfoDocumento();
            int idDoc = infoDocElimina.getIdDocumento();
            int version = infoDocElimina.getVersion();
            int numDoc = infoDocElimina.getNumeroDocumento();
            int idCat = ManejoSesion.getExpediente().getIdCategoria();
            int idSubCat = ManejoSesion.getExpediente().getIdSubCategoria();
            String idExpediente = ManejoSesion.getExpediente().getIdExpediente();
            traza.trace("buscando el documento idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);

            infoDocBuscado = new QuitaDocumento().buscarDatosDoc(idInfoDocumento, idExpediente, version, numDoc);
//            infoDocBuscado = new QuitaDocumento().buscarDatosDoc(idInfoDocumento, idDoc, idCat, idSubCat, idExpediente, version);

            if (infoDocBuscado != null) {

                File fileCod = new File(toolsFile.getArchivoCodificado());
                if (toolsFile.getDirTemporal().exists()) {
                    File[] files = toolsFile.getDirTemporal().listFiles();
                    for (File f : files) {
                        if (f.delete()) {
                            traza.trace("eliminado archivo " + f.getName(), Level.INFO);
                        } else {
                            traza.trace("problemas al eliminar el archivo " + f.getName(), Level.WARN);
                        }
                    }
                }

                try {
                    XMLGregorianCalendar xmlCalendar = infoDocBuscado.getFechaVencimiento();
                    GregorianCalendar fechaVencimiento = xmlCalendar.toGregorianCalendar();

                    jlFechaVencimiento.setText(sdf.format(fechaVencimiento.getTime()));
                    jlFechaVencimiento.setVisible(true);
                } catch (NullPointerException e) {
                }

                ruta = infoDocBuscado.getRutaArchivo();
                archivo = infoDocBuscado.getNombreArchivo();
                version = infoDocBuscado.getVersion();

                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + version, Level.INFO);

                if (archivo == null) {
                    throw new DW4JDesktopExcepcion("Falta información del documento");
                }

                //busca el archivo fisico del tipo de documento
                buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);

                if (!buffer.isExiste()) {
                    throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                }
                escribiendo = new FileOutputStream(fileCod);
                //escribiendo.write(buffer);
                escribiendo.write(buffer.getBufer());
                escribiendo.flush();
                escribiendo.close();

                filePDF = new File("documento" + Constantes.CONTADOR++ + "." + infoDocElimina.getFormato());

                toolsFile.decodificar(filePDF.getName());

                openFile(new File(toolsFile.getTempPath() + filePDF));

                setVisible(true);
            } else {
                throw new IOException("Documento o Archivo no encontrado");
            }

        } catch (SOAPException ex) {
            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

        } catch (FileNotFoundException ex) {

            traza.trace("Error al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

        } catch (SOAPFaultException e) {
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

        } catch (DW4JDesktopExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            traza.trace("Error general al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Crea los indices dinamicos en el formulario
     */
    private void crearObjetos() {
        try {

            traza.trace("creando indices dinamicos", Level.INFO);
            GridBagConstraints constraints = new GridBagConstraints();
            CreaObjetosDinamicos uv = new CreaObjetosDinamicos(this);

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;

//            panelIndices.setLayout(new FlowLayout(FlowLayout.LEFT));
//            panelIndices.add(uv.mostrarIndices(expediente)); 
            uv.mostrarIndices(expediente);
            setTitle(uv.crearTituloExpediente());

        } catch (Exception e) {
            traza.trace("error al crear los indices dinamicos", Level.ERROR, e);
        }

    }

    /**
     * <p>
     * Open a specific pdf file. Creates a DocumentInfo from the file, and opens
     * that.</p>
     *
     * <p>
     * <b>Note:</b> Mapping the file locks the file until the PDFFile is
     * closed.</p>
     *
     * @param file the file to open
     * @throws IOException
     */
    private void openFile(File file) {
        RandomAccessFile raf;
        FileChannel channel;
        ByteBuffer buf;
        PDFFile newfile;

        traza.trace("abriendo el archivo " + file.getName(), Level.INFO);
        try {
            
            raf = new RandomAccessFile(file, "r");
            channel = raf.getChannel();
            
            buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            traza.trace("solo lectura " + buf.isReadOnly(), Level.INFO);
            
            newfile = new PDFFile(buf);
            archivoPDF = newfile;

            thumbs = new ThumbPanel(archivoPDF);
            thumbs.addPageChangeListener(acciones);
            thumbsScroll.getViewport().setView(thumbs);
            thumbsScroll.getViewport().setBackground(Color.gray);

            cantidadPaginas = archivoPDF.getNumPages();

            // display page 1.
            forceGotoPage(0);

        } catch (FileNotFoundException ex) {
            traza.trace("archivo no encontrado ", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("error al leer el archivo ", Level.ERROR, ex);
        }
    }

//    /**
//     * open the ByteBuffer data as a PDFFile and start to process it.
//     *
//     * @param buf
//     * @param path
//     */
//    private void openPDFByteBuffer(ByteBuffer buf, String path, String name) {
//
////        JDialog dialog = new JDialog(new JFrame(), "Outline");
////        OutlineNode outline = null;
//        traza.trace("creando el archivo " + name + " con el bufer " + buf, Level.INFO);
//        traza.trace("archivoPDF " + archivoPDF, Level.INFO);
//        // create a PDFFile from the data
//        //PDFFile newfile = null;
//        try {
//            // set up our document
//            archivoPDF = new PDFFile(buf);
//
//            //newfile = new PDFFile(buf);
//        } catch (IOException ioe) {
//
//            traza.trace("no parece ser un archivo pdf", Level.ERROR, ioe);
//
//            JOptionPane.showMessageDialog(split, "no parece ser un archivo pdf \n" + ioe.getMessage(), "Error al abrir el archivo ", JOptionPane.ERROR_MESSAGE);
//
//            return;
//        }
//        traza.trace("archivoPDF " + archivoPDF, Level.INFO);
//
//        // Now that we're reasonably sure this document is real, close the
//        // old one.
//        //doClose();
//        // set up our document
//        //archivoPDF = newfile;;
//        // set up the thumbnails
//        //if (doThumb) {
//        thumbs = new ThumbPanel(archivoPDF);
//        thumbs.addPageChangeListener(acciones);
//        thumbsScroll.getViewport().setView(thumbs);
//        thumbsScroll.getViewport().setBackground(Color.gray);
//        //}
//
//        //setEnabling();
//        //panelPagina.showPage(null);
//        cantidadPaginas = archivoPDF.getNumPages();
//        traza.trace("cantidad de paginas del archivo pdf " + cantidadPaginas, Level.INFO);
//        // display page 1.
//        forceGotoPage(0);
//
//        // if the PDF has an outline, display it.
////        try {
////            outline = archivoPDF.getOutline();
////        } catch (IOException ioe) {
////            traza.trace("problema con el objecto OutlineNode", Level.ERROR, ioe);
////        }
////
////        traza.trace("outline " + outline, Level.INFO);
////        if (outline != null) {
////            if (outline.getChildCount() > 0) {
////                //olf = new JDialog(new JFrame(), "Outline");
////                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
////                dialog.setLocation(this.getLocation());
////                JTree jt = new JTree(outline);
////                jt.setRootVisible(false);
////                jt.addTreeSelectionListener(acciones);
////                JScrollPane jsp = new JScrollPane(jt);
////                dialog.getContentPane().add(jsp);
////                dialog.pack();
////                dialog.setVisible(true);
////            } else {
////                if (dialog != null) {
////                    dialog.setVisible(false);
////                    dialog = null;
////                }
////            }
////        }
//    }
    /**
     * Changes the displayed page.
     *
     * @param pagenum the page to display
     */
    public void forceGotoPage(int pagenum) {

        int proximaPAgina;

        if (pagenum <= 0) {
            pagenum = 0;
        } else if (pagenum >= archivoPDF.getNumPages()) {
            pagenum = archivoPDF.getNumPages() - 1;
            //System.out.println("pagenum "+pagenum);
        }

        paginaActual = pagenum;
        proximaPAgina = paginaActual + 1;
        traza.trace("pagina actual " + paginaActual, Level.INFO);

        // update the page text field
        pagina.setText(String.valueOf(proximaPAgina));

        traza.trace("moviendose a la pagina " + (proximaPAgina), Level.INFO);

        PDFPage pg = archivoPDF.getPage(pagenum + 1);
        if (acciones.getFspp() != null) {
            acciones.getFspp().showPage(pg);
            acciones.getFspp().requestFocus();
        } else {
            panelPagina.showPage(pg);
            panelPagina.requestFocus();
        }

        traza.trace("actual pagina " + panelPagina.getPage(), Level.INFO);

        thumbs.pageShown(pagenum);

        //split.updateUI();
        // stop any previous page prepper, and start a new one
        traza.trace("pagePrep " + pagePrep, Level.INFO);
        if (pagePrep != null) {
            pagePrep.quit();
        }
        pagePrep = new PreparaPagina(pagenum, this);
        //pagePrep.run();
        pagePrep.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barra = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        primero = new javax.swing.JButton();
        previo = new javax.swing.JButton();
        pagina = new javax.swing.JTextField();
        siguiente = new javax.swing.JButton();
        ultimo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbImprimir = new javax.swing.JButton();
        jbGuardar = new javax.swing.JButton();
        cboVersion = new javax.swing.JComboBox();
        btnAbrir = new javax.swing.JButton();
        zommOut = new javax.swing.JButton();
        zommIn = new javax.swing.JButton();
        fullScreem = new javax.swing.JToggleButton();
        jlFechaVencimiento = new javax.swing.JLabel();
        jbAprobar = new javax.swing.JButton();
        jbRechazar = new javax.swing.JButton();
        split = new javax.swing.JSplitPane();
        cancelar = new javax.swing.JButton();
        btnDatosAdicionales = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        barra.setFloatable(false);

        primero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/pdf/first.gif"))); // NOI18N
        primero.setToolTipText("Primero");
        primero.setFocusable(false);
        primero.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        primero.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        primero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primeroActionPerformed(evt);
            }
        });

        previo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/pdf/prev.gif"))); // NOI18N
        previo.setToolTipText("Anterior");
        previo.setFocusable(false);
        previo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        previo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previoActionPerformed(evt);
            }
        });

        siguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/pdf/next.gif"))); // NOI18N
        siguiente.setToolTipText("Siguiente");
        siguiente.setFocusable(false);
        siguiente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        siguiente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siguienteActionPerformed(evt);
            }
        });

        ultimo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/pdf/last.gif"))); // NOI18N
        ultimo.setToolTipText("Ultimo");
        ultimo.setFocusable(false);
        ultimo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ultimo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ultimoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(primero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pagina, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(siguiente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ultimo))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ultimo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pagina, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(primero, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(previo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(siguiente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        barra.add(jPanel1);

        jbImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Printer.png"))); // NOI18N
        jbImprimir.setToolTipText("Imprimir");
        jbImprimir.setFocusable(false);
        jbImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbImprimirActionPerformed(evt);
            }
        });

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/save1.png"))); // NOI18N
        jbGuardar.setToolTipText("Guardar el Documento");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        cboVersion.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        cboVersion.setToolTipText("Seleccione la versión del documento que desea ver");
        cboVersion.setDoubleBuffered(true);
        cboVersion.setName(""); // NOI18N
        cboVersion.setPreferredSize(new java.awt.Dimension(122, 25));
        cboVersion.setVerifyInputWhenFocusTarget(false);

        btnAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Open24.gif"))); // NOI18N
        btnAbrir.setToolTipText("Abrir");
        btnAbrir.setFocusable(false);
        btnAbrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbrir.setPreferredSize(new java.awt.Dimension(59, 25));
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });

        zommOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomOut24.gif"))); // NOI18N
        zommOut.setToolTipText("Disminuir Imagen");
        zommOut.setPreferredSize(new java.awt.Dimension(50, 40));
        zommOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zommOutActionPerformed(evt);
            }
        });

        zommIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomIn24.gif"))); // NOI18N
        zommIn.setToolTipText("Aumentar Imagen");
        zommIn.setPreferredSize(new java.awt.Dimension(50, 40));
        zommIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zommInActionPerformed(evt);
            }
        });

        fullScreem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/page_peppermint.png"))); // NOI18N
        fullScreem.setToolTipText("Pantalla Completa");
        fullScreem.setPreferredSize(new java.awt.Dimension(50, 19));

        jlFechaVencimiento.setText("fecha Vencimiento");

        jbAprobar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/page_tick.gif"))); // NOI18N
        jbAprobar.setToolTipText("Aprobar");
        jbAprobar.setFocusable(false);
        jbAprobar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAprobar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAprobarActionPerformed(evt);
            }
        });

        jbRechazar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/page_text_delete.gif"))); // NOI18N
        jbRechazar.setToolTipText("Rechazar");
        jbRechazar.setFocusable(false);
        jbRechazar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbRechazar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbRechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRechazarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(zommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullScreem, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jlFechaVencimiento)
                .addGap(32, 32, 32)
                .addComponent(jbAprobar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(zommOut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(zommIn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(fullScreem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jbRechazar, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jbAprobar, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(cboVersion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jbImprimir)
                .addComponent(jbGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
            .addComponent(jlFechaVencimiento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        barra.add(jPanel2);

        split.setAutoscrolls(true);

        cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Knob Cancel1.png"))); // NOI18N
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        btnDatosAdicionales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/dato1.png"))); // NOI18N
        btnDatosAdicionales.setToolTipText("Ver Datos los Adicionales");
        btnDatosAdicionales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosAdicionalesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(split)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDatosAdicionales, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDatosAdicionales, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(split, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void primeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primeroActionPerformed
        acciones.doFirst();
    }//GEN-LAST:event_primeroActionPerformed

    private void previoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previoActionPerformed
        acciones.doPrev();
    }//GEN-LAST:event_previoActionPerformed

    private void siguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siguienteActionPerformed
        acciones.doNext();
    }//GEN-LAST:event_siguienteActionPerformed

    private void ultimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ultimoActionPerformed
        acciones.doLast();
    }//GEN-LAST:event_ultimoActionPerformed

    private void jbImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImprimirActionPerformed
        acciones.doPrint();
    }//GEN-LAST:event_jbImprimirActionPerformed

    private void zommOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zommOutActionPerformed
        acciones.doFitInWindow();
    }//GEN-LAST:event_zommOutActionPerformed

    private void zommInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zommInActionPerformed
        acciones.doZoomTool();
    }//GEN-LAST:event_zommInActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed

        if (thumbs != null) {
            thumbs.stop();
            thumbs.removeAll();
            thumbs = null;
        }

        if (panelPagina != null) {
            panelPagina.removeAll();
            panelPagina = null;
        }

        if (thumbsScroll != null) {
            thumbsScroll.removeAll();
            thumbsScroll = null;
        }

        if (pageScroll != null) {
            pageScroll.removeAll();
            pageScroll = null;
        }

        if (pagePrep != null) {
            pagePrep.quit();
            pagePrep.interrupt();
            pagePrep = null;
        }

        acciones.setFullScreenMode(false, false);

        acciones = null;
        archivoPDF = null;
        filePDF = null;

        this.dispose();
        Principal.desktop.remove(this);
        if (!vers) {
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR") || Constantes.ACCION.equalsIgnoreCase("GUARDAR")) {
                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
                Principal.desktop.repaint();
            }
        }
    }//GEN-LAST:event_cancelarActionPerformed

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed

        if (flagSave) {

            String acc = accion;
            acc = acc.toLowerCase();
            char[] cs = acc.toCharArray();
            char ch = cs[0];
            cs[0] = Character.toUpperCase(ch);
            acc = String.valueOf(cs);

            int n = JOptionPane.showOptionDialog(this,
                    "Desea " + acc + " \n" + expediente.getTipoDocumento() + " \nen el expediente " + expediente.getIdExpediente(),
                    "¿?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"SI", "NO"}, "NO");

            if (n == JOptionPane.YES_OPTION) {
                enviar();
            }

        } else {
            traza.trace("archivo  a guardar localmente " + filePDF, Level.INFO);
            toolsFile.guardarArchivoPDF(new File(toolsFile.getTempPath() + filePDF));
        }


    }//GEN-LAST:event_jbGuardarActionPerformed

    private void enviar() {
        final MostrarProceso proceso = new MostrarProceso("Guardando el documento");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (enviarArchivo()) {

//                    Principal.desktop.removeAll();
//                    DigitalizaDocumento dd = new DigitalizaDocumento();
//                    dd.find();
//                    //CreaEditaDocumento cee = new CreaEditaDocumento();
//                    Principal.desktop.add(dd);
                }
                traza.trace("mostrando la ventana", Level.INFO);

                proceso.detener();
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
//        ToolsTiff toolsTiff = new ToolsTiff();
        String dirTipoDoc, dirCat, dirSubCat, ruta, lib, exito;
        Matcher mat;
        Pattern pat;
        int idDoc;
//        String archivoSerializado;

        pat = Pattern.compile(" ");

        InputStream leyendo;
        byte[] bu;
        Bufer buffer = new Bufer();

        try {

            traza.trace("guardando el documento", Level.INFO);

            lib = expediente.getLibreria();
            dirCat = expediente.getCategoria();
            dirSubCat = expediente.getSubCategoria();
            dirTipoDoc = expediente.getTipoDocumento();
            idDoc = expediente.getIdTipoDocumento();

            mat = pat.matcher(lib);
            lib = mat.replaceAll("");

            mat = pat.matcher(dirCat);
            dirCat = mat.replaceAll("");

            mat = pat.matcher(dirSubCat);
            dirSubCat = mat.replaceAll("");

            mat = pat.matcher(dirTipoDoc);
            dirTipoDoc = mat.replaceAll("");

            lib = EncriptaDesencripta.encripta(lib);
            dirCat = EncriptaDesencripta.encripta(dirCat);
            dirSubCat = EncriptaDesencripta.encripta(dirSubCat);
            dirTipoDoc = EncriptaDesencripta.encripta(dirTipoDoc);
            ruta = lib + "/" + dirCat + "/" + dirSubCat;

            leyendo = new FileInputStream(toolsFile.getArchivoCodificado());
            traza.trace("posible tamaño del archivo a transferir " + (leyendo.available() / 1024000), Level.INFO);
            bu = new byte[leyendo.available()];
            //bu = new byte[8192];
            leyendo.read(bu);
            leyendo.close();
            buffer.setBufer(bu);

            infoDocumento.setNombreArchivo(dirTipoDoc);
            infoDocumento.setRutaArchivo(ruta);
            infoDocumento.setCantPaginas(cantidadPaginas);
            exito = new GestionArchivos().almacenaArchivo(buffer, accion, infoDocumento, expediente.getIdExpediente(), ManejoSesion.getLogin());

            if (exito.equalsIgnoreCase("exito")) {
                traza.trace("se guardo el documento con exito", Level.INFO);
                JOptionPane.showMessageDialog(this, "Documento generado satisfactoriamente", "Información", JOptionPane.INFORMATION_MESSAGE);
                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                dd.find();
                //CreaEditaDocumento cee = new CreaEditaDocumento();
                Principal.desktop.add(dd);
                setVisible(false);
                dispose();
                resp = true;
            } else {
                traza.trace("problemas al guardar el archivo objecto \n respuesta del webservices " + exito, Level.INFO);
                JOptionPane.showMessageDialog(this, "Problemas al guardar el documento, contacte al administrador\n" + exito, "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
//                cancelarActionPerformed(null);
            }

        } catch (FileNotFoundException ex) {
            traza.trace("Error archivo: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error archivo: \n" + ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            traza.trace("Error lectura archivo: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error lectura archivo: \n" + ex.getMessage(), "Error", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            traza.trace("Error general: ", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error General", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        }
        return resp;
    }

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed

        versionSeleccionada = Integer.parseInt(cboVersion.getSelectedItem().toString());

        version(versionSeleccionada);
}//GEN-LAST:event_btnAbrirActionPerformed

    private void version(int version) {
        final MostrarProceso proceso = new MostrarProceso("Buscando la version " + version + " del documento");
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getVersion(versionSeleccionada);
                traza.trace("mostrando la ventana", Level.INFO);

                proceso.detener();
            }
        }).start();

    }

    /**
     * Busca la version seleccionada del documento
     *
     * @param version La version seleccionada
     */
    private void getVersion(int version) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ruta, archivo, formato;
        //String buffer;
        //byte[] buffer;
        Bufer buffer;
        BufferedReader leer;
        PrintWriter escribir;
        OutputStream escribiendo;

        traza.trace("version a buscar " + version, Level.INFO);

        try {
            for (InfoDocumento infoDoc : infoDocumentos) {
                if (infoDoc.getVersion() == version) {

                    formato = infoDoc.getFormato();

                    thumbs = new ThumbPanel(null);

                    thumbsScroll.getViewport().setView(thumbs);

                    thumbsScroll.updateUI();

                    if (toolsFile.getDirTemporal().exists()) {
                        File[] files = toolsFile.getDirTemporal().listFiles();
                        for (File f : files) {
                            try {
                                boolean del = f.delete();
                                if (del) {
                                    traza.trace("eliminado archivo " + f.getName(), Level.INFO);
                                } else {
                                    traza.trace("problemas al eliminar el archivo " + f.getName(), Level.WARN);
                                }
                            } catch (Exception e) {
                                traza.trace("problemas al eliminar el archivo " + f.getName(), Level.ERROR, e);
                            }
                        }
                    }

                    if (formato.equalsIgnoreCase("pdf")) {

                        if (infoDoc.getFechaVencimiento() != null) {

                            XMLGregorianCalendar xmlCalendar = infoDoc.getFechaVencimiento();
                            GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
                            jlFechaVencimiento.setText(sdf.format(rechaVencimiento.getTime()));
                            jlFechaVencimiento.setVisible(true);

                            ruta = infoDoc.getRutaArchivo();
                            archivo = infoDoc.getNombreArchivo();
                            version = infoDoc.getVersion();

                        } else {
                            ruta = infoDoc.getRutaArchivo();
                            archivo = infoDoc.getNombreArchivo();
                            version = infoDoc.getVersion();
                        }

                        if (archivo == null) {
                            throw new DW4JDesktopExcepcion("Falta información del documento");
                        }

                        buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);
                        File fileCod = new File(toolsFile.getArchivoCodificado());

                        if (buffer == null) {
                            throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                        }
                        escribiendo = new FileOutputStream(fileCod);
                        escribiendo.write(buffer.getBufer());
                        escribiendo.flush();
                        escribiendo.close();

                        filePDF = new File("documento" + Constantes.CONTADOR++ + "." + formato);

                        toolsFile.decodificar(filePDF.getName());

                        openFile(new File(toolsFile.getTempPath() + filePDF));
                        break;
                        //openFile(new File(toolsFile.getTempPath() + "documento.pdf"));
                    } else if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {

                        VerImagenes vi = new VerImagenes("", infoDoc, null, false, version, true);
                        Principal.desktop.add(vi);
                        vi.toFront();
                        dispose();
                        break;

                    } else if (formato.equalsIgnoreCase("tif") || formato.equalsIgnoreCase("tiff")) {

                        VerDocumento vd = new VerDocumento(infoDoc, true, version);
                        Principal.desktop.add(vd);
                        vd.toFront();
                        dispose();
                        break;
                    }
                }
            }
        } catch (DW4JDesktopExcepcion ex) {
            traza.trace("Error general al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
                ResultadoExpediente re = new ResultadoExpediente();
                Principal.desktop.add(re);
            }
        } catch (SOAPException ex) {
            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
            this.dispose();
            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                ResultadoExpediente re = new ResultadoExpediente();
                Principal.desktop.add(re);
                Principal.desktop.repaint();
            }
        } catch (IOException ex) {
            traza.trace("Error al buscar la version del archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error al buscar la version del archivo\n" + ex.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
        } catch (SOAPFaultException e) {
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    private void jbAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAprobarActionPerformed

        String tipoDoc = infoDocumento.getTipoDocumento();
        boolean aprobado;
        String usuario = ManejoSesion.getLogin();

        try {
            int n = JOptionPane.showOptionDialog(this,
                    "Seguro que desea aprobar el documento " + tipoDoc + " version " + versionSeleccionada + " \n(" + idInfoDocumento + ")",
                    "¿?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"SI", "NO"}, "NO");

            if (n == JOptionPane.YES_OPTION) {

                if (idInfoDocumento != 0) {
                    aprobado = new ve.com.develcom.aprueba.CalidadDocumento().aprobarDoc(idInfoDocumento, usuario);

                    if (aprobado) {
                        JOptionPane.showMessageDialog(this, "Documento aprobado con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);

                        DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) CalidadDocumento.getSeleccionado();
                        DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) verificador.getRoot();
                        DefaultMutableTreeNode padre = (DefaultMutableTreeNode) verificador.getParent();
                        MutableTreeNode node = verificador;
                        traza.trace("removiendo la hoja " + node + " del arbol", Level.INFO);
                        padre.remove(node);
                        try {
                            TreeNode tn = padre.getFirstChild();
                        } catch (NoSuchElementException e) {
                            MutableTreeNode nodePadre = padre;
                            raiz.remove(nodePadre);

                        }

                        this.dispose();

                        if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
                            eliminaDocumento.getTree().updateUI();
                        } else {
                            calidadDocumento.getTree().updateUI();
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Problemas al aprobar el documento", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SOAPException ex) {
            traza.trace("error soapf en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }//GEN-LAST:event_jbAprobarActionPerformed

    private void jbRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRechazarActionPerformed

        boolean exito;
        RechazarDocumento rd;

        if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
            traza.trace("eliminando el tipo de documento " + infoDocElimina.getTipoDocumento() + " su id " + idInfoDocumento, Level.INFO);
            rd = new RechazarDocumento(infoDocElimina);
        } else {
            traza.trace("rechazando el tipo de documento " + infoDocumento.getTipoDocumento() + " su id " + idInfoDocumento, Level.INFO);
            rd = new RechazarDocumento(idInfoDocumento, infoDocumento.getTipoDocumento(), versionSeleccionada);
        }
        exito = rd.isResultado();

        traza.trace("exito rechazo " + exito, Level.INFO);
        if (exito) {
            DefaultMutableTreeNode verificador;
            if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
                verificador = EliminaDocumento.getSeleccionado();
            } else {
                verificador = CalidadDocumento.getSeleccionado();
            }
            DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) verificador.getRoot();
            DefaultMutableTreeNode padre = (DefaultMutableTreeNode) verificador.getParent();
            MutableTreeNode node = verificador;
            padre.remove(node);

            try {
                TreeNode tn = padre.getFirstChild();
            } catch (NoSuchElementException e) {
                MutableTreeNode nodePadre = padre;
                raiz.remove(nodePadre);

            }
            setVisible(false);
            dispose();

            if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
                eliminaDocumento.getTree().updateUI();
                eliminaDocumento.refrescar();
            } else {
                calidadDocumento.getTree().updateUI();
            }
        }

    }//GEN-LAST:event_jbRechazarActionPerformed

    private void btnDatosAdicionalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosAdicionalesActionPerformed

        if (infoDocumento.isTipoDocDatoAdicional()) {
            if (Constantes.ACCION.equalsIgnoreCase("GUARDAR")) {
                ManejoSesion.getExpediente().setIdTipoDocumento(infoDocumento.getIdDocumento());
                DatoAdicional dag = new DatoAdicional(infoDocumento.getLsDatosAdicionales());
            } else {
                btnDatosAdicionales.setVisible(true);
                ManejoSesion.getExpediente().setIdTipoDocumento(infoDocumento.getIdDocumento());
                DatoAdicional dag = new DatoAdicional(infoDocumento.getNumeroDocumento(), Integer.parseInt(cboVersion.getSelectedItem().toString()));
            }
        }
    }//GEN-LAST:event_btnDatosAdicionalesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barra;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnDatosAdicionales;
    private javax.swing.JButton cancelar;
    private javax.swing.JComboBox cboVersion;
    private javax.swing.JToggleButton fullScreem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbAprobar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JButton jbImprimir;
    private javax.swing.JButton jbRechazar;
    private javax.swing.JLabel jlFechaVencimiento;
    private javax.swing.JTextField pagina;
    private javax.swing.JButton previo;
    private javax.swing.JButton primero;
    private javax.swing.JButton siguiente;
    private javax.swing.JSplitPane split;
    private javax.swing.JButton ultimo;
    private javax.swing.JButton zommIn;
    private javax.swing.JButton zommOut;
    // End of variables declaration//GEN-END:variables

    public PagePanel getPanelPagina() {
        return panelPagina;
    }

    public void setPanelPagina(PagePanel panelPagina) {
        this.panelPagina = panelPagina;
    }

    public ThumbPanel getThumbs() {
        return thumbs;
    }

    public JScrollPane getThumbsScroll() {
        return thumbsScroll;
    }

    public JSplitPane getSplit() {
        return split;
    }

    public PDFFile getArchivoPDF() {
        return archivoPDF;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public JTextField getPagina() {
        return pagina;
    }

    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }

    public File getFilePDF() {
        return filePDF;
    }

    public JToggleButton getFullScreem() {
        return fullScreem;
    }

    public Acciones getAcciones() {
        return acciones;
    }
}
