package au.edu.jcu.cp3406.educationalgame;

import android.graphics.Bitmap;

class Tile {
    private Bitmap shape;
    private Bitmap lightedShape;
    private Bitmap activeShape;

    Tile(Bitmap[] files) {
        shape = files[0];
        lightedShape = files[1];
        activeShape = shape;
    }

    void lightUp() {
        activeShape = lightedShape;
    }

    void lightOff() {
        activeShape = shape;
    }

    Bitmap getActive() {
        return activeShape;
    }
}
