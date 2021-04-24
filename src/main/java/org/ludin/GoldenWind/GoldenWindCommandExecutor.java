package org.ludin.GoldenWind;

import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.Command;



public class GoldenWindCommandExecutor implements CommandExecutor, TabExecutor
{

  private GoldenWind plugin;

  Map<String,GoldenWindCommand> commandMap;
  

  GoldenWindCommandExecutor( GoldenWind plugin )
  {
    this.plugin = plugin;
    commandMap = new HashMap<>();

    commandMap.put( "explode", new ExplodingMobs( plugin ) );
    commandMap.put( "halfsleep", new SleepOnHalf( plugin ) );
    commandMap.put( "playerlog", new PlayerLog( plugin ) );
    commandMap.put( "environment", new EnvironmentMods( plugin ) );
    commandMap.put( "items", new Items( plugin ) );
    commandMap.put( "fastcarts", new FastCarts( plugin ) );
    commandMap.put( "blockautofish", new BlockAutoFish( plugin ) );
    //    commandMap.put( "playermask", new PlayerMask( plugin ) );
  }
  

  @Override
  public List<String> onTabComplete( CommandSender sender, Command command, String alias, String[] args )
  {
    List<String> tabComplete = new ArrayList<>();

    if ( args.length == 1 )
    {
      for ( String name: commandMap.keySet() )
      {
        if ( name.startsWith(args[0]) )
        {
          tabComplete.add(name);
        }
      }
    }
    else
    {
      GoldenWindCommand cmd = commandMap.get( args[0] );
      if ( cmd != null )
      {
        tabComplete = cmd.complete( GoldenWindCommand.slice( args, 1 ) );
      }
    }
    
    return tabComplete;
  }



  
  @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ( args.length < 2 ) return false;

    Logger log = plugin.getLogger();

    GoldenWindCommand command = commandMap.get(args[0]);
    if ( command == null )
    {
      return false;
    }

    command.dispatch( args[1], GoldenWindCommand.slice(args, 2) );

    plugin.saveConfig();

    log.info( plugin.getConfig().saveToString() );
    
    return false;
	}
  
}

