package networking.project.game.entities.events;

import networking.project.game.entities.creatures.Player;

/**
 * Created by nick on 4/3/17.
 */
public class PlayerDisconnectEvent extends PlayerEvent {

    public PlayerDisconnectEvent(Player p)
    {
        super(p);
    }
}