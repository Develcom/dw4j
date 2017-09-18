/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.paginas;

import com.develcom.dao.DatosTabla;
import com.develcom.dao.Expediente;
import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import com.develcom.tool.Herramientas;
import com.develcom.tool.log.Traza;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
@ManagedBean
@RequestScoped
public class Resultado {

    private Traza traza = new Traza(Resultado.class);
    private Expediente expediente;// = ManejoSesion.getExpediente();
    private String idExpediente;
    private List<DatosTabla> datosTablas = new ArrayList<DatosTabla>();
    private int paginas;
    private String clavePrimaria;
    private String claveSec1;
    private String claveSec2;
    private String claveSec3;
    private String login;
    private HttpSession session;
    private Herramientas herramientas = new Herramientas();

    /**
     * Creates a new instance of Resultado
     */
    public Resultado() {
        int tmp, tamCons;
        float mod;

        try {

            session = herramientas.crearSesion();

            if (session != null) {
                tamCons = Integer.parseInt(session.getAttribute("tamañoConsulta").toString());
                login = session.getAttribute("login").toString();
                expediente = (Expediente) session.getAttribute("expediente");

                if (tamCons > 10) {

                    tmp = tamCons / 10;
                    mod = tamCons % 10;

                    if (mod != 0) {
                        paginas = tmp + 1;
                    } else {
                        paginas = tmp;
                    }
                }

                traza.trace("cantidad de paginas " + paginas, Level.INFO);

                llenarDatosTabla();
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
            traza.trace("error al iniciar los resultados", Level.INFO, e);
            try {
                herramientas.getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                traza.trace("error", Level.ERROR, ex);
            }
        }
    }

    private void llenarDatosTabla() {

        traza.trace("armando el data table", Level.INFO);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosTabla dt = null;
        boolean sec1 = true, sec2 = true, sec3 = true, ban = true;
        int cont = 0;
        List<ConsultaDinamica> consultaDinamicas;
        List<Indice> listaIndice;

        try {

            if (session != null) {

                consultaDinamicas = (List<ConsultaDinamica>) session.getAttribute("consultaDinamicas");

                for (ConsultaDinamica cd : consultaDinamicas) {
                    listaIndice = cd.getIndices();

                    for (Indice ind : listaIndice) {

                        try {

                            if (ban) {
                                dt = new DatosTabla();
                                sec1 = true;
                                sec2 = true;
                                sec3 = true;
                            }

                            if (ind.getClave().equalsIgnoreCase("y")) {

                                if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                    XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                    Calendar c = fechaIngreso.toGregorianCalendar();
                                    clavePrimaria = crearEtiqueta(ind.getIndice());
                                    dt.setClavePrimaria(crearEtiqueta(ind.getIndice()));
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                    dt.setDatoPrimario(ind.getValor().toString());

                                } else {

                                    clavePrimaria = crearEtiqueta(ind.getIndice());
                                    dt.setClavePrimaria(crearEtiqueta(ind.getIndice()));
                                    dt.setDatoPrimario(ind.getValor().toString());
                                }

                                ban = false;
                                cont++;
                                traza.trace(dt.getClavePrimaria() + " - " + dt.getDatoPrimario() + " - " + ind.getClave(), Level.INFO);

                            } else if (ind.getClave().equalsIgnoreCase("s")) {

                                if (sec1) {

                                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                        Calendar c = fechaIngreso.toGregorianCalendar();
                                        claveSec1 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec1(crearEtiqueta(ind.getIndice()));
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec1(ind.getValor().toString());

                                    } else {

                                        claveSec1 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec1(crearEtiqueta(ind.getIndice()));
                                        dt.setDatoSec1(ind.getValor().toString());
                                    }

                                    sec1 = false;
                                    cont++;
                                    traza.trace(dt.getClaveSec1() + " - " + dt.getDatoSec1() + " - " + ind.getClave(), Level.INFO);

                                } else if (sec2) {

                                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                        Calendar c = fechaIngreso.toGregorianCalendar();
                                        claveSec2 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec2(ind.getValor().toString());

                                    } else {

                                        claveSec2 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec2(crearEtiqueta(ind.getIndice()));
                                        dt.setDatoSec2(ind.getValor().toString());
                                    }

                                    sec2 = false;
                                    cont++;
                                    traza.trace(dt.getClaveSec2() + " - " + dt.getDatoSec2() + " - " + ind.getClave(), Level.INFO);

                                } else if (sec3) {

                                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                        Calendar c = fechaIngreso.toGregorianCalendar();
                                        claveSec3 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec3(ind.getValor().toString());

                                    } else {

                                        claveSec3 = crearEtiqueta(ind.getIndice());
                                        dt.setClaveSec3(crearEtiqueta(ind.getIndice()));
                                        dt.setDatoSec3(ind.getValor().toString());
                                    }

                                    sec3 = false;
                                    cont++;
                                    traza.trace(dt.getClaveSec3() + " - " + dt.getDatoSec3() + " - " + ind.getClave(), Level.INFO);
                                }
                            }

                            if (cont == 4) {
                                datosTablas.add(dt);
                                ban = true;
                                cont = 0;
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                }
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (Exception e) {
            herramientas.error(e.getMessage());
            traza.trace("error en el resultado de la consulta", Level.ERROR, e);
        }
    }

    private String crearEtiqueta(String arg) {

        arg = arg.replace("_", " ");
        arg = arg.toLowerCase();
        char[] cs = arg.toCharArray();
        char ch = cs[0];
        cs[0] = Character.toUpperCase(ch);
        arg = String.valueOf(cs);
        return arg;
    }

    public String llenarArbol() {

        traza.trace("selecciono el expediente " + idExpediente, Level.INFO);
        expediente.setIdExpediente(idExpediente);
        session.setAttribute("expediente", expediente);
        return "arbol";
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public String getIdExpediente() {
        return idExpediente;
    }

    public void setIdExpediente(String idExpediente) {
        this.idExpediente = idExpediente;
    }

    public List<DatosTabla> getDatosTablas() {
        return datosTablas;
    }

    public void setDatosTablas(List<DatosTabla> datosTablas) {
        this.datosTablas = datosTablas;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getClavePrimaria() {
        return clavePrimaria;
    }

    public void setClavePrimaria(String clavePrimaria) {
        this.clavePrimaria = clavePrimaria;
    }

    public String getClaveSec1() {
        return claveSec1;
    }

    public void setClaveSec1(String claveSec1) {
        this.claveSec1 = claveSec1;
    }

    public String getClaveSec2() {
        return claveSec2;
    }

    public void setClaveSec2(String claveSec2) {
        this.claveSec2 = claveSec2;
    }

    public String getClaveSec3() {
        return claveSec3;
    }

    public void setClaveSec3(String claveSec3) {
        this.claveSec3 = claveSec3;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void regresar() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                herramientas.getExternalContext().redirect("consultar.xhtml");
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (IOException ex) {
            traza.trace("error", Level.ERROR, ex);
        }
    }

    public void salir() {

        try {
            System.runFinalization();
            System.gc();
            if (session != null) {
                herramientas.cerrarSesion();
                herramientas.getExternalContext().redirect("index.xhtml");
            } else {
                herramientas.getExternalContext().redirect("index.xhtml");
            }
        } catch (IOException ex) {
            traza.trace("error al salir", Level.ERROR, ex);
        }
    }
}
