package BlackJack;

import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Exchange system & iConomy substitute class
 * 
 * @author Xeror Battler
 * @version 1.1
 */
public class Exchanger
{
    private final Logger log = Logger.getLogger("Minecraft");
    private String[][] exchangeRate;
    private final double buySellRatio=1.25;
    /**
     * Initialization class
     */
    public Exchanger(File dataFolder)
    {
        //init
        setRates(dataFolder);
    }
    /**
     * Exchanger set his own rates for exchange from config file
     */
    private void setRates(File dataFolder)
    {
        prepConfig(dataFolder);
    }
    /**
     * Loads configuration file, or creates new one if null
     * 
     * @param file File config file
     * @throws Exception if file's not found
     * @return boolean load status (fail/success)
     */
    private boolean loadConfig(File file)throws Exception
    {
        if(file==null)return false;
        FileReader fr=null;
        try
        {
            fr=new FileReader(file);
            Scanner sc=new Scanner(fr);
            int count=0;
            while(sc.hasNextLine())
            {
                sc.nextLine();
                count++;
            }
            exchangeRate=new String[count][3];
            //reinitializating reader & scanner
            fr.close();
            sc.close();
            fr=new FileReader(file);
            sc=new Scanner(fr);
            int i=0;
            String line="";
            String splitLine[]=new String[3];
            while(sc.hasNextLine())
            {
                line=sc.nextLine();
                splitLine=line.split(":");
                if(splitLine.length<3)
                {
                    log.info("[BlackJack] Line "+(i+1)+" in exchange config have invalid format!");
                    continue;
                }
                //fuck you manual array copy! :D
                for(int j=0;j<splitLine.length;j++)
                {
                    exchangeRate[i][j]=splitLine[j];
                }
                i++;
            }
        }
        catch(Exception e)
        {
            throw new Exception(e);
        }
        return true;
    }
    public void prepConfig(File dataFolder)
    {
        File dirName=dataFolder;
        if (!new File(dirName.toString()).exists())
        {
            new File(dirName.toString()).mkdir();
        }
        File config=new File(dirName+File.separator+"exchange.yml");
        try
        {
            config=new File(dirName+File.separator+"exchange.yml");
            loadConfig(config);
            log.info("[BlackJack] Loading exchange config file!");
        }
        catch(Exception e)
        {
            log.info("[BlackJack] Invalid exchange configuration file! Making new one!");
            try
            {
                config.createNewFile();
            }
            catch(Exception ex)
            {
                log.info("[BlackJack] Can't create new file! Error: "+ex);
            }
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter(config, false));
                out.flush();
                out.write("diamond:264:1000");
                out.newLine();
                out.write("diamond_block:57:10000");
                out.newLine();
                out.write("gold_bar:266:200");
                out.newLine();
                out.write("gold_block:41:2000");
                out.newLine();
                out.close();
                loadConfig(config);
            }
            catch (Exception ex)
            {
                log.info("[BlackJack] Can't write exchange config file! Error: "+ex);
            }
            
        }
    }
    /**
     * This method exchanges items for credit and backward
     * 
     * @param item String item name
     * @param playerAcc PlayerAccount changing player
     */
    public void exchange(String item, PlayerAccount playerAcc)
    {
        try
        {
            if(item.length()>0)
            {
                creditToDiamond(playerAcc);
            }
            else
            {
                itemToCredit(playerAcc);
            }
        }
        catch(Exception e)
        {
            playerAcc.errorMessage("Exchange error!");
            log.info("[BlackJack] "+playerAcc.getPlayerName()+" caused error at exchange. (error: "+e+")");
        }
    }
    /**
     * Changes item in hand of player to credits (if its possible)
     * 
     * @param playerAcc PlayerAccount changing player
     * @return boolean change status (fail/success)
     */
    private boolean itemToCredit(PlayerAccount playerAcc)
    {
        int itemId=playerAcc.getPlayer().getItemInHand().getTypeId();
        int arrayPos=getArrayPos(itemId);
        if(arrayPos>=0)
        {
            int itemValue=Integer.parseInt(exchangeRate[arrayPos][2]);
            int itemCount=playerAcc.getPlayer().getItemInHand().getAmount();
            int cash=itemValue*(itemCount);
            playerAcc.getPlayer().getInventory().removeItem(playerAcc.getPlayer().getInventory().getItemInHand());
            playerAcc.addCash(cash);
            playerAcc.sendMessage("You got "+cash+" credits for that items.");
            playerAcc.infoMessage("Balance: "+playerAcc.getCash());
        }
        else
        {
            log.info(playerAcc.getPlayerName()+" tried to exchange item "+itemId+" to credit.");
            playerAcc.errorMessage("What?! You want credits for "+playerAcc.getPlayer().getItemInHand().getType().name().toLowerCase()+"? Good joke.");
        }
        return false;
    }
    /**
     * Special variant of creditToItem method
     * 
     * @param playerAcc PlayerAccount changing player
     * @return boolean change status (fail/success)
     */
    private boolean creditToDiamond(PlayerAccount playerAcc)
    {
        creditToItem("Diamond",playerAcc);
        return true;
    }
    /**
     * Changes credit to item if its possible
     * 
     * @param itemName String wanted item
     * @param playerAcc PlayerAccount changing player
     * @return boolean change status (fail/success)
     */
    private boolean creditToItem(String itemName, PlayerAccount playerAcc)
    {
        int itemId=getItemId(itemName);
        int credits=playerAcc.getCash();
        int itemValue=(int)Math.ceil(Integer.parseInt(exchangeRate[getArrayPos(itemId)][2])*buySellRatio);
        int itemCount=(int)Math.floor(credits/itemValue);
        credits=credits%itemValue;
        try
        {
            if(itemCount>0)
            {
                playerAcc.getPlayer().getInventory().addItem(new ItemStack(itemId,(itemCount)));
            }
        }
        catch(Exception e)
        {
            log.info(playerAcc.getPlayerName()+" tried to exchange credits to items (error: "+e+")");
            playerAcc.errorMessage("Error! Full inventory!");
            return false;
        }
        playerAcc.sendMessage("You got "+itemCount+" diamond"+((itemCount>1)?"s":"")+".");
        playerAcc.setCash(credits);
        playerAcc.infoMessage("Balance: "+playerAcc.getCash());
        return true;
    }
    /**
     * Getter, gets position in array, if not found returns -1
     * 
     * @param itemId int item id which we looking for
     * @return int position in exchangeRates array
     */
    
    private int getArrayPos(int itemId)
    {
        for(int i=0;i<exchangeRate.length;i++)
        {
            if(Integer.parseInt(exchangeRate[i][1])==itemId)
            {
                return i;
            }
        }
        return -1;
    }
    /**
     * Gets Minecraft item id by name, its loaded from config file
     * 
     * @param name String item name
     * @return int Minecraft itemID
     */
    private int getItemId(String name)
    {
        for(int i=0;i<exchangeRate.length;i++)
        {
            if(exchangeRate[i][0].equalsIgnoreCase("diamond"))
            {
                return Integer.parseInt(exchangeRate[i][1]);
            }
        }
        return -1;
    }
    @Override
    public String toString()
    {
       String returnString="";
       for(int i=0;i<exchangeRate.length;i++)
       {
           returnString+="["+exchangeRate[i][0]+":"+exchangeRate[i][1]+":"+exchangeRate[i][2]+"]";
       }
       return returnString;
    }
    
}
