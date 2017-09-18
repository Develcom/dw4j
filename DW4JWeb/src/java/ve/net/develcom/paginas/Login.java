/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.autentica.Sesion;
import com.develcom.autentica.Usuario;
import java.io.File;
import java.net.ConnectException;
import java.util.List;
import java.util.Properties;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.net.develcom.tool.Propiedades;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;
import ve.com.develcom.sesion.IniciaSesion;
import ve.com.develcom.tools.Configuracion;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Constantes;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.ToolsFiles;
import ve.net.develcom.tool.barra.IBarraEstado;

/**
 *
 * @author develcom
 */
public class Login extends SelectorComposer<Component> {

    private static final long serialVersionUID = 6575089769533027567L;

    @Wire
    private Combobox usuario;

    @Wire
    private Textbox pass;

    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(Login.class);
    private Session session;
    private Properties propiedades;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    private void iniciar() {
        List<Usuario> users;

        try {

            Configuracion.setWeb(true);
            propiedades = Propiedades.cargarPropiedades();

//            Properties env = System.getProperties();
//
//            Enumeration pro = env.propertyNames();
//            Enumeration prop = env.elements();
//
//            while (pro.hasMoreElements() && prop.hasMoreElements()) {
//                traza.trace("propiedad " + pro.nextElement().toString() + " valor " + prop.nextElement().toString(), Level.INFO);
//            }
            users = new IniciaSesion().autocomplete();

            Comboitem item;

            for (Usuario user : users) {
                item = new Comboitem();
                item.setValue(user.getIdUsuario());
                item.setLabel(user.getIdUsuario());
                item.setParent(usuario);
            }

        } catch (SOAPException | ConnectException ex) {
            herramientas.error("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n", ex);
        } catch (SOAPFaultException ex) {
            herramientas.error("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n", ex);
        } catch (WebServiceException ex) {
            herramientas.error("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n", ex);
        }

    }

    @Listen("onClick = #sesion")
    public void iniciarSesion() {
        com.develcom.autentica.Configuracion configuracion;
        String sesionID, navegador;
        Sesion verifica;
        List<Sesion> sesion;
        boolean consulta = false, ficha;
        //IBarraEstado barraEstado;
        //String userdir = System.getProperty("user.dir");
        int tiempoSesion;

        try {
            Expediente expediente = new Expediente();

            if (!pass.getValue().equalsIgnoreCase("") && !usuario.getValue().equalsIgnoreCase("")) {

                traza.trace("ingresando el usuario " + usuario.getValue(), Level.INFO);

                traza.trace("validando el usuario", Level.INFO);

                verifica = new IniciaSesion().iniciarSesion(usuario.getValue(), pass.getValue());

                if (!verifica.getVerificar().equalsIgnoreCase("ldap")) {
                    if (!verifica.getVerificar().equalsIgnoreCase("basedato")) {
                        if (!verifica.getVerificar().equalsIgnoreCase("inactivo")) {

                            if (verifica.getVerificar().equalsIgnoreCase("exito")) {

                                configuracion = verifica.getConfiguracion();
                                traza.trace("creando la sesion del usuario", Level.INFO);

                                sesion = new IniciaSesion().armarSesion(usuario.getValue());
                                //ManejoSesion.setSesion(sesion);

                                if ((sesion != null) && (sesion.size() >= 1)) {
                                    //ManejoSesion.setLogin(sesion.get(0).getIdUsuario());

                                    traza.trace("id de la sesion webService " + sesion.get(0).getIdSession(), Level.INFO);
                                    traza.trace("fecha y hora de inicio de la sesion " + sesion.get(0).getFechaHora(), Level.INFO);

                                    if (sesion.get(0).getEstatusUsuario().equalsIgnoreCase(Constantes.ACTIVO)) {

                                        session = herramientas.crearSesion();
                                        //barraEstado = herramientas.getBarraEstado();
                                        
                                        //barraEstado.setStatus("Verificando el Rol del usuario "+usuario.getValue());
                                        for (Sesion s : sesion) {

                                            if (s.getRolUsuario().getRol().equalsIgnoreCase(Constantes.CONSULTAR)) {
                                                consulta = true;
                                                break;
                                            } else {
                                                consulta = false;
                                            }
                                        }
                                        for (Sesion s : sesion) {
                                            usuario.setValue(s.getIdUsuario());
                                            if (s.getRolUsuario().getRol().equalsIgnoreCase(Constantes.IMPRIMIR)) {
                                                expediente.setRolUsuario(Constantes.IMPRIMIR);
                                                session.setAttribute("expediente", expediente);
                                                break;
                                            }
//                                            else{
//                                                session.setAttribute("expediente", expediente);
//                                            }
                                        }
                                        traza.trace("consulta " + consulta, Level.INFO);
                                        if (consulta) {
                                            
                                            //barraEstado.setStatus("Fijando variables de entorno");

                                            ficha = verifica.getConfiguracion().isFicha();
                                            sesionID = herramientas.getID();
                                            navegador = herramientas.getNavegador();

                                            traza.trace("navegador " + navegador, Level.INFO);
                                            traza.trace("id sesion del usuario " + usuario + " " + sesionID, Level.INFO);
                                            traza.trace("configuracion ficha " + ficha, Level.INFO);
                                            traza.trace("directorio " + session.getWebApp().getDirectory(), Level.INFO);

                                            session.setAttribute("configuracion", configuracion);
                                            session.setAttribute("ficha", ficha);
                                            session.setAttribute("login", usuario.getValue());
                                            session.setAttribute("sesionID", sesionID);
                                            session.setAttribute("propiedades", propiedades);
                                            tiempoSesion = Integer.parseInt(propiedades.getProperty("timeSession"));
                                            session.setMaxInactiveInterval(tiempoSesion);
                                            session.setAttribute("navegador", navegador);

                                            if (new ToolsFiles().getDirTemporal().exists()) {

                                                File[] files = new ToolsFiles().getDirTemporal().listFiles();
                                                for (File f : files) {
                                                    if (f.delete()) {
                                                        traza.trace("eliminado archivo " + f.getName(), Level.INFO);
                                                    } else {
                                                        traza.trace("problemas al eliminar el archivo " + f.getName(), Level.WARN);
                                                        f.deleteOnExit();
                                                    }
                                                }
                                            }

                                            herramientas.navegarPagina("libreria.zul");

                                        } else {
                                            herramientas.warn("El usuario " + usuario + " no tiene rol de CONSULTAR");
                                            traza.trace("El usuario " + usuario + " no tiene rol de CONSULTAR", Level.INFO);
                                        }
                                    } else {
                                        herramientas.info("Usuario Inactivo");
                                    }

                                } else {
                                    herramientas.info("Problemas para crear la sesion \ncomuniquese con el administrador del sistema");
                                }

                            } else {
                                herramientas.info(verifica.getVerificar() + "\ncomuniquese con el administrador del sistema\n");
                            }

                        } else {
                            herramientas.info("Usuario esta inactivo para el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                        }

                    } else {
                        herramientas.info("Usuario no registrado en el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                    }
                } else {
                    herramientas.info(verifica.getRespuesta() + "\ncomuniquese con el administrador del sistema");
                }
            } else {
                herramientas.info("Debe INGRESAR USUARIO y CONTRASEÑA");
            }
        } catch (Exception ex) {
            herramientas.error("Error general en el inicio de sesion ", ex);
            traza.trace("error en el inicio de sesion", Level.ERROR, ex);
        }

    }

}
