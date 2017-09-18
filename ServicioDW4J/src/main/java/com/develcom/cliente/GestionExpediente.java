/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.cliente;

import com.develcom.expediente.Expedientes;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GestionExpediente {

    private Logger traza = LoggerFactory.getLogger(GestionExpediente.class);

    /**
     * Guarda un nuevo expediente
     *
     * @param expediente Toda la informacion relacionada con el expediente
     * @return Verdadero si se guardo, falso en caso contrario
     */
    public boolean archivarExpediente(Expedientes expediente) throws SOAPException, SOAPFaultException {
        traza.trace("guarda un expediente nuevo " + expediente);
        return guardarExpediente(expediente);
    }

    /**
     * Verifica si el expediente exite o no para crearlo
     *
     * @param idExpediente El id del expediente
     * @param idCategoria El id de la categoria
     * @param idLibreria El id de la libreria
     * @return La infoprmacion del expediente, o los mensaje realcionado con el
     * expediente si existe o no, o si esta asociado a otra categoria
     * @throws javax.xml.soap.SOAPException
     */
    public Expedientes encuentraExpediente(String idExpediente, int idCategoria, int idLibreria) throws SOAPException, SOAPFaultException {
        traza.trace("comprueba si existe un expediente");
        traza.trace("idExpediente " + idExpediente);
        traza.trace("idCategoria " + idCategoria);
        traza.trace("idLibreria " + idLibreria);
        return buscarExpediente(idExpediente, idCategoria, idLibreria);
    }


    private static boolean guardarExpediente(com.develcom.expediente.Expedientes expediente) {
        com.develcom.cliente.servicios.ExpedienteServicio service = new com.develcom.cliente.servicios.ExpedienteServicio();
        com.develcom.expediente.Expediente port = service.getExpedientePort();
        return port.guardarExpediente(expediente);
    }

    private static Expedientes buscarExpediente(java.lang.String idExpediente, int idCategoria, int idLibreria) {
        com.develcom.cliente.servicios.ExpedienteServicio service = new com.develcom.cliente.servicios.ExpedienteServicio();
        com.develcom.expediente.Expediente port = service.getExpedientePort();
        return port.buscarExpediente(idExpediente, idCategoria, idLibreria);
    }
    
}
