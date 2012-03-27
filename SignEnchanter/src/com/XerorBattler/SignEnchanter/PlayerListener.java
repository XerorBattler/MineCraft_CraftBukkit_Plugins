package com.XerorBattler.SignEnchanter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/* Example Template
 * By Adamki11s
 * HUGE Plugin Tutorial
 */

public class ExamplePlayerListener extends PlayerListener {
	
	public static Example plugin;
	
	public ExamplePlayerListener(Example instance) {
		plugin = instance;
	}

	public void onPlayerMove(PlayerMoveEvent event){
		
		Player player = event.getPlayer();
		Location playerLoc = player.getLocation();
		
		player.sendMessage("Your X Coordinates : " + playerLoc.getX());
		player.sendMessage("Your Y Coordinates : " + playerLoc.getY());
		player.sendMessage("Your Z Coordinates : " + playerLoc.getZ());
	}

}