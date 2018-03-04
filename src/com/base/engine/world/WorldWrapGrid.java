package com.base.engine.world;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WorldWrapGrid {
    private int width,height;
    private ArrayList<WorldWrapGrid.Cell> grid;
    private int ox, oy; //ordered pair of the "origin" cell

    private Set<Cell> activeCells;

    public interface Cell{
        int DEAD    = 0;
        int DORMANT = 1;
        int ACTIVE  = 2;
        int GENERATE  = 3;
        int WAKE = 4;

        int UP = 0;
        int RIGHT = 1;
        int DOWN = 2;
        int LEFT = 3;

        int WIDTH = 16;
        int HEIGHT = 16;

        boolean update(double ox, double oy,Cell[] neighbors);
        void draw(Graphics b);
        void setStatus(int status);
        int getStatus();
        int getX();
        int getY();
        int getType();
    }

    public WorldWrapGrid(int width, int height){
        this.width  = width/Cell.WIDTH;
        this.height = height/Cell.HEIGHT;

        this.grid = new ArrayList<>(this.width*this.height);
        for(int i = 0; i < this.width*this.height; i++){
            grid.add(new GridCell(i%this.width,i/this.width));
        }

        activeCells = new HashSet<>();

        this.ox = this.width/2;
        this.oy = this.height/2;

        System.out.println("("+ox+","+oy+")");
        Cell seed = getCell(ox,oy);

        seed.setStatus(Cell.GENERATE);

        activeCells.add(seed);

        SpriteLoader loader = SpriteLoader.getInstance();
        loader.loadPNG("stone_brick",16,16,2,0);
        loader.loadPNG("dirt_floor",16,16,2,0);
        loader.loadPNG("dirt_cracked",16,16,2,0);
    }

    /**
     * Get a cell from a raw ordered pair
     * @param x x index of the cell to retrieve
     * @param y y index of the cell to retrieve
     * @return Cell in the requested index, or null if an invalid index is given
     */
    public Cell getCell(int x, int y){
        if(0 <= x && x < width && 0 <= y && y < height){
            return this.grid.get(x+y*width);
        }
        return null;
    }

    /**
     * Get a cell from an ordered pair in the player's coordinate system,
     * indices are wrapped to map width
     * @param cx x offset from origin cell
     * @param cy y offset from origin cell
     * @return Cell in the requested relative index
     */
    public Cell getRelativeCell(int cx,int cy){
        return this.getCell((ox+cx)%this.width,(ox+cy)%this.height);
    }

    public void update(){
        for (Iterator<Cell> i = activeCells.iterator(); i.hasNext();) {
            Cell cell = i.next();
            //pass cell its neighbors so it can take appropriate actions
            if (!cell.update(ox,oy,this.getNeighbors(cell))) {
                i.remove();
            }
        }
    }

    public void draw(Graphics b){
        //draw all the cells first
        for(Cell c : grid){
            c.draw(b);
        }

        //draw other objects
    }

    public void reset() {
        //kill all the cells in the grid
        for(Cell c: grid){
            c.setStatus(Cell.DEAD);
        }

        //reset player data
    }

    private Cell[] getNeighbors(Cell c){
        int x = c.getX();
        int y = c.getY();
        Cell[] neighbors = new Cell[4];

        neighbors[Cell.UP]      = this.getRelativeCell(x,y+1);
        neighbors[Cell.RIGHT]   = this.getRelativeCell(x+1,y);
        neighbors[Cell.DOWN]    = this.getRelativeCell(x,y-1);
        neighbors[Cell.LEFT]    = this.getRelativeCell(x-1,y);

        return neighbors;
    }

    public void click(int x, int y){

    }

    public void mouse(int x, int y){

    }

    public void key(int code){

    }

    public void release(int code){

    }
}
