package org.jboss.tools.forge.core.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.jboss.tools.forge.core.internal.ForgeCorePlugin;
import org.jboss.tools.forge.core.preferences.ForgeCorePreferences;

public class ForgeCorePreferencesInitializer extends AbstractPreferenceInitializer {

	public static final String INITIAL_RUNTIMES_PREFERENCE =
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
			"<forgeRuntimes/>";

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(ForgeCorePlugin.PLUGIN_ID);
		preferences.put(ForgeCorePreferences.PREF_FORGE_RUNTIMES, INITIAL_RUNTIMES_PREFERENCE);
	}
	
}
