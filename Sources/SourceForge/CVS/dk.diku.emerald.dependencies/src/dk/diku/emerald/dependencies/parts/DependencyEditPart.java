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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


import dk.diku.emerald.dependencies.model.Dependency;
import dk.diku.emerald.dependencies.model.ModelElement;
import dk.diku.emerald.dependencies.model.commands.DependencyDeleteCommand;

/**
 * Edit part for Dependency model elements.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * @author Elias Volanakis
 */
class DependencyEditPart extends AbstractConnectionEditPart 
	implements PropertyChangeListener {

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
 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
 */
protected void createEditPolicies() {
	// Selection handle edit policy. 
	// Makes the connection show a feedback, when selected by the user.
	installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
			new ConnectionEndpointEditPolicy());
	// Allows the removal of the connection model element
	installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
		protected Command getDeleteCommand(GroupRequest request) {
			return new DependencyDeleteCommand(getCastedModel());
		}
	});
}

/* (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	PolylineConnection connection = (PolylineConnection) super.createFigure();
	connection.setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
	connection.setLineStyle(getCastedModel().getLineStyle());  // line drawing style
	return connection;
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

private Dependency getCastedModel() {
	return (Dependency) getModel();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent event) {
	String property = event.getPropertyName();
	if (Dependency.LINESTYLE_PROP.equals(property)) {
		((PolylineConnection) getFigure()).setLineStyle(getCastedModel().getLineStyle());
	}
}

}