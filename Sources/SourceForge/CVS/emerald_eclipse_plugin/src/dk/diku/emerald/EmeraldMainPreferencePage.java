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
 * Created on Nov 25, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * @author mb
 *
 */
public class EmeraldMainPreferencePage extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {
    
    
    /**
     * 
     */
    public EmeraldMainPreferencePage() {
        super(GRID);
        IPreferenceStore store = EmeraldPlugin.getDefault().getPreferenceStore();
        setDescription("Emerald preferences:");
        setPreferenceStore(store);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors() {
        //getErrorMessage();
        //setTitle("");
        addField(new DirectoryFieldEditor(EmeraldPlugin.EMERALD_ROOT_PREFERENCE, 
                "Emerald root ($EMERALDROOT):", getFieldEditorParent()));
        addField(new FileFieldEditor(EmeraldPlugin.EMERALD_ECPATH_PREFERENCE, 
                "Path to ec (compiler):", getFieldEditorParent()));
        addField(new FileFieldEditor(EmeraldPlugin.EMERALD_EMXPATH_PREFERENCE, 
                "Path to emx (interpreter):", getFieldEditorParent()));
        addField(
                new StringFieldEditor(EmeraldPlugin.EMERALD_ARCH_PREFERENCE, "Machine Architecture ($EMERALDARCH):", getFieldEditorParent()));
        addField(
                new StringFieldEditor(EmeraldPlugin.EMERALD_PATH_PREFERENCE, "Emerald path (will be added to $PATH):", getFieldEditorParent()));
        addField(new IntegerFieldEditor(EmeraldPlugin.EMERALD_TAB_WIDTH_PREFERENCE,"Tab width:",getFieldEditorParent()));
    }
    
    
     /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.IPreferencePage#isValid()
     */
    public boolean isValid() {
        if (!super.isValid())
            return super.isValid();
        else {
            return true;
        }
    }
}
