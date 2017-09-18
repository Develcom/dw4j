/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tool;

import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Level;


public class Herramientas  implements Serializable{

    private Traza traza = new Traza(Herramientas.class);
    private HttpSession session;
    private HttpServletRequest request;// = (HttpServletRequest) getExternalContext().getRequest();
    //private static final long serialVersionUID = 3L;

    public void cerrarSesion() {
        getExternalContext().invalidateSession();
//        //request = (HttpServletRequest) getExternalContext().getRequest();
//        session = (HttpSession) getExternalContext().getSession(false);
//        if (session != null) {
//            //session = request.getSession(false);
//            session.invalidate();
//        }
    }

    /**
     * Crea la Sesion
     *
     * @return Objecto sesion
     */
    public HttpSession crearSesion() {
//        HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
//        request = (HttpServletRequest) getExternalContext().getRequest();
        session = (HttpSession) getExternalContext().getSession(true);
        try {
            
            
            if (session == null) {
                session = (HttpSession) getExternalContext().getSession(true);                
            }
            
//            if (session == null) {
//                traza.trace("es sesion null se crea", Level.INFO);
//                session = request.getSession(true);
//            } else if (!session.isNew()) {
//                traza.trace("sesion no es nueva se rescata o se obtiene " + session.isNew(), Level.INFO);
//                session = request.getSession();
//            } else {
//                traza.trace("sesion expirada ", Level.INFO);
//                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
//            }
        } catch (Exception e) {
            traza.trace("error session", Level.ERROR, e);
        }
        return session;
    }
    
    public String navegador(){
        
        Enumeration nav = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getHeaders("User-Agent");
        
        while(nav.hasMoreElements()){
            String obj = nav.nextElement().toString();
            traza.trace("browser "+obj, Level.INFO);
        }
        
        ExternalContext context = getFacesContext().getExternalContext();
        String userAgent = context.getRequestHeaderMap().get("User-Agent");
        return userAgent;
    }

    public FacesContext getFacesContext() {
        //FacesContext.getCurrentInstance().release();
        return FacesContext.getCurrentInstance();
    }

    public ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public Map<String, Object> getSessionScope() {
        return getExternalContext().getSessionMap();
    }

    /**
     * Busca un componente en el arbol de la vista
     *
     * @param id El id de la etiqueta
     * @return Un objeto tipo componente de la vista
     */
    public UIComponent findComponentInRoot(String id) {
        UIComponent ret = null;
        traza.trace("buscando un componente", Level.INFO);
        //FacesContext facesContext = FacesContext.getCurrentInstance();
        if (getFacesContext() != null) {
            UIComponent root = getFacesContext().getViewRoot();
            traza.trace("componente raiz "+root.getFamily(), Level.INFO);
            ret = findComponent(root, id);
            traza.trace("componente encontrado "+ret.getFamily()+ " id "+ret.getId(), Level.INFO);
        }
        return ret;
    }

    private UIComponent findComponent(UIComponent base, String id) {

        UIComponent kid;
        UIComponent result = null;
        
        if (id.equals(base.getId())) {
            return base;
        }
        
        Iterator<UIComponent> kids = base.getFacetsAndChildren();
        while (kids.hasNext() && (result == null)) {
            kid = kids.next();
            traza.trace("componente "+kid.getFamily(), Level.INFO);
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findComponent(kid, id);
            if (result != null) {
                traza.trace("componente buscado "+result.getFamily(), Level.INFO);
                break;
            }
        }
        
        return result;
    }

    /**
     * <p> Metodo que agrega un mensaje de Informacion al Faces. </p>
     *
     * @param summary
     */
    public void info(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_INFO);
    }

    /**
     * <p> Metodo que agrega un mensaje de Informacion al Faces de un
     * componente. </p>
     *
     * @param summary
     */
    public void info(UIComponent component, String summary) {
        if (component == null) {
            info(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Warning al Faces. </p>
     *
     * @param summary
     */
    public void warn(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_WARN);
    }

    /**
     * <p> Metodo que agrega un mensaje de Warning al Faces de un componente.
     * </p>
     *
     * @param summary
     */
    public void warn(UIComponent component, String summary) {

        if (component == null) {
            warn(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Error al Faces. </p>
     *
     * @param summary
     */
    public void error(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * <p> Metodo que agrega un mensaje de Error al Faces de un componente. </p>
     *
     * @param summary
     */
    public void error(UIComponent component, String summary) {

        if (component == null) {
            error(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Fatal al Faces. </p>
     *
     * @param summary
     */
    public void fatal(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_FATAL);
    }

    /**
     * <p> Metodo que agrega un mensaje de Fatal al Faces de un componente. </p>
     *
     * @param summary
     */
    public void fatal(UIComponent component, String summary) {

        if (component == null) {
            fatal(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null));
        }
    }

    /**
     * <p> Agrega un nuevo mensaje al contexto Faces solo si no hay otro ya con
     * el mismo texto (summary) y severidad (severity) que se quiere agregar.
     * </p> <p> Esto evita que se muestre m�s de una vez el mismo mensaje. </p>
     * <p> SESS - 14.01.2010 </p>
     *
     * @param summary
     * @param severity
     */
    private void addUniqueMessage(String summary, FacesMessage.Severity severity) {
        
        for (Iterator<FacesMessage> i = getFacesContext().getMessages(); i.hasNext();) {
            FacesMessage m = i.next();
            String s = m.getSummary();
            if (s != null && s.equals(summary) && m.getSeverity() == severity) {
                return;
            }
        }
        getFacesContext().addMessage(null, new FacesMessage(severity, summary, null));
    }

    /**
     * <p> Metodo que navega de pagina con el nombre del contexto de la pagina.
     * </p>
     *
     * @param nombrePagina
     */
    public void navegarPagina(String nombrePagina) {

        try {
            if (nombrePagina != null) {
//                ExternalContext contex = getFacesContext().getExternalContext();
//                String pagina = contex.getRequestContextPath() + "/"
//                        + nombrePagina + ".xhtml";
                String pagina = nombrePagina+".xhtml";
                traza.trace("redireccionando a la pagina "+pagina, Level.INFO);
                getExternalContext().redirect(pagina);

            }
        } catch (Exception e) {
            traza.trace("error en redireccionar la pagina", Level.ERROR, e);
        }

    }
    
    
//    public void crearCookie(String nombre, Object valor){
//        HttpServletResponse response;
//        Cookie cookie = new Cookie(nombre, (String) valor);
////        response = (HttpServletResponse) getExternalContext().getResponse();
////        response.addCookie(cookie);
//        crearSesion();
//        response = Sesion.getResponse();
//        response.addCookie(cookie);
//    }
//
//    public Object leerCookie(String nombre){
//        Object object = new Object();
//        HttpServletRequest request;
//        Cookie cookie[];
////        request = (HttpServletRequest) getExternalContext().getRequest();
////        cookie = request.getCookies();
//        crearSesion();
//        request = Sesion.getRequest();
//        cookie = request.getCookies();
//        if(cookie != null && cookie.length > 0){
//            for(int i = 0; i<cookie.length; i++){
//                if(cookie[i].getName().equalsIgnoreCase(nombre)){
//                    object = (Object) cookie[i].getValue();
//                }
//            }
//        }
//        return object;
//    }
}
