/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.elimina;

import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.documento.Bufer;
import com.develcom.documento.DatoAdicional;
import com.develcom.elimina.InfoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.Libreria;
import com.develcom.gui.Principal;
import com.develcom.gui.calidad.RechazarDocumento;
import com.develcom.gui.tools.CentraVentanas;
import com.develcom.gui.tools.CreaObjetosDinamicos;
import com.develcom.gui.tools.MostrarProceso;
import com.develcom.gui.visor.VerDocumentoPDF;
import com.develcom.gui.visor.VerImagenes;
import com.develcom.tools.Constantes;
import com.develcom.tools.Imagenes;
import com.develcom.tools.ToolsFiles;
import com.develcom.tools.trazas.Traza;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import uk.co.mmscomputing.application.imageviewer.ImagePanel;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.elimina.QuitaDocumento;
import ve.com.develcom.expediente.GestionDocumentos;

/**
 *
 * @author develcom
 */
public class EliminaDocumento extends javax.swing.JInternalFrame {

    private static DefaultMutableTreeNode seleccionado;
    private static final long serialVersionUID = -1649640288983172921L;
    private double scalaImagen = 1;
    private JTabbedPane panelImagen = new JTabbedPane();
    private JTree arbolDocumentos = new JTree();
    private String idExpediente = null;
    /**
     * Lista de bufer de imagenes
     */
    private ArrayList<BufferedImage> imagenesTiff;
    /**
     * escribe trazas en el log
     */
    private Traza traza = new Traza(EliminaDocumento.class);
    /**
     * Datos del expediente
     */
    private Expediente expediente;
    /**
     * Listado de subCategorias
     */
    private List<SubCategoria> listaSubCategorias;
    /**
     * Lista de informacion del documento
     */
    private List<InfoDocumento> infoDocumentosArbol;
    /**
     * Listado de tipos de docuemtos digitalizados
     */
    private List<InfoDocumento> documentosEliminar = new ArrayList<InfoDocumento>();
    /**
     * objecto de informacion del documento
     */
    private InfoDocumento infoDocumentoEliminar;
    private int idInfoDocumento;
    private ToolsFiles toolsFile = new ToolsFiles();

