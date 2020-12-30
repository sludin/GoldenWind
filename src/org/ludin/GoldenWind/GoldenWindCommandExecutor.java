package org.ludin.GoldenWind;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class GoldenWindCommandExecutor implements CommandExecutor
{

  private Main plugin;
  
  public GoldenWindCommandExecutor( Main plugin )
  {
    this.plugin = plugin;
  }
  
  @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ( args.length == 0 ) return false;
                             
    
    if ( args[0].equalsIgnoreCase("explode") && args.length > 1 )
    {
      if ( args[1].equalsIgnoreCase("enable") && args.length > 2 )
      {

        if ( args[2].equalsIgnoreCase("true") )
        {
          plugin.getConfig().set("Explosion.Enabled", true);
        }
        else if ( args[2].equalsIgnoreCase("false") )
        {
          plugin.getConfig().set("Explosion.Enabled", false);
        }
        else
        {
          return false;
        }

        plugin.saveConfig();
        return true;
      }
      else if ( args[1].equalsIgnoreCase("radius") && args.length > 2 )
      {
        float radius = Float.parseFloat(args[2]);

        if ( radius < 0 || radius > 64 )
        {
          return false;
        }

        plugin.getConfig().set("Explosion.Radius", radius );
        plugin.saveConfig();

        return true;
      }
      else if ( args[1].equalsIgnoreCase("fire") && args.length > 2 )
      {
        if ( args[2].equalsIgnoreCase("true") )
        {
            plugin.getConfig().set("Explosion.Fire", true);
        }
        else if ( args[2].equalsIgnoreCase("false") )
        {
          plugin.getConfig().set("Explosion.Fire", false);
        }
        else
        {
          return false;
        }

        plugin.saveConfig();
        return true;
      }
      
      return false;
    }
    

    return false;
	}
  
}

