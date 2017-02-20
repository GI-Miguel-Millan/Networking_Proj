package networking.project.game.entities.creatures.enemies;

import networking.project.game.Handler;

public class GreaterInterceptor extends Interceptor{

	public GreaterInterceptor(Handler handler, float x, float y) {
		super(handler, x, y);
		health = 2;
	}

}
