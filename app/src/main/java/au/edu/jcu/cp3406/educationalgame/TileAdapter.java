package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class TileAdapter extends BaseAdapter {
    private final Context context;
    private final Tile[] tiles;

    TileAdapter(Context context, Tile[] tiles) {
        this.context = context;
        this.tiles = tiles;
    }

    @Override
    public int getCount() {
        return tiles.length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tile tile = tiles[position];

        GridView gv = (GridView) parent;
        gv.setColumnWidth(200);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.tile, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.tileImageView);
        imageView.setImageBitmap(tile.getActive());
        return convertView;
    }
}
