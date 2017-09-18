package com.develcom.recupera.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:resources.properties")
public class Propiedades {

    private static final Logger LOG = LoggerFactory.getLogger(Propiedades.class);

    @Autowired
    private Environment env;

    public Properties configuracionBaseDatos() {

        //LOG.info("cargando configuracion de la base de datos (env) "+env);

        Properties prop = new Properties();

        if (env != null) {

            prop.put("srvbasedato", env.getProperty("srvbasedato"));
            prop.put("portbasedato", env.getProperty("portbasedato"));
            prop.put("basedato", env.getProperty("basedato"));
            prop.put("userbasedato", env.getProperty("userbasedato"));
            prop.put("passbasedato", env.getProperty("passbasedato"));
            prop.put("driverpostgres", env.getProperty("driverpostgres"));
            prop.put("urlbasedato", env.getProperty("urlbasedato"));
        }
//        else{
//            prop = buscarProperties();
//        }
        return prop;
    }

    public Properties configuracionCarpeta() {

        //LOG.info("cargando configuracion para los archivos (env) "+env);
        Properties prop = new Properties();

        prop.put("ruta", env.getProperty("ruta"));
        prop.put("rutaRaiz", env.getProperty("rutaRaiz"));
        prop.put("rutaWin", env.getProperty("rutaWin"));
        prop.put("rutaTmp", env.getProperty("rutaTmp"));
        prop.put("rutaTmpWin", env.getProperty("rutaTmpWin"));

        return prop;
    }
    
    public Properties configuracionProtocolo(){

        //LOG.info("cargando configuracion para los archivos (env) "+env);
        Properties prop = new Properties();

        prop.put("servidor", env.getProperty("servidor"));
        prop.put("puerto", env.getProperty("puerto"));
        
        return prop;
    }

    public Properties buscarProperties() {

        //File file = new File(getClass().getResource("/resources.properties").getFile());
        Properties prop = new Properties();
        InputStream ips;
        try {

            ips = Propiedades.class.getResourceAsStream("/resources.properties");//new FileInputStream(file);
            prop.load(ips);
        } catch (FileNotFoundException ex) {
            LOG.error("archivo de propiedades no encontrado", ex);
        } catch (IOException ex) {
            LOG.error("problemas al leer el archivo de propiedades", ex);
        }
        return prop;
    }
}
