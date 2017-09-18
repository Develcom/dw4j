/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.autentica;

import biz.source_code.base64Coder.Base64Coder;
import com.develcom.dao.Categoria;
import com.develcom.dao.Configuracion;
import com.develcom.dao.Indice;
import com.develcom.dao.Libreria;
import com.develcom.dao.Rol;
import com.develcom.dao.Sesion;
import com.develcom.dao.Usuario;
import com.develcom.dao.administrar.Perfil;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.mantenimiento.Mantenimientos;
import com.develcom.tools.BaseDato;
import com.develcom.tools.Utilitario;
import com.develcom.tools.UtilitarioFecha;
import com.develcom.tools.ldap.ActiveDirectory;
import com.develcom.tools.ldap.UsuarioLDAP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "Autentica")
public class Autentica {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(Autentica.class);

    /**
     * Crea una sesion para el usuarios que ingreso al sistema DW4J cliente
     * agregando la configuracion necesario
     *
     * @param usuario Un usuario registrado en ldpa y DW4J
     * @return Una lista con la configuracion del DW4J cliente y del usuario
     */
    @WebMethod(operationName = "crearSesion")
    public List<Sesion> crearSesion(@WebParam(name = "usuario") String usuario) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Sesion> sesion = new ArrayList<>();
        Sesion sess;
        Rol rol;
        String idSesion, horaFecha;
        int registro = 1;
        ResultSet rsSesion;