    public EliminaDocumento() {

        expediente = ManejoSesion.getExpediente();
        initComponents();

        lblFechaVencimiento.setVisible(false);
        CentraVentanas.centrar(this, Principal.desktop);

        arbolDocumentos.setModel(null);
        panelImagen.removeAll();
        panelImagen.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        panelImagen.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        JScrollPane scrollTreeView = new JScrollPane(arbolDocumentos);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(300);
        splitPane.setLeftComponent(scrollTreeView);
        splitPane.setRightComponent(panelImagen);

        arbolDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtrDocDigitMouseClicked(evt);
            }
        });

        llenarSubCategorias();
        setVisible(true);
        setTitle("Eliminar documento ");
    }

    /**
     * LLena el combo con las subCategorias
     *
     * @param idCategoria
     */
    private void llenarSubCategorias() {

        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        modelo.addElement("");
        try {
            traza.trace("llenado listado de subCategorias", Level.INFO);

            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria(null, expediente.getIdCategoria(), 0);
            if (listaSubCategorias != null && !listaSubCategorias.isEmpty()) {

                for (SubCategoria sc : listaSubCategorias) {
                    if (sc.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        modelo.addElement(sc.getSubCategoria().trim());
                    }
                }
                cmbSubCategorias.setModel(modelo);
            } else {
                JOptionPane.showMessageDialog(this, "La Categoria " + expediente.getCategoria() + "\nno tiene subCategorias", "Advertencia", JOptionPane.WARNING_MESSAGE);
                jButtonCerrarActionPerformed(null);
            }
        } catch (Exception e) {
            traza.trace("error al llenar el listado de subCategorias", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "Error al llenar el listado de subCategorias\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
//        return resp;
    }

    private void jtrDocDigitMouseClicked(java.awt.event.MouseEvent evt) {
        abrir();
    }

    /**
     * Muestra el documento digitalizado
     */
    private void abrir() {

        DefaultMutableTreeNode dmtn;
        TreeNode categori;
        TreeNode subCategor;
        String tipoDocumento, formato = "";

        if (!arbolDocumentos.isSelectionEmpty()) {

            dmtn = (DefaultMutableTreeNode) arbolDocumentos.getLastSelectedPathComponent();
            seleccionado = dmtn;

            if (dmtn.isLeaf()) {

                categori = dmtn.getRoot();
                subCategor = dmtn.getParent();
                tipoDocumento = dmtn.toString().trim();

                traza.trace("tipo de documento a abrir " + tipoDocumento, Level.INFO);
                ManejoSesion.getExpediente().setSubCategoria(subCategor.toString());

                ManejoSesion.getExpediente().setIdSubCategoria(0);
                if (listaSubCategorias != null) {
                    for (SubCategoria sc : listaSubCategorias) {
                        if (sc.getSubCategoria().equalsIgnoreCase(subCategor.toString())) {
                            ManejoSesion.getExpediente().setIdSubCategoria(sc.getIdSubCategoria());
                            ManejoSesion.getExpediente().setSubCategoria(sc.getSubCategoria());
                            traza.trace("id de la subCategoria seleccionada " + sc.getIdSubCategoria(), Level.INFO);
                            break;
                        }
                    }
                }
                if (ManejoSesion.getExpediente().getIdSubCategoria() == 0) {
                    traza.trace("no se tiene el id de la subCategoria", Level.WARN);
                }

                traza.trace("buscando la extencion del tipo de documento " + tipoDocumento, Level.INFO);
                for (InfoDocumento infDoc : documentosEliminar) {
                    traza.trace("tipo de documento a comparar " + infDoc.getTipoDocumento(), Level.INFO);

                    if (tipoDocumento.contains(infDoc.getTipoDocumento())) {

                        ManejoSesion.getExpediente().setTipoDocumento(tipoDocumento);
                        ManejoSesion.getExpediente().setIdTipoDocumento(infDoc.getIdDocumento());

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
                        infoDocumentoEliminar = infDoc;
                        break;
                        //infoDocumentos.add(infDoc);
                    }
                }

                //arbolDocumentos.clearSelection();
                try {
                    if (formato.equalsIgnoreCase("tif") || formato.equalsIgnoreCase("tiff")) {

                        buscandoDocumentoFisico();

                    } else if (formato.equalsIgnoreCase("pdf")) {

                        int tabs = panelImagen.getTabCount();

                        if (tabs >= 1) {
                            panelImagen.removeAll();
                        }

                        VerDocumentoPDF vdpdf = new VerDocumentoPDF(infoDocumentoEliminar, this);
                        Principal.desktop.add(vdpdf);
                        vdpdf.toFront();
                        this.toBack();
                    } else if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {

                        int tabs = panelImagen.getTabCount();

                        if (tabs >= 1) {
                            panelImagen.removeAll();
                        }

                        VerImagenes vi = new VerImagenes(infoDocumentoEliminar, this);
                        Principal.desktop.add(vi);
                        vi.toFront();
                        this.toBack();
                    } else {
                        JOptionPane.showMessageDialog(this, "No fue posible identificar el formato del Tipo de Documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    traza.trace("error en la seleccion del formato del tipo de documento", Level.ERROR, e);
                }

            } else {
                //JOptionPane.showMessageDialog(this, "Debe elegir un tipo de documento para ser consultado", "Informacion...", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            //JOptionPane.showMessageDialog(this, "Elija uno de los documentos digitalizados", "Información...", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void buscandoDocumentoFisico() {
        final MostrarProceso proceso = new MostrarProceso("Buscando y Decodificando el documento");
        proceso.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mostrarDocumentos();
                proceso.detener();
            }
        }).start();

    }

    /**
     * Busca y muestra el documento previamente digitalizado
     */
    private void mostrarDocumentos() {

        String ruta, archivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Bufer buffer;
        FileOutputStream escribiendo;
        InfoDocumento infoDocBuscado;

        try {

            idInfoDocumento = infoDocumentoEliminar.getIdInfoDocumento();
            int idDoc = infoDocumentoEliminar.getIdDocumento();
            int version = infoDocumentoEliminar.getVersion();
            int numDoc = infoDocumentoEliminar.getNumeroDocumento();
            int idCat = ManejoSesion.getExpediente().getIdCategoria();
            int idSubCat = ManejoSesion.getExpediente().getIdSubCategoria();

//            if (idCat == 0) {
//                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idCat) " + idCat);
//            }
//            if (idSubCat == 0) {
//                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idCat) " + idSubCat);
//            }
            if (idExpediente == null) {
                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idExpediente) " + idExpediente);
            }

            traza.trace("buscando el documento: idInfoDocumento " + idInfoDocumento + " idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);
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

                    lblFechaVencimiento.setText("Fecha de Vencimiento: " + sdf.format(fechaVencimiento.getTime()));
                    lblFechaVencimiento.setVisible(true);
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

                if (fileCod.exists()) {
                    if (imagenesTiff != null) {
                        imagenesTiff.clear();
                    }
                    panelImagen.removeAll();
//                    new ToolsTiff().decodificar();
                    toolsFile.decodificar();
                    imagenesTiff = toolsFile.open(toolsFile.getArchivoTif());
                    int size = imagenesTiff.size();

                    for (int i = 0; i < size; i++) {
                        ImagePanel imageTab = new ImagePanel();
                        JScrollPane sp = new JScrollPane(imageTab);
//                        sp.getVerticalScrollBar().setUnitIncrement(100);
//                        sp.getHorizontalScrollBar().setUnitIncrement(100);
                        imageTab.setImage(imagenesTiff.get(i));
                        //imageTab.repaint();
                        panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
                    }
                }
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

        if (!infoDocumentosArbol.isEmpty()) {
            infoDocumentosArbol.clear();
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

        splitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmbSubCategorias = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jButtonRotIzq = new javax.swing.JButton();
        jButtonRotDer = new javax.swing.JButton();
        jButtonZommIn = new javax.swing.JButton();
        jButtonZommOut = new javax.swing.JButton();
        jButtonRotar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jbEliminar = new javax.swing.JButton();
        jButtonCerrar = new javax.swing.JButton();
        lblFechaVencimiento = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Identificador del Expediente");

        jLabel2.setText("SubCategorias");

        cmbSubCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSubCategoriasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtExpediente)
                    .addComponent(cmbSubCategorias, 0, 320, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cmbSubCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonRotIzq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_left.png"))); // NOI18N
        jButtonRotIzq.setMnemonic('b');
        jButtonRotIzq.setToolTipText("Pag.Anterior");
        jButtonRotIzq.setFocusable(false);
        jButtonRotIzq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRotIzq.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotIzq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRotIzq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotIzqActionPerformed(evt);
            }
        });

        jButtonRotDer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_right.png"))); // NOI18N
        jButtonRotDer.setMnemonic('n');
        jButtonRotDer.setToolTipText("Pag. Siguiente");
        jButtonRotDer.setFocusable(false);
        jButtonRotDer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRotDer.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotDer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRotDer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotDerActionPerformed(evt);
            }
        });

        jButtonZommIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomIn24.gif"))); // NOI18N
        jButtonZommIn.setMnemonic('+');
        jButtonZommIn.setToolTipText("Aumentar Imagen");
        jButtonZommIn.setFocusable(false);
        jButtonZommIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZommIn.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZommIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommInActionPerformed(evt);
            }
        });

        jButtonZommOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/ZoomOut24.gif"))); // NOI18N
        jButtonZommOut.setMnemonic('-');
        jButtonZommOut.setToolTipText("Disminuir Imagen");
        jButtonZommOut.setFocusable(false);
        jButtonZommOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonZommOut.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonZommOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonZommOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZommOutActionPerformed(evt);
            }
        });

        jButtonRotar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/arrow_rotate_anticlockwise.png"))); // NOI18N
        jButtonRotar.setMnemonic('r');
        jButtonRotar.setToolTipText("Disminuir Imagen");
        jButtonRotar.setFocusable(false);
        jButtonRotar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRotar.setPreferredSize(new java.awt.Dimension(50, 40));
        jButtonRotar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRotar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRotarActionPerformed(evt);
            }
        });

        jbEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/page_text_delete.gif"))); // NOI18N
        jbEliminar.setToolTipText("Eliminar");
        jbEliminar.setFocusable(false);
        jbEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminarActionPerformed(evt);
            }
        });

        jButtonCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/action_stop.gif"))); // NOI18N
        jButtonCerrar.setToolTipText("Cerrar");
        jButtonCerrar.setFocusable(false);
        jButtonCerrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonCerrar.setPreferredSize(new java.awt.Dimension(100, 40));
        jButtonCerrar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jbEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jbEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        lblFechaVencimiento.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFechaVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(248, 248, 248)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splitPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void jButtonRotIzqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotIzqActionPerformed
        try {
            if (panelImagen.getSelectedIndex() - 1 >= 0) {
                panelImagen.setSelectedIndex(panelImagen.getSelectedIndex() - 1);
            }
        } catch (IndexOutOfBoundsException ex) {
        }
    }//GEN-LAST:event_jButtonRotIzqActionPerformed

    private void jButtonRotDerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRotDerActionPerformed
        try {
            panelImagen.setSelectedIndex(panelImagen.getSelectedIndex() + 1);
        } catch (IndexOutOfBoundsException ex) {
        }
    }//GEN-LAST:event_jButtonRotDerActionPerformed

    private void jbEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminarActionPerformed

        boolean exito;
        DefaultMutableTreeNode dmtn;

        dmtn = (DefaultMutableTreeNode) arbolDocumentos.getLastSelectedPathComponent();

        try {

            if (dmtn.isLeaf() || dmtn != null) {

                traza.trace("eliminando el tipo de documento " + infoDocumentoEliminar.getTipoDocumento() + " su id " + idInfoDocumento, Level.INFO);
                expediente.setIdExpediente(idExpediente);
                ManejoSesion.setExpediente(expediente);
//            infoDocumentoEliminar.setIdExpediente(idExpediente);
                RechazarDocumento rd = new RechazarDocumento(infoDocumentoEliminar);
                exito = rd.isResultado();

                traza.trace("exito eliminar " + exito, Level.INFO);
                if (exito) {
                    DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) seleccionado;
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

                    panelImagen.removeAll();
                    refrescar();
                    arbolDocumentos.updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe selecionar un tipo de documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Debe selecionar un tipo de documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jbEliminarActionPerformed


    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        this.dispose();

        //Principal.desktop.removeAll();
        Principal.desktop.repaint();
        Libreria libreria = new Libreria(ManejoSesion.getSesion(), "Seleccionar Libreria - Eliminar");
        Principal.desktop.add(libreria);

    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void cmbSubCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSubCategoriasActionPerformed

        refrescar();

    }//GEN-LAST:event_cmbSubCategoriasActionPerformed

    public void refrescar() {
        CreaObjetosDinamicos uv = new CreaObjetosDinamicos();
        String subCategoria = cmbSubCategorias.getSelectedItem().toString();
        idExpediente = txtExpediente.getText();

        idExpediente = idExpediente.trim();

        if (!idExpediente.equalsIgnoreCase("")) {

            ManejoSesion.getExpediente().setIdExpediente(idExpediente);
            panelImagen.removeAll();

            uv.mostrarIndices(ManejoSesion.getExpediente());
            setTitle("Eliminar documento (" + uv.crearTituloExpediente() + ")");

            if (!subCategoria.equalsIgnoreCase("")) {
                for (SubCategoria sc : listaSubCategorias) {
                    if (subCategoria.equalsIgnoreCase(sc.getSubCategoria())) {
                        arboles(idExpediente, sc.getIdSubCategoria(), sc.getSubCategoria());
                        break;
//                llenarComboTiposDocumentos(sc.getIdSubCategoria());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Debe seleccionar una SubCategoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Debe colocar un identificador del Expediente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            cmbSubCategorias.setSelectedItem("");
        }
    }

    private void arboles(final String idExpediente, final int idSubCategoria, final String subCategoria) {

        final MostrarProceso proceso = new MostrarProceso("Construyendo el expediente " + idExpediente);
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mostrarArbol(idExpediente, idSubCategoria, subCategoria);
                proceso.detener();
//                setVisible(true);
            }
        }).start();
    }

    /**
     * Construye el arbol con los tipos de documentos digitalizados
     *
     * @param idExpediente El identificador del expediente seleccionado
     */
    private void mostrarArbol(String idExpediente, int idSubCategoria, String subCategoria) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<TipoDocumento> tipoDocumentos = new ArrayList<TipoDocumento>();
        QuitaDocumento ed = new QuitaDocumento();
        GestionDocumentos btd = new GestionDocumentos();
        List<Integer> idDocumento = new ArrayList<Integer>();
        String cat, nombreTD, tmp[], da = "";
        int idCat, totalDocDig, cont = 0;

//        List<InfoDocumento> infoDoc = new ArrayList<InfoDocumento>();
        DefaultMutableTreeNode arbolDigitalizados = new DefaultMutableTreeNode();
        DefaultMutableTreeNode ramaDigitalizados = null;

        traza.trace("armando el arbol con el expediente " + idExpediente, Level.INFO);

        try {

            arbolDocumentos.setModel(null);

            idCat = expediente.getIdCategoria();
            cat = expediente.getCategoria();

            traza.trace("id categoria: " + idCat + " - Categoria: " + cat, Level.INFO);
            traza.trace("buscando los tipos de documentos disponibles", Level.INFO);

            arbolDigitalizados.setUserObject(cat + " - " + idExpediente);

//            for (com.develcom.expediente.SubCategoria sc : listaSubCategorias) {
            traza.trace("subCategoria " + subCategoria, Level.INFO);
            tipoDocumentos = new AdministracionBusqueda().buscarTipoDocumento(null, idCat, idSubCategoria);

            for (TipoDocumento td : tipoDocumentos) {
                if (td.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                    idDocumento.add(td.getIdTipoDocumento());
                }
            }

            if (!idDocumento.isEmpty()) {
                //busca los tipos de documentos digitalizados
                traza.trace("busca informacion de los documentos de la SubCategoria " + subCategoria + " su id " + idSubCategoria, Level.INFO);
                //infoDoc = ed.buscarListadoInfoDocumentos(idDocumento, idExpediente);
//                infoDocumentosArbol = new QuitaDocumento().buscarListadoInfoDocumentos(idDocumento, idExpediente);
                List<com.develcom.documento.InfoDocumento> infoDocumentosArbolDoc = new GestionDocumentos().encontrarInformacionDoc(idDocumento, idExpediente, 0, 0, 2, 2, false);
                convertir(infoDocumentosArbolDoc);
                traza.trace("tamaño lista de InfoDocumento " + infoDocumentosArbol.size(), Level.INFO);

                if (!documentosEliminar.isEmpty()) {
                    documentosEliminar.clear();
                }

                //if (!infoDoc.isEmpty()) {
                if (!infoDocumentosArbol.isEmpty()) {
                    //convertir(infoDoc);

                    traza.trace("rama subCategoria " + subCategoria, Level.INFO);
                    ramaDigitalizados = new DefaultMutableTreeNode(subCategoria);

                    totalDocDig = infoDocumentosArbol.size();
                    traza.trace("total documentos digitalizados de la subCategoria " + subCategoria, Level.INFO);

                    tmp = new String[totalDocDig];

                    for (InfoDocumento id : infoDocumentosArbol) {
                        if (id.isTipoDocDatoAdicional()) {
                            da = "";
//                        datosAdicionales.clear();
                            List<com.develcom.elimina.DatoAdicional> datosAdicionales = id.getLsDatosAdicionales();
                            int size = datosAdicionales.size(), i = 1;
                            for (com.develcom.elimina.DatoAdicional datAd : datosAdicionales) {

                                if (datAd.getTipo().equalsIgnoreCase("fecha")) {
//                                        XMLGregorianCalendar calendar = (XMLGregorianCalendar) datAd.getValor();
//                                        Calendar cal = calendar.toGregorianCalendar();
                                    if (i == size) {
                                        da = da + " " + datAd.getValor();
                                    } else {
                                        da = da + " " + datAd.getValor() + ",";
                                    }
                                } else {
                                    if (i == size) {
                                        da = da + " " + datAd.getValor();
                                    } else {
                                        da = da + " " + datAd.getValor() + ",";
                                    }
                                }
                                i++;
                            }
                        }
                        da = da.trim();
                        if (id.isTipoDocDatoAdicional()) {
                            nombreTD = id.getTipoDocumento() + " - " + id.getNumeroDocumento() + " " + id.getEstatusDocumento() + " " + id.getVersion() + " (" + da + ")";
                            nombreTD = nombreTD.trim();
                            id.setTipoDocumento(nombreTD);
                            documentosEliminar.add(id);
                            ramaDigitalizados.add(new DefaultMutableTreeNode(nombreTD));
                            traza.trace("tipo documento " + nombreTD + " de la subCategoria " + subCategoria, Level.INFO);
                        } else {
                            nombreTD = id.getTipoDocumento() + " - " + id.getNumeroDocumento() + " " + id.getEstatusDocumento() + " " + id.getVersion();
                            nombreTD = nombreTD.trim();
                            id.setTipoDocumento(nombreTD);
                            documentosEliminar.add(id);
                            ramaDigitalizados.add(new DefaultMutableTreeNode(nombreTD));
                            traza.trace("tipo documento " + nombreTD + " de la subCategoria " + subCategoria, Level.INFO);
                        }
                    }
                    arbolDigitalizados.add(ramaDigitalizados);
                } else {
                    JOptionPane.showMessageDialog(this, "La SubCategoria seleccionada no posee documento rechazados", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                traza.trace("Problema con la lista de documento de la SubCategoria " + subCategoria, Level.INFO);
            }

            cont = 0;

            if (!idDocumento.isEmpty()) {
                idDocumento.clear();
            }
            if (!tipoDocumentos.isEmpty()) {
                tipoDocumentos.clear();
            }
//            if (!infoDoc.isEmpty()) {
//                infoDoc.clear();
//            }
            if (!infoDocumentosArbol.isEmpty()) {
                infoDocumentosArbol.clear();
            }
//            }

            arbolDocumentos.setModel(new DefaultTreeModel(arbolDigitalizados));

            for (Enumeration e = ((TreeNode) arbolDocumentos.getModel().getRoot()).children(); e.hasMoreElements();) {
                TreeNode tn = (TreeNode) e.nextElement();
                arbolDocumentos.expandPath(new TreePath(((DefaultTreeModel) arbolDocumentos.getModel()).getPathToRoot(tn)));
            }

        } catch (SOAPException e) {
            traza.trace("Error al crear el arbol de documentos", Level.ERROR, e);
            JOptionPane.showMessageDialog(this, "problemas al construir el Expediente \n" + e.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
            Principal.desktop.removeAll();
            Principal.desktop.repaint();

        }
    }

    private void convertir(List<com.develcom.documento.InfoDocumento> documentos) {
        infoDocumentosArbol = new ArrayList<InfoDocumento>();
        InfoDocumento id;
        for (com.develcom.documento.InfoDocumento ide : documentos) {
            id = new InfoDocumento();

            id.setCantPaginas(ide.getCantPaginas());
            id.setCausaRechazo(ide.getCausaRechazo());
            id.setDatoAdicional(ide.getDatoAdicional());
            id.setEstatus(ide.getEstatus());
            id.setEstatusDocumento(ide.getEstatusDocumento());
            id.setFechaActual(ide.getFechaActual());
            id.setFechaAprobacion(ide.getFechaAprobacion());
            id.setFechaDigitalizacion(ide.getFechaDigitalizacion());
            id.setFechaRechazo(ide.getFechaRechazo());
            id.setFechaVencimiento(ide.getFechaVencimiento());
            id.setFolio(ide.getFolio());
            id.setFormato(ide.getFormato());
            id.setIdDocumento(ide.getIdDocumento());
            id.setIdExpediente(ide.getIdExpediente());
            id.setIdInfoDocumento(ide.getIdInfoDocumento());
            id.setMotivoRechazo(ide.getMotivoRechazo());
            id.setNombreArchivo(ide.getNombreArchivo());
            id.setNuevo(ide.isNuevo());
            id.setNumeroDocumento(ide.getNumeroDocumento());
            id.setReDigitalizo(ide.isReDigitalizo());
            id.setRutaArchivo(ide.getRutaArchivo());
            id.setTipoDocumento(ide.getTipoDocumento());
            id.setUsuarioAprobacion(ide.getUsuarioAprobacion());
            id.setUsuarioDigitalizo(ide.getUsuarioDigitalizo());
            id.setUsuarioRechazo(ide.getUsuarioRechazo());
            id.setVersion(ide.getVersion());
            id.setTipoDocDatoAdicional(ide.isTipoDocDatoAdicional());
            if (!ide.getLsDatosAdicionales().isEmpty()) {
                for (DatoAdicional da : ide.getLsDatosAdicionales()) {
                    com.develcom.elimina.DatoAdicional da1 = new com.develcom.elimina.DatoAdicional();
                    da1.setCodigo(da.getCodigo());
                    da1.setIndiceDatoAdicional(da.getIndiceDatoAdicional());
                    da1.setIdDatoAdicional(da.getIdDatoAdicional());
                    da1.setIdTipoDocumento(da.getIdTipoDocumento());
                    da1.setIdValor(da.getIdValor());
                    da1.setNumeroDocumento(da.getNumeroDocumento());
                    da1.setTipo(da.getTipo());
                    da1.setValor(da.getValor());
                    id.getLsDatosAdicionales().add(da1);
                }
            }

            infoDocumentosArbol.add(id);
        }
    }
//    private void version(int version) {
//        final MostrarProceso proceso = new MostrarProceso("Buscando la version " + version + " del documento");
//        proceso.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getVersion(versionSeleccionada);
//                traza.trace("mostrando la ventana", Level.INFO);
//
//                proceso.detener();
//            }
//        }).start();
//
//    }
    /**
     * Busca la version seleccionada del documento
     *
     * @param version La version seleccionada
     */
//    private void getVersion(int version) {
//
//        String ruta, archivo = null;
//        Bufer buffer;
//        BufferedReader leer;
//        PrintWriter escribir;
//        OutputStream escribiendo;
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//
//        traza.trace("version a buscar " + version, Level.INFO);
//
//        try {
//            for (InfoDocumento infoDoc : infoDocumentosArbol) {
//                if (infoDoc.getVersion() == version) {
//
//                    File fileCod = new File(toolsFile.getArchivoCodificado());
//                    if (toolsFile.getDirTemporal().exists()) {
//                        File[] files = toolsFile.getDirTemporal().listFiles();
//                        for (File f : files) {
//                            if (f.delete()) {
//                                traza.trace("eliminado archivo " + f.getName(), Level.INFO);
//                            } else {
//                                traza.trace("problemas al eliminar el archivo " + f.getName(), Level.WARN);
//                            }
//                        }
//                    }
//
//
//                    if (infoDoc.getFechaVencimiento() != null) {
//
//                        XMLGregorianCalendar xmlCalendar = infoDoc.getFechaVencimiento();
//                        GregorianCalendar fechaVencimiento = xmlCalendar.toGregorianCalendar();
//
//                        lblFechaVencimiento.setText("Fecha de Vencimiento: " + sdf.format(fechaVencimiento.getTime()));
//                        lblFechaVencimiento.setVisible(true);
//
//                    }
//
//                    if (archivo == null) {
//                        throw new DW4JDesktopExcepcion("Falta información del documento");
//                    }
//                    ruta = infoDoc.getRutaArchivo();
//                    archivo = infoDoc.getNombreArchivo();
//
//                    if (archivo == null) {
//                        throw new DW4JDesktopExcepcion("Falta información del documento");
//                    }
//                    buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);
//
//                    if (buffer == null) {
//                        throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
//                    }
//                    escribiendo = new FileOutputStream(fileCod);
//                    escribiendo.write(buffer.getBufer());
//                    escribiendo.flush();
//                    escribiendo.close();
//
//                    if (fileCod.exists()) {
//                        new ToolsFiles().decodificar();
//
//                        verVersion(toolsFile.getArchivoTif());
//                    }
//
//                }
//            }
//        } catch (DW4JDesktopExcepcion e) {
//            JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
//        } catch (SOAPException ex) {
//            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
//            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
//        } catch (IOException ex) {
//            traza.trace("Error al buscar la version del archivo", Level.ERROR, ex);
//            JOptionPane.showMessageDialog(this, "Error al buscar la version del archivo\n" + ex.getMessage(), "Error...", JOptionPane.ERROR_MESSAGE);
//        } catch (SOAPFaultException e) {
//            traza.trace("error al buscar el archivo " + e.getMessage(), Level.ERROR, e);
//            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//    }
    /**
     * Muestra la version seleccionada del documento
     *
     * @param ruta Ruta donde se aloja el archivo
     */
//    private void verVersion(String ruta) {
//        traza.trace("ruta del archivo a mostrar " + ruta, Level.INFO);
//        imagenesTiff = null;
//        try {
//            int i;
//            panelImagen.removeAll();
//            imagenesTiff = new ToolsFiles().open(ruta);
//            for (i = 0; i < imagenesTiff.size(); i++) {
//                ImagePanel imageTab = new ImagePanel();
//                JScrollPane sp = new JScrollPane(imageTab);
////                sp.getVerticalScrollBar().setUnitIncrement(100);
////                sp.getHorizontalScrollBar().setUnitIncrement(100);
//                imageTab.setImage(imagenesTiff.get(i));
//                panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
//            }
//
//        } catch (IOException ex) {
//            traza.trace("problemas al carga la version del documento", Level.ERROR, ex);
//            JOptionPane.showMessageDialog(this, "problemas al carga la version del documento\n" + ex.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
//        }
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbSubCategorias;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonRotDer;
    private javax.swing.JButton jButtonRotIzq;
    private javax.swing.JButton jButtonRotar;
    private javax.swing.JButton jButtonZommIn;
    private javax.swing.JButton jButtonZommOut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbEliminar;
    private javax.swing.JLabel lblFechaVencimiento;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField txtExpediente;
    // End of variables declaration//GEN-END:variables

    public JTree getTree() {
        return arbolDocumentos;
    }

//    public JTabbedPane getPanelImagen() {
//        return panelImagen;
//    }
//
    public static DefaultMutableTreeNode getSeleccionado() {
        return seleccionado;
    }

    public void setIdExpediente(String idExpediente) {
        this.idExpediente = idExpediente;
    }
}
