package com.base.engine.consumables;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.base.engine.attachments.Attachment;
import com.base.engine.particles.RedSmoke;
import com.base.engine.tileEffects.Ice;
import com.base.engine.world.Cell;
import com.base.engine.world.NPC;
import com.base.engine.world.Player;
import com.base.engine.world.World;
import com.base.engine.buffs.BlastBuff;
import com.base.engine.buffs.Slow;


public class Blast implements Consumable, Attachment
{
	private static Image img;
	
	public Blast()
	{
		if(img == null)
		{
			try {
				img = new Image("img/blast.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String toString()
	{
		return "blast";
	}
	
	@Override
	public void use(World world, Player player, Cell occupied) 
	{
		ArrayList<Cell> rad = new ArrayList<Cell>();
		for(int i = 0; i < world.getGrid().size(); i++)
			if(world.getGrid().get(i).d2() < 25 * Cell.width * Cell.width)
			{
				rad.add(world.getGrid().get(i));
				world.getGrid().get(i).clearEffects();
				world.getGrid().get(i).clearAttachment();
			}
		for(int i = 0; i < rad.size(); i++)
			if(rad.get(i).getStatus() == 1)
				rad.get(i).setStatus(0);
		for(int i = 0; i < rad.size(); i++)
		{
			rad.get(i).update();
			for(int j = 0; j < 4; j++)
			{
				if(rad.get(i).getNeighbor(j) != null && rad.get(i).getNeighbor(j).getStatus() == 1)
					rad.get(i).getNeighbor(j).update();
			}
		}
		for(int i = 0; i < world.getObjects().size(); i++)
			if(world.getObjects().get(i) instanceof NPC)
			{
				NPC npc = (NPC)world.getObjects().get(i);
				if(npc.d2(0,0) < 25 * Cell.width * Cell.width)
					npc.addBuff(new BlastBuff(1 + (int)((5 * Cell.width - (int)Math.sqrt(world.getObjects().get(i).d2(0,0)))/10)));
			}
		world.addParticle(new RedSmoke(0,0));	
	}
	
	public void draw(Graphics b, int x, int y)
	{
		b.drawImage(img,  x + Cell.width / 2 - img.getWidth() / 2, y + Cell.width / 2 - img.getHeight() / 2);
	}
	
	public boolean act(Player p)
	{
		return (p.pickUp(this));
	}

}
