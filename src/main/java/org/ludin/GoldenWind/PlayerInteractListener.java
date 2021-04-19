package org.ludin.GoldenWind;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;

import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

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


public class PlayerInteractListener implements Listener {

  private GoldenWind plugin;
  
  public PlayerInteractListener( GoldenWind plugin )
  {
    this.plugin = plugin;
  }

  @EventHandler
  public void onInteract(PlayerInteractAtEntityEvent e) {

    Logger log = plugin.getLogger();
    
    if (e.getRightClicked() instanceof Villager) {

      Villager villager = (Villager) e.getRightClicked();

      if (villager.getProfession() == Villager.Profession.NONE)
        return;

      List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());

      recipes.clear();

      MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Material.GRASS_BLOCK), 1);
      recipe.addIngredient(new ItemStack(Material.DIAMOND, 64));
      recipes.add(recipe);

      villager.setRecipes(recipes);

      System.out.println(GoldenWind.PREFIX + "Villager trade");

    } else if (e.getRightClicked() instanceof WanderingTrader) {
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

        System.out.println(GoldenWind.PREFIX + "Trader value: " + trades);

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
              System.out.println(GoldenWind.PREFIX + "Added trident trade");
              break;

            case 1:
              n = (int) (Math.random() * 6) + 15;
              recipe = new MerchantRecipe(new ItemStack(Material.CREEPER_HEAD), 1);
              recipe.addIngredient(new ItemStack(Material.EMERALD, n));
              recipes.add(recipe);
              System.out.println(GoldenWind.PREFIX + "Added mob head trade");
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
              System.out.println(GoldenWind.PREFIX + "Added enchanted book trade");
              break;

          case 3:
              n = (int) (Math.random() * 4) + 13;
              int x = (int) (Math.random() * 3) + 7;
              
              recipe = new MerchantRecipe(new ItemStack(Material.EMERALD), x);
              recipe.addIngredient(new ItemStack(Material.DIAMOND, n));
              recipes.add(recipe);
              System.out.println(GoldenWind.PREFIX + "Added Diamond -> Emerald trade");
              break;

            default:
              System.out.println(GoldenWind.PREFIX + "Unexpected random trade: " + n);

          }

          trader.setRecipes(recipes);

        } else if (trades > 5) {
          // Clear trades
          List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
          recipes.clear();
          trader.setRecipes(recipes);

          System.out.println(GoldenWind.PREFIX + "Clearing trades");
        }

      } else {
        System.out.println(GoldenWind.PREFIX + "Trader value: not found");
      }

      System.out.println(GoldenWind.PREFIX + "Wandering trader trade");

      /***
       * 12-15 emeralds -> mending book 15-20 emeralds -> trident 7-8 emeralds -> mob
       * head
       */

    }

  }

}
