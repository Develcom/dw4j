/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

/**
 *
 * @author develcom
 */
public class InterpretaArbol implements TreeitemRenderer<NodoArbol> {

    @Override
    public void render(Treeitem item, NodoArbol data, int index) throws Exception {
        NodoArbol nodo = data;
        DatoNodoArbol dna = (DatoNodoArbol) nodo.getData();
        Treerow dataRow = new Treerow();
        dataRow.setParent(item);
        item.setValue(nodo);
        item.setOpen(nodo.isOpen());

        if (isCategory(dna)) {
            
            CeldaArbol doc = new CeldaArbol(dna.getTipoDocumento());
            doc.setIdDocumento(dna.getIdDocumento());
            doc.setIdInfoDocumento(dna.getIdInfoDocumento());
            doc.setIdSubCategoria(dna.getIdSubCategoria());
            doc.setSubCategoria(dna.getSubCategoria());
            dataRow.appendChild(doc);
            
            CeldaArbol da = new CeldaArbol(dna.getDatosAdicionales());            
            dataRow.appendChild(da);

        }else{
            dataRow.appendChild(new CeldaArbol(dna.getSubCategoria()));
        }

    }

    private boolean isCategory(DatoNodoArbol hojaArbol) {
        return hojaArbol.getSubCategoria() == null;
    }
}
