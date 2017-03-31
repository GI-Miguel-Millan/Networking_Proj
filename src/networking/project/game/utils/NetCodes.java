package networking.project.game.utils;

/**
 * Created by nick on 3/28/17.
 */
public interface NetCodes {
    byte CONN_REQ = 0x01; // Client is connecting to the server
    byte CONN_DISC = 0x02; // Client is disconnecting from the server
    byte CONN_MSG = 0x03; // Server telling us a message
    // CLIENT VS CLIENT (player and game data)
    byte GAME_PLAYER_UPDATE = 0x20; // Update player positioning
    byte GAME_PROJ_UPDATE = 0x21; // Update projectile stuff
    byte GAME_KILL_UPDATE = 0x22; // Something died
    byte GAME_START = 0x23; // Start a new game
}
