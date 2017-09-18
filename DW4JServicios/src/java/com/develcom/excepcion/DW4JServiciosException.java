/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.develcom.excepcion;

import java.io.Serializable;
import javax.xml.ws.WebFault;

/**
 *
 * @author develcom
 */
@WebFault(name="DW4JServiciosException")
public class DW4JServiciosException extends Throwable implements Serializable{

    private DW4JServiciosException dwjse;
    private static final long serialVersionUID = 1692398413474730780L;

    public DW4JServiciosException(String message) {
        super(message);
    }

    public DW4JServiciosException(DW4JServiciosException dwjse) {
        this.dwjse = dwjse;
    }
    
    public DW4JServiciosException getFaultInfo() {
        return dwjse;
    }

}
