    /*
 * guardarDocumentosJDialog.java
 *
 * Created on 29 de agosto de 2007, 03:48 PM
 */
package com.develcom.gui.visor;

import com.develcom.autentica.Perfil;
import com.develcom.dao.ManejoSesion;
import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.expediente.Indice;
import com.develcom.gui.Principal;
import com.develcom.gui.calidad.RechazarDocumento;
import com.develcom.gui.captura.DatoAdicional;
import com.develcom.gui.captura.DigitalizaDocumento;
import com.develcom.gui.consulta.ResultadoExpediente;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.Impresor;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.tools.Constantes;
import com.develcom.tools.Imagenes;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.trazas.Traza;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import uk.co.mmscomputing.application.imageviewer.ImagePanel;
import ve.com.develcom.aprueba.CalidadDocumento;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.sesion.IniciaSesion;

/**
 * Permite visualizar un documento digitalizado
 *
 * @author develcom
 */
public class VerDocumento extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = -544708594033944141L;
    /**
     * Lista de bufer de imagenes
     */
    private ArrayList<BufferedImage> imagenesTiff;
    /**
     * Lista de informacion del documento
     */
    private List<InfoDocumento> infoDocumentos;
    private double scalaImagen = 1;
    /**
     * escribe trazas en el log
     */
    private Traza traza = new Traza(VerDocumento.class);
    /**
     * objecto de informacion del documento
     */
    private InfoDocumento infoDocumento;
    private int idInfoDocumento;
    private int versionSeleccionada;
    private int versionMostrar;
    private boolean vers;
    private ToolsFiles toolsFile = new ToolsFiles();

    /**
     * Constructor, inicia los componentes y muestra los documentos digitalizado
     *
     * @param infoDocumento Objeto con la informacion del documento a mostrar
     * @param vers
     * @param version
     */
    public VerDocumento(InfoDocumento infoDocumento, boolean vers, int version) {
        this.vers = vers;
        versionMostrar = version;
        iniciar(infoDocumento);
    }

    public VerDocumento(InfoDocumento infoDocumento) {
        iniciar(infoDocumento);
    }

    private void iniciar(InfoDocumento infoDocumento) {

        try {
            this.infoDocumento = infoDocumento;
            initComponents();
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
            } else {
                jbRechazar.setVisible(false);
                jbAprobar.setVisible(false);
            }

            if (Constantes.ACCION.equalsIgnoreCase("GUARDAR")) {
                btnDatosAdicionales.setVisible(false);
            } else {
                if (infoDocumento.isTipoDocDatoAdicional()) {
                    btnDatosAdicionales.setVisible(true);
                } else {
                    btnDatosAdicionales.setVisible(false);
                }
            }

            jlFechaVencimiento.setVisible(false);
            CentraVentanas.centrar(this, Principal.desktop);
            //setVisible(true);

            crearObjetos();
            //mostrarDocumentos();
            documentos();
            //setVisible(true);
            Principal.desktop.updateUI();
        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al buscar las libreria y categoria segun el perfil", Level.ERROR, ex);
        }
    }

    /**
     * Crea los indices dinamicos en el formulario
     */
    private void crearObjetos() {
        List<Indice> lstIndices;
        CreaObjetosDinamicos uv = new CreaObjetosDinamicos();
        GridBagConstraints constraints = new GridBagConstraints();
        try {

            traza.trace("creando indices dinamicos", Level.INFO);

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.gridheight = 1;

            uv.mostrarIndices(ManejoSesion.getExpediente());
            setTitle(uv.crearTituloExpediente());

        } catch (Exception e) {
            traza.trace("error al crear los indices dinamicos", Level.ERROR, e);
        }

    }

    private void documentos() {
        final MostrarProceso proceso = new MostrarProceso("Buscando la imagen del documento");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mostrarDocumentos();
                traza.trace("mostrando la ventana", Level.INFO);

                proceso.detener();
                setVisible(true);
            }
        }).start();

    }

    /**
     * Busca y muestra el documento previamente digitalizado
     */
    private void mostrarDocumentos() {

        String ruta = "", archivo = null;
        //String buffer;
//        BufferedReader leer;
//        PrintWriter escribir;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int contDocAprob = 0, contDocPend = 0;
        //byte[] buffer;
        Bufer buffer;
        FileOutputStream escribiendo;

        try {
            System.gc();

            idInfoDocumento = infoDocumento.getIdInfoDocumento();
            int idDoc = infoDocumento.getIdDocumento();
            int version = infoDocumento.getVersion();
            int numDoc = infoDocumento.getNumeroDocumento();
            int idCat = ManejoSesion.getExpediente().getIdCategoria();
            int idSubCat = ManejoSesion.getExpediente().getIdSubCategoria();
            String idExpediente = ManejoSesion.getExpediente().getIdExpediente();
            traza.trace("buscando el documento: idInfoDocumento " + idInfoDocumento + " idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);

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
                        }
                    }
                }

                for (InfoDocumento id : infoDocumentos) {
                    
                    if (vers) {
                        if (id.getVersion() != versionMostrar) {
                            continue;
                        }
                    }

                    if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {
                        if (id.getEstatusDocumento().equalsIgnoreCase("aprobado")) {

                            traza.trace("mostrar archivo en modulo consultar y documentos aprobados", Level.INFO);

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
                    } else if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {

                        if (id.getEstatusDocumento().equalsIgnoreCase("pendiente")) {

                            traza.trace("mostrar archivo en modulo aprobar y documentos pendientes", Level.INFO);

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
                    } else if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

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
                }

                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + version, Level.INFO);

                if (archivo == null) {
                    throw new DW4JDesktopExcepcion("Falta información del documento");
                }

                buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);

                if (!buffer.isExiste()) {
                    throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                }
                escribiendo = new FileOutputStream(fileCod);
                escribiendo.write(buffer.getBufer());
                escribiendo.flush();
                escribiendo.close();

                if (fileCod.exists()) {
                    toolsFile.decodificar();
                    imagenesTiff = toolsFile.open(toolsFile.getArchivoTif());
                    int size = imagenesTiff.size();

                    for (int i = 0; i < size; i++) {
                        ImagePanel imageTab = new ImagePanel();
                        imageTab.setImage(imagenesTiff.get(i));
                        imageTab.repaint();
                        JScrollPane sp = new JScrollPane(imageTab);
                        sp.getVerticalScrollBar().setUnitIncrement(100);
                        sp.getHorizontalScrollBar().setUnitIncrement(100);
                        panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
                        //panelImagen.addTab("Pag." + (i + 1), new ImageIcon("/ve/gob/mcti/gui/imagenes/develcom/Properties16.gif"), sp);

                    }
//                    toolsFile.clean();

                    traza.trace("contador documentos aprobados " + contDocAprob, Level.INFO);
                    traza.trace("contador documentos pendientes " + contDocPend, Level.INFO);

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
                    //setVisible(true);
                }
            } else {
                throw new IOException("Documento o Archivo no encontrado");
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
            this.dispose();
            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                Principal.desktop.repaint();

            }
        } catch (FileNotFoundException ex) {

            this.dispose();
            traza.trace("Error al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                Principal.desktop.repaint();

            }
        } catch (SOAPFaultException e) {

            this.dispose();
            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                Principal.desktop.repaint();

            }
        } catch (IOException e) {

            this.dispose();
            traza.trace("error al abrir la imagen", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Problemas al abrir el documento\n" + e.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                Principal.desktop.repaint();

            }

        } catch (Exception ex) {
            this.dispose();

            traza.trace("Error general al buscar el archivo", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);

            if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

                Principal.desktop.removeAll();
                DigitalizaDocumento dd = new DigitalizaDocumento();
                Principal.desktop.add(dd);
                dd.find();
                Principal.desktop.repaint();

            } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

                Principal.desktop.repaint();

            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator3 = new javax.swing.JSeparator();
        panelImagen = new javax.swing.JTabbedPane();
        barraHerramienta = new javax.swing.JToolBar();
        jbImprimir = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cboVersion = new javax.swing.JComboBox();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnAbrir = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButtonZommOut = new javax.swing.JButton();
        jButtonZommIn = new javax.swing.JButton();
        jButtonRotar = new javax.swing.JButton();
        jButtonRotIzq = new javax.swing.JButton();
        jButtonRotDer = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jlFechaVencimiento = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jbAprobar = new javax.swing.JButton();
        jbRechazar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButtonCerrar = new javax.swing.JButton();
        jbGuardar = new javax.swing.JButton();
        btnDatosAdicionales = new javax.swing.JButton();

        setBackground(new java.awt.Color(224, 239, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoscrolls(true);
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        panelImagen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        panelImagen.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        panelImagen.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        panelImagen.setAutoscrolls(true);

        barraHerramienta.setFloatable(false);

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
        barraHerramienta.add(jbImprimir);
        barraHerramienta.add(jSeparator4);

        cboVersion.setFont(new java.awt.Font("DejaVu Sans", 0, 12)); // NOI18N
        cboVersion.setToolTipText("Seleccione la versión del documento que desea ver");
        cboVersion.setDoubleBuffered(true);
        cboVersion.setName(""); // NOI18N
        cboVersion.setPreferredSize(new java.awt.Dimension(122, 25));
        cboVersion.setVerifyInputWhenFocusTarget(false);
        barraHerramienta.add(cboVersion);
        barraHerramienta.add(jSeparator6);

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
        barraHerramienta.add(btnAbrir);
        barraHerramienta.add(jSeparator1);

        jButtonZommOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomOut24.gif"))); // NOI18N
        jButtonZommOut.setMnemonic('-');
        jButtonZommOut.setToolTipText("Disminuir Imagen");
        jButtonZommOut.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommOutActionPerformed(evt);
            }
        });
        barraHerramienta.add(jButtonZommOut);

        jButtonZommIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomIn24.gif"))); // NOI18N
        jButtonZommIn.setMnemonic('+');
        jButtonZommIn.setToolTipText("Aumentar Imagen");
        jButtonZommIn.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommInActionPerformed(evt);
            }
        });
        barraHerramienta.add(jButtonZommIn);

        jButtonRotar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_rotate_anticlockwise.png"))); // NOI18N
        jButtonRotar.setMnemonic('r');
        jButtonRotar.setToolTipText("Disminuir Imagen");
        jButtonRotar.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotarActionPerformed(evt);
            }
        });
        barraHerramienta.add(jButtonRotar);

        jButtonRotIzq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_left.png"))); // NOI18N
        jButtonRotIzq.setMnemonic('b');
        jButtonRotIzq.setToolTipText("Pag.Anterior");
        jButtonRotIzq.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotIzq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotIzqActionPerformed(evt);
            }
        });
        barraHerramienta.add(jButtonRotIzq);

        jButtonRotDer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_right.png"))); // NOI18N
        jButtonRotDer.setMnemonic('n');
        jButtonRotDer.setToolTipText("Pag. Siguiente");
        jButtonRotDer.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotDer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotDerActionPerformed(evt);
            }
        });
        barraHerramienta.add(jButtonRotDer);
        barraHerramienta.add(jSeparator5);

        jlFechaVencimiento.setText("jLabel1");
        barraHerramienta.add(jlFechaVencimiento);
        barraHerramienta.add(jSeparator8);
        barraHerramienta.add(jSeparator7);

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
        barraHerramienta.add(jbAprobar);

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
        barraHerramienta.add(jbRechazar);
        barraHerramienta.add(jSeparator2);

        jButtonCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/action_stop.gif"))); // NOI18N
        jButtonCerrar.setToolTipText("Cerrar");
        jButtonCerrar.setPreferredSize(new java.awt.Dimension(100, 40));
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        jbGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/save1.png"))); // NOI18N
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });

        btnDatosAdicionales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/dato1.png"))); // NOI18N
        btnDatosAdicionales.setToolTipText("Ver Datos los Adicionales");
        btnDatosAdicionales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatosAdicionalesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, panelImagen)
                    .add(layout.createSequentialGroup()
                        .add(barraHerramienta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(36, 36, 36)
                        .add(jbGuardar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(43, 43, 43)
                        .add(btnDatosAdicionales)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonCerrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 93, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(barraHerramienta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jbGuardar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jButtonCerrar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(btnDatosAdicionales)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelImagen, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Toma la version del documento a mostrar
     *
     * @param evt
     */
    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed

        versionSeleccionada = Integer.parseInt(cboVersion.getSelectedItem().toString());

        //getVersion(versionSeleccionada);
        version(versionSeleccionada);
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void version(int version) {
        final MostrarProceso proceso = new MostrarProceso("Buscando la version " + version + " del documento");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
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

        String ruta, archivo, formato;
        //String buffer;
        //byte[] buffer;
        Bufer buffer;
        BufferedReader leer;
        PrintWriter escribir;
        OutputStream escribiendo;
        //ToolsTiff toolsTiff = new ToolsTiff();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

                    if (formato.equalsIgnoreCase("tif") || formato.equalsIgnoreCase("tiff")) {
                        if (infoDoc.getFechaVencimiento() != null) {

                            XMLGregorianCalendar xmlCalendar = infoDoc.getFechaVencimiento();
                            GregorianCalendar fechaVencimiento = xmlCalendar.toGregorianCalendar();

                            jlFechaVencimiento.setText(sdf.format(fechaVencimiento.getTime()));
                            jlFechaVencimiento.setVisible(true);

                        }

                        ruta = infoDoc.getRutaArchivo();
                        archivo = infoDoc.getNombreArchivo();

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

                        if (fileCod.exists()) {
                            new ToolsFiles().decodificar();

                            verVersion(toolsFile.getArchivoTif());
                        }
                    } else if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {

                        VerImagenes vi = new VerImagenes("", infoDoc, null, false, version, true);
                        Principal.desktop.add(vi);
                        vi.toFront();
                        dispose();
                        break;

                    } else if (formato.equalsIgnoreCase("pdf")) {
                        VerDocumentoPDF vdpdf = new VerDocumentoPDF("", infoDoc, null, false, true, version);
                        Principal.desktop.add(vdpdf);
                        vdpdf.toFront();
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
//                Principal.resultadoExpediente.show();
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

    /**
     * Muestra la version seleccionada del documento
     *
     * @param ruta Ruta donde se aloja el archivo
     */
    private void verVersion(String ruta) {
        traza.trace("ruta del archivo a mostrar " + ruta, Level.INFO);
        imagenesTiff = null;
        try {
            int i;
            panelImagen.removeAll();
            imagenesTiff = new ToolsFiles().open(ruta);
            for (i = 0; i < imagenesTiff.size(); i++) {
                ImagePanel imageTab = new ImagePanel();
                JScrollPane sp = new JScrollPane(imageTab);
                sp.getVerticalScrollBar().setUnitIncrement(100);
                sp.getHorizontalScrollBar().setUnitIncrement(100);
                imageTab.setImage(imagenesTiff.get(i));
                panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
            }

        } catch (IOException ex) {
            traza.trace("problemas al carga la version del documento", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "problemas al carga la version del documento\n" + ex.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Va al derecha del documento
     *
     * @param evt
     */
private void jButtonRotDerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotDerActionPerformed
    try {
        panelImagen.setSelectedIndex(panelImagen.getSelectedIndex() + 1);
    } catch (IndexOutOfBoundsException ex) {
    }
}//GEN-LAST:event_jButtonRotDerActionPerformed

    /**
     * Va a la izquierda del documento
     *
     * @param evt
     */
private void jButtonRotIzqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotIzqActionPerformed
    try {
        if (panelImagen.getSelectedIndex() - 1 >= 0) {
            panelImagen.setSelectedIndex(panelImagen.getSelectedIndex() - 1);
        }
    } catch (IndexOutOfBoundsException ex) {
    }
}//GEN-LAST:event_jButtonRotIzqActionPerformed

    /**
     * Rpta el documento
     *
     * @param evt
     */
private void jButtonRotarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotarActionPerformed
    ImagePanel imageTab = new ImagePanel();
    imagenesTiff.set(panelImagen.getSelectedIndex(), Imagenes.rotate((BufferedImage) imagenesTiff.get(panelImagen.getSelectedIndex()), 90));
    imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(panelImagen.getSelectedIndex())));
    JScrollPane sp = new JScrollPane(imageTab);
    sp.getVerticalScrollBar().setUnitIncrement(100);
    sp.getHorizontalScrollBar().setUnitIncrement(100);
    panelImagen.setComponentAt(panelImagen.getSelectedIndex(), sp);
    panelImagen.repaint();

}//GEN-LAST:event_jButtonRotarActionPerformed

    /**
     * Aleja el documento
     *
     * @param evt
     */
private void jButtonZommOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZommOutActionPerformed
    scalaImagen = scalaImagen * 0.8;
    ImagePanel imageTab = new ImagePanel();
    imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(panelImagen.getSelectedIndex())));
    JScrollPane sp = new JScrollPane(imageTab);
    sp.getVerticalScrollBar().setUnitIncrement(100);
    sp.getHorizontalScrollBar().setUnitIncrement(100);
    panelImagen.setComponentAt(panelImagen.getSelectedIndex(), sp);
    panelImagen.repaint();

}//GEN-LAST:event_jButtonZommOutActionPerformed

    /**
     * Acerca el documento
     *
     * @param evt
     */
private void jButtonZommInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZommInActionPerformed
    scalaImagen = scalaImagen * 1.2;
    ImagePanel imageTab = new ImagePanel();
    imageTab.setImage(Imagenes.scale(scalaImagen, (BufferedImage) imagenesTiff.get(panelImagen.getSelectedIndex())));
    JScrollPane sp = new JScrollPane(imageTab);
    sp.getVerticalScrollBar().setUnitIncrement(100);
    sp.getHorizontalScrollBar().setUnitIncrement(100);
    panelImagen.setComponentAt(panelImagen.getSelectedIndex(), sp);
    panelImagen.repaint();

}//GEN-LAST:event_jButtonZommInActionPerformed

    /**
     * Cierra la ventana
     *
     * @param evt
     */
