/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
 *    IBM Corporation
?*******************************************************************************/
package dk.diku.emerald.dependencies.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;

import dk.diku.emerald.dependencies.model.ModelElement;
import dk.diku.emerald.dependencies.model.DependencyFile;

/**
 * TreeEditPart used for DependencyFile instances (more specific for EllipticalShape and
 * EmeraldDependencyFile instances). This is used in the Outline View of the DependencyFilesEditor.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class DependencyFileTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

/**
 * Create a new instance of this edit part using the given model element.
 * @param model a non-null Shapes instance
 */
DependencyFileTreeEditPart(DependencyFile model) {
	super(model);
}

/**
 * Upon activation, attach to the model element as a property change listener.
 */
public void activate() {
	if (!isActive()) {
		super.activate();
		((ModelElement) getModel()).addPropertyChangeListener(this);
	}
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	// allow removal of the associated model element
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new DependencyFileComponentEditPolicy());
}

/**
 * Upon deactivation, detach from the model element as a property change listener.
 */
public void deactivate() {
	if (isActive()) {
		super.deactivate();
		((ModelElement) getModel()).removePropertyChangeListener(this);
	}
}

private DependencyFile getCastedModel() {
	return (DependencyFile) getModel();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
 */
protected Image getImage() {
	return getCastedModel().getIcon();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
 */
protected String getText() {
	return getCastedModel().toString();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	refreshVisuals(); // this will cause an invocation of getImage() and getText(), see below
}
}