        try {

            traza.trace("creando la sesion", Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_crear_sesion( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, usuario);
            stored.execute();

            rsSesion = (ResultSet) stored.getObject(1);
            traza.trace("rsSesion " + rsSesion, Level.INFO);

            idSesion = buscarIdSesion();
            horaFecha = new UtilitarioFecha().convertLongDate(System.currentTimeMillis());
            traza.trace("id de la sesion (WS) " + idSesion + " del usuario " + usuario + " fecha hora " + horaFecha, Level.INFO);

            while (rsSesion.next()) {
                traza.trace("registro de sesion " + registro, Level.INFO);
                sess = new Sesion();
                rol = new Rol();

                rol.setRol(rsSesion.getString("desc_rol"));
                sess.setIdSession(idSesion);
                sess.setIdUsuario(rsSesion.getString("id_user"));
                sess.setRolUsuario(rol);
                sess.setEstatusUsuario(rsSesion.getString("estatus_usuario").trim());
                sess.setFechaHora(horaFecha);

                registro++;
                sesion.add(sess);

            }

        } catch (SQLException ex) {
            traza.trace("problemas en crear la sesion del usuario " + usuario, Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("el objeto sesion " + sesion, Level.INFO);
        return sesion;
    }

    /**
     * Verifica si el usuario exite en la base de datos y ldap
     *
     * @param login Usuario
     * @param password Contraseña
     * @return Respuesta de la verificación
     */
    @WebMethod(operationName = "verificarUsuario")
    public Sesion verificarUsuario(@WebParam(name = "login") String login, @WebParam(name = "password") String password) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        Sesion resp = new Sesion();
        Configuracion config = new Configuracion(), conf;
        ResultSet rsLogin;
        UsuarioLDAP userLDAP;
        int flag;
        traza.trace("verificando al usuario: " + login, Level.INFO);
        try {

            conf = new Mantenimientos().buscarMantenimiento();

            if (login.equalsIgnoreCase("dw4jconf") || login.equalsIgnoreCase("dw4jdemo")) {

                password = Base64Coder.encodeString(password);

                stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, login);
                stored.execute();

                rsLogin = (ResultSet) stored.getObject(1);

                if (rsLogin.next()) {
                    if (rsLogin.getString("passUser").equalsIgnoreCase(password)) {
                        config.setCalidadActivo(rsLogin.getBoolean("CALIDAD"));
                        config.setFileCode(rsLogin.getString("ARCHIVO_COD"));
                        config.setFileTif(rsLogin.getString("ARCHIVO_TIF"));
                        config.setPathTmp(rsLogin.getString("RUTA_TEMPORAL"));
                        config.setFoliatura(rsLogin.getBoolean("FOLIATURA"));
                        config.setServerName(rsLogin.getString("SERVER_NAME"));
                        config.setDatabaseName(rsLogin.getString("DATABASE_NAME"));
                        config.setPort(rsLogin.getInt("PORT"));
                        config.setUser(rsLogin.getString("USERBD"));
                        config.setPassword(rsLogin.getString("PASSWORD"));

                        flag = rsLogin.getInt("FICHA");
                        if (flag == 1) {
                            config.setFicha(true);
                        } else {
                            config.setFicha(false);
                        }

                        flag = rsLogin.getInt("FABRICA");
                        if (flag == 1) {
                            config.setFabrica(true);
                        } else {
                            config.setFabrica(false);
                        }

                        flag = rsLogin.getInt("ELIMINA");
                        if (flag == 1) {
                            config.setElimina(true);
                        } else {
                            config.setElimina(true);
                        }

                        flag = rsLogin.getInt("LDAP");
                        if (flag == 1) {
                            config.setLdap(true);
                        } else {
                            config.setLdap(true);
                        }
                        resp.setConfiguracion(config);
                        resp.setVerificar("exito");
                        traza.trace("exito usuario en base de datos", Level.INFO);
                        rsLogin.close();
                    } else {
                        traza.trace("la contraseña de usuario " + login + " es incorrecta", Level.INFO);
                        resp.setRespuesta("la contraseña de usuario " + login + " es incorrecta (WS)");
                        resp.setVerificar("basedato");
                    }
                }

            } else if (conf.isLdap()) {

                userLDAP = new ActiveDirectory().comprobarUsuario(login, password);

                traza.trace("objeto del usuario ldap " + userLDAP, Level.INFO);

                if (userLDAP != null) {

                    if ((userLDAP.getLogin().equalsIgnoreCase(login)) && (userLDAP.isPass())) {

                        stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, login);
                        stored.execute();

                        rsLogin = (ResultSet) stored.getObject(1);

                        if (rsLogin.next()) {
                            if (rsLogin.getInt("ID_ESTATUS") == 1) {
                                config.setCalidadActivo(rsLogin.getBoolean("CALIDAD"));
                                config.setFileCode(rsLogin.getString("ARCHIVO_COD"));
                                config.setFileTif(rsLogin.getString("ARCHIVO_TIF"));
                                config.setPathTmp(rsLogin.getString("RUTA_TEMPORAL"));
                                config.setFoliatura(rsLogin.getBoolean("FOLIATURA"));
                                config.setServerName(rsLogin.getString("SERVER_NAME"));
                                config.setDatabaseName(rsLogin.getString("DATABASE_NAME"));
                                config.setPort(rsLogin.getInt("PORT"));
                                config.setUser(rsLogin.getString("USERBD"));
                                config.setPassword(rsLogin.getString("PASSWORD"));

                                flag = rsLogin.getInt("FICHA");
                                if (flag == 1) {
                                    config.setFicha(true);
                                } else {
                                    config.setFicha(false);
                                }

                                flag = rsLogin.getInt("FABRICA");
                                if (flag == 1) {
                                    config.setFabrica(true);
                                } else {
                                    config.setFabrica(false);
                                }

                                flag = rsLogin.getInt("ELIMINA");
                                if (flag == 1) {
                                    config.setElimina(true);
                                } else {
                                    config.setElimina(true);
                                }

                                flag = rsLogin.getInt("LDAP");
                                if (flag == 1) {
                                    config.setLdap(true);
                                } else {
                                    config.setLdap(true);
                                }

                                resp.setConfiguracion(config);
                                resp.setVerificar("exito");
                                traza.trace("exito", Level.INFO);
                                rsLogin.close();
                            } else {
                                traza.trace("usuario " + login + " esta inactivo", Level.INFO);
                                resp.setRespuesta("usuario: " + login + " esta inactivo (WS)");
                                resp.setVerificar("inactivo");
                            }
                        } else {
                            traza.trace("usuario " + login + " no encontrado en basedato", Level.INFO);
                            resp.setRespuesta("usuario: " + login + " no encontrado en basedato (WS)");
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
                        if (!userLDAP.isPass()) {
                            traza.trace("la contraseña de usuario " + login + " es incorrecta", Level.INFO);
                            resp.setRespuesta("la contraseña de usuario " + login + " es incorrecta (WS)");
                            resp.setVerificar("ldap");
                        }
                    }
                } else {
                    traza.trace("error generado en la busqueda del usuario: " + login + " en LDAP ", Level.INFO);
                    resp.setRespuesta("error generado en la busqueda del usuario: " + login + " en LDAP ");
                    resp.setVerificar("error generado en la busqueda del usuario: " + login + " en LDAP");
                    throw new DW4JServiciosException("Usuario o Contraseña incorrectos\nintente nuevamente o");
                }
            } else {

                stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, login);
                stored.execute();

                rsLogin = (ResultSet) stored.getObject(1);

                if (rsLogin.next()) {
                    if (rsLogin.getInt("ID_ESTATUS") == 1) {
                        config.setCalidadActivo(rsLogin.getBoolean("CALIDAD"));
                        config.setFileCode(rsLogin.getString("ARCHIVO_COD"));
                        config.setFileTif(rsLogin.getString("ARCHIVO_TIF"));
                        config.setPathTmp(rsLogin.getString("RUTA_TEMPORAL"));
                        config.setFoliatura(rsLogin.getBoolean("FOLIATURA"));
                        config.setServerName(rsLogin.getString("SERVER_NAME"));
                        config.setDatabaseName(rsLogin.getString("DATABASE_NAME"));
                        config.setPort(rsLogin.getInt("PORT"));
                        config.setUser(rsLogin.getString("USERBD"));
                        config.setPassword(rsLogin.getString("PASSWORD"));

                        flag = rsLogin.getInt("FICHA");
                        if (flag == 1) {
                            config.setFicha(true);
                        } else {
                            config.setFicha(false);
                        }

                        flag = rsLogin.getInt("FABRICA");
                        if (flag == 1) {
                            config.setFabrica(true);
                        } else {
                            config.setFabrica(false);
                        }

                        flag = rsLogin.getInt("ELIMINA");
                        if (flag == 1) {
                            config.setElimina(true);
                        } else {
                            config.setElimina(true);
                        }

                        resp.setConfiguracion(config);
                        resp.setVerificar("exito");
                        traza.trace("exito", Level.INFO);
                        rsLogin.close();
                    } else {
                        traza.trace("usuario " + login + " esta inactivo", Level.INFO);
                        resp.setRespuesta("usuario: " + login + " esta inactivo (WS)");
                        resp.setVerificar("inactivo");
                    }
                } else {
                    traza.trace("usuario " + login + " no encontrado en basedato", Level.INFO);
                    resp.setRespuesta("usuario: " + login + " no encontrado en basedato (WS)");
                    resp.setVerificar("basedato");
                }

            }

        } catch (DW4JServiciosException ex) {
            traza.trace("Problemas en la consulta del usuario " + login, Level.ERROR, ex);
            resp.setRespuesta("Problemas en la consulta del usuario " + login + "\n" + ex.getMessage());
            resp.setVerificar("ldap");
        } catch (SQLException ex) {
            traza.trace("problema al buscar el usuario " + login + " en base de dato", Level.ERROR, ex);
            resp.setRespuesta("problema al buscar el usuario " + login + " en base de dato\n" + ex.getMessage());
            resp.setVerificar("basedato");
        } catch (Exception ex) {
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
        traza.trace("respuesta al verificar el ususario " + login + " " + resp, Level.INFO);
        return resp;
    }

