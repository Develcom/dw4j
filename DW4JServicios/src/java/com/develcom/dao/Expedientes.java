/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao;

import java.io.Serializable;
import java.util.List;





/**
 *
 * @author develcom
 */
public class Expedientes  implements Serializable{
    private static final long serialVersionUID = 1525022219212073677L;

    private int idEspedientes;
    private String expediente;
    private String mensaje;
    private int idLibreria;
    private int idCategoria;
    private SistemaRRHH rrhh;
    private boolean existe;
    private List<Indice> indices;
    private List<ListaIndice> listaIndices;

    public int getIdEspedientes() {
        return idEspedientes;
    }

    public void setIdEspedientes(int idEspedientes) {
        this.idEspedientes = idEspedientes;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public int getIdLibreria() {
        return idLibreria;
    }

    public void setIdLibreria(int idLibreria) {
        this.idLibreria = idLibreria;
    }

    public List<Indice> getIndices() {
        return indices;
    }

    public void setIndices(List<Indice> indices) {
        this.indices = indices;
    }

    public List<ListaIndice> getListaIndices() {
        return listaIndices;
    }

    public void setListaIndices(List<ListaIndice> listaIndices) {
        this.listaIndices = listaIndices;
    }

    public SistemaRRHH getRrhh() {
        return rrhh;
    }

    public void setRrhh(SistemaRRHH rrhh) {
        this.rrhh = rrhh;
    }
}
