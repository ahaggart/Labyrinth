package com.base.engine.consumables;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.attachments.Attachment;
import com.base.engine.buffs.Frost;
import com.base.engine.particles.FrostParticle;
import com.base.engine.tileEffects.Ice;
import com.base.engine.world.Cell;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public class Freeze implements Consumable, Attachment
{

	private static Image img;
	
	public Freeze()
	{
		if(img == null)
		{
			try {
				img = new Image("img/frost.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String toString()
	{
		return "freeze";
	}
	
	@Override
	public boolean act(Player p)
	{
		return (p.pickUp(this));
	}

	@Override
	public void draw(Graphics b, int x, int y) 
	{
		b.drawImage(img,  x + Cell.width / 2 - img.getWidth() / 2, y + Cell.width / 2 - img.getHeight() / 2);
	}

	@Override
	public void use(World world, Player player, Cell occupied) 
	{
		int rad = 400;
		for(int i = 0; i < world.getGrid().size(); i++)
			if(world.getGrid().get(i).d2() < rad * Cell.width * Cell.width)
				if(world.getGrid().get(i).getStatus() != 1)
				{
					float opacity = (float)world.getGrid().get(i).d2() / (rad * Cell.width * Cell.width);
					world.getGrid().get(i).addEffect(new Ice(1 - opacity));
				}
		rad = 81;
		for(int i = 0; i < world.getObjects().size(); i++)
			if(world.getObjects().get(i) instanceof NPC)
			{
				NPC npc = (NPC)world.getObjects().get(i);
				if(npc.d2(0,0) < rad * Cell.width * Cell.width)
				{
					float duration = (1.0f - (float)Math.sqrt((float)npc.d2(0,0) / (rad * Cell.width * Cell.width)));
					npc.addBuff(new Frost((int)(240 * duration)));
				}
			}
		for(int i = 0; i < 30; i++)
			world.addParticle(new FrostParticle(0,0));

	}
	
}
