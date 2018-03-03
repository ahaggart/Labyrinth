package com.base.engine.world;
import	org.newdawn.slick.Graphics;
import	org.newdawn.slick.Color;

import com.base.engine.buffs.Buff;
import com.base.engine.core.BaseGame;

import	java.util.ArrayList;

public class GridObject
{	
	protected float x, y;
	protected int width = 16;
	protected Cell occupied;
	protected World world;
	
	protected boolean remove = false;
	
	public GridObject(float x, float y, World world)
	{
		this.x = x;
		this.y = y;
		this.world = world;
	}
	
	public GridObject(Cell start, World world)
	{
		this(start.getX(), start.getY(), world);
	}
	
	public void update()
	{

	}
	
	public void draw(Graphics b)
	{
		if(inBounds())
		{
			b.setColor(new Color(0,0,1,0.4f));
			b.fillOval(x - width / 2 + World.width / 2, y - width / 2 + World.height / 2, width, width);
			b.setColor(Color.green);
			b.drawOval(x - width / 2 + World.width / 2, y - width / 2 + World.height / 2, width, width);
		}		
	}
	
	public boolean inBounds()
	{
		if(x + World.width / 2 + width / 2 > 0 && y + World.height / 2 + width / 2 > 0)
			if(x + World.width / 2 - width / 2 < World.width && y + World.height / 2 - width / 2 < World.height)
				return true;
		return false;
	}
	
	public boolean inBounds(float x, float y)
	{
		if(x + World.width / 2 + width / 2 > 0 && y + World.height / 2 + width / 2 > 0)
			if(x + World.width / 2 - width / 2 < World.width && y + World.height / 2 - width / 2 < World.height)
				return true;
		return false;
	}
	
	public int getX()
	{
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	public float d2(float x, float y)
	{
		float dx = this.x - x;
		float dy = this.y - y;
		return dx * dx + dy * dy;
	}
	
	public void move(float dx, float dy)
	{
		x += dx;
		y += dy;
	}
	
	protected void flagRemove()
	{
		remove = true;
	}
	
	public boolean remove()
	{
		return remove;
	}
	
	public Cell getOccupied()
	{
		return occupied;
	}
	
	public int gridCoordX(int wx, int xOff)
	{
		int gx = (int)((float)(wx - xOff + BaseGame.width / 2) / Cell.width + World.bufferSize);
		return gx;
	}
	
	public int gridCoordY(int wy, int yOff)
	{
		int gy = (int)((float)(wy - yOff + BaseGame.height / 2) / Cell.width + World.bufferSize);
		return gy;
	}
}