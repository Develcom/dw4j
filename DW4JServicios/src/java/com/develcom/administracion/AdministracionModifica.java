/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.administracion;

import com.develcom.dao.Categoria;
import com.develcom.dao.Combo;
import com.develcom.dao.Configuracion;
import com.develcom.dao.Fabrica;
import com.develcom.dao.Libreria;
import com.develcom.dao.Rol;
import com.develcom.dao.SubCategoria;
import com.develcom.dao.TipoDocumento;
import com.develcom.dao.administrar.Perfil;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.mantenimiento.Mantenimientos;
import com.develcom.tools.BaseDato;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "AdministracionModifica")
public class AdministracionModifica {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(AdministracionModifica.class);

    /**
     * Modifica el actual Perfil del usuario
     *
     * @param perfil Lita con el Perfil
     * @return
     */
    @WebMethod(operationName = "modificarPerfil")
    public boolean modificarPerfil(@WebParam(name = "perfil") List<Perfil> perfil) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsUser;
        boolean resp = false, perf = false, fab = false;
        String usuario = "", cat = "", lib = "", ro = "";
        Categoria categoria;
        Libreria libreria;
        Fabrica fabrica;
        Rol rol;
        boolean estatus;
        Configuracion config;
        int sizeList, cont = 0;

        try {

            if (perfil.get(0).getEstatus() > 0) {
                estatus = modificarEstatusUsuarios(perfil.get(0).getUsuario(), perfil.get(0).getEstatus());
                if (estatus) {
                    traza.trace("se cambio el estatus del usuario", Level.INFO);
                } else {
                    traza.trace("problemas al cambiar el estatus del usuario", Level.INFO);
                }
            }

            stored = bd.conectar().prepareCall("{ call p_eliminar_perfil( ? ) }");
            stored.setString(1, perfil.get(0).getUsuario());
            stored.execute();
            bd.commit();

            stored = null;
            sizeList = perfil.size();
            for (Perfil p : perfil) {

                libreria = p.getLibreria();
                categoria = p.getCategoria();
                rol = p.getRol();
                usuario = p.getUsuario();

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

                traza.trace("usuario " + usuario, Level.INFO);
                traza.trace("libreria " + lib, Level.INFO);
                traza.trace("categoria " + cat, Level.INFO);
                traza.trace("rol " + ro, Level.INFO);

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

                stored.setString(3, usuario);

                try {
                    stored.setInt(4, rol.getIdRol());
                } catch (NullPointerException e) {
                    stored.setInt(4, 0);
                }

                stored.execute();
                cont++;
            }

            if (sizeList == cont) {
                perf = true;
                bd.commit();
                bd.desconectar();
            } else {
                perf = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas modificando el perfil del usuario");
            }

            config = new Mantenimientos().buscarMantenimiento();

            if (config.isFabrica()) {

                stored = null;
                stored = bd.conectar().prepareCall("{ ? = call f_buscar_usuario_fabrica( ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, usuario);
                stored.execute();

                rsUser = (ResultSet) stored.getObject(1);

                if (rsUser.next()) {
                    if (!usuario.equalsIgnoreCase(rsUser.getString("usuario"))) {

                        fabrica = perfil.get(0).getFabrica();

                        traza.trace("configurando el usuario para la cola de la fabrica", Level.INFO);
                        traza.trace("el usuario " + fabrica.getUsuario() + " pertenece a la fabrica " + fabrica.isPertenece(), Level.INFO);

                        bd.desconectar();
                        stored = null;
                        fabrica = perfil.get(0).getFabrica();

                        this.traza.trace("configurando el usuario para la cola de la fabrica", Level.INFO);
                        this.traza.trace("el usuario " + fabrica.getUsuario() + " pertenece a la fabrica " + fabrica.isPertenece(), Level.INFO);

                        stored = bd.conectar().prepareCall("{ call p_agregar_fabrica( ?, ? ) }");
                        stored.setString(1, fabrica.getUsuario());
                        stored.setBoolean(2, fabrica.isPertenece());
                        stored.execute();

                        bd.commit();
                        fab = true;
                        bd.desconectar();

                    } else {
                        fab = true;
                    }
                }

            } else {
                fab = true;
            }
            
        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace("problema en la base de datos", Level.ERROR, e);
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar el perfil del usuario", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            traza.trace("error al modificar el perfil del usuario", Level.ERROR, ex);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException e) {
                traza.trace("error rollback", Level.ERROR, e);
            }

        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        if (perf && fab) {
            resp = true;
        } else {
            resp = false;
        }

        traza.trace("respuesta al modificar el perfil " + resp, Level.INFO);
        return resp;

    }

    /**
     * Modifica es estatus del usuario
     *
     * @param idUsuario El login del usuario
     * @param estatus El id del estatus
     * @return Verdadero si el estatus fue modificado, falso en caso contrario
     */
    private boolean modificarEstatusUsuarios(String idUsuario, int estatus) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsModEstatusUser;
        String status, des, stus;
        boolean resp = false;

