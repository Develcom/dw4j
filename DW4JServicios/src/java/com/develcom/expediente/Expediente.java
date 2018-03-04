/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.expediente;

import com.develcom.administracion.AdministracionBusquedas;
import com.develcom.dao.Indice;
import com.develcom.dao.ListaIndice;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.tools.Propiedades;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "Expediente")
public class Expediente {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(Expediente.class);

    /**
     * Busca el Expedientes
     *
     * @param idExpediente El expediente
     * @param idCategoria El id de la Categoria
     * @param idLibreria El id de la Libreira
     * @return Los datos del Expedientes
     */
    @WebMethod(operationName = "buscarExpediente")
    public com.develcom.dao.Expedientes buscarExpediente(@WebParam(name = "idExpediente") String idExpediente,
            @WebParam(name = "idCategoria") int idCategoria,
            @WebParam(name = "idLibreria") int idLibreria) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CallableStatement stored;
        BaseDato bd = new BaseDato();
        com.develcom.dao.Expedientes expediente = new com.develcom.dao.Expedientes();
        List<ListaIndice> listaIndices = new ArrayList<>();
        List<Indice> indices;
        List<Indice> indicesExt = null, indicesExiste, indicesNew;
        ResultSet rsExpediente, rsExterno;
        Statement buscar;
        ListaIndice listaIndice = null;
        Indice indice;
        int idCate = 0;

        traza.trace("Buscando el expediente: " + idExpediente, Level.INFO);
        traza.trace("idLibreria : " + idLibreria, Level.INFO);
        traza.trace("idCategoria : " + idCategoria, Level.INFO);

