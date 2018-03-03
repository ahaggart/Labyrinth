package com.base.engine.particles;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.base.engine.core.BaseGame;
import com.base.engine.world.World;


public class GhostParticle implements Particle 
{
	private Image sprite;
	private int x, y;
	private float duration = 60.0f;
	private boolean remove;
	
	public GhostParticle(Image sprite, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		remove = false;
	}
	
	@Override
	public void draw(Graphics b) 
	{
		b.drawImage(sprite, x + BaseGame.width / 2 - sprite.getWidth() / 2, y + BaseGame.height / 2 + 4 - sprite.getHeight(), new Color(0.0f,0.6f,1, duration / 120.0f + 0.4f));
		duration -= 6.0f;
		if(duration < 0)
			remove = true;
	}

	@Override
	public boolean remove() 
	{
		return remove;
	}

	@Override
	public void move(int dx, int dy) 
	{
		x += dx;
		y += dy;
	}

}
