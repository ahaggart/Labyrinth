package com.base.engine.world;

import org.newdawn.slick.Graphics;

public class AirTile extends CellType{
    private static final int NUM_SPRITES = 3;
    public AirTile(){
        sx = 1+(int)(Math.random() * NUM_SPRITES);
        sy=1;
        sheet = "dirt_floor";
    }

    @Override
    public void act(GridObject o){

    }

    @Override
    public int getTypeCode(){
        return 0;
    }
//
//    @Override
//    public void draw(Graphics b,double x, double y){
//
//    }

    public void updateSpriteCoords(){

    }
}
