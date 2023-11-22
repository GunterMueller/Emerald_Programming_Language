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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

/**
 * @author mb
 *
 */
public class EmeraldMultipleLaunchConfigurationDelegate extends
        LaunchConfigurationDelegate {

    private IPreferenceStore preferences;

	/**
     * 
     */
    public EmeraldMultipleLaunchConfigurationDelegate() {
        super();
		preferences = EmeraldPlugin.getDefault().getPreferenceStore();
    }

    /* (non-Javadoc)
     * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
     */
    public void launch(ILaunchConfiguration configuration, String mode,
            ILaunch launch, IProgressMonitor mon) throws CoreException {
        String localhost;
        String fileArgument;
        String commandLineFlagsArgument;
        
        if (mon == null) {
            mon = new NullProgressMonitor();
        }
        String[] environment = new String[] {
                "PATH="+DebugPlugin.getDefault().getLaunchManager().getNativeEnvironment().get("PATH")+ ":"+preferences.getString(EmeraldPlugin.EMERALD_PATH_PREFERENCE),
                "EMERALDROOT="+ preferences.getString(EmeraldPlugin.EMERALD_ROOT_PREFERENCE),
				"EMERALDARCH="+ preferences.getString(EmeraldPlugin.EMERALD_ARCH_PREFERENCE) };
   
        
        
        ArrayList hosts = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_HOST, new ArrayList());
        ArrayList usernames = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_USERNAME, new ArrayList());
        ArrayList emxs = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_EMX, new ArrayList());
        ArrayList enableds = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ENABLED, new ArrayList());
        ArrayList argumentss = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ARGUMENTS, new ArrayList());
        ArrayList roothosts = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ROOTHOST, new ArrayList());
        ArrayList files = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_FILES, new ArrayList());
        ArrayList priorities = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_PRIORITY, new ArrayList());
        ArrayList monitors = (ArrayList) configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_MONITOR, new ArrayList());
        String localssh = configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SSH_LOCATION, "");
        String localscp = configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SCP_LOCATION, "");
        String remoteworkingdir = configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY, "");
        boolean defaultremoteworkingdir = configuration.getAttribute(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY_DEFAULT_CHECK, true);
        if(defaultremoteworkingdir) {
            remoteworkingdir = EmeraldPlugin.DEFAULT_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY;
        }
        
        Iterator iter1 = usernames.iterator();
        Iterator iter2 = emxs.iterator();
        Iterator iter3 = enableds.iterator();
        Iterator iter4 = argumentss.iterator();
        Iterator iter5 = roothosts.iterator();
        Iterator iter6 = files.iterator();
        Iterator iter7 = priorities.iterator();
        Iterator iter8 = monitors.iterator();
        int i = 1;
        Integer lastPriority = new Integer(0);
        for (Iterator iter0 = hosts.iterator(); iter0.hasNext();) {
            String host = (String) iter0.next();
            String username= (String) iter1.next();
            String emx = (String) iter2.next();
            Boolean enabled = new Boolean((String) iter3.next());
            String arguments = (String) iter4.next();
            Boolean roothost = new Boolean((String) iter5.next());
            String file = (String) iter6.next();
            Integer priority = new Integer((String) iter7.next());
            Boolean monitor = new Boolean((String) iter8.next());
            
            if (enabled.booleanValue()){
                //Priority is interpreted as amount of seconds to wait before running command
                int waitingPeriod = priority.intValue() - lastPriority.intValue();
                try {
                    if(waitingPeriod>0) {
                        Thread.sleep(waitingPeriod*1000);
                    }
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                lastPriority = priority;

                if(host.equalsIgnoreCase("localhost")){
                    String[] commandLine = new String[] {  };
                    
                    commandLine = addCommandLineArgument(commandLine,emx);
                    commandLine = addCommandLineArgument(commandLine,"-U");
                    
                    if(roothost.booleanValue())
                        commandLine = addCommandLineArgument(commandLine,"-R");
                    String[] argumentsArray = arguments.split(" ");
                    for (int j = 0; j < argumentsArray.length; j++) {
                        if(!argumentsArray[j].equalsIgnoreCase("")) {
                            commandLine = addCommandLineArgument(commandLine,argumentsArray[j]);
                        }
                    }

                    Process process = DebugPlugin.exec(commandLine, null, environment);
                    
                    if (monitor.booleanValue()) {
                        IProcess p = DebugPlugin.newProcess(launch, process,"Host no. " + i + " : " + host + "(" + priority + ")");

                        //ProcessConsole pr = new ProcessConsole(p);
                        //pr.createPage( Workbench.getInstance().);

                        launch.addProcess(p);
                    }

                } else {

                    
                    String[] commandLine = new String[] {};
                    String[] s = file.split("result:");
                    String filestring = "";
                    if(s.length>1){
                        filestring = s[1];
                        String[] strs = filestring.split(":");
                        
                        for (int j = 0; j < strs.length; j++) {
                            commandLine = new String[] {};
                            String directory = strs[j].substring(0,strs[j].lastIndexOf("/"));
                            if (directory.indexOf("/")==0) {
                                directory = directory.substring(1);
                            }
                            directory = remoteworkingdir + "/" + directory;
                            
                            commandLine = addCommandLineArgument(commandLine,localssh);
                            //commandLine = addCommandLineArgument(commandLine,"-tt");
                            
                            commandLine = addCommandLineArgument(commandLine,username+"@"+host);
                            commandLine = addCommandLineArgument(commandLine,"ls");
                            commandLine = addCommandLineArgument(commandLine,directory);
                            //System.out.println(username+"@"+host+":"+directory);
                            //printCommandLine(commandLine);
                            Process process = DebugPlugin.exec(commandLine, null, environment);
                            try {
                                process.waitFor();
                                //System.out.println(process.exitValue());
                              
                                if (process.exitValue()>0){
                                    if(makeRemoteDirectory(directory,localssh,username,host)>0){
                                        Display.getDefault().asyncExec(new EmeraldErrorMessage("error on remote host "+username+"@"+host , "The directory \""+directory+"\" does not exist and it was not possible to create it. Please make sure that the directory exists in the home-directory on the remote host.") );
                                        return;
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //IProcess p = DebugPlugin.newProcess(launch, process, "checking directories "+j);
                            
                            //scp file to remote host
                            commandLine = new String[] {};
                            commandLine = addCommandLineArgument(commandLine,localscp);
                            commandLine = addCommandLineArgument(commandLine,ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() + "/"+ strs[j]);

                            commandLine = addCommandLineArgument(commandLine,username+"@"+host+":~/"+directory);
                            //System.out.println("copying the file "+ ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() + "/"+ strs[j] + " to remote host");
                            //commandLine = addCommandLineArgument(commandLine,";");
                            process = DebugPlugin.exec(commandLine, null, environment);
                            try {
                                process.waitFor();
                                //IProcess p = DebugPlugin.newProcess(launch, process, "Copying files "+j);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        
                        
                    }
                    commandLine = new String[] {};
                    //String[] commandLine = new String[] { "/usr/bin/ssh" };
                    commandLine = addCommandLineArgument(commandLine,localssh);
                    commandLine = addCommandLineArgument(commandLine,"-tt");

                    
                    commandLine = addCommandLineArgument(commandLine,username+"@"+host);
                    commandLine = addCommandLineArgument(commandLine,emx);
                    commandLine = addCommandLineArgument(commandLine,"-U");
                    if(roothost.booleanValue())
                        commandLine = addCommandLineArgument(commandLine,"-R");
                    String[] argumentsArray = arguments.split(" ");
                    for (int j = 0; j < argumentsArray.length; j++) {
                        if(!argumentsArray[j].equalsIgnoreCase("")) {
                            commandLine = addCommandLineArgument(commandLine,argumentsArray[j]);
                        }
                    }
                    String[] strs = filestring.split(":");
                    for (int j = 0; j < strs.length; j++) {
                        if(!strs[j].equalsIgnoreCase(""))
                            commandLine = addCommandLineArgument(commandLine, "~/"+remoteworkingdir+strs[j]);
                    }
                    //commandLine = addCommandLineArgument(commandLine,arguments);
                    
                    //printCommandLine(commandLine);
                    
                    Process process = DebugPlugin.exec(commandLine, null, environment);
                    
                    if(monitor.booleanValue()) {
                        IProcess p = DebugPlugin.newProcess(launch, process, "Host no. "+i+" : "+host+"("+priority+")");
                    
                        launch.addProcess(p);
                    }
                }
                i++;
            }
        }
 
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

    private void printCommandLine(String[] s){
        String out = "";
        for (int i = 0; i < s.length; i++) {
            out += s[i]+" ";
        }
        System.out.println(out);
    }

    //configuration.getFile();

	private int makeRemoteDirectory(String directory, String localssh, String username, String host) throws CoreException {
        String[] segments = directory.split("/");
        int returnValue = 0;
        String segment = "";
        
        for (int i = 0; i < segments.length; i++) {
            segment = segment + segments[i]+"/";            
            String[] commandLine = new String[] {};            
                       
            commandLine = addCommandLineArgument(commandLine,localssh);
            //commandLine = addCommandLineArgument(commandLine,"-tt");
            
            commandLine = addCommandLineArgument(commandLine,username+"@"+host);
            commandLine = addCommandLineArgument(commandLine,"ls");
            commandLine = addCommandLineArgument(commandLine,segment);
            //System.out.println(username+"@"+host+":"+directory);
            //printCommandLine(commandLine);
            Process process = DebugPlugin.exec(commandLine, null);
            try {
                process.waitFor();
                //System.out.println(process.exitValue());
              
                if (process.exitValue()>0){
                    commandLine = new String[] {};            
                    commandLine = addCommandLineArgument(commandLine,localssh);
                        
                    commandLine = addCommandLineArgument(commandLine,username+"@"+host);
                    commandLine = addCommandLineArgument(commandLine,"mkdir");
                    commandLine = addCommandLineArgument(commandLine,segment);
                    //ystem.out.println(username+"@"+host+":"+directory);
                    //printCommandLine(commandLine);
                    process = DebugPlugin.exec(commandLine, null);
                    try {
                        process.waitFor();
                        returnValue = process.exitValue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        returnValue = 1;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        return returnValue;
	}
	

}
