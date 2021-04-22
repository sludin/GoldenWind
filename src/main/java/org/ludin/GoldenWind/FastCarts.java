package org.ludin.GoldenWind;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class FastCarts extends GoldenWindCommand implements Listener {

  public final static String CONFIG_NAME = "FastCarts";

  
  public FastCarts( GoldenWind plugin )
  {
    super(plugin);

    registerCommand( "enable", this::enable );
    registerCommand( "disable", this::disable );
    registerCommand( "speed", this::speed, 1, new String[]{"<double>"} );

  }
  
  @EventHandler
  public void onVehicleCreate(VehicleCreateEvent event)
  {
    ConfigurationSection conf = config.getConfigurationSection(CONFIG_NAME);
    if ( conf.getBoolean("Enabled") != true )
    {
      return;
    }

    Vehicle vehicle = event.getVehicle();
    log.info("Vehicle created: " + vehicle.toString());
    
    if ( ! (vehicle instanceof Minecart) )
    {
      return;
    }
    
    log.info("Setting speed to: " + conf.getDouble("Speed"));
    Minecart cart = (Minecart)vehicle;
    cart.setMaxSpeed( conf.getDouble("Speed") );
  }


  
  
  boolean enable( String[] args )
  {
    config.set( CONFIG_NAME + ".Enabled", true);
    return true;
  }
    
  boolean disable( String[] args )
  {
    config.set( CONFIG_NAME + ".Enabled", false);
    return true;
  }

  boolean speed( String[] args )
  {
    try
    {
      double speed = Double.parseDouble(args[0]);
        
      if ( speed < 0.0 )
      {
        speed = 0;
      }
      else if ( speed > 1.0 )
      {
        speed = 1.0;
      }
      
      config.set( CONFIG_NAME + ".Speed", speed );
    }
    catch( java.lang.NumberFormatException e )
    {
      return false;
    }

    return true;
  }


}

