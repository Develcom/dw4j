/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.mantenimiento.Mantenimientos;
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
//public class MantenimientosServicio extends Service{
public class MantenimientosServicio {


    private Traza logger = new Traza(MantenimientosServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/Mantenimientos?wsdl";
    private URL url;
    private QName serviceName = new QName("http://mantenimiento.develcom.com/", "Mantenimientos");
    private QName portName = new QName("http://mantenimiento.develcom.com/", "MantenimientosPort");
    private Service service;// = Service.create(serviceName);

    public MantenimientosServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Mantenimientos?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "MantenimientosPort")
    public Mantenimientos getMantenimientosPort() {
        return service.getPort(portName, Mantenimientos.class);
    }

//    private static Logger logger = Logger.getLogger(AutenticaServicio.class);
//    private final static Properties properties = Propiedades.buscarProperties("lib.properties");
//    private final static String servidor = properties.getProperty("ip");
//    private final static String puerto = properties.getProperty("puerto");
//
//    private final static URL MANTENIMIENTOSSERVICE_WSDL_LOCATION;
//    private final static WebServiceException MANTENIMIENTOSSERVICE_EXCEPTION;
//    private final static QName MANTENIMIENTOSSERVICE_QNAME = new QName("http://mantenimiento.develcom.com/", "MantenimientosService");
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//        try {
//            url = new URL("http://localhost:8080/DW4JServicios/Mantenimientos?wsdl");
//        } catch (MalformedURLException ex) {
//            e = new WebServiceException(ex);
//        }
//        MANTENIMIENTOSSERVICE_WSDL_LOCATION = url;
//        MANTENIMIENTOSSERVICE_EXCEPTION = e;
//    }
//
//    public MantenimientosServicio() {
//        super(__getWsdlLocation(), MANTENIMIENTOSSERVICE_QNAME);
//    }
//
//    public MantenimientosServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public MantenimientosServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//    public Mantenimientos getMantenimientosPort() {
//        return super.getPort(new QName("http://mantenimiento.develcom.com/", "MantenimientosPort"), Mantenimientos.class);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (MANTENIMIENTOSSERVICE_EXCEPTION!= null) {
//            throw MANTENIMIENTOSSERVICE_EXCEPTION;
//        }
//        return MANTENIMIENTOSSERVICE_WSDL_LOCATION;
//    }

}
