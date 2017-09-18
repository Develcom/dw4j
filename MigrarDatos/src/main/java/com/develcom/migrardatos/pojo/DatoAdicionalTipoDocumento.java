/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.pojo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author familia
 */
public class DatoAdicionalTipoDocumento implements Serializable{

    private static final long serialVersionUID = 7223221238911708014L;
    
    private String tipoDocumento;
    private String indiceAdicional;
    private int codigo;
    private String valor;
    private int numero;
    private int version;
    private String expediente;
    private List<Combo> combo;

    /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the indiceAdicional
     */
    public String getIndiceAdicional() {
        return indiceAdicional;
    }

    /**
     * @param indiceAdicional the indiceAdicional to set
     */
    public void setIndiceAdicional(String indiceAdicional) {
        this.indiceAdicional = indiceAdicional;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @return the expediente
     */
    public String getExpediente() {
        return expediente;
    }

    /**
     * @param expediente the expediente to set
     */
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    /**
     * @return the combo
     */
    public List<Combo> getCombo() {
        return combo;
    }

    /**
     * @param combo the combo to set
     */
    public void setCombo(List<Combo> combo) {
        this.combo = combo;
    }
    
}
