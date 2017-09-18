/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.tool.barra;

import org.apache.log4j.Level;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import ve.net.develcom.log.Traza;
import ve.net.develcom.tool.Herramientas;

/**
 *
 * @author develcom
 */
//public class BarraEstado extends Textbox implements Composer<Component>, IBarraEstado {
public class BarraEstado extends Textbox implements AfterCompose, IBarraEstado {

    private static final long serialVersionUID = 1582128542041479067L;
    private Traza traza = new Traza(BarraEstado.class);

    public BarraEstado() {
    }

    @Override
    public void afterCompose() {
        Herramientas herramientas = new Herramientas();

        Session session = herramientas.crearSesion();
        
        if(session != null){
            session.setAttribute("status", this);
        }
    }

    @Override
    public void setStatus(final String statusText, final int timePeriod) {
        
        traza.trace("estatus "+statusText+" periodo de tiempo "+timePeriod, Level.INFO);
        setStatus(statusText);
        
        // the timer must be in the page, or the page is inactive and this
        // call would never happen?
        Timer timer = (Timer) getFellow("statusTimer");
        timer.setDelay(timePeriod * 1000);
        timer.start();
    }

    @Override
    public void setStatus(final String statusText) {
        traza.trace("estatus "+statusText, Level.INFO);
        setText(statusText);
    }

    @Override
    public String getStatus() {
        return getText();
    }

}
