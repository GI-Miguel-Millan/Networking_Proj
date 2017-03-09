package networking.project.game.entities;

import java.awt.Graphics;
import java.util.*;
import java.util.Map.Entry;

import networking.project.game.Handler;
import networking.project.game.entities.creatures.*;

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
	private HashMap<Integer, Entity> entities;
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
		entities = new HashMap<Integer, Entity>();
		for (Player p: player)
			addEntity(p);
	}
	
	/**
	 * 	Calls the tick() method of each entity, and sorts the entities 
	 *  within the entities ArrayList.
	 */
	public void tick(){

		Set<Entry<Integer, Entity>> set = entities.entrySet();
		
		Iterator<Entry<Integer, Entity>> iter = set.iterator();
		
		ArrayList<Integer> entities_to_remove = new ArrayList<Integer>();
		
		while(iter.hasNext()){
			Entity e = iter.next().getValue();
			e.tick();
			
			if(!e.isActive())
				entities_to_remove.add(e.getID());
		}
		
		//If an Entity has died since the last tick(), remove it from entities.
		Iterator<Integer> iter2 = entities_to_remove.iterator();
		while(iter2.hasNext()){
			entities.remove(iter2.next());
		}
		
		//entities.sort(renderSorter);
	}
	
	/**
	 * Calls the render() method of each entity.
	 * 
	 * @param g
	 */
	public void render(Graphics g){
		
		Set<Entry<Integer, Entity>> set = entities.entrySet();
		
		Iterator<Entry<Integer, Entity>> iter = set.iterator();
		
		while(iter.hasNext()){
			iter.next().getValue().render(g);
		}
	}

	/**
	 * Adds an Entity to the entities ArrayList.
	 * 
	 * @param e the Entity to add to entities.
	 */
	public void addEntity(Entity e){
			entities.put(e.getID(), e);
	}
	
	/**
	 * Removes an Entity from the entities hash map.
	 * 
	 * @param e the Entity to remove from entities.
	 */
	public void removeEntity(Entity e){
		entities.remove(e.getID());
	}
	
	/**
	 * Removes an Entity from the entities hash map.
	 * 
	 * @param e the Entity to remove from entities.
	 */
	public void removeEntity(int id){
		entities.remove(id);
	}
	
	public Entity getEntity(int index){
		return entities.get(index);
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
	 * @return entities the HashMap of entities.
	 */
	public HashMap<Integer, Entity> getEntities() {
		return entities;
	}

	/**
	 * Sets a new ArrayList of entities to entities.
	 * @param entities
	 */
	public void setEntities(HashMap<Integer, Entity> entities) {
		this.entities = entities;
	}

}
