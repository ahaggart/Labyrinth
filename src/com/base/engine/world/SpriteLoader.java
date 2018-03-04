package com.base.engine.world;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.util.HashMap;

public class SpriteLoader {
    private static final SpriteLoader instance = new SpriteLoader();

    private HashMap<String,SpriteSheet> sheets;

    private SpriteLoader(){
        sheets = new HashMap<>();
    }

    public void load(){

    }

    public SpriteSheet getSpriteSheet(String name){
        return sheets.get(name);
    }

    public void loadPNG(String name,int tw,int th,int spacing,int margin){
        load(name,getPath("img/",name,"png"),tw,tw,spacing,margin);
    }

    private void load(String name,String fullPath,int tw,int th,int spacing,int margin){
        try {
            sheets.put(name,
                    new SpriteSheet(new Image(fullPath),tw,th,spacing,margin));
        }catch (SlickException e){
            e.printStackTrace();
        }
    }

    private String getPath(String path,String name,String extension){
        return path + "/" + name + "." + extension;
    }

    public static SpriteLoader getInstance(){
        return instance;
    }
}
