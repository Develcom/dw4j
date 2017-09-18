/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.main;

import com.develcom.dao.ManejoSesion;
import com.develcom.gui.Login;
import com.develcom.tools.trazas.Traza;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Level;

/**
 * Inicia la aplicación gestor documental
 *
 * @author develcom
 */
public class DW4JDesktop {

    private static Traza traza = new Traza(DW4JDesktop.class);

    /**
     * Inicia la aplicación gestor documental
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String appPath = System.getProperties().getProperty("user.dir");
        String properties = "/lib/dw4japp.properties";// ManejoSesion.getConfiguracion().getLogProperties();//ManejoSesion.getPropedades().getProperty("log");
        String fileProperties = appPath+properties;
        String logo = appPath+"/lib/";
        Properties prop = new Properties();
        InputStream ips;
        ImageIcon icon;
        File file;
        Login login = new Login();
        
        traza.trace("iniciando la aplicacion Gestor Documental bienvenido", Level.INFO);

//            Properties env = System.getProperties();

        traza.trace("maximo memoria " + Runtime.getRuntime().maxMemory(), Level.INFO);
        traza.trace("memoria disponible " + Runtime.getRuntime().freeMemory(), Level.INFO);
        traza.trace("total memoria " + Runtime.getRuntime().totalMemory(), Level.INFO);
        
        try {
            
            ips=new FileInputStream(fileProperties);
            prop.load(ips);
            
            ManejoSesion.setSesPropiedades(prop);
            logo = logo+prop.getProperty("logo");
            
            file = new File(logo);
            
            if(!file.exists()){
                JOptionPane.showMessageDialog(new JFrame(), "Imagen del Logo de la Empresa no encontrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                
            }
            
            icon = new ImageIcon(logo);
            login.getPanelLogo().setImagen(icon.getImage());
            login.mostrar();   
            
        } catch (FileNotFoundException ex) {
            traza.trace("archivo de propiedades no encontrado", Level.ERROR, ex);
            JOptionPane.showMessageDialog(new JFrame(), "Archivo de propiedades no encontrado\n" + ex.getMessage() + "\nComuniquese con el administrador del sistema", "Error", JOptionPane.ERROR_MESSAGE);
            
        } catch (IOException ex) {
            traza.trace("problemas de lectura del archivo de propiedades", Level.ERROR, ex);
            JOptionPane.showMessageDialog(new JFrame(), "Problemas de lectura del archivo de propiedades\n" + ex.getMessage() + "\nComuniquese con el administrador del sistema", "Error", JOptionPane.ERROR_MESSAGE);
            
        }

            
    }
}
