package networking.project.game.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

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
	private ArrayList<Entity> entities;
	private Comparator<Entity> renderSorter = (a, b) -> {
        if(a.getY() + a.getHeight() < b.getY() + b.getHeight())
            return -1;
        return 1;
    };
	
	public EntityManager(Handler handler, ArrayList<Player> player){
		this.handler = handler;
		entities = new ArrayList<>();
        player.forEach(this::addEntity);
	}
	
	/**
	 * 	Calls the tick() method of each entity, and sorts the entities 
	 *  within the entities ArrayList.
	 */
	public void tick(){
		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext())
		{
			Entity e = entityIterator.next();
			e.tick();
			if (!e.isActive())
				entityIterator.remove();
		}

		entities.sort(renderSorter);
	}
	
	/**
	 * Calls the render() method of each entity.
	 * 
	 * @param g
	 */
	public void render(Graphics g) {
		entities.forEach(e -> e.render(g));
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
