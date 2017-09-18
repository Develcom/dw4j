/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.migrardatos.bean;

import com.develcom.migrardatos.pojo.DatoAdicionalTipoDocumento;
import com.develcom.migrardatos.pojo.DatosInfoDocumento;
import com.develcom.migrardatos.pojo.Expediente;
import com.develcom.migrardatos.pojo.Indice;
import com.develcom.migrardatos.pojo.InfoDocumento;
import com.develcom.migrardatos.util.AppJDBC;
import com.develcom.migrardatos.util.Herramientas;
import com.develcom.migrardatos.util.Propiedades;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

/**
 *
 * @author familia
 */
@Component
@ManagedBean
@ViewScoped
public class RecuperaBean {

    private static final Logger LOG = LoggerFactory.getLogger(RecuperaBean.class);

    @Autowired
    private Herramientas herramientas;

    @Autowired
    private Propiedades propiedades;

    @Autowired
    private AppJDBC appJDBC;

    private String rutaTemp = "";

    public String restaurarDatos() {

        Properties prop = propiedades.configuracionCarpeta();
        String archivoComprimido = "", so;

        try {

            so = System.getProperty("os.name");
            LOG.info("sistema operativo " + so);

            if (so.contains("Windows")) {
                rutaTemp = prop.getProperty("rutaTmpWin");
                archivoComprimido = prop.getProperty("rutaWin") + "imagenes.zip";
            } else if (so.contains("Linux")) {
                rutaTemp = prop.getProperty("rutaTmp");
                archivoComprimido = prop.getProperty("ruta") + "imagenes.zip";
            }

            if (!"".equals(archivoComprimido) && !"".equals(rutaTemp)) {
                descomprimirArchivos(archivoComprimido, rutaTemp);
            } else {
                throw new Exception("problemas al descomprimir el archivo de imagenes");
            }

            agregarExpedientes();
            agregarDatosAdicionales();
            agregarInfoDocumento();
            herramientas.info("finalizado", "Fin del proceso", "");

        } catch (IOException | ClassNotFoundException | ZipException ex) {
            LOG.debug("Problemas al resturar los datos", ex);
            herramientas.error("Problemas al restaurar los datos " + ex.getMessage());
        } catch (Exception ex) {
            LOG.debug("Problemas al resturar los datos", ex);
            herramientas.error("Problemas al restaurar los datos " + ex.getMessage());
        }

        return "restaurar";
    }

    private void agregarExpedientes() throws IOException, FileNotFoundException, ClassNotFoundException {

        List<Expediente> expedientes;
        List<Indice> indices, indExterno;
        String sql, sqlAdd, tmpLib = "", tmpCat = "";
        SqlRowSet rsBuscar;
        Object[] params;
        int registro = 0, idLibreria = 0, idCategoria = 0;
        Indice indice;
        Expediente expediente;

        LOG.info("agregando expedientes");

        expedientes = desSerializar("expedientes");

        for (Expediente expe : expedientes) {

            if ((!tmpLib.equalsIgnoreCase(expe.getLibreria()))
                    && (!tmpCat.equalsIgnoreCase(expe.getCategoria()))) {

                tmpLib = expe.getLibreria();
                tmpCat = expe.getCategoria();

                sql = "select distinct l.id_libreria, c.id_categoria "
                        + "from libreria l inner join categoria c on c.id_libreria=l.id_libreria "
                        + "where libreria='" + expe.getLibreria() + "' and categoria='" + expe.getCategoria() + "'";

                rsBuscar = appJDBC.buscar(sql);

                if (rsBuscar.next()) {
                    idLibreria = rsBuscar.getInt("id_libreria");
                    idCategoria = rsBuscar.getInt("id_categoria");
                }

            }

            sql = "select * from expedientes where expediente='" + expe.getExpediente() + "'";

            rsBuscar = appJDBC.buscar(sql);

            if (!rsBuscar.next()) {

                expediente = new Expediente();

                expediente.setExpediente(expe.getExpediente());

                sql = "select * from indices where id_categoria=" + idCategoria;
                rsBuscar = appJDBC.buscar(sql);

                indices = new ArrayList<>();
                while (rsBuscar.next()) {
                    indice = new Indice();
                    indice.setClave(rsBuscar.getString("clave"));
                    indice.setCodigo(rsBuscar.getInt("codigo"));
                    indice.setIdIndice(rsBuscar.getInt("id_indice"));
                    indice.setIndice(rsBuscar.getString("indice"));
                    indice.setTipo(rsBuscar.getString("tipo"));
                    indices.add(indice);

                }

                indExterno = new ArrayList<>();
                for (Indice ind : expe.getIndices()) {
                    for (Indice indB : indices) {
                        if (indB.getIndice().equals(ind.getIndice())) {
                            indB.setValor(ind.getValor());
                            indExterno.add(indB);
                            break;
                        }
                    }
                }

                expediente.setIndices(indExterno);

                for (Indice indAdd : expediente.getIndices()) {

                    if ("FECHA".equalsIgnoreCase(indAdd.getTipo())) {

                        sqlAdd = "insert into expedientes (expediente, id_indice, fecha_indice, id_libreria, id_categoria) "
                                + "values (?, ?, ?, ?, ?)";
                    } else {

                        sqlAdd = "insert into expedientes (expediente, id_indice, valor, id_libreria, id_categoria) "
                                + "values (?, ?, ?, ?, ?)";
                    }

                    params = new Object[]{expediente.getExpediente(), indAdd.getIdIndice(), indAdd.getValor(), idLibreria, idCategoria};

                    registro = registro + appJDBC.agregarRegistro(sqlAdd, params);

                    LOG.info("registro agregado numero " + registro + " expediente " + expediente.getExpediente()
                            + " indice " + indAdd.getIndice() + " valor " + indAdd.getValor());
                }
            } else {
                LOG.info("Expediente registrado " + expe.getExpediente());
            }

            System.gc();
        }

        LOG.info("finalizado agregar expediente");
        herramientas.info("expediente", "expedientes agregados", "");
    }

