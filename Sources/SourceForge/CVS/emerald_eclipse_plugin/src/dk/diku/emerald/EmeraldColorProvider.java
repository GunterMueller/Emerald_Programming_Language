/*
 * The Emerald Language Eclipse Plugin
 * 
 * Copyright (C) 2004 Mathias Bertelsen <mathias@bertelsen.co.uk>
 * 
 * This file is part of the Emerald Language Eclipse Plugin.
 *
 * The Emerald Language Eclipse Plugin is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 *  The Emerald Language Eclipse Plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with the Emerald Language Eclipse Plugin; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Created on Nov 19, 2004
 *
 */
package dk.diku.emerald;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author mb
 *
 */
public class EmeraldColorProvider {
    public static final RGB SINGLE_LINE_COMMENT= new RGB(128, 128, 0);
    public static final RGB KEYWORD= new RGB(0, 0, 255);
    public static final RGB TYPE= new RGB(0, 255, 0);
    public static final RGB STRING= new RGB(0, 128, 0);
    public static final RGB DEFAULT= new RGB(0, 0, 0);
    
    protected Map fColorTable= new HashMap(10);
    
    /**
     * Release all of the color resources held onto by the receiver.
     */	
    public void dispose() {
        Iterator e= fColorTable.values().iterator();
        while (e.hasNext())
            ((Color) e.next()).dispose();
    }
    
    /**
     * Return the Color that is stored in the Color table as rgb.
     */
    public Color getColor(RGB rgb) {
        Color color= (Color) fColorTable.get(rgb);
        if (color == null) {
            color= new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
}
