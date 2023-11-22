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
 * Created on Nov 30, 2004
 *
 */
package dk.diku.emerald;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;



/**
 * @author mb
 *
 */
public class EmeraldMultipleTab extends AbstractLaunchConfigurationTab {

    private Button sshFileSystemButton;
    private IFile configurationfile;
    private Composite comp;
    private String[] columnNames = {"Host","username","emx","Use?","Arguments","Root","Files","Priority","Monitor"};
    private Table table;
    //private TableViewer tableViewer;
    private EmeraldTableViewer emeraldTableViewer;
    private EmeraldHostList hostList;
    private Vector hosts;
    private ILaunchConfiguration launchConfiguration;
    private Label localSshLocationLabel;
    private Text localSshLocationText;
    private Font font;
    private Label localScpLocationLabel;
    private Text localScpLocationText;
    private Button scpFileSystemButton;
    private Label remoteWorkingDirectoryLabel;
    private Text remoteWorkingDirectoryText;
    private Button remoteWorkingDirectoryDefaultCheckbox;
    

    public EmeraldMultipleTab(){
        super();
    }
    
    
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        font = parent.getFont();
        
        comp = new Composite(parent, SWT.NONE);
        
        setControl(comp);
        GridLayout topLayout = new GridLayout();
        comp.setLayout(topLayout);		
        GridData gd;

        emeraldTableViewer = new EmeraldTableViewer(comp,this);
       
