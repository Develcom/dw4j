

package com.develcom.dao.administrar;

import com.develcom.dao.Categoria;
import com.develcom.dao.Indice;
import com.develcom.dao.Libreria;
import com.develcom.dao.SubCategoria;
import java.io.Serializable;

/**
 *
 * @author develcom
 */
public class Indices implements Serializable{
    private static final long serialVersionUID = 881904566910279554L;


    private Libreria libreria;
    private Categoria categoria;
    private SubCategoria subCategoria;
    private Indice indice;
    private com.develcom.dao.Indices argumentos;

    public com.develcom.dao.Indices getArgumentos() {
        return argumentos;
    }

    public void setArgumentos(com.develcom.dao.Indices argumentos) {
        this.argumentos = argumentos;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }

    public Libreria getLibreria() {
        return libreria;
    }

    public void setLibreria(Libreria libreria) {
        this.libreria = libreria;
    }

    public SubCategoria getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(SubCategoria subCategoria) {
        this.subCategoria = subCategoria;
    }

}
