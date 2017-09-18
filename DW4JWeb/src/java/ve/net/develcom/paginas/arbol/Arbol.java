/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.documento.InfoDocumento;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.expediente.GestionDocumentos;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.barra.IBarraEstado;

/**
 *
 * @author develcom
 */
public class Arbol extends SelectorComposer<Component> {

    @Wire
    private Listbox indices;

    @Wire
    private Tree arbolDoc;

    @Wire
    private Panel panelArbol;

    @Wire
    private Panel panelIndice;

    private List<InfoDocumento> infoDocumentos = new ArrayList<>();
    private static final long serialVersionUID = -6479607980888608690L;
    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(Arbol.class);
    private Session session;
    private String login;
    private Expediente expediente;
    private boolean ficha;
    //private IBarraEstado barraEstado;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {

        session = herramientas.crearSesion();

        if (session != null) {

            //barraEstado = herramientas.getBarraEstado();
            login = session.getAttribute("login").toString();
            ficha = Boolean.parseBoolean(session.getAttribute("ficha").toString());
            expediente = (Expediente) session.getAttribute("expediente");

            panelArbol.setTitle("Expediente: " + expediente.getIdExpediente());
            panelIndice.setTitle("Indices del Expediente: " + expediente.getIdExpediente());
            construirIndices();
            construirArbol();
        } else {

            herramientas.warn("Problemas con la Sesión, posiblemente se venció");
            herramientas.navegarPagina("index.zul");
        }

    }

    private void construirArbol() {

        List<TipoDocumento> listaTipoDoc;
        List<SubCategoria> subCategorias;
        int contTD = 0, totalDocDig, cont = 0;
        List<InfoDocumento> infoDocumentosBuscados = new ArrayList<>();
        GestionDocumentos btd = new GestionDocumentos();
        List<Integer> idDocumento = new ArrayList<>();
        String nombreTD, tmp[], idExpediente;

        Treecols treecols = new Treecols();
        Treecol categoria;
        Treecol datoAdicionale = new Treecol("Datos Adicionales");

        NodoArbol raiz;
        LinkedList<NodoArbol> nodoSubCate = new LinkedList();
        LinkedList<NodoArbol> nodoTipoDoc = new LinkedList();
        DatoNodoArbol dna;

        try {

            //barraEstado.setStatus("Generando el Expediente");
            if (session != null) {

                listaTipoDoc = expediente.getTipoDocumentos();
                idExpediente = expediente.getIdExpediente();
                subCategorias = expediente.getSubCategorias();

                categoria = new Treecol(expediente.getCategoria());
                categoria.setParent(treecols);
                datoAdicionale.setParent(treecols);
                treecols.setParent(arbolDoc);

                if (subCategorias.isEmpty()) {
                    subCategorias = new AdministracionBusqueda().buscarSubCategoria("", expediente.getIdCategoria(), 0);
                }

                for (SubCategoria sc : subCategorias) {

                    if (!listaTipoDoc.isEmpty()) {

                        for (TipoDocumento td : listaTipoDoc) {
                            if (td.getEstatus().equalsIgnoreCase("ACTIVO")) {
                                idDocumento.add(td.getIdTipoDocumento());
                            }
                        }

                    } else {
                       // barraEstado.setStatus("Generando el Expediente: buscando los documento de la SubCategoria "+sc.getSubCategoria());
                        listaTipoDoc = new AdministracionBusqueda().buscarTipoDocumento(null, sc.getIdCategoria(), sc.getIdSubCategoria());

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

                            totalDocDig = infoDocumentosBuscados.size();

                            tmp = new String[totalDocDig];

//                            subCate = new DatoNodoArbol(sc.getSubCategoria());
//                            subCate.setParent(treerowR);
//                            
//                            treerowR.setParent(treeitemR);
//                            treeitemR.setParent(treechildrenP);
                            for (InfoDocumento id : infoDocumentosBuscados) {
                                //barraEstado.setStatus("Generando el Expediente: Agregando el Tipo de Documento "+id.getTipoDocumento()+" tiene dato adicioanl "+id.isTipoDocDatoAdicional());
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

                                        dna = new DatoNodoArbol(id.getTipoDocumento(), da);
                                        dna.setIdInfoDocumento(id.getIdInfoDocumento());
                                        dna.setIdDocumento(id.getIdDocumento());
                                        dna.setIdSubCategoria(sc.getIdSubCategoria());
                                        nodoTipoDoc.add(new NodoArbol(dna));

                                    } else {

                                        dna = new DatoNodoArbol(id.getTipoDocumento(), "");
                                        dna.setIdInfoDocumento(id.getIdInfoDocumento());
                                        dna.setIdDocumento(id.getIdDocumento());
                                        dna.setIdSubCategoria(sc.getIdSubCategoria());
                                        nodoTipoDoc.add(new NodoArbol(dna));

                                    }
                                    traza.trace("tipo de documento digitalizado " + id.getTipoDocumento() + " id InfoDocumento " + id.getIdInfoDocumento(), Level.INFO);
                                }
                            }
                            nodoSubCate.add(new NodoArbol(new DatoNodoArbol(sc.getSubCategoria()), nodoTipoDoc));
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
                    if (!nodoTipoDoc.isEmpty()) {
                        nodoTipoDoc.clear();
                    }
                }
                raiz = new NodoArbol(null, nodoSubCate);
                arbolDoc.setItemRenderer(new InterpretaArbol());
                arbolDoc.setModel(new ModeloArbol(raiz));
            } else {
                herramientas.navegarPagina("index.zul");
            }

        } catch (SOAPException | SOAPFaultException e) {
            traza.trace("Error al crear el arbol de documentos", Level.ERROR, e);
            herramientas.error("Error al crear el arbol de documentos", e);
        }
    }

    private void construirIndices() {

        List<ConsultaDinamica> consultaDinamicas;
        List<Indice> listaIndic = null;
        boolean ban = true;
        Listitem datos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            if (session != null) {
                
                //barraEstado.setStatus("Buscando los Indices del Expediente");

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
                    //barraEstado.setStatus("Agregando el indice "+ind.getIndice() + ": " + ind.getValor());
                    datos = new Listitem();
                    datos.appendChild(new Listcell(ind.getIndice() + ": " + ind.getValor()));
                    datos.setParent(indices);
                }

            }

        } catch (Exception e) {
            herramientas.error("Problemas con los indices del expediente", e);
        }
    }

    @Listen("onSelect = #arbolDoc")
    public void verDocumento() {

//        Integer percent = 1 * 100 / 8;
//        Properties propiedades;
        InfoDocumento infoDocumento;
        int idInfoDocumento, idDocumento, idSubCategoria;
        String subCategoria, tipoDocumento;
//        Treeitem treeitem;
//        Treerow treerow;
//        CeldaArbol tipoDoc;
        NodoArbol<DatoNodoArbol> nodoArbol;
        DatoNodoArbol dna;

        try {
            if (session != null) {

                nodoArbol= arbolDoc.getSelectedItem().getValue();
                dna = nodoArbol.getData();
                
//                treeitem = arbolDoc.getSelectedItem();
//                treerow = treeitem.getTreerow();
//                tipoDoc = (CeldaArbol) treerow.getFirstChild();
                traza.trace("id infodocumento seleccionado " + dna.getIdInfoDocumento(), Level.INFO);
//                traza.trace("documento seleccionado " + tipoDoc.getLabel(), Level.INFO);

//                idInfoDocumento = tipoDoc.getIdInfoDocumento();
                idInfoDocumento = dna.getIdInfoDocumento();

                for (InfoDocumento id : infoDocumentos) {
                    if (id.getIdInfoDocumento() == idInfoDocumento) {

//                        propiedades = (Properties) session.getAttribute("propiedades");
//                        String ipServidorWeb = propiedades.getProperty("servidorWEB");
//                        String puertoSrvWeb = propiedades.getProperty("puertoWeb");
//                        String fileJNLP = "http://" + ipServidorWeb + ":" + puertoSrvWeb + "/jnlp/verDocumento.jnlp";

//                        idSubCategoria = tipoDoc.getIdSubCategoria();
//                        tipoDocumento = tipoDoc.getLabel();
//                        idDocumento = tipoDoc.getIdDocumento();
                        idSubCategoria = dna.getIdSubCategoria();
                        tipoDocumento = dna.getTipoDocumento();
                        idDocumento = dna.getIdDocumento();

                        expediente.setTipoDocumento(tipoDocumento);
//                        expediente.setSubCategoria(subCategoria);
                        expediente.setIdSubCategoria(idSubCategoria);
                        expediente.setIdTipoDocumento(idDocumento);

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

                        session.setAttribute("infoDocumento", infoDocumento);
                        session.setAttribute("expediente", expediente);

                        herramientas.navegarPagina("documento.zul");

                        break;
                    }
                }

            }
        } catch (Exception e) {
            herramientas.error("Problemas al generar la información para mostra el Documento", e);
            traza.trace("error al ver el documento", Level.ERROR, e);
        }
    }

    @Listen("onClick = #exit")
    public void salir() {

        try {
            System.runFinalization();
            System.gc();
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
            if (session != null) {
                herramientas.navegarPagina("consulta.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al regresar", Level.ERROR, ex);
        }
    }

    @Listen("onClick = #consulta")
    public void nuevaConsulta() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {

                herramientas.navegarPagina("consulta.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error en nueva consulta", Level.ERROR, ex);
        }
    }
}
