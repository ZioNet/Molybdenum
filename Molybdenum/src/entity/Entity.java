package entity;

import game.Molybdenum;

import java.awt.Color;

import state.GameState;
import state.StateManager;
import map.MapData;

public class Entity {
	
	public MapData map;
	
	public Color color;
	public char character;
	
	public int x;
	public int y;
	
	public EntityManager getEm(){
		return ((GameState)Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).em;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
}
