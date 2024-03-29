package edu.csub.rhythmtracker;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

/**
 * Main Driver for Rhythm Tracker: Program starts here
 * @author Richard Guiles
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Activates full screen mode when game is launched
     * @param savedInstanceState Data from previous launch
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Launches the Game Activity when play button is clicked
     * @param view Game Canvas
     */
    public void onPlayButtonClicked(View view) {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}