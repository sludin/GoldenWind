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
      ConfigurationSection config = plugin.getConfig().getConfigurationSection("Explosion");

      if ( config.getBoolean("Enabled") == true )
      {
        Location loc = entity.getLocation();
        loc.subtract( 0, 1, 0 );
        
        World w = loc.getWorld();

        int radius = config.getInt("Radius");
        Boolean fire = config.getBoolean("Fire");
        w.createExplosion( loc, radius, fire );
      
        event.setCancelled(true);

        System.out.println( GoldenWind.PREFIX + "Monster BOOM");
      }
      else
      {
        if ( entity instanceof Creeper )
        {
          Creeper c = (Creeper) entity;
          c.setMaxFuseTicks(20);
          c.setExplosionRadius(4);
        }
      }
      
    }
      /*
    else if ( entity instanceof WanderingTrader )
    {
        WanderingTrader trader = (WanderingTrader) entity;

        System.out.println(GoldenWind.PREFIX + "Wandering trader spawn");
        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

        recipes.clear();
                
        MerchantRecipe recipe = new MerchantRecipe( new ItemStack(Material.GRASS_BLOCK), 2);
        recipe.addIngredient( new ItemStack( Material.DIAMOND, 64));
        recipes.add(recipe);
    
        trader.setRecipes(recipes);

    }
       */
      
    
  }


}


