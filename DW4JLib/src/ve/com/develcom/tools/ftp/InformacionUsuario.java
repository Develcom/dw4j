/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.tools.ftp;


import com.jcraft.jsch.UserInfo;
import java.util.Properties;
import ve.com.develcom.tools.Propiedades;


/**
 *
 * @author develcom
 */
public class InformacionUsuario implements UserInfo{

    private String pass;
    private String contrasena;
    private Properties propedades = new Propiedades().getPropiedades();

    public InformacionUsuario()    {
        contrasena = propedades.getProperty("passwordFTP");
    }

    public boolean promptYesNo(String str) {
        return true;
    }

    public void showMessage(String message){}

    public boolean promptPassword(String message) {
        this.pass = this.contrasena;
        return true;
    }

    public String getPassword() {
        return this.pass;
    }

    public boolean promptPassphrase(String mensaje) {
        return false;
    }

    public String getPassphrase() {
        return null;
    }
}
