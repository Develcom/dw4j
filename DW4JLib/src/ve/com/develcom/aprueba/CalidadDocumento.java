/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.aprueba;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class CalidadDocumento {

    private Traza traza = new Traza(CalidadDocumento.class);

    /**
     * Busca documentos digitalizado
     * para aprobar o rechazar (lista)
     * @param usuario
     * @param fechaDesde
     * La fecha desde en la busqueda
     * @param fechaHasta
     * La fecha hasta en la busqueda
     * @param estatusDocumento
     * El estatus del documento
     * @param idCategoria
     * El id de la categoria
     * @param idExpediente
     * El id del expediente
     * @return
     * Lista con documentos
     * @throws SOAPException
     */
    public java.util.List<com.develcom.calidad.ConsultaDinamica> buscarDocumentosPendientes(java.lang.String usuario, javax.xml.datatype.XMLGregorianCalendar fechaDesde, javax.xml.datatype.XMLGregorianCalendar fechaHasta, int estatusDocumento, int idCategoria, java.lang.String idExpediente) throws SOAPException, SOAPFaultException{
        traza.trace("buscar documentos", Level.INFO);
        traza.trace("fecha desde"+fechaDesde, Level.INFO);
        traza.trace("fecha hasta"+fechaHasta, Level.INFO);
        traza.trace("estatus documento "+estatusDocumento, Level.INFO);
        traza.trace("idCategoria "+idCategoria, Level.INFO);
        traza.trace("idExpediente "+idExpediente, Level.INFO);
        return buscarExpedientesPendientes(usuario, fechaDesde, fechaHasta, estatusDocumento, idCategoria, idExpediente);
    }

    /**
     * Aprueba un documento digitalizado
     * @param idInfoDocumento
     * El id del info documento
     * @param usuario
     * El usuario aprobador
     * @return
     * Verdadro si tuvo exito el aprobar,
     * falso en caso contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean aprobarDoc(int idInfoDocumento, java.lang.String usuario) throws SOAPException, SOAPFaultException {
        traza.trace("aprobar documento", Level.INFO);
        traza.trace("idInfoDocumento "+idInfoDocumento+" usuario "+usuario, Level.INFO);
        return aprobarDocumento(idInfoDocumento, usuario);
    }

    /**
     * Rechaza un documento digitalizado
     * @param idInfoDocumento
     * El id del info documento
     * @param usuario
     * El usuario rechazador
     * @param causa
     * La causa del rechazo
     * (Lista desplegable)
     * @param motivo
     * El motivo del rechazo
     * @return
     */
    public boolean rechazarDoc(int idInfoDocumento, java.lang.String usuario, java.lang.String causa, java.lang.String motivo) throws SOAPException, SOAPFaultException {
        traza.trace("rechaza documento", Level.INFO);
        traza.trace("idInfoDocumento "+idInfoDocumento, Level.INFO);
        traza.trace("usuario "+usuario, Level.INFO);
        traza.trace("causa "+causa, Level.INFO);
        traza.trace("motivo "+motivo, Level.INFO);
        return rechazarDocumento(idInfoDocumento, usuario, causa, motivo);
    }

    /**
     * Busca las causas de
     * rechazo para llenar 
     * una lista desplegable
     * @return 
     * Una lista con las
     * causas de rechazo
     */
    public java.util.List<java.lang.String> encontrarCausasRechazo() throws SOAPException, SOAPFaultException {
        return buscarCausasRechazo();
    }

    private static boolean aprobarDocumento(int idInfoDocumento, java.lang.String usuario) {
        ve.com.develcom.servicios.CalidadDocumentoServicio service = new ve.com.develcom.servicios.CalidadDocumentoServicio();
        //com.develcom.aprobar.ApruebaRechazaDocumentoService service = new com.develcom.aprobar.ApruebaRechazaDocumentoService();
        com.develcom.calidad.CalidadDocumento port = service.getApruebaRechazaDocumentoPort();
        return port.aprobarDocumento(idInfoDocumento, usuario);
    }

    private static boolean rechazarDocumento(int idInfoDocumento, java.lang.String usuario, java.lang.String causa, java.lang.String motivo) {
        ve.com.develcom.servicios.CalidadDocumentoServicio service = new ve.com.develcom.servicios.CalidadDocumentoServicio();
        //com.develcom.aprobar.ApruebaRechazaDocumentoService service = new com.develcom.aprobar.ApruebaRechazaDocumentoService();
        com.develcom.calidad.CalidadDocumento port = service.getApruebaRechazaDocumentoPort();
        return port.rechazarDocumento(idInfoDocumento, usuario, causa, motivo);
    }

    private static java.util.List<java.lang.String> buscarCausasRechazo() {
        ve.com.develcom.servicios.CalidadDocumentoServicio service = new ve.com.develcom.servicios.CalidadDocumentoServicio();
        //com.develcom.aprobar.ApruebaRechazaDocumento_Service service = new com.develcom.aprobar.ApruebaRechazaDocumento_Service();
        com.develcom.calidad.CalidadDocumento port = service.getApruebaRechazaDocumentoPort();
        return port.buscarCausasRechazo();
    }

    private static java.util.List<com.develcom.calidad.ConsultaDinamica> buscarExpedientesPendientes(java.lang.String usuario, javax.xml.datatype.XMLGregorianCalendar fechaDesde, javax.xml.datatype.XMLGregorianCalendar fechaHasta, int estatusDocumento, int idCategoria, java.lang.String idExpediente) {
        ve.com.develcom.servicios.CalidadDocumentoServicio service = new ve.com.develcom.servicios.CalidadDocumentoServicio();
//        com.develcom.aprobar.ApruebaRechazaDocumento_Service service = new com.develcom.aprobar.ApruebaRechazaDocumento_Service();
        com.develcom.calidad.CalidadDocumento port = service.getApruebaRechazaDocumentoPort();
        return port.buscarExpedientesPendientes(usuario, fechaDesde, fechaHasta, estatusDocumento, idCategoria, idExpediente);
    }
    
}
