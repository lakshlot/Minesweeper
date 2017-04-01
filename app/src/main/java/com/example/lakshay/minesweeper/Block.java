package com.example.lakshay.minesweeper;

import android.content.Context;

import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.widget.Button;


public class Block extends Button {
    private final int Mine = -1;

    private boolean clickable = true;
    private boolean flagged = false;
    private boolean revealed = false;
    private int noOfMinesAround = 0;
    private int row;
    private int column;
    private int size;


    public Block(Context context, int size) {
        super(context);
        this.size = size;
    }

    public void setup(int row, int col, int value) {
        this.row = row;
        this.column = col;
        this.noOfMinesAround = value;
        flagged = false;
        revealed = false;
        clickable = true;

    }

    public void reveal() {
        revealed = true;
        flagged = false;


    }


    public void setFlag(boolean flag) {
        if (!revealed) {
            flagged = flag;

        }
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isMine() {
        return noOfMinesAround == Mine;
    }

    public boolean isEmpty() {
        return noOfMinesAround == 0;
    }

    public boolean isClickable() {
        if (!revealed) {
            clickable = true;
            return clickable;
        } else {
            return clickable = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (revealed) {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.darkgrey));
            if (noOfMinesAround == Mine) {
                setText("*");
            } else if (noOfMinesAround == 0) {
                setText(" ");
            } else {
                setText(String.valueOf(noOfMinesAround));
            }


        } else {
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.lightgrey));
            if (flagged) {
                setText("!");
            } else {
                setText(" ");
            }
        }
    }
}



