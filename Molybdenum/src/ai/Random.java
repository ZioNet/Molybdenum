package ai;

import entity.Creature;
import entity.EntityManager;
import game.Molybdenum;

public class Random extends AI{
	
	public Random(Creature c){
		super(c);
	}
	public void update(int delta){}
	public void move(EntityManager em){
		int nx = c.x;
		int ny = c.y;
		
		nx += Molybdenum.getRandom().nextInt(3)-1;
		ny += Molybdenum.getRandom().nextInt(3)-1;
		
		c.move(nx, ny);
	}
	
}
