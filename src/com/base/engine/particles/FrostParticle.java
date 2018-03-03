package com.base.engine.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.base.engine.core.BaseGame;

public class FrostParticle implements Particle
{
	private static Image frost;
	private float x, y;
	private float vx, vy;
	private float spin;
	private float angle;
	private int duration;
	
	public FrostParticle(float x, float y)
	{
		this.x = x;
		this.y = y;
		duration = 15;
		angle = 0;
		if(frost == null)
		{
			try {
				frost = new Image("img/frost.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		spin = (float)(Math.random() - 0.5) * 15;
		float dir = (int)(Math.random()*361);
		float speed = (float)Math.random() * 10 + 5;
		vx = speed * (float)Math.cos(dir);
		vy = speed * (float)Math.sin(dir);
	}
	
	@Override
	public void draw(Graphics b)
	{
		Image img = frost.copy();
		img.rotate(angle + spin);
		b.drawImage(img, x + BaseGame.width / 2 - frost.getWidth() / 2, y + BaseGame.height / 2 + 4 - frost.getHeight());
		x += vx;
		y += vy;
		angle += spin;
		duration--;
	}

	@Override
	public boolean remove() 
	{
		return (duration < 1);
	}

	@Override
	public void move(int dx, int dy) 
	{
		x += dx;
		y += dy;
	}

}
