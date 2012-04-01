package net.XerorBattler.SignEnchanter;

public class SEConfig {
    private static boolean debugMode = false;
    private static String signName = "[en]";
    private static int minimalBaseCost = 100;
    private static int maximalBaseCost = 5000;
    
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
    
    public int getMaxBaseCost()
    {
        return maximalBaseCost;
    }
}
