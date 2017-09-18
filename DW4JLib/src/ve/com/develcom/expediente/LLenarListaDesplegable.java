/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.expediente;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class LLenarListaDesplegable {

    private static Traza traza = new Traza(LLenarListaDesplegable.class);

    /**
     * Busca la informacion
     * de la lista desplegable
     * segun el codigo recibido
     * @param codigo
     * El codigo realcionado
     * con la lista desplegables
     * @param bandera
     * Indica que datos buscar, verdadero para
     * los datos adicionales y falso para los
     * expediente
     * @return
     * Un listado con la informacion
     * de la lista desplegable
     * @throws SOAPException
     */
    public java.util.List<com.develcom.administracion.Combo> buscarData(int codigo, boolean bandera) throws SOAPException, SOAPFaultException{
        traza.trace("buscar datos de la lista desplegable segun codigo "+codigo, Level.INFO);
        return buscarDatosCombo(codigo, bandera);
    }

    private static java.util.List<com.develcom.administracion.Combo> buscarDatosCombo(int idArgumento, boolean bandera) {
        ve.com.develcom.servicios.AdministracionBusquedasServicio service = new ve.com.develcom.servicios.AdministracionBusquedasServicio();
//        com.develcom.administracion.AdministracionBusquedas_Service service = new com.develcom.administracion.AdministracionBusquedas_Service();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarDatosCombo(idArgumento, bandera);
    }
    
}
