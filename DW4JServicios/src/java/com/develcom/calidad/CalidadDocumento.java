/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.calidad;

import com.develcom.administracion.AdministracionBusquedas;
import com.develcom.dao.Indices;
import com.develcom.dao.Configuracion;
import com.develcom.dao.ConsultaDinamica;
import com.develcom.dao.Indice;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.mantenimiento.Mantenimientos;
import com.develcom.tools.BaseDato;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "CalidadDocumento")
public class CalidadDocumento {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(CalidadDocumento.class);

    /**
     * Realiza busqueda de expedientes para su aprobación o rechazo segun el
     * usuario si es de la fabrica o no
     *
     * @param usuario Identificador del usuario
     * @param fechaDesde Rango de de fecha de inicio
     * @param fechaHasta Rango de fecha final
     * @param estatusDocumento El estatus del documento debe ser pendiente
     * @param idCategoria Identificador de la categoria
     * @param idExpediente Identificador del expediente
     * @return Listado de expedientes consultados
     */
    @WebMethod(operationName = "buscarExpedientesPendientes")
    public List<ConsultaDinamica> buscarExpedientesPendientes(@WebParam(name = "usuario") String usuario,
            @WebParam(name = "fechaDesde") Date fechaDesde,
            @WebParam(name = "fechaHasta") Date fechaHasta,
            @WebParam(name = "estatusDocumento") int estatusDocumento,
            @WebParam(name = "idCategoria") int idCategoria,
            @WebParam(name = "idExpediente") String idExpediente) {

        Configuracion config = new Mantenimientos().buscarMantenimiento();

        traza.trace("usuario " + usuario, Level.INFO);

        if (config.isFabrica()) {
            traza.trace("buscando documentos con cola de trabajo (fabrica)", Level.INFO);
            return buscarDocumentosFabrica(usuario, fechaDesde, fechaHasta, estatusDocumento, idCategoria, idExpediente);
        } else {
            traza.trace("buscando documentos sin cola de trabajo (no fabrica)", Level.INFO);
            return buscarDocumentosNoFabrica(fechaDesde, fechaHasta, estatusDocumento, idCategoria, idExpediente);
        }
    }

    /**
     * Realiza busqueda de expedientes si esta activada la opcion de separar los
     * usuario que pertenecen a la fabrica de digitalizacion o no
     *
     * @param usuario Identificador del usuario
     * @param fechaDesde Rango de de fecha de inicio
     * @param fechaHasta Rango de fecha final
     * @param estatusDocumento El estatus del documento debe ser pendiente
     * @param idCategoria Identificador de la categoria
     * @param idExpediente Identificador del expediente
     * @return Listado de expedientes consultados
     */
    private List<ConsultaDinamica> buscarDocumentosFabrica(String usuario, Date fechaDesde,
            Date fechaHasta, int estatusDocumento,
            int idCategoria, String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ResultSet rsInfoDoc;
        List<ConsultaDinamica> consultasDinamicas = new ArrayList<>();
        ConsultaDinamica consultaDinamica = new ConsultaDinamica();
        List<Indice> indices = new ArrayList<>();
        Indice indice;
        int registro = 1;

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_fabrica( ?, ?, ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, usuario);
            try {
                stored.setDate(3, new java.sql.Date(fechaDesde.getTime()));
            } catch (NullPointerException e) {
                stored.setDate(3, null);
            }
            try {
                stored.setDate(4, new java.sql.Date(fechaHasta.getTime()));
            } catch (NullPointerException e) {
                stored.setDate(4, null);
            }
            stored.setInt(5, estatusDocumento);
            stored.setInt(6, idCategoria);
            stored.setString(7, idExpediente);
            stored.execute();

            rsInfoDoc = (ResultSet) stored.getObject(1);

            while (rsInfoDoc.next()) {

                traza.trace("el usuario " + usuario + " pertenece a la fabrica " + rsInfoDoc.getString("fabrica"), Level.INFO);

                traza.trace("registro " + registro, Level.INFO);
                traza.trace("expediente " + rsInfoDoc.getString("expediente"), Level.INFO);
                traza.trace("id indice " + rsInfoDoc.getString("id_indice"), Level.INFO);
                traza.trace("indice " + rsInfoDoc.getString("indice"), Level.INFO);
                traza.trace("valor " + rsInfoDoc.getString("valor"), Level.INFO);
                traza.trace("clave " + rsInfoDoc.getString("clave"), Level.INFO);
                traza.trace("tipo " + rsInfoDoc.getString("tipo"), Level.INFO);

                if (!rsInfoDoc.getString("clave").equalsIgnoreCase("o")) {
                    indice = new Indice();
                    indice.setIdIndice(rsInfoDoc.getInt("id_indice"));
                    indice.setIndice(rsInfoDoc.getString("indice"));

                    if (rsInfoDoc.getString("clave") != null) {
                        indice.setClave(rsInfoDoc.getString("clave"));
                    } else {
                        indice.setClave("");
                    }

                    indice.setTipo(rsInfoDoc.getString("tipo"));

                    if (rsInfoDoc.getObject("valor") != null) {
                        indice.setValor(rsInfoDoc.getObject("valor"));
                    } else {
                        indice.setValor(sdf.format(rsInfoDoc.getDate("fecha_indice")));
                    }

                    indices.add(indice);
                }

                registro++;
            }

            traza.trace("tamaño total lista indices " + indices.size(), Level.INFO);

            if (!indices.isEmpty()) {
                consultaDinamica.setExiste(true);
                consultaDinamica.setIndices(indices);
                consultasDinamicas.add(consultaDinamica);
                rsInfoDoc.close();
            } else {
                consultaDinamica.setExiste(false);
                consultasDinamicas.add(consultaDinamica);
            }

        } catch (SQLException ex) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultasDinamicas.add(consultaDinamica);
            traza.trace("error al buscar expedientes fabrica", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la consulta de expediente sin fabrica " + consultasDinamicas.size(), Level.INFO);
        return consultasDinamicas;
    }

