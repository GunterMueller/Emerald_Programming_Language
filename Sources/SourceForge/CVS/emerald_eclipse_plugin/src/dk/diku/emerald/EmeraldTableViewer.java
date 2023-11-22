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
 * Created on Nov 19, 2004
 *
 */

package dk.diku.emerald;
import java.util.Arrays;
import java.util.Vector;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class EmeraldTableViewer {
private EmeraldMultipleTab tab;
private TextCellEditor textEditor;

/**
	 * @param parent
	 */
	public EmeraldTableViewer(Composite parent) {
	    hostList = new EmeraldHostList();
		this.addChildControls(parent);
	}

	public EmeraldTableViewer(Composite parent, Vector h, EmeraldMultipleTab tab) {
	    this.tab = tab;
	    hostList = new EmeraldHostList(h);
		this.addChildControls(parent);
	}

	public EmeraldTableViewer(Composite parent, EmeraldMultipleTab tab) {
	    this.tab = tab;
	    hostList = new EmeraldHostList();
		this.addChildControls(parent);
	}

	
	//	private Shell shell;
	private Table table;
	private TableViewer tableViewer;
	private Button closeButton;
	
	// Create a EmeraldHostList and assign it to an instance variable
	private EmeraldHostList hostList;// = new EmeraldHostList(); 

	// Set the table column property names
	private final String HOST_COLUMN 		= "Host";
	private final String USERNAME_COLUMN 	= "Username";
	private final String EMX_COLUMN 			= "emx";
	private final String ENABLED_COLUMN 		= "hostenabled";
	private final String ARGUMENTS_COLUMN 		= "arguments";
	private final String ROOTHOST_COLUMN 		= "roothost";
	private final String FILES_COLUMN 		= "files";
	private final String PRIORITY_COLUMN 		= "priority";
	private final String MONITOR_COLUMN 		= "monitor";

	// Set column names
	private String[] columnNames = new String[] { 
			HOST_COLUMN, 
			USERNAME_COLUMN,
			EMX_COLUMN,
			ENABLED_COLUMN,
			ARGUMENTS_COLUMN,
			ROOTHOST_COLUMN,
			FILES_COLUMN,
			PRIORITY_COLUMN,
			MONITOR_COLUMN
			};
    protected Object s;
    protected int direction;
    protected int sorter = EmeraldHostSorter.HOST;


	/**
	 * Run and wait for a close event
	 * @param shell Instance of Shell
	 */
	private void run(Shell shell) {
		
		// Add a listener for the close button
		closeButton.addSelectionListener(new SelectionAdapter() {
       	
			// Close the view i.e. dispose of the composite's parent
			public void widgetSelected(SelectionEvent e) {
				table.getParent().getParent().dispose();
			}
		});
		
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Release resources
	 */
	public void dispose() {
		
		// Tell the label provider to release its ressources
		tableViewer.getLabelProvider().dispose();
	}

	/**
	 * Create a new shell, add the widgets, open the shell
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite composite) {

		// Create a composite to hold the children
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData (gridData);

		// Set numColumns to 3 for the buttons 
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 4;
		composite.setLayout (layout);

		// Create the table 
		createTable(composite);
		
		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new EmeraldTableContentProvider());
		tableViewer.setLabelProvider(new EmeraldTableLabelProvider());
		// The input for the table viewer is the instance of EmeraldHostList
		//hostList = new EmeraldHostList();
		tableViewer.setInput(hostList);

		// Add the buttons
		createButtons(composite);
	}

	/**
	 * Create the Table
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		final int NUMBER_COLUMNS = EmeraldHost.getNumSettings();

		table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column with hostname
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);		
		column.setText("Host");
		column.setWidth(250);
		// Add listener to column so tasks are sorted by description when clicked 
		column.addSelectionListener(new SelectionAdapter() {
       	
			public void widgetSelected(SelectionEvent e) {
			    sorter = EmeraldHostSorter.HOST;
			    direction = -getDirection();
			    setSorter(sorter,direction);
			}
		});
		
		// 2nd column with username
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Username");
		column.setWidth(100);
		// Add listener to column so tasks are sorted by owner when clicked
		column.addSelectionListener(new SelectionAdapter() {
       	
			public void widgetSelected(SelectionEvent e) {
			    sorter = EmeraldHostSorter.USERNAME;
			    direction = -getDirection();
			    setSorter(sorter,direction);
			}
		});

		// 3rd column with path to emx
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Path to emx");
		column.setWidth(100);
		//  Add listener to column so tasks are sorted by percent when clicked
		column.addSelectionListener(new SelectionAdapter() {
       	
			public void widgetSelected(SelectionEvent e) {
			    sorter = EmeraldHostSorter.EMX;
			    direction = -getDirection();
			    setSorter(sorter,direction);
			}
		});

		// 4th column with enabled 
		column = new TableColumn(table, SWT.CENTER, 3);
		column.setText("Enabled");
		column.setWidth(25);
		column.pack();
		
		// 5rd column with arguments
		column = new TableColumn(table, SWT.LEFT, 4);
		column.setText("Arguments");
		column.setWidth(100);
		
		// 6th column with roothost 
		column = new TableColumn(table, SWT.CENTER, 5);
		column.setText("Roothost");
		column.setWidth(25);
		column.pack();
		
		// 7th column with files
		column = new TableColumn(table, SWT.CENTER, 6);
		column.setText("Files");
		column.setWidth(20);
		column.pack();
		
		//8th column with files
		column = new TableColumn(table, SWT.CENTER, 7);
		column.setText("Priority");
		column.setWidth(25);
		column.pack();
		column.addSelectionListener(new SelectionAdapter() {
	       	
				public void widgetSelected(SelectionEvent e) {
				    sorter = EmeraldHostSorter.PRIORITY;
				    direction = -getDirection();
				    setSorter(sorter,direction);
				}
			});

		
		// 9th column - enable monitor? 
		column = new TableColumn(table, SWT.CENTER, 8);
		column.setText("Monitor");
		column.setWidth(25);
		column.pack();
	
		}

	/**
	 * Create the TableViewer 
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		tableViewer.setColumnProperties(columnNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.length];


		// Column 0 : host
		textEditor = new TextCellEditor(table);
		//((Text) textEditor.getControl()).setTextLimit(60);
		editors[0] = textEditor;


		// Column 1 : username 
		textEditor = new TextCellEditor(table);
		//((Text) textEditor.getControl()).setTextLimit(60);
		editors[1] = textEditor;

		// Column 2 : emx
		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).addVerifyListener(
		
			new VerifyListener() {
				public void verifyText(VerifyEvent e) {
					// Here, we could use a RegExp such as the following 
					// if using JRE1.4 such as  e.doit = e.text.matches("[\\-0-9]*");
					//e.doit = "0123456789".indexOf(e.text) >= 0 ;
				}
			});
		((Text) textEditor.getControl()).addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e) {
                //hostList.hostChanged()
                tab.update();
                
            }});
	    
		editors[2] = textEditor;

		// Column 3 : enabled
		editors[3] = new CheckboxCellEditor(table);

		// Column 4 : arguments
		textEditor = new TextCellEditor(table);
		editors[4] = textEditor;

		CheckboxCellEditor rootHostEditor = new CheckboxCellEditor(table);
		editors[5] = rootHostEditor;
		
		EmeraldDialogCellEditor filesEditor = new EmeraldDialogCellEditor(table);
		//filesEditor.setValue(new File[0]);
		editors[6] = filesEditor;

		// Column 7 : priority
		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).addVerifyListener(
		
			new VerifyListener() {
				public void verifyText(VerifyEvent e) {
					// Here, we could use a RegExp such as the following 
					// if using JRE1.4 such as  e.doit = e.text.matches("[\\-0-9]*");
					//e.doit = "0123456789".indexOf(e.text) >= 0 ;
					e.doit = e.text.matches("[\\-0-9]*");
				}
			});
		((Text) textEditor.getControl()).addModifyListener(new ModifyListener(){

            public void modifyText(ModifyEvent e) {
             
             
                /*System.out.println("changed");
                try {
                    tab.performApply(tab.getLaunchConfiguration().getWorkingCopy());
                } catch (CoreException e1) {
                    e1.printStackTrace();
                }*/
                //tableViewer.getCellModifier().modify(((EmeraldTableCellModifier)tableViewer.getCellModifier()).getHost(),PRIORITY_COLUMN,((Text)e.getSource()).getText());
				//System.out.println("changed");
				
                
                //getHostList().hostChanged(host);
                
            }});
		editors[7] = textEditor;

		// Column 8 : monitor
		editors[8] = new CheckboxCellEditor(table);

		
		//hostList.getAsSortedLists();
		// Assign the cell editors to the viewer 
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new EmeraldTableCellModifier(this));
		// Set the default sorter for the viewer
		int s = EmeraldPlugin.getDefault().getPreferenceStore().getInt(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SORTER);
		int d = EmeraldPlugin.getDefault().getPreferenceStore().getInt(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_DIRECTION);
		setSorter(s,d); 
//		EmeraldPlugin.getDefault().getPreferenceStore().setValue(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SORTER,7);
//		EmeraldPlugin.getDefault().getPreferenceStore().getInt(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SORTER);
		
	}

	/*
	 * Close the window and dispose of resources
	 */
	public void close() {
		Shell shell = table.getShell();

		if (shell != null && !shell.isDisposed())
			shell.dispose();
	}


	/**
	 * InnerClass that acts as a proxy for the EmeraldHostList 
	 * providing content for the Table. It implements the IEmeraldHostListViewer 
	 * interface since it must register changeListeners with the 
	 * EmeraldHostList 
	 */
	class EmeraldTableContentProvider implements IStructuredContentProvider, IEmeraldHostListViewer {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null)
				((EmeraldHostList) newInput).addChangeListener(this);
			if (oldInput != null)
				((EmeraldHostList) oldInput).removeChangeListener(this);
		}

		public void dispose() {
		    if(hostList!=null)
		        hostList.removeChangeListener(this);
		}

		// Return the tasks as an array of Objects
		public Object[] getElements(Object parent) {
			return hostList.getHosts().toArray();
		}

		/* (non-Javadoc)
		 * @see IEmeraldHostListViewer#addTask(EmeraldHost)
		 */
		public void addHost(EmeraldHost host) {
			tableViewer.add(host);
		}

		/* (non-Javadoc)
		 * @see IEmeraldHostListViewer#removeTask(EmeraldHost)
		 */
		public void removeHost(EmeraldHost host) {
			tableViewer.remove(host);
		}

		/* (non-Javadoc)
		 * @see IEmeraldHostListViewer#updateTask(EmeraldHost)
		 */
		public void updateHost(EmeraldHost host) {
			tableViewer.update(host, null);
		}
	}
	
	/**
	 * Add the "Add", "Delete" and "Close" buttons
	 * @param parent the parent composite
	 */
	private void createButtons(Composite parent) {
		
		// Create and configure the "Add" button
		Button add = new Button(parent, SWT.PUSH | SWT.CENTER);
		add.setText("Add");
		
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		add.setLayoutData(gridData);
		add.addSelectionListener(new SelectionAdapter() {
       	
       		// Add a task to the EmeraldHostList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				hostList.addHost();
				tableViewer.refresh();
			}
		});

		//	Create and configure the "Delete" button
		Button delete = new Button(parent, SWT.PUSH | SWT.CENTER);
		delete.setText("Delete");
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.widthHint = 80; 
		delete.setLayoutData(gridData); 

		delete.addSelectionListener(new SelectionAdapter() {
       	
			//	Remove the selection and refresh the view
			public void widgetSelected(SelectionEvent e) {
				EmeraldHost host = (EmeraldHost) ((IStructuredSelection) 
						tableViewer.getSelection()).getFirstElement();
				if (host != null) {
					hostList.removeHost(host);
					tableViewer.refresh();
				} 				
			}
		});

	}

	/**
	 * Return the column names in a collection
	 * 
	 * @return List  containing column names
	 */
	public java.util.List getColumnNames() {
		return Arrays.asList(columnNames);
	}

	/**
	 * @return currently selected item
	 */
	public ISelection getSelection() {
		return tableViewer.getSelection();
	}

	/**
	 * Return the EmeraldHostList
	 */
	public EmeraldHostList getHostList() {
		return hostList;	
	}

    /**
     * @param hostList The hostList to set.
     */
    public void setHostList(EmeraldHostList taskList) {
        this.hostList = taskList;
        tableViewer.refresh();
    }
	/**
	 * Return the parent composite
	 */
	public Control getControl() {
		return table.getParent();
	}

	/**
	 * Return the 'close' Button
	 */
	public Button getCloseButton() {
		return closeButton;
	}
    /**
     * @return Returns the tableViewer.
     */
    public TableViewer getTableViewer() {
        return tableViewer;
    }
    /**
     * @return Returns the table.
     */
    public Table getTable() {
        return table;
    }
public EmeraldMultipleTab getTab() {
    return tab;
}
public void setTab(EmeraldMultipleTab tab) {
    this.tab = tab;
}
    public int getDirection() {
        return direction;
    }
    public void setDirection(int direction) {
//        this.direction = direction;
    
/*        if (tableViewer!=null) {
            tableViewer.setSorter(new EmeraldHostSorter(sorter, direction));
        }
*/ 
    }
    public int getSorter() {
        return sorter;
    }
    public void setSorter(int sorter, int direction) {
        
        EmeraldPlugin.getDefault().getPreferenceStore().setValue(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SORTER,sorter);
        EmeraldPlugin.getDefault().getPreferenceStore().setValue(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_DIRECTION,direction);
        if (tableViewer!=null) {
            tableViewer.setSorter(new EmeraldHostSorter(sorter, direction));
            this.sorter = sorter;
            this.direction = direction;
        }

    }
}