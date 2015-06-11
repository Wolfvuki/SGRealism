package me.wolfvuki.SGRealism.main;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Configs {

	//TODO: Use TEMP folder for player inventories.
	//TODO: Add in DEBUG
	//TODO: Add to API
	//TODO: Create Spawnpoint system
	
	private SGRealism core;

	File Settings = new File(core.getDataFolder() + File.separator + "Data", "Settings.yml");
	FileConfiguration SFile = YamlConfiguration.loadConfiguration(Settings);

	File Configuration = new File(core.getDataFolder(), "Configuration.yml");
	FileConfiguration CFile = YamlConfiguration.loadConfiguration(Configuration);

	File Arena = new File(core.getDataFolder() + File.separator + "Data", "Arena.yml");
	FileConfiguration AFile = YamlConfiguration.loadConfiguration(Arena);

	public void onSetup(){

		if(!Settings.exists()){
			try {
				Settings.createNewFile();
				this.addSDefault();
			} catch (Exception e) {
				System.err.println("[SGR] Could not create Settings.yml");
			}
		}

		if(SFile == null){
			this.addSDefault();
		}

		if(!Configuration.exists()){
			try {
				Configuration.createNewFile();
			} catch (Exception e) {
				System.err.println("[SGR] Could not create Configuration.yml");
			}
		}

		if(CFile == null){
			this.addCDefault();
		}
		
	}

	public void addSDefault(){
		String general = "General.";
		String chests = "Chests.";
		SFile.addDefault(general + "Realism Mode", true);
		SFile.addDefault(general + "Players Per Game", 20);
		SFile.addDefault(general + "Countdown Length", 10);
		SFile.addDefault(general + "Effects", true);
		SFile.addDefault(chests + "Items Per Chest.Min", 1);
		SFile.addDefault(chests + "Items Per Chest.Max", 5);
	}

	public void addCDefault(){
		String admin = "Admin.";
		CFile.addDefault("Debug Mode", false);
		CFile.addDefault(admin + "Use Admin Permission instead of OP", false);
		CFile.addDefault(admin + "Admin Permission", "SGR.admin");
	}

	public void createAFile(Player p){
		if(!Arena.exists()){
			try {
				Arena.createNewFile();
			} catch (Exception e) {
				System.err.println("[SGR] Cound not create Arena.yml");
			} finally {
				AFile.addDefault("World", "None");
				AFile.addDefault("X1", 0);
				AFile.addDefault("Y1", 0);
				AFile.addDefault("Z1", 0);
				AFile.addDefault("X2", 0);
				AFile.addDefault("Y2", 0);
				AFile.addDefault("Z2", 0);
				AFile.addDefault("CX", 0);
				AFile.addDefault("CY", 0);
				AFile.addDefault("CZ", 0);
				AFile.set("World", p.getWorld().getName());
			}
		}
	}
	
	public FileConfiguration getArena(){
		return AFile;
	}
	
	public File getArenaFile(){
		return Arena;
	}
	
}
