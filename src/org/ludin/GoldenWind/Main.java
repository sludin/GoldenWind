package org.ludin.GoldenWind;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;

import org.bukkit.configuration.ConfigurationSection;

public class Main extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        System.out.println(ChatColor.GREEN + "[GoldenWind Plugin] GoldenWind plugin initiated");

        this.getCommand("gw").setExecutor( new GoldenWindCommandExecutor(this) );
        this.getCommand("goldenwind").setExecutor( new GoldenWindCommandExecutor(this) );

        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);

        ConfigurationSection config = getConfig().getConfigurationSection("Explosion");
        int radius = config.getInt("Radius");
        Boolean fire = config.getBoolean("Fire");
        Boolean enabled = config.getBoolean("Enabled");

        System.out.println("[GoldenWind Plugin] Settings: Exploder: " + enabled + " " + String.valueOf(radius) + " " + fire );
        

          

    }

    @Override
    public void onDisable() {
        System.out.println("[GoldenWind Plugin] Fly plugin shutting down");
    }
}

