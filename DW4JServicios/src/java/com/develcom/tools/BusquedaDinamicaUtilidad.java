/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.tools;

import com.develcom.administracion.AdministracionBusquedas;
import com.develcom.dao.Categoria;
import com.develcom.dao.Indice;
import com.develcom.dao.SubCategoria;
import com.develcom.dao.TipoDocumento;
import com.develcom.logs.Traza;
import java.util.List;
import org.apache.log4j.Level;

/**
 *
 * @author develcom
 */
public class BusquedaDinamicaUtilidad {

    private Traza traza = new Traza(BusquedaDinamicaUtilidad.class);
    private String INDICE_PRIMARIO;
    private String tabla;
    private String subCategoria = "";
    private String categoria;
    private String libreria;
    int idCategoria = 0;

//    public BusquedaDinamicaUtilidad(int estatusDocumento) {
//        //this.estatusDocumento = estatusDocumento;
//        traza.trace("estatus documento " + estatusDocumento, Level.INFO);
//    }
    /**
     * arma los campos de la comsulta dinamica
     *
     * @param listaIndice Lista de indices
     * @return Una cadena con todos los campos
     */
    public String crearCampos(List<Indice> listaIndice) {
        String campos = "";
        int flag = 1, sizeIndice = 0;
        String campoDesde, tmp = "", campoHasta;

        sizeIndice = listaIndice.size();

        try {

            for (Indice indi : listaIndice) {
                if (indi.getIndice().equalsIgnoreCase("ID_CATEGORIA")) {
                    //if(indi.getValor().toString().equalsIgnoreCase("")){
                    if (indi.getValor() != null) {
                        traza.trace("para buscar la subCategoria el idCategoria es " + indi.getValor(), Level.INFO);
                        List<SubCategoria> scs = new AdministracionBusquedas().buscarSubCategorias("", Integer.valueOf(indi.getValor().toString()), 0);
                        subCategoria = scs.get(0).getSubCategoria();
                        traza.trace("subCategoria " + subCategoria, Level.INFO);

                        traza.trace("para buscar la categoria el idCategoria es " + indi.getValor(), Level.INFO);
                        List<Categoria> cs = new AdministracionBusquedas().buscarCategorias("", 0, Integer.valueOf(indi.getValor().toString()));
                        categoria = cs.get(0).getCategoria();
                        traza.trace("categoria " + categoria, Level.INFO);
                    }
                }
            }

            for (Indice indi : listaIndice) {

                traza.trace("tamaño de los indices " + sizeIndice, Level.INFO);
                traza.trace("flag " + flag, Level.INFO);

                if (flag == sizeIndice) {
                    if (indi.getTipo().equalsIgnoreCase("fecha")) {

                        String lblDesde = indi.getIndice();
                        int desde = lblDesde.indexOf("DESDE");

                        String lblHasta = indi.getIndice();
                        int hasta = lblHasta.indexOf("HASTA");

                        if (desde != -1) {
                            campoDesde = lblDesde.substring(0, desde - 1);

                            if (!campoDesde.equalsIgnoreCase(tmp)) {

                                tmp = lblDesde.substring(0, desde - 1);

                                try {
                                    if (indi.getClave().equalsIgnoreCase("y")) {

                                        INDICE_PRIMARIO = campoDesde.toUpperCase();
                                        campos = " DISTINCT t." + INDICE_PRIMARIO;

                                    } else {

                                        campos = campos + "t." + campoDesde.toUpperCase();
                                    }
                                } catch (NullPointerException e) {
                                    campos = campos + "t." + campoDesde.toUpperCase();
                                }

                                //banDesde=false;
                            }
                        } else if (hasta != -1) {
                            campoHasta = lblHasta.substring(0, hasta - 1);
                            if (!campoHasta.equalsIgnoreCase(tmp)) {
                                tmp = lblHasta.substring(0, desde - 1);

                                try {
                                    if (indi.getClave().equalsIgnoreCase("y")) {

                                        INDICE_PRIMARIO = "" + campoHasta.toUpperCase();
                                        campos = " DISTINCT t." + INDICE_PRIMARIO;

                                    } else {

                                        campos = campos + "t." + campoHasta.toUpperCase();
                                    }
                                } catch (NullPointerException e) {
                                    campos = campos + "t." + campoHasta.toUpperCase();
                                }

                                //banHasta=false;
                            }
                        } else {
                            try {
                                if (indi.getClave().equalsIgnoreCase("y")) {

                                    INDICE_PRIMARIO = "" + indi.getIndice().toUpperCase();
                                    campos = " DISTINCT t." + INDICE_PRIMARIO;

                                } else {

                                    campos = campos + "t." + indi.getIndice().toUpperCase();
                                }
                            } catch (NullPointerException e) {
                                campos = campos + "t." + indi.getIndice().toUpperCase();
                            }
                        }

                    } else {
                        try {
                            if (indi.getClave().equalsIgnoreCase("y")) {

                                INDICE_PRIMARIO = "" + indi.getIndice();
                                campos = " DISTINCT t." + INDICE_PRIMARIO;

                            } else {

                                campos = campos + "t." + indi.getIndice().toUpperCase();
                            }
                        } catch (NullPointerException e) {
                            campos = campos + "t." + indi.getIndice().toUpperCase();
                        }
                    }

                } else if (indi.getTipo().equalsIgnoreCase("fecha")) {

                    //traza.trace("fecha "+indi.getArgumento(), Level.INFO);
                    String lblDesde = indi.getIndice();
                    int desde = lblDesde.indexOf("DESDE");

                    String lblHasta = indi.getIndice();
                    int hasta = lblHasta.indexOf("HASTA");

                    if (desde != -1) {
                        campoDesde = lblDesde.substring(0, desde - 1);

                        if (!campoDesde.equalsIgnoreCase(tmp)) {

                            tmp = lblDesde.substring(0, desde - 1);

                            //traza.trace("campo desde "+campoDesde, Level.INFO);
                            try {
                                if (indi.getClave().equalsIgnoreCase("y")) {

                                    INDICE_PRIMARIO = "" + campoDesde.toUpperCase() + ",";
                                    campos = " DISTINCT t." + INDICE_PRIMARIO;

                                } else {

                                    campos = campos + "t." + campoDesde.toUpperCase() + ",";
                                }
                            } catch (NullPointerException e) {
                                campos = campos + "t." + campoDesde.toUpperCase() + ",";
                            }

                            //banDesde=false;
                        }
                    } else if (hasta != -1) {
                        campoHasta = lblHasta.substring(0, hasta - 1);

                        if (!campoHasta.equalsIgnoreCase(tmp)) {
                            tmp = lblHasta.substring(0, desde - 1);

                            traza.trace("campo hasta " + campoHasta, Level.INFO);

                            try {
                                if (indi.getClave().equalsIgnoreCase("y")) {

                                    INDICE_PRIMARIO = "" + campoHasta.toUpperCase() + ",";
                                    campos = " DISTINCT t." + INDICE_PRIMARIO;

                                } else {

                                    campos = campos + "t." + campoHasta.toUpperCase() + ",";
                                }
                            } catch (NullPointerException e) {
                                campos = campos + "t." + campoHasta.toUpperCase() + ",";
                            }

                            //banHasta=false;
                        }
                    } else {
                        try {
                            if (indi.getClave().equalsIgnoreCase("y")) {

                                INDICE_PRIMARIO = "" + indi.getIndice().toUpperCase() + ",";
                                campos = " DISTINCT t." + INDICE_PRIMARIO;

                            } else {

                                campos = campos + "t." + indi.getIndice().toUpperCase() + ",";
                            }
                        } catch (NullPointerException e) {
                            campos = campos + "t." + indi.getIndice().toUpperCase() + ",";
                        }
                    }

                } else {

                    try {
                        if (indi.getClave().equalsIgnoreCase("y")) {

                            traza.trace("indice principal " + indi.getIndice(), Level.INFO);

                            INDICE_PRIMARIO = indi.getIndice() + ",";
                            campos = " DISTINCT t." + INDICE_PRIMARIO;

                        } else {

                            campos = campos + "t." + indi.getIndice().toUpperCase() + ",";
                        }
                    } catch (NullPointerException e) {
                        campos = campos + "t." + indi.getIndice().toUpperCase() + ",";
                    }
                }

                flag++;
            }
        } catch (Exception e) {
            traza.trace("error al armar los capos de la consulta dinamica", Level.ERROR, e);
        }
        return campos;
    }

