package net.XerorBattler.SignEnchanter;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignEnchanter extends JavaPlugin{
	private final Logger log = Logger.getLogger("Minecraft");
        private static SEListener listener;
        private static final SEConfig config = new SEConfig();
        private ArrayList<SESign> signs;
        
	@Override
	public void onDisable()
        {
	}
        
	@Override
	public void onEnable() {
                PluginManager pm = getServer().getPluginManager();
                listener = new SEListener(this);
                signs = new ArrayList<>();
                pm.registerEvents(listener, this);
	}
        
        protected int getSignCount()
        {
            return this.signs.size();
        }
        
        protected void addSign(SESign sign)
        {
            signs.add(sign);
        }
        
        protected SESign getSign(Location location)
        {
            if(signs.isEmpty())return null;
            for(SESign sign : signs)
            {
                if(sign.getLocation().equals(location))return sign;
            }
            return null;
        }
        
        protected boolean destroySign(Location location)
        {
            if(signs.isEmpty())return false;
            for(SESign sign : signs)
            {                
                if(sign.getLocation().equals(location))
                {
                    signs.remove(sign);
                    return true;
                }
            }
            return false;
        }
        
	protected void logInfo (String text, Boolean isDebug) {
                if(isDebug == true && config.getDebugMode()== false)
                {
                    return;
                }
		log.info("[" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "] " + text);
	}

	protected void logWarn (String text) {
		log.warning("[" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "] " + text);
	}
        
        protected SEConfig getSEConfig()
        {
            return config;
        }
}
