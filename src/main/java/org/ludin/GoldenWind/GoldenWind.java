package org.ludin.GoldenWind;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GoldenWind extends JavaPlugin
{
  public static final String PREFIX = "[GoldenWind] ";

  private static ArrayList<Player> sleeping;

  @Override
  public void onEnable()
  {
    saveDefaultConfig();
    System.out.println( GoldenWind.PREFIX + "Plugin initiated");
    
    this.getCommand("gw").setExecutor( new GoldenWindCommandExecutor(this) );
    this.getCommand("goldenwind").setExecutor( new GoldenWindCommandExecutor(this) );
    
    getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
    getServer().getPluginManager().registerEvents(new GenericEventListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
    getServer().getPluginManager().registerEvents(new SleepListener(this), this);
        
        
    ConfigurationSection config = getConfig().getConfigurationSection("Explosion");
    int radius = config.getInt("Radius");
    Boolean fire = config.getBoolean("Fire");
    Boolean enabled = config.getBoolean("Enabled");
    System.out.println( GoldenWind.PREFIX + "Settings: Exploder: " + enabled + " " + String.valueOf(radius) + " " + fire );

    sleeping = new ArrayList<Player>();
    
  }

  public ArrayList<Player> getSleepingList()
  {
    return sleeping;
  }
  
  @Override
  public void onDisable()
  {
    System.out.println( GoldenWind.PREFIX + "Plugin shutting down");
  }
}

