package com.develcom.gui.calidad;

import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.dao.Expediente;
import com.develcom.dao.ManejoSesion;
import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import com.develcom.excepcion.DW4JDesktopExcepcion;
import com.develcom.gui.Principal;
import com.develcom.gui.reportes.tools.Foliatura;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import uk.co.mmscomputing.application.imageviewer.ImagePanel;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.expediente.GestionDocumentos;

/**
 *
 * @author develcom
 */
public class CalidadDocumento extends javax.swing.JInternalFrame {

    private static DefaultMutableTreeNode seleccionado;
    private static final long serialVersionUID = -1649640288983172921L;
    private double scalaImagen = 1;
    private JTabbedPane panelImagen = new JTabbedPane();
    private JTree arbolDocumentos = new JTree();
    private int versionSeleccionada;
    private String idExpedienteCalidad;
    /**
     * Lista de bufer de imagenes
     */
    private ArrayList<BufferedImage> imagenesTiff;
    /**
     * escribe trazas en el log
     */
    private Traza traza = new Traza(CalidadDocumento.class);
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
    private List<InfoDocumento> infoDocumentos = new ArrayList<InfoDocumento>();
    /**
     * Listado de tipos de docuemtos digitalizados
     */
    private List<InfoDocumento> documentosPendientes = new ArrayList<InfoDocumento>();
    /**
     * objecto de informacion del documento
     */
    private InfoDocumento infoDocumento;
    private int idInfoDocumento;
    private ToolsFiles toolsFile = new ToolsFiles();

    public CalidadDocumento(String idExpediente) {
//        CreaObjetosDinamicos uv = new CreaObjetosDinamicos();

        expediente = ManejoSesion.getExpediente();
        idExpedienteCalidad = idExpediente;
        initComponents();
        
        if(!ManejoSesion.getConfiguracion().isFoliatura()){
            jbtFoliatura.setVisible(false);
        }

        if (Constantes.ACCION.equalsIgnoreCase("APROBAR")) {
            cboVersion.setVisible(false);
            btnAbrir.setVisible(false);
            if (ManejoSesion.getConfiguracion().isCalidadActivo()) {
                jbRechazar.setVisible(true);
                jbAprobar.setVisible(true);
            } else {
                jbRechazar.setVisible(false);
                jbAprobar.setVisible(false);
            }
        } else {
            cboVersion.setEnabled(false);
            btnAbrir.setEnabled(false);
            jbRechazar.setVisible(false);
            jbAprobar.setVisible(false);
        }

        jlFechaVencimiento.setVisible(false);
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

//        uv.mostrarIndices(ManejoSesion.getExpediente());
//        setTitle(uv.crearTituloExpediente());
//        arboles(idExpediente);
        buscar(idExpediente);
        //setVisible(true);
    }

