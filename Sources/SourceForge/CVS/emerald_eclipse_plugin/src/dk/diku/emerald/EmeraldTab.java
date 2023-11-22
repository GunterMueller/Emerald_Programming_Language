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
import org.eclipse.swt.widgets.Text;

/**
 * @author mb
 *
 */
public class EmeraldTab extends AbstractLaunchConfigurationTab {

	private Button isDistributed;
    private Button isRootHost;
    private Label rootHostLabel;
    private Text rootHostText;
    private Button FileSystemButton;
    private Label launchFileLabel;
    private Text launchFileText;
    private IFile configurationfile;
    private Label commandLineFlagsLabel;
    private Text commandLineFlagsText;


    public EmeraldTab(){
        super();
    }
    
    
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent) {
        Font font = parent.getFont();
        
        Composite comp = new Composite(parent, SWT.NONE);
        setControl(comp);
        GridLayout topLayout = new GridLayout();
        comp.setLayout(topLayout);		
        GridData gd;
        
        
        isDistributed = createCheckButton(comp,"Run distributed");
        isDistributed.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                updateEnabledStates();
                updateLaunchConfigurationDialog();                	
            }


            public void widgetDefaultSelected(SelectionEvent e) {
            }});
		
		
		rootHostLabel = new Label(comp, SWT.NONE);
		rootHostLabel.setText("Root host:"); 
		rootHostLabel.setFont(font);
		rootHostText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		rootHostText.setLayoutData(gd);
		rootHostText.setFont(font);
		rootHostText.addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e) {
		        updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
		    });
		
		isRootHost = createCheckButton(comp, "Run as Root Host");
		isRootHost.addSelectionListener(new SelectionListener(){            
		    public void widgetSelected(SelectionEvent e) {
		        updateEnabledStates();
                updateLaunchConfigurationDialog();
		    }
		    
		    public void widgetDefaultSelected(SelectionEvent e) {
		    }});
		
		launchFileLabel = new Label(comp, SWT.NONE);
		launchFileLabel.setText("File to launch:"); 
		launchFileLabel.setFont(font);
		launchFileText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		launchFileText.setLayoutData(gd);
		launchFileText.setFont(font);
		launchFileText.addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e) {
		        updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
		    });

		
		FileSystemButton = createPushButton(comp, "browse...", null);
		FileSystemButton.addSelectionListener(new SelectionListener(){

		    public void widgetSelected(SelectionEvent e) {
		        FileDialog dialog = new FileDialog(getShell());
		        
		        dialog.setFileName(launchFileText.getText()); 
		        String selectedDirectory = dialog.open();
		        if (selectedDirectory != null) {
		            launchFileText.setText(selectedDirectory);
		        }
		        updateEnabledStates();
		        updateLaunchConfigurationDialog();
		        
		    }
		    
		    public void widgetDefaultSelected(SelectionEvent e) {
            }
		    });
		
		commandLineFlagsLabel = new Label(comp, SWT.NONE);
		commandLineFlagsLabel.setText("Command Line flags (will be passed to emx):"); 
		commandLineFlagsLabel.setFont(font);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		commandLineFlagsText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		commandLineFlagsText.setLayoutData(gd);
		commandLineFlagsText.setFont(font);
		commandLineFlagsText.addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e) {
		        updateEnabledStates();
                updateLaunchConfigurationDialog();
            }
		    });
		
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
	    try {
	        isDistributed.setSelection(configuration.getAttribute(
	                EmeraldPlugin.EMERALD_LAUNCH_RUN_DISTRIBUTED,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_RUN_DISTRIBUTED  ));
	        isRootHost.setSelection(configuration.getAttribute(
	                EmeraldPlugin.EMERALD_LAUNCH_RUN_AS_ROOT_HOST,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_RUN_AS_ROOT_HOST ));
	        rootHostText.setText(configuration.getAttribute(
	                EmeraldPlugin.EMERALD_LAUNCH_ROOT_HOST,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_ROOT_HOST));
	        launchFileText.setText(configuration.getAttribute(
	                EmeraldPlugin.EMERALD_LAUNCH_FILE,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_FILE));
	        commandLineFlagsText.setText(configuration.getAttribute(
	                EmeraldPlugin.EMERALD_LAUNCH_COMMAND_LINE_FLAGS,EmeraldPlugin.DEFAULT_EMERALD_LAUNCH_COMMAND_LINE_FLAGS));
	    } catch (CoreException e) {
	        e.printStackTrace();
	    }
	    updateEnabledStates();
	    updateLaunchConfigurationDialog();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(
		 	   EmeraldPlugin.EMERALD_LAUNCH_RUN_DISTRIBUTED,
		 	   isDistributed.getSelection());
		configuration.setAttribute(
			 	   EmeraldPlugin.EMERALD_LAUNCH_RUN_AS_ROOT_HOST,
			 	   isRootHost.getSelection());
		configuration.setAttribute(
			 	   EmeraldPlugin.EMERALD_LAUNCH_ROOT_HOST,
			 	   rootHostText.getText());
		configuration.setAttribute(
			 	   EmeraldPlugin.EMERALD_LAUNCH_FILE,
			 	   launchFileText.getText());
		configuration.setAttribute(
			 	   EmeraldPlugin.EMERALD_LAUNCH_COMMAND_LINE_FLAGS,
			 	   commandLineFlagsText.getText());
		
		updateEnabledStates();
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
        if(isDistributed.getSelection()){	
            isRootHost.setEnabled(true);
    	    if (isRootHost.getSelection() ) {
                rootHostText.setEnabled(false);
                rootHostLabel.setEnabled(false);
            } else { 
                rootHostText.setEnabled(true);
                rootHostLabel.setEnabled(true);
            }
        } else {
            isRootHost.setSelection(false);
            isRootHost.setEnabled(false);
    	    rootHostText.setEnabled(false);
    	    rootHostLabel.setEnabled(false);
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
}
