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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


import dk.diku.emerald.dependencies.model.DependencyFile;
import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;
import dk.diku.emerald.dependencies.model.commands.DependencyFileDeleteCommand;

/**
 * This edit policy enables the removal of a Shapes instance from its container. 
 * @see DependencyFileEditPart#createEditPolicies()
 * @see DependencyFileTreeEditPart#createEditPolicies()
 * @author Elias Volanakis
 */
class DependencyFileComponentEditPolicy extends ComponentEditPolicy {

/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
 */
protected Command createDeleteCommand(GroupRequest deleteRequest) {
	Object parent = getHost().getParent().getModel();
	Object child = getHost().getModel();
	if (parent instanceof EmeraldDependencyDiagram && child instanceof DependencyFile) {
		return new DependencyFileDeleteCommand((EmeraldDependencyDiagram) parent, (DependencyFile) child);
	}
	return super.createDeleteCommand(deleteRequest);
}
}