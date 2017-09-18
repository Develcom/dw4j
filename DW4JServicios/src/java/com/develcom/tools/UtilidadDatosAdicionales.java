/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;

import com.develcom.dao.DatoAdicional;
import com.develcom.logs.Traza;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class UtilidadDatosAdicionales {

    private Traza traza = new Traza(UtilidadDatosAdicionales.class);

    public boolean crearCampos(List<DatoAdicional> lsDatosAdicionales, String idExpediente) {

        String campos = "", datos = "";
        int flag = 1, sizeda, idDato = 0;
        boolean exito = false;
        BaseDato bd = new BaseDato();

        try {

            sizeda = lsDatosAdicionales.size();

            for (DatoAdicional da : lsDatosAdicionales) {
                
                idDato = da.getIdDatoAdicional();

                traza.trace("dato adicional " + da.getIndiceDatoAdicional(), Level.INFO);
                traza.trace("bandera " + flag, Level.INFO);
                if (flag == sizeda) {
                    campos = campos + da.getIndiceDatoAdicional();
                } else {
                    campos = campos + da.getIndiceDatoAdicional() + ",";
                }

                if (da.getTipo().equalsIgnoreCase("numero")) {

                    if (flag == sizeda) {
                        datos = datos + da.getValor();
                    } else {
                        datos = datos + da.getValor() + ",";
                    }

                } else if (da.getTipo().equalsIgnoreCase("fecha")) {

                    if (flag == sizeda) {
                        datos = datos + " to_date('" + da.getValor() + "', 'DD/MM/RR')";
                    } else {
                        datos = datos + " to_date('" + da.getValor() + "', 'DD/MM/RR'),";
                    }
                } else {
                    int chr = da.getValor().toString().indexOf("'");

                    if (chr != -1) {
                        String data = da.getValor().toString().replaceAll("'", "\\\\'");

                        if (flag == sizeda) {
                            datos = datos + "E'" + data + "'";
                        } else {
                            datos = datos + "E'" + data + "',";
                        }
                    } else {

                        if (flag == sizeda) {
                            datos = datos + "'" + da.getValor() + "'";
                        } else {
                            datos = datos + "'" + da.getValor() + "',";
                        }
                    }
                }
                flag++;
            }

            if (campos.endsWith(",") && datos.endsWith(",")) {

                String arg[] = campos.split(",");
                String value[] = datos.split(",");
                String tmp = "";

                traza.trace("tamaño arg " + arg.length, Level.INFO);
                traza.trace("tamaño value " + value.length, Level.INFO);

                for (int i = 0; i < arg.length; i++) {

                    traza.trace("argl[" + i + "] " + arg[i], Level.INFO);

                    if (!arg[i].equalsIgnoreCase("")) {
                        if (i == arg.length - 1) {
                            tmp = tmp + arg[i];
                        } else {
                            tmp = tmp + arg[i] + ",";
                        }
                    }
                }

                traza.trace("argumentos temporal " + tmp, Level.INFO);
                campos = tmp;
                tmp = "";
                for (int i = 0; i < value.length; i++) {

                    traza.trace("value[" + i + "] " + value[i], Level.INFO);

                    if (!value[i].equalsIgnoreCase("")) {
                        if (i == arg.length - 1) {
                            tmp = tmp + value[i];
                        } else {
                            tmp = tmp + value[i] + ",";
                        }
                    }
                }
                traza.trace("valores temporal " + tmp, Level.INFO);
                datos = tmp;
                tmp = "";

            }
            
            campos = "insert into VALOR_DATO_ADICIONAL (id_dato_adicional, id_expediente," + campos + ")"
                    + " values ("+idDato+",'"+idExpediente+"'," + datos + ")";
            
            exito = bd.ejecutarSentencia(campos);
            
            traza.trace("exito al guardar los datos adicionales "+exito, Level.INFO);
            
        } catch (SQLException e) {
            traza.trace("problemas al los datos adicionales en base de datos", Level.ERROR, e);
        } catch (Exception e){
            traza.trace("problemas al crear los campos de datos adicionales", Level.ERROR, e);
        }
        
        return exito;
    }

}
