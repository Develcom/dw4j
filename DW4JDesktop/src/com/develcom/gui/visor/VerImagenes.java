/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VerImagenes.java
 *
 * Created on 10-sep-2012, 13:03:52
 */
package com.develcom.gui.visor;

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
import com.develcom.tools.Constantes;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.cryto.EncriptaDesencripta;
import com.develcom.tools.trazas.Traza;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.elimina.QuitaDocumento;

/**
 *
 * @author develcom
 */
public class VerImagenes extends javax.swing.JInternalFrame implements Printable {

    private static final long serialVersionUID = 6205538173093983342L;
    /**
     * Total de paginas digitalizadas
     */
    private int cantidadPaginas = 0;
    /**
     * lista de indices dinamicos
     */
    private List<Indice> indices = new ArrayList<Indice>();
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
    private Traza traza = new Traza(VerImagenes.class);
    private int versionSeleccionada;
    private int versionMostrar;
    private Image imagen;
    private File archivoImagen;
    private ToolsFiles toolsFile = new ToolsFiles();
    private boolean flagSave;
    private boolean vers;
    private CalidadDocumento calidadDocumento;
    private EliminaDocumento eliminaDocumento;

    public VerImagenes(String accion, InfoDocumento infoDocumento, File file, boolean flagSave, int version, boolean vers) {
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocumento = infoDocumento;
        this.accion = accion;

        this.flagSave = flagSave;
        this.vers = vers;
        versionMostrar=version;
        archivoImagen = file;

        iniciar(file, flagSave);
    }

    public VerImagenes(InfoDocumento infoDocumento, CalidadDocumento calidadDocumento) {
        this.calidadDocumento = calidadDocumento;
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocumento = infoDocumento;

        iniciar(null, false);
    }

    public VerImagenes(com.develcom.elimina.InfoDocumento infoDocumento, EliminaDocumento eliminaDocumento) {
        this.eliminaDocumento = eliminaDocumento;
        this.expediente = ManejoSesion.getExpediente();
        this.infoDocElimina = infoDocumento;

        iniciar(null, false);
    }

    private void iniciar(File file, boolean flag) {

        initComponents();

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
//        nombreDocumento = infoDocumento.getTipoDocumento().trim();
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
            btnDatosAdicionales.setVisible(false);
            cboVersion.setVisible(false);
            btnAbrir.setVisible(false);
        } else if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
        } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
        }

