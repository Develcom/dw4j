/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.dao.Expediente;
import com.develcom.documento.Bufer;
import com.develcom.expediente.Indice;
import com.develcom.tool.Herramientas;
import com.develcom.tool.cryto.CodDecodArchivos;
import com.develcom.tool.log.Traza;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Level;
import ve.com.develcom.archivo.GestionArchivos;
import ve.com.develcom.busquedadinamica.BuscaExpedienteDinamico;

/**
 *
 * @author develcom
 */
@ManagedBean
@RequestScoped
public class Ficha {

    private Traza traza = new Traza(Ficha.class);
    private HtmlPanelGrid datosFicha = new HtmlPanelGrid();
    private HttpSession session;
    private Herramientas herramientas = new Herramientas();
    private boolean ficha;
    private boolean exiteFoto=true;
    private Expediente expediente;
    private String login;
    private String fotoFicha;
    private String msgFoto;

    /**
     * Creates a new instance of Ficha
     */
    public Ficha() {
        try {
            session = herramientas.crearSesion();
            if (session != null) {
                login = session.getAttribute("login").toString();
                expediente = (Expediente) session.getAttribute("expediente");
                ficha = Boolean.parseBoolean(session.getAttribute("ficha").toString());
                
            }
        } catch (Exception e) {
            traza.trace("problemas al capturar la sesion", Level.ERROR, e);
            herramientas.error(e.getMessage());
            try {
                herramientas.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                traza.trace("error", Level.ERROR, ex);
            }
        }
        traza.trace("mostrar ficha? " + ficha, Level.INFO);
    }

