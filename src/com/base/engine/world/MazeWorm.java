package com.base.engine.world;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;

public class MazeWorm extends NPC 
{
	private static Image head, body, body2, tail;
	
	private double frame = 0;
	private int runDirection = 0;
	private float headAngle;
	private int distance = 0;
	private ArrayList<Node> trail;
	private ArrayList<Float> angle;
	private int[] bodyProgress;
	private boolean[] bodyType;
	private boolean move;

	
	public MazeWorm(Cell start, World world)
	{
		super(start, world);
		speed = 2.0f;
		if(head == null)
		{
			try {
				head = new Image("img/worm_head_3.png");
				body = new Image("img/worm_body.png");
				body2 = new Image("img/worm_body_3.png");
				tail = new Image("img/worm_tail.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		trail = new ArrayList<Node>();
		trail.add(new Node(start));
		angle = new ArrayList<Float>();
		angle.add((float)0);
		bodyProgress = new int[30];
		bodyType = new boolean[30];
		for(int i = 0; i < bodyProgress.length; i++)
		{
			bodyProgress[i] = 1 + i * (int)(16 / getSpeed());
			bodyType[i] = Math.random() > 0.2;
		}

	}
	
	@Override
	public void update()
	{
		super.update();
		move = false;
		pathTo(0,0);

		if(move)
		{
			trail.add(0,new Node(x,y));
			angle.add(0,(float)(runDirection * 90));
		}
		if(trail.size() > bodyProgress[bodyProgress.length - 1] + 1)
		{
			int index = trail.size() - 1;
			trail.remove(index);
			angle.remove(index);
		}
	}
	
	@Override
	public void draw(Graphics b)
	{
		int x = (int)this.x;
		int y = (int)this.y;

		Image head1 = head.copy();
		head1.rotate(headAngle);
		if(!trapped)
			frame = (frame + 0.2 * (double)getSpeed() / baseSpeed) % 4;
		b.setColor(Color.red);
		int length = bodyProgress.length - 1;
		int spacing = (int)(4 / getSpeed());
		for(int i = angle.size() - spacing - 1; i > spacing - 1; i--)
		{
			angle.set(i, 90 +(float)Math.toDegrees(Math.atan2(trail.get(i-spacing).getY() - trail.get(i + spacing).getY(), trail.get(i-spacing).getX() - trail.get(i + spacing).getX())));
		}
		if(bodyProgress[length] < trail.size())
			if(inBounds(trail.get(bodyProgress[length]).getX(), trail.get(bodyProgress[length]).getY()))
			{
				Image tail1 = tail.copy();
				tail1.rotate((float)(angle.get(bodyProgress[length])));
				b.drawImage(tail1,(int)trail.get(bodyProgress[length]).getX() + World.width / 2 - tail1.getWidth() / 2, (int)trail.get(bodyProgress[length]).getY() + World.height / 2 - tail1.getHeight() / 2);
			}
		for(int i = length - 1; i > 0; i--)
			if(bodyProgress[i] < trail.size())
				if(inBounds(trail.get(bodyProgress[i]).getX(), trail.get(bodyProgress[i]).getY()))
				{
					Image body1;
					if(bodyType[i])
						body1 = body.copy();
					else
						body1 = body2.copy();
					body1.rotate((float)(angle.get(bodyProgress[i])));
					b.drawImage(body1,(int)trail.get(bodyProgress[i]).getX() + World.width / 2 - body1.getWidth() / 2, (int)trail.get(bodyProgress[i]).getY() + World.height / 2 - body1.getHeight() / 2);	
				}
		if(inBounds())
			b.drawImage(head1, x + World.width / 2 - head1.getWidth() / 2, y + World.height / 2 - head1.getHeight() / 2);
	
		
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
	
	public boolean followPath(ArrayList<Node> open, ArrayList<Node> closed)
	{	
		ArrayList<Cell> path = new ArrayList<Cell>();
		int index = closed.size() - 1;
		Node step = closed.get(index);
		path.add(0,closed.get(index).getRef());
		while(step.getParent() != null)//a step with a null parent is the root step
		{
			step = step.getParent();
			path.add(0, step.getRef());
		}
		if(path.size() > 1)
		{
			Cell hold = path.get(1);
			if(hold != null)
			{
				int dx = hold.getX() + Cell.width / 2 - (int)x;
				int dy = hold.getY() + Cell.width / 2 - (int)y;
				moveTo(dx,dy);
			}
		}
		else
			return false;
//		for(Cell c : path)
//			c.select();
		return true;
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
		angle.clear();
		trail.clear();
	}
	
	@Override
	public void move(float dx, float dy)
	{
		super.move(dx, dy);
		
		for(int i = 0; i < trail.size(); i++)
		{
			trail.get(i).setX(trail.get(i).getX() + dx);
			trail.get(i).setY(trail.get(i).getY() + dy);
		}

		trapped = false;		
	}
	
	public void movei(float dx, float dy)
	{
		super.move(dx, dy);
		distance += 1;
//		headAngle = (float)Math.toDegrees(Math.atan2(dy, dx)) + 90;
		headAngle = runDirection * 90;
	}
	
	@Override
	public void moveTo(float dx, float dy)
	{
		float ox = x;
		float oy = y;

		if(dx > 0)
		{
			if(dx > getSpeed())
				movei(getSpeed(), 0);
			else
				movei(dx,0);
		}
		else
		{
			if(dx < -getSpeed())
				movei(-getSpeed(), 0);
			else
				movei(dx,0);
		}
		if(dy > 0)
		{
			if(dy > getSpeed())
				movei(0, getSpeed());
			else
				movei(0,dy);
		}
		else
		{
			if(dy < -getSpeed())
				movei(0,-getSpeed());
			else
				movei(0,dy);
		}		
		if(ox != x || oy != y)
		{
			move = true;
		}
		
		runDirection = getDirection(dx,dy);
		
//		if(oldDirection != runDirection)
//		{
//			trail.add(1, new Node(occupied));
//			for(int i = 0; i < bodyProgress.length; i++)
//			{
//				if(bodyProgress[i] < trail.size() - 1)
//					bodyProgress[i]++;
//			}
//		}
	}
	
	public int getDirection(float dx, float dy)
	{
		if(Math.abs(dx) > Math.abs(dy))
		{
			if(dx < 0)
				return 3;
			else
				return 1;
		}
		else if(Math.abs(dx) < Math.abs(dy))
		{
			if(dy > 0)
				return 2;
			else
				return 0;
		}
		return runDirection;
	}
}
