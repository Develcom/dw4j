/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools.ldap;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.log4j.Level;
import com.develcom.excepcion.DW4JServiciosException;
import com.develcom.logs.Traza;
import com.develcom.tools.Propiedades;
import com.novell.ldap.LDAPAttributeSet;
import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author develcom
 */
public class LDAP {

    Traza traza = new Traza(LDAP.class);


    public UsuarioLDAP getUser(String usuario, String pass) throws LDAPException, DW4JServiciosException, UnsupportedEncodingException {

        UsuarioLDAP user = new UsuarioLDAP();
        Properties prop; //= new Properties();
        InputStream ips;
        String searchFilter;
        String searchBase;
        String ext;
        String dominio;
        String file;
        String buscar;
        LDAPAttribute attrs;
        LDAPEntry entry;
        boolean resp;

//        try {
        traza.trace("buscando a: " + usuario + " en LDAP", Level.INFO);

        prop = Propiedades.cargarPropiedadesWS();

        searchBase = "cn=" + prop.getProperty("user") + "," + prop.getProperty("base");
        traza.trace("searchBase " + searchBase, Level.INFO);

        LDAPConnection lc = new LDAPConnection();

        traza.trace("conecion LDAP " + lc, Level.INFO);

        try {

            if (lc != null) {
                lc.connect(prop.getProperty("server"), Integer.valueOf(prop.getProperty("port")));
                lc.bind(LDAPConnection.LDAP_V3, searchBase, prop.getProperty("password").getBytes("UTF8"));

                int searchScope = LDAPConnection.SCOPE_BASE;
                searchFilter = "Objectclass=*";

                buscar = "uid=" + usuario + "," + prop.getProperty("base");
                traza.trace("buscando en ldap " + buscar, Level.INFO);
                LDAPSearchResults searchResults = lc.search(buscar,
                        searchScope, searchFilter, null, // return all attributes
                        false); // return attrs and values

                traza.trace("LDAPSearchResults " + searchResults, Level.INFO);

                if (searchResults == null) {
                    throw new DW4JServiciosException("usuario " + usuario + " no encontrado en LDAP");
                }

                entry = searchResults.next();
                traza.trace("LDAPEntry "+entry, Level.INFO);


                if (entry != null) {

                    LDAPAttributeSet attributeSet = entry.getAttributeSet();
                    Iterator allAttributes = attributeSet.iterator();

                    while (allAttributes.hasNext()) {
                        LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                        String attributeName = attribute.getName();
                        Enumeration allValues = attribute.getStringValues();
                        
                        if (allValues != null) {
                            while (allValues.hasMoreElements()) {
                                String value = (String) allValues.nextElement();
                                traza.trace("atributo: "+attributeName + " valor: " + value, Level.INFO);
                            }
                        }
                    }



                    if (entry.getAttribute("employeeNumber") != null) {
                        attrs = entry.getAttribute("employeeNumber");
                        user.setCedula(attrs.getStringValue());
                        traza.trace("employeeNumber: " + attrs.getStringValue().toString(), Level.INFO);
                    }



                    if (entry.getAttribute("givenname") != null) {
                        attrs = entry.getAttribute("givenname");
                        user.setNombre(attrs.getStringValue());
                        traza.trace("nombre: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("mail") != null) {
                        attrs = entry.getAttribute("mail");
                        user.setCorreo(attrs.getStringValue());
                        traza.trace("email: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("description") != null) {
                        attrs = entry.getAttribute("description");
                        user.setCargo(attrs.getStringValue());
                        traza.trace("cargo: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("sn") != null) {
                        attrs = entry.getAttribute("sn");
                        user.setApellido(attrs.getStringValue());
                        traza.trace("apellido: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("socialsecuritynumber") != null) {
                        attrs = entry.getAttribute("socialsecuritynumber");
                        user.setCedula(attrs.getStringValue());
                        traza.trace("cedula: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("uid") != null) {
                        attrs = entry.getAttribute("uid");
                        user.setLogin(attrs.getStringValue());
                        traza.trace("login: " + attrs.getStringValue().toString(), Level.INFO);
                    }

                    if (entry.getAttribute("userPassword") != null) {
                        attrs = entry.getAttribute("userPassword");
                        SSHA ssha = SSHA.getInstance();
                        resp = ssha.checkDigest(attrs.getStringValue(), pass);
                        user.setPass(resp);
                        traza.trace("contraseña: " + attrs.getStringValue().toString(), Level.INFO);
                        traza.trace("respuesta contraseña comparada: " + resp, Level.INFO);
                        
                    }

                } else {
                    user = null; 
                    traza.trace("objeto user " + user, Level.INFO);
                    throw new LDAPException("usuarios " + usuario + " no encontrado en LDAP", LDAPException.INVALID_CREDENTIALS, "");
                }

            } else {
                traza.trace("problemas en la coneccion con el servidor LDAP", Level.ERROR, new DW4JServiciosException("problemas en la coneccion con el servidor LDAP"));
                throw new DW4JServiciosException("problemas en la coneccion con el servidor LDAP");
            }



        } catch (LDAPException el) {
            traza.trace("poblemas en la busqueda del usuario " + usuario, Level.ERROR, el);
            throw new DW4JServiciosException("<html>Usuario " + usuario + " <br/>no encontrado en LDAP<html/>");
        } catch (DW4JServiciosException dw) {
            traza.trace("poblemas en la busqueda del usuario " + usuario, Level.ERROR, dw);
            throw new DW4JServiciosException("<html>Usuario " + usuario + " <br/>no encontrado en LDAP<html/>");
        } catch (Exception e) {
            traza.trace("poblemas en la busqueda del usuario " + usuario, Level.ERROR, e);
            throw new DW4JServiciosException("Usuario " + usuario + " no encontrado en LDAP");
        }



        return user;

    }
    
    
//    private UsuarioLDAP buscarDatos(String usuario) throws AuthenticationException, Exception{
//        
//        UsuarioLDAP user=null;// = new UsuarioLDAP();
//        Properties prop;
//        String searchFilter;
//        String searchBase;
//        LDAPEntry entry;
//        NamingEnumeration answer;
//        LdapContext ctx;
//        
//        prop = Propiedades.cargar();
//        
//        ctx = getContext(prop.getProperty("server"), prop.getProperty("port"), prop.getProperty("user"), prop.getProperty("password"), prop.getProperty("dominio"));
//        
//        SearchControls searchCtls = new SearchControls();
//        searchCtls.setReturningAttributes(null);
//        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//        
//        searchFilter = "sAMAccountName=" + usuario;//"(&(" + usuario + "))";
//        searchBase = prop.getProperty("base");
//        
//        answer = ctx.search(searchBase, searchFilter, searchCtls);
//        
//        if (answer.hasMoreElements()) {
//                
//            SearchResult sr = (SearchResult) answer.next();
//            Attributes attrs = sr.getAttributes();
//            user = new UsuarioLDAP();
//            
//            user.setPass(true);
//            
//            
//            if (attrs.get("sAMAccountName") != null){
//                user.setLogin((String)attrs.get("sAMAccountName").get());
//                traza.trace("login "+attrs.get("sAMAccountName").get(), Level.INFO);
//            }
//
//            if (attrs.get("givenName") != null){
//                user.setNombre((String)attrs.get("givenName").get());
//                traza.trace("nombre "+attrs.get("givenName").get(), Level.INFO);
//            }
//
//            if (attrs.get("sn") != null){
//                user.setApellido((String)attrs.get("sn").get());
//                traza.trace("apellido "+attrs.get("sn").get(), Level.INFO);
//            }
//
//            if (attrs.get("mail") != null) {
//                user.setCorreo((String)attrs.get("mail").get());
//                traza.trace("correo "+attrs.get("mail").get(), Level.INFO);
//            }
//
//            if (attrs.get("displayName") != null) {
//                user.setNombreCompleto((String)attrs.get("displayName").get());
//                traza.trace("nombre completo "+attrs.get("displayName").get(), Level.INFO);
//            }
//            
//        } else {
//            
//            user = null; 
//            traza.trace("objeto user " + user, Level.INFO);
//        }
//        
//        return user;
//    }
//    
//    
//    public UsuarioLDAP getUsuario(String usuario, String pass){
//        
//        UsuarioLDAP user = null;//new UsuarioLDAP();
//        Properties prop;
//        LdapContext ctx;
//        
//        
//        
//        traza.trace("buscando a: " + usuario + " en LDAP", Level.INFO);
//        try {
//            prop = Propiedades.cargar();
//            ctx = getContext(prop.getProperty("server"), prop.getProperty("port"), usuario, pass, prop.getProperty("dominio"));
//            user = buscarDatos(usuario);
//            
//            if(user==null){
//                throw new Exception("Usuario sin datos");
//            }
//        
//        } catch (Exception ex) {
//            traza.trace("Error en el usuario "+usuario, Level.ERROR, ex);
//        }
//        
//        
//        return user;
//        
//    }
//    
//    
//    private LdapContext getContext(String server, String port, String user, String password, String dominio) throws Exception{
//
//        
//
//        traza.trace("servidor: "+server, Level.INFO);
//        traza.trace("puerto: "+port, Level.INFO);
//        traza.trace("usuario: "+user, Level.INFO);
//        traza.trace("dominio: "+dominio, Level.INFO);
//
//        Properties env = System.getProperties();
//        
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port);
//        env.put(Context.SECURITY_AUTHENTICATION, "simple");
//        env.put(Context.SECURITY_PRINCIPAL, user + "@" + dominio);
//        env.put(Context.SECURITY_CREDENTIALS, password);
//        
////        env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
////        env.put("java.naming.provider.url", "ldap://" + server + ":" + port+"/dc=develcom,dc=com,cd=ve");
////        env.put("java.naming.security.authentication", "simple");
////        env.put("java.naming.security.principal", user + "@" + domain);
////        env.put("java.naming.security.credentials", password);
//
//        return new InitialLdapContext(env, null);
//    }
}
