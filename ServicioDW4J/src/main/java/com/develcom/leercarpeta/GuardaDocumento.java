package com.develcom.leercarpeta;

import com.develcom.administracion.Categoria;
import com.develcom.administracion.Libreria;
import com.develcom.administracion.SubCategoria;
import com.develcom.administracion.TipoDocumento;
import com.develcom.cliente.AdministracionBusqueda;
import com.develcom.cliente.GestionArchivos;
import com.develcom.cliente.GestionExpediente;
import com.develcom.documento.Bufer;
import com.develcom.documento.DatoAdicional;
import com.develcom.documento.InfoDocumento;
import com.develcom.expediente.Expedientes;
import com.develcom.expediente.Indice;
import com.develcom.util.CodDecodArchivos;
import com.develcom.util.EncriptaDesencripta;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Service
//@Configurable
public class GuardaDocumento {

    private final Logger traza = LoggerFactory.getLogger(GuardaDocumento.class);

    @Autowired
    private GestionExpediente ge;

    @Autowired
    private AdministracionBusqueda ab;

    @Autowired
    private GestionArchivos ga;

    @Autowired
    private CodDecodArchivos cda;

    public boolean guardarDocumento(List<Properties> lstProp, File rutaArchivoLocal) {

        InfoDocumento infoDoc;
        TipoDocumento td = null;
        Expedientes expedientes = new Expedientes();
        List<Libreria> librerias = new ArrayList<>();
        List<Categoria> categorias = new ArrayList<>();
        List<SubCategoria> subCategorias = new ArrayList<>();
        List<TipoDocumento> documentos = new ArrayList<>();
        List<DatoAdicional> datosAdicionales = new ArrayList<>();
        Bufer bufer = new Bufer();
        int idDoc = 0, idCat, idSubCat, idLib, contIndice = 1;
        String resp, tipoDocumento, categoria, subCategoria, ruta, libreria, expediente, accion;
        InputStream leyendo = null;
        byte[] bu;
        char[] acc;
        Matcher mat;
        Pattern pat;
        pat = Pattern.compile(" ");
        boolean existeExpe, expeRegistrado, guardo = false;
        Indice indice;
        File archivoLocal;

        for (Properties prop : lstProp) {

            try {

                expediente = prop.getProperty("expediente").trim();
                libreria = prop.getProperty("libreria").trim();
                categoria = prop.getProperty("categoria").trim();
                subCategoria = prop.getProperty("subcategoria").trim();
                tipoDocumento = prop.getProperty("tipoDocumento").trim();
                accion = prop.getProperty("accion").trim();
                acc = accion.toCharArray();
                acc[0] = String.valueOf(acc[0]).toUpperCase().charAt(0);
                accion = String.copyValueOf(acc);

                traza.info("guardando documento del expediente " + expediente);
                traza.info("accion a realizar con el documento " + accion);

                if (!librerias.isEmpty()) {
                    librerias.clear();
                }

                if (!categorias.isEmpty()) {
                    categorias.clear();
                }

                if (!subCategorias.isEmpty()) {
                    subCategorias.clear();
                }

                if (!documentos.isEmpty()) {
                    documentos.clear();
                }

                librerias = ab.buscarLibreria(libreria, 0);
                idLib = librerias.get(0).getIdLibreria();
                traza.info("libreria " + libreria + " id " + idLib);

                categorias = ab.buscarCategoria(categoria, idLib, 0);
                idCat = categorias.get(0).getIdCategoria();
                traza.info("categoria " + categoria + " id " + idCat);

                subCategorias = ab.buscarSubCategoria(subCategoria, idCat, 0);
                idSubCat = subCategorias.get(0).getIdSubCategoria();
                traza.info("subCategoria " + subCategoria + " id " + idSubCat);

                documentos = ab.buscarTipoDocumento(tipoDocumento, idCat, idSubCat);
                for (TipoDocumento doc : documentos) {
                    if (tipoDocumento.equalsIgnoreCase(doc.getTipoDocumento().trim())) {

                        td = new TipoDocumento();
                        td.setDatoAdicional(doc.getDatoAdicional());
                        td.setEstatus(doc.getEstatus());
                        td.setFicha(doc.getFicha());
                        td.setIdCategoria(doc.getIdCategoria());
                        td.setIdSubCategoria(doc.getIdSubCategoria());
                        td.setIdTipoDocumento(doc.getIdTipoDocumento());
                        td.setTipoDocumento(doc.getTipoDocumento());
                        td.setVencimiento(doc.getVencimiento());

                        idDoc = td.getIdTipoDocumento();
                        break;
                    }
                }

                if (td.getDatoAdicional().equalsIgnoreCase("1")) {
                    traza.info("tiene datos adicionales buscandolos...");
                    datosAdicionales = armarDatosAdicionales(prop, idDoc);
                }
                traza.info("tipoDocumento " + td.getTipoDocumento() + " id " + idDoc);

                if (!librerias.isEmpty()) {

                    existeExpe = buscarExpediente(expediente, categoria, libreria);

                    if (!existeExpe) {

                        List<com.develcom.administracion.Indice> indices1 = ab.buscandoIndices(idCat);

                        for (com.develcom.administracion.Indice ind : indices1) {
                            indice = new Indice();
                            String valor = prop.getProperty("indice" + contIndice);

                            if (valor != null && !"".equalsIgnoreCase(valor)) {
                                indice.setIdIndice(ind.getIdIndice());
                                indice.setIdCategoria(ind.getIdCategoria());
                                indice.setTipo(ind.getTipo());
                                indice.setClave(ind.getClave());
                                indice.setCodigo(ind.getCodigo());
                                indice.setIndice(ind.getIndice());
                                indice.setValor(valor);
                                expedientes.getIndices().add(indice);
                            }
                            contIndice++;
                        }
                        expedientes.setExpediente(expediente);
                        expedientes.setIdLibreria(idLib);
                        expedientes.setIdCategoria(idCat);
                        expedientes.setExiste(false);

                        expeRegistrado = ge.archivarExpediente(expedientes);

                    } else {
                        expeRegistrado = true;
                    }

                    if (expeRegistrado) {

                        archivoLocal = new File(rutaArchivoLocal.getAbsolutePath() + "/" + tipoDocumento + ".pdf");

                        traza.info("archivo a guardar " + archivoLocal.getName());

                        mat = pat.matcher(libreria);
                        libreria = mat.replaceAll("");

                        mat = pat.matcher(categoria);
                        categoria = mat.replaceAll("");

                        mat = pat.matcher(subCategoria);
                        subCategoria = mat.replaceAll("");

                        mat = pat.matcher(tipoDocumento);
                        tipoDocumento = mat.replaceAll("");

                        libreria = EncriptaDesencripta.encripta(libreria);
                        categoria = EncriptaDesencripta.encripta(categoria);
                        subCategoria = EncriptaDesencripta.encripta(subCategoria);
                        tipoDocumento = EncriptaDesencripta.encripta(tipoDocumento);
                        ruta = libreria + "/" + categoria + "/" + subCategoria;

                        if (archivoLocal.exists()) {

                            traza.info("archivo a codificar " + archivoLocal.getAbsolutePath());

                            cda.codificar(archivoLocal.getAbsolutePath(), rutaArchivoLocal.getAbsolutePath() + "/codificado.cod");

                            leyendo = new FileInputStream(rutaArchivoLocal.getAbsolutePath() + "/codificado.cod");
                            traza.info("posible tamaño del archivo a transferir " + (leyendo.available() / 1024000));
                            bu = new byte[leyendo.available()];
                            leyendo.read(bu);
                            leyendo.close();
                            bufer.setBufer(bu);

                            infoDoc = new InfoDocumento();
                            infoDoc.setIdDocumento(idDoc);
                            infoDoc.setRutaArchivo(ruta);
                            infoDoc.setNombreArchivo(tipoDocumento);
                            infoDoc.setReDigitalizo(false);
                            infoDoc.setIdExpediente(expediente);
                            infoDoc.setCantPaginas(Integer.parseInt(prop.getProperty("cantidadPaginas")));
                            infoDoc.setFormato("pdf");
                            infoDoc.setEstatus(1);
                            infoDoc.setVersion(0);

                            if (!datosAdicionales.isEmpty()) {
                                for (DatoAdicional da : datosAdicionales) {
                                    traza.info("agregando el dato adicional " + da.getIndiceDatoAdicional() 
                                            + " valor " + da.getValor().toString() + " al objecto infoDocumento");
                                    infoDoc.getLsDatosAdicionales().add(da);
                                }
                            }

                            resp = ga.almacenaArchivo(bufer, accion, infoDoc, expediente, "externo");

                            if ("exito".equalsIgnoreCase(resp)) {
                                guardo = true;
                            }
                        } else {
                            throw new Exception("Archivo a codificar no exixte " + archivoLocal);
                        }

                    } else {
                        throw new Exception("Problemas al guardar el expediente " + expediente);
                    }
                } else {
                    throw new Exception("Libreria no existe " + prop.getProperty("libreria"));
                }

            } catch (SOAPFaultException | SOAPException | IOException | SQLException | ParseException ex) {
                traza.error("problemas al guardar el archivo", ex);
            } catch (Exception ex) {
                traza.error("problemas al guardar el archivo", ex);
            } finally {
                try {
                    leyendo.close();
                } catch (IOException ex) {
                    traza.error("error al cerrar el archivo", ex);
                }
            }
        }
        return guardo;
    }

