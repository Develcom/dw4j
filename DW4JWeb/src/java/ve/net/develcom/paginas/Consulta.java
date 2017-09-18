/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.administracion.Combo;
import com.develcom.administracion.Indices;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;
import ve.com.develcom.expediente.BuscaIndice;
import ve.com.develcom.expediente.LLenarListaDesplegable;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;

/**
 *
 * @author develcom
 */
public class Consulta extends SelectorComposer<Component> {

    @Wire
    private Listbox subcategoria;

    @Wire
    private Listbox tipodocumento;

    @Wire
    private Grid panelIndice;

    private List<SubCategoria> listaSubCategorias;
    private List<TipoDocumento> listaTipoDocumentos = new ArrayList<>();
    private static final long serialVersionUID = 5209281633154421872L;
    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(Consulta.class);
    private Session session;
    private Expediente expediente;
    private List<Indice> lstIndices = new ArrayList<>();
    private List<Integer> idSubCate = new ArrayList<>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {

        try {

            session = herramientas.crearSesion();

            if (session != null) {
                expediente = (Expediente) session.getAttribute("expediente");

                buscarIndces();
                llenarListaSubcategorias();
                tipodocumento.appendItem("Debe seleccionar una o varias SubCategorias", "");
            }

        } catch (Exception e) {
            traza.trace("error al iniciar el objeto consulta", Level.INFO, e);
            herramientas.navegarPagina("index.zul");
        }

    }

