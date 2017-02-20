package networking.project.game.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.*;
import networking.project.game.entities.creatures.enemies.*;
import networking.project.game.entities.creatures.enemies.bosses.*;
import networking.project.game.entities.statics.DeadEntity;
import networking.project.game.entities.statics.powerups.PowerUp;

/**
 *	The EntityManager manages all entities, rendering each entity
 *	and calling its tick method.
 * 	
 *	@author 
 *	@version 1.0
 *	@since version 1.0
 */
public class EntityManager {
	
	private Handler handler;
	private Player player;
	private ArrayList<Entity> entities;
	private Comparator<Entity> renderSorter = new Comparator<Entity>(){
		@Override
		public int compare(Entity a, Entity b) {
			if(a.getY() + a.getHeight() < b.getY() + b.getHeight())
				return -1;
			return 1;
		}
	};
	
	public EntityManager(Handler handler, Player player){
		this.handler = handler;
		this.player = player;
		entities = new ArrayList<Entity>();
		addEntity(player);
	}
	
	/**
	 * 	Calls the tick() method of each entity, and sorts the entities 
	 *  within the entities ArrayList.
	 */
	public void tick(){
		for(int i = 0;i < entities.size();i++){
			Entity e = entities.get(i);
			e.tick();
			
			if(!e.isActive() && e.isEnemy()){
				PowerUp.spawnPowerUp(handler, (int)e.getX(), (int)e.getY());
				DeadEntity.addDeadEntity(handler, e);
			}
			
			// If an Entity has died since the last tick(), remove it from entities.
			if(!e.isActive())
				entities.remove(e);
		}
		
		for (int i = 0; i < PowerUp.powerUps.size();i++){
			Entity e = PowerUp.powerUps.get(i);
			e.tick();
			
			if(!e.isActive())
				PowerUp.powerUps.remove(e);
		}
		
		for (int i = 0; i < DeadEntity.deadEntities.size();i++){
			Entity e = DeadEntity.deadEntities.get(i);
			e.tick();
			
			if(!e.isActive())
				DeadEntity.deadEntities.remove(e);
		}
		entities.sort(renderSorter);
	}
	
	/**
	 * Calls the render() method of each entity.
	 * 
	 * @param g
	 */
	public void render(Graphics g){
		for(Entity e: PowerUp.powerUps){
			e.render(g);
		}
		
		for(Entity e : entities){
			e.render(g);
		}
		
		for(Entity e : DeadEntity.deadEntities){
			e.render(g);
		}
	}
	
	/**
	 * Method used to create a new enemy of a given type and position.
	 * Type 1 = Interceptor, Type 2 = AssaultFighter, Type 3 = StealthFigher,
	 * Default = LesserInterceptor
	 * 
	 * @param handler 
	 * @param x the x position of the enemy being spawned
	 * @param y the y position of the enemy being spawned
	 * @param type what type of enemy it is depends on the integer assigned to each.
	 */
	public void spawnEnemy(Handler handler, int x, int y, int type){
		if(type == 1){
			Interceptor enemy = new Interceptor(handler, x, y);
				this.addEntity(enemy);
		}else if(type == 2){
			AssaultFighter enemy = new AssaultFighter(handler, x, y);
				this.addEntity(enemy);
		}else if(type == 3){
			StealthFighter enemy = new StealthFighter(handler, x, y);
				this.addEntity(enemy);
		}else{
			try{
				GreaterInterceptor enemy = new GreaterInterceptor(handler, x, y);
				if(!enemy.checkEntityCollisions(0, 0) && enemy.isLegalSpawn())
					this.addEntity(enemy);
			}catch(Exception e){
				System.out.println("You tried to spawn a lesserInterceptor before the game finished loading");
			}
			
		}
	}
	
	
	
	/**
	 * Adds an Entity to the entities ArrayList.
	 * 
	 * @param e the Entity to add to entities.
	 */
	public void addEntity(Entity e){
			entities.add(e);
	}
	
	/**
	 * Removes an Entity from the entities ArrayList.
	 * 
	 * @param e the Entity to remove from entities.
	 */
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public Entity getEntity(int index){
		return entities.get(index);
	}
	
	/**
	 * @param e an Entity contained in the entities ArrayList
	 * @return the index of e in the entities ArrayList
	 */
	public int getIndex(Entity e){
		return entities.indexOf(e);
	}
	/**
	 * @return handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * @param handler the Handler 
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets a new player to be the player.
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return entities the ArrayList of entities.
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}

	/**
	 * Sets a new ArrayList of entities to entities.
	 * @param entities
	 */
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

	/**
	 * Method used to create a Boss of a given type and position.
	 * Type 0 = EagleBoss, Type 1 = GiantHeadBoss, Type 2 = Mothership,
	 * Type 3 = DarkTumorRang, Default = EagleBoss
	 * 
	 * @param handler 
	 * @param x the x position of the boss being spawned
	 * @param y the y position of the boss being spawned
	 * @param type what type of boss it is depends on the integer assigned to each.
	 */
	public void spawnBoss(Handler handler, int x, int y, int type) {
		if(type == 0){
			this.addEntity(new EagleBoss(handler, x, y));
		}else if(type == 1){
			this.addEntity(new GiantHeadBoss(handler, x, y));
		}else if(type == 2){
			this.addEntity(new MothershipBoss(handler, x, y));
		}else if(type == 3){
			this.addEntity(new DarkTumorRang(handler, x, y));
		}else{
			this.addEntity(new EagleBoss(handler, x, y));
		}
		
	}

	public void removeLesserEnemies() {
		for(Entity e: entities){
			if(!e.isBoss() && !e.getClass().equals(Player.class) && !e.isProjectile() && !e.getClass().equals(GreaterInterceptor.class)){
				e.setActive(false);
			}
		}	
	}
}
