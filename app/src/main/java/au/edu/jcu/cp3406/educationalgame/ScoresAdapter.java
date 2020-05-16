package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoresAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> entryId;
    private ArrayList<String> date;
    private ArrayList<String> difficulty;
    private ArrayList<Integer> score;
    private ArrayList<String> game;

    ScoresAdapter(Context context, ArrayList<String> id,
                  ArrayList<String> date, ArrayList<String> difficulty, ArrayList<Integer> score, ArrayList<String> game) {
        this.context = context;
        entryId = id;
        this.date = date;
        this.difficulty = difficulty;
        this.score = score;
        this.game = game;
    }

    public int getCount() {
        return entryId.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;
        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            child = layoutInflater.inflate(R.layout.highscoreslayout, parent, false);

            holder = new Holder();
            holder.textviewid = child.findViewById(R.id.textViewID);
            holder.textviewdate = child.findViewById(R.id.textViewDATE);
            holder.textviewdifficulty = child.findViewById(R.id.textViewDIFFICULTY);
            holder.textviewscore = child.findViewById(R.id.textViewSCORE);
            holder.textviewgame = child.findViewById(R.id.textViewGAME);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.textviewid.setText(entryId.get(position));
        holder.textviewdate.setText(date.get(position));
        holder.textviewdifficulty.setText(difficulty.get(position));
        holder.textviewscore.setText(String.valueOf(score.get(position)));
        holder.textviewgame.setText(game.get(position));

        return child;
    }

    public static class Holder {
        TextView textviewid;
        TextView textviewdate;
        TextView textviewdifficulty;
        TextView textviewscore;
        TextView textviewgame;
    }
}
