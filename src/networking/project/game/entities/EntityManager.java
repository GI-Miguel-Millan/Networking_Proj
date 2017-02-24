package networking.project.game.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.*;
import networking.project.game.entities.statics.DeadEntity;

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
	private ArrayList<Entity> entities;
	private Comparator<Entity> renderSorter = new Comparator<Entity>(){
		@Override
		public int compare(Entity a, Entity b) {
			if(a.getY() + a.getHeight() < b.getY() + b.getHeight())
				return -1;
			return 1;
		}
	};
	
	public EntityManager(Handler handler, ArrayList<Player> player){
		this.handler = handler;
		entities = new ArrayList<Entity>();
		for (Player p: player)
			addEntity(p);
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
				DeadEntity.addDeadEntity(handler, e);
			}
			
			// If an Entity has died since the last tick(), remove it from entities.
			if(!e.isActive())
				entities.remove(e);
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
		
		for(Entity e : entities){
			e.render(g);
		}
		
		for(Entity e : DeadEntity.deadEntities){
			e.render(g);
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

}
