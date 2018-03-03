package com.base.engine.particles;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.base.engine.core.BaseGame;
import com.base.engine.world.World;


public class RedSmoke implements Particle 
{

	private int x, y;
	private static SpriteSheet animation;
	private float frame;
	private boolean remove = false;
	
	public RedSmoke(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		if(animation == null)
		{
			Image src;
			try {
				src = new Image("img/smoke.png");
				animation = new SpriteSheet(src, 47, 56, 0);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		frame = 0;
	}
	@Override
	public void draw(Graphics b) 
	{
		if(!(frame > 10))
		{
			Image img = animation.getSubImage((int)frame,0);
			b.drawImage(img, x+BaseGame.width / 2 - img.getWidth()*2, y+BaseGame.height / 2 - img.getHeight()*2, x+BaseGame.width / 2 + img.getWidth()*2, y+BaseGame.height / 2 + img.getHeight()*2, 0, 0, img.getWidth(), img.getHeight());
			if(frame <= 4)
				frame = frame + 0.5f;
			else
				frame += 0.2f;
		}
		else
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
