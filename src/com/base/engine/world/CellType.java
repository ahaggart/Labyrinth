package com.base.engine.world;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class CellType implements CellAttachment{
    protected int sx,sy;
    protected String sheet;
    protected char enc;
    public abstract void act(GridObject o);
    public abstract int getTypeCode();
    public abstract void updateSpriteCoords();
    public void update(){}
    public void draw(Graphics b,int x,int y){
        Image img = SpriteLoader.getInstance().getSpriteSheet(sheet).getSprite(sx,sy);
        b.drawImage(img,x,y);
//        b.drawString(""+(int)enc,x,y);
    }

    protected void setEncoding(boolean[] occupancy){
        enc = 0;
        enc += occupancy[WorldWrapGrid.Cell.UP]?8:0;
        enc += occupancy[WorldWrapGrid.Cell.RIGHT]?4:0;
        enc += occupancy[WorldWrapGrid.Cell.DOWN]?2:0;
        enc += occupancy[WorldWrapGrid.Cell.LEFT]?1:0;
    }

    protected boolean[] getOccupancy(){
        boolean occ[] = new boolean[4];
        char enc = this.enc;
        for(int i  = 3; i >= 0; i--){
            occ[i] = (enc & 1) == 1;
            enc >>= 1;
        }
        return occ;
    }

    public void updateOccupancy(int neighbor,int dir){
        boolean[] occ = getOccupancy();
        occ[GridCell.reverseDir(dir)] = neighbor == 1;

//        setEncoding(occ);
//        updateSpriteCoords();
    }


}
