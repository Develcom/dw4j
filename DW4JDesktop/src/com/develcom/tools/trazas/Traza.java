/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tools.trazas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


/**
 * Permite crear y escribir
 * trazas en el log
 * @author develcom
 */
public class Traza {

    /**
     * Atributo principar
     * del log4j para
     * escribir las trazas
     * segun su nivel
     */
    private static Logger logger;
    private String nombreClase;

    /**
     * Constructor, inicializa
     * el atributo logger con
     * la clase el cual se escribiran
     * las diferentes trazas en el
     * log
     * @param clase
     * La clase al cual
     * se escribiran las
     * trazas en el log
     */
    public Traza(Class clase) {
        nombreClase=clase.getSimpleName();
        logger = Logger.getLogger(clase);
        //logger = LoggerFactory.getLogger(clase);
        
        PropertyConfigurator.configure(buscarProperties());
    }


    /**
     * Permite escribir el mensaje
     * de traza en el log segun su
     * nivel.
     * http://logging.apache.org/log4j/1.2/manual.html
     * @param mensaje
     * El mensaje que se
     * escribira
     * @param nivel
     * El nivel del log
     * @param ex
     * La excepcion que
     * se generó
     */
    public void trace(String mensaje, Level nivel, Throwable ex) {

        Logger log = Logger.getLogger(Traza.class);

        try {

            //PropertyConfigurator.configure(buscarProperties());


            if (nivel.equals(Level.INFO)) {
                logger.info(nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug(nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn(nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error(nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal(nombreClase+" - "+mensaje, ex);
            }
        } catch (Exception e) {
            log.error("Error al escribir la traza", e);
            //ex.printStackTrace();
        }
    }

    /**
     * Permite escribir el mensaje
     * de traza en el log segun su
     * nivel.
     * http://logging.apache.org/log4j/1.2/manual.html
     * @param mensaje
     * El mensaje que se
     * escribira
     * @param nivel
     * El nivel del log
     */
    public void trace(String mensaje, Level nivel) {

        Logger log = Logger.getLogger(Traza.class);

        try {

            //PropertyConfigurator.configure(buscarProperties());


            if (nivel.equals(Level.INFO)) {
                logger.info(nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug(nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn(nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error(nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal(nombreClase+" - "+mensaje);
            }
        } catch (Exception e) {
            log.error("Error al escribir la traza", e);
            //e.printStackTrace();
        }
    }

    /**
     * Busca el archivo
     * de propiedades
     * de log4j
     * @return
     * Un objecto con las
     * propiedades del
     * log4j
     */
    private Properties buscarProperties(){
        Logger log = Logger.getLogger(Traza.class);
        String appPath = System.getProperties().getProperty("user.dir");
        String properties = "/lib/log4j-config.properties";// ManejoSesion.getConfiguracion().getLogProperties();//ManejoSesion.getPropedades().getProperty("log");
        String fileProperties = appPath+properties;

        Properties prop = new Properties();
        InputStream ips;
        try {
            //log.info("buscando el archivo de propiedades");
            
            ips=new FileInputStream(fileProperties);
            prop.load(ips);
        } catch (FileNotFoundException ex) {
            log.error("archivo de propiedades no encontrado", ex);
            //ex.printStackTrace();
        } catch (IOException ex) {
            log.error("problemas al leer el archivo de propiedades", ex);
            //ex.printStackTrace();
        }
        return prop;
    }

}
