package net.XerorBattler.SignEnchanter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SESign
{
    private HashMap<Enchantment, Integer> enchants = new HashMap<>();
    private int baseCost;
    private Location location;

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
    
    public boolean EnchantItem(Player player)
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
            String enchantName = SEEnchantsOld.getMinecraftName(enchant.getName());
            if(enchantLevel >= enchant.getMaxLevel() || enchantLevel >= enchants.get(enchant))
            {
                String infoString = "possible";
                if(enchant.getMaxLevel() > enchants.get(enchant))infoString = "sign allowed";
                player.sendMessage("Enchant " + enchantName + " reached max " + infoString + " level.");
                return false;
            }
            player.sendMessage("Price for enchant " + enchantName + " lvl " + (enchantLevel + 1) + ": " + price);
            if(enchant.canEnchantItem(item))
            {
                item.addEnchantment(enchant, (enchantLevel + 1));
                player.sendMessage("Item enchanted with " + enchantName + " lvl " + (enchantLevel + 1) + " for " + price);
                return true;
            }
            player.sendMessage("This item can't be enchanted with " + enchantName);
            
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
