package com.develcom.migrardatos.bean;

//import com.develcom.migrardatos.pojo.Combo;
import com.develcom.migrardatos.pojo.DatoAdicionalTipoDocumento;
import com.develcom.migrardatos.pojo.DatosInfoDocumento;
import com.develcom.migrardatos.pojo.Expediente;
import com.develcom.migrardatos.pojo.Indice;
import com.develcom.migrardatos.pojo.InfoDocumento;
import com.develcom.migrardatos.util.AppJDBC;
import com.develcom.migrardatos.util.Herramientas;
import com.develcom.migrardatos.util.Propiedades;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
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
public class BusquedaBean {

    private static final Logger LOG = LoggerFactory.getLogger(BusquedaBean.class);

    @Autowired
    private Herramientas herramientas;

    @Autowired
    private AppJDBC appJDBC;

//    @Autowired
//    private Combo combo;
    @Autowired
    private Propiedades propiedades;

    private Date fechaDesde;
    private Date fechaHasta;
    private boolean exito;
    private String fechaInicio;
    private String fechaFin;

    private List<Expediente> expedientes;
    List<DatoAdicionalTipoDocumento> datds;
    List<InfoDocumento> infoDocumentos;

    /**
     * Creates a new instance of BusquedaBean
     */
    public BusquedaBean() {

    }

