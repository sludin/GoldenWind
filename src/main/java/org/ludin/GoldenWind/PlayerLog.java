package org.ludin.GoldenWind;

import java.util.Collection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerLog extends GoldenWindCommand implements Runnable
{
  private final String CONFIG_NAME = "PlayerLog";

  public PlayerLog( GoldenWind plugin )
  {
    super(plugin);
    
    registerCommand( "enable", this::enable );
    registerCommand( "disable", this::disable );
  }

  @Override
  public void run() {

    ConfigurationSection config = plugin.getConfig().getConfigurationSection(CONFIG_NAME);
    if ( config.getBoolean("Enabled") != true )
    {
      return;
    }
    
    Server server = Bukkit.getServer();
    final Collection<? extends Player> players = server.getOnlinePlayers();

    if ( players.isEmpty() )
    {
      return;
    }
    
    Logger log = plugin.getLogger();

    log.info("Player Positions:");
    for( Player p : players )
    {
      World.Environment env = p.getWorld().getEnvironment();
      String world = "Unknown";
      switch (env) {
      case NETHER:
        world = "Nether";
        break;
      case NORMAL:
        world = "Normal";
        break;
      case THE_END:
        world = "End";
        break;
      case CUSTOM:
        world = "Custom";
        break;
      }
        
      Location pos = p.getLocation();
      log.info("  " + p.getDisplayName() + ": "
               + (int)pos.getX() + " "
               + (int)pos.getY() + " "
               + (int)pos.getZ() + " "
               + world );
    }
  }


  boolean enable( String[] args )
  {
    config.set( CONFIG_NAME + ".Enabled", true);
    return true;
  }
    
  boolean disable( String[] args )
  {
    config.set( CONFIG_NAME + ".Enabled", false);
    return true;
  }

}
