/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.util;

import java.io.Serializable;

/**
 *
 * @author develcom
 */
public class DatoAdicional implements Serializable{
    private static final long serialVersionUID = 6652097072996225821L;
    
    private Object valor;
    private int idValor;
    private int idDatoAdicional;
    private int idTipoDocumento;
    private String IndiceDatoAdicional;
    private String tipo;
    private int codigo;
    private int numeroDocumento;
    private int version;

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public int getIdValor() {
        return idValor;
    }

    public void setIdValor(int idValor) {
        this.idValor = idValor;
    }

    public int getIdDatoAdicional() {
        return idDatoAdicional;
    }

    public void setIdDatoAdicional(int idDatoAdicional) {
        this.idDatoAdicional = idDatoAdicional;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getIndiceDatoAdicional() {
        return IndiceDatoAdicional;
    }

    public void setIndiceDatoAdicional(String IndiceDatoAdicional) {
        this.IndiceDatoAdicional = IndiceDatoAdicional;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
