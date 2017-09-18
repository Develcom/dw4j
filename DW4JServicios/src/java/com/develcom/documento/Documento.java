/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.documento;

import com.develcom.dao.DatoAdicional;
import com.develcom.dao.InfoDocumento;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "Documento")
public class Documento {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(Documento.class);

    /**
     * Busca los Documentos digitalizados
     *
     * @param idDocumento El id del Documentos
     * @param idExpediente EL expediente
     * @param idCategoria El id de la Categoria
     * @param idSubCategoria El id de la SubCategoria
     * @param estatusDocumento
     * @param estatusAprobado
     * @param reDigitalizar
     * @return Lista con la Información del documento
     */
    @WebMethod(operationName = "buscarInformacionDocumento")
    public List<com.develcom.dao.InfoDocumento> buscarInformacionDocumento(@WebParam(name = "idDocumento") List<Integer> idDocumento,
            @WebParam(name = "idExpediente") String idExpediente,
            @WebParam(name = "idCategoria") int idCategoria,
            @WebParam(name = "idSubCategoria") int idSubCategoria,
            @WebParam(name = "estatusDocumento") int estatusDocumento,
            @WebParam(name = "estatusAprobado") int estatusAprobado,
            @WebParam(name = "reDigitalizar") boolean reDigitalizar) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsInfoDoc;
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        List<InfoDocumento> infoDocument = new ArrayList<>();
        List<DatoAdicional> datosAdicionales = new ArrayList<>();
        com.develcom.dao.InfoDocumento infoDocumento;
        String ids = "";
        int cont = 0;

