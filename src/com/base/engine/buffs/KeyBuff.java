package com.base.engine.buffs;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.world.GridObject;
import com.base.engine.world.Player;

public class KeyBuff extends Buff
{
	private static Image icon;
	
	public KeyBuff()
	{
		super(1,-1,0);
		if(icon == null)
		{
			try {
				icon = new Image("img/key.png");
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

	}
	
	@Override
	public void update()
	{
		
	}

	@Override
	public void expire() 
	{
		remove = true;
	}

	@Override
	public int targetType()
	{
		return 0;
	}

}
