package com.base.engine.world;

import java.util.LinkedList;

public class CellPopulator {
    private static final int WALL_TILE = 1;
    private static final int AIR_TILE  = 0;

    private static final double WALL_PROB_BASE = 0.5;
    private static final double TREASURE_PROB_BASE = 0.01;
    private static final double LOOT_PROB_BASE = 0.1;

    CellPopulator(){

    }

    public void populate(LinkedList<CellAttachment> attachments){
        double decider = Math.random();


        //the top element should be a CellType attachment
        attachments.push(getCellType(decider));
    }

    public void finalPopulate(LinkedList<CellAttachment> attachments,WorldWrapGrid.Cell[] neighbors){
        CellAttachment typeAttachment = attachments.peek();
        CellType type;
        try{
            type = (CellType)typeAttachment;
        } catch(ClassCastException cce){
           //why doesnt this cell have a type?
            System.out.println("Untyped cell found :(");
            return;
        }
        if(type instanceof WallTile) {
            //clear orphaned wall blocks
            if (countNeighborTypeMatch(neighbors, AIR_TILE) > 3) {
                attachments.removeLast(); //remove the existing type and attach a new one
                attachments.add(getCellType(AIR_TILE));
            } else{
                WallTile tile = (WallTile)attachments.peekLast();
                tile.setSprite(neighbors); //set the tile to use the correct sprite
            }
        } else if (type instanceof AirTile){
            if(countNeighborTypeMatch(neighbors,AIR_TILE) == 1){
                //this is a valid treasure location
                double decider = Math.random();
                if(decider < TREASURE_PROB_BASE){
                    //put a special treasure item here
                } else if(decider < LOOT_PROB_BASE){
                    //put a standard loot item here
                }
            }
        }

    }

    private CellType getCellType(double decider){
        int typecode = 0;
        if(decider > WALL_PROB_BASE){
            typecode = 1;
        }
        return getCellType(typecode);
    }

    private CellType getCellType(int code){
        switch(code){
            case WALL_TILE:
                return new WallTile();
            case AIR_TILE:
                return new AirTile();
            default:
                return new AirTile();
        }
    }

    private int countNeighborTypeMatch(WorldWrapGrid.Cell[] neighbors,int type){
        int count = 0;
        for(int i = 0; i < 4; i++){
            if(neighbors[i].getType() == type){
                count++;
            }
        }
        return count;
    }
}
