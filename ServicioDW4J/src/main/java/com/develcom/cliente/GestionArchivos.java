/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.cliente;


import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GestionArchivos {

    private Logger traza = LoggerFactory.getLogger(GestionArchivos.class);

    /**
     * Crea el archivo en el
     * servidor y guarda toda
     * la informacion del mismo
     * en la base de datos
     * @param buffer
     * El buffer del archivo
     * @param accion
     * La accion a realizar
     * (guardar, reemplazar, versionar)
     * @param infoDocumento
     * Toda la informacion del archivo
     * @param expediente
     * La informacion del expediente
     * @param usuario
     * El usuario del guarda el archivo
     * @return
     * @throws javax.xml.soap.SOAPException
     */
    public String almacenaArchivo(com.develcom.documento.Bufer buffer, java.lang.String accion, 
            com.develcom.documento.InfoDocumento infoDocumento, 
            java.lang.String expediente, java.lang.String usuario) throws SOAPException, SOAPFaultException {
        traza.trace("guarda el archivo");
        traza.trace("accion "+accion);
        traza.trace("infoDocumento "+infoDocumento);
        traza.trace("expediente "+expediente);
        traza.trace("usuario "+usuario);
        return guardarArchivo(buffer, accion, infoDocumento, expediente, usuario);
    }


    /**
     * Busca el documento y
     * el archivo para ser
     * visualizado
     * @param idInfoDocumento
     * El id de la informacion del archivo
     * @param idDocumento
     * El id del tipo de documento
     * @param idCategoria
     * El id de la categoria
     * @param idSubCategoria
     * El id de la subcategoria
     * @param idExpediente
     * El id del expediente
     * @param version
     * La version del documento
     * @param numeroDocumento
     * El numero del documento
     * @return
     * Listado del toda la informacion
     * del archivo
     * @throws javax.xml.soap.SOAPException
     */
    public List<InfoDocumento> buscarImagenDocumentos (int idInfoDocumento, int idDocumento, int idCategoria, int idSubCategoria, 
            int version, int numeroDocumento, java.lang.String idExpediente) throws SOAPException, SOAPFaultException {
        traza.trace("buscar los tipos de documento");
        traza.trace("idInfoDocumento "+idInfoDocumento);
        traza.trace("idDocumento "+idDocumento);
        traza.trace("idCategoria "+idCategoria);
        traza.trace("idSubCategoria "+idSubCategoria);
        traza.trace("version "+version);
        traza.trace("numero del documeto "+numeroDocumento);
        traza.trace("idExpediente "+idExpediente);
        return buscarFisicoDocumento(idInfoDocumento, idDocumento, idCategoria, idSubCategoria, version, numeroDocumento, idExpediente);
    }


    /**
     * Busca el archivo
     * fisico para ser
     * guradado y visualizado
     * en forma local
     * @param ruta
     * Ruta donde esta alojado
     * el archivo
     * @param archivo
     * Nombre del archivo
     * @return
     * El bufer del archivo
     * @throws javax.xml.soap.SOAPException
     */
    public Bufer buscandoArchivo(String ruta, String archivo) throws SOAPFaultException, SOAPException{
        traza.trace("buscar archivo");
        traza.trace("archivo "+archivo);
        traza.trace("ruta "+ruta);
        Bufer resp = null;
        try {
            resp = buscarBuferArchivo(ruta, archivo);
        } catch (Exception ex) {
            traza.trace("archivo no encontrado", ex);
        }
        return resp;
    }

    private static Bufer buscarBuferArchivo(java.lang.String ruta, java.lang.String archivo) {
        com.develcom.cliente.servicios.ArchivoServicio service = new com.develcom.cliente.servicios.ArchivoServicio();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.buscarBuferArchivo(ruta, archivo);
    }

    private static String guardarArchivo(com.develcom.documento.Bufer buffer, java.lang.String accion, com.develcom.documento.InfoDocumento infoDocumento, java.lang.String expediente, java.lang.String usuario) {
        com.develcom.cliente.servicios.ArchivoServicio service = new com.develcom.cliente.servicios.ArchivoServicio();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.guardarArchivo(buffer, accion, infoDocumento, expediente, usuario);
    }

    private static java.util.List<com.develcom.documento.InfoDocumento> buscarFisicoDocumento(int idInfoDocumento, int idDocumento, int parameter1, int idSubCategoria, int version, int numeroDocumento, java.lang.String idExpediente) {
        com.develcom.cliente.servicios.ArchivoServicio service = new com.develcom.cliente.servicios.ArchivoServicio();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.buscarFisicoDocumento(idInfoDocumento, idDocumento, parameter1, idSubCategoria, version, numeroDocumento, idExpediente);
    }
    
}
