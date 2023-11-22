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


import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class EmeraldPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static EmeraldPlugin plugin;

	private ResourceBundle resourceBundle;

	public static final String EMERALD_COLOR_MULTI_LINE_COMMENT_PREFERENCE = "emeraldcolormultilinecomment";
	public static final String EMERALD_COLOR_SINGLE_LINE_COMMENT_PREFERENCE = "emeraldcolorsinglelinecomment";
	public static final String EMERALD_COLOR_KEYWORD_PREFERENCE = "emeraldcolorkeyword";
	public static final String EMERALD_COLOR_CONSTANT_PREFERENCE = "emeraldcolortype";
	public static final String EMERALD_COLOR_STRING_PREFERENCE = "emeraldcolorstring";
	public static final String EMERALD_COLOR_DEFAULT_PREFERENCE = "emeraldcolordefault";
    public static final String EMERALD_ROOT_PREFERENCE = "emeraldroot";
    public static final String EMERALD_ARCH_PREFERENCE = "emeraldarch";
    public static final String EMERALD_PATH_PREFERENCE = "emeraldpath";
    public static final String EMERALD_ECPATH_PREFERENCE = "emeraldecpath";
    public static final String EMERALD_EMXPATH_PREFERENCE = "emeraldemxpath";
    public static final String EMERALD_TAB_WIDTH_PREFERENCE = "emeraldlaunchfile";
    public static final String EMERALD_LAUNCH_RUN_DISTRIBUTED = "emeraldlaunchrundistributed";
    public static final String EMERALD_LAUNCH_RUN_AS_ROOT_HOST = "emeraldlaunchrunasroothost";
    public static final String EMERALD_LAUNCH_ROOT_HOST = "emeraldlaunchroothost";
    public static final String EMERALD_LAUNCH_FILE = "emeraldlaunchfile";
    public static final String EMERALD_LAUNCH_CONFIGURATION_TYPE= "dk.diku.emerald.EmeraldLaunchConfigurationType";
    public static final String EMERALD_BUILDER_NAME = "dk.diku.emerald.Builder";
    public static final String EMERALD_LAUNCH_COMMAND_LINE_FLAGS = "emeraldcommandlineflags";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS = "emeraldmultiplelaunchhosts";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_HOST = "emeraldmultiplelaunchhostshost";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_USERNAME = "emeraldmultiplelaunchhostsusername";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_EMX = "emeraldmultiplelaunchhostsemx";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_ENABLED = "emeraldmultiplelaunchhostsenabled";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_ARGUMENTS = "emeraldmultiplelaunchhostsarguments";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_ROOTHOST = "emeraldmultiplelaunchhostsroothost";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_FILES = "emeraldmultiplelaunchhostsfiles";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_PRIORITY = "emeraldmultiplelaunchhostspriority";
    public static final String EMERALD_MULTIPLE_LAUNCH_HOSTS_MONITOR = "emeraldmultiplelaunchhostsmonitor";
    public static final String EMERALD_MULTIPLE_LAUNCH_SSH_LOCATION = "emeraldmultiplelaunchsshlocation";
    public static final String EMERALD_MULTIPLE_LAUNCH_SCP_LOCATION = "emeraldmultiplelaunchscplocation";
    public static final String EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY = "emeraldmultiplelaunchremoteworkingdirectory";
    public static final String EMERALD_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY_DEFAULT_CHECK = "emeraldmultiplelaunchremoteworkingdirectorydefaultcheck";
    public static final String EMERALD_MULTIPLE_LAUNCH_DIRECTION = "emeraldmultiplelaunchdirection";
    public static final String EMERALD_MULTIPLE_LAUNCH_SORTER = "emeraldmultiplelaunchsorter";
    //TODO make sane defaults (requires emerald to install sanely)
    public static final RGB DEFAULT_EMERALD_COLOR_SINGLE_LINE_COMMENT = EmeraldColorProvider.SINGLE_LINE_COMMENT;
    public static final RGB DEFAULT_EMERALD_COLOR_KEYWORD = EmeraldColorProvider.KEYWORD;
    public static final RGB DEFAULT_EMERALD_COLOR_TYPE = EmeraldColorProvider.TYPE;
    public static final RGB DEFAULT_EMERALD_COLOR_STRING = EmeraldColorProvider.STRING;
    public static final RGB DEFAULT_EMERALD_COLOR_DEFAULT = EmeraldColorProvider.DEFAULT;    
    private static final String DEFAULT_EMERALD_ROOT = "/opt/emerald";
    private static final String DEFAULT_EMERALD_ARCH = "i686mt";
    private static final String DEFAULT_EMERALD_PATH = DEFAULT_EMERALD_ROOT+"/bin/:"+DEFAULT_EMERALD_ROOT+"/bin/"+DEFAULT_EMERALD_ARCH;
   
    private static final String DEFAULT_EMERALD_ECPATH = DEFAULT_EMERALD_ROOT+"/bin/ec";

    private static final String DEFAULT_EMERALD_EMXPATH = DEFAULT_EMERALD_ROOT+"/bin/"+DEFAULT_EMERALD_ARCH+"/emx";

    public static final boolean DEFAULT_EMERALD_LAUNCH_RUN_DISTRIBUTED = true;
    public static final boolean DEFAULT_EMERALD_LAUNCH_RUN_AS_ROOT_HOST = true;
    public static final String DEFAULT_EMERALD_LAUNCH_ROOT_HOST = "";
    public static final String DEFAULT_MULTIPLE_LAUNCH_REMOTE_WORKING_DIRECTORY = "emerald_files";

    public static final String DEFAULT_EMERALD_LAUNCH_FILE = "";
    public static final String DEFAULT_EMERALD_LAUNCH_COMMAND_LINE_FLAGS = "";
    public static final String[] EMERALD_LAUNCH_COLUMN_NAMES = new String[]{"host","username","emx","use?"};
    
    public static final int DEFAULT_EMERALD_TAB_WIDTH = 4;
    private IPreferenceStore store;

	//TODO Is there multiline comments?
    //TODO Add keyboard-shortcuts to labels (add &'s in label names)
    //TODO Make launch with an empty file work
    //TODO Use IOConsole if available
    //TODO Add error checking at calling ec/emx
    //TODO ' strings
    //TODO check on exitValue everywhere we call exec.
    //TODO implement "ask to start next host" capability.
    //TODO distibute a new release

    /**
	 * The constructor.
	 */
	public EmeraldPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("dk.diku.emerald.EmeraldPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		store = plugin.getPreferenceStore();
		initializeDefaultPreferences(store);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static EmeraldPlugin getDefault() {
	    return plugin;
	}
	
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
	    ResourceBundle bundle = EmeraldPlugin.getDefault().getResourceBundle();
	    try {
	        return (bundle != null) ? bundle.getString(key) : key;
	    } catch (MissingResourceException e) {
	        return key;
	    }
	}
	
	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
	    return resourceBundle;
	}
	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
	    store.setDefault(EMERALD_ROOT_PREFERENCE, DEFAULT_EMERALD_ROOT);
	    store.setDefault(EMERALD_ARCH_PREFERENCE, DEFAULT_EMERALD_ARCH);
	    store.setDefault(EMERALD_PATH_PREFERENCE, DEFAULT_EMERALD_PATH);
	    store.setDefault(EMERALD_ECPATH_PREFERENCE, DEFAULT_EMERALD_ECPATH);
	    store.setDefault(EMERALD_EMXPATH_PREFERENCE, DEFAULT_EMERALD_EMXPATH);
	    store.setDefault(EMERALD_TAB_WIDTH_PREFERENCE, DEFAULT_EMERALD_TAB_WIDTH);
	    store.setDefault(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_DIRECTION,1);
	    store.setDefault(EmeraldPlugin.EMERALD_MULTIPLE_LAUNCH_SORTER,0);
		PreferenceConverter.setDefault(store,  EMERALD_COLOR_SINGLE_LINE_COMMENT_PREFERENCE, DEFAULT_EMERALD_COLOR_SINGLE_LINE_COMMENT);
		PreferenceConverter.setDefault(store,  EMERALD_COLOR_KEYWORD_PREFERENCE, DEFAULT_EMERALD_COLOR_KEYWORD);
		PreferenceConverter.setDefault(store,  EMERALD_COLOR_CONSTANT_PREFERENCE, DEFAULT_EMERALD_COLOR_TYPE);
		PreferenceConverter.setDefault(store,  EMERALD_COLOR_STRING_PREFERENCE, DEFAULT_EMERALD_COLOR_STRING);
		PreferenceConverter.setDefault(store,  EMERALD_COLOR_DEFAULT_PREFERENCE, DEFAULT_EMERALD_COLOR_DEFAULT);
	}
}