        try {

            expediente.setExiste(false);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente( ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.setInt(3, idLibreria);
            stored.setInt(4, idCategoria);
            stored.setString(5, "1");
            stored.execute();

            rsExpediente = (ResultSet) stored.getObject(1);

            if (rsExpediente.isBeforeFirst()) {
//                while (rsExpediente.next()) {
//
//                    expediente.setExpediente(rsExpediente.getString("EXPEDIENTE").trim());
//                    expediente.setIdLibreria(rsExpediente.getInt("ID_LIBRERIA"));
//                    expediente.setIdCategoria(rsExpediente.getInt("ID_CATEGORIA"));
//                    expediente.setExiste(true);
//
//                    traza.trace("libreria del expediente " + rsExpediente.getString("libreria"), Level.INFO);
//                    traza.trace("categoria del expediente " + rsExpediente.getString("categoria"), Level.INFO);
//                    indice = new Indice();
//
//                    indice.setClave(rsExpediente.getString("clave"));
//                    indice.setCodigo(rsExpediente.getInt("codigo"));
//                    indice.setIdCategoria(rsExpediente.getInt("id_categoria"));
//                    indice.setIdIndice(rsExpediente.getInt("id_indice"));
//                    indice.setTipo(rsExpediente.getString("tipo"));
//
//                    if (rsExpediente.getObject("valor") != null) {
//                        indice.setValor(rsExpediente.getObject("valor"));
//                    } else {
//                        indice.setValor(rsExpediente.getDate("fecha_indice"));
//                    }
//
//                    indice.setIndice(rsExpediente.getString("indice"));
//                    indices.add(indice);
//
//                    traza.trace("indice " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);
//                }
                expediente.setExiste(true);
                indices = new ArrayList<>();
                while (rsExpediente.next()) {
                    indice = new Indice();

                    if (idCate != rsExpediente.getInt("id_categoria")) {
                        if (idCate == 0) {

                        } else {
                            listaIndice = new ListaIndice();
                            listaIndice.setIndices(indices);
                            listaIndices.add(listaIndice);
                        }
                    }

                    indice.setClave(rsExpediente.getString("clave"));
                    indice.setCodigo(rsExpediente.getInt("codigo"));
                    indice.setIdCategoria(rsExpediente.getInt("id_categoria"));
                    indice.setIdIndice(rsExpediente.getInt("id_indice"));
                    indice.setTipo(rsExpediente.getString("tipo"));

                    if (rsExpediente.getObject("valor") != null) {
                        indice.setValor(rsExpediente.getObject("valor"));
                    } else {
                        indice.setValor(rsExpediente.getDate("fecha_indice"));
                    }
                    indice.setIndice(rsExpediente.getString("indice"));

                    idCate = indice.getIdCategoria();

                    indices.add(indice);

                }
                if (listaIndice == null) {
                    listaIndice = new ListaIndice();
                    listaIndice.setIndices(indices);
                    listaIndices.add(listaIndice);
                }
                expediente.setListaIndices(listaIndices);

            } else {

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente( ?, ?, ?, ? ) } ");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, idExpediente);
                stored.setInt(3, 0);
                stored.setInt(4, 0);
                stored.setString(5, "0");
                stored.execute();

                rsExpediente = (ResultSet) stored.getObject(1);

                if (rsExpediente.isBeforeFirst()) {
                    expediente.setExiste(true);
                    indicesExiste = new ArrayList<>();

                    while (rsExpediente.next()) {
                        indice = new Indice();

                        if (idCate != rsExpediente.getInt("id_categoria")) {
                            if (idCate == 0) {

                            } else {
                                listaIndice = new ListaIndice();
                                listaIndice.setIndices(indicesExiste);
                                listaIndices.add(listaIndice);
                            }
                        }

                        indice.setClave(rsExpediente.getString("clave"));
                        indice.setCodigo(rsExpediente.getInt("codigo"));
                        indice.setIdCategoria(rsExpediente.getInt("id_categoria"));
                        indice.setIdIndice(rsExpediente.getInt("id_indice"));
                        indice.setTipo(rsExpediente.getString("tipo"));

                        if (rsExpediente.getObject("valor") != null) {
                            indice.setValor(rsExpediente.getObject("valor"));
                        } else {
                            indice.setValor(rsExpediente.getDate("fecha_indice"));
                        }
                        indice.setIndice(rsExpediente.getString("indice"));

                        idCate = indice.getIdCategoria();

                        indicesExiste.add(indice);

                    }
                    if (listaIndice == null) {
                        listaIndice = new ListaIndice();
                        listaIndice.setIndices(indicesExiste);
                        listaIndices.add(listaIndice);
                    }
                }

                if (bd.conectar("mfprestaciones03_10_16") != null) {

                    Properties prop = Propiedades.cargarPropiedadesWS();
                    
                    buscar = bd.conectar("mfprestaciones03_10_16").createStatement();

                    rsExterno = buscar.executeQuery(prop.getProperty("queryExterno") + idExpediente);

                    indicesNew = new AdministracionBusquedas().buscarIndices(idCategoria);

                    if (listaIndices.isEmpty()) {

                        while (rsExterno.next()) {

                            listaIndice = new ListaIndice();
                            indicesExt = new ArrayList<>();
                            for (Indice ind : indicesNew) {

                                indice = new Indice();

                                if ("NUMERO DE SOLICITUD U OFICIO".equalsIgnoreCase(ind.getIndice())) {
                                    ind.setValor(rsExterno.getObject("c_numero_oficio"));
                                } else if ("CEDULA DE IDENTIDAD DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                                    ind.setValor(rsExterno.getObject("c_cedula_persona"));
                                } else if ("APELLIDOS Y NOMBRES DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                                    ind.setValor(rsExterno.getObject("c_nombre_completo"));
                                } else if ("ANO DE SOLICITUD".equalsIgnoreCase(ind.getIndice())) {
                                    String anio = sdf.format(rsExterno.getDate("c_fecha_recepcion"));
                                    anio = anio.substring(6);
                                    ind.setValor(anio);
                                } else if ("ORGANISMO PRINCIPAL".equalsIgnoreCase(ind.getIndice())) {
                                    ind.setValor(rsExterno.getObject("c_descripcion_organismo"));
                                }

                                indice.setClave(ind.getClave());
                                indice.setCodigo(ind.getCodigo());
                                indice.setIdCategoria(idCategoria);
                                indice.setIdIndice(ind.getIdIndice());
                                indice.setIndice(ind.getIndice());
                                indice.setTipo(ind.getTipo());
                                indice.setUpdateIndices(ind.isUpdateIndices());
                                indice.setValor(ind.getValor());

                                indicesExt.add(indice);
                            }
                            listaIndice.setIndices(indicesExt);
                            listaIndices.add(listaIndice);
                        }

                    } else {

                        while (rsExterno.next()) {

                            listaIndice = new ListaIndice();
                            indicesExt = new ArrayList<>();

                            for (ListaIndice listInd : listaIndices) {
                                for (Indice indi : listInd.getIndices()) {

                                    if (!indi.getValor().equals(rsExterno.getObject("c_numero_oficio"))) {

                                        for (Indice ind : indicesNew) {

                                            indice = new Indice();

                                            if (ind.getValor() != null) {
                                                if ("NUMERO DE SOLICITUD U OFICIO".equalsIgnoreCase(ind.getIndice())) {
                                                    ind.setValor(rsExterno.getObject("c_numero_oficio"));
                                                } else if ("CEDULA DE IDENTIDAD DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                                                    ind.setValor(rsExterno.getObject("c_cedula_persona"));
                                                } else if ("APELLIDOS Y NOMBRES DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                                                    ind.setValor(rsExterno.getObject("c_nombre_completo"));
                                                } else if ("ANO DE SOLICITUD".equalsIgnoreCase(ind.getIndice())) {
                                                    String anio = sdf.format(rsExterno.getDate("c_fecha_recepcion"));
                                                    anio = anio.substring(6);
                                                    ind.setValor(anio);
                                                } else if ("ORGANISMO PRINCIPAL".equalsIgnoreCase(ind.getIndice())) {
                                                    ind.setValor(rsExterno.getObject("c_descripcion_organismo"));
                                                } else {
                                                    ind.setValor("");
                                                }

                                                indice.setClave(ind.getClave());
                                                indice.setCodigo(ind.getCodigo());
                                                indice.setIdCategoria(idCategoria);
                                                indice.setIdIndice(ind.getIdIndice());
                                                indice.setIndice(ind.getIndice());
                                                indice.setTipo(ind.getTipo());
                                                indice.setUpdateIndices(ind.isUpdateIndices());
                                                indice.setValor(ind.getValor());

                                                indicesExt.add(indice);
                                            }
                                        }
                                        listaIndice.setIndices(indicesExt);
                                        listaIndices.remove(listInd);
                                        listaIndices.add(listaIndice);
                                    }
                                }
                            }
                        }
                    }
                    expediente.setListaIndices(listaIndices);
                }

            }
            
            if (listaIndices.size() == 1) {
                expediente.setIndices(indicesExt);
            } else {
                expediente.setListaIndices(listaIndices);
                indices = new AdministracionBusquedas().buscarIndices(idCategoria);
                expediente.setIndices(indices);
            }

        } catch (SQLException ex) {
            traza.trace("Error en la busqueda del expediente en la base de datos", Level.ERROR, ex);
            expediente.setExiste(false);
            expediente.setMensaje(ex.getMessage());
        } catch (Exception ex) {
            traza.trace("Error general al buscar el expediente " + idExpediente, Level.ERROR, ex);
            expediente.setExiste(false);
            expediente.setMensaje(ex.getMessage());
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        if (!expediente.isExiste()) {
            expediente.setMensaje("El expediente " + idExpediente + " no se encuentra registrado");
        }
        traza.trace("existe expediente " + expediente.isExiste(), Level.INFO);
        return expediente;
    }