    private void buscar(final String idExpediente) {
        final CreaObjetosDinamicos uv = new CreaObjetosDinamicos();
        final MostrarProceso proceso = new MostrarProceso("Armado el Expedente " + idExpediente);
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                uv.mostrarIndices(ManejoSesion.getExpediente());
                setTitle(uv.crearTituloExpediente());
                mostrarArbol(idExpediente);
                proceso.detener();
                setVisible(true);
            }
        }).start();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        List<com.develcom.documento.DatoAdicional> datosAdicionales = new ArrayList<com.develcom.documento.DatoAdicional>();

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
                            traza.trace("id de la subCategoria seleccionada " + sc.getIdSubCategoria(), Level.INFO);
                            break;
                        }
                    }
                }
                if (ManejoSesion.getExpediente().getIdSubCategoria() == 0) {
                    traza.trace("no se tiene el id de la subCategoria", Level.WARN);
                }

                traza.trace("buscando la extencion del tipo de documento " + tipoDocumento, Level.INFO);
                for (InfoDocumento infDoc : documentosPendientes) {
                    String tipoDoc = infDoc.getTipoDocumento();
                    tipoDoc = tipoDoc.trim();
                    traza.trace("tipo de documento a comparar " + tipoDoc, Level.INFO);

                    if (tipoDocumento.contains(tipoDoc)) {

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
                        if (infoDocumento != null) {
                            infoDocumento = null;
                        }
                        infoDocumento = infDoc;

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

                        VerDocumentoPDF vdpdf = new VerDocumentoPDF(infoDocumento, this);
                        Principal.desktop.add(vdpdf);
                        vdpdf.toFront();
                        this.toBack();
                    } else if (formato.equalsIgnoreCase("jpg") || formato.equalsIgnoreCase("jpeg")) {

                        int tabs = panelImagen.getTabCount();

                        if (tabs >= 1) {
                            panelImagen.removeAll();
                        }

                        VerImagenes vi = new VerImagenes(infoDocumento, this);
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

    private void arboles(final String idExpediente) {

        final MostrarProceso proceso = new MostrarProceso("Construyendo el expediente " + idExpediente);
        proceso.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mostrarArbol(idExpediente);
                proceso.detener();
                setVisible(true);
            }
        }).start();
    }

    /**
     * Construye el arbol con los tipos de documentos digitalizados
     *
     * @param idExpediente el id del expediente selccionado
     */
    private void mostrarArbol(String idExpediente) {

        List<TipoDocumento> tipoDocumentos;
        List<Integer> idDocumento = new ArrayList<>();
        String cat, nombreTD, tmp[], datoAdicional;
        int idCat, totalDocDig, cont = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DefaultMutableTreeNode arbolDigitalizados = new DefaultMutableTreeNode();
        DefaultMutableTreeNode ramaDigitalizados = null;

        List<com.develcom.documento.DatoAdicional> datosAdicionales = new ArrayList<com.develcom.documento.DatoAdicional>();
        traza.trace("armando el arbol con el expediente " + idExpediente, Level.INFO);

        try {

            arbolDocumentos.setModel(null);

            idCat = expediente.getIdCategoria();
            cat = expediente.getCategoria();

            traza.trace("id categoria: " + idCat + " - Categoria: " + cat, Level.INFO);
            traza.trace("buscando los tipos de documentos disponibles", Level.INFO);

            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria(null, idCat, 0);
            traza.trace("tamaño lista de SubCategoria " + listaSubCategorias.size(), Level.INFO);

            arbolDigitalizados.setUserObject(cat + " - " + expediente.getIdExpediente());

            for (SubCategoria sc : listaSubCategorias) {

                traza.trace("subCategoria " + sc.getSubCategoria(), Level.INFO);
                tipoDocumentos = new AdministracionBusqueda().buscarTipoDocumento(null, idCat, sc.getIdSubCategoria());

                for (TipoDocumento td : tipoDocumentos) {
                    if (td.getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                        idDocumento.add(td.getIdTipoDocumento());
                    }
                }

                if (!idDocumento.isEmpty()) {
                    //busca los tipos de documentos digitalizados
                    traza.trace("busca informacion de los documentos de la SubCategoria " + sc.getSubCategoria() + " su id " + sc.getIdSubCategoria(), Level.INFO);
                    infoDocumentos = new GestionDocumentos().encontrarInformacionDoc(idDocumento, idExpediente, idCat, sc.getIdSubCategoria(), 0, 0, false);
                    traza.trace("tamaño lista de InfoDocumento " + infoDocumentos.size(), Level.INFO);

                    if (!infoDocumentos.isEmpty()) {

                        traza.trace("rama subCategoria " + sc.getSubCategoria(), Level.INFO);
                        ramaDigitalizados = new DefaultMutableTreeNode(sc.getSubCategoria());

                        totalDocDig = infoDocumentos.size();
                        traza.trace("total documentos digitalizados de la subCategoria " + sc.getSubCategoria(), Level.INFO);

                        tmp = new String[totalDocDig];

                        for (InfoDocumento id : infoDocumentos) {

                            nombreTD = id.getTipoDocumento() + " - " + id.getNumeroDocumento();
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
                                if (id.isTipoDocDatoAdicional()) {
                                    datosAdicionales.clear();
                                    datosAdicionales = id.getLsDatosAdicionales();
                                    int size = datosAdicionales.size(), i = 1;
                                    for (com.develcom.documento.DatoAdicional datAd : datosAdicionales) {

                                        if (datAd.getTipo().equalsIgnoreCase("fecha")) {
//                                            XMLGregorianCalendar calendar = (XMLGregorianCalendar) datAd.getValor();
//                                            Calendar cal = calendar.toGregorianCalendar();
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
                                DefaultMutableTreeNode hijo;
                                if (id.isTipoDocDatoAdicional()) {
                                    datoAdicional = nombreTD + " " + id.getEstatusDocumento() + " (" + da + ")";
                                    hijo = new DefaultMutableTreeNode(datoAdicional);
                                } else {
                                    datoAdicional = nombreTD + " " + id.getEstatusDocumento();
                                    hijo = new DefaultMutableTreeNode(datoAdicional);
                                }
                                id.setTipoDocumento(datoAdicional);
                                documentosPendientes.add(id);
                                ramaDigitalizados.add(hijo);
//                                ramaDigitalizados.add(new DefaultMutableTreeNode(nombreTD + " (" + da.trim() + ")"));
                                traza.trace("tipo documento " + nombreTD + " de la subCategoria " + sc.getSubCategoria(), Level.INFO);
                            }
                        }
                        arbolDigitalizados.add(ramaDigitalizados);
                    }
                } else {
                    traza.trace("Problema con la lista de documento de la SubCategoria " + sc.getSubCategoria(), Level.INFO);
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
            ResultadoExpedienteCalidad re = new ResultadoExpedienteCalidad();
            re.convertir();

        }

    }

    private void buscandoDocumentoFisico() {
        final MostrarProceso proceso = new MostrarProceso("Buscando y decodificando el documento");
        proceso.start();
        //proceso.mensaje("Armando los arboles");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mostrarDocumentos();
                traza.trace("mostrando la ventana", Level.INFO);

                proceso.detener();
            }
        }).start();

    }

    /**
     * Busca y muestra el documento previamente digitalizado
     */
    private void mostrarDocumentos() {

        String ruta = "", archivo = "";
        //String buffer;
//        BufferedReader leer;
//        PrintWriter escribir;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int contDocAprob = 0, contDocPend = 0;
        //byte[] buffer;
        Bufer buffer;
        FileOutputStream escribiendo;

        try {

            jlFechaVencimiento.setVisible(false);
            idInfoDocumento = infoDocumento.getIdInfoDocumento();
            int idDoc = infoDocumento.getIdDocumento();
            versionSeleccionada = infoDocumento.getVersion();
            int numDoc = infoDocumento.getNumeroDocumento();
            int idCat = ManejoSesion.getExpediente().getIdCategoria();
            int idSubCat = ManejoSesion.getExpediente().getIdSubCategoria();
            String idExpediente = ManejoSesion.getExpediente().getIdExpediente();

            if (idCat == 0) {
                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idCat) " + idCat);
            }
            if (idSubCat == 0) {
                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idCat) " + idSubCat);
            }
            if (idExpediente == null) {
                throw new DW4JDesktopExcepcion("Problemas con los datos del Documento (idExpediente) " + idExpediente);
            }

            traza.trace("buscando el documento: idInfoDocumento " + idInfoDocumento + " idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);
            infoDocumentos = new GestionArchivos().buscarImagenDocumentos(idInfoDocumento, idDoc, idCat, idSubCat, versionSeleccionada, numDoc, idExpediente);

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

//                    if (Constantes.ACCION.equalsIgnoreCase("CAPTURAR")) {
                    if (id.getFechaVencimiento() != null) {

                        XMLGregorianCalendar xmlCalendar = id.getFechaVencimiento();
                        GregorianCalendar fechaVencimiento = xmlCalendar.toGregorianCalendar();

                        jlFechaVencimiento.setText(sdf.format(fechaVencimiento.getTime()));
                        jlFechaVencimiento.setVisible(true);

                        ruta = id.getRutaArchivo();
                        archivo = id.getNombreArchivo();
                        versionSeleccionada = id.getVersion();
                        break;

                    } else {
                        ruta = id.getRutaArchivo();
                        archivo = id.getNombreArchivo();
                        versionSeleccionada = id.getVersion();
                        break;
                    }
//                    }
                }

                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + versionSeleccionada, Level.INFO);

                if (archivo == null) {
                    throw new DW4JDesktopExcepcion("Falta información del documento");
                }

                //busca el archivo fisico del tipo de documento
                buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);
