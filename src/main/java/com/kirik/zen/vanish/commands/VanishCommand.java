package com.kirik.zen.vanish.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.kirik.zen.commands.system.ICommand;
import com.kirik.zen.commands.system.ICommand.Help;
import com.kirik.zen.commands.system.ICommand.Names;
import com.kirik.zen.commands.system.ICommand.Permission;
import com.kirik.zen.commands.system.ICommand.Usage;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.main.ZenCommandException;

@Names({"vanish", "v"})
@Usage("/vanish")
@Help("Vanishes yourself")
@Permission("zen.admin.vanish")
public class VanishCommand extends ICommand {
	
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr, String commandName) throws ZenCommandException {
		final Player player = (Player)commandSender;
		final PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		boolean isVanished = playerConfig.getPlayerConfig().getBoolean("isVanished");
		if(isVanished){
			plugin.vanish.showPlayer(player);
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				int pLevel = playerHelper.getPlayerLevel(p);
				int senderLevel = playerHelper.getPlayerLevel(player);
				if(pLevel >= senderLevel) {
					playerHelper.sendDirectedMessage(p, player.getName() + " unvanished!");
				}
			}
		}else{
			plugin.vanish.hidePlayer(player);
			/*IChatBaseComponent barmsg = ChatSerializer.a("{\"text\":\"\u00a7aYou are currently vanished!\"}");
			PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(barmsg, ChatMessageType.GAME_INFO);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);*/
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				int pLevel = playerHelper.getPlayerLevel(p);
				int senderLevel = playerHelper.getPlayerLevel(player);
				if(pLevel >= senderLevel) {
					playerHelper.sendDirectedMessage(p, player.getName() + " vanished!");
				}
			}
		}
	}

}
