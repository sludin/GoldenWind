package org.ludin.GoldenWind;

import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class BlockAutoFish extends GoldenWindCommand implements Listener
{
  public static final String CONFIG_NAME = "BlockAutoFish";
  
  BlockAutoFish( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);
    registerEnableDisableCommand();
    registerIntCommand( "threshold", "Threshold" );
    registerDoubleCommand( "range", "Range" );
  }

  
  @EventHandler
	public void onFish(PlayerFishEvent e)
  {
    if ( ! enabled() )
    {
      log.info("BlockAutoFish not enabled");
      return;
    }
    

    Player player = e.getPlayer();
    
    if ( player == null )
    {
      return;
    }
    
    GoFish gofish = GoFish.lookup( player, config.getDouble( "Range" ) );

    FishHook hook = e.getHook();
    if (hook == null)
    {
      return;
    }

    Location hookLoc = hook.getLocation();

    log.info("Hook position: " + locToString(hookLoc) );
    if ( gofish.isBlockedSpot( hookLoc ) )
    {
      log.info( "Blocking fishing in a blacklisted spot: player = " + player.getPlayerListName() );
      e.setCancelled(true);
      hook.remove(); 
      e.setExpToDrop(0);
      return;
    }

    if (e.getCaught() != null)
    {
      log.info("Caught fish. Registering: " + player.getPlayerListName() + " " + locToString(hookLoc));
      gofish.caughtFish( hookLoc );
      return;
    }

    

    log.info( "Same spot count: " + gofish.getSameSpot() + " " + player.getPlayerListName() + " " + locToString(hookLoc) );
    int threshold = config.getInt( "Threshold" );
    boolean sameSpot = gofish.getSameSpot() >= threshold;

    if ( sameSpot )
    {
      System.out.println("Blocking fishing in the same spot." +
                         " Count = " + gofish.getSameSpot()   +
                         " Player: " + player.getPlayerListName() );
      e.setCancelled(true);
      hook.remove(); 
      e.setExpToDrop(0);
      gofish.block(hookLoc);
      gofish.reset();
      return;

    }
    

  }


  static String locToString( Location loc )
  {
    String s = "{ x=";
    s += loc.getX();
    s += " ,y=";
    s += loc.getY();
    s += " ,z=";
    s += loc.getZ();
    s += " }";
    return s;
  }


}


class GoFish
{
  static HashMap<String, GoFish> casts = new HashMap<>();

  double rangeThreshold;
  private int sameSpot = 0;
  Location previousLoc = null;
  private List<Location> blockedLocations = new ArrayList<>();

  
  public GoFish( Player p, double range )
  {
    casts.put(p.getPlayerListName(), this);
    rangeThreshold = range;
  }

  public static GoFish lookup(Player p, double range)
  {
    
    if (casts.containsKey( p.getPlayerListName() ) )
    {
      return casts.get(p.getPlayerListName() );
    }
    
    return new GoFish(p, range);
  }


  public void caughtFish( Location loc ) {
    if ( previousLoc == null )
    {
      previousLoc = loc;
    }
    Location tloc = loc.clone();
    tloc.setY( previousLoc.getY() );

    System.out.println( "Distance: " + previousLoc.distance(tloc) );
    System.out.println( "Same spot: " + sameSpot);
    System.out.println( "Location: " + BlockAutoFish.locToString( loc ));
    System.out.println( "Prev Location: " + BlockAutoFish.locToString( previousLoc ));
    
    
    if ( previousLoc.distance(tloc) <= rangeThreshold)
    {
      incSameSpot();
    }
    else
    {
      setSameSpot(1);
      previousLoc = loc;
    }
  }
  


  public int incSameSpot()
  {
    sameSpot++;
    return sameSpot;
  }

    
  public int getSameSpot()
  {
    return sameSpot;
  }

  public void setSameSpot(int n)
  {
    sameSpot = n;
  }

  public void reset()
  {
    sameSpot = 0;
    previousLoc = null;
  }

  public void block(Location loc)
  {
    if ( blockedLocations.size() > 5 )
    {
      blockedLocations.remove(0);
    }
    blockedLocations.add(loc);
  }

  public boolean isBlockedSpot(Location loc)
  {
    for ( Location bloc: blockedLocations )
    {
      Location tloc = bloc.clone();
      tloc.setY( loc.getY() );

      if (tloc.distance(loc) <= rangeThreshold)
      {
        System.out.println( "Threshold: " + rangeThreshold );
        System.out.println( "Distance: " + tloc.distance(loc) );
        System.out.println( "Location: " + BlockAutoFish.locToString( loc ));
        System.out.println( "Prev Location: " + BlockAutoFish.locToString( bloc ));
        System.out.println( "Test Location: " + BlockAutoFish.locToString( tloc ));

        System.out.println( "Close enough" );
        return true;
      }

      //      System.out.println( "Too far" );

    }
    
    return false;

  }



}


