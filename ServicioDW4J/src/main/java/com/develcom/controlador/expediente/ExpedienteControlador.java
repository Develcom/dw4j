/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.controlador.expediente;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.cliente.GestionArchivos;
import com.develcom.documento.Bufer;
import com.develcom.documento.InfoDocumento;
import com.develcom.util.BaseDato;
import com.develcom.util.Propiedades;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author develcom
 */
@RestController
@RequestMapping("/expediente")
public class ExpedienteControlador {

    @Autowired
    private Propiedades propiedades;

    @Autowired
    private BaseDato bd;

    @Autowired
    private GestionArchivos ga;

    private static final Logger LOG = LoggerFactory.getLogger(ExpedienteControlador.class);

    @RequestMapping(value = "/buscarExpediente/{expediente}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
//    public JSONObject buscarExpediente(@PathVariable String expediente)
//            throws SQLException, JsonProcessingException, ParseException {
    public Expedientes buscarExpediente(@PathVariable String expediente)
            throws SQLException, JsonProcessingException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        JSONParser jp = new JSONParser();
        JSONObject datosExp;
        Expedientes expedientes = new Expedientes();
        String sqlIndices, sql, lib = "", cat = "", subCat = "", tipoDoc, ind, val;
        int idInfo = 0, idCat = 0, idSubCat = 0;
        ResultSet rsExpediente;
        Libreria libreria;
        List<Libreria> librerias = new ArrayList<>();
        Categoria categoria;
        List<Categoria> categorias = new ArrayList<>();
        SubCategoria subCategoria;
        List<SubCategoria> subCategorias = new ArrayList<>();
        TipoDocumento tipoDocumento;
        List<TipoDocumento> tiposDocumentos = new ArrayList<>();
        InfoDocumento infoDocumento;
        List<InfoDocumento> infoDocumentos = new ArrayList<>();
        List<Indices> indicesList = new ArrayList<>();
        Indices indices;

        LOG.info("buscando datos del expediente " + expediente);

        sqlIndices = "select e.expediente, e.valor, e.fecha_indice, i.indice, i.tipo "
                + "from expedientes e "
                + "inner join indices i on e.id_indice=i.id_indice "
                + "where e.expediente='" + expediente + "'"
                + "order by i.clave desc";

        rsExpediente = bd.consultas(sqlIndices);

        while (rsExpediente.next()) {

            ind = rsExpediente.getString("indice");
            LOG.info("indice agregado " + ind);

            if (!"FECHA".equalsIgnoreCase(rsExpediente.getString("tipo"))) {
                val = rsExpediente.getString("valor");
                indices = new Indices();
                indices.setIndice(ind);
                indices.setValor(val);
                indicesList.add(indices);
                LOG.info("valor agregado " + val);
            } else {
                val = sdf.format(rsExpediente.getDate("fecha_indice"));
                indices = new Indices();
                indices.setIndice(ind);
                indices.setValor(val);
                indicesList.add(indices);
                LOG.info("valor fecha agregado " + val);
            }
        }

        sql = "select distinct i.id_expediente, l.id_libreria, l.libreria, c.id_categoria, "
                + "c.categoria, s.id_subcategoria, s.subcategoria, t.id_documento, t.tipo_documento, i.formato, "
                + "i.id_infodocumento, i.numero_documento, i.version, i.ruta_archivo, i.nombre_archivo "
                + "from infodocumento i "
                + "inner join tipodocumento t on t.id_documento=i.id_documento "
                + "inner join subcategoria s on s.id_subcategoria=t.id_subcategoria "
                + "inner join categoria c on c.id_categoria=s.id_categoria "
                + "inner join libreria l on l.id_libreria=c.id_libreria "
                + "where i.id_expediente='" + expediente + "' and i.estatus_documento=1 "
                + "order by t.tipo_documento, i.numero_documento, i.version desc";

        rsExpediente = bd.consultas(sql);

        while (rsExpediente.next()) {
            if (expediente.equals(rsExpediente.getString("id_expediente"))) {
                libreria = new Libreria();
                categoria = new Categoria();
                subCategoria = new SubCategoria();
                tipoDocumento = new TipoDocumento();
                infoDocumento = new InfoDocumento();
                
                if(idCat != rsExpediente.getInt("id_categoria")){
                    idCat = rsExpediente.getInt("id_categoria");
                }
                
                if(idSubCat != rsExpediente.getInt("id_subcategoria")){
                    idSubCat = rsExpediente.getInt("id_subcategoria");
                }

                if (!lib.equalsIgnoreCase(rsExpediente.getString("libreria"))) {
                    lib = rsExpediente.getString("libreria");
                    libreria.setDescripcion(lib);
                    libreria.setIdLibreria(rsExpediente.getInt("id_libreria"));
                    librerias.add(libreria);
                    LOG.info("libreria agregada " + lib);
                }

                if (!cat.equalsIgnoreCase(rsExpediente.getString("categoria"))) {
                    cat = rsExpediente.getString("categoria");
                    categoria.setCategoria(cat);
                    categoria.setIdCategoria(rsExpediente.getInt("id_categoria"));
                    categorias.add(categoria);
                    LOG.info("categoria agregada " + cat);
                }

                if (!subCat.equalsIgnoreCase(rsExpediente.getString("subcategoria"))) {
                    subCat = rsExpediente.getString("subcategoria");
                    subCategoria.setSubCategoria(subCat);
                    subCategoria.setIdSubCategoria(rsExpediente.getInt("id_subcategoria"));
                    subCategorias.add(subCategoria);
                    LOG.info("subCategoria agregada " + subCat);
                }

//                if (!tipoDoc.equalsIgnoreCase(rsExpediente.getString("tipo_documento"))) {
//                    tipoDoc = rsExpediente.getString("tipo_documento");
//                    tipoDocumento.setTipoDocumento(tipoDoc);
//                    tipoDocumento.setIdTipoDocumento(rsExpediente.getInt("id_documento"));
//                    tiposDocumentos.add(tipoDocumento);
//                    LOG.info("tipo de documentos agregado " + tipoDoc);
//                }
                if (idInfo != rsExpediente.getInt("id_infodocumento")) {

                    tipoDoc = rsExpediente.getString("tipo_documento");
                    tipoDocumento.setTipoDocumento(tipoDoc);
                    tipoDocumento.setIdTipoDocumento(rsExpediente.getInt("id_documento"));
                    tipoDocumento.setIdCategoria(idCat);
                    tipoDocumento.setIdSubCategoria(idSubCat);
                    tiposDocumentos.add(tipoDocumento);
                    LOG.info("tipo de documentos agregado " + tipoDoc);

                    idInfo = rsExpediente.getInt("id_infodocumento");
                    infoDocumento.setFormato(rsExpediente.getString("formato"));
                    infoDocumento.setIdInfoDocumento(rsExpediente.getInt("id_infodocumento"));
                    infoDocumento.setNumeroDocumento(rsExpediente.getInt("numero_documento"));
                    infoDocumento.setVersion(rsExpediente.getInt("version"));
//                    infoDocumento.setNombreArchivo(rsExpediente.getString("nombre_archivo"));
//                    infoDocumento.setRutaArchivo(rsExpediente.getString("ruta_archivo"));
                    infoDocumento.setIdDocumento(rsExpediente.getInt("id_documento"));
                    infoDocumentos.add(infoDocumento);
                    LOG.info("id infodocumento agregado " + tipoDoc);
                }
            }
        }

        expedientes.setExpediente(expediente);
        expedientes.setCategoria(categorias);
        expedientes.setSubCategoria(subCategorias);
        expedientes.setTiposDocumentos(tiposDocumentos);
        expedientes.setLibreria(librerias);
        expedientes.setInfoDocumentos(infoDocumentos);
        expedientes.setIndices(indicesList);

        datosExp = (JSONObject) jp.parse(new ObjectMapper().writeValueAsString(expedientes));
//        return datosExp;
        return expedientes;

    }

