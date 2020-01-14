package com.example.appprototype;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/*
* Clase que contiene todos los elementos del juego
*
*/

public class SuperficieJuego extends SurfaceView implements SurfaceHolder.Callback {

    protected GameBackground backgrund;
    protected  GameThread gameThread;
    protected int width;
    protected int height;
    protected  GameScene initSceene;
    public List<Sprite> campoBattalla;
    public Context cnt;
    public View v;

    // Lista casillas
    // Jugador
    // Escena
    //


    public SuperficieJuego(Context context, int width, int height, View v) {
        super(context);

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);



        this.width = width;
        this.height = height;

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            this.height += resources.getDimensionPixelSize(resourceId);
        }
        this.cnt = context;
        this.v = v;

    }

    public void makeInitSceene() {
        calcularSuperficieJuego();
    }

    public void calcularSuperficieJuego() {
        // cartasAbajo
        // campoBattalla
        // tamañoCasilla
        // tamañoCarta
        //
        int cardsDisplayerHeight,
            battlefieldWidth,
            battlefieldHeight;
        int tilesNumX = 6;
        int tilesNumY = 4;
        int tileInitialSize = 400;
        int initialBattlefieldPosY = 200;
        int initialBattlefieldPosX = 100;

        cardsDisplayerHeight = 400;
        int sitioDisponible = this.height - cardsDisplayerHeight - initialBattlefieldPosY;
        int sitioX = this.width - initialBattlefieldPosX;
        battlefieldWidth = tilesNumX * tileInitialSize;
        battlefieldHeight = tilesNumY * tileInitialSize * 2 + 1;
        while(battlefieldWidth > sitioX || battlefieldHeight > sitioDisponible) {
            tileInitialSize--;
            battlefieldWidth = tilesNumX * tileInitialSize;
            battlefieldHeight = tilesNumY * tileInitialSize * 2;
        }

        initialBattlefieldPosX = (this.width - battlefieldWidth) / 2;

        campoBattalla = new ArrayList<>();

        Bitmap tilea = BitmapFactory.decodeResource(this.getResources(),R.drawable.tilea);
        Bitmap tileb = BitmapFactory.decodeResource(this.getResources(),R.drawable.tileb);


        tilea = Bitmap.createScaledBitmap(tilea, tileInitialSize, tileInitialSize, false);
        tileb = Bitmap.createScaledBitmap(tileb, tileInitialSize, tileInitialSize, false);

        int posX = initialBattlefieldPosX;
        int posY = initialBattlefieldPosY;

        for(int i = 0; i < tilesNumY; i++) {
            for(int n = 0; n < tilesNumX; n++) {
                if(i % 2 == 0) {
                    if(n % 2 == 0) {
                        campoBattalla.add(new GameBackground(this, tilea, posX, posY, tileInitialSize, tileInitialSize));
                    }else {
                        campoBattalla.add(new GameBackground(this, tileb, posX, posY, tileInitialSize, tileInitialSize));
                    }
                }else {
                    if(n % 2 != 0) {
                        campoBattalla.add(new GameBackground(this, tilea, posX, posY, tileInitialSize, tileInitialSize));
                    }else {
                        campoBattalla.add(new GameBackground(this, tileb, posX, posY, tileInitialSize, tileInitialSize));
                    }
                }
                posX += tileInitialSize;

            }
            posY += tileInitialSize;
            posX = initialBattlefieldPosX;
        }

        posY += tileInitialSize;

        posX = initialBattlefieldPosX;
        for(int i = 0; i < tilesNumY; i++) {
            for(int n = 0; n < tilesNumX; n++) {
                if(i % 2 ==  0 || n % 2 != 0) {
                    campoBattalla.add(new GameBackground(this, tilea, posX, posY, tileInitialSize, tileInitialSize));
                }else {
                    campoBattalla.add(new GameBackground(this, tileb, posX, posY, tileInitialSize, tileInitialSize));
                }
                posX += tileInitialSize;

            }
            posY += tileInitialSize;
            posX = initialBattlefieldPosX;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSystemUI();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int)event.getX();
            int y = (int)event.getY();

            Toast.makeText(this.cnt, "Click done in " + x + "x" + y, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    //Funcion que actualiza el estado de los elementos de la superfice
    public void  update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.backgrund.draw(canvas);
        for(Sprite sp : campoBattalla) {
            GameBackground itm = (GameBackground) sp;
            itm.draw(canvas);
        }
        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawRect(0, this.height - 1, 200, this.height, p);
    }

    //Funcion setup, se ejecuta al principio del juego y es donde se inicializan todas las cosas
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.flat_night_4_bg);
        //chibiBitmap1 = Bitmap.createScaledBitmap(chibiBitmap1, 0, this.height, false);
        this.backgrund = new GameBackground(this, null, 0,0, this.width, this.height);

        makeInitSceene();

        //makeScreenUIgoFuckAway();

        this.gameThread = new GameThread(this, surfaceHolder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    public void makeScreenUIgoFuckAway() {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        this.getRootView().setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getRootView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}