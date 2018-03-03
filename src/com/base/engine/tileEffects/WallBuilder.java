package com.base.engine.tileEffects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.core.BaseGame;
import com.base.engine.world.Cell;
import com.base.engine.world.GridObject;
import com.base.engine.world.Player;
import com.base.engine.world.World;

public class WallBuilder implements TileEffect
{
	private static Image img;
	private Cell home;
	private boolean clear, remove;
	
	public WallBuilder(Cell home)
	{
		this.home = home;
		if(img == null)
		{
			try {
				Image src = new Image("img/stone_brick.png");
				img = src.getSubImage(162, 54, 16, 16);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		clear = false;
		remove = false;
	}
	@Override
	public void act(Player p) 
	{
		clear = false;
	}

	@Override
	public void act(GridObject o) 
	{

	}

	@Override
	public void draw(Graphics b, int x, int y) 
	{
		b.drawImage(img, x+BaseGame.width / 2, y+BaseGame.height / 2);
	}

	@Override
	public void update() 
	{
		if(clear)
		{
			home.setStatus(1, true);
			home.update();
			home.updateNeighbors();
			remove = true;
		}
		else
			clear = true;
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
		return 0;
	}

}
