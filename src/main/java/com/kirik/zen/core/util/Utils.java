package com.kirik.zen.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.kirik.zen.core.Zen;

public class Utils {
	
	private Zen plugin;
	
	public Utils(Zen plugin){
		this.plugin = plugin;
	}
	
	public static UUID CONSOLE_UUID = UUID.nameUUIDFromBytes("[CONSOLE]".getBytes());
	
	public static UUID getCommandSenderUUID(CommandSender commandSender){
		if(commandSender instanceof Player)
			return ((Player)commandSender).getUniqueId();
		if(commandSender instanceof ConsoleCommandSender)
			return CONSOLE_UUID;
		return UUID.nameUUIDFromBytes(("[CSUUID:" + commandSender.getClass().getName() + "]").getBytes());
	}
	
	public static String concatArray(String[] array, int start, String def){
		if(array.length <= start)
			return def;
		if(array.length <= start + 1)
			return array[start];
		StringBuilder ret = new StringBuilder(array[start]);
		for(int i = start + 1; i < array.length; i++){
			ret.append(' ');
			ret.append(array[i]);
		}
		return ret.toString();
	}
	
	public static String serializeLocation(Location loc){
		return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch() + ";" + loc.getWorld().getName();
	}
	
	public static Location unserializeLocation(String str){
		String[] split = str.split(";");
		return new Location(Bukkit.getWorld(split[5]), Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Float.valueOf(split[3]), Float.valueOf(split[4]));
	}
	
