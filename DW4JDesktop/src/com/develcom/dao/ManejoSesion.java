


package com.develcom.dao;

import com.develcom.autentica.Configuracion;
import com.develcom.autentica.Sesion;
import com.develcom.expediente.Indice;
import com.develcom.expediente.SistemaRRHH;
import java.util.List;
import java.util.Properties;



public class  ManejoSesion {

    private static String login;
    private static String nombres;
    private static List<Sesion> sesion;
    private static SistemaRRHH sistemaRRHH;
    private static Expediente expediente;
    private static List<Indice> indices;
    private static int sizeSearch;
    private static Configuracion configuracion;
    private static Properties sesPropiedades;
    private static boolean buscarTodosExpedientes;

    public static List<Indice> getIndices() {
        return indices;
    }

    public static void setIndices(List<Indice> indices) {
        ManejoSesion.indices = indices;
    }

    public static int getSizeSearch() {
        return sizeSearch;
    }

    public static void setSizeSearch(int sizeSearch) {
        ManejoSesion.sizeSearch = sizeSearch;
    }


    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        ManejoSesion.login = login;
    }

    public static String getNombres() {
        return nombres;
    }

    public static void setNombres(String nombres) {
        ManejoSesion.nombres = nombres;
    }

    public static List<Sesion> getSesion() {
        return sesion;
    }

    public static void setSesion(List<Sesion> sesion) {
        ManejoSesion.sesion = sesion;
    }

    public static SistemaRRHH getSistemaRRHH() {
        return sistemaRRHH;
    }

    public static void setSistemaRRHH(SistemaRRHH sistemaRRHH) {
        ManejoSesion.sistemaRRHH = sistemaRRHH;
    }

    public static Expediente getExpediente() {
        return expediente;
    }

    public static void setExpediente(Expediente expediente) {
        ManejoSesion.expediente = expediente;
    }

    public static Configuracion getConfiguracion() {
        return configuracion;
    }

    public static void setConfiguracion(Configuracion configuracion) {
        ManejoSesion.configuracion = configuracion;
    }

    public static Properties getSesPropiedades() {
        return sesPropiedades;
    }

    public static void setSesPropiedades(Properties sesPropiedades) {
        ManejoSesion.sesPropiedades = sesPropiedades;
    }

    public static boolean isBuscarTodosExpedientes() {
        return buscarTodosExpedientes;
    }

    public static void setBuscarTodosExpedientes(boolean buscarTodosExpedientes) {
        ManejoSesion.buscarTodosExpedientes = buscarTodosExpedientes;
    }
}
