package org.ludin.GoldenWind;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.World;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class SleepListener implements Listener
{

  private GoldenWind plugin;
  
  public SleepListener( GoldenWind plugin )
  {
    this.plugin = plugin;
  }

  @EventHandler
  public void onBedEnterEvent( PlayerBedEnterEvent e ) 
  {
    if( e.getBedEnterResult() == BedEnterResult.OK )
    {
      ArrayList<Player> sleeping = plugin.getSleepingList();
      
      sleeping.add(e.getPlayer());

      int nOnline = plugin.getServer().getOnlinePlayers().size();
      int nAsleep = sleeping.size();
      int half = ((int) Math.ceil(((double) nOnline / 2)));

      System.out.println( GoldenWind.PREFIX + "Player went to sleep (" + e.getPlayer().getName() + ")");

      if (nAsleep >= half) {

        System.out.println( GoldenWind.PREFIX + "Half of players asleep. Skipping to morning");

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
    System.out.println( GoldenWind.PREFIX + "Player got up (" + e.getPlayer().getName() + ")");
		plugin.getSleepingList().remove(e.getPlayer());
  }

}
