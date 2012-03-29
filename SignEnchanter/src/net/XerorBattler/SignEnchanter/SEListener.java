package net.XerorBattler.SignEnchanter;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SEListener implements Listener {
	
	public static SignEnchanter signEnchanter;
	
	public SEListener(SignEnchanter instance) {
		signEnchanter = instance;
	}
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onBlockUse(final PlayerInteractEvent event)
        {
            if(signEnchanter.GetSign() == null)return;
            if(event.getClickedBlock().getTypeId() == 68)
            {
                signEnchanter.logInfo("Sign used", Boolean.TRUE);
                EnchantingSign sign = signEnchanter.GetSign();
                sign.EnchantItem(event.getPlayer().getItemInHand());
            }
        }
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onSignChangeEvent(final SignChangeEvent event)
        {
            signEnchanter.logInfo(event.getLine(0), true);
            signEnchanter.logInfo(event.getLine(1), true);
            signEnchanter.logInfo(event.getLine(2), true);
            signEnchanter.logInfo(event.getLine(3), true);
            if(event.getLine(0).equalsIgnoreCase("[en]"))
            {
                if(Integer.parseInt(event.getLine(1)) > 0 && Integer.parseInt(event.getLine(3)) > 0)
                {
                    String[] enchantNames = event.getLine(2).split(";");
                    for(String enchantName : enchantNames)
                    {
                        if(enchantName.length() == 2)
                        {
                            if(enchantName.equals("DB"))
                            {
                                EnchantingSign sign = new EnchantingSign(Integer.parseInt(event.getLine(1)));
                                sign.AddEnchant("DURABILITY", Integer.parseInt(event.getLine(3)));
                                signEnchanter.AddSign(sign);
                                signEnchanter.logInfo("Enchanting sign created", Boolean.TRUE);
                            }
                        }
                    }
                }
            }
        }

}