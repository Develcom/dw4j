/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.pojo;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author familia
 */
@Component
public class Indice implements Serializable {

    private static final long serialVersionUID = -3096665171625866579L;

    private String indice;
    private String tipo;
    private int codigo;
    private String clave;
    private String valor;
    private int idIndice;
    private List<Combo> combo;

    /**
     * @return the indice
     */
    public String getIndice() {
        return indice;
    }

    /**
     * @param indice the indice to set
     */
    public void setIndice(String indice) {
        this.indice = indice;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
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
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
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
     * @return the idIndice
     */
    public int getIdIndice() {
        return idIndice;
    }

    /**
     * @param idIndice the idIndice to set
     */
    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
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
