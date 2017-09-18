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
public class TipoDocumento  implements Serializable{
    private static final long serialVersionUID = -2262850499910203728L;
    private int idTipoDocumento;
    private String tipoDocumento;
    private int idCategoria;
    private int idSubCategoria;
    private String estatus;
    private String vencimiento;
    private String datoAdicional;
    private String ficha;

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdSubCategoria() {
        return idSubCategoria;
    }

    public void setIdSubCategoria(int idSubCategoria) {
        this.idSubCategoria = idSubCategoria;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getDatoAdicional() {
        return datoAdicional;
    }

    public void setDatoAdicional(String datoAdicional) {
        this.datoAdicional = datoAdicional;
    }    

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }
}
