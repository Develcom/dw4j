/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;

/**
 *
 * @author develcom
 */
public class ModeloArbol extends DefaultTreeModel<DatoNodoArbol>{
    private static final long serialVersionUID = 9203892892987605283L;

    public ModeloArbol(TreeNode<DatoNodoArbol> root) {
        super(root);
    }
    
}