//
//                leer = new BufferedReader(new StringReader(buffer));
//                escribir = new PrintWriter(new BufferedWriter(new FileWriter(fileCod)));
//
//                while ((buffer = leer.readLine()) != null) {
//                    escribir.println(buffer);
//                }
//                escribir.close();

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
                        //panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), bar);
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
                    } else if (contDocAprob > 1) {
                        cboVersion.setEnabled(true);
                        btnAbrir.setEnabled(true);
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

        if (!infoDocumentos.isEmpty()) {
            infoDocumentos.clear();
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

        barra = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jButtonZommOut = new javax.swing.JButton();
        jButtonZommIn = new javax.swing.JButton();
        jButtonRotar = new javax.swing.JButton();
        jButtonRotIzq = new javax.swing.JButton();
        jlFechaVencimiento = new javax.swing.JLabel();
        jButtonCerrar = new javax.swing.JButton();
        jbtFoliatura = new javax.swing.JButton();
        jbAprobar = new javax.swing.JButton();
        jbRechazar = new javax.swing.JButton();
        jButtonRotDer = new javax.swing.JButton();
        cboVersion = new javax.swing.JComboBox();
        btnAbrir = new javax.swing.JButton();
        btnVerIndices = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();

        barra.setFloatable(false);
        barra.setRollover(true);

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

        jlFechaVencimiento.setText("jLabel1");

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

        jbtFoliatura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/reporte/1344612993_document.png"))); // NOI18N
        jbtFoliatura.setText("Foliatura");
        jbtFoliatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtFoliaturaActionPerformed(evt);
            }
        });

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

        btnVerIndices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/indicessmal.png"))); // NOI18N
        btnVerIndices.setToolTipText("Mostrar Indices");
        btnVerIndices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerIndicesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(cboVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jbAprobar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRechazar)
                .addGap(63, 63, 63)
                .addComponent(btnVerIndices)
                .addGap(60, 60, 60)
                .addComponent(jlFechaVencimiento)
                .addGap(63, 63, 63)
                .addComponent(jbtFoliatura, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonZommOut, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonZommIn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRotar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRotIzq, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRotDer, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jbAprobar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVerIndices, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jbtFoliatura, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jlFechaVencimiento)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        barra.add(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(splitPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
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

    private void jbAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAprobarActionPerformed

        String tipoDoc = infoDocumento.getTipoDocumento().trim();
        boolean aprobado;
        String usuario = ManejoSesion.getLogin();
        int tabs;
        DefaultMutableTreeNode dmtn;

        try {
            dmtn = (DefaultMutableTreeNode) arbolDocumentos.getLastSelectedPathComponent();

            if (dmtn.isLeaf()) {

                int n = JOptionPane.showOptionDialog(this,
                        //"Seguro que desea aprobar el documento " + tipoDoc + " version " + versionSeleccionada + " \n(" + idInfoDocumento + ")",
                        "Seguro que desea aprobar el documento " + tipoDoc + " version " + versionSeleccionada,
                        "¿?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"SI", "NO"}, "NO");

                if (n == JOptionPane.YES_OPTION) {

                    tabs = panelImagen.getTabCount();
                    traza.trace("aprobando " + tabs + " paginas", Level.INFO);

                    if (tabs >= 1) {

                        traza.trace("aprobando el tipo de documento " + tipoDoc + " " + idInfoDocumento, Level.INFO);

                        if (idInfoDocumento != 0) {

                            aprobado = new ve.com.develcom.aprueba.CalidadDocumento().aprobarDoc(idInfoDocumento, usuario);

                            traza.trace("exito aprobado " + aprobado, Level.INFO);

                            if (aprobado) {
                                DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) seleccionado;
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

                                arbolDocumentos.updateUI();
                                panelImagen.removeAll();

                                JOptionPane.showMessageDialog(this, "Documento aprobado con exito", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Problemas al aprobar el documento", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay documento que aprobar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe selecionar un tipo de documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

    }//GEN-LAST:event_jbAprobarActionPerformed

    private void jbRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRechazarActionPerformed

        boolean exito;
        DefaultMutableTreeNode dmtn;

        dmtn = (DefaultMutableTreeNode) arbolDocumentos.getLastSelectedPathComponent();

        if (dmtn.isLeaf()) {
            traza.trace("rechazando el tipo de documento " + infoDocumento.getTipoDocumento() + " su id " + idInfoDocumento, Level.INFO);
            RechazarDocumento rd = new RechazarDocumento(idInfoDocumento, infoDocumento.getTipoDocumento(), versionSeleccionada);
            exito = rd.isResultado();

            traza.trace("exito rechazo " + exito, Level.INFO);
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

                arbolDocumentos.updateUI();
                panelImagen.removeAll();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe selecionar un tipo de documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jbRechazarActionPerformed

    private void jButtonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarActionPerformed
        this.dispose();

        Principal.desktop.removeAll();
        Principal.desktop.repaint();
        ResultadoExpedienteCalidad re = new ResultadoExpedienteCalidad();

        try {
            documentosPendientes.clear();
            TreeModel model = arbolDocumentos.getModel();
            DefaultMutableTreeNode verificador = (DefaultMutableTreeNode) model.getRoot();//jtrDocDigit.getLastSelectedPathComponent();
            DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) verificador.getRoot();
            TreeNode tn = raiz.getFirstChild();
        } catch (NoSuchElementException e) {
            re.setIdExpedienteCalidad(idExpedienteCalidad);
        } catch (NullPointerException e) {
        }
        re.convertir();
        re.toFront();
        System.gc();
        Principal.desktop.add(re);

    }//GEN-LAST:event_jButtonCerrarActionPerformed

    private void jbtFoliaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtFoliaturaActionPerformed

        Expediente expedient = ManejoSesion.getExpediente();
        int idLib = expedient.getIdLibreria();
        int idCat = expedient.getIdCategoria();
        String idExpediente = expedient.getIdExpediente();

        traza.trace("imprimiendo la foliatura", Level.INFO);
        traza.trace("idLibreria: " + idLib, Level.INFO);
        traza.trace("idCategoria: " + idCat, Level.INFO);
        traza.trace("idExpediente: " + idExpediente, Level.INFO);

        foliaturas(idLib, idCat, idExpediente);

    }//GEN-LAST:event_jbtFoliaturaActionPerformed

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

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirActionPerformed

        versionSeleccionada = Integer.parseInt(cboVersion.getSelectedItem().toString());

        //getVersion(versionSeleccionada);
        version(versionSeleccionada);
    }//GEN-LAST:event_btnAbrirActionPerformed

    private void btnVerIndicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerIndicesActionPerformed
        
        VisorIndices vi = new VisorIndices();
        
    }//GEN-LAST:event_btnVerIndicesActionPerformed

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

        String ruta, archivo = null;
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

                    if (infoDoc.getFechaVencimiento() != null) {

                        XMLGregorianCalendar xmlCalendar = infoDoc.getFechaVencimiento();
                        GregorianCalendar fechaVencimiento = xmlCalendar.toGregorianCalendar();

                        jlFechaVencimiento.setText(sdf.format(fechaVencimiento.getTime()));
                        jlFechaVencimiento.setVisible(true);

                    }

                    if (archivo == null) {
                        throw new DW4JDesktopExcepcion("Falta información del documento");
                    }
                    ruta = infoDoc.getRutaArchivo();
                    archivo = infoDoc.getNombreArchivo();

                    if (archivo == null) {
                        throw new DW4JDesktopExcepcion("Falta información del documento");
                    }
                    buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);

//                    leer = new BufferedReader(new StringReader(buffer));
//                    escribir = new PrintWriter(new BufferedWriter(new FileWriter(fileCod)));
//
//                    while ((buffer = leer.readLine()) != null) {
//                        escribir.println(buffer);
//                    }
//                    escribir.close();
                    if (buffer == null) {
                        throw new DW4JDesktopExcepcion("Problemas al buscar el fisico del documento\nComuniquese con el administrador del sistema");
                    }
                    escribiendo = new FileOutputStream(fileCod);
                    //escribiendo.write(buffer);
                    escribiendo.write(buffer.getBufer());
                    escribiendo.flush();
                    escribiendo.close();
//                    toolsFile.clean();

                    if (fileCod.exists()) {
                        new ToolsFiles().decodificar();

                        verVersion(toolsFile.getArchivoTif());
                    }

                }
            }
        } catch (DW4JDesktopExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
        } catch (SOAPException ex) {
            traza.trace("error al buscar el archivo " + ex.getMessage(), Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "Error general al buscar el archivo.\n Documento o Archivo no encontrado", "Error...", JOptionPane.DEFAULT_OPTION + JOptionPane.ERROR_MESSAGE);
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
//                sp.getVerticalScrollBar().setUnitIncrement(100);
//                sp.getHorizontalScrollBar().setUnitIncrement(100);
                imageTab.setImage(imagenesTiff.get(i));
                panelImagen.addTab("Pag." + (i + 1), new ImageIcon(getClass().getResource("/com/develcom/gui/imagenes/develcom/Properties16.gif")), sp);
            }

        } catch (IOException ex) {
            traza.trace("problemas al carga la version del documento", Level.ERROR, ex);
            JOptionPane.showMessageDialog(this, "problemas al carga la version del documento\n" + ex.getMessage(), "Alerta...", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barra;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnVerIndices;
    private javax.swing.JComboBox cboVersion;
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JButton jButtonRotDer;
    private javax.swing.JButton jButtonRotIzq;
    private javax.swing.JButton jButtonRotar;
    private javax.swing.JButton jButtonZommIn;
    private javax.swing.JButton jButtonZommOut;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbAprobar;
    private javax.swing.JButton jbRechazar;
    private javax.swing.JButton jbtFoliatura;
    private javax.swing.JLabel jlFechaVencimiento;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

    public JTree getTree() {
        return arbolDocumentos;
    }

    public JTabbedPane getPanelImagen() {
        return panelImagen;
    }

    public static DefaultMutableTreeNode getSeleccionado() {
        return seleccionado;
    }
}
