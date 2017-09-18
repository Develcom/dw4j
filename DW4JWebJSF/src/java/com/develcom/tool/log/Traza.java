/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tool.log;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author develcom
 */
//public class Traza extends Logger{
public class Traza implements Serializable {

    private static Logger logger;
    private String nombreClase;
    private static final long serialVersionUID = 221L;

    public Traza(Class clase) {
        PropertyConfigurator.configure(cargar());
        nombreClase=clase.getSimpleName();
        logger = Logger.getLogger(clase);
    }



    public void trace(String mensaje, Level nivel, Throwable ex) {
//        Context context;
//        String prop;
        Logger log = Logger.getLogger(Traza.class);
        try {
            
            //InitialContext context = new InitialContext();
            //context  = new InitialContext();
            //prop=context.lookup("java:comp/env/log4j").toString();
            //PropertyConfigurator.configure(prop);
            //PropertyConfigurator.configure(cargar());
            //PropertyConfigurator.configure(new InitialContext().lookup("java:comp/env/log4j").toString());
            //System.out.println(new InitialContext().lookup("java:comp/env/log4j").toString());
            //InputStream ruta=getClass().getResourceAsStream(context.lookup("java:comp/env/trace").toString());
            //URL ruta  =getClass().getResource(context.lookup("java:comp/env/trace").toString());

            //PropertyConfigurator.configure(String.valueOf(ruta.getContent()));

            if (nivel.equals(Level.INFO)) {
                logger.info("(ConsWeb) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(ConsWeb) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(ConsWeb) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(ConsWeb) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(ConsWeb) "+nombreClase+" - "+mensaje, ex);
            }
            //context.close();
        } catch (Exception e) {
            log.error("Error al escribir la traza", e);
            //ex.printStackTrace();
        }
    }

    public void trace(String mensaje, Level nivel) {
        //Properties prop = new Properties();
        //InputStream ips;
//        Context context;
//        String prop;
        Logger log = Logger.getLogger(Traza.class);
        try {
            //ips=new FileInputStream("./WEB-INF/log4j-config.properties");
            //ips=new FileInputStream(new InitialContext().lookup("java:comp/env/log4j").toString());
            //prop.load(ips);
            //PropertyConfigurator.configure(prop);
            //PropertyConfigurator.configure(new InitialContext().lookup("java:comp/env/log4j").toString());

            //InitialContext context = new InitialContext();
            //context  = new InitialContext();
            //prop=context.lookup("java:comp/env/log4j").toString();
            //PropertyConfigurator.configure(prop);
            //PropertyConfigurator.configure(cargar());
            //PropertyConfigurator.configure(new InitialContext().lookup("java:comp/env/log4j").toString());
            //System.out.println(new InitialContext().lookup("java:comp/env/log4j").toString());
            //InputStream ruta=getClass().getResourceAsStream(context.lookup("java:comp/env/trace").toString());
            //URL ruta  =getClass().getResource(context.lookup("java:comp/env/trace").toString());

            //PropertyConfigurator.configure(String.valueOf(ruta.getContent()));

            if (nivel.equals(Level.INFO)) {
                logger.info("(ConsWeb) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(ConsWeb) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(ConsWeb) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(ConsWeb) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(ConsWeb) "+nombreClase+" - "+mensaje);
            }
            //context.close();
        } catch (Exception e) {
            log.error("Error al escribir la traza", e);
            //e.printStackTrace();
        }
    }

    private Properties cargar(){
        Logger log = Logger.getLogger(Traza.class);
        Properties properties = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream("../log4j-config.properties");
        try {
            //log.info("buscando el archivo de propiedades");
            properties.load(in);
        } catch (IOException ex) {
            log.error("error buscando el archivo de propiedades", ex);
        }
        return properties;
    }

}
