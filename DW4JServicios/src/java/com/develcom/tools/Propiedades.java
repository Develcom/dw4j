/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools;

import com.develcom.logs.Traza;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Propiedades {

    private static Traza traza = new Traza(Propiedades.class);
    
    
    public static Properties cargarPropiedadesWS(){
//        String appPath = System.getProperties().getProperty("user.dir");
//        String propertie ="/web/WEB-INF/DW4JServicios.properties";
//        String fileProperties = appPath+propertie;

        Properties propiedades = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream("../DW4JServicios.properties");
        
        try {
            propiedades.load(in);
            traza.trace("propiedades cargadas del servicio web", Level.INFO);
        } catch (IOException ex) {
            traza.trace("error al cargar las propiedades", Level.ERROR, ex);
        }
        return propiedades;
    }



}
