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
 * Created on Dec 2, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author mb
 * 
 */
public class EmeraldColorPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {

    /**
     * @param style
     */
    public EmeraldColorPreferencePage() {
        super(GRID);
        IPreferenceStore store = EmeraldPlugin.getDefault().getPreferenceStore();
        setDescription("Editor colors preferences");
        setPreferenceStore(store);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors() {
        addField(new ColorFieldEditor(EmeraldPlugin.EMERALD_COLOR_SINGLE_LINE_COMMENT_PREFERENCE, 
                "Single-line comment:", getFieldEditorParent()));
        addField(new ColorFieldEditor(EmeraldPlugin.EMERALD_COLOR_KEYWORD_PREFERENCE, 
                "Keywords:", getFieldEditorParent()));
        addField(new ColorFieldEditor(EmeraldPlugin.EMERALD_COLOR_CONSTANT_PREFERENCE, 
                "Constants:", getFieldEditorParent()));
        addField(new ColorFieldEditor(EmeraldPlugin.EMERALD_COLOR_STRING_PREFERENCE, 
                "Strings:", getFieldEditorParent()));
        addField(new ColorFieldEditor(EmeraldPlugin.EMERALD_COLOR_DEFAULT_PREFERENCE, 
                "Default (normal text):", getFieldEditorParent()));
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}
