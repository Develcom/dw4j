/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools.ldap;

import com.develcom.logs.Traza;
import com.develcom.tools.Propiedades;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class ActiveDirectory {
    
    private Traza traza = new Traza(ActiveDirectory.class);
    private Properties prop;
    
    private LdapContext conectar(String server, String port, String user, String password, String domain) throws NamingException{
        
        Properties env = System.getProperties();        
        InitialLdapContext context;
        
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
        context = new InitialLdapContext(env, null);
        
        return context;
    }
    
    
    public UsuarioLDAP comprobarUsuario(String usuario, String contrasenia){
        boolean exito=false;
        UsuarioLDAP user = null;
        LdapContext context;
        
        try {
            prop = Propiedades.cargarPropiedadesWS();
            context=conectar(prop.getProperty("server"), prop.getProperty("port"), usuario, contrasenia, prop.getProperty("dominio"));
            traza.trace("LdapContext comprobarUsuario "+context, Level.INFO);
            user = buscarDatosUsuario(usuario);
            exito=true;
        } catch (NamingException ex) {
            traza.trace("Problemas con el usuario", Level.ERROR, ex);
        }
        
        return user;
    }
    
    
    public UsuarioLDAP buscarDatosUsuario(String usuario) throws NamingException{
        UsuarioLDAP user = null;// = new UsuarioLDAP();
        LdapContext ctx;
        NamingEnumeration answer;
        SearchResult sr;
        Attributes attrs;
        SearchControls searchCtls = new SearchControls();
        
//        try {
            prop = Propiedades.cargarPropiedadesWS();
            ctx = conectar(prop.getProperty("server"), prop.getProperty("port"), 
                    prop.getProperty("user"), prop.getProperty("password"), prop.getProperty("dominio"));
            
            traza.trace("LdapContext buscarDatosUsuario "+ctx, Level.INFO);
            
            searchCtls.setReturningAttributes(null);
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            
            usuario = "sAMAccountName=" + usuario;
            //usuario = "(&(sn="+ usuario +")"; //"(&(sn=W*)(l=Criteria*))"
            //usuario = "(&(" + usuario + "))";
            
            answer = ctx.search(prop.getProperty("base"), usuario, searchCtls);
            
            traza.trace("respuesta en la busqueda en ldap "+answer, Level.INFO);
            
            if (answer.hasMoreElements()) {
                user = new UsuarioLDAP();
                sr = (SearchResult)answer.next();
                attrs = sr.getAttributes();
                
//                NamingEnumeration<Attribute> ne = (NamingEnumeration<Attribute>) attrs.getAll();
//                while(ne.hasMore()){
//                    Attribute a = ne.next();
//                    traza.trace("id: "+a.getID()+" "+a.get().toString(), Level.INFO);
//                }
                
//                traza.trace("SearchResult "+sr, Level.INFO);
//                traza.trace("Attributes "+attrs, Level.INFO);
                
                user.setPass(true);
                
                if(attrs.get("displayname") != null){
                    user.setNombreCompleto((String)attrs.get("displayname").get());
                    traza.trace("nombre completo "+attrs.get("displayname").get(), Level.INFO);
                }
                
                if(attrs.get("givenName") != null){
                    user.setApellido((String)attrs.get("givenName").get());
                    traza.trace("nombre "+attrs.get("givenName").get(), Level.INFO);
                }
                
                if(attrs.get("sn") != null){
                    user.setNombre((String)attrs.get("sn").get());
                    traza.trace("apellido "+attrs.get("sn").get(), Level.INFO);
                }
                
//                if(attrs.get("givenName") != null){
//                    user.setNombre((String)attrs.get("givenName").get());
//                    traza.trace("nombre "+attrs.get("givenName").get(), Level.INFO);
//                }
//                
//                if(attrs.get("sn") != null){
//                    user.setApellido((String)attrs.get("sn").get());
//                    traza.trace("apellido "+attrs.get("sn").get(), Level.INFO);
//                }
                
                if(attrs.get("mail") != null){
                    user.setCorreo((String)attrs.get("mail").get());
                    traza.trace("correo "+attrs.get("mail").get(), Level.INFO);
                }
                
                if(attrs.get("telephoneNumber") != null){
                    user.setTelefono((String)attrs.get("telephoneNumber").get());
                    traza.trace("telefono "+attrs.get("telephoneNumber").get(), Level.INFO);
                }
                
                if(attrs.get("description") != null){
                    user.setCargo((String)attrs.get("description").get());
                    traza.trace("cargo "+attrs.get("description").get(), Level.INFO);
                }
                
                if(attrs.get("physicaldeliveryofficename") != null) {
                    user.setUbicacion((String)attrs.get("physicaldeliveryofficename").get());
                    traza.trace("ubicacion "+attrs.get("physicaldeliveryofficename").get(), Level.INFO);
                }
                
//                if(attrs.get("socialsecuritynumber") != null) {
//                    user.setCedula((String)attrs.get("socialsecuritynumber").get());
//                    traza.trace("cedula socialsecuritynumber "+attrs.get("socialsecuritynumber").get(), Level.INFO);
//                }

                if(attrs.get("sAMAccountName") != null) {
                    user.setLogin((String)attrs.get("sAMAccountName").get());
                    traza.trace("login "+attrs.get("sAMAccountName").get(), Level.INFO);
                }
                
                if(attrs.get("postalCode") != null) {
                    user.setCedula((String)attrs.get("postalCode").get());
                    traza.trace("cedula "+attrs.get("postalCode").get(), Level.INFO);
                }
//
//                if(attrs.get("nickname") != null) {
//                    user.setLogin((String)attrs.get("nickname").get());
//                    traza.trace("login "+attrs.get("nickname").get(), Level.INFO);
//                }
            }
            
//        } catch (NamingException ex) {
//            traza.trace("problemas al conectarse al directorio activo", Level.ERROR, ex);
//        }
        
        return user;
    }
}
