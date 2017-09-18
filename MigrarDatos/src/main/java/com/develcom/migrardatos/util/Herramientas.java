/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.util;

import com.develcom.migrardatos.bean.BusquedaBean;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class Herramientas  implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(BusquedaBean.class);
    private static final long serialVersionUID = 4627885783168127409L;
    private HttpSession session;
    private HttpServletRequest request;// = (HttpServletRequest) getExternalContext().getRequest();
    //private static final long serialVersionUID = 3L;

    public void cerrarSesion() {
        getExternalContext().invalidateSession();
    }

    /**
     * Crea la Sesion
     *
     * @return Objecto sesion
     */
    public HttpSession crearSesion() {
        session = (HttpSession) getExternalContext().getSession(true);
        try {
            
            
            if (session == null) {
                session = (HttpSession) getExternalContext().getSession(true);                
            }
        } catch (Exception e) {
            LOG.error("error session", e);
        }
        return session;
    }
    
    public String navegador(){
        
        Enumeration nav = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getHeaders("User-Agent");
        
        while(nav.hasMoreElements()){
            String obj = nav.nextElement().toString();
            LOG.info("browser "+obj);
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
        LOG.info("buscando un componente");
        
        if (getFacesContext() != null) {
            /*UIViewRoot viewRoot = getFacesContext().getViewRoot();
            ret = viewRoot.findComponent(id);*/
            UIComponent root = getFacesContext().getViewRoot();
            LOG.info("componente raiz "+root.getFamily());
            ret = findComponent(root, id);
            LOG.info("componente encontrado "+ret.getFamily()+ " id "+ret.getId());
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
            LOG.info("componente "+kid.getFamily());
            if (id.equals(kid.getId())) {
                result = kid;
                break;
            }
            result = findComponent(kid, id);
            if (result != null) {
                LOG.info("componente buscado "+result.getFamily());
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
        //getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, ""));
    }

    /**
     * <p> Metodo que agrega un mensaje de Informacion al Faces de un
     * componente. </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void info(UIComponent component, String summary, String detail) {
        if (component == null) {
            info(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
        }
    }
    
    /**
     * <p> Metodo que agrega un mensaje de Informacion al Faces de un
     * componente. </p>
     * 
     * @param component
     * @param summary
     * @param detail 
     */
    public void info(String component, String summary, String detail) {
        if (component == null) {
            info(summary);
        } else {
            getFacesContext().addMessage(component, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Warning al Faces. </p>
     *
     * @param summary
     */
    public void warn(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_WARN);
        //getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, ""));
    }

    /**
     * <p> Metodo que agrega un mensaje de Warning al Faces de un componente.
     * </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void warn(UIComponent component, String summary, String detail) {

        if (component == null) {
            warn(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Warning al Faces de un componente.
     * </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void warn(String component, String summary, String detail) {

        if (component == null) {
            warn(summary);
        } else {
            getFacesContext().addMessage(component, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Error al Faces. </p>
     *
     * @param summary
     */
    public void error(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_ERROR);
        //getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, ""));
    }

    /**
     * <p> Metodo que agrega un mensaje de Error al Faces de un componente. </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void error(UIComponent component, String summary, String detail) {

        if (component == null) {
            error(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Error al Faces de un componente. </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void error(String component, String summary, String detail) {

        if (component == null) {
            error(summary);
        } else {
            getFacesContext().addMessage(component, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Fatal al Faces. </p>
     *
     * @param summary
     */
    public void fatal(String summary) {
        addUniqueMessage(summary, FacesMessage.SEVERITY_FATAL);
        //getFacesContext().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, ""));
    }

    /**
     * <p> Metodo que agrega un mensaje de Fatal al Faces de un componente. </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void fatal(UIComponent component, String summary, String detail) {

        if (component == null) {
            fatal(summary);
        } else {
            getFacesContext().addMessage(component.getClientId(getFacesContext()), new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
        }
    }

    /**
     * <p> Metodo que agrega un mensaje de Fatal al Faces de un componente. </p>
     *
     * @param component
     * @param summary
     * @param detail
     */
    public void fatal(String component, String summary, String detail) {

        if (component == null) {
            fatal(summary);
        } else {
            getFacesContext().addMessage(component, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail));
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
                LOG.info("redireccionando a la pagina "+pagina);
                getExternalContext().redirect(pagina);

            }
        } catch (IOException e) {
            LOG.error("error en redireccionar la pagina", e);
        }

    }

    public void crearRutas(String[] rutas, String rutaRaiz) {
        File path;
        
        path = new File(rutaRaiz + rutas[0]);
        if (!path.exists()) {
            path.mkdirs();
        }
        
        path = new File(rutaRaiz + rutas[0] + "/" + rutas[1]);
        if (!path.exists()) {
            path.mkdirs();
        }
        
        path = new File(rutaRaiz + rutas[0] + "/" + rutas[1] + "/" + rutas[2]);
        if (!path.exists()) {
            path.mkdirs();
        }
    }
    
    public void copiarArchivos(File origen, File destino) throws IOException{
        Files.copy(origen.toPath(), destino.toPath());
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
