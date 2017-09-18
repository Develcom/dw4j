package ve.com.develcom.servicios;

import com.develcom.administracion.AdministracionAgregar;
import com.develcom.externo.DatosExternos;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Propiedades;
import ve.com.develcom.tools.Traza;

public class DatosExternosServicios {

    private Traza traza = new Traza(AdministracionAgregarServicio.class);
    private final Properties properties = new Propiedades().getPropiedades();
    private final String servidor = properties.getProperty("ip");
    private final String puerto = properties.getProperty("puerto");

    private String endpointURL = "http://" + servidor + ":" + puerto + "/DW4JServicios/DatosExternos?wsdl";
    private URL url;
    private QName serviceName = new QName("http://administracion.develcom.com/", "DatosExternos");
    private QName portName = new QName("http://administracion.develcom.com/", "DatosExternosPort");
    private Service service;

    public DatosExternosServicios() {
        try {
            traza.trace("servidor " + servidor, Level.INFO);
            traza.trace("puerto " + puerto, Level.INFO);
            url = new URL(endpointURL);

            service = Service.create(url, serviceName);

        } catch (MalformedURLException ex) {
            traza.trace("error en la url del WS", Level.ERROR, ex);
        }
    }

    @WebEndpoint(name = "DatosExternosPort")
    public DatosExternos getDatosExternosPort() {
        return service.getPort(portName, DatosExternos.class);
    }
}
