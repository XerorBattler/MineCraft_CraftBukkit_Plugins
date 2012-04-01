package net.XerorBattler.SignEnchanter;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SESign
{
    private HashMap<Enchantment, Integer> enchants = new HashMap<>();
    private int baseCost;
    private Location location;
    private final Method bank = Methods.getMethod();

    public SESign(int baseCost, Location location)
    {
        this.baseCost = baseCost;
        this.location = location;
    }
    
    public void AddEnchant(String enchantName, Integer level)
    {
        Enchantment enchant = Enchantment.getByName(enchantName);
        if(enchant != null)
        {
            this.enchants.put(enchant, level);
        }
    }
    
    public boolean EnchantItem(Player player, boolean enchantIt)
    {
        if(enchants.size() == 1)
        {
            ItemStack item = player.getItemInHand();
            int price = baseCost;
            Collection collection = enchants.keySet();
            Iterator itr = collection.iterator();
            Enchantment enchant = (Enchantment)itr.next();
            int enchantLevel = 0;
            if(item.containsEnchantment(enchant))
            {
                enchantLevel = item.getEnchantmentLevel(enchant);
                price = calculatePrice(enchantLevel);
            }
            String enchantText = SEEnchants.getEnchantTextByMCName(enchant.getName());
            if(enchantLevel >= enchant.getMaxLevel() || enchantLevel >= enchants.get(enchant))
            {
                String infoString = "possible";
                if(enchant.getMaxLevel() > enchants.get(enchant))infoString = "sign allowed";
                player.sendMessage(ChatColor.GOLD + "Enchant " + enchantText + " reached max " + infoString + " level.");
                return false;
            }
            if(!player.hasPermission("SignEnchant.use"))
            {
                player.sendMessage(ChatColor.RED + "You can't use enchanting sign! You don't have permissions for that.");
                return false;
            }
            if(enchant.canEnchantItem(item))
            {
                if(enchantIt)
                {
                    if(bank.getAccount(player.getName()).hasEnough(price))
                    {
                        item.addEnchantment(enchant, (enchantLevel + 1));
                        bank.getAccount(player.getName()).subtract(price);
                        player.sendMessage(ChatColor.GREEN + "Item enchanted with " + enchantText + " lvl " + (enchantLevel + 1) + " for " + price);
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + "You don't have enough money! Enchant cost: " + price);
                    return false;
                }
                player.sendMessage(ChatColor.AQUA + "Price for enchant " + enchantText + " lvl " + (enchantLevel + 1) + ": " + price);
                return false;
            }
            player.sendMessage(ChatColor.RED + "This item can't be enchanted with " + enchantText);
        }
        return false;
//        for(Enchantment ench : item.getEnchantments().keySet())
//        {
//            item.removeEnchantment(ench);
//        }
//        item.addUnsafeEnchantments(this.enchants);
//        return true;
    }
    
    public Location getLocation()
    {
        return this.location;
    }
    
    public int calculatePrice(int itemLevel)
    {
        return (int)(baseCost * Math.round(Math.pow(2, itemLevel)));
    }
}
