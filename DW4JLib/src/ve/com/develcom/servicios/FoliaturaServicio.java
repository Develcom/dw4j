/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.foliatura.Foliatura;
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
//public class FoliaturaServicio extends Service{
public class FoliaturaServicio {


    private Traza logger = new Traza(FoliaturaServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/Foliatura?wsdl";
    private URL url;
    private QName serviceName = new QName("http://foliatura.develcom.com/", "Foliatura");
    private QName portName = new QName("http://foliatura.develcom.com/", "FoliaturaPort");
    private Service service;// = Service.create(serviceName);

    public FoliaturaServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Foliatura?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "FoliaturaPort")
    public Foliatura getFoliaturaPort() {
        return service.getPort(portName, Foliatura.class);
    }


//    private static Logger logger = Logger.getLogger(FoliaturaServicio.class);
//    private final static Properties properties = Propiedades.buscarProperties("lib.properties");
//    private final static String servidor = properties.getProperty("ip");
//    private final static String puerto = properties.getProperty("puerto");
//
//    private final static URL FOLIATURASERVICE_WSDL_LOCATION;
//    private final static WebServiceException FOLIATURASERVICE_EXCEPTION;
//    private final static QName FOLIATURASERVICE_QNAME = new QName("http://foliatura.develcom.com/", "FoliaturaService");
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//        try {
//            logger.info("servidor "+servidor);
//            logger.info("puerto "+puerto);
//            url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Foliatura?wsdl");
//        } catch (MalformedURLException ex) {
//            logger.error("error en la url del WS", ex);
//            e = new WebServiceException(ex);
//        }
//        FOLIATURASERVICE_WSDL_LOCATION = url;
//        FOLIATURASERVICE_EXCEPTION = e;
//    }
//
//    public FoliaturaServicio() {
//        super(__getWsdlLocation(), FOLIATURASERVICE_QNAME);
//    }
//
//    public FoliaturaServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public FoliaturaServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//    public Foliatura getFoliaturaPort() {
//        return super.getPort(new QName("http://foliatura.develcom.com/", "FoliaturaPort"), Foliatura.class);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (FOLIATURASERVICE_EXCEPTION!= null) {
//            throw FOLIATURASERVICE_EXCEPTION;
//        }
//        return FOLIATURASERVICE_WSDL_LOCATION;
//    }

}