    private void cargarFoto() {
//        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
//        Properties prop = System.getProperties();
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//        String userdir = System.getProperty("user.dir");
//        String temp = System.getProperty("java.io.tmpdir");
        String catalinaBase = System.getProperty("catalina.base");
        String rutaCompleta = catalinaBase + "/webapps/DW4JWeb/resources/images";
        FileOutputStream escribiendo;
        com.develcom.expediente.InfoDocumento infoDocumento;
        File fileCod, fileFoto;
        String rutaCod, rutaFoto;

        try {
//            traza.trace("directorio de usuario " + userdir, Level.INFO);
            traza.trace("base catalina (tomcat)  " + catalinaBase, Level.INFO);
            traza.trace("ruta completa  " + rutaCompleta, Level.INFO);
            rutaCod = rutaCompleta + "/foto.cod";
            rutaFoto = rutaCompleta + "/foto.jpg";

            fileCod = new File(rutaCod);
            fileFoto = new File(rutaFoto);
            
            if(fileCod.exists()){
                fileCod.delete();
            }
            if(fileFoto.exists()){
                fileFoto.delete();
            }

            
            infoDocumento = new BuscaExpedienteDinamico().buscarFotoFicha(expediente.getIdExpediente());

            if ((infoDocumento != null) && (infoDocumento.getEstatus() != 0) && (infoDocumento.getEstatus() != 2)) {
                exiteFoto=true;
                Bufer buffer = new GestionArchivos().buscandoArchivo(infoDocumento.getRutaArchivo(), infoDocumento.getNombreArchivo());
                //byte[] buffer = new GestionArchivos().buscandoArchivo(infoDocumento.getRutaArchivo(), infoDocumento.getNombreArchivo());

                escribiendo = new FileOutputStream(fileCod);
                //escribiendo.write(buffer);
                escribiendo.write(buffer.getBufer());
                escribiendo.flush();
                escribiendo.close();

                new CodDecodArchivos().decodificar(rutaCod, rutaFoto);
                
                if(fileFoto.exists()){
                    fotoFicha = "foto.jpg";
                }else{
                    herramientas.warn("Problemas al buscar la foto del expediente Posiblemente no existe o no esta configurado");
                    fotoFicha = "no_disponible.jpg";
                }
                
            } else {
                fotoFicha="";
                fotoFicha = "no_disponible.jpg";
                exiteFoto=false;
                herramientas.warn("Problemas al buscar la foto del expediente Posiblemente no existe, no esta configurado o no este aprobado");
//                JOptionPane.showMessageDialog(this, "Problemas al buscar la foto del expediente\nPosiblemente no existe o\nno esta configurado", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            herramientas.error("Problemas al obtener la foto \n"+ex.getMessage());
            traza.trace("problema al cargar la foto", Level.INFO, ex);
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

    public HtmlPanelGrid getDatosFicha() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        FacesContext context = FacesContext.getCurrentInstance();
//        UIViewRoot viewRoot = context.getViewRoot();
        Application app = context.getApplication();
        datosFicha = (HtmlPanelGrid) app.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
//        List<Indice> listaIndice = ManejoSesion.getIndices();
        List<Indice> listaIndice;
        String fecha = "";
        HtmlOutputText label;

        try {
            if (session != null) {
                listaIndice = (List<Indice>) session.getAttribute("listaIndices");
                //listaIndice = (List<Indice>) herramientas.getSessionScope().get("listaIndices");
//                listaIndice = (List<Indice>) herramientas.leerCookie("listaIndices");
                for (Indice ind : listaIndice) {

                    label = (HtmlOutputText) app.createComponent(HtmlOutputText.COMPONENT_TYPE);

                    if ((!ind.getIndice().equalsIgnoreCase("ID_CATEGORIA"))
                            && (!ind.getIndice().equalsIgnoreCase("ID_SUBCATEGORIA"))) {

                        if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                            try {

                                if (!ind.getIndice().equalsIgnoreCase(fecha)) {

//                                    fecha = ind.getIndice();
//                                    XMLGregorianCalendar xmlCal = (XMLGregorianCalendar) ind.getValor();
//                                    Calendar cal = xmlCal.toGregorianCalendar();

//                                    label.setId(ind.getIndice());

                                    String dato = ind.getValor().toString();

                                    label.setStyle("ficha");
                                    label.setValue(crearEtiqueta(ind.getIndice()) + ": " + dato);

                                    datosFicha.getChildren().add(label);

                                    traza.trace(label.getValue().toString(), Level.INFO);

//                                    label = null;

                                }

                            } catch (NullPointerException e) {

                                fecha = ind.getIndice();
//                                label.setId(ind.getIndice());

                                label.setStyle("ficha");
                                label.setValue(crearEtiqueta(ind.getIndice()) + ": ");

                                datosFicha.getChildren().add(label);

                                traza.trace(label.getValue().toString(), Level.INFO);

//                                label = null;
                            }


                        } else {
                            try {
//                                label.setId(ind.getIndice());

                                label.setStyle("ficha");
                                label.setValue(crearEtiqueta(ind.getIndice()) + ": " + ind.getValor().toString());

                                datosFicha.getChildren().add(label);

                                traza.trace(label.getValue().toString(), Level.INFO);

//                                label = null;
                            } catch (NullPointerException e) {
//                                label.setId(ind.getIndice());

                                label.setStyle("ficha");
                                label.setValue(crearEtiqueta(ind.getIndice()) + ": ");

                                datosFicha.getChildren().add(label);

                                traza.trace(label.getValue().toString(), Level.INFO);

//                                label = null;
                            }
                        }
                    }
                }
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
        }
        cargarFoto();
        return datosFicha;
    }

    public void setDatosFicha(HtmlPanelGrid datosFicha) {
        this.datosFicha = datosFicha;
    }

    public boolean isFicha() {
        return ficha;
    }

    public void setFicha(boolean ficha) {
        this.ficha = ficha;
    }

    public boolean isExiteFoto() {
        return exiteFoto;
    }

    public void setExiteFoto(boolean exiteFoto) {
        this.exiteFoto = exiteFoto;
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

    public String getFotoFicha() {
        return fotoFicha;
    }

    public void setFotoFicha(String fotoFicha) {
        this.fotoFicha = fotoFicha;
    }

    public String getMsgFoto() {
        return msgFoto;
    }

    public void setMsgFoto(String msgFoto) {
        this.msgFoto = msgFoto;
    }

    public void regresar() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                herramientas.getExternalContext().redirect("arbol.xhtml");
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
