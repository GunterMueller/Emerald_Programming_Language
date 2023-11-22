/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
 *    IBM Corporation
?*******************************************************************************/
package dk.diku.emerald.dependencies;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The plugin class (singleton).
 * <p>
 * This instance can be shared between all extensions in the plugin. Information
 * shared between extensions can be persisted by using the PreferenceStore.
 * </p>
 * @see org.eclipse.ui.plugin.AbstractUIPlugin#getPreferenceStore()
 * @author Elias Volanakis
 */
public class DependencyFilesPlugin extends AbstractUIPlugin {

/** Single plugin instance. */
private static DependencyFilesPlugin singleton;
//Resource bundle.
private ResourceBundle resourceBundle;
/** Names of XML elements and attributes*/
public static final String XML_EMERALDDEPENDENCYDIAGRAM = "emeraldDependencyDiagram";
public static final String XML_EMERALDFILE = "emeraldFile";
public static final String XML_EMERALDFILE_NAME = "name";
public static final String XML_EMERALDFILE_X = "x";
public static final String XML_EMERALDFILE_Y = "y";
public static final String XML_DEPENDS = "depends";
public static final String XML_DEPENDS_NAME = "name";
public static final String PluginId = "dk.diku.emerald.dependencies";
public static final String DEPENDENCY_FILE_NAME = "/dependency.depend";

/**
 * Returns the shared plugin instance.
 */
public static DependencyFilesPlugin getDefault() {
	return singleton;
}

/** 
 * The constructor. 
 */
public DependencyFilesPlugin() {
	if (singleton == null) {
		singleton = this;
	}
}

/**
 * This method is called upon plug-in activation
 */
public void start(BundleContext context) throws Exception {
	super.start(context);
}

/**
 * This method is called when the plug-in is stopped
 */
public void stop(BundleContext context) throws Exception {
	super.stop(context);
	singleton = null; 
	resourceBundle = null;
}

/**
 * Returns the string from the plugin's resource bundle,
 * or 'key' if not found.
 */
public static String getResourceString(String key) {
	ResourceBundle bundle = DependencyFilesPlugin.getDefault().getResourceBundle();
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
	try {
		if (resourceBundle == null)
			resourceBundle = ResourceBundle.getBundle("dk.diku.emerald.dependencies");
	} catch (MissingResourceException x) {
		resourceBundle = null;
	}
	return resourceBundle;
}

/**
 * Returns an image descriptor for the image file at the given
 * plug-in relative path.
 *
 * @param path the path
 * @return the image descriptor
 */
public static ImageDescriptor getImageDescriptor(String path) {
	return AbstractUIPlugin.imageDescriptorFromPlugin("dk.diku.emerald.dependencies", path);
}

}