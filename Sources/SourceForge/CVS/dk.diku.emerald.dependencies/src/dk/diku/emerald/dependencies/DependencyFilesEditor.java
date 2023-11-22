/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
 *    IBM Corporation
?*******************************************************************************/
package dk.diku.emerald.dependencies;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import dk.diku.emerald.dependencies.model.EmeraldDependencyDiagram;
import dk.diku.emerald.dependencies.parts.DependencyFilesEditPartFactory;
import dk.diku.emerald.dependencies.parts.DependencyFilesTreeEditPartFactory;

/**
 * A graphical editor with flyout palette that can edit .shapes files.
 * The binding between the .shapes file extension and this editor is done in plugin.xml
 * @author Elias Volanakis
 */
public class DependencyFilesEditor 
	extends GraphicalEditorWithFlyoutPalette 
{

/** This is the root of the editor's model. */
private EmeraldDependencyDiagram diagram;
/** Palette component, holding the tools and shapes. */
private static PaletteRoot PALETTE_MODEL;

/** Create a new DependencyFilesEditor instance. This is called by the Workspace. */
public DependencyFilesEditor() {
	setEditDomain(new DefaultEditDomain(this));
}

/**
 * Configure the graphical viewer before it receives contents.
 * <p>This is the place to choose an appropriate RootEditPart and EditPartFactory
 * for your editor. The RootEditPart determines the behavior of the editor's "work-area".
 * For example, GEF includes zoomable and scrollable root edit parts. The EditPartFactory
 * maps model elements to edit parts (controllers).</p>
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
 */
protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	
	GraphicalViewer viewer = getGraphicalViewer();
	viewer.setEditPartFactory(new DependencyFilesEditPartFactory());
	viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

	// configure the context menu provider
	ContextMenuProvider cmProvider =
			new DependencyFilesEditorContextMenuProvider(viewer, getActionRegistry());
	viewer.setContextMenu(cmProvider);
	getSite().registerContextMenu(cmProvider, viewer);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util.EventObject)
 */
public void commandStackChanged(EventObject event) {
	firePropertyChange(IEditorPart.PROP_DIRTY);
	super.commandStackChanged(event);
}

