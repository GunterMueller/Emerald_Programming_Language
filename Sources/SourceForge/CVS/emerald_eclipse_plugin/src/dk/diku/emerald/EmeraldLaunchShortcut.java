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
 * Created on Dec 1, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * @author mb
 * 
 */
public class EmeraldLaunchShortcut implements ILaunchShortcut {

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection, java.lang.String)
     */
    public void launch(ISelection selection, String mode) {
 
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
     */
    public void launch(IEditorPart editor, String mode) {
        
        IEditorInput input = editor.getEditorInput();
        try {
            IFile file = ((IFileEditorInput) input).getFile();
            if (file != null) {
                String fileLocation = file.getLocation().toOSString();
                String executableFileLocation = fileLocation.substring(0,fileLocation.lastIndexOf("."))+".x";
                Path path = new Path(executableFileLocation);
                if (path.toFile().isFile()) {
                    ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
                    ILaunchConfigurationType type = manager.getLaunchConfigurationType(EmeraldPlugin.EMERALD_LAUNCH_CONFIGURATION_TYPE);
                    ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
                    for (int i = 0; i < configurations.length; i++) {
                        ILaunchConfiguration configuration = configurations[i];
                        if (configuration.getName().equals("Launch Emerald File")) {
                            configuration.delete();
                            break;
                        }
                    }
                    ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, "Launch "+file.getName()+" as Emerald File");
                    workingCopy.setAttribute(EmeraldPlugin.EMERALD_LAUNCH_FILE,executableFileLocation);
                    workingCopy.setAttribute(EmeraldPlugin.EMERALD_LAUNCH_RUN_DISTRIBUTED,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_RUN_DISTRIBUTED);
                    workingCopy.setAttribute(EmeraldPlugin.EMERALD_LAUNCH_ROOT_HOST,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_ROOT_HOST);
                    workingCopy.setAttribute(EmeraldPlugin.EMERALD_LAUNCH_RUN_AS_ROOT_HOST,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_RUN_AS_ROOT_HOST);
                    workingCopy.setAttribute(EmeraldPlugin.EMERALD_LAUNCH_COMMAND_LINE_FLAGS,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_COMMAND_LINE_FLAGS);
                    ILaunchConfiguration configuration = workingCopy.doSave();
                    DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
                } else {
            		MessageDialog.openInformation(editor.getSite().getShell(),"Launch error","Can not launch this file as an Emerald program, as the file is not compiled or the executable ("+executableFileLocation+") is not available");
                }
            } else {
                MessageDialog.openInformation(editor.getSite().getShell(),"Launch error","Can not launch this editor as an Emerald program as it is not editing a real file\n\n File is null");
            }
        } catch (ClassCastException e) {
            MessageDialog.openInformation(editor.getSite().getShell(),"Launch error","Can not launch this editor as an Emerald program as it is not editing a real file\n\nCan not cast to IFileEditorInput");
        } catch (CoreException e) {
            MessageDialog.openInformation(editor.getSite().getShell(),"Launch error","Error launching\n\nCoreException");
            e.printStackTrace();
        }
        
        
                
        
    }
}
