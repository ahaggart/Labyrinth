package com.base.engine.world;

import org.newdawn.slick.Graphics;

public class WallTile extends CellType {
    private static final int NUM_VARIANTS = 3;
    private int spriteRandomizer;
    public WallTile(){
        //use a default sprite if we cant calculate one
        sx = 9;
        sy = 3;
        sheet = "stone_brick";
        enc = 0;
        spriteRandomizer = (int)(Math.random()*NUM_VARIANTS);
    }

    public void setSprite(WorldWrapGrid.Cell[] neighbors){
        //calculate which sprite to use based on neighbors
        boolean occ[] = new boolean[4];
        occ[0]  = neighbors[WorldWrapGrid.Cell.UP].getType() == 1;
        occ[1]  = neighbors[WorldWrapGrid.Cell.RIGHT].getType() == 1;
        occ[2]  = neighbors[WorldWrapGrid.Cell.DOWN].getType() == 1;
        occ[3]  = neighbors[WorldWrapGrid.Cell.LEFT].getType() == 1;

        setEncoding(occ);
        updateSpriteCoords();


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


    public void updateSpriteCoords(){
        //TODO: I could probably beautify this with k-maps ... lol
        switch(enc) {
            case 15:
                sx = 1 + spriteRandomizer;
                sy = 1;
                break;
            case 14:
                sx = 0;
                sy = spriteRandomizer;
                break;
            case 13:
                sx = 1 + spriteRandomizer;
                sy = 2;
                break;
            case 12:
                sx = 2*spriteRandomizer;
                sy = 4;
                break;
            case 11:
                sx = 4;
                sy = spriteRandomizer;
                break;
            case 10:
                sx = 5;
                sy = spriteRandomizer;
                break;
            case 9:
                sx = 1 + 2*spriteRandomizer;
                sy = 4;
                break;
            case 8:
                sx = 6 + spriteRandomizer;
                sy = 3;
                break;
            case 7:
                sx = 1 + spriteRandomizer;
                sy = 0;
                break;
            case 6:
                sx = 2*spriteRandomizer;
                sy = 3;
                break;
            case 5:
                sx = 6 + spriteRandomizer;
                sy = 4;
                break;
            case 4:
                sx = 9;
                sy = spriteRandomizer;
                break;
            case 3:
                sx = 1 + 2*spriteRandomizer;
                sy = 3;
                break;
            case 2:
                sx = 6 + spriteRandomizer;
                sy = 0;
                break;
            case 1:
                sx = 12;
                sy = spriteRandomizer;
                break;
            default://0
                sx = 1 + spriteRandomizer;
                sy = 1;
        }
    }

}
