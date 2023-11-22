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
package dk.diku.emerald.dependencies.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

import dk.diku.emerald.dependencies.model.DependencyFile;
import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;
import dk.diku.emerald.dependencies.model.EmeraldDependencyFile;


/**
 * A command to add a DependencyFile to a ShapeDiagram.
 * The command can be undone or redone.
 * @author Elias Volanakis
 */
public class DependencyFileCreateCommand extends Command {
/** The new shape. */ 
private DependencyFile newShape;

/** ShapeDiagram to add to. */
private final EmeraldDependencyDiagram parent;
/** A request to create a new DependencyFile. */
private final CreateRequest request;
/** True, if newShape was added to parent. */
private boolean shapeAdded;

/**
 * Create a command that will add a new DependencyFile to a EmeraldDependencyDiagram.
 * @param parent the EmeraldDependencyDiagram that will hold the new element
 * @param req     a request to create a new DependencyFile
 * @throws IllegalArgumentException if any parameter is null, or the request
 * 						  does not provide a new DependencyFile instance
 */
public DependencyFileCreateCommand(EmeraldDependencyDiagram parent, CreateRequest req) {
	if (parent == null || req == null || !(req.getNewObject() instanceof EmeraldDependencyFile)) {
		throw new IllegalArgumentException();
	}
	this.parent = parent;
	this.request = req;
	setLabel("shape creation");
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#canUndo()
 */
public boolean canUndo() {
	return shapeAdded;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	// Obtain the new DependencyFile instance from the request.
	// This causes the factory stored in the request to create a new instance.
	// The factory is supplied in the palette-tool-entry, see
	// DependencyFilesEditorPaletteFactory#createComponentsGroup()
	newShape = (DependencyFile) request.getNewObject();
	if(newShape instanceof EmeraldDependencyFile) {
		EmeraldDependencyFile castedShape = (EmeraldDependencyFile) newShape;
		Shell shell = new Shell();
		InputDialog dialog = new InputDialog(shell,"Filename","Enter name of file","",new IInputValidator() {
			public String isValid(String newText) {
				return null;
			}});
		dialog.open();
		String text = dialog.getValue();
		castedShape.setFile(text);
	}
	// Get desired location and size from the request
	newShape.setSize(request.getSize()); // might be null!
	newShape.setLocation(request.getLocation());
	redo();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	shapeAdded = parent.addChild(newShape);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	parent.removeChild(newShape);
}
	
}
