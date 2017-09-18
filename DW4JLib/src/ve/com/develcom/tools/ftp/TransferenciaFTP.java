/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.tools.ftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Level;
import ve.com.develcom.tools.Propiedades;
import ve.com.develcom.tools.Traza;


/**
 *
 * @author develcom
 */
public class TransferenciaFTP {

    //private Traza trazas = new Traza(TransferenciaFTP.class);;
    private Properties propedades = new Propiedades().getPropiedades();
    private String usuario;
    private String servidor;

    public TransferenciaFTP() {
        usuario = propedades.getProperty("userFTPr");
        servidor = propedades.getProperty("ipServidorFTP");

    }



    public boolean transferirArchivo(String entrada, String salida) {
        boolean resp = false;
        try {

            JSch jsch = new JSch();

            Session session = jsch.getSession(this.usuario, this.servidor, 22);

            UserInfo ui = new InfoUser();
            session.setUserInfo(ui);

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            long time=System.currentTimeMillis();
            
            channelSftp.put(entrada, salida);

            time=System.currentTimeMillis()-time;
//            trazas.trace("tiempo usado para transferir el archivo es de: "+time+" milisegundos", Level.INFO);
//            trazas.trace("Tiempo de conversión total de decodificación fue " + (time * 0.001) / 60 + " minutos", Level.INFO);
//            trazas.trace("Se transfirio el archivo: " + new File(entrada).getName() + " y  se coloco en: " + salida, Level.INFO);

            session.disconnect();
            channel.disconnect();
            resp = true;
        }
        catch (Exception e) {
//            this.trazas.trace("Error durante la transferencia de archivos " + e.getMessage(), Level.ERROR);
        }

        return resp;
    }


    public void crearDirectorio(String directorio) {
        try {
            JSch jsch = new JSch();
            String raiz=propedades.getProperty("dirRaizFTP");
            Session session = jsch.getSession(this.usuario, this.servidor, 22);

            UserInfo ui = new InformacionUsuario();
            session.setUserInfo(ui);
            session.connect();

            Channel channel = session.openChannel("exec");

            ((ChannelExec)channel).setCommand("mkdir "+raiz+directorio);

            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                if (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);

                    if (i >= 0) {
//                        this.trazas.trace("respuesta al crear el directorio: " + channel.getExitStatus(), Level.INFO);
                    }
                }
                if (channel.isClosed()) {
//                    this.trazas.trace("respuesta al crear el directorio: " + channel.getExitStatus(), Level.INFO);
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }


    public void cambiarAtributo(String archivo, String atributo) {
        try {
            JSch jsch = new JSch();
            String raiz=propedades.getProperty("dirRaizFTP");
            Session session = jsch.getSession(this.usuario, this.servidor, 22);

            UserInfo ui = new InformacionUsuario();
            session.setUserInfo(ui);
            session.connect();

            Channel channel = session.openChannel("exec");

            ((ChannelExec)channel).setCommand("chmod "+atributo+" "+archivo);

            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                if (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);

                    if (i >= 0) {
//                        this.trazas.trace("respuesta al cambiar el atributo: " + channel.getExitStatus(), Level.INFO);
                    }
                }
                if (channel.isClosed()) {
//                    this.trazas.trace("respuesta al cambiar el atributo: " + channel.getExitStatus(), Level.INFO);
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }




    public void ejecutaScript(String script) {
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(this.usuario, this.servidor, 22);

            UserInfo ui = new InformacionUsuario();
            session.setUserInfo(ui);
            session.connect();

            Channel channel = session.openChannel("exec");

            ((ChannelExec)channel).setCommand(script);

            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                if (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);

                    if (i >= 0) {
//                        this.trazas.trace("respuesta al ejecutar el shell-script: " + channel.getExitStatus(), Level.INFO);
                    }
                }
                if (channel.isClosed()) {
//                    this.trazas.trace("respuesta al ejecutar el shell-script: " + channel.getExitStatus(), Level.INFO);
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    class InfoUser implements UserInfo{

        private String pass;
        private String contrasena;
        //private Properties propedades = new Propiedades().buscarProperties("DW4JLib.properties");

        public InfoUser()    {
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


//    public static void main(String[] a){
//      //new TransferenciaFTP().transferirArchivo("/home/develcom/NetBeansProjects/MCTI/DW4JDesktop/temp/comprimido.zip", "/files/documentos/repositorio");
//      new TransferenciaFTP().crearDirectorio("prueba");
//  }


}