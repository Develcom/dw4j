/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.elimina;

import com.develcom.dao.DatoAdicional;
import com.develcom.dao.InfoDocumento;
import com.develcom.documento.Documento;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.tools.Propiedades;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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
@WebService(serviceName = "EliminaDocumento")
public class EliminaDocumento {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(EliminaDocumento.class);

    /**
     * Elimina el fisico de un tipo de documento
     *
     * @param elimnaDocuento Un objeto con la informacion necesaria para
     * eliminar un tipo de documento
     * @return Verdadero si se elimino el tipo de documento, falso en caso
     * contrario
     */
    @WebMethod(operationName = "eliminarDocumento")
    public boolean eliminarDocumento(@WebParam(name = "elimnaDocuento") com.develcom.dao.EliminaDocumento elimnaDocuento) {

        BaseDato bd = new BaseDato();
        BaseDato bdDA = new BaseDato();
        CallableStatement stored, storedDA = null;
        boolean resp = false;
        InfoDocumento infoDocumento;
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        List<InfoDocumento> lstInfoDocumentos = new ArrayList<>();
        List<InfoDocumento> lstInfoDoc = new ArrayList<>();
        List<DatoAdicional> datosAdicionales = new ArrayList<>();
        List<DatoAdicional> datosAdicionalesNew = new ArrayList<>();
        ResultSet rsDatos;
        Properties prop;
        String eliminaArchivo;
        File delFile;
        boolean del = false, elimino, flag;
        int numDocEliminado, guia = 1, version, sizeReindex, sizeList, cont = 0, con = 0;

        try {
            prop = Propiedades.cargarPropiedadesWS();

            traza.trace("buscando la ruta y nombre del archivo", Level.INFO);
            stored = bd.conectar().prepareCall("{ ? = call f_buscar_infodocumento_elimina( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, elimnaDocuento.getInfoDocumento().getIdInfoDocumento());
            stored.setInt(3, 0);
            stored.setString(4, "");
            stored.execute();

            rsDatos = (ResultSet) stored.getObject(1);

            if (rsDatos.next()) {
                infoDocumento = new InfoDocumento();
                infoDocumento.setIdDocumento(rsDatos.getInt("ID_DOCUMENTO"));
                infoDocumento.setRutaArchivo(rsDatos.getString("RUTA_ARCHIVO"));
                infoDocumento.setNombreArchivo(rsDatos.getString("NOMBRE_ARCHIVO"));
                infoDocumento.setNumeroDocumento(rsDatos.getInt("NUMERO_DOCUMENTO"));

                numDocEliminado = infoDocumento.getNumeroDocumento();
                traza.trace("tipo de documento a eliminar " + elimnaDocuento.getInfoDocumento().getTipoDocumento() + " " + numDocEliminado, Level.INFO);
                eliminaArchivo = prop.getProperty("rutaRaiz") + infoDocumento.getRutaArchivo() + "/" + infoDocumento.getNombreArchivo();
                delFile = new File(eliminaArchivo);

                traza.trace("eliminando el archivo", Level.INFO);
                if (delFile.exists()) {
                    del = delFile.delete();
                    traza.trace("elimino el archivo? " + del, Level.INFO);
                } else {
                    traza.trace("no exixte el fisico del documento " + elimnaDocuento.getInfoDocumento().getTipoDocumento(), Level.INFO);
                    del = true;
                }
            } else {
                throw new DW4JServiciosException("problemas al buscar el puntero del fisico del archivo");
            }

            if (del) {

                bd.commit();
                bd.desconectar();

                traza.trace("eliminado el registro del archivo", Level.INFO);
                traza.trace("id infodocumento a eliminar " + elimnaDocuento.getInfoDocumento().getIdInfoDocumento(), Level.INFO);

                stored = null;
                stored = bd.conectar().prepareCall(" { call p_eliminar_infodocumento( ? ) } ");
                stored.setInt(1, elimnaDocuento.getInfoDocumento().getIdInfoDocumento());
                elimino = stored.execute();

                traza.trace("buscando los datos adicionales del documento " + elimnaDocuento.getInfoDocumento().getTipoDocumento(), Level.INFO);
                datosAdicionales = new Documento().buscarValorDatoAdicional(elimnaDocuento.getInfoDocumento().getIdDocumento(), elimnaDocuento.getIdExpediente(),
                        elimnaDocuento.getInfoDocumento().getNumeroDocumento(), elimnaDocuento.getInfoDocumento().getVersion());
                if (!datosAdicionales.isEmpty()) {
                    sizeList = datosAdicionales.size();

                    for (DatoAdicional da : datosAdicionales) {

                        if (storedDA == null) {
                            storedDA = bdDA.conectar().prepareCall(" { call p_eliminar_valordatadic( ?, ?, ? ) } ");
                        }

                        traza.trace("eliminando el valor del dato adicional " + da.getIndiceDatoAdicional() + " valor " + da.getValor() + " id valor " + da.getIdValor(), Level.INFO);

                        storedDA.setInt(1, da.getIdValor());
                        storedDA.setInt(2, da.getVersion());
                        storedDA.setInt(3, da.getNumeroDocumento());
                        elimino = storedDA.execute();

                        cont++;
                    }

                    if (sizeList == cont) {
                        bd.commit();
                        bd.desconectar();
                        stored = null;

                        bdDA.commit();
                        bdDA.desconectar();
                        storedDA = null;

                        sizeList = 0;
                        cont = 0;
                    } else {
                        bd.rollback();
                        bdDA.rollback();
                        sizeList = 0;
                        cont = 0;
                        throw new DW4JServiciosException("problemas al eliminar el registro del documento ");
                    }
                } else {
                    bd.commit();
                    bd.desconectar();
                    stored = null;

                    sizeList = 0;
                    cont = 0;
                }
                datosAdicionales.clear();
                traza.trace("elimino el registro " + elimino, Level.INFO);

                traza.trace("re-numerando los registros del tipo de documento", Level.INFO);

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_infodocumento_elimina( ?, ?, ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setInt(2, 0);
                stored.setInt(3, elimnaDocuento.getInfoDocumento().getIdDocumento());
                stored.setString(4, elimnaDocuento.getIdExpediente());
                stored.execute();

                rsDatos = (ResultSet) stored.getObject(1);

                while (rsDatos.next()) {
                    infoDocumento = new InfoDocumento();

                    infoDocumento.setIdInfoDocumento(rsDatos.getInt("ID_INFODOCUMENTO"));
                    infoDocumento.setIdDocumento(rsDatos.getInt("ID_DOCUMENTO"));
                    infoDocumento.setNumeroDocumento(rsDatos.getInt("NUMERO_DOCUMENTO"));
                    infoDocumento.setIdExpediente(rsDatos.getString("ID_EXPEDIENTE"));
                    infoDocumento.setVersion(rsDatos.getInt("VERSION"));

                    if (rsDatos.getString("dato_adicional").equalsIgnoreCase("1")) {
                        infoDocumento.setTipoDocDatoAdicional(true);
                    } else {
                        infoDocumento.setTipoDocDatoAdicional(false);
                    }

                    infoDocumento.setTipoDocumento(rsDatos.getString("tipo_documento"));

                    lstInfoDocumentos.add(infoDocumento);
                }

                traza.trace("lista de infodocumento para re-indexar vacia " + lstInfoDocumentos.isEmpty(), Level.INFO);

                if (!lstInfoDocumentos.isEmpty()) {

                    traza.trace("agregando los datos adicionales", Level.INFO);

                    for (InfoDocumento id : lstInfoDocumentos) {

                        if (id.isTipoDocDatoAdicional()) {
                            datosAdicionales = new Documento().buscarValorDatoAdicional(id.getIdDocumento(), elimnaDocuento.getIdExpediente(), id.getNumeroDocumento(), id.getVersion());
                            traza.trace("datos adicionales del documento " + id.getTipoDocumento() + " numero " + id.getNumeroDocumento() + " version " + id.getVersion() + " size " + datosAdicionales.size() + " expediente " + elimnaDocuento.getIdExpediente(), Level.INFO);
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

                    sizeReindex = infoDocumentos.size();
                    traza.trace("tamaño de la lista para la re-indexacion " + sizeReindex, Level.INFO);

                    for (int i = 0; i < infoDocumentos.size(); i++) {

                        InfoDocumento infoDoc = infoDocumentos.get(i);
                        datosAdicionalesNew = infoDoc.getLsDatosAdicionales();
                        flag = true;
                        traza.trace("indice lista " + i + " numero guia " + guia + " numero documento " + infoDoc.getNumeroDocumento()
                                + " version " + infoDoc.getVersion(), Level.INFO);
                        version = infoDoc.getVersion();

                        if (guia == infoDoc.getNumeroDocumento()) {
                            if (version == 0) {
                                lstInfoDoc.add(infoDoc);
                                guia++;
                            } else {
                                guia--;
                                infoDoc.setNumeroDocumento(guia);
                                if ((infoDoc.isTipoDocDatoAdicional()) || datosAdicionalesNew != null) {
                                    for (int j = 0; j < datosAdicionalesNew.size(); j++) {
                                        datosAdicionalesNew.get(j).setNumeroDocumento(guia);
                                    }
                                }
                                infoDoc.setLsDatosAdicionales(datosAdicionalesNew);
                                lstInfoDoc.add(infoDoc);
                                i++;
                                version++;
                                if (i == sizeReindex) {
                                    break;
                                }
                                infoDoc = infoDocumentos.get(i);
                                traza.trace("(if despues del incremento) indice lista " + i + " var version " + version
                                        + " numero documento " + infoDoc.getNumeroDocumento() + " version " + infoDoc.getVersion() + " en el sub-bucle", Level.INFO);

                                while (flag) {
                                    if (infoDoc.getVersion() == version) {
                                        infoDoc.setNumeroDocumento(guia);
                                        if ((infoDoc.isTipoDocDatoAdicional()) || datosAdicionalesNew != null) {
                                            for (int j = 0; j < datosAdicionalesNew.size(); j++) {
                                                datosAdicionalesNew.get(j).setNumeroDocumento(guia);
                                            }
                                        }
                                        infoDoc.setLsDatosAdicionales(datosAdicionalesNew);
                                        lstInfoDoc.add(infoDoc);
                                        i++;
                                        version++;
                                        if (i == sizeReindex) {
                                            break;
                                        }
                                        infoDoc = infoDocumentos.get(i);
                                    } else {
                                        i--;
                                        guia++;
                                        flag = false;
                                    }
                                    traza.trace("(if) indice lista " + i + " var version " + version + " numero documento " + infoDoc.getNumeroDocumento()
                                            + " version " + infoDoc.getVersion() + " en el sub-bucle", Level.INFO);
                                }
                            }
                        } else {
                            if (version == 0) {
                                infoDoc.setNumeroDocumento(guia);
                                if ((infoDoc.isTipoDocDatoAdicional()) || datosAdicionalesNew != null) {
                                    for (int j = 0; j < datosAdicionalesNew.size(); j++) {
                                        datosAdicionalesNew.get(j).setNumeroDocumento(guia);
                                    }
                                }
                                infoDoc.setLsDatosAdicionales(datosAdicionalesNew);
                                lstInfoDoc.add(infoDoc);
                                guia++;
                            } else {
                                guia--;
                                infoDoc.setNumeroDocumento(guia);
                                if ((infoDoc.isTipoDocDatoAdicional()) || datosAdicionalesNew != null) {
                                    for (int j = 0; j < datosAdicionalesNew.size(); j++) {
                                        datosAdicionalesNew.get(j).setNumeroDocumento(guia);
                                    }
                                }
                                infoDoc.setLsDatosAdicionales(datosAdicionalesNew);
                                lstInfoDoc.add(infoDoc);
                                i++;
                                version++;
                                if (i == sizeReindex) {
                                    break;
                                }
                                infoDoc = infoDocumentos.get(i);
                                traza.trace("(else despues del incremento) indice lista " + i + " var version " + version
                                        + " numero documento " + infoDoc.getNumeroDocumento() + " version " + infoDoc.getVersion() + " en el sub-bucle", Level.INFO);

                                while (flag) {
                                    if (infoDoc.getVersion() == version) {
                                        infoDoc.setNumeroDocumento(guia);
                                        if ((infoDoc.isTipoDocDatoAdicional()) || datosAdicionalesNew != null) {
                                            for (int j = 0; j < datosAdicionalesNew.size(); j++) {
                                                datosAdicionalesNew.get(j).setNumeroDocumento(guia);
                                            }
                                        }
                                        infoDoc.setLsDatosAdicionales(datosAdicionalesNew);
                                        lstInfoDoc.add(infoDoc);
                                        i++;
                                        version++;
                                        if (i == sizeReindex) {
                                            break;
                                        }
                                        infoDoc = infoDocumentos.get(i);
                                    } else {
                                        i--;
                                        guia++;
                                        flag = false;
                                    }
                                    traza.trace("(else) indice lista " + i + " var version " + version + " numero documento " + infoDoc.getNumeroDocumento()
                                            + " version " + infoDoc.getVersion() + " en el sub-bucle", Level.INFO);
                                }
                            }
                        }
                        traza.trace("(despues del proceso) indice lista " + i + " guia " + guia + " numero del documento " + infoDoc.getNumeroDocumento() + " version " + infoDoc.getVersion(), Level.INFO);
                    }

                    traza.trace("tamaño de la lista de infodocumento para actualizar la secuencia de numeros " + lstInfoDoc.size(), Level.INFO);

                    stored = null;
                    bd.desconectar();
                    flag = true;
                    cont = 0;

                    for (InfoDocumento infoDoc : lstInfoDoc) {

                        if (stored == null) {
                            stored = bd.conectar().prepareCall(" { call p_actualizar_infodocumento( ?, ?, ? ) } ");
                        }

                        traza.trace("numero del documento despues de la indexacion " + infoDoc.getNumeroDocumento()
                                + " version " + infoDoc.getVersion(), Level.INFO);

                        stored.setInt(1, infoDoc.getNumeroDocumento());
                        stored.setInt(2, infoDoc.getIdInfoDocumento());
                        stored.setString(3, elimnaDocuento.getIdExpediente());
                        stored.execute();
                        con++;

                        if (infoDoc.getLsDatosAdicionales() != null) {

                            sizeList = infoDoc.getLsDatosAdicionales().size();
                            flag = false;

                            traza.trace("tamaño lista de datos adicionales " + sizeList + " del numero de documento " + infoDoc.getNumeroDocumento()
                                    + " version " + infoDoc.getVersion(), Level.INFO);

                            for (DatoAdicional da : infoDoc.getLsDatosAdicionales()) {

                                if (storedDA == null) {
                                    storedDA = bdDA.conectar().prepareCall(" { call p_actualiza_numero_da( ?, ?, ?, ? ) } ");
                                }

                                traza.trace("actualizando el numero los datos adicionales del documento " + infoDoc.getTipoDocumento()
                                        + " su numero " + da.getNumeroDocumento() + " su version " + infoDoc.getVersion(), Level.INFO);
                                traza.trace("id valor del dato adicional " + da.getIdValor() + " valor " + da.getValor().toString(), Level.INFO);

                                storedDA.setInt(1, da.getIdValor());
                                storedDA.setInt(2, da.getNumeroDocumento());
                                storedDA.setInt(3, da.getVersion());
                                storedDA.setString(4, elimnaDocuento.getIdExpediente());
                                storedDA.execute();

                                traza.trace("contador datos adicionales " + cont, Level.INFO);

                                cont++;
                            }

                            if (sizeList == cont) {
                                bd.commit();
                                bd.desconectar();
                                stored = null;

                                bdDA.commit();
                                bdDA.desconectar();
                                storedDA = null;

                                cont = 0;
                            } else {
                                bd.rollback();
                                bdDA.rollback();
                                traza.trace("no se actualizo en idInfoDocumento " + infoDoc.getNumeroDocumento(), Level.WARN);
                                throw new DW4JServiciosException("no se actualizo en idInfoDocumento " + infoDoc.getNumeroDocumento());
                            }
                        }
                    }
                    if (flag) {
                        if (lstInfoDoc.size() == con) {
                            bd.commit();
                            bd.desconectar();
                            stored = null;
                        }
                    }

                }
//                else {
//                    throw new DW4JServiciosException("problemas al buscar la numeracion del tipo documental, no se re-indexo la numeracion ");
//                }

                traza.trace("escribiendo la traza en la base de datos", Level.INFO);
                stored = null;
                stored = bd.conectar().prepareCall(" { call p_traza_elimina_documento( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) } ");
                stored.setString(1, elimnaDocuento.getIdExpediente());
                stored.setInt(2, elimnaDocuento.getLibreria().getIdLibreria());
                stored.setInt(3, elimnaDocuento.getCategoria().getIdCategoria());
                stored.setInt(4, elimnaDocuento.getSubCategoria().getIdSubCategoria());
                stored.setInt(5, elimnaDocuento.getInfoDocumento().getIdDocumento());
                stored.setInt(6, elimnaDocuento.getInfoDocumento().getNumeroDocumento());
                try {
                    if (elimnaDocuento.getInfoDocumento().getFechaVencimiento() != null) {
                        stored.setDate(7, new java.sql.Date(elimnaDocuento.getInfoDocumento().getFechaVencimiento().getTime()));
                    } else {
                        stored.setDate(7, null);
                    }
                } catch (NullPointerException e) {
                }
                try {
                    if (elimnaDocuento.getInfoDocumento().getFechaDigitalizacion() != null) {
                        stored.setDate(8, new java.sql.Date(elimnaDocuento.getInfoDocumento().getFechaDigitalizacion().getTime()));
                    } else {
                        stored.setDate(8, null);
                    }
                } catch (NullPointerException e) {
                }
                try {
                    if (elimnaDocuento.getInfoDocumento().getFechaRechazo() != null) {
                        stored.setDate(9, new java.sql.Date(elimnaDocuento.getInfoDocumento().getFechaRechazo().getTime()));
                    } else {
                        stored.setDate(9, null);
                    }
                } catch (NullPointerException e) {
                }
                stored.setInt(10, elimnaDocuento.getInfoDocumento().getCantPaginas());
                stored.setInt(11, elimnaDocuento.getInfoDocumento().getVersion());
                stored.setString(12, elimnaDocuento.getUsuarioElimino());
                stored.setString(13, elimnaDocuento.getInfoDocumento().getUsuarioDigitalizo());
                stored.setString(14, elimnaDocuento.getInfoDocumento().getUsuarioRechazo());
                stored.setString(15, elimnaDocuento.getCausaElimino());
                stored.setString(16, elimnaDocuento.getMotivoElimino());
                stored.setString(17, elimnaDocuento.getInfoDocumento().getCausaRechazo());
                stored.setString(18, elimnaDocuento.getInfoDocumento().getMotivoRechazo());
                stored.execute();

                bd.commit();
                resp = true;

            } else {
                traza.trace("no se elimino el archivo", Level.INFO);
                throw new DW4JServiciosException("Problema al eliminar el archivo");
            }

        } catch (SQLException ex) {
            resp = false;
            traza.trace("problemas al buscar los datos del indice", Level.ERROR, ex);
        } catch (DW4JServiciosException e) {
            resp = false;
            traza.trace("problemas en la eliminacion del tipo de documento", Level.ERROR, e);
        } finally {
            System.gc();
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta de eliminar el documento " + resp, Level.INFO);
        return resp;
    }

    /**
     * Busca un listado de tipos de documentos con estatus rechazado para ser
     * eliminados
     *
     * @param idDocumento Un listado de identificadores de tipo de documento
     * @param idExpediente Identificador del expediente
     * @return Un listado con la información de los tipos de documentos
     */
    @WebMethod(operationName = "buscarInfoDocuEliminado")
    public List<com.develcom.dao.InfoDocumento> buscarInfoDocuEliminado(@WebParam(name = "idDocumento") List<java.lang.Integer> idDocumento, @WebParam(name = "idExpediente") String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsInfoDoc;
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        com.develcom.dao.InfoDocumento infoDocumento;
        String id = "";
        int cont = 0;

        traza.trace("Buscando informacion de los documentos con el idDocumento: " + idDocumento, Level.INFO);
        traza.trace("Buscando informacion de los documentos con el idExpediente: " + idExpediente, Level.INFO);

        try {

            for (Integer in : idDocumento) {

                id = id.trim() + ",";
                if (cont == 0) {
                    //borro la primera coma
                    id = id.replace(",", "");
                    //id=id+"'";
                    id = id + in;
                } else {
                    id = id + in;
                }
                cont++;
            }

            traza.trace("id documentos " + id, Level.INFO);

//            for (Integer idDoc : idDocumento) {
//                traza.trace("buscando con el id tipo documento " + idDoc, Level.INFO);
            stored = bd.conectar().prepareCall("{ ? = call f_buscar_informaciondoc(?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.setString(3, id);
//                stored.setInt(3, ido);
            stored.execute();

            rsInfoDoc = (ResultSet) stored.getObject(1);
            boolean flag = rsInfoDoc.next();
            traza.trace("resultset " + flag, Level.INFO);

//                    while (rsInfoDoc.next()) {
            if (flag) {

                String documento = rsInfoDoc.getString("tipoDoc");
                int idInfoDoc = rsInfoDoc.getInt("ID_INFODOCUMENTO");
                int version = rsInfoDoc.getInt("VERSION");
                int num = rsInfoDoc.getInt("NUMERO_DOCUMENTO");

                traza.trace("idInfoDocumento encontrado: " + idInfoDoc, Level.INFO);
                traza.trace("documento encontrado: " + documento, Level.INFO);
                traza.trace("version doc: " + version, Level.INFO);
                traza.trace("numero doc: " + num, Level.INFO);

                infoDocumento = new InfoDocumento();
                infoDocumento.setIdInfoDocumento(idInfoDoc);
                infoDocumento.setIdDocumento(rsInfoDoc.getInt("ID_DOCUMENTO"));
                infoDocumento.setTipoDocumento(documento);
                infoDocumento.setRutaArchivo(rsInfoDoc.getString("RUTA"));
                infoDocumento.setNombreArchivo(rsInfoDoc.getString("NOMBRE"));
                infoDocumento.setVersion(version);
                infoDocumento.setIdExpediente(rsInfoDoc.getString("ID_EXPEDIENTE"));
                infoDocumento.setNumeroDocumento(num);
                infoDocumento.setFechaVencimiento(rsInfoDoc.getDate("FECHA_VENCIMIENTO"));
                infoDocumento.setDatoAdicional(rsInfoDoc.getString("DATO_ADICIONAL"));
                infoDocumento.setFechaDigitalizacion(rsInfoDoc.getDate("FECHA_DIGITALIZACION"));
                infoDocumento.setEstatusDocumento(rsInfoDoc.getString("ESTATUS_DOCUMENTO"));
                infoDocumento.setEstatus(rsInfoDoc.getInt("ID_ESTATUS_DOCUMENTO"));
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

                infoDocumentos.add(infoDocumento);
            }
//            }

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
        traza.trace("tamaño de la lista de informacion del tipo documental " + infoDocumentos.size(), Level.INFO);
        return infoDocumentos;
    }

    /**
     * Busca todos los datos del documento a ser eliminado
     *
     * @param idInfoDocumento Identificador de la informacion del documento
     * @param idExpediente Identificador del expediente
     * @param version La verison del documento
     * @param numeroDocumento numero consecutivo del tipo de documento
     * @return Un objeto con la informacion necesaria del documento a eliminar
     */
    @WebMethod(operationName = "buscarImagenDocumento")
    public InfoDocumento buscarImagenDocumento(@WebParam(name = "idInfoDocumento") int idInfoDocumento,
            @WebParam(name = "idExpediente") String idExpediente,
            @WebParam(name = "version") int version,
            @WebParam(name = "numeroDocumento") int numeroDocumento) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        InfoDocumento infoDocumento = new InfoDocumento();
        ResultSet rsArchivo;

        traza.trace("buscando en documento:", Level.INFO);
        traza.trace("idInfoDocumento " + idInfoDocumento, Level.INFO);
        traza.trace("Expediente " + idExpediente, Level.INFO);
        traza.trace("version " + version, Level.INFO);
        traza.trace("numero documento " + numeroDocumento, Level.INFO);

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_datosdoc( ?, ?, ?, ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idInfoDocumento);
            stored.setInt(3, version);
            stored.setString(4, idExpediente);
            stored.setInt(5, numeroDocumento);
            stored.execute();

            rsArchivo = (ResultSet) stored.getObject(1);

            while (rsArchivo.next()) {
                traza.trace("exito en la busqueda del documento digitalizado", Level.INFO);
                infoDocumento = new InfoDocumento();
                infoDocumento.setIdInfoDocumento(rsArchivo.getInt("ID_INFODOCUMENTO"));
                infoDocumento.setIdDocumento(rsArchivo.getInt("ID_DOCUMENTO"));
                infoDocumento.setNombreArchivo(rsArchivo.getString("NOMBRE_ARCHIVO"));
                infoDocumento.setRutaArchivo(rsArchivo.getString("RUTA_ARCHIVO"));
                infoDocumento.setVersion(rsArchivo.getInt("VERSION"));
                infoDocumento.setTipoDocumento(rsArchivo.getString("tipoDoc"));
                infoDocumento.setFechaVencimiento(rsArchivo.getDate("FECHA_VENCIMIENTO"));
                infoDocumento.setNumeroDocumento(rsArchivo.getInt("NUMERO_DOCUMENTO"));
                infoDocumento.setCantPaginas(rsArchivo.getInt("PAGINAS"));
                infoDocumento.setEstatus(rsArchivo.getInt("ESTATUS_DOCUMENTO"));
                infoDocumento.setEstatusDocumento(rsArchivo.getString("estatusArchivo"));
                infoDocumento.setFormato(rsArchivo.getString("FORMATO"));
            }

        } catch (SQLException ex) {
            traza.trace("error al buscar el archivo digitalizado", Level.ERROR, ex);
        } catch (Exception e) {
            traza.trace("error grave al buscar la informacion del tipo de documento", Level.FATAL, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        return infoDocumento;
    }

}
