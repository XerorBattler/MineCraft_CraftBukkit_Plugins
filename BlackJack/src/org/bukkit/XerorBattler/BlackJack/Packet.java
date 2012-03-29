package BlackJack;

import java.util.Collections;
import java.util.Arrays;
import org.bukkit.ChatColor;
/**
 * Card packet class
 * 
 * @author Xeror Battler
 * @version 1.1
 */
public class Packet {
    private Card[] packet=new Card[4*13];
    private int cardCount=52;
    /**
     * Creates shuffled packet
     */
    public Packet()
    {
        int value=0;
        String text="";
        ChatColor color=ChatColor.WHITE;
        for(int i=0;i<4;i++)
        {
            if(i<=1)
            {
                color=ChatColor.DARK_GRAY;
            }
            else
            {
                color=ChatColor.RED;
            }
            for(int j=0;j<13;j++)
            {
                if(j<9)
                {
                    text=String.valueOf(j+2);
                    value=j+2;
                }
                else
                {
                    switch(j){
                        case 9:
                            text="J";
                            value=10;
                        break;
                        case 10:
                            text="Q";
                            value=10;
                        break;
                        case 11:
                            text="K";
                            value=10;
                        break;
                        case 12:
                            text="A";
                            value=11;
                        break;
                    }
                }
                this.packet[i*13+j]=new Card(text,value,color);
            }
        }
        Collections.shuffle(Arrays.asList(this.packet));
        Collections.shuffle(Arrays.asList(this.packet));
    }
    /**
     * Method which takes random card from packet
     * 
     * @return Card card
     */
    public Card takeCard()
    {
        this.cardCount--;
        Card card=packet[cardCount];
        packet[cardCount]=null;
        return card;
    }
    /**
     * Method which count card value
     * 
     * @return int packet value
     */
    public int packetValue()
    {
        int sum=0;
        for(Card card:packet)
        {   
            if(card==null)continue;
            sum+=card.getValue();
        }
        return sum;
    }
    /**
     * Getter for card count
     * 
     * @return int card count 
     */
    public int getCardsLeft()
    {
        return this.cardCount;
    }
    /**
     * Creates String with card list
     * 
     * @return String cards
     */
    @Override
    public String toString()
    {
        String ret="";
        for (Card card : packet)
        {
            if(card==null)continue;
            ret+=" "+card.getColor()+card.getName();
        }
        return ret;
    }
    
    
}
