package au.edu.jcu.cp3406.educationalgame;

import android.graphics.Bitmap;

public class Tile {
    Bitmap tileImage;
    private int image;
    private int lightedImage;
    private int activeTile;
    private int previous;
    private String shape;
    boolean on;

    Tile(int imageResource) {
        //tileImage = imageResource;
        image = imageResource;
        lightedImage = imageResource + 1;
        activeTile = imageResource;
        previous = 0;
        on = false;
    }

    int getImg() {
        return activeTile;
    }

    void lightUp() {
        activeTile = lightedImage;
        System.out.println("Light UP"+activeTile+"-------------------------------------------------");
        on = true;
    }

    void lightOff() {
        activeTile = image;
        on = false;
    }

    public boolean isOn() {
        return on;
    }
}
