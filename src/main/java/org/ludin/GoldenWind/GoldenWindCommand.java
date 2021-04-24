package org.ludin.GoldenWind;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.function.Predicate;


interface Completer
{
  public List<String> getCompletions( String arg );
  public Map<String,Completer> getCompleters();
}

public class GoldenWindCommand implements Completer
{
  protected GoldenWind plugin;
  protected ConfigurationSection config;
  protected Logger log;
  private Map<String,Cmd> methodMap;
  private Map<String,GoldenWindCommand> subCommandMap;
  private String configName;

  public static String[] slice( String[] arr, int start )
  {
    return slice( arr, start, arr.length );
  }

  public static String[] slice( String[] arr, int start, int end )
  {
    if ( start > arr.length )
    {
      return new String[0];
    }

    if ( end > arr.length )
    {
      end = arr.length;
    }
    
    String[] slice = new String[end-start];
    for (int i = 0; i < slice.length && start + i < arr.length; i++) {
      slice[i] = arr[start + i];
    }

    return slice;
  }

  public GoldenWindCommand( GoldenWind plugin, String configName )
  {
    this.plugin = plugin;
    this.log = plugin.getLogger();
    this.configName = configName;
    this.config = plugin.getConfig().getConfigurationSection(configName);

    methodMap = new HashMap<>();
    subCommandMap = new HashMap<>();
  }


  public boolean dispatch( String cmdName, String[] args )
  {
    args = args == null ? new String[0] : args;

    Cmd cmd = methodMap.get( cmdName );
    if ( cmd != null )
    {
      return cmd.execute( args );
    }

    GoldenWindCommand subCmd = subCommandMap.get( cmdName );
    if ( subCmd != null )
    {
      if ( args.length > 0 )
      {
        return subCmd.dispatch( args[0], slice( args, 1 ) );
      }

      return false;
    }
      
    log.info( "Could not find command: " + cmd );
    return false;
  }

  public Map<String,Completer> getCompleters()
  {
    Map<String,Completer> comps = new HashMap<>();

    comps.putAll( methodMap );
    comps.putAll( subCommandMap );
    return comps;
  }
  
  public List<String> getCompletions( String arg )
  {
    List<String> tabComplete = new ArrayList<>();

    for (String name : getCompleters().keySet())
    {
      if ( name.startsWith(arg) )
      {
        tabComplete.add( name );
      }
    }

    return tabComplete;
  }


  protected boolean enabled()
  {
    return config.getBoolean("Enabled");
  }

  


  protected void registerSubCommand( String name, GoldenWindCommand cmd )
  {
    subCommandMap.put( name, cmd );
  }
  
  protected void registerCommand( String name, Predicate<String[]> method )
  {
    registerCommand( name, method, 0, null );
  }

  protected void registerCommand( String name, Predicate<String[]> method, int nArgs )
  {
    registerCommand( name, method, nArgs, null );
  }

  protected void registerCommand( String name, Predicate<String[]> method, int nArgs, String[] values )
  {
    Cmd cmd = new Cmd( name, method, nArgs, values );
    methodMap.put( name, cmd );
  }

  protected void registerTFCommand( String name, String variable )
  {
    registerCommand( name,
                     (args) -> {
                       if ( args[0].equalsIgnoreCase("true") ||
                            args[0].equalsIgnoreCase("false") )
                       {
                         config.set( variable, args[0].equalsIgnoreCase("true") ? true : false );
                         return true;
                       }
                       return false;
                     },
                     1,
                     new String[]{"true","false"} );

  }

  protected void registerEnableDisableCommand()
  {
    registerCommand( "enable",
                     (args) -> {
                         config.set( "Enabled", true );
                         return true;
                     });
    registerCommand( "disable",
                     (args) -> {
                         config.set( "Enabled", false );
                         return true;
                     });
  }
  

  protected void registerDoubleCommand( String command, String configEntry )
  {
    registerCommand( command,
                     (args) -> {
                       try
                       {
                         double value = Double.parseDouble(args[0]);
                         value = value < 0 ? 0 : value;
                         config.set( configEntry, value );
                       }
                       catch( NumberFormatException e )
                       {
                         return false;
                       }
                       return true;
                     },
                     1,
                     new String[]{"<double>"} );
    
  }
  
  protected void registerIntCommand( String command, String configEntry )
  {
    registerCommand( command,
                     (args) -> {
                       try
                       {
                         int value = Integer.parseInt(args[0]);
                         value = value < 0 ? 0 : value;
                         config.set( configEntry, value );
                       }
                       catch( NumberFormatException e )
                       {
                         return false;
                       }
                       return true;
                     },
                     1,
                     new String[]{"<int>"} );
    
  }

  public List<String> complete( String[] args )
  {
    List<String> tabComplete = new ArrayList<>();

    Completer comp = this;

    int i;
    for ( i = 0; i < args.length - 1 && comp != null; i++ )
    {
      Map<String,Completer> comps = comp.getCompleters();
      comp = comps.get(args[i]);
    }

    if ( comp == null )
    {
      return tabComplete;
    }

    tabComplete.addAll( comp.getCompletions( args[i] ) );

    return tabComplete;
  }

  

}

class Cmd implements Completer {
  String name;
  Predicate<String[]> method;
  String[] values;
  int nArgs;
  
  Cmd( String name, Predicate<String[]> method )
  {
    this( name, method, 0, null );
  }

  Cmd( String name, Predicate<String[]> method, int nArgs, String[] values )
  {
    this.name = name;
    this.method = method;
    this.nArgs = nArgs;
    this.values = values == null ? new String[0] : values;
  }

  public Map<String,Completer> getCompleters()
  {
    Map<String,Completer> comps = new HashMap<>();
    return comps;
  }

  public boolean execute( String[] args )
  {
    args = args == null ? new String[0] : args;

    if ( args.length < nArgs )
    {
      return false;
    }
    
    return method.test(args);
  }

  public List<String> getCompletions( String arg )
  {
    List<String> tabComplete = new ArrayList<>();
    
    for (String name : values )
    {
      if ( name.startsWith(arg) )
      {
        tabComplete.add( name );
      }
    }

    return tabComplete;
  }

    
}



