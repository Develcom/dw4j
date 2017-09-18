/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.visor.tool;

import com.develcom.tools.trazas.Traza;
import org.apache.log4j.Level;
/**
 *
 * @author develcom
 */
public class PageBuilder implements Runnable {
    
    private Traza traza = new Traza(PageBuilder.class);
    private Acciones acciones;

    public PageBuilder(Acciones acciones) {
        this.acciones = acciones;
        traza.trace("acciones "+acciones, Level.INFO);
    }
    
    

    int value = 0;
    long timeout;
    Thread anim;
    static final long TIMEOUT = 500;

    /**
     * add the digit to the page number and start the timeout thread
     */
    public synchronized void keyTyped(int keyval) {
        traza.trace("valor de la tecla "+keyval, Level.INFO);
        value = value * 10 + keyval;
        traza.trace("valor "+value, Level.INFO);
        timeout = System.currentTimeMillis() + TIMEOUT;
        if (anim == null) {
            anim = new Thread(this);
            anim.setName(getClass().getName());
            anim.start();
        }
    }

    /**
     * waits for the timeout, and if time expires, go to the specified page
     * number
     */
    @Override
    public void run() {
        long now, then;
        synchronized (this) {
            now = System.currentTimeMillis();
            then = timeout;
            traza.trace("ahora "+now, Level.INFO);
            traza.trace("despues "+then, Level.INFO);
        }
        while (now < then) {
            try {
                Thread.sleep(timeout - now);
            } catch (InterruptedException ie) {
            }
            synchronized (this) {
                now = System.currentTimeMillis();
                then = timeout;
                traza.trace("ahora while "+now, Level.INFO);
                traza.trace("despues while "+then, Level.INFO);
            }
        }
        synchronized (this) {
            acciones.gotoPage(value - 1);
            anim = null;
            value = 0;
        }
    }
}
