package net.XerorBattler.SignEnchanter;

public enum SEEnchants {
    protection("PROTECTION_ENVIRONMENTAL","damage protection"),
    prot("PROTECTION_ENVIRONMENTAL","damage protection"),
    fireprotection("PROTECTION_FIRE","fire protection"),
    fireprot("PROTECTION_FIRE","fire protection"),
    featherfalling("PROTECTION_FALL","fall protection"),
    lightness("PROTECTION_FALL","fall protection"),
    blastprotection("PROTECTION_EXPLOSIONS","blast protection"),
    projectileprot("PROTECTION_PROJECTILE","arrow protection"),
    kevlar("PROTECTION_PROJECTILE","arrow protection"),
    respiration("OXYGEN","water breathing"),
    waterbreathing("OXYGEN","water breathing"),
    oxygen("OXYGEN","water breathing"),
    aquaaffinity("WATER_WORKER","water working"),
    waterworking("WATER_WORKER","water working"),
    //weapon enchants
    sharpness("DAMAGE_ALL","weapon damage"),
    enpower("DAMAGE_ALL", "weapon damageox"),
    smite("DAMAGE_UNDEAD","holy damage"),
    sanctity("DAMAGE_UNDEAD","holy damage"),
    fireaspect("FIRE_ASPECT","fire damage"),
    burner("FIRE_ASPECT","fire damage"),
    bane("DAMAGE_ARTHROPODS","dungeon damage"),
    crusher("DAMAGE_ARTHROPODS","dungeon damage"),
    looting("LOOT_BONUS_MOBS","looting"),
    lootbonus("LOOT_BONUS_MOBS","looting"),
    drop("LOOT_BONUS_MOBS","looting"),
    //bow enchants
    power("ARROW_DAMAGE","arrow damage"),
    vigor("ARROW_DAMAGE","arrow damage"),
    punch("ARROW_KNOCKBACK","arrow knockback"),
    throwing("ARROW_KNOCKBACK","arrow knockback"),
    flame("ARROW_FIRE","fire damage"),
    firearrows("ARROW_FIRE","fire damage"),
    infinity("ARROW_INFINITE","spiritual arrows"),
    noammo("ARROW_INFINITE","spiritual arrows"),
    //tool enchants
    efficiency("DIG_SPEED","use speed"),
    silktouch("SILK_TOUCH","silk touch"),
    unbreaking("DURABILITY","durability"),
    durability("DURABILITY","durability"),
    luck("LOOT_BONUS_BLOCKS","fortune"),
    fortune("LOOT_BONUS_BLOCKS","fortune");   

    private final String enchant;
    private final String text;

    SEEnchants(String enchant, String text) {
        this.enchant = enchant;
        this.text = text;
    }
    
    /**
     * Returns bukkit name from custom name
     * @return Bukkit core name
     */
    public String getName()
    {
        return this.text;
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
            if(name.equalsIgnoreCase(inputName.enchant))return inputName.toString();
        }
       return null;
    }
    /**
     * This method returns real bukkit enchant name (from unbreakable to DURABILITY)
     * @param name MineCraft name
     * @return Bukkit core name
     */
    public static String getBukkitName(String name)
    {
        for (SEEnchants inputName: SEEnchants.values())
        {
            if(name.equalsIgnoreCase(inputName.toString()))return inputName.enchant;
        }
        return null;
   }
   public static String getEnchantTextByMCName(String name)
   {
        for (SEEnchants inputName: SEEnchants.values())
        {
            if(name.equalsIgnoreCase(inputName.toString()))return inputName.text;
        }
        return null;
   }
   
    public static String getEnchantTextByBName(String name)
   {
        for (SEEnchants inputName: SEEnchants.values())
        {
            if(name.equalsIgnoreCase(inputName.enchant))return inputName.text;
        }
        return null;
   }
}
