/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.documento.Bufer;
import com.develcom.documento.DatoAdicional;
import com.develcom.documento.InfoDocumento;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.expediente.GestionDocumentos;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Constantes;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.ToolsFiles;

/**
 *
 * @author develcom
 */
public class MuestraDocumento extends SelectorComposer<Component> {

    @Wire
    private Label fechaVencimiento;

    @Wire
    private Combobox cboVersion;

    @Wire
    private Button btnAbrir;

    @Wire
    private Iframe documento;

    @Wire
    private Label datosAdicionales;

    @Wire
    private Grid gridVersion;

    private InfoDocumento infoDocumento;
    private List<InfoDocumento> infoDocumentos;
    private static final long serialVersionUID = 150644697265214660L;
    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(MuestraDocumento.class);
    private Session session;
    private ToolsFiles toolsFile = new ToolsFiles();
    private Expediente expediente;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    private void iniciar() {
        
        session = herramientas.crearSesion();

        if (session != null) {

            fechaVencimiento.setVisible(false);

            infoDocumento = (InfoDocumento) session.getAttribute("infoDocumento");
            expediente = (Expediente) session.getAttribute("expediente");
            
            buscarDocumentos();
        } else {
            herramientas.warn("Termino la Sesión");
            herramientas.navegarPagina("index.zul");
        }
    }

    private void buscarDocumentos() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ruta = "", archivo = "", idExpediente;
        OutputStream escribiendoPDF;
        Bufer buffer;
        int cont = 0, idInfoDocumento, idDoc, version, numDoc, idCat, idSubCat;        
        File doc, fileCod;
        Comboitem item;

