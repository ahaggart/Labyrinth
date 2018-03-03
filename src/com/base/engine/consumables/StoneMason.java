package com.base.engine.consumables;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.attachments.Attachment;
import com.base.engine.tileEffects.WallBuilder;
import com.base.engine.world.Cell;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public class StoneMason implements Consumable, Attachment 
{
	private static Image icon;
	
	public StoneMason()
	{
		if(icon == null)
		{
			try {
				icon = new Image("img/hammer.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String toString()
	{
		return "Wall";
	}
	
	@Override
	public boolean act(Player p)
	{
		return p.pickUp(this);
	}

	@Override
	public void draw(Graphics b, int x, int y)
	{
		b.drawImage(icon,  x + Cell.width / 2 - icon.getWidth() / 2, y + Cell.width / 2 - icon.getHeight() / 2);
	}

	@Override
	public void use(World world, Player player, Cell occupied) 
	{
		occupied.addEffect(new WallBuilder(occupied));
	}

}
