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
            signEnchanter.logInfo("clicked block" + event.getClickedBlock().getType().name(), true);
            if(!event.isBlockInHand())return;
            if(event.getClickedBlock() != null && event.getClickedBlock().getTypeId() == 68)
            {
                if(event.getClickedBlock().getLocation() == null)return;
                signEnchanter.logInfo("Sign used", true);
                if(signEnchanter.GetSign(event.getClickedBlock().getLocation()) == null)return;
                signEnchanter.logInfo("Registered sign used", true);
                EnchantingSign sign = signEnchanter.GetSign(event.getClickedBlock().getLocation());
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
            String title = event.getLine(0);

            try
            {
                int baseLevelCost = Integer.parseInt(event.getLine(1));
                String enchant = event.getLine(2);
                int maxLevel = Integer.parseInt(event.getLine(3));
                if(title.equals(signEnchanter.getSEConfig().getSignTitle()))
                {
                    if(baseLevelCost < signEnchanter.getSEConfig().getMinBaseCost())
                    {
                        throw new IllegalArgumentException("Base cost is lower then minimal possible value. (" 
                                + signEnchanter.getSEConfig().getMinBaseCost() + ")");
                    }
//                    if(!SEEnchantList.isMember(enchant))
//                    {
//                        throw new IllegalArgumentException("Unknown enchant used.");
//                    }
//                    enchant = SEEnchantList.valueOf(enchant).getName();
                    enchant = "DURABILITY";
                    event.setLine(0, "&1"+signEnchanter.getSEConfig().getSignTitle());
                    event.setLine(3, "LB->info | RB->buy");
                    signEnchanter.logInfo("Enchanting sign created.", Boolean.TRUE);
                    EnchantingSign sign = new EnchantingSign(baseLevelCost, event.getBlock().getLocation());
                    sign.AddEnchant(enchant, maxLevel);
                    signEnchanter.AddSign(sign);
                    
                }
            }
            catch(IllegalArgumentException ex)
            {
                signEnchanter.logWarn(ex.getMessage());
            }
            
            
//            Integer.parseInt(event.getLine(1));
//            if(event.getLine(0).equalsIgnoreCase("[en]"))
//            {
//                if(Integer.parseInt(event.getLine(1)) > 0 && Integer.parseInt(event.getLine(3)) > 0)
//                {
//                    String[] enchantNames = event.getLine(2).split(";");
//                    for(String enchantName : enchantNames)
//                    {
//                        if(enchantName.length() == 2)
//                        {
//                            if(enchantName.equals("DB"))
//                            {
//                                EnchantingSign sign = new EnchantingSign(Integer.parseInt(event.getLine(1)));
//                                sign.AddEnchant("DURABILITY", Integer.parseInt(event.getLine(3)));
//                                signEnchanter.AddSign(sign);
//                                signEnchanter.logInfo("Enchanting sign created", Boolean.TRUE);
//                            }
//                        }
//                    }
//                }
//            }
        }

}