    public String crearFiltrosIndices(List<Indice> listaIndice, List<Categoria> listaCat,
            List<SubCategoria> listaSubCat, List<TipoDocumento> listaTipoDoc, boolean generico) {

        String data;

        traza.trace("creando los filtros", Level.INFO);
        traza.trace("tamaño de los indices " + listaIndice.size(), Level.INFO);
        traza.trace("tamaño de las categorias " + listaCat.size(), Level.INFO);
        traza.trace("tamaño de las subcategorias " + listaSubCat.size(), Level.INFO);
        traza.trace("tamaño de los tipos de documentos " + listaTipoDoc.size(), Level.INFO);

        if (generico) {
            data = filtrosExpedienteGenerico(listaIndice);
        } else {
            data = filtrosExpediente(listaIndice);
        }

        if (!listaCat.isEmpty()) {

            traza.trace("filtros subCategorias antes " + data, Level.INFO);

            if (!data.equalsIgnoreCase("WHERE ")) {
                data = data + " AND " + crearFiltroCategoria(listaCat);
            } else {
                data = data + crearFiltroCategoria(listaCat);
            }
            traza.trace("filtros categorias despues " + data, Level.INFO);
        }

        if (!listaSubCat.isEmpty()) {

            traza.trace("filtros subCategorias antes " + data, Level.INFO);

            if (!data.equalsIgnoreCase("WHERE ")) {
                data = data + " AND " + crearFiltroSubCategoria(listaSubCat);
            } else {
                data = data + crearFiltroSubCategoria(listaSubCat);
            }
            traza.trace("filtros subCategorias despues " + data, Level.INFO);
        }

        if (!listaTipoDoc.isEmpty()) {

            traza.trace("filtros tipo documento antes " + data, Level.INFO);

            if (!data.equalsIgnoreCase("WHERE ")) {
                data = data + " AND " + crearFiltroTipoDocumento(listaTipoDoc);
            } else {
                data = data + crearFiltroTipoDocumento(listaTipoDoc);
            }

            traza.trace("filtros tipo documento despues " + data, Level.INFO);
        }

        return data;
    }

