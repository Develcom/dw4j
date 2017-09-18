/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.logs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
public class Traza {

    private static Logger logger;
    private String nombreClase;

    public Traza(Class clase) {
        this.nombreClase=clase.getSimpleName();
//        Method[] metodos =  clase.getMethods();
        //super("");
        logger = Logger.getLogger(clase);
        PropertyConfigurator.configure(cargar());
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
//            PropertyConfigurator.configure(cargar());
            //PropertyConfigurator.configure(new InitialContext().lookup("java:comp/env/log4j").toString());
            //System.out.println(new InitialContext().lookup("java:comp/env/log4j").toString());
            //InputStream ruta=getClass().getResourceAsStream(context.lookup("java:comp/env/trace").toString());
            //URL ruta  =getClass().getResource(context.lookup("java:comp/env/trace").toString());

            //PropertyConfigurator.configure(String.valueOf(ruta.getContent()));

            if (nivel.equals(Level.INFO)) {
                logger.info("(WS) - "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(WS) - "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(WS) - "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(WS) - "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(WS) - "+nombreClase+" - "+mensaje, ex);
            }
            //context.close();
        } catch (Exception e) {
            log.error(Traza.class.getSimpleName()+" Error al escribir la traza", e);
            //e.printStackTrace();
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
//            PropertyConfigurator.configure(cargar());
            //PropertyConfigurator.configure(new InitialContext().lookup("java:comp/env/log4j").toString());
            //System.out.println(new InitialContext().lookup("java:comp/env/log4j").toString());
            //InputStream ruta=getClass().getResourceAsStream(context.lookup("java:comp/env/trace").toString());
            //URL ruta  =getClass().getResource(context.lookup("java:comp/env/trace").toString());

            //PropertyConfigurator.configure(String.valueOf(ruta.getContent()));

            if (nivel.equals(Level.INFO)) {
                logger.info("(WS) - "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(WS) - "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(WS) - "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(WS) - "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(WS) - "+nombreClase+" - "+mensaje);
            }
            //context.close();
        } catch (Exception e) {
            log.error(Traza.class.getSimpleName()+ " Error al escribir la traza", e);
            //e.printStackTrace();
        }
    }

    private Properties cargar(){
        Logger log = Logger.getLogger(Traza.class);
        Properties properties = new Properties();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream("../log4j-config.properties");
        
        try {
//            InputStream in = new FileInputStream("E:\\NetBeansProjects\\Develcom-0.4\\DW4JServicios\\web\\WEB-INF\\log4j-config.properties");
            //log.info(Traza.class.getSimpleName()+" buscando el archivo de propiedades");
            properties.load(in);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Traza.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return properties;
    }

}
