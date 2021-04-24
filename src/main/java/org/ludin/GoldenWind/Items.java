package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
}

