package net.XerorBattler.SignEnchanter;

import org.bukkit.enchantments.Enchantment;

public enum SEEnchants {
    //TODO: set other texts
    //armor enchants
    protection(Enchantment.PROTECTION_ENVIRONMENTAL,"damage protection"),
    prot(Enchantment.PROTECTION_ENVIRONMENTAL,"damage protection"),
    fireprotection(Enchantment.PROTECTION_FIRE,"fire protection"),
    fireprot(Enchantment.PROTECTION_FIRE,"fire protection"),
    featherfalling(Enchantment.PROTECTION_FALL,"fall protection"),
    lightness(Enchantment.PROTECTION_FALL,"fall protection"),
    blastprotection(Enchantment.PROTECTION_EXPLOSIONS,""),
    projectileprot(Enchantment.PROTECTION_PROJECTILE,"arrow protection"),
    kevlar(Enchantment.PROTECTION_PROJECTILE,"arrow protection"),
    respiration(Enchantment.OXYGEN,""),
    waterbreathing(Enchantment.OXYGEN,""),
    oxygen(Enchantment.OXYGEN,""),
    aquaaffinity(Enchantment.WATER_WORKER,""),
    waterworking(Enchantment.WATER_WORKER,""),
    //weapon enchants
    sharpness(Enchantment.DAMAGE_ALL,""),
    enpower(Enchantment.DAMAGE_ALL,""),
    smite(Enchantment.DAMAGE_UNDEAD,""),
    sanctity(Enchantment.DAMAGE_UNDEAD,""),
    fireaspect(Enchantment.FIRE_ASPECT,""),
    burner(Enchantment.FIRE_ASPECT,""),
    bane(Enchantment.DAMAGE_ARTHROPODS,""),
    crusher(Enchantment.DAMAGE_ARTHROPODS,""),
    looting(Enchantment.LOOT_BONUS_MOBS,""),
    lootbonus(Enchantment.LOOT_BONUS_MOBS,""),
    drop(Enchantment.LOOT_BONUS_MOBS,""),
    //bow enchants
    power(Enchantment.ARROW_DAMAGE,""),
    vigor(Enchantment.ARROW_DAMAGE,""),
    punch(Enchantment.ARROW_KNOCKBACK,""),
    throwing(Enchantment.ARROW_KNOCKBACK,""),
    flame(Enchantment.ARROW_FIRE,""),
    firearrows(Enchantment.ARROW_FIRE,""),
    infinity(Enchantment.ARROW_INFINITE,""),
    noammo(Enchantment.ARROW_INFINITE,""),
    //tool enchants
    efficiency(Enchantment.DIG_SPEED,""),
    silktouch(Enchantment.SILK_TOUCH,""),
    unbreaking(Enchantment.DURABILITY,""),
    durability(Enchantment.DURABILITY,""),
    luck(Enchantment.LOOT_BONUS_BLOCKS,""),
    fortune(Enchantment.LOOT_BONUS_BLOCKS,"");   

    private final String text;
    private final Enchantment enchant;

    SEEnchants(Enchantment enchant, String text) {
        this.enchant = enchant;
        this.text = text;
    }
    
    public String getText()
    {
        return this.text;
    }
    
    /**
     * Returns bukkit name from custom name
     * @return Bukkit core name
     */
    public String getName()
    {
        return this.enchant.getName();
    }
    
    /**
     * This method returns minecraft name (from DURABILITY to unbreakable)
     * @param name Bukkit core name
     * @return MineCraft name
     */
    public static String getMinecraftName(String name)
    {
        for (SEEnchants inputName: SEEnchants.values())
        {
            if(name.equalsIgnoreCase(inputName.getName()))return inputName.toString();
        }
       return null;
    }
    public static String getMinecraftName(Enchantment enchant)
    {
        return getMinecraftName(enchant.getName());
    }
    /**
     * This method returns enchant from MineCraft name
     * @param name MineCraft name
     * @return Bukkit enchantment
     */
    public static Enchantment getEnchant(String name)
    {
        for (SEEnchants inputName: SEEnchants.values())
        {
            if(name.equalsIgnoreCase(inputName.getName()))return inputName.enchant;
        }
        return null;
   }
}
