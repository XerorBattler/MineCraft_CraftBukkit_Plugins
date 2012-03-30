/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.XerorBattler.SignEnchanter;

/**
 *
 * @author Xeror
 */
public class SEConfig {
    private static boolean debugMode = true;
    private static String signName = "[en]";
    private static int minimalBaseCost = 100;
    
    public boolean getDebugMode()
    {
        return debugMode;
    }
    
    public String getSignTitle()
    {
        return signName;
    }
    
    public int getMinBaseCost()
    {
        return minimalBaseCost;
    }
}
