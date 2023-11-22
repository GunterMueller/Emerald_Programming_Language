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
 *  Created on Nov 22, 2004
 */
package dk.diku.emerald;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;


/**
 * @author mb
 *
 */
public class EmeraldIncrementalProjectBuilder extends IncrementalProjectBuilder {
	
	private IPreferenceStore preferences;
	private static String EMERALD_ROOT;
	private static String EMERALD_PATH;
	private static String EMERALD_ARCH;
    private String tracebackLines = "";
	
	/**
	 * 
	 */
	public EmeraldIncrementalProjectBuilder() {
		super();
		preferences = EmeraldPlugin.getDefault().getPreferenceStore();
		
		EMERALD_ROOT = preferences.getDefaultString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE);
		EMERALD_PATH = preferences.getDefaultString(EmeraldPlugin.EMERALD_PATH_PREFERENCE);
		EMERALD_ARCH = preferences.getDefaultString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE);
		preferences.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty().equals(EmeraldPlugin.EMERALD_ROOT_PREFERENCE)) {
					EMERALD_ROOT = preferences.getString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE);
				}
				if(event.getProperty().equals(EmeraldPlugin.EMERALD_PATH_PREFERENCE)) {
					EMERALD_PATH = preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE);
				}	
				if(event.getProperty().equals(EmeraldPlugin.EMERALD_ARCH_PREFERENCE)) {
					EMERALD_ARCH = preferences.getString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE);
				}
				
			}   
			
		});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
	throws CoreException {
		if(kind == EmeraldIncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if(delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta,monitor);
			}
			
		}
		
		return null;	
	}
	
	
	
	/**
	 * @param monitor
	 */
	private void fullBuild(IProgressMonitor monitor) {
//		System.out.println("full build");
		try {
			getProject().getWorkspace().getRoot().accept(new IResourceVisitor(){

				public boolean visit(IResource resource) throws CoreException {
					buildResource(resource);
					return true;
				}});
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param delta
	 * @param monitor
	 */
	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
//		System.out.println("incremental build on " + delta);
		monitor.beginTask("IncrementalBuild",IProgressMonitor.UNKNOWN);
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) {
					IResource resource = delta.getResource();
					buildResource(resource);
					return true; //visit children too
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
		monitor.done();
	}
	/**
	 * Creates a string buffer from the given input stream
	 */
	protected String getStringFromStream(InputStream stream) throws IOException {
		StringBuffer buffer = new StringBuffer();
		byte[] b = new byte[100];
		int finished = 0;
		while (finished != -1) {
			finished = stream.read(b);
			if (finished != -1) {
				String current = new String(b, 0, finished);
				buffer.append(current);
			}
		}
		return buffer.toString();
	}
	
	/**
	 * @param resource
	 */
	private void buildResource(IResource resource) {
	    if (resource != null) {
	        if (resource.getRawLocation() != null) {

	            String location = resource.getRawLocation().toOSString();
	            
	            if (resource.isAccessible()) {
	                if (resource.getFileExtension()!=null && resource.getFileExtension().equalsIgnoreCase("m")) {
	                    try {
	                        //System.out.println("Building: " + location);
	                        resource.deleteMarkers(IMarker.PROBLEM, true,
	                                IResource.DEPTH_INFINITE);
	                        Runtime r = Runtime.getRuntime();
	                        
	                        //System.out.println("normal: "+resource.getRawLocation().toString());
	                        //System.out.println("UNC: "+resource.getRawLocation().makeUNC(true));
	                        //System.out.println(preferences.getString(EmeraldPlugin.EMERALD_ECPATH_PREFERENCE));
	                        
	                        //String[] commandLine = new String[] {
	                          //      preferences.getString(EmeraldPlugin.EMERALD_ECPATH_PREFERENCE),
	                            //    resource.getRawLocation().toString()};
	                        String commandLine = preferences.getString(EmeraldPlugin.EMERALD_ECPATH_PREFERENCE)+" "+resource.getRawLocation().toString();

	                        //System.out.println("PATH=" + preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE));
	                        //System.out.println("EMERALDROOT=" + preferences.getString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE));
	                        //System.out.println("EMERALDARCH=" + preferences.getString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE));
	                        String[] environment = new String[] {
	                                "PATH="+DebugPlugin.getDefault().getLaunchManager().getNativeEnvironment().get("PATH")+ ":"+preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE),
	                                "EMERALDROOT="+ preferences.getString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE),
	                                "EMERALDARCH="+ preferences.getString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE) };
	                        
	                        Process p = r.exec(commandLine, environment);
	                        try {
	                            p.waitFor();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            
    /*                        IWorkbenchPage[] pages = EmeraldPlugin.getDefault().getWorkbench().getWorkbenchWindows()[0].getPages();
                            
                            IViewPart ciew;
                            IWorkbenchPart view;
                            
                            for (int i = 0; i < pages.length; i++) {
                                view = pages[i].findView("org.eclipse.ui.views.ProblemView");
                                if (view != null) {
                                    pages[i].bringToTop(view);
                                }
                                    
                            }
                             */
                            if(p.exitValue()>0) {
                                InputStream stream = p.getErrorStream();
                                
                                String compilerErrorOutput = getStringFromStream(stream);
                                
                                stream = p.getInputStream();
                                
                                String compilerOutput = getStringFromStream(stream);

                                if(p.exitValue()==127){
                                    Display.getDefault().asyncExec(new EmeraldErrorMessage("The Emerald compiler exited with an error" , "The compiler exited with the following error message:\n\n"+compilerErrorOutput+"\n\nIf you want to disable the automatic background compiler, you can remove the check mark in \"Project->Build Automatically\"") );
                                }
                                
                                
                                //System.out.println(p.exitValue()+"");
                                
                                // prints out the information
                                //System.out.println(compilerErrorOutput);
                                //System.out.println(compilerOutput);
                                
                                String[] compilerOutputLines = compilerOutput
                                .split("\n");
                                for (int i = 0; i < compilerOutputLines.length; i++) {
                                    String line = compilerOutputLines[i];
                                    if (line.startsWith("Compiling"))
                                        continue;
                                    if (line.startsWith("\"" + location + "\",")) {
                                        int lineno;
                                        boolean warning = false;
                                        
                                        if (line.indexOf(" Warning: ")==-1) {
                                            lineno = Integer.parseInt(line.substring(
                                                    line.indexOf("\", line") + 8,
                                                    line.indexOf(": ")));
                                        } else {
                                            lineno = Integer.parseInt(line.substring(
                                                    line.indexOf("\", line") + 8,
                                                    line.indexOf(" Warning: ")));
                                            warning = true;
                                        }
                                        
                                        String msg = line.substring(line
                                                .indexOf(":") + 2);
                                        
                                        msg+=tracebackLines;
                                        
                                        IMarker m = resource
                                        .createMarker(IMarker.PROBLEM);
                                        
                                        m.setAttribute(IMarker.LINE_NUMBER, lineno);
                                        m.setAttribute(IMarker.MESSAGE, msg);
                                        m.setAttribute(
                                                IMarker.USER_EDITABLE,
                                                false);
                                        m.setAttribute(IMarker.PRIORITY,
                                                IMarker.PRIORITY_NORMAL);
                                        if (warning) {
                                            m.setAttribute(IMarker.SEVERITY,
                                                    IMarker.SEVERITY_WARNING);
                                        } else {
                                            m.setAttribute(IMarker.SEVERITY,
                                                    IMarker.SEVERITY_ERROR);
                                        }
                                        
                                        if (msg.equalsIgnoreCase("syntax error")) {
                                            //TODO implement syntax error squiggles
                                            //int charstart = compilerOutputLines[i + 2].indexOf("^");
                                            //int charend = compilerOutputLines[i + 2].lastIndexOf("^");
                                            //m.setAttribute(IMarker.CHAR_START,2);
                                            //m.setAttribute(IMarker.CHAR_END,3);
                                            break;
                                        }
                                        //reset tracebackLines at every line 
                                        tracebackLines = "";
                                        
                                    } else {
                                        tracebackLines = " <-"+line+tracebackLines;								    
                                    }
                                }
                            }
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    } catch (CoreException e) {
	                        // Something went wrong
	                        e.printStackTrace();
	                    }
	                }
	            }
	            
	        }
	    }
	}
	
}
