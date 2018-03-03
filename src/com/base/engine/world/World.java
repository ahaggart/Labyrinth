package com.base.engine.world;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import com.base.engine.consumables.Teleport;
import com.base.engine.core.BaseGame;
import com.base.engine.particles.Particle;
import com.base.engine.tileEffects.*;

public class World
{
	public static int width, height;
	public static final int bufferSize = 10;
	
	private ArrayList<Cell> grid;
	private ArrayList<Cell> active;
	private ArrayList<GridObject> objects;
	private ArrayList<Particle> particles;
	private Player player;
	
	private ArrayList<Cell> proximity = new ArrayList<Cell>();
	
	private int activeCells;
/*
The active list should be constructed every tick out of cells which are marked as active. Cells which are too close to the player will create
cells farther out and deactivate themselves, effectively removing them from the update cycle. When a cell is too far from the player, it will
mark itself for destruction and will be removed from the grid list, and will notify neighboring tiles to wake up and re-enter the update cycle,
as they will compose the new outer ring of active cells until they are despawned or deactivated, depending on the movement of the player. 
*/	
	
	protected ArrayList<Integer> lastPressed = new ArrayList<Integer>();
	protected ArrayList<Integer> currentPressed = new ArrayList<Integer>();	
	
	private Cell[][] grid2;
	public int mouseX, mouseY;
		
	public World()
	{
		
	}
	public World(int width, int height)
	{
		grid = new ArrayList<Cell>();
		active = new ArrayList<Cell>();
		objects = new ArrayList<GridObject>();
		particles = new ArrayList<Particle>();
		activeCells = 0;
		Cell seed = new Cell(0,0, grid);
//cell behavior automatically generates new cells when an active cell is too close to the origin(player)
//by adding a single cell at the origin, we can 'seed' the cell grid and allow it to grow out from the origin procedurally
//this can lead to some homogeneity in the color of cells produced by initial generation, but color is a temporary test feature anyways
		grid.add(seed);
		player = new Player(this);
		
		this.width = width;
		this.height = height;
		
		grid2 = new Cell[width / Cell.width + bufferSize * 2][height / Cell.width + bufferSize * 2];
		
	}
	
	public void reset()// clear and reseed the grid
	{
		grid.clear();
		active.clear();
		objects.clear();
		particles.clear();
		player.reset();
		Cell seed = new Cell(0,0, grid);
		grid.add(seed);
	}
	
