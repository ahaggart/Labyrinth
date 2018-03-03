package com.base.engine.consumables;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.base.engine.attachments.Attachment;
import com.base.engine.particles.PurpleSmoke;
import com.base.engine.world.Cell;
import com.base.engine.world.GridObject;
import com.base.engine.world.Player;
import com.base.engine.world.World;
import com.base.engine.buffs.CastTime;

public class Teleport implements Consumable, Attachment
{
	private int stage;
	private static Image img;
	int dir = 0;
	private World w;
	private Player p;
	
	public Teleport(int stage)
	{
		this.stage = stage;
		if(img == null)
		{
			try {
				img = new Image("img/teleport.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Teleport()
	{
		this(0);
	}
	
	public String toString()
	{
		return "Teleport";
	}
	
	@Override
	public void use(World world, Player player, Cell occupied) 
	{
		if(stage == 0)
		{
			if(world.getPressed().contains(Input.KEY_D))
				dir = 1;
			else if(world.getPressed().contains(Input.KEY_S))
				dir = 2;
			else if(world.getPressed().contains(Input.KEY_A))
				dir = 3;
			player.addBuff(new CastTime(this,19,img));
			world.addParticle(new PurpleSmoke(0,0,true));
			this.w = world;
			this.p = player;
			stage = 1;
		}
		else if(stage == 1)
		{
			int count = 1;
			Cell next = p.getOccupied().getNeighbor(dir);
		//	GridObject dummy = new GridObject(next,w.getGrid());
			while(count > 0 || next.getStatus() == 1/* || !dummy.pathTo(p.getX(), p.getY())*/)
			{
				count--;
				next = next.getNeighbor(dir);
			//	dummy = new GridObject(next,w.getGrid());
			}
			w.moveGrid(-next.getX() - Cell.width / 2, -next.getY() - Cell.width / 2);
			w.addParticle(new PurpleSmoke(0,0,false));
		}
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