        traza.trace("Buscando informacion de los documentos con el idDocumento: " + idDocumento, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el idExpediente: " + idExpediente, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el idCategoria: " + idCategoria, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el idSubCategoria: " + idSubCategoria, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el estatusDocumento: " + estatusDocumento, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el estatusAprobado: " + estatusAprobado, Level.INFO);
        traza.trace("Buscando informacion de los documentos con reDigitalizar: " + reDigitalizar, Level.INFO);

        try {

            for (Integer in : idDocumento) {

                ids = ids.trim() + ",";
                if (cont == 0) {
                    //borro la primera coma
                    ids = ids.replace(",", "");
                    //ids=ids+"'";
                    ids = ids + in;
                } else {
                    ids = ids + in;
                }
                cont++;
            }

            //ids=ids+"'";
            traza.trace("id documentos " + ids, Level.INFO);

//            for (Integer in : idDocumento) {
//                traza.trace("idDcoumento para la consulta " + in, Level.INFO);
            stored = bd.conectar().prepareCall(" { ? = call f_buscar_infodocumento( ?, ?, ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.setString(3, ids);
//                stored.setInt(3, in);
            stored.setInt(4, estatusDocumento);
            if (reDigitalizar) {
                stored.setString(5, "1");
            } else {
                stored.setString(5, "0");
            }
            stored.setInt(6, estatusAprobado);
            stored.execute();

            rsInfoDoc = (ResultSet) stored.getObject(1);

            while (rsInfoDoc.next()) {

                String documento = rsInfoDoc.getString("tipoDoc");
                int idInfoDoc = rsInfoDoc.getInt("ID_INFODOCUMENTO");
                int idTipoDoc = rsInfoDoc.getInt("ID_DOCUMENTO");
                int version = rsInfoDoc.getInt("VERSION");
                int num = rsInfoDoc.getInt("NUMERO_DOCUMENTO");

                traza.trace("idInfoDocumento encontrado: " + idInfoDoc, Level.INFO);
                traza.trace("documento encontrado: " + documento, Level.INFO);
                traza.trace("version doc: " + version, Level.INFO);
                traza.trace("numero doc: " + num, Level.INFO);

                infoDocumento = new InfoDocumento();

                infoDocumento.setIdInfoDocumento(idInfoDoc);
                infoDocumento.setIdDocumento(idTipoDoc);
                infoDocumento.setTipoDocumento(documento);
                infoDocumento.setRutaArchivo(rsInfoDoc.getString("RUTA_ARCHIVO"));
                infoDocumento.setNombreArchivo(rsInfoDoc.getString("NOMBRE_ARCHIVO"));
                infoDocumento.setVersion(version);
                infoDocumento.setIdExpediente(rsInfoDoc.getString("ID_EXPEDIENTE"));
                infoDocumento.setNumeroDocumento(num);
                infoDocumento.setFechaVencimiento(rsInfoDoc.getDate("FECHA_VENCIMIENTO"));
                infoDocumento.setDatoAdicional(rsInfoDoc.getString("DATO_ADICIONAL"));
                infoDocumento.setFechaDigitalizacion(rsInfoDoc.getDate("FECHA_DIGITALIZACION"));
                infoDocumento.setEstatusDocumento(rsInfoDoc.getString("ESTATUS_DOCUMENTO"));
                infoDocumento.setEstatus(rsInfoDoc.getInt("idStatus"));
                infoDocumento.setUsuarioDigitalizo(rsInfoDoc.getString("USUARIO_DIGITALIZO"));
                infoDocumento.setFechaAprobacion(rsInfoDoc.getDate("FECHA_APROBACION"));
                infoDocumento.setUsuarioAprobacion(rsInfoDoc.getString("USUARIO_APROBACION"));
                infoDocumento.setFechaRechazo(rsInfoDoc.getDate("FECHA_RECHAZO"));
                infoDocumento.setUsuarioRechazo(rsInfoDoc.getString("USUARIO_RECHAZO"));
                infoDocumento.setMotivoRechazo(rsInfoDoc.getString("MOTIVO_RECHAZO"));
                infoDocumento.setCausaRechazo(rsInfoDoc.getString("CAUSA_RECHAZO"));

                if (rsInfoDoc.getInt("RE_DIGITALIZADO") == 1) {
                    infoDocumento.setReDigitalizo(true);
                } else {
                    infoDocumento.setReDigitalizo(false);
                }

                infoDocumento.setFormato(rsInfoDoc.getString("FORMATO"));
                infoDocumento.setFechaActual(new GregorianCalendar());

                if (rsInfoDoc.getInt("datipodoc") == 1) {
                    infoDocumento.setTipoDocDatoAdicional(true);
                } else {
                    infoDocumento.setTipoDocDatoAdicional(false);
                }

                infoDocument.add(infoDocumento);
            }

//            }
            traza.trace("agregando los datos adicionales", Level.INFO);
            for (InfoDocumento id : infoDocument) {

                if (id.isTipoDocDatoAdicional()) {
                    datosAdicionales = buscarValorDatoAdicional(id.getIdDocumento(), idExpediente, id.getNumeroDocumento(), id.getVersion());
                    traza.trace("datos adicionales del documento " + id.getTipoDocumento() + " numero " + id.getNumeroDocumento() + " version " + id.getVersion() + " size " + datosAdicionales.size() + " expediente " + idExpediente, Level.INFO);
                    if (!datosAdicionales.isEmpty()) {
                        id.setLsDatosAdicionales(datosAdicionales);
                        infoDocumentos.add(id);
                    } else {
                        infoDocumentos.add(id);
                        datosAdicionales.clear();
                    }
                } else {
                    traza.trace("documento sin dato adicional " + id.getTipoDocumento(), Level.INFO);
                    infoDocumentos.add(id);
                }
            }

        } catch (SQLException ex) {
            traza.trace("Error en la consulta de la informacion para los tipos de documentos", Level.INFO, ex);
            infoDocumentos.clear();
        } catch (Exception e) {
            traza.trace("error grave en la consulta de la informacion de los tipos de documentos", Level.FATAL, e);
            infoDocumentos.clear();
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de informacion de documentos " + infoDocumentos.size(), Level.INFO);
        return infoDocumentos;
    }

    /**
     * Busca la ultima version del documento
     *
     * @param idDocumento El id del Documento
     * @param idExpediente El expediente
     * @param numeroDocumento El numero del Documento
     * @return La version del documento
     */
    @WebMethod(operationName = "buscarUltimaVersionDoc")
    public int buscarUltimaVersionDoc(@WebParam(name = "idDocumento") int idDocumento, @WebParam(name = "idExpediente") String idExpediente, @WebParam(name = "numeroDocumento") int numeroDocumento) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        int version = 0;
        ResultSet rsVersion;

        try {
            traza.trace("expediente " + idExpediente, Level.INFO);
            traza.trace("idDocumento " + idDocumento, Level.INFO);
            traza.trace("numero del documento " + numeroDocumento, Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_ultima_version( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idDocumento);
            stored.setString(3, idExpediente);
            stored.setInt(4, numeroDocumento);
            stored.execute();

            rsVersion = (ResultSet) stored.getObject(1);

            if (rsVersion.next()) {
                version = rsVersion.getInt("version");
                traza.trace("ultima version " + version, Level.INFO);
            }

        } catch (SQLException ex) {
            traza.trace("problemas al buscar la ultima version del documento", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        return version;
    }

    /**
     * Buscar la informacion contenida en cada dato adicional
     *
     * @param idTipoDocumento Identificador del tipo de documento
     * @param idExpediente Identificador del expediente
     * @param numeroDocumento Numero consecutivo del documento
     * @param version Version del documento
     * @return Listado con toda la informacion de los datos adicionales
     */
    @WebMethod(operationName = "buscarValorDatoAdicional")
    public List<DatoAdicional> buscarValorDatoAdicional(@WebParam(name = "idTipoDocumento") int idTipoDocumento,
            @WebParam(name = "idExpediente") String idExpediente,
            @WebParam(name = "numeroDocumento") int numeroDocumento,
            @WebParam(name = "version") int version) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsDatoAdicional;
        List<DatoAdicional> lsDatosAdicionalesValores = new ArrayList<>();
        DatoAdicional da;
        int registro = 1;

        try {
            traza.trace("id tipo documento " + idTipoDocumento, Level.INFO);
            traza.trace("expediente " + idExpediente, Level.INFO);
            traza.trace("numero del documento " + numeroDocumento, Level.INFO);
            traza.trace("version del documento " + version, Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_valor_dato_adicional( ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idTipoDocumento);
            stored.setString(3, idExpediente);
            stored.setInt(4, numeroDocumento);
            stored.setInt(5, version);
            stored.execute();

            rsDatoAdicional = (ResultSet) stored.getObject(1);

            while (rsDatoAdicional.next()) {
                traza.trace("indice dato adicional " + rsDatoAdicional.getString("INDICE_ADICIONAL") + " su valor " + rsDatoAdicional.getString("VALOR"), Level.INFO);

                da = new DatoAdicional();
                
                da.setIdValor(rsDatoAdicional.getInt("id_valor"));
                da.setIdDatoAdicional(rsDatoAdicional.getInt("ID_DATO_ADICIONAL"));
                da.setIndiceDatoAdicional(rsDatoAdicional.getString("INDICE_ADICIONAL"));
                da.setTipo(rsDatoAdicional.getString("TIPO"));
                da.setValor(rsDatoAdicional.getString("VALOR"));
                da.setIdTipoDocumento(rsDatoAdicional.getInt("ID_DOCUMENTO"));
                da.setNumeroDocumento(rsDatoAdicional.getInt("NUMERO"));
                da.setVersion(rsDatoAdicional.getInt("VERSION"));
                da.setCodigo(rsDatoAdicional.getInt("CODIGO"));
                lsDatosAdicionalesValores.add(da);

                traza.trace("registro " + registro, Level.INFO);
                registro++;
            }

        } catch (SQLException ex) {
            traza.trace("problemas al buscar los datos adicionales y sus valores", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("tamaño de la lista de datos adicionale y sus valores " + lsDatosAdicionalesValores.size(), Level.INFO);
        return lsDatosAdicionalesValores;
    }
}
