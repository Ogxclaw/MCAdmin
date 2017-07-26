package com.kirik.mcadmin.offlinebukkit;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.craftbukkit.v1_12_R1.CraftOfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

public abstract class AbstractPlayer extends CraftOfflinePlayer implements Player {
	
	public AbstractPlayer(CraftServer server, UUID uuid, String name){
		super(server, new GameProfile(uuid, name));
	}
	
	public AbstractPlayer(CraftServer server, String name){
		this(server, null, name);
	}
	
	public AbstractPlayer(CraftServer server, UUID uuid){
		this(server, uuid, null);
	}
	
	@Override public ItemStack getItemInHand() {
		return getInventory().getItemInMainHand();
	}
	@Override public void setItemInHand(ItemStack item) {
		getInventory().setItemInMainHand(item);
	}
	@Override public double getEyeHeight() {
		return getEyeHeight(false);
	}
	@Override public double getEyeHeight(boolean ignoreSneaking) {
		if (ignoreSneaking || !isSneaking())
			return 1.62D;

		return 1.42D;
	}

	private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
		if (maxDistance > 120) {
			maxDistance = 120;
		}
		ArrayList<Block> blocks = new ArrayList<>();
		Iterator<Block> itr = new BlockIterator(this, maxDistance);
		while (itr.hasNext()) {
			Block block = itr.next();
			blocks.add(block);
			if (maxLength != 0 && blocks.size() > maxLength) {
				blocks.remove(0);
			}
			int id = block.getType().getId();
			if (transparent == null) {
				if (id != 0) {
					break;
				}
			} else {
				if (!transparent.contains((byte)id)) {
					break;
				}
			}
		}
		return blocks;
	}

	@Override
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
		List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
		return blocks.get(0);
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
		return getLineOfSight(transparent, maxDistance, 2);
	}

	@Override
	public Location getLocation(Location location) {
		location.subtract(location);
		location.add(getLocation());
		return null;
	}

	@Override
	public boolean teleport(Entity destination) {
		return teleport(destination.getLocation());
	}

	@Override
	public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
		return teleport(location);
	}

	@Override
	public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
		return teleport(destination);
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		return new HashSet<>();
	}

	@Override
	public boolean isValid() {
		return !isDead();
	}

	@Override public void setPlayerWeather(WeatherType weatherType) { }
	@Override public boolean isHealthScaled() { return false; }
	@Override public void setHealthScaled(boolean b) { }
	@Override public void setHealthScale(double v) throws IllegalArgumentException { }
	@Override public Spigot spigot() { return null; }
	@Override public void setLastDamage(double v) { }
	@Override public void damage(double v) { }
	@Override public void damage(double v, Entity entity) { }
	@Override public void setHealth(double v) { }
	@Override public void setMaxHealth(double v) { }
	@Override public Scoreboard getScoreboard() { return null; }
	@Override public void setScoreboard(Scoreboard scoreboard) { }
	@Override public WeatherType getPlayerWeather() { return null; }
	@Override public void resetPlayerWeather() { }
	@Override public boolean isOnGround() { return true; }
	@Override public void resetMaxHealth() { }
	@Override public void playEffect(EntityEffect effect) { }
	@Override public PlayerInventory getInventory() { return null; }
	@Override public Inventory getEnderChest() { return null; }
	@Override public double getHealth() { return 0; }
	@Override public boolean isInsideVehicle() { return false; }
	@Override public boolean leaveVehicle() { return false; }
	@Override public Vehicle getVehicle() { return null; }
	@Override public int getRemainingAir() { return 0; }
	@Override public void setRemainingAir(int ticks) { }
	@Override public int getMaximumAir() { return 0; }
	@Override public void setMaximumAir(int ticks) { }
	@Override public int getFireTicks() { return 0; }
	@Override public int getMaxFireTicks() { return 0; }
	@Override public void setFireTicks(int ticks) { }
	@Override public void remove() { }
	@Override public void sendMessage(String message) { }
	@Override public void setCompassTarget(Location loc) { }
	@Override public InetSocketAddress getAddress() { return null; }
	@Override public void kickPlayer(String message) { }
	@Override public void chat(String msg) { }
	@Override public boolean performCommand(String command) { return false; }
	@Override public boolean isSneaking() { return false; }
	@Override public void setSneaking(boolean sneak) { }
	@Override public void updateInventory() { }
	@Override public Location getEyeLocation() { return null; }
	@Override public void setVelocity(Vector velocity) { }
	@Override public Vector getVelocity() { return null; }
	@Override public int getMaximumNoDamageTicks() { return 0; }
	@Override public void setMaximumNoDamageTicks(int ticks) { }
	@Override public double getLastDamage() { return 0; }
	@Override public int getNoDamageTicks() { return 0; }
	@Override public void setNoDamageTicks(int ticks) { }
	@Override public Player getKiller() { return null; }
	@Override public boolean teleport(Location location) { return false; }
	@Override public Entity getPassenger() { return null; }
	@Override public boolean setPassenger(Entity passenger) { return false; }
	@Override public boolean isEmpty() { return false; }
	@Override public boolean eject() { return false; }
	@Override public Location getCompassTarget() { return null; }
	@Override public void sendRawMessage(String message) { }
	@Override public boolean isSleeping() { return false; }
	@Override public int getSleepTicks() { return 0; }
	@Override public List<Entity> getNearbyEntities(double x, double y, double z) { return null; }
	@Override public boolean isDead() { return false; }
	@Override public float getFallDistance() { return 0; }
	@Override public void setFallDistance(float distance) { }
	@Override public void saveData() { }
	@Override public void loadData() { }
	@Override public void setSleepingIgnored(boolean isSleeping) { }
	@Override public boolean isSleepingIgnored() { return false; }
	@Override public void awardAchievement(Achievement achievement) { }
	@Override public void incrementStatistic(Statistic statistic) { }
	@Override public void incrementStatistic(Statistic statistic, int amount) { }
	@Override public void incrementStatistic(Statistic statistic, Material material) { }
	@Override public void incrementStatistic(Statistic statistic, Material material, int amount) { }
	@Override public void setLastDamageCause(EntityDamageEvent event) { }
	@Override public EntityDamageEvent getLastDamageCause() { return null; }
	@Override public void playNote(Location loc, byte instrument, byte note) { }
	@Override public void sendBlockChange(Location loc, Material material, byte data) { }
	@Override public void sendBlockChange(Location loc, int material, byte data) { }
	@Override public long getPlayerTime() { return 0; }
	@Override public long getPlayerTimeOffset() { return 0; }
	@Override public boolean isPlayerTimeRelative() { return false; }
	@Override public void playEffect(Location arg0, Effect arg1, int arg2) { }
	@Override public void playNote(Location arg0, Instrument arg1, Note arg2) { }
	@Override public void playSound(Location location, Sound sound, float v, float v1) { }
	@Override public void resetPlayerTime() { }
	@Override public boolean sendChunkChange(Location arg0, int arg1, int arg2, int arg3, byte[] arg4) { return false; }
	@Override public void setPlayerTime(long arg0, boolean arg1) { }
	@Override public boolean isPermissionSet(String name) { return false; }
	@Override public boolean isPermissionSet(Permission perm) { return false; }
	@Override public boolean hasPermission(String name) { return false; }
	@Override public boolean hasPermission(Permission perm) { return false; }
	@Override public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) { return null; }
	@Override public PermissionAttachment addAttachment(Plugin plugin) { return null; }
	@Override public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) { return null; }
	@Override public PermissionAttachment addAttachment(Plugin plugin, int ticks) { return null; }
	@Override public void removeAttachment(PermissionAttachment attachment) { }
	@Override public void recalculatePermissions() { }
	@Override public Set<PermissionAttachmentInfo> getEffectivePermissions() { return null; }
	@Override public void sendMap(MapView map) { }
	@Override public GameMode getGameMode() { return null; }
	@Override public void setGameMode(GameMode arg0) { }
	@Override public Location getBedSpawnLocation() { return null; }
	@Override public void setBedSpawnLocation(Location location) { }
	@Override public float getExhaustion() { return 0; }
	@Override public int getFoodLevel() { return 0; }
	@Override public int getLevel() { return 0; }
	@Override public float getSaturation() { return 0; }
	@Override public int getTotalExperience() { return 0; }
	@Override public void setExhaustion(float arg0) { }
	@Override public void setFoodLevel(int arg0) { }
	@Override public void setLevel(int arg0) { }
	@Override public void setSaturation(float arg0) { }
	@Override public void setTotalExperience(int arg0) { }
	@Override public void setSprinting(boolean arg0) { }
	@Override public boolean isSprinting() { return false; }
	@Override public int getTicksLived() { return 0; }
	@Override public void setTicksLived(int value) { }
	@Override public String getPlayerListName() { return null; }
	@Override public void setPlayerListName(String name) { }
	@Override public double getMaxHealth() { return 0; }
	@Override public void giveExp(int amount) { }
	@Override public float getExp() { return 0; }
	@Override public void setExp(float exp) { }
	@Override public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) { }
	@Override public void setAllowFlight(boolean b) { }
	@Override public boolean getAllowFlight() { return true; }
	@Override public void hidePlayer(Player player) { }
	@Override public void showPlayer(Player player) { }
	@Override public boolean canSee(Player player) { return false; }
	@Override public boolean isFlying() { return false; }
	@Override public void setFlying(boolean b) { }
	@Override public void setFlySpeed(float v) throws IllegalArgumentException { }
	@Override public void setWalkSpeed(float v) throws IllegalArgumentException { }
	@Override public float getFlySpeed() { return 0; }
	@Override public float getWalkSpeed() { return 0; }
	@Override public void setTexturePack(String s) { }
	@Override public boolean addPotionEffect(PotionEffect effect) { return false; }
	@Override public boolean addPotionEffect(PotionEffect effect, boolean force) { return false; }
	@Override public boolean addPotionEffects(Collection<PotionEffect> effects) { return false; }
	@Override public boolean hasPotionEffect(PotionEffectType type) { return false; }
	@Override public void removePotionEffect(PotionEffectType type) { }
	@Override public Collection<PotionEffect> getActivePotionEffects() { return null; }
	@Override public boolean hasLineOfSight(Entity entity) { return false; }
	@Override public boolean getRemoveWhenFarAway() { return false; }
	@Override public void setRemoveWhenFarAway(boolean b) { }
	@Override public EntityEquipment getEquipment() { return null; }
	@Override public void setCanPickupItems(boolean b) { }
	@Override public boolean getCanPickupItems() { return false; }
	@Override public void setCustomName(String s) { }
	@Override public String getCustomName() { return null; }
	@Override public void setCustomNameVisible(boolean b) { }
	@Override public boolean isCustomNameVisible() { return false; }
	@Override public boolean isLeashed() { return false; }
	@Override public Entity getLeashHolder() throws IllegalStateException { return null; }
	@Override public boolean setLeashHolder(Entity entity) { return false; }
	@Override public void closeInventory() { }
	@Override public ItemStack getItemOnCursor() { return null; }
	@Override public InventoryView getOpenInventory() { return null; }
	@Override public InventoryView openEnchanting(Location arg0, boolean arg1) { return null; }
	@Override public InventoryView openInventory(Inventory arg0) { return null; }
	@Override public void openInventory(InventoryView arg0) { }
	@Override public InventoryView openWorkbench(Location arg0, boolean arg1) { return null; }
	@Override public void setItemOnCursor(ItemStack arg0) { }
	@Override public boolean setWindowProperty(InventoryView.Property arg0, int arg1) { return false; }
	@Override public <T extends Projectile> T launchProjectile(Class<? extends T> arg0) { return null; }
	@Override public EntityType getType() { return null; }
	@Override public void abandonConversation(Conversation arg0) { }
	@Override public void acceptConversationInput(String arg0) { }
	@Override public boolean beginConversation(Conversation arg0) { return false; }
	@Override public boolean isConversing() { return false; }
	@Override public void sendMessage(String[] arg0) { }
	@Override public <T> void playEffect(Location arg0, Effect arg1, T arg2) { }
	@Override public boolean isBlocking() { return false; }
	@Override public void abandonConversation(Conversation arg0, ConversationAbandonedEvent arg1) { }
	@Override public int getExpToLevel() { return 0; }
	@Override public void giveExpLevels(int amount) { }
	@Override public void setBedSpawnLocation(Location location, boolean force) { }
	@Override public void playSound(Location arg0, String arg1, float arg2, float arg3) { }
	@Override public void setResourcePack(String s) { }
	@Override
	public void decrementStatistic(Statistic arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decrementStatistic(Statistic arg0, EntityType arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AdvancementProgress getAdvancementProgress(Advancement arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getHealthScale() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getSpectatorTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatistic(Statistic arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatistic(Statistic arg0, Material arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasAchievement(Achievement arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementStatistic(Statistic arg0, EntityType arg1, int arg2) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location arg0, Sound arg1, SoundCategory arg2, float arg3, float arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Location arg0, String arg1, SoundCategory arg2, float arg3, float arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAchievement(Achievement arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetTitle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSignChange(Location arg0, String[] arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTitle(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTitle(String arg0, String arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisplayName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourcePack(String arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSpectatorTarget(Entity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatistic(Statistic arg0, int arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatistic(Statistic arg0, Material arg1, int arg2) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatistic(Statistic arg0, EntityType arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, Location arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, T arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, T arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
			T arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
			double arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6,
			double arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, Location arg1, int arg2, double arg3, double arg4, double arg5,
			double arg6, T arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5,
			double arg6, double arg7, T arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5, double arg6,
			double arg7, double arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void spawnParticle(Particle arg0, double arg1, double arg2, double arg3, int arg4, double arg5,
			double arg6, double arg7, double arg8, T arg9) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSound(Sound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSound(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSound(Sound arg0, SoundCategory arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSound(String arg0, SoundCategory arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCooldown(Material arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MainHand getMainHand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getShoulderEntityLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entity getShoulderEntityRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCooldown(Material arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHandRaised() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InventoryView openMerchant(Villager arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InventoryView openMerchant(Merchant arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCooldown(Material arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShoulderEntityLeft(Entity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShoulderEntityRight(Entity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Block> getLineOfSight(Set<Material> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PotionEffect getPotionEffect(PotionEffectType arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Block getTargetBlock(Set<Material> arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAI() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGliding() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAI(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCollidable(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGliding(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AttributeInstance getAttribute(Attribute arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addPassenger(Entity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addScoreboardTag(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getEntityId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getPassengers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPortalCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<String> getScoreboardTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasGravity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGlowing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInvulnerable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSilent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removePassenger(Entity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeScoreboardTag(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGlowing(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGravity(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInvulnerable(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPortalCooldown(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSilent(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> arg0, Vector arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