    public String buscarDatos() {
        Properties prop = propiedades.configuracionCarpeta();
        File ruta = null;
        String so;
        boolean expe, datosAdd, infoDoc;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            so = System.getProperty("os.name");
            LOG.info("sistema operativo " + so);

            if (so.contains("Windows")) {
                ruta = new File(prop.getProperty("rutaWin"));
            } else if (so.contains("Linux")) {
                ruta = new File(prop.getProperty("ruta"));
            }

            if (!ruta.exists()) {
                ruta.mkdir();
            }

            if (fechaDesde != null) {
                if (fechaHasta != null) {

                    LOG.info("fecha desde " + getFechaDesde() + " fecha hasta " + getFechaHasta());

                    expe = buscarExpedientes();
                    datosAdd = buscarDatosAdicionales();
                    infoDoc = buscarInfoDocumentos();

                    if (expe && datosAdd && infoDoc) {
                        LOG.info("proceso de respaldo finalizado con exito");
                        exito = true;
                        fechaInicio = sdf.format(fechaDesde);
                        fechaFin = sdf.format(fechaHasta);
                    } else {
                        herramientas.error("Problemas realizando el respaldo");
                    }
                } else {
                    herramientas.warn("Debe colocar la fecha fin");
                }
            } else {
                herramientas.warn("Debe colocar la fecha inicio");
            }
        } catch (SQLException | IOException ex) {
            LOG.error("Problemas al restaurar los datos", ex);
            herramientas.error("Problemas en la restauracion de datos " + ex.getMessage());
        }
        return "respaldo";
    }

    private boolean buscarExpedientes() throws SQLException, FileNotFoundException, IOException {

        Expediente expediente;
        Indice indice;
        List<Indice> indices;
        SqlRowSet rsBuscar, rsIndice;
        String sql, expe, sqlInd;
        File archivo;
        boolean resp = false;

        expedientes = new ArrayList<>();

        sql = "select DISTINCT e.expediente, l.libreria, c.categoria from expedientes e "
                + "inner join libreria l on l.id_libreria=e.id_libreria "
                + "inner join categoria c on c.id_categoria=e.id_categoria "
                + "where expediente in "
                + "(select DISTINCT id_expediente from tipodocumento t "
                + "inner join infodocumento i on t.id_documento=i.id_documento "
                + "inner join datos_infodocumento d on i.id_infodocumento=d.id_infodocumento "
                + "where d.fecha_digitalizacion between '" + getFechaDesde() + "' and '" + getFechaHasta() + "') "
                + "order by e.expediente";

        rsBuscar = appJDBC.buscar(sql);

        LOG.info("agregando expedientes a las lista");

        while (rsBuscar.next()) {

            expe = rsBuscar.getString("expediente");

            expediente = new Expediente();

            expediente.setExpediente(expe);
            expediente.setLibreria(rsBuscar.getString("libreria"));
            expediente.setCategoria(rsBuscar.getString("categoria"));

            sqlInd = "select i.indice, i.tipo, i.clave, i.codigo, e.expediente, e.valor, e.fecha_indice "
                    + "from indices i inner join expedientes e on i.id_indice=e.id_indice "
                    + "where e.expediente='" + expe + "' order by i.clave desc";

            rsIndice = appJDBC.buscar(sqlInd);

            indices = new ArrayList<>();

            while (rsIndice.next()) {

                indice = new Indice();

                indice.setIndice(rsIndice.getString("indice"));
                indice.setTipo(rsIndice.getString("tipo"));
                indice.setClave(rsIndice.getString("clave"));

                if (rsIndice.getDate("fecha_indice") != null) {
                    indice.setValor(rsIndice.getDate("fecha_indice").toString());
                } else {
                    indice.setValor(rsIndice.getString("valor"));
                }
                
                LOG.info("agregando el indice "+indice.getIndice()+" del expediente "+expediente.getExpediente());

                indices.add(indice);

            }

            expediente.setIndices(indices);

            expedientes.add(expediente);

            LOG.info("expediente agregado " + expediente.getExpediente()+" tamaño de los indices "+indices.size());

        }

        LOG.info("finalizado el agregar expedientes a la lista");

        archivo = new File(serializar(expedientes, "expedientes"));

        if (archivo.exists() && !expedientes.isEmpty()) {
            resp = true;
        }

        LOG.info("proceso terminado de buscar los expedientes " + resp);

        return resp;
    }

    private boolean buscarDatosAdicionales() throws SQLException, FileNotFoundException, IOException {

        DatoAdicionalTipoDocumento datoAdicionalTipoDocumento;
        SqlRowSet rsBuscar;
        String sql;
        File archivo;
        boolean resp = false;

        datds = new ArrayList<>();

        LOG.info("fecha desde " + getFechaDesde() + " fecha hasta " + getFechaHasta());

        sql = "select t.tipo_documento, da.*, vda.* from dato_adicional da "
                + "inner join valor_dato_adicional vda on da.id_dato_adicional=vda.id_dato_adicional "
                + "inner join tipodocumento t on t.id_documento=da.id_documento "
                + "where da.id_documento in "
                + "(select i.id_documento from tipodocumento t "
                + "inner join infodocumento i on t.id_documento=i.id_documento "
                + "inner join datos_infodocumento d on i.id_infodocumento=d.id_infodocumento "
                + "where d.fecha_digitalizacion between '" + getFechaDesde() + "' and '" + getFechaHasta() + "') "
                + "order by t.tipo_documento, da.indice_adicional";

        rsBuscar = appJDBC.buscar(sql);

        LOG.info("agregando datos adicionales a la lista");

        while (rsBuscar.next()) {

            datoAdicionalTipoDocumento = new DatoAdicionalTipoDocumento();

            datoAdicionalTipoDocumento.setExpediente(rsBuscar.getString("expediente"));
            datoAdicionalTipoDocumento.setIndiceAdicional(rsBuscar.getString("indice_adicional"));
            datoAdicionalTipoDocumento.setNumero(rsBuscar.getInt("numero"));
            datoAdicionalTipoDocumento.setTipoDocumento(rsBuscar.getString("tipo_documento"));
            datoAdicionalTipoDocumento.setValor(rsBuscar.getString("valor"));
            datoAdicionalTipoDocumento.setVersion(rsBuscar.getInt("version"));

//            if (rsBuscar.getInt("codigo") != 0) {
//                datoAdicionalTipoDocumento.setCombo(buscarValoresCombo(rsBuscar.getInt("codigo")));
//            }
            datds.add(datoAdicionalTipoDocumento);

            LOG.info("dato adicional agregado " + datoAdicionalTipoDocumento.getIndiceAdicional() 
                    + " expediente " + datoAdicionalTipoDocumento.getExpediente());
        }
        LOG.info("finalizado el agregar datos adicionales a la lista");

        archivo = new File(serializar(datds, "datosAdicionales"));

        if (archivo.exists() && !datds.isEmpty()) {
            resp = true;
        }

        LOG.info("proceso terminado de buscar los datos adicionales " + resp);

        return resp;
    }

    public boolean buscarInfoDocumentos() throws SQLException, FileNotFoundException, IOException {

        InfoDocumento infoDocumento;
        DatosInfoDocumento datosInfoDocumento;
        Properties prop = propiedades.configuracionCarpeta();
        ArrayList<File> archivos = new ArrayList<>();
        SqlRowSet rsBuscar;
        String sql;
        File archivoSer;
        boolean resp = false;

        infoDocumentos = new ArrayList<>();

        LOG.info("fecha desde " + getFechaDesde() + " fecha hasta " + getFechaHasta());

        sql = "select t.tipo_documento, s.subcategoria, i.*, d.* "
                + "from tipodocumento t inner join subcategoria s on s.id_subcategoria=t.id_subcategoria "
                + "inner join infodocumento i on t.id_documento=i.id_documento "
                + "inner join datos_infodocumento d on i.id_infodocumento=d.id_infodocumento "
                + "where d.fecha_digitalizacion between '" + getFechaDesde() + "' and '" + getFechaHasta() + "' "
                + "order by i.id_infodocumento, i.id_expediente";

        rsBuscar = appJDBC.buscar(sql);

        LOG.info("agregando infodocumentos a la lista");

        while (rsBuscar.next()) {

            infoDocumento = new InfoDocumento();
            datosInfoDocumento = new DatosInfoDocumento();

            datosInfoDocumento.setCausaElimino(rsBuscar.getString("causa_elimino"));
            datosInfoDocumento.setCausaRechazo(rsBuscar.getString("causa_rechazo"));
            datosInfoDocumento.setFechaDigitalizacion(rsBuscar.getDate("fecha_digitalizacion"));
            datosInfoDocumento.setFechaAprobacion(rsBuscar.getDate("fecha_aprobacion"));
            datosInfoDocumento.setFechaElimino(rsBuscar.getDate("fecha_eliminado"));
            datosInfoDocumento.setFechaRechazo(rsBuscar.getDate("fecha_rechazo"));
            datosInfoDocumento.setMotivoElimino(rsBuscar.getString("motivo_elimino"));
            datosInfoDocumento.setMotivoRechazo(rsBuscar.getString("motivo_rechazo"));
            datosInfoDocumento.setUsuarioDigitalizo(rsBuscar.getString("usuario_digitalizo"));
            datosInfoDocumento.setUsuarioAprobo(rsBuscar.getString("usuario_aprobacion"));
            datosInfoDocumento.setUsuarioElimino(rsBuscar.getString("usuario_elimino"));
            datosInfoDocumento.setUsuarioRechazo(rsBuscar.getString("usuario_rechazo"));

            infoDocumento.setDatosInfoDocumento(datosInfoDocumento);
            infoDocumento.setEstatusDocumento(rsBuscar.getInt("estatus_documento"));
            infoDocumento.setExpediente(rsBuscar.getString("id_expediente"));
            infoDocumento.setFechaVencimiento(rsBuscar.getDate("fecha_vencimiento"));
            infoDocumento.setFormato(rsBuscar.getString("formato"));
            infoDocumento.setNombreArchivo(rsBuscar.getString("nombre_archivo"));
            infoDocumento.setNumeroDocumento(rsBuscar.getInt("numero_documento"));
            infoDocumento.setPaginas(rsBuscar.getInt("paginas"));
            infoDocumento.setReDigitalizo(rsBuscar.getString("re_digitalizado"));
            infoDocumento.setRutaArchivo(rsBuscar.getString("ruta_archivo"));
            infoDocumento.setRutaRaiz(prop.getProperty("rutaRaiz"));
            infoDocumento.setSubCategoria(rsBuscar.getString("subcategoria"));
            infoDocumento.setTipoDocumento(rsBuscar.getString("tipo_documento"));
            infoDocumento.setVersion(rsBuscar.getInt("version"));

            File archivo = new File(infoDocumento.getRutaRaiz() + infoDocumento.getRutaArchivo() + "/" + infoDocumento.getNombreArchivo());
            LOG.info("agregando el archivo " + archivo.getName());
            archivos.add(archivo);

            infoDocumentos.add(infoDocumento);

        }
        LOG.info("finalizado el agregar infoDocumentos a la lista");

        archivoSer = new File(serializar(infoDocumentos, "infodocumentos"));

        comprimirArchivos(archivos);

        if (archivoSer.exists() && !infoDocumentos.isEmpty()) {
            resp = true;
        }

        LOG.info("proceso terminado de buscar los expedientes " + resp);

        return resp;
    }

