/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.autentica.Perfil;
import java.util.List;
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
import ve.com.develcom.sesion.IniciaSesion;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Constantes;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.barra.IBarraEstado;

/**
 *
 * @author develcom
 */
public class Libreria extends SelectorComposer<Component> {

    private static final long serialVersionUID = -5123136117133297527L;

    @Wire
    private Combobox libreria;

    @Wire
    private Combobox categoria;

    private Herramientas herramientas = new Herramientas();
    private List<Perfil> perfiles;
    private Traza traza = new Traza(Libreria.class);
    private Session session;
    private String login;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {

        session = herramientas.crearSesion();

        if (session != null) {
            login = session.getAttribute("login").toString();

            llenarComboLibrerias();
        }

    }

    private void llenarComboLibrerias() {

        Comboitem item;

        try {
            categoria.setDisabled(true);

            if (session != null) {

                perfiles = new IniciaSesion().buscarLibCatPerfil(login, "CONSULTAR");

                traza.trace("llenando lista de librerias", Level.INFO);
                String lib = "";

                for (Perfil perfil : perfiles) {
                    String des = perfil.getLibreria().getDescripcion();

                    if (!lib.equalsIgnoreCase(des)) {
                        lib = perfil.getLibreria().getDescripcion();

                        if (perfil.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                            traza.trace("libreria agregada " + lib, Level.INFO);
                            item = new Comboitem();
                            item.setValue(perfil.getLibreria().getIdLibreria());
                            item.setLabel(lib);
                            item.setParent(libreria);
                        }
                    }
                }

            } else {
                herramientas.warn("Problemas con el objecto Sesion");
                herramientas.navegarPagina("index.zul");
            }
        } catch (SOAPException | SOAPFaultException e) {
            herramientas.error("Error al llenar la lista de librerias ", e);
            traza.trace("error llenando el combo libreria", Level.ERROR, e);
        }
    }

    @Listen("onSelect = #libreria")
    public void llenarComboCategoria() {
        int lib;
        String cat = "";
        Comboitem item;

        traza.trace("libreria selecionada para llenar combo categoria " + libreria.getValue(), Level.INFO);

        try {

            try {

                for (int i = 0; i < categoria.getItems().size(); i++) {
                    categoria.removeItemAt(i);
                }
                categoria.getItems().clear();
            } catch (NullPointerException e) {
            }

            if (session != null) {

//                if (libreria != null) {
                lib = libreria.getSelectedItem().getValue();

                traza.trace("llenando lista de categorias", Level.INFO);
                traza.trace("id libreria seleccionada " + lib, Level.INFO);

                for (Perfil cate : perfiles) {
                    String categ = cate.getCategoria().getCategoria();

                    if (cate.getLibreria().getIdLibreria() == lib) {
                        if (!cat.equalsIgnoreCase(categ)) {
                            cat = cate.getCategoria().getCategoria();

                            if (cate.getCategoria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                                traza.trace("categoria agregada " + cat, Level.INFO);
                                item = new Comboitem();
                                item.setValue(cate.getCategoria().getIdCategoria());
                                item.setLabel(cat);
                                item.setParent(categoria);
                            }
                        }
                    }
                }
                categoria.setDisabled(false);
//                }
            } else {
                herramientas.warn("Problemas con el objecto Sesion");
                herramientas.navegarPagina("index.zul");
            }

        } catch (Exception e) {
            herramientas.error("Problemas al llenar la lista de categorias", e);
            traza.trace("error al llenar lista de categoria", Level.ERROR, e);
        }
    }

    @Listen("onClick = #ok")
    public void aceptar() {

        String libr, cate;
        int lib, cat;
        //IBarraEstado barraEstado;
        Expediente expediente;

        try {

            expediente = (Expediente) session.getAttribute("expediente");
            //barraEstado = herramientas.getBarraEstado();

            libr = libreria.getValue();
            cate = categoria.getValue();

            traza.trace("libreria selecionada " + libr, Level.INFO);
            traza.trace("categoria selecionada " + cate, Level.INFO);

            //barraEstado.setStatus("Comprobando la Libreria y la Categoria");
            if (!libr.equalsIgnoreCase("")) {
                if (!cate.equalsIgnoreCase("")) {

                    lib = libreria.getSelectedItem().getValue();
                    cat = categoria.getSelectedItem().getValue();

                    for (Perfil perfil : perfiles) {

                        if (perfil.getLibreria().getIdLibreria() == lib) {
                            expediente.setIdLibreria(lib);
                            expediente.setLibreria(perfil.getLibreria().getDescripcion());
                        }
                        if (perfil.getCategoria().getIdCategoria() == cat) {
                            expediente.setIdCategoria(cat);
                            expediente.setCategoria(perfil.getCategoria().getCategoria());
                        }
                    }

                    session.setAttribute("expediente", expediente);
                   // barraEstado.setStatus("Correcto...!!!");
                    herramientas.navegarPagina("consulta.zul");

                    traza.trace("seleccion la libreria " + expediente.getLibreria() + " id " + expediente.getIdLibreria(), Level.INFO);
                    traza.trace("seleccion la categoria " + expediente.getCategoria() + " id " + expediente.getIdCategoria(), Level.INFO);
                } else {
                    herramientas.warn("Debe seleccionar una categoria");
                }
            } else {
                herramientas.warn("Debe seleccionar una libreria");
            }
        } catch (NumberFormatException ex) {
            herramientas.error("", ex);
            traza.trace("error en la conversion del id", Level.ERROR, ex);
        } catch (WrongValueException ex) {
            herramientas.error("", ex);
            traza.trace("Error al aceptar", Level.ERROR, ex);
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

}
