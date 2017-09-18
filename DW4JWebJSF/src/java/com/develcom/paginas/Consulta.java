/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.administracion.Combo;
import com.develcom.administracion.Indices;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.dao.Expediente;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.tool.Herramientas;
import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import org.primefaces.component.calendar.Calendar;
import ve.com.develcom.administracion.AdministracionBusqueda;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;
import ve.com.develcom.expediente.BuscaIndice;
import ve.com.develcom.expediente.LLenarListaDesplegable;

/**
 *
 * @author develcom
 */
@ManagedBean
@RequestScoped
public class Consulta {

    private String login;
    private Expediente expediente;
    private Traza traza = new Traza(Consulta.class);
    private HtmlPanelGrid panelIndicesDinamico = new HtmlPanelGrid();
    private List<Indice> lstIndices = new ArrayList<Indice>();
    private List<Object> subCategoria = new ArrayList<Object>();
    private List<SelectItem> subCategorias = new ArrayList<SelectItem>();
    private List<Object> tipoDocumento = new ArrayList<Object>();
    private List<SelectItem> tiposDocumentos = new ArrayList<SelectItem>();
    private List<SubCategoria> listaSubCategorias;
    private List<TipoDocumento> listaTipoDocumentos = new ArrayList<TipoDocumento>();
    private List<ConsultaDinamica> consultaDinamicas;
    private HttpSession session;
    private Herramientas herramientas = new Herramientas();

    /**
     * Creates a new instance of Consulta
     */
    public Consulta() {
        try {

            session = herramientas.crearSesion();
            login = (String) session.getAttribute("login");
            expediente = (Expediente) session.getAttribute("expediente");

        } catch (Exception e) {
            traza.trace("error al iniciar el objeto consulta", Level.INFO, e);
            try {
                herramientas.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                traza.trace("error", Level.ERROR, ex);
            }
        }
    }

    public void consultar() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        GregorianCalendar fechaDesde;
        GregorianCalendar fechaHasta;
        HashMap<String, GregorianCalendar> fechas;
        List<HashMap<String, GregorianCalendar>> lstFechas = new ArrayList<HashMap<String, GregorianCalendar>>();

        Object[] subCateSel, tipoDocSel;

        List<TipoDocumento> listaTipoDoc = new ArrayList<TipoDocumento>();
        List<SubCategoria> listaSubCat = new ArrayList<SubCategoria>();
        Indice indice;
        List<Indice> listaIndices = new ArrayList<Indice>();

        List<UIComponent> hijos = panelIndicesDinamico.getChildren();
        String resp = "", labelHasta, labelDesde;