    /**
     * Guarda el expediente
     *
     * @param expediente Los datos del expediente
     * @return Verdadero si lo guardo, falso en caso contrario
     */
    @WebMethod(operationName = "guardarExpediente")
    public boolean guardarExpediente(@WebParam(name = "expediente") com.develcom.dao.Expedientes expediente) {

        CallableStatement stored = null;
        BaseDato bd = new BaseDato();
        boolean resp = false, flag = false;
        int sizeIndiceValor, cont = 0;
        Date dateInd;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        List<Indice> indicesValor = expediente.getIndices();
        sizeIndiceValor = indicesValor.size();

        traza.trace("tamaño de los indices: " + sizeIndiceValor, Level.INFO);
        traza.trace("GUARDANDO EL EXPEDIENTE: " + expediente.getExpediente().toUpperCase(), Level.INFO);
        traza.trace("id libreria del expediente " + expediente.getIdLibreria(), Level.INFO);
        traza.trace("id categoria del expediente " + expediente.getIdCategoria(), Level.INFO);

        try {

            for (Indice indice : indicesValor) {
                traza.trace("guardando el indice " + indice.getIndice() + " su valor " + indice.getValor()
                        + " expediente " + expediente.getExpediente(), Level.INFO);

                if (stored == null) {
                    stored = bd.conectar().prepareCall(" { call p_guardar_expediente( ?, ?, ?, ?, ?, ? ) } ");
                }

                stored.setString(1, expediente.getExpediente());
                stored.setInt(2, indice.getIdIndice());

                if (indice.getTipo().equalsIgnoreCase("fecha")) {

                    dateInd = new Date(sdf.parse(indice.getValor().toString()).getTime());
                    traza.trace("fecha indice " + dateInd, Level.INFO);
                    stored.setString(3, null);
                    stored.setDate(4, dateInd);

                } else {
                    stored.setString(3, indice.getValor().toString());
                    stored.setDate(4, null);
                }
                stored.setInt(5, expediente.getIdLibreria());
                stored.setInt(6, expediente.getIdCategoria());
                stored.execute();

                traza.trace("objeto CallableStatement " + stored, Level.INFO);

                cont++;

            }
            if (sizeIndiceValor == cont) {
                resp = true;
                bd.commit();
                bd.desconectar();
            } else {
                resp = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas modificando las librerias");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            traza.trace("problemas en la base de datos", Level.ERROR, e);
            try {
                stored = null;
                stored = bd.conectar().prepareCall(" { call p_eliminar_expediente( ? ) } ");
                stored.setString(1, expediente.getExpediente());
                stored.execute();

                bd.commit();
                traza.trace("se elimino el expediente " + expediente.getExpediente(), Level.INFO);

            } catch (SQLException ex1) {
                traza.trace("error durante la eliminacion del expediente: " + expediente.getExpediente() + " en la tabla expediente", Level.ERROR, ex1);
            }

        } catch (SQLException ex) {
            resp = false;
            traza.trace("Error al insertar datos del expediente: " + expediente.getExpediente(), Level.ERROR, ex);
        } catch (ParseException ex) {
            traza.trace("probelmas en el parser de la fecha", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta al guardar el expediente " + resp, Level.INFO);
        return resp;
    }

}