//    private List<Combo> buscarValoresCombo(int codigo) throws SQLException {
//
//        List<Combo> combos = new ArrayList<>();
//        SqlRowSet rsCombo;
//
//        rsCombo = appJDBC.buscar("select * from lista_desplegables where codigo_indice=" + codigo);
//
//        while (rsCombo.next()) {
//
//            combo.setDescripcion(rsCombo.getString("descripcion"));
//            combos.add(combo);
//        }
//
//        return combos;
//    }
    private <T> String serializar(List<T> list, String nombreArchivo) throws FileNotFoundException, IOException {

        Properties prop = propiedades.configuracionCarpeta();
//        String ruta = properties.getProperty("ruta");
        File ruta = null, file;
        String so, archivo;
        FileOutputStream fos;
        ObjectOutputStream salida;

        so = System.getProperty("os.name");
        LOG.info("sistema operativo " + so);

        if (so.contains("Windows")) {
            ruta = new File(prop.getProperty("rutaWin"));
        } else if (so.contains("Linux")) {
            ruta = new File(prop.getProperty("ruta"));
        }

        LOG.info("creando la ruta " + ruta);

        if (!ruta.exists()) {
            LOG.info("ruta creada " + ruta.mkdirs());
        }

        archivo = ruta.getAbsolutePath() + "/" + nombreArchivo + ".ser";

        file = new File(archivo);

        LOG.info("creando la ruta " + file);

        if (!file.exists()) {
            if (file.createNewFile()) {
                LOG.info("creado el archivo " + archivo);
            }
        }

        fos = new FileOutputStream(archivo);
        salida = new ObjectOutputStream(fos);

        salida.writeObject(list);

        fos.close();
        salida.close();

        return archivo;
    }

    private void comprimirArchivos(ArrayList<File> archivos) {

        Properties properties = propiedades.configuracionCarpeta();
        String ruta = properties.getProperty("ruta");
        ZipFile zipFile;
        ZipParameters parameters = new ZipParameters();
        File archivoZip = new File(ruta + "imagenes.zip");
        //File archivoZip;

        try {
            //archivoZip = File.createTempFile("archivoCiclos", ".zip");

            if (archivoZip.exists()) {
                archivoZip.delete();
            }

            zipFile = new ZipFile(archivoZip);

            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

            zipFile.createZipFile(archivos, parameters);

        } catch (ZipException e) {
            LOG.error("Error al comprimir los archivos", e);
            herramientas.error("Problemas al comprimir los archivos" + e.getMessage());
        }
    }

    /**
     * @return the fechaDesde
     */
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fechaDesde the fechaDesde to set
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    /**
     * @return the fechaHasta
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fechaHasta the fechaHasta to set
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    /**
     * @return the exito
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * @param exito the exito to set
     */
    public void setExito(boolean exito) {
        this.exito = exito;
    }

    /**
     * @return the fechaInicio
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

}
