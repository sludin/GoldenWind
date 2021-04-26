package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.world.LootGenerateEvent;


public class Items extends GoldenWindCommand implements Listener {

  public final static String CONFIG_NAME = "Items";
  
  public Items( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);

    registerTFCommand( "mendingnetherite", "MendingNetherite" );
    registerTFCommand( "blockmending", "BlockMending" );
  }

  // MendingNetherite
  @EventHandler
  public void onSmithItemEvent(SmithItemEvent event)
  {
    if ( config.getBoolean( "MendingNetherite" ) != true )
    {
      return;
    }

    SmithingInventory inv = event.getInventory();

    Recipe recipe = inv.getRecipe();

    ItemStack result = inv.getResult();

    
    Material m = result.getType();
    
    if ( m == Material.NETHERITE_AXE        ||
         m == Material.NETHERITE_BOOTS      ||
         m == Material.NETHERITE_CHESTPLATE ||
         m == Material.NETHERITE_HELMET     ||
         m == Material.NETHERITE_HOE        ||
         m == Material.NETHERITE_LEGGINGS   ||
         m == Material.NETHERITE_PICKAXE    ||
         m == Material.NETHERITE_SHOVEL     ||
         m == Material.NETHERITE_SWORD )
    {
      result.addEnchantment(Enchantment.MENDING, 1);
      inv.setResult​(result);
    }
    
    
  }

  @EventHandler
  public void onPlayerFish(PlayerFishEvent event)
  {
    if ( config.getBoolean( "BlockMending" ) != true )
    {
      return;
    }

    if ( event.getState() == State.CAUGHT_FISH )
    {
      if ( event.getCaught() != null )
      {
        ItemStack stack = ((Item)event.getCaught()).getItemStack();

        log.info( "Player Fish:" );
        log.info( "  Caught: " + event.getCaught() != null ? ((Item)event.getCaught()).getItemStack().toString() : "null" );
        log.info( "  Exp: " + event.getExpToDrop() );
        log.info( "  State: " + event.getState().toString() );

        if ( stack.getType() == Material.ENCHANTED_BOOK )
        {
          if ( isMendingBook(stack) )
          {
            log.info("Mending Book found while fishing.  Cancelling");
            stack.setType(Material.COBBLESTONE);
            event.setCancelled(true);
            return;
          }
          
        }
        
                    
      }
      
    }
    
  }

    
  @EventHandler
  public void onLootGenerateEvent(LootGenerateEvent event)
  {
    log.info( "Loot Generation:" );

    if (event.getEntity() != null) {
      log.info("  " + event.getEntity().toString());
    }
    log.info( "  " + event.getInventoryHolder().toString() );
    log.info( "  " + event.getLoot().toString() );
    log.info( "  " + event.getLootContext().toString() );
    log.info( "  " + event.getLootTable().toString() );

    List<ItemStack> loot = event.getLoot();
    /*
    loot.stream().forEach( item -> {
        if ( item.getType() == Material.IRON_INGOT ||
             item.getType() == Material.APPLE ||
             item.getType() == Material.GOLD_INGOT ||
             item.getType() == Material.BREAD ||
             item.getType() == Material.POTATO 

           )
        {
          event.getLoot().remove(item);
        }
      });

    log.info( "  " + event.getLoot().toString() );
     */
  }

  boolean isMendingBook( ItemStack item )
  {
    //    log.info("Checking isMendingBook");
    if ( item.getType() == Material.ENCHANTED_BOOK )
    {
      //      log.info("It's a book!");
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
      Map<Enchantment, Integer> enchants = meta.getStoredEnchants();

      //      log.info("nEnchants: " + enchants.size());
      //      enchants.keySet().stream().forEach( e -> log.info( "Enchant: " + e.toString() ) );
      //      enchants.keySet().stream().forEach( e -> log.info( "Enchant: " + e.hashCode() ) );
      //      enchants.keySet().stream().forEach( e -> log.info( "Enchant: " + e.getKey() ) );


      List<Enchantment> mending = enchants.keySet().stream()
                                  .filter(enchant -> enchant.equals(Enchantment.MENDING))
                                  .collect(Collectors.toList());

      //      log.info( "Size: " + mending.size());
      if ( mending.size() > 0 )
      {
        log.info("Mending book");
        return true;
      }
    }

    return false;
  }
  
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event)
  {
    if ( config.getBoolean( "BlockMending" ) != true )
    {
      return;
    }

    if ( event.getEntityType() != EntityType.PLAYER ||
         ((Player)event.getEntity()).getName().equalsIgnoreCase("shaztsu") )
    {

      List<ItemStack> stacks = event.getDrops();

      List<ItemStack> books = stacks.stream()
                              .filter( is -> isMendingBook(is) )
                              .collect(Collectors.toList());

      books.stream().forEach(b -> event.getDrops().remove(b));

      if ( books.size() > 0 )
      {
        log.info("Entity Death:");
        log.info("  Entity: " + event.getEntity().getType().toString());
        log.info("  Drops: " + event.getDrops().toString());
        log.info("  Exp: " + event.getDroppedExp());
        log.info("  Msg: Removing mending book");
      }

      
    }

    
  }


  @EventHandler
  public void onChestOpen(InventoryOpenEvent event)
  {
    if ( config.getBoolean( "BlockMending" ) != true )
    {
      return;
    }
   
    Inventory inv = event.getInventory();
    
    if(inv.getType().equals(InventoryType.CHEST))
    {
      if ( inv.getHolder() instanceof DoubleChest )
      {
        return;
      }
      
      Lootable chest = (Lootable)inv.getHolder();
      LootTable table = chest.getLootTable();
      if ( table != null )
      {
        log.info(table.toString());
      }
      

      log.info("Chest opened: " + event.getPlayer().getName());
      log.info("Loot table: " + (table == null ? "null" : table.toString()));

      if ( inv.contains( Material.ENCHANTED_BOOK ) )
      {
        log.info("Loot table: " + (table == null ? "null" : table.toString()));



        HashMap<Integer, ? extends ItemStack> stacks = inv.all(Material.ENCHANTED_BOOK);

        
        //        books.entrySet().stream().forEach(e -> log.info("  " + e.getKey() + ": " + e.getValue()));
        List<Integer> books = stacks.entrySet().stream()
                              .filter( e -> isMendingBook(e.getValue()) )
                              .map( e -> e.getKey() )
                              .collect( Collectors.toList() );

        if ( books.size() > 0 )
        {
          log.info("Removing mending books from chest");
          books.stream().forEach(b -> inv.remove(stacks.get(b)));
        }


      }
      
    }
       
  }

}

