package com.base.engine.buffs;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.world.GridObject;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;

public class Frost extends Buff 
{
	private static Image icon;
	
	public Frost(int duration)
	{
		super(duration, 3, 10);
		if(icon == null)
		{
			try {
				icon = new Image("img/ice_block.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		setIcon(icon);
	}

	@Override
	public void act(Player p) 
	{

	}

	@Override
	public void act(GridObject o)
	{
		if(o instanceof NPC)
		{
			NPC n = (NPC)o;
			float old = n.getRawSpeed();
			n.setSpeed(0);//immobilize this GridObject
			dif = old;
		}
		this.o = o;
	}

	@Override
	public void update() 
	{
		if(duration > 0)
		{
			if(o instanceof NPC)
			{
				NPC n = (NPC)o;
				float old = n.getRawSpeed();
				n.setSpeed(0);
				dif += old;
			}
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
		return 0;
	}

}
