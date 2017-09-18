/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.tool.Propiedades;
import com.develcom.tool.log.Traza;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@ManagedBean
@RequestScoped
public class Imagen {

    private Traza traza = new Traza(Imagen.class);
    private String logo;
    private String fotoFicha;
    private Properties propiedades;
    private String ipServidorWeb;
    private String rutaFoto;

    /**
     * Creates a new instance of Imagen
     */
    public Imagen() {
        propiedades = new Propiedades().cargarPropiedades();
//        Herramientas herramientas = new Herramientas();
        
        
//        if(Herramientas.FLAG_FOTO){
//            FacesContext context = herramientas.getFacesContext();
//            ExternalContext ec = herramientas.getExternalContext();
//            HttpServletResponse response = (HttpServletResponse) ec.getResponse();
//            response.setContentType("image/jpeg");
//        }
        
        logo = propiedades.getProperty("logo");
        traza.trace("archivo logo "+logo, Level.INFO);
        
        fotoFicha = propiedades.getProperty("foto");
        traza.trace("archivo foto ficha "+fotoFicha, Level.INFO);
        
        ipServidorWeb = propiedades.getProperty("servidorWEB");
        traza.trace("direccion ip del servidor web "+ipServidorWeb, Level.INFO);
        
        rutaFoto = "http://"+ipServidorWeb+":8080/DW4JWeb/resources/images/"+fotoFicha;
        traza.trace("ruta de la foto "+rutaFoto, Level.INFO);
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFotoFicha() {
        return fotoFicha;
    }

    public void setFotoFicha(String fotoFicha) {
        this.fotoFicha = fotoFicha;
    }

    public String getIpServidorWeb() {
        return ipServidorWeb;
    }

    public void setIpServidorWeb(String ipServidorWeb) {
        this.ipServidorWeb = ipServidorWeb;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }
}
