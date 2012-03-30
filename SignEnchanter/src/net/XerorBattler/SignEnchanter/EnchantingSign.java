package net.XerorBattler.SignEnchanter;

import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantingSign
{
    private HashMap<Enchantment, Integer> enchants = new HashMap<>();
    private int baseCost;
    private Location location;

    public EnchantingSign(int baseCost, Location location)
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
    
    public void EnchantItem(ItemStack item)
    {
        for(Enchantment ench : item.getEnchantments().keySet())
        {
            item.removeEnchantment(ench);
        }
        item.addUnsafeEnchantments(this.enchants);
    }
    
    public Location getLocation()
    {
        return this.location;
    }
}
