/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.autentica.Perfil;
import com.develcom.dao.Expediente;
import com.develcom.tool.Constantes;
import com.develcom.tool.Herramientas;
import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Level;
import ve.com.develcom.sesion.IniciaSesion;

/**
 *
 * @author develcom
 */
@ManagedBean
@ViewScoped
public class Libreria implements Serializable{

    private Herramientas herramientas = new Herramientas();
    private List<Perfil> perfiles;
    private Traza traza = new Traza(Libreria.class);
    private static final long serialVersionUID = 224876215L;
    private String login;
    private List<SelectItem> librerias = new ArrayList<SelectItem>();
    private String libreria;
    private List<SelectItem> categorias = new ArrayList<SelectItem>();
    private String categoria;
    private boolean enable;
    private HttpSession session;
    private Herramientas tools = new Herramientas();

    /**
     * Creates a new instance of Libreria
     */
    public Libreria() {
        try {
            session = tools.crearSesion();

            if (session != null) {

                login = session.getAttribute("login").toString();

            } else {
                tools.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
            traza.trace("error al inicar el objeto libreria", Level.INFO, e);
            tools.fatal("error al inicar el objeto libreria " + e.getMessage());
            try {
                traza.trace("error con la sesion", Level.ERROR, e);
                tools.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                traza.trace("error", Level.ERROR, ex);
            }
        }
    }
    private void llenarComboLibrerias() {

        traza.trace("bean Libreria", Level.INFO);

        try {

            if (session != null) {

                perfiles = new IniciaSesion().buscarLibCatPerfil(login, "CONSULTAR");

                traza.trace("llenando lista de librerias", Level.INFO);
                String lib = "";

                try {

                    if (!librerias.isEmpty()) {
                        librerias.clear();
                    }

                    for (Perfil perfil : perfiles) {
                        String des = perfil.getLibreria().getDescripcion();

                        if (!lib.equalsIgnoreCase(des)) {
                            lib = perfil.getLibreria().getDescripcion();

                            if (perfil.getLibreria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                                traza.trace("libreria agregada " + lib, Level.INFO);
                                librerias.add(new SelectItem(perfil.getLibreria().getIdLibreria(), lib));
                            }
                        }
                    }

                } catch (Exception e) {
                    tools.error(e.getMessage());
                    traza.trace("error al llenar el combo libreria", Level.ERROR, e);
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
            tools.error(e.getMessage());
            traza.trace("error llenando el combo libreria", Level.ERROR, e);
        }
    }
    
    public String aceptar() {

        String libr, cate;
        int lib, cat;
        String resp = "";
        HtmlSelectOneMenu compLib, compCat;
//        Expediente expediente = (Expediente) tools.getSessionMap().get("expediente");
        Expediente expediente = (Expediente) session.getAttribute("expediente");

        try {
            
            libr = libreria;
            cate = categoria;
            
            if(libr.equalsIgnoreCase("") && cate.equalsIgnoreCase("")){
                compLib = (HtmlSelectOneMenu) tools.findComponentInRoot("lib");
                compCat = (HtmlSelectOneMenu) tools.findComponentInRoot("cate");

                libr = compLib.getValue().toString();
                cate = compCat.getValue().toString();
            }
            
            traza.trace("libreria selecionada " + libr, Level.INFO);
            traza.trace("categoria selecionada " + cate, Level.INFO);

            if (!libr.equalsIgnoreCase("")) {
                if (!cate.equalsIgnoreCase("")) {

                    lib = Integer.parseInt(libreria);
                    cat = Integer.parseInt(categoria);

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
//                    tools.getSessionMap().put("expediente", expediente);
                    session.setAttribute("expediente", expediente);

                    resp = "consultar";

                    traza.trace("seleccion la libreria " + expediente.getLibreria() + " id " + expediente.getIdLibreria(), Level.INFO);
                    traza.trace("seleccion la categoria " + expediente.getCategoria() + " id " + expediente.getIdCategoria(), Level.INFO);
                } else {
                    tools.warn("Debe seleccionar una categoria");
                }
            } else {
                tools.warn("Debe seleccionar una libreria");
            }
        } catch (NumberFormatException ex) {
            tools.error(ex.getMessage());
            traza.trace("error en la conversion del id", Level.ERROR, ex);
        } catch (Exception ex) {
            tools.error(ex.getMessage());
            traza.trace("Error al aceptar", Level.ERROR, ex);
        }

        libreria = "";
        return resp;

    }

    public boolean isEnable() {

        if (libreria != null) {
            enable = false;
        } else {
            enable = true;
        }
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<SelectItem> getLibrerias() {
        llenarComboLibrerias();
        return librerias;
    }

    public void setLibrerias(List<SelectItem> librerias) {
        this.librerias = librerias;
    }

    public String getLibreria() {
        return libreria;
    }

    public void setLibreria(String libreria) {
        this.libreria = libreria;
    }

    public List<SelectItem> getCategorias() {
        String lib;
        String cat = "";

        traza.trace("libreria selecionada para llenar combo categoria " + libreria, Level.INFO);

        try {

            if (session != null) {

                if (!categorias.isEmpty()) {
                    categorias.clear();
                }

                if (libreria != null) {
                    lib = libreria;

                    traza.trace("llenando lista de categorias", Level.INFO);
                    traza.trace("id libreria seleccionada " + lib, Level.INFO);

                    for (Perfil cate : perfiles) {
                        String categ = cate.getCategoria().getCategoria();

                        if (cate.getLibreria().getIdLibreria() == Integer.parseInt(lib)) {
                            if (!cat.equalsIgnoreCase(categ)) {
                                cat = cate.getCategoria().getCategoria();

                                if (cate.getCategoria().getEstatus().equalsIgnoreCase(Constantes.ACTIVO)) {
                                    traza.trace("categoria agregada " + cat, Level.INFO);
                                    categorias.add(new SelectItem(cate.getCategoria().getIdCategoria(), cat));
                                }
                            }
                        }
                    }
                }
            } else {
                tools.getExternalContext().redirect("index.xhtml");
            }

        } catch (Exception e) {
            tools.error(e.getMessage());
            traza.trace("error al llenar lista de categoria", Level.ERROR, e);
        }

        return categorias;
    }

    public void setCategorias(List<SelectItem> categorias) {
        this.categorias = categorias;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
