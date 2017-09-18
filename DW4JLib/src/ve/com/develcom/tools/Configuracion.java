/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Configuracion {

    private static boolean web = false;
    private static boolean docView = false;
    private Properties propiedades = new Properties();
    private Traza traza = new Traza(Configuracion.class);

    private static String srvWS = null;
    private static String puertoWS = null;

    public static boolean isWeb() {
        return web;
    }

    public static void setWeb(boolean web) {
        Configuracion.web = web;
    }

    public static void setDocView(boolean docView) {
        Configuracion.docView = docView;
    }

    public static boolean isDocView() {
        return docView;
    }

    public static void setSrvWS(String srvWS) {
        Configuracion.srvWS = srvWS;
    }

    public static void setPuertoWS(String puertoWS) {
        Configuracion.puertoWS = puertoWS;
    }

    
    

    private void buscarProperties(){
        traza.trace("cargando el archivo de propiedades", Level.INFO);
        
        InputStream ips;
        File fileProperties;
        String appPath = System.getProperties().getProperty("user.dir");
//        String appPath1 = System.getProperties().getProperty("line.separator");
//        String appPath2 = System.getProperties().getProperty("path.separator");
//        String appPath3 = System.getProperties().getProperty("file.separator");
//        String appPath4 = System.getProperties().getProperty("os.name");
//        String appPath5 = System.getProperties().getProperty("os.arch");
//        String appPath6 = System.getProperties().getProperty("os.version");
        try {
            if(Configuracion.isWeb()){

                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                ips = cl.getResourceAsStream("../lib.properties");
                propiedades.load(ips);

            }else if(Configuracion.isDocView()){

                traza.trace("servidor WS "+srvWS, Level.INFO);
                traza.trace("puerto WS "+puertoWS, Level.INFO);

                System.setProperty("ip", srvWS);
                System.setProperty("puerto", puertoWS);
                propiedades = System.getProperties();
                
            }else{

                //traza.trace("ruta "+appPath, Level.INFO);
                traza.trace("ruta con archivo "+appPath+"/lib/lib.properties", Level.INFO);
                fileProperties = new File(appPath+"/lib/lib.properties");

                ips=new FileInputStream(fileProperties);
                propiedades.load(ips);
            }


        } catch (FileNotFoundException ex) {
            traza.trace("Error archivo de propiedades no encontrado lib.properties", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("Error al leer el archivo de propiedades lib.properties", Level.ERROR, ex);
        }
        
    }

    public Properties getPropiedades() {
        buscarProperties();
        return propiedades;
    }

    

}
