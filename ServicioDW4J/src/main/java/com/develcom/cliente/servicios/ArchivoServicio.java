package com.develcom.cliente.servicios;

import com.develcom.documento.Archivo;
import com.develcom.util.Propiedades;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.soap.MTOMFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebServiceClient
public class ArchivoServicio {

//    @Autowired
//    private Propiedades propiedades;

    private Logger traza = LoggerFactory.getLogger(ArchivoServicio.class);
    private QName portName = new QName("http://documento.develcom.com/", "ArchivoPort");
    private Service service;

    public ArchivoServicio() {
        try {

            Propiedades propiedades = new Propiedades();
            String servidor;
            String puerto;
            String endpointURL;
            URL url;
            QName serviceName = new QName("http://documento.develcom.com/", "Archivo");

            Properties prop = propiedades.buscarProperties();
            servidor = prop.getProperty("servidor");
            puerto = prop.getProperty("puerto");
            endpointURL = "http://" + servidor + ":" + puerto + "/DW4JServicios/Archivo?wsdl";

            traza.info("servidor " + servidor);
            traza.info("puerto " + puerto);
            url = new URL(endpointURL);

            service = Service.create(url, serviceName);
        } catch (MalformedURLException ex) {
            traza.error("error en la url del WS", ex);
        }
//        }
    }

    @WebEndpoint(name = "ArchivoPort")
    public Archivo getArchivoPort() {
        return service.getPort(portName, Archivo.class, new MTOMFeature());

    }

}
