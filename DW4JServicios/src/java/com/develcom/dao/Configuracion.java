/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;

/**
 *
 * @author develcom
 */
public class Configuracion  implements Serializable{
    private static final long serialVersionUID = -4177500139119370004L;

    private boolean calidadActivo;
    private String pathTmp;
    private String fileTif;
    private String fileCode;
    private String logProperties;
    private boolean foliatura;
    private String serverName;
    private String databaseName;
    private int port;
    private String user;
    private String password;
    private boolean ficha;
    private boolean fabrica;
    private boolean elimina;
    private boolean ldap;

    public boolean isCalidadActivo() {
        return calidadActivo;
    }

    public void setCalidadActivo(boolean calidadActivo) {
        this.calidadActivo = calidadActivo;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileTif() {
        return fileTif;
    }

    public void setFileTif(String fileTif) {
        this.fileTif = fileTif;
    }

    public String getLogProperties() {
        return logProperties;
    }

    public void setLogProperties(String logProperties) {
        this.logProperties = logProperties;
    }

    public String getPathTmp() {
        return pathTmp;
    }

    public void setPathTmp(String pathTmp) {
        this.pathTmp = pathTmp;
    }

    public boolean isFoliatura() {
        return foliatura;
    }

    public void setFoliatura(boolean foliatura) {
        this.foliatura = foliatura;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isFicha() {
        return ficha;
    }

    public void setFicha(boolean ficha) {
        this.ficha = ficha;
    }

    public boolean isFabrica() {
        return fabrica;
    }

    public void setFabrica(boolean fabrica) {
        this.fabrica = fabrica;
    }

    public boolean isElimina() {
        return elimina;
    }

    public void setElimina(boolean elimina) {
        this.elimina = elimina;
    }

    public boolean isLdap() {
        return ldap;
    }

    public void setLdap(boolean ldap) {
        this.ldap = ldap;
    }
}
