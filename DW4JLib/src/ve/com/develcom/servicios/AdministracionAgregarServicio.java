/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.administracion.AdministracionAgregar;
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
public class AdministracionAgregarServicio {
//public class AdministracionAgregarServicio extends Service{


    private Traza traza = new Traza(AdministracionAgregarServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionAgregar?wsdl";
    private URL url;
    private QName serviceName = new QName("http://administracion.develcom.com/", "AdministracionAgregar");
    private QName portName = new QName("http://administracion.develcom.com/", "AdministracionAgregarPort");
    private Service service;// = Service.create(serviceName);




    public AdministracionAgregarServicio() {
        try {
            traza.trace("servidor "+servidor, Level.INFO);
            traza.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionAgregar?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            traza.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "AdministracionAgregarPort")
    public AdministracionAgregar getAdministracionAgregarPort() {
        return service.getPort(portName, AdministracionAgregar.class);
    }
}
