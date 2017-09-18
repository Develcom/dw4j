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
public class Combo  implements Serializable{
    private static final long serialVersionUID = -9015899178987510482L;

    int idCombo;
    int codigoIndice;
    String datoCombo;
    String indice;

    public int getCodigoIndice() {
        return codigoIndice;
    }

    public void setCodigoIndice(int codigoIndice) {
        this.codigoIndice = codigoIndice;
    }

    public String getDatoCombo() {
        return datoCombo;
    }

    public void setDatoCombo(String datoCombo) {
        this.datoCombo = datoCombo;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

}
