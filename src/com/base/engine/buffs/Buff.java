package com.base.engine.buffs;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import com.base.engine.world.GridObject;
import com.base.engine.world.Player;



public abstract class Buff
{
	/*	Target Types:
	 * 	0 = Player
	 * 	1 = GridObject
	 * 	2 = both
	 * 
	 * 	Buff Types:
	 * -1 = Key (dummy buff for Chest)
	 * 	0 = Haste (speed)
	 * 	1 = BlastBuff (knockback)
	 * 	2 = CastTime (spell/item delay)
	 * 	3 = Frost (immobilize)
	 * 	4 = Slow
	 */
	
	protected int duration;
	private int totalDuration;
	protected int type;
	protected float dif;
	protected float power;
	protected boolean remove = false;
	protected Player p;
	protected GridObject o;
	private Image icon;
	private static Color overlay;
	private static Image unknown;
	
	public Buff(int duration, int type, float power)
	{
		this.duration = duration;
		totalDuration = duration;
		this.type = type;
		this.power = power;
		if(overlay == null)
			overlay = new Color(0,0,1,0.8f);
		if(unknown == null)
			try {
				unknown = new Image("img/default.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public boolean equals(Object o)
	{
		Buff b = (Buff)o;
		return b.getType() == type;
	}
	
	public abstract void act(Player p);
	public abstract void act(GridObject o);
	public abstract void expire();
	public abstract int targetType();
	
	public void update()
	{
		if(duration > 0)
			duration--;
		else
			expire();
	}
	
	public boolean remove() 
	{
		return remove;
	}
	
	public void draw(Graphics b, int x, int y)
	{
		Image icon;
		if(this.icon == null)
			icon = unknown;
		else
			icon = this.icon;
		float ratio = 1;
		if(icon.getWidth() > 32 || icon.getHeight() > 32)
		{
			if(icon.getHeight() > icon.getWidth())
				ratio = 32.0f / icon.getHeight();
			else
				ratio = 32.0f / icon.getWidth();
		}
		b.drawImage(icon, x + 16 -  icon.getWidth() * ratio / 2, y + 16 -  icon.getHeight() * ratio / 2, x + 16 + icon.getWidth() * ratio / 2, y + 16 +  icon.getHeight() * ratio / 2, 0, 0, icon.getWidth(), icon.getHeight());
		b.setColor(overlay);
		ratio = 1.0f - (float)duration / totalDuration;
		int points = (int)(ratio * 180);
		float[] polygon = new float[points * 2 + 2];
		polygon[0] = x + 16;
		polygon[1] = y + 16;
		for(int i = 0; i < points; i++)
		{
			polygon[2 + i*2] = x + 16 + (float)(22 * Math.cos(Math.toRadians(270 - i*2)));
			polygon[3 + i*2] = y + 16 + (float)(22 * Math.sin(Math.toRadians(270 - i*2)));
		}
		b.fill(new Polygon(polygon));
	}
	
	protected void setIcon(Image icon)
	{
		this.icon = icon;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public float getPower()
	{
		return power;
	}
}