	@Deprecated
	public static <T> List<Class<? extends T>> getSubClasses(Class<T> baseClass){
		final List<Class<? extends T>> ret = new ArrayList<Class<? extends T>>();
		final File file;
		try {
			final ProtectionDomain protectionDomain = baseClass.getProtectionDomain();
			final CodeSource codeSource = protectionDomain.getCodeSource();
			final URL location = codeSource.getLocation();
			final URI uri = location.toURI();
			file = new File(uri);
		}catch(URISyntaxException e){
			e.printStackTrace();
			return ret;
		}
		final String[] fileList;
		
		String packageName = baseClass.getPackage().getName();
		if(file.isDirectory() || (file.isFile() && !file.getName().endsWith(".jar"))){
			String packageFolderName = "/"+packageName.replace('.','/');
			
			URL url = baseClass.getResource(packageFolderName);
			if(url == null)
				return ret;
			
			File directory = new File(url.getFile());
			if(!directory.exists())
				return ret;
			
			fileList = directory.list();
		}else if(file.isFile()){
			final List<String> tmp = new ArrayList<String>();
			final JarFile jarFile;
			try {
				//TODO Leaky, but I dunno how to fix until I rework command system...
				jarFile = new JarFile(file);
			}catch(IOException e){
				e.printStackTrace();
				return ret;
			}
			
			Pattern pathPattern = Pattern.compile(packageName.replace('.','/')+"/(.+\\.class)");
			final Enumeration<JarEntry> entries = jarFile.entries();
			while(entries.hasMoreElements()){
				Matcher matcher = pathPattern.matcher(entries.nextElement().getName());
				if(!matcher.matches())
					continue;
				
				tmp.add(matcher.group(1));
			}
			
			fileList = tmp.toArray(new String[tmp.size()]);
			//TODO Don't be alarmed if this doesn't work. If it does, awesome
			try {
				jarFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			return ret;
		}
		
		Pattern classFilePattern = Pattern.compile("(.+)\\.class");
		for(String fileName : fileList){
			//only class files 'round these parts
			Matcher matcher = classFilePattern.matcher(fileName);
			if(!matcher.matches())
				continue;
			
			//remove .class
			String classname = matcher.group(1);
			try {
				final Class<?> classObject = Class.forName(packageName + "." + classname.replace('/', '.'));
				final Class<? extends T> classT = classObject.asSubclass(baseClass);
				
				ret.add(classT);
			}catch(ClassNotFoundException e){
				System.err.println(e);
			}catch(ClassCastException e){
				continue;
			}
		}
		return ret;
	}
	
	//Added a package intake var
	public static <T> List<Class<? extends T>> getSubClasses(Class<T> baseClass, String packageName) {
		final List<Class<? extends T>> ret = new ArrayList<>();
		final File file;
		try {
			final ProtectionDomain protectionDomain = baseClass.getProtectionDomain();
			final CodeSource codeSource = protectionDomain.getCodeSource();
			if(codeSource == null)
				return ret;

			final URL location = codeSource.getLocation();
			final URI uri = location.toURI();
			file = new File(uri);
		} catch(URISyntaxException e) {
			e.printStackTrace();
			return ret;
		}
		final String[] fileList;

		if(file.isDirectory() || (file.isFile() && !file.getName().endsWith(".jar"))) {
			String packageFolderName = "/" + packageName.replace('.', '/');

			URL url = baseClass.getResource(packageFolderName);
			if(url == null)
				return ret;

			File directory = new File(url.getFile());
			if(!directory.exists())
				return ret;

			// Get the list of the files contained in the package
			fileList = directory.list();
		} else if(file.isFile()) {
			final List<String> tmp = new ArrayList<>();
			final JarFile jarFile;
			try {
				jarFile = new JarFile(file);
			} catch(IOException e) {
				e.printStackTrace();
				return ret;
			}

			Pattern pathPattern = Pattern.compile(packageName.replace('.', '/') + "/(.+\\.class)");
			final Enumeration<JarEntry> entries = jarFile.entries();
			while(entries.hasMoreElements()) {
				Matcher matcher = pathPattern.matcher(entries.nextElement().getName());
				if(!matcher.matches())
					continue;

				tmp.add(matcher.group(1));
			}

			fileList = tmp.toArray(new String[tmp.size()]);
		} else {
			return ret;
		}

		Pattern classFilePattern = Pattern.compile("(.+)\\.class");
		for(String fileName : fileList) {
			// we are only interested in .class files
			Matcher matcher = classFilePattern.matcher(fileName);
			if(!matcher.matches())
				continue;

			// removes the .class extension
			String classname = matcher.group(1);
			try {
				final String qualifiedName = packageName + "." + classname.replace('/', '.');
				final Class<?> classObject = Class.forName(qualifiedName);
				final Class<? extends T> classT = classObject.asSubclass(baseClass);

				// Try to create an instance of the object
				ret.add(classT);
			} catch(ClassCastException e) {
				//noinspection UnnecessaryContinue
				continue;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return ret;
	}
	
	public static String joinList(final List<String> strings, String separator){
		final Iterator<String> iter = strings.iterator();
		StringBuilder sb = new StringBuilder();
		if(iter.hasNext()){
			sb.append(iter.next());
			while(iter.hasNext()){
				sb.append(separator).append(iter.next());
			}
		}
		return sb.toString();
	}
	
	public static double vectorToYaw(Vector offset){
		return Math.toDegrees(Math.atan2(-offset.getX(), offset.getZ()));
	}
	
	static String[] directions = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW" };
	public static String yawToDirection(double yaw){
		yaw = (yaw % 360 + 630) % 360;
		
		int intdeg = (int) Math.round(yaw / 22.5F);
		if(intdeg < 0)
			intdeg += 16;
		if(intdeg >= 16)
			intdeg -= 16;
		
		return directions[intdeg];
	}
	
	public static StringBuilder enumerateStrings(final List<String> strings){
		final StringBuilder sb = new StringBuilder();
		for(int i = 0; i < strings.size(); ++i){
			final String distance = strings.get(i);
			if(i == 0) {
				//nothing
			}else if(i == strings.size() - 1){
				sb.append(" and ");
			}else{
				sb.append(", ");
			}
			sb.append(distance);
		}
		return sb;
	}

}
