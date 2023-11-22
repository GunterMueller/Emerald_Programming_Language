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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * Label provider for the EmeraldTableViewer
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class EmeraldTableLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider {

	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "checked";
	public static final String UNCHECKED_IMAGE  = "unchecked";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */ 
	static {
		String iconPath = "icons/"; 
	
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromURL(EmeraldPlugin.getDefault().getBundle().getEntry(iconPath+CHECKED_IMAGE+".gif")));
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromURL(EmeraldPlugin.getDefault().getBundle().getEntry(iconPath+UNCHECKED_IMAGE+".gif")));
	}
	
	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	private Image getImage(Boolean isSelected) {
		String key = isSelected.booleanValue() ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		Image h = imageRegistry.get(key);
		return h;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		EmeraldHost host = (EmeraldHost) element;
		switch (columnIndex) {
			case 0: 
				result = host.getHost();
				break;
			case 1 :
				result = host.getUsername();
				break;
			case 2 :
				result = host.getEmx();
				break;
			case 3 :
				break;
			case 4 :
				result = host.getArguments();
				break;
			case 5 :
				break;
			case 6 :
			    result = host.getFiles();
			    break;
			case 7 :
			    result = host.getPriority().toString();
			    break;
			case 8 :
				break;
			default :
				break; 	
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
	    switch (columnIndex) {
	    case 3:
	        return getImage(((EmeraldHost) element).getEnabled()); 
	    case 5:
	        return getImage(((EmeraldHost) element).getRoothost());
	    case 8:
	        return getImage(((EmeraldHost) element).getMonitor()); 
	    default:
	        return null;
	    }
	}

}
