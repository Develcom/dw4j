/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.gui.tools;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JDesktopPane;
import javax.swing.Scrollable;

/**
 *
 * @author develcom
 */
public class ScrollDesktop extends JDesktopPane implements Scrollable {
    private static final long serialVersionUID = -4426410150899514861L;

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle r, int axis, int dir) {
        return 50;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle r, int axis, int dir) {
        return 200;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