    /**
     * Realiza busqueda de expedientes si esta desactivada la opcion de separar
     * los usuario que pertenecen a la fabrica de digitalizacion o no
     *
     * @param fechaDesde Rango de de fecha de inicio
     * @param fechaHasta Rango de fecha final
     * @param estatusDocumento El estatus del documento debe ser pendiente
     * @param idCategoria Identificador de la categoria
     * @param idExpediente Identificador del expediente
     * @return Listado de expedientes consultados
     */
    private List<ConsultaDinamica> buscarDocumentosNoFabrica(Date fechaDesde,
            Date fechaHasta, int estatusDocumento,
            int idCategoria, String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ResultSet rsInfoDoc;
        List<Indice> camposIndices;
        List<ConsultaDinamica> consultasDinamicas = new ArrayList<>();
        ConsultaDinamica consultaDinamica = new ConsultaDinamica();
        List<Indice> indices = new ArrayList<>();
        Indice indice;
        int registro = 1;

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_no_fabrica( ?, ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            try {
                stored.setDate(2, new java.sql.Date(fechaDesde.getTime()));
            } catch (NullPointerException e) {
                stored.setDate(2, null);
            }
            try {
                stored.setDate(3, new java.sql.Date(fechaHasta.getTime()));
            } catch (NullPointerException e) {
                stored.setDate(3, null);
            }
            stored.setInt(4, estatusDocumento);
            stored.setInt(5, idCategoria);
            stored.setString(6, idExpediente);
            stored.execute();

            camposIndices = new AdministracionBusquedas().buscarIndices(idCategoria);

            if (!camposIndices.isEmpty()) {

                rsInfoDoc = (ResultSet) stored.getObject(1);

                while (rsInfoDoc.next()) {

                    traza.trace("registro " + registro, Level.INFO);
                    traza.trace("expediente " + rsInfoDoc.getString("expediente"), Level.INFO);
                    traza.trace("id indice " + rsInfoDoc.getString("id_indice"), Level.INFO);
                    traza.trace("indice " + rsInfoDoc.getString("indice"), Level.INFO);
                    traza.trace("valor " + rsInfoDoc.getString("valor"), Level.INFO);
                    traza.trace("clave " + rsInfoDoc.getString("clave"), Level.INFO);
                    traza.trace("tipo " + rsInfoDoc.getString("tipo"), Level.INFO);

                    if (!rsInfoDoc.getString("clave").equalsIgnoreCase("o")) {
                        indice = new Indice();
                        indice.setIdIndice(rsInfoDoc.getInt("id_indice"));
                        indice.setIndice(rsInfoDoc.getString("indice"));

                        if (rsInfoDoc.getString("clave") != null) {
                            indice.setClave(rsInfoDoc.getString("clave"));
                        } else {
                            indice.setClave("");
                        }

                        indice.setTipo(rsInfoDoc.getString("tipo"));

                        if (rsInfoDoc.getObject("valor") != null) {
                            indice.setValor(rsInfoDoc.getObject("valor"));
                        } else {
                            indice.setValor(sdf.format(rsInfoDoc.getDate("fecha_indice")));
                        }

                        indices.add(indice);
                    }

                    registro++;
                }

                traza.trace("tamaño total lista indices " + indices.size(), Level.INFO);

                if (!indices.isEmpty()) {
                    consultaDinamica.setExiste(true);
                    consultaDinamica.setIndices(indices);
                    consultasDinamicas.add(consultaDinamica);
                    rsInfoDoc.close();
                } else {

                    consultaDinamica.setExiste(false);
                    consultasDinamicas.add(consultaDinamica);
                }

            } else {
                throw new DW4JServiciosException("objeto argumentos nulo o vacio");
            }

        } catch (SQLException ex) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultasDinamicas.add(consultaDinamica);
            traza.trace("error al buscar infodocumento", Level.ERROR, ex);
        } catch (DW4JServiciosException e) {
            traza.trace("problema al buscar los argumentos", Level.ERROR, e);
            consultasDinamicas.clear();
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("tamaño de la consulta de expediente sin fabrica " + consultasDinamicas.size(), Level.INFO);
        return consultasDinamicas;
    }

