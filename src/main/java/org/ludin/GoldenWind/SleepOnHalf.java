package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.World;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class SleepOnHalf extends GoldenWindCommand implements Listener
{

  private static final String CONFIG_NAME = "SleepOnHalf";
  
  public SleepOnHalf( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);

    registerEnableDisableCommand();
    registerCommand( "ratio", this::ratio, 1 );
  }

  private void checkForSleep( boolean leaving )
  {
    ArrayList<Player> sleeping = plugin.getSleepingList();
    double ratio = config.getDouble("Ratio");

    int nOnline = plugin.getServer().getOnlinePlayers().stream()
                  .filter(p -> p.getWorld().getEnvironment() == World.Environment.NORMAL)
                  .collect(Collectors.toList())
                  .size();

    if ( leaving && nOnline > 0)
    {
      nOnline--;
      if ( nOnline == 0 )
      {
        return;
      }
    }
    
    int nAsleep = sleeping.size();

    double ratioAsleep = ((double) nAsleep / (double) nOnline) * 100.0;

    if (ratioAsleep >= ratio) {

      log.info(ratioAsleep + "% players asleep. Skipping to morning");

      World world = plugin.getSleepingList().get(0).getWorld();
      world.setTime(0);

      for (Player p : sleeping) {
        p.setHealth(20);
        p.setFoodLevel(20);
      }

    }
  }
  
  @EventHandler
  public void onBedEnterEvent( PlayerBedEnterEvent e ) 
  {
    if ( ! enabled() )
    {
      return;
    }


    if( e.getBedEnterResult() == BedEnterResult.OK )
    {
      ArrayList<Player> sleeping = plugin.getSleepingList();
      
      sleeping.add(e.getPlayer());
      log.info( "Player went to sleep (" + e.getPlayer().getName() + ")");

      checkForSleep(false);
    }
  }

  @EventHandler
  public void onPlayerQuit( PlayerQuitEvent event )
  {
    if ( ! enabled() )
    {
      return;
    }

    Player player = event.getPlayer();
		plugin.getSleepingList().remove(player);

    plugin.getServer().getOnlinePlayers().stream()
      .filter(p -> p.getWorld().getEnvironment() == World.Environment.NORMAL)
      .forEach(p -> log.info("On quit: " + p.getName()));
    
    
    checkForSleep(true);
  }
  

  @EventHandler
  public void onPlayerPortal( PlayerPortalEvent event )
  {
    if ( ! enabled() )
    {
      return;
    }

    Player player = event.getPlayer();
		plugin.getSleepingList().remove(player);

    plugin.getServer().getOnlinePlayers().stream()
      .filter(p -> p.getWorld().getEnvironment() == World.Environment.NORMAL)
      .forEach(p -> log.info("On portal: " + p.getName()));
    
    checkForSleep(true);
  }
  
  @EventHandler
  public void onBedExitEvent( PlayerBedLeaveEvent e ) 
  {
    if ( ! enabled() )
    {
      return;
    }

    log.info( "Player got up (" + e.getPlayer().getName() + ")");
		plugin.getSleepingList().remove(e.getPlayer());
  }


  boolean ratio( String[] args )
  {
    try
    {
      double ratio = Double.parseDouble(args[0]);
        
      if ( ratio < 0 || ratio > 100 )
      {
        return false;
      }
      config.set( "Ratio", ratio );
    }
    catch( java.lang.NumberFormatException e )
    {
      return false;
    }

    return true;
  }


}
