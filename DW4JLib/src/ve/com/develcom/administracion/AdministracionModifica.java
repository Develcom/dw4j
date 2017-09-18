/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.administracion;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class AdministracionModifica {

    private Traza traza = new Traza(AdministracionModifica.class);

    /**
     * Modifica el perfil
     * del usuario
     * @param perfil
     * Lista con el perfil
     * del usuario
     * @return
     * Una lista con los id
     * del perfil del usuario
     * @throws javax.xml.soap.SOAPException
     */
//    public java.util.List<java.lang.Integer> modificandoPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) throws SOAPException, SOAPFaultException {
//        traza.trace("modificar perfil "+perfil, Level.INFO);
//        return modificarPerfil(perfil);
//    }
    public boolean modificandoPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) throws SOAPException, SOAPFaultException {
        traza.trace("modificar perfil "+perfil, Level.INFO);
        return modificarPerfil(perfil);
    }

    /**
     * Modifica el estatus de la
     * libreria
     * @param librerias
     * Una lista de librerias
     * @return
     * Verdadero si se cambio el
     * estatus, falso en caso
     * contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean modificandoLibreria(java.util.List<com.develcom.administracion.Libreria> librerias) throws SOAPException, SOAPFaultException{
        traza.trace("modificar librerias "+librerias, Level.INFO);
        return modificarLibreria(librerias);
    }

    /**
     * Modifica el estatus de la
     * categorias
     * @param categorias
     * Una lista de categorias
     * @return
     * Verdadero si se cambio el
     * estatus, falso en caso
     * contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean modificandoCategoria(java.util.List<com.develcom.administracion.Categoria> categorias) throws SOAPException, SOAPFaultException {
        traza.trace("modificar categorias "+categorias, Level.INFO);
        return modificarCategoria(categorias);
    }

    /**
     * Modifica el estatus de la
     * subCategorias
     * @param subCategorias
     * Una lista de subCategorias
     * @return
     * Verdadero si se cambio el
     * estatus, falso en caso
     * contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean modificandoSubCategoria(java.util.List<com.develcom.administracion.SubCategoria> subCategorias) throws SOAPException, SOAPFaultException {
        traza.trace("modificar subCategorias "+subCategorias, Level.INFO);
        return modificarSubCategoria(subCategorias);
    }

    /**
     * Modifica el estatus de
     * tipo de documentos
     * @param tipodocumentos
     * Una lista de tipo de documentos
     * @return
     * Verdadero si se cambio el
     * estatus, falso en caso
     * contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean modificandoipoDocumento(java.util.List<com.develcom.administracion.TipoDocumento> tipodocumentos) throws SOAPException, SOAPFaultException {
        traza.trace("modificar tipo de documentos "+tipodocumentos, Level.INFO);
        return modificarTipoDocumento(tipodocumentos);
    }

    /**
     * Modifica los datos
     * del combo
     * @param combos
     * Una lista de datos
     * @return
     * Verdadero si se modifico el
     * dato, falso en caso
     * contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean modificandoDatosCombo(java.util.List<com.develcom.administracion.Combo> combos) throws SOAPException, SOAPFaultException {
        traza.trace("modificar datos de la lista desplegable "+combos, Level.INFO);
        return modificarDatosCombo(combos);
    }

    /**
     * Cambia es estatus de un 
     * usuario
     * @param usuario
     * El id del usuario
     * @param estatus
     * El estatus del
     * usuario
     * @return
     * Verdadero si se cambio 
     * el esttus, falso en caso
     * contrario
     */
//    @Deprecated
//    public boolean modificandoEstatusUsuarios(java.lang.String usuario, int estatus) throws SOAPException, SOAPFaultException {
//        traza.trace("modificar estatus "+estatus+" usuario "+usuario, Level.INFO);
//        return cambiarEstatusUsuarios(usuario, estatus);
//    }
//    
    /**
     * Establece cual es el 
     * tipo de documento
     * que contiene la foto
     * del expediente
     * @param idTipoDocumento
     * @return 
     * @throws javax.xml.soap.SOAPException 
     */
    public boolean agregaTipoDocumentoFoto(int idTipoDocumento) throws SOAPException, SOAPFaultException {
        return modificarTipoDocumentoFoto(idTipoDocumento);
    }

    private static boolean modificarCategoria(java.util.List<com.develcom.administracion.Categoria> categorias) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarCategoria(categorias);
    }

    private static boolean modificarDatosCombo(java.util.List<com.develcom.administracion.Combo> combos) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarDatosCombo(combos);
    }

    private static boolean modificarLibreria(java.util.List<com.develcom.administracion.Libreria> librerias) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarLibreria(librerias);
    }

//    private static java.util.List<java.lang.Integer> modificarPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) {
//        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
//        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
//        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
//        return port.modificarPerfil(perfil);
//    }

    private static boolean modificarPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
//        com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarPerfil(perfil);
    }

    private static boolean modificarSubCategoria(java.util.List<com.develcom.administracion.SubCategoria> subCategorias) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarSubCategoria(subCategorias);
    }

    private static boolean modificarTipoDocumento(java.util.List<com.develcom.administracion.TipoDocumento> tipodocumentos) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
        //com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarTipoDocumento(tipodocumentos);
    }

    private static boolean modificarTipoDocumentoFoto(int idTipoDocumento) {
        ve.com.develcom.servicios.AdministracionModificaServicio service = new ve.com.develcom.servicios.AdministracionModificaServicio();
//        com.develcom.administracion.AdministracionModifica_Service service = new com.develcom.administracion.AdministracionModifica_Service();
        com.develcom.administracion.AdministracionModifica port = service.getAdministracionModificaPort();
        return port.modificarTipoDocumentoFoto(idTipoDocumento);
    }
    
}
