/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas.arbol;

import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.dao.Expediente;
import com.develcom.documento.InfoDocumento;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.paginas.arbol.modelo.NodoTipoDocumento;
import com.develcom.tool.CreaArchivoJNLP;
import com.develcom.tool.Herramientas;
import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Level;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.expediente.GestionDocumentos;

/**
 *
 * @author develcom
 */
@ManagedBean
@RequestScoped
//@ViewScoped
//@SessionScoped
public class Arbol implements Serializable {

    private static final long serialVersionUID = -4951040424524924747L;
    private TreeNode raiz;
    private TreeNode hojaSeleccionada = null;
    private Traza traza = new Traza(Arbol.class);
    private HtmlPanelGrid panelIndicesDinamico = new HtmlPanelGrid();
    private Expediente expediente;
    private List<InfoDocumento> infoDocumentos = new ArrayList<InfoDocumento>();
//    private int idInfoDocumento;
    private HttpSession session;
    private Herramientas herramientas = new Herramientas();
    private String login;
    private boolean ficha;
    private boolean verDoc;
    private StreamedContent archivo;

    /**
     * Creates a new instance of Arbol
     */
    public Arbol() {
        Properties propiedades;

        try {
            session = herramientas.crearSesion();

            if (session != null) {

                login = session.getAttribute("login").toString();
                ficha = Boolean.parseBoolean(session.getAttribute("ficha").toString());
                expediente = (Expediente) session.getAttribute("expediente");
                construirArbol();
            }

        } catch (Exception e) {
            herramientas.error(e.getMessage());
            traza.trace("error al inicializar arbol ", Level.ERROR, e);
            try {
                herramientas.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                traza.trace("error", Level.ERROR, ex);
            }
        }
    }