        try {

            traza.trace("cambiando el estatus del usuario " + idUsuario, Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idUsuario);
            stored.execute();

            rsModEstatusUser = (ResultSet) stored.getObject(1);

            if (rsModEstatusUser.next()) {
                status = rsModEstatusUser.getString("DESCRIPCION");

                traza.trace("el estatus actual del usuario " + idUsuario + " es " + status, Level.INFO);

                stored = null;
                rsModEstatusUser = null;
                stored = bd.conectar().prepareCall("{ ? = f_buscar_estatus( ?, ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, "");
                stored.setInt(3, estatus);
                stored.execute();

                rsModEstatusUser = (ResultSet) stored.getObject(1);

                if (rsModEstatusUser.next()) {
                    des = rsModEstatusUser.getString("DESCRIPCION");

                    if (!status.equalsIgnoreCase(des)) {

                        stored = null;
                        rsModEstatusUser = null;
                        stored = bd.conectar().prepareCall("{ call p_modificar_usuario( ?, ? ) }");
                        stored.setString(1, idUsuario);
                        stored.setInt(2, estatus);
                        stored.execute();

                        bd.commit();

                        stored = bd.conectar().prepareCall(" { ? = call f_verificar_usuario( ? ) } ");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, idUsuario);
                        stored.execute();

                        rsModEstatusUser = (ResultSet) stored.getObject(1);

                        if (rsModEstatusUser.next()) {
                            stus = rsModEstatusUser.getString("DESCRIPCION");

                            if (!stus.equalsIgnoreCase(status)) {
                                resp = true;
                            } else {
                                resp = false;
                            }
                        }
                    } else {
                        traza.trace("el estatus del usuario " + idUsuario + " es la misma de la base de datos", Level.INFO);
                    }
                }
            }

            traza.trace("respuesta al cambiar el estatus del usuario " + resp, Level.INFO);

        } catch (SQLException e) {
            traza.trace("error durante el cambio de estatus del usuario " + idUsuario, Level.ERROR, e);
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
     * Modifica las librerias
     *
     * @param librerias Listado de librerias a modificar
     * @return Verdadero si la modificacion fue exitosa, falso en caso contrario
     */
    @WebMethod(operationName = "modificarLibreria")
    public boolean modificarLibreria(@WebParam(name = "librerias") List<Libreria> librerias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        int sizeList, cont = 0;

        try {

            sizeList = librerias.size();

            for (Libreria lib : librerias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_librerias( ?, ? ) }");
                }

                traza.trace("modificando la libreria " + lib.getDescripcion(), Level.INFO);
                traza.trace("el id la libreria " + lib.getIdLibreria(), Level.INFO);

                stored.setInt(1, lib.getIdLibreria());
                stored.setInt(2, Integer.parseInt(lib.getEstatus()));
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
                throw new DW4JServiciosException("problemas modificando las librerias");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar las librerias", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al modificar la libreria", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta modificando la libreria " + resp, Level.INFO);
        return resp;
    }

    /**
     * Actualiza el estatus de las categorias
     *
     * @param categorias Listado de Categoria
     * @return Verdadero se se modifico el estatus de las categorisa, falso en
     * caso contrario
     */
    @WebMethod(operationName = "modificarCategoria")
    public boolean modificarCategoria(@WebParam(name = "categorias") List<Categoria> categorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        int sizeList, cont = 0;

        try {

            sizeList = categorias.size();

            for (Categoria ca : categorias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_categorias( ?, ? ) }");
                }

                traza.trace("modificando la categoria " + ca.getCategoria(), Level.INFO);
                traza.trace("el id la categoria " + ca.getIdCategoria(), Level.INFO);

                stored.setInt(1, ca.getIdCategoria());
                stored.setInt(2, Integer.parseInt(ca.getEstatus()));
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
                throw new DW4JServiciosException("problemas modificando las categorias");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar las categorias", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al modificar la categoria", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta modificando la categoria " + resp, Level.INFO);
        return resp;
    }

    /**
     * Actualiza el estatus de las subcategorias
     *
     * @param subCategorias Listado de SubCategoria
     * @return Verdadero se se modifico el estatus de las subCategorisa, falso
     * en caso contrario
     */
    @WebMethod(operationName = "modificarSubCategoria")
    public boolean modificarSubCategoria(@WebParam(name = "subCategorias") List<SubCategoria> subCategorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        String subCat;
        int idSubCat, sizeList, cont = 0;

        try {

            sizeList = subCategorias.size();

            for (SubCategoria subCa : subCategorias) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_subcategorias( ?, ? ) }");
                }

                subCat = subCa.getSubCategoria();
                idSubCat = subCa.getIdSubCategoria();

                traza.trace("modificando la SubCategoria " + subCat, Level.INFO);
                traza.trace("el id de la SubCategoria " + idSubCat, Level.INFO);

                stored.setInt(1, idSubCat);
                stored.setInt(2, Integer.parseInt(subCa.getEstatus()));
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
                throw new DW4JServiciosException("problemas modificando las subCategorias");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar las SubCategoria", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al modificar la SubCategoria", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta modificando la subCategoria " + resp, Level.INFO);
        return resp;
    }

