/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.foliatura;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class Foliatura {


    Traza traza = new Traza(Foliatura.class);

    /**
     * Crear el informe de la foliatura
     * del expediente
     * @param idExpediente
     * @param idLibreria
     * @param idCategoria
     * @return
     * Verdadero si tuvo exito,
     * falso en caso contrario
     * @throws SOAPException
     */
    public boolean armarFoliatura(java.lang.String idExpediente, int idLibreria, int idCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("armar la foliatura", Level.INFO);
        traza.trace("idExpediente "+idExpediente, Level.INFO);
        traza.trace("idLibreria "+idLibreria, Level.INFO);
        traza.trace("idCategoria "+idCategoria, Level.INFO);
        return crearFoliatura(idExpediente, idLibreria, idCategoria);
    }
    
    public boolean generarReporteDinamico(int idCategoria, java.lang.String consulta) throws SOAPException, SOAPFaultException {
        return crearReporte(idCategoria, consulta);
    }

    private static boolean crearFoliatura(java.lang.String idExpediente, int idLibreria, int idCategoria) {
        ve.com.develcom.servicios.FoliaturaServicio service = new ve.com.develcom.servicios.FoliaturaServicio();
        //com.develcom.foliatura.FoliaturaService service = new com.develcom.foliatura.FoliaturaService();
        com.develcom.foliatura.Foliatura port = service.getFoliaturaPort();
        return port.crearFoliatura(idExpediente, idLibreria, idCategoria);
    }

    private static boolean crearReporte(int idCategoria, java.lang.String consulta) {
        ve.com.develcom.servicios.FoliaturaServicio service = new ve.com.develcom.servicios.FoliaturaServicio();
//        com.develcom.foliatura.Foliatura_Service service = new com.develcom.foliatura.Foliatura_Service();
        com.develcom.foliatura.Foliatura port = service.getFoliaturaPort();
        return port.crearReporte(idCategoria, consulta);
    }
    
}
