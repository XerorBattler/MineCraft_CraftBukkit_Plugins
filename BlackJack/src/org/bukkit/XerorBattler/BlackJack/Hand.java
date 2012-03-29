package BlackJack;

import org.bukkit.entity.Player;

/**
 * Class which represent player hand
 *  
 * @author Xeror Battler
 * @version 1.1
 */
public class Hand {
    private Card[] hand;
    /**
     * Hand constructor
     * 
     * @param owner player
     */
    public Hand(Player owner)
    {
        this.hand=null;
    }
    /**
     * Adds card to player hand
     * 
     * @param card added card
     */
    public void addCard(Card card)
    {
        if(this.hand==null)
        {
           this.hand=new Card[1];
           this.hand[0]=card;
        }
        else
        {
            Card[] newHand=new Card[this.hand.length+1];
            System.arraycopy(this.hand,0,newHand,0,this.hand.length);
            newHand[newHand.length-1]=card;
            this.hand=new Card[newHand.length];
            System.arraycopy(newHand,0,this.hand,0,newHand.length);
        }
    }
    /**
     * Counts card value in hand, getter
     * 
     * @return int card sum
     */
    public int getCardSum()
    {
        int sum=0;
        int ace=0;
        if(hand==null)return 0;
        for (Card card : hand)
        {
            if(card.getValue()==11)
            {
                ace++;
            }
            sum+=card.getValue();
        }
        if(ace==2 && sum==22)return 21;
        while(sum>21 && ace>0)
        {
            sum-=10;
            ace--;
        }
        return sum;
    }
    /**
     * Method which puts all cards names into String
     * 
     * @return String cards in String
     */
    public String showCards()
    {
        String ret="";
        if(hand==null)return null;
        for(Card card:hand)
        {
            ret+=card.getColor()+card.getName()+" ";
        }
        return ret;
    }
    /**
     * Verify if the cards are the same, if so returns the second of these
     * 
     * @return Card splitted card
     */
    public Card split()
    {
        if(topCardsSame()&&this.hand!=null)
        {
                Card[] tempHand=new Card[this.hand.length];
                System.arraycopy(this.hand,0,tempHand,0,this.hand.length);
                this.hand=new Card[this.hand.length-1];
                System.arraycopy(tempHand,0,this.hand,0,this.hand.length);
                return this.hand[this.hand.length-1];
        }
        return null;
    }
    /**
     * Verify if the cards are the same, if so returns true
     * 
     * @return boolean true if are cards same
     */
    public boolean topCardsSame()
    {
        if(hand!=null && hand.length==2)
        {
            if(hand[0].getName().equalsIgnoreCase(hand[1].getName()))
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Counts cards, getter
     * 
     * @return int card count
     */
    public int getCountCards()
    {
        return this.hand.length;
    }
    /**
     * Verify if the cards are blackjack, if so returns true
     * 
     * @return boolean if are cards blackjack
     */
    public boolean isBlackJack()
    {
        if(hand.length==2 && getCardSum()==21)
        {
            return true;
        }
        return false;
    }
    /**
     * Creates String with card in hand list
     * 
     * @return String cards
     */
    @Override
    public String toString()
    {
        String ret="";
        for(Card card:hand)
        {   
            ret+=" | "+card.getColor()+card.getName()+" "+card.getValue();
        }
        return ret;
    }
    
}
