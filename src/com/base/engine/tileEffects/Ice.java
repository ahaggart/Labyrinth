package com.base.engine.tileEffects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.buffs.Frost;
import com.base.engine.buffs.Slow;
import com.base.engine.core.BaseGame;
import com.base.engine.world.GridObject;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public class Ice implements TileEffect 
{
	private static Image ice;
	private int duration;
	private boolean remove;
	private float opacity;
	
	public Ice(float opacity)
	{
		duration = (int)(240 * opacity);
		if(ice == null)
		{
			try {
				ice = new Image("img/ice_block.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		this.opacity = opacity;
	}
	
	@Override
	public void act(Player p) 
	{

	}

	@Override
	public void act(GridObject o)
	{
		if(o.getClass() == NPC.class)
		{
			NPC npc = (NPC)o;
			npc.addBuff(new Slow(1,opacity));
		}
	}

	@Override
	public void draw(Graphics b, int x, int y) 
	{
		b.drawImage(ice, x+ BaseGame.width / 2, y + BaseGame.height / 2, new Color(1,1,1,(duration / 400.0f) + 0.25f));
	}

	@Override
	public void update() 
	{
		if(duration > 0)
			duration--;
		else
			remove = true;
	}

	@Override
	public int getType() 
	{
		return 0;
	}

	@Override
	public boolean remove() 
	{
		return remove;
	}
	
	@Override
	public int getDuration()
	{
		return duration;
	}

}
