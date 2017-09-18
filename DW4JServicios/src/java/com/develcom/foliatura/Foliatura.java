package com.develcom.foliatura;

import com.develcom.dao.Folio;
import com.develcom.dao.Indice;
import com.develcom.dao.Indices;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Level;

@WebService(serviceName = "Foliatura")
public class Foliatura {

    private Traza traza = new Traza(Foliatura.class);

    @WebMethod(operationName = "crearFoliatura")
    public boolean crearFoliatura(@WebParam(name = "idExpediente") String idExpediente, @WebParam(name = "idLibreria") int idLibreria, @WebParam(name = "idCategoria") int idCategoria) {

        BaseDato bd = new BaseDato();
        CallableStatement stored;
        ResultSet rsFoliatura;
        boolean resp = false;
        List<Folio> listaFoliatura = new ArrayList<>();
        int pagina = 1;
        int registro = 1;
        Folio foliatura;

        this.traza.trace("idExpediente " + idExpediente, Level.INFO);
        this.traza.trace("idLibreria " + idLibreria, Level.INFO);
        this.traza.trace("idCategoria " + idCategoria, Level.INFO);

        try {

            bd.ejecutarSentencia("DELETE FROM FOLIATURA");
            bd.commit();
            bd.desconectar();

            stored = bd.conectar().prepareCall("{ ? = call f_foliatura_buscar_expediente( ?, ?, ? ) }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.setString(2, idExpediente);
            stored.setInt(3, idLibreria);
            stored.setInt(4, idCategoria);
            stored.execute();

            rsFoliatura = (ResultSet) stored.getObject(1);

            while (rsFoliatura.next()) {

                this.traza.trace("registro " + registro, Level.INFO);

                int cantidadPagina = rsFoliatura.getInt("cantidadPAginas");
                this.traza.trace("cantidad de paginas " + cantidadPagina, Level.INFO);
                this.traza.trace("documento " + rsFoliatura.getString("documento"), Level.INFO);

                for (int i = 0; i < cantidadPagina; i++) {

                    foliatura = new Folio();

                    foliatura.setExpediente(rsFoliatura.getString("ID_EXPEDIENTE"));
                    foliatura.setIdDocumento(rsFoliatura.getInt("ID_DOCUMENTO"));
                    foliatura.setIdInfoDocumento(rsFoliatura.getInt("ID_INFODOCUMENTO"));
                    foliatura.setIdSubCategoria(rsFoliatura.getInt("ID_SUBCATEGORIA"));

                    this.traza.trace("idExpediente " + foliatura.getExpediente(), Level.INFO);
                    this.traza.trace("idLibreria " + rsFoliatura.getInt("ID_LIBRERIA"), Level.INFO);
                    this.traza.trace("idCategoria " + rsFoliatura.getInt("ID_CATEGORIA"), Level.INFO);
                    this.traza.trace("idSubCategoria " + foliatura.getIdSubCategoria(), Level.INFO);
                    this.traza.trace("idDocumento " + foliatura.getIdDocumento(), Level.INFO);
                    this.traza.trace("idInfoDocumento " + foliatura.getIdInfoDocumento(), Level.INFO);
                    this.traza.trace("documento " + rsFoliatura.getString("documento"), Level.INFO);

                    this.traza.trace("pagina " + pagina, Level.INFO);
                    foliatura.setPagina(pagina);
                    listaFoliatura.add(foliatura);
                    pagina++;
                }
                registro++;
            }

            this.traza.trace("tamaño lista " + listaFoliatura.size(), Level.INFO);

            stored = null;

            if (!listaFoliatura.isEmpty()) {
                bd.desconectar();
                for (Folio folio : listaFoliatura) {

                    if (stored == null) {
                        stored = bd.conectar().prepareCall("{ call p_agregar_foliaturas( ?, ?, ?, ? ) } ");
                    }

                    stored.setInt(1, folio.getIdInfoDocumento());
                    stored.setInt(2, folio.getIdDocumento());
                    stored.setString(3, folio.getExpediente());
                    stored.setInt(4, folio.getPagina());
                    stored.execute();

                    bd.commit();
                    resp = true;
                    //bd.desconectar();
                }

            } else {
                throw new DW4JServiciosException("lista busquedad de expediente foliatura vacia");
            }

        } catch (DW4JServiciosException e) {
            resp = false;
            this.traza.trace(e.getMessage(), Level.ERROR, e);
        } catch (SQLException e) {
            resp = false;
            this.traza.trace("error al buscar expediente para la foliatura", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }

        this.traza.trace("respuesta al generar la foliatura " + resp, Level.INFO);

        return resp;
    }

    /**
     * Crea una tabla temporal para los reportes dinamicos
     *
     * @param idCategoria EL identificador de la Categoria
     * @param consulta
     * @return Verdadero si se creo la tablas con sus datos correspondientes,
     * falso en caso contrario
     */
    @WebMethod(operationName = "crearReporte")
    public boolean crearReporte(@WebParam(name = "idCategoria") int idCategoria, @WebParam(name = "consulta") String consulta) {

        boolean resp = false, eliminar = false, crear, clave1 = false, clave2 = false, clave3 = false, clave4 = false, sec = false;
        BaseDato bd = new BaseDato();
        CallableStatement stored;
        String campos, crearTabla, data = "", tmp = "", idExpediente = "";
        int sizeList, cont = 0;
        ResultSet rsConsulta;
        List<Indice> indices = new ArrayList<>();
        Indice indice;
        List<String> querys = new ArrayList<>();

        try {

            traza.trace("id de la categoria " + idCategoria, Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_expediente_reporte( ? ) } ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stored.registerOutParameter(1, Types.OTHER);
            stored.setInt(2, idCategoria);
            stored.execute();

            rsConsulta = (ResultSet) stored.getObject(1);

            while (rsConsulta.next()) {

                traza.trace("variable temporal " + tmp, Level.INFO);

                if (rsConsulta.getString("valor") != null) {

                    if (rsConsulta.getString("clave").equalsIgnoreCase("y")) {

                        if (!tmp.equalsIgnoreCase(rsConsulta.getString("valor"))) {

                            tmp = rsConsulta.getString("valor");
                            sec = true;

                            indice = new Indice();

                            indice.setTipo(rsConsulta.getString("tipo"));
                            indice.setIndice(rsConsulta.getString("indice"));
                            indice.setValor(rsConsulta.getString("valor"));
                            indice.setClave(rsConsulta.getString("clave"));
                            indice.setIdIndice(rsConsulta.getInt("id_indice"));
                            indices.add(indice);

                            traza.trace("indice principal " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);
                        }
                    } else {

                        if (sec) {
                            do {
                                if (rsConsulta.getString("clave").equalsIgnoreCase("s")) {

                                    if (rsConsulta.getString("valor") != null) {

                                        indice = new Indice();

                                        indice.setTipo(rsConsulta.getString("tipo"));
                                        indice.setIndice(rsConsulta.getString("indice"));
                                        indice.setValor(rsConsulta.getString("valor"));
                                        indice.setClave(rsConsulta.getString("clave"));
                                        indice.setIdIndice(rsConsulta.getInt("id_indice"));
                                        indices.add(indice);

                                        traza.trace("indice secundario " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);

                                    } else if (rsConsulta.getString("fecha_indice") != null) {

                                        indice = new Indice();

                                        indice.setTipo(rsConsulta.getString("tipo"));
                                        indice.setIndice(rsConsulta.getString("indice"));
                                        indice.setValor(rsConsulta.getString("fecha_indice"));
                                        indice.setClave(rsConsulta.getString("clave"));
                                        indice.setIdIndice(rsConsulta.getInt("id_indice"));
                                        indices.add(indice);

                                        traza.trace("indice secundario " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);
                                    }

                                } else {

                                    if (rsConsulta.getString("clave").equalsIgnoreCase("y")) {
                                        rsConsulta.previous();
                                        sec = false;
                                        break;
                                    }
                                }
                            } while (rsConsulta.next());
                        }
                    }

                } else if (rsConsulta.getString("fecha_indice") != null) {

                    if (rsConsulta.getString("clave").equalsIgnoreCase("y")) {

                        if (!tmp.equalsIgnoreCase(rsConsulta.getString("fecha_indice"))) {

                            tmp = rsConsulta.getString("fecha_indice");

                            indice = new Indice();

                            indice.setTipo(rsConsulta.getString("tipo"));
                            indice.setIndice(rsConsulta.getString("indice"));
                            indice.setValor(rsConsulta.getString("fecha_indice"));
                            indice.setClave(rsConsulta.getString("clave"));
                            indice.setIdIndice(rsConsulta.getInt("id_indice"));
                            indices.add(indice);

                            traza.trace("indice principal " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);
                        }
                    } else {

                        if (sec) {

                            do {

                                if (rsConsulta.getString("clave").equalsIgnoreCase("s")) {

                                    if (!tmp.equalsIgnoreCase(rsConsulta.getString("valor"))) {

                                        if (rsConsulta.getString("valor") != null) {

                                            indice = new Indice();

                                            indice.setTipo(rsConsulta.getString("tipo"));
                                            indice.setIndice(rsConsulta.getString("indice"));
                                            indice.setValor(rsConsulta.getString("valor"));
                                            indice.setClave(rsConsulta.getString("clave"));
                                            indice.setIdIndice(rsConsulta.getInt("id_indice"));
                                            indices.add(indice);

                                            traza.trace("indice secundario " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);

                                        } else if (rsConsulta.getString("fecha_indice") != null) {

                                            indice = new Indice();

                                            indice.setTipo(rsConsulta.getString("tipo"));
                                            indice.setIndice(rsConsulta.getString("indice"));
                                            indice.setValor(rsConsulta.getString("fecha_indice"));
                                            indice.setClave(rsConsulta.getString("clave"));
                                            indice.setIdIndice(rsConsulta.getInt("id_indice"));
                                            indices.add(indice);

                                            traza.trace("indice secundario " + indice.getIndice() + " valor " + indice.getValor(), Level.INFO);
                                        }
                                    }

                                } else {

                                    if (rsConsulta.getString("clave").equalsIgnoreCase("y")) {
                                        rsConsulta.previous();
                                        break;
                                    }
                                }

                            } while (rsConsulta.next());
                        }
                    }
                }
            }
            traza.trace("lista de indice vacia? " + indices.isEmpty(), Level.INFO);

            if (!indices.isEmpty()) {
                rsConsulta.close();

                bd.desconectar();
                rsConsulta = bd.consultas("select table_catalog, table_name, column_name, data_type from information_schema.columns where table_name='reporte'");

                if (rsConsulta.next()) {
                    eliminar = bd.ejecutarSentencia("DROP TABLE reporte");
                    bd.commit();
                }
                traza.trace("se elimino la tabla reporte " + eliminar, Level.INFO);

                crearTabla = "create table reporte (id_categoria integer, expediente character varying(250), ";
                sizeList = indices.size();
                traza.trace("tamaño de la lista de indices a procesar " + sizeList, Level.INFO);

                for (Indices ind : indices) {

                    if (ind.getTipo().equalsIgnoreCase("texto")
                            || ind.getTipo().equalsIgnoreCase("combo")
                            || ind.getTipo().equalsIgnoreCase("area")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " character varying(250)";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " character varying(250), ";
                            }
                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " character varying(250)";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " character varying(250), ";
                            }
                        }

                    } else if (ind.getTipo().equalsIgnoreCase("numero")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " integer";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " integer, ";
                            }
                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " integer";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " integer, ";
                            }
                        }

                    } else if (ind.getTipo().equalsIgnoreCase("fecha")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " date";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " date, ";
                            }
                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (cont == 3) {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " date";
                            } else {
                                crearTabla = crearTabla + ind.getIndice().replace(" ", "_") + " date, ";
                            }
                        }
                    }

                    traza.trace("variable crear tabla " + crearTabla, Level.INFO);
                    traza.trace("contador " + cont, Level.INFO);
                    if (cont == 3) {
                        break;
                    }
                }

                crearTabla = crearTabla + ")";
                cont = crearTabla.lastIndexOf(", )");
                traza.trace("variable que verifica si termina en una coma " + cont, Level.INFO);
                if (cont != -1) {
                    crearTabla = crearTabla.substring(0, cont) + ")";
                }

                cont = 0;
                campos = "insert into reporte (id_categoria,expediente,";
                for (Indice ind : indices) {

                    if (ind.getTipo().equalsIgnoreCase("texto")
                            || ind.getTipo().equalsIgnoreCase("combo")
                            || ind.getTipo().equalsIgnoreCase("area")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            clave1 = true;
                            idExpediente = ind.getValor().toString();
                            traza.trace("expediente " + idExpediente, Level.INFO);

                            if (cont == 3) {
                                data = data + "'" + ind.getValor() + "'";
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + "'" + ind.getValor() + "',";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }

                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (!clave2) {
                                clave2 = true;
                            } else if (!clave3) {
                                clave3 = true;
                            } else if (!clave4) {
                                clave4 = true;
                            }

                            if (cont == 3) {
                                data = data + "'" + ind.getValor() + "'";
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + "'" + ind.getValor() + "',";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }
                        }

                    } else if (ind.getTipo().equalsIgnoreCase("numero")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            clave1 = true;
                            idExpediente = ind.getValor().toString();
                            traza.trace("expediente " + idExpediente, Level.INFO);

                            if (cont == 3) {
                                data = data + ind.getValor();
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + ind.getValor() + ",";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }
                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (!clave2) {
                                clave2 = true;
                            } else if (!clave3) {
                                clave3 = true;
                            } else if (!clave4) {
                                clave4 = true;
                            }

                            if (cont == 3) {
                                data = data + ind.getValor();
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + ind.getValor() + ",";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }
                        }

                    } else if (ind.getTipo().equalsIgnoreCase("fecha")) {

                        if (ind.getClave().equalsIgnoreCase("y")) {

                            clave1 = true;
                            idExpediente = ind.getValor().toString();
                            traza.trace("expediente " + idExpediente, Level.INFO);

                            if (cont == 3) {
                                data = data + "'" + ind.getValor() + "'";
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + "'" + ind.getValor() + "',";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }
                        } else if (ind.getClave().equalsIgnoreCase("s")) {
                            cont++;

                            if (!clave2) {
                                clave2 = true;
                            } else if (!clave3) {
                                clave3 = true;
                            } else if (!clave4) {
                                clave4 = true;
                            }

                            if (cont == 3) {
                                data = data + "'" + ind.getValor() + "'";
                                campos = campos + ind.getIndice().replace(" ", "_");
                            } else {
                                data = data + "'" + ind.getValor() + "',";
                                campos = campos + ind.getIndice().replace(" ", "_") + ",";
                            }
                        }
                    }

                    traza.trace("bandera clave primaria " + clave1, Level.INFO);
                    traza.trace("bandera clave secundaria1 " + clave2, Level.INFO);
                    traza.trace("bandera clave secundaria2 " + clave3, Level.INFO);
                    traza.trace("bandera clave secundaria3 " + clave4, Level.INFO);
                    traza.trace("variable data " + data, Level.INFO);
                    traza.trace("variable campos " + campos, Level.INFO);
                    traza.trace("contador en campos y datos " + cont, Level.INFO);
                    
                    if ((clave1) && (clave2) && (clave3) && (clave4)) {
                        campos = campos + ") values (" + idCategoria + ",'" + idExpediente + "'," + data + ")";
                        querys.add(campos);
                        traza.trace("<<<<<campos completos>>>>>> ".toUpperCase()+campos, Level.INFO);
                        campos = "insert into reporte (id_categoria,expediente,";
                        data = "";
                        clave1 = false;
                        clave2 = false;
                        clave3 = false;
                        clave4 = false;
                        cont = 0;
                    }
                }

                traza.trace("tamaño de la lista de insert " + querys.size(), Level.INFO);

                bd.desconectar();
                crear = bd.ejecutarSentencia(crearTabla);
                traza.trace("respuetsa al crear la tabla " + crear, Level.INFO);

                if (crear) {
                    bd.commit();
                    rsConsulta.close();
                    rsConsulta = bd.consultas("select table_catalog, table_name, column_name, data_type from information_schema.columns where table_name='reporte'");
                    if (rsConsulta.next()) {
                        cont = 0;

                        for (String query : querys) {
                            resp = bd.ejecutarSentencia(query);
                            bd.commit();
                            cont++;
                        }

                        traza.trace("respuesta al agregar los datos " + resp, Level.INFO);
                    } else {
                        throw new DW4JServiciosException("no se creo la tabla reporte");
                    }
                }
            } else {
                throw new DW4JServiciosException("problemas en la consulta inicial para el reporte");
            }

        } catch (DW4JServiciosException e) {
            traza.trace("error en la generacion de gestion de reporte dinamico", Level.ERROR, e);
        } catch (SQLException e) {
            resp = false;
            this.traza.trace("error al crear la tabla para los reportes dinamicos", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        return resp;
    }
}
