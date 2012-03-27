package BlackJack;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
/**
 * BlackJack player listener
 * 
 * @author Xeror Battler
 * @version 1.0
 */
public class BJPlayerListener extends PlayerListener 
{
    private BlackJack instance;
    /**
     * Sends BlackJack instance to listener for further use
     * 
     * @param instance BlackJack instance
     */
    public BJPlayerListener(BlackJack instance)
    {
        this.instance=instance;
    }
    /**
     * Call special clearing event on playerQuit, overrides Bukkit class
     * 
     * @param PlayerQuitEvent
     */
@Override
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.instance.playerLeft(event.getPlayer());
    }
}
