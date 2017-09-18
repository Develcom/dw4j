/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

import org.zkoss.zul.Treecell;


/**
 *
 * @author develcom
 */
public class CeldaArbol extends Treecell {

    private static final long serialVersionUID = -4116211713632405610L;
    
    private int idInfoDocumento;
    private int idDocumento;
    private int idSubCategoria;
    private String subCategoria;

    public CeldaArbol() {
    }

    public CeldaArbol(String label) {
        super(label);
    }

    public CeldaArbol(String label, String src) {
        super(label, src);
    }

    public int getIdInfoDocumento() {
        return idInfoDocumento;
    }

    public void setIdInfoDocumento(int idInfoDocumento) {
        this.idInfoDocumento = idInfoDocumento;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdSubCategoria() {
        return idSubCategoria;
    }

    public void setIdSubCategoria(int idSubCategoria) {
        this.idSubCategoria = idSubCategoria;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }

}
