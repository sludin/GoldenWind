package org.ludin.GoldenWind;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Monster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;


public class SpawnListener implements Listener {

  private Main instance;
  
  public SpawnListener( Main instance )
  {
    this.instance = instance;
  }
  
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    LivingEntity entity = event.getEntity();
    
    if (entity.getType() == EntityType.IRON_GOLEM) {
      //Bukkit.broadcastMessage("Blocking the spawn of an iron golem");
      event.setCancelled(true);
    } else if (entity instanceof Villager) {
      Villager v = (Villager) entity;
    }
    else if ( entity instanceof Monster )
    {
      ConfigurationSection config = instance.getConfig().getConfigurationSection("Explosion");

      //      Bukkit.broadcastMessage( config.getBoolean("Enabled") ? "True" : "False" );
      //      Bukkit.broadcastMessage( String.valueOf(config.getInt("Radius")) );
      
      if ( config.getBoolean("Enabled") == true )
      {
        Location loc = entity.getLocation();
        loc.subtract( 0, 1, 0 );
        Block b = loc.getBlock();
        //Bukkit.broadcastMessage( "Mob spawned on block: (" + entity.toString() + ":" + b.toString() + ")" );
        
        World w = loc.getWorld();

        int radius = config.getInt("Radius");
        Boolean fire = config.getBoolean("Fire");
        w.createExplosion( loc, radius, fire );
      
        event.setCancelled(true);
      }
      
    }
    


  }
  
  @EventHandler
  public void onInteract(PlayerInteractAtEntityEvent e)
  {
    if (!(e.getRightClicked() instanceof Villager)) return;
    
    Villager villager = (Villager) e.getRightClicked();
    
    if ( villager.getProfession() == Villager.Profession.NONE ) return;
    
    //Bukkit.broadcastMessage("Villager:");
    //Bukkit.broadcastMessage("  Profession: " + villager.getProfession().name());
    //Bukkit.broadcastMessage("  Type: " + villager.getVillagerType().name());
    
    
    
    List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
    
    recipes.clear();
    
    MerchantRecipe recipe = new MerchantRecipe( new ItemStack(Material.GRASS_BLOCK), 1);
    recipe.addIngredient( new ItemStack( Material.DIRT, 2));
    recipes.add(recipe);
    
    
    villager.setRecipes(recipes);
  }
  
}
