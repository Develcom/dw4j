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
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;


/**
 *
 * @author develcom
 */
public class Traza {

    private Logger logger;
    private String archivo;
    private String nombreClase;
    private DailyRollingFileAppender fileAppender;
    private ConsoleAppender console;
    //private String appPath = System.getProperties().getProperty("user.dir");
    private final String RUTALOCAL = "visorDoc";
    
    public Traza(Class clase) {

        Logger log = Logger.getLogger(Traza.class);
        
        String home = System.getProperty("user.home");
        String so = System.getProperty("os.name");

        nombreClase=clase.getSimpleName();
        logger = Logger.getLogger(clase);

//        log.info("(lib) "+Traza.class.getSimpleName()+" - visor de documento jnlp "+Configuracion.isDocView());

        if(Configuracion.isDocView()){
            //log.info("(lib) "+Traza.class.getSimpleName()+" - ruta del log del jnlp "+appPath+"/logViewDoc");
            //File ruta = new File(appPath+"/logViewDoc");
            
            File ruta = null;
            if(so.equalsIgnoreCase("windows")){
                ruta = new File("c:\\"+RUTALOCAL+"\\logViewDoc");
            }else if(so.equalsIgnoreCase("linux")){
                ruta = new File(home+"/"+RUTALOCAL+"/logViewDoc");
            }
            
            
            try {
                if (!ruta.exists()){
                    if(ruta.mkdirs()){

                    }
                }

                PatternLayout patron = new PatternLayout();
                this.archivo = ruta+"/verdoc.log";
                patron.setConversionPattern("[%5p] (%d{dd/MM/yyyy HH:mm:ss,SSS} %5p) - %m%n");


                this.console = new ConsoleAppender(patron);
                this.console.activateOptions();
                this.console.setImmediateFlush(true);
                this.console.setTarget("System.out");

                this.fileAppender = new DailyRollingFileAppender(patron, this.archivo, "'.'yyyyMMdd");

                logger.removeAllAppenders();
                logger.addAppender(this.fileAppender);
                logger.addAppender(this.console);
            } catch (IOException ex)  {
                log.error("error en el flujo del archivo verdoc.log", ex);
                //ex.printStackTrace();
            } catch(Exception e){
                log.error("error general en ver documendo desde la web (jnlp)", e);
                //e.printStackTrace();
            }
        }else{
            PropertyConfigurator.configure(buscarProperties());
//            log.info("(lib) "+Traza.class.getSimpleName()+" - viene de la web? "+Configuracion.isWeb());
        }
    }



    public void trace(String mensaje, Level nivel, Throwable ex) {

        Logger log = Logger.getLogger(Traza.class);

        try {

            //PropertyConfigurator.configure(buscarProperties());


            if (nivel.equals(Level.INFO)) {
                logger.info("(lib) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(lib) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(lib) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(lib) "+nombreClase+" - "+mensaje, ex);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(lib) "+nombreClase+" - "+mensaje, ex);
            }
        } catch (Exception e) {
            log.error("Error al escribir la traza", ex);
            //ex.printStackTrace();
        }
    }

    public void trace(String mensaje, Level nivel) {

        Logger log = Logger.getLogger(Traza.class);

        try {

            //PropertyConfigurator.configure(buscarProperties());


            if (nivel.equals(Level.INFO)) {
                logger.info("(lib) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.DEBUG)) {
                logger.debug("(lib) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.WARN)) {
                logger.warn("(lib) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.ERROR)) {
                logger.error("(lib) "+nombreClase+" - "+mensaje);
            }
            if (nivel.equals(Level.FATAL)) {
                logger.fatal("(lib) "+nombreClase+" - "+mensaje);
            }
        } catch (Exception e) {
            log.error("Error al escribir la traza", e);
            //e.printStackTrace();
        }
    }
    
//    private Properties buscarProperties(){
//        Logger log = Logger.getLogger(Traza.class);
//        Properties properties = new Properties();
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//        InputStream in = cl.getResourceAsStream("../log4j-config.properties");
//        try {
//            //log.info("buscando el archivo de propiedades");
//            properties.load(in);
//        } catch (IOException ex) {
//            log.error("error buscando el archivo de propiedades", ex);
//        }
//        return properties;
//    }

    private Properties buscarProperties(){
        
        Logger log = Logger.getLogger(Traza.class);
        
        Properties prop = new Properties();
        InputStream ips;
        File fileProperties;
        
        String appPath = System.getProperty("user.dir");

        //log.info("(lib) "+Traza.class.getSimpleName()+" - viene de la web? "+Configuracion.isWeb());

        try {

            if(Configuracion.isWeb()){

                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                ips = cl.getResourceAsStream("../log4j-config.properties");

            }else{

                //log.info("(lib) "+Traza.class.getSimpleName()+" - ruta archivo log "+appPath+"/lib/log4j-config.properties");
                fileProperties = new File(appPath+"/lib/log4j-config.properties");
                ips=new FileInputStream(fileProperties);

            }
            
            prop.load(ips);
        } catch (FileNotFoundException ex) {
            log.error("(lib) "+Traza.class.getSimpleName()+" - Error archivo de propiedades no encontrado ",ex);
        } catch (IOException ex) {
            log.error("(lib) "+Traza.class.getSimpleName()+" - Error al leer el archivo de propiedades ",ex);
        }
        return prop;
    }

}
