/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.expediente;

import com.develcom.dao.Categoria;
import com.develcom.dao.ConsultaDinamica;
import com.develcom.dao.Indice;
import com.develcom.dao.InfoDocumento;
import com.develcom.dao.SubCategoria;
import com.develcom.dao.TipoDocumento;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.tools.BusquedaDinamicaUtilidad;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@WebService(serviceName = "BusquedaExpediente")
public class BusquedaExpediente {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(BusquedaExpediente.class);

    /**
     * Metodo que crea la consulta dinamica segun los datos recibidos
     *
     * @param listaIndice Listado de indices segun la categoria
     * @param listaCat
     * @param listaSubCat Listado de subCategoria si fue selecionada
     * @param listaTipoDoc Listado de tipos de documentos si fue selecionado
     * @param estatusDocumento El estatus del documento
     * @param idLibreria EL id de la Libreria
     * @return Un objecto con el resultado de la busqueda dinamica
     *
     */
    @WebMethod(operationName = "buscarExpedienteDinamico")
    public List<ConsultaDinamica> buscarExpedienteDinamico(
            @WebParam(name = "listaIndice") List<com.develcom.dao.Indice> listaIndice,
            @WebParam(name = "listaCat") List<com.develcom.dao.Categoria> listaCat,
            @WebParam(name = "listaSubCat") List<com.develcom.dao.SubCategoria> listaSubCat,
            @WebParam(name = "listaTipoDoc") List<com.develcom.dao.TipoDocumento> listaTipoDoc,
            @WebParam(name = "estatusDocumento") int estatusDocumento,
            @WebParam(name = "idLibreria") int idLibreria) {

        String WHERE, tmp = "";
        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsDynamicQuery;
        BusquedaDinamicaUtilidad utilidad = new BusquedaDinamicaUtilidad();
        List<ConsultaDinamica> consultaDinamicas = new ArrayList<>();
        ConsultaDinamica consultaDinamica;
        List<Indice> indices;
        Indice indice;
        List<String> expedientes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            traza.trace("tamaño lista categoria " + listaCat.size(), Level.INFO);
            traza.trace("tamaño lista subCategoria " + listaSubCat.size(), Level.INFO);
            traza.trace("tamaño lista tipo documento " + listaTipoDoc.size(), Level.INFO);
            traza.trace("tamaño lista indice " + listaIndice.size(), Level.INFO);

            traza.trace("construyendo la consulta dinamica", Level.INFO);

            if (!listaIndice.isEmpty()) {
                traza.trace("creando los filtros de la consulta dinamica", Level.INFO);
                WHERE = utilidad.crearFiltrosIndices(listaIndice, listaCat, listaSubCat, listaTipoDoc, false);
                traza.trace("filtros consulta dinamica " + WHERE, Level.INFO);

                if (listaIndice.get(0).isUpdateIndices()) {
                    WHERE = WHERE + " AND c.id_categoria=" + utilidad.getIdCategoria()
                            + " AND l.id_libreria=" + idLibreria + "\n ORDER BY e.expediente, i.id_indice";
                } else if (!listaCat.isEmpty()) {
                    WHERE = WHERE + " AND d.ESTATUS_DOCUMENTO=" + estatusDocumento
                            + " AND l.id_libreria=" + idLibreria + "\n ORDER BY e.expediente, i.id_indice";
                } else {
                    WHERE = WHERE + " AND d.ESTATUS_DOCUMENTO=" + estatusDocumento
                            + " AND c.id_categoria=" + utilidad.getIdCategoria()
                            + " AND l.id_libreria=" + idLibreria + "\n ORDER BY e.expediente, i.id_indice";
                }

                traza.trace("filtro antes de llamar al procedimiento \"f_buscar_expediente_dinamico\" " + WHERE, Level.INFO);

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente_dinamico( ?, ?, ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, WHERE);
                stored.setString(3, null);

                if (listaIndice.get(0).isUpdateIndices()) {
                    stored.setString(4, "1");
                } else {
                    stored.setString(4, "0");
                }

                stored.execute();

                rsDynamicQuery = (ResultSet) stored.getObject(1);

                while (rsDynamicQuery.next()) {
                    if (!tmp.equalsIgnoreCase(rsDynamicQuery.getString("expediente"))) {
                        tmp = rsDynamicQuery.getString("expediente");
                        traza.trace("expediente " + tmp, Level.INFO);
                        expedientes.add(rsDynamicQuery.getString("expediente"));
                    }
                }

                if (!expedientes.isEmpty()) {
                    rsDynamicQuery.close();

                    traza.trace("tamaño lista de resultado de la consulta para obtener los expedientes " + expedientes.size(), Level.INFO);

                    for (String expe : expedientes) {
                        indices = new ArrayList<>();

                        stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente_dinamico( ?, ?, ? ) }");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, null);
                        stored.setString(3, expe);
                        stored.setString(4, " ");

                        stored.execute();

                        rsDynamicQuery = (ResultSet) stored.getObject(1);

                        while (rsDynamicQuery.next()) {

                            String clave = rsDynamicQuery.getString("clave");

                            indice = new Indice();

                            indice.setClave(clave);
                            indice.setIdIndice(rsDynamicQuery.getInt("id_indice"));
                            indice.setIndice(rsDynamicQuery.getString("indice"));
                            indice.setTipo(rsDynamicQuery.getString("tipo"));

                            if (rsDynamicQuery.getObject("valor") != null) {
                                traza.trace("indice " + rsDynamicQuery.getString("indice") + " valor " + rsDynamicQuery.getObject("valor") + " clave " + clave, Level.INFO);
                                indice.setValor(rsDynamicQuery.getObject("valor"));
                            } else {
                                traza.trace("indice " + rsDynamicQuery.getString("indice") + " valor " + rsDynamicQuery.getObject("valor") + " clave " + clave, Level.INFO);
                                indice.setValor(sdf.format(rsDynamicQuery.getDate("fecha_indice")));
                            }

                            indices.add(indice);
                        }
                        rsDynamicQuery.close();
                        consultaDinamica = new ConsultaDinamica();

                        traza.trace("tamaño total lista de indices " + indices.size() + " del expediente " + expe, Level.INFO);
                        if (!indices.isEmpty()) {
                            consultaDinamica.setExiste(true);
                            consultaDinamica.setIndices(indices);
                            consultaDinamicas.add(consultaDinamica);
                        }
                    }

                    if (consultaDinamicas.isEmpty()) {
                        consultaDinamica = new ConsultaDinamica();
                        consultaDinamica.setExiste(false);
                        consultaDinamicas.add(consultaDinamica);
                    }

                } else {
                    throw new DW4JServiciosException("sin resultado en la primera consulta");
                }

                traza.trace("tamaño total lista consulta dinamica " + consultaDinamicas.size(), Level.INFO);

            } else {
                throw new DW4JServiciosException("lista de indices vacia, \n no se raliza la consulta");
            }
        } catch (DW4JServiciosException e) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultaDinamicas.add(consultaDinamica);
            traza.trace("error al armar la consulta dinamica", Level.ERROR, e);
        } catch (SQLException e) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultaDinamicas.add(consultaDinamica);
            traza.trace("error en la consulta dinamica " + consultaDinamica.isExiste(), Level.ERROR, e);
        } catch (Exception e) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultaDinamicas.add(consultaDinamica);
            traza.trace("error general en la busqueda", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        return consultaDinamicas;
    }

    /**
     *
     * @param idSubCategorias
     * @return
     */
    @WebMethod(operationName = "buscarTipoDocumento")
    public List<TipoDocumento> buscarTipoDocumento(@WebParam(name = "idSubCategorias") List<Integer> idSubCategorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        String ids = "";
        int cont = 0;
        TipoDocumento tp;
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        ResultSet rsTipoDoc;

        traza.trace("Buscando tipos de documentos con los id sub-categoria: " + idSubCategorias, Level.INFO);
        try {

            if (idSubCategorias.size() == 1) {
                ids = String.valueOf(idSubCategorias.get(0));
            } else {
                for (Integer in : idSubCategorias) {

                    ids = ids.trim() + ",";
                    if (cont == 0) {
                        //borro la primera coma
                        ids = ids.replace(",", "");
                        ids = ids + in;
                    } else {
                        ids = ids + in;
                    }
                    cont++;
                }
            }

            traza.trace("idSubCategorias: " + ids, Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_tipo_documento( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, ids);

            stored.execute();

            rsTipoDoc = (ResultSet) stored.getObject(1);

            traza.trace("rsTipoDoc despues de la ejecucion " + rsTipoDoc, Level.INFO);

            while (rsTipoDoc.next()) {

                int id = rsTipoDoc.getInt("ID_DOCUMENTO");
                String tipoDoc = rsTipoDoc.getString("TIPO_DOCUMENTO");
                String estatus = rsTipoDoc.getString("ESTATUS");
                String vencimiento = rsTipoDoc.getString("VENCIMIENTO");

                traza.trace("id del tipo documento " + id, Level.INFO);
                traza.trace("tipo documento encontrado " + tipoDoc, Level.INFO);
                traza.trace("estatus del tipo documento " + estatus, Level.INFO);
                traza.trace("vencimiento del tipo documento " + vencimiento, Level.INFO);

                tp = new TipoDocumento();
                tp.setTipoDocumento(tipoDoc);
                tp.setIdTipoDocumento(id);
                tp.setIdSubCategoria(rsTipoDoc.getInt("ID_SUBCATEGORIA"));
                tp.setIdCategoria(rsTipoDoc.getInt("ID_CATEGORIA"));
                tp.setVencimiento(vencimiento);
                tp.setEstatus(estatus);
                tipoDocumentos.add(tp);

            }

            traza.trace("tamaño de la lista " + tipoDocumentos.size(), Level.INFO);

        } catch (SQLException ex) {
            traza.trace("Error al buscar el tipo de documento", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        return tipoDocumentos;

    }

    /**
     *
     * @param idCategorias
     * @return
     */
    @WebMethod(operationName = "buscarSubCategorias")
    public List<SubCategoria> buscarSubCategorias(@WebParam(name = "idCategorias") List<Integer> idCategorias) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        String ids = "";
        int cont = 0;
        SubCategoria SC;
        List<SubCategoria> subCategorias = new ArrayList<>();
        ResultSet rsSubCate;

        traza.trace("Buscando las subcategorias con los id categoria: " + idCategorias, Level.INFO);
        try {

            if (idCategorias.size() == 1) {
                ids = String.valueOf(idCategorias.get(0));
            } else {
                for (Integer in : idCategorias) {

                    ids = ids.trim() + ",";
                    if (cont == 0) {
                        //borro la primera coma
                        ids = ids.replace(",", "");
                        ids = ids + in;
                    } else {
                        ids = ids + in;
                    }
                    cont++;
                }
            }

            traza.trace("idSubCategorias: " + ids, Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_subcategorias( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, ids);

            stored.execute();

            rsSubCate = (ResultSet) stored.getObject(1);

            traza.trace("rsTipoDoc despues de la ejecucion " + rsSubCate, Level.INFO);

            while (rsSubCate.next()) {

                int id = rsSubCate.getInt("id_subcategoria");
                String subCateg = rsSubCate.getString("subcategoria");
                String estatus = rsSubCate.getString("ESTATUS");

                traza.trace("id de la subCategorias " + id, Level.INFO);
                traza.trace("subCategoria encontrada " + subCateg, Level.INFO);
                traza.trace("estatus de la subCategoria" + estatus, Level.INFO);

                SC = new SubCategoria();
                SC.setSubCategoria(subCateg);
                SC.setIdSubCategoria(id);
                SC.setIdCategoria(rsSubCate.getInt("ID_CATEGORIA"));
                SC.setEstatus(estatus);
                subCategorias.add(SC);

            }

            traza.trace("tamaño de la lista " + subCategorias.size(), Level.INFO);

        } catch (SQLException ex) {
            traza.trace("Error al buscar las subCategorias", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        return subCategorias;

    }

    /**
     * Busca la imagen que identifica al expediente
     *
     * @param idExpediente Identificador del expediente
     * @return Objeto con la informacion ruta y nombre del archivo de imagen
     */
    @WebMethod(operationName = "buscarFicha")
    public InfoDocumento buscarFicha(@WebParam(name = "idExpediente") String idExpediente) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        InfoDocumento infoDocumento = null;
        ResultSet rsFicha;

        try {
            traza.trace("buscando el tipo de documento con la foto de la ficha", Level.INFO);
            traza.trace("expediente " + idExpediente, Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_foto_ficha( ? ) } ");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.execute();

            rsFicha = (ResultSet) stored.getObject(1);

            traza.trace("el resultset de la foto ficha " + rsFicha, Level.INFO);

            if (rsFicha.next()) {
                infoDocumento = new InfoDocumento();

                infoDocumento.setIdInfoDocumento(rsFicha.getInt("ID_INFODOCUMENTO"));
                infoDocumento.setIdInfoDocumento(rsFicha.getInt("ID_DOCUMENTO"));
                infoDocumento.setNombreArchivo(rsFicha.getString("nombreArchivo"));
                infoDocumento.setRutaArchivo(rsFicha.getString("RUTA"));
                infoDocumento.setEstatus(rsFicha.getInt("ESTATUS_DOCUMENTO"));

            }

        } catch (SQLException e) {
            traza.trace("error al buscar la foto de la ficha", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        traza.trace("el infoDocumento " + infoDocumento, Level.INFO);

        return infoDocumento;
    }

    @WebMethod(operationName = "buscarExpedienteGenerico")
    public List<ConsultaDinamica> buscarExpedienteGenerico(
            @WebParam(name = "listaIndice") List<com.develcom.dao.Indice> listaIndice,
            @WebParam(name = "idLibreria") int idLibreria,
            @WebParam(name = "idCategorias") String idCategorias) {

        BusquedaDinamicaUtilidad utilidad = new BusquedaDinamicaUtilidad();
        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsExpeGene;
        List<ConsultaDinamica> consultaDinamicas = new ArrayList<>();
        ConsultaDinamica consultaDinamica;
        List<Indice> indices = new ArrayList<>();
        Indice indice;
        String WHERE, tmp = "";
        int registro = 1;
        List<String> expedientes = new ArrayList<>();

        try {

            if (!listaIndice.isEmpty()) {

                traza.trace("creando los filtros de la consulta dinamica", Level.INFO);
                WHERE = utilidad.crearFiltrosIndices(listaIndice, new ArrayList<Categoria>(),
                        new ArrayList<SubCategoria>(), new ArrayList<TipoDocumento>(), true);
                traza.trace("filtros consulta dinamica " + WHERE, Level.INFO);

//                WHERE = WHERE + "  and l.id_libreria=" + idLibreria + " and c.id_categoria in (" + idCategorias
//                        + ")\n ORDER BY e.expediente, i.id_indice";
                WHERE = WHERE + " AND d.ESTATUS_DOCUMENTO=1"
                        + " AND c.id_categoria=" + utilidad.getIdCategoria()
                        + " AND l.id_libreria=" + idLibreria + "\n ORDER BY e.expediente, i.id_indice";

                traza.trace("filtro antes de llamar al procedimiento \"f_buscar_expediente_generico\" " + WHERE, Level.INFO);

                stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente_generico( ?, ?, ? ) }");
                stored.registerOutParameter(1, Types.OTHER);
                stored.setString(2, WHERE);
                stored.setString(3, null);
                stored.setString(4, "1");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                stored.execute();

                rsExpeGene = (ResultSet) stored.getObject(1);

                while (rsExpeGene.next()) {
                    if (!tmp.equalsIgnoreCase(rsExpeGene.getString("expediente"))) {
                        tmp = rsExpeGene.getString("expediente");
                        traza.trace("expediente " + tmp, Level.INFO);
                        expedientes.add(rsExpeGene.getString("expediente"));
                    }
                }

                tmp = "";

                if (!expedientes.isEmpty()) {
                    rsExpeGene.close();

                    traza.trace("tamaño lista de resultado de la consulta para obtener los expedientes " + expedientes.size(), Level.INFO);

                    for (String expe : expedientes) {

                        stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente_generico( ?, ?, ? ) }");
                        stored.registerOutParameter(1, Types.OTHER);
                        stored.setString(2, null);
                        stored.setString(3, expe);
                        stored.setString(4, " ");

                        stored.execute();

                        rsExpeGene = (ResultSet) stored.getObject(1);

                        while (rsExpeGene.next()) {

                            String clave = rsExpeGene.getString("clave");

                            indice = new Indice();

                            indice.setClave(clave);
                            indice.setIdIndice(rsExpeGene.getInt("id_indice"));
                            indice.setIndice(rsExpeGene.getString("indice"));
                            indice.setTipo(rsExpeGene.getString("tipo"));
                            indice.setIdCategoria(rsExpeGene.getInt("id_categoria"));

                            if (rsExpeGene.getObject("valor") != null) {
                                traza.trace("indice " + rsExpeGene.getString("indice") + " valor " + rsExpeGene.getObject("valor") + " clave " + clave, Level.INFO);
                                indice.setValor(rsExpeGene.getObject("valor"));
                            } else {
                                traza.trace("indice " + rsExpeGene.getString("indice") + " valor " + rsExpeGene.getObject("valor") + " clave " + clave, Level.INFO);
                                indice.setValor(sdf.format(rsExpeGene.getDate("fecha_indice")));
                            }

                            if (clave.equalsIgnoreCase("y")) {

                                if (!tmp.equalsIgnoreCase(rsExpeGene.getObject("valor").toString())) {

                                    if (rsExpeGene.getObject("valor") != null) {
                                        tmp = rsExpeGene.getObject("valor").toString();
                                    } else {
                                        tmp = sdf.format(rsExpeGene.getDate("fecha_indice"));
                                    }
                                } else {
                                    consultaDinamica = new ConsultaDinamica();
                                    consultaDinamica.setExiste(true);
                                    consultaDinamica.setIndices(indices);
                                    consultaDinamicas.add(consultaDinamica);
                                    indices = new ArrayList<>();
                                }
                            }

                            indices.add(indice);
                        }
                        rsExpeGene.close();

                        traza.trace("tamaño total lista de indices " + indices.size() + " del expediente " + expe, Level.INFO);

                        if (!indices.isEmpty()) {
                            consultaDinamica = new ConsultaDinamica();
                            consultaDinamica.setExiste(true);
                            consultaDinamica.setIndices(indices);
                            consultaDinamicas.add(consultaDinamica);
                        }
                    }

                    if (consultaDinamicas.isEmpty()) {
                        consultaDinamica = new ConsultaDinamica();
                        consultaDinamica.setExiste(false);
                        consultaDinamicas.add(consultaDinamica);
                    }

                } else {
                    throw new DW4JServiciosException("sin resultado en la primera consulta");
                }

//                while (rsExpeGene.next()) {
//
//                    traza.trace("registro " + registro, Level.INFO);
//                    traza.trace("expediente " + rsExpeGene.getString("expediente"), Level.INFO);
//                    traza.trace("id indice " + rsExpeGene.getString("id_indice"), Level.INFO);
//                    traza.trace("indice " + rsExpeGene.getString("indice"), Level.INFO);
//                    traza.trace("valor " + rsExpeGene.getString("valor"), Level.INFO);
//                    traza.trace("clave " + rsExpeGene.getString("clave"), Level.INFO);
//                    traza.trace("tipo " + rsExpeGene.getString("tipo"), Level.INFO);
//
//                    if (!rsExpeGene.getString("clave").equalsIgnoreCase("o")) {
//                        indice = new Indice();
//                        indice.setIdIndice(rsExpeGene.getInt("id_indice"));
//                        indice.setIndice(rsExpeGene.getString("indice"));
//
//                        if (rsExpeGene.getString("clave") != null) {
//                            indice.setClave(rsExpeGene.getString("clave"));
//                        } else {
//                            indice.setClave("");
//                        }
//
//                        indice.setTipo(rsExpeGene.getString("tipo"));
//
//                        if (rsExpeGene.getObject("valor") != null) {
//                            indice.setValor(rsExpeGene.getObject("valor"));
//                        } else {
//                            indice.setValor(sdf.format(rsExpeGene.getDate("fecha_indice")));
//                        }
//
//                        indices.add(indice);
//                    }
//
//                    registro++;
//
//                }
//
//                traza.trace("tamaño total lista indices " + indices.size(), Level.INFO);
//
//                if (!indices.isEmpty()) {
//                    consultaDinamica.setExiste(true);
//                    consultaDinamica.setIndices(indices);
//                    consultaDinamicas.add(consultaDinamica);
//                    rsExpeGene.close();
//                } else {
//                    consultaDinamica.setExiste(false);
//                    consultaDinamicas.add(consultaDinamica);
//                }
            } else {
                throw new DW4JServiciosException("lista de indices vacia, \n no se raliza la consulta");
            }

        } catch (DW4JServiciosException e) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultaDinamicas.add(consultaDinamica);
            traza.trace("error al armar la consulta dinamica", Level.ERROR, e);

        } catch (SQLException ex) {
            consultaDinamica = new ConsultaDinamica();
            consultaDinamica.setExiste(false);
            consultaDinamicas.add(consultaDinamica);
            traza.trace("error al buscar expedientes genericos", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        return consultaDinamicas;

    }

}
