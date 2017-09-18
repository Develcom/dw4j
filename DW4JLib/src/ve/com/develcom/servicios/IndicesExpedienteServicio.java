/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.expediente.IndicesExpediente;
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
//public class IndicesExpedienteServicio extends Service{
public class IndicesExpedienteServicio {


    private Traza logger = new Traza(IndicesExpedienteServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/IndicesExpediente?wsdl";
    private URL url;
    private QName serviceName = new QName("http://expediente.develcom.com/", "IndicesExpediente");
    private QName portName = new QName("http://expediente.develcom.com/", "IndicesExpedientePort");
    private Service service;// = Service.create(serviceName);

    public IndicesExpedienteServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Argumento?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "IndicesExpedientePort")
    public IndicesExpediente getIndicesExpedientePort() {
        return service.getPort(portName, IndicesExpediente.class);
    }

}
