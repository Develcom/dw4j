/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.sesion;

import com.develcom.autentica.Sesion;
import java.net.ConnectException;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class IniciaSesion {

    private Traza traza = new Traza(IniciaSesion.class);

    /**
     * Inicia sesion del
     * usuario valido
     * @param login
     * @param password
     * @return
     * Un objeto con la 
     * informacion de sesion
     * del usuario
     * @throws SOAPException
     */
    public Sesion iniciarSesion(String login, String password) throws SOAPException, SOAPFaultException{
        traza.trace("Comprobando el usuario "+login, Level.INFO);
        return verificarUsuario(login, password);
    }

    /**
     * Construye la sesion
     * del usuario validado
     * @param user
     * @return
     * Una lista con toda
     * la informacion del
     * usuario
     * @throws SOAPException
     */
    public List<Sesion> armarSesion(String user) throws SOAPException, SOAPFaultException{
        traza.trace("Creando sesion del usuario "+user, Level.INFO);
        return crearSesion(user);
    }

    /**
     * Busca las libreria y
     * Categorias segun el
     * perfil del usuario
     * @param usuario
     * @param perfil
     * @return
     * Listado de libreria y
     * Categorias
     * @throws SOAPException
     */
    public java.util.List<com.develcom.autentica.Perfil> buscarLibCatPerfil(java.lang.String usuario, java.lang.String perfil) throws SOAPException, SOAPFaultException {
        return buscarLibreriaCategoriasPerfil(usuario, perfil);
    }

    /**
     * Busca todos los usuarios
     * para la autocompletación
     * en el login
     * @return
     * @throws SOAPException
     * @throws SOAPFaultException 
     * @throws java.net.ConnectException 
     */
    public java.util.List<com.develcom.autentica.Usuario>  autocomplete() throws SOAPException, SOAPFaultException, ConnectException {
        return autocompletar();
    }
    
    /**
     * Busca las libreria,
     * Categorias e indices 
     * segun el perfil 
     * del usuario
     * 
     * @param usuario
     * @param perfil
     * @return 
     * @throws javax.xml.soap.SOAPException 
     * @throws java.net.ConnectException 
     */
    public java.util.List<com.develcom.autentica.Perfil> buscarLibCatIndicePerfil(String usuario, String perfil) throws SOAPException, SOAPFaultException, ConnectException {
        return buscarLibreriaCategoriasIndicePerfil(usuario, perfil);
    }

    private static Sesion verificarUsuario(java.lang.String login, java.lang.String password) {
        //ServicioAutentica service = new ServicioAutentica();
        ve.com.develcom.servicios.AutenticaServicio service = new ve.com.develcom.servicios.AutenticaServicio();
        //com.develcom.autentica.AutenticaService service = new com.develcom.autentica.AutenticaService();
        com.develcom.autentica.Autentica port = service.getAutenticaPort();
        return port.verificarUsuario(login, password);
    }

    private static java.util.List<com.develcom.autentica.Sesion> crearSesion(java.lang.String user) {
        //ServicioAutentica service = new ServicioAutentica();
        ve.com.develcom.servicios.AutenticaServicio service = new ve.com.develcom.servicios.AutenticaServicio();
        //com.develcom.autentica.AutenticaService service = new com.develcom.autentica.AutenticaService();
        com.develcom.autentica.Autentica port = service.getAutenticaPort();
        return port.crearSesion(user);
    }

    private static java.util.List<com.develcom.autentica.Perfil> buscarLibreriaCategoriasPerfil(java.lang.String usuario, java.lang.String perfil) {
        //ServicioAutentica service = new ServicioAutentica();
        ve.com.develcom.servicios.AutenticaServicio service = new ve.com.develcom.servicios.AutenticaServicio();
        //com.develcom.autentica.AutenticaService service = new com.develcom.autentica.AutenticaService();
        com.develcom.autentica.Autentica port = service.getAutenticaPort();
        return port.buscarLibreriaCategoriasPerfil(usuario, perfil);
    }

    private static java.util.List<com.develcom.autentica.Usuario> autocompletar() {
        //ServicioAutentica service = new ServicioAutentica();
        ve.com.develcom.servicios.AutenticaServicio service = new ve.com.develcom.servicios.AutenticaServicio();
        //com.develcom.autentica.AutenticaService service = new com.develcom.autentica.AutenticaService();
        com.develcom.autentica.Autentica port = service.getAutenticaPort();
        return port.autocompletar();
    }

    private static java.util.List<com.develcom.autentica.Perfil> buscarLibreriaCategoriasIndicePerfil(java.lang.String usuario, java.lang.String perfil) {
        ve.com.develcom.servicios.AutenticaServicio service = new ve.com.develcom.servicios.AutenticaServicio();
//        com.develcom.autentica.Autentica_Service service = new com.develcom.autentica.Autentica_Service();
        com.develcom.autentica.Autentica port = service.getAutenticaPort();
        return port.buscarLibreriaCategoriasIndicePerfil(usuario, perfil);
    }
    
}