    private boolean buscarExpediente(String expediente, String categoria, String libreria)
            throws SOAPException, SQLException, JsonProcessingException, ParseException {

        List<Libreria> librerias;
        List<Categoria> categorias;
        Expedientes expedientes;
        int idLib, idCat;
        boolean existe = false;

        categorias = ab.buscarCategoria(categoria, 0, 0);
        idCat = categorias.get(0).getIdCategoria();

        librerias = ab.buscarLibreria(libreria, 0);
        idLib = librerias.get(0).getIdLibreria();

        expedientes = ge.encuentraExpediente(expediente, idCat, idLib);

        traza.info("expediente existe " + expedientes.isExiste());

        if (expedientes.isExiste()) {
            existe = true;
        } else {

        }

        return existe;
    }

    private List<DatoAdicional> armarDatosAdicionales(Properties prop, int idDoc) throws SOAPException {

        List<DatoAdicional> datosAdicionales = new ArrayList<>();
        List<com.develcom.administracion.DatoAdicional> daBus;
        DatoAdicional da;
        String[] valores;
        int cont = 1;

        daBus = ab.buscarIndDatoAdicional(idDoc);
        traza.info("tamaño lista datos adicionales buscados " + daBus.size());

        for (com.develcom.administracion.DatoAdicional daBu : daBus) {

            String daProp = prop.getProperty("datoadicional" + cont);
            valores = daProp.split(",");
            String ind = valores[0];
            String valor = valores[1];

            traza.info("indice dato adicional properties " + ind + " valor " + valor);

            if (daBu.getIndiceDatoAdicional().equalsIgnoreCase(valores[0])) {
                da = new DatoAdicional();

                da.setCodigo(daBu.getCodigo());
                da.setIdDatoAdicional(daBu.getIdDatoAdicional());
                da.setIdTipoDocumento(daBu.getIdTipoDocumento());
                da.setIdValor(daBu.getIdValor());
                da.setIndiceDatoAdicional(daBu.getIndiceDatoAdicional());
                da.setNumeroDocumento(daBu.getNumeroDocumento());
                da.setTipo(daBu.getTipo());
                da.setValor(valor);
                da.setVersion(daBu.getVersion());
                datosAdicionales.add(da);
            }
        }
        traza.info("tamaño lista datos adicionales " + datosAdicionales.size());
        return datosAdicionales;
    }

}
