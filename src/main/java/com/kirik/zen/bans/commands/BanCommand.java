package com.kirik.zen.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.bans.BanType;
import com.kirik.zen.bans.Bans;
import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.BooleanFlags;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.StringFlags;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.UUIDConfiguration;
import com.kirik.zen.core.Zen;
import com.kirik.zen.core.util.Utils;
import com.kirik.zen.main.PermissionDeniedException;
import com.kirik.zen.main.ZenCommandException;

@Names("ban")
@Help("Bans specified user. Specify offline \nplayers in quotation marks. \n" + 
		"Flags: \n" +
		" -r to rollback the player \n" + 
		" -o to ban an offline player \n" + 
		" -t <time> to issue a temporary ban. \n" +
		" Possible suffixes: \nd=days")
@Usage("/ban [flags] <name> [reason]")
@BooleanFlags("or")
@StringFlags("t")
@Permission("zen.users.ban")
public class BanCommand extends ICommand {

	static Bans bans = new Bans(Zen.instance);
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		if(args.length < 1)
			throw new ZenCommandException(this.getUsage());
		
		args = parseFlags(args);
		if(!booleanFlags.contains('o'))
			executeBan(commandSender, args[0], Utils.concatArray(args, 1, null), plugin, booleanFlags.contains('r'), stringFlags.get('t'));
		else
			executeOfflineBan(commandSender, args[0], Utils.concatArray(args, 1, null), plugin, booleanFlags.contains('r'), stringFlags.get('t'));
	}
	
	public static void executeBan(CommandSender commandSender, String playerName, String reason, Zen plugin, boolean rollback, final String duration) throws ZenCommandException {
		
		if(!(commandSender instanceof Player)) {
			Player target = plugin.playerHelper.matchPlayerSingle(playerName, false);
			
			if(rollback){
				plugin.getServer().dispatchCommand(commandSender, "co rollback u:" + target.getName() + " r:-1 t:100d");
				//asPlayer(commandSender).chat("/co rollback u:" + target.getName() + " r:-1 t:100d");
			}
			
			if(reason == null){
				reason = "Kickbanned by CONSOLE";
			}
			
			final BanType type;
			if(duration != null){
				type = BanType.TEMPORARY;
				
				if(duration.length() < 2)
					throw new ZenCommandException("Malformed ban duration");
				
				bans.ban(commandSender, target, reason, type, duration);
			}else{
				type = BanType.PERMANENT;
				
				bans.ban(commandSender, target, reason, type);
			}
		
			KickCommand.kickPlayer(commandSender, target, reason);
			return;
		}
		
		if(!commandSender.hasPermission("zen.users.ban"))
			throw new PermissionDeniedException();
		
		Player target = plugin.playerHelper.matchPlayerSingle(playerName, false);
		
		if(plugin.playerHelper.getPlayerLevel((Player)commandSender) <= plugin.playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		if(rollback){
			asPlayer(commandSender).chat("/co rollback u:" + target.getName() + " r:-1 t:100d");
		}
		
		if(reason == null){
			reason = "Kickbanned by " + commandSender.getName();
		}
		
		final BanType type;
		if(duration != null){
			type = BanType.TEMPORARY;
			
			if(duration.length() < 2)
				throw new ZenCommandException("Malformed ban duration");
			
			bans.ban(commandSender, target, reason, type, duration);
		}else{
			type = BanType.PERMANENT;
			
			bans.ban(commandSender, target, reason, type);
		}
	
		KickCommand.kickPlayer(commandSender, target, reason);
	}
	
	public static void executeOfflineBan(CommandSender commandSender, String playerName, String reason, Zen plugin, boolean rollback, final String duration) throws ZenCommandException {
		if(!commandSender.hasPermission("zen.users.ban"))
			throw new PermissionDeniedException();
		
		UUIDConfiguration uuidConfig = new UUIDConfiguration();
		String uuid = uuidConfig.getUUIDConfig().getString(playerName.toLowerCase() + ".uuid");
		
		if(reason == null){
			reason = "Banned by " + commandSender.getName();
		}
		
		if(rollback){
			asPlayer(commandSender).chat("/co rollback u:" + playerName + " r:-1 t:100d");
		}
		
		final BanType type;
		if(duration != null){
			type = BanType.TEMPORARY;
			
			if(duration.length() < 2)
				throw new ZenCommandException("Malformed ban duration");
			
			bans.offlineBan(commandSender, playerName, uuid, reason, type, duration);
		}else{
			type = BanType.PERMANENT;
			
			bans.offlineBan(commandSender, playerName, uuid, reason, type, "");
		}
	}
}
