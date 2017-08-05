package com.kirik.zen.componentsystem;

import java.lang.reflect.InvocationTargetException;

import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.core.util.Utils;

public abstract class Component {

	protected final Zen plugin;
	protected final PlayerHelper playerHelper;

	public Component() {
		plugin = Zen.instance;
		playerHelper = plugin.playerHelper;
		System.out.print(getClass());
	}

	public void registerCommands() {
		final String packageName = this.getClass().getPackage().getName();
		plugin.commandSystem.scanCommands(packageName + ".commands");
	}

	public void registerListeners() {
		final String packageName = this.getClass().getPackage().getName();
		for (Class<?> cls : Utils.getSubClasses(ZenListener.class, packageName + ".listeners")) {
			try {
				cls.getConstructor(Zen.class).newInstance(plugin);
				plugin.logToConsole("Registered Listener '" + cls.getName() + "' for component '" + packageName + "'.");
				continue;
			} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException
					| InstantiationException ignored) {
			}

			try {
				cls.newInstance();
				plugin.logToConsole("Registered Listener '" + cls.getName() + "' for component '" + packageName + "'.");
				continue;
			} catch (InstantiationException | IllegalAccessException ignored) {
			}

			plugin.logToConsole(
					"Could not register Listener '" + cls.getName() + "' for component '" + packageName + "'.");
		}
	}

	public void onEnable() {
		registerCommands();
		registerListeners();
	}

}
