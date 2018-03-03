package com.base.engine.attachments;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.buffs.Buff;
import com.base.engine.buffs.Haste;
import com.base.engine.buffs.KeyBuff;
import com.base.engine.world.Cell;
import com.base.engine.world.Player;

public class Chest implements Attachment 
{
	private static Image chest;
	
	public Chest()
	{
		if(chest == null)
		{
			try {
				chest = new Image("img/chest.png");
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
			{
				p.addCoins(50);
				buffs.get(i).expire();
				p.addBuff(new Haste(2 * 60,2.0f));
				return true;
			}
		return false;
	}

	@Override
	public void draw(Graphics b, int x, int y) 
	{
		b.drawImage(chest, x + Cell.width / 2 - 8, y + Cell.width / 2 - 7, x + Cell.width / 2 + 8, y + Cell.width / 2 + 7, 0, 0, 32, 28);
	}

}
