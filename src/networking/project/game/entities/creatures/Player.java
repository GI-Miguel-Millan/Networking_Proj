package networking.project.game.entities.creatures;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.gfx.Assets;
import networking.project.game.gfx.GameCamera;
import networking.project.game.input.MouseManager;
import networking.project.game.utils.InputFlags;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.InetAddress;

/**
 * Player is a Creature controlled by the user. This class takes input from the user
 * for movement, and is able to shoot projectiles.
 *
 * @author
 * @version 1.0
 * @since version 1.0
 */
public class Player extends Creature implements InputFlags {
    //Networking info
    private InetAddress ip;
    private final int port;

    // The player's input. Use the InputFlags interface
    // and bitwise & to check if an input is currently on.
    // Ex: If a player is moving up, do (input & IN_UP) == IN_UP
    private byte input;

    private double rotation;

    private boolean readyFire;
    private int counter;
    private int score = 1000;
    private Rectangle playerBounds = new Rectangle(16, 22, 32, 12);
    private float mouseX = 0, mouseY = 0, camX = 0, camY = 0;
    public boolean wantToFire = false;

    public Player(Handler handler, float x, float y, InetAddress ip, int port, int id) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT, id);

        //bounds = playerBounds;
        counter = 0;
        input = 0;
        readyFire = true;
        health = 50;
        speed = 5;

        this.ip = ip;
        this.port = port;
    }

    @Override
    public void tick() {
        // Calculations for local player
        if (this == handler.getClientPlayer()) {
            GameCamera gc = handler.getGameCamera();
            MouseManager mouse = handler.getMouseManager();

            mouseX = mouse.getMouseX();
            mouseY = mouse.getMouseY();
            camX = gc.getXOffset();
            camY = gc.getYOffset();

            posX = (x - gc.getXOffset());
            posY = (y - gc.getYOffset());

            rotation = Math.atan2((posX + width / 2) - mouseX, -((posY + height / 2) - mouseY)) - Math.PI;

            if (isPressingKey(IN_ESC)) {
                // TODO: DEAL WITH THIS
            }

            if (isPressingKey(IN_ATTK) && readyFire)
            {
                // TODO let the server know we fired, perhaps respond to the server's packet instead of creating this here?
//                handler.getWorld().getEntityManager().addEntity(new Projectile(handler, this, mouseX, mouseY, 100));
//                readyFire = false;
            	
            	wantToFire = true;
            }else
            	wantToFire = false;
        }

        applyInput();

        //lowerBoundCheck();
        updateFireCounter();

        move();
        // only clear movement values after we've moved.
        yMove = 0;
        xMove = 0;
    }

    /**
     * Updates counters
     */
    private void updateFireCounter() {
        if (!readyFire && !isPressingKey(IN_ATTK))
            counter++;

        if (counter >= 20) {
            readyFire = true;
            counter = 0;
        }
    }


    /**
     * Gets input from the user and sets the players yMove and
     * xMove according to which key is pressed.
     */
    public void applyInput() {
        //System.out.println("up: " + up +" down: " + down +" left: " + left +" right: "+ right );

        if (isPressingKey(IN_UP)) {
            yMove = -speed;
        }

        if (isPressingKey(IN_DOWN)) {
            yMove = speed;
        }

        if (isPressingKey(IN_LEFT)) {
            xMove = -speed;
        }

        if (isPressingKey(IN_RIGHT)) {
            xMove = speed;
        }

        if (isPressingKey(IN_ESC)) {
            // TODO: Do we want to do anything with this?
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        AffineTransform transform = gr.getTransform();
        GameCamera gc = handler.getGameCamera();
        posX = (x - gc.getXOffset());
        posY = (y - gc.getYOffset());

        gr.rotate(rotation, posX + width / 2.0, posY + width / 2.0);

        if (this.ID == 1)
            gr.drawImage(Assets.eagle, (int) posX, (int) posY, width, height, null);
        if (this.ID == 2)
            gr.drawImage(Assets.assault, (int) posX, (int) posY, width, height, null);
        if (this.ID == 3)
            gr.drawImage(Assets.stealth, (int) posX, (int) posY, width, height, null);
        if (this.ID == 4)
            gr.drawImage(Assets.interceptor, (int) posX, (int) posY, width, height, null);

        gr.setTransform(transform);

        //g.drawLine((int)posX+width/2, (int)posY, (int)(handler.getMouseManager().getMouseX()),(int)(handler.getMouseManager().getMouseY()));
        //g.drawRect(posX, posY, width, height);
//		g.fillRect((int) (x + bounds.x - handler.getGameCamera().getXOffset()),
//				(int) (y + bounds.y - handler.getGameCamera().getYOffset()),
//				bounds.width, bounds.height);
    }

    @Override
    public void die() {
        // implement action upon player death
        handler.getK_ID().add(this.ID);
        active = false;
    }

    /**
     * The hurt method of the Player must be overridden so that
     * every time the player takes damage, the handler can update
     * the player health.
     *
     * @Override
     */
    public void hurt(int amt) {
        if (!isInvinc) {
            health -= amt;
            //isHurt = true;
        }


        if (health <= 0) {
            active = false;
            die();
        }
    }

    /**
     * Add integer to the players score.
     *
     * @param score
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * @return player score
     */
    public int getScore() {
        return score;
    }

    public boolean getIsInvinc() {
        return isInvinc;
    }

    public InetAddress getIP() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public int getID() {
        return this.ID;
    }

    public String toString() {
        return "Player ID: " + getID() + ", Address: " + getIP() + ", Port: " + getPort() + ", x position: " + getX() + " y position: " + getY();

    }

    public void setMouseCoord(float x, float y) {
        mouseX = x;
        mouseY = y;
    }

    public float getMouseX() {
        return mouseX;
    }

    public float getMouseY() {

        return mouseY;
    }

    public boolean isReadyToFire() {
        return readyFire;
    }

    public void setReadyToFire(boolean b) {
        readyFire = b;
    }

    public float getCamX() {
        return camX;
    }

    public float getCamY() {
        return camY;
    }

    // Should be called from parsing data from the server
    public void setCamPosition(float cx, float cy) {
        this.camX = cx;
        this.camY = cy;
    }

    public boolean isPressingKey(byte toCheck) {
        return (input & toCheck) == toCheck;
    }

    public void setInput(byte newInput) {
        input = newInput;
    }

    public byte getInput()
    {
        return input;
    }

    public double getRotation()
    {
        return this.rotation;
    }

    public void setRotation(double rot)
    {
        rotation = rot;
    }
}


