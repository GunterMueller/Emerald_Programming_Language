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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import dk.diku.emerald.dependencies.model.DependencyFile;
import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;


/**
 * Factory that maps model elements to TreeEditParts.
 * TreeEditParts are used in the outline view of the DependencyFilesEditor.
 * @author Elias Volanakis
 */
public class DependencyFilesTreeEditPartFactory implements EditPartFactory {

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
 */
public EditPart createEditPart(EditPart context, Object model) {
	if (model instanceof DependencyFile) {
		return new DependencyFileTreeEditPart((DependencyFile) model);
	}
	if (model instanceof EmeraldDependencyDiagram) {
		return new DiagramTreeEditPart((EmeraldDependencyDiagram) model);
	}
	return null; // will not show an entry for the corresponding model instance
}

}
