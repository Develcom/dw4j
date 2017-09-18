/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.tool;

import com.develcom.dao.Expediente;
import com.develcom.documento.InfoDocumento;
import com.develcom.tool.log.Traza;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Level;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author develcom
 */
public class CreaArchivoJNLP {

    private Traza traza = new Traza(CreaArchivoJNLP.class);

    private HttpSession session;
    private Herramientas herramientas = new Herramientas();
    private String appPath = System.getProperties().getProperty("user.dir");
    private Properties propiedades = null;
    private String catalinaBase = System.getProperties().getProperty("catalina.base");
    //private String appPathDescargar = System.getProperties().getProperty("user.dir");
    //private final String ruta = appPath+"../webapps/ROOT/jnlp";


    public CreaArchivoJNLP() {
        int len;
        try{
            
//            Properties env = System.getProperties();
//            
//            Enumeration pro = env.propertyNames();
//            Enumeration prop = env.elements();
//            
//            while (pro.hasMoreElements() && prop.hasMoreElements()) {                
//                traza.trace("propiedad "+pro.nextElement().toString()+" valor "+prop.nextElement().toString(), Level.INFO);
//            }
            
//            traza.trace("appPath "+appPath, Level.INFO);
//            len = appPath.length();
            //appPath=appPath.substring(0, appPath.indexOf("bin"));
//            appPath=appPath.substring(0, (appPath.length()-3));
//            traza.trace("appPath despues del subString "+appPath, Level.INFO);
        }catch(Exception e){
            traza.trace("error en la ruta ", Level.ERROR, e);
        }
    }



    //public void crearArchivoJNLP(int idInfoDoc, int idDoc, int version, int numDoc, int idCat, int idSubCat, String idExpediente){
    public boolean crearArchivoJNLP(InfoDocumento infoDocumento, Expediente expediente, String login){

        boolean resp = false;
        

        session = herramientas.crearSesion();
        if(session!=null){
            propiedades = (Properties) session.getAttribute("propiedades");
        }

        //appPathCrear=appPathCrear.substring(0, appPathCrear.indexOf("bin"));
        String ruta = catalinaBase+propiedades.getProperty("rutaJNLP");
        traza.trace("ruta para crear el jnlp "+ruta, Level.INFO);
        File fileJnlp = new File(ruta);
        String ipServidorWeb = propiedades.getProperty("servidorWEB");
        String puertoSrvWeb = propiedades.getProperty("puertoWeb");
//        String ipServidorWeb = ManejoSesion.getPropedades().getProperty("servidorWEB");// = new InitialContext().lookup("java:comp/env/confiApplets").toString();
//        String puertoSrvWeb = ManejoSesion.getPropedades().getProperty("puertoWeb");
        String servidor = "http://"+ipServidorWeb+":"+puertoSrvWeb+"/jnlp/";
        String servidor1 = "http://"+ipServidorWeb+":"+puertoSrvWeb;

        String srvWS = propiedades.getProperty("srvWS");
        String puertoWS = propiedades.getProperty("puertoWS");
//        String srvWS = ManejoSesion.getPropedades().getProperty("srvWS");
//        String puertoWS = ManejoSesion.getPropedades().getProperty("puertoWS")

        try {

            if(fileJnlp.exists()){
                traza.trace("eliminando el archivo "+fileJnlp.getName()+" - "+fileJnlp.delete(), Level.INFO);
            }
            

            Element jnlp = new Element("jnlp");
            jnlp.setAttribute("spec", "1.0+");
            jnlp.setAttribute("codebase", servidor);//"http://localhost:8080/DW4JWeb/jnlp");
//            jnlp.setAttribute("href", servidor+"/verDocumento.jnlp");
            jnlp.setAttribute("href", "verDocumento.jnlp");
            
            
            
            Element information = new Element("information");
            Element title = new Element("title").setText("Ver Documento Digitalizado");
            Element vendor = new Element("vendor").setText("Develcom");
            Element description = new Element("description").setText("Ver Documento Digitalizado");
            Element homepage = new Element("homepage");
            homepage.setAttribute("href", servidor1+"/faces/index.xhtml");
            information.addContent(title);
            information.addContent(vendor);
            information.addContent(description);
            information.addContent(homepage);
            
            Element update = new Element("update");
            update.setAttribute("check", "always");

            Element security = new Element("security");
            Element all_permissions = new Element("all-permissions");
            security.addContent(all_permissions);
            Element resources = new Element("resources");

//            Element j6se = new Element("j6se");
            Element j7se = new Element("j2se");
            j7se.setAttribute("version", "1.7+");
            j7se.setAttribute("java-vm-args", "-Xmx512M");
            //j6se.setAttribute("href", "http://java.sun.com/products/autodl/j2se");

            Element jar = new Element("jar");
            jar.setAttribute("href", "DW4JViewDoc.jar");
            jar.setAttribute("main", "true");

            Element DW4JLib = new Element("jar");
            DW4JLib.setAttribute("href", "/jnlp/lib/DW4JLib.jar");

            Element libImage = new Element("jar");
            libImage.setAttribute("href", "/jnlp/lib/jai_imageio.jar");

            Element libJcalendar = new Element("jar");
            libJcalendar.setAttribute("href", "/jnlp/lib/jcalendar-1.4.jar");

            Element log4j = new Element("jar");
            log4j.setAttribute("href", "/jnlp/lib/log4j-1.2.16.jar");

            Element pdf = new Element("jar");
            pdf.setAttribute("href", "/jnlp/lib/PDFRenderer.jar");

//            Element mmsc = new Element("nativelib");
//            mmsc.setAttribute("href", "/jnlp/lib/mmsc.jar");

            resources.addContent(j7se);
            resources.addContent(jar);
            resources.addContent(DW4JLib);
            resources.addContent(libImage);
            resources.addContent(libJcalendar);
            resources.addContent(log4j);
            resources.addContent(pdf);
//            resources.addContent(mmsc);


            Element application_desc = new Element("application-desc");
            application_desc.setAttribute("main-class", "com.develcom.Main");
            Element argument0 = new Element("argument").setText(String.valueOf(infoDocumento.getIdInfoDocumento()));
            Element argument1 = new Element("argument").setText(String.valueOf(infoDocumento.getIdDocumento()));
            Element argument2 = new Element("argument").setText(String.valueOf(infoDocumento.getVersion()));
            Element argument3 = new Element("argument").setText(String.valueOf(infoDocumento.getNumeroDocumento()));
            Element argument4 = new Element("argument").setText(String.valueOf(expediente.getIdCategoria()));
            Element argument5 = new Element("argument").setText(String.valueOf(expediente.getIdSubCategoria()));
            Element argument6 = new Element("argument").setText(String.valueOf(expediente.getIdExpediente()));
            Element argument7 = new Element("argument").setText("\""+expediente.getLibreria()+"\"");
            Element argument8 = new Element("argument").setText("\""+expediente.getCategoria()+"\"");
            Element argument9 = new Element("argument").setText("\""+expediente.getSubCategoria()+"\"");
            Element argument10 = new Element("argument").setText("\""+infoDocumento.getTipoDocumento()+"\"");
            Element argument11 = new Element("argument").setText("\""+login+"\"");
            Element argument12 = new Element("argument").setText(infoDocumento.getFormato());
            Element argument13 = new Element("argument").setText(srvWS);
            Element argument14 = new Element("argument").setText(puertoWS);

            application_desc.addContent(argument0);
            application_desc.addContent(argument1);
            application_desc.addContent(argument2);
            application_desc.addContent(argument3);
            application_desc.addContent(argument4);
            application_desc.addContent(argument5);
            application_desc.addContent(argument6);
            application_desc.addContent(argument7);
            application_desc.addContent(argument8);
            application_desc.addContent(argument9);
            application_desc.addContent(argument10);
            application_desc.addContent(argument11);
            application_desc.addContent(argument12);
            application_desc.addContent(argument13);
            application_desc.addContent(argument14);

            jnlp.addContent(information);
            jnlp.addContent(update);
            jnlp.addContent(security);
            jnlp.addContent(resources);
            jnlp.addContent(application_desc);
            Document docJNLP = new Document(jnlp);


            XMLOutputter xlmfileJNLP = new XMLOutputter(Format.getPrettyFormat());
            FileOutputStream fileOutJNLP = new FileOutputStream(fileJnlp);
            xlmfileJNLP.output(docJNLP, fileOutJNLP);
            //fileJNLP.output(docJNLP,System.out);
            fileOutJNLP.flush();
            fileOutJNLP.close();
            
            if(fileJnlp.exists()){
                traza.trace("se genero un nuevo jnlp", Level.INFO);
                resp=true;
            }

        } catch (FileNotFoundException ex) {
            traza.trace("error con el archivo", Level.ERROR, ex);
        } catch (IOException ex) {
            traza.trace("error al crear el archivo", Level.ERROR, ex);
        }
        traza.trace("repsuesta creando el jnlp "+resp, Level.INFO);
        return resp;
    }


