package com.base.engine.tileEffects;

import org.newdawn.slick.Graphics;

import com.base.engine.world.*;

public interface TileEffect 
{
	public void act(Player p);
	public void act(GridObject o);
	public void draw(Graphics b, int x, int y);
	public void update();
	public int getType();
	public boolean remove();
	public int getDuration();
}