private void createOutputStream(OutputStream os) throws IOException {
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(getModel());
	oos.close();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPaletteViewerProvider()
 */
protected PaletteViewerProvider createPaletteViewerProvider() {
	return new PaletteViewerProvider(getEditDomain()) {
		protected void configurePaletteViewer(PaletteViewer viewer) {
			super.configurePaletteViewer(viewer);
			// create a drag source listener for this palette viewer
			// together with an appropriate transfer drop target listener, this will enable
			// model element creation by dragging a CombinatedTemplateCreationEntries 
			// from the palette into the editor
			// @see DependencyFilesEditor#createTransferDropTargetListener()
			viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
		}
	};
}

/**
 * Create a transfer drop target listener. When using a CombinedTemplateCreationEntry
 * tool in the palette, this will enable model element creation by dragging from the palette.
 * @see #createPaletteViewerProvider()
 */
private TransferDropTargetListener createTransferDropTargetListener() {
	return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
		protected CreationFactory getFactory(Object template) {
			return new SimpleFactory((Class) template);
		}
	};
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
 */
public void doSave(IProgressMonitor monitor) {
	final IFile file = ((IFileEditorInput) getEditorInput()).getFile();
	if(file.isSynchronized(IResource.DEPTH_ZERO)) {
		actualDoSave(file);	
		getCommandStack().markSaveLocation();
	} else {
		boolean answer = MessageDialog.openQuestion(
				new Shell(),
				"Error saving file",
				"The files has changed on disk. Saving will overwrite any changes made to the file on disk.\nSave file?");
		if(answer) {
			actualDoSave(file);
			getCommandStack().markSaveLocation();
		} else {
			answer = MessageDialog.openQuestion(
					new Shell(),
					"",
					"Should i reload the file from disk?");
			if (answer) {

//				new Shell().getDisplay().syncExec(new Runnable() {
//          public void run () {
//          	IWorkbenchPage page =
//          		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//           
//          }
//      });
//				try {
//					file.refreshLocal(IResource.DEPTH_ZERO,new NullProgressMonitor());
//				} catch (CoreException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				try {
					new ProgressMonitorDialog(new Shell()).run(
							false, // don't fork
							false, // not cancelable
							new WorkspaceModifyOperation() { // run this operation
								public void execute(final IProgressMonitor monitor) {
									try {
										file.touch(new NullProgressMonitor());
									} catch (CoreException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				new Shell().getDisplay().syncExec(new Runnable() {
					public void run() {
						IWorkbenchPage page =
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						page.closeEditor(DependencyFilesEditor.this, false);
						File newfile = (File) ResourcesPlugin.getWorkspace().getRoot().getFile(file.getFullPath());
						try {
							IDE.openEditor(page, newfile, true);
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
}

private void actualDoSave(final IFile file) {
	Shell shell = getSite().getWorkbenchWindow().getShell();
	try {
		new ProgressMonitorDialog(shell).run(
				false, // don't fork
				false, // not cancelable
				new WorkspaceModifyOperation() { // run this operation
					public void execute(final IProgressMonitor monitor) {
						diagram.export(file.getRawLocation().toOSString());
					}
				});
			file.refreshLocal(IResource.DEPTH_ZERO,new NullProgressMonitor());
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (CoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
 */
public void doSaveAs() {
	//TODO update this method
	// Show a SaveAs dialog
	Shell shell = getSite().getWorkbenchWindow().getShell();
	SaveAsDialog dialog = new SaveAsDialog(shell);
	dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
	dialog.open();
	
	IPath path = dialog.getResult();	
	if (path != null) {
		// try to save the editor's contents under a different file name
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		actualDoSave(file);
		setInput(new FileEditorInput(file));
		getCommandStack().markSaveLocation();
		
	} // if
}

public Object getAdapter(Class type) {
	if (type == IContentOutlinePage.class)
		return new ShapesOutlinePage(new TreeViewer());
	return super.getAdapter(type);
}

EmeraldDependencyDiagram getModel() {
	return diagram;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPalettePreferences()
 */
protected FlyoutPreferences getPalettePreferences() {
	return DependencyFilesEditorPaletteFactory.createPalettePreferences();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
 */
protected PaletteRoot getPaletteRoot() {
	if (PALETTE_MODEL == null)
		PALETTE_MODEL = DependencyFilesEditorPaletteFactory.createPalette();
	return PALETTE_MODEL;
}

private void handleLoadException(Exception e) {
	System.err.println("** Load failed. Using default model. **");
	e.printStackTrace();
	diagram = new EmeraldDependencyDiagram();
}

/**
 * Set up the editor's inital content (after creation).
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
 */
protected void initializeGraphicalViewer() {
	super.initializeGraphicalViewer();
	GraphicalViewer viewer = getGraphicalViewer();
	viewer.setContents(getModel()); // set the contents of this editor
	
	// add the ShortestPathConnectionRouter
	ScalableFreeformRootEditPart root = 
			(ScalableFreeformRootEditPart)viewer.getRootEditPart();
	ConnectionLayer connLayer =
			(ConnectionLayer)root.getLayer(LayerConstants.CONNECTION_LAYER);
	GraphicalEditPart contentEditPart = (GraphicalEditPart)root.getContents();
	ShortestPathConnectionRouter router = 
			new ShortestPathConnectionRouter(contentEditPart.getFigure());
	connLayer.setConnectionRouter(router);

	// listen for dropped parts
	viewer.addDropTargetListener(createTransferDropTargetListener());
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
 */
public boolean isSaveAsAllowed() {
	return true;
}


/* (non-Javadoc)
 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
 */
protected void setInput(IEditorInput input) {
	super.setInput(input);
	IFile file = ((IFileEditorInput) input).getFile();
	diagram = new EmeraldDependencyDiagram(file.getRawLocation().toOSString());
	
	setPartName(file.getName());
}

/**
 * Creates an outline pagebook for this editor.
 */
public class ShapesOutlinePage extends ContentOutlinePage {	
	/**
	 * Create a new outline page for the shapes editor.
	 * @param viewer a viewer (TreeViewer instance) used for this outline page
	 * @throws IllegalArgumentException if editor is null
	 */
	public ShapesOutlinePage(EditPartViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// create outline viewer page
		getViewer().createControl(parent);
		// configure outline viewer
		getViewer().setEditDomain(getEditDomain());
		getViewer().setEditPartFactory(new DependencyFilesTreeEditPartFactory());
		// configure & add context menu to viewer
		ContextMenuProvider cmProvider = new DependencyFilesEditorContextMenuProvider(
				getViewer(), getActionRegistry()); 
		getViewer().setContextMenu(cmProvider);
		getSite().registerContextMenu(
				"dk.diku.emerald.dependencies.outline.contextmenu",
				cmProvider, getSite().getSelectionProvider());		
		// hook outline viewer
		getSelectionSynchronizer().addViewer(getViewer());
		// initialize outline viewer with model
		getViewer().setContents(getModel());
		// show outline viewer
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		// unhook outline viewer
		getSelectionSynchronizer().removeViewer(getViewer());
		// dispose
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return getViewer().getControl();
	}
	
	/**
	 * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
	 */
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		ActionRegistry registry = getActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		String id = ActionFactory.UNDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.REDO.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = ActionFactory.DELETE.getId();
		bars.setGlobalActionHandler(id, registry.getAction(id));
	}
}

}