    /**
     * Construye el arbol de tipos de documentos segun la busqueda previamente
     * realizada
     */
    private void construirArbol() {

        List<TipoDocumento> listaTipoDoc;
        List<SubCategoria> subCategorias;

        int idCat, totalDocDig, cont = 0;
//        List<com.develcom.documento.TipoDocumento> tipoDocumentos = new ArrayList<com.develcom.documento.TipoDocumento>();
        List<InfoDocumento> infoDocumentosBuscados = new ArrayList<InfoDocumento>();
        GestionDocumentos btd = new GestionDocumentos();
        List<Integer> idDocumento = new ArrayList<Integer>();
        String nombreTD, tmp[], idExpediente;

        DefaultTreeNode categoria;
        DefaultTreeNode subCategoria;
        List<DefaultTreeNode> nodosSubCategorias = new ArrayList<DefaultTreeNode>();
        NodoTipoDocumento tipoDocumento;

        try {

            if (session != null) {

                listaTipoDoc = expediente.getTipoDocumentos();
                idExpediente = expediente.getIdExpediente();
                subCategorias = expediente.getSubCategorias();

                if (subCategorias.isEmpty()) {
                    subCategorias = new AdministracionBusqueda().buscarSubCategoria("", expediente.getIdCategoria(), 0);
//                    subCategorias = new GestionExpediente().encuentraSubCategoria(expediente.getIdCategoria());
                }

                raiz = new DefaultTreeNode("raiz", null);
                categoria = new DefaultTreeNode(expediente.getCategoria(), raiz);

                for (SubCategoria sc : subCategorias) {

                    if (!listaTipoDoc.isEmpty()) {

                        for (TipoDocumento td : listaTipoDoc) {
                            if (td.getEstatus().equalsIgnoreCase("ACTIVO")) {
                                idDocumento.add(td.getIdTipoDocumento());
                            }
                        }

                    } else {

                        listaTipoDoc = new AdministracionBusqueda().buscarTipoDocumento(idExpediente, sc.getIdCategoria(), sc.getIdSubCategoria());

                        for (TipoDocumento td : listaTipoDoc) {
                            if (td.getEstatus().equalsIgnoreCase("ACTIVO")) {
                                idDocumento.add(td.getIdTipoDocumento());
                            }
                        }
                    }

                    if (!idDocumento.isEmpty()) {

                        //busca los tipos de documentos digitalizados
                        traza.trace("busca info documento del idSubCategoria " + sc.getIdSubCategoria(), Level.INFO);
                        infoDocumentosBuscados = btd.encontrarInformacionDoc(idDocumento, idExpediente, expediente.getIdCategoria(), sc.getIdSubCategoria(), 1, 0, false);

                        if (!infoDocumentosBuscados.isEmpty()) {

                            subCategoria = new DefaultTreeNode(sc.getSubCategoria(), categoria);

                            totalDocDig = infoDocumentosBuscados.size();

                            tmp = new String[totalDocDig];

                            for (InfoDocumento id : infoDocumentosBuscados) {

                                infoDocumentos.add(id);

                                nombreTD = id.getTipoDocumento() + " - " + id.getNumeroDocumento();
                                id.setTipoDocumento(nombreTD);
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
                                        List<com.develcom.documento.DatoAdicional> datosAdicionales = id.getLsDatosAdicionales();
                                        int size = datosAdicionales.size(), i = 1;
                                        for (com.develcom.documento.DatoAdicional datAd : datosAdicionales) {

                                            if (datAd.getTipo().equalsIgnoreCase("fecha")) {

                                                if (i == size) {
                                                    da = da + " " + datAd.getValor().toString();
                                                } else {
                                                    da = da + " " + datAd.getValor().toString() + ",";
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
                                        tipoDocumento = new NodoTipoDocumento(id.getTipoDocumento() + " (" + da + ")", id.getIdInfoDocumento(), subCategoria);
                                    } else {
                                        tipoDocumento = new NodoTipoDocumento(id.getTipoDocumento(), id.getIdInfoDocumento(), subCategoria);
                                    }
                                    traza.trace("tipo de documento digitalizado " + id.getTipoDocumento(), Level.INFO);
                                }
                            }
                        }
                    }

                    cont = 0;
                    if (!idDocumento.isEmpty()) {
                        idDocumento.clear();
                    }
                    if (!listaTipoDoc.isEmpty()) {
                        listaTipoDoc.clear();
                    }
                    if (!infoDocumentosBuscados.isEmpty()) {
                        infoDocumentosBuscados.clear();
                    }
                }

            } else {
                herramientas.navegarPagina("index");
            }

        } catch (Exception e) {
            herramientas.error(e.getMessage());
            traza.trace("Error al crear el arbol de documentos", Level.ERROR, e);
        }

    }

    private String crearEtiqueta(String arg) {

        arg = arg.toString().replace("_", " ");
        arg = arg.toLowerCase();
        char[] cs = arg.toCharArray();
        char ch = cs[0];
        cs[0] = Character.toUpperCase(ch);
        arg = String.valueOf(cs);

        return arg;
    }

    public void verFicha() {
        String resp = "";
        if (session != null) {

            herramientas.navegarPagina("ficha");
        } else {
            herramientas.navegarPagina("index");
        }

    }

//    public void onNodeSelect(NodeSelectEvent event) {
//        Properties propiedades;
//        TreeNode nodo = event.getTreeNode();
//
//
//
//
//
//        if (nodo.isLeaf()) {
////            try {
//
//            NodoTipoDocumento tipoDoc = (NodoTipoDocumento) nodo;
//            idInfoDocumento = tipoDoc.getIdInfoDocumento();
//            verDocumento();
//            verDoc = true;
//            propiedades = (Properties) session.getAttribute("propiedades");
//            String ipServidorWeb = propiedades.getProperty("servidorWEB");
//            String puertoSrvWeb = propiedades.getProperty("puertoWeb");
//            String fileJNLP = "http://" + ipServidorWeb + ":" + puertoSrvWeb + "/jnlp/verDocumento.jnlp";
//
//            traza.trace("jnlp " + fileJNLP, Level.INFO);
//
//            InputStream stream = ((ServletContext) herramientas.getExternalContext().getContext()).getResourceAsStream(fileJNLP);
//            archivo = new DefaultStreamedContent(stream, "application/x-java-jnlp-file", "verDocumento.jnlp");
//
//
////                URL u = new URL(servidor);
////                HttpURLConnection conec = (HttpURLConnection) u.openConnection();
////                conec.setRequestMethod("POST");
////                conec.setRequestProperty("Content-Type", "application/x-java-jnlp-file");
////                conec.connect();
////
////                conec.getErrorStream();
////                int code = conec.getResponseCode();                
////                
////                Desktop.getDesktop().browse(new URI(servidor));
////
////                traza.trace("respuesta " + code, Level.INFO);
//
////            } catch (MalformedURLException ex) {
////                traza.trace("error al formar el url", Level.ERROR, ex);
////            } catch (IOException ex) {
////                traza.trace("error abriendo la coneccion", Level.ERROR, ex);
////            } catch (URISyntaxException ex) {
////                traza.trace("error de uri", Level.ERROR, ex);
////            }
//        } else {
//            herramientas.warn("Debe seleccionar un Tipo de Documento");
//        }
//    }
    public void verDocumento(NodeSelectEvent event) {
        TreeNode nodoSubCategoria;
        Properties propiedades;
        NodoTipoDocumento tipoDoc;
        InfoDocumento infoDocumento;
        CreaArchivoJNLP archivoJNLP = new CreaArchivoJNLP();
        boolean creoJNLP;
        String subCategoria;
        int idInfoDocumento;

        TreeNode nodo = event.getTreeNode();

        try {
            if (nodo.isLeaf()) {

                nodoSubCategoria = nodo.getParent();

                propiedades = (Properties) session.getAttribute("propiedades");
                String ipServidorWeb = propiedades.getProperty("servidorWEB");
                String puertoSrvWeb = propiedades.getProperty("puertoWeb");
                String fileJNLP = "http://" + ipServidorWeb + ":" + puertoSrvWeb + "/jnlp/verDocumento.jnlp";

                traza.trace("jnlp " + fileJNLP, Level.INFO);

                subCategoria = nodoSubCategoria.getData().toString();
                expediente.setSubCategoria(subCategoria);

                tipoDoc = (NodoTipoDocumento) hojaSeleccionada;
                idInfoDocumento = tipoDoc.getIdInfoDocumento();
                expediente.setTipoDocumento(tipoDoc.getData().toString());
                verDoc = true;

                traza.trace("idInfoDocumento " + idInfoDocumento, Level.INFO);

                if (session != null) {
                    for (InfoDocumento id : infoDocumentos) {
                        if (id.getIdInfoDocumento() == idInfoDocumento) {

                            traza.trace("tipo de documento a mostrar " + id.getTipoDocumento(), Level.INFO);
                            traza.trace("nombre del archivo " + id.getNombreArchivo(), Level.INFO);
                            traza.trace("ruta del archivo " + id.getRutaArchivo(), Level.INFO);
                            traza.trace("version del archivo " + id.getVersion(), Level.INFO);
                            traza.trace("id del archivo " + id.getIdInfoDocumento(), Level.INFO);
                            traza.trace("numero del documento (archivo) " + id.getNumeroDocumento(), Level.INFO);
                            traza.trace("id del documento " + id.getIdDocumento(), Level.INFO);
                            traza.trace("fecha vencimiento del archivo " + id.getFechaVencimiento(), Level.INFO);

                            infoDocumento = id;
                            infoDocumento.setIdInfoDocumento(idInfoDocumento);

                            creoJNLP = archivoJNLP.crearArchivoJNLP(infoDocumento, expediente, login);
                            traza.trace("se creo el jnlp " + creoJNLP, Level.INFO);
                            if (creoJNLP) {
                                herramientas.getFacesContext().getExternalContext().redirect(fileJNLP);
//                                InputStream stream = ((ServletContext) herramientas.getExternalContext().getContext()).getResourceAsStream(fileJNLP);
//                                archivo = new DefaultStreamedContent(stream, "application/x-java-jnlp-file", "verDocumento.jnlp");
                                //archivoJNLP.descargarJNLP();
                                //descargar();                          
                                break;
                            }
                        }
                    }
                } else {
                    herramientas.getExternalContext().redirect("index.xhtml");
                }

            } else {
                herramientas.warn("Debe seleccionar un Tipo de Documento");
            }
        } catch (Exception e) {
            traza.trace("error al ver el documento", Level.ERROR, e);
        }
    }

    public TreeNode getRaiz() {
        return raiz;
    }

    public void setRaiz(TreeNode raiz) {
        this.raiz = raiz;
    }

    public TreeNode getHojaSeleccionada() {
        return hojaSeleccionada;
    }

    public void setHojaSeleccionada(TreeNode hojaSeleccionada) {
        this.hojaSeleccionada = hojaSeleccionada;
    }

    private String crearID(String arg) {

        arg = arg.replace(" ", "_");

        return arg;
    }

    public HtmlPanelGrid getPanelIndicesDinamico() {

        List<ConsultaDinamica> consultaDinamicas;
        List<Indice> listaIndic = null;
        boolean ban = true;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        FacesContext context = FacesContext.getCurrentInstance();
//        UIViewRoot viewRoot = context.getViewRoot();
        Application app = context.getApplication();
        panelIndicesDinamico = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        //List<Indice> listaIndic;
        String fecha = "";
        HtmlOutputText label;

        try {
            if (session != null) {

                consultaDinamicas = (List<ConsultaDinamica>) session.getAttribute("consultaDinamicas");
                for (ConsultaDinamica cd : consultaDinamicas) {
                    if (ban) {
                        listaIndic = cd.getIndices();
                        for (Indice ind : listaIndic) {
                            try {
                                if (ind.getClave().equalsIgnoreCase("y")) {
                                    if (ind.getValor().toString().equalsIgnoreCase(expediente.getIdExpediente())) {
                                        ban = false;
                                        break;
                                    }
                                }
                            } catch (NullPointerException e) {
                            }
                        }
                    }
                }

                session.setAttribute("listaIndices", listaIndic);

                for (Indice ind : listaIndic) {

                    label = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);

                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                        try {

                            if (!ind.getIndice().equalsIgnoreCase(fecha)) {

                                fecha = ind.getIndice();
//                                XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) ind.getValor();
//                                Calendar cal = xmlCal.toGregorianCalendar();

                                label.setId(crearID(ind.getIndice()));

//                                String dato = sdf.format(cal.getTime());
                                String dato = ind.getValor().toString();

                                label.setValue(crearEtiqueta(ind.getIndice()) + ": " + dato);

                                panelIndicesDinamico.getChildren().add(label);

                                //label = null;
                            }

                        } catch (NullPointerException e) {

                            fecha = ind.getIndice();
                            label.setId(crearID(ind.getIndice()));

                            label.setValue(crearEtiqueta(ind.getIndice()) + ": ");

                            panelIndicesDinamico.getChildren().add(label);

                            //label = null;
                        }

                    } else {
                        try {
                            label.setId(crearID(ind.getIndice()));

                            label.setValue(crearEtiqueta(ind.getIndice()) + ": " + ind.getValor().toString());

                            panelIndicesDinamico.getChildren().add(label);

                            //label = null;
                        } catch (NullPointerException e) {
                            label.setId(crearID(ind.getIndice()));

                            label.setValue(crearEtiqueta(ind.getIndice()) + ": ");

                            panelIndicesDinamico.getChildren().add(label);

                            //label = null;
                        }
                    }
                }

            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
        }
        return panelIndicesDinamico;
    }

