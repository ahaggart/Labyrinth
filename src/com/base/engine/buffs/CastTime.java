package com.base.engine.buffs;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.consumables.Consumable;
import com.base.engine.consumables.Teleport;
import com.base.engine.world.GridObject;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public class CastTime extends Buff 
{
	private float dif;
	private Consumable c;
	
	public CastTime(Consumable c, int duration, Image icon)
	{
		super(duration, 2, -1);
		this.c = c;
		super.setIcon(icon);
	}
	
	@Override
	public void act(Player p)
	{
		float old = p.getRawSpeed();
		p.setSpeed(0);
		dif = old;
		this.p = p;
	}

	@Override
	public void act(GridObject o) 
	{
		//this buff does not act of GridObjects
	}

	@Override
	public void update() 
	{
		if(duration > 0)
			duration--;
		else
			expire();
	}

	@Override
	public void expire() 
	{
		remove = true;
		p.setSpeed(dif + p.getRawSpeed());
		c.use(null,null,null);
	}

	@Override
	public int targetType()
	{
		return 0;
	}

}
