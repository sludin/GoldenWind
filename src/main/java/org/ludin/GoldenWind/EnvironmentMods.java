package org.ludin.GoldenWind;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

class TradeData extends MetadataValueAdapter {

  private int count;

  public TradeData(Plugin plugin) {
    super(plugin);
    count = 0;
  }

  public void invalidate() {
    count = 0;
  }

  public Object value() {
    return this;
  }

  public int asInt() {
    return count;
  }

  public void increment() {
    count++;
  }

}
  
public class EnvironmentMods extends GoldenWindCommand implements Listener {

  public final static String CONFIG_NAME = "EnvironmentMods";

  class CreeperBuff extends GoldenWindCommand {

    public CreeperBuff(GoldenWind plugin) {
      super(plugin, CONFIG_NAME);
      registerCommand("ticks", this::ticks, 1, new String[]{ "<int>" });
      registerCommand("radius", this::radius, 1, new String[]{ "<double>" });
    }

    boolean ticks( String[] args )
    {
      try
      {
        int ticks = Integer.parseInt(args[0]);
        config.set( "CreeperBuff.FuseTicks", ticks);
      }
      catch( NumberFormatException e )
      {
        return false;
      }

      return true;
    }

    boolean radius( String[] args )
    {
      try
      {
        double radius = Double.parseDouble(args[0]);
        if ( radius > 32 )
        {
          radius = 32;
        }
        config.set( "CreeperBuff.DamageRadius", radius);
      }
      catch( NumberFormatException e )
      {
        return false;
      }

      return true;
    }

    
  }
  
  
  public EnvironmentMods( GoldenWind plugin )
  {
    super(plugin, CONFIG_NAME);
    
    registerTFCommand( "blockgolems","BlockIronGolems" );
    registerTFCommand( "blocktrades", "BlockVillagerTrades" );
    registerTFCommand( "modifytraders", "ModifyTraderTrades" );
    registerTFCommand( "blockmobfalldamage", "BlockMobFallDamage" );
    registerSubCommand( "creeperbuff", new CreeperBuff(plugin) );
  }

  // BlockIronGolems
  // CreeperBuff
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    LivingEntity entity = event.getEntity();

    boolean blockIronGolems = config.getBoolean( "BlockIronGolems" );
    int creeperFuseTicks = config.getInt( "CreeperBuff.FuseTicks" );
    int creeperDamageRadius = config.getInt( "CreeperBuff.DamageRadius" );

    
    if (entity.getType() == EntityType.IRON_GOLEM && blockIronGolems ) {
      log.info( "Blocking iron golem spawn");
      event.setCancelled(true);
    }
    else if ( entity instanceof Monster )
    {
      if ( entity instanceof Creeper )
      {
        Creeper c = (Creeper) entity;
        c.setMaxFuseTicks(creeperFuseTicks);
        c.setExplosionRadius(creeperDamageRadius);
      }
      
    }
  }

  // BlockMobFallDamage
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDamage( EntityDamageEvent event )
  {
    boolean blockMobFallDamage = config.getBoolean( "BlockMobFallDamage" );

    if (event.isCancelled()
        || ! blockMobFallDamage
        || event.getEntityType().equals(EntityType.PLAYER)
        || event.getEntityType().equals(EntityType.ENDERMAN) )
    {
      return;
    }

    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
      event.setCancelled(true);
    }
  }

  // BlockVillagerTrades
  // ModifyTraderTrades
  @EventHandler
  public void onInteract(PlayerInteractAtEntityEvent e)
  {

    Logger log = plugin.getLogger();

    boolean blockVillagerTrades = config.getBoolean( "BlockVillagerTrades" );
    boolean modifyTraderTrades = config.getBoolean( "ModifyTraderTrades" );
    
    if (e.getRightClicked() instanceof Villager && blockVillagerTrades )
    {

      Villager villager = (Villager) e.getRightClicked();

      if (villager.getProfession() == Villager.Profession.NONE)
        return;

      List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());

      recipes.clear();

      MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.GRASS_BLOCK), 1);
      recipe.addIngredient(new ItemStack(Material.DIAMOND, 64));
      recipes.add(recipe);

      villager.setRecipes(recipes);

      log.info( "Villager trade");

    }
    else if (e.getRightClicked() instanceof WanderingTrader && modifyTraderTrades )
    {
      WanderingTrader trader = (WanderingTrader) e.getRightClicked();

      if (!trader.hasMetadata("trades")) {
        TradeData td = new TradeData(plugin);
        trader.setMetadata("trades", td);
      }

      List<MetadataValue> mvs = trader.getMetadata("trades");

      if (mvs.size() != 0) {
        TradeData td = (TradeData) mvs.get(0);
        td.increment();

        int trades = td.asInt();

        log.info( "Trader value: " + trades);

        if (trades == 1) {
          // Init with the random trade

          List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

          recipes.clear();

          int n = (int) (Math.random() * 4);

          switch (n) {

            case 0:
              n = (int) (Math.random() * 4) + 12;
              MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.TRIDENT), 1);
              recipe.addIngredient(new ItemStack(Material.EMERALD, n));
              recipes.add(recipe);
              log.info( "Added trident trade");
              break;

            case 1:
              n = (int) (Math.random() * 6) + 15;
              recipe = new MerchantRecipe(new ItemStack(Material.CREEPER_HEAD), 1);
              recipe.addIngredient(new ItemStack(Material.EMERALD, n));
              recipes.add(recipe);
              log.info( "Added mob head trade");
              break;

            case 2:
              n = (int) (Math.random() * 2) + 7;
              ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
              EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
              meta.addStoredEnchant(Enchantment.MENDING, 5, true);
              book.setItemMeta(meta);

              recipe = new MerchantRecipe(book, 1);
              recipe.addIngredient(new ItemStack(Material.EMERALD, n));
              recipes.add(recipe);
              log.info( "Added enchanted book trade");
              break;

          case 3:
              n = (int) (Math.random() * 4) + 13;
              int x = (int) (Math.random() * 3) + 7;
              
              recipe = new MerchantRecipe(new ItemStack(Material.EMERALD), x);
              recipe.addIngredient(new ItemStack(Material.DIAMOND, n));
              recipes.add(recipe);
              log.info( "Added Diamond -> Emerald trade");
              break;

            default:
              log.info( "Unexpected random trade: " + n);

          }

          trader.setRecipes(recipes);

        } else if (trades > 5) {
          // Clear trades
          List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
          recipes.clear();
          trader.setRecipes(recipes);

          log.info( "Clearing trades");
        }

      } else {
        log.info( "Trader value: not found");
      }

      log.info( "Wandering trader trade");

      /***
       * 12-15 emeralds -> mending book 15-20 emeralds -> trident 7-8 emeralds -> mob
       * head
       */

    }

  }


    
}

