package org.ludin.GoldenWind;

import org.bukkit.configuration.file.FileConfiguration;

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
  GoldenWind plugin;
  FileConfiguration config;
  Logger log;

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

  public GoldenWindCommand( GoldenWind plugin )
  {
    this.plugin = plugin;
    this.config = plugin.getConfig();
    this.log    = plugin.getLogger();
    
    methodMap = new HashMap<>();
    subCommandMap = new HashMap<>();
  }

  Map<String,Cmd> methodMap;
  Map<String,GoldenWindCommand> subCommandMap;

  public boolean dispatch( String cmdName, String[] args )
  {
    args = args == null ? new String[0] : args;

    log.info( "Command name: "+ cmdName );
    
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
  


  void registerSubCommand( String name, GoldenWindCommand cmd )
  {
    subCommandMap.put( name, cmd );
  }
  
  void registerCommand( String name, Predicate<String[]> method )
  {
    registerCommand( name, method, 0, null );
  }

  void registerCommand( String name, Predicate<String[]> method, int nArgs )
  {
    registerCommand( name, method, nArgs, null );
  }

  void registerCommand( String name, Predicate<String[]> method, int nArgs, String[] values )
  {
    Cmd cmd = new Cmd( name, method, nArgs, values );
    methodMap.put( name, cmd );
  }

  void registerTFCommand( String name, String configName, String variable )
  {
    registerCommand( name,
                     (args) -> {
                       if ( args[0].equalsIgnoreCase("true") ||
                            args[0].equalsIgnoreCase("false") )
                       {
                         config.set( configName + "." + variable, args[0].equalsIgnoreCase("true") ? true : false );
                         return true;
                       }
                       return false;
                     },
                     1,
                     new String[]{"true","false"} );

  }


  List<String> complete( String[] args )
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

  void saveConfig()
  {
    plugin.saveConfig();
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