private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
    dispose();
    if (!vers) {
        if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {

            Principal.desktop.removeAll();
            DigitalizaDocumento dd = new DigitalizaDocumento();
            Principal.desktop.add(dd);
            dd.find();
            Principal.desktop.repaint();

        } else if (Constantes.ACCION.equalsIgnoreCase("CONSULTAR")) {

            Principal.desktop.repaint();

        } else if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {

            ResultadoExpediente re = new ResultadoExpediente();
            re.convertir();
            re.refrescar();
            re.toFront();
            Principal.desktop.add(re);
            Principal.desktop.repaint();

        }
    }
}//GEN-LAST:event_jButtonCerrarActionPerformed

private void jbImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbImprimirActionPerformed

    Impresor impresor = new Impresor();
    for (int i = 0; i < panelImagen.getTabCount(); i++) {
        JScrollPane sp = (JScrollPane) panelImagen.getComponentAt(i);
        ImagePanel ip = (ImagePanel) sp.getViewport().getView();
        impresor.append(ip);
    }
    impresor.print();
}//GEN-LAST:event_jbImprimirActionPerformed

private void jbAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAprobarActionPerformed

    String tipoDoc = infoDocumento.getTipoDocumento().trim();
    boolean aprobado = false;
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
                aprobado = new CalidadDocumento().aprobarDoc(idInfoDocumento, usuario);

                if (aprobado) {
                    JOptionPane.showMessageDialog(this, "Documento aprobado con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    Principal.desktop.removeAll();
                    Principal.desktop.repaint();
                    ResultadoExpediente re = new ResultadoExpediente();
                    re.convertir();
                    re.refrescar();
                    re.toFront();
                    Principal.desktop.add(re);
                } else {
                    JOptionPane.showMessageDialog(this, "Problemas al aprobar el documento", "Error", JOptionPane.ERROR_MESSAGE);
                    Principal.desktop.removeAll();
                    Principal.desktop.repaint();
                    ResultadoExpediente re = new ResultadoExpediente();
                    re.convertir();
                    re.refrescar();
                    re.toFront();
                    Principal.desktop.add(re);
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
    RechazarDocumento rd = new RechazarDocumento(idInfoDocumento, infoDocumento.getTipoDocumento(), versionSeleccionada);
}//GEN-LAST:event_jbRechazarActionPerformed

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGuardarActionPerformed

        toolsFile.guardarArchivoTIFFtoPDF();

    }//GEN-LAST:event_jbGuardarActionPerformed

    private void btnDatosAdicionalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatosAdicionalesActionPerformed

        if (infoDocumento.isTipoDocDatoAdicional()) {
            btnDatosAdicionales.setVisible(true);
            ManejoSesion.getExpediente().setIdTipoDocumento(infoDocumento.getIdDocumento());
            DatoAdicional dag = new DatoAdicional(infoDocumento.getNumeroDocumento(), Integer.parseInt(cboVersion.getSelectedItem().toString()));
        }
    }//GEN-LAST:event_btnDatosAdicionalesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraHerramienta;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnDatosAdicionales;
    private javax.swing.JComboBox cboVersion;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonRotDer;
    private javax.swing.JButton jButtonRotIzq;
    private javax.swing.JButton jButtonRotar;
    private javax.swing.JButton jButtonZommIn;
    private javax.swing.JButton jButtonZommOut;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JButton jbAprobar;
    private javax.swing.JButton jbGuardar;
    private javax.swing.JButton jbImprimir;
    private javax.swing.JButton jbRechazar;
    private javax.swing.JLabel jlFechaVencimiento;
    private javax.swing.JTabbedPane panelImagen;
    // End of variables declaration//GEN-END:variables
}
