/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.cliente;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.Perfil;
import com.develcom.administracion.Sesion;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdministracionBusqueda {

    private Logger traza = LoggerFactory.getLogger(AdministracionBusqueda.class);
    
//    @Autowired
//    private static com.develcom.cliente.servicios.AdministracionBusquedasServicio service;

    /**
     * Busca las librerias y categorias
     * para crear el perfil del usuario
     * asociando las categorias a ese
     * perfil
     * @return
     * Listado de libreria y sus categorias
     * que se encuentran activas
     * @throws javax.xml.soap.SOAPException
     */
    public List<Perfil> buscarLibCat() throws SOAPException, SOAPFaultException{
        traza.trace("busca librerias y categoria para configurar el usuario");
        return buscarLibreriasCategorias();
    }

    /**
     * Comprueba que el usuario
     * se encuentre en LDAP y/o
     * base de datos
     * @param user
     * El usuario a comprobar
     * @return
     * Si el usuario existe retorna
     * el perfil del mismo, si existe
     * en LDAP y no en base de datos
     * retorna la respuesta correspondiente,
     * si no existe en LDAP retorna la
     * respeusta correspondiente
     * @throws javax.xml.soap.SOAPException
     */
    public Sesion compruebaUsuario(String user) throws SOAPException, SOAPFaultException{
        traza.trace("comprobando usuario "+user);
        return comprobarUsuario(user);
    }

    /**
     * Busca librerias
     * en base de datos
     * @param libreria
     * Nombre de la libreria
     * @param idLibreria
     * El id de la libreria
     * @return
     * Lista con las librerias
     * buscadas
     * @throws javax.xml.soap.SOAPException
     */
    public List<Libreria> buscarLibreria(String libreria, int idLibreria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar libreria");
        traza.trace("idLibreria "+idLibreria+" libreria "+libreria);
        return burcarLibrerias(libreria, idLibreria);
    }

    /**
     * Busca categorias
     * en la base de datos
     * @param categoria
     * Nombre de la categoria
     * @param idCategoria
     * El id de la categoria
     * @param idLibreria
     * El id de la libreria
     * @return
     * Listado con las categorias
     * buscada
     * @throws javax.xml.soap.SOAPException
     */
    public List<Categoria> buscarCategoria(java.lang.String categoria, int idLibreria, int idCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar categoria");
        traza.trace("idLibreria "+idLibreria+" idCategoria "+idCategoria+" categoria "+categoria);
        return buscarCategorias(categoria, idLibreria, idCategoria);
    }

    /**
     * Busca subcategorias
     * en la base de datos
     * @param subCategoria
     * Nombre de la subcategoria
     * @param idCategoria
     * El id de la categoria
     * @param idSubCategoria
     * El id de la subcategoria
     * @return
     * Listado de las subcategorias
     * buscadas
     * @throws javax.xml.soap.SOAPException
     */
    public List<SubCategoria> buscarSubCategoria(String subCategoria, int idCategoria, int idSubCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar subCategoria");
        traza.trace("idCategoria "+idCategoria+" idSubCategoria "+idSubCategoria+" subCategoria "+subCategoria);
        return burcarSubCategorias(subCategoria, idCategoria, idSubCategoria);
    }

    /**
     * Busca los tipos de
     * documentos en la
     * base de datos
     * @param tipoDocumento
     * Nombre del tipo de documento
     * @param idCategoria
     * El id de la categoria
     * @param idSubCategoria
     * el id de la subcategoria
     * @return
     * Listado de los tipos de
     * documentos buscados
     * @throws javax.xml.soap.SOAPException
     */
    public List<TipoDocumento> buscarTipoDocumento(String tipoDocumento, int idCategoria, int idSubCategoria) throws SOAPException, SOAPFaultException{
        traza.trace("buscar Tipo de Documento");
        traza.trace("idCategoria "+idCategoria+" idSubCategoria "+idSubCategoria+" tipo de documento "+tipoDocumento);
        return burcarTiposDocumentos(tipoDocumento, idCategoria, idSubCategoria);
    }

    /**
     * Busca el perfil del
     * usuario
     * @param user
     * El usuario
     * @return
     * Listado con el perfil del
     * usuario
     * @throws javax.xml.soap.SOAPException
     */
    public java.util.List<com.develcom.administracion.Sesion> buscandoPerfil(java.lang.String user) throws SOAPException, SOAPFaultException {
        traza.trace("buscar perfil");
        return buscarPerfil(user);
    }

    /**
     * Busca los argumento
     * (indices dinamicos)
     * en la base de datos
     * @param idCategoria
     * El id de la categoria
     * @return
     * Listado con los argurmnetos
     * buscados
     * @throws javax.xml.soap.SOAPException
     */
    public java.util.List<com.develcom.administracion.Indice> buscandoIndices(int idCategoria) throws SOAPException, SOAPFaultException {
        traza.trace("buscar argumentos");
        traza.trace("idCategoria "+idCategoria);
        return buscarIndices(idCategoria);
    }

    /**
     * Busca los datos de la
     * lista desplegable
     * @param idArgumento
     * El id del argumento (indice)
     * @return
     * Listado con los datos
     * de la lista desplegable
     */
    public java.util.List<com.develcom.administracion.Combo> buscandoDatosCombo(int idArgumento, boolean bandera) throws SOAPException, SOAPFaultException {
        traza.trace("buscar datos de la lista desplegable");
        traza.trace("idArgumento "+idArgumento);
        return buscarDatosCombo(idArgumento, bandera);
    }
    
    /**
     * Busca los indices de los
     * datos adicionales del
     * tipo de documento
     * @param idTipoDocumento
     * Identificadro del
     * tipo de documento
     * @return 
     */
    public java.util.List<com.develcom.administracion.DatoAdicional> buscarIndDatoAdicional(int idTipoDocumento) throws SOAPException, SOAPFaultException {
        return buscarIndiceDatoAdicional(idTipoDocumento);
    }

    private static java.util.List<com.develcom.administracion.Perfil> buscarLibreriasCategorias() {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarLibreriasCategorias();
    }

    private static Sesion comprobarUsuario(java.lang.String login) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.comprobarUsuario(login);
    }

    private static java.util.List<Libreria> burcarLibrerias(java.lang.String libreria, int idLibreria) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarLibrerias(libreria, idLibreria);
    }

    private static java.util.List<com.develcom.administracion.TipoDocumento> burcarTiposDocumentos(java.lang.String tipoDocumento, int idCategoria, int idSubCategoria) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarTiposDocumentos(tipoDocumento, idCategoria, idSubCategoria);
    }

    private static java.util.List<com.develcom.administracion.Sesion> buscarPerfil(java.lang.String user) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarPerfil(user);
    }

    private static java.util.List<com.develcom.administracion.Combo> buscarDatosCombo(int idArgumento, boolean bandera) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarDatosCombo(idArgumento, bandera);
    }

    private static java.util.List<com.develcom.administracion.SubCategoria> burcarSubCategorias(java.lang.String subCategoria, int idCategoria, int idSubCategoria) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarSubCategorias(subCategoria, idCategoria, idSubCategoria);
    }

    private static java.util.List<com.develcom.administracion.DatoAdicional> buscarIndiceDatoAdicional(int idTipoDocumento) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarIndiceDatoAdicional(idTipoDocumento);
    }

    private static java.util.List<com.develcom.administracion.Indice> buscarIndices(int idCategoria) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarIndices(idCategoria);
    }

    private static java.util.List<com.develcom.administracion.Categoria> buscarCategorias(java.lang.String categoria, int idLibreria, int idCategoria) {
        com.develcom.cliente.servicios.AdministracionBusquedasServicio service = new com.develcom.cliente.servicios.AdministracionBusquedasServicio();
        com.develcom.administracion.AdministracionBusquedas port = service.getAdministracionBusquedasPort();
        return port.buscarCategorias(categoria, idLibreria, idCategoria);
    }
    
    
}
