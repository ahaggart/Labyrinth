package com.base.engine.attachments;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.base.engine.world.Cell;
import com.base.engine.world.Player;
import com.base.engine.world.World;

import com.base.engine.buffs.Haste;


public class Coin implements Attachment 
{
	private static SpriteSheet coin;
	private float frame = 0;
	private Color color = Color.white;
	private int value = 1;
	
	private World world;
	
	public Coin()
	{	
		if(coin == null)
		{
			try {
				Image src = new Image("img/coin.png");
				coin = new SpriteSheet(src, 27, 28, 2);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		frame = (float)(8 * Math.random());
		if(frame > 7.8)
		{
			value = 10;
			color = Color.blue;
		}
		else if(frame > 7)
		{
			value = 5;
			color = Color.red;
		}
	}
	
	public String toString()
	{
		return "coin";
	}
	
	public boolean act(Player p)
	{
		p.addCoins(value);
		p.addBuff(new Haste(value * 60,0.5f));
		return true;
	}
	
	public void draw(Graphics b, int x, int y)
	{
		b.drawImage(coin.getSubImage((int)frame, 0), x, y, x + Cell.width, y + Cell.width,0,0,27,28, color);
		frame = (frame + 0.3f) % 8;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
}
