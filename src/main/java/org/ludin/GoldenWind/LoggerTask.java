package org.ludin.GoldenWind;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LoggerTask implements Runnable {

  private final GoldenWind plugin;

  public LoggerTask(GoldenWind plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
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
      }
        
      Location pos = p.getLocation();
      log.info("  " + p.getDisplayName() + ": "
               + (int)pos.getX() + " "
               + (int)pos.getY() + " "
               + (int)pos.getZ() + " "
               + world );
    }
  }
}
