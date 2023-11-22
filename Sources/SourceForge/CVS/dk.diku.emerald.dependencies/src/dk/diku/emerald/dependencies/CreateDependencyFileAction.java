package dk.diku.emerald.dependencies;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;
import dk.diku.emerald.dependencies.model.EmeraldDependencyFile;

public class CreateDependencyFileAction implements IObjectActionDelegate {

	private EmeraldDependencyDiagram edd;
	private ISelection selection;
	private Point verticalposition = new Point();;
	/**
	 * Constructor for Action1.
	 */
	public CreateDependencyFileAction() {
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
		try {
			edd = new EmeraldDependencyDiagram();
		
			Project project = ((Project) ((org.eclipse.jface.viewers.StructuredSelection) selection).getFirstElement());
			IPath projectPath = project.getFullPath();
			String filename = "";
			if (projectPath!= null) 
				filename = projectPath.toOSString()+DependencyFilesPlugin.DEPENDENCY_FILE_NAME;
			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource != null) {
						if (resource instanceof File) {
							if (resource.getRawLocation() != null) {
								if (resource.isAccessible()) {
									if (resource.getFileExtension()!=null && resource.getFileExtension().equalsIgnoreCase("m")) {
										
//										if(((File) resource).getFullPath().getFileExtension().equalsIgnoreCase("m")) {
										EmeraldDependencyFile newfile = new EmeraldDependencyFile(((File) resource).getFullPath().toOSString());
										newfile.setLocation(verticalposition);
										verticalposition.setLocation(verticalposition.x, verticalposition.y+20);
										edd.addChild(newfile);
										return true;
									}
								}
							}
						}
						
					}
					return true;
				}});
			createfile(filename);
			verticalposition = new Point();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (java.lang.ClassCastException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	protected InputStream getInitialContents() {
		ByteArrayInputStream bais = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			edd.createDocument().write(baos);
			bais = new ByteArrayInputStream(baos.toByteArray());
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return bais;
	}

	private void createfile(String FilePath) {
		IPath newFilePath = new Path(FilePath);
    final IFile newFileHandle = createFileHandle(newFilePath);
    final InputStream initialContents = getInitialContents();
    try {
			newFileHandle.create(initialContents,false,new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
			MessageDialog.openError(
					new Shell(),
					"Error creating file",
					"Something happened when creating the file: \n\n"+FilePath+"\n\nIt probably means that the file already exists. Delete it and try again.");
		}

	}
  protected IFile createFileHandle(IPath filePath) {
    return ResourcesPlugin.getWorkspace().getRoot().getFile(
            filePath);
  }
}