    /**
     * Actualiza el estatus de los tipos de documentos
     *
     * @param tipodocumentos Listado de tipos de documentos
     * @return Verdadero se se modifico el estatus de los tipos de documentos,
     * falso en caso contrario
     */
    @WebMethod(operationName = "modificarTipoDocumento")
    public boolean modificarTipoDocumento(@WebParam(name = "tipodocumentos") List<TipoDocumento> tipodocumentos) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        String tipoDoc = "", vencimiento, datoAdicional;
        int idTipoDoc, sizeList, cont = 0;

        try {

            sizeList = tipodocumentos.size();

            for (TipoDocumento td : tipodocumentos) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_tipodocumento( ?, ?, ?, ? ) }");
                }

                tipoDoc = td.getTipoDocumento();
                vencimiento = td.getVencimiento();
                datoAdicional = td.getDatoAdicional();

                idTipoDoc = td.getIdTipoDocumento();

                traza.trace("modificando el tipo de documento " + tipoDoc, Level.INFO);
                traza.trace("el id del tipo de documento " + idTipoDoc, Level.INFO);
                traza.trace("estatus del tipo de documento " + td.getEstatus(), Level.INFO);

                traza.trace("vencimiento? " + vencimiento, Level.INFO);
                traza.trace("dato adicional? " + datoAdicional, Level.INFO);

                stored.setInt(1, idTipoDoc);
                stored.setInt(2, Integer.parseInt(td.getEstatus()));
                stored.setString(3, vencimiento);
                stored.setString(4, datoAdicional);
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
                throw new DW4JServiciosException("problemas modificando los tipos de documentos");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar los tipos de documentos", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al modificar el tipo de documento " + tipoDoc, Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }

        }
        traza.trace("respuesta modificando los tipos de documentos " + resp, Level.INFO);
        return resp;
    }

    /**
     * Actualiza datos de la lista desplegable
     *
     * @param combos Lista de datos
     * @return Verdadero si actualizo con exito, falso en caso contrario
     */
    @WebMethod(operationName = "modificarDatosCombo")
    public boolean modificarDatosCombo(@WebParam(name = "combos") List<Combo> combos) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean resp = false;
        int sizeList, cont = 0;

        try {

            sizeList = combos.size();

            for (Combo cbo : combos) {

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_datos_combo( ?, ? ) }");
                }

                traza.trace("actualizando el dato " + cbo.getDatoCombo() + " del indice " + cbo.getIndice(), Level.INFO);
                traza.trace("id el dato " + cbo.getDatoCombo() + " " + cbo.getIdCombo(), Level.INFO);

                stored.setInt(1, cbo.getIdCombo());
                stored.setString(2, cbo.getDatoCombo());
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
                throw new DW4JServiciosException("problemas modificando los datos del combo");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            resp = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar los datos del combo", Level.ERROR, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al modificar los datos del combo", Level.ERROR, ex);
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
     * Actualiza la bandera que indica a el tipo de documento es lo foto del
     * expediente
     *
     * @param idTipoDocumento Identificador del tipo de documento
     * @return Verdadero si la actualizacion fue exitosa, falso en caso
     * contrario
     */
    @WebMethod(operationName = "modificarTipoDocumentoFoto")
    public boolean modificarTipoDocumentoFoto(@WebParam(name = "idTipoDocumento") int idTipoDocumento) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        boolean resp = false;
        ResultSet rsComprobarFicha;

        try {

            traza.trace("actualizando tipo de documento para la ficha (foto)", Level.INFO);

            stored = bd.conectar().prepareCall("{ call p_actualiza_td_foto( ? ) }");
            stored.setInt(1, idTipoDocumento);
            resp = stored.execute();

            resp = stored.execute();
            bd.commit();
            bd.desconectar();

            traza.trace("comprobando la actualizacion", Level.INFO);
            stored = bd.conectar().prepareCall("{ ? = call f_comprobar_foto_ficha( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idTipoDocumento);
            resp = stored.execute();

            rsComprobarFicha = (ResultSet) stored.getObject(1);

            if (rsComprobarFicha.next()) {
                resp = true;
            }
            traza.trace("respuesta de la actualizacion tipo de documento para la ficha " + resp, Level.INFO);

        } catch (SQLException e) {
            traza.trace("problemas al actualizar el tipo de docuemnto para la ficha (foto)", Level.ERROR, e);
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
}
