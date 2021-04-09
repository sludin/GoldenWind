package org.ludin.GoldenWind;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class GenericEventListener implements Listener {

  private GoldenWind plugin;
  
  public GenericEventListener( GoldenWind plugin )
  {
    this.plugin = plugin;
  }

  @EventHandler
  public void onAquireTrade( VillagerAcquireTradeEvent Event ) {
    System.out.println( GoldenWind.PREFIX + "VillagerAquireTradeEvent fired");
  }

  @EventHandler
  public void onReplinishTrade( VillagerReplenishTradeEvent event ) {
    System.out.println( GoldenWind.PREFIX + "VillagerReplenishTradeEvent fired");
  }

  @EventHandler
  public void onTradeSelect( TradeSelectEvent event ) {
    System.out.println(GoldenWind.PREFIX + "TradeSelectEvent fired");
  }

  /*  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    System.out.println( GoldenWind.PREFIX + "InventoryClickEvent fired");
  }

  @EventHandler
  public void onCraftItem(CraftItemEvent event) {
    System.out.println( GoldenWind.PREFIX + "CraftItemEvent fired");
  }
  */
    

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDamage( EntityDamageEvent event )
  {

    //    System.out.println( GoldenWind.PREFIX + "Something falling " + event.getEntityType().toString() );


    if (event.isCancelled()
        || event.getEntityType().equals(EntityType.PLAYER)
        || event.getEntityType().equals(EntityType.ENDERMAN) )
    {
      return;
    }

    // System.out.println( GoldenWind.PREFIX + "Cancelling" );
    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
      event.setCancelled(true);
    }
  }

}


