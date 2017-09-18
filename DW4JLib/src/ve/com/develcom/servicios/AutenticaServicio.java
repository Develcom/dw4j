/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.autentica.Autentica;
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
//public class AutenticaServicio extends Service{
public class AutenticaServicio {
    
    private Traza traza = new Traza(AutenticaServicio.class);
    private final  Properties properties = new Propiedades().getPropiedades();
    private final  String servidor = properties.getProperty("ip");
    private final  String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/Autentica?wsdl";
    private URL url;
    private QName serviceName = new QName("http://autentica.develcom.com/", "Autentica");
    private QName portName = new QName("http://autentica.develcom.com/", "AutenticaPort");
    private Service service;// = Service.create(serviceName);


    public AutenticaServicio(){
        try {
            traza.trace("servidor "+servidor, Level.INFO);
            traza.trace("puerto "+puerto, Level.INFO);
            
            url = new URL(endpointURL);

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            traza.trace("error en la url del WS", Level.ERROR, ex);
        }

    }

    @WebEndpoint(name = "AutenticaPort")
    public Autentica getAutenticaPort() {
        return service.getPort(portName, Autentica.class);
    }

}
