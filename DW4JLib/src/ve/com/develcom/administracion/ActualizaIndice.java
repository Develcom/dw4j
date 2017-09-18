/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.administracion;

import com.develcom.expediente.Expedientes;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class ActualizaIndice {

    private Traza traza = new Traza(ActualizaIndice.class);

    /**
     * Actualiza los indices
     * @param expediente
     * Listado con la informacion
     * nueva de los indices
     * @return
     * Verdadero si la actualizacion
     * fue exitosa, falso en caso
     * contrario
     * @throws SOAPException
     * Excepcion enviada por el servicio
     * http://docs.oracle.com/javaee/1.4/api/javax/xml/soap/SOAPException.html
     */
    public boolean updateIndices(Expedientes expediente) throws SOAPException, SOAPFaultException{
        traza.trace("expediente "+expediente, Level.INFO);
        return actualizarIndices(expediente);
    }

    private static boolean actualizarIndices(com.develcom.expediente.Expedientes expediente) {
        ve.com.develcom.servicios.IndicesExpedienteServicio service = new ve.com.develcom.servicios.IndicesExpedienteServicio();
//        com.develcom.expediente.IndicesExpediente_Service service = new com.develcom.expediente.IndicesExpediente_Service();
        com.develcom.expediente.IndicesExpediente port = service.getIndicesExpedientePort();
        return port.actualizarIndices(expediente);
    }

    
}
