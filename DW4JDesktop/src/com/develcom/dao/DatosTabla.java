/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.util.List;

/**
 *
 * @author develcom
 */
public class DatosTabla {

    private String categoria;
    private int registro;
    private String clavePrimaria;
    private String claveSec1;
    private String claveSec2;
    private String claveSec3;
    private String datoPrimario;
    private String datoSec1;
    private String datoSec2;
    private String datoSec3;
    private List<OtrosDatos> otrosDatos;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public String getClavePrimaria() {
        return clavePrimaria;
    }

    public void setClavePrimaria(String clavePrimaria) {
        this.clavePrimaria = clavePrimaria;
    }

    public String getClaveSec1() {
        return claveSec1;
    }

    public void setClaveSec1(String claveSec1) {
        this.claveSec1 = claveSec1;
    }

    public String getClaveSec2() {
        return claveSec2;
    }

    public void setClaveSec2(String claveSec2) {
        this.claveSec2 = claveSec2;
    }

    public String getClaveSec3() {
        return claveSec3;
    }

    public void setClaveSec3(String claveSec3) {
        this.claveSec3 = claveSec3;
    }

    public String getDatoPrimario() {
        return datoPrimario;
    }

    public void setDatoPrimario(String datoPrimario) {
        this.datoPrimario = datoPrimario;
    }

    public String getDatoSec1() {
        return datoSec1;
    }

    public void setDatoSec1(String datoSec1) {
        this.datoSec1 = datoSec1;
    }

    public String getDatoSec2() {
        return datoSec2;
    }

    public void setDatoSec2(String datoSec2) {
        this.datoSec2 = datoSec2;
    }

    public String getDatoSec3() {
        return datoSec3;
    }

    public void setDatoSec3(String datoSec3) {
        this.datoSec3 = datoSec3;
    }

    public List<OtrosDatos> getOtrosDatos() {
        return otrosDatos;
}

    public void setOtrosDatos(List<OtrosDatos> otrosDatos) {
        this.otrosDatos = otrosDatos;
    }
}
