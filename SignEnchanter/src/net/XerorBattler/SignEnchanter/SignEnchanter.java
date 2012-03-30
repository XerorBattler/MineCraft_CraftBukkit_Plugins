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
        private ArrayList<EnchantingSign> signs;
        
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
        
        protected void AddSign(EnchantingSign sign)
        {
            signs.add(sign);
        }
        
        protected EnchantingSign GetSign(Location location)
        {
            if(signs.isEmpty())return null;
            for(EnchantingSign sign : signs)
            {
                if(sign.getLocation() == location)return sign;
            }
            return null;
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
