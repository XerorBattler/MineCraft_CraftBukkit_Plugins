//package net.XerorBattler.SignEnchanter;
//
//public enum SEEnchantsOld {
//    //armor enchants
//    protection("PROTECTION_ENVIRONMENTAL"),
//    prot("PROTECTION_ENVIRONMENTAL"),
//    fireprotection("PROTECTION_FIRE"),
//    fireprot("PROTECTION_FIRE"),
//    featherfalling("PROTECTION_FALL"),
//    lightness("PROTECTION_FALL"),
//    blastprotection("PROTECTION_EXPLOSIONS"),
//    projectileprot("PROTECTION_PROJECTILE"),
//    kevlar("PROTECTION_PROJECTILE"),
//    respiration("OXYGEN"),
//    waterbreathing("OXYGEN"),
//    oxygen("OXYGEN"),
//    aquaaffinity("WATER_WORKER"),
//    waterworking("WATER_WORKER"),
//    //weapon enchants
//    sharpness("DAMAGE_ALL"),
//    enpower("DAMAGE_ALL"),
//    smite("DAMAGE_UNDEAD"),
//    sanctity("DAMAGE_UNDEAD"),
//    fireaspect("FIRE_ASPECT"),
//    burner("FIRE_ASPECT"),
//    bane("DAMAGE_ARTHROPODS"),
//    crusher("DAMAGE_ARTHROPODS"),
//    looting("LOOT_BONUS_MOBS"),
//    lootbonus("LOOT_BONUS_MOBS"),
//    drop("LOOT_BONUS_MOBS"),
//    //bow enchants
//    power("ARROW_DAMAGE"),
//    vigor("ARROW_DAMAGE"),
//    punch("ARROW_KNOCKBACK"),
//    throwing("ARROW_KNOCKBACK"),
//    flame("ARROW_FIRE"),
//    firearrows("ARROW_FIRE"),
//    infinity("ARROW_INFINITE"),
//    noammo("ARROW_INFINITE"),
//    //tool enchants
//    efficiency("DIG_SPEED"),
//    silktouch("SILK_TOUCH"),
//    unbreaking("DURABILITY"),
//    durability("DURABILITY"),
//    luck("LOOT_BONUS_BLOCKS"),
//    fortune("LOOT_BONUS_BLOCKS");   
//
//    private final String name;
//
//    SEEnchantsOld(String name) {
//        this.name = name;
//    }
//    
//    /**
//     * Returns bukkit name from custom name
//     * @return Bukkit core name
//     */
//    public String getName()
//    {
//        return this.name;
//    }
//    
//    /**
//     * This method returns minecraft name (from DURABILITY to unbreakable)
//     * @param name Bukkit core name
//     * @return MineCraft name
//     */
//    public static String getMinecraftName(String name)
//    {
//        for (SEEnchantsOld inputName: SEEnchantsOld.values())
//        {
//            if(name.equalsIgnoreCase(inputName.name))return inputName.toString();
//        }
//       return null;
//    }
//    /**
//     * This method returns real bukkit enchant name (from unbreakable to DURABILITY)
//     * @param name MineCraft name
//     * @return Bukkit core name
//     */
//    public static String getBukkitName(String name)
//    {
//        for (SEEnchantsOld inputName: SEEnchantsOld.values())
//        {
//            if(name.equalsIgnoreCase(inputName.toString()))return inputName.name;
//        }
//        return null;
//   }
//}
