/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.tool;

import org.apache.log4j.Level;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.barra.IBarraEstado;

/**
 *
 * @author develcom
 */
public class Herramientas {

    private Traza traza = new Traza(Herramientas.class);
    private Session session;

    public Session crearSesion() {

        if (session == null) {
            session = Sessions.getCurrent();
        }

        return session;
    }

    public void cerrarSesion() {
        session.invalidate();
    }

    public String getNavegador() {

        String userAgent = Executions.getCurrent().getUserAgent();
        return userAgent;
    }

    public void error(String error, Exception exception) {
        Messagebox.show(error + "\n" + exception, "Error", Messagebox.OK, Messagebox.ERROR);

    }

    public void info(String message) {
        Messagebox.show(message, "Informacion", Messagebox.OK, Messagebox.INFORMATION);
    }

    public void warn(String message) {
        Messagebox.show(message, "Informacion", Messagebox.OK, Messagebox.EXCLAMATION);
    }

    public void navegarPagina(String url) {
        Executions.sendRedirect(url);
    }
    
    public IBarraEstado getBarraEstado(){        
        IBarraEstado barraEstado = null;
        
        if(barraEstado == null){
            barraEstado = (IBarraEstado) session.getAttribute("status");
        }
        
        traza.trace("barra de estado " + barraEstado, Level.INFO);
        
        return barraEstado;
    }

    public String getIP() {
        String ip;
        ip = Executions.getCurrent().getRemoteAddr();
        return ip;
    }

    public String getHost() {
        String host;
        host = Executions.getCurrent().getRemoteHost();
        return host;
    }

    public String getUsuario() {
        String usuario;
        usuario = Executions.getCurrent().getRemoteUser();        
        return usuario;
    }

    public String getID() {
        String id;
        id = Executions.getCurrent().getDesktop().getId();        
        return id;
    }
}
