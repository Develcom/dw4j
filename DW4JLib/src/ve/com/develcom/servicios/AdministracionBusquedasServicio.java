/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.administracion.AdministracionBusquedas;
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
//public class AdministracionBusquedasServicio extends Service{
public class AdministracionBusquedasServicio {


    private Traza logger = new Traza(AdministracionBusquedasServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionBusquedas?wsdl";
    private URL url;
    private QName serviceName = new QName("http://administracion.develcom.com/", "AdministracionBusquedas");
    private QName portName = new QName("http://administracion.develcom.com/", "AdministracionBusquedasPort");
    private Service service;// = Service.create(serviceName);

    public AdministracionBusquedasServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionBusquedas?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }





    @WebEndpoint(name = "AdministracionBusquedasPort")
    public AdministracionBusquedas getAdministracionBusquedasPort() {
        return service.getPort(portName, AdministracionBusquedas.class);
    }

//    private static Logger logger = Logger.getLogger(AdministracionBusquedasServicio.class);
//    private final static Properties properties = Propiedades.buscarProperties("lib.properties");
//    private final static String servidor = properties.getProperty("ip");
//    private final static String puerto = properties.getProperty("puerto");
//
//    private final static URL ADMINISTRACIONBUSQUEDASSERVICE_WSDL_LOCATION;
//    private final static WebServiceException ADMINISTRACIONBUSQUEDASSERVICE_EXCEPTION;
//    private final static QName ADMINISTRACIONBUSQUEDASSERVICE_QNAME = new QName("http://administracion.develcom.com/", "AdministracionBusquedasService");
//
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//        try {
//            logger.info("servidor "+servidor);
//            logger.info("puerto "+puerto);
//            url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionBusquedas?wsdl");
//        } catch (MalformedURLException ex) {
//            logger.error("error en la url del WS", ex);
//            e = new WebServiceException(ex);
//        }
//        ADMINISTRACIONBUSQUEDASSERVICE_WSDL_LOCATION = url;
//        ADMINISTRACIONBUSQUEDASSERVICE_EXCEPTION = e;
//    }
//
//    public AdministracionBusquedasServicio() {
//        super(__getWsdlLocation(), ADMINISTRACIONBUSQUEDASSERVICE_QNAME);
//    }
//
//    public AdministracionBusquedasServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public AdministracionBusquedasServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//
//    public AdministracionBusquedas getAdministracionBusquedasPort() {
//        return super.getPort(new QName("http://administracion.develcom.com/", "AdministracionBusquedasPort"), AdministracionBusquedas.class);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (ADMINISTRACIONBUSQUEDASSERVICE_EXCEPTION!= null) {
//            throw ADMINISTRACIONBUSQUEDASSERVICE_EXCEPTION;
//        }
//        return ADMINISTRACIONBUSQUEDASSERVICE_WSDL_LOCATION;
//    }

}
