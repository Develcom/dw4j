/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.administracion.AdministracionModifica;
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
//public class AdministracionModificaServicio extends Service{
public class AdministracionModificaServicio {


    private Traza logger = new Traza(AdministracionModificaServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionModifica?wsdl";
    private URL url;
    private QName serviceName = new QName("http://administracion.develcom.com/", "AdministracionModifica");
    private QName portName = new QName("http://administracion.develcom.com/", "AdministracionModificaPort");
    private Service service;// = Service.create(serviceName);


    public AdministracionModificaServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionModifica?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "AdministracionModificaPort")
    public AdministracionModifica getAdministracionModificaPort() {
        return service.getPort(portName, AdministracionModifica.class);
    }

//    private static Logger logger = Logger.getLogger(AdministracionModificaServicio.class);
//    private final static Properties properties = Propiedades.buscarProperties("lib.properties");
//    private final static String servidor = properties.getProperty("ip");
//    private final static String puerto = properties.getProperty("puerto");
//
//    private final static URL ADMINISTRACIONMODIFICASERVICE_WSDL_LOCATION;
//    private final static WebServiceException ADMINISTRACIONMODIFICASERVICE_EXCEPTION;
//    private final static QName ADMINISTRACIONMODIFICASERVICE_QNAME = new QName("http://administracion.develcom.com/", "AdministracionModificaService");
//
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//        try {
//            logger.info("servidor "+servidor);
//            logger.info("puerto "+puerto);
//            url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/AdministracionModifica?wsdl");
//        } catch (MalformedURLException ex) {
//            logger.error("error en la url del WS", ex);
//            e = new WebServiceException(ex);
//        }
//        ADMINISTRACIONMODIFICASERVICE_WSDL_LOCATION = url;
//        ADMINISTRACIONMODIFICASERVICE_EXCEPTION = e;
//    }
//
//    public AdministracionModificaServicio() {
//        super(__getWsdlLocation(), ADMINISTRACIONMODIFICASERVICE_QNAME);
//    }
//
//    public AdministracionModificaServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public AdministracionModificaServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//    public AdministracionModifica getAdministracionModificaPort() {
//        return super.getPort(new QName("http://administracion.develcom.com/", "AdministracionModificaPort"), AdministracionModifica.class);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (ADMINISTRACIONMODIFICASERVICE_EXCEPTION!= null) {
//            throw ADMINISTRACIONMODIFICASERVICE_EXCEPTION;
//        }
//        return ADMINISTRACIONMODIFICASERVICE_WSDL_LOCATION;
//    }

}
