package BlackJack;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Server;
/**
 *
 * @author Xeror Battler
 */
public class PlayerAccount {
    private int cash;
    private Player player;
    private Hand hand;
    private Hand splitedHand;
    private Packet packet;
    private int round;
    private int dealerRound;
    private int bet;
    private boolean display=false;
    private double blackJackRatio=1.5;
    private Server server;
    public PlayerAccount(Player player, Server server, boolean display)
    {
        this.player=player;
        this.server=server;
        this.display=display;
        this.blackJackRatio=BlackJack.getInstance().getBlackJackRatio();
    }
    public Player getPlayer()
    {
        return this.player;
    }
    public String getPlayerName()
    {
        return this.player.getName();
    }
    public int getCash()
    {
        return this.cash;
    }
    public void addCash(int value)
    {
        this.cash+=value;
    }
    public void setCash(int value)
    {
        this.cash=Math.abs(value);
    }
    public boolean plays()
    {
        if(this.round>0)return true;
        return false;
    }
    public void dealCards(int bet)
    {
        if(this.cash>=bet)
        {
            this.cash-=bet;
            this.bet=bet;
            this.packet=new Packet();
            this.hand=new Hand(this.player);
            this.splitedHand=null;
            this.player.sendMessage(ChatColor.GREEN+"Game begins!");
            this.player.sendMessage(ChatColor.GREEN+"(Commands: /bjack "+ChatColor.WHITE+"h"+ChatColor.GREEN+"it, /bjack "+ChatColor.WHITE+"s"+ChatColor.GREEN+"tay");
            this.hand.addCard(this.packet.takeCard());
            this.player.sendMessage(ChatColor.GREEN+"Round "+(++this.round)+".: "+hand.showCards());
            if(this.bet>0)
            {
                this.player.sendMessage(ChatColor.GREEN+"Try double your bet with /bjack "+ChatColor.WHITE+"d"+ChatColor.GREEN+"ouble");
            }
        }
        else
        {
            this.player.sendMessage(ChatColor.RED+"You don't have enough credit!");
        }
    }
    public void nextCard()
    {
        Hand temp=(this.splitedHand!=null&&this.round>=50)?this.splitedHand:this.hand;
        if(this.round == 1 && temp!=null && temp.topCardsSame() && this.splitedHand==null)
        {
            this.player.sendMessage(ChatColor.GREEN+"You can split your cards with /bjack "+ChatColor.WHITE+"s"+ChatColor.GREEN+"plit");
        }
        temp.addCard(this.packet.takeCard());
        this.round++;
        this.player.sendMessage(ChatColor.GREEN+"Round "+((this.round>=50)?(this.round-50):this.round));
        this.player.sendMessage(ChatColor.GREEN+"Hand: "+temp.showCards());
        if (temp.getCardSum()>21)
        {
            hold();
        }
    }
    public void showCards()
    {
        if(this.hand!=null)
        {
            this.player.sendMessage(ChatColor.GREEN+"Your cards in hand 1: "+hand.showCards());
        }
        if(this.splitedHand!=null)
        {
            this.player.sendMessage(ChatColor.GREEN+"Your cards in hand 2: "+splitedHand.showCards());
        }
    }
    public void hold()
    {
        int playerCards=(round>=50)?(round-50):round;
        int betCash=0;
        this.player.sendMessage(ChatColor.GREEN+"Your points: "+((round>=50)?splitedHand.getCardSum():hand.getCardSum())+", cards: "+((round>50)?splitedHand.showCards():hand.showCards()));
        int playerScore=(this.round>=50)?splitedHand.getCardSum():hand.getCardSum();
        int dealerScore=0;
        if(playerScore<=21)
        {
            dealerScore=dealer(this.packet,playerScore,playerCards);
        }
        else
        {
            this.player.sendMessage(ChatColor.GREEN+"Sorry, you are over 21!");
        }
        String betString=(this.bet>0)?String.valueOf(this.bet):"";
        //debug
        //this.server.broadcastMessage("BlackJack DEBUG ~ BET: "+((this.bet!=0)?this.bet:"N/A")+", PS:"+((playerScore!=0)?playerScore:"N/A")+", PC:"+((playerCards!=0)?playerCards:"N/A")+", DS:"+((dealerScore!=0)?dealerScore:"N/A")+", DC:"+((this.dealerRound!=0)?this.dealerRound:"N/A"));
        if((playerScore<=21 && (playerScore>dealerScore ||(dealerScore>21&&playerScore<=21)))||(playerScore<=21&&playerScore==dealerScore&&playerCards<this.dealerRound))
        {
            betCash=(this.bet>0)?Integer.parseInt(betString):0;
            int wonCash=betCash;
            if(wonCash>0)
            {
                if(splitedHand!=null && this.round<50)
                {
                    wonCash/=2;
                    betCash/=2;
                }
                if(hand!=null && hand.isBlackJack())
                {
                    wonCash*=this.blackJackRatio;
                }
                else if(splitedHand!=null && splitedHand.isBlackJack())
                {
                    wonCash*=this.blackJackRatio;
                }
            }
            this.player.sendMessage(ChatColor.GREEN+"You won "+wonCash+"!");
            if(this.bet>0)
            {
                this.cash+=betCash+wonCash;
            }
            if(display)
            {
                this.server.broadcastMessage(this.player.getDisplayName()+" has won "+wonCash+" in BlackJack");
            }
            if(this.round>50 && this.splitedHand!=null)
            {
                this.splitedHand=null;
            }
            else if(this.round<50 && this.splitedHand!=null)
            {
                this.hand=null;
            }
        }
        else if(playerScore <= 21 && playerScore==dealerScore && playerCards==this.dealerRound)
        {
            this.player.sendMessage(ChatColor.GREEN+"It's a tie!");
            if(this.bet>0)
            {
                this.cash+=(splitedHand!=null && this.round<50)?Integer.parseInt(betString):Integer.parseInt(betString)/2.0;
            }
            if(this.round>50 && this.splitedHand!=null)
            {
                this.splitedHand=null;
            }
            else if(this.round<50 && this.splitedHand!=null)
            {
                this.hand=null;
            }
        }
        else
        {
            betCash=(this.bet>0)?Integer.parseInt(betString):0;
            if(betCash>0)
            {
                if(splitedHand!=null && this.round<50)
                {
                    betCash/=2;
                }
            }
            this.player.sendMessage(ChatColor.GREEN+"You lose "+betCash+"!");
            if(this.bet>0 && BlackJack.getInstance().getAnnounce())
            {
                this.server.broadcastMessage(this.player.getDisplayName()+" has lost "+betCash+" in BlackJack");
            }
            if(this.round>50 && this.splitedHand!=null)
            {
                this.splitedHand=null;
            }
            else if(this.round<50 && this.splitedHand!=null)
            {
                this.hand=null;
            }
        }
        this.round=(this.splitedHand!=null)?51:0;
        this.bet=(this.round>=50)?this.bet/2:0;
        if(this.round==51)
        {
            this.player.sendMessage(ChatColor.GREEN+"One more hand left ("+splitedHand.showCards()+")");
        }
        else{
            this.round=0;
        }
    }
    public void split()
    {
        if(this.cash>=(this.bet*2) && this.round==2 &&this.splitedHand==null && this.hand!=null && this.hand.topCardsSame())
        {
            this.cash-=bet;
            this.bet*=2;
            this.splitedHand=new Hand(this.player);
            this.splitedHand.addCard(this.hand.split());
            this.round--;
            this.player.sendMessage("You splited the cards!");
        }
        else if(this.splitedHand!=null)
        {
            this.errorMessage("You can split only once!");
        }
        else if(this.hand.topCardsSame())
        {
            this.errorMessage("You need two same cards for split");
        }
        else if(this.round!=2)
        {
            this.errorMessage("You can split only in second round!");
        }
        else if(this.cash<(this.bet*2))
        {
            this.errorMessage("You don't have enough credit!");
        }
        else
        {
            this.errorMessage("You can't split now!");
        }
    }
    private int dealer(Packet packet, int playerScore, int playerCards)
    {
        Hand dealerHand=new Hand(null);
        int sum=0;
        this.dealerRound=0;
        while(sum < 21 && (sum < playerScore || sum <=17))
        {
            this.dealerRound++;
            dealerHand.addCard(packet.takeCard());
            sum=dealerHand.getCardSum();
            if(playerScore>21 && sum>=17 || playerScore<sum && sum>=17)break;
            if(playerScore==21&&playerCards<=this.dealerRound)break;
        }
        this.player.sendMessage(ChatColor.GRAY+"Dealer's points: "+dealerHand.getCardSum()+" ( "+dealerHand.showCards()+")");
        return sum;
    }
    public void playerQuit()
    {
        if(this.round>0)
        {
            this.round=0;
        }
    }
    public void doubleBet()
    {
        if(this.round==1){
            if(this.cash>=this.bet)
            {
                this.cash-=this.bet;
                this.bet*=2;
                this.player.sendMessage(ChatColor.GREEN+"You doubled your bet");
                this.nextCard();
                this.hold();
            }
            else
            {
                this.errorMessage("You don't have enough credit!");
            }
        }
        else
        {
            this.errorMessage("You can't double the bet!");
        }
    }
    public void errorMessage(String message)
    {
        if(message.length()>0)
        {
            this.player.sendMessage(ChatColor.RED+message);
        }
    }
    public void sendMessage(String message)
    {
        if(message.length()>0)
        {
            this.player.sendMessage(ChatColor.WHITE+message);
        }
    }
    public void infoMessage(String message)
    {
        if(message.length()>0)
        {
            this.player.sendMessage(ChatColor.GRAY+message);
        }
    }
}
