/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tool.skin;

import com.develcom.tool.Propiedades;
import com.develcom.tool.log.Traza;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@ManagedBean
//@RequestScoped
@SessionScoped
public class SkinBean {
    
    //@ManagedProperty(value="ruby")
    private String skin;
    private Properties propiedades;
    private Traza traza = new Traza(SkinBean.class);
    
    
    
    /**
     * Creates a new instance of SkinBean
     */
    public SkinBean() {
        propiedades = new Propiedades().cargarPropiedades();
        skin = propiedades.getProperty("skin");
        
    }


    public String getSkin() {
        traza.trace("obteniendo es skin "+skin, Level.INFO);
        return skin;

    }

    public void setSkin(String skin) {
        traza.trace("añadiendo el skin "+skin, Level.INFO);
        this.skin = skin;

    }
}
