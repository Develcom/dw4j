/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;

import com.develcom.logs.Traza;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Level;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author develcom
 */
public class BaseDato {

    /**
     * Objeto coneccion a base de datos
     */
    private Connection conect;
    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(BaseDato.class);

    private Connection conec() {
        try {
            InitialContext context = new InitialContext();
            Context envCtx = (Context) context.lookup("java:comp/env");
//            traza.trace("context "+envCtx, Level.INFO);
            DataSource ds = (DataSource) envCtx.lookup("jdbc/dw4j");
//            traza.trace("DataSource "+ds, Level.INFO);
            conect = ds.getConnection();
            conect.setAutoCommit(false);
        } catch (SQLException ex) {
            traza.trace("error en la conexion con la base de datos", Level.ERROR, ex);
        } catch (NamingException ex) {
            traza.trace("error en el contexto de la base de datos", Level.ERROR, ex);
        }
        return conect;
    }

    public Connection conectar() throws SQLException {

        Properties prop = Propiedades.cargarPropiedadesWS();
        PGPoolingDataSource pgDS = new PGPoolingDataSource();

        traza.trace("servidor base datos: " + prop.getProperty("srvbasedato"), Level.INFO);
        traza.trace("base datos: " + prop.getProperty("basedato"), Level.INFO);
        traza.trace("usuario base datos: " + prop.getProperty("userbasedato"), Level.INFO);

        pgDS.setServerName(prop.getProperty("srvbasedato"));
        pgDS.setDatabaseName(prop.getProperty("basedato"));
        pgDS.setPortNumber(Integer.parseInt(prop.getProperty("portbasedato")));
        pgDS.setUser(prop.getProperty("userbasedato"));
        pgDS.setPassword(prop.getProperty("passbasedato"));
        conect = pgDS.getConnection();

        traza.trace("conect " + conect, Level.INFO);

        if (conect != null) {
            traza.trace("coneccion exitosa", Level.INFO);
            conect.setAutoCommit(false);
            traza.trace("commit de la coneccion " + conect.getAutoCommit(), Level.INFO);
        } else {
            traza.trace("problemas en la coneccion", Level.ERROR);
        }
        return conect;
    }

    public Connection conectar(String baseDatos) {

        Properties prop = Propiedades.cargarPropiedadesWS();
        PGPoolingDataSource pgDS = new PGPoolingDataSource();

        try {
            traza.trace("servidor base datos: " + prop.getProperty("srvbasedato"), Level.INFO);
            traza.trace("base datos: " + prop.getProperty("basedato"), Level.INFO);
            traza.trace("usuario base datos: " + prop.getProperty("userbasedato"), Level.INFO);

            pgDS.setServerName(prop.getProperty("srvbasedato"));
            pgDS.setDatabaseName(baseDatos);
            pgDS.setPortNumber(Integer.parseInt(prop.getProperty("portbasedato")));
            pgDS.setUser(prop.getProperty("userbasedato"));
            pgDS.setPassword(prop.getProperty("passbasedato"));
            conect = pgDS.getConnection();

            traza.trace("conect " + conect, Level.INFO);

            if (conect != null) {
                traza.trace("coneccion exitosa", Level.INFO);
                conect.setAutoCommit(false);
                traza.trace("commit de la coneccion " + conect.getAutoCommit(), Level.INFO);
            } else {
                traza.trace("problemas en la coneccion", Level.ERROR);
            }
        } catch (SQLException e) {
            traza.trace("problemas de coneccion con la base de datos " + baseDatos, Level.ERROR, e);
            return null;
        }
        return conect;
    }

    /**
     * Desconecta la base de datos
     *
     * @throws SQLException Es lanzada si ya esta desconectada la base de datos
     */
    public void desconectar() throws SQLException {
        if (conect != null) {
            if (!conect.getAutoCommit()) {
                conect.commit();
            }
            traza.trace("desconectandose de la base de datos", Level.INFO);
            conect.close();
            System.gc();
        }
    }

