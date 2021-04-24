package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerMask extends GoldenWindCommand implements Listener {

  public final static String CONFIG_NAME = "PlayerMask";
  
  public PlayerMask( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);
    registerEnableDisableCommand();
    
  }

  @EventHandler
  public void onPlayerJoint(PlayerJoinEvent event)
  {
    if ( ! enabled() ) {
      return;
    }

    Player player = event.getPlayer();

    String name = player.getPlayerListName();

    log.info("Player joined: " + name);

    if (name.equalsIgnoreCase("Shaztsu")) {
      event.setJoinMessage("");
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event)
  {
    if ( ! enabled() )
    {
      return;
    }
    
    Player player = event.getPlayer();

    String name = player.getPlayerListName();
    
    log.info("Player quit: " + name );

    if ( name.equalsIgnoreCase("Shaztsu") )
    {
      event.setQuitMessage( "" );
    }
  }

}