    private String filtrosExpediente(List<Indice> listaIndice) {

        String data = "WHERE ";
        String dataDesde = "", dataHasta = "";
        int bandera = 0, banderaFechDesde = 0, banderaFechHasta = 0;

        for (Indice ind : listaIndice) {

            if (idCategoria == 0) {
                idCategoria = ind.getIdCategoria();
            }

            if ((ind.getValor() != null) && (!ind.getValor().equals(""))) {

                traza.trace("idIndice " + ind.getIdIndice(), Level.INFO);
                traza.trace("indice " + ind.getIndice(), Level.INFO);
                traza.trace("valor " + ind.getValor(), Level.INFO);
                traza.trace("tipo " + ind.getTipo(), Level.INFO);
                traza.trace("clave " + ind.getClave().trim(), Level.INFO);

                if (ind.getTipo().equalsIgnoreCase("NUMERO")) {
                    if (bandera == 0) {
//                        data = data + "TRIM(TO_CHAR(e.valor, '9999999999')) ~* '" + ind.getValor().toString().trim() + "'";
                        data = data + "e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar) and i.id_indice=" + ind.getIdIndice() + " ";
                        traza.trace("entro tipo numero (data)  valor " + data + " bandera " + bandera, Level.INFO);
                        bandera++;
                    } else {
//                        data = data + "and TRIM(TO_CHAR(e.valor, '9999999999')) ~* '" + ind.getValor().toString().trim() + "'";
                        data = data + "or e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar) and i.id_indice=" + ind.getIdIndice() + " ";
                        traza.trace("entro tipo numero (data)  valor " + data + " bandera " + bandera, Level.INFO);
                    }
                } else if ((ind.getTipo().equalsIgnoreCase("TEXTO"))
                        || (ind.getTipo().equalsIgnoreCase("AREA"))
                        || (ind.getTipo().equalsIgnoreCase("COMBO"))) {

                    if (bandera == 0) {
                        data = data + "e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar) and i.id_indice=" + ind.getIdIndice() + " ";
                        traza.trace("entro tipo texto area combo (data)  valor " + data + " bandera " + bandera, Level.INFO);
                        bandera++;
                    } else {
                        data = data + " or e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar) and i.id_indice=" + ind.getIdIndice() + " ";
                        traza.trace("entro tipo texto area combo (data)  valor " + data + " bandera " + bandera, Level.INFO);
                    }

                } else if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                    String lblDesde = ind.getIndice();
                    int desde = lblDesde.indexOf("DESDE");

                    String lblHasta = ind.getIndice();
                    int hasta = lblHasta.indexOf("HASTA");

                    if (desde != -1) {

                        traza.trace("armando fecha desde", Level.INFO);

                        if (banderaFechDesde == 0) {
                            dataDesde = dataDesde + "e.fecha_indice >= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataDesde) valor " + dataDesde + " bandera fecha desde " + banderaFechDesde, Level.INFO);
                            banderaFechDesde++;
                        } else {
                            dataDesde = dataDesde + " or e.fecha_indice >= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataDesde) valor " + dataDesde + " bandera fecha desde " + banderaFechDesde, Level.INFO);
                        }
                    } else if (hasta != -1) {

                        traza.trace("armando fecha hasta", Level.INFO);

                        if (banderaFechHasta == 0) {
                            dataHasta = dataHasta + "CAST e.fecha_indice <= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataHasta) valor " + dataHasta + " bandera fecha hasta " + banderaFechHasta, Level.INFO);
                            banderaFechHasta++;
                        } else {
                            dataHasta = dataHasta + " and e.fecha_indice <= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataHasta) valor " + dataHasta + " bandera fecha hasta " + banderaFechHasta, Level.INFO);
                        }
                    }
                }
            }

        }

        if ((!dataDesde.equalsIgnoreCase(""))
                || (!dataHasta.equalsIgnoreCase(""))) {

            traza.trace("filtros fechas antes " + data, Level.INFO);

            if (data.equalsIgnoreCase("WHERE ")) {
                data = data + filtroFechas(dataDesde, dataHasta);
            } else {
                data = data + " AND " + filtroFechas(dataDesde, dataHasta);
            }

            traza.trace("filtros fechas despues " + data, Level.INFO);
        }
        return data;
    }

    private String filtrosExpedienteGenerico(List<Indice> listaIndice) {

        String data = "WHERE ";
        String dataDesde = "", dataHasta = "";
        int bandera = 0, banderaFechDesde = 0, banderaFechHasta = 0;

        for (Indice ind : listaIndice) {

            if (idCategoria == 0) {
                idCategoria = ind.getIdCategoria();
            }

            if ((ind.getValor() != null) && (!ind.getValor().equals(""))) {

                traza.trace("idIndice " + ind.getIdIndice(), Level.INFO);
                traza.trace("indice " + ind.getIndice(), Level.INFO);
                traza.trace("valor " + ind.getValor(), Level.INFO);
                traza.trace("tipo " + ind.getTipo(), Level.INFO);
                traza.trace("clave " + ind.getClave().trim(), Level.INFO);

                if (ind.getTipo().equalsIgnoreCase("NUMERO")) {
                    if (bandera == 0) {
//                        data = data + "TRIM(TO_CHAR(e.valor, '9999999999')) ~* '" + ind.getValor().toString().trim() + "'";
                        data = data + "e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar)  ";
                        traza.trace("entro tipo numero (data)  valor " + data + " bandera " + bandera, Level.INFO);
                        bandera++;
                    } else {
//                        data = data + "and TRIM(TO_CHAR(e.valor, '9999999999')) ~* '" + ind.getValor().toString().trim() + "'";
                        data = data + "or e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar)  ";
                        traza.trace("entro tipo numero (data)  valor " + data + " bandera " + bandera, Level.INFO);
                    }
                } else if ((ind.getTipo().equalsIgnoreCase("TEXTO"))
                        || (ind.getTipo().equalsIgnoreCase("AREA"))
                        || (ind.getTipo().equalsIgnoreCase("COMBO"))) {

                    if (bandera == 0) {
                        data = data + "e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar)  ";
                        traza.trace("entro tipo texto area combo (data)  valor " + data + " bandera " + bandera, Level.INFO);
                        bandera++;
                    } else {
                        data = data + " or e.valor ~* cast ('" + ind.getValor().toString().trim() + "' as varchar)  ";
                        traza.trace("entro tipo texto area combo (data)  valor " + data + " bandera " + bandera, Level.INFO);
                    }

                } else if (ind.getTipo().equalsIgnoreCase("FECHA")) {

                    String lblDesde = ind.getIndice();
                    int desde = lblDesde.indexOf("DESDE");

                    String lblHasta = ind.getIndice();
                    int hasta = lblHasta.indexOf("HASTA");

                    if (desde != -1) {

                        traza.trace("armando fecha desde", Level.INFO);

                        if (banderaFechDesde == 0) {
                            dataDesde = dataDesde + "e.fecha_indice >= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataDesde) valor " + dataDesde + " bandera fecha desde " + banderaFechDesde, Level.INFO);
                            banderaFechDesde++;
                        } else {
                            dataDesde = dataDesde + " or e.fecha_indice >= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataDesde) valor " + dataDesde + " bandera fecha desde " + banderaFechDesde, Level.INFO);
                        }
                    } else if (hasta != -1) {

                        traza.trace("armando fecha hasta", Level.INFO);

                        if (banderaFechHasta == 0) {
                            dataHasta = dataHasta + "CAST e.fecha_indice <= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataHasta) valor " + dataHasta + " bandera fecha hasta " + banderaFechHasta, Level.INFO);
                            banderaFechHasta++;
                        } else {
                            dataHasta = dataHasta + " and e.fecha_indice <= CAST ('" + ind.getValor().toString().trim() + "' AS DATE)";
                            traza.trace("entro tipo fecha (dataHasta) valor " + dataHasta + " bandera fecha hasta " + banderaFechHasta, Level.INFO);
                        }
                    }
                }
            }

        }

        if ((!dataDesde.equalsIgnoreCase(""))
                || (!dataHasta.equalsIgnoreCase(""))) {

            traza.trace("filtros fechas antes " + data, Level.INFO);

            if (data.equalsIgnoreCase("WHERE ")) {
                data = data + filtroFechas(dataDesde, dataHasta);
            } else {
                data = data + " AND " + filtroFechas(dataDesde, dataHasta);
            }

            traza.trace("filtros fechas despues " + data, Level.INFO);
        }
        return data;
    }

    private String filtroFechas(String dataDesde, String dataHasta) {

        String fecha = "";

        traza.trace("fecha desde " + dataDesde, Level.INFO);
        traza.trace("fecha hasta " + dataHasta, Level.INFO);

        if ((!dataDesde.equalsIgnoreCase(""))
                && (!dataHasta.equalsIgnoreCase(""))) {

            traza.trace("contruyendo rango de fechas (desde-hasta)", Level.INFO);

            int desde = dataDesde.indexOf(">=");
            int hasta = dataHasta.indexOf("<=");

            String campo = dataDesde.substring(0, desde);

            String datoDesde = dataDesde.substring(desde);
            String datoHasta = dataHasta.substring(hasta);

            traza.trace("dato fecha desde " + datoDesde, Level.INFO);
            traza.trace("dato fecha hasta " + datoHasta, Level.INFO);

            //fecha = campo+"WHERE BETWEEN CAST ("+datoDesde+" AS DATE) AND CAST ("+datoHasta+" AS DATE)";
            fecha = campo + datoDesde + " AND " + campo + datoHasta;

//            if(estatusDocumento==0){
//                fecha = "d.FECHA_DIGITALIZACION"+datoDesde+" AND d.FECHA_DIGITALIZACION"+datoHasta;
//            }else{
//                fecha = campo+datoDesde+" AND "+campo+datoHasta;
//            }
        } else if ((!dataHasta.equalsIgnoreCase(""))
                && (dataDesde.equalsIgnoreCase(""))) {
            //fecha = "WHERE CAST ("+dataDesde+" AS DATE)";

            traza.trace("fecha hasta " + dataHasta, Level.INFO);
            fecha = dataHasta;

//            if(estatusDocumento==0){
//                fecha = "d.FECHA_DIGITALIZACION"+dataDesde;
//            }else{
//                fecha = dataDesde;
//            }
        } else if ((!dataDesde.equalsIgnoreCase(""))
                && (dataHasta.equalsIgnoreCase(""))) {

            traza.trace("fecha desde " + dataDesde, Level.INFO);

            //fecha = "WHERE CAST ("+dataHasta+" AS DATE)";
            fecha = dataDesde;

//            if(estatusDocumento==0){
//                fecha = "d.FECHA_DIGITALIZACION"+dataHasta;
//            }else{
//                fecha = dataDesde;
//            }
        }
        traza.trace("resultado filtro fechas " + fecha, Level.INFO);

        return fecha;

    }

    private String crearFiltroSubCategoria(List<SubCategoria> listaSubCat) {

        String idSubCateg = "", subCat = "";

        if (listaSubCat.size() == 1) {

            idSubCateg = String.valueOf(listaSubCat.get(0).getIdSubCategoria());

        } else {
            idSubCateg = String.valueOf(listaSubCat.get(0).getIdSubCategoria());
            for (int i = 1; i < listaSubCat.size(); i++) {
                SubCategoria sc = listaSubCat.get(i);
                idSubCateg = idSubCateg + "," + String.valueOf(sc.getIdSubCategoria());
            }
        }

        subCat = "s.ID_SUBCATEGORIA in (" + idSubCateg + ")";

        traza.trace("datos de la subCategoria " + subCat, Level.INFO);
        return subCat;
    }

    private String crearFiltroCategoria(List<Categoria> listaCat) {

        String idCateg = "", cat = "";

        if (listaCat.size() == 1) {

            idCateg = String.valueOf(listaCat.get(0).getIdCategoria());

        } else {
            idCateg = String.valueOf(listaCat.get(0).getIdCategoria());
            for (int i = 1; i < listaCat.size(); i++) {
                Categoria sc = listaCat.get(i);
                idCateg = idCateg + "," + String.valueOf(sc.getIdCategoria());
            }
        }

        cat = "c.ID_CATEGORIA in (" + idCateg + ")";

        traza.trace("datos de la categoria " + cat, Level.INFO);
        return cat;
    }

    private String crearFiltroTipoDocumento(List<TipoDocumento> listaTipoDoc) {

        String idTipoDocs = "", tipoDoc = "";

        if (listaTipoDoc.size() == 1) {

            idTipoDocs = String.valueOf(listaTipoDoc.get(0).getIdTipoDocumento());

        } else {
            idTipoDocs = String.valueOf(listaTipoDoc.get(0).getIdTipoDocumento());
            for (int i = 1; i < listaTipoDoc.size(); i++) {
                TipoDocumento td = listaTipoDoc.get(i);
                idTipoDocs = idTipoDocs + "," + String.valueOf(td.getIdTipoDocumento());
            }
        }
        tipoDoc = "t.ID_DOCUMENTO in (" + idTipoDocs + ")";
        traza.trace("datos de los tipod de documentos " + tipoDoc, Level.INFO);
        return tipoDoc;
    }

    public String getINDICE_PRIMARIO() {
        return INDICE_PRIMARIO;
    }

    public String getTabla() {
        return tabla;
    }

    public String getSubCategoria() {
        return subCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

}
