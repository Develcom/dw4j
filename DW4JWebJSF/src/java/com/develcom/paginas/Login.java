/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.autentica.Sesion;
import com.develcom.autentica.Usuario;
import com.develcom.dao.Expediente;
import com.develcom.tool.Constantes;
import com.develcom.tool.Herramientas;
import com.develcom.tool.Propiedades;
import com.develcom.tool.log.Traza;
import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.sesion.IniciaSesion;
import ve.com.develcom.tools.Configuracion;

/**
 *
 * @author develcom
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable {
    
    private String navegador;
    private List<Usuario> usuarios;
    private static final long serialVersionUID = 4209431922341098902L;
    private Properties propiedades;
    private String usuario;
    private String contrasenia;
    private Traza traza = new Traza(Login.class);
    private HttpSession session;    
    private Herramientas herramientas = new Herramientas();
    //private List<String> autocompletarusuarios = new ArrayList<String>();

    /**
     * Creates a new instance of Login
     */
    public Login() {
        try {
            Configuracion.setWeb(true);
            propiedades = new Propiedades().cargarPropiedades();

            usuarios = new IniciaSesion().autocomplete();

            navegador = herramientas.navegador();

            int ff = navegador.indexOf("Firefox");
            int ie = navegador.indexOf("MSIE");
            int chro = navegador.indexOf("Chrome");

            if (ff != -1) {
                navegador = navegador.substring(ff, (ff + 7));
            } else if (ie != -1) {
                navegador = navegador.substring(ie, (ie + 4));
            } else if (chro != -1) {
                navegador = navegador.substring(chro, (chro + 6));
            }
            traza.trace("navegador " + navegador, Level.INFO);
        } catch (SOAPException ex) {
            herramientas.fatal("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n" + ex.getMessage());
        } catch (SOAPFaultException ex) {
            herramientas.fatal("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n" + ex.getMessage());
        } catch (ConnectException ex) {
            herramientas.fatal("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n" + ex.getMessage());
        } catch (WebServiceException ex){
            herramientas.fatal("Problemas en el inicio de sesion\nComuniquese con el administrador del Sistema\n" + ex.getMessage());
        }
    }

    public List<String> autocompletarusuarios(String query) {
        List<String> users = new ArrayList<String>();

        for (Usuario user : usuarios) {
            if (user.getIdUsuario().startsWith(query)) {
                users.add(user.getIdUsuario());
            }
        }

        return users;
    }

    public String inicarSesion() {
        String resp = "", sesionID;
        Sesion verifica;
        List<Sesion> sesion;
        boolean consulta = false, ficha;
        //String userdir = System.getProperty("user.dir");
        int tiempoSesion;


        try {
            Expediente expediente = new Expediente();

            if (!contrasenia.equalsIgnoreCase("") && !usuario.equalsIgnoreCase("")) {

                traza.trace("ingresando el usuario " + usuario, Level.INFO);

                traza.trace("validando el usuario", Level.INFO);

                verifica = new IniciaSesion().iniciarSesion(usuario, contrasenia);

                if (!verifica.getVerificar().equalsIgnoreCase("ldap")) {
                    if (!verifica.getVerificar().equalsIgnoreCase("basedato")) {
                        if (!verifica.getVerificar().equalsIgnoreCase("inactivo")) {

                            if (verifica.getVerificar().equalsIgnoreCase("exito")) {

                                traza.trace("creando la sesion del usuario", Level.INFO);

                                sesion = new IniciaSesion().armarSesion(usuario);
                                //ManejoSesion.setSesion(sesion);

                                if ((sesion != null) && (sesion.size() >= 1)) {
                                    //ManejoSesion.setLogin(sesion.get(0).getIdUsuario());

                                    traza.trace("id de la sesion webService " + sesion.get(0).getIdSession(), Level.INFO);
                                    traza.trace("fecha y hora de inicio de la sesion " + sesion.get(0).getFechaHora(), Level.INFO);


                                    if (sesion.get(0).getEstatusUsuario().equalsIgnoreCase(Constantes.ACTIVO)) {

                                        session = herramientas.crearSesion();

                                        for (Sesion s : sesion) {

                                            if (s.getRolUsuario().getRol().equalsIgnoreCase(Constantes.CONSULTAR)) {
                                                consulta = true;
                                                break;
                                            } else {
                                                consulta = false;
                                            }
                                        }
                                        for (Sesion s : sesion) {
                                            usuario = s.getIdUsuario();
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

                                            sesionID = session.getId();
                                            
                                            ficha = verifica.getConfiguracion().isFicha();

                                            traza.trace("id sesion del usuario " + usuario + " " + sesionID, Level.INFO);
                                            traza.trace("configuracion ficha " + ficha, Level.INFO);

                                            session.setAttribute("ficha", ficha);
                                            session.setAttribute("login", usuario);
                                            session.setAttribute("sesionID", sesionID);
                                            session.setAttribute("propiedades", propiedades);
                                            tiempoSesion = Integer.parseInt(propiedades.getProperty("timeSession"));
                                            session.setMaxInactiveInterval(tiempoSesion);
                                            session.setAttribute("navegador", navegador);

                                            traza.trace("id de la sesion aplicacion web " + sesionID, Level.INFO);

                                            resp = "libreria";
                                        } else {
                                            herramientas.warn("El usuario " + usuario + " no tiene rol de CONSULTAR");
                                            traza.trace("El usuario " + usuario + " no tiene rol de CONSULTAR", Level.INFO);
                                        }
                                    } else {
                                        herramientas.info("Usuario Inactivo");
                                        //throw new Exception("Usuario Inactivo");
                                    }

                                } else {
                                    herramientas.info("Problemas para crear la sesion \ncomuniquese con el administrador del sistema");
                                    //throw new Exception("Problemas para crear la crearSesion \ncomuniquese con el administrador del sistema");
                                }

                            } else {
                                herramientas.info(verifica.getVerificar() + "\ncomuniquese con el administrador del sistema\n");
                                //throw new Exception(verifica.getVerificar() + "\ncomuniquese con el administrador del sistema\n");
                            }

                        } else {
                            herramientas.info("Usuario esta inactivo para el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                            //throw new Exception("Usuario esta inactivo para el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                        }

                    } else {
                        herramientas.info("Usuario no registrado en el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                        //throw new Exception("Usuario no registrado en el Sistema Gestion Documental\ncomuniquese con el administrador del sistema\n" + verifica.getRespuesta());
                    }
                } else {
                    herramientas.warn(verifica.getRespuesta() + "\ncomuniquese con el administrador del sistema");
                    //throw new Exception(verifica.getRespuesta() + "\ncomuniquese con el administrador del sistema");
                }
            } else {
                herramientas.info("Debe INGRESAR USUARIO y CONTRASEÑA");
                //throw new Exception("Debe INGRESAR USUARIO y CONTRASEÑA");
            }
        } catch (Exception ex) {
            herramientas.error(ex.getMessage());
            traza.trace("error en el inicio de sesion", Level.ERROR, ex);
        }
        return resp;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
