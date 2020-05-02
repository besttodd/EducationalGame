package au.edu.jcu.cp3406.educationalgame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TileManager {
    private String[] shapes;
    private Tile[] tiles;
    private Tile[] lightedTiles;
    private AssetManager assetManager;

    TileManager(AssetManager assetManager, String assetPath) {
        this.assetManager = assetManager;
        try {
            shapes = assetManager.list(assetPath);
        } catch (IOException e) {
            Log.i("TileManger", "Failed to get image names");
        }
        tiles = new Tile[shapes.length/2];
        tiles = new Tile[shapes.length/2];
    }

    Tile[] getTiles() {
        InputStream stream = null;

        for (int i = 0; i < shapes.length; i=i+2) {

            try {
                stream = assetManager.open("Shapes/" + shapes[i]);
            } catch (IOException e) {
                Log.i("TileManager", "Failed to open Shapes/");
            }
            //tiles[i] = new Tile(BitmapFactory.decodeStream(stream));
        }
        return tiles;
    }

    Tile[] getLightedTiles() {
        InputStream stream = null;

        for (int i = 1; i < shapes.length; i=i+2) {

            try {
                stream = assetManager.open("Shapes/" + shapes[i]);
            } catch (IOException e) {
                Log.i("TileManager", "Failed to open Shapes/");
            }
            //lightedTiles[i] = new Tile(BitmapFactory.decodeStream(stream));
        }
        return lightedTiles;
    }

    public String getName(int i) {
        int dotIndex = shapes[i].lastIndexOf(".");
        return shapes[i].substring(0, dotIndex);
    }

    public int count() {
        return shapes.length;
    }
}
