/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.XerorBattler.SignEnchanter;

import org.bukkit.enchantments.Enchantment;

/**
 *
 * @author Vaclav Haramule (Xeror Battler)
 */
public class EnumTEst {
    public static void main(String[] args)
    {
       System.out.println("Vkladam enchant text OXYGEN");
       System.out.println(SEEnchants.getMinecraftName("OXYGEN"));
       System.out.println("Vkladam enchant text oxygen");
       System.out.println(SEEnchants.getBukkitName("oxygen"));
       System.out.println("Vkladam enchant text oxygen");
       System.out.println(SEEnchants.getEnchantTextByMCName("oxygen"));
       System.out.println("Vkladam enchant text oxygen");
       System.out.println(SEEnchants.getEnchantTextByBName("OXYGEN"));
       
    }
}
