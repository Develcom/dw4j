/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.net.develcom.tool.barra;

import java.io.Serializable;

/**
 * Handles setting of status bar messages.  All calls to this object must be
 * done from within the ZK framework, such as inside an event.
 *
 * Hides the details of what component is actually a status bar.  It could be a
 * textbox, or something else, but we don't want to be dependant on any specific
 * type of control, in case it changes in the future.
 * 
 * @author develcom
 */
public interface IBarraEstado extends Serializable{
    /**
     * Sets the status bar text for the time period indicated.
     *
     * @param statusText status text
     * @param timePeriod delay in seconds, before the status bar will be
     *                   cleared.
     */
    public void setStatus(final String statusText, final int timePeriod);

    /**
     * Sets the status bar text.  Pass null to clear.
     *
     * @param statusText status text or null to clear
     */
    public void setStatus(final String statusText);

    /**
     * @return the status text of the status bar.
     */
    public String getStatus();
    
}