    private void buscarIndces() {

        Rows rows;
        Row row = null;
        List<com.develcom.administracion.Indice> listaIndices;
        Label label;
        Textbox textbox;
        Combobox combobox;
        int cont = 2;

        try {

            listaIndices = new BuscaIndice().buscarIndice(expediente.getIdCategoria());
//            convertirIndices(listaIndices);

            rows = panelIndice.getRows();

            for (Indice ind : lstIndices) {

                if (cont == 2) {
                    cont = 0;
                    row = new Row();
                }

                if (ind.getTipo().equalsIgnoreCase("NUMERO")) {

                    label = new Label(ind.getIndice());

                    textbox = new Textbox();
                    textbox.setId(ind.getIndice().replace(" ", "_"));

                    row.appendChild(label);
                    row.appendChild(textbox);

                    rows.appendChild(row);

                    cont++;

                } else if (ind.getTipo().equalsIgnoreCase("TEXTO")) {

                    label = new Label(ind.getIndice());

                    textbox = new Textbox();
                    textbox.setId(ind.getIndice().replace(" ", "_"));

                    row.appendChild(label);
                    row.appendChild(textbox);

                    rows.appendChild(row);

                    cont++;

                } else if (ind.getTipo().equalsIgnoreCase("AREA")) {

                    label = new Label(ind.getIndice());

                    textbox = new Textbox();
                    textbox.setId(ind.getIndice().replace(" ", "_"));
                    textbox.setRows(6);

                    row.appendChild(label);
                    row.appendChild(textbox);

                    rows.appendChild(row);

                    cont++;

                } else if (ind.getTipo().equalsIgnoreCase("COMBO")) {
                    Comboitem item;

                    label = new Label(ind.getIndice());

                    combobox = new Combobox();
                    combobox.setId(ind.getIndice().replace(" ", "_"));

                    List<Combo> datosCombo = new LLenarListaDesplegable().buscarData(ind.getCodigo(), false);

                    for (Combo combo : datosCombo) {
                        item = new Comboitem();
                        item.setValue(combo.getIdCombo());
                        item.setLabel(combo.getDatoCombo());
                        item.setParent(combobox);
                    }

                    row.appendChild(label);
                    row.appendChild(combobox);

                    rows.appendChild(row);

                    cont++;

                } else if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                    Label labelDesde = new Label(ind.getIndice().replace(" ", "_") + "_desde");
                    Datebox dateboxDesde = new Datebox();
                    dateboxDesde.setId(ind.getIndice().replace(" ", "_") + "_desde");
                    dateboxDesde.setFormat("dd/MM/yyyy");

                    row.appendChild(labelDesde);
                    row.appendChild(dateboxDesde);

                    rows.appendChild(row);

                    cont++;

                    if (cont == 2) {
                        cont = 0;
                        row = new Row();
                    }

                    Label labelHasta = new Label(ind.getIndice().replace(" ", "_") + "_hasta");
                    Datebox dateboxHasta = new Datebox();
                    dateboxHasta.setId(ind.getIndice().replace(" ", "_") + "_hasta");
                    dateboxHasta.setFormat("dd/MM/yyyy");

                    row.appendChild(labelHasta);
                    row.appendChild(dateboxHasta);

                    rows.appendChild(row);

                    cont++;
                }

            }

        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al buscar los indices", Level.ERROR, ex);
        }

    }

    private void llenarListaSubcategorias() {

        Listitem item;

        try {
            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria("", expediente.getIdCategoria(), 0);

            for (SubCategoria sc : listaSubCategorias) {
                if (sc.getEstatus().equalsIgnoreCase("activo")) {
                    item = new Listitem();
                    item.setValue(sc.getIdSubCategoria());
                    item.appendChild(new Listcell(sc.getSubCategoria()));
//		item.appendChild(addCheckBox(item));
                    item.setParent(subcategoria);
                }
            }

        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al llenar la lista de subcategorias", Level.ERROR, ex);
        }

    }

    @Listen("onSelect = #subcategoria")
    public void llenarListaTipoDocumentos() {

        List<com.develcom.expediente.TipoDocumento> tds;// = new ArrayList<com.develcom.expediente.TipoDocumento>();
        Set<Listitem> listitemsSC;
        Iterator<Listitem> iteratorSC;
        List<Listcell> childsTD;
        Listitem itemTipoDoc;
        TipoDocumento documento;

        try {

            childsTD = tipodocumento.getChildren();
            childsTD.clear();

            idSubCate.clear();
            listitemsSC = subcategoria.getSelectedItems();
            iteratorSC = listitemsSC.iterator();

            while (iteratorSC.hasNext()) {
                Listitem next = iteratorSC.next();
                idSubCate.add((Integer) next.getValue());
            }

            traza.trace("idSubCate " + idSubCate, Level.INFO);
            traza.trace("buscando los tipos de documentos de las subcategoria " + idSubCate, Level.INFO);
            tds = new BuscaExpedienteDinamico().buscarTiposDocumentos(idSubCate);

            if (tds.isEmpty() || tds == null) {

                tipodocumento.appendItem("Debe seleccionar una o varias SubCategorias", "");

            } else {

                listaTipoDocumentos.clear();

                for (com.develcom.expediente.TipoDocumento tp : tds) {
                    documento = new TipoDocumento();
                    documento.setDatoAdicional(tp.getDatoAdicional());
                    documento.setEstatus(tp.getEstatus());
                    documento.setFicha(tp.getFicha());
                    documento.setIdCategoria(tp.getIdCategoria());
                    documento.setIdSubCategoria(tp.getIdSubCategoria());
                    documento.setIdTipoDocumento(tp.getIdTipoDocumento());
                    documento.setTipoDocumento(tp.getTipoDocumento());
                    documento.setVencimiento(tp.getVencimiento());
                    listaTipoDocumentos.add(documento);

                    itemTipoDoc = new Listitem();
                    itemTipoDoc.setValue(tp.getIdTipoDocumento());
                    itemTipoDoc.appendChild(new Listcell(tp.getTipoDocumento()));
//		item.appendChild(addCheckBox(item));
                    itemTipoDoc.setParent(tipodocumento);
                }
            }

        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al llenar la lista de los tipos de documentos", Level.ERROR, ex);
        }

    }

    private void convertirIndices(List<Indices> listaIndices) {

        Indice indice;

        for (Indices indices : listaIndices) {
            indice = new Indice();

            indice.setClave(indices.getClave());
            indice.setCodigo(indices.getCodigo());
            indice.setIdCategoria(indices.getIdCategoria());
            indice.setIdIndice(indices.getIdIndice());
            indice.setIndice(indices.getIndice());
            indice.setTipo(indices.getTipo());
            lstIndices.add(indice);
        }

    }

    @Listen("onClick = #consulta")
    public void consultar() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Set<Listitem> listitems;
        Iterator<Listitem> it;
        List<ConsultaDinamica> consultaDinamicas;
        List<TipoDocumento> listaTipoDoc = new ArrayList<>();
        List<SubCategoria> listaSubCat = new ArrayList<>();
        Indice indice;
        List<Indice> listaIndices = new ArrayList<>();
        List<Integer> idTipoDoc = new ArrayList<>();
        //IBarraEstado barraEstado;

        try {

            //barraEstado = herramientas.getBarraEstado();
            
            listitems = tipodocumento.getSelectedItems();

            it = listitems.iterator();

            while (it.hasNext()) {
                Listitem next = it.next();
                idTipoDoc.add((Integer) next.getValue());
            }
            
            //barraEstado.setStatus("Tomando los datos del formulario");
            
            if (session != null) {

                for (Indice ind : lstIndices) {

                    if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_")) instanceof Textbox) {

                        Textbox textbox = (Textbox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_"));

                        traza.trace("argumento " + ind.getIndice() + " valor " + textbox.getValue(), Level.INFO);

                        if (textbox.getValue() != null || !textbox.getValue().equalsIgnoreCase("")) {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(textbox.getValue());
                            listaIndices.add(indice);

                        } else {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            listaIndices.add(indice);

                        }

                    } else if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_")) instanceof Combobox) {

                        Combobox combobox = (Combobox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_"));

                        traza.trace("argumento " + ind.getIndice() + " valor " + combobox.getValue(), Level.INFO);

                        if (combobox.getValue() != null || !combobox.getValue().equalsIgnoreCase("")) {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(combobox.getValue());
                            listaIndices.add(indice);

                        } else {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            listaIndices.add(indice);

                        }

                    } else if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_desde") instanceof Datebox) {

                        Datebox datebox = (Datebox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_desde");

                        traza.trace("argumento " + ind.getIndice() + " valor " + datebox.getValue(), Level.INFO);

                        if (datebox.getValue() != null) {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(sdf.format(datebox.getValue()));
                            listaIndices.add(indice);

                        } else {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            listaIndices.add(indice);
                        }

                    } else if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_hasta") instanceof Datebox) {

                        Datebox input = (Datebox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_hasta");

                        traza.trace("argumento " + ind.getIndice() + " valor " + input.getValue(), Level.INFO);

                        if (input.getValue() != null) {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            indice.setValor(sdf.format(input.getValue()));
                            listaIndices.add(indice);

                        } else {

                            indice = new Indice();
                            indice.setIdCategoria(ind.getIdCategoria());
                            indice.setIdIndice(ind.getIdIndice());
                            indice.setIndice(ind.getIndice());
                            indice.setClave(ind.getClave());
                            indice.setTipo(ind.getTipo());
                            listaIndices.add(indice);
                        }
                    }
                }
                
                //barraEstado.setStatus("Tomando las SubCategrias Seleccionadas");
                for (SubCategoria sc : listaSubCategorias) {

                    int idSubCat = sc.getIdSubCategoria();

                    for (Integer obj : idSubCate) {

                        if (idSubCat == obj) {
                            listaSubCat.add(sc);
                            traza.trace("id selecionado " + obj, Level.INFO);
                            traza.trace("id del objecto agregado " + sc.getIdSubCategoria(), Level.INFO);
                        }
                    }
                }

                //barraEstado.setStatus("Tomando los Tipos de Documentos Seleccionados");
                for (TipoDocumento td : listaTipoDocumentos) {
                    for (Integer obj : idTipoDoc) {
                        if (td.getIdTipoDocumento() == obj) {
                            listaTipoDoc.add(td);
                        }
                    }
                }

                //barraEstado.setStatus("Combrobando las Fechas");
                if (comprobarFechas()) {
                    
                    //barraEstado.setStatus("Realizando la consulta, Espere por favor...!!!");
                    consultaDinamicas = new BuscaExpedienteDinamico().consultarExpedienteDinamico(listaIndices, null, convertirSubCategorias(listaSubCat), convertirTipoDocumento(listaTipoDoc), 1, expediente.getIdLibreria());

                    if (!consultaDinamicas.isEmpty()) {
                        if (consultaDinamicas.get(0).isExiste()) {

                            expediente.setTipoDocumentos(listaTipoDoc);
                            expediente.setSubCategorias(listaSubCat);

                            session.setAttribute("consultaDinamicas", consultaDinamicas);
                            session.setAttribute("expediente", expediente);
                            session.setAttribute("tamañoConsulta", consultaDinamicas.size());

                            traza.trace("tamaño de la consulta " + consultaDinamicas.size(), Level.INFO);
                            double ta = (consultaDinamicas.size() % 10);
                            traza.trace("modulo de la consulta " + ta, Level.INFO);
                            traza.trace("exito al en la consulta dinamica ", Level.INFO);

                            herramientas.navegarPagina("resultado.zul");
                        } else {
                            herramientas.warn("Debe indicar algún Índice de Búsqueda");
                        }
                    } else {
                        herramientas.warn("No se encontraron expedientes asociados a la búsqueda ingresada");
                    }
                } else {
                    herramientas.warn("Verifique los rangos de las fechas entrantes y salientes");
                }
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (WrongValueException | NumberFormatException | SOAPException | SOAPFaultException e) {
            herramientas.error("", e);
            traza.trace("error al realizar la consulta", Level.ERROR, e);
        }

        //return null;
    }

    private List<com.develcom.expediente.SubCategoria> convertirSubCategorias(List<SubCategoria> listSubCat) {
        List<com.develcom.expediente.SubCategoria> scs = new ArrayList<>();
        com.develcom.expediente.SubCategoria sc;
        for (SubCategoria subCa : listSubCat) {
            sc = new com.develcom.expediente.SubCategoria();
            sc.setEstatus(subCa.getEstatus());
            sc.setIdCategoria(subCa.getIdCategoria());
            sc.setIdSubCategoria(subCa.getIdSubCategoria());
            sc.setSubCategoria(subCa.getSubCategoria());
            scs.add(sc);
        }
        return scs;
    }

    private List<com.develcom.expediente.TipoDocumento> convertirTipoDocumento(List<TipoDocumento> documentos) {
        List<com.develcom.expediente.TipoDocumento> tds = new ArrayList<>();
        com.develcom.expediente.TipoDocumento td;

        for (TipoDocumento tp : documentos) {
            td = new com.develcom.expediente.TipoDocumento();
            td.setDatoAdicional(tp.getDatoAdicional());
            td.setEstatus(tp.getEstatus());
            td.setFicha(tp.getFicha());
            td.setIdCategoria(tp.getIdCategoria());
            td.setIdSubCategoria(tp.getIdSubCategoria());
            td.setIdTipoDocumento(tp.getIdTipoDocumento());
            td.setTipoDocumento(tp.getTipoDocumento());
            td.setVencimiento(tp.getVencimiento());
            tds.add(td);
        }

        return tds;
    }

    private boolean comprobarFechas() {
        boolean resp = true;
        Date fechDesde = null, fechHasta = null;
        String etiDesde = null, etiHasta = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Indice ind : lstIndices) {

            if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_desde") instanceof Datebox) {

                Datebox input = (Datebox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_desde");
                etiDesde = input.getId().replace("_", " ");
                fechDesde = input.getValue();

            } else if (panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_hasta") instanceof Datebox) {

                Datebox input = (Datebox) panelIndice.getFellowIfAny(ind.getIndice().replace(" ", "_") + "_hasta");
                etiHasta = input.getId().replace("_", " ");
                fechHasta = input.getValue();

            }

        }

        if (fechHasta != null) {
            if (fechDesde != null) {
                if (fechDesde.after(fechHasta)) {
                    resp = false;
                    herramientas.warn("problema de fechas");
                    herramientas.warn(etiDesde + " - " + sdf.format(fechDesde));
                    herramientas.warn(etiHasta + " - " + sdf.format(fechHasta));
                    traza.trace("problema de fechas", Level.INFO);
                    traza.trace(etiDesde + " - " + sdf.format(fechDesde.getTime()), Level.INFO);
                    traza.trace(etiHasta + " - " + sdf.format(fechHasta.getTime()), Level.INFO);
                }
            }
        }

        return resp;
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
                herramientas.navegarPagina("libreria.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al regresar", Level.ERROR, ex);
        }
    }

}
