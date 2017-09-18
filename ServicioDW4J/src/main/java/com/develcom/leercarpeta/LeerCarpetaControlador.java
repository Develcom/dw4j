package com.develcom.leercarpeta;

import com.develcom.util.Propiedades;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableAsync
@EnableScheduling
@RestController
@RequestMapping("/leerCarpeta")
public class LeerCarpetaControlador {

    private static final Logger LOG = LoggerFactory.getLogger(LeerCarpetaControlador.class);

    @Autowired
    private Propiedades propiedades;

    @Autowired
    private GuardaDocumento gd;

//    @RequestMapping(value = "/home", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @ResponseBody
//    public Map<String, String> home() throws JSONException {
//
//        Map<String, String> json = new HashMap<>();
//        Properties prop = System.getProperties();
//
//        Enumeration<Object> keys = prop.keys();
//
//        while (keys.hasMoreElements()) {
//            String key = keys.nextElement().toString();
////            LOG.info("key: " + key + " - value: " + prop.get(key));
//            json.put(key, prop.get(key).toString());
//        }
//
//        return json;
//    }
    @RequestMapping(value = "/leerLog", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public StringBuilder leerLog() throws FileNotFoundException, IOException {

        StringBuilder cadena = new StringBuilder();
        String leer;
        File archivo = new File("./log/leerCarpeta.log");

        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);

        while ((leer = b.readLine()) != null) {
//            if (leer.contains("TRACE")) {
//                leer = leer + "\n";
//                cadena.append(leer);
//            }
            leer = leer + "\n";
            cadena.append(leer);
        }
        b.close();
        return cadena;
    }

    @Scheduled(fixedRate = 300000)
    public void cargarDocumentos() throws IOException, ServicioExcepcion {

//        GuardaDocumento gd = new GuardaDocumento();
        Properties prop = propiedades.configuracionCarpeta();
        List<Properties> lstProp = new ArrayList<>();
        Properties props;
        String so, ext = "";
        File ruta = null, archivoCOD = null;
        File[] archivos = null;
        boolean exito;
        int a;

        so = System.getProperty("os.name");
        LOG.info("sistema operativo " + so);

        if (so.contains("Windows")) {
            ruta = new File(prop.getProperty("rutaWindows"));
            archivoCOD = new File(prop.getProperty("rutaWindows") + "/codificado.cod");
        } else if (so.contains("Linux")) {
            ruta = new File(prop.getProperty("rutaLinux"));
            archivoCOD = new File(prop.getProperty("rutaLinux") + "/codificado.cod");
        }

        if (ruta != null) {
            LOG.info("buscando archivos en el directorio " + ruta);
            if ((ruta.exists()) && (ruta.isDirectory())) {
                archivos = ruta.listFiles();
                LOG.info("total de archivos buscados " + archivos.length);
            } else {
                throw new ServicioExcepcion("directorio no existe");
            }
        }

        if (archivos != null) {
            for (File archivo : archivos) {
                LOG.info("archivo " + archivo);

                a = archivo.getName().indexOf(".");

                if (a != -1) {
                    ext = archivo.getName().substring(archivo.getName().lastIndexOf('.') + 1);
                }

                if ("properties".equalsIgnoreCase(ext)) {
                    LOG.info("agregando el archivo " + archivo);

                    props = new Properties();
                    props.load(new FileInputStream(archivo));
                    lstProp.add(props);
                }
            }
            exito = gd.guardarDocumento(lstProp, ruta);

            if (exito) {
                archivos = null;
                archivos = ruta.listFiles();
                for (File archivo : archivos) {
                    archivo.delete();
                }
                LOG.info("se registraron los documentos con exito");
            }
        } else {
            LOG.info("directorio vacio");
        }
    }

    public Propiedades getPropiedades() {
        return propiedades;
    }

}
