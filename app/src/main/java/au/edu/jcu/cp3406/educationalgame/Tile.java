package au.edu.jcu.cp3406.educationalgame;

import android.graphics.Bitmap;

public class Tile {
    Bitmap tileImage;
    private int image;
    private int lightedImage;
    private int activeTile;
    private String shape;
    boolean on;

    public Tile(int imageResource) {
        //tileImage = imageResource;
        image = imageResource;
        lightedImage = imageResource + 1;
        activeTile = imageResource;
        on = false;
    }

    int getImg() {
        return activeTile;
    }

    public void lightUp() {
        activeTile = lightedImage;
        System.out.println("Light UP"+activeTile+"-------------------------------------------------");
        on = true;
    }

    public void lightOff() {
        activeTile = image;
        on = false;
    }

    public boolean isOn() {
        return on;
    }
}
