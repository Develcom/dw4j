/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.reportes.tools;

import biz.source_code.base64Coder.Base64Coder;
import com.develcom.autentica.Configuracion;
import com.develcom.dao.ManejoSesion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Level;
import org.postgresql.ds.PGPoolingDataSource;
import ve.com.develcom.tools.Traza;

/**
 *
 * @author develcom
 */
public class BaseDato {

    private Traza traza = new Traza(BaseDato.class);

    /**
     * Objeto coneccion a base de datos
     */
    private Connection conect = null;

    /**
     * Establece la connecion con la Base de Datos
     *
     * @return Un objeto con la coneccion establecida
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     * @throws java.lang.ClassNotFoundException
     */
    public Connection conectar() throws SQLException, ClassNotFoundException {
        Configuracion config = ManejoSesion.getConfiguracion();
//        OracleDataSource ods = new OracleDataSource();
//        String user, pass, url;

        traza.trace("servidor " + config.getServerName(), Level.INFO);
        traza.trace("base de dato " + config.getDatabaseName(), Level.INFO);
        traza.trace("puerto " + config.getPort(), Level.INFO);

//        url="jdbc:oracle:thin:@"+config.getServerName()+":"+config.getPort()+":"+config.getDatabaseName();
//        user = Base64Coder.decodeString(config.getUser());
//        pass = Base64Coder.decodeString(config.getPassword());
//        
//        traza.trace("url base dato "+url, Level.INFO);
//        traza.trace("user "+user, Level.INFO);
//        traza.trace("pass "+pass, Level.INFO);
//        
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        conect = DriverManager.getConnection(url,user,pass);

        
        
//        ods.setServerName(config.getServerName());
//        ods.setDatabaseName(config.getDatabaseName());
//        ods.setPortNumber(config.getPort());
//        ods.setDriverType("thin");
//        ods.setUser(Base64Coder.decodeString(config.getUser()));
//        ods.setPassword(Base64Coder.decodeString(config.getPassword()));
        
//        traza.trace("url por ods "+ods.getURL(), Level.INFO);
        
//        conect = ods.getConnection();
        
        
        
        
        PGPoolingDataSource pgDS = new PGPoolingDataSource();
        pgDS.setServerName(config.getServerName());
        pgDS.setDatabaseName(config.getDatabaseName());
        pgDS.setPortNumber(config.getPort());
        pgDS.setUser(Base64Coder.decodeString(config.getUser()));
        pgDS.setPassword(Base64Coder.decodeString(config.getPassword()));
        
        conect = pgDS.getConnection();
        traza.trace("url por ods "+pgDS.getUrl(), Level.INFO);
        
        
        if (conect == null) {
            traza.trace("problemas con la coneccion a la base de datos ", Level.INFO);
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
//            traza.trace("desconectandose de la base de datos", Level.INFO);
            conect.close();
            Runtime.getRuntime().gc();
        }
    }

    /**
     * Comprueba si la coneccion con la base de datos esta abierta
     *
     * @return Verdadero si esta cerrada falso en caso contrario
     * @throws SQLException
     */
    public boolean isClosed() throws SQLException {
//        traza.trace("verificando si esta cerrada la coneccion", Level.INFO);
        return conect.isClosed();
    }

    /**
     * Realiza una consulta en la base de datos (select)
     *
     * @param consulta Una cadena que contien la consulta
     * @return
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     */
    public ResultSet consultas(String consulta) throws SQLException, ClassNotFoundException {
        ResultSet rs = null;
//        traza.trace("se raliza la siguiente consulta: "+consulta, Level.INFO);
        conectar();
        Statement buscar = conect.createStatement();
        rs = buscar.executeQuery(consulta);
        desconectar();

        if (rs != null) {
//            traza.trace("consulta exitosa", Level.INFO);
        } else {
//            traza.trace("error en la consulta", Level.ERROR);
        }
        return rs;
    }

