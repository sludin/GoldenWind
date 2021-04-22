package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


class Area {
  public Area(Location from, Location to)
  {

    x1 = from.getX();
    y1 = from.getY();
    z1 = from.getZ();
    x2 = to.getX();
    y2 = to.getY();
    z2 = to.getZ();
   
    if ( x2 < x1 )
    {
      double tmp = x1;
      x1 = x2;
      x2 = tmp;
    }
      
    if ( y2 < y1 )
    {
      double tmp = y1;
      y1 = y2;
      y2 = tmp;
    }

    if ( z2 < z1 )
    {
      double tmp = z1;
      z1 = z2;
      z2 = tmp;
    }
  }
  
  private double x1;
  private double x2;
  private double y1;
  private double y2;
  private double z1;
  private double z2;
  
  public boolean inArea( Location loc )
  {
    double x = loc.getX();
    double y = loc.getY();
    double z = loc.getZ();

    //    log.info( "loc=" + x + "," + y + "," + z
    //                        + " from=" + x1 + "," + y1 + "," + z1
    //                    + " to="  + x2 + "," + y2 + "," + z2 );

    if ( x >= x1 && x <= x2 )
    {
      if ( y >= y1 && y <= y2 )
      {
        if (z >= z1 && z <= z2) {
          return true;
        }
      }
    }

    return false;
  }
}


public class ExplodingMobs extends GoldenWindCommand implements Listener {

  private static List<Area> areas = new ArrayList<>();

  public final static String CONFIG_NAME = "ExplodingMobs";

  
  public ExplodingMobs( GoldenWind plugin )
  {
    super(plugin);

    registerCommand( "enable", this::enable );
    registerCommand( "disable", this::disable );
    registerCommand( "radius", this::radius, 1, new String[]{"<double>"} );
    registerTFCommand( "fire", CONFIG_NAME, "Fire" );

    initArea();
  }
  
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    LivingEntity entity = event.getEntity();
  
    if ( entity instanceof Monster )
    {
      ConfigurationSection config = plugin.getConfig().getConfigurationSection("ExplodingMobs");
      
      if ( config.getBoolean("Enabled") == true )
      {
        Location loc = entity.getLocation();

        boolean inArea = false;
        for ( Area a: areas )
        {
          inArea |= a.inArea(loc);
        }

        if ( inArea )
        {
          loc.subtract(0, 1, 0);

          World w = loc.getWorld();

          int radius = config.getInt("Radius");
          Boolean fire = config.getBoolean("Fire");
          w.createExplosion(loc, radius, fire);

          event.setCancelled(true);

          log.info( "Monster BOOM");
        }
      }
    }
  }


  private void initArea()
  {
    ConfigurationSection config = plugin.getConfig().getConfigurationSection(CONFIG_NAME);
    if ( config != null )
    {
      List<?> areasConfig = config.getList("Areas");
      if ( areasConfig != null )
      {
        World normal = plugin.getServer().getWorlds().get(0);
        
        for (Object a : areasConfig) {
          String s = (String) a;

          //          log.info(s);
          
          String[] parts = s.split(" +");
          if ( parts.length == 2 )
          {
            //            log.info(parts[0] + " - " + parts[1]);

            String[] coordsFrom = parts[0].split(",");
            String[] coordsTo = parts[1].split(",");

            if ( coordsFrom.length == 3 && coordsTo.length == 3 )
            {
              try
              {
                Area area = new Area( new Location( normal,
                                                    Double.parseDouble(coordsFrom[0]),
                                                    Double.parseDouble(coordsFrom[1]),
                                                    Double.parseDouble(coordsFrom[2])),
                                      new Location( normal,
                                                    Double.parseDouble(coordsTo[0]),
                                                    Double.parseDouble(coordsTo[1]),
                                                    Double.parseDouble(coordsTo[2])));
                areas.add(area);
              }
              catch( NumberFormatException e )
              {
                log.info("Parse error for area: " + s);
              }
            }
            
          }
          else
          {
            for ( String part: parts )
            {
              log.info("Part: " + part);
            }
          }
        }
      }
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

  boolean radius( String[] args )
  {
    try
    {
      float radius = Float.parseFloat(args[0]);
        
      if ( radius < 0 || radius > 64 )
      {
        return false;
      }
      config.set( CONFIG_NAME + ".Radius", radius );
    }
    catch( java.lang.NumberFormatException e )
    {
      return false;
    }

    return true;
  }


}

