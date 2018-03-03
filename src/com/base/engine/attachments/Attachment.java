package com.base.engine.attachments;
import org.newdawn.slick.Graphics;

import com.base.engine.world.Player;

public interface Attachment
{	
	public abstract boolean act(Player p);	
	public abstract void draw(Graphics b, int x, int y);
}
