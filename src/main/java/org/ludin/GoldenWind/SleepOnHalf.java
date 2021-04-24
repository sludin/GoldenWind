package org.ludin.GoldenWind;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepOnHalf extends GoldenWindCommand implements Listener
{

  private static final String CONFIG_NAME = "SleepOnHalf";
  
  public SleepOnHalf( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);

    registerEnableDisableCommand();
    registerCommand( "ratio", this::ratio, 1 );
  }

  @EventHandler
  public void onBedEnterEvent( PlayerBedEnterEvent e ) 
  {
    if ( ! enabled() )
    {
      return;
    }

    double ratio = config.getDouble("Ratio");

    if( e.getBedEnterResult() == BedEnterResult.OK )
    {
      ArrayList<Player> sleeping = plugin.getSleepingList();
      
      sleeping.add(e.getPlayer());

      int nOnline = plugin.getServer().getOnlinePlayers().size();
      int nAsleep = sleeping.size();

      double ratioAsleep = ((double) nAsleep / (double) nOnline) * 100.0;
      
      log.info( "Player went to sleep (" + e.getPlayer().getName() + ")");

      if (ratioAsleep >= ratio) {

        log.info( ratioAsleep + "% players asleep. Skipping to morning");

        World world = plugin.getSleepingList().get(0).getWorld();
        world.setTime(0);

        for ( Player p: sleeping )
        {
            p.setHealth(20);
            p.setFoodLevel(20);
        }
        
      }
    }
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
