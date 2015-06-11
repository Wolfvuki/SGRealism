package me.wolfvuki.SGRealism.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SGRealism extends JavaPlugin{

	private SGAPI api;
	private Configs conf;

	public List<String> playing;
	public List<String> pregame;
	public List<String> starting;
	public List<String> spectating;
	public List<Player> setL1;
	public List<Player> setL2;
	public List<Player> center;
	public HashMap<Player, Location> locations;

	public static String Tag = ChatColor.GOLD + "[" + ChatColor.AQUA + "SGR" + ChatColor.GOLD + "] ";

	File Data = new File(this.getDataFolder() + File.separator + "Data");
	File TEMP = new File(this.getDataFolder() + File.separator + "TEMP");
	
	@Override
	public void onEnable(){
		
		this.Setup();

		

	}
	
	@Override
	public void onDisable(){
		
	}
	
	public void Setup(){

		if(!Data.exists()){
			Data.mkdirs();
		}
		if(!TEMP.exists()){
			TEMP.mkdirs();
		}
		conf.onSetup();
	}

}
