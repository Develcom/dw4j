package com.develcom.migrardatos.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.postgresql.ds.PGPoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDato {

    @Autowired
    private Propiedades propiedades;
    
    private Connection conect;

    private final Logger traza = LoggerFactory.getLogger(BaseDato.class);

    public Connection conectar() throws SQLException {

//        Propiedades propiedades = new Propiedades();

        Properties prop = propiedades.configuracionBaseDatos();
        PGPoolingDataSource pgDS = new PGPoolingDataSource();

        traza.info("servidor base datos: " + prop.getProperty("srvbasedato"));
        traza.info("base datos: " + prop.getProperty("basedato"));
        traza.info("usuario base datos: " + prop.getProperty("userbasedato"));

        pgDS.setServerName(prop.getProperty("srvbasedato"));
        pgDS.setDatabaseName(prop.getProperty("basedato"));
        pgDS.setPortNumber(Integer.parseInt(prop.getProperty("portbasedato")));
        pgDS.setUser(prop.getProperty("userbasedato"));
        pgDS.setPassword(prop.getProperty("passbasedato"));
        conect = pgDS.getConnection();
        traza.info("conect " + conect);

        if (conect != null) {
            traza.info("coneccion exitosa");
            conect.setAutoCommit(false);
            traza.info("commit de la coneccion " + conect.getAutoCommit());
        } else {
            traza.info("problemas en la coneccion");
        }
        return conect;
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

        Connection con;
        ResultSet rs;

        traza.info("se raliza la siguiente consulta: \n" + consulta);
        con = conectar();
        Statement buscar = con.createStatement();
        rs = buscar.executeQuery(consulta);

        if (rs != null) {
            traza.info("consulta exitosa");
        } else {
            traza.info("error en la consulta");
        }
        con.close();
        return rs;
    }

    public void commit() throws SQLException {
        traza.info("se raliza el commit");
        conect.commit();
        traza.info("commit de la coneccion " + conect.getAutoCommit());
    }

    public void desconectar() throws SQLException {
        if (conect != null) {
            if (!conect.getAutoCommit()) {
                conect.commit();
            }
            traza.info("desconectandose de la base de datos");
            conect.close();
            System.gc();
        }
    }

//    private Connection conec() {
//        try {
//            InitialContext context = new InitialContext();
//            Context envCtx = (Context) context.lookup("java:comp/env");
////            traza.info("context "+envCtx);
//            DataSource ds = (DataSource) envCtx.lookup("jdbc/dw4j");
////            traza.info("DataSource "+ds);
//            conect = ds.getConnection();
//            conect.setAutoCommit(false);
//        } catch (SQLException ex) {
//            traza.info("error en la conexion con la base de datos", ex);
//        } catch (NamingException ex) {
//            traza.info("error en el contexto de la base de datos", ex);
//        }
//        return conect;
//    }
}
