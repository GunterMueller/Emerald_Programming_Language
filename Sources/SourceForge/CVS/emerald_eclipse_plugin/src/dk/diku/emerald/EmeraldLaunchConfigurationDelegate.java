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
 * Created on Nov 22, 2004
 *
 */
package dk.diku.emerald;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * @author mb
 *
 */
public class EmeraldLaunchConfigurationDelegate extends
        LaunchConfigurationDelegate {

    private IPreferenceStore preferences;

	/**
     * 
     */
    public EmeraldLaunchConfigurationDelegate() {
        super();
		preferences = EmeraldPlugin.getDefault().getPreferenceStore();
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
     */
    public void launch(ILaunchConfiguration configuration, String mode,
            ILaunch launch, IProgressMonitor monitor) throws CoreException {
        String localhost;
        String fileArgument;
        String commandLineFlagsArgument;
        
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
   
        String[] commandLine = new String[] { preferences.getString(EmeraldPlugin.EMERALD_EMXPATH_PREFERENCE)};
        commandLine = addCommandLineArgument(commandLine,"-U");
        
        if (configuration.getAttribute(EmeraldPlugin.EMERALD_LAUNCH_RUN_DISTRIBUTED, false) 
                && !(localhost = configuration.getAttribute(EmeraldPlugin.EMERALD_LAUNCH_ROOT_HOST, "")).equalsIgnoreCase("")) {
            //ystem.out.println("Distributed");
            commandLine = addCommandLineArgument(commandLine, "-R"+localhost);
        }
        if (configuration.getAttribute(EmeraldPlugin.EMERALD_LAUNCH_RUN_AS_ROOT_HOST, false)) {
            commandLine = addCommandLineArgument(commandLine, "-R");
            //ystem.out.println("RootHost");
        }
        
        if((fileArgument = configuration.getAttribute(EmeraldPlugin.EMERALD_LAUNCH_FILE, ""))!="") {
            commandLine = addCommandLineArgument(commandLine, fileArgument); 
        }
        
        if((commandLineFlagsArgument = configuration.getAttribute(EmeraldPlugin.EMERALD_LAUNCH_COMMAND_LINE_FLAGS, ""))!="") {
            String[] argumentsArray = commandLineFlagsArgument.split(" ");
            for (int j = 0; j < argumentsArray.length; j++) {
                if(!argumentsArray[j].equalsIgnoreCase("")) {
                    commandLine = addCommandLineArgument(commandLine,argumentsArray[j]);
                }
            }
        }


        //"PATH=/bin:/usr/bin:/usr/local/bin:/opt/bin:/usr/i686-pc-linux-gnu/gcc-bin/3.3:/usr/X11R6/bin:/opt/sun-jdk-1.4.2.06/bin:/opt/sun-jdk-1.4.2.06/jre/bin:/opt/sun-jdk-1.4.2.06/jre/javaws:/usr/qt/3/bin:/usr/kde/3.3/bin:/home/mb/bin:"+ preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE),
        String[] environment = new String[] {
                //ystem.out.println(DebugPlugin.getDefault().getLaunchManager().getNativeEnvironment().get("PATH"));
                //"PATH="+preferences.getString(EmeraldPlugin.EMERALD_SYSTEMS_PATH_PREFERENCE)+ preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE),
                "PATH="+DebugPlugin.getDefault().getLaunchManager().getNativeEnvironment().get("PATH")+ ":"+
                                 preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE),
				"EMERALDROOT="+ preferences.getString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE),
				"EMERALDARCH="+ preferences.getString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE) };
 
        Process process = DebugPlugin.exec(commandLine, null, environment);
        IProcess p = DebugPlugin.newProcess(launch, process, "");
        /*
        String out = "";        
        for (int i = 0; i < commandLine.length; i++) {
            out += commandLine[i]+" ";
        }
        ystem.out.println(out);

        
        String compilerError = p.getStreamsProxy().getErrorStreamMonitor().getContents();
        String compilerOutput = p.getStreamsProxy().getOutputStreamMonitor().getContents();
        
        if(compilerOutput !=null)
            ystem.out.println("compilerOutput:"+compilerOutput);
        else
            ystem.out.println("null in");
        if(compilerError !=null)
            ystem.out.println("compilerError:"+compilerError);
        else
            ystem.out.println("null err");
        //printResultInConsole(compilerOutput);
         * 
         */
        
        launch.addProcess(p);
    }
    /**
     * @param commandLine
     * @param fileArgument
     * @return
     */
    private String[] addCommandLineArgument(String[] commandLine, String fileArgument) {
        String[] newCommandLine = new String[commandLine.length+1];
        for (int i = 0; i < commandLine.length; i++) {
            newCommandLine[i] = commandLine[i]; 
        }
        newCommandLine[newCommandLine.length-1] = fileArgument;
        return newCommandLine;
    }

    //configuration.getFile();
    
}
