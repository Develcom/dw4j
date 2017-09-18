/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas.arbol.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author develcom
 */
//public class NodoTipoDocumento implements TreeNode, Serializable {
public class NodoTipoDocumento extends DefaultTreeNode implements Serializable {

    public static final String DEFAULT_TYPE = "default";
    private static final long serialVersionUID = -4663766885601008997L;
    private String type;
    private Object data;
    private List<TreeNode> children;
    private TreeNode parent;
    private boolean expanded;
    private boolean selected;
    private boolean selectable = true;
    private int idInfoDocumento;

    public NodoTipoDocumento() {
        this.type = DEFAULT_TYPE;
        children = new ArrayList<TreeNode>();
    }

    public NodoTipoDocumento(Object data, int idInfoDocumento, TreeNode parent) {
        this.type = DEFAULT_TYPE;
        this.data = data;
        this.idInfoDocumento=idInfoDocumento;
        children = new ArrayList<TreeNode>();
        this.parent = parent;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
    }

    public NodoTipoDocumento(Object data, TreeNode parent) {
        this.type = DEFAULT_TYPE;
        this.data = data;
        children = new ArrayList<TreeNode>();
        this.parent = parent;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
    }

    public NodoTipoDocumento(String type, Object data, TreeNode parent) {
        this.type = type;
        this.data = data;
        children = new ArrayList<TreeNode>();
        this.parent = parent;
        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        if (this.parent != null) {
            this.parent.getChildren().remove(this);
        }

        this.parent = parent;

        if (this.parent != null) {
            this.parent.getChildren().add(this);
        }
    }

    @Override
    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setSelected(boolean value) {
        this.selected = value;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public boolean isLeaf() {
        if (children == null) {
            return true;
        }

        return children.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        NodoTipoDocumento other = (NodoTipoDocumento) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        if (data != null) {
            return data.toString();
        } else {
            return super.toString();
        }
    }

    public int getIdInfoDocumento() {
        return idInfoDocumento;
    }

    public void setIdInfoDocumento(int idInfoDocumento) {
        this.idInfoDocumento = idInfoDocumento;
    }
}
