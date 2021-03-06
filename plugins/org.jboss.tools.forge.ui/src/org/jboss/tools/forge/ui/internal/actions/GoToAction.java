/**
 * Copyright (c) Red Hat, Inc., contributors and others 2013 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.forge.ui.internal.actions;

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFile;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.forge.core.runtime.ForgeRuntime;
import org.jboss.tools.forge.core.runtime.ForgeRuntimeState;
import org.jboss.tools.forge.ui.internal.ForgeUIPlugin;
import org.jboss.tools.forge.ui.internal.console.ForgeConsole;
import org.jboss.tools.forge.ui.internal.console.ForgeConsoleManager;

public class GoToAction extends Action implements ISelectionListener,
		AutoCloseable {

	private ISelection selection;
	private ForgeRuntime runtime;

	public GoToAction(ForgeRuntime runtime) {
		super();
		this.runtime = runtime;
		setImageDescriptor(createImageDescriptor());
		setToolTipText("Go To Selection");
		getSelectionService().addPostSelectionListener(this);
	}

	@Override
	public void run() {
		goToSelection();
	}

	@Override
	public void close() {
		getSelectionService().removeSelectionListener(this);
	}

	@Override
	protected void finalize() {
		getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection newSelection) {
		selection = newSelection;
	}

	public boolean isEnabled() {
		return ForgeRuntimeState.RUNNING.equals(runtime.getState());
	}

	private ImageDescriptor createImageDescriptor() {
		URL url = ForgeUIPlugin.getDefault().getBundle().getEntry("icons/goto_obj.gif");
		return ImageDescriptor.createFromURL(url);
	}

	public boolean goToSelection() {
		ForgeConsole console = ForgeConsoleManager.INSTANCE.getConsole(runtime);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object first = ss.getFirstElement();
			if (first instanceof IResource) {
				console.goToPath(((IResource) first).getLocation().toOSString());
			} else if (first instanceof IJavaElement) {
				try {
					IResource resource = ((IJavaElement) first)
							.getCorrespondingResource();
					if (resource == null)
						resource = ((IJavaElement) first).getResource();
					if (resource == null)
						return false;
					console.goToPath(resource.getLocation().toOSString());
				} catch (JavaModelException e) {
					ForgeUIPlugin.log(e);
					return false;
				}
			} else if (first instanceof IRemoteFile) {
				console.goToPath(((IRemoteFile) first).getAbsolutePath());
			}
			return true;
		} else {
			return false;
		}
	}

	private ISelectionService getSelectionService() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
	}
	
}