//        traza.trace("nombre del documento " + nombreDocumento, Level.INFO);
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
        } else {

            if (infoDocumento != null) {
                if (infoDocumento.isTipoDocDatoAdicional()) {
                    btnDatosAdicionales.setVisible(true);
                } else {
                    btnDatosAdicionales.setVisible(false);
                }
            }
        }

        jlFechaVencimiento.setVisible(false);

        zommOut.setVisible(false);
        zommIn.setVisible(false);

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

        cboVersion.setVisible(false);
        btnAbrir.setVisible(false);

        imagen = null;
        imagen = new ImageIcon(file.toString()).getImage();
        cantidadPaginas = 1;
        panelImagen.setImagen(imagen);
        setVisible(true);
    }

    private void mostrar() {
        final MostrarProceso proceso = new MostrarProceso("Buscando el documento");
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                traza.trace("mostrando la ventana", Level.INFO);
                if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
                    mostrarDocumentos();
                } else {
                    visualizar();
                }
                proceso.detener();
            }
        }).start();

    }

    private void visualizar() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String ruta = "", archivo = "";//, buffer;
        BufferedReader leer;
        PrintWriter escribir;
        File archivoCodificadoJPG;
        OutputStream escribiendoJPG;
        //byte[] buffer;
        Bufer bufferJPG;

        int contDocAprob = 0, contDocPend = 0;

        if (!Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            jbGuardar.setVisible(true);
        }

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

                archivoCodificadoJPG = new File(toolsFile.getArchivoCodificado());
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
                    } else {
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
                }
                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + version, Level.INFO);

                //jlMensaje4.setText("Tipo de Documento: "+infoDocumento.getTipoDocumento()+" \"ULTIMA VERSION: "+version+"\"");
                if (archivo == null) {
                    throw new DW4JDesktopExcepcion("Falta información del documento");
                }
                //busca el archivo fisico del tipo de documento
                bufferJPG = new GestionArchivos().buscandoArchivo(ruta, archivo);

                if (!bufferJPG.isExiste()) {
                    throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                }

                escribiendoJPG = new FileOutputStream(archivoCodificadoJPG);
                escribiendoJPG.write(bufferJPG.getBufer());
                escribiendoJPG.flush();
                escribiendoJPG.close();

                if (archivoImagen != null) {
                    archivoImagen = null;
                }

                archivoImagen = new File("imagen" + Constantes.CONTADOR++ + "." + infoDocumento.getFormato());

                toolsFile.decodificar(archivoImagen.getName());

                imagen = null;

                imagen = new ImageIcon(toolsFile.getTempPath() + archivoImagen).getImage();

                panelImagen.removeAll();

                panelImagen.setImagen(imagen);

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
            }

        } catch (DW4JDesktopExcepcion e) {
            this.dispose();
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

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

        String ruta, archivo;
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

//            infoDocBuscado = new QuitaDocumento().buscarDatosDoc(idInfoDocumento, idDoc, idCat, idSubCat, idExpediente, version);
            infoDocBuscado = new QuitaDocumento().buscarDatosDoc(idInfoDocumento, idExpediente, version, numDoc);

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

                if (archivoImagen != null) {
                    archivoImagen = null;
                }

                archivoImagen = new File("imagen" + Constantes.CONTADOR++ + "." + infoDocElimina.getFormato());

                toolsFile.decodificar(archivoImagen.getName());

                imagen = null;

                imagen = new ImageIcon(toolsFile.getTempPath() + archivoImagen).getImage();

                panelImagen.removeAll();
                panelImagen.setImagen(imagen);

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
        List<Indice> lstIndices;
        CreaObjetosDinamicos uv = new CreaObjetosDinamicos(this);
        GridBagConstraints constraints = new GridBagConstraints();
        try {

            traza.trace("creando indices dinamicos", Level.INFO);

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelImagen = new com.develcom.tools.PanelImagen();
        jToolBar2 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jbAprobar = new javax.swing.JButton();
        jbRechazar = new javax.swing.JButton();
        jbImprimir = new javax.swing.JButton();
        jbGuardar = new javax.swing.JButton();
        cboVersion = new javax.swing.JComboBox();
        btnAbrir = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        btnDatosAdicionales = new javax.swing.JButton();
        jlFechaVencimiento = new javax.swing.JLabel();
        zommOut = new javax.swing.JButton();
        zommIn = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(224, 239, 255));

        javax.swing.GroupLayout panelImagenLayout = new javax.swing.GroupLayout(panelImagen);
        panelImagen.setLayout(panelImagenLayout);
        panelImagenLayout.setHorizontalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelImagenLayout.setVerticalGroup(
            panelImagenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 609, Short.MAX_VALUE)
        );

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

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

        jlFechaVencimiento.setText("jLabel1");

        zommOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomOut24.gif"))); // NOI18N
        zommOut.setMnemonic('-');
        zommOut.setToolTipText("Disminuir Imagen");
        zommOut.setPreferredSize(new java.awt.Dimension(50, 40));

        zommIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomIn24.gif"))); // NOI18N
        zommIn.setMnemonic('+');
        zommIn.setToolTipText("Aumentar Imagen");
        zommIn.setPreferredSize(new java.awt.Dimension(50, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cboVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(zommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jbAprobar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jlFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDatosAdicionales, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbAprobar)
                    .addComponent(jbRechazar)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jbGuardar, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                        .addComponent(jbImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDatosAdicionales)
                    .addComponent(jlFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar2.add(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelImagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed

        panelImagen.removeAll();

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

        this.dispose();
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

        //toolsFile.guardarArchivoJPGtoPDF(archivoImagen);
        String tipoDoc = infoDocumento.getTipoDocumento().trim();

        if (flagSave) {

            String acc = accion;
            acc = acc.toLowerCase();
            char[] cs = acc.toCharArray();
            char ch = cs[0];
            cs[0] = Character.toUpperCase(ch);
            acc = String.valueOf(cs);

            int n = JOptionPane.showOptionDialog(this,
                    "Desea " + acc + " \n" + expediente.getTipoDocumento().trim() + " \nen el expediente " + expediente.getIdExpediente(),
                    "¿?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"SI", "NO"}, "NO");

            if (n == JOptionPane.YES_OPTION) {
                enviar();
            }
//            if(enviarArchivo()){
//                this.dispose();
//                Principal.desktop.removeAll();
//                CreaEditaExpediente cee = new CreaEditaExpediente();
//                Principal.desktop.add(cee);
//            }

        } else {
            toolsFile.guardarArchivoJPGtoPDF(archivoImagen);
        }

    }//GEN-LAST:event_jbGuardarActionPerformed

    private void enviar() {
        final MostrarProceso proceso = new MostrarProceso("Guardando el documento");
        proceso.start();
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

                proceso.detener();
            }
        }).start();

        this.dispose();
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
        //ToolsTiff toolsTiff = new ToolsTiff();
        String dirTipoDoc, dirCat, dirSubCat, ruta, lib, exito;
        Matcher mat;
        Pattern pat;
        int idDoc;
//        String archivoSerializado;

        pat = Pattern.compile(" ");
        InputStream leyendo;
        byte[] bu;
        Bufer buffer = new Bufer();

        //zip.comprimirArchivos(new File(toolsTiff.getArchivoCodificado()));
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
            leyendo.read(bu);
            leyendo.close();

            buffer.setBufer(bu);

            infoDocumento.setNombreArchivo(dirTipoDoc);
            infoDocumento.setRutaArchivo(ruta);
            infoDocumento.setCantPaginas(cantidadPaginas);
            traza.trace("cantidad de paginas " + cantidadPaginas, Level.INFO);
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
                resp = true;
                dispose();
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

    private void jbImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImprimirActionPerformed

        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            //libro.append(this, job.defaultPage());
            //job.setPageable(libro);
            job.setPrintable(this);
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException ex) {
            traza.trace("error al imprimir", Level.ERROR, ex);
        }

//        final DocFlavor doc = DocFlavor.BYTE_ARRAY.JPEG;
//        //final DocFlavor doc = DocFlavor.BYTE_ARRAY.PDF;
//        //PrintService[] printerSupportXYZ = PrintServiceLookup.lookupPrintServices(doc, null);
//        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
//        
//        final String image = toolsFile.getTempPath()+archivoImagen.getName();
//        
//        try {
////            if (printerSupportXYZ.length > 0) {
////                PrintService printService = (PrintService) JOptionPane
////                        .showInputDialog(null, "Choose one", "Printer",
////                        JOptionPane.INFORMATION_MESSAGE,
////                        null, printerSupportXYZ, printerSupportXYZ[0]);
//                if (printer != null) {
//                    DocPrintJob pj = printer.createPrintJob();
//                    Doc printDoc = (new Doc() {
//
//                        InputStream is = new java.io.FileInputStream(image);
//
//                        @Override
//                        public java.io.Reader getReaderForText() {
//                            return null;
//                        }
//
//                        @Override
//                        public javax.print.attribute.DocAttributeSet getAttributes() {
//                            return null;
//                        }
//
//                        @Override
//                        public java.io.InputStream getStreamForBytes() {
//                            return is;
//                        }
//
//                        @Override
//                        public Object getPrintData() {
//                            return is;
//                        }
//
//                        @Override
//                        public DocFlavor getDocFlavor() {
//                            return doc;
//                        }
//                    });
//                    pj.print(printDoc, null);
//                    //job.print(printDoc, null);
//                } else {
//                    System.exit(-2);
//                }
////            } else {
////                System.out.println("No printers supported!");
////                System.exit(-1);
////            }
//        } catch (FileNotFoundException e) {
//            traza.trace("archivo no encontrado ", Level.INFO, e);
//            JOptionPane.showMessageDialog(this, "Error archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
//        } catch (PrintException e) {
//            traza.trace("problemas al imprimir ", Level.INFO, e);
//            JOptionPane.showMessageDialog(this, "Error al imprimir la foto", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
//            
//        }

}//GEN-LAST:event_jbImprimirActionPerformed

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;

        //Punto donde empezará a imprimir dentro la pagina (100, 50)
        //g2d.translate(pageFormat.getImageableX()+100, pageFormat.getImageableY()+50);
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        g2d.scale(0.9, 0.9); //Reducción de la impresión al 50%
        //g2d.scale(0.50,0.50); //Reducción de la impresión al 50%

        panelImagen.printAll(graphics);
        //panelPrincipal.printComponents(graphics);

        return PAGE_EXISTS;
    }

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed

        versionSeleccionada = Integer.parseInt(cboVersion.getSelectedItem().toString());

        //getVersion(versionSeleccionada);
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
     * Busca la versión seleccionada del documento
     *
     * @param version La versión seleccionada
     */
    private void getVersion(int version) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ruta, archivo, formato;
        //String buffer;
        //BufferedReader leer;
        //PrintWriter escribir;
        //ToolsTiff toolsTiff = new ToolsTiff();
        OutputStream escribiendo;
        //byte[] buffer;
        Bufer buffer;

        traza.trace("version a buscar " + version, Level.INFO);

        try {
            for (InfoDocumento infoDoc : infoDocumentos) {
                if (infoDoc.getVersion() == version) {

                    formato = infoDoc.getFormato();

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

                    if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {

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

                        if (buffer == null) {
                            throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                        }
                        escribiendo = new FileOutputStream(fileCod);
                        escribiendo.write(buffer.getBufer());
                        escribiendo.flush();
                        escribiendo.close();

                        toolsFile.decodificar("imagen." + formato);

//                        imagen = null;
                        imagen = new ImageIcon(toolsFile.getTempPath() + "imagen." + formato).getImage();

//                        panelImagen.removeAll();
                        panelImagen.setImagen(imagen);
                        panelImagen.updateUI();
                        break;

                    } else if (formato.equalsIgnoreCase("pdf")) {

                        VerDocumentoPDF vdpdf = new VerDocumentoPDF("", infoDoc, null, false, true, version);
                        Principal.desktop.add(vdpdf);
                        vdpdf.toFront();
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
        } catch (DW4JDesktopExcepcion e) {
            this.dispose();
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

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

        String tipoDoc = infoDocumento.getTipoDocumento().trim();
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

                        DefaultMutableTreeNode verificador = CalidadDocumento.getSeleccionado();
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
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }


    }//GEN-LAST:event_jbAprobarActionPerformed
    private void jbRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRechazarActionPerformed

        RechazarDocumento rd;

        boolean exito;

        if (Constantes.ACCION.equalsIgnoreCase("ELIMINAR")) {
            traza.trace("rechazando el tipo de documento " + infoDocElimina.getTipoDocumento() + " su id " + idInfoDocumento, Level.INFO);
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
                if (infoDocumento.isTipoDocDatoAdicional()) {
                    btnDatosAdicionales.setVisible(true);
                    ManejoSesion.getExpediente().setIdTipoDocumento(infoDocumento.getIdDocumento());
                    DatoAdicional dag = new DatoAdicional(infoDocumento.getNumeroDocumento(), Integer.parseInt(cboVersion.getSelectedItem().toString()));
                }
            }
        }
    }//GEN-LAST:event_btnDatosAdicionalesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnDatosAdicionales;
    private javax.swing.JButton cancelar;
    private javax.swing.JComboBox cboVersion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton jbAprobar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JButton jbImprimir;
    private javax.swing.JButton jbRechazar;
    private javax.swing.JLabel jlFechaVencimiento;
    private com.develcom.tools.PanelImagen panelImagen;
    private javax.swing.JButton zommIn;
    private javax.swing.JButton zommOut;
    // End of variables declaration//GEN-END:variables

    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }
}
