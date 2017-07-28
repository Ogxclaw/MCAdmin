package com.kirik.mcadmin.componentsystem;

import java.lang.reflect.InvocationTargetException;

import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.core.util.PlayerHelper;
import com.kirik.mcadmin.core.util.Utils;

public abstract class Component {
	
	protected final MCAdmin plugin;
	protected final PlayerHelper playerHelper;
	
	public Component(){
		plugin = MCAdmin.instance;
		playerHelper = plugin.playerHelper;
		System.out.print(getClass());
	}
	
	public void registerCommands() {
		final String packageName = this.getClass().getPackage().getName();
		plugin.commandSystem.scanCommands(packageName+".commands");
	}

	public void registerListeners() {
		final String packageName = this.getClass().getPackage().getName();
		for (Class<?> cls : Utils.getSubClasses(MCAdminListener.class, packageName+".listeners")) {
			try {
				cls.getConstructor(MCAdmin.class).newInstance(plugin);
				System.out.println("Registered Listener '"+cls.getName()+"' for component '"+packageName+"'.");
				continue;
			}
			catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ignored) {
			}

			try {
				cls.newInstance();
				System.out.println("Registered Listener '"+cls.getName()+"' for component '"+packageName+"'.");
				continue;
			} catch (InstantiationException | IllegalAccessException ignored) {
			}

			System.err.println("Could not register Listener '"+cls.getName()+"' for component '"+packageName+"'.");
		}
	}
	
	public void onEnable(){
		registerCommands();
		//registerListeners();
	}

}
