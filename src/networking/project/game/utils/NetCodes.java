package networking.project.game.utils;

/**
 * Created by nick on 3/28/17.
 */
public interface NetCodes {
    // SERVER VS CLIENT (server related data)
    byte CONN_DATA = 0x01; // Client is talking to the server about non-game stuff
    byte CONN_REQ = 0x02; // Client is connecting to the server
    byte CONN_DISC = 0x03; // Client is disconnecting from the server
    byte CONN_MSG = 0x04; // Server telling us a message
    // CLIENT VS CLIENT (player and game data)
    byte GAME_DATA = 0x10; // Client and server are talking about the game
    byte GAME_PLAYER_UPDATE = 0x11; // Update player positioning
    byte GAME_PROJ_UPDATE = 0x12; // Update projectile stuff
    byte GAME_KILL_UPDATE = 0x13; // Something died
    byte GAME_START = 0x14; // Start a new game
}
