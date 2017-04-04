package networking.project.game.utils;

/**
 * Created by nick on 3/28/17.
 */
public interface InputFlags {
    byte IN_UP = 1;
    byte IN_RIGHT = 1 << 1;
    byte IN_DOWN = 1 << 2;
    byte IN_LEFT = 1 << 3;
    byte IN_ATTK = 1 << 4;
    byte IN_CTRL = 1 << 5;
    byte IN_ESC = 1 << 6;
}