    private void agregarDatosAdicionales() throws IOException, FileNotFoundException, ClassNotFoundException {

        List<DatoAdicionalTipoDocumento> datds;
        SqlRowSet rsBuscar;
        Object[] params;
        int registro = 0, idIndiceDA = 0, idTipoDoc = 0;
        String sql, sqlAdd, tmpTD = "", tmpIDA = "";

        LOG.info("agregando datos adicionales");

        datds = desSerializar("datosAdicionales");

        sqlAdd = "insert into valor_dato_adicional (id_dato_adicional, valor, numero, version, expediente) values (?, ?, ?, ?, ?)";

        for (DatoAdicionalTipoDocumento da : datds) {

            if (!tmpTD.equalsIgnoreCase(da.getTipoDocumento())) {
                tmpTD = da.getTipoDocumento();
                sql = "select id_documento from tipodocumento where tipo_documento ='" + da.getTipoDocumento().trim() + "'";
                rsBuscar = appJDBC.buscar(sql);

                rsBuscar.next();
                idTipoDoc = rsBuscar.getInt("id_documento");
            }

            if (!tmpIDA.equalsIgnoreCase(da.getIndiceAdicional())) {
                tmpIDA = da.getIndiceAdicional();
                sql = "select id_dato_adicional from dato_adicional "
                        + "where indice_adicional ='" + da.getIndiceAdicional().trim() + "' and id_documento="+idTipoDoc;
                rsBuscar = appJDBC.buscar(sql);

                rsBuscar.next();
                idIndiceDA = rsBuscar.getInt("id_dato_adicional");
            }

            params = new Object[]{idIndiceDA, da.getValor(), da.getNumero(), da.getVersion(), da.getExpediente()};

            registro = registro + appJDBC.agregarRegistro(sqlAdd, params);
            LOG.info("registro agregado " + registro + " expediente " + da.getExpediente()
                    + " indice dato adicional " + da.getIndiceAdicional() + " valor " + da.getValor());
        }
        LOG.info("finalizado agregar datos adicionales");
        herramientas.info("datosAdicionales", "Datos Adicionales agregados", "");
    }

