package com.base.engine.attachments;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.buffs.Buff;
import com.base.engine.buffs.KeyBuff;
import com.base.engine.world.Cell;
import com.base.engine.world.Player;

public class Key implements Attachment 
{
	private static Image icon;
	
	public Key()
	{
		if(icon == null)
		{
			try {
				icon = new Image("img/key.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean act(Player p) 
	{
		
		ArrayList<Buff> buffs = p.getBuffs();
		for(int i = 0; i < buffs.size(); i++)
			if(buffs.get(i).getClass() == KeyBuff.class)
				return false;	
		p.addBuff(new KeyBuff());
		return true;
	}

	@Override
	public void draw(Graphics b, int x, int y) 
	{
		b.drawImage(icon, x + Cell.width / 2 - 7, y + Cell.width - 16);
	}

}
