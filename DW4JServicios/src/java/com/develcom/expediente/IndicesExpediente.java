/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.expediente;

import com.develcom.dao.Indice;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.dao.Expedientes;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@WebService(serviceName = "IndicesExpediente")
public class IndicesExpediente {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(IndicesExpediente.class);

    /**
     * Busca los datos del Indice
     *
     * @param idExpediente El expediente
     * @param idLibreria El id de la Libreria
     * @param idCategoria El id de la Categoria
     * @return Lista de indices con sus datos
     */
    @WebMethod(operationName = "buscarDatosIndicesExpediente")
    public List<Indice> buscarDatosIndicesExpediente(@WebParam(name = "idExpediente") String idExpediente,
            @WebParam(name = "idLibreria") int idLibreria,
            @WebParam(name = "idCategoria") int idCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<Indice> indices = new ArrayList<>();
        ResultSet rsExpediente;
        Indice indice;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            traza.trace("buscando indices del expediente: " + idExpediente, Level.INFO);
            traza.trace("idLibreria: " + idLibreria, Level.INFO);
            traza.trace("idCategoria: " + idCategoria, Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente( ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.setInt(3, idLibreria);
            stored.setInt(4, idCategoria);
            stored.setString(5, "1");
            stored.execute();

            rsExpediente = (ResultSet) stored.getObject(1);

            while (rsExpediente.next()) {

                traza.trace("libreria del expediente " + rsExpediente.getString("libreria"), Level.INFO);
                traza.trace("categoria del expediente " + rsExpediente.getString("categoria"), Level.INFO);
                indice = new Indice();

                indice.setIndice(rsExpediente.getString("indice"));
                indice.setClave(rsExpediente.getString("clave"));
                indice.setCodigo(rsExpediente.getInt("codigo"));
                indice.setIdCategoria(rsExpediente.getInt("id_categoria"));
                indice.setIdIndice(rsExpediente.getInt("id_indice"));
                indice.setTipo(rsExpediente.getString("tipo"));

                if (rsExpediente.getObject("valor") != null) {
                    indice.setValor(rsExpediente.getObject("valor"));
                } else {
                    indice.setValor(sdf.format(rsExpediente.getDate("fecha_indice")));
                }

                indices.add(indice);
            }

        } catch (SQLException ex) {
            traza.trace("error al buscar los indices del expediente: " + idExpediente, Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño lista " + indices.size(), Level.INFO);

        return indices;
    }

    /**
     * Actualiza los inices de un expediente especifico
     *
     * @param expediente Objeto con todos los datos de los indices del
     * expediente
     * @return Verdadero si la actualizacion tuvo exito, falso en caso contrario
     */
    @WebMethod(operationName = "actualizarIndices")
    public boolean actualizarIndices(@WebParam(name = "expediente") Expedientes expediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        ResultSet rsNuevoExpe;
        boolean resp = false, respStored;
        String idExpedienteOld, idExpedienteNew = "";
        int sizeIndices, idLibreria, idCategoria, sizeList, cont = 0;
        List<Indice> indices;
        Date dateInd;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            indices = expediente.getIndices();
            idExpedienteOld = expediente.getExpediente();
            idLibreria = expediente.getIdLibreria();
            idCategoria = expediente.getIdCategoria();

            traza.trace("actualizando los indices del expediente " + idExpedienteOld, Level.INFO);

            sizeIndices = indices.size();
            traza.trace("tamaño lista indices " + sizeIndices, Level.INFO);

            for (Indice indice : indices) {
                if (indice.getClave().equalsIgnoreCase("y")) {
                    idExpedienteNew = indice.getValor().toString();
                }
            }

            traza.trace("expediente actual " + idExpedienteOld, Level.INFO);
            traza.trace("expediente nuevo " + idExpedienteNew, Level.INFO);

            sizeList = indices.size();

            for (Indice indice : indices) {

                traza.trace("objeto CallableStatement " + stored, Level.INFO);

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_actualizar_indices( ?, ?, ?, ?, ?, ?, ? ) }");
                }

                stored.setString(1, idExpedienteNew);
                stored.setString(2, idExpedienteOld);
                stored.setInt(3, indice.getIdIndice());

                if (indice.getTipo().equalsIgnoreCase("fecha")) {

                    dateInd = new Date(sdf.parse(indice.getValor().toString()).getTime());
                    traza.trace("fecha indice " + dateInd, Level.INFO);
                    stored.setString(4, null);
                    stored.setDate(5, dateInd);

                } else {
                    stored.setString(4, indice.getValor().toString());
                    stored.setDate(5, null);
                }

                stored.setInt(6, idLibreria);
                stored.setInt(7, idCategoria);
                stored.execute();

                traza.trace("objeto CallableStatement " + stored, Level.INFO);

                cont++;
            }

            if (sizeList == cont) {
                respStored = true;
                bd.commit();
                bd.desconectar();
            } else {
                respStored = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas modificando las librerias");
            }

            if (respStored) {
                resp = true;

                if (!idExpedienteOld.equalsIgnoreCase(idExpedienteNew)) {

                    //bd.desconectar();
                    stored = null;

                    stored = bd.conectar().prepareCall("{ ? = call f_modificar_indices( ?, ?, ? ) }");
                    stored.registerOutParameter(1, Types.OTHER);
                    stored.setString(2, idExpedienteNew);
                    stored.setString(3, null);
                    stored.setString(4, "0");
                    stored.execute();

                    rsNuevoExpe = (ResultSet) stored.getObject(1);

                    if (rsNuevoExpe.next()) {
                        bd.desconectar();
                        stored = null;

                        stored = bd.conectar().prepareCall("{ ? = call f_modificar_indices( ?, ?, ? ) }");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, null);
                        stored.setString(3, idExpedienteOld);
                        stored.setString(4, "1");
                        //stored.registerOutParameter(5, Types.VARCHAR);
                        stored.execute();

                        bd.commit();
                        resp = true;
                    }

                }
            } else {
                bd.rollback();
                resp = false;
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            try {
                bd.rollback();
                traza.trace("problemas en la base de datos", Level.ERROR, e);
            } catch (SQLException ex) {
                traza.trace("problemas en el rollback de actualizar los indices ", Level.INFO, ex);
            }
        } catch (SQLException ex) {
            resp = false;
            traza.trace("error al actualizar los indices del expediente " + expediente.getExpediente(), Level.ERROR, ex);
        } catch (ParseException ex) {
            traza.trace("problemas al realizar el parser de la fecha", Level.ERROR, ex);
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