	public void draw(Graphics b)
	{
		for(int i = 0; i < grid.size(); i++)
			if(grid.get(i).checkBounds())
		//		if(!grid.get(i).isActive())
					grid.get(i).draw(b);
		for(int i = 0; i < grid.size(); i++)
			if(grid.get(i).checkBounds())
				if(!grid.get(i).isActive())
					grid.get(i).drawAttached(b);
		for(int i = 0; i < objects.size(); i++)
			objects.get(i).draw(b);
	
		player.draw(b);
		ArrayList<Particle> playerParticles = player.getParticles();
		for(int i = 0; i < playerParticles.size(); i++)
			particles.add(playerParticles.get(i));
		playerParticles.clear();
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).draw(b);	
		for(int i = particles.size() - 1; i >= 0; i--)
			if(particles.get(i).remove())
				particles.remove(i);
		
	}
	
	public void update()
	{
		runKeys();
		move(-player.getX(), -player.getY());
		grid2 = new Cell[width / Cell.width + bufferSize*2][height / Cell.width + bufferSize*2];
		int xOff = grid.get(0).getX() % Cell.width;
		int yOff = grid.get(0).getY() % Cell.width;
		grid2[0][0] = new Cell(xOff, yOff);
		//construct active cell list from list of all cells
		for(int i = 0; i < grid.size(); i++)
		{
			if(grid.get(i).isActive())
				active.add(grid.get(i));
			grid.get(i).updateEffects();
			int x = (int)((float)(grid.get(i).getX() - xOff + BaseGame.width / 2) / Cell.width + bufferSize);
			int y = (int)((float)(grid.get(i).getY() - yOff + BaseGame.height / 2) / Cell.width + bufferSize);
			if(x >= 0 && x < grid2.length && y >=0 && y < grid2[0].length)
				grid2[x][y] = grid.get(i);
		}
//		for(int i = 0; i < grid2.length; i++)
//			for(int j = 0; j < grid2[0].length; j++)
//				if(grid2[i][j] != null)
//					grid2[i][j].setColor(Color.green);
		int x = (int)((float)(mouseX - xOff) / Cell.width + bufferSize);
		int y = (int)((float)(mouseY - yOff) / Cell.width + bufferSize);
		ArrayList<Cell> cursor = getSubGrid(x,y,3,3);
		for(int i = 0; i < cursor.size(); i++)
			cursor.get(i).setColor(Color.red);
		//update active cells
		for(int i = 0; i < active.size(); i++)
			active.get(i).update();
		activeCells = active.size();
		active.clear();
		//cull 'dead' cells
		for(int i = grid.size() - 1; i >= 0; i--)
			if(grid.get(i).remove())
				grid.remove(i);
				
		for(int i = 0; i < objects.size(); i++)
		{
			objects.get(i).update();
			if(objects.get(i).getOccupied() != null)
				objects.get(i).getOccupied().act(objects.get(i));
		}
		
		for(int i = objects.size() - 1; i >= 0; i--)
			if(objects.get(i).remove())
				objects.remove(i);
				
		//code for labyrinth test
		proximity.clear();
		int px = (int)((float)(-xOff + BaseGame.width / 2) / Cell.width + bufferSize);
		int py = (int)((float)(-yOff + BaseGame.height / 2) / Cell.width + bufferSize);
		proximity = getSubGrid(px,py,3,3);
		
		Cell occupied = null;
		for(int i = 0; i < proximity.size(); i++)
			if(proximity.get(i).contains(0, 0))
			{
				proximity.get(i).collect(player);
				occupied = proximity.get(i);
			}
				
		if(grid.size() > 2000 && objects.size() < 2)//minotaur populating loop
		{
			int rand = 1700 + (int)(300 * Math.random());
			while(grid.get(rand).getStatus() == 1)
				rand = 1700 + (int)(300 * Math.random());
			objects.add(new Minotaur(grid.get(rand), this));
		}
		player.update(occupied);
	}
	
	public void move(int dx, int dy)
	{	
		for(int i = 0; i < Math.abs(dx); i++)
		{
			boolean move = true;
			for(Cell c : proximity)
				if(c.getStatus() == 1)
					if(c.contains((int)(-dx / Math.abs(dx)), 0))
						move = false;
			if(move)			
				moveGrid((int)(dx / Math.abs(dx)), 0);
		}
		for(int i = 0; i < Math.abs(dy); i++)
		{
			boolean move = true;
			for(Cell c : proximity)
				if(c.getStatus() == 1)
					if(c.contains(0, (int)(-dy / Math.abs(dy))))
						move = false;
			if(move)			
				moveGrid(0, (int)(dy / Math.abs(dy)));
		}
		
	}
	
	public void moveGrid(int dx, int dy)
	{
		for(int i = 0; i < grid.size(); i++)
			grid.get(i).move(dx,dy);
		for(int i = 0; i < objects.size(); i++)
			objects.get(i).move(dx,dy);
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).move(dx,dy);
			
		player.addDistance(Math.abs(dx) + Math.abs(dy));
	}
	
	public void addParticle(Particle particle)
	{
		particles.add(particle);
	}
	
	public void click(int x, int y)
	{
		double angle = Math.atan2(mouseY - height / 2, mouseX - width / 2);
		objects.add(new Projectile(0, 0, this, 5*(float)Math.cos(angle), 5*(float)Math.sin(angle)));
	}
	
	public void key(int keyCode)
	{
		currentPressed.add(keyCode);
		lastPressed.add(keyCode);
	}
	
	public void release(int keyCode)
	{
		currentPressed.remove((Integer)keyCode);
	}
	
	public void runKeys()
	{
		
		for(int i = 0; i < currentPressed.size();i++)
		{
			switch(currentPressed.get(i))
			{
				case Input.KEY_A:
					player.moveLeft();
					break;
				case Input.KEY_S:
					player.moveDown();
					break;
				case Input.KEY_D:
					player.moveRight();
					break;
				case Input.KEY_W:
					player.moveUp();
					break;
				case Input.KEY_R:
					reset();
					break;
//				case Input.KEY_Q:
//					player.pickUp(new Teleport());
//					break;
//				case Input.KEY_SPACE:
//					player.useItem(this);
//					break;
			}
		}
		if(lastPressed.contains(Input.KEY_SPACE))
			player.useItem(this);
		
		for(int i = 0; i < lastPressed.size(); i++)
			if(!currentPressed.contains(lastPressed.get(i)))
				currentPressed.add(lastPressed.get(i));
		lastPressed.clear();	
	}
	
	public ArrayList<Integer> getPressed()
	{
		return currentPressed;
	}
	
	public ArrayList<Cell> getGrid()
	{
		return grid;
	}
	
	public Cell[][] getGrid2()
	{
		return grid2;
	}
	
	public ArrayList<Cell> getSubGrid(int x, int y, int width, int height)
	{
		ArrayList<Cell> list = new ArrayList<Cell>();
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++)
			{
				int gx = x - width / 2 + i;
				int gy = y - height / 2 + j;
				if(gx > 0 && gx < grid2.length)
					if(gy > 0 && gy < grid2[0].length)
						if(grid2[gx][gy] != null)
							list.add(grid2[gx][gy]);
			}
		return list;
	}
	
	public ArrayList<GridObject> getObjects()
	{
		return objects;
	}
	
	public Cell getCell(int x, int y)
	{
		return grid2[x][y];
	}
	
	public void mouse(int x, int y)
	{
		mouseX = x;
		mouseY = y;
	}
	
}