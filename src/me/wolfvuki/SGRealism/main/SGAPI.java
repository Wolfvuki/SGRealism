package me.wolfvuki.SGRealism.main;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SGAPI {
	
	private SGRealism core;
	
	public List<String> getPlayerList(){
		return core.playing;
	}
	
	public void removePlayer(Player p, String reason){
		p.teleport(core.locations.get(p));
		core.locations.remove(p);
		core.playing.remove(p.getName());
		if(core.pregame != null){
			core.pregame.remove(p.getName());
		}
		p.sendMessage(SGRealism.Tag + ChatColor.GOLD + "You were removed from the game.");
		p.sendMessage(SGRealism.Tag + ChatColor.RED + "Reason: " + reason);
	}
	
	public List<String> getSpectatorList(){
		return core.spectating;
	}
	
	//TODO: Make spectator options
	public void addSpectator(Player p){
		/*
		 * Add to spectator list
		 * Give invisibility, restricted creative (or flight), etc.
		 * TP to location
		 */
		core.spectating.add(p.getName());
		
	}
	
	public void removeSpectator(Player p){
		/*
		 * Remove from spectator list
		 * Remove invisibility, restricted creative (or flight), etc.
		 * TP to previous location
		 */
	}
	
	@SuppressWarnings("deprecation")
	public void End(){
		Bukkit.broadcastMessage(SGRealism.Tag + ChatColor.GREEN + core.playing.get(0) + " won the game!");
		for(String last : core.playing){
			Player ksk = Bukkit.getPlayer(last);
			this.removePlayer(ksk, ChatColor.GREEN + "Game over. You won!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void Tied(){
		Bukkit.broadcastMessage(SGRealism.Tag + ChatColor.DARK_PURPLE + "Game over! Tied game.");
		for(String last : core.playing){
			Player ksk = Bukkit.getPlayer(last);
			this.removePlayer(ksk, "Game over.");
		}
	}
}
