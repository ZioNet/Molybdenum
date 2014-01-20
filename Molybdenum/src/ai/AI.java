package ai;

import entity.Creature;
import entity.EntityManager;

public abstract class AI {
	
	protected Creature c;
	
	public AI(Creature c){
		this.c = c;
	}
	
	public abstract void update(int delta);
	public abstract void move(EntityManager em);
	public void generatePath(){}
	public void injured(){}
	
}
