package dk.diku.emerald.dependencies;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.search.internal.core.SearchScope;
import org.eclipse.search.internal.ui.text.FileSearchQuery;
import org.eclipse.search.internal.ui.text.FileSearchResult;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import dk.diku.emerald.dependencies.model.Dependency;
import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;
import dk.diku.emerald.dependencies.model.EmeraldDependencyFile;

public class EmeraldMarkerResolutionGenerator implements
IMarkerResolutionGenerator2 {
	
	private class EmeraldMarkerResolution implements IMarkerResolution {
		private File file;
		private String search;
		private EmeraldDependencyDiagram edd;
		
		private EmeraldMarkerResolution(File file, String search) {
			super();
			this.file = file;
			this.search = search;
		}
		
		public String getLabel() {
			return "Create a dependency on the file '"+file.getFullPath().toOSString()+"'? This emerald file uses '" 
				+ search + "' which seems to be exported in the file '"+file.getFullPath().toOSString()+"'. Creating a dependency might solve this issue.";
		}
		
		public void run(IMarker marker) {
			final String project = marker.getResource().getProject().getRawLocation().toOSString();
			edd = new EmeraldDependencyDiagram(project+DependencyFilesPlugin.DEPENDENCY_FILE_NAME);
			EmeraldDependencyFile originFile = edd.getNamedEmeraldDependencyFile(marker.getResource().getFullPath().toOSString());
			EmeraldDependencyFile targetFile = edd.getNamedEmeraldDependencyFile(file.getFullPath().toOSString());
			new Dependency(originFile,targetFile);
			try {
				new ProgressMonitorDialog(new Shell()).run(
						false, // don't fork
						false, // not cancelable
						new WorkspaceModifyOperation() { // run this operation
							public void execute(final IProgressMonitor monitor) {
								edd.export(project+DependencyFilesPlugin.DEPENDENCY_FILE_NAME);
							}
						});
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private IResource resource;
	private EmeraldMarkerResolution[] resolutions;
	
	public EmeraldMarkerResolutionGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public IMarkerResolution[] getResolutions(IMarker marker) {
		if (resolutions==null) {
			this.resource = marker.getResource();
			try {
				String msg = marker.getAttribute(IMarker.MESSAGE,"");
				if(msg.startsWith("Undefined identifier ")) {
					String search = msg.substring(msg.indexOf("Undefined identifier ")+"Undefined identifier ".length(),msg.length());
					IResource[] members = ((IProject) resource.getWorkspace().getRoot().getProject("Emerald_Compiler2")).members();
					FileSearchQuery s = new FileSearchQuery(SearchScope.newSearchScope("",members), "i","export *"+search,false);
					
					s.run(new NullProgressMonitor());
					FileSearchResult result = (FileSearchResult) s.getSearchResult();
					Object[] elements = result.getElements();
					this.resolutions = new EmeraldMarkerResolution[elements.length];			
					
					for (int i = 0; i < elements.length; i++) {
						if(elements[i] instanceof File) {
							File file = (File) elements[i];
							EmeraldMarkerResolution resolution = new EmeraldMarkerResolution(file,search);
							this.resolutions[i] = resolution;
						}
					}
				} else {
					this.resolutions = new EmeraldMarkerResolution[] {};
				}
				
				int i = 1;
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
			
		return resolutions;
	}

	public boolean hasResolutions(IMarker marker) {
		String msg = marker.getAttribute(IMarker.MESSAGE,"");
		if(msg.startsWith("Undefined identifier ")) {
			return true;
		}
		return false;
	}
	
}
