package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
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

    //    System.out.println( "loc=" + x + "," + y + "," + z
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


public class Exploder implements Listener {

  private GoldenWind plugin;
  private static List<Area> areas = new ArrayList<>();
  
  public Exploder( GoldenWind plugin )
  {
    this.plugin = plugin;
    Logger log = plugin.getLogger();

    ConfigurationSection config = plugin.getConfig().getConfigurationSection("Explosion");
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

  

  
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    LivingEntity entity = event.getEntity();
  
    if ( entity instanceof Monster )
    {
      ConfigurationSection config = plugin.getConfig().getConfigurationSection("Explosion");
      
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

          System.out.println(GoldenWind.PREFIX + "Monster BOOM");
        }
      }
    }
  }
}

