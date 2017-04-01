package com.example.lakshay.minesweeper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity ;
import android.os.Bundle;
import java.util.Random;


import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    TableLayout tableLayout;
    private static int NoofRows=6;
    private static int NoofCols=7;
    private static int NoofMines=10;
    private TextView scoretextView;
    private Block[][] blocks;
    private static final int[][] NEIGHBOUR_COORDS = {
            {1, 0}, {1, 1}, {1, -1}, {0, 1}, {0, -1}, {-1, 0}, {-1, 1}, {-1, -1}
    };


    private int[][] board;
    private int score = 0;
    private int size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        scoretextView = (TextView) findViewById(R.id.scoretextView);
        setUpBoard();
        initGame();


    }

    private void initGame() {
        score = 0;
        for (int i = 0; i < NoofRows; i++) {
            for (int j = 0; j < NoofCols; j++) {
                board[i][j] = 0;
            }
        }
        setRandomMines();
        refreshBoard();
    }


    private void setRandomMines() {

        //Set a specific no of mines
        int minesCount = 0;
        Random random = new Random();
        while (minesCount < NoofMines) {
            int randomInt = random.nextInt(NoofRows * NoofCols);
            int row = randomInt / NoofCols;
            int col = randomInt % NoofCols;
            if (board[row][col] != -1) {
                board[row][col] = -1;

                increaseNeighbourValues(row, col);//to increase value of neighbouring tiles by one.

                minesCount++;
            }
        }

    }

    private void increaseNeighbourValues(int row, int col) {
        for (int i = 0; i < NEIGHBOUR_COORDS.length; i++) {
            int[] neighbourCoords = NEIGHBOUR_COORDS[i];
            int neighbourRow = row + neighbourCoords[0];
            int neighbourCol = col + neighbourCoords[1];
            //Increase the value if neighbour sqaure is inside board and is not a mine
            if (isInBounds(neighbourRow, neighbourCol) && board[neighbourRow][neighbourCol] != -1) {
                board[neighbourRow][neighbourCol]++;
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        //Check if it's inside the board
        return row >= 0 && row < NoofRows && col >= 0 && col < NoofCols;
    }

    private void refreshBoard() {
        //set value for each square
        for (int i = 0; i < NoofRows; i++) {
            for (int j = 0; j < NoofCols; j++) {
                Block block = blocks[i][j];
                block.setup(i, j, board[i][j]);
            }
        }
    }


    private void setUpBoard() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int size = width / NoofCols;

        blocks = new Block[NoofRows][NoofCols];
        board = new int[NoofRows][NoofCols];
        for (int i = 0; i < NoofRows; i++) {
            TableRow tableRow = new TableRow(MainActivity.this);


            for (int j = 0; j < NoofCols; j++) {
                Block block = new Block(MainActivity.this, size);
                blocks[i][j] = block;
                block.setOnClickListener(MainActivity.this);
                block.setOnLongClickListener(MainActivity.this);
                tableRow.addView(block);


            }

            tableLayout.addView(tableRow);
        }


    }

    @Override
    public void onClick(View v) {
        Block block = (Block) v;
        if (!block.isRevealed() && !block.isFlagged()) {
            block.reveal();
            if (block.isMine()) {
                Toast.makeText(this, "Haar gaya Bc", Toast.LENGTH_LONG).show();
                revealAll();

            } else {
                score++;
                if (block.isEmpty()) {
                    revealNeighbours(block);

                }
            }
            updateScore();
            checkIfGameCompleted();
        }


    }

    private void checkIfGameCompleted() {

        //if the no of unrevealed tiles is equal to no of mines, then the user wins
        if (NoofMines == NoofCols * NoofRows - score) {
            Toast.makeText(this, "You win", Toast.LENGTH_LONG).show();
            revealAll();

        }
    }


    private void revealNeighbours(Block block) {
        int row = block.getRow();
        int col = block.getColumn();
        for (int i = 0; i < NEIGHBOUR_COORDS.length; i++) {
            int[] neighbourCoords = NEIGHBOUR_COORDS[i];
            int neighbourRow = row + neighbourCoords[0];
            int neighbourCol = col + neighbourCoords[1];
            if (isInBounds(neighbourRow, neighbourCol)) {
                Block neighbourblock= blocks[neighbourRow][neighbourCol];
                if (!neighbourblock.isRevealed()) {
                    neighbourblock.reveal();
                    score++;
                    if (neighbourblock.isEmpty()) {
                        revealNeighbours(neighbourblock);
                    }
                }
                board[neighbourRow][neighbourCol]++;
            }
        }
    }

    private void revealAll() {
        for (int i = 0; i < NoofRows; i++) {
            for (int j = 0; j < NoofCols; j++) {
                Block block= blocks[i][j];
                if (!block.isRevealed()) {
                    block.reveal();
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //Toggle flag
        Block block = (Block) v;
        block.setFlag(!block.isFlagged());
        return true;
    }
    private void updateScore() {
        scoretextView.setText("Score: " + score);
    }


}