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
public class BuscaIndice {

    private Traza traza = new Traza(BuscaIndice.class);

   /**
    * Busca todos los argumentos
    * para armar los indices
    * dinamicos
    * @param idCategoria
    * El id de la categoria
    * @return
    * Listado con todos
    * los argumentos
     * @throws javax.xml.soap.SOAPException
    */
    public java.util.List<com.develcom.administracion.Indice> buscarIndice(int idCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar los argumento "+idCategoria, Level.INFO);
        return buscarIndices(idCategoria);
    }

    /**
     * Busca los datos de
     * los indices dinamicos
     * @param idCategoria
     * El id de la categoria
     * @param idExpediente
     * El idExpediente
     * @param idLibreria
     * El id de la Libreria
     * @return
     * Lista con los indices y
     * sus datos
     * @throws javax.xml.soap.SOAPException
     */
    public java.util.List<com.develcom.expediente.Indice> buscaDatosIndice(String idExpediente, int idLibreria , int idCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar datos indices ", Level.INFO);
        traza.trace("idCategoria "+idCategoria, Level.INFO);
        traza.trace("idExpediente "+idExpediente, Level.INFO);
        traza.trace("idLibreria "+idLibreria, Level.INFO);
        return buscarDatosIndicesExpediente(idExpediente, idLibreria, idCategoria);
    }

    private static java.util.List<com.develcom.administracion.Indice> buscarIndices(int idCategoria) {
        ve.com.develcom.servicios.AdministracionBusquedasServicio service = new ve.com.develcom.servicios.AdministracionBusquedasServicio();
//        ve.com.develcom.servicios.AdministracionBusquedasServicio service = new ve.com.develcom.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarIndices(idCategoria);
    }

    private static java.util.List<com.develcom.expediente.Indice> buscarDatosIndicesExpediente(java.lang.String idExpediente, int idLibreria, int idCategoria) {
        ve.com.develcom.servicios.IndicesExpedienteServicio service = new ve.com.develcom.servicios.IndicesExpedienteServicio();
//        com.develcom.expediente.IndicesExpediente_Service service = new com.develcom.expediente.IndicesExpediente_Service();
        com.develcom.expediente.IndicesExpediente port = service.getIndicesExpedientePort();
        return port.buscarDatosIndicesExpediente(idExpediente, idLibreria, idCategoria);
    }
    
}
