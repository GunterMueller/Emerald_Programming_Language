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
 * Created on Nov 18, 2004
 *
 */

package dk.diku.emerald;


import org.eclipse.ui.editors.text.TextEditor;



/**
 * @author mb
 *
 */
public class EmeraldEditor extends TextEditor {
    
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class required) {
		return super.getAdapter(required);
		
	}
    public EmeraldEditor() {
		//ColorManager colorManager = new ColorManager();
        setSourceViewerConfiguration(new EmeraldSourceViewerConfiguration());
        setDocumentProvider(new EmeraldDocumentProvider());
        
    }
    
    protected void createActions() {
        super.createActions();
    }
}
