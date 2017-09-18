/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.documento.Documento;
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
//public class DocumentoServicio extends Service{
public class DocumentoServicio {


    private Traza logger = new Traza(DocumentoServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/Documento?wsdl";
    private URL url;
    private QName serviceName = new QName("http://documento.develcom.com/", "Documento");
    private QName portName = new QName("http://documento.develcom.com/", "DocumentoPort");
    private Service service;// = Service.create(serviceName);

    public DocumentoServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Documento?wsdl");

            service = Service.create(url, serviceName);

            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "DocumentoPort")
    public Documento getDocumentoPort() {
        return service.getPort(portName, Documento.class);
    }

//    private static Logger logger = Logger.getLogger(DocumentoServicio.class);
//    private final static Properties properties = Propiedades.buscarProperties("lib.properties");
//    private final static String servidor = properties.getProperty("ip");
//    private final static String puerto = properties.getProperty("puerto");
//
//    private final static URL DOCUMENTOSERVICE_WSDL_LOCATION;
//    private final static WebServiceException DOCUMENTOSERVICE_EXCEPTION;
//    private final static QName DOCUMENTOSERVICE_QNAME = new QName("http://documento.develcom.com/", "DocumentoService");
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//        try {
//            logger.info("servidor "+servidor);
//            logger.info("puerto "+puerto);
//            url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Documento?wsdl");
//        } catch (MalformedURLException ex) {
//            logger.error("error en la url del WS", ex);
//            e = new WebServiceException(ex);
//        }
//        DOCUMENTOSERVICE_WSDL_LOCATION = url;
//        DOCUMENTOSERVICE_EXCEPTION = e;
//    }
//
//    public DocumentoServicio() {
//        super(__getWsdlLocation(), DOCUMENTOSERVICE_QNAME);
//    }
//
//    public DocumentoServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public DocumentoServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//    public Documento getDocumentoPort() {
//        return super.getPort(new QName("http://documento.develcom.com/", "DocumentoPort"), Documento.class);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (DOCUMENTOSERVICE_EXCEPTION!= null) {
//            throw DOCUMENTOSERVICE_EXCEPTION;
//        }
//        return DOCUMENTOSERVICE_WSDL_LOCATION;
//    }
}
