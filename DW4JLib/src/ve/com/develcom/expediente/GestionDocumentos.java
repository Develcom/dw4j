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
public class GestionDocumentos {

    private Traza traza = new Traza(GestionDocumentos.class);

    /**
     * Busca los documentos
     * digitalizados del
     * expediente
     * @param idDocumento
     * El id del documento
     * @param idExpediente
     * El id del expediente
     * @param idCategoria
     * El id de la categoria
     * @param idSubCategoria
     * El id de la subcategoria
     * @param estatusDocumento
     * El estatus del documento
     * (aprobado, rechazado, pendiente)
     * @param estatusAprobado
     * @param reDigitalizar
     * @return
     * Listado de los documentos
     * digitalizado del expediente
     * @throws javax.xml.soap.SOAPException
     */
    public java.util.List<com.develcom.documento.InfoDocumento> encontrarInformacionDoc(java.util.List<java.lang.Integer> idDocumento, 
            java.lang.String idExpediente, int idCategoria, int idSubCategoria, int estatusDocumento,  
            int estatusAprobado, boolean reDigitalizar) throws SOAPException, SOAPFaultException{
        traza.trace("buscar documentos digitalizados", Level.INFO);
        traza.trace("idDocumento "+idDocumento, Level.INFO);
        traza.trace("idExpediente "+idExpediente, Level.INFO);
        traza.trace("idCategoria "+idCategoria, Level.INFO);
        traza.trace("idSubCategoria "+idSubCategoria, Level.INFO);
        traza.trace("estatus del documento "+estatusDocumento, Level.INFO);
        return buscarInformacionDocumento(idDocumento, idExpediente, idCategoria, idSubCategoria, estatusDocumento, estatusAprobado, reDigitalizar);
    }

    /**
     * Busca la ultima version
     * de un documento especifico
     * @param idDocumento
     * El id del documento
     * @param idExpediente
     * El id del expedienye
     * @param numeroDocumento
     * El numero del documento
     * @return
     * El numenro de la ultima
     * version del documento
     */
    public int buscarVersionUltima(int idDocumento, java.lang.String idExpediente, int numeroDocumento) throws SOAPException, SOAPFaultException{
        traza.trace("busca la ultima version del documento", Level.INFO);
        traza.trace("idExpediente "+idExpediente+" numero del documento "+numeroDocumento, Level.INFO);
        return buscarUltimaVersionDoc(idDocumento, idExpediente, numeroDocumento);
    }

    /**
     * Busca los datos adicionales
     * con sus valores
     * @param idTipoDocumento
     * Identificador del tipo
     * de documento
     * @param idExpediente
     * Identificador del expediente
     * @return 
     * Listado con los datos adicionales
     * y sus valores
     * @throws javax.xml.soap.SOAPException
     */
    public java.util.List<com.develcom.documento.DatoAdicional> encontrarValorDatoAdicional(int idTipoDocumento, java.lang.String idExpediente, int numeroDocumento, int version) throws SOAPException, SOAPFaultException{
        return buscarValorDatoAdicional(idTipoDocumento, idExpediente, numeroDocumento, version);
    }
    private static int buscarUltimaVersionDoc(int idDocumento, java.lang.String idExpediente, int numeroDocumento) {
        ve.com.develcom.servicios.DocumentoServicio service = new ve.com.develcom.servicios.DocumentoServicio();
//        com.develcom.documento.Documento_Service service = new com.develcom.documento.Documento_Service();
        com.develcom.documento.Documento port = service.getDocumentoPort();
        return port.buscarUltimaVersionDoc(idDocumento, idExpediente, numeroDocumento);
    }

    private static java.util.List<com.develcom.documento.InfoDocumento> buscarInformacionDocumento(java.util.List<java.lang.Integer> idDocumento, java.lang.String idExpediente, int idCategoria, int idSubCategoria, int estatusDocumento, int estatusAprobado, boolean reDigitalizar) {
        ve.com.develcom.servicios.DocumentoServicio service = new ve.com.develcom.servicios.DocumentoServicio();
//        com.develcom.documento.Documento_Service service = new com.develcom.documento.Documento_Service();
        com.develcom.documento.Documento port = service.getDocumentoPort();
        return port.buscarInformacionDocumento(idDocumento, idExpediente, idCategoria, idSubCategoria, estatusDocumento, estatusAprobado, reDigitalizar);
    }

    private static java.util.List<com.develcom.documento.DatoAdicional> buscarValorDatoAdicional(int idTipoDocumento, java.lang.String idExpediente, int numeroDocumento, int version) {
        ve.com.develcom.servicios.DocumentoServicio service = new ve.com.develcom.servicios.DocumentoServicio();
//        com.develcom.documento.Documento_Service service = new com.develcom.documento.Documento_Service();
        com.develcom.documento.Documento port = service.getDocumentoPort();
        return port.buscarValorDatoAdicional(idTipoDocumento, idExpediente, numeroDocumento, version);
    }
    
}
