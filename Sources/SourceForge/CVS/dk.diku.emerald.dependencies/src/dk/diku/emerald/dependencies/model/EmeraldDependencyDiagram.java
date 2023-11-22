/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *????Elias Volanakis - initial API and implementation
 *    IBM Corporation
 *******************************************************************************/
package dk.diku.emerald.dependencies.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.crimson.jaxp.DocumentBuilderFactoryImpl;
import org.apache.crimson.tree.XmlDocument;
import org.apache.crimson.tree.XmlDocumentBuilder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dk.diku.emerald.dependencies.DependencyFilesPlugin;


/**
 * A container for multiple dependencyFiles.
 * This is the "root" of the model data structure.
 * @author Elias Volanakis
 */
public class EmeraldDependencyDiagram extends ModelElement {

/** Property ID to use when a child is added to this diagram. */
public static final String CHILD_ADDED_PROP = "EmeraldDependencyDiagram.ChildAdded";
/** Property ID to use when a child is removed from this diagram. */
public static final String CHILD_REMOVED_PROP = "EmeraldDependencyDiagram.ChildRemoved";
private static final long serialVersionUID = 1;
private List dependencyFiles = new ArrayList();

public EmeraldDependencyDiagram() {
}

public EmeraldDependencyDiagram(String filename) {
	try {
		Document d = new DocumentBuilderFactoryImpl().newDocumentBuilder().parse(filename);
		
//		XmlDocument d = XmlDocument.createXmlDocument(filename);
		
		NodeList files = d.getElementsByTagName(DependencyFilesPlugin.XML_EMERALDFILE);
		Node file;
		for(int i = 0;(file=files.item(i))!=null;i++) {
			EmeraldDependencyFile newfile = new EmeraldDependencyFile(file.getAttributes().getNamedItem(DependencyFilesPlugin.XML_DEPENDS_NAME).getNodeValue());
			try {
				int x = Integer.parseInt(file.getAttributes().getNamedItem(DependencyFilesPlugin.XML_EMERALDFILE_X).getNodeValue());
				int y = Integer.parseInt(file.getAttributes().getNamedItem(DependencyFilesPlugin.XML_EMERALDFILE_Y).getNodeValue());
				newfile.setLocation(new Point(x,y));
			} catch (Exception e) {
			}
			addChild(newfile);
		}
		
		for(int i = 0;(file=files.item(i))!=null;i++) {
			EmeraldDependencyFile originFile = getNamedEmeraldDependencyFile(file.getAttributes().getNamedItem(DependencyFilesPlugin.XML_DEPENDS_NAME).getNodeValue());
			NodeList depends = file.getChildNodes();
			Node depend;
			for(int j = 0;(depend=depends.item(j))!=null;j++) {
				if (depend.getNodeName().equalsIgnoreCase(DependencyFilesPlugin.XML_DEPENDS)) {
					EmeraldDependencyFile targetFile = getNamedEmeraldDependencyFile(depend.getAttributes().getNamedItem(DependencyFilesPlugin.XML_DEPENDS_NAME).getNodeValue());
					if (targetFile != null) {
						new Dependency(originFile,targetFile);
					}
				}
			}
		}
	} catch (IOException e1) {
		e1.printStackTrace();
	} catch (SAXException e1) {
		e1.printStackTrace();
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	}

}

public void export(String exportfilename) {
	try {
		XmlDocument document = createDocument();
		document.write(new FileOutputStream(exportfilename));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

public XmlDocument createDocument() {
	XmlDocumentBuilder builder = new XmlDocumentBuilder();
	XmlDocument document = builder.createDocument();
	Element maindocumentelement = document.createElement(DependencyFilesPlugin.XML_EMERALDDEPENDENCYDIAGRAM);
	document.appendChild(maindocumentelement);
	
	Object f;
	Element el;
	for(Iterator i = dependencyFiles.iterator();(i.hasNext()?(f = i.next())!=null:false);) {
		if(f instanceof EmeraldDependencyFile) {
			EmeraldDependencyFile file = (EmeraldDependencyFile) f;
			el = document.createElement(DependencyFilesPlugin.XML_EMERALDFILE);
			el.setAttribute(DependencyFilesPlugin.XML_EMERALDFILE_NAME,file.getFile());
			el.setAttribute(DependencyFilesPlugin.XML_EMERALDFILE_X,String.valueOf(file.getLocation().x));
			el.setAttribute(DependencyFilesPlugin.XML_EMERALDFILE_Y,String.valueOf(file.getLocation().y));
			Object conn;
			for(Iterator j = file.getSourceConnections().iterator();(j.hasNext()?(conn = j.next())!=null:false);) {
				if(conn instanceof Dependency) {
					Dependency dep = (Dependency) conn;
					if(dep.getTarget() instanceof EmeraldDependencyFile) {
						EmeraldDependencyFile target = (EmeraldDependencyFile) dep.getTarget();
						Element el2 = document.createElement(DependencyFilesPlugin.XML_DEPENDS);
						el2.setAttribute(DependencyFilesPlugin.XML_DEPENDS_NAME,target.getFile());
						el.appendChild(el2);
					}
				}
			}
			maindocumentelement.appendChild(el);
		}
	}
	return document;
}

public EmeraldDependencyFile getNamedEmeraldDependencyFile(String name) {
	Object f;
	for(Iterator i = dependencyFiles.iterator();(f = i.next())!=null;) {
		if(f instanceof EmeraldDependencyFile) {
			if(((EmeraldDependencyFile) f).getFile().equalsIgnoreCase(name)) {
				return (EmeraldDependencyFile) f;
			}
		}
	}
	return null;
}

/** 
 * Add a shape to this diagram.
 * @param s a non-null shape instance
 * @return true, if the shape was added, false otherwise
 */
public boolean addChild(DependencyFile s) {
	if (s != null && dependencyFiles.add(s)) {
		firePropertyChange(CHILD_ADDED_PROP, null, s);
		return true;
	}
	return false;
}

/** Return a List of Shapes in this diagram.  The returned List should not be modified. */
public List getChildren() {
	return dependencyFiles;
}

/**
 * Remove a shape from this diagram.
 * @param s a non-null shape instance;
 * @return true, if the shape was removed, false otherwise
 */
public boolean removeChild(DependencyFile s) {
	if (s != null && dependencyFiles.remove(s)) {
		firePropertyChange(CHILD_REMOVED_PROP, null, s);
		return true;
	}
	return false;
}

public IPropertyDescriptor[] getPropertyDescriptors() {
	return null;
}
}