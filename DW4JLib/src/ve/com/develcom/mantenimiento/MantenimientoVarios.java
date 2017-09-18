/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ve.com.develcom.mantenimiento;

import com.develcom.mantenimiento.Configuracion;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
 * @author develcom
 */
public class MantenimientoVarios {
    
    /**
     * Busca la configuracion actual
     * del DW4J
     * @return
     * Un objecto con los valores
     * actual de la configuracion
     * @throws SOAPException
     * @throws SOAPFaultException 
     */
    public Configuracion buscandoMantenimiento() throws SOAPException, SOAPFaultException{
        return buscarMantenimiento();
    }
    
    /**
     * Establece la nueva configuracion
     * para el DW4J
     * @param configuracion
     * Un objeto con los valores
     * de la nueva configuracion
     * @return
     * Verdadero se el cambio fue 
     * exitos, falso en caso contrario
     * @throws SOAPException
     * @throws SOAPFaultException 
     */
    public boolean mantenimientoBaseDatos(Configuracion configuracion) throws SOAPException, SOAPFaultException{
        return mantenerBaseDatos(configuracion);
    }

    private static Configuracion buscarMantenimiento() {
        ve.com.develcom.servicios.MantenimientosServicio service = new ve.com.develcom.servicios.MantenimientosServicio();
//        com.develcom.mantenimiento.Mantenimientos_Service service = new com.develcom.mantenimiento.Mantenimientos_Service();
        com.develcom.mantenimiento.Mantenimientos port = service.getMantenimientosPort();
        return port.buscarMantenimiento();
    }

    private static boolean mantenerBaseDatos(com.develcom.mantenimiento.Configuracion configuracion) {
        ve.com.develcom.servicios.MantenimientosServicio service = new ve.com.develcom.servicios.MantenimientosServicio();
//        com.develcom.mantenimiento.Mantenimientos_Service service = new com.develcom.mantenimiento.Mantenimientos_Service();
        com.develcom.mantenimiento.Mantenimientos port = service.getMantenimientosPort();
        return port.mantenerBaseDatos(configuracion);
    }
    
}
