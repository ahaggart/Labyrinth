package com.base.engine.world;

import org.newdawn.slick.Graphics;

public class WallTile extends CellType {
    public WallTile(){
        //use a default sprite if we cant calculate one
        sprite = 0;
        sheet = "stone_brick";
    }

    public void setSprite(WorldWrapGrid.Cell[] neighbors){
        //calculate which sprite to use based on neighbors
    }

    @Override
    public void act(GridObject o){

    }
    @Override
    public int getTypeCode(){
        return 1;
    }

//    @Override
//    public void draw(Graphics b,double x, double y){
//
//    }

}
