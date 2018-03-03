package com.base.engine.core;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.base.engine.world.World;
import com.base.engine.world.WorldGrid;

public class BaseGame extends BasicGame 
{
	public static AppGameContainer app;
	
	public static Input input;
	
	public static int width = 1366;
	public static int height = 768;

	private World world;
	
	public BaseGame()
	{
		super("Labyrinth");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		world = new World(width, height);
		input = gc.getInput();
		System.out.println("" + gc.getScreenWidth() + " x " + gc.getScreenHeight());
	}
	
	@Override
	public void render(GameContainer gc, Graphics b) throws SlickException 
	{
		world.draw(b);
		b.setColor(Color.white);
		b.drawString("FPS: " + gc.getFPS(), 20, 40);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		world.update();
	}
	
    public static void main(String[] args) 
    {
        try 
        {
			app = new AppGameContainer(new BaseGame());
			app.setDisplayMode(width,height,false);
			app.setVSync(false);
			app.setTargetFrameRate(60);
			app.setShowFPS(true);
			app.start();
        } 
        catch (SlickException e) 
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void keyPressed(int key, char c)
    {
    	world.key(key);
    }
    
    @Override
    public void keyReleased(int key, char c)
    {
    	world.release(key);
    }
    
    @Override
    public void mouseMoved(int oldX, int oldY, int newX, int newY)
    {
    	world.mouse(newX, newY);
    }
    
    @Override
    public void mouseDragged(int oldX, int oldY, int newX, int newY)
    {
    	world.mouse(newX, newY);
    }
    
    @Override
    public void mousePressed(int button, int x, int y)
    {
    	world.click(x, y);
    }

}
