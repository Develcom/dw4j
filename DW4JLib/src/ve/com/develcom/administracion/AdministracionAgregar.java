/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.administracion;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;

/**
 *
 * @author develcom
 */
public class AdministracionAgregar {

    private Class clase = AdministracionAgregar.class;
    private Logger traza = Logger.getLogger(clase);
    
    /**
     * Agrega un nuevo perfil
     * del usuario
     * @param perfil
     * Una lista con el
     * perfil del usuario
     * @return
     * Una lista con los
     * id de los diferentes
     * registro de la base de datos
     * al agregar el perfil del usuario,
     * así permite saber si se agrego o no
     * el nuevo perfil
     */
    public boolean agregandoPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) throws SOAPException, SOAPFaultException{
        traza.info("agregando perfil "+perfil);
        return agregarPerfil(perfil);
    }

    /**
     * Agrega nuevas librerias
     * a la base de datos
     * @param librerias
     * Listado con las nuevas
     * librerias
     * @return
     * Verdadero si se agregaron las
     * librerias, falso en caso
     * contrario
     */
    public boolean insertarLibreria(java.util.List<com.develcom.administracion.Libreria> librerias) throws SOAPException, SOAPFaultException{
        traza.info("agregando librerias "+librerias);
        return agregarLibreria(librerias);
    }

    /**
     * Agrega categorias
     * a la base de datos
     * @param categorias
     * Lista con las nuevas
     * categorias
     * @return
     * Verdadero si se agregaron las
     * categorias, falso en caso
     * contrario
     */
    public boolean insertarCategoria(java.util.List<com.develcom.administracion.Perfil> categorias) throws SOAPException, SOAPFaultException{
        traza.info("agregando categorias "+categorias);
        return agregarCategoria(categorias);
    }

    /**
     * Agrega subcategorias
     * a la base de datos
     * @param subCategorias
     * Lista con las nuevas
     * subcategorias
     * @return
     * Verdadero si se agregaron las
     * subcategorias, falso en caso
     * contrario
     */
    public boolean insertarSubCategoria(java.util.List<com.develcom.administracion.Perfil> subCategorias) throws SOAPException, SOAPFaultException{
        traza.info("agregando subCategorias "+subCategorias);
        return agregarSubCategoria(subCategorias);
    }

    /**
     * Agrega tipos de documentos
     * a la base de datos
     * @param tipodocumentos
     * Lista con los nuevos
     * tipos de documentos
     * @return
     * Verdadero si se agregaron los
     * tipos de documentos,
     * falso en caso contrario
     */
    public boolean insertaTipoDocumento(java.util.List<com.develcom.administracion.Perfil> tipodocumentos) throws SOAPException, SOAPFaultException{
        traza.info("agregando tipodocumentos "+tipodocumentos);
        return agregarTipoDocumento(tipodocumentos);
    }

    /**
     * Agrega argumentos
     * (indeces dinamicos)
     * a la base de datos
     * @param argumentos
     * Lista con los nuevos
     * argumentos
     * @return
     * Verdadero si se agregaron los
     * argumentos, falso en
     * caso contrario
     */
    public boolean guardarIndices(java.util.List<com.develcom.administracion.Indice> argumentos) throws SOAPException, SOAPFaultException {
        traza.info("agregando argumentos "+argumentos);
        return agregarIndices(argumentos);
    }

    /**
     * Agrega datos a las diferentes
     * listas desplegables de los
     * indices dinamicos
     * @param listaDesplegable
     * Listado con los datos de la
     * lista desplegable seleccionada
     * @param bandera
     * Variable para indicar si los valores son
     * del expediente o del dato adicional
     * @return
     * Verdadero si se agregaron los
     * datos, falso en
     * caso contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean agregandoListaDesplegables(java.util.List<com.develcom.administracion.Combo> listaDesplegable, boolean bandera) throws SOAPException, SOAPFaultException {
        traza.info("agregando listaDesplegable "+listaDesplegable);
        return agregarListaDesplegables(listaDesplegable, bandera);
    }
    
    /**
     * Agrega los datos adicionales de
     * un tipo de documento
     * @param indiceDatos
     * Listado de los datos adicionales
     * @return 
     * Verdadero si se agrego con exito,
     * falso en caso contrario
     * @throws javax.xml.soap.SOAPException
     */
    public boolean insertarIndiceDatoAdicional(java.util.List<com.develcom.administracion.DatoAdicional> indiceDatos)  throws SOAPException, SOAPFaultException {
        return agregarIndiceDatoAdicional(indiceDatos);
    }

    private static boolean agregarPerfil(java.util.List<com.develcom.administracion.Perfil> perfil) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
//        com.develcom.administracion.AdministracionAgregar_Service service = new com.develcom.administracion.AdministracionAgregar_Service();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarPerfil(perfil);
    }

    private static boolean agregarLibreria(java.util.List<com.develcom.administracion.Libreria> librerias) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
        //com.develcom.administracion.AdministracionAgregarService service = new com.develcom.administracion.AdministracionAgregarService();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarLibreria(librerias);
    }

    private static boolean agregarCategoria(java.util.List<com.develcom.administracion.Perfil> categorias) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
        //com.develcom.administracion.AdministracionAgregarService service = new com.develcom.administracion.AdministracionAgregarService();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarCategoria(categorias);
    }

    private static boolean agregarSubCategoria(java.util.List<com.develcom.administracion.Perfil> subCategorias) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
        //com.develcom.administracion.AdministracionAgregarService service = new com.develcom.administracion.AdministracionAgregarService();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarSubCategoria(subCategorias);
    }

    private static boolean agregarTipoDocumento(java.util.List<com.develcom.administracion.Perfil> tipodocumentos) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
        //com.develcom.administracion.AdministracionAgregarService service = new com.develcom.administracion.AdministracionAgregarService();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarTipoDocumento(tipodocumentos);
    }

    private static boolean agregarIndiceDatoAdicional(java.util.List<com.develcom.administracion.DatoAdicional> indiceDatos) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
//        com.develcom.administracion.AdministracionAgregar_Service service = new com.develcom.administracion.AdministracionAgregar_Service();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarIndiceDatoAdicional(indiceDatos);
    }

    private static boolean agregarListaDesplegables(java.util.List<com.develcom.administracion.Combo> listaDesplegable, boolean bandera) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
//        com.develcom.administracion.AdministracionAgregar_Service service = new com.develcom.administracion.AdministracionAgregar_Service();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarListaDesplegables(listaDesplegable, bandera);
    }

    private static boolean agregarIndices(java.util.List<com.develcom.administracion.Indice> argumentos) {
        ve.com.develcom.servicios.AdministracionAgregarServicio service = new ve.com.develcom.servicios.AdministracionAgregarServicio();
        com.develcom.administracion.AdministracionAgregar port = service.getAdministracionAgregarPort();
        return port.agregarIndices(argumentos);
    }
    
}
