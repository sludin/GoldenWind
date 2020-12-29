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
    plugin.getLogger().info( "Entering onCommand" );
    if ( args.length == 0 ) return false;
    plugin.getLogger().info( "Past first check" );
    plugin.getLogger().info( "args length: " + args.length );

    plugin.getLogger().info( "args[0] == " + args[0] );
                             
    
    if ( args[0].equalsIgnoreCase("explode") && args.length > 1 )
    {
      plugin.getLogger().info( "In explode command" );
      
      if ( args[1].equalsIgnoreCase("enable") && args.length > 2 )
      {
        plugin.getLogger().info( "In enable command: " + args[2] );

        if ( args[2].equalsIgnoreCase("true") )
        {
          plugin.getConfig().set("Explosion.Enabled", true);
          plugin.getLogger().info( "Setting explody to true" );
        }
        else if ( args[2].equalsIgnoreCase("false") )
        {
          plugin.getConfig().set("Explosion.Enabled", false);
          plugin.getLogger().info( "Setting explody to false" );
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