        createSeparator(comp,3);
        localSshLocationLabel = new Label(comp, SWT.NONE );
        localSshLocationLabel.setText("Location of local shh:"); 
        localSshLocationLabel.setFont(font);
        localSshLocationText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        localSshLocationText.setLayoutData(gd);
        localSshLocationText.setFont(font);
        localSshLocationText.addModifyListener(new ModifyListener(){
            
            public void modifyText(ModifyEvent e) {
                updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
        });
        sshFileSystemButton = createPushButton(comp, "browse...", null);
        sshFileSystemButton.addSelectionListener(new SelectionListener(){
            
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                
                dialog.setFileName(localSshLocationText.getText()); 
                String selectedDirectory = dialog.open();
                if (selectedDirectory != null) {
                    localSshLocationText.setText(selectedDirectory);
                }
                updateEnabledStates();
                updateLaunchConfigurationDialog();
                
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        

        createSeparator(comp,3);
        localScpLocationLabel = new Label(comp, SWT.NONE );
        localScpLocationLabel.setText("Location of local scp:"); 
        localScpLocationLabel.setFont(font);
        localScpLocationText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        localScpLocationText.setLayoutData(gd);
        localScpLocationText.setFont(font);
        localScpLocationText.addModifyListener(new ModifyListener(){
            
            public void modifyText(ModifyEvent e) {
                updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
        });
        scpFileSystemButton = createPushButton(comp, "browse...", null);
        scpFileSystemButton.addSelectionListener(new SelectionListener(){
            
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                
                dialog.setFileName(localScpLocationText.getText()); 
                String selectedDirectory = dialog.open();
                if (selectedDirectory != null) {
                    localScpLocationText.setText(selectedDirectory);
                }
                updateEnabledStates();
                updateLaunchConfigurationDialog();
                
            }
            
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        createSeparator(comp,3);
        remoteWorkingDirectoryLabel = new Label(comp, SWT.NONE );
        remoteWorkingDirectoryLabel.setText("Remote working directory (will be created in home directory):"); 
        remoteWorkingDirectoryLabel.setFont(font);
        remoteWorkingDirectoryText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        remoteWorkingDirectoryText.setLayoutData(gd);
        remoteWorkingDirectoryText.setFont(font);
        remoteWorkingDirectoryText.addModifyListener(new ModifyListener(){
            
            public void modifyText(ModifyEvent e) {
                updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
        });
        remoteWorkingDirectoryDefaultCheckbox = createCheckButton(comp,"Use default ("+EmeraldPlugin.DEFAULT_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY+"):");
        remoteWorkingDirectoryDefaultCheckbox.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                updateEnabledStates();
                updateLaunchConfigurationDialog();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        updateEnabledStates();
//        emeraldTableViewer = new EmeraldTableViewer(comp);
        //hostList = emeraldTableViewer.getHostList();
        //hosts = hostList.getHosts();
	
	}
      
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
        //ystem.out.println("Initialize from");
        this.launchConfiguration = configuration;
	    Vector confHosts;
        Vector confUsername;
        Vector confEmx;
        Vector confEnabled;
        Vector confArguments;
        Vector confRoothost;
        Vector confFiles;
        Vector confPriority;
        Vector confMonitor;
        Vector tmphosts = new Vector(2);	    
        try {
            List l =
                configuration.getAttribute(
                        EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_HOST,  new Vector(0));
            confHosts =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_USERNAME,  new Vector(0));
            confUsername =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_EMX,  new Vector(0));
            confEmx =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ENABLED,  new Vector(0));
            confEnabled =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ARGUMENTS,  new Vector(0));
            confArguments =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ROOTHOST,  new Vector(0));
            confRoothost =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_FILES,  new Vector(0));
            confFiles =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_PRIORITY,  new Vector(0));
            confPriority =new Vector(l);
            l = configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_MONITOR,  new Vector(0));
            confMonitor =new Vector(l);
            
            Iterator iterUsername = confUsername.iterator();
            Iterator iterEmx = confEmx.iterator();
            Iterator iterEnabled = confEnabled.iterator();
            Iterator iterArguments = confArguments.iterator();
            Iterator iterRoothost = confRoothost.iterator();
            Iterator iterFiles = confFiles.iterator();
            Iterator iterPriority = confPriority.iterator();
            Iterator iterMonitor = confMonitor.iterator();
            for (Iterator iterHosts = confHosts.iterator(); iterHosts.hasNext();) {
                String hoststring = getNextElement(iterHosts);
                String usernamestring = getNextElement(iterUsername);
                String emxstring = getNextElement(iterEmx);
                Boolean enabledstring = new Boolean(getNextElement(iterEnabled));
                String argumentsstring = getNextElement(iterArguments);
                Boolean roothoststring = new Boolean(getNextElement(iterRoothost));
                String filesstring = getNextElement(iterFiles);
                String p = getNextElement(iterPriority);
                Integer prioritystring = new Integer(p.matches("[\\-0-9]+")?p:"1");
                Boolean monitorstring = new Boolean(getNextElement(iterMonitor));
                
                EmeraldHost tmpTask = new EmeraldHost(hoststring);
                tmpTask.setUsername(usernamestring);
                tmpTask.setEmx(emxstring);
                tmpTask.setEnabled(enabledstring);
                tmpTask.setArguments(argumentsstring);
                tmpTask.setRoothost(roothoststring);
                tmpTask.setFiles(filesstring);
                tmpTask.setPriority(prioritystring);
                tmpTask.setMonitor(monitorstring);
                //System.out.println("Initialize: "+filesstring);
                tmphosts.add(tmpTask);
            }
            localSshLocationText.setText(configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SSH_LOCATION,  ""));
        
            localScpLocationText.setText(configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SCP_LOCATION,  ""));

            remoteWorkingDirectoryText.setText(configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY,  ""));
            
            remoteWorkingDirectoryDefaultCheckbox.setSelection(configuration.getAttribute(
                    EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY_DEFAULT_CHECK,  true));
            
            updateEnabledStates();
            
            emeraldTableViewer.setHostList(new EmeraldHostList(tmphosts));
            hostList = emeraldTableViewer.getHostList();
        } catch (CoreException e1) {
            e1.printStackTrace();
        }
        
        
        
        //emeraldTableViewer = new EmeraldTableViewer(comp,tmphosts);
        /*	    hostList = new EmeraldHostList(tmphosts);
         if(hostList==null)
         ystem.out.println("EmeraldMultipleTab.dong er null!");
	    //Vector d = hostList.getData();
	    TableViewer t = emeraldTableViewer.getTableViewer();
	    t.setInput(hostList);
*/	    //updateEnabledStates();
	    updateLaunchConfigurationDialog();
	    
	    
	    emeraldTableViewer.getTable().addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                //ystem.out.println("changed");
                updateLaunchConfigurationDialog();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                updateLaunchConfigurationDialog();
            }});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
	    //ystem.out.println("performApply");
	    hostList = emeraldTableViewer.getHostList();
	    //emeraldTableViewer.getTableViewer().refresh();
	    if(hostList!=null) {
	        Vector[] a = hostList.getAsSortedLists();
	        Vector tmphosts = a[0];
	        Vector username = a[1];
	        Vector emx = a[2];
	        Vector enabled = a[3];
	        Vector arguments = a[4];
	        Vector roothost = a[5];
	        Vector files = a[6];
	        Vector priority = a[7];
	        Vector monitor = a[8];
	   /*     for (Iterator iter = files.iterator(); iter.hasNext();) {
                String element = (String) iter.next();
                //System.out.println("Perform: "+element);
            }
	      */  
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_HOST,
	                tmphosts);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_USERNAME,
	                username);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_EMX,
	                emx);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ENABLED,
	                enabled);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ARGUMENTS,
	                arguments);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_ROOTHOST,
	                roothost);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_FILES,
	                files);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_PRIORITY,
	                priority);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_HOSTS_MONITOR,
	                monitor);
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SSH_LOCATION,
	                localSshLocationText.getText());
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SCP_LOCATION,
	                localScpLocationText.getText());
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY,
	                remoteWorkingDirectoryText.getText());
	        configuration.setAttribute(
	                EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY_DEFAULT_CHECK,
	                remoteWorkingDirectoryDefaultCheckbox.getSelection());
	    }
		updateEnabledStates();
	    this.launchConfiguration = configuration;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Run setup";
	}
	
    /**
     * 
     */
	private void updateEnabledStates() {
	    if(remoteWorkingDirectoryDefaultCheckbox != null) {
	        if(remoteWorkingDirectoryDefaultCheckbox.getSelection()){	
	            remoteWorkingDirectoryLabel.setEnabled(false);
	            remoteWorkingDirectoryText.setEnabled(false);
	        } else {
	            remoteWorkingDirectoryLabel.setEnabled(true);
	            remoteWorkingDirectoryText.setEnabled(true);
	        } 
	    }
    }



    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#deactivated(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void deactivated(ILaunchConfigurationWorkingCopy workingCopy) {
        super.deactivated(workingCopy);
    }
    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#activated(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
     */
    public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
        initializeFrom(workingCopy);
    }
    /* (non-Javadoc)
     * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setLaunchConfigurationDialog(org.eclipse.debug.ui.ILaunchConfigurationDialog)
     */
    public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
        super.setLaunchConfigurationDialog(dialog);
    }
    
    private String getNextElement(Iterator i){
        if (i.hasNext())
            return (String) i.next();
        else
            return "";
    }
     
    public void update(){
        updateLaunchConfigurationDialog();
    }
    public ILaunchConfiguration getLaunchConfiguration() {
        return launchConfiguration;
    }
    public void setLaunchConfiguration(ILaunchConfiguration configuration) {
        this.launchConfiguration = configuration;
    }
}
