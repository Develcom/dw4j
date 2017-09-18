/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.expediente.ConsultaDinamica;
import com.develcom.expediente.Indice;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import ve.net.develcom.dao.DatosTabla;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;
import ve.net.develcom.tool.barra.IBarraEstado;

/**
 *
 * @author develcom
 */
public class Resultado extends SelectorComposer<Component> {

    @Wire
    private Listbox resultado;

    private static final long serialVersionUID = 4679389834585865080L;
    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(Resultado.class);
    private Session session;
    private Expediente expediente;

    private List<DatosTabla> datosTablas = new ArrayList<>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {

        try {

            session = herramientas.crearSesion();

            if (session != null) {

                expediente = (Expediente) session.getAttribute("expediente");

                llenarDatosTabla();
                construirTabla();

            }

        } catch (Exception e) {
            traza.trace("error al iniciar el objeto consulta", Level.INFO, e);
            herramientas.navegarPagina("index.zul");
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
                                    dt.setClavePrimaria(ind.getIndice());
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                    dt.setDatoPrimario(ind.getValor().toString());

                                } else {

                                    dt.setClavePrimaria(ind.getIndice());
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
                                        dt.setClaveSec1(ind.getIndice());
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec1(ind.getValor().toString());

                                    } else {

                                        dt.setClaveSec1(ind.getIndice());
                                        dt.setDatoSec1(ind.getValor().toString());
                                    }

                                    sec1 = false;
                                    cont++;
                                    traza.trace(dt.getClaveSec1() + " - " + dt.getDatoSec1() + " - " + ind.getClave(), Level.INFO);

                                } else if (sec2) {

                                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                        Calendar c = fechaIngreso.toGregorianCalendar();
                                        dt.setClaveSec2(ind.getIndice());
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec2(ind.getValor().toString());

                                    } else {

                                        dt.setClaveSec2(ind.getIndice());
                                        dt.setDatoSec2(ind.getValor().toString());
                                    }

                                    sec2 = false;
                                    cont++;
                                    traza.trace(dt.getClaveSec2() + " - " + dt.getDatoSec2() + " - " + ind.getClave(), Level.INFO);

                                } else if (sec3) {

                                    if (ind.getTipo().equalsIgnoreCase("FECHA")) {

//                                        XMLGregorianCalendar fechaIngreso = (XMLGregorianCalendar) ind.getValor();
//                                        Calendar c = fechaIngreso.toGregorianCalendar();
                                        dt.setClaveSec3(ind.getIndice());
//                                    dt.setDatoPrimario(sdf.format(c.getTime()));
                                        dt.setDatoSec3(ind.getValor().toString());

                                    } else {

                                        dt.setClaveSec3(ind.getIndice());
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
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception e) {
            herramientas.error("Error generando el resultado de la consulta", e);
            traza.trace("error generando el resultado de la consulta", Level.ERROR, e);
        }
    }

    private void construirTabla() {

        Listhead titulos;
        Listitem datos;
        boolean cabezera = false;

        try {

            for (DatosTabla data : datosTablas) {

                if (!cabezera) {

                    titulos = new Listhead();
                    titulos.setValue(data.getClavePrimaria());
                    titulos.appendChild(new Listheader(data.getClavePrimaria()));
                    titulos.appendChild(new Listheader(data.getClaveSec1()));
                    titulos.appendChild(new Listheader(data.getClaveSec2()));
                    titulos.appendChild(new Listheader(data.getClaveSec3()));
                    titulos.setParent(resultado);

                    cabezera = true;

                }

                datos = new Listitem();
                datos.setValue(data.getDatoPrimario());
                datos.appendChild(new Listcell(data.getDatoPrimario()));
                datos.appendChild(new Listcell(data.getDatoSec1()));
                datos.appendChild(new Listcell(data.getDatoSec2()));
                datos.appendChild(new Listcell(data.getDatoSec3()));
                datos.setParent(resultado);

            }
        } catch (Exception e) {
            traza.trace("Problemas generando la tabla de resultados", Level.ERROR, e);
            herramientas.error("Problemas generando la tabla de resultados", e);
        }
    }

    @Listen("onSelect = #resultado")
    public void llenarArbol() {

        Listitem seleccion;
        String idExpediente;
        //IBarraEstado barraEstado;

        seleccion = resultado.getSelectedItem();
        idExpediente = seleccion.getValue();

        //barraEstado = herramientas.getBarraEstado();
        //barraEstado.setStatus("Selecciono el Expediente: " + idExpediente);

        traza.trace("selecciono el expediente " + idExpediente, Level.INFO);

        expediente.setIdExpediente(idExpediente);
        session.setAttribute("expediente", expediente);
        herramientas.navegarPagina("arbol.zul");
    }

    @Listen("onClick = #exit")
    public void salir() {

        try {
            System.runFinalization();
            System.gc();
            if (session != null) {
                herramientas.cerrarSesion();
                herramientas.navegarPagina("index.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al salir", Level.ERROR, ex);
        }
    }

    @Listen("onClick = #regresa")
    public void regresar() {

        System.runFinalization();
        System.gc();
        try {
            if (session != null) {
                herramientas.navegarPagina("consulta.zul");
            } else {
                herramientas.navegarPagina("index.zul");
            }
        } catch (Exception ex) {
            traza.trace("error al regresar", Level.ERROR, ex);
        }
    }
}
