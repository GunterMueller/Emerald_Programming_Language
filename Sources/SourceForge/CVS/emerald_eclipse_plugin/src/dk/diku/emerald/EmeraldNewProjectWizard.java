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
 * Created on Dec 9, 2004
 *
 */
package dk.diku.emerald;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.internal.events.BuildCommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * @author mb
 * 
 */
public class EmeraldNewProjectWizard extends Wizard implements INewWizard {

//    private ISelection selection;
    private EmeraldNewProjectWizardPage page;
    protected IProject project;

    /**
     * 
     */
    public EmeraldNewProjectWizard() {
        super();
        setNeedsProgressMonitor(true);        
    }
    
	public void addPages() {
	    page = new EmeraldNewProjectWizardPage("Emerald Project");
		addPage(page);
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish() {
		final String projectName = page.getProjectName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(projectName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
    }
    
	private void doFinish(String projectName, IProgressMonitor monitor) throws CoreException {
			// create a sample file
			monitor.beginTask("Creating " + projectName, 2);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			// If i don't run this next line (createNewProject()) as a synchronous execution i get a "invalid thread access" error.
			getShell().getDisplay().syncExec(new Runnable() {
                public void run() {
                    project = createNewProject();
                }
            });
						
			monitor.worked(1);
	}
		
	/**
	 * Creates a new project resource with the entered name.
	 *
	 * @return the created project resource, or <code>null</code> if the project
	 *    was not created
	 */
	private IProject createNewProject() {
		// get a project handle
	    final IProject newProjectHandle = page.getProjectHandle();
	    // get a project descriptor
		IPath defaultPath = Platform.getLocation();
		IPath newPath = page.getLocationPath();
		if (defaultPath.equals(newPath))
			newPath = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
		description.setLocation(newPath);
		BuildCommand emeraldBuildCommand = new BuildCommand();
		emeraldBuildCommand.setBuilderName("dk.diku.emerald.Builder");
		BuildCommand[] builders = new BuildCommand[]{emeraldBuildCommand}; 
		description.setBuildSpec(builders);
		
		
	
		// define the operation to create a new project
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor) throws CoreException {
				createProject(description, newProjectHandle, monitor);
			}
		};
	
		// run the operation to create a new project
		try {
			getContainer().run(true, true, op);
		}
		catch (InterruptedException e) {
			return null;
		}
		catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof CoreException) {
				if (((CoreException)t).getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
					MessageDialog.openError(
						getShell(), 
						"Error creating project",  //$NON-NLS-1$
						"Case Variant Exists"//IDEWorkbenchMessages.getString("CreateProjectWizard.caseVariantExistsError")  //$NON-NLS-1$,
						);	
				} else {
					ErrorDialog.openError(
						getShell(), 
						"Error creating project",  //$NON-NLS-1$
						null, // no special message
				 		((CoreException) t).getStatus());
				}
			} else {
				// Unexpected runtime exceptions and errors may still occur.
				MessageDialog.openError(
					getShell(),
					"Error creating project",  //$NON-NLS-1$
					"Internal error"//IDEWorkbenchMessages.format("CreateProjectWizard.internalError", new Object[] {t.getMessage()})
					); 
			}
			return null;
		}
	
		return newProjectHandle;
	}
	
	/**
	 * Creates a project resource given the project handle and description.
	 *
	 * @param description the project description to create a project resource for
	 * @param projectHandle the project handle to create a project resource for
	 * @param monitor the progress monitor to show visual progress with
	 *
	 * @exception CoreException if the operation fails
	 * @exception OperationCanceledException if the operation is canceled
	 */
	private void createProject(IProjectDescription description, IProject projectHandle, IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		try {
			monitor.beginTask("", 2000); //$NON-NLS-1$
			projectHandle.create(description, new SubProgressMonitor(monitor,1000));
			if (monitor.isCanceled())
				throw new OperationCanceledException();
			projectHandle.open(new SubProgressMonitor(monitor,1000));
		} finally {
			monitor.done();
		}
	}


    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

}
