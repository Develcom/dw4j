/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tool;

import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Propiedades {
    
    
    private Traza traza = new Traza(Propiedades.class);
    private Properties propiedades = new Properties();
    
    
    public Properties cargarPropiedades(){
        //Properties propiedades = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        InputStream in;

        try {
            in = cl.getResourceAsStream("../dw4jweb.properties");
            propiedades.load(in);
        } catch (IOException ex) {
            traza.trace("error al cargar las propiedades", Level.ERROR, ex);
        }
        return propiedades;
    }
    
    
    
}
