package com.base.engine.world;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class Minotaur extends NPC
{
	private static SpriteSheet animation;
	
	private double frame = 0;
	private int runDirection = 0;

	
	public Minotaur(Cell start, World world)
	{
		super(start, world);
		if(animation == null)
		{
			try {
				Image src = new Image("img/monster1.png");
				animation = new SpriteSheet(src, 40, 35, 0);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public void update()
	{
		super.update();
		pathTo(0,0);
	}
	
	@Override
	public void draw(Graphics b)
	{
		int x = (int)this.x;
		int y = (int)this.y;
		if(inBounds())
		{
			b.drawImage(animation.getSubImage((int)frame, runDirection), x + World.width / 2 - 33 / 2, y + World.height / 2 - 30 / 2 - width / 2);
			if(!trapped)
				frame = (frame + 0.2 * (double)getSpeed() / baseSpeed) % 4;
		}
		if(d2(0,0) > 36 * Cell.width * Cell.width)//arrow indicator points towards GridObject from center of screen
		{
			double angle = Math.atan2(y,x);
			double a1 = Math.atan2(-2,-3) - Math.PI / 2;
			double a2 = Math.atan2(-2, 3) - Math.PI / 2;
			double a3 = Math.atan2(1,0) - Math.PI / 2;
			int x1 = (int)(Math.cos(angle) * 20) + World.width / 2;
			int y1 = (int)(Math.sin(angle) * 20) + World.height / 2;
			int[] px = {(int)(10 * Math.cos(angle + a1)) + x1,(int)(10 * Math.cos(angle + a2)) + x1,(int)(12 * Math.cos(angle + a3)) + x1};
			int[] py = {(int)(10 * Math.sin(angle + a1)) + y1,(int)(10 * Math.sin(angle + a2)) + y1,(int)(12 * Math.sin(angle + a3)) + y1};
			float[] p = {px[0],py[0],px[1],py[1],px[2],py[2]};
			b.setColor(Color.blue);
			b.fill(new Polygon(p));
			b.setColor(Color.green);
			b.draw(new Polygon(p));			
		}
	}
	
	public void respawn()
	{
		ArrayList<Cell> grid = world.getGrid();
		int rand = 1700 + (int)(300 * Math.random());
		while(grid.get(rand).getStatus() == 1 || grid.get(rand).d2() < 400 * Cell.width * Cell.width)
			rand = 1700 + (int)(300 * Math.random());
		this.x = grid.get(rand).getX();
		this.y = grid.get(rand).getY();
		clearBuffs();
		speed = baseSpeed;
		trapped = false;
	}
	
	@Override
	public void move(float dx, float dy)
	{
		super.move(dx, dy);
		trapped = false;
	}
	
	@Override
	public void moveTo(float dx, float dy)
	{
		super.moveTo(dx, dy);
		
		if(Math.abs(dx) > Math.abs(dy))
		{
			if(dx < 0)
				runDirection = 0;
			else
				runDirection = 1;
		}
		else if(Math.abs(dx) < Math.abs(dy))
		{
			if(dy > 0)
				runDirection = 2;
			else
				runDirection = 3;
		}
	}
}
