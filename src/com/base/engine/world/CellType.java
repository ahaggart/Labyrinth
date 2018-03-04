package com.base.engine.world;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class CellType implements CellAttachment{
    protected int sprite;
    protected String sheet;
    public abstract void act(GridObject o);
    public abstract int getTypeCode();
    public void update(){}
    public void draw(Graphics b,int x,int y){
        Image img = SpriteLoader.getInstance().getSpriteSheet(sheet).getSprite(0,0);
        b.drawImage(img,x,y);
    }
}
