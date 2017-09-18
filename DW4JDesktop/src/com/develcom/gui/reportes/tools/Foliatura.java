/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.gui.reportes.tools;

import com.develcom.tools.trazas.Traza;
import java.util.HashMap;
import org.apache.log4j.Level;
import javax.xml.soap.SOAPException;

/**
 *
 * @author develcom
 */
public class Foliatura {

    private Traza traza = new Traza(Foliatura.class);
      
    

    public boolean generarFoliatura(int idLibreria, int idCategoria, String expediente){

        boolean imprimir=false;
        HashMap folio = new HashMap();
        ProcesaReporte pr = new ProcesaReporte();

        try {

            imprimir = new ve.com.develcom.foliatura.Foliatura().armarFoliatura(expediente, idLibreria, idCategoria);

            traza.trace("respuesta al armar la foliatura "+imprimir, Level.INFO);

            if(imprimir){
                folio.put("idLib", idLibreria);
                folio.put("idCat", idCategoria);
                folio.put("idExpediente", expediente);
                pr.crearReporte("foliatura.jrxml", folio, "Indice de Foliatura");
            }

        } catch (SOAPException ex) {
            traza.trace("problemas al al crear la foliatura", Level.ERROR, ex);
        }
        return imprimir;
    }



}
