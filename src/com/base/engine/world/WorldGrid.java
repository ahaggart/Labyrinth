package com.base.engine.world;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.base.engine.core.BaseGame;
import com.base.engine.particles.Particle;

public class WorldGrid extends World
{
	public static int width, height;
	
	private Cell[][] grid;
	private int offX, offY;
	private int bufferSize;
	
	private Player player;
	private ArrayList<GridObject> objects;
	private ArrayList<Particle> particles;
	
	protected ArrayList<Integer> lastPressed = new ArrayList<Integer>();
	protected ArrayList<Integer> currentPressed = new ArrayList<Integer>();	
	
	public WorldGrid(int width, int height)
	{
		this.width = width;
		this.height = height;
		bufferSize = 2;
		
		player = new Player(null);
		objects = new ArrayList<GridObject>();
		particles = new ArrayList<Particle>();
		
		grid = new Cell[width / Cell.width + bufferSize*2][height / Cell.width + bufferSize*2];
		
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = newCell((i - bufferSize) * Cell.width, (j - bufferSize) * Cell.width);
				grid[i][j].sleep();
			}
		updateNeighbors();
		for(int i = 0; i < grid.length; i++)
			cleanColumn(i);
		
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				grid[i][j].updateSprite();
		
	}
	
	public void draw(Graphics b)
	{
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
			{
//				int x = i - grid.length / 2 + bufferSize;
//				int y = j - grid[0].length / 2 + bufferSize;
//				if(x*x + y*y < 400)
					grid[i][j].draw(b,(int)offX,(int)offY);
			}
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				grid[i][j].drawAttached(b,(int)offX,(int)offY);
		
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
		int dx = -player.getX();
		int dy = -player.getY();
		offX += dx;
		offY += dy;
		shiftGrid();
		
		Cell occupied = grid[(BaseGame.width / 2 - offX) / Cell.width + bufferSize][(BaseGame.height / 2 - offY) / Cell.width + bufferSize];
		occupied.setColor(Color.green);
		occupied.collect(player);
		player.update(occupied);
		
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).move(dx,dy);
	}
	
	public void shiftGrid()
	{
		int dx = 0;
		int dy = 0;
		if(Math.abs(offX) > Cell.width)
		{
			if(offX > 0)
			{
				for(int i = grid.length - 1; i > 0; i--)
					for(int j = 0; j < grid[i].length; j++)
					{
						grid[i][j] = grid[i-1][j];
						grid[i][j].move(Cell.width, 0);
					}
				for(int j = 0; j < grid[0].length; j++)
				{
					grid[0][j] = newCell(-bufferSize*Cell.width, (j-bufferSize)*Cell.width);
					grid[0][j].sleep();
				}
				updateColumnNeighbors(0);
				updateColumnNeighbors(1);
				cleanColumn(0);
				dx = Cell.width;
			}
			else
			{
				for(int i = 0; i < grid.length-1; i++)
					for(int j = 0; j < grid[i].length; j++)
					{
						grid[i][j] = grid[i+1][j];
						grid[i][j].move(-Cell.width, 0);
					}
				for(int j = 0; j < grid[grid.length - 1].length; j++)
				{
					grid[grid.length - 1][j] = newCell((grid.length - 1 - bufferSize) * Cell.width, (j-bufferSize)*Cell.width);
					grid[grid.length - 1][j].sleep();
				}
				updateColumnNeighbors(grid.length - 1);
				updateColumnNeighbors(grid.length - 2);
				cleanColumn(grid.length - 1);
				dx = -Cell.width;
			}
			offX = offX % Cell.width;
		}
		
		if(Math.abs(offY) > Cell.width)
		{
			if(offY > 0)
			{
				for(int i = 0; i < grid.length; i++)
					for(int j = grid[i].length - 1; j > 0; j--)
					{
						grid[i][j] = grid[i][j-1];
						grid[i][j].move(0, Cell.width);
					}
				for(int i = 0; i < grid.length; i++)
				{
					grid[i][0] = newCell((i-bufferSize)*Cell.width, -bufferSize*Cell.width);
					grid[i][0].sleep();
				}
				updateRowNeighbors(0);
				updateRowNeighbors(1);
				cleanRow(0);
				dy = Cell.width;
			}
			else
			{
				for(int i = 0; i < grid.length; i++)
					for(int j = 0; j < grid[i].length - 1; j++)
					{
						grid[i][j] = grid[i][j+1];
						grid[i][j].move(0, -Cell.width);
					}
				for(int i = 0; i < grid.length; i++)
				{
					grid[i][grid[i].length - 1] = newCell((i-bufferSize) * Cell.width, (grid[i].length - 1 - bufferSize)*Cell.width);
					grid[i][grid[i].length - 1].sleep();
				}
				updateRowNeighbors(grid[0].length - 1);
				updateRowNeighbors(grid[0].length - 2);
				cleanRow(grid[0].length - 1);
				dy = -Cell.width;
			}
			offY = offY % Cell.width;
		}
		
//		for(int i = 0; i < particles.size(); i++)
//			particles.get(i).move(dx,dy);
	}
	
	public void updateNeighbors()
	{
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
			{
				if( i > 0)
				{
					grid[i][j].setNeighbor(grid[i-1][j], 3);
				}
				if( i < grid.length - 1)
				{
					grid[i][j].setNeighbor(grid[i+1][j], 1);
				}
				if( j > 0)
				{
					grid[i][j].setNeighbor(grid[i][j-1], 0);
				}
				if( j < grid[i].length - 1)
				{
					grid[i][j].setNeighbor(grid[i][j+1], 2);
				}
				grid[i][j].updateSprite();
			}
	}
	
	public void updateRowNeighbors(int row)
	{
		for(int i = 0; i < grid.length; i++)
		{
			if( i > 0)
			{
				grid[i][row].setNeighbor(grid[i-1][row], 3);
			}
			if( i < grid.length - 1)
			{
				grid[i][row].setNeighbor(grid[i+1][row], 1);
			}
			if( row > 0)
			{
				grid[i][row].setNeighbor(grid[i][row-1], 0);
			}
			if( row < grid[i].length - 1)
			{
				grid[i][row].setNeighbor(grid[i][row+1], 2);
			}
			grid[i][row].updateSprite();
		}
	}
	
	public void updateColumnNeighbors(int column)
	{
		for(int j = 0; j < grid[column].length; j++)
		{
			if( column > 0)
			{
				grid[column][j].setNeighbor(grid[column-1][j], 3);
			}
			if( column < grid.length - 1)
			{
				grid[column][j].setNeighbor(grid[column+1][j], 1);
			}
			if( j > 0)
			{
				grid[column][j].setNeighbor(grid[column][j-1], 0);
			}
			if( j < grid[column].length - 1)
			{
				grid[column][j].setNeighbor(grid[column][j+1], 2);
			}
			grid[column][j].updateSprite();
		}
	}
	
	public void cleanColumn(int column)
	{
		for(int i = 0; i < grid[0].length; i++)
			grid[column][i].clearStrayBlocks();

	}
	
	public void cleanRow(int row)
	{
		for(int i = 0; i < grid.length; i++)
			grid[i][row].clearStrayBlocks();

	}
	
	public void reset()
	{
		
	}
	
	public Cell newCell(int x, int y)
	{
		return new Cell(x - BaseGame.width / 2, y - BaseGame.height / 2);
	}
	
	public void addParticle(Particle particle)
	{
		particles.add(particle);
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
//		if(lastPressed.contains(Input.KEY_SPACE))
//			player.useItem(this);
		
		for(int i = 0; i < lastPressed.size(); i++)
			if(!currentPressed.contains(lastPressed.get(i)))
				currentPressed.add(lastPressed.get(i));
		lastPressed.clear();	
	}
}
