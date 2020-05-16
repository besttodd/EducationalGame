package au.edu.jcu.cp3406.educationalgame;

import android.graphics.Bitmap;

class Tile {
    private Bitmap shape;
    private Bitmap lightedImage;
    private Bitmap activeTile;

    Tile(Bitmap[] files) {
        shape = files[0];
        lightedImage = files[1];
        activeTile = shape;
    }

    void lightUp() {
        activeTile = lightedImage;
    }

    void lightOff() {
        activeTile = shape;
    }

    Bitmap getActive() {
        return activeTile;
    }
}
