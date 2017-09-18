/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

/**
 *
 * @author develcom
 * @param <T>
 */
public class DatoNodoArbol <T> {
    
    private static final long serialVersionUID = 626852549041965429L;
    
    private int idInfoDocumento;
    private int idDocumento;
    private int idSubCategoria;
    private String tipoDocumento;
    private String datosAdicionales;
    private String subCategoria;

    public DatoNodoArbol(String tipoDocumento, String datosAdicionales) {
        this.tipoDocumento = tipoDocumento;
        this.datosAdicionales = datosAdicionales;
    }

    public DatoNodoArbol(String subCategoria) {
        this.subCategoria = subCategoria;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDatosAdicionales() {
        return datosAdicionales;
    }

    public void setDatosAdicionales(String datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(String subCategoria) {
        this.subCategoria = subCategoria;
    }
}
