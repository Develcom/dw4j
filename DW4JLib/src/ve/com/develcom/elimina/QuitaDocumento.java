/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.elimina;

import com.develcom.elimina.InfoDocumento;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author develcom
 */
public class QuitaDocumento {
    
//    /**
//     * Busca los expediente
//     * de los documentos que
//     * se eliminaran
//     * @param usuario
//     * Id del usuario
//     * @param fechaDesde
//     * Fecha inicio
//     * @param fechaHasta
//     * Fecha fin
//     * @param estatusDocumento
//     * Id del estado del documento
//     * @param idCategoria
//     * Id de la categoria
//     * @param idExpediente
//     * Id del expediente
//     * @return 
//     */
//    public java.util.List<com.develcom.elimina.ConsultaDinamica> buscarDocElimiar(java.lang.String usuario, javax.xml.datatype.XMLGregorianCalendar fechaDesde, javax.xml.datatype.XMLGregorianCalendar fechaHasta, int estatusDocumento, int idCategoria, java.lang.String idExpediente) throws SOAPException, SOAPFaultException {
//        return buscarDocumentos(usuario, fechaDesde, fechaHasta, estatusDocumento, idCategoria, idExpediente);
//    }
//    public InfoDocumento buscarDocElimiar(int idInfoDocumento, int idDocumento, int idCategoria, int idSubCategoria, java.lang.String idExpediente, int version, int numeroDocumento) {
//        return buscarFisicoDocumento(idInfoDocumento, idDocumento, idCategoria, idSubCategoria, idExpediente, version, numeroDocumento);
//    }
    
    /**
     * Elimina un tipo de 
     * documento
     * @param elimnaDocuento
     * Un objeto con toda la
     * información del documento
     * a eliminar
     * @return 
     * @throws javax.xml.soap.SOAPException 
     */
    public boolean eliminarTipoDocumento(com.develcom.elimina.EliminaDocumento_Type elimnaDocuento) throws SOAPException, SOAPFaultException {
        return eliminarDocumento(elimnaDocuento);
    }

    /**
     * Busca todo los documentos
     * a eliminar
     * @param idDocumento
     * Identificador del tipo documental
     * @param idExpediente
     * Identificador del expediente
     * @return 
     */
    public java.util.List<com.develcom.elimina.InfoDocumento> buscarListadoInfoDocumentos(java.util.List<java.lang.Integer> idDocumento, java.lang.String idExpediente) throws SOAPException, SOAPFaultException {
        return buscarInfoDocuEliminado(idDocumento, idExpediente);
    }
    
    /**
     * Busca los datos del tipo documental
     * para luego buscar el fisico
     * del mismo
     * @param idInfoDocumento
     * Identificador del registro
     * en base de dato
     * @param idDocumento
     * Identificador del tipo 
     * documental
     * @param idCategoria
     * Identificador de la 
     * categoria
     * @param idSubCategoria
     * Identificador de la
     * subCategoria
     * @param idExpediente
     * Identificador del
     * expediente
     * @param version
     * La version del
     * documento
     * @param numeroDocumento
     * EL consecutivo del
     * documento
     * @return 
     */
    public InfoDocumento buscarDatosDoc(int idInfoDocumento, java.lang.String idExpediente, int version, int numeroDocumento) {
        return buscarImagenDocumento(idInfoDocumento, idExpediente, version, numeroDocumento);
    }

//    private static InfoDocumento buscarFisicoDocumento(int idInfoDocumento, int idDocumento, int idCategoria, int idSubCategoria, java.lang.String idExpediente, int version, int numeroDocumento) {
//        ve.com.develcom.servicios.EliminaDocumentoServicio service = new ve.com.develcom.servicios.EliminaDocumentoServicio();
////        com.develcom.elimina.EliminaDocumento_Service service = new com.develcom.elimina.EliminaDocumento_Service();
//        com.develcom.elimina.EliminaDocumento port = service.getEliminaDocumentoPort();
//        return port.buscarFisicoDocumento(idInfoDocumento, idDocumento, idCategoria, idSubCategoria, idExpediente, version, numeroDocumento);
//    }

    private static boolean eliminarDocumento(com.develcom.elimina.EliminaDocumento_Type elimnaDocuento) {
        ve.com.develcom.servicios.EliminaDocumentoServicio service = new ve.com.develcom.servicios.EliminaDocumentoServicio();
//        com.develcom.elimina.EliminaDocumento_Service service = new com.develcom.elimina.EliminaDocumento_Service();
        com.develcom.elimina.EliminaDocumento port = service.getEliminaDocumentoPort();
        return port.eliminarDocumento(elimnaDocuento);
    }


    private static InfoDocumento buscarImagenDocumento(int idInfoDocumento, java.lang.String idExpediente, int version, int numeroDocumento) {
        ve.com.develcom.servicios.EliminaDocumentoServicio service = new ve.com.develcom.servicios.EliminaDocumentoServicio();
//        com.develcom.elimina.EliminaTipoDocumento_Service service = new com.develcom.elimina.EliminaTipoDocumento_Service();
        com.develcom.elimina.EliminaDocumento port = service.getEliminaDocumentoPort();
        return port.buscarImagenDocumento(idInfoDocumento, idExpediente, version, numeroDocumento);
    }

    private static java.util.List<com.develcom.elimina.InfoDocumento> buscarInfoDocuEliminado(java.util.List<java.lang.Integer> idDocumento, java.lang.String idExpediente) {
        ve.com.develcom.servicios.EliminaDocumentoServicio service = new ve.com.develcom.servicios.EliminaDocumentoServicio();
//        com.develcom.elimina.EliminaDocumento_Service service = new com.develcom.elimina.EliminaDocumento_Service();
        com.develcom.elimina.EliminaDocumento port = service.getEliminaDocumentoPort();
        return port.buscarInfoDocuEliminado(idDocumento, idExpediente);    
    }
    
}
