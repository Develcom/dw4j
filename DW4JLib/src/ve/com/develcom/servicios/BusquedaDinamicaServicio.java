/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.expediente.BusquedaExpediente;
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
//public class BusquedaDinamicaServicio extends Service{
public class BusquedaDinamicaServicio {


    private Traza logger = new Traza(BusquedaDinamicaServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/BusquedaExpediente?wsdl";
    private URL url;
    private QName serviceName = new QName("http://expediente.develcom.com/", "BusquedaExpediente");
    private QName portName = new QName("http://expediente.develcom.com/", "BusquedaExpedientePort");
    private Service service;// = Service.create(serviceName);

    public BusquedaDinamicaServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/BusquedaExpediente?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    
    /**
     * 
     * @return
     *     returns BusquedaExpediente
     */
    @WebEndpoint(name = "BusquedaExpedientePort")
    public BusquedaExpediente getBusquedaExpedientePort() {
        return service.getPort(portName, BusquedaExpediente.class);
    }
    
}