    @RequestMapping(value = "/buscarFisicoDocumento/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> buscarFisicoDocumento(HttpServletRequest request,
            @PathVariable int id) throws IOException, SQLException, SOAPFaultException, SOAPException {

        HttpHeaders headers = new HttpHeaders();
        ResultSet rsInfoDoc;
        String sql, ruta, nombre, raiz, rutaCompeta;
        ResponseEntity<byte[]> responseEntity = null;
        Properties prop;
        Bufer bufer;
        byte[] media;

        sql = "select * from infodocumento where id_infodocumento=" + id;
        rsInfoDoc = bd.consultas(sql);

        if (rsInfoDoc.next()) {

            prop = propiedades.configuracionCarpeta();

            raiz = prop.getProperty("rutaRaiz");
            ruta = rsInfoDoc.getString("ruta_archivo");
            nombre = rsInfoDoc.getString("nombre_archivo");
            rutaCompeta = raiz + ruta + "/" + nombre;

            LOG.info("nombre " + nombre);
            LOG.info("ruta " + ruta);

            bufer = ga.buscandoArchivo(ruta, nombre);

            media = bufer.getBufer();

            LOG.info("bufer " + media);

//            in = request.getServletContext().getResourceAsStream(rutaCompeta);
//            media = IOUtils.toByteArray(in);
//            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
//            responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
            responseEntity = new ResponseEntity<>(media, HttpStatus.OK);

            LOG.info("responseEntity " + responseEntity);

        }
        return responseEntity;

    }
}
