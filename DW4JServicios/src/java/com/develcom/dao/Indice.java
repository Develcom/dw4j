/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

//import com.sun.xml.ws.developer.Serialization;
import java.io.Serializable;

/**
 *
 * @author develcom
 */
public class Indice extends Indices implements Serializable{
    private static final long serialVersionUID = -4367658067100782147L;

    Object valor;
    boolean updateIndices;

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public boolean isUpdateIndices() {
        return updateIndices;
    }

    public void setUpdateIndices(boolean updateIndices) {
        this.updateIndices = updateIndices;
    }

    
    
}
