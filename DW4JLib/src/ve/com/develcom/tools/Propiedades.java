/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.tools;

import java.util.Properties;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class Propiedades {

    private Traza traza = new Traza(Propiedades.class);
    private Properties propiedades = new Properties();

    public Propiedades() {
        propiedades = new Configuracion().getPropiedades();
        traza.trace("propiedades cargadas "+propiedades, Level.INFO);
    }

    public Properties getPropiedades() {
        return propiedades;
    }
}
