/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.paginas;

import com.develcom.documento.DatoAdicional;
import com.develcom.documento.InfoDocumento;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;
import ve.com.develcom.expediente.GestionDocumentos;
import ve.net.develcom.dao.Expediente;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;

/**
 *
 * @author develcom
 */
public class DatosAdicionales extends SelectorComposer<Component> {

    @Wire
    private Grid panelDatosAdicionales;

    @Wire
    private Window winDatosAdicionales;
    
    @Wire
    private Button closeBtn;

    private static final long serialVersionUID = 8625284817586431595L;
    private Herramientas herramientas = new Herramientas();
    private Traza traza = new Traza(DatosAdicionales.class);
    private Session session;
    private Expediente expediente;
    private InfoDocumento infoDocumento;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        iniciar();
    }

    public void iniciar() {

        session = herramientas.crearSesion();

        if (session != null) {

            expediente = (Expediente) session.getAttribute("expediente");
            infoDocumento = (InfoDocumento) session.getAttribute("infoDocumento");
            mostrarDatosAdicionales();
        }

    }

    private void mostrarDatosAdicionales() {

        int version = 0, cont = 2;
        Label label;
        Rows rows;
        Row row = null;
        List<com.develcom.documento.DatoAdicional> lsDatosAdicionales;

        try {
            if (infoDocumento.isTipoDocDatoAdicional()) {
                
                version = (int) session.getAttribute("version");
                winDatosAdicionales.setTitle("Datos Adicionales del Documento: " + expediente.getTipoDocumento());

                lsDatosAdicionales = new GestionDocumentos().encontrarValorDatoAdicional(expediente.getIdTipoDocumento(), expediente.getIdExpediente(), infoDocumento.getNumeroDocumento(), version);

                if (!lsDatosAdicionales.isEmpty()) {

                    rows = panelDatosAdicionales.getRows();

                    for (DatoAdicional da : lsDatosAdicionales) {

                        if (cont == 2) {
                            cont = 0;
                            row = new Row();
                        }

                        label = new Label(da.getIndiceDatoAdicional() + ": " + da.getValor().toString());

                        row.appendChild(label);
                        rows.appendChild(row);

                        cont++;
                    }

                }
            }

        } catch (SOAPException | SOAPFaultException ex) {
            traza.trace("problemas al buscar los datos adicionales", Level.ERROR, ex);
        }

    }

    @Listen("onClick = #closeBtn")
    public void cerrarVentana(Event e) {
        winDatosAdicionales.detach();
    }
}
