package edu.csub.startracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that controls instance of player spaceship
 */
public class Player implements GameObject {

    private float x, y, prevX, prevY, health = 100f;
    private final Bitmap playerImg, playerLeft, playerRight;
    private Bitmap curImage;
    private Paint paint = new Paint();
    private final float dpi;
    private int frameTicks = 0, shotTicks = 0, laserTicks = 0;
    private final Resources res;
    private final int width, height;
    ArrayList<Laser> lasers = new ArrayList<>();
    private final MediaPlayer laserSound;

    /**
     * Constructor that instantiates needed ship attributes
     * @param res android resources
     */
    public Player(Resources res, Context context){
        this.res = res;
        playerImg = BitmapFactory.decodeResource(res, R.mipmap.player);
        playerLeft = BitmapFactory.decodeResource(res, R.mipmap.player_left);
        playerRight = BitmapFactory.decodeResource(res, R.mipmap.player_right);
        curImage = playerImg;
        width = curImage.getWidth();
        height = curImage.getHeight();

        DisplayMetrics dm = res.getDisplayMetrics();
        dpi = dm.densityDpi;
        x = (dm.widthPixels / 2f) - (playerImg.getHeight() / 2f);
        y = (dm.heightPixels * 0.75f);

        laserSound = MediaPlayer.create(context, R.raw.laser);
        laserSound.setVolume(0.5f, 0.5f);
    }

    /**
     * Method specifically used for updating users touch-point
     * @param touchX x axis variable
     * @param touchY y axis variable
     */
    public void updateTouch(int touchX, int touchY){
        if(touchX > 0 && touchY > 0){
            this.x = touchX - (playerImg.getWidth() / 2f);
            this.y = touchY - (playerImg.getHeight() * 2f);
        }
    }

    /**
     * Continuously updates player ship and lasers and determines which asset to display
     */
    @Override
    public void update(){

        if(health <= 0)
            return;

        if(Math.abs(x - prevX) < 0.04 * dpi){
            frameTicks++;
        } else if (frameTicks > 5){
            frameTicks = 0;
        }

        if(this.x < prevX - (0.04 * dpi)){
            curImage = playerLeft;
        }
        else if(this.x > prevX + (0.04 * dpi)){
            curImage = playerRight;
        } else {
            curImage = playerImg;
        }

        prevX = x;
        prevY = y;
        shotTicks++;
        laserTicks++;

        if(laserTicks >= 42) {
            laserSound.start();
            laserTicks = 0;
        }

        if(shotTicks >= 14){
            Laser tmp = new Laser(this.res);
            tmp.setX(x + (playerImg.getWidth() / 2f) - tmp.getMidX());
            tmp.setY(y - (tmp.getHeight() / 2f));

            lasers.add(tmp);
            shotTicks = 0;
        }

        for(Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext();){
            Laser laser = iterator.next();
            if(!laser.isOnScreen() || !laser.isAlive())
                iterator.remove();
        }

        for(Laser laser : lasers){
            laser.update();
        }
    }

    /**
     * Continuously displays player assets to the screen
     * @param canvas android activity of game
     */
    public void draw(Canvas canvas){

        if(health <= 0)
            return;

        canvas.drawBitmap(curImage, this.x, this.y, this.paint);
        for(Laser laser : lasers)
            laser.draw(canvas);
    }

    /**
     * Getter
     * @return x
     */
    @Override
    public float getX() {return x;}

    /**
     * Getter
     * @return y
     */
    @Override
    public float getY() {return y;}

    /**
     * Getter
     * @return width
     */
    @Override
    public float getWidth() {return width;}

    /**
     * Getter
     * @return height
     */
    @Override
    public float getHeight() {return height;}

    /**
     * Check if player still has health
     * @return bool to see if player is still alive
     */
    @Override
    public boolean isAlive() {return health > 0f;}

    /**
     * Getter
     * @return players remaining health
     */
    @Override
    public float getHealth() {return health;}

    /**
     * Applies damage taken
     * @param damage amount of damage from enemy
     * @return health - damage
     */
    @Override
    public float takeDamage(float damage) {return health -= damage;}

    /**
     * Applies health restored
     * @param repairAmount amount of health re-applied
     * @return health + restoration
     */
    @Override
    public float addHealth(float repairAmount) {return health += repairAmount;}

    /**
     * Getter function
     * @return all live lasers
     */
    public ArrayList<Laser> getLasers(){return lasers;}
}
