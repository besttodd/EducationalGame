package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {
    private Tile[] tiles =
     {new Tile(R.drawable.square), new Tile(R.drawable.circle), new Tile(R.drawable.square), new Tile(R.drawable.circle), new Tile(R.drawable.square), new Tile(R.drawable.circle)};
    TileManager tileManager;
    private GameBuilder game = new GameBuilder();
    private Difficulty level;
    final Handler handler = new Handler();
    Runnable runnable;
    TileAdapter tileAdapter;
    GridView gridView;

    List<Integer> sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        //tileManager = new TileManager(this.getAssets(), "Shapes");
        //tiles = tileManager.getTiles();

        gridView = findViewById(R.id.gridView);
        tileAdapter = new TileAdapter(this, tiles);
        gridView.setAdapter(tileAdapter);

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        sequence = game.newGame(level);
        if (game.sequenceRunning()) {
            playSequence(sequence);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tile tile = tiles[position];
                tile.lightUp();
                //turn off after 1000 millisec
                //check if its next in the sequence
                game.checkOrder(tile);

                tileAdapter.notifyDataSetChanged();
            }
        });
    }

    public void playSequence(final List<Integer> sequence) {
        final Iterator<Integer> iterator = sequence.iterator();
        final Integer[] previous = {0};

        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    tiles[previous[0]].lightOff();
                    previous[0] = iterator.next();
                    tiles[previous[0]].lightUp();
                    gridView.setAdapter(tileAdapter);
                    handler.postDelayed(this, 1200);
                } else {
                    tiles[previous[0]].lightOff(); //Not working
                    game.sequenceComplete();
                    Toast.makeText(getBaseContext(), "YOUR TURN", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
