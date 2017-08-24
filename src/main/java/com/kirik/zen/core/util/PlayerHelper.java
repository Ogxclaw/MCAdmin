package com.kirik.zen.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.kirik.zen.bans.BanType;
import com.kirik.zen.config.PlayerConfiguration;
import com.kirik.zen.core.Zen;
import com.kirik.zen.main.StateContainer;
import com.kirik.zen.main.console.ZenCommandExecutor;
import com.kirik.zen.main.offlinebukkit.OfflinePlayer;

import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PlayerHelper extends StateContainer {
	
	public Zen plugin;
	
	public PlayerHelper(Zen plugin){
		this.plugin = plugin;
	}
	
	//uuid
	public Player getPlayerByUUID(UUID uuid){
		for(Player p : plugin.getServer().getOnlinePlayers())
			if(p.getUniqueId().equals(uuid))
				return p;
		throw new IllegalArgumentException();
	}
	
	public String getUUIDfromPlayer(String playerName){
		return plugin.getUUIDConfig().getString(playerName + ".uuid");
	}
	
	//get player
	private Player literalMatch(String name){
		Player onlinePlayer = plugin.getServer().getPlayer(name);
		if(onlinePlayer != null)
			return onlinePlayer;
		
		return new OfflinePlayer(plugin.getServer(), name);
	}	
	
	private static final Pattern quotePattern = Pattern.compile("^\"(.*)\"$");
	public Player matchPlayerSingle(String subString, boolean implicitlyLiteral) throws PlayerNotFoundException, MultiplePlayersFoundException {
		Matcher matcher = quotePattern.matcher(subString);
		
		if(matcher.matches())
			return literalMatch(matcher.group(1));
		
		List<Player> players = plugin.getServer().matchPlayer(subString);
		
		int c = players.size();
		if(c < 1)
			if(implicitlyLiteral)
				return literalMatch(subString);
			else
				throw new PlayerNotFoundException();
		if(c > 1)
			throw new MultiplePlayersFoundException(players);
		
		return players.get(0);
	}
	
	public Player matchPlayerSingle(String subString) throws PlayerNotFoundException, MultiplePlayersFoundException {
		return matchPlayerSingle(subString, false);
	}
	
	public String completePlayerName(String subString, boolean implicitlyLiteralNames){
		Matcher matcher = quotePattern.matcher(subString);
		
		if(matcher.matches())
			return matcher.group(1);
		
		List<Player> otherPlayers = plugin.getServer().matchPlayer(subString);
		int c = otherPlayers.size();
		
		if(c == 0 && implicitlyLiteralNames)
			return subString;
		
		if(c == 1)
			return otherPlayers.get(0).getName();
		
		return null;
	}
	
	//message sending
	public void sendChatMessage(CommandSender commandSender, String msg){
		commandSender.sendMessage(msg);
	}
	
	public void sendServerMessage(String msg){
		sendServerMessage(msg, '5');
	}
	
	public void sendServerMessage(String msg, char colorCode){
		msg = "\u00a7" + colorCode + "[Zen]\u00a7f " + msg;
		
		Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
		
		for(Player player : players){
			player.sendMessage(msg);
		}
	}
	
	public void sendDirectedMessage(CommandSender commandSender, String s){
		s = "\u00a75" + "[Zen] \u00a7f" + s;
		commandSender.sendMessage(s);
	}
	
	public static void sendDirectedMessage(CommandSender commandSender, String s, char c){
		s = "\u00a7" + c + "[Zen] \u00a7f" + s;
		commandSender.sendMessage(s);
	}
	
	//prefix
	public String getPlayerPrefix(Player player){
		return plugin.chat.getGroupPrefix(player.getWorld(), this.getPlayerRank(player));
	}
	
	public String getPersonalPlayerPrefix(Player player){
		return plugin.chat.getPlayerPrefix(null, player);
	}
	
	public void setPlayerPrefix(Player player, String prefix){
		plugin.chat.setPlayerPrefix(null, player, prefix);
	}
	
	public void setPlayerSuffix(Player player, String suffix){
		plugin.chat.setPlayerSuffix(null, player, suffix);
	}
	
	public String getPlayerSuffix(Player player){
		return plugin.chat.getGroupSuffix(player.getWorld(), this.getPlayerRank(player));
	}
	
	public String getPersonalPlayerSuffix(Player player){
		return plugin.chat.getPlayerSuffix(null, player);
	}
	
	//ranks
	public String getPlayerRank(Player player){
		return plugin.permission.getPrimaryGroup(null, player);
	}
	
	public void setPlayerRank(Player player, String rankName){
		PlayerConfiguration rank = new PlayerConfiguration(player.getUniqueId());
		plugin.permission.playerRemoveGroup(null, player, this.getPlayerRank(player));
		plugin.permission.playerAddGroup(null, player, rankName);
		
		rank.getPlayerConfig().set("rank", rankName);	
		rank.getPlayerConfig().set("level", getPlayerLevel(player));
		
		rank.savePlayerConfig();
	}
	
	//level
	public int getPlayerLevel(Player player){
		String rank = this.getPlayerRank(player);
		if(player instanceof ZenCommandExecutor){
			return 9999;
		}
		return getLevelOfRank(rank);
	}
	
	public int getLevelOfRank(String rankName){
		switch(rankName){
		case "tism":
			return 0;
		case "member":
			return 10;
		case "helper":
			return 20;
		case "mod":
			return 30;
		case "admin":
			return 40;
		case "manager":
			return 50;
		case "kirik":
			return 666;
		default:
			return 10;
		}
	}
	
	/*public int getPlayerLevel(Player player){
		return plugin.config.getPlayerConfig(player.getUniqueId()).getInt("level");
	}*/
	
	//homes
	public Location getHome(Player player){
		PlayerConfiguration home = new PlayerConfiguration(player.getUniqueId());
		return (Location) home.getPlayerConfig().get("home");
	}
	
	public void setHome(Player player){
		PlayerConfiguration home = new PlayerConfiguration(player.getUniqueId());
		Location loc = player.getLocation();
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		home.getPlayerConfig().set("home", loc);
		home.savePlayerConfig();
	}
	
	//IP
	public static final HashMap<UUID, String> playerHosts = new HashMap<>();
	public static final HashMap<UUID, String> playerIPs = new HashMap<>();
	
	public String getPlayerIP(Player player){
		return player.getAddress().getHostString();
	}
	
	public String getPlayerIP(UUID uuid){
		synchronized(PlayerHelper.playerIPs){
			return playerIPs.get(uuid);
		}
	}
	
	public String getPlayerHost(Player player){
		return player.getAddress().toString();
	}
	
	public String getPlayerHost(UUID uuid){
		synchronized(PlayerHelper.playerIPs){
			return playerHosts.get(uuid);
		}
	}
	
	//freeze
	public void setPlayerFrozen(Player player, boolean flag){
		PlayerConfiguration config = new PlayerConfiguration(player.getUniqueId());
		config.getPlayerConfig().set("frozen", flag);
		config.savePlayerConfig();
	}
	
	public boolean isFrozen(Player player){
		PlayerConfiguration config = new PlayerConfiguration(player.getUniqueId());
		return config.getPlayerConfig().getBoolean("frozen");
	}
	
	//bans
	public boolean isBanned(String uuid){	
		return plugin.getBansConfig().getBoolean(uuid + ".isBanned");
	}
	
	public void banPlayer(String uuid){
		plugin.getBansConfig().set(uuid + ".isBanned", true);
		plugin.saveBansConfig();
	}
	
	public String getBanReason(String uuid){
		return plugin.getBansConfig().getString(uuid + ".reason");
	}
	
	public void setBanReason(String uuid, String reason){
		plugin.getBansConfig().set(uuid + ".reason", reason);
		plugin.saveBansConfig();
	}
	
	public void unbanPlayer(String uuid){
		plugin.getBansConfig().set(uuid + ".isBanned", false);
		plugin.saveBansConfig();
	}
	
	public void setUnbanDate(String uuid, int date){
		plugin.getBansConfig().set(uuid + ".unbanDate", date);
		plugin.saveBansConfig();
	}
	
	public void setBanType(String uuid, BanType type){
		plugin.getBansConfig().set(uuid + ".banType", type.getName());
		plugin.saveBansConfig();
	}
	
	//mute
	public void setPlayerMuted(Player player, boolean flag){
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		playerConfig.getPlayerConfig().set("isMuted", flag);
		playerConfig.savePlayerConfig();
	}
	
	public boolean isMuted(Player player){
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		return playerConfig.getPlayerConfig().getBoolean("isMuted");
	}
	
	Map<UUID, UUID> leashMasters = new HashMap<>();
	private int leashTaskId;
	
	public boolean toggleLeash(Player master, Player slave){
		if(!leashMasters.containsKey(slave.getUniqueId())){
			addLeash(master, slave);
			return true;
		}else if(leashMasters.get(slave.getUniqueId()).equals(master.getUniqueId())){
			removeLeash(slave);
			return false;
		}else{
			addLeash(master, slave);
			return true;
		}
	}
	
	public void addLeash(Player master, Player slave){
		if(leashMasters.isEmpty()){
			final Server server = plugin.getServer();
			
			Runnable task = new Runnable() {
				public void run(){
					for(Iterator<Entry<UUID, UUID>> leashMastersIter = leashMasters.entrySet().iterator(); leashMastersIter.hasNext();){
						Entry<UUID, UUID> entry = leashMastersIter.next();
						
						final UUID slaveName = entry.getKey();
						final Player slave = server.getPlayer(slaveName);
						
						if(slave == null)
							continue;
						
						final UUID masterName = entry.getValue();
						final Player master = server.getPlayer(masterName);
						
						if(master == null || !master.isOnline()){
							leashMastersIter.remove();
							sendServerMessage("Player " + slave.getName() + " was unleashed automatically.");
							removeHandler(slave);
							continue;
						}
						
						final Vector slavePos = slave.getLocation().toVector();
						final Vector masterPos = master.getLocation().toVector();
						
						final Vector masterVelocity = master.getVelocity();
						
						final Vector directionXZ = masterPos.clone().subtract(slavePos);
						double directionY = directionXZ.getY();
						directionXZ.setY(0D);
						
						final double distanceXZ = directionXZ.length();
						
						final double targetDistanceXZ = 2;
						final double maxSpeed = Math.max(0, 0.1 + masterVelocity.clone().setY(0).length() + 0.5 * Math.max(0, distanceXZ - targetDistanceXZ));
						final double maxYSpeed = 0.5;
						
						final Vector velocity = new Vector();
						if(distanceXZ > targetDistanceXZ)
							velocity.add(directionXZ.clone().normalize().multiply(maxSpeed));
						
						if(directionY < -2 || directionY > 2)
							velocity.setY(Math.signum(directionY) * maxSpeed);
						else if(distanceXZ > targetDistanceXZ && directionY > 0)
							velocity.setY(maxYSpeed);
						else
							velocity.setY(slave.getVelocity().getY() * 0.8 + masterVelocity.getY() * 0.2);
						
						final EntityPlayer ePly = ((CraftPlayer)slave).getHandle();
						if(!ePly.onGround)
							velocity.multiply(0.5);
						
						slave.setVelocity(velocity);
					}
				}
			};
			leashTaskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0, 10);
		}
		leashMasters.put(slave.getUniqueId(), master.getUniqueId());
	}
	
	private void removeHandler(Player slave){
		if(leashMasters.isEmpty()){
			plugin.getServer().getScheduler().cancelTask(leashTaskId);
		}
	}
	
	public void removeLeash(Player slave){
		leashMasters.remove(slave.getUniqueId());
		
		removeHandler(slave);
	}
	
	//TP TIMER
	//public int tpTimer;
	
	/*public boolean hasPlayerTpTimer(Player player){
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		return playerConfig.getPlayerConfig().getBoolean("tpTimer");
	}
	
	public void setPlayerTpTimer(Player player, boolean flag){
		PlayerConfiguration playerConfig = new PlayerConfiguration(player.getUniqueId());
		playerConfig.getPlayerConfig().set("tpTimer", flag);
		playerConfig.savePlayerConfig();
	}*/
	
	//wild tp
	public int getSolidBlock(int x, int z, Player target) {
        /*if(target.getWorld().getBiome(target.getLocation().getBlockX(),target.getLocation().getBlockZ()).equals(Biome.HELL))
            return getSolidBlockNether(x,z,target);
        int y = 0;
        if(target.getWorld().getBiome(x,z).equals(Biome.HELL))
            return getSolidBlockNether(x,z,target);*/
		int y = 0;
        for (int i = 0; i <= target.getWorld().getMaxHeight(); i++) {
            y = i;
            if (!target.getWorld().getBlockAt(x, y, z).isEmpty() && target.getWorld().getBlockAt(x, y + 1, z).isEmpty()
                    && target.getWorld().getBlockAt(x, y + 2, z).isEmpty()
                    && target.getWorld().getBlockAt(x, y + 3, z).isEmpty() 
                    && target.getWorld().getBlockAt(x, y + 4, z).isEmpty() 
                    && target.getWorld().getBlockAt(x, y + 5, z).isEmpty()
                    && !checkBlocks(target, x, y, z))
                return y + 5;
        }
        return 5;
    }
	
	private boolean checkBlocks(Player p, int x, int y, int z) {
        return p.getWorld().getBlockAt(x, y, z).getType().equals(Material.LEAVES) &&
                p.getWorld().getBlockAt(x, y, z).getType().equals(Material.LEAVES_2)&&
                !p.getWorld().getBlockAt(x,y,z).isLiquid() && p.getWorld().getBlockAt(x, y - 1, z).getType().equals(Material.LEAVES) &&
                p.getWorld().getBlockAt(x, y - 1, z).getType().equals(Material.LEAVES_2)&&
                !p.getWorld().getBlockAt(x,y - 1,z).isLiquid();
    }
	
	public int getSolidBlock(int x, int z, String w, Player p) {
        int y = 0;
        World world = Bukkit.getWorld(w);
        /*if (world.getBiome(x, z).equals(Biome.HELL)) {
           return getSolidBlockNether(x,z,p);
        } else {
            */for (int i = world.getMaxHeight(); i >= 0; i--) {
                y = i;
                if (!world.getBlockAt(x, y, z).isEmpty()) {
                    return y+ 3;
                }
            }
        //}
        return 5;
    }

    public int getSoildBlock(int x, int z, String w, Player p) {
        return getSolidBlock(x, z, w, p);
    }
    /*private int getSolidBlockNether(int x, int z, Player p) {
        for (int y = 124; y > 2; y--) {
            if(p.getWorld().getBlockAt(x,y,z).isEmpty()){
                if(p.getWorld().getBlockAt(x,y-1,z).isEmpty() &&
                        !p.getWorld().getBlockAt(x,y-2,z).isEmpty()&&
                        !p.getWorld().getBlockAt(x,y-2,z).isLiquid()){
                    return y-1;
                }
            }
        }
        return 10;
    }*/
}
