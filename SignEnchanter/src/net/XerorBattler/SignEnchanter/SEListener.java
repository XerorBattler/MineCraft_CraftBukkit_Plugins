package net.XerorBattler.SignEnchanter;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SEListener implements Listener {
	
	public static SignEnchanter signEnchanter;
	
	public SEListener(SignEnchanter instance) {
		signEnchanter = instance;
	}
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onBlockDestroy(final BlockBreakEvent event)
        {
            if(signEnchanter.destroySign(event.getBlock().getLocation()) == false)return;
            signEnchanter.logInfo("Sign unregistered, " + signEnchanter.getSignCount() + " registered sign(s) left", true);
        }
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onBlockUse(final PlayerInteractEvent event)
        {
            if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
            if(event.getClickedBlock() != null && event.getClickedBlock().getTypeId() == 68)
            {
                
                if(event.getClickedBlock().getLocation() == null)return;
                if(signEnchanter.getSign(event.getClickedBlock().getLocation()) == null)return;
                signEnchanter.logInfo("Enchanting sign used by " + event.getPlayer().getName(), true);
                SESign sign = signEnchanter.getSign(event.getClickedBlock().getLocation());
                if(sign.EnchantItem(event.getPlayer()))
                {
                    event.getPlayer().sendMessage("Your item was enchanted!");
                }
            }
        }
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onSignChangeEvent(final SignChangeEvent event)
        {
            String title = event.getLine(0);
            String baseCostString = event.getLine(1);
            String enchantName = event.getLine(2);
            String maxLevelString = event.getLine(3);
           
            //DEBUG CODE! This obey any sign control
            if(title.equals("debug") && signEnchanter.getSEConfig().getDebugMode())
            {
                SESign sign = new SESign(100, event.getBlock().getLocation());
                sign.AddEnchant(SEEnchantsOld.getBukkitName("durability"), 5);
                signEnchanter.addSign(sign);
                signEnchanter.logInfo("Debug sign created.", Boolean.TRUE);
                event.setLine(0, ChatColor.YELLOW + "SignEnchanter");
                event.setLine(1, ChatColor.RED + "DEBUG MODE");
                event.setLine(2, ChatColor.YELLOW + "durability");
                event.setLine(3, ChatColor.YELLOW + "max lvl 4");
                return;
            }
            //DEBUG CODE!

            if(title.length() == 0 || baseCostString.length() == 0 || enchantName.length() == 0 || maxLevelString.length() == 0)return;

            if(!title.equalsIgnoreCase(signEnchanter.getSEConfig().getSignTitle()))return;
            
//            if(!event.getPlayer().hasPermission("SignEnchanter.create"))
//            {
//                event.getPlayer().sendMessage(ChatColor.RED + "You don't have a permission to create signs.");
//                return;
//            }
            
            signEnchanter.logInfo(title, true);
            signEnchanter.logInfo(baseCostString, true);
            signEnchanter.logInfo(enchantName, true);
            signEnchanter.logInfo(maxLevelString, true);
            
            try
            {
                int baseCost = Integer.parseInt(baseCostString);
                int maxLevel = Integer.parseInt(maxLevelString);
                enchantName = SEEnchantsOld.getBukkitName(enchantName);
                if(enchantName == null)
                {
                    throw new IllegalArgumentException("Unknown enchant used.");
                }
                if(title.equals(signEnchanter.getSEConfig().getSignTitle()))
                {
                    if(baseCost < signEnchanter.getSEConfig().getMinBaseCost())
                    {
                        throw new IllegalArgumentException("Base cost is lower then minimal possible value. (" 
                                + signEnchanter.getSEConfig().getMinBaseCost() + ")");
                    }
                    signEnchanter.logInfo("Sign enchant: " + enchantName, Boolean.TRUE);
                    event.setLine(0, "&1"+signEnchanter.getSEConfig().getSignTitle());
                    event.setLine(3, "LB:info RB:buy");
                    signEnchanter.logInfo("Enchanting sign created.", Boolean.TRUE);
                    SESign sign = new SESign(baseCost, event.getBlock().getLocation());
                    sign.AddEnchant(enchantName, maxLevel);
                    signEnchanter.addSign(sign);
                    
                }
            }
            catch(IllegalArgumentException ex)
            {
                signEnchanter.logWarn(ex.getMessage());
            }
            
            
        }

}