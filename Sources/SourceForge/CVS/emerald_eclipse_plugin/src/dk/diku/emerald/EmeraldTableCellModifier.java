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
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifes a cell in the 
 * tableViewer
 */

public class EmeraldTableCellModifier implements ICellModifier {
	private EmeraldTableViewer emeraldTableViewer;
	private String[] columnNames;
    private EmeraldHost host;
	
	/**
	 * Constructor 
	 * @param EmeraldTableViewer an instance of a EmeraldTableViewer 
	 */
	public EmeraldTableCellModifier(EmeraldTableViewer emeraldTableViewer) {
		super();
		this.emeraldTableViewer = emeraldTableViewer;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {

		// Find the index of the column
		int columnIndex = emeraldTableViewer.getColumnNames().indexOf(property);

		Object result = null;
		EmeraldHost host = (EmeraldHost) element;
		this.host = host;
		
		//System.out.println(columnIndex);
		switch (columnIndex) {
			case 0 : // HOST_COLUMN 
				result = host.getHost();
				break;
			case 1 : // USERNAME_COLUMN 
				result = host.getUsername();
				break;
			case 2 : // EMX_COLUMN 
				result = host.getEmx() + "";
				break;
			case 3 : // ENABLED_COLUMN 
				result = host.getEnabled();
				break;
			case 4 : // ARGUMENTS_COLUMN 
				result = host.getArguments();
				break;
			case 5 : // ROOTHOST_COLUMN 
				result = host.getRoothost();
				break;
			case 6 : //FILES_COLUMN
			    result = host.getFiles();
			    break;
			case 7 : //PRIORITY_COLUMN
			    result = host.getPriority().toString();
			    break;
			case 8 : // MONITOR_COLUMN 
				result = host.getMonitor();
				break;
			default :
				result = "";
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	

		// Find the index of the column 
		int columnIndex	= emeraldTableViewer.getColumnNames().indexOf(property);
		TableItem item;
		EmeraldHost host;
		String valueString;
		if(element instanceof EmeraldHost){
		    host = (EmeraldHost) element;
		} else {  
		    item = (TableItem) element;
		    host = (EmeraldHost) item.getData();
		}
		//System.out.println(columnIndex);
		
		switch (columnIndex) {
			case 0 : //HOST_COLUMN 
				valueString = ((String) value).trim();
				host.setHost(valueString);
				break;
			case 1 : // USERNAME_COLUMN 
				valueString = ((String) value).trim();
				host.setUsername(valueString);
				break;
			case 2 : // EMX_COLUMN 
				valueString = ((String) value).trim();
				host.setEmx(valueString);
				break;
			case 3 : // ENABLED_COLUMN
			    host.setEnabled((Boolean) value);
			    break;
			case 4 : // ARGUMENTS_COLUMN 
				valueString = ((String) value).trim();
				host.setArguments(valueString);
				break;
			case 5 : // ROOTHOST_COLUMN
			    host.setRoothost((Boolean) value);
			    break;
			case 6 : //FILES_COLUMN
			    valueString = ((String) value).trim();
			    if(!valueString.equalsIgnoreCase("")){
			        host.setFiles(valueString);
			        
			        //System.out.println("setFiles:"+value);
			        //emeraldTableViewer.getTab().update();
			        //System.out.println("bum");
			    }
			    break;
			case 7 : // PRIORITY_COLUMN
//			    System.out.println("value:"+value);
			    host.setPriority(new Integer((String) value));
			    break;
			case 8 : // MONITOR_COLUMN
			    host.setMonitor((Boolean) value);
			    break;
			default :
			}
		emeraldTableViewer.getHostList().hostChanged(host);
		emeraldTableViewer.getTableViewer().refresh();
	}
    public EmeraldHost getHost() {
        return host;
    }
    public void setHost(EmeraldHost host) {
        this.host = host;
    }
}
