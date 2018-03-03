package com.base.engine.world;

import java.lang.Thread.State;
import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.base.engine.buffs.Buff;

public abstract class NPC extends GridObject 
{
	public static final float maxSpeed = 4;
	public static final float baseSpeed = 1.6f;
	protected float speed = baseSpeed;
	protected boolean trapped = false;
	
	private ArrayList<Buff> buffs;

	public class Node
	{
		float nx, ny, h, g;
		Node parent;
		Cell ref;
		public Node(Cell ref)
		{
			nx = ref.getX() + Cell.width / 2;
			ny = ref.getY() + Cell.width / 2;
			h = 0;
			g = 0;
			this.ref = ref;
			this.parent = null;
		}	
		public Node(float x, float y)
		{
			nx = x;
			ny = y;
			h = 0;
			g = 0;
		}
		public void setG()
		{
			this.g = g;
		}
		public float getG()
		{
			return g;
		}
		public void setH()
		{
			this.h = h;
		}
		public void setH(Node t)
		{
			h = 0;
			h += 10 * Math.abs(t.getX() - nx);
			h += 10 * Math.abs(t.getY() - ny);
		}
		public float getH()
		{
			return h;
		}
		public float getF()
		{
			return h + g;
		}
		public boolean equals(Object o)
		{
			Node n = (Node)o;
			return (n.getX() == nx && n.getY() == ny);
		}
		public void setParent(Node parent)
		{
			this.parent = parent;
			this.g = parent.getG() + 10;
		}
		public Node getParent()
		{
			return parent;
		}
		public Cell getRef()
		{
			return ref;
		}
		public void setX(float x)
		{
			this.nx = x;
		}
		public void setY(float y)
		{
			this.ny = y;
		}
		public float getX()
		{
			return nx;
		}
		public float getY()
		{
			return ny;
		}
	}
	
	public NPC(float x, float y, World world)
	{
		super(x,y,world);
		
		buffs = new ArrayList<Buff>();
	}
	
	public NPC(Cell start, World world)
	{
		this(start.getX(), start.getY(), world);
	}
	
	public void update()
	{
		for(int i = 0; i < buffs.size(); i++)
			buffs.get(i).update();
		for(int i = buffs.size() - 1; i >= 0; i--)
			if(buffs.get(i).remove())
				buffs.remove(i);
	}
	
	public void respawn()
	{
		
	}
	
	public boolean pathTo(int tx, int ty)
	{
		if(trapped)
			return false;
		//find current tile
		ArrayList<Cell> grid = world.getGrid();
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
		if(home == null)//no containing tile found, abort	
		{
			respawn();
			return false;
		}
		occupied = home;
		Cell target = null;
		gx = gridCoordX((int)tx,offX);
		gy = gridCoordY((int)ty,offY);
		prox = world.getSubGrid(gx, gy, 2, 2);
		for(int i = 0; i < prox.size(); i++)
			if(prox.get(i).contains((int)tx,(int)ty))
				target = prox.get(i);
		if(target == null)//no containing tile found, abort
			return false;
			
		ArrayList<Node> closed = new ArrayList<Node>();
		ArrayList<Node> open = new ArrayList<Node>();
		if(eval(new Node(home), open, closed, new Node(target)))
		{
			if(!followPath(open, closed))
				moveTo(tx - x, ty - y);
			return true;
		}
		else
		{
			trapped = true;
			return false;
		}
	}
	
	public boolean eval(Node c, ArrayList<Node> open, ArrayList<Node> closed, Node target)
	{
		closed.add(c);
		if(c.equals(target))
			return true;
		for(int i = 0; i < 4; i++)//update open list
		{
			Cell n = c.getRef().getNeighbor(i);
			if(n != null)
				if(n.getStatus() != 1)
				{
					Node node = new Node(n);
					if(!open.contains(node))
					{
						if(!closed.contains(node))
						{
							node.setH(target);
							node.setParent(c);
							open.add(node);
						}
					}
					else//the list already has the node, find it
					{
						Node f = null;
						for(int j = 0; j < open.size(); j++)
							if(open.get(j).equals(node))
							{
								f = open.get(j);
								break;
							}
						if(f.getG() > c.getG() + 10)
							f.setParent(c);
					}
				}
		}
		if(open.size() < 1)
			return false;
		float lowest = open.get(0).getF();
		int index = 0;
		for(int i = 1; i < open.size(); i++)
			if(open.get(i).getF() < lowest)
			{
				lowest = open.get(i).getF();
				index = i;
			}
		Node next = open.get(index);
		open.remove(index);
		return eval(next, open, closed, target);			
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

	public void addBuff(Buff buff)
	{
		if(!buffs.contains(buff))
		{
			buff.act(this);
			buffs.add(buff);
		}
		else
		{
			int index = buffs.indexOf(buff);
			Buff comp = buffs.get(index);
			if(comp.getPower() < buff.getPower())
			{
				comp.expire();
				buffs.remove(index);
				buffs.add(buff);
				buff.act(this);
			}
			else if(comp.getPower() == buff.getPower())
			{
				if(comp.getDuration() < buff.getDuration())
				{
					comp.expire();
					buffs.remove(index);
					buffs.add(buff);
					buff.act(this);
				}
			}
		}
	}

	public void clearBuffs()
	{
		buffs.clear();
		speed = baseSpeed;
	}
	
	public void moveTo(float dx, float dy)
	{
		if(dx > 0)
		{
			if(dx > getSpeed())
				move(getSpeed(), 0);
			else
				move(dx,0);
		}
		else
		{
			if(dx < -getSpeed())
				move(-getSpeed(), 0);
			else
				move(dx,0);
		}
		if(dy > 0)
		{
			if(dy > getSpeed())
				move(0, getSpeed());
			else
				move(0,dy);
		}
		else
		{
			if(dy < -getSpeed())
				move(0,-getSpeed());
			else
				move(0,dy);
		}
	}
	
	public float getSpeed()
	{
		return Math.max(0, Math.min(maxSpeed, speed));
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public float getRawSpeed()
	{
		return speed;
	}
	
	public void increaseSpeed(float inc)
	{
		speed += inc;
	}
	
	public void decreaseSpeed(float dec)
	{
		speed -= dec;
	}

}
