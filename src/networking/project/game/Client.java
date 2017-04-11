package networking.project.game;

import networking.project.game.entities.creatures.Player;
import networking.project.game.entities.creatures.projectiles.Projectile;
import networking.project.game.network.packets.*;
import networking.project.game.utils.NetCodes;
import networking.project.game.utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client implements Runnable, NetCodes {
    private Thread thread;
    private InputThread inThread;
    private boolean running = false;
    private Game game = null;
    private int playerID;
    private String hostname;

    public Client(String hostname) {
        this.hostname = hostname;
    }

    public void startClient() {
        System.out.println("Started client!");
        this.start();
    }

    private void tick() {
//        game.getGameCamera().centerOnEntity(game.getHandler().getClientPlayer());
//        //game.getGameCamera().centerOnCursor();
//        game.tick();
//        game.render();
//        game.getKeyManager().tick();
    }

    @Override
    public void run() {
        int fps = 60;  // How many times every second we want to run tick() and render()
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        int port = 7777;

        try {
            DatagramSocket client_socket = new DatagramSocket();

            InetAddress host;

            try {
                host = InetAddress.getByName(hostname);
            } catch (UnknownHostException e) {
                host = InetAddress.getByName("localhost");    // default to localhost
                e.printStackTrace();
            }


            ConnectionPacket connect = new ConnectionPacket();
            connect.type = CONN_REQ;
            connect.ID = -1;        // This client doesn't have an ID yet;
            connect.compose();
            connect.send(client_socket, host, port);

            while (running) {
                now = System.nanoTime();
                delta += (now - lastTime) / timePerTick;
                lastTime = now;

                if (delta >= 1 /*&& sendData*/) {
                    tick();
                    delta--;
                }
                // Get data from server
                byte[] buffer = new byte[1500];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                client_socket.receive(reply);

                byte[] data = reply.getData();
                Packet p = Packet.determinePacket(data);
                evaluateData(p, reply, client_socket);
            }
            stop();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void evaluateData(Packet p, DatagramPacket serverDatagram, DatagramSocket clientSocket) {

        if (p instanceof ConnectionPacket) {
            Utils.debug("received connection packet");
            ConnectionPacket cp = (ConnectionPacket) p;
            switch (cp.type) {
                case CONN_ACK: {
                    playerID = cp.ID;

                    break;
                }
                case CONN_DISC: {
                    int otherID = cp.ID;
                    game.getHandler().getPlayer(otherID).setActive(false);
                }
                default:
                    break;
            }
        }
        if (p instanceof GameStartPacket) {
            Utils.debug("received gameStart packet");
            GameStartPacket gs = (GameStartPacket) p;
            game = new Game("Battle Arena", gs.gameWidth, gs.gameHeight);
            game.init(false);
            int numP = gs.numPlayers;
            for (int i = 0; i < numP; i++) {
                // players are identified by their ID's, so no need
                // to specify the address and port on the client side.
                game.getHandler().getPlayers().add(new Player(game.getHandler(), 0, 0, null, -1, i + 1));
            }

            game.getHandler().setClientPlayer(playerID);
            game.getGameState().displayState();
            game.start();
            inThread = new InputThread(game, clientSocket, serverDatagram.getAddress(), serverDatagram.getPort());
            game.getHandler().getClientPlayer().addListener(inThread);
            inThread.start();

        } else if (p instanceof PlayerUpdatePacket) {
            Utils.debug("received playerUpdate packet");
            PlayerUpdatePacket pup = (PlayerUpdatePacket) p;
            Player player = game.getHandler().getPlayer(pup.ID);
            if (player == null) {
                player = new Player(game.getHandler(), 0, 0, null, -1, pup.ID);
                game.getHandler().getPlayers().add(player);
            }

            player.setInput(pup.input);
            player.setHealth(pup.health);
            player.setX(pup.posX);
            player.setY(pup.posY);
            player.setRotation(pup.rotation);
        } else if (p instanceof ProjectileUpdatePacket) {
            Utils.debug("received ProjectileUpdatePacket");
            ProjectileUpdatePacket pup = (ProjectileUpdatePacket) p;
            Handler h = game.getHandler();
            h.getWorld().getEntityManager().addEntity(new Projectile(h, h.getPlayer(pup.parentID), pup.mX, pup.mY, pup.ID));
            if (h.getClientPlayer().getID() == pup.parentID) {
                h.getClientPlayer().setReadyToFire(false);
            }
        }
    }

    /**
     * Starts the thread which runs the game,
     * if it is not already running
     */
    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();

    }

    /**
     * Joins the game thread, stopping the game,
     * if it is already running.
     */
    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
            inThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}