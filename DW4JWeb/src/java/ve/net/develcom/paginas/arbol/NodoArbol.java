/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas.arbol;

import java.util.Collection;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

/**
 *
 * @author develcom
 * @param <DatoNodoArbol>
 */
public class NodoArbol<DatoNodoArbol> extends DefaultTreeNode<DatoNodoArbol>{
    
    private static final long serialVersionUID = -1899847671628435150L;
    private boolean open = false;

    public NodoArbol(DatoNodoArbol data, Collection<? extends TreeNode<DatoNodoArbol>> children) {
        super(data, children);
    }

    public NodoArbol(DatoNodoArbol data, Collection<? extends TreeNode<DatoNodoArbol>> children, boolean nullAsMax) {
        super(data, children, nullAsMax);
        setOpen(nullAsMax);
    }

    public NodoArbol(DatoNodoArbol data, TreeNode<DatoNodoArbol>[] children) {
        super(data, children);
    }

    public NodoArbol(DatoNodoArbol data) {
        super(data);
    }

    public NodoArbol(DatoNodoArbol data, boolean nullAsMax) {
        super(data, nullAsMax);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
}
