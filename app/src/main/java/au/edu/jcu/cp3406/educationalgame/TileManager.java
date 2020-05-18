package au.edu.jcu.cp3406.educationalgame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class TileManager {
    private AssetManager assetManager;
    private String[] shapes;

    TileManager(AssetManager assetManager, String assetPath) {
        this.assetManager = assetManager;
        try {
            shapes = assetManager.list(assetPath);
        } catch (IOException e) {
            Log.i("TileManger", "Failed to get image names");
        }
    }

    public Bitmap getTileImage(int i) {
        InputStream stream = null;
        try {
            stream = assetManager.open("Shapes/" + shapes[i]);
        } catch (IOException e) {
            Log.i("TileManager", "Failed to open Shapes/" + shapes[i]);
        }
        return BitmapFactory.decodeStream(stream);
    }

    Tile[] getTiles(int numTiles) {
        Tile[] tiles = new Tile[numTiles];
        List<Tile> list = new ArrayList<>();
        Bitmap[] selected = new Bitmap[2];

        for (int i = 0; i < numTiles * 2; i += 2) {
            selected[0] = getTileImage(i);
            selected[1] = getTileImage(i + 1);
            list.add(new Tile(selected));
        }
        list.toArray(tiles);
        return tiles;
    }
}
