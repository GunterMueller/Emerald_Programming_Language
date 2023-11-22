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
 * Created on Dec 10, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author mb
 * 
 */
public class EmeraldProjectActionDelegate implements IObjectActionDelegate {

    
	private ISelection selection;

    /**
	 * Constructor
	 */
	public EmeraldProjectActionDelegate() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
	    Object[] elements = ((StructuredSelection) selection).toArray();
	    for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof Project) {
                try {
                    boolean addBuilder = true;
                    IProjectDescription description = ((Project) elements[i]).getDescription();
                    BuildCommand emeraldBuildCommand = new BuildCommand();
            		emeraldBuildCommand.setBuilderName(EmeraldPlugin.EMERALD_BUILDER_NAME);
            		ICommand[] originalBuilders = description.getBuildSpec();
            		
            		ICommand[] builders = new ICommand[originalBuilders.length+1];
            		for (int j = 0; j < originalBuilders.length; j++) {
            		    if (originalBuilders[j].getBuilderName().equalsIgnoreCase(EmeraldPlugin.EMERALD_BUILDER_NAME) )
            		        addBuilder = false;
                        builders[j] = originalBuilders[j];
                    }
            		builders[builders.length-1] = emeraldBuildCommand;
           		                    
            		description.setBuildSpec(builders);

            	    Shell shell = new Shell();
            	    if (addBuilder){
            		    ((Project) elements[i]).setDescription(description,new NullProgressMonitor(){});
            		    MessageDialog.openInformation(shell, "adding emerald builder", "The builder was added to the project");
            		} else {
            		    MessageDialog.openInformation(shell, "adding emerald builder", "There is already an emerald builder attached to this project");
            		}
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
                
	        
        }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	this.selection = selection;
	}

}
