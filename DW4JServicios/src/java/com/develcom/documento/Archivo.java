/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.documento;

import com.develcom.dao.Bufer;
import com.develcom.dao.DatoAdicional;
import com.develcom.dao.InfoDocumento;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.tools.Propiedades;
import com.develcom.tools.UtilitarioFecha;
import com.develcom.tools.cry.EncriptaDesencripta;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "Archivo")
public class Archivo {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(Archivo.class);
    /**
     * id del documento
     */
    private int numeroDocumento;
    private int contador;

    private void restaurar(int id, String nombre) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;

        try {

            traza.trace("idInfodocumento a eliminar " + id, Level.INFO);
            traza.trace("nombre de archivo a eliminar " + nombre, Level.INFO);

            stored = bd.conectar().prepareCall(" { call p_eliminar_registro_archivo( ? ) } ");
            stored.setInt(1, id);
            stored.execute();

            bd.commit();

        } catch (SQLException ex) {
            traza.trace("problemas al eliminar la informacion del documento", Level.ERROR, ex);
        }
    }

    /**
     * Gestiona el almacenamiento e información del tipo documental
     *
     * @param buffer El bufer del archivo a escribir en disco
     * @param accion La acciona a tomar: Guardar Reemplazar o Versionar
     * @param infoDocumento Todos lo Datos del tipo Documental
     * @param expediente El expediente
     * @param usuario El usuario que guarda el tipo documental
     * @return Un mensaje segun el procedimiento generado
     */
    @WebMethod(operationName = "guardarArchivo")
    public String guardarArchivo(@WebParam(name = "buffer") Bufer buffer, @WebParam(name = "accion") String accion, @WebParam(name = "infoDocumento") InfoDocumento infoDocumento, @WebParam(name = "expediente") String expediente, @WebParam(name = "usuario") String usuario) {
        //TODO write your implementation code here:
        return gestionArchivo(buffer, accion, infoDocumento, expediente, usuario);
    }

    /**
     * Gestiona el almacenamiento e información del tipo documental
     *
     * @param buffer El bufer del archivo a escribir en disco
     * @param accion La acciona a tomar: Guardar Reemplazar o Versionar
     * @param infoDocumento Todos lo Datos del tipo Documental
     * @param expediente El expediente
     * @param usuario El usuario que guarda el tipo documental
     * @return Un mensaje segun el procedimiento generado
     */
    @WebMethod(operationName = "reemplazarArchivo")
    public String reemplazarArchivo(@WebParam(name = "buffer") Bufer buffer, @WebParam(name = "accion") String accion, @WebParam(name = "infoDocumento") InfoDocumento infoDocumento, @WebParam(name = "expediente") String expediente, @WebParam(name = "usuario") String usuario) {
        //TODO write your implementation code here:
        return gestionArchivo(buffer, accion, infoDocumento, expediente, usuario);
    }

    /**
     * Guarda los datos adicionales asociado a un documento
     *
     * @param lsDatosAdicionales Listado con los datos adicionales y sus valores
     * @param idExpediente Identificador del expediente
     * @param flag Bandera que indica si es nuevo registro o se actualiza el
     * mismo
     * @param elimina Bandera que indica la actualizacion de registro por la
     * eliminacion del documento
     * @return
     */
    public boolean guardarDatosAdicionales(List<DatoAdicional> lsDatosAdicionales, String idExpediente, String flag, boolean elimina) {

        BaseDato bd = new BaseDato();
        CallableStatement stored = null;
        boolean exito = false;
        int sizeList, cont = 0;

        try {

            traza.trace("bandera del documento eliminado en los datos adicionales " + elimina, Level.INFO);
            traza.trace("bandera dato adicional " + flag, Level.INFO);

            sizeList = lsDatosAdicionales.size();

            for (DatoAdicional da : lsDatosAdicionales) {

                if (elimina) {
                    numeroDocumento = da.getNumeroDocumento();
                }

                traza.trace("id valor de los datos adicionales " + da.getIdValor(), Level.INFO);
                traza.trace("numero del documento en los datos adicionales " + numeroDocumento, Level.INFO);
                traza.trace("version del documento en los datos adicionales " + da.getVersion(), Level.INFO);
                traza.trace("id dato adicional " + da.getIdDatoAdicional(), Level.INFO);
                traza.trace("valor dato adicional: " + da.getValor().toString(), Level.INFO);
                traza.trace("id expediente del valor dato adicional " + idExpediente, Level.INFO);

                if (stored == null) {
                    stored = bd.conectar().prepareCall("{ call p_guarda_valor_dato_adicional( ?, ?, ?, ?, ?, ?, ? ) } ");
                }

                stored.setInt(1, da.getIdDatoAdicional());
                stored.setInt(2, da.getIdValor());
                stored.setString(3, da.getValor().toString());
                stored.setInt(4, numeroDocumento);
                stored.setInt(5, da.getVersion());
                stored.setString(6, idExpediente);
                stored.setString(7, flag);

                stored.execute();

                cont++;

            }

            if (sizeList == cont) {
                exito = true;
                bd.commit();
                bd.desconectar();
            } else {
                exito = false;
                bd.rollback();
                throw new DW4JServiciosException("problemas gurdando los valores de los datos adicionales");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("problema en la base de datos", Level.ERROR, e);
            exito = false;
            try {
                bd.rollback();
            } catch (SQLException ex) {
                traza.trace("problema con el rollback al modificar las librerias", Level.ERROR, ex);
            }
        } catch (SQLException e) {
            exito = false;
            try {
                bd.rollback();
                traza.trace("problemas en la base de datos", Level.ERROR, e);
            } catch (SQLException ex) {
                traza.trace("problemas en el rollback en guardar los datos adicionales ", Level.INFO, ex);
            }
            traza.trace("problemas al guardar el valor de los datos adicionales en base de datos", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta al guardar los valores de los datos adicionales " + exito, Level.INFO);
        return exito;
    }

    /**
     * Gestiona el almacenamiento e información del tipo documental
     *
     * @param buffer El bufer del archivo a escribir en disco
     * @param accion La acciona a tomar: Guardar Reemplazar o Versionar
     * @param infoDocumento Todos lo Datos del tipo Documental
     * @param expediente El expediente
     * @param usuario El usuario que guarda el tipo documental
     * @return Un mensaje segun el procedimiento generado
     */
    private String gestionArchivo(Bufer buffer, String accion, InfoDocumento infoDocumento, String expediente, String usuario) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        int idInfoDocumento = 0, idDoc;
        boolean creaDir, gestion, eliminarArchivo, isFile, compNum, compNomAr, datosAdicionales;
        String rutaArchivo, rutaCompleta, id, nombreArchivo, elimina, resp = null, flag = "";
        File path, documento, eliminar;
        Properties prop;

        prop = Propiedades.cargarPropiedadesWS();

        idDoc = infoDocumento.getIdDocumento();
        nombreArchivo = infoDocumento.getNombreArchivo();

        traza.trace("idInfoDocumento " + infoDocumento.getIdInfoDocumento(), Level.INFO);
        traza.trace("id documento " + idDoc, Level.INFO);
        traza.trace("tipo documento " + infoDocumento.getTipoDocumento(), Level.INFO);
        traza.trace("version del documento " + infoDocumento.getVersion(), Level.INFO);
        traza.trace("numero del documento " + infoDocumento.getNumeroDocumento(), Level.INFO);
        traza.trace("nuevo documento " + infoDocumento.isNuevo(), Level.INFO);
        traza.trace("ruta del archivo: " + infoDocumento.getRutaArchivo(), Level.INFO);
        traza.trace("ACCION A REALIZAR " + accion.toUpperCase(), Level.INFO);
        traza.trace("EXPEDIENTE " + expediente, Level.INFO);
        traza.trace("cantidad de paginas " + infoDocumento.getCantPaginas(), Level.INFO);
        traza.trace("re-digitaliza? " + infoDocumento.isReDigitalizo(), Level.INFO);
        try {
            traza.trace("fecha de vencimiento " + new UtilitarioFecha().convertLongDate(infoDocumento.getFechaVencimiento().getTime()), Level.INFO);
        } catch (NullPointerException e) {
        }
        try {
            traza.trace("dato adicional " + infoDocumento.getDatoAdicional(), Level.INFO);
        } catch (NullPointerException e) {
        }

        try {

            if (accion.equalsIgnoreCase("reemplazar") || infoDocumento.isReDigitalizo() || accion.equalsIgnoreCase("versionar")) {
                numeroDocumento = infoDocumento.getNumeroDocumento();
                compNum = true;
            } else {
                compNum = combrobarNumeracion(idDoc, expediente);
            }
            if (compNum) {

                if (infoDocumento.isReDigitalizo()) {

                    stored = bd.conectar().prepareCall(" { ? = call f_eliminar_archivo( ? ) } ");
                    stored.registerOutParameter(1, Types.OTHER);
                    stored.setInt(2, infoDocumento.getIdInfoDocumento());
                    stored.execute();

                    ResultSet rsInfoDoc = (ResultSet) stored.getObject(1);

                    if (rsInfoDoc.next()) {
                        elimina = prop.getProperty("rutaRaiz") + rsInfoDoc.getString("ruta_archivo") + "/" + rsInfoDoc.getString("nombre_archivo");
                        traza.trace("archivo a eliminar " + elimina, Level.INFO);
                        eliminar = new File(elimina);
                        if (eliminar.exists()) {
                            if (eliminarArchivo = eliminar.delete()) {
                                traza.trace("se elimino el archivo " + eliminarArchivo, Level.INFO);
                                bd.commit();
                            } else {
                                traza.trace("problemas al eliminar el archivo", Level.INFO);
                                throw new DW4JServiciosException("problemas al eliminar el archivo");
                            }
                        } else {
                            traza.trace("no existe el archivo ", Level.INFO);
                        }
                    } else {
                        throw new DW4JServiciosException("sin informacion (puntero del fisico) del archivo a eliminar");
                    }
                }

                traza.trace("ejecutando el procedimiento almacenado p_agregar_registro_archivo ", Level.INFO);
                stored = bd.conectar().prepareCall("{ ? = call p_agregar_registro_archivo( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) } ");
                stored.registerOutParameter(1, Types.INTEGER);
                stored.setString(2, accion);
                stored.setString(3, "");
                stored.setInt(4, idDoc);
                stored.setString(5, infoDocumento.getRutaArchivo());
                stored.setInt(6, infoDocumento.getCantPaginas());
                stored.setInt(7, infoDocumento.getVersion());
                stored.setString(8, expediente);
                stored.setInt(9, numeroDocumento);
                try {
                    if (infoDocumento.getFechaVencimiento() != null) {
                        stored.setDate(10, new Date(infoDocumento.getFechaVencimiento().getTime()));
                    } else {
                        stored.setDate(10, null);
                    }
                } catch (Exception e) {
                }
                stored.setString(11, infoDocumento.getDatoAdicional());
                stored.setString(12, usuario);
                stored.setInt(13, infoDocumento.getIdInfoDocumento());
                stored.setInt(14, infoDocumento.getEstatus());
                if (infoDocumento.isReDigitalizo()) {
                    stored.setString(15, "1");
                } else {
                    stored.setString(15, "0");
                }
                stored.setString(16, infoDocumento.getFormato());

                gestion = stored.execute();

                bd.commit();

                idInfoDocumento = stored.getInt(1);

                traza.trace("respuesta del procedimiento almacenado p_agregar_registro_archivo " + gestion, Level.INFO);

                if (idInfoDocumento != 0) {
                    traza.trace("id infoDocumento " + idInfoDocumento, Level.INFO);

                    if (idInfoDocumento != -1) {
                        id = EncriptaDesencripta.encripta(String.valueOf(idInfoDocumento));
                        traza.trace("idInfoDocumento encriptado " + id, Level.INFO);
                        nombreArchivo = nombreArchivo + id;
                    } else {
                        idInfoDocumento = infoDocumento.getIdInfoDocumento();
                        id = EncriptaDesencripta.encripta(String.valueOf(infoDocumento.getIdInfoDocumento()));
                        traza.trace("idInfoDocumento encriptado reemplaza " + id, Level.INFO);
                        nombreArchivo = nombreArchivo + id;
                    }

                    nombreArchivo = nombreArchivo + "-" + infoDocumento.getVersion();
                    traza.trace("se " + accion.toUpperCase() + " archivo", Level.INFO);

                    //actualizando la informacion del documento con el nombre del archivo encriptado
                    stored = null;
                    bd.desconectar();

                    traza.trace("ejecutando el procedimiento almacenado update ", Level.INFO);

                    traza.trace("nombre del archivo a " + accion + " " + nombreArchivo, Level.INFO);
                    traza.trace("idInfoDocumento del archivo a " + accion + " " + idInfoDocumento, Level.INFO);
                    traza.trace("idDocumento del archivo a " + accion + " " + infoDocumento.getIdDocumento(), Level.INFO);
                    traza.trace("idExpediente del archivo a " + accion + " " + expediente, Level.INFO);

                    stored = bd.conectar().prepareCall(" { call p_actualiza_nombre_archivo( ?, ? ) } ");
                    stored.setString(1, nombreArchivo);
                    stored.setInt(2, idInfoDocumento);

                    gestion = stored.execute();

                    bd.commit();

                    traza.trace("respuesta del procedimiento almacenado p_actualiza_nombre_archivo " + gestion, Level.INFO);

                    compNomAr = comprobarNombreArchivo(idInfoDocumento, expediente, nombreArchivo);

                    if (compNomAr) {

                        //escribiendo el archivo en el disco del servidor
                        String[] rutas = infoDocumento.getRutaArchivo().split("/");
                        traza.trace("rutas: " + rutas[0], Level.INFO);
                        traza.trace("rutas: " + rutas[1], Level.INFO);
                        traza.trace("rutas: " + rutas[2], Level.INFO);
                        rutaArchivo = prop.getProperty("rutaRaiz") + infoDocumento.getRutaArchivo();
//                        path = new File(rutaArchivo);

//                        traza.trace("existe el directorio " + path.exists(), Level.INFO);
                        path = new File(prop.getProperty("rutaRaiz") + rutas[0]);
                        if (!path.exists()) {
                            traza.trace("se creara el directorio: " + path.getAbsolutePath(), Level.INFO);
                            creaDir = path.mkdirs();
                            traza.trace("respuesta al crear el directorio: " + creaDir, Level.INFO);

                        }
                        path = new File(prop.getProperty("rutaRaiz") + rutas[0] + "/" + rutas[1]);
                        if (!path.exists()) {
                            traza.trace("se creara el directorio: " + path.getAbsolutePath(), Level.INFO);
                            creaDir = path.mkdirs();
                            traza.trace("respuesta al crear el directorio: " + creaDir, Level.INFO);

                        }
                        path = new File(prop.getProperty("rutaRaiz") + rutas[0] + "/" + rutas[1] + "/" + rutas[2]);
                        if (!path.exists()) {
                            traza.trace("se creara el directorio: " + path.getAbsolutePath(), Level.INFO);
                            creaDir = path.mkdirs();
                            traza.trace("respuesta al crear el directorio: " + creaDir, Level.INFO);
                        }

                        rutaCompleta = rutaArchivo + "/" + nombreArchivo;
                        documento = new File(rutaCompleta);

                        traza.trace("escribiendo el archivo " + nombreArchivo + " en el disco", Level.INFO);

                        isFile = crearArchivo(documento, buffer.getBufer());

                        traza.trace("finalizo la escritura del archivo", Level.INFO);
                        traza.trace("se creo el archivo? " + isFile, Level.INFO);

                        if (!isFile) {
                            resp = "Problemas al escribir el archivo.";
                            throw new DW4JServiciosException("Problemas al escribir el archivo");
                        } else {

                            if ((infoDocumento.isReDigitalizo()) && (accion.equalsIgnoreCase("guardar"))) {
                                flag = "0";
                            } else if (((accion.equalsIgnoreCase("guardar")) || (accion.equalsIgnoreCase("versionar")))) {
                                flag = "1";
                            } else if (accion.equalsIgnoreCase("reemplazar")) {
                                flag = "0";
                            }

                            //agrega datos adicionales
                            if (infoDocumento.getLsDatosAdicionales() != null) {
                                datosAdicionales = guardarDatosAdicionales(infoDocumento.getLsDatosAdicionales(), expediente, flag, false);
                            } else {
                                datosAdicionales = true;
                            }

                            if (datosAdicionales) {
                                resp = "exito";
                            } else {
                                bd.rollback();
                                if (!accion.equalsIgnoreCase("reemplazar")) {
                                    restaurar(idInfoDocumento, nombreArchivo);
                                }
                                resp = "Problemas al guardar los datos adicionales";
                                throw new DW4JServiciosException("Problemas al guardar los datos adicionales");
                            }
                        }

                    } else {
                        resp = "Problemas al actualizar el nombre del archivo del tipo de documento en la base de datos.";
                        restaurar(idInfoDocumento, nombreArchivo);
                        throw new DW4JServiciosException("Problemas al actualizar el nombre del archivo del tipo de documento en la base de datos");
                    }

                } else {
                    resp = "Problemas al agregar los datos del tipo de documento en la base de datos.";
                    bd.rollback();
                    throw new DW4JServiciosException("Problemas al agregar los datos del tipo de documento en la base de datos.");
                }

            } else {
                resp = "Problemas con la numeracion del tipo de documento.";
                throw new DW4JServiciosException("Problemas con la numeracion del tipo de documento.");
            }

        } catch (SQLException ex) {
            restaurar(idInfoDocumento, nombreArchivo);
            traza.trace("error al agregar el documento", Level.ERROR, ex);
            resp = "Error al agregar el documento en Base de Datos " + ex.getMessage() + " " + ex.getSQLState();
        } catch (DW4JServiciosException e) {
            restaurar(idInfoDocumento, nombreArchivo);
            traza.trace(e.getMessage(), Level.ERROR, e);
        } catch (Exception ex) {
            restaurar(idInfoDocumento, nombreArchivo);
            traza.trace("error gestion archivo", Level.ERROR, ex);
            resp = "Error general al guardar el documento " + ex.getMessage();
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("respuesta de registrar el archivo del tipo documental " + resp, Level.INFO);
        return resp;
    }

    private boolean combrobarNumeracion(int idDocumento, String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsBuscar;
        boolean resp = false, existe = false;
        int numConsecutivo = 0, numeEncontrado, numeroBuscado;

        try {
            if (contador > 3) {
                contador = 0;
            }
            traza.trace("expediente para comprobar el consecutivo del tipo documental " + idExpediente, Level.INFO);
            traza.trace("idDocumento para comprobar el consecutivo del tipo documental " + idDocumento, Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_ultimo_numero( ?, ? ) }");
            stored.registerOutParameter(1, Types.INTEGER);
            stored.setInt(2, idDocumento);
            stored.setString(3, idExpediente);
            stored.execute();

            numeroBuscado = stored.getInt(1);

            if (numeroBuscado != -1) {

                numConsecutivo = numeroBuscado + 1;

                stored = bd.conectar().prepareCall("{ ? = call f_comprobar_numero_documento( ?, ? ) } ");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setInt(2, idDocumento);
                stored.setString(3, idExpediente);
                stored.execute();

                rsBuscar = (ResultSet) stored.getObject(1);

                while (rsBuscar.next()) {
                    numeEncontrado = rsBuscar.getInt("NUMERO_DOCUMENTO");
                    traza.trace("numero del documento encontrado " + numeEncontrado + " del expediente " + idExpediente, Level.INFO);
                    if (numConsecutivo == numeEncontrado) {
                        existe = true;
                        break;
                    }
                }

            } else {
                throw new DW4JServiciosException("problemas buscando el ultimo numero del tipo documental");
            }

            if (!existe) {
                resp = true;
                numeroDocumento = numConsecutivo;
                traza.trace("ultimo numero " + numeroDocumento, Level.INFO);
            }

        } catch (DW4JServiciosException ex) {
            traza.trace("problemas en la base de datos", Level.ERROR, ex);
        } catch (SQLException ex) {
            traza.trace("problemas al buscar el ultimo numero del documento", Level.ERROR, ex);
        } catch (Exception e) {
            traza.trace("error grave al buscar el ultimo numero del tipo de documento", Level.FATAL, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        if (numeroDocumento == 0) {
            contador++;
            if (contador == 3) {
                return false;
            } else {
                combrobarNumeracion(idDocumento, idExpediente);
            }
        }
        traza.trace("respuesta al buscar el ultimo numero del tipo documental " + resp, Level.INFO);
        return resp;
    }

    private boolean comprobarNombreArchivo(int idInfoDocumento, String idExpediente, String nombreArchivo) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        boolean resp = false;
        String nombre;

        try {

            traza.trace("nombre del archivo a comprobar " + nombreArchivo, Level.INFO);

            stored = bd.conectar().prepareCall(" { ? = call f_comprobar_nombre_archivo( ?, ? ) } ");
            stored.registerOutParameter(1, Types.VARCHAR);
            stored.setInt(2, idInfoDocumento);
            stored.setString(3, idExpediente);
            stored.execute();

            nombre = stored.getString(1);

            if (nombre != null) {

                traza.trace("nombre del archivo en base de datos a comparar " + nombre, Level.INFO);
                if (nombre.equalsIgnoreCase(nombreArchivo)) {
                    traza.trace("exito en la comparacion del archivo", Level.INFO);
                    resp = true;
                }
            } else {
                throw new DW4JServiciosException("problemas buscando el el nombre de archivo del tipo documental");
            }

        } catch (DW4JServiciosException ex) {
            traza.trace("problemas en la base de datos", Level.ERROR, ex);
        } catch (SQLException ex) {
            traza.trace("problemas al buscar el nombre del archivo", Level.ERROR, ex);
        } catch (Exception e) {
            traza.trace("error grave al buscar el nombre del archivo", Level.FATAL, e);
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

    private boolean crearArchivo(File archivo, byte[] buffer) {
        boolean resp = false;
        OutputStream escribiendo;

        try {

            escribiendo = new FileOutputStream(archivo);
            escribiendo.write(buffer);
            escribiendo.flush();
            escribiendo.close();
            resp = archivo.exists();
            traza.trace("exito al escribir el archivo en disco " + resp, Level.INFO);

        } catch (FileNotFoundException ex) {
            traza.trace("problemas al escribir el archivo en el disco", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("problemas en la escritura en el disco", Level.ERROR, ex);
        }
        return resp;
    }

    /**
     * Genera el bufer del archivo y lo envia al cliente para ser creado en el
     * disco
     *
     * @param ruta La ruta donde se aloja el archivo
     * @param archivo El nombre del archivo encriptado
     * @return
     */
    @WebMethod(operationName = "buscarBuferArchivo")
    public Bufer buscarBuferArchivo(@WebParam(name = "ruta") String ruta, @WebParam(name = "archivo") String archivo) {

        Bufer bufer = new Bufer();
        boolean existe;
        InputStream leyendo = null;
        Properties prop;
        String rutaRaiz, rutaCompleta;
        byte[] bu;
        File fichero;

        try {

            if (contador > 3) {
                contador = 0;
            }

            if (!archivo.equalsIgnoreCase("")) {
                prop = Propiedades.cargarPropiedadesWS();
                rutaRaiz = prop.getProperty("rutaRaiz");
                rutaCompleta = rutaRaiz + ruta + "/" + archivo;
                fichero = new File(rutaCompleta);

//                traza.trace("ruta donde se aloja el archivo " + ruta, Level.INFO);
//                traza.trace("archivo a buscar " + archivo, Level.INFO);
                //traza.trace("ruta completa de archivo " + rutaCompleta, Level.INFO);                
                existe = fichero.exists();
                traza.trace("existe el archivo " + existe, Level.INFO);
                if (existe) {
                    traza.trace("generando el buffer", Level.INFO);
                    leyendo = new FileInputStream(rutaCompleta);
                    bu = new byte[leyendo.available()];
                    leyendo.read(bu);
                    bufer.setBufer(bu);
                    bufer.setExiste(existe);
                } else {
                    traza.trace("archivo no existe", Level.INFO);
                    bufer.setExiste(false);
                }
            } else {
                traza.trace("sin nombre del archivo", Level.INFO);
                bufer.setExiste(false);
            }

        } catch (FileNotFoundException ex) {
            bufer.setExiste(false);
            traza.trace("error archivo a buscar el archivo", Level.ERROR, ex);
        } catch (IOException ex) {
            bufer.setExiste(false);
            traza.trace("error al leer el archivo a buscar", Level.ERROR, ex);
        } finally {
            try {
                if (leyendo != null) {
                    leyendo.close();
                }
            } catch (IOException ex) {
                traza.trace("error al cerrar el archivo", Level.ERROR, ex);
            }
        }

        traza.trace("existe el bufer " + bufer.isExiste(), Level.INFO);
        if (!bufer.isExiste()) {
            contador++;
            traza.trace("contador para buscar el bufer del archivo " + contador, Level.INFO);
            if (contador == 3) {
                bufer.setExiste(false);
                return bufer;
            } else {
                buscarBuferArchivo(ruta, archivo);
            }
        }
        //traza.trace("buffer "+bu, Level.INFO);
        //traza.trace("buffer "+buffer, Level.INFO);
        traza.trace("bufer existe? " + bufer.isExiste(), Level.INFO);
        traza.trace("bufer generado? " + bufer, Level.INFO);
        return bufer;
        //return bu;
    }

    /**
     * Busca un listado de los tipos de documentos para mostrar el fisico del
     * mismo
     *
     * @param idInfoDocumento Identificador de infoDocumento
     * @param idDocumento Identificador del tipo de documento
     * @param idCategoria Identificador de la Categoria
     * @param idSubCategoria Identificador de la SubCategoria
     * @param idExpediente Identificador del Expedientes
     * @param version Version del tipo de documento
     * @param numeroDocumento Numero del tipo de documento
     * @return Un listado con información de los tipos de documentos
     */
    @WebMethod(operationName = "buscarFisicoDocumento")
    public java.util.List<InfoDocumento> buscarFisicoDocumento(@WebParam(name = "idInfoDocumento") int idInfoDocumento,
            @WebParam(name = "idDocumento") int idDocumento,
            @WebParam(name = "idCategoria") int idCategoria,
            @WebParam(name = "idSubCategoria") int idSubCategoria,
            @WebParam(name = "version") int version,
            @WebParam(name = "numeroDocumento") int numeroDocumento,
            @WebParam(name = "idExpediente") String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        List<DatoAdicional> datosAdicionales = new ArrayList<>();
        InfoDocumento infoDoc = null;
        ResultSet rsArchivo;

        traza.trace("buscando el documento:", Level.INFO);
        traza.trace("idInfoDocumento " + idInfoDocumento, Level.INFO);
        traza.trace("idDocumento " + idDocumento, Level.INFO);
        traza.trace("version " + version, Level.INFO);
        traza.trace("numero del documento " + numeroDocumento, Level.INFO);
        traza.trace("Expediente " + idExpediente, Level.INFO);

        try {

            stored = bd.conectar().prepareCall(" { ? = call f_buscar_fisico_documento( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idDocumento);
            stored.setInt(3, numeroDocumento);
            stored.setString(4, idExpediente);
            stored.execute();

            rsArchivo = (ResultSet) stored.getObject(1);
            traza.trace("ResultSet " + rsArchivo, Level.INFO);

            while (rsArchivo.next()) {

                traza.trace("exito en la busqueda del documento digitalizado", Level.INFO);
                infoDoc = new InfoDocumento();
                infoDoc.setIdInfoDocumento(rsArchivo.getInt("ID_INFODOCUMENTO"));
                infoDoc.setIdDocumento(rsArchivo.getInt("ID_DOCUMENTO"));
                infoDoc.setNombreArchivo(rsArchivo.getString("NOMBRE_ARCHIVO"));
                infoDoc.setRutaArchivo(rsArchivo.getString("RUTA_ARCHIVO"));
                infoDoc.setVersion(rsArchivo.getInt("VERSION"));
                infoDoc.setTipoDocumento(rsArchivo.getString("tipoDoc"));
                infoDoc.setFechaVencimiento(rsArchivo.getDate("FECHA_VENCIMIENTO"));
                infoDoc.setNumeroDocumento(rsArchivo.getInt("NUMERO_DOCUMENTO"));
                infoDoc.setCantPaginas(rsArchivo.getInt("PAGINAS"));
                infoDoc.setEstatus(rsArchivo.getInt("ESTATUS_DOCUMENTO"));
                infoDoc.setEstatusDocumento(rsArchivo.getString("estatusArchivo"));
                infoDoc.setFormato(rsArchivo.getString("FORMATO"));

                infoDocumentos.add(infoDoc);
            }

            traza.trace("agregando los datos adicionales", Level.INFO);
            for (int i = 0; i < infoDocumentos.size(); i++) {
                InfoDocumento id = infoDocumentos.get(i);

                if (id.isTipoDocDatoAdicional()) {
                    datosAdicionales = new Documento().buscarValorDatoAdicional(id.getIdDocumento(), idExpediente, id.getNumeroDocumento(), id.getVersion());
                    traza.trace("datos adicionales del documento " + id.getTipoDocumento() + " numero " + id.getNumeroDocumento() + " version " + id.getVersion() + " size " + datosAdicionales.size() + " expediente " + idExpediente, Level.INFO);
                    if (!datosAdicionales.isEmpty()) {
                        infoDocumentos.get(i).setLsDatosAdicionales(datosAdicionales);
                    }

                } else {
                    traza.trace("documento sin dato adicional " + id.getTipoDocumento(), Level.INFO);
                }
            }

            traza.trace("objeto infodoc " + infoDoc, Level.INFO);
            traza.trace("tanaño de la lista de infoDocuemnto " + infoDocumentos.size(), Level.INFO);

        } catch (SQLException ex) {
            traza.trace("error al buscar el archivo digitalizado", Level.ERROR, ex);
            infoDocumentos.clear();
        } catch (Exception e) {
            traza.trace("error grave al buscar el tipo de documento", Level.FATAL, e);
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

        return infoDocumentos;
    }
}
