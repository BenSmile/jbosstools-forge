/**
 * Copyright (c) Red Hat, Inc., contributors and others 2004 - 2014. All rights reserved
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.tools.forge.ui.internal.commands;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.jboss.tools.forge.core.preferences.ForgeCorePreferences;
import org.jboss.tools.forge.core.runtime.ForgeRuntime;
import org.jboss.tools.forge.core.runtime.ForgeRuntimeState;
import org.jboss.tools.forge.core.util.ProjectTools;


public class NewProjectPostProcessor implements ForgeCommandPostProcessor {
	
	private String makePlatformIndependent(String path) {
		if (File.separatorChar == '\\') {
			// we are on Windows
			path = path.replace('\\', '/');
		}
		int index = path.indexOf('/');
		return (index != -1) ? path.substring(index) : path;
	}

	@Override
	public void postProcess(Map<String, String> commandDetails) {
		String projectPath = makePlatformIndependent(commandDetails.get("cpn"));
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String workspacePath = makePlatformIndependent(workspaceRoot.getLocation().toString());
		if (workspacePath.equals(projectPath)) {
			if (MessageDialog.open(
					MessageDialog.QUESTION, 
					null, 
					"Project Import Failed", 
					"The Forge runtime created the project in the workspace root. " +
					"Such a project cannot be imported.\n" +
					"Do you want to remove the created artifacts?",
					SWT.NONE)) {
				String fileSeparator = System.getProperty("file.separator");
				String pomPath = projectPath + fileSeparator + "pom.xml";
				File pomFile = new File(pomPath);
				if (pomFile.exists()) {
					delete(pomFile);
				}
				String srcPath = projectPath + fileSeparator + "src";
				File srcDir = new File(srcPath);
				if (srcDir.exists()) {
					delete(srcDir);
				}
				resetRuntime();
			}
		} else {
			int index = projectPath.lastIndexOf('/');
			if (index != -1) {
				String projectDirName = projectPath.substring(index + 1);
				String projectBaseDirPath = projectPath.substring(0, index);
				ProjectTools.importProject(projectBaseDirPath, projectDirName);
			}
		}
	}
	
	private void delete(File f) {
		if (f.isDirectory()) {
			for (String s : f.list()) {
				delete(new File(f, s));
			}
		}
		f.delete();
	}
	
	private void resetRuntime() {
		ForgeRuntime runtime = ForgeCorePreferences.INSTANCE.getDefaultRuntime();
		if (runtime != null && ForgeRuntimeState.RUNNING.equals(runtime.getState())) {
			runtime.sendInput("reset\n");
		}
	}

}
