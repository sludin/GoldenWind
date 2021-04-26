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
  
  
}