    public boolean ejecutarSentencia(String sql) throws SQLException, ClassNotFoundException {
        boolean resp = false;
        int res = 0, i = 0, u = 0, d = 0, t = 0, a = 0;

        t = sql.indexOf("CREATE TABLE");
        a = sql.indexOf("ALTER TABLE");
        if (t == -1 || a == -1) {
            i = sql.indexOf("INSERT");
            u = sql.indexOf("UPDATE");
            d = sql.indexOf("DELETE");
        }
//        traza.trace("se realiza la siguiente sentencia: \n"+sql, Level.INFO);
        conectar();
        Statement stmt = conect.createStatement();
//        System.out.println(stmt.executeUpdate(sql));
        res = stmt.executeUpdate(sql);
        stmt.close();
//        traza.trace("respuesta en la sentecia "+res, Level.INFO);
        if (res == 1 || res == 0) {
            resp = true;
//            if(resp){
//                if(i!=-1){
//                    traza.trace("exito al insertar (insert)", Level.INFO);
//                }
//                if(u!=-1){
//                    traza.trace("exito al actualizar (update)", Level.INFO);
//                }
//                if(d!=-1){
//                    traza.trace("exito al eliminar (delete)", Level.INFO);
//                }
//                if(t!=-1){
//                    traza.trace("exito al crear la tabla", Level.INFO);
//                }
//                if(a!=-1){
//                    traza.trace("exito al modificar la tabla", Level.INFO);
//                }
//            }else{
//                if(i!=-1){
//                    traza.trace("error al insertar (insert)", Level.ERROR);
//                }
//                if(u!=-1){
//                    traza.trace("error al actualizar (update)", Level.ERROR);
//                }
//                if(d!=-1){
//                    traza.trace("error al eliminar (delete)", Level.ERROR);
//                }
//                if(t!=-1){
//                    traza.trace("error al crear la tabla", Level.ERROR);
//                }
//                if(a!=-1){
//                    traza.trace("error al modificar la tabla", Level.ERROR);
//                }
//
//            }
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
//        traza.trace("se raliza el commit", Level.INFO);
        conect.commit();
//        traza.trace("commit de la coneccion "+conect.getAutoCommit(), Level.INFO);
    }

    /**
     * Realiza el rollback en la base de datos
     *
     * @throws SQLException Una excepcion es lanzada si ocurre algun problema
     * con la base de datos
     */
    public void rollback() throws SQLException {
//        traza.trace("se realiza el rollback", Level.INFO);
        conect.rollback();
    }

//    public static void main(String[] arg){
//        try {
//            BaseDato b = new BaseDato();
//            b.ejecutarSentencia("CREATE TABLE \"PRUEBACATEGORIA1\" (\"ID_INDICE\" INTEGER NOT NULL DEFAULT NEXTVAL('INCREMENTO'::REGCLASS),\"ID_CATEGORIA\" INTEGER NOT NULL,\"ID_SUBCATEGORIA\" INTEGER NOT NULL,\"PRUEBA_INDICE1\" CHARACTER VARYING(250) NOT NULL,\"PRUEBA_INDICE2\" INTEGER NOT NULL,\"PRUEBA_INDICE3\" DATE NOT NULL,\"PRUEBA_INDICE4\" CHARACTER VARYING(250) NOT NULL,\"PRUEBA_INDICE5\" CHARACTER VARYING(250), CONSTRAINT pk_pruebacategoria1 PRIMARY KEY (\"PRUEBA_INDICE1\", \"ID_CATEGORIA\", \"ID_SUBCATEGORIA\"),  CONSTRAINT fk_pruebacategoria1_categoria FOREIGN KEY (\"ID_CATEGORIA\") REFERENCES \"CATEGORIA\" (\"ID_CATEGORIA\") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT fk_pruebacategoria1_subcategoria FOREIGN KEY (\"ID_SUBCATEGORIA\") REFERENCES \"SUBCATEGORIA\" (\"ID_SUBCATEGORIA\") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT uq_pruebacategoria1 UNIQUE (\"ID_INDICE\") )");
//            //b.ejecutarSentencia("INSERT INTO prueba(prueba) VALUES ('inserta')");
//            b.commit();
//            b.desconectar();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
}