        try {
            if (session != null) {
                for (Indice ind : lstIndices) {

                    for (UIComponent uiComp : hijos) {
                        if (uiComp.getFamily().equalsIgnoreCase("javax.faces.Input")) {

                            if (uiComp instanceof HtmlInputText) {
                                HtmlInputText input = (HtmlInputText) uiComp;

                                if (uiComp.getId().replace("_", " ").equalsIgnoreCase(ind.getIndice())) {

                                    traza.trace("argumento " + ind.getIndice() + " valor " + input.getValue(), Level.INFO);

                                    indice = new Indice();
                                    indice.setIdCategoria(ind.getIdCategoria());
                                    indice.setIdIndice(ind.getIdIndice());
                                    indice.setIndice(ind.getIndice());
                                    indice.setClave(ind.getClave());
                                    indice.setTipo(ind.getTipo());
                                    indice.setValor(input.getValue());
                                    listaIndices.add(indice);
                                }

                            } else if (uiComp instanceof HtmlInputTextarea) {
                                HtmlInputTextarea inputArea = (HtmlInputTextarea) uiComp;

                                if (uiComp.getId().replace("_", " ").equalsIgnoreCase(ind.getIndice())) {

                                    traza.trace("argumento " + ind.getIndice() + " valor " + inputArea.getValue(), Level.INFO);

                                    indice = new Indice();
                                    indice.setIdCategoria(ind.getIdCategoria());
                                    indice.setIdIndice(ind.getIdIndice());
                                    indice.setIndice(ind.getIndice());
                                    indice.setClave(ind.getClave());
                                    indice.setTipo(ind.getTipo());
                                    indice.setValor(inputArea.getValue());
                                    listaIndices.add(indice);
                                }
                            }
                            //}else if(uiComp.getFamily().equalsIgnoreCase("org.richfaces.Calendar")){
                        } else if (uiComp.getFamily().equalsIgnoreCase("org.primefaces.component")) {

                            if (uiComp instanceof Calendar) {
                                Calendar cal = (Calendar) uiComp;

                                labelHasta = ind.getIndice() + "_HASTA";
                                labelDesde = ind.getIndice() + "_DESDE";

                                if (uiComp.getId().replace("_", " ").equalsIgnoreCase(labelHasta)) {
                                    traza.trace("argumento " + ind.getIndice() + "_HASTA" + " valor " + cal.getValue(), Level.INFO);

                                    if (cal.getValue() != null) {

                                        fechaHasta = new GregorianCalendar();
                                        fechas = new HashMap<String, GregorianCalendar>();

                                        Date fechHas = (Date) cal.getValue();
                                        try {
                                            fechaHasta.setTime(fechHas);
                                        } catch (NullPointerException e) {
                                        }

                                        fechas.put(labelHasta, fechaHasta);
                                        lstFechas.add(fechas);

                                        indice = new Indice();
                                        indice.setIdCategoria(ind.getIdCategoria());
                                        indice.setIdIndice(ind.getIdIndice());
                                        indice.setIndice(ind.getIndice());
                                        indice.setClave(ind.getClave());
                                        indice.setTipo(ind.getTipo());
                                        indice.setValor(sdf.format(cal.getValue()));
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

                                } else if (uiComp.getId().replace("_", " ").equalsIgnoreCase(labelDesde)) {
                                    traza.trace("argumento " + ind.getIndice() + "_DESDE" + " valor " + cal.getValue(), Level.INFO);

                                    if (cal.getValue() != null) {
                                        fechaDesde = new GregorianCalendar();
                                        fechas = new HashMap<String, GregorianCalendar>();

                                        Date fechDes = (Date) cal.getValue();
                                        try {
                                            fechaDesde.setTime(fechDes);
                                        } catch (NullPointerException e) {
                                        }

                                        fechas.put(labelDesde, fechaDesde);
                                        lstFechas.add(fechas);

                                        indice = new Indice();
                                        indice.setIdCategoria(ind.getIdCategoria());
                                        indice.setIdIndice(ind.getIdIndice());
                                        indice.setIndice(ind.getIndice());
                                        indice.setClave(ind.getClave());
                                        indice.setTipo(ind.getTipo());
                                        indice.setValor(sdf.format(cal.getValue()));
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
                        } else if (uiComp.getFamily().equalsIgnoreCase("javax.faces.SelectOne")) {

                            if (uiComp instanceof HtmlSelectOneMenu) {

                                HtmlSelectOneMenu oneMenu = (HtmlSelectOneMenu) uiComp;

                                if (uiComp.getId().replace("_", " ").equalsIgnoreCase(ind.getIndice())) {

                                    traza.trace("argumento " + ind.getIndice() + " valor " + oneMenu.getValue(), Level.INFO);

                                    indice = new Indice();
                                    indice.setIdCategoria(ind.getIdCategoria());
                                    indice.setIdIndice(ind.getIdIndice());
                                    indice.setIndice(ind.getIndice());
                                    indice.setClave(ind.getClave());
                                    indice.setTipo(ind.getTipo());
                                    indice.setValor(oneMenu.getValue());
                                    listaIndices.add(indice);
                                }

                            }
                        }
                    }
                }

                if (!subCategoria.isEmpty()) {
                    subCateSel = subCategoria.toArray();

                    for (SubCategoria sc : listaSubCategorias) {

                        int idSubCat = sc.getIdSubCategoria();

                        for (Object obj : subCateSel) {
                            int idSel = Integer.parseInt(obj.toString());

                            if (idSubCat == idSel) {
                                listaSubCat.add(sc);
                                traza.trace("id selecionado " + idSel, Level.INFO);
                                traza.trace("id del objecto agregado " + sc.getIdSubCategoria(), Level.INFO);
                            }
                        }
                    }

                }

                if (!tipoDocumento.isEmpty()) {
                    tipoDocSel = tipoDocumento.toArray();

                    for (TipoDocumento td : listaTipoDocumentos) {
                        for (Object obj : tipoDocSel) {
                            if (td.getIdTipoDocumento() == Integer.parseInt(obj.toString())) {
                                listaTipoDoc.add(td);
                            }
                        }
                    }
                }

                if (comprobarFechas(lstFechas, hijos)) {

                    consultaDinamicas = new BuscaExpedienteDinamico().consultarExpedienteDinamico(listaIndices, convertirSubCategorias(listaSubCat), convertirTipoDocumento(listaTipoDoc), 1, expediente.getIdLibreria());

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

                            resp = "resultado";

                            herramientas.navegarPagina("resultado");
                            traza.trace("exito al en la consulta dinamica ", Level.INFO);
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
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
            herramientas.error(e.getMessage());
            traza.trace("error al realizar la consulta", Level.ERROR, e);
        }

        //return null;
    }

    private List<com.develcom.expediente.SubCategoria> convertirSubCategorias(List<SubCategoria> listSubCat) {
        List<com.develcom.expediente.SubCategoria> scs = new ArrayList<com.develcom.expediente.SubCategoria>();
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
        List<com.develcom.expediente.TipoDocumento> tds = new ArrayList<com.develcom.expediente.TipoDocumento>();
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

    private boolean comprobarFechas(List<HashMap<String, GregorianCalendar>> lstFechas, List<UIComponent> hijos) {
        boolean resp = true;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < lstFechas.size(); i++) {

            HashMap<String, GregorianCalendar> fecha = lstFechas.get(i);

            for (UIComponent ui : hijos) {
                if (ui.getFamily().equalsIgnoreCase("org.richfaces.Calendar")) {
                    if (ui instanceof Calendar) {
                        Calendar cal = (Calendar) ui;
                        String etiDesde = cal.getId();
                        int desde = etiDesde.indexOf("DESDE");

                        if (desde != -1) {
                            String lblDesde = etiDesde.substring(0, desde - 1);
                            GregorianCalendar fechDesde = fecha.get(etiDesde);

                            for (int j = 0; j < lstFechas.size(); j++) {
                                HashMap<String, GregorianCalendar> fech = lstFechas.get(j);

                                for (UIComponent ui1 : hijos) {
                                    if (ui1.getFamily().equalsIgnoreCase("org.richfaces.Calendar")) {
                                        if (ui1 instanceof Calendar) {
                                            Calendar cal1 = (Calendar) ui1;
                                            String etiHasta = cal1.getId();
                                            int hasta = etiHasta.indexOf("HASTA");

                                            if (hasta != -1) {
                                                String lblHasta = etiHasta.substring(0, hasta - 1);
                                                GregorianCalendar fechHasta = fech.get(etiHasta);

                                                if (lblHasta.equalsIgnoreCase(lblDesde)) {
                                                    if (fechHasta != null) {
                                                        if (fechDesde != null) {
                                                            if (fechDesde.after(fechHasta)) {
                                                                resp = false;
                                                                herramientas.warn("problema de fechas");
                                                                herramientas.warn(etiDesde + " - " + sdf.format(fechDesde.getTime()));
                                                                herramientas.warn(etiHasta + " - " + sdf.format(fechHasta.getTime()));
                                                                traza.trace("problema de fechas", Level.INFO);
                                                                traza.trace(etiDesde + " - " + sdf.format(fechDesde.getTime()), Level.INFO);
                                                                traza.trace(etiHasta + " - " + sdf.format(fechHasta.getTime()), Level.INFO);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return resp;
    }

    private String crearID(String arg) {

        arg = arg.replace(" ", "_");

        return arg;
    }

    private String crearEtiqueta(String arg) {

        arg = arg.replace("_", " ");
        arg = arg.toLowerCase();
        char[] cs = arg.toCharArray();
        char ch = cs[0];
        cs[0] = Character.toUpperCase(ch);
        arg = String.valueOf(cs);

        return arg;
    }

    private UISelectItems llenarCombo(int codigo, boolean bandera) {

        UISelectItems items = new UISelectItems();
        List<SelectItem> datos = new ArrayList<SelectItem>();

        try {
            List<Combo> datosCombo = new LLenarListaDesplegable().buscarData(codigo, bandera);

            datos.add(new SelectItem(""));

            for (Combo combo : datosCombo) {
                datos.add(new SelectItem(combo.getDatoCombo()));
            }
            items.setValue(datos);

        } catch (SOAPException ex) {
            traza.trace("problemas al llenar la lista desplegable " + codigo, Level.ERROR, ex);
        }
        return items;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public HtmlPanelGrid getPanelIndicesDinamico() {

        FacesContext context = herramientas.getFacesContext();
        Application app = context.getApplication();
        panelIndicesDinamico = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
        List<Indices> listaIndices = new ArrayList<Indices>();

        try {

            traza.trace("creando formulario dinamico", Level.INFO);

            if (session != null) {

                listaIndices = new BuscaIndice().buscarIndice(expediente.getIdCategoria());
                convertirIndices(listaIndices);

                for (Indice ind : lstIndices) {

                    HtmlInputText input = (HtmlInputText) app.createComponent(HtmlInputText.COMPONENT_TYPE);
                    HtmlInputTextarea inputArea = (HtmlInputTextarea) app.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
                    HtmlOutputText label = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);
                    HtmlSelectOneMenu oneMenu = (HtmlSelectOneMenu) app.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);

                    if (ind.getTipo().equalsIgnoreCase("NUMERO")) {

                        input.setId(crearID(ind.getIndice()));
//                            input.setStyleClass("letra");

                        label.setValue(crearEtiqueta(ind.getIndice()));
                        label.setStyleClass("normalNegrita");

                        panelIndicesDinamico.getChildren().add(label);
                        panelIndicesDinamico.getChildren().add(input);

                    } else if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                        //desde
                        Calendar calendarDesde = (Calendar) app.createComponent(context, "org.primefaces.component.Calendar", "org.primefaces.component.CalendarRenderer");
                        HtmlOutputText labelDesde = new HtmlOutputText();

                        calendarDesde.setId(crearID(ind.getIndice()) + "_DESDE");
//                            calendarDesde.setStyleClass("letra");
                        calendarDesde.setPattern("dd/MM/yyyy");
                        //calendarDesde.setEnableManualInput(true);

                        labelDesde.setValue(crearEtiqueta(ind.getIndice()) + " desde");

                        panelIndicesDinamico.getChildren().add(labelDesde);
                        panelIndicesDinamico.getChildren().add(calendarDesde);

                        //hasta
                        Calendar calendarHasta = (Calendar) app.createComponent(context, "org.primefaces.component.Calendar", "org.primefaces.component.CalendarRenderer");
                        HtmlOutputText labelHasta = new HtmlOutputText();

                        calendarHasta.setId(crearID(ind.getIndice()) + "_HASTA");
//                            calendarHasta.setStyleClass("letra");
                        calendarHasta.setPattern("dd/MM/yyyy");
                        //calendarHasta.setEnableManualInput(true);

                        labelHasta.setValue(crearEtiqueta(ind.getIndice()) + " hasta");

                        panelIndicesDinamico.getChildren().add(labelHasta);
                        panelIndicesDinamico.getChildren().add(calendarHasta);

                    } else if (ind.getTipo().equalsIgnoreCase("COMBO")) {

                        oneMenu.setId(crearID(ind.getIndice()));
                        //oneMenu.setStyleClass("letra");
                        oneMenu.getChildren().add(llenarCombo(ind.getCodigo(), false));

                        label.setValue(crearEtiqueta(ind.getIndice()));
                        label.setStyleClass("normalNegrita");

                        panelIndicesDinamico.getChildren().add(label);
                        panelIndicesDinamico.getChildren().add(oneMenu);

                    } else if (ind.getTipo().equalsIgnoreCase("TEXTO")) {

                        input.setId(crearID(ind.getIndice()));
//                            input.setStyleClass("letra");

                        label.setValue(crearEtiqueta(ind.getIndice()));
                        label.setStyleClass("normalNegrita");

                        panelIndicesDinamico.getChildren().add(label);
                        panelIndicesDinamico.getChildren().add(input);

                    } else if (ind.getTipo().equalsIgnoreCase("AREA")) {

                        inputArea.setId(crearID(ind.getIndice()));
                        //inputArea.setStyleClass("letra");

                        label.setValue(crearEtiqueta(ind.getIndice()));
                        label.setStyleClass("normalNegrita");

                        panelIndicesDinamico.getChildren().add(label);
                        panelIndicesDinamico.getChildren().add(inputArea);

                    }
                }

            } else {
                herramientas.navegarPagina("index");
            }
        } catch (Exception e) {
            herramientas.error(e.getMessage());
            traza.trace("error al armar los indices dinamicos", Level.ERROR, e);

            herramientas.navegarPagina("index");
        }
        return panelIndicesDinamico;
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

    public void setPanelIndicesDinamico(HtmlPanelGrid panelIndicesDinamico) {
        this.panelIndicesDinamico = panelIndicesDinamico;
    }

    public List<Object> getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(List<Object> subCategoria) {
        this.subCategoria = subCategoria;
    }

    public List<SelectItem> getSubCategorias() {

        traza.trace("buscando subCategoria del expediente " + expediente.getCategoria() + " idCategoria " + expediente.getIdCategoria(), Level.INFO);

        try {

            if (!subCategorias.isEmpty()) {
                subCategorias.clear();
            }

            listaSubCategorias = new AdministracionBusqueda().buscarSubCategoria("", expediente.getIdCategoria(), 0);

            for (SubCategoria subCat : listaSubCategorias) {
                subCategorias.add(new SelectItem(subCat.getIdSubCategoria(), subCat.getSubCategoria()));
            }

        } catch (Exception e) {
            traza.trace("error al cargar las subCategorias", Level.ERROR, e);
        }
        return subCategorias;
    }

    public void setSubCategorias(List<SelectItem> subCategorias) {
        this.subCategorias = subCategorias;
    }

    public List<Object> getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(List<Object> tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<SelectItem> getTiposDocumentos() {

        Object[] subCateSel;
        List<Integer> idSubCate = new ArrayList<Integer>();
        List<com.develcom.expediente.TipoDocumento> tds;// = new ArrayList<com.develcom.expediente.TipoDocumento>();
        TipoDocumento documento;

        try {
            if (!tiposDocumentos.isEmpty()) {
                tiposDocumentos.clear();
            }

            tiposDocumentos.add(new SelectItem("Debe seleccionar una o varias SubCategorias"));

            if (!subCategoria.isEmpty()) {
                subCateSel = subCategoria.toArray();

                for (SubCategoria sc : listaSubCategorias) {
                    for (Object obj : subCateSel) {

                        if (sc.getSubCategoria().equalsIgnoreCase(obj.toString())) {
                            idSubCate.add(sc.getIdSubCategoria());
                        }
                    }
                }

                traza.trace("buscando los tipos de documentos de las subcategoria " + idSubCate, Level.INFO);
                tds = new BuscaExpedienteDinamico().buscarTiposDocumentos(idSubCate);
//                listaTipoDocumentos = new BuscaExpedienteDinamico().buscarTiposDocumentos(idSubCate);

                tiposDocumentos.clear();
                //for (TipoDocumento td : listaTipoDocumentos) {
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
                    tiposDocumentos.add(new SelectItem(tp.getIdTipoDocumento(), tp.getTipoDocumento()));
                }
            }
            idSubCate.clear();
        } catch (SOAPException ex) {
            traza.trace("error soap en el webservice", Level.ERROR, ex);
        } catch (SOAPFaultException ex) {
            traza.trace("error soapfault en el webservice", Level.ERROR, ex);
        }

        return tiposDocumentos;
    }

    public void setTiposDocumentos(List<SelectItem> tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

    public void regresar() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                herramientas.getExternalContext().redirect("libreria.xhtml");
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
