/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.dao;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author develcom
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bufer implements Serializable{
    private static final long serialVersionUID = -2069642847456543872L;
    
    @XmlInlineBinaryData
    private byte[] bufer;
    private boolean existe;

    public byte[] getBufer() {
        return bufer;
    }

    public void setBufer(byte[] bufer) {
        this.bufer = bufer;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
}
