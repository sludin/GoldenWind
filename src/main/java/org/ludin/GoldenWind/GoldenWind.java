package org.ludin.GoldenWind;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
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
    getConfig().options().copyDefaults();
    
    getLogger().info( "Plugin initiated");

    GoldenWindCommandExecutor gwe = new GoldenWindCommandExecutor(this);

    this.getCommand("gw").setExecutor( gwe );
    this.getCommand("goldenwind").setExecutor( gwe );
    this.getCommand("gw").setTabCompleter(gwe);
    this.getCommand("goldenwind").setTabCompleter(gwe);
    
    
    getServer().getPluginManager().registerEvents(new ExplodingMobs(this), this);
    getServer().getPluginManager().registerEvents(new SleepOnHalf(this), this);
    getServer().getPluginManager().registerEvents(new EnvironmentMods(this), this);
    //    getServer().getPluginManager().registerEvents(new PlayerLog(this), this);
    getServer().getPluginManager().registerEvents(new Items(this), this);
    getServer().getPluginManager().registerEvents(new LootTableListener(this), this);
        
    getLogger().info(getConfig().saveToString());

    sleeping = new ArrayList<Player>();

    Bukkit.getScheduler().runTaskTimer(this, new PlayerLog(this), 20*60, 20*60);
    
  }

  public ArrayList<Player> getSleepingList()
  {
    return sleeping;
  }
  
  @Override
  public void onDisable()
  {
    getLogger().info( "Plugin shutting down");
  }
}

