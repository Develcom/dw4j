package com.develcom.mantenimiento;

import biz.source_code.base64Coder.Base64Coder;
import com.develcom.dao.Configuracion;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Level;

@WebService(serviceName = "Mantenimientos")
public class Mantenimientos {

    private Traza traza = new Traza(Mantenimientos.class);

    @WebMethod(operationName = "buscarMantenimiento")
    public Configuracion buscarMantenimiento() {

        String user, pass;
        ResultSet rsConf;
        BaseDato bd = new BaseDato();
        Configuracion conf = null;
        CallableStatement stored;
        int flag;

        try {

            this.traza.trace("buscando la configuracion actual", Level.INFO);

            stored = bd.conectar().prepareCall("{ ? = call f_buscar_configuracion() }");
            stored.registerOutParameter(1, Types.OTHER);
            stored.execute();

            rsConf = (ResultSet) stored.getObject(1);

            if (rsConf.next()) {

                user = rsConf.getString("USERBD");
                pass = rsConf.getString("PASSWORD");

                conf = new Configuracion();

                conf.setCalidadActivo(rsConf.getBoolean("CALIDAD"));
                conf.setDatabaseName(rsConf.getString("DATABASE_NAME"));
                conf.setFoliatura(rsConf.getBoolean("FOLIATURA"));
                conf.setServerName(rsConf.getString("SERVER_NAME"));
                conf.setPort(rsConf.getInt("PORT"));
                if (user != null) {
                    conf.setUser(user);
                } else {
                    conf.setUser("");
                }
                if (pass != null) {
                    conf.setPassword(pass);
                } else {
                    conf.setPassword("");
                }
                conf.setPathTmp(rsConf.getString("RUTA_TEMPORAL"));
                conf.setFileTif(rsConf.getString("ARCHIVO_TIF"));
                conf.setFileCode(rsConf.getString("ARCHIVO_COD"));

                flag = rsConf.getInt("FICHA");

                if (flag == 1) {
                    conf.setFicha(true);
                } else {
                    conf.setFicha(false);
                }

                flag = rsConf.getInt("FABRICA");
                if (flag == 1) {
                    conf.setFabrica(true);
                } else {
                    conf.setFabrica(false);
                }

                flag = rsConf.getInt("ELIMINA");
                if (flag == 1) {
                    conf.setElimina(true);
                } else {
                    conf.setElimina(false);
                }

                flag = rsConf.getInt("LDAP");
                if (flag == 1) {
                    conf.setLdap(true);
                } else {
                    conf.setLdap(false);
                }
            }

        } catch (SQLException ex) {
            this.traza.trace("error en la busqueda de mantenimiento", Level.ERROR, ex);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("objecto de configuracion "+conf, Level.INFO);
        return conf;
    }

    @WebMethod(operationName = "mantenerBaseDatos")
    public boolean mantenerBaseDatos(@WebParam(name = "configuracion") Configuracion configuracion) {

        CallableStatement stored;
        BaseDato bd = new BaseDato();
        boolean resp = false;
        String userEncriptado, passEncriptado;

        try {
            
            this.traza.trace("servidor " + configuracion.getServerName(), Level.INFO);
            this.traza.trace("base de datos " + configuracion.getDatabaseName(), Level.INFO);
            this.traza.trace("puerto " + configuracion.getPort(), Level.INFO);
            this.traza.trace("usuario " + configuracion.getUser(), Level.INFO);
            this.traza.trace("contraseña " + configuracion.getPassword(), Level.INFO);
            this.traza.trace("calidad activo " + configuracion.isCalidadActivo(), Level.INFO);
            this.traza.trace("foliatura activo " + configuracion.isFoliatura(), Level.INFO);
            this.traza.trace("ficha activo " + configuracion.isFicha(), Level.INFO);
            this.traza.trace("fabrica activo " + configuracion.isFabrica(), Level.INFO);
            this.traza.trace("elimina activo " + configuracion.isElimina(), Level.INFO);

            userEncriptado = Base64Coder.encodeString(configuracion.getUser());
            passEncriptado = Base64Coder.encodeString(configuracion.getPassword());

            stored = bd.conectar().prepareCall("{ call p_agregar_configuracion( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) }");

            stored.setString(1, configuracion.getServerName());
            stored.setString(2, configuracion.getDatabaseName());
            stored.setInt(3, configuracion.getPort());
            stored.setString(4, userEncriptado);
            stored.setString(5, passEncriptado);

            if (configuracion.isCalidadActivo()) {
                stored.setString(6, "1");
            } else {
                stored.setString(6, "0");
            }
            if (configuracion.isFoliatura()) {
                stored.setString(7, "1");
            } else {
                stored.setString(7, "0");
            }
            if (configuracion.isFicha()) {
                stored.setString(8, "1");
            } else {
                stored.setString(8, "0");
            }
            if (configuracion.isFabrica()) {
                stored.setString(9, "1");
            } else {
                stored.setString(9, "0");
            }
            if (configuracion.isElimina()) {
                stored.setString(10, "1");
            } else {
                stored.setString(10, "0");
            }
            if (configuracion.isLdap()) {
                stored.setString(11, "1");
            } else {
                stored.setString(11, "0");
            }
            stored.execute();

            bd.commit();
            resp = true;

        } catch (SQLException ex) {
            resp = false;
            this.traza.trace("error actualizando el mantenimiento de la base de datos", Level.ERROR, ex);
        } catch (Exception e) {
            resp = false;
            this.traza.trace("error general en actualizando el mantenimiento de la base de datos", Level.ERROR, e);
        } finally {
            try {
                if (!bd.isClosed()) {
                    bd.desconectar();
                }
            } catch (SQLException ex) {
                this.traza.trace("problemas al desconectar la base de datos", Level.ERROR, ex);
            }
        }
        traza.trace("respuesta al buscar la configuracion "+resp, Level.INFO);
        return resp;
    }
}
