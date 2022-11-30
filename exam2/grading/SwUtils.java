package grading;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;


/**
 * $Id: WindowUtils.java,v 1.16 2009/05/25 16:37:52 kschaefe Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Taken from http://www.java2s.com/Code/Java/Swing-JFC/GetAllComponentsinacontainer.htm
 *
 * Encapsulates various utilities for windows (ie: <code>Frame</code> and
 * <code>Dialog</code> objects and descendants, in particular).
 *
 * @author Richard Bair
 * @author modified E Brown Nov 2022
 */
class SwUtils {
    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    /* string for identing component in tests */
    public static String cIdent(Component c){

        if( c instanceof JFrame) return ((JFrame) c).getTitle();
        if( c instanceof JLabel) return ((JLabel) c).getText();
        if( c instanceof JButton) return ((JButton) c).getText();
        if( c instanceof JMenu) return ((JMenu) c).getText();
        if( c instanceof JMenuItem) return ((JMenuItem) c).getText();
        if( c.getName() != null ) return c.getName();
        return c.toString();
    }
}

