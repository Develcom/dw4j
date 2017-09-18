/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.administracion;

import com.develcom.dao.Indices;
import com.develcom.dao.Categoria;
import com.develcom.dao.Combo;
import com.develcom.dao.Configuracion;
import com.develcom.dao.DatoAdicional;
import com.develcom.dao.Fabrica;
import com.develcom.dao.Indice;
import com.develcom.dao.Libreria;
import com.develcom.dao.Rol;
import com.develcom.dao.Sesion;
import com.develcom.dao.SubCategoria;
import com.develcom.dao.TipoDocumento;
import com.develcom.dao.administrar.Perfil;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.mantenimiento.Mantenimientos;
import com.develcom.tools.BaseDato;
import com.develcom.tools.ldap.ActiveDirectory;
import com.develcom.tools.ldap.UsuarioLDAP;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.NamingException;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "AdministracionBusquedas")
public class AdministracionBusquedas {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(AdministracionBusquedas.class);

    /**
     * Busca Categorias
     *
     * @param categoria Nombre de la Categoria
     * @param idLibreria El id de la Libreria
     * @param idCategoria
     * @return Lista de Categorias
     */
    @WebMethod(operationName = "buscarCategorias")
    public List<Categoria> buscarCategorias(@WebParam(name = "categoria") String categoria, @WebParam(name = "idLibreria") int idLibreria, @WebParam(name = "idCategoria") int idCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Categoria> categorias = new ArrayList<>();
        Categoria cat;
        ResultSet rsCat;

        traza.trace("idLibreria " + idLibreria, Level.INFO);
        traza.trace("Categoria " + categoria, Level.INFO);
        traza.trace("idCategoria " + idCategoria, Level.INFO);

        try {

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_categoria( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idCategoria);
            stored.setInt(3, idLibreria);
            stored.setString(4, categoria);
            stored.execute();

            rsCat = (ResultSet) stored.getObject(1);

            while (rsCat.next()) {
                cat = new Categoria();

                cat.setIdCategoria(rsCat.getInt("ID_CATEGORIA"));
                cat.setCategoria(rsCat.getString("CATEGORIA"));
                cat.setEstatus(rsCat.getString("ESTATUS"));
                traza.trace("categoria encontrada " + cat.getCategoria(), Level.INFO);
                traza.trace("estatus categoria " + cat.getEstatus(), Level.INFO);
                categorias.add(cat);
            }

        } catch (SQLException ex) {
            traza.trace("Problemas al buscar las libreria", Level.INFO, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de categoria buscadas " + categorias.size(), Level.INFO);
        return categorias;
    }

    /**
     * Busca SubCategorias
     *
     * @param subCategoria Nombre de la SubCategoria
     * @param idCategoria El id de la Categoria
     * @param idSubCategoria El id de la SubCategoria
     * @return Lista de SubCategorias
     */
    @WebMethod(operationName = "buscarSubCategorias")
    public List<SubCategoria> buscarSubCategorias(@WebParam(name = "subCategoria") String subCategoria, @WebParam(name = "idCategoria") int idCategoria, @WebParam(name = "idSubCategoria") int idSubCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<SubCategoria> subCategorias = new ArrayList<>();
        SubCategoria subCat;
        ResultSet rsSubCat;

        traza.trace("SubCategoria " + subCategoria, Level.INFO);
        traza.trace("idCategoria " + idCategoria, Level.INFO);
        traza.trace("idSubCategoria " + idSubCategoria, Level.INFO);

        try {

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_subcategoria( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idCategoria);
            stored.setInt(3, idSubCategoria);
            stored.setString(4, subCategoria);
            stored.execute();

            rsSubCat = (ResultSet) stored.getObject(1);

            while (rsSubCat.next()) {
                subCat = new SubCategoria();

                subCat.setIdSubCategoria(rsSubCat.getInt("ID_SUBCATEGORIA"));
                subCat.setIdCategoria(rsSubCat.getInt("ID_CATEGORIA"));
                subCat.setSubCategoria(rsSubCat.getString("SUBCATEGORIA"));
                subCat.setEstatus(rsSubCat.getString("ESTATUS"));

                subCategorias.add(subCat);
            }

        } catch (SQLException ex) {
            traza.trace("Problemas al buscar las libreria", Level.INFO, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de subCategoria buscadas " + subCategorias.size(), Level.INFO);
        return subCategorias;
    }

    /**
     * Busca Tipos de Documentos
     *
     * @param tipoDocumento Nombre del Tipo de Documento
     * @param idCategoria El id de la Categoria
     * @param idSubCategoria El id de la SubCategoria
     * @return Lista de Tipos de Documentos
     */
    @WebMethod(operationName = "buscarTiposDocumentos")
    public List<TipoDocumento> buscarTiposDocumentos(@WebParam(name = "tipoDocumento") String tipoDocumento, @WebParam(name = "idCategoria") int idCategoria, @WebParam(name = "idSubCategoria") int idSubCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        TipoDocumento tipoDoc;
        ResultSet rsTipoDoc;

        traza.trace("tipo de Documento " + tipoDocumento, Level.INFO);
        traza.trace("idCategoria " + idCategoria, Level.INFO);
        traza.trace("idSubCategoria " + idSubCategoria, Level.INFO);

        try {

            try {
                if (tipoDocumento.equalsIgnoreCase("")) {
                    tipoDocumento = null;
                }
            } catch (NullPointerException e) {
            }

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_tipo_documento( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idCategoria);
            stored.setInt(3, idSubCategoria);
            stored.setString(4, tipoDocumento);
            stored.execute();

            rsTipoDoc = (ResultSet) stored.getObject(1);

            while (rsTipoDoc.next()) {
                tipoDoc = new TipoDocumento();

                tipoDoc.setIdTipoDocumento(rsTipoDoc.getInt("ID_DOCUMENTO"));
                tipoDoc.setIdSubCategoria(rsTipoDoc.getInt("ID_SUBCATEGORIA"));
                tipoDoc.setIdCategoria(rsTipoDoc.getInt("ID_CATEGORIA"));
                tipoDoc.setTipoDocumento(rsTipoDoc.getString("TIPO_DOCUMENTO"));
                tipoDoc.setVencimiento(rsTipoDoc.getString("VENCIMIENTO"));
                tipoDoc.setDatoAdicional(rsTipoDoc.getString("DATO_ADICIONAL"));
                tipoDoc.setFicha(rsTipoDoc.getString("FICHA"));
                tipoDoc.setEstatus(rsTipoDoc.getString("ESTATUS"));

                tipoDocumentos.add(tipoDoc);
            }

        } catch (SQLException ex) {
            traza.trace("Problemas al buscar las libreria", Level.INFO, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de los tipos de documentos buscados " + tipoDocumentos.size(), Level.INFO);
        return tipoDocumentos;
    }

    /**
     * Busca los Indices
     *
     * @param idCategoria El id de la Categoria
     * @return Lista de Indices
     */
    @WebMethod(operationName = "buscarIndices")
    public List<Indice> buscarIndices(@WebParam(name = "idCategoria") int idCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Indice> indices = new ArrayList<>();
        Indice indice;
        ResultSet rsArg;

        traza.trace("buscando los indices del id categoria " + idCategoria, Level.INFO);
        try {
            stored = bd.conectar().prepareCall("{ ? = call f_buscar_indices( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idCategoria);
            stored.execute();

            rsArg = (ResultSet) stored.getObject(1);

            while (rsArg.next()) {
                indice = new Indice();

                String clave = rsArg.getString("CLAVE");

                indice.setIdIndice(rsArg.getInt("ID_INDICE"));
                indice.setIdCategoria(rsArg.getInt("ID_CATEGORIA"));
                indice.setIndice(rsArg.getString("INDICE"));
                indice.setTipo(rsArg.getString("TIPO"));
                indice.setCodigo(rsArg.getInt("CODIGO"));
                if (clave != null) {
                    indice.setClave(clave);
                } else {
                    indice.setClave("");
                }
                traza.trace("indice encontrado " + indice.getIndice(), Level.INFO);

                indices.add(indice);
            }

        } catch (SQLException ex) {
            traza.trace("Problemas al buscar los argumentos", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la lista de indices " + indices.size(), Level.INFO);
        return indices;
    }

    /**
     * busca las Librerias con sus Categorias para la configuracion del usuario
     *
     * @return Una lista con las librerias y sus categorias y los roles
     */
    @WebMethod(operationName = "buscarLibreriasCategorias")
    public List<Perfil> buscarLibreriasCategorias() {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Perfil> libreriasCategorias = new ArrayList<>();
        Libreria libreria;
        Categoria categoria;
        Rol rol;
        Perfil perfil;
        ResultSet rslibCat;
        try {

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_libreriascategorias( ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.execute();

            rslibCat = (ResultSet) stored.getObject(1);

            while (rslibCat.next()) {

                libreria = new Libreria();
                categoria = new Categoria();
                perfil = new Perfil();
                rol = new Rol();

                libreria.setIdLibreria(rslibCat.getInt("ID_LIBRERIA"));
                libreria.setDescripcion(rslibCat.getString("descLibreria"));
                libreria.setEstatus(rslibCat.getString("desEstatus"));

                categoria.setIdCategoria(rslibCat.getInt("ID_CATEGORIA"));
                categoria.setCategoria(rslibCat.getString("descCategoria"));
                categoria.setEstatus(rslibCat.getString("desEstatus"));

                rol.setRol(rslibCat.getString("descRol"));
                rol.setIdRol(rslibCat.getInt("ID_ROL"));

                perfil.setLibreria(libreria);
                perfil.setCategoria(categoria);
                perfil.setRol(rol);

                libreriasCategorias.add(perfil);

                traza.trace("libreria " + perfil.getLibreria().getDescripcion(), Level.INFO);
                traza.trace("categoria " + perfil.getCategoria().getCategoria(), Level.INFO);
                traza.trace("rol " + perfil.getRol().getRol(), Level.INFO);

            }

        } catch (SQLException ex) {
            traza.trace("Error al consultar las librerias con sus categorias", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos en consultar las librerias con sus categorias", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño del objeto libreriasCategorias: " + libreriasCategorias.size(), Level.INFO);

        return libreriasCategorias;
    }

    /**
     * Verifica que el usuario exite en base de datos y ldap para su
     * configuración
     *
     * @param login Indetificador del usuario
     * @return Un objeto con la respuesta según sea el caso de exito si el
     * usuario se encuentra en la base de datos y en el directorio activo; o si
     * el usuario no se encuentra en la base de datos o no esta registrado en el
     * directorio activo
     */
    @WebMethod(operationName = "comprobarUsuario")
    public Sesion comprobarUsuario(@WebParam(name = "login") String login) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        Sesion resp = new Sesion();
        Fabrica fabrica = new Fabrica();
        ResultSet rsLogin;
        UsuarioLDAP userLDAP;
        String userStatus;
        ActiveDirectory ad = new ActiveDirectory();
        Configuracion conf = new Configuracion();

        traza.trace("comprobando el usuario: " + login, Level.INFO);

        try {

            if (conf.isLdap()) {
                userLDAP = ad.buscarDatosUsuario(login);
                if (userLDAP != null) {

                    if ((userLDAP.getLogin().equalsIgnoreCase(login))) {

                        stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, login);
                        stored.execute();

                        rsLogin = (ResultSet) stored.getObject(1);

                        if (rsLogin.next()) {

                            traza.trace("exito", Level.INFO);
                            resp.setVerificar("exito");
                            resp.setRespuesta("usuario: " + login + " ya existe en basedato (WS)");
                            userStatus = rsLogin.getString("ESTATUS");
                            traza.trace("estatus del usuario " + userStatus, Level.INFO);
                            resp.setEstatusUsuario(userStatus);
                            resp.setIdUsuario(rsLogin.getString("ID_USUARIO"));
                            fabrica.setPertenece(Boolean.valueOf(rsLogin.getString("fabrica")));
                            resp.setFabrica(fabrica);
                            rsLogin.close();

                            stored = bd.conectar().prepareCall(" { ? = call f_buscar_fabrica( ? ) } ");
                            stored.registerOutParameter(1, Types.OTHER);
                            stored.setString(2, login);
                            stored.execute();

                            rsLogin = (ResultSet) stored.getObject(1);
                            if (rsLogin.next()) {
                                fabrica.setPertenece(Boolean.valueOf(rsLogin.getString("fabrica")));
                                resp.setFabrica(fabrica);
                                rsLogin.close();
                            }

                        } else {
                            traza.trace("usuario " + login + " no encontrado en basedato", Level.INFO);
                            resp.setRespuesta("<html>Usuario " + login + " no <br/>registrado.");
                            resp.setVerificar("basedato");
                        }

                    } else {
                        String u = userLDAP.getLogin();
                        traza.trace("usuario LDAP " + userLDAP.getLogin(), Level.INFO);
                        if (!u.equalsIgnoreCase(login)) {
                            traza.trace("usuario " + login + " no encontrado en ldap", Level.INFO);
                            resp.setRespuesta("usuario: " + login + " no encontrado en ldap (WS)");
                            resp.setVerificar("ldap");

                        }
                    }
                } else {
                    traza.trace("error generado en la busqueda del usuario: " + login + " en LDAP ", Level.INFO);
                    resp.setRespuesta("Problemas en la busqueda del usuario: " + login + " en LDAP ");
                    resp.setVerificar("Problemas en la busqueda del usuario: " + login + " en LDAP");
                    throw new DW4JServiciosException("<html>Problemas en la busqueda del </br>susuario: " + login + " en LDAP</html>");
                }
            } else {

                stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, login);
                stored.execute();

                rsLogin = (ResultSet) stored.getObject(1);

                if (rsLogin.next()) {

                    traza.trace("exito", Level.INFO);
                    resp.setVerificar("exito");
                    resp.setRespuesta("usuario: " + login + " ya existe en basedato (WS)");
                    userStatus = rsLogin.getString("ESTATUS");
                    traza.trace("estatus del usuario " + userStatus, Level.INFO);
                    resp.setEstatusUsuario(userStatus);
                    resp.setIdUsuario(rsLogin.getString("ID_USUARIO"));
                    fabrica.setPertenece(Boolean.valueOf(rsLogin.getString("fabrica")));
                    resp.setFabrica(fabrica);
                    rsLogin.close();

                    stored = bd.conectar().prepareCall(" { ? = call f_buscar_fabrica( ? ) } ");
                    stored.registerOutParameter(1, Types.OTHER);
                    stored.setString(2, login);
                    stored.execute();

                    rsLogin = (ResultSet) stored.getObject(1);
                    if (rsLogin.next()) {
                        fabrica.setPertenece(Boolean.valueOf(rsLogin.getString("fabrica")));
                        resp.setFabrica(fabrica);
                        rsLogin.close();
                    }

                } else {
                    traza.trace("usuario " + login + " no encontrado en basedato", Level.INFO);
                    resp.setRespuesta("<html>Usuario " + login + " no <br/>registrado.");
                    resp.setVerificar("basedato");
                }
            }

        } catch (DW4JServiciosException ex) {
            traza.trace("Error en la consulta del usuario " + login, Level.ERROR, ex);
            resp.setRespuesta(ex.getMessage());
            resp.setVerificar("ldap");
        } catch (SQLException | NamingException ex) {
            traza.trace("Error al buscar el usuario " + login, Level.ERROR, ex);
            resp.setVerificar("Error al buscar el usuario " + login + " " + ex.getMessage());
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        return resp;
    }

    /**
     * Busca el actual perfil del usuario
     *
     * @param user Identificador del usuario
     * @return un objeto con el perfil del usuario
     */
    @WebMethod(operationName = "buscarPerfil")
    public List<Sesion> buscarPerfil(@WebParam(name = "user") String user) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Sesion> sesion = new ArrayList<>();
        Sesion sess;
        Libreria libreria;
        Fabrica fabrica;
        Categoria categoria;
        Rol rol;
        ResultSet rsSesion;
        Configuracion config;

        try {

            config = new Mantenimientos().buscarMantenimiento();

            if (config != null) {

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_perfil( ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, user);
                stored.execute();

                rsSesion = (ResultSet) stored.getObject(1);

                while (rsSesion.next()) {
                    sess = new Sesion();
                    libreria = new Libreria();
                    categoria = new Categoria();
                    rol = new Rol();

                    rol.setRol(rsSesion.getString("rol"));
                    sess.setIdUsuario(rsSesion.getString("id_usuario"));
                    sess.setRolUsuario(rol);
//                        sess.setEstatusUsuario(rsSesion.getString("estatus_usuario").trim());

                    if (rsSesion.getString("libreria") != null) {
                        Integer idLib = rsSesion.getInt("id_libreria");
                        libreria.setIdLibreria(idLib);
                        libreria.setDescripcion(rsSesion.getString("libreria").trim());
//                            libreria.setEstatus(rsSesion.getString("estatus_lib").trim());
                        sess.setLibreria(libreria);
                    }

                    if (rsSesion.getString("categoria") != null) {
                        categoria.setIdCategoria(rsSesion.getInt("id_categoria"));
                        categoria.setCategoria(rsSesion.getString("categoria").trim());
//                            categoria.setEstatus(rsSesion.getString("estatus_cat").trim());
                        sess.setCategoria(categoria);
                    }

                    if (config.isFabrica()) {

                        fabrica = new Fabrica();

                        fabrica.setUsuario(rsSesion.getString("id_usuario"));
                        int flag = rsSesion.getInt("pertenece");
                        if (flag == 1) {
                            fabrica.setPertenece(true);
                        } else {
                            fabrica.setPertenece(false);
                        }
//                            fabrica.setPertenece(rsSesion.getBoolean("pertenece"));
                        sess.setFabrica(fabrica);
                    }

                    sesion.add(sess);

                }
            } else {
                throw new DW4JServiciosException("\nproblemas al cargar la configuracion desde AdministracionBusqueda-->>buscarPerfil");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
        } catch (SQLException ex) {
            traza.trace("Error en la consulta a la base de datos", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de la sesion del usuario " + user + " - " + sesion.size(), Level.INFO);
        return sesion;
    }

    /**
     * Busca solo Librerias
     *
     * @param libreria Nombre de la Libreria
     * @param idLibreria Identificador de la Libreria
     * @return Lista de Librerias
     */
    @WebMethod(operationName = "buscarLibrerias")
    public List<Libreria> buscarLibrerias(@WebParam(name = "libreria") String libreria, @WebParam(name = "idLibreria") int idLibreria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Libreria> librerias = new ArrayList<>();
        Libreria lib;
        ResultSet rsLib;

        try {
            traza.trace("libreria " + libreria, Level.INFO);
            traza.trace("idLibreria " + idLibreria, Level.INFO);

            if (libreria.equalsIgnoreCase("")) {
                libreria = null;
            }

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_libreria( ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idLibreria);
            stored.setString(3, libreria);
            stored.execute();

            rsLib = (ResultSet) stored.getObject(1);

            while (rsLib.next()) {
                lib = new Libreria();

                lib.setIdLibreria(rsLib.getInt("ID_LIBRERIA"));
                lib.setDescripcion(rsLib.getString("LIBRERIA"));
                lib.setEstatus(rsLib.getString("status"));

                librerias.add(lib);
            }

        } catch (SQLException ex) {
            traza.trace("Problemas al buscar las libreria", Level.INFO, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de librerias buscadas " + librerias.size(), Level.INFO);
        return librerias;
    }

    /**
     * Busca datos de la lista desplegable
     *
     * @param idArgumento Codigo del indice
     * @param bandera Bandera que indica si se busca los valores para el
     * expediente o para los datos adicionales
     * @return Lista de datos
     */
    @WebMethod(operationName = "buscarDatosCombo")
    public List<Combo> buscarDatosCombo(@WebParam(name = "idArgumento") int idArgumento,
            @WebParam(name = "bandera") boolean bandera) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Combo> combos = new ArrayList<>();
        Combo combo;
        ResultSet rsDatos;

        traza.trace("id del argumento " + idArgumento, Level.INFO);
        traza.trace("bandera " + bandera, Level.INFO);

        try {

            if (bandera) {

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_valor_datoadicional( ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setInt(2, idArgumento);
                stored.execute();

                rsDatos = (ResultSet) stored.getObject(1);

                while (rsDatos.next()) {
                    combo = new Combo();

                    combo.setIndice(rsDatos.getString("indice_adicional"));
                    combo.setCodigoIndice(rsDatos.getInt("CODIGO_INDICE"));
                    combo.setDatoCombo(rsDatos.getString("DESCRIPCION"));
                    combo.setIdCombo(rsDatos.getInt("ID_LISTA"));

                    combos.add(combo);
                }

            } else {

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_datos_combo( ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setInt(2, idArgumento);
                stored.execute();

                rsDatos = (ResultSet) stored.getObject(1);

                while (rsDatos.next()) {
                    combo = new Combo();

                    combo.setIndice(rsDatos.getString("INDICE"));
                    combo.setCodigoIndice(rsDatos.getInt("CODIGO_INDICE"));
                    combo.setDatoCombo(rsDatos.getString("DESCRIPCION"));
                    combo.setIdCombo(rsDatos.getInt("ID_LISTA"));

                    combos.add(combo);
                }
            }

        } catch (SQLException ex) {
            traza.trace("problemas al buscar los datos del indice", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la lista desplegable " + combos.size(), Level.INFO);
        return combos;
    }

    /**
     * Busca los indices de los datos adicionlaes de un tipo de documento
     *
     * @param idTipoDocumento Identificador del tipo de documento
     * @return
     */
    @WebMethod(operationName = "buscarIndiceDatoAdicional")
    public List<com.develcom.dao.DatoAdicional> buscarIndiceDatoAdicional(
            @WebParam(name = "idTipoDocumento") int idTipoDocumento) {
//    public List<com.develcom.dao.DatoAdicional> buscarIndiceDatoAdicional(
//                                   @WebParam(name = "idTipoDocumento") int idTipoDocumento,
//                                   @WebParam(name = "numero") int numero,
//                                   @WebParam(name = "version") int version) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<DatoAdicional> lsDatoAdicional = new ArrayList<>();
        DatoAdicional da;
        ResultSet rsDA;

        traza.trace("buscando los indice de los datos adicional del id tipo documento " + idTipoDocumento, Level.INFO);

        try {
            stored = bd.conectar().prepareCall("{ ? = call f_buscar_indice_datosadicional( ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idTipoDocumento);
//            stored.setInt(2, numero);
//            stored.setInt(2, version);
            stored.execute();

            rsDA = (ResultSet) stored.getObject(1);

            while (rsDA.next()) {
                da = new DatoAdicional();

                da.setIdDatoAdicional(rsDA.getInt("ID_DATO_ADICIONAL"));
                da.setIdTipoDocumento(rsDA.getInt("ID_DOCUMENTO"));
                da.setIndiceDatoAdicional(rsDA.getString("INDICE_ADICIONAL"));
                da.setTipo(rsDA.getString("tipo"));
                da.setCodigo(rsDA.getInt("codigo"));

                traza.trace("dato adicional " + da.getIndiceDatoAdicional(), Level.INFO);

                lsDatoAdicional.add(da);

            }

        } catch (SQLException ex) {
            traza.trace("problemas al buscar los indices del dato adicional", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la lista de dato adicional " + lsDatoAdicional.size(), Level.INFO);

        return lsDatoAdicional;
    }
}
