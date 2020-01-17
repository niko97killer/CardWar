package com.example.appprototype;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardDisplayer {

    /*
    * #ee8572
    * #35495e
    * #347474
    * #63b7af
    * */

    public Point position;
    public Point dimensions;
    public int cardsCount;
    // cards back layer
    // cards array
    public Point cardDimensions;
    public String backColor = "#35495e";
    public List<Card> cartas;
    public Card selectedCard = null;
    public int separation;
    public Context cnt;


    public CardDisplayer(int x, int y, int w, int h, Context cnt) {
        this.position = new Point(x, y);
        this.dimensions = new Point(w, h);

        this.cnt = cnt;

        Bitmap tilea = BitmapFactory.decodeResource(this.cnt.getResources(), R.drawable.card1);
        this.cartas = new ArrayList<>();

        this.cardsCount = 12;


        int separation = 180 + (((this.dimensions.x - 80) - ((this.cardsCount) * 180)) / this.cardsCount);
        int initpos = ((this.dimensions.x) - (separation * cardsCount)) / 2;
        separation -= (180 - separation) / this.cardsCount;
        this.separation = separation;
        int bajator = 0;
        for(int i = 0; i < this.cardsCount; i++) {
            this.cartas.add(new Card(initpos, this.position.y + 70 - bajator, 180, 240, "#9999ff"));
                    ///new GameBackground(null, null, initpos, this.position.y + 70 - bajator, 180, 240));
            initpos += separation;
            if(i > this.cardsCount / 2 -1) {
                bajator -= 10;
            }else {
                bajator += 10;
            }
        }
    }

    public void calculateCardsPosition() {

        this.cardsCount = this.cartas.size();

        if(this.cartas.size() == 0) {
            return;
        }

        int separation = 180 + (((this.dimensions.x - 80) - ((this.cardsCount) * 180)) / this.cardsCount);
        int initpos = ((this.dimensions.x) - (separation * cardsCount)) / 2;
        separation -= (180 - separation) / this.cardsCount;
        this.separation = separation;
        int bajator = 0;
        for(int i = 0; i < this.cardsCount; i++) {
            Card itm = this.cartas.get(i);//add(new GameBackground(null, tilea, initpos, this.position.y + 70 - bajator, 180, 240));
            itm.x = initpos;
            itm.y = this.position.y + 70 - bajator;
            initpos += separation;
            if(i > this.cardsCount / 2 -1) {
                bajator -= 10;
            }else {
                bajator += 10;
            }
        }
    }

    public void calculateCardOrientation() {

    }

    public void draw(Canvas cnv) {
        Paint pnt = new Paint();
        pnt.setColor(Color.parseColor(this.backColor));

        int w = this.position.x + this.dimensions.x;
        int h = this.position.y + this.dimensions.y;
        cnv.drawRoundRect(this.position.x, this.position.y, w, h, 10, 10, pnt);

        if(this.cartas.size() > 0) {

            int incliCounter = 20 / this.cartas.size();
            int initialCount = 10;

            Card auxSelection = null;

            for(int i = this.cartas.size() - 1; i >= 0; i--) {
                Card itm = this.cartas.get(i);
                if(itm.selected) {
                    auxSelection = itm;
                }else {
                    cnv.save();
                    //cnv.rotate(initialCount, itm.x + 90, itm.y + 120);
                    initialCount -= incliCounter;
                    itm.draw(cnv);
                    cnv.restore();
                }
            }

            if(auxSelection != null) {
                cnv.save();
                cnv.translate(0, - 100);
                auxSelection.draw(cnv);
                cnv.restore();
            }
        }


        //restore canvas
    }

    public void checkCardSelection(int x, int y) {
        for(int i = 0; i < this.cartas.size(); i++) {
            this.cartas.get(i).selected = false;
        }
        for(int i = 0; i < this.cartas.size(); i++) {
            Card itm = this.cartas.get(i);
            int xPos = itm.x + (180 -  this.separation);
            int wSize = this.separation;
            if(i == 0) {
                xPos = itm.x;
                wSize = 180;
            }
            if(x > xPos && x < xPos + wSize && y > itm.y && x < itm.y + itm.h) {
                //this.cartas.remove(i);
                //this.selectedCard = cartas.get(i);
                this.cartas.get(i).selected = true;
                //Toast.makeText(this.cnt, "Card " + i + " selected", Toast.LENGTH_LONG).show();
                //calculateCardsPosition();
                return;
            }else {
                //this.selectedCard = null;
            }
        }
    }

    public void update() {

    }
}
