package com.base.engine.buffs;

import com.base.engine.world.GridObject;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;

public class Slow extends Buff 
{
	public Slow(int duration, float power)
	{
		super(duration, 4, power);
	}
	
	@Override
	public void act(Player p)
	{
		p.decreaseSpeed(power);
		this.p = p;
	}

	@Override
	public void act(GridObject o) 
	{
		if(o instanceof NPC)
		{
			NPC n = (NPC)o;
			n.decreaseSpeed(power);
		}
		this.o = o;
	}

	@Override
	public void expire()
	{
		if(o != null)
			if(o instanceof NPC)
			{
				NPC n = (NPC)o;
				n.decreaseSpeed(-power);
			}
		if(p != null)
			p.decreaseSpeed(-power);
		remove = true;
	}

	@Override
	public int targetType() 
	{
		return 2;
	}

}
