package com.example.tko_chess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.tko_chess.Tile;

public final class chessboard extends View {
    private static final String TAG = chessboard.class.getSimpleName();

    private static final int collumns = 8;
    private static final int rows = 8;

    private final Tile[][] mTiles;

    private int x0 = 0;
    private int y0 = 0;
    private int squareSize = 0;

    private boolean flipped = false;

    public chessboard(final Context context, final AttributeSet attrs){
        super(context, attrs);
        this.mTiles = new Tile[collumns][rows];

        setFocusable(true);

        buildTiles();
    }

    private void buildTiles() {
        for(int i = 0; i < collumns; i++){
            for(int j = 0; j < rows; j++){
                mTiles[i][j] = new Tile(i,j);
            }
        }
    }

    @Override
    protected void onDraw(final Canvas canvas){
        final int width = getWidth();
        final int height = getHeight();

        this.squareSize = Math.min(getWidth(width), getHeight(height));

        for(int i = 0; i < collumns; i++){
            for(int j = 0; j < rows; j++){
                final int xCoordinate = getXCoordinate(i);
                final int yCoordinate = getYCoordinate(j);

                final Rect tileRect = new Rect(xCoordinate, yCoordinate, (xCoordinate + squareSize), (yCoordinate + squareSize));
                mTiles[i][j].setTileRect(tileRect);
                mTiles[i][j].draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event){
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        Tile tile;
        for(int i = 0; i < collumns; i++){
            for(int j = 0; j < rows; j++){
                tile = mTiles[i][j];
                if(tile.isTouched(x,y))
                    tile.handleTouch();
            }
        }
        return true;
    }

    private int getYCoordinate(final int Y) { return y0 + squareSize * (flipped ? Y : 7 - Y);}

    private int getXCoordinate(final int X) { return x0 + squareSize * (flipped ? 7 - X : X);}

    private int getHeight(int height) {
        return height / 8;
    }

    private int getWidth(int width) {
        return width / 8;
    }

    private void computeOrigins(final int width, final int height){
        this.x0 = (width - squareSize * 8) / 2;
        this.y0 = (height - squareSize * 8) / 2;
    }

}