    /**
     * Pasa un tipo de documento de estatus pendiente a aprobado
     *
     * @param idInfoDocumento Identificador de la informacion del tipo de
     * documento
     * @param usuario Identificador del usuario
     * @return Verdadero si el cambio fue exitoso, falso en caso contrario
     */
    @WebMethod(operationName = "aprobarDocumento")
    public boolean aprobarDocumento(@WebParam(name = "idInfoDocumento") int idInfoDocumento, @WebParam(name = "usuario") String usuario) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        boolean resp = false, exec;

        try {

            traza.trace("aprobando el documento " + idInfoDocumento, Level.INFO);

            traza.trace("actualizando el documento ", Level.INFO);
            stored = bd.conectar().prepareCall(" { call p_aprobar_documento( ?, ? ) } ");
            stored.setInt(1, idInfoDocumento);
            stored.setString(2, usuario);
            exec = stored.execute();

            resp = true;
            traza.trace("respuesta del procedimiento aprobar documento " + exec, Level.INFO);
            bd.commit();
            bd.desconectar();

        } catch (SQLException e) {
            traza.trace("problemas al aprobar el documento", Level.INFO, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta de aprobar el documento " + resp, Level.INFO);
        return resp;
    }

    /**
     * Busca las causas de rechazos registradas en base de datos para llenar un
     * combo
     *
     * @return Lista con las causas de rechazos
     */
    @WebMethod(operationName = "buscarCausasRechazo")
    public List<java.lang.String> buscarCausasRechazo() {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<String> listCausa = new ArrayList<>();
        ResultSet rsCausa;

        try {
            stored = bd.conectar().prepareCall("{ ? = call f_buscar_causas_rechazo() }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.execute();

            rsCausa = (ResultSet) stored.getObject(1);

            while (rsCausa.next()) {
                listCausa.add(rsCausa.getString("CAUSA"));
            }

        } catch (SQLException e) {
            traza.trace("problemas al buscar los las causa de rechazos", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de las causas de rechazo " + listCausa.size(), Level.INFO);
        return listCausa;
    }

    /**
     * Cambia el estado de un tipo de documento de pendiente a rechazado
     *
     * @param idInfoDocumento Identificador de la información del documento
     * @param usuario Identificador del usuario
     * @param causa La causa del rechazo
     * @param motivo El motivo del rechazo
     * @return
     */
    @WebMethod(operationName = "rechazarDocumento")
    public boolean rechazarDocumento(@WebParam(name = "idInfoDocumento") int idInfoDocumento,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "causa") String causa,
            @WebParam(name = "motivo") String motivo) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        boolean resp = false;

        try {

            traza.trace("rechazando el documento " + idInfoDocumento, Level.INFO);

            traza.trace("actualizando el documento ", Level.INFO);
            stored = bd.conectar().prepareCall(" { call p_rechazar_documento( ?, ?, ?, ? ) }");
            stored.setInt(1, idInfoDocumento);
            stored.setString(2, usuario);
            stored.setString(3, causa);
            stored.setString(4, motivo);
            stored.execute();

            bd.commit();
            bd.desconectar();
            resp = true;

        } catch (SQLException e) {
            traza.trace("problemas al rechazar el documento", Level.INFO, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta de rechazar el documento " + resp, Level.INFO);
        return resp;
    }
}
