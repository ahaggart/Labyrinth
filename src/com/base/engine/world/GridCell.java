package com.base.engine.world;

import com.base.engine.attachments.Attachment;
import org.newdawn.slick.Graphics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class GridCell implements WorldWrapGrid.Cell{
    private int status;
    private int x,y;

    private static final float maxDistanceSquared = 576;
    private static final float minDistanceSquared = 529;

    private static final CellPopulator pop = new CellPopulator();

    private LinkedList<CellAttachment> attachments;

    public GridCell(int x, int y){
        this.x = x;
        this.y = y;

        this.status = DEAD;

        attachments = new LinkedList<>();
    }

    @Override
    public boolean update(double ox, double oy,WorldWrapGrid.Cell[] neighbors,HashSet<WorldWrapGrid.Cell> activated) {
        double x_diff = this.x - ox;
        double y_diff = this.y - oy;
        double dist = x_diff*x_diff+y_diff*y_diff;
        if(dist < minDistanceSquared){
            //if too close to origin, generate new cells
//            System.out.println("Generating neighbors");
            generate(neighbors);
            pop.finalPopulate(attachments,neighbors);
            setStatus(DORMANT);
            activated.addAll(Arrays.asList(neighbors));
            return false;
        } else if(dist > maxDistanceSquared){
            //if too far from origin, die and wake neighbors
            wake(neighbors);
            setStatus(DEAD);
            return false;
        }

        for(CellAttachment a : attachments){
            a.update();
        }

        return true;
    }

    @Override
    public void draw(Graphics b){
        int pixelX = x * WIDTH;
        int pixelY = y * HEIGHT;
        for(CellAttachment a : attachments){
            a.draw(b,pixelX,pixelY);
        }
    }

    @Override
    public void setStatus(int status){

        switch(status){
            case GENERATE:
                this.generateContents();
            case WAKE:
//                System.out.println("("+x+","+y+"): activated");
                this.status = ACTIVE;
                break;
            default:
                this.status = status;
        }
    }

    @Override
    public int getStatus(){
        return this.status;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    private void generate(WorldWrapGrid.Cell[] neighbors){
        for(int i = 0; i < 4; i++){ //TODO: no magic numbers?
            if(neighbors[i].getStatus() == DEAD){
                neighbors[i].setStatus(GENERATE);
            }
        }
    }

    private void wake(WorldWrapGrid.Cell[] neighbors){
        for(int i = 0; i < 4; i++){ //TODO: no magic numbers?
            if(neighbors[i].getStatus() == DORMANT){
                neighbors[i].setStatus(WAKE);
            }
        }
    }

    private void generateContents(){
        pop.populate(this.attachments);
    }

    private CellType getCellType(){
        CellAttachment lastAttachment = this.attachments.peek();
        if(lastAttachment instanceof CellType){
            return (CellType)lastAttachment;
        }
        return null;
    }

    public int getType(){
        return this.getCellType().getTypeCode();
    }

    public static int reverseDir(int dir){
        return (dir+2)%4;
    }
}
