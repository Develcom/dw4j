/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.calidad.CalidadDocumento;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Propiedades;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class CalidadDocumentoServicio {


    private Traza logger = new Traza(CalidadDocumentoServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/CalidadDocumento?wsdl";
    private URL url;
    private QName serviceName = new QName("http://calidad.develcom.com/", "CalidadDocumento");
    private QName portName = new QName("http://calidad.develcom.com/", "CalidadDocumentoPort");
    private Service service;// = Service.create(serviceName);

    public CalidadDocumentoServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/ApruebaRechazaDocumento?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }



    @WebEndpoint(name = "CalidadDocumento")
    public CalidadDocumento getApruebaRechazaDocumentoPort() {
        return service.getPort(portName, CalidadDocumento.class);
    }


}
