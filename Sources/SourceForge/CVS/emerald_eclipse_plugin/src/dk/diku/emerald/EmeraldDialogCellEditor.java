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
 * Created on Jan 26, 2005
 *
 */
package dk.diku.emerald;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author mb
 * 
 */
public class EmeraldDialogCellEditor extends DialogCellEditor {

    private ICellEditorValidator validator;
    private String value = ""; 
    
    /**
     * 
     */
    public EmeraldDialogCellEditor() {
        super();
    }

    protected boolean isCorrect(Object value) {
    	String errorMessage = null;
    	if (validator == null)
    		return true;

    	errorMessage = validator.isValid(value);
    	return (errorMessage == null || errorMessage.equals(""));//$NON-NLS-1$

        //return true;
    }
    
    class EmeraldCellEditorValidator implements ICellEditorValidator{
        public String isValid(Object value) {
            if(value instanceof String)
                return null;
            else
                return "";
        }
    }
    
    /**
     * @param parent
     */
    public EmeraldDialogCellEditor(Composite parent) {
        super(parent);
        validator = new EmeraldCellEditorValidator();
    }
    
    

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
     */
    protected Object doGetValue() {
        return value;
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
     */
    protected void doSetValue(Object value) {
        if (value instanceof String) {
    //        System.out.println("doSetValue: "+value);  
            if(!((String) value).equalsIgnoreCase(""))
                this.value = (String) value;
            
        }
    }
    /**
     * @param parent
     * @param style
     */
    public EmeraldDialogCellEditor(Composite parent, int style) {
        super(parent, style);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    protected Object openDialogBox(Control cellEditorWindow) {
        EmeraldResourceSelectionDialog r = new EmeraldResourceSelectionDialog(cellEditorWindow.getShell(),ResourcesPlugin.getWorkspace().getRoot(),"");
        r.setTitle("Select files to be transfered and launched");
        Object value = getValue();
        String stringvalue= "";
        if (value instanceof String) {
            stringvalue = (String) value;
        }
        String[] s = stringvalue.split("result:");
        if(s.length>1){
            String[] strs = s[1].split(":");
            File[] initial = new File[strs.length];
            for (int i = 0; i < strs.length; i++) {
                //System.out.println(i+":"+strs[i]);
                initial[i] = new EmeraldFile(new Path(strs[i]),(Workspace) ResourcesPlugin.getWorkspace());
            }
            
            r.setInitialSelections(initial);
        }
        String result = stringvalue;
        int success = r.open();
        if(success == 0) {
            ICellEditorValidator v = validator;
            Object[] res = r.getResult();
            result = "result:";
            if(res!=null){
                for (int i = 0; i < res.length; i++) {
                    if(res[i] instanceof File)
                        result += ((File) res[i]).getFullPath().toOSString()+":";
                }
            }
        } 
        return result;
    }
    
    
}
