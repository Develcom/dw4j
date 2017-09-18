/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.servicios;

import com.develcom.documento.Archivo;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.soap.MTOMFeature;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Propiedades;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
//public class ArchivoServicio extends Service{
@WebServiceClient
public class ArchivoServicio{


    private  Traza logger = new Traza(ArchivoServicio.class);
    private final  Properties properties = new Propiedades().getPropiedades();
    private final  String servidor = properties.getProperty("ip");
    private final  String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://"+servidor+":"+puerto+"/DW4JServicios/Archivo?wsdl";
    private URL url;
    private QName serviceName = new QName("http://documento.develcom.com/", "Archivo");
    private QName portName = new QName("http://documento.develcom.com/", "ArchivoPort");
    private Service service;// = Service.create(serviceName);


    public ArchivoServicio() {
        try {
            logger.trace("servidor "+servidor, Level.INFO);
            logger.trace("puerto "+puerto, Level.INFO);
            url = new URL(endpointURL);
            //url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Archivo?wsdl");
            
            service = Service.create(url, serviceName);
            //service = Service.create(serviceName);
            
            //service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, endpointURL);
//            Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);
//            MessageFactory messagefactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);

        
//        } catch (SOAPException ex) {
//            logger.trace("error SOAP", Level.ERROR, ex);
        } catch (MalformedURLException ex) {
            logger.trace("error en la url del WS", Level.ERROR, ex);
        } 
//        catch (IOException ex) {
//            logger.trace("error I/O", Level.ERROR, ex);
//        }
    }


    @WebEndpoint(name = "ArchivoPort")
    public Archivo getArchivoPort() {
        //return service.getPort(new QName("http://documento.develcom.com/", "ArchivoPort"), Archivo.class);
        //return service.getPort(Archivo.class);
        //return service.getPort(portName, Archivo.class);
        return service.getPort(portName, Archivo.class, new MTOMFeature());
         
    }


//    private final static URL ARCHIVO_WSDL_LOCATION;
//    private final static WebServiceException ARCHIVO_EXCEPTION;
//    private final static QName ARCHIVO_QNAME = new QName("http://documento.develcom.com/", "Archivo");
//
//    static {
//        URL url = null;
//        WebServiceException e = null;
//            logger.trace("servidor "+servidor, Level.INFO);
//            logger.trace("puerto "+puerto, Level.INFO);
//        try {
//            url = new URL("http://"+servidor+":"+puerto+"/DW4JServicios/Archivo?wsdl");
//        } catch (MalformedURLException ex) {
//            e = new WebServiceException(ex);
//        }
//        ARCHIVO_WSDL_LOCATION = url;
//        ARCHIVO_EXCEPTION = e;
//    }
//
//    public ArchivoServicio() {
//        super(__getWsdlLocation(), ARCHIVO_QNAME);
//    }
//
//    public ArchivoServicio(WebServiceFeature... features) {
//        super(__getWsdlLocation(), ARCHIVO_QNAME, features);
//    }
//
//    public ArchivoServicio(URL wsdlLocation) {
//        super(wsdlLocation, ARCHIVO_QNAME);
//    }
//
//    public ArchivoServicio(URL wsdlLocation, WebServiceFeature... features) {
//        super(wsdlLocation, ARCHIVO_QNAME, features);
//    }
//
//    public ArchivoServicio(URL wsdlLocation, QName serviceName) {
//        super(wsdlLocation, serviceName);
//    }
//
//    public ArchivoServicio(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
//        super(wsdlLocation, serviceName, features);
//    }
//
//    /**
//     * 
//     * @return
//     *     returns Archivo
//     */
//    @WebEndpoint(name = "ArchivoPort")
//    public Archivo getArchivoPort() {
//        return super.getPort(new QName("http://documento.develcom.com/", "ArchivoPort"), Archivo.class);
//    }
//
//    /**
//     * 
//     * @param features
//     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
//     * @return
//     *     returns Archivo
//     */
//    @WebEndpoint(name = "ArchivoPort")
//    public Archivo getArchivoPort(WebServiceFeature... features) {
//        return super.getPort(new QName("http://documento.develcom.com/", "ArchivoPort"), Archivo.class, features);
//    }
//
//    private static URL __getWsdlLocation() {
//        if (ARCHIVO_EXCEPTION!= null) {
//            throw ARCHIVO_EXCEPTION;
//        }
//        return ARCHIVO_WSDL_LOCATION;
//    }
    
}
