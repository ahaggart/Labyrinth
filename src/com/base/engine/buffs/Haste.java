package com.base.engine.buffs;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.particles.GhostParticle;
import com.base.engine.world.GridObject;
import com.base.engine.world.Player;


public class Haste extends Buff
{
	private static Image icon;
	
	public Haste(int duration, float power)
	{
		super(duration, 0, power);
		if(icon == null)
		{
			try {
				icon = new Image("img/haste.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		super.setIcon(icon);
	}
	
	public void act(Player p)
	{
		p.increaseSpeed(power);
		this.p = p;
	}
	
	public void act(GridObject o)
	{
		//this buff does not act on GridObjects
	}

	
	public void update()
	{
		if(duration < 1)
			expire();
		duration--;
		if(duration % 5 == 0)
			p.addParticle(new GhostParticle(p.getSprite(),0,0));
	}
	
	public void expire()
	{
		p.increaseSpeed(-power);
		remove = true;
	}
	
	public int targetType()
	{
		return 0;
	}
	
}
