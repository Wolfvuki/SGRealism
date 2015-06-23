package me.wolfvuki.SGRealism.main;

import java.io.File;
import java.util.List;

import me.wolfvuki.SGRealism.Minigame.Minigame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SGAPI {
	
	private SGRealism core;
	private Configs conf;
	private Minigame game;
	
	//Get players in the game
	public List<Player> getPlayerList(){
		return core.playing;
	}
	
	//Get players who are joining
	public List<Player> getJoiningList(){
		return core.joining;
	}
	
	//Get specatators
	public List<Player> getSpectatorList(){
		return core.spectating;
	}
	
	//Get spawnpoints
	public Location[] getSpawnpointLocations(){
		return core.spawnpoints;
	}
	
	//Add player to the list of players joining
	public void addPlayer(Player p){
		if(!core.joining.contains(p)){
			core.joining.add(p);
		}
		p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "Successfully joined SGR.");
		for(Player msg : core.joining){
			msg.sendMessage(SGRealism.Tag + ChatColor.GOLD + "Currently " + core.joining.size() + " in the queue.");
		}
	}
	
	//remove player from the list of players joining
	//[inform] Use default msg to let the player know that they left SGR
	public void removePlayer(Player p, boolean inform){
		if(core.joining.contains(p)) core.joining.remove(p);
		if(inform) p.sendMessage(SGRealism.Tag + ChatColor.GREEN + "Successfully left SGR.");
		for(Player msg : core.joining){
			msg.sendMessage(SGRealism.Tag + ChatColor.GOLD + "Currently " + core.joining.size() + " in the queue.");
		}
	}
	
	//remove from mid-game
	public void removePlayer(Player p, String reason){
		p.teleport(core.locations.get(p));
		core.locations.remove(p);
		core.playing.remove(p);
		if(core.pregame != null) core.pregame.remove(p);
		p.sendMessage(SGRealism.Tag + ChatColor.GOLD + "You were removed from the game.");
		p.sendMessage(SGRealism.Tag + ChatColor.RED + "Reason: " + reason);
	}
	
	public void addSpectator(Player p){
		core.spectating.add(p);
		game.giveSpectator(p);
		core.locations.put(p, p.getLocation());
		p.teleport(core.specspawn);
	}
	
	public void removeSpectator(Player p){
		core.spectating.remove(p);
		game.removeSpectator(p);
		p.teleport(core.locations.get(p));
	}
	
	public void Start(){
		//TODO: Code
	}
	
	public void End(){
		Bukkit.broadcastMessage(SGRealism.Tag + ChatColor.GREEN + core.playing.get(0) + " won the game!");
		for(Player last : core.playing){
			this.removePlayer(last, ChatColor.GREEN + "Game over. You won!");
		}
	}
	
	public void Tied(){
		Bukkit.broadcastMessage(SGRealism.Tag + ChatColor.DARK_PURPLE + "Game over! Tied game.");
		for(Player last : core.playing){
			this.removePlayer(last, "Game over.");
		}
	}
	
	public void teleportArena(){
		int it = 0;
		for(Player pnames : core.playing){
			pnames.teleport(core.spawnpoints[it]);
			it++;
			if(it<=20){
				it=0;
			}
		}
	}
	
	public void saveInventory(Player p){
		conf.createInvFile(p);
	}
	
	public void restoreInventory(Player p){
		File Inv = new File(core.getDataFolder() + File.separator + "Inventories", p.getName() + ".yml");
		FileConfiguration InvFile = YamlConfiguration.loadConfiguration(Inv);
		p.getInventory().setContents((ItemStack[]) InvFile.get("Inventory"));
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
	
	public FileConfiguration getInventoryFile(Player p){
		File Inv = new File(core.getDataFolder() + File.separator + "Inventories", p.getName() + ".yml");
		FileConfiguration InvFile = YamlConfiguration.loadConfiguration(Inv);
		if(Inv.exists()){
			return InvFile;
		}
		return null;
	}
	
	public void removeInventoryFile(Player p){
		File Inv = new File(core.getDataFolder() + File.separator + "Inventories", p.getName() + ".yml");
		if(!Inv.exists()){
			Inv.delete();
		}
	}
}