    /**
     * Comprueba si la coneccion con la base de datos esta abierta
     *
     * @return Verdadero si esta cerrada falso en caso contrario
     * @throws SQLException
     */
    public boolean isClosed() throws SQLException {
        boolean resp = false;
        traza.trace("verificando si esta cerrada la coneccion", Level.INFO);
        if (conect != null) {
            resp = conect.isClosed();
        }
        return resp;
    }

    /**
     * Realiza una consulta en la base de datos (select)
     *
     * @param consulta Una cadena que contien la consulta
     * @return
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     */
    public ResultSet consultas(String consulta) throws SQLException {
        ResultSet rs;
        traza.trace("se raliza la siguiente consulta: \n" + consulta, Level.INFO);

        Statement buscar = conectar().createStatement();
//        Statement buscar = conectar().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        Statement buscar = conectar().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

//        Statement buscar = conectar().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        Statement buscar = conectar().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rs = buscar.executeQuery(consulta);

        if (rs != null) {
            traza.trace("consulta exitosa", Level.INFO);
        } else {
            traza.trace("error en la consulta", Level.ERROR);
        }
        return rs;
    }

    public boolean ejecutarSentencia(String sql) throws SQLException {
        boolean resp = false;
        int res = -1, i = -1, u = -1, d = -1, t = -1, a = -1, drop = -1;
        Statement stmt;

        t = sql.indexOf("create table");
        a = sql.indexOf("alter table");
        drop = sql.indexOf("DROP");
        if ((t == -1) && (a == -1) && (drop == -1)) {
            i = sql.indexOf("insert");
            u = sql.indexOf("update");
            d = sql.indexOf("delete");
        }
        traza.trace("se crea una tabla " + t, Level.INFO);
        traza.trace("se modifica una tabla " + a, Level.INFO);
        traza.trace("se elimina una tabla " + drop, Level.INFO);
        traza.trace("se inserta registro " + i, Level.INFO);
        traza.trace("se actualiza registro " + u, Level.INFO);
        traza.trace("se elimina registro " + d, Level.INFO);
        traza.trace("se realiza la siguiente sentencia: \n" + sql, Level.INFO);

        stmt = conectar().createStatement();

        if (t != -1) {
            res = stmt.executeUpdate(sql);
            traza.trace("respuesta en la sentecia crear tabla " + res, Level.INFO);
            resp = true;
        } else if (a != -1) {
            res = stmt.executeUpdate(sql);
            traza.trace("respuesta en la sentecia modificar tabla " + res, Level.INFO);
            resp = true;
        } else if (drop != -1) {
            res = stmt.executeUpdate(sql);
            traza.trace("respuesta en la sentecia eliminar tabla " + res, Level.INFO);
            resp = true;
        } else {
            res = stmt.executeUpdate(sql);
            traza.trace("respuesta en la sentecia " + res, Level.INFO);
        }
        stmt.close();

        if (res >= 1) {
            resp = true;
            if (i != -1) {
                traza.trace("exito al insertar (insert)", Level.INFO);
            }
            if (u != -1) {
                traza.trace("exito al actualizar (update)", Level.INFO);
            }
            if (d != -1) {
                traza.trace("exito al eliminar (delete)", Level.INFO);
            }
        } else {
            if (i != -1) {
                traza.trace("error al insertar (insert)", Level.ERROR);
            }
            if (u != -1) {
                traza.trace("error al actualizar (update)", Level.ERROR);
            }
            if (d != -1) {
                traza.trace("error al eliminar (delete)", Level.ERROR);
            }
        }

        return resp;
    }

    /**
     * Realiza el commit en la base de datos
     *
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     */
    public void commit() throws SQLException {
        traza.trace("se raliza el commit", Level.INFO);
        conect.commit();
        traza.trace("commit de la coneccion " + conect.getAutoCommit(), Level.INFO);
    }

    /**
     * Realiza el rollback en la base de datos
     *
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     */
    public void rollback() throws SQLException {
        traza.trace("se realiza el rollback", Level.INFO);
        conect.rollback();
    }
}
