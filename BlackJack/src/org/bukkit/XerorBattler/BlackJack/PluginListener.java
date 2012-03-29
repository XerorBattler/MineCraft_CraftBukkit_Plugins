package BlackJack;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.ServerListener;

import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

import org.bukkit.plugin.Plugin;

/**
 * Checks for plugins whenever one is enabled, use primary for iConomy
 *
 * @author Nijikokun&Xeror Battler
 * @version 1.2
 */
public class PluginListener extends ServerListener {
    private BlackJack pluginListener;
    /**
     * Creates BlackJack listener for further use
     * 
     * @param plugin instance of BlackJack 
     */
    public PluginListener(BlackJack plugin)
    {
        this.pluginListener = plugin;
    }
    /**
     * On plugin disable disables iConomy plugin
     * 
     * @param event PluginDisableEvent
     */
@Override
    public void onPluginDisable(PluginDisableEvent event)
    {
        if (pluginListener.getiConomy() != null)
        {
            if (event.getPlugin().getDescription().getName().equals("iConomy"))
            {
                pluginListener.setiConomy(null);
                System.out.println("[BlackJack] un-hooked from iConomy.");
            }
        }
        else if(pluginListener.getBOSEconomy() != null)
        {
            if (event.getPlugin().getDescription().getName().equals("BOSEconomy"))
            {
                pluginListener.setBOSEconomy(null);
                System.out.println("[BlackJack] un-hooked from BOSEconomy.");
            }
        }
    }
    /**
     * On plugin enable creates new instance of iConomy plugin
     * 
     * @param event PluginEnableEvent
     */
@Override
    public void onPluginEnable(PluginEnableEvent event)
    {
        if (pluginListener.getiConomy() == null && pluginListener.getBOSEconomy() == null)
        {
            Plugin iConomy = pluginListener.getServer().getPluginManager().getPlugin("iConomy");
            Plugin BOSEConomy = pluginListener.getServer().getPluginManager().getPlugin("BOSEconomy");
            if (iConomy != null)
            {
                if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy"))
                {
                    pluginListener.setiConomy((iConomy)iConomy);
                    System.out.println("[BlackJack] hooked into iConomy.");
                }
            }
            else if(BOSEConomy != null)
            {
                if (BOSEConomy.isEnabled() && BOSEConomy.getClass().getName().equals("cosine.boseconomy.BOSEconomy"))
                {
                    pluginListener.setBOSEconomy((BOSEconomy)BOSEConomy);
                    System.out.println("[BlackJack] hooked into BOSEconomy.");
                }
            }
            else if(!pluginListener.getExchanger())
            {
                System.out.println("[BlackJack] iConomy not found! Using alternative method.");
                pluginListener.setExchanger();
            }
        }
    }
}