    public void setPanelIndicesDinamico(HtmlPanelGrid panelIndicesDinamico) {
        this.panelIndicesDinamico = panelIndicesDinamico;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isFicha() {
        return ficha;
    }

    public void setFicha(boolean ficha) {
        this.ficha = ficha;
    }

    public boolean isVerDoc() {
        return verDoc;
    }

    public void setVerDoc(boolean verDoc) {
        this.verDoc = verDoc;
    }

    public StreamedContent getArchivo() {
        return archivo;
    }

    public void setArchivo(StreamedContent archivo) {
        this.archivo = archivo;
    }

    public void regresar() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                raiz.getChildren().clear();
                herramientas.getExternalContext().redirect("resultado.xhtml");
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (IOException ex) {
            traza.trace("error", Level.ERROR, ex);
        }
    }

    public void nuevaConsulta() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                raiz.getChildren().clear();
                herramientas.getExternalContext().redirect("consultar.xhtml");
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (IOException ex) {
            traza.trace("error", Level.ERROR, ex);
        }
    }

    public void salir() {

        try {
            System.runFinalization();
            System.gc();
            if (session != null) {
                herramientas.cerrarSesion();
                herramientas.getExternalContext().redirect("index.xhtml");
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (IOException ex) {
            traza.trace("error al salir", Level.ERROR, ex);
        }
    }

}