        try {
            session = herramientas.crearSesion();

            expediente = (Expediente) session.getAttribute("expediente");

            idInfoDocumento = infoDocumento.getIdInfoDocumento();
            idDoc = infoDocumento.getIdDocumento();
            version = infoDocumento.getVersion();
            numDoc = infoDocumento.getNumeroDocumento();
            idCat = expediente.getIdCategoria();
            idSubCat = expediente.getIdSubCategoria();
            idExpediente = expediente.getIdExpediente();
            traza.trace("buscando el documento idDocumento " + idDoc + " idCategoria " + idCat + " idSubCategoria " + idSubCat + " idExpediente " + idExpediente, Level.INFO);

            System.gc();
            infoDocumentos = new GestionArchivos().buscarImagenDocumentos(idInfoDocumento, idDoc, idCat, idSubCat, version, numDoc, idExpediente);

            if (infoDocumentos.size() > 0) {

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

                    if (version > 0) {
                        if (id.getVersion() == version) {
                            if (id.getFechaVencimiento() != null) {

                                XMLGregorianCalendar xmlCalendar = id.getFechaVencimiento();
                                GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
                                fechaVencimiento.setValue("Fecha de Vencimiento: " + sdf.format(rechaVencimiento.getTime()));
                                fechaVencimiento.setVisible(true);

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
                            fechaVencimiento.setValue("Fecha de Vencimiento: " + sdf.format(rechaVencimiento.getTime()));
                            fechaVencimiento.setVisible(true);

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

                for (InfoDocumento infoDoc : infoDocumentos) {

                    item = new Comboitem();
                    item.setLabel(String.valueOf(infoDoc.getVersion()));
                    item.setValue(infoDoc.getVersion());
                    item.setParent(cboVersion);
                    cont++;
                }

                if (infoDocumentos.size() == 1 || cont == 1) {
                    gridVersion.setVisible(false);
                    cboVersion.setVisible(false);
                    btnAbrir.setVisible(false);
                }

                traza.trace("ruta a buscar " + ruta, Level.INFO);
                traza.trace("archivo a buscar " + archivo, Level.INFO);
                traza.trace("version del documento a mostrar " + version, Level.INFO);

                //jlMensaje4.setText("Tipo de Documento: "+infoDocumento.getTipoDocumento()+" \"ULTIMA VERSION: "+version+"\"");
                if (archivo == null) {
                    throw new Exception("Falta información del documento");
                }
                //busca el archivo fisico del tipo de documento
                buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);

                if (!buffer.isExiste()) {
                    throw new Exception("Problemas al buscar el fisico del documento");
                }
                fileCod = new File(toolsFile.getArchivoCodificado());
                escribiendoPDF = new FileOutputStream(fileCod);
                escribiendoPDF.write(buffer.getBufer());
                escribiendoPDF.flush();
                escribiendoPDF.close();

                doc = new File("documento" + Constantes.CONTADOR++ + "." + infoDocumento.getFormato());

                toolsFile.decodificar(doc.getName());

                mostarDocumento(toolsFile.getTempPath(), doc.getName(), infoDocumento.getFormato(), version);

            } else {
                throw new IOException("Documento o Archivo no encontrado");
            }

        } catch (SOAPException | SOAPFaultException ex) {
            herramientas.error("Problemas con los Servicios Web", ex);
            traza.trace("problemas webswervice", Level.ERROR, ex);
        } catch (FileNotFoundException ex) {
            herramientas.error("Archivo no encontrado", ex);
            traza.trace("problemas archivo no encontrado", Level.ERROR, ex);
        } catch (IOException ex) {
            herramientas.error("Problemas al crear el documento", ex);
            traza.trace("problemas i/o", Level.ERROR, ex);
        } catch (Exception ex) {
            herramientas.error("Problema al visualizar el documento", ex);
            traza.trace("problemas general", Level.ERROR, ex);
        }
    }

    @Listen("onClick = #btnAbrir")
    public void getVersion() {
        int version = cboVersion.getSelectedItem().getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ruta = "", archivo = "";
        OutputStream escribiendoPDF;
        Bufer buffer;
        File doc, fileCod;

        try {
            for (InfoDocumento infoDoc : infoDocumentos) {

                if (infoDoc.getVersion() == version) {
                    documento.setContent(null);
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

                    if (infoDoc.getFechaVencimiento() != null) {

                        XMLGregorianCalendar xmlCalendar = infoDoc.getFechaVencimiento();
                        GregorianCalendar rechaVencimiento = xmlCalendar.toGregorianCalendar();
                        fechaVencimiento.setValue("Fecha de Vencimiento: " + sdf.format(rechaVencimiento.getTime()));
                        fechaVencimiento.setVisible(true);

                        ruta = infoDoc.getRutaArchivo();
                        archivo = infoDoc.getNombreArchivo();
                        version = infoDoc.getVersion();

                    } else {
                        ruta = infoDoc.getRutaArchivo();
                        archivo = infoDoc.getNombreArchivo();
                        version = infoDoc.getVersion();
                    }

                    traza.trace("ruta a buscar " + ruta, Level.INFO);
                    traza.trace("archivo a buscar " + archivo, Level.INFO);
                    traza.trace("version del documento a mostrar " + version, Level.INFO);

                    //jlMensaje4.setText("Tipo de Documento: "+infoDocumento.getTipoDocumento()+" \"ULTIMA VERSION: "+version+"\"");
                    if (archivo == null) {
                        throw new Exception("Falta información del documento");
                    }
                    //busca el archivo fisico del tipo de documento
                    buffer = new GestionArchivos().buscandoArchivo(ruta, archivo);

                    if (!buffer.isExiste()) {
                        throw new Exception("Problemas al buscar el fisico del documento");
                    }
                    fileCod = new File(toolsFile.getArchivoCodificado());
                    escribiendoPDF = new FileOutputStream(fileCod);
                    escribiendoPDF.write(buffer.getBufer());
                    escribiendoPDF.flush();
                    escribiendoPDF.close();

                    doc = new File("documento" + Constantes.CONTADOR++ + "." + infoDoc.getFormato());

                    toolsFile.decodificar(doc.getName());

                    mostarDocumento(toolsFile.getTempPath(), doc.getName(), infoDoc.getFormato(), version);
                }
            }
        } catch (SOAPException | SOAPFaultException ex) {
            herramientas.error("Problemas con los Servicios Web", ex);
            traza.trace("problemas webswervice", Level.ERROR, ex);
        } catch (FileNotFoundException ex) {
            herramientas.error("Archivo no encontrado", ex);
            traza.trace("problemas archivo no encontrado", Level.ERROR, ex);
        } catch (IOException ex) {
            herramientas.error("Problemas al crear el documento", ex);
            traza.trace("problemas i/o", Level.ERROR, ex);
        } catch (Exception ex) {
            herramientas.error("Problema general al visualizar la version del documento", ex);
            traza.trace("problemas general", Level.ERROR, ex);
        }
    }

    private void mostarDocumento(String ruta, String nombre, String formato, int version) throws FileNotFoundException, IOException, SOAPException {
        AMedia media;
        File file;
        ByteArrayInputStream is;
        FileInputStream fs;
        byte[] buffer;
        List<com.develcom.documento.DatoAdicional> lsDatosAdicionales;
        int size, i = 1;
        String da = "";

        if (infoDocumento.isTipoDocDatoAdicional()) {
            datosAdicionales.setValue("");
            lsDatosAdicionales = new GestionDocumentos().encontrarValorDatoAdicional(expediente.getIdTipoDocumento(), expediente.getIdExpediente(), infoDocumento.getNumeroDocumento(), version);
            size = lsDatosAdicionales.size();
            if (!lsDatosAdicionales.isEmpty()) {

                for (DatoAdicional datAd : lsDatosAdicionales) {

                    if (i == size) {
                        da = da + " " + datAd.getValor();
                    } else {
                        da = da + " " + datAd.getValor() + " - ";
                    }
                    i++;
                }
            }
            datosAdicionales.setValue("Datos Adicionales: \"" + da+"\"");
        }

        if (formato.equalsIgnoreCase("pdf")) {

            file = new File(ruta + nombre);
            buffer = new byte[(int) file.length()];
            fs = new FileInputStream(file);
            fs.read(buffer);
            fs.close();
            is = new ByteArrayInputStream(buffer);

            media = new AMedia("documento", "pdf", "application/pdf", is);
            documento.setContent(media);

        } else if ((formato.equalsIgnoreCase("jpg")) || (formato.equalsIgnoreCase("jpge"))) {

            file = toolsFile.guardarArchivoJPGtoPDF(ruta + nombre);
            buffer = new byte[(int) file.length()];
            fs = new FileInputStream(file);
            fs.read(buffer);
            fs.close();
            is = new ByteArrayInputStream(buffer);

            media = new AMedia("documento", "pdf", "application/pdf", is);
            documento.setContent(media);

        } else if ((formato.equalsIgnoreCase("tif")) || (formato.equalsIgnoreCase("tiff"))) {

            file = toolsFile.guardarArchivoTIFFtoPDF(ruta + nombre);
            buffer = new byte[(int) file.length()];
            fs = new FileInputStream(file);
            fs.read(buffer);
            fs.close();
            is = new ByteArrayInputStream(buffer);

            media = new AMedia("documento", "pdf", "application/pdf", is);
            documento.setContent(media);

        }else{
            herramientas.warn("Hubo un problema en la conversión del documento, se regresará");
            regresar();
        }
    }

//    @Listen("onClick = #datosAdd")
//    public void mostrarDatosAdicionales() {
//        int version = infoDocumento.getVersion();
//        if (infoDocumento.isTipoDocDatoAdicional()) {
//            if (cboVersion.isVisible()) {
//                version = cboVersion.getSelectedItem().getValue();
//            }
//            session.setAttribute("version", version);
//            ((Window) Executions.createComponents("/datosAdicionales.zul", null, null)).doModal();
//        }
//    }

    @Listen("onClick = #exit")
    public void salir() {

        try {
            System.runFinalization();
            System.gc();

            documento.setContent(null);
//            documento.detach();
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

            if (session != null) {
                herramientas.cerrarSesion();
                herramientas.navegarPagina("index.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al salir", Level.ERROR, ex);
        }
    }

    @Listen("onClick = #regresa")
    public void regresar() {

        System.runFinalization();
        System.gc();
        try {

            documento.setContent(null);
//            documento.detach();
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

            if (session != null) {
                herramientas.navegarPagina("arbol.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al regresar", Level.ERROR, ex);
        }
    }

}
