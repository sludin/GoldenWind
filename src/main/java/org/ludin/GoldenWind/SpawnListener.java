package org.ludin.GoldenWind;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class SpawnListener implements Listener {

  private GoldenWind plugin;
  
  public SpawnListener( GoldenWind plugin )
  {
    this.plugin = plugin;
  }


  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    LivingEntity entity = event.getEntity();
  
    if (entity.getType() == EntityType.IRON_GOLEM) {
      System.out.println(GoldenWind.PREFIX + "Blocking iron golem spawn");
      event.setCancelled(true);
    }
    else if ( entity instanceof Monster )
    {
      if ( entity instanceof Creeper )
      {
        Creeper c = (Creeper) entity;
        c.setMaxFuseTicks(30);
        c.setExplosionRadius(3);
      }
      
    }
  }
}


