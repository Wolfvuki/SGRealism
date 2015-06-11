package me.wolfvuki.SGRealism.Minigame;

import java.util.List;
import java.util.Random;

import me.wolfvuki.SGRealism.main.Configs;
import me.wolfvuki.SGRealism.main.SGAPI;
import me.wolfvuki.SGRealism.main.SGRealism;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Minigame implements Listener{
	
	//TODO: Add spectator controls
	
	private SGRealism core;
	private Configs conf;
	private SGAPI api;
	private String Tagg = SGRealism.Tag;
	private int countdown = 40;
	private List<Chest> chests;
	private Random rnd = new Random();
	private int timepassed = 0;
	
	@SuppressWarnings("deprecation")
	public void PreGame(){
		PotionEffect adr1 = new PotionEffect(PotionEffectType.SPEED, 10, 1);
		PotionEffect adr2 = new PotionEffect(PotionEffectType.SPEED, 10, 2);
		for(String players : core.playing){
			core.pregame.add(players);
		}
		for(String pgplayer : core.pregame){
			Player p = Bukkit.getPlayer(pgplayer);
			p.sendMessage(Tagg + ChatColor.AQUA + "Pregame session started.");
			p.sendMessage(Tagg + ChatColor.AQUA + "The game is currently making final preparations.");
			p.sendMessage(Tagg + ChatColor.DARK_RED + "You may move after 10 seconds have passed.");
		}
		for(int x = 0; x <= 40; x++){
			if(x <= 40){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(core, new Runnable() {
				public void run() { 
					countdown--;
				}}, 20L);
			for(String pgp : core.playing){
				Player pp = Bukkit.getPlayer(pgp);
				if(countdown == 30){
					core.starting.clear();
					pp.sendMessage(Tagg + ChatColor.DARK_GREEN + "You may now move.");
					pp.sendMessage(Tagg + ChatColor.DARK_PURPLE + "The game will start in 30 seconds. Get ready.");
					pp.addPotionEffect(adr1);
				}
				if(countdown == 15){
					pp.sendMessage(Tagg + ChatColor.DARK_BLUE + "15 seconds remaining...");
				}
				if(countdown <= 10){
					if(countdown == 10){
						pp.addPotionEffect(adr2);
					}
					pp.sendMessage(Tagg + ChatColor.DARK_RED + countdown + "...");
				}
				if(countdown <= 0){
					pp.removePotionEffect(PotionEffectType.SPEED);
					core.starting.clear();
					pp.sendMessage(Tagg + ChatColor.GOLD + "Time's up.");
					pp.sendMessage(Tagg + ChatColor.GREEN + "Let the games begin...");
					this.Start();
					break;
				}
			}
			continue;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void Start(){
		Bukkit.getWorld(conf.getArena().getString("World")).playSound(
				new Location(Bukkit.getWorld(conf.getArena().getString("World")),conf.getArena().getDouble("X1"),conf.getArena().getDouble("Y1"),conf.getArena().getDouble("Z1")), 
				Sound.EXPLODE, 10, 1);
		for(int time = 0; time <= 600; time++){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(core, new Runnable() {
				public void run() { 
					timepassed++;
				}}, 0L, 20L);
			for(String all : core.playing){
				Player pla = Bukkit.getPlayer(all);
				if(timepassed == 300){
					pla.sendMessage(Tagg + ChatColor.GOLD + "5 minutes remaining...");
				} else
				if(timepassed == 420){
					pla.sendMessage(Tagg + ChatColor.GOLD + "3 minutes remaining...");
				} else
				if(timepassed == 540){
					pla.sendMessage(Tagg + ChatColor.RED + "1 minute remaining...");
					pla.sendMessage(Tagg + ChatColor.DARK_RED + "Sudden death!");
					pla.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 2));
					//TODO: Resend remaining players to spawnpoints.
				} else
				if(timepassed == 600){
					api.Tied();
				}
			}
			continue;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void returnPlayers(){
		for(String p : core.playing){
			Player bp = Bukkit.getPlayer(p);
			bp.teleport(core.locations.get(p));
		}
	}
	
	public void listChests(){
		int x1 = conf.getArena().getInt("X1");
		int y1 = conf.getArena().getInt("Y1");
		int z1 = conf.getArena().getInt("Z1");
		int x2 = conf.getArena().getInt("X2");
		int y2 = conf.getArena().getInt("Y2");
		int z2 = conf.getArena().getInt("Z2");
		for(int x = x1; x <= x2; x++){
			for(int y = y1; y <= y2; y++){
				for(int z = z1; z <= z2; z++){
					Block b = Bukkit.getWorld(conf.getArena().getString("World")).getBlockAt(x, y, z);
					if(b.getType() == Material.CHEST){
						Chest ch = (Chest)b;
						this.chests.add(ch);
						this.generateItems();
					}
				}
			}
		}
	}
	
	public void generateItems(){
		for(Chest chest : this.chests){
			Inventory inv = chest.getInventory();
			int numitem = rnd.nextInt(8);
			for(int x = 0; x <= numitem; x++){
				int rnditem = rnd.nextInt(154);
				if(rnditem <= 50){									//Offensive
					if(rnditem <= 6){
						inv.addItem(new ItemStack(Material.GOLD_SWORD,1));
					} else if ((6 < rnditem)&&(rnditem <= 15)){
						inv.addItem(new ItemStack(Material.IRON_SWORD,1));
					} else if ((15 < rnditem)&&(rnditem <= 33)){
						inv.addItem(new ItemStack(Material.WOOD_SWORD,1));
					} else if ((33 < rnditem)&&(rnditem <= 36)){
						inv.addItem(new ItemStack(Material.DIAMOND_SWORD,1));
					} else if ((36 < rnditem)&&(rnditem <= 40)){
						ItemStack wks = new ItemStack(Material.WOOD_SWORD, 1);
						wks.addEnchantment(Enchantment.KNOCKBACK, 3);
						inv.addItem(wks);
					} else if ((40 < rnditem)&&(rnditem < 45)){
						ItemStack ise = new ItemStack(Material.IRON_SWORD, 1);
						ise.addEnchantment(Enchantment.DAMAGE_ALL, 1);
						inv.addItem(ise);
					} else if ((45 < rnditem)&&(rnditem < 50)){
						ItemStack gse = new ItemStack(Material.GOLD_SWORD, 1);
						gse.addEnchantment(Enchantment.DAMAGE_ALL, 2);
						inv.addItem(gse);
					}
				} else if((50 < rnditem)&&(rnditem <= 100)){ 		//Food
					if(rnditem <= 54){
						inv.addItem(new ItemStack(Material.COOKED_BEEF,5));
					} else if ((54 < rnditem)&&(rnditem <= 60)){
						inv.addItem(new ItemStack(Material.APPLE,4));
					} else if ((60 < rnditem)&&(rnditem <= 72)){
						inv.addItem(new ItemStack(Material.MELON,6));
					} else if ((72 < rnditem)&&(rnditem <= 78)){
						inv.addItem(new ItemStack(Material.CARROT,3));
					} else if ((78 < rnditem)&&(rnditem <= 86)){
						inv.addItem(new ItemStack(Material.BREAD,3));
					} else if ((86 < rnditem)&&(rnditem <= 88)){
						inv.addItem(new ItemStack(Material.GOLDEN_APPLE,1));
					} else if ((88 < rnditem)&&(rnditem <= 92)){
						inv.addItem(new ItemStack(Material.POTATO,1));
					} else if ((92 < rnditem)&&(rnditem <= 100)){
						ItemStack gw = new ItemStack(Material.POTION);
						ItemMeta imgw = gw.getItemMeta();
						imgw.setDisplayName("Water");
						inv.addItem(gw);
					}
				} else if((100 < rnditem)&&(rnditem <= 154)){		//Armor
					if(rnditem <= 104){
						inv.addItem(new ItemStack(Material.GOLD_CHESTPLATE,1));
					} else if ((104 < rnditem)&&(rnditem <= 110)){
						inv.addItem(new ItemStack(Material.IRON_CHESTPLATE,1));
					} else if ((110 < rnditem)&&(rnditem <= 118)){
						inv.addItem(new ItemStack(Material.LEATHER_CHESTPLATE,1));
					} else if ((118 < rnditem)&&(rnditem <= 126)){
						inv.addItem(new ItemStack(Material.LEATHER_LEGGINGS,1));
					} else if ((126 < rnditem)&&(rnditem < 134)){
						inv.addItem(new ItemStack(Material.IRON_LEGGINGS,1));
					} else if ((134 < rnditem)&&(rnditem < 138)){
						inv.addItem(new ItemStack(Material.GOLD_LEGGINGS,1));
					} else if ((138 < rnditem)&&(rnditem < 146)){
						inv.addItem(new ItemStack(Material.IRON_BOOTS,1));
					} else if ((146 < rnditem)&&(rnditem < 120)){
						inv.addItem(new ItemStack(Material.GOLD_BOOTS,1));
					} else if ((120 < rnditem)&&(rnditem < 126)){
						inv.addItem(new ItemStack(Material.LEATHER_BOOTS,1));
					} else if ((132 < rnditem)&&(rnditem < 138)){
						inv.addItem(new ItemStack(Material.GOLD_HELMET,1));
					} else if ((138 < rnditem)&&(rnditem < 144)){
						inv.addItem(new ItemStack(Material.IRON_HELMET,1));
					} else if ((144 < rnditem)&&(rnditem < 150)){
						inv.addItem(new ItemStack(Material.LEATHER_HELMET,1));
					} else if ((150 < rnditem)&&(rnditem < 154)){
						inv.addItem(new ItemStack(Material.DIAMOND_CHESTPLATE,1));
					}
				}
			}
		}
	}
	
	public void giveSpectator(Player p){
		for(Player ww : Bukkit.getOnlinePlayers()){
			ww.hidePlayer(p);
		}
		p.setAllowFlight(true);
		p.setFlying(true);
	}
}