    public void descargarJNLP()  {
        
        //String appPath = System.getProperties().getProperty("user.dir");
        //appPathDescargar=appPathDescargar.substring(0, appPathDescargar.indexOf("bin"));
        //File jnlp = new File( appPath+"webapps/ROOT/jnlp/verDocumento.jnlp");
        String ruta = catalinaBase+propiedades.getProperty("rutaJNLP");
        traza.trace("ruta del jnlp "+ruta, Level.INFO);
        File jnlp = new File(ruta);

        traza.trace("archivo jnlp "+jnlp.getName(), Level.INFO);
        ServletOutputStream sos;
//        File doc = new File(toolsTif.getArchivoTif());
//        copia(toolsTif.getArchivoTif(), toolsTif.getDocTiff());
        //response.setContentType("text/html;charset=UTF-8");
        //response.setContentType("image/tiff");
        
        FileInputStream fis;// = new FileInputStream(doc);
        byte[] bytes = new byte[1024];
        int read;
        

        try {
            //if (!ctx.getResponseComplete()) {
            if (!herramientas.getFacesContext().getResponseComplete()) {

                traza.trace("abriendo el jnlp", Level.INFO);
                //traza.trace("FacesContext "+ctx, Level.INFO);

                fis = new FileInputStream(jnlp);
                String fileName = jnlp.getName();
                String contentType = "application/x-java-jnlp-file";
                //String contentType = "image/tiff";
                //String contentType = "application/vnd.ms-excel";
                //String contentType = "application/pdf";
                
                HttpServletResponse response = (HttpServletResponse) herramientas.getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                //response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");

                traza.trace("fileName "+fileName, Level.INFO);
                traza.trace("Content Type "+response.getContentType(), Level.INFO);
                traza.trace("response "+response, Level.INFO);

                sos = response.getOutputStream();

                traza.trace("ServletOutputStream "+sos.toString(), Level.INFO);

                while ((read = fis.read(bytes)) != -1) {
                    traza.trace("escribiendo el jnlp "+read, Level.INFO);
                    sos.write(bytes, 0, read);
                }
                sos.flush();
                sos.close();
                herramientas.getFacesContext().responseComplete();

            }
        } catch (IOException ex) {
            traza.trace("error al descargar el jnlp", Level.ERROR, ex);
        }
        //return "";
    }



}
