package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent.State;

public class LootTableListener implements Listener {

  private GoldenWind plugin;
  private Logger log;
  
  public LootTableListener( GoldenWind plugin )
  {
    this.plugin = plugin;
    log = plugin.getLogger();
  }
  
  @EventHandler
  public void onLootGenerateEvent(LootGenerateEvent event)
  {
    log.info( "Loot Generation:" );
    log.info( "  " + event.getEntity().toString() );
    log.info( "  " + event.getInventoryHolder().toString() );
    log.info( "  " + event.getLoot().toString() );
    log.info( "  " + event.getLootContext().toString() );
    log.info( "  " + event.getLootTable().toString() );
    
  }

  @EventHandler
  public void onEntityDeath(EntityDeathEvent event)
  {
    log.info( "Entity Death:" );
    log.info( "  Entity: " + event.getEntity().getType().toString() );
    log.info( "  Drops: " + event.getDrops().toString() );
    log.info( "  Exp: " + event.getDroppedExp() );
  }

  @EventHandler
  public void onPlayerFish(PlayerFishEvent event)
  {
    if ( event.getState() == State.CAUGHT_FISH )
    {
      log.info( "Player Fish:" );
      log.info( "  Caught: " + event.getCaught() != null ? ((Item)event.getCaught()).getItemStack().toString() : "null" );
      log.info( "  Exp: " + event.getExpToDrop() );
      log.info( "  State: " + event.getState().toString() );
    }
    else
    {
      log.info( "Player Fish:" );
      log.info( "  Exp: " + event.getExpToDrop() );
      log.info( "  State: " + event.getState().toString() );
    }
    
  }
  
}

