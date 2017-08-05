package com.kirik.zen.commands.system;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.PlayerHelper;
import com.kirik.zen.main.ZenCommandException;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;
import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public abstract class ICommand {
	
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Names {
		String[] value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Help {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Usage {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Level {
		int value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Permission {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface AbusePotential {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Cost {
		double value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Disabled {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface BooleanFlags {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface StringFlags {
		String value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface NumericFlags {
		String value();
	}

	public enum FlagType {
		BOOLEAN, STRING, NUMERIC
	}

	private final TCharObjectMap<FlagType> flagTypes = new TCharObjectHashMap<>();

	protected final TCharSet booleanFlags = new TCharHashSet();
	protected final TCharObjectMap<String> stringFlags = new TCharObjectHashMap<>();
	protected final TCharObjectMap<Double> numericFlags = new TCharObjectHashMap<>();

	protected Zen plugin;
	protected PlayerHelper playerHelper;

	protected ICommand() {
		this(Zen.instance.commandSystem);
	}

	private ICommand(CommandSystem commandSystem) {
		plugin = Zen.instance;
		playerHelper = plugin.playerHelper;

		if (this.getClass().getAnnotation(Disabled.class) != null)
			return;

		for (String name : getNames()) {
			commandSystem.registerCommand(name, this);
		}

		parseFlagsAnnotations();
	}

	private void parseFlagsAnnotations() {
		final BooleanFlags booleanFlagsAnnotation = this.getClass().getAnnotation(BooleanFlags.class);
		if (booleanFlagsAnnotation != null) {
			parseFlagsAnnotation(booleanFlagsAnnotation.value(), FlagType.BOOLEAN);
		}

		final StringFlags stringFlagsAnnotation = this.getClass().getAnnotation(StringFlags.class);
		if (stringFlagsAnnotation != null) {
			parseFlagsAnnotation(stringFlagsAnnotation.value(), FlagType.STRING);
		}

		final NumericFlags numericFlagsAnnotation = this.getClass().getAnnotation(NumericFlags.class);
		if (numericFlagsAnnotation != null) {
			parseFlagsAnnotation(numericFlagsAnnotation.value(), FlagType.NUMERIC);
		}
	}

	private void parseFlagsAnnotation(final String flags, final FlagType flagType) {
		for (int i = 0; i < flags.length(); ++i) {
			flagTypes.put(flags.charAt(i), flagType);
		}
	}

	protected String parseFlags(String argStr) throws ZenCommandException {
		if (argStr.trim().isEmpty()) {
			booleanFlags.clear();
			stringFlags.clear();
			numericFlags.clear();
			return argStr;
		}

		String[] args = argStr.split(" ");

		args = parseFlags(args);

		if (args.length == 0)
			return "";

		StringBuilder sb = new StringBuilder(args[0]);
		for (int i = 1; i < args.length; ++i) {
			sb.append(' ');
			sb.append(args[i]);
		}

		return sb.toString();
	}

	protected String[] parseFlags(String[] args) throws ZenCommandException {
		int nextArg = 0;

		parseFlagsAnnotations();
		booleanFlags.clear();
		stringFlags.clear();
		numericFlags.clear();

		while (nextArg < args.length) {
			// Fetch argument
			String arg = args[nextArg++];

			// Empty argument? (multiple consecutive spaces)
			if (arg.isEmpty())
				continue;

			// No more flags?
			if (arg.charAt(0) != '-' || arg.length() == 1) {
				--nextArg;
				break;
			}

			// Handle flag parsing terminator --
			if (arg.equals("--"))
				break;

			if (!Character.isLetter(arg.charAt(1))) {
				--nextArg;
				break;
			}

			// Go through the flags
			for (int i = 1; i < arg.length(); ++i) {
				char flagName = arg.charAt(i);

				final FlagType flagType = flagTypes.get(flagName);
				if (flagType == null)
					throw new ZenCommandException("Invalid flag '" + flagName + "' specified.");

				switch (flagType) {
				case BOOLEAN:
					booleanFlags.add(flagName);
					break;

				case STRING:
					// Skip empty arguments...
					while (nextArg < args.length && args[nextArg].isEmpty())
						++nextArg;

					if (nextArg >= args.length)
						throw new ZenCommandException("No value specified for " + flagName + " flag.");

					stringFlags.put(flagName, args[nextArg++]);
					break;

				case NUMERIC:
					// Skip empty arguments...
					while (nextArg < args.length && args[nextArg].isEmpty())
						++nextArg;

					if (nextArg >= args.length)
						throw new ZenCommandException("No value specified for " + flagName + " flag.");

					numericFlags.put(flagName, Double.parseDouble(args[nextArg++]));
					break;
				}
			}
		}

		return Arrays.copyOfRange(args, nextArg, args.length);
	}

	public void Run(Player player, String[] args, String argStr, String commandName) throws ZenCommandException {
	}

	public void run(CommandSender commandSender, String[] args, String argStr, String commandName)
			throws ZenCommandException {
		Run(asPlayer(commandSender), args, argStr, commandName);
	}

	public static Player asPlayer(CommandSender commandSender) throws ZenCommandException {
		if (!(commandSender instanceof Player))
			throw new ZenCommandException("This command can only be run as a player.");

		return (Player) commandSender;
	}

	public static Location getCommandSenderLocation(CommandSender commandSender, boolean elevated)
			throws ZenCommandException {
		final Location location = getCommandSenderLocation(commandSender, elevated, null);
		if (location == null)
			throw new ZenCommandException("This command can only be run as a player or a command block.");

		return location;
	}

	public static Location getCommandSenderLocation(CommandSender commandSender, boolean elevated,
			Location defaultValue) {
		if (commandSender instanceof Player) {
			final Player player = (Player) commandSender;
			if (elevated)
				return player.getEyeLocation();
			else
				return player.getLocation();
		}

		if (commandSender instanceof BlockCommandSender)
			return ((BlockCommandSender) commandSender).getBlock().getLocation().add(0.5, elevated ? 1 : 0.5, 0.5);

		return defaultValue;
	}

	public static CraftPlayer asCraftPlayer(CommandSender commandSender) throws ZenCommandException {
		if (!(commandSender instanceof Player))
			throw new ZenCommandException("This command can only be run as a player.");

		if (!(commandSender instanceof CraftPlayer))
			throw new ZenCommandException("This command can only be run on CraftBukkit.");

		return (CraftPlayer) commandSender;
	}

	public static EntityPlayer asNotchPlayer(CommandSender commandSender) throws ZenCommandException {
		return asCraftPlayer(commandSender).getHandle();
	}

	public static EntityPlayer asNotchPlayer(CommandSender commandSender, EntityPlayer defaultValue)
			throws ZenCommandException {
		if (!(commandSender instanceof CraftPlayer))
			return defaultValue;

		return asNotchPlayer(commandSender);
	}

	public boolean canPlayerUseCommand(CommandSender commandSender) {
		/*
		 * if (hasAbusePotential() &&
		 * AbusePotentialManager.isAbusive(Utils.getCommandSenderUUID(
		 * commandSender))) return false;
		 */

		final String requiredPermission = getRequiredPermission();
		if (requiredPermission != null)
			return commandSender.hasPermission(requiredPermission);
		
		/*return true;*/

		final int playerLevel = playerHelper.getPlayerLevel((Player)commandSender);
		final int requiredLevel = getRequiredLevel();

		return playerLevel >= requiredLevel;
	}

	public String[] getNames() {
		final Names namesAnnotation = this.getClass().getAnnotation(Names.class);
		if (namesAnnotation == null)
			return new String[0];

		return namesAnnotation.value();
	}

	public final String getHelp() {
		final Help helpAnnotation = this.getClass().getAnnotation(Help.class);
		if (helpAnnotation == null)
			return "";

		return helpAnnotation.value();
	}

	public final String getUsage() {
		final Usage usageAnnotation = this.getClass().getAnnotation(Usage.class);
		if (usageAnnotation == null)
			return "";

		return usageAnnotation.value();
	}

	public String getRequiredPermission() {
		final Permission permissionAnnotation = this.getClass().getAnnotation(Permission.class);
		if (permissionAnnotation == null)
			return null;

		return permissionAnnotation.value();
	}

	public final int getRequiredLevel() {
		final Level levelAnnotation = this.getClass().getAnnotation(Level.class);
		if (levelAnnotation == null)
			throw new UnsupportedOperationException("You need either a GetMinLevel method or an @Level annotation.");

		return levelAnnotation.value();
	}

	public boolean hasAbusePotential() {
		final AbusePotential abusePotentialAnnotation = this.getClass().getAnnotation(AbusePotential.class);
		return abusePotentialAnnotation != null;
	}
}