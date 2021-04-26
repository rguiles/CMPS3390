package edu.csub.startracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Main Driver for StarTracker: Program starts here
 * @author Richard Guiles
 * @version 1.2
 */
public class MainActivity extends AppCompatActivity {

    private HighScore highScore = HighScore.getInstance();

    /**
     * Activates full screen mode when game is launched
     * @param savedInstanceState data from previous launch
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getTopScores(10);
        TextView tvHighScore = findViewById(R.id.tvHighScore);
        EditText etPlayerName = findViewById(R.id.etPlayerName);
        etPlayerName.setText(highScore.getPlayerName());
        tvHighScore.setText(String.format("High Score: %s", highScore.getHighScore()));
        if(highScore.getHighScore() != 0 && highScore.getHighScore() == highScore.getCurScore())
            highScore.postHighScore();
    }

    private void getTopScores(int howMany){
        ListView highScores = findViewById(R.id.lvTopScores);
        highScore.getHighScores(10, highScores, this);
    }

    /**
     * Creates Instance when PLAY button is clicked
     * @param view Activity that displays game to screen
     */
    public void onPlayButtonClick(View view) {
        highScore.resetCurScore();
        EditText etPlayerName = findViewById(R.id.etPlayerName);
        highScore.setPlayerName(etPlayerName.getText().toString());
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}