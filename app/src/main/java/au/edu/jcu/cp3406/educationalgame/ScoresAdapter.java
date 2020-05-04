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

    ScoresAdapter(Context context, ArrayList<String> id,
                  ArrayList<String> date, ArrayList<String> difficulty, ArrayList<Integer> score) {
        this.context = context;
        entryId = id;
        this.date = date;
        this.difficulty = difficulty;
        this.score = score;
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
            child = layoutInflater.inflate(R.layout.highscoreslayout, null);

            holder = new Holder();
            holder.textviewid = (TextView) child.findViewById(R.id.textViewID);
            holder.textviewdate = (TextView) child.findViewById(R.id.textViewDATE);
            holder.textviewdifficulty = (TextView) child.findViewById(R.id.textViewDIFFICULTY);
            holder.textviewscore = (TextView) child.findViewById(R.id.textViewSCORE);
            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.textviewid.setText(entryId.get(position));
        holder.textviewdate.setText(date.get(position));
        holder.textviewdifficulty.setText(difficulty.get(position));
        holder.textviewscore.setText(score.get(position).toString());

        return child;
    }

    public static class Holder {
        TextView textviewid;
        TextView textviewdate;
        TextView textviewdifficulty;
        TextView textviewscore;
    }
}
