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
 */
 package dk.diku.emerald;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;


public class EmeraldNewProjectWizardPage extends WizardPage {
	private Text projectText;
    private boolean useDefaults = true;
    private Label locationLabel;
    private Text locationPathField;
    private Button browseButton;
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
    protected String customLocationFieldValue;
    private IPath initialLocationFieldValue;

	/**
	 * Constructor for SampleNewWizardPage.
	 * @param pageName
	 */
	public EmeraldNewProjectWizardPage(String title) {
		super(title);
		setTitle("Emerald Project");
		setDescription("This wizard creates a new Emerald project with an Emerald compiler associated with it");
//		this.selection = selection;
		initialLocationFieldValue = Platform.getLocation().append("/"+getProjectName());
		customLocationFieldValue = initialLocationFieldValue.toOSString();
		
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Label label = new Label(container, SWT.NULL);
		label.setText("&Project name:");
		
		projectText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectText.setLayoutData(gd);
		projectText.addListener(SWT.Modify, modificationListener);
		projectText.addListener(SWT.Modify,new Listener() {
		    public void handleEvent(Event e) {
		        useDefaults = useDefaultsButton.getSelection();
		        if (useDefaults) {
		            setLocationForSelection();
		        } else {
		            locationPathField.setText(customLocationFieldValue);
		        }
		        
		    }
		});
		createProjectLocationGroup(container);
		initialize();
		setPageComplete(validatePage());
		setControl(container);
		
	}
	
	/**
	 * Creates the project location specification controls.
	 *
	 * @param parent the parent composite
	 */
	private final void createProjectLocationGroup(Composite parent) {
		Font font = parent.getFont();
		// project specification group
		Composite projectGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		projectGroup.setLayout(layout);
		projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectGroup.setFont(font);
		
		// new project label
		Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
		projectContentsLabel.setFont(font);
		projectContentsLabel.setText("Project Contents Messages");

		GridData labelData = new GridData();
		labelData.horizontalSpan = 3;
		projectContentsLabel.setLayoutData(labelData);

		useDefaultsButton = new Button(projectGroup, SWT.CHECK | SWT.RIGHT);
        useDefaultsButton.setText("&Use Default");
		useDefaultsButton.setSelection(useDefaults = true);
		useDefaultsButton.setFont(font);

		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 3;
		useDefaultsButton.setLayoutData(buttonData);

		createUserSpecifiedProjectLocationGroup(projectGroup, !useDefaults);

		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				useDefaults = useDefaultsButton.getSelection();
				browseButton.setEnabled(!useDefaults);
				locationPathField.setEnabled(!useDefaults);
				locationLabel.setEnabled(!useDefaults);
				if (useDefaults) {
					customLocationFieldValue = locationPathField.getText();
					setLocationForSelection();
				} else {
					locationPathField.setText(customLocationFieldValue);
				}
			}
		};
		useDefaultsButton.addSelectionListener(listener);
		
	}
	
	/**
	 * Creates the project location specification controls.
	 *
	 * @param projectGroup the parent composite
	 * @param boolean - the initial enabled state of the widgets created
	 */
	private void createUserSpecifiedProjectLocationGroup(Composite projectGroup, boolean enabled) {
		Font font = projectGroup.getFont();
		// location label
		locationLabel = new Label(projectGroup, SWT.NONE);
		locationLabel.setFont(font);
		locationLabel.setText("Director&y"); //$NON-NLS-1$
		locationLabel.setEnabled(enabled);

		// project location entry field
		locationPathField = new Text(projectGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		locationPathField.setLayoutData(data);
		locationPathField.setFont(font);
		locationPathField.setEnabled(enabled);

		// browse button
		browseButton = new Button(projectGroup, SWT.PUSH);
		browseButton.setFont(font);
		browseButton.setText("B&rowse..."); //$NON-NLS-1$
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				handleLocationBrowseButtonPressed();
			}
		});

		browseButton.setEnabled(enabled);

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (initialLocationFieldValue != null)
			locationPathField.setText(initialLocationFieldValue.toOSString());
		locationPathField.addListener(SWT.Modify, modificationListener);
	}
	
	private void setLocationForSelection() {
		if (useDefaults) {
			IPath defaultPath = Platform.getLocation().append(getProjectName());
			locationPathField.setText(defaultPath.toOSString());
		}
	}
	
	/**
	 *	Open an appropriate directory browser
	 */
	private void handleLocationBrowseButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(locationPathField.getShell());
		dialog.setMessage("Select the project contents directory."); //$NON-NLS-1$

		String dirName = getProjectLocation();
		if (!dirName.equals("")) { //$NON-NLS-1$
			File path = new File(dirName);
			if (path.exists())
				dialog.setFilterPath(new Path(dirName).toOSString());
		}

		String selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			customLocationFieldValue = selectedDirectory;
			locationPathField.setText(customLocationFieldValue);
		}
	}

	private Listener modificationListener = new Listener() {
		public void handleEvent(Event e) {
			setPageComplete(validatePage());
		}
	};
    private Button useDefaultsButton;

	private boolean validatePage() {
		IWorkspace workspace = IDEWorkbenchPlugin.getPluginWorkspace();

		String projectName = getProjectName();
		if (projectName.length() == 0) {
			setErrorMessage("Project name name must be specified");
			return false;
		}
		
		IStatus nameStatus = workspace.validateName(projectName, IResource.PROJECT);
		if (!nameStatus.isOK()) {
			setErrorMessage(nameStatus.getMessage());
			return false;
		}

		String locationFieldContents = getProjectLocation();

		if (locationFieldContents.equals("")) { //$NON-NLS-1$
			setErrorMessage(null);
			setMessage("Project contents directory must be specified"); //$NON-NLS-1$
			return false;
		}

		IPath path = new Path(""); //$NON-NLS-1$
		if (!path.isValidPath(locationFieldContents)) {
			setErrorMessage("Invalid project contents directory"); //$NON-NLS-1$
			return false;
		}
		if (!useDefaults && Platform.getLocation().isPrefixOf(new Path(locationFieldContents))) {
			setErrorMessage("Project contents cannot be inside workspace directory"); //$NON-NLS-1$
			return false;
		}

		if (getProjectHandle().exists()) {
			setErrorMessage("A project with that name already exists in the workspace"); //$NON-NLS-1$
			return false;
		}

		if (isExistingProjectLocation()) {
			setErrorMessage("Another project exist at the specified content directory"); //$NON-NLS-1$
			return false;
		}
		
		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
	}
	
	private boolean isExistingProjectLocation() {
		IPath path = getLocationPath();
		path = path.append(IProjectDescription.DESCRIPTION_FILE_NAME);
		return path.toFile().exists();
	}

	IPath getLocationPath() {
			if (useDefaults)
				return initialLocationFieldValue;

			return new Path(getProjectLocation());
		}
		

	private String getProjectLocation() {
		if (locationPathField == null)
			return ""; //$NON-NLS-1$
		else
			return locationPathField.getText().trim();
	}
	
	
	private void initialize() {
		projectText.setText("new_project");
	}
	
	public String getProjectName() {
	    if (projectText == null)
			return ""; 
		else
		    return projectText.getText().trim();
	}
	
	protected GridLayout initGridLayout(GridLayout layout, boolean margins) {
		layout.horizontalSpacing= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		if (margins) {
			layout.marginWidth= convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
			layout.marginHeight= convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		} else {
			layout.marginWidth= 0;
			layout.marginHeight= 0;
		}
		return layout;
	}
	
}