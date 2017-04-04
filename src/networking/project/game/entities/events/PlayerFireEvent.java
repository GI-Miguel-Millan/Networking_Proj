package networking.project.game.entities.events;

import networking.project.game.entities.creatures.Player;

/**
 * Created by nick on 4/3/17.
 */
public class PlayerFireEvent extends PlayerEvent {

    public PlayerFireEvent(Player p)
    {
        super(p);
    }

}
