/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.archivo;


import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class GestionArchivos {

    private Traza traza = new Traza(GestionArchivos.class);

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
        traza.trace("guarda el archivo", Level.INFO);
        traza.trace("accion "+accion, Level.INFO);
        traza.trace("infoDocumento "+infoDocumento, Level.INFO);
        traza.trace("expediente "+expediente, Level.INFO);
        traza.trace("usuario "+usuario, Level.INFO);
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
        traza.trace("buscar los tipos de documento", Level.INFO);
        traza.trace("idInfoDocumento "+idInfoDocumento, Level.INFO);
        traza.trace("idDocumento "+idDocumento, Level.INFO);
        traza.trace("idCategoria "+idCategoria, Level.INFO);
        traza.trace("idSubCategoria "+idSubCategoria, Level.INFO);
        traza.trace("version "+version, Level.INFO);
        traza.trace("numero del documeto "+numeroDocumento, Level.INFO);
        traza.trace("idExpediente "+idExpediente, Level.INFO);
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
        traza.trace("buscar archivo", Level.INFO);
        traza.trace("archivo "+archivo, Level.INFO);
        traza.trace("ruta "+ruta, Level.INFO);
        Bufer resp = null;
        try {
            resp = buscarBuferArchivo(ruta, archivo);
        } catch (Exception ex) {
            traza.trace("archivo no encontrado", Level.ERROR, ex);
        }
        return resp;
    }
    
    /**
     * Busca la imagen
     * foto del expediente
     * @param idExpediente
     * @return 
     */
//    public com.develcom.expediente.InfoDocumento buscarFotoFicha(java.lang.String idExpediente) throws SOAPException, SOAPFaultException {
//        return buscarFicha(idExpediente);
//    }
//
//    private static java.util.List<com.develcom.documento.InfoDocumento> findTipoDocumento(int idInfoDocumento, int idDocumento, int idCategoria, int idSubCategoria, java.lang.String idExpediente, int version, int numeroDocumento) {
//        ve.com.develcom.servicios.ArchivoServicio service = new ve.com.develcom.servicios.ArchivoServicio();
//        //com.develcom.documento.ArchivoService service = new com.develcom.documento.ArchivoService();
//        com.develcom.documento.Archivo port = service.getArchivoPort();
//        return port.findTipoDocumento(idInfoDocumento, idDocumento, idCategoria, idSubCategoria, idExpediente, version, numeroDocumento);
//    }
//
//    private static com.develcom.expediente.InfoDocumento buscarFicha(java.lang.String idExpediente) {
//        ve.com.develcom.servicios.BusquedaDinamicaServicio service = new ve.com.develcom.servicios.BusquedaDinamicaServicio();
//        //com.develcom.expediente.BusquedaDinamicaService service = new com.develcom.expediente.BusquedaDinamicaService();
//        com.develcom.expediente.BusquedaExpediente port = service.getBusquedaExpedientePort();
//        return port.buscarFicha(idExpediente);
//    }

//    private static Bufer buscarArchivo(java.lang.String ruta, java.lang.String archivo) {
//        ve.com.develcom.servicios.ArchivoServicio service = new ve.com.develcom.servicios.ArchivoServicio();
//        //com.develcom.documento.Archivo_Service service = new com.develcom.documento.Archivo_Service();
//        com.develcom.documento.Archivo port = service.getArchivoPort();
//        return port.buscarArchivo(ruta, archivo);
//    }

    private static Bufer buscarBuferArchivo(java.lang.String ruta, java.lang.String archivo) {
        ve.com.develcom.servicios.ArchivoServicio service = new ve.com.develcom.servicios.ArchivoServicio();
//        com.develcom.documento.Archivo_Service service = new com.develcom.documento.Archivo_Service();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.buscarBuferArchivo(ruta, archivo);
    }

    private static String guardarArchivo(com.develcom.documento.Bufer buffer, java.lang.String accion, com.develcom.documento.InfoDocumento infoDocumento, java.lang.String expediente, java.lang.String usuario) {
        ve.com.develcom.servicios.ArchivoServicio service = new ve.com.develcom.servicios.ArchivoServicio();
        //com.develcom.documento.Archivo_Service service = new com.develcom.documento.Archivo_Service();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.guardarArchivo(buffer, accion, infoDocumento, expediente, usuario);
    }

    private static java.util.List<com.develcom.documento.InfoDocumento> buscarFisicoDocumento(int idInfoDocumento, int idDocumento, int parameter1, int idSubCategoria, int version, int numeroDocumento, java.lang.String idExpediente) {
        ve.com.develcom.servicios.ArchivoServicio service = new ve.com.develcom.servicios.ArchivoServicio();
//        com.develcom.documento.Archivo_Service service = new com.develcom.documento.Archivo_Service();
        com.develcom.documento.Archivo port = service.getArchivoPort();
        return port.buscarFisicoDocumento(idInfoDocumento, idDocumento, parameter1, idSubCategoria, version, numeroDocumento, idExpediente);
    }
    
}