    /**
     * Busca la(s) libreria(s) y categoria(s) segun el perfil del usuario
     *
     * @param usuario El id del usuario
     * @param perfil El perfil del usuario
     * @return
     */
    @WebMethod(operationName = "buscarLibreriaCategoriasPerfil")
    public List<Perfil> buscarLibreriaCategoriasPerfil(@WebParam(name = "usuario") String usuario, @WebParam(name = "perfil") String perfil) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Perfil> perfiles = new ArrayList<>();
        Perfil rol;
        Libreria libreria;
        Categoria categoria;
        ResultSet rsLibCatUsPer;
        String user, lib, cat;

        traza.trace("buscando libreria y categorias del usuario " + usuario, Level.INFO);
        traza.trace("perfil para las libreria y categorias del usuario " + perfil, Level.INFO);

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_lib_cat_perfil( ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, usuario);
            stored.setString(3, perfil);
            stored.execute();

            rsLibCatUsPer = (ResultSet) stored.getObject(1);

            while (rsLibCatUsPer.next()) {
                rol = new Perfil();
                libreria = new Libreria();
                categoria = new Categoria();

                user = rsLibCatUsPer.getString("usuario");
                rol.setUsuario(user);
                traza.trace("usuario encontrado " + user, Level.INFO);

                lib = rsLibCatUsPer.getString("libreria");
                libreria.setDescripcion(lib);
                libreria.setIdLibreria(rsLibCatUsPer.getInt("ID_LIBRERIA"));
                libreria.setEstatus(rsLibCatUsPer.getString("status_lib"));
                rol.setLibreria(libreria);
                traza.trace("libreria encontrada " + lib, Level.INFO);

                cat = rsLibCatUsPer.getString("categoria");
                categoria.setCategoria(cat);
                categoria.setIdCategoria(rsLibCatUsPer.getInt("ID_CATEGORIA"));
                categoria.setEstatus(rsLibCatUsPer.getString("status_cat"));
                rol.setCategoria(categoria);
                traza.trace("categoria encontrada " + cat, Level.INFO);

                perfiles.add(rol);
            }

        } catch (SQLException ex) {
            traza.trace("error al buscar las libreria y categorias del usuario " + usuario + " con el perfil " + perfil, Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño del objecto " + perfiles.size(), Level.INFO);

        return perfiles;
    }

    /**
     * Busca la(s) libreria(s), categoria(s) e indices segun el perfil del
     * usuario
     *
     * @param usuario El id del usuario
     * @param rol El perfil del usuario
     * @return
     */
    @WebMethod(operationName = "buscarLibreriaCategoriasIndicePerfil")
    public List<Perfil> buscarLibreriaCategoriasIndicePerfil(@WebParam(name = "usuario") String usuario, @WebParam(name = "perfil") String rol) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Perfil> perfiles = new ArrayList<>();
        Perfil perfil;
        Libreria libreria;
        Categoria categoria;
        Indice indice;
        ResultSet rsLibCatUsPer;
        String user, lib, cat, ind;

        traza.trace("buscando libreria y categorias e indices del usuario " + usuario, Level.INFO);
        traza.trace("rol para las libreria y categorias del usuario " + rol, Level.INFO);

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_lib_cat_indice_perfil( ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, usuario);
            stored.setString(3, rol);
            stored.execute();

            rsLibCatUsPer = (ResultSet) stored.getObject(1);

            while (rsLibCatUsPer.next()) {

                perfil = new Perfil();
                libreria = new Libreria();
                categoria = new Categoria();
                indice = new Indice();

                user = rsLibCatUsPer.getString("usuario");
                perfil.setUsuario(user);
                traza.trace("usuario encontrado " + user, Level.INFO);

                lib = rsLibCatUsPer.getString("libreria");
                libreria.setDescripcion(lib);
                libreria.setIdLibreria(rsLibCatUsPer.getInt("ID_LIBRERIA"));
                libreria.setEstatus(rsLibCatUsPer.getString("status_lib"));
                perfil.setLibreria(libreria);
                traza.trace("libreria encontrada " + lib, Level.INFO);

                cat = rsLibCatUsPer.getString("categoria");
                categoria.setCategoria(cat);
                categoria.setIdCategoria(rsLibCatUsPer.getInt("ID_CATEGORIA"));
                categoria.setEstatus(rsLibCatUsPer.getString("status_cat"));
                perfil.setCategoria(categoria);
                traza.trace("categoria encontrada " + cat, Level.INFO);

                ind = rsLibCatUsPer.getString("indice");
                indice.setIndice(ind);
                indice.setIdIndice(rsLibCatUsPer.getInt("id_indice"));
                indice.setClave(rsLibCatUsPer.getString("clave"));
                indice.setTipo(rsLibCatUsPer.getString("tipo"));
                perfil.setIndice(indice);
                traza.trace("indice encontrado " + ind, Level.INFO);

                perfiles.add(perfil);
            }

        } catch (SQLException ex) {
            traza.trace("error al buscar las libreria y categorias del usuario " + usuario + " con el perfil " + rol, Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño del objecto " + perfiles.size(), Level.INFO);

        return perfiles;
    }

    /**
     * Genera una lista de los usuarios registrados en el Gestor Documental para
     * el autocompletar.
     *
     * @return Lista de usuarios registrados.
     */
    @WebMethod(operationName = "autocompletar")
    public List<Usuario> autocompletar() {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        Usuario usuario;
        List<Usuario> usuarios = new ArrayList<>();
        ResultSet rsBuscar;

        try {
            stored = bd.conectar().prepareCall(" { ? = call f_usuarios() } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.execute();

            rsBuscar = (ResultSet) stored.getObject(1);

            while (rsBuscar.next()) {
                if (!rsBuscar.getString("ID_USUARIO").equalsIgnoreCase("dw4jconf")) {

                    if (rsBuscar.getInt("id_estatus") == 1) {

                        usuario = new Usuario();

                        usuario.setIdUsuario(rsBuscar.getString("ID_USUARIO"));
                        usuario.setNombre(rsBuscar.getString("NOMBRE"));
                        usuario.setApellido(rsBuscar.getString("APELLIDO"));
                        usuario.setCedula(rsBuscar.getString("CEDULA"));
                        usuario.setIdEstatus(rsBuscar.getInt("id_estatus"));
                        usuario.setEstatus(rsBuscar.getString("estatus"));

                        usuarios.add(usuario);
                    }
                }
            }

        } catch (SQLException e) {
            traza.trace("problemas al buscar los usuarios de autocompletar", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la lista de autocompletar " + usuarios.size(), Level.INFO);

        return usuarios;
    }

    /**
     * Crea el id de la Sesión
     *
     * @return Id de la sesion
     */
    private String buscarIdSesion() {
        String resp = "";
        String inputLine;
        String inputText = "";
        BufferedReader in;

        try {

            URL busca = new URL("http://localhost:8080/DW4JServicios/Utilitario");

            in = new BufferedReader(new InputStreamReader(busca.openStream()));

            resp = Utilitario.getIdSesion();

            traza.trace("id de la sesion " + resp, Level.INFO);
            while ((inputLine = in.readLine()) != null) {
                inputText = inputText + inputLine;
            }

            traza.trace("El contenido de la URL es: " + inputText, Level.INFO);
            in.close();

        } catch (MalformedURLException ex) {
            traza.trace("Error generando el URL (ID Sesion)", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("Error de comunicacion (ID Sesion)", Level.ERROR, ex);
        }

        return resp;
    }
}
