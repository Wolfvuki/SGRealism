package me.wolfvuki.SGRealism.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import me.wolfvuki.SGRealism.Minigame.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SGRealism extends JavaPlugin implements Listener{

	private SGAPI api;
	private Configs conf;

	public List<Player> playing;
	public List<Player> joining; //Add join code
	public List<Player> pregame;
	public List<Player> starting;
	public List<Player> spectating;
	public List<Player> setL1;
	public List<Player> setL2;
	public List<Player> setSP;
	public List<Player> center;
	public Location[] spawnpoints;
	public Location specspawn;
	public HashMap<Player, Location> locations;

	public static String Tag = ChatColor.GOLD + "[" + ChatColor.AQUA + "SGR" + ChatColor.GOLD + "] ";

	File Data = new File(this.getDataFolder() + File.separator + "Data");
	File SpawnPoints = new File(this.getDataFolder() + File.separator + "Data" + File.separator + "SpawnPoints");
	File Points = new File(this.getDataFolder() + File.separator + "Data" + File.separator + "Points");
	File Inv = new File(this.getDataFolder() + File.separator + "Inventories");
	
	@Override
	public void onEnable(){
		
		System.out.println("[SGR] Survival Games Realism starting up...");
		this.Setup();
		this.getServer().getPluginManager().registerEvents(new Events(), this);

	}
	
	@Override
	public void onDisable(){
		/*	Return players if server restarts (set to prev. location and set to original inventory)
		 *  Clear temp (clear lists, arrays, inv and loc files, etc.)
		 */
	}
	
	public void Setup(){

		if(!Data.exists()){
			System.out.println("[SGR] Creating Data folder...");
			Data.mkdirs();
			System.out.println("[SGR] Data folder created.");
		}
		if(!SpawnPoints.exists()){
			System.out.println("[SGR] Creating SpawnPoints folder...");
			SpawnPoints.mkdirs();
			System.out.println("[SGR] SpawnPoints folder created.");
		}
		if(!Points.exists()){
			System.out.println("[SGR] Creating Points folder...");
			Points.mkdirs();
			System.out.println("[SGR] Points folder created.");
		}
		if(!Inv.exists()){
			System.out.println("[SGR] Creating Inventories folder...");
			Inv.mkdirs();
			System.out.println("[SGR] Inventories folder created.");
		}
		conf.onSetup();
		System.out.println("[SGR] Setup process complete.");
	}
	
	//TODO: Finish commands
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("[SGR] Only players may use these commands!");
		} else {
		Player p = (Player)sender;
		if(commandLabel.equalsIgnoreCase("SGR")){
			if(args.length == 0){
				p.sendMessage(ChatColor.GOLD + "========================");
				p.sendMessage(ChatColor.AQUA + "=Survival Games Realism=");
				p.sendMessage(ChatColor.GOLD + "========================");
				p.sendMessage(ChatColor.AQUA + "/SGR Join - Joins the game.");
				p.sendMessage(ChatColor.AQUA + "/SGR Leave - Leave the game.");
				p.sendMessage(ChatColor.AQUA + "/SGR Spectate Join - Spectate the game.");
				p.sendMessage(ChatColor.AQUA + "/SGR Spectate Leave - Return from spectating.");
				if(p.isOp()){
					p.sendMessage(ChatColor.GOLD + "/SGR Setup World - Set the World of the Arena.");
					p.sendMessage(ChatColor.GOLD + "/SGR Setup L1 - Set the 1st Corner of the Map.");
					p.sendMessage(ChatColor.GOLD + "/SGR Setup L2 - Set the 2nd Corner of the Map.");
					p.sendMessage(ChatColor.GOLD + "/SGR Setup SP # - Set SpawnPoint <1-20>.");
					p.sendMessage(ChatColor.GOLD + "/SGR Setup Help - How to setup the Arena.");
				}
				return true;
			} else if(args.length == 1){
				if(args[0].equalsIgnoreCase("join")){
					api.addPlayer(p);
					return true;
				} else if(args[0].equalsIgnoreCase("leave")){
					api.removePlayer(p,true);
					return true;
				} else if(args[0].equalsIgnoreCase("spectate")){
					p.sendMessage(Tag + ChatColor.DARK_GRAY + "/SGR Spectate [Join | Leave]");
					return true;
				}
			} else if(args.length == 2){
				if(args[0].equalsIgnoreCase("spectate")){
					if(args[1].equalsIgnoreCase("join")){
						if(!this.spectating.contains(p)){
							api.addSpectator(p);
							return true;
						}
					} else if(args[1].equalsIgnoreCase("leave")){
						if(this.spectating.contains(p)){
							api.removeSpectator(p);
							return true;
						}
					}
				} else if(args[0].equalsIgnoreCase("setup")){
					if(p.isOp()){
						if(args[1].equalsIgnoreCase("world")){
							if(!conf.getArenaFile().exists()){
								conf.createAFile(p);
							} else {
								conf.getArena().set("World", p.getLocation().getWorld().getName());
							}
						}
					}
				}
			}
		}
	}
	return false;
	}
}
