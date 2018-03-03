package com.base.engine.world;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.base.engine.attachments.Attachment;
import com.base.engine.attachments.Coin;
import com.base.engine.consumables.Consumable;
import com.base.engine.consumables.Teleport;
import com.base.engine.core.BaseGame;
import com.base.engine.particles.Particle;
import com.base.engine.buffs.Buff;


public class Player
{
	private static final float baseSpeed = 1.5f;
	private static final float maxSpeed = 4;
	
	private static SpriteSheet animation;
	private float frame;
	private int dir = 2;
	private boolean move;
	
	private float x, y;
	
	private float speed;
	private int distance;
	private int coins;
	
	private Consumable item;
	
	private ArrayList<Buff> buffs;
	private World world;
	private ArrayList<Particle> particles;
	private Cell occupied;
	
	private Coin coin;
	
	public Player(World world)
	{
		this.world = world;
		speed = 1.5f;
		coins = 0;
		distance = 0;
		this.x = 0;
		this.y = 0;
		
		coin = new Coin();
		coin.setColor(Color.white);
		
		buffs = new ArrayList<Buff>();
		particles = new ArrayList<Particle>();
		
		item = null;
		
		if(animation == null)
		{
			Image src;
			try {
				src = new Image("img/player.png");
				animation = new SpriteSheet(src,32,32,0);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		frame = 0.0f;
	}
	
	public void update(Cell occupied)
	{
		this.occupied = occupied;
		for(int i = 0; i < buffs.size(); i++)
			buffs.get(i).update();
		for(int i = buffs.size() - 1; i >= 0; i--)
			if(buffs.get(i).remove())
				buffs.remove(i);
	}
	
	public void draw(Graphics b)
	{
		b.setColor(Color.red);
		b.fillOval(BaseGame.width / 2 - 4, BaseGame.height / 2 - 4, 8, 8);
		Image img = getSprite();
		b.drawImage(img, 0 + BaseGame.width / 2 - img.getWidth() / 2, 0 + BaseGame.height / 2 + 4 - img.getHeight());
		if(move)
		{
			frame += 0.375f * (float)getSpeed() / (float)baseSpeed;
			frame = frame % 10;
			move = false;
		}
		
		//HUD
		b.setColor(Color.gray);
		b.fillRect(900,16, 100, 60);
		b.setColor(Color.red);
		b.drawRect(900,16, 100, 60);
		b.setColor(Color.black);
		b.drawString("= " + coins, 930, 20);
		coin.draw(b, 910, 20);
		b.drawString("Speed: " + speed, 910, 36);
		b.drawString("Item: " + item, 910, 52);
		
		for(int i = 0; i < buffs.size(); i++)
		{
			b.setColor(Color.gray);
			b.fillOval(908, 78 + i * 46, 46, 46);
			buffs.get(i).draw(b, 915, 85 + i * 46);
			b.setColor(Color.black);
			b.drawOval(908, 78 + i * 46, 46, 46);
		}

	}
	
	public Image getSprite()
	{
		return animation.getSubImage((int)frame,dir);
	}
	
	public void addBuff(Buff buff)
	{
		if(!buffs.contains(buff))
		{
			buff.act(this);
			buffs.add(buff);
		}
		else
		{
			int index = buffs.indexOf(buff);
			Buff comp = buffs.get(index);
			if(comp.getPower() < buff.getPower())
			{
				comp.expire();
				buffs.remove(index);
				buffs.add(buff);
				buff.act(this);
			}
			else if(comp.getPower() == buff.getPower())
			{
				if(comp.getDuration() < buff.getDuration())
				{
					comp.expire();
					buffs.remove(index);
					buffs.add(buff);
					buff.act(this);
				}
			}
		}
	}
	
	public void addParticle(Particle p)
	{
		particles.add(p);
	}
	
	public ArrayList<Particle> getParticles()
	{
		return particles;
	}
	
	public void reset()
	{
		speed = baseSpeed;
		coins = 0;
		distance = 0;
		buffs.clear();
		particles.clear();
		item = null;
	}
	
	public float getSpeed()
	{
		return Math.max(0, Math.min(speed, maxSpeed));
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public float getRawSpeed()
	{
		return speed;
	}
	
	public void increaseSpeed(float inc)
	{
		speed += inc;
	}
	
	public void decreaseSpeed(float dec)
	{
		speed -= dec;
	}
	
	public void setDirection(int dir)
	{
		this.dir = dir;
		move = true;
	}
	
	public void addCoins(int value)
	{
		coins += value;
//		if(value > 9 && item == null)
//			item = new Teleport(0);
			
	}
	
	public int getCoins()
	{
		return coins;
	}
	
	public void addDistance(int d)
	{
		distance += d;
	}
	
	public int getDistance()
	{
		return distance;
	}
	
	public void useItem(World world)
	{
		if(item != null)
		{
			item.use(world, this, occupied);
			item = null;
		}
	}
	
	public Cell getOccupied()
	{
		return occupied;
	}
	
	public boolean pickUp(Consumable item)
	{
		if(this.item == null)
		{
			this.item = item;
			return true;
		}
		return false;		
	}
	
	public boolean interact(Attachment attached)
	{
		if(attached != null)
			return(attached.act(this));
		return false;
	}
	
	public ArrayList<Buff> getBuffs()
	{
		return buffs;
	}
	
	public void moveUp()
	{
		y -= speed;
		dir = 0;
		move = true;
	}
	
	public void moveRight()
	{
		x += speed;
		dir = 1;
		move = true;
	}
	
	public void moveDown()
	{
		y += speed;
		dir = 2;
		move = true;
	}
	
	public void moveLeft()
	{
		x -= speed;
		dir = 3;
		move = true;
	}
	
	public int getX()
	{
		float nx = x % 1;
		int dx = (int)(x - nx);
		x = nx;
		return dx;
	}
	
	public int getY()
	{
		float ny = y % 1;
		int dy = (int)(y - ny);
		y = ny;
		return dy;
	}
}
