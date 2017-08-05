package com.kirik.mcadmin.bans.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.mcadmin.bans.BanType;
import com.kirik.mcadmin.bans.Bans;
import com.kirik.mcadmin.commands.system.ICommand;
import com.kirik.mcadmin.commands.system.ICommand.BooleanFlags;
import com.kirik.mcadmin.commands.system.ICommand.Help;
import com.kirik.mcadmin.commands.system.ICommand.Names;
import com.kirik.mcadmin.commands.system.ICommand.Permission;
import com.kirik.mcadmin.commands.system.ICommand.StringFlags;
import com.kirik.mcadmin.commands.system.ICommand.Usage;
import com.kirik.mcadmin.core.MCAdmin;
import com.kirik.mcadmin.core.util.Utils;
import com.kirik.mcadmin.main.MCAdminCommandException;
import com.kirik.mcadmin.main.PermissionDeniedException;

@Names("ban")
@Help("Bans specified user. Specify offline players in quotation marks. \n" + 
		"Flags: \n" +
		" -j to unjail the player first (TODO) \n" +
		" -r to rollback the player \n" + 
		" -t <time> to issue a temporary ban. Possible suffixes: \n" +
		"	m=minutes, h=hours, d=days")
@Usage("[<flags>] <name> [reason]")
@BooleanFlags("jr")
@StringFlags("t")
@Permission("mcadmin.users.ban")
public class BanCommand extends ICommand {

	static Bans bans = new Bans(MCAdmin.instance);
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws MCAdminCommandException {
		args = parseFlags(args);
		executeBan(commandSender, args[0], Utils.concatArray(args, 1, null), plugin, booleanFlags.contains('j'), booleanFlags.contains('r'), stringFlags.get('t'));
	}
	
	public static void executeBan(CommandSender commandSender, String playerName, String reason, MCAdmin plugin, boolean unjail, boolean rollback, final String duration) throws MCAdminCommandException {
		if(!commandSender.hasPermission("mcadmin.users.ban"))
			throw new PermissionDeniedException();
		
		final Player target = plugin.playerHelper.matchPlayerSingle(playerName, false);
		
		if(plugin.playerHelper.getPlayerLevel((Player)commandSender) <= plugin.playerHelper.getPlayerLevel(target))
			throw new PermissionDeniedException();
		
		if(unjail){
			//TODO
		}
		
		if(rollback){
			asPlayer(commandSender).chat("/co rollback u:" + target.getName() + " t:100d");
		}
		
		if(reason == null){
			reason = "Kickbanned by " + commandSender.getName();
		}
		
		final BanType type;
		if(duration != null){
			type = BanType.TEMPORARY;
			
			if(duration.length() < 2)
				throw new MCAdminCommandException("Malformed ban duration");
			
			final String measure = duration.substring(duration.length() - 1);
			
			final long durationValue;
			try {
				durationValue = Long.parseLong(duration.substring(0, duration.length() - 2).trim());
			}catch(NumberFormatException e){
				throw new MCAdminCommandException("Malformed ban duration");
			}
			
			bans.ban(commandSender, target, reason, type, durationValue, measure);
		}else{
			type = BanType.PERMANENT;
			
			bans.ban(commandSender, target, reason, type);
		}
		
		KickCommand.kickPlayer(commandSender, target, reason);
	}
}
