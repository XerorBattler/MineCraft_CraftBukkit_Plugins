package BlackJack;

import org.bukkit.ChatColor;
/**
 * Card class
 * 
 * @author Xeror Battler
 * @version 1.1
 */
public class Card {
    private String cardName;
    private int cardValue;
    private ChatColor cardColor;
    /**
     * Card constructor
     * 
     * @param cardName String card name
     * @param cardValue int card value
     * @param cardColor ChatColor card color
     */
    public Card(String cardName, int cardValue, ChatColor cardColor)
    {
        this.cardName=cardName;
        this.cardValue=cardValue;
        this.cardColor=cardColor;
    }
    /**
     * Card name getter
     * 
     * @return String card name
     */
    public String getName()
    {
        return this.cardName;
    }
    /**
     * Card color getter
     * 
     * @return ChatColor card color
     */
    public ChatColor getColor()
    {
        return this.cardColor;
    }
    /**
     * Card value getter
     * 
     * @return int card value
     */
    public int getValue()
    {
        return this.cardValue;
    }
}
