package me.wolfvuki.SGRealism.Minigame;

import me.wolfvuki.SGRealism.main.SGAPI;
import me.wolfvuki.SGRealism.main.SGRealism;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener{
	
	private SGRealism core;
	private SGAPI api;
	
	@EventHandler
	public void onFall(EntityDamageEvent e){					//Player Falling
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(core.playing.contains(p.getName())){
				if(e.getCause() == DamageCause.FALL){
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2));
					p.sendMessage(ChatColor.RED + "You've injured your legs!");
				}
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e){				//Break Blocks
		if(core.playing.contains(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreGameHit(EntityDamageByEntityEvent e){		//Player Hit Player
		if(e.getDamager() instanceof Player){
			if(e.getEntity() instanceof Player){
				Player dmgr = (Player)e.getDamager();
				if(core.pregame.contains(dmgr.getName())){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onStartMove(PlayerMoveEvent e){					//Player Movement
		if(core.starting.contains(e.getPlayer().getName())){
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
		if(core.starting.contains(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrinkPotion(PlayerItemConsumeEvent e){		//Drinking Potions
		if(core.pregame.contains(e.getPlayer().getName())){
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
			if(core.pregame.contains(p.getName())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onWater(PlayerItemConsumeEvent e){				//Drink "Water"
		if(core.playing.contains(e.getPlayer().getName())){
			ItemStack gw = new ItemStack(Material.POTION);
			ItemMeta imgw = gw.getItemMeta();
			imgw.setDisplayName("Water");
			if(e.getItem() == gw){
				double h = e.getPlayer().getHealth();
				if(h <= 20){
					e.getPlayer().setHealth((double)1 + h);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e){					//Player Death
		if(core.playing.contains(e.getEntity().getName())){
			api.removePlayer(e.getEntity(), ChatColor.RED + "You died.");
		}
		for(String all : core.playing){
			Bukkit.getPlayer(all).sendMessage(SGRealism.Tag + ChatColor.GOLD + e.getEntity() + " died.");
			Bukkit.getPlayer(all).sendMessage(SGRealism.Tag + ChatColor.AQUA + core.playing.size() + ChatColor.BLUE + " players remaining.");
		}
		if(core.playing.size() == 1){
			api.End();
		}
	}
}
