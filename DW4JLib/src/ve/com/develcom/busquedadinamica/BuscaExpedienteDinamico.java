/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.busquedadinamica;

import com.develcom.expediente.Categoria;
import javax.xml.soap.SOAPException;
import com.develcom.expediente.Indice;
import com.develcom.expediente.InfoDocumento;
import com.develcom.expediente.SubCategoria;
import com.develcom.expediente.TipoDocumento;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class BuscaExpedienteDinamico {

    private Traza traza = new Traza(BuscaExpedienteDinamico.class);

    /**
     * Busca los expediente de forma dinamica
     * generando en el webservice el sql segun
     * datos enviado por parametros
     * @param listaIndice
     * Lista de indices
     * @param listaCat
     * @param listaSubCat
     * Lista de SubCategorias
     * @param listaTipoDoc
     * Lista de tipos de documentos
     * @param estatusDocumento
     * Estatus del los documentos
     * @param idLibreria
     * @return
     * Lista con toda la informacion del expediente,
     * los indices primarios con sus datos correspondiente
     * @throws SOAPException
     */
    public java.util.List<com.develcom.expediente.ConsultaDinamica> consultarExpedienteDinamico(java.util.List<Indice> listaIndice,  java.util.List<Categoria> listaCat, java.util.List<SubCategoria> listaSubCat, java.util.List<TipoDocumento> listaTipoDoc, int estatusDocumento, int idLibreria) throws SOAPException, SOAPFaultException {
        traza.trace("buscar los expediente de forma dinamica", Level.INFO);
        traza.trace("lista indices "+listaIndice, Level.INFO);
        traza.trace("lista subCategoria "+listaSubCat, Level.INFO);
        traza.trace("lista tipo de documento "+listaIndice, Level.INFO);
        traza.trace("estatu documento "+estatusDocumento, Level.INFO);
        traza.trace("idLibreria "+idLibreria, Level.INFO);
        return buscarExpedienteDinamico(listaIndice, listaCat, listaSubCat, listaTipoDoc, estatusDocumento, idLibreria);
    }
    
    /**
     * Busca los tipos de documentos
     * para llenar la lista en la
     * busqueda del expediente
     * @param idSubCategorias
     * Listado de identificadores de
     * las subCategorias
     * @return
     * Listado de los tipos de documentos
     * @throws SOAPException
     * @throws SOAPFaultException 
     */
    public java.util.List<com.develcom.expediente.TipoDocumento> buscarTiposDocumentos(java.util.List<java.lang.Integer> idSubCategorias) throws SOAPException, SOAPFaultException {
        traza.trace("idSubCategoria "+idSubCategorias, Level.INFO);
        return buscarTipoDocumento(idSubCategorias);
    }
    
    /**
     * Busca las subCategorias
     * para llenar la lista en la
     * busqueda del expediente
     * @param idCategorias
     * @return
     * @throws SOAPException
     * @throws SOAPFaultException 
     */
    public java.util.List<com.develcom.expediente.SubCategoria> encontrarSubCategorias(java.util.List<java.lang.Integer> idCategorias) throws SOAPException, SOAPFaultException {
        return buscarSubCategorias(idCategorias);
    }
    
    /**
     * Busca la imagen
     * foto del expediente
     * @param idExpediente
     * @return 
     */
    public com.develcom.expediente.InfoDocumento buscarFotoFicha(java.lang.String idExpediente) throws SOAPException, SOAPFaultException {
        return buscarFicha(idExpediente);
    }
    
    /**
     * Busca expediente de manera generica
     * @param listaIndice
     * @param idCategorias
     * @param idLibreria
     * @return
     * @throws SOAPException
     * @throws SOAPFaultException 
     */
    public java.util.List<com.develcom.expediente.ConsultaDinamica> encontrarExpedienteGenerico(java.util.List<com.develcom.expediente.Indice> listaIndice, int idLibreria, java.lang.String idCategorias) throws SOAPException, SOAPFaultException {
        return buscarExpedienteGenerico(listaIndice, idLibreria, idCategorias);
    }

    private static java.util.List<com.develcom.expediente.ConsultaDinamica> buscarExpedienteDinamico(java.util.List<com.develcom.expediente.Indice> listaIndice, java.util.List<com.develcom.expediente.Categoria> listaCat, java.util.List<com.develcom.expediente.SubCategoria> listaSubCat, java.util.List<com.develcom.expediente.TipoDocumento> listaTipoDoc, int estatusDocumento, int idLibreria) {
        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        com.develcom.expediente.BusquedaExpediente_Service service = new com.develcom.expediente.BusquedaExpediente_Service();
        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
        return port.buscarExpedienteDinamico(listaIndice, listaCat, listaSubCat, listaTipoDoc, estatusDocumento, idLibreria);
    }

    private static java.util.List<com.develcom.expediente.TipoDocumento> buscarTipoDocumento(java.util.List<java.lang.Integer> idSubCategorias) {
        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        com.develcom.expediente.BusquedaExpediente_Service service = new com.develcom.expediente.BusquedaExpediente_Service();
        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
        return port.buscarTipoDocumento(idSubCategorias);
    }

    private static InfoDocumento buscarFicha(java.lang.String idExpediente) {
        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        com.develcom.expediente.BusquedaExpediente_Service service = new com.develcom.expediente.BusquedaExpediente_Service();
        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
        return port.buscarFicha(idExpediente);
    }

    private static java.util.List<com.develcom.expediente.SubCategoria> buscarSubCategorias(java.util.List<java.lang.Integer> idCategorias) {
        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        com.develcom.expediente.BusquedaExpediente_Service service = new com.develcom.expediente.BusquedaExpediente_Service();
        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
        return port.buscarSubCategorias(idCategorias);
    }

    private static java.util.List<com.develcom.expediente.ConsultaDinamica> buscarExpedienteGenerico(java.util.List<com.develcom.expediente.Indice> listaIndice, int idLibreria, java.lang.String idCategorias) {
        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        com.develcom.expediente.BusquedaExpediente_Service service = new com.develcom.expediente.BusquedaExpediente_Service();
        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
        return port.buscarExpedienteGenerico(listaIndice, idLibreria, idCategorias);
    }
    
}
