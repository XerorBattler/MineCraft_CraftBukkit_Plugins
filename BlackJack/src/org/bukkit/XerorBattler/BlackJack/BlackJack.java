package BlackJack;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import org.bukkit.entity.Player;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.iConomy.iConomy;

import cosine.boseconomy.BOSEconomy;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.event.Event.Type;

import org.bukkit.event.Event.Priority;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.Arrays;
/**
 *
 * @author Xeror Battler
 */
public class BlackJack extends JavaPlugin{
    private final Logger log = Logger.getLogger("Minecraft");
    private PlayerAccount[] accounts;
    private static PluginManager pm;
    private static Server Server = null;
    private static PluginListener PluginListener = null;
    private PluginDescriptionFile desc;
    //config default values
    private int minBet=1;
    private int maxBet=10000;
    private int minBuyIn=10;
    private int maxBuyIn=5000;
    private boolean announce=true;
    private boolean shortCmdsAllow=false;
    private double blackJackRatio=1.5;
    //iconomy
    private static iConomy iConomy = null;
    private static BOSEconomy BOSEconomy = null;
    private static PermissionHandler permissionHandler;
    private static BlackJack INSTANCE=new BlackJack();
    private boolean iConomyActive=false;
    private boolean BOSEconomyActive=false;
    //iconome subst.
    private Exchanger exchanger=null;
    public BlackJack()
    {
    }
    @Override
    public void onDisable()
    {
        if(INSTANCE.accounts!=null)
        {
            for(PlayerAccount accListed:INSTANCE.accounts)
            {
                if(accListed!=null)
                {
                    if(iConomyActive)
                    {
                        iConomy.getAccount(accListed.getPlayerName()).getHoldings().add(accListed.getCash());
                    }
                }
            }
        }
        log.info("[BlackJack]: No table now");
    }
    @Override
    public void onEnable()
    {     
        if(INSTANCE==null)
        {
            INSTANCE=this;
        }
        INSTANCE.desc=getDescription();
        Server = getServer();
        pm=Server.getPluginManager();
        PluginListener = new PluginListener(this);
        setupPermissions();
        fileLoader();
        //Event Registration
        pm.registerEvent(Type.PLUGIN_ENABLE, PluginListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, PluginListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_QUIT, new BJPlayerListener(this), Priority.Normal, this);
//        if(pm.getPlugin("iConomy") == null && getServer().getPluginManager().getPlugin("BOSEconomy") != null)
//        {
//            BOSEconomy=(BOSEconomy)pm.getPlugin("BOSEconomy");
//            System.out.println("[BlackJack] hooked into BOSEconomy.");
//        }
        log.info("[BlackJack] v"+INSTANCE.desc.getVersion()+" is ready!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        boolean canUseCommand = true;
        String[] split=new String[args.length+1];
        split[0] = command.getName().toLowerCase();
        if (!(sender instanceof Player)){
            log.info("[BlackJack] Only players can use BlackJack commands");
            return false;
        }
        else
        {
            if (BlackJack.permissionHandler != null) {
		canUseCommand = permissionHandler.has((Player)sender, "BlackJack.use");
            }
        }
        Player player = (Player)sender;
        System.arraycopy(args, 0, split, 1, args.length);
        int pArrayPos=getPlayer(player);
        PlayerAccount playerAcc=INSTANCE.accounts[pArrayPos];
        //debug log
        //log.info("[BlackJack] "+player.getName()+" used command: "+Arrays.toString(split));
        if(pArrayPos>=0 && canUseCommand)
        {
            //if(args.length<1)
            try
            {
                if(args.length>=0)
                {
                    if(INSTANCE.shortCmdsAllow && split[0].equalsIgnoreCase("hit"))
                    {
                        hit(playerAcc);
                    }
                    else if(INSTANCE.shortCmdsAllow && split[0].equalsIgnoreCase("stay"))
                    {
                        stay(playerAcc);
                    }
                    else if(INSTANCE.shortCmdsAllow && split[0].equalsIgnoreCase("double"))
                    {
                        doubleBet(playerAcc);
                    }
                    else if(INSTANCE.shortCmdsAllow && split[0].equalsIgnoreCase("split"))
                    {
                        splitCards(playerAcc);
                    }
                    else if(args.length>0)
                    {
                        if(args[0].equalsIgnoreCase("status"))
                        {
                            playerAcc.infoMessage("= BlackJack settings status =");
                            playerAcc.infoMessage("Bet: "+INSTANCE.minBet+" to "+INSTANCE.maxBet);
                            playerAcc.infoMessage("Buyin: "+INSTANCE.minBuyIn+" to "+INSTANCE.maxBuyIn);
                            playerAcc.infoMessage("BJack ratio: "+INSTANCE.getBlackJackRatio());
                            playerAcc.infoMessage("Announce: "+((INSTANCE.getAnnounce())?"ON":"OFF"));
                            if(INSTANCE.getShortCmds())
                            {
                                playerAcc.infoMessage("Short commands: ON");
                            }
                            else
                            {
                                log.info("[BlackJack] Short commands isn't allowed in this version, download version with short commands if u wanna use it");
                            }
                        }
                        else if(args[0].equalsIgnoreCase("exchangeitem") || args[0].equalsIgnoreCase("ei"))
                        {
                            if(iConomyActive || BOSEconomyActive)
                            {
                                playerAcc.errorMessage("Sorry, you can't exchange items or credits here!");
                            }
                            else
                            {
                                exchanger.exchange(split[0],playerAcc);
                            }
                        }
                        else if(args[0].equalsIgnoreCase("exchangecredit") || args[0].equalsIgnoreCase("ec"))
                        {
                            if(iConomyActive || BOSEconomyActive)
                            {
                                playerAcc.errorMessage("Sorry, you can't exchange items or credits here!");
                            }
                            else
                            {
                                exchanger.exchange("",playerAcc);
                            }
                        }
                        else if(args[0].equalsIgnoreCase("game") || args[0].equalsIgnoreCase("g"))
                        {
                            game(playerAcc,stringToInt(split,2));
                        }
                        else if(args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("hit"))
                        {
                            hit(playerAcc);
                        }
                        else if(args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("stay"))
                        {
                            stay(playerAcc);
                        }
                        else if(args[0].equalsIgnoreCase("db") || args[0].equalsIgnoreCase("double"))
                        {
                            doubleBet(playerAcc);
                        }
                        if(args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("split"))
                        {
                            splitCards(playerAcc);
                        }
                        else if(args[0].equalsIgnoreCase("checkin") || args[0].equalsIgnoreCase("ci"))
                        {
                            checkIn(playerAcc,stringToInt(split,2));
                        }
                        else if(args[0].equalsIgnoreCase("checkout") || args[0].equalsIgnoreCase("co"))
                        {
                            checkOut(playerAcc);
                        }
                        else if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("b"))
                        {
                            playerAcc.infoMessage("Balance: "+playerAcc.getCash());
                        }
                        else if(args[0].equalsIgnoreCase("sh") || args[0].equalsIgnoreCase("show"))
                        {
                            showCards(playerAcc);
                        }
                    }
                    else
                    {
                            help(playerAcc);
                    }
                }
            }
            catch(Exception e)
            {
                help(playerAcc);
                log.info("[BlackJack] user input error "+e);
            }
        }
        return false;
    }
    public boolean playerNotExists(Player player)
    {
        if(INSTANCE.accounts==null)return false;
        for(PlayerAccount playerListed:INSTANCE.accounts)
        {
            if(playerListed.getPlayerName().equalsIgnoreCase(player.getName()))return true;
        }
        return false;
    }
    public void addMoney(Player player, int value)
    {
        INSTANCE.accounts[INSTANCE.getPlayer(player)].addCash(value);
    }
    public void playerLeft(Player player)
    {
        int playerID=getPlayer(player);
        INSTANCE.accounts[playerID].playerQuit();
        if(iConomyActive)
        {
            iConomy.getAccount(INSTANCE.accounts[playerID].getPlayerName()).getHoldings().add(INSTANCE.accounts[playerID].getCash());
        }
        else if(BOSEconomyActive)
        {
            BOSEconomy.addBankMoney(player.getName(), INSTANCE.accounts[playerID].getCash(), false);
        }
        INSTANCE.accounts[playerID]=null;
    }
    public PluginManager getPluginManager()
    {
        return pm;
    }
    private void configLoader()
    {
        INSTANCE.minBet=Integer.parseInt(getConfiguration().getProperty("min-bet").toString());
        INSTANCE.maxBet=Integer.parseInt(getConfiguration().getProperty("max-bet").toString());
        INSTANCE.minBuyIn=Integer.parseInt(getConfiguration().getProperty("min-buyin").toString());
        INSTANCE.maxBuyIn=Integer.parseInt(getConfiguration().getProperty("max-buyin").toString());
        INSTANCE.announce=getConfiguration().getProperty("announcements").toString().contains("true");
        //INSTANCE.shortCmdsAllow=getConfiguration().getProperty("short-cmds").toString().contains("true");
        INSTANCE.blackJackRatio=Float.parseFloat(getConfiguration().getProperty("blackjack-ratio").toString());
    }
    private boolean fileLoader()
    {
        if (!new File(getDataFolder().toString()).exists())
        {
        	new File(getDataFolder().toString()).mkdir();
        }
        File config = new File(getDataFolder()+File.separator+"config.yml");
        try
        {
            log.info("[BlackJack] Loading config file!");
            this.configLoader();
        }
        catch(Exception e)
        {
            log.info("[BlackJack] Old configuration file detected! Creating new one! Error: "+e);
            config.delete();
            config = new File(getDataFolder()+File.separator+"config.yml");
            try
            {
                config.createNewFile();
            }
            catch (IOException ex)
            {
                log.info("Can't create file "+config.getPath());
            }
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter(config, true));
                out.write("min-bet: "+INSTANCE.minBet);
                out.newLine();
                out.write("max-bet: "+INSTANCE.maxBet);
                out.newLine();
                out.write("min-buyin: "+INSTANCE.minBuyIn);
                out.newLine();
                out.write("max-buyin: "+INSTANCE.maxBuyIn);
                out.newLine();
                out.write("announcements: "+INSTANCE.announce);
                out.newLine();
                //out.write("short-cmds: "+INSTANCE.shortCmdsAllow);
                //out.newLine();
                out.write("blackjack-ratio: "+INSTANCE.blackJackRatio);
                out.newLine();
                out.close();
                Thread.sleep(2000);
                this.configLoader();
            }
            catch (Exception ex)
            {
                log.info("Can't write config file: "+ex);
            }
        }
        return false;
    }
    private void checkOut(PlayerAccount playerAcc)
    {
        if(iConomyActive)
        {
            playerAcc.infoMessage("You withdraw: "+playerAcc.getCash());
            iConomy.getAccount(playerAcc.getPlayerName()).getHoldings().add(playerAcc.getCash());
            playerAcc.setCash(0);
        }
        else if(BOSEconomyActive)
        {
            playerAcc.infoMessage("You withdraw: "+playerAcc.getCash());
            BOSEconomy.addPlayerMoney(playerAcc.getPlayerName(), playerAcc.getCash(), false);
            playerAcc.setCash(0);
        }        
        else
        {
            playerAcc.errorMessage("Sorry, you can't checkout money here!");
            playerAcc.sendMessage("Try /bjack "+ChatColor.GRAY+"e"+ChatColor.WHITE+"xchange"+ChatColor.GRAY+"i"+ChatColor.WHITE+"tem command!");
        }
    }
    private void game(PlayerAccount playerAcc,int cash)
    {
                    if(playerAcc.plays())
                    {
                        playerAcc.errorMessage("You already ingame, finish it before start new one!");
                    }
                    else
                    {
                        if(cash==0)
                        {
                            playerAcc.infoMessage("[TRAINING] Game without bets!");
                            playerAcc.dealCards(0);
                        }
                        else{
                            if(cash>0 && cash>=INSTANCE.minBet && cash<=INSTANCE.maxBet)
                            {
                                playerAcc.dealCards(cash);
                            }
                            else if(cash<INSTANCE.minBet || cash>INSTANCE.maxBet)
                            {
                                playerAcc.errorMessage("You can bet only in interval: "+INSTANCE.minBet+" to "+INSTANCE.maxBet);
                            }
                        }
                    }
    }
    private void hit(PlayerAccount playerAcc)
    {
        if(playerAcc.plays())
        {
            playerAcc.nextCard();
        }
        else
        {
            playerAcc.errorMessage("No game in progress, start new one with: /bjack "+ChatColor.WHITE+"g"+ChatColor.RED+"ame!");
        }
    }
    private void stay(PlayerAccount playerAcc)
    {
        if(playerAcc.plays())
        {
            playerAcc.hold();
        }
        else
        {
            playerAcc.errorMessage("No game in progress, start new one with: /bjack "+ChatColor.WHITE+"g"+ChatColor.RED+"ame!");
        }
    }
    private void doubleBet(PlayerAccount playerAcc)
    {
        if(playerAcc.plays())
        {
            playerAcc.doubleBet();
        }
        else
        {
            playerAcc.errorMessage("No game in progress, start new one with: /bjack "+ChatColor.WHITE+"g"+ChatColor.RED+"ame!");
        }
    }
    private void showCards(PlayerAccount playerAcc)
    {
        if(playerAcc.plays())
        {
            playerAcc.showCards();
        }
        else
        {
            playerAcc.errorMessage("No game in progress, start new one with: /bjack "+ChatColor.WHITE+"g"+ChatColor.RED+"ame!");
        }
    }
    private void splitCards(PlayerAccount playerAcc)
    {
        if(playerAcc.plays())
        {
            playerAcc.split();
        }
        else
        {
            playerAcc.errorMessage("No game in progress, start new one with: /bjack "+ChatColor.WHITE+"g"+ChatColor.RED+"ame!");
        }
    }
    private void checkIn(PlayerAccount playerAcc,int cash)
    {
        if(iConomyActive)
        {
            if(cash>0 && iConomy.getAccount(playerAcc.getPlayerName()).getHoldings().hasEnough(cash) && cash>=INSTANCE.minBuyIn && cash<=INSTANCE.maxBuyIn)
            {
                playerAcc.sendMessage("You deposited: "+cash);
                playerAcc.addCash(cash);
                iConomy.getAccount(playerAcc.getPlayerName()).getHoldings().add(-cash);
            }
            else if(!iConomy.getAccount(playerAcc.getPlayerName()).getHoldings().hasEnough(cash))
            {
                playerAcc.errorMessage("You don't have enough money!");
            }
            else if(cash<INSTANCE.minBuyIn || cash>INSTANCE.maxBuyIn)
            {
                playerAcc.errorMessage("You can insert money in interval: "+INSTANCE.minBuyIn+" to "+INSTANCE.maxBuyIn);
            }
            else
            {
                playerAcc.errorMessage("Wrong value!");
            }
        }
        else if(BOSEconomyActive)
        {
            if(cash>0 && BOSEconomy.getPlayerMoney(playerAcc.getPlayerName())>=cash && cash>=INSTANCE.minBuyIn && cash<=INSTANCE.maxBuyIn)
            {
                playerAcc.sendMessage("You deposited: "+cash);
                playerAcc.addCash(cash);
                BOSEconomy.setPlayerMoney(playerAcc.getPlayerName(), (BOSEconomy.getPlayerMoney(playerAcc.getPlayerName())-cash), false);
            }
            else if(BOSEconomy.getPlayerMoney(playerAcc.getPlayerName())<cash)
            {
                playerAcc.errorMessage("You don't have enough money!");
            }
            else if(cash<INSTANCE.minBuyIn || cash>INSTANCE.maxBuyIn)
            {
                playerAcc.errorMessage("You can insert money in interval: "+INSTANCE.minBuyIn+" to "+INSTANCE.maxBuyIn);
            }
            else
            {
                playerAcc.errorMessage("Wrong value!");
            }
        }
        else
        {
            playerAcc.errorMessage("Sorry, you can't check in money here!");
            playerAcc.sendMessage("Try /bjack "+ChatColor.GRAY+"e"+ChatColor.WHITE+"xchange"+ChatColor.GRAY+"c"+ChatColor.WHITE+"redit command!");
        }
    }
    private void help(PlayerAccount playerAcc)
    {
        playerAcc.sendMessage(ChatColor.DARK_AQUA+"["+INSTANCE.desc.getName()+"] plugin by "+INSTANCE.desc.getAuthors().get(0) +", version "+INSTANCE.desc.getVersion());
        playerAcc.sendMessage("You have to checkin for cash games (/bjack "+ChatColor.GRAY+"c"+ChatColor.WHITE+"heck"+ChatColor.GRAY+"i"+ChatColor.WHITE+"n [value])");
        playerAcc.sendMessage("You can take all money with /bjack "+ChatColor.GRAY+"c"+ChatColor.WHITE+"heck"+ChatColor.GRAY+"o"+ChatColor.WHITE+"ut)");
        playerAcc.sendMessage("For game start use /bjack "+ChatColor.GRAY+"g"+ChatColor.WHITE+"ame [bet]");
        playerAcc.sendMessage("To show account balance use /bjack "+ChatColor.GRAY+"b"+ChatColor.WHITE+"alance");
        playerAcc.sendMessage("You can double bet in first round /bjack "+ChatColor.GRAY+"d"+ChatColor.WHITE+"ouble, /double");
        playerAcc.sendMessage("You can split same cards in sec. round /bjack "+ChatColor.GRAY+"s"+ChatColor.WHITE+"plit, /split");
    }
    //setters
    public boolean setiConomy(iConomy plugin) {
        if (iConomy == null)
        {
            iConomyActive=true;
            iConomy = plugin;
            return true;
        }
        return false;
    }
    public boolean setBOSEconomy(BOSEconomy plugin) {
        if (BOSEconomy == null)
        {
            BOSEconomyActive=true;
            BOSEconomy = plugin;
            return true;
        }
        return false;
    }
    private void setupPermissions() {
      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
      if (permissionHandler == null) {
          if (permissionsPlugin != null) {
              permissionHandler = ((Permissions) permissionsPlugin).getHandler();
              log.info("[BlackJack] hooked into Permissions.");
          }
      }
    }
    public void setMinBet(int ammount)
    {
        INSTANCE.minBet=ammount;
    }
    public void setMaxBet(int ammount)
    {
        INSTANCE.maxBet=ammount;
    }
    public void setMinBuyIn(int ammount)
    {
        INSTANCE.minBuyIn=ammount;
    }
    public void setMaxBuyIn(int ammount)
    {
        INSTANCE.maxBuyIn=ammount;
    }
    public void setAnnounce(boolean bool)
    {
        INSTANCE.announce=bool;
    }
    public void setExchanger()
    {
        exchanger=new Exchanger(getDataFolder());
    }
    //getters
    public boolean getExchanger()
    {
        if(this.exchanger!=null)return true;
        return false;
    }
    public String getAccountList()
    {
        if(INSTANCE.accounts==null)return "";
        String ret="";
        for(PlayerAccount playerListed:INSTANCE.accounts)
        {
            if(playerListed!=null)
            {
                ret+=" "+playerListed.getPlayerName();
            }
        }
        return ret;
    }
    public boolean getAnnounce()
    {
        return INSTANCE.announce;
    }
    public boolean getShortCmds()
    {
        return INSTANCE.shortCmdsAllow;
    }
    public double getBlackJackRatio()
    {
        return INSTANCE.blackJackRatio;
    }
    public int getPlayer(Player player)
    {
        if(INSTANCE.accounts==null)
        {
            INSTANCE.accounts=new PlayerAccount[1];
            INSTANCE.accounts[0]=new PlayerAccount(player,Server,INSTANCE.announce);
            return 0;
        }
        for(int i=0;i<INSTANCE.accounts.length;i++)
        {
            if(INSTANCE.accounts[i]==null)continue;
            if(INSTANCE.accounts[i].getPlayerName().equalsIgnoreCase(player.getName()))
            {
                return i;
            }
        }
        PlayerAccount[] temp=new PlayerAccount[INSTANCE.accounts.length+1];
        System.arraycopy(INSTANCE.accounts, 0, temp, 0, INSTANCE.accounts.length);
        temp[INSTANCE.accounts.length]=new PlayerAccount(player,Server,INSTANCE.announce);
        INSTANCE.accounts=new PlayerAccount[temp.length];
        System.arraycopy(temp, 0, INSTANCE.accounts, 0, temp.length);
        return INSTANCE.accounts.length-1;
    }
    public static BlackJack getInstance()
    {
        return INSTANCE;
    }
    public Server getBukkitServer() {
        return Server;
    }

    public iConomy getiConomy() {
        return iConomy;
    }
    public BOSEconomy getBOSEconomy() {
        return BOSEconomy;
    }
    public File getDataDir()
    {
        return getDataFolder();
    }
    /**
     * Converts string to integer, returns positive number or zero.
     * 
     * @param args convertable splited command
     * @param pos position of number in array
     * @return converted number
     */
    private int stringToInt(String[] args,int pos)
    {
        try{
            if(args.length>=pos)
            {
                String numString=args[pos];
                if(numString.length()>0 && numString.length()<=9 && numString.charAt(0)>=48 && numString.charAt(0)<=57)
                {
                    return Math.abs(Integer.parseInt(numString));
                }
            }
        }
        catch(Exception e)
        {
            return 0;
        }
        return 0;
    }
   
}
