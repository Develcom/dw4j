/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

//import com.develcom.dao.Expediente;
import com.develcom.expediente.Expedientes;
import com.develcom.autentica.Usuario;
import javax.swing.JTextField;
import com.develcom.tools.trazas.Traza;
import java.util.List;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Autocompletar {

    /**
     * Escribe trazas en el log
     */
    Traza traza = new Traza(Autocompletar.class);

    public void addTexto(JTextField textField, String newDato) {

        String datoBuscado = "";
        int nroActual = textField.getText().length();

        traza.trace("nuevo dato " + newDato, Level.INFO);
        datoBuscado = newDato.substring(nroActual, newDato.length());

        if (newDato.isEmpty() || datoBuscado.isEmpty()) {
            return;
        }

        try {
            traza.trace("dato buscado " + datoBuscado, Level.INFO);
            textField.getDocument().insertString(textField.getCaretPosition(), datoBuscado, null);
        } catch (Exception e) {
            traza.trace("error al insertar el dato", Level.ERROR, e);
        }

        textField.select(nroActual, textField.getText().length());
    }

    public String getTextoListaUsuario(String datoBuscar, List<Usuario> usuarios) {

        int nroPosicion = getPosicionLista(datoBuscar, usuarios, null);

        if (nroPosicion == -1) {
            traza.trace("dato a buscar " + datoBuscar, Level.INFO);
            return datoBuscar;
        }
        //return veDatos[nroPosicion];

        return usuarios.get(nroPosicion).getIdUsuario();
    }

    public String getTextoListaExpediente(String datoBuscar, List<Expedientes> expedientes) {

        int nroPosicion = getPosicionLista(datoBuscar, null, expedientes);

        if (nroPosicion == -1) {
            traza.trace("dato a buscar " + datoBuscar, Level.INFO);
            return datoBuscar;
        }
        //return veDatos[nroPosicion];

        return expedientes.get(nroPosicion).getExpediente();
    }

    public int getPosicionLista(String datoBuscar, List<Usuario> usuarios, List<Expedientes> expedientes) {

        try {

            if (usuarios != null) {

                for (int i = 0; i < usuarios.size(); i++) {

                    if (datoBuscar.equalsIgnoreCase(usuarios.get(i).getIdUsuario().substring(0, datoBuscar.length()))) {
                        traza.trace("posicion " + i, Level.INFO);
                        return i;
                    }

                }
            }

            if (expedientes != null) {

                for (int i = 0; i < expedientes.size(); i++) {

                    if (datoBuscar.equalsIgnoreCase(expedientes.get(i).getExpediente().substring(0, datoBuscar.length()))) {
                        traza.trace("posicion " + i, Level.INFO);
                        return i;
                    }

                }
            }
        } catch (Exception e) {
            traza.trace("error al obtener la posicion del vector", Level.ERROR, e);
        }
        return -1;
    }
}
