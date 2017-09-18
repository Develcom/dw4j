/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.dao.administrar;

import com.develcom.dao.Categoria;
import com.develcom.dao.Fabrica;
import com.develcom.dao.Indice;
import com.develcom.dao.Libreria;
import com.develcom.dao.Rol;
import com.develcom.dao.SubCategoria;
import com.develcom.dao.TipoDocumento;
import java.io.Serializable;


/**
 *
 * @author develcom
 */
public class Perfil implements Serializable{
    private static final long serialVersionUID = -2674573183068952380L;

    private Rol rol;
    private Libreria libreria;
    private Categoria categoria;
    private SubCategoria subCategoria;
    private TipoDocumento tipoDocumento;
    private Fabrica fabrica;
    private String usuario;
    private int estatus;
    private Indice indice;

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estutus) {
        this.estatus = estutus;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public SubCategoria getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Libreria getLibreria() {
        return libreria;
    }

    public void setLibreria(Libreria libreria) {
        this.libreria = libreria;
    }

    public Fabrica getFabrica() {
        return fabrica;
    }

    public void setFabrica(Fabrica fabrica) {
        this.fabrica = fabrica;
    }

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }
}
