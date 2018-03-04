package com.base.engine.world;

import org.newdawn.slick.Graphics;

public interface CellAttachment {
    void update();
    void act(GridObject o);
    int getTypeCode();
    void draw(Graphics b,int x,int y);
}
