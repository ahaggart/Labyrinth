package com.base.engine.world;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Projectile extends GridObject
{
	private float vx, vy, angle;
	private static Image arrow;
	
	public Projectile(float x, float y, World world, float vx, float vy)
	{
		super(x,y, world);
		this.vx = vx;
		this.vy = vy;
		angle = (float)Math.toDegrees(Math.atan2(vy, vx)) + 90;
		if(arrow == null)
		{
			try {
				arrow = new Image("img/arrow.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	//	arrow.setCenterOfRotation(7, 2);
	}
	
	public void update()
	{
		updateOccupied();
		if(occupied == null)
			remove = true;
		move(vx,vy);
	}
	
	public void draw(Graphics b)
	{
		Image img = arrow.copy();
		img.rotate(angle);
		b.drawImage(img, x + World.width / 2 - 7, y + World.height / 2 - 16);
		b.setColor(Color.red);
		b.drawRect(x + World.width / 2 - 2, y + World.height / 2 - 2, 4, 4);
	}
	
	public void updateOccupied()
	{
		Cell[][] grid2 = world.getGrid2();
		int offX = grid2[0][0].getX();
		int offY = grid2[0][0].getY();
		int gx = gridCoordX((int)x,offX);
		int gy = gridCoordY((int)y,offY);
		Cell home = null;
		ArrayList<Cell> prox = world.getSubGrid(gx, gy, 2, 2);
		for(int i = 0; i < prox.size(); i++)
			if(prox.get(i).contains((int)x,(int)y))
				home = prox.get(i);
		occupied = home;
	}
}
