package com.develcom.externo;

import com.develcom.administracion.AdministracionBusquedas;
import com.develcom.dao.Indice;
import com.develcom.logs.Traza;
import com.develcom.tools.BaseDato;
import com.develcom.tools.Propiedades;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Level;

@WebService(serviceName = "DatosExternos")
public class DatosExternos {

    /**
     * Escribe trazas en el log
     */
    private Traza traza = new Traza(AdministracionBusquedas.class);

    @WebMethod(operationName = "buscarDatosExternos")
    public List<Indice> buscarDatosExternos(
            @WebParam(name = "cedula") String cedula,
            @WebParam(name = "idCategoria") int idCategoria) {

        List<List<Indice>> listaIndices = new ArrayList<>();
        List<Indice> indices = null;
        ResultSet rsExterno;
        BaseDato bd = new BaseDato();
        Statement buscar;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Properties prop = Propiedades.cargarPropiedadesWS();

            buscar = bd.conectar("mfprestaciones03_10_16").createStatement();

            rsExterno = buscar.executeQuery(prop.getProperty("queryExterno") + cedula);

            indices = new AdministracionBusquedas().buscarIndices(idCategoria);

            while (rsExterno.next()) {
                for (Indice ind : indices) {

                    if ("NUMERO DE SOLICITUD U OFICIO".equalsIgnoreCase(ind.getIndice())) {
                        ind.setValor(rsExterno.getObject("c_numero_oficio"));
                    } else if ("CEDULA DE IDENTIDAD DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                        ind.setValor(rsExterno.getObject("c_cedula_persona"));
                    } else if ("APELLIDOS Y NOMBRES DE EMPLEADO".equalsIgnoreCase(ind.getIndice())) {
                        ind.setValor(rsExterno.getObject("c_nombre_completo"));
                    } else if ("ANO DE SOLICITUD".equalsIgnoreCase(ind.getIndice())) {
                        String anio = sdf.format(rsExterno.getDate("c_fecha_recepcion"));
                        anio = anio.substring(6);
                        ind.setValor(anio);
                    }
                }
                listaIndices.add(indices);
            }

        } catch (SQLException ex) {
            traza.trace("problemas al buscar los datos externos", Level.ERROR, ex);
        }
        return indices;
    }
}
