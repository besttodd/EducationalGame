package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TileAdapter extends BaseAdapter {
    private final Context mContext;
    private final Tile[] tiles;

    TileAdapter(Context context, Tile[] tiles) {
        this.mContext = context;
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

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.tile, null);
        }

        ImageView imageView = convertView.findViewById(R.id.tileView);
        imageView.setImageResource(tile.getImg());

        return convertView;
    }
}
