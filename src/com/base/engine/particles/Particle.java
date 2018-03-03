package com.base.engine.particles;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public interface Particle 
{		
	public void draw(Graphics b);	
	public boolean remove();	
	public void move(int dx, int dy);
}
