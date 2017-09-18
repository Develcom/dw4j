/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.administracion;

import com.develcom.dao.Categoria;
import com.develcom.dao.Combo;
import com.develcom.dao.Configuracion;
import com.develcom.dao.DatoAdicional;
import com.develcom.dao.Fabrica;
import com.develcom.dao.Indice;
import com.develcom.dao.Libreria;
import com.develcom.dao.Rol;
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
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.NamingException;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "AdministracionAgregar")
public class AdministracionAgregar {

    private Traza traza = new Traza(AdministracionAgregar.class);

    @WebMethod(operationName = "agregarPerfil")
    public boolean agregarPerfil(@WebParam(name = "perfil") List<Perfil> perfil) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsUser;
        boolean perf = false;
        boolean fab = false;
        String cat = "";
        String lib = "";
        String ro = "";
        int cont = 0, estatus, sizeList;
        String usuario, user;
        UsuarioLDAP userLDAP;
        Libreria libreria;
        Categoria categoria;
        Configuracion config;
        Fabrica fabrica;
        boolean resp;

        try {

            this.traza.trace("tamaño del objeto perfil " + perfil.size(), Level.INFO);
            usuario = perfil.get(0).getUsuario();
            this.traza.trace("buscando el usuario " + usuario + " en ldap", Level.INFO);

            userLDAP = new ActiveDirectory().buscarDatosUsuario(usuario);
            estatus = perfil.get(0).getEstatus();
            user = userLDAP.getLogin();
            user = user.toLowerCase();

            if (userLDAP != null) {

                this.traza.trace("agregando el usuario " + user, Level.INFO);
                this.traza.trace("nombre del usuario " + userLDAP.getNombre(), Level.INFO);
                this.traza.trace("apellido del usuario " + userLDAP.getApellido(), Level.INFO);
                this.traza.trace("cedula del usuario " + userLDAP.getSexo(), Level.INFO);
                this.traza.trace("estatus del usuario " + estatus, Level.INFO);

                stored = bd.conectar().prepareCall("{ call p_agrega_usuario( ?, ?, ?, ?, ? ) }");
                stored.setString(1, user);
                stored.setString(2, userLDAP.getNombre());
                stored.setString(3, userLDAP.getApellido());
                stored.setString(4, userLDAP.getCedula());
                stored.setString(5, userLDAP.getSexo());
                stored.execute();

                bd.commit();
                bd.desconectar();
                stored = null;

                sizeList = perfil.size();

                traza.trace("tamaño de la lista del perfil " + sizeList + " del usuario " + usuario, Level.INFO);

                for (Perfil perfi : perfil) {

                    libreria = perfi.getLibreria();
                    categoria = perfi.getCategoria();
                    Rol rol = perfi.getRol();
                    try {
                        lib = libreria.getDescripcion();
                    } catch (Exception e) {
                    }
                    try {
                        cat = categoria.getCategoria();
                    } catch (Exception e) {
                    }
                    try {
                        ro = rol.getRol();
                    } catch (Exception e) {
                    }
                    this.traza.trace("usuario " + user, Level.INFO);
                    this.traza.trace("libreria " + lib, Level.INFO);
                    this.traza.trace("categoria " + cat, Level.INFO);
                    this.traza.trace("rol " + ro, Level.INFO);

                    if (stored == null) {
                        stored = bd.conectar().prepareCall("{ call p_agregar_perfil( ?, ?, ?, ? ) }");
                    }

                    try {
                        stored.setInt(1, libreria.getIdLibreria());
                    } catch (NullPointerException e) {
                        stored.setInt(1, 0);
                    }
                    try {
                        stored.setInt(2, categoria.getIdCategoria());
                    } catch (NullPointerException e) {
                        stored.setInt(2, 0);
                    }

                    stored.setString(3, user);
                    try {
                        stored.setInt(4, rol.getIdRol());
                    } catch (NullPointerException e) {
                        stored.setInt(4, 0);
                    }

                    stored.execute();
                    traza.trace("contador de registros agregados " + cont, Level.INFO);
                    cont++;
                }

                if (sizeList == cont) {
                    perf = true;
                    bd.commit();
                    bd.desconectar();
                } else {
                    perf = false;
                    bd.rollback();
                    throw new DW4JServiciosException("problemas guardando el perfil del usuario");
                }

                config = new Mantenimientos().buscarMantenimiento();

                if (config.isFabrica()) {

                    stored = bd.conectar().prepareCall("{ ? = call f_buscar_usuario_fabrica( ? ) }");
                    stored.registerOutParameter(1, Types.OTHER);
                    stored.setString(2, usuario);
                    stored.execute();

                    rsUser = (ResultSet) stored.getObject(1);

                    if (!rsUser.next()) {

                        bd.desconectar();
                        fabrica = perfil.get(0).getFabrica();

                        this.traza.trace("configurando el usuario para la cola de la fabrica", Level.INFO);
                        this.traza.trace("el usuario " + fabrica.getUsuario() + " pertenece a la fabrica " + fabrica.isPertenece(), Level.INFO);

                        stored = bd.conectar().prepareCall("{ call p_agregar_fabrica( ?, ? ) }");
                        stored.setString(1, user);
                        if (fabrica.isPertenece()) {
                            stored.setString(2, "1");
                        } else {
                            stored.setString(2, "0");
                        }
                        stored.execute();

                        bd.commit();
                        fab = true;
                        bd.desconectar();

                    } else {
                        fab = true;
                    }

                } else {
                    fab = true;
                }
                
            } else {
                traza.trace("problemas al buscar el usuario " + usuario + " en ldap", Level.ERROR);
            }
        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar el perfil del usuario", Level.ERROR, ex);
            }
        } catch (NamingException ex) {
            this.traza.trace("error LDAP", Level.ERROR, ex);
        } catch (SQLException ex) {
            this.traza.trace("error al agregar el perfil del usuario", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("bandera perfil " + perf, Level.INFO);
        traza.trace("bandera fabrica " + fab, Level.INFO);

        if (perf && fab) {
            resp = true;
        } else {
            resp = false;
        }

        return resp;
    }

    @WebMethod(operationName = "agregarLibreria")
    public boolean agregarLibreria(@WebParam(name = "librerias") List<Libreria> librerias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false, respMod = true;
        int sizeList, cont = 0;
        List<Libreria> libreria = new ArrayList<>();

        try {
            sizeList = librerias.size();
            for (Libreria lib : librerias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_agregar_libreria( ?, ? ) }");
                }

                if (lib.getIdLibreria() == 0) {

                    this.traza.trace("agregando la libreria " + lib.getDescripcion(), Level.INFO);

                    stored.setString(1, lib.getDescripcion());
                    stored.setInt(2, Integer.parseInt(lib.getEstatus()));
                    stored.execute();

                    cont++;
                    this.traza.trace("libreria " + lib.getDescripcion() + " agregada con exito ", Level.INFO);

                } else {
                    libreria.add(lib);
                }

            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else if (libreria.isEmpty()) {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando las librerias");
            }

            this.traza.trace("tamaño de la lista a modificar " + libreria.size(), Level.INFO);

            if (!libreria.isEmpty()) {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
                respMod = new AdministracionModifica().modificarLibreria(libreria);
            }

            if (respMod) {
                resp = true;
            } else {
                resp = false;
                throw new DW4JServiciosException("problemas actualizando las librerias");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar las librerias", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al agregar la libreria", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("problema con el rollback al guardar las categorias", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }

        traza.trace("respuesta al agregar las librerias " + resp, Level.INFO);
        return resp;
    }

    @WebMethod(operationName = "agregarCategoria")
    public boolean agregarCategoria(@WebParam(name = "categorias") List<Perfil> categorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false, respMod = true;
        int sizeList, cont = 0;
        List<Categoria> categoria = new ArrayList<>();

        try {
            sizeList = categorias.size();
            for (Perfil categori : categorias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_agregar_categoria( ?, ?, ? ) }");
                }

                if (categori.getCategoria().getIdCategoria() == 0) {

                    this.traza.trace("agregando la categoria " + categori.getCategoria().getCategoria() + " de la libreria " + categori.getLibreria().getDescripcion(), Level.INFO);

                    stored.setInt(1, categori.getLibreria().getIdLibreria());
                    stored.setString(2, categori.getCategoria().getCategoria());
                    stored.setInt(3, Integer.parseInt(categori.getCategoria().getEstatus()));
                    stored.execute();
                    cont++;

                } else {
                    categoria.add(categori.getCategoria());
                }
            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else if (categoria.isEmpty()) {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando las categorias");
            }

            this.traza.trace("tamaño de la lista de categorias a modificar " + categoria.size(), Level.INFO);

            if (!categoria.isEmpty()) {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
                respMod = new AdministracionModifica().modificarCategoria(categoria);
            }

            if (respMod) {
                resp = true;
            } else {
                resp = false;
                throw new DW4JServiciosException("problemas actualizando las categorias");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar las categorias", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al agregar la categoria", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("problema con el rollback al guardar las categorias", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta al agregar las categorias " + resp, Level.INFO);

        return resp;
    }

    @WebMethod(operationName = "agregarSubCategoria")
    public boolean agregarSubCategoria(@WebParam(name = "subCategorias") List<Perfil> subCategorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        String lib, cat, subCat;
        boolean resp = false, respMod = true;
        int idCat, sizeList, cont = 0;
        List<SubCategoria> subCategoria = new ArrayList<>();

        try {
            sizeList = subCategorias.size();
            for (Perfil subCa : subCategorias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_agregar_subcategoria( ?, ?, ? ) }");
                }

                if (subCa.getSubCategoria().getIdSubCategoria() == 0) {

                    lib = subCa.getLibreria().getDescripcion();
                    cat = subCa.getCategoria().getCategoria();
                    subCat = subCa.getSubCategoria().getSubCategoria();

                    idCat = subCa.getCategoria().getIdCategoria();

                    this.traza.trace("agregando la SubCategoria " + subCat + " de la categoria " + cat + " de la libreria " + lib, Level.INFO);

                    stored.setInt(1, idCat);
                    stored.setString(2, subCat);
                    stored.setInt(3, Integer.parseInt(subCa.getSubCategoria().getEstatus()));
                    stored.execute();

                    cont++;

                } else {
                    subCategoria.add(subCa.getSubCategoria());
                }
            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else if (subCategoria.isEmpty()) {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando las subCategorias");
            }

            this.traza.trace("tamaño de la lista de subcategorias a modificar " + subCategoria.size(), Level.INFO);

            if (!subCategoria.isEmpty()) {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
                respMod = new AdministracionModifica().modificarSubCategoria(subCategoria);
            }

            if (respMod) {
                resp = true;
            } else {
                resp = false;
                throw new DW4JServiciosException("problemas actualizando las subCategorias");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar las subCategorias", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al agregar la SubCategoria", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException ex1) {
                traza.trace("problema con el rollback al guardar las subCategorias", Level.ERROR, ex1);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta al agregar las subCategorias " + resp, Level.INFO);

        return resp;
    }

    @WebMethod(operationName = "agregarTipoDocumento")
    public boolean agregarTipoDocumento(@WebParam(name = "tipodocumentos") List<Perfil> tipodocumentos) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        int idCat, idSubCat, idLib, sizeList, cont = 0;
        boolean resp = false;
        boolean respMod = true;
        String lib, cat, subCat, tipoDoc, vencimiento, datoAdicional;
        List<TipoDocumento> tipodocumento = new ArrayList<>();

        try {
            sizeList = tipodocumentos.size();

            for (Perfil td : tipodocumentos) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_agregar_tipodocumento( ?, ?, ?, ?, ?, ? ) }");
                }

                this.traza.trace("id tipo documento " + td.getTipoDocumento().getIdTipoDocumento(), Level.INFO);

                if (td.getTipoDocumento().getIdTipoDocumento() == 0) {

                    lib = td.getLibreria().getDescripcion();
                    cat = td.getCategoria().getCategoria();
                    subCat = td.getSubCategoria().getSubCategoria();
                    tipoDoc = td.getTipoDocumento().getTipoDocumento();
                    vencimiento = td.getTipoDocumento().getVencimiento();
                    datoAdicional = td.getTipoDocumento().getDatoAdicional();

                    idCat = td.getCategoria().getIdCategoria();
                    idSubCat = td.getSubCategoria().getIdSubCategoria();
                    idLib = td.getLibreria().getIdLibreria();

                    this.traza.trace("agregando el tipo de documento " + tipoDoc, Level.INFO);
                    this.traza.trace("de la subCategoria " + subCat + " id " + idSubCat, Level.INFO);
                    this.traza.trace("de la categoria " + cat + " id " + idCat, Level.INFO);
                    this.traza.trace("de la libreria " + lib + " id " + idLib, Level.INFO);

                    this.traza.trace("vencimiento? " + vencimiento, Level.INFO);
                    this.traza.trace("dato adicional? " + datoAdicional, Level.INFO);

                    stored.setInt(1, idCat);
                    stored.setInt(2, idSubCat);
                    stored.setString(3, tipoDoc);
                    stored.setInt(4, Integer.parseInt(td.getTipoDocumento().getEstatus()));
                    stored.setString(5, vencimiento);
                    stored.setString(6, datoAdicional);
                    stored.execute();
                    cont++;

                } else {
                    tipodocumento.add(td.getTipoDocumento());
                }
            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else if (tipodocumento.isEmpty()) {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando los tipos de documentos");
            }

            this.traza.trace("tamaño de la lista de tipos de documento a modificar " + tipodocumento.size(), Level.INFO);

            if (!tipodocumento.isEmpty()) {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
                respMod = new AdministracionModifica().modificarTipoDocumento(tipodocumento);
            }

            if (respMod) {
                resp = true;
            } else {
                resp = false;
                throw new DW4JServiciosException("problemas actualizando los tipos de documentos");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar los tipos de documentos", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al agregar la SubCategoria", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("problema con el rollback al guardar los tipos de documentos", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta al agregar los tipo de documentos " + resp, Level.INFO);

        return resp;
    }

    @WebMethod(operationName = "agregarIndices")
    public boolean agregarIndices(@WebParam(name = "argumentos") List<Indice> indices) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        String indice, tipo, clave;
        int idCat, sizeList, cont = 0;

        try {
            sizeList = indices.size();

            for (Indice arg : indices) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_agregar_indices( ?, ?, ?, ?, ? ) } ");
                }

                indice = arg.getIndice();
                tipo = arg.getTipo();

                try {
                    clave = arg.getClave();
                } catch (NullPointerException e) {
                    clave = "";
                }

                idCat = arg.getIdCategoria();

                this.traza.trace("agregando el indice " + indice, Level.INFO);
                this.traza.trace("su tipo de datos " + tipo, Level.INFO);
                this.traza.trace("su clave " + clave, Level.INFO);
                this.traza.trace("el idCategoria " + idCat, Level.INFO);

                stored.setInt(1, idCat);
                stored.setString(2, indice);
                stored.setString(3, tipo);
                stored.setInt(4, arg.getCodigo());
                stored.setString(5, clave);
                stored.execute();
                cont++;
            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando las indices");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar los indices", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al crear los argumentos", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("problema con el rollback al guardar los tipos de documentos", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta al agregar los indices " + resp, Level.INFO);

        return resp;
    }

    @WebMethod(operationName = "agregarListaDesplegables")
    public boolean agregarListaDesplegables(@WebParam(name = "listaDesplegable") List<Combo> listaDesplegable, @WebParam(name = "bandera") boolean bandera) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false, respMod = true, update = false;
        int codigoIndice, sizeList, cont = 0;
        List<Combo> modLista = new ArrayList<>();

        try {

            this.traza.trace("bandera " + bandera, Level.INFO);

            sizeList = listaDesplegable.size();

            for (Combo cbo : listaDesplegable) {

                String datoCombo = cbo.getDatoCombo();
                String indice = cbo.getIndice();
                codigoIndice = cbo.getCodigoIndice();
                int idCombo = cbo.getIdCombo();

                traza.trace("idCombo " + idCombo + " del indice " + indice + " su dato " + datoCombo, Level.INFO);

                if (idCombo == 0) {

                    this.traza.trace("indice " + indice, Level.INFO);
                    this.traza.trace("dato del indice " + datoCombo, Level.INFO);
                    this.traza.trace("codigo del indice " + codigoIndice, Level.INFO);
                    this.traza.trace("bandera actualiza codigo combo " + update, Level.INFO);

                    if (bandera) {

                        if (!update) {
                            stored = bd.conectar().prepareCall("{ call p_actualiza_codigo_combo( ?, ? ) }");
                            stored.setInt(1, codigoIndice);
                            stored.setString(2, "1");
                            stored.execute();

                            update = true;
                            bd.commit();
                            stored = null;
                            bd.desconectar();
                        }

                        if (stored == null) {
                            stored = bd.conectar().prepareCall("{ call p_agregar_datos_combo_da( ?, ? ) }");
                        }

                        stored.setInt(1, codigoIndice);
                        stored.setString(2, datoCombo);
                        stored.execute();

                    } else {

                        if (!update) {
                            stored = bd.conectar().prepareCall("{ call p_actualiza_codigo_combo( ?, ? ) }");
                            stored.setInt(1, codigoIndice);
                            stored.setString(2, "0");
                            stored.execute();

                            update = true;
                            bd.commit();
                            stored = null;
                            bd.desconectar();
                        }

                        if (stored == null) {
                            stored = bd.conectar().prepareCall("{ call p_agregar_datos_combo( ?, ? ) }");
                        }

                        stored.setInt(1, codigoIndice);
                        stored.setString(2, datoCombo);
                        stored.execute();
                    }

                    cont++;

                } else {
                    this.traza.trace("modificar el dato " + cbo.getDatoCombo() + " su id " + cbo.getIdCombo(), Level.INFO);
                    modLista.add(cbo);
                }
            }

            if (!modLista.isEmpty()) {
                traza.trace("tamaño lista de combo a modificar " + modLista.size(), Level.INFO);
                respMod = new AdministracionModifica().modificarDatosCombo(modLista);

                if (respMod) {
                    resp = true;
                } else {
                    resp = false;
                    throw new DW4JServiciosException("problemas actualizando las lista desplegable");
                }

            } else if (cont > 0) {

                if (sizeList == cont) {
                    resp = true;
                    bd.commit();
                    bd.desconectar();
                } else {
                    resp = false;
                    bd.rollback();
                    throw new DW4JServiciosException("problemas guardando las listas desplegables");
                }
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar los tipos de documentos", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al agregar los datos de la lista desplegable", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("problema con el rollback al guardar los tipos de documentos", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta al agregar los datos del combo " + resp, Level.INFO);
        return resp;
    }

    @WebMethod(operationName = "agregarIndiceDatoAdicional")
    public boolean agregarIndiceDatoAdicional(@WebParam(name = "indiceDatos") List<DatoAdicional> indiceDatos) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        int idTipoDoc, sizeList, cont = 0;
        String datoAdicional;

        try {

            this.traza.trace("tamaño de la lista de indices para datos adicioanles " + indiceDatos.size(), Level.INFO);

            sizeList = indiceDatos.size();

            for (DatoAdicional da : indiceDatos) {
                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_guardar_indice_datoadicional( ?, ?, ? ) }");
                }

                idTipoDoc = da.getIdTipoDocumento();
                datoAdicional = da.getIndiceDatoAdicional();
                this.traza.trace("idTipoDocumento " + idTipoDoc + " para el dato adicional " + datoAdicional + " su tipo " + da.getTipo(), Level.INFO);

                stored.setInt(1, idTipoDoc);
                stored.setString(2, datoAdicional);
                stored.setString(3, da.getTipo());
                stored.execute();

                cont++;
            }

            if (sizeList == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas guardando los indices del dato adicional");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al guardar los indices del dato adicional", Level.ERROR, ex);
            }

        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("problemas al guardar los indices del dato adicional", Level.ERROR, ex);
            try {
                bd.rollback();
            } catch (SQLException e) {
                this.traza.trace("roblemas en el rollback de datos adicionales", Level.ERROR, e);
            }
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("respuesta al agregar los indices de datos adicionales " + resp, Level.INFO);
        return resp;
    }
}
