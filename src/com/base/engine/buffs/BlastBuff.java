package com.base.engine.buffs;
import org.newdawn.slick.Graphics;

import com.base.engine.world.GridObject;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;


public class BlastBuff extends Buff 
{
	private double x, y;
	private double nx, ny;
	
	public BlastBuff(int power)
	{
		super(15,1,power);
	}
	
	@Override
	public void act(Player p) 
	{
		//this buff does not act on players
	}
	
	public void act(GridObject o)
	{
		if(o instanceof NPC)
		{
			NPC n = (NPC)o;
			float old = n.getRawSpeed();
			n.setSpeed(0);//immobilize this GridObject
			dif = old;
			this.o = o;
			this.x = o.getX();
			this.y = o.getY();
			nx = x;
			ny = y;
			double length = Math.sqrt(x*x+y*y);
			if(length > 0)
			{
				this.x /= length;
				this.y /= length;
			}
			else
			{
				double rand = Math.random() * 2 * Math.PI;
				this.x = Math.cos(rand);
				this.y = Math.sin(rand);
			}
		}
	}

	@Override
	public void update() 
	{
		if(o.getOccupied().getStatus() == 1 || duration > 0)
		{
			if(o instanceof NPC)
			{
				NPC n = (NPC)o;
				nx += x * power;
				ny += y * power;
				float old = n.getRawSpeed();
				n.setSpeed(0);
				dif += old;
				o.move((float)(nx - o.getX()),(float)(ny - o.getY()));
			}
			if(duration > 0)
				duration--;
		}
		else
			expire();
	}

	@Override
	public void expire() 
	{
		if(o instanceof NPC)
		{
			NPC n = (NPC)o;
			n.setSpeed(dif + n.getRawSpeed());
		}
		remove = true;
	}

	@Override
	public boolean remove() 
	{
		return remove;
	}

	@Override
	public int targetType() 
	{
		return 1;
	}

}