    private void agregarInfoDocumento() throws IOException, FileNotFoundException, ClassNotFoundException {

        Properties prop = propiedades.configuracionCarpeta();
        List<InfoDocumento> infoDocumentos;
        DatosInfoDocumento did;
        SqlRowSet rsBuscar, rsIdInfoDoc;
        Object[] paramsInfoDoc, paramsDatInfoDoc;
        int registroInfoDoc = 0, registroDatosInfo = 0, idInfo, idTipoDoc;
        String[] rutas;
        String sql, sqlInfo, sqlDatosInfo, sqlIdInfo, tmpTD = "", rutaRaiz;
        File path, origen, destino;

        LOG.info("agregando infoDocumentos");

        infoDocumentos = desSerializar("infodocumentos");

        sqlInfo = "insert into infodocumento (id_infodocumento, id_documento, id_expediente, nombre_archivo, ruta_archivo, formato, numero_documento, version,"
                + "paginas, fecha_vencimiento, estatus_documento, re_digitalizado) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        sqlDatosInfo = "insert into datos_infodocumento (id_infodocumento, fecha_digitalizacion, usuario_digitalizo, fecha_aprobacion,"
                + "usuario_aprobacion, fecha_rechazo, usuario_rechazo, motivo_rechazo, causa_rechazo, fecha_eliminado, usuario_elimino"
                + "motivo_elimino, causa_elimino) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (InfoDocumento infoDoc : infoDocumentos) {

            if (!tmpTD.equalsIgnoreCase(infoDoc.getTipoDocumento())) {
                tmpTD = infoDoc.getTipoDocumento();
                sql = "select id_documento from tipodocumento where tipo_documento ='" + infoDoc.getTipoDocumento().trim() + "'";
                rsBuscar = appJDBC.buscar(sql);

                rsBuscar.next();
                idTipoDoc = rsBuscar.getInt("id_documento");

                paramsInfoDoc = new Object[]{idTipoDoc, infoDoc.getExpediente(), infoDoc.getNombreArchivo(), infoDoc.getRutaArchivo(),
                    infoDoc.getFormato(), infoDoc.getNumeroDocumento(), infoDoc.getVersion(), infoDoc.getPaginas(), infoDoc.getFechaVencimiento(),
                    infoDoc.getEstatusDocumento(), infoDoc.getReDigitalizo()};

                registroInfoDoc = registroInfoDoc + appJDBC.agregarRegistro(sqlInfo, paramsInfoDoc);
                LOG.info("registro agregado infodocumento " + registroInfoDoc + " expediente (infoDocumento) " + infoDoc.getExpediente()
                        + " archivo " + infoDoc.getNombreArchivo() + " formato " + infoDoc.getFormato());

                sqlIdInfo = "SELECT last_value as idinfodoc from sq_infodocumento";
                rsIdInfoDoc = appJDBC.buscar(sqlIdInfo);
                rsIdInfoDoc.next();
                idInfo = rsIdInfoDoc.getInt("idinfodoc");

                did = infoDoc.getDatosInfoDocumento();

                paramsDatInfoDoc = new Object[]{idInfo, idTipoDoc, did.getFechaDigitalizacion(), did.getUsuarioDigitalizo(), did.getFechaAprobacion(),
                    did.getUsuarioAprobo(), did.getFechaRechazo(), did.getUsuarioRechazo(), did.getMotivoRechazo(), did.getCausaRechazo(), did.getFechaElimino(),
                    did.getUsuarioElimino(), did.getMotivoElimino(), did.getCausaElimino()};

                registroDatosInfo = registroDatosInfo + appJDBC.agregarRegistro(sqlDatosInfo, paramsDatInfoDoc);
                LOG.info("registro agregado datos infodocumento " + registroDatosInfo + " id infoDocumento) " + idInfo);

                rutas = infoDoc.getRutaArchivo().split("/");
                rutaRaiz = prop.getProperty("rutaRaiz");

                herramientas.crearRutas(rutas, rutaRaiz);

                origen = new File(rutaTemp + "/" + infoDoc.getNombreArchivo());
                destino = new File(rutaRaiz + "/" + infoDoc.getRutaArchivo() + "/" + infoDoc.getNombreArchivo());
                herramientas.copiarArchivos(origen, destino);
                LOG.info("se copio el archivo " + origen + " hacia " + destino);

            }
        }

        LOG.info("finalizado agregar infoDocumentos y sus datos relacionados");
        herramientas.info("infoDocumento", "InfoDocumentos agregados", "");
    }

    private void descomprimirArchivos(String compressedFile, String destination) throws ZipException, FileNotFoundException, IOException, Exception {

        ZipInputStream is;
        OutputStream os;
        int BUFF_SIZE = 4096;

        LOG.info("archivo comprimido " + compressedFile);
        LOG.info("ruta de descomprecion " + destination);

        ZipFile zipFile = new ZipFile(compressedFile);

        List fileHeaderList = zipFile.getFileHeaders();

        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
            if (fileHeader != null) {
                // Build the output file
                String outFilePath = destination
                        + System.getProperty("file.separator")
                        + fileHeader.getFileName();
                File outFile = new File(outFilePath);
                // Checks if the file is a directory
                if (fileHeader.isDirectory()) {
                    // This functionality is up to your requirements
                    // For now I create the directory
                    outFile.mkdirs();
                    continue;
                }
                // Check if the directories(including parent directories)
                // in the output file path exists
                File parentDir = outFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                // Get the InputStream from the ZipFile
                is = zipFile.getInputStream(fileHeader);
                // Initialize the output stream
                os = new FileOutputStream(outFile);
                int readLen = -1;
                byte[] buff = new byte[BUFF_SIZE];
                // Loop until End of File and write the contents to the
                // output stream
                while ((readLen = is.read(buff)) != -1) {
                    os.write(buff, 0, readLen);
                }
                // Please have a look into this method for some important
                // comments
                //closeFileHandlers(is, os);
                os.close();
                is.close();
                // To restore File attributes (ex: last modified file time,
                // read only flag, etc) of the extracted file, a utility
                // class can be used as shown below
                UnzipUtil.applyFileAttributes(fileHeader, outFile);
                LOG.info("Done extracting: " + fileHeader.getFileName());
            } else {
                System.err.println("fileheader is null. Shouldn't be here");
                throw new Exception("problemas para descomprimir el archivo");
            }
        }

        //zipFile.extractAll(destination);
    }

    private <T> T desSerializar(String nombreArchivo) throws FileNotFoundException, IOException, ClassNotFoundException {

        Properties prop = propiedades.configuracionCarpeta();
        File ruta = null;
        String so;
        T obj;

        so = System.getProperty("os.name");
        LOG.info("sistema operativo " + so);

        if (so.contains("Windows")) {
            ruta = new File(prop.getProperty("rutaWin"));
        } else if (so.contains("Linux")) {
            ruta = new File(prop.getProperty("ruta"));
        }

        FileInputStream fis;
        ObjectInputStream entrada;

        fis = new FileInputStream(ruta.getAbsolutePath() + "/" + nombreArchivo + ".ser");
        entrada = new ObjectInputStream(fis);

        obj = (T) entrada.readObject();

        return obj;
    }

}
