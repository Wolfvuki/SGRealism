package me.wolfvuki.SGRealism.Minigame;

import java.io.File;

import me.wolfvuki.SGRealism.main.Configs;
import me.wolfvuki.SGRealism.main.SGAPI;
import me.wolfvuki.SGRealism.main.SGRealism;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class Events implements Listener{
	
	private SGRealism core;
	private SGAPI api;
	private Configs conf;
	
	@EventHandler
	public void onFall(EntityDamageEvent e){					//Player Falling
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(core.playing.contains(p)){
				if(e.getCause() == DamageCause.FALL){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
					p.sendMessage(ChatColor.RED + "You've injured your legs!");
				}
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e){				//Break Blocks
		if((core.playing.contains(e.getPlayer()))||(core.spectating.contains(e.getPlayer()))){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreGameHit(EntityDamageByEntityEvent e){		//Player Hit Player
		if(e.getDamager() instanceof Player){
			if(e.getEntity() instanceof Player){
				Player dmgr = (Player)e.getDamager();
				if(core.pregame.contains(dmgr)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onStartMove(PlayerMoveEvent e){					//Player Movement
		if(core.starting.contains(e.getPlayer())){
			if(e.getTo().getBlockX() != e.getFrom().getBlockX()){
				e.setCancelled(true);
			}
			if(e.getTo().getBlockY() != e.getFrom().getBlockY()){
				e.setCancelled(true);
			}
			if(e.getTo().getBlockZ() != e.getFrom().getBlockZ()){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onStartInv(PlayerDropItemEvent e){				//Dropping Items
		if(core.starting.contains(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrinkPotion(PlayerItemConsumeEvent e){		//Drinking Potions
		if(core.pregame.contains(e.getPlayer())){
			if(e.getItem().getType() == Material.POTION){
				e.setCancelled(true);
				e.getPlayer().sendMessage(SGRealism.Tag + ChatColor.DARK_RED + "You may not consume potions during the pregame session.");
			}
		}
	}
	
	@EventHandler
	public void onThrowPotion(PotionSplashEvent e){				//Throwing Potions
		if(e.getPotion().getShooter() instanceof Player){
			Player p = (Player)e.getPotion().getShooter();
			if(core.pregame.contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onWater(PlayerItemConsumeEvent e){				//Drink "Water"
		if(core.playing.contains(e.getPlayer())){
			ItemStack gw = new ItemStack(Material.POTION);
			ItemMeta imgw = gw.getItemMeta();
			imgw.setDisplayName("Water");
			if(e.getItem() == gw){
				double h = e.getPlayer().getHealth();
				if(h < 20){
					e.getPlayer().setHealth((double)1 + h);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){					//Player Death
		if(core.playing.contains(e.getEntity())) api.removePlayer(e.getEntity(), ChatColor.RED + "You died.");
		for(Player all : core.playing){
			all.sendMessage(SGRealism.Tag + ChatColor.GOLD + e.getEntity() + " died.");
			all.sendMessage(SGRealism.Tag + ChatColor.AQUA + core.playing.size() + ChatColor.BLUE + " players remaining.");
		}
		if(core.playing.size() == 1){
			api.End();
		}
	}
	
	@EventHandler
	public void onCmd(PlayerChatEvent e){
		if(core.spectating.contains(e.getPlayer())){
			if((e.getMessage().startsWith("/"))&&(!e.getMessage().equalsIgnoreCase("/spectator leave"))){
				e.setCancelled(true);
				e.getPlayer().sendMessage(SGRealism.Tag + ChatColor.BLUE + "You must do /spectator leave before you do any commands.");
			}
		}
	}
	
	/*
	 * ##############################################################################################
	 * #####################################Set location events######################################
	 * ##############################################################################################
	 */
	
	//TODO: Add check system to make sure spawnpoints are in the same world and area the arena is set to
	
	@EventHandler
	public void setSpawnPoint(PlayerInteractEvent e, Integer x){
		if(core.setSP.contains(e.getPlayer())){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Location cb = e.getClickedBlock().getLocation();
				File SpawnPoint = new File(core.getDataFolder() + File.separator + "Data" + File.separator + "SpawnPoints", x + ".yml");
				if(!SpawnPoint.exists()){
					try{
						SpawnPoint.createNewFile();
					} catch(Exception eba){
						System.err.println("[SGR] Could not make spawnpoint " + x + " file.");
						e.getPlayer().sendMessage(SGRealism.Tag + ChatColor.RED + "Error: Couldn't make spawnpoint file.");
					}
				}
				FileConfiguration SPFile = YamlConfiguration.loadConfiguration(SpawnPoint);
				SPFile.addDefault("X", 0);
				SPFile.addDefault("Y", 0);
				SPFile.addDefault("Z", 0);
				SPFile.set("X", cb.getBlockX());
				SPFile.set("Y", cb.getBlockX());
				SPFile.set("Z", cb.getBlockX());
				core.setSP.remove(e.getPlayer());
				e.getPlayer().sendMessage(SGRealism.Tag + ChatColor.GREEN + "Successfully set spawnpoint " + x);
			}
		}
	}

	@EventHandler
	public void setAFileCoord1(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(core.setL1.contains(p)){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Location cb = e.getClickedBlock().getLocation();
				if(conf.getArenaFile().exists()){
					conf.getArena().set("X1", cb.getBlockX());
					conf.getArena().set("Y1", cb.getBlockY());
					conf.getArena().set("Z1", cb.getBlockZ());
					p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "L1 successfully set.");
					core.setL1.remove(p);
				} else {
					p.sendMessage(SGRealism.Tag + ChatColor.RED + "World must be set first.");
					core.setL1.remove(p);
				}
			}
		}
	}

	@EventHandler
	public void setAFileCoord2(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(core.setL2.contains(p)){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Location cb = e.getClickedBlock().getLocation();
				if(conf.getArenaFile().exists()){
					conf.getArena().set("X2", cb.getBlockX());
					conf.getArena().set("Y2", cb.getBlockY());
					conf.getArena().set("Z2", cb.getBlockZ());
					p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "L2 successfully set.");
				} else {
					p.sendMessage(SGRealism.Tag + ChatColor.RED + "World must be set first.");
					core.setL2.remove(p);
				}
			}
		}
	}

	@EventHandler
	public void setAFileCenter(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(core.center.contains(p)){
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Location cb = e.getClickedBlock().getLocation();
				if(conf.getArenaFile().exists()){
					conf.getArena().set("CX", cb.getBlockX());
					conf.getArena().set("CY", cb.getBlockY());
					conf.getArena().set("CZ", cb.getBlockZ());
					p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "Center location was successfully set.");
					core.center.remove(p);
				} else {
					p.sendMessage(SGRealism.Tag + ChatColor.RED + "World must be set first.");
					core.center.remove(p);
				}
			}
		}
	}
}
