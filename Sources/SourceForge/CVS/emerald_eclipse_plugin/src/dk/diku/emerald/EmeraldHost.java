package dk.diku.emerald;
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
 *
 */

public class EmeraldHost {

	private Boolean enabled 	= new Boolean(true);
	private String host 	= "";
	private String username 		= "";
	private String emx = "";  
	private String arguments = "";
	private Boolean roothost = new Boolean(true);
	private String files = "";
	private Integer priority = new Integer(1);
	private Boolean monitor = new Boolean(true);
	
	private static final int numSettings = 9; 
    /**
     * @return Returns the arguments.
     */
    public String getArguments() {
        return arguments;
    }
    /**
     * @param arguments The arguments to set.
     */
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
	/**
	 * Create a host with an initial hostname
	 * 
	 * @param string
	 */
	public EmeraldHost(String string) {
		
		super();
		setHost(string);
	}

	/**
	 * @return true if enabled, false otherwise
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * @return String  host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return int enabled
	 * 
	 */
	public String getEmx() {
		return emx;
	}

	/**
	 * Set the 'enabled' property
	 * 
	 * @param b
	 */
	public void setEnabled(Boolean b) {
		enabled = b;
	}

	/**
	 * Set the 'host' property
	 * 
	 * @param string
	 */
	public void setHost(String string) {
		host = string;
	}

	/**
	 * Set the 'username' property
	 * 
	 * @param string
	 */
	public void setUsername(String string) {
		username = string;
	}

	/**
	 * Set the 'emx' property
	 * 
	 * @param i
	 */
	public void setEmx(String i) {
		emx = i;
	}

    /**
     * @return Returns the roothost.
     */
    public Boolean getRoothost() {
        return roothost;
    }
    /**
     * @param roothost The roothost to set.
     */
    public void setRoothost(Boolean roothost) {
        this.roothost = roothost;
    }
    /**
     * @return Returns the numSettings.
     */
    public static int getNumSettings() {
        return numSettings;
    }
    /**
     * @return Returns the files.
     */
    public String getFiles() {
        return files;
    }
    /**
     * @param files The files to set.
     */
    public void setFiles(String files) {
        this.files = files;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Boolean getMonitor() {
        return monitor;
    }
    public void setMonitor(Boolean monitor) {
        this.monitor = monitor;
    }
}
