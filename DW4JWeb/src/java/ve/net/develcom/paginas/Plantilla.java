/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Level;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Column;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.Propiedades;

/**
 *
 * @author develcom
 */
public class Plantilla extends SelectorComposer<Component> {

    private static final long serialVersionUID = 3047366744222835818L;

    @Wire
    private Image logo;

    @Wire
    private Column login;

    @Wire
    private Label lib;

    @Wire
    private Label cat;

    private Traza traza = new Traza(Plantilla.class);

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {
        AMedia image;

        File file;
        ByteArrayInputStream is;
        FileInputStream fs;
        byte[] buffer;

        Herramientas herramientas = new Herramientas();
        String userdir = System.getProperty("user.dir");
        String catalinaBase = System.getProperty("catalina.base");
        String rutaCompleta = catalinaBase + "/webapps/DW4JWeb/resources/images/";
        String user = null;
        Properties propiedades;
        Session session = herramientas.crearSesion();
        Expediente expediente = null;

        try {

            propiedades = Propiedades.cargarPropiedades();

            traza.trace("logo " + rutaCompleta + propiedades.getProperty("logo"), Level.INFO);
            traza.trace("userdir " + userdir, Level.INFO);
            traza.trace("logo " + userdir + "/resources/images/" + propiedades.getProperty("logo"), Level.INFO);
            logo.setSrc("/img/" + propiedades.getProperty("logo"));
//            logo.setSrc(rutaCompleta + propiedades.getProperty("logo"));
//            logo.setSrc(userdir + "/resources/images/" + propiedades.getProperty("logo"));

            try {
                user = session.getAttribute("login").toString();
                expediente = (Expediente) session.getAttribute("expediente");
                session.getWebApp().setAppName("DW4JWeb");
            } catch (Exception e) {
            }

            if (user != null) {
                login.setLabel(user);
            } else {
                login.setLabel("Bienvenido");
            }

            try {
                if ((expediente.getCategoria() != null) && (expediente.getLibreria() != null)) {
                    lib.setValue("Libreria: " + expediente.getLibreria());
                    cat.setValue("Categoria: " + expediente.getCategoria());
                } else {
                    lib.setVisible(false);
                    cat.setVisible(false);
                }
            } catch (NullPointerException e) {
            }
            
        } catch (Exception e) {
            traza.trace("problemas con la plantilla", Level.ERROR, e);
        }
    }

}
