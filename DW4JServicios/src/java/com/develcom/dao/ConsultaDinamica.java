/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author develcom
 */
public class ConsultaDinamica  implements Serializable{
    private static final long serialVersionUID = -5361077461522583417L;

    private List<Indice> indices = new ArrayList<>();
    private boolean existe;

    public List<Indice> getIndices() {
        return indices;
    }

    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

}
