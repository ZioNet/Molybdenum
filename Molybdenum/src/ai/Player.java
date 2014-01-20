package ai;

import org.lwjgl.input.Keyboard;

import entity.Creature;
import entity.EntityManager;
import game.Molybdenum;

public class Player extends AI{

	public Player(Creature c) {
		super(c);
	}

	public void update(int delta){}

	public void move(EntityManager em) {		
		int nx = c.x;
		int ny = c.y;
		if(!c.isDancing){
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				ny++;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				ny--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				nx--;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				nx++;
			}
		}else{
			nx += Molybdenum.getRandom().nextInt(3)-1;
			ny += Molybdenum.getRandom().nextInt(3)-1;
		}

		if(c.canAct){
			c.move(nx,ny);
			em.speedCounter++;
		}		
	}

}
