/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

import com.develcom.tools.trazas.Traza;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class ModeloTabla extends DefaultTableModel {

    private static final long serialVersionUID = -7926326760615757047L;

    Traza traza = new Traza(ModeloTabla.class);
    private String nombre;
    private int id;

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void eliminarFilas() {
        int rows = this.getRowCount(), i = 0;

        traza.trace("filas " + rows, Level.INFO);

        while (rows > 0) {
            this.removeRow(i);
            rows = this.getRowCount();
            traza.trace("filas " + rows, Level.INFO);

        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
