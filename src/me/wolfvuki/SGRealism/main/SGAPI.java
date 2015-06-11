package me.wolfvuki.SGRealism.main;

import java.util.List;

import me.wolfvuki.SGRealism.Minigame.Minigame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SGAPI {
	
	private SGRealism core;
	private Configs conf;
	private Minigame game;
	
	public List<String> getPlayerList(){
		return core.playing;
	}
	
	@SuppressWarnings("deprecation")
	public void addPlayer(Player p){
		if(!core.joining.contains(p.getName())){
			core.joining.add(p.getName());
		}
		p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "Successfully joined SGR.");
		for(String msg : core.joining){
			Bukkit.getPlayer(msg).sendMessage(SGRealism.Tag + ChatColor.GOLD + "Currently " + core.joining.size() + " in the queue.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removePlayer2(Player p){
		if(core.joining.contains(p.getName())){
			core.joining.remove(p.getName());
		}
		p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "Successfully left SGR.");
		for(String msg : core.joining){
			Bukkit.getPlayer(msg).sendMessage(SGRealism.Tag + ChatColor.GOLD + "Currently " + core.joining.size() + " in the queue.");
		}
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
	
	//TODO: Finish spectator options
	public void addSpectator(Player p){
		/*
		 * -Add to spectator list
		 * -Give invisibility, restricted creative (or flight), etc.
		 * TP to location
		 */
		core.spectating.add(p.getName());
		game.giveSpectator(p);
		core.locations.put(p, p.getLocation());
		//TP to location
	}
	
	public void removeSpectator(Player p){
		core.spectating.remove(p.getName());
		game.removeSpectator(p);
		p.teleport(core.locations.get(p));
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
	
	public void teleportArena(){
		//TODO: Code
	}
	
	public FileConfiguration getSettings(){
		return conf.SFile;
	}
	
	public FileConfiguration getConfiguration(){
		return conf.CFile;
	}
	
	public FileConfiguration getArena(){
		return conf.AFile;
	}
}
