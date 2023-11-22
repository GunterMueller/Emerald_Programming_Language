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
package dk.diku.emerald.dependencies.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import dk.diku.emerald.dependencies.model.Dependency;
import dk.diku.emerald.dependencies.model.DependencyFile;
import dk.diku.emerald.dependencies.model.EmeraldDependencyFile;
import dk.diku.emerald.dependencies.model.ModelElement;
import dk.diku.emerald.dependencies.model.commands.DependencyCreateCommand;
import dk.diku.emerald.dependencies.model.commands.DependencyReconnectCommand;

/**
 * EditPart used for DependencyFile instances (more specific for EllipticalShape and
 * EmeraldDependencyFile instances).
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Elias Volanakis
 */
class DependencyFileEditPart extends AbstractGraphicalEditPart 
	implements PropertyChangeListener, NodeEditPart {
	
private ConnectionAnchor anchor;

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
	// allow removal of the associated model element
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new DependencyFileComponentEditPolicy());
	// allow the creation of connections and 
	// and the reconnection of connections between DependencyFile instances
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {
		/* (non-Javadoc)
		 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
		 */
		protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
			DependencyCreateCommand cmd 
				= (DependencyCreateCommand) request.getStartCommand();
			cmd.setTarget((DependencyFile) getHost().getModel());
			return cmd;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
		 */
		protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
			DependencyFile source = (DependencyFile) getHost().getModel();
			int style = ((Integer) request.getNewObjectType()).intValue();
			DependencyCreateCommand cmd = new DependencyCreateCommand(source, style);
			request.setStartCommand(cmd);
			return cmd;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
		 */
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			Dependency conn = (Dependency) request.getConnectionEditPart().getModel();
			DependencyFile newSource = (DependencyFile) getHost().getModel();
			DependencyReconnectCommand cmd = new DependencyReconnectCommand(conn);
			cmd.setNewSource(newSource);
			return cmd;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
		 */
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			Dependency conn = (Dependency) request.getConnectionEditPart().getModel();
			DependencyFile newTarget = (DependencyFile) getHost().getModel();
			DependencyReconnectCommand cmd = new DependencyReconnectCommand(conn);
			cmd.setNewTarget(newTarget);
			return cmd;
		}
	});
}
	
/*(non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	IFigure f = createFigureForModel();
	f.setOpaque(true); // non-transparent figure
	f.setBackgroundColor(ColorConstants.green);
	return f;
}



public class EmeraldFileFigure extends Figure {
	public EmeraldFileFigure(Label name) {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black,1));
		setOpaque(true);
		
		add(name);
	}
}

/**
 * Return a IFigure depending on the instance of the current model element.
 * This allows this EditPart to be used for both sublasses of DependencyFile. 
 */
private IFigure createFigureForModel() {
	if (getModel() instanceof EmeraldDependencyFile) {
		EmeraldFileFigure eff = new EmeraldFileFigure(new Label(((EmeraldDependencyFile) getCastedModel()).getFile()));
		
		return eff;//RectangleFigure();
	} 
	else {
		// if Shapes gets extended the conditions above must be updated
		throw new IllegalArgumentException();
	}
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

protected ConnectionAnchor getConnectionAnchor() {
	if (anchor == null) {
    if (getModel() instanceof EmeraldDependencyFile)
			anchor = new ChopboxAnchor(getFigure());
		else
			// if Shapes gets extended the conditions above must be updated
			throw new IllegalArgumentException("unexpected model");
	}
	return anchor;
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
 */
protected List getModelSourceConnections() {
	return getCastedModel().getSourceConnections();
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
 */
protected List getModelTargetConnections() {
	return getCastedModel().getTargetConnections();
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
	return getConnectionAnchor();
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getSourceConnectionAnchor(Request request) {
	return getConnectionAnchor();
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
	return getConnectionAnchor();
}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
 */
public ConnectionAnchor getTargetConnectionAnchor(Request request) {
	return getConnectionAnchor();
}

/* (non-Javadoc)
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	String prop = evt.getPropertyName();
	if (DependencyFile.SIZE_PROP.equals(prop) || DependencyFile.LOCATION_PROP.equals(prop)) {
		refreshVisuals();
	} else if (DependencyFile.SOURCE_CONNECTIONS_PROP.equals(prop)) {
		refreshSourceConnections();
	} else if (DependencyFile.TARGET_CONNECTIONS_PROP.equals(prop)) {
		refreshTargetConnections();
	}
}

protected void refreshVisuals() {
	// notify parent container of changed position & location
	// if this line is removed, the XYLayoutManager used by the parent container 
	// (the Figure of the ShapesDiagramEditPart), will not know the bounds of this figure
	// and will not draw it correctly.
	Rectangle bounds = new Rectangle(getCastedModel().getLocation(),
			getFigure().getPreferredSize());
	((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
}
}