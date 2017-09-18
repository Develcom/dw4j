/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author develcom
 */
public class SistemaRRHH  implements Serializable{
    private static final long serialVersionUID = -8238757680895420985L;


    private String cedula;
    private String nombre;
    private String apellido;
    private String ubicacionAdministrativa;
    private String ubicacionFisica;
    private String cargo;
    private String profesion;
    private String tipoPersonal;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private boolean encontrado;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getTipoPersonal() {
        return tipoPersonal;
    }

    public void setTipoPersonal(String tipoPersonal) {
        this.tipoPersonal = tipoPersonal;
    }

    public String getUbicacionAdministrativa() {
        return ubicacionAdministrativa;
    }

    public void setUbicacionAdministrativa(String ubicacionAdministrativa) {
        this.ubicacionAdministrativa = ubicacionAdministrativa;
    }

    public String getUbicacionFisica() {
        return ubicacionFisica;
    }

    public void setUbicacionFisica(String ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }
}
