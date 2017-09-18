package com.javahelps.minesweeper;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static int WIDTH = 16;
    public static int HEIGHT = 10;
    public static int score = 0;
    public static int BOMBCLICKED = 0;
    static LinearLayout rootLayout;
    static MyButton button[][];
    static LinearLayout rowLayout[];
    static TextView textview;
    public String KEY_USERNAME = "score";
    ImageButton bomb, setting, smiley;

    public static MyButton[][] generate(MyButton[][] buttons, int bombNumber) {

        Random r = new Random();
        while (bombNumber > 0) {
            int x = r.nextInt(buttons.length);
            int y = r.nextInt(buttons[0].length);

            if (buttons[x][y].value != -1) {
                buttons[x][y].value = -1;
                bombNumber--;
            }
        }
        buttons = calculateNeighbour(buttons);
        return buttons;
    }

    private static MyButton[][] calculateNeighbour(MyButton[][] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j].value = getNeighbourNumber(buttons, i, j);
            }
        }
        return buttons;
    }

    private static int getNeighbourNumber(MyButton[][] buttons, int x, int y) {

        if (buttons[x][y].value == -1) {
            return -1;
        }
        int count = 0;

        if (isMineAt(buttons, x - 1, y - 1))
            count++;
        if (isMineAt(buttons, x, y - 1))
            count++;
        if (isMineAt(buttons, x + 1, y - 1))
            count++;
        if (isMineAt(buttons, x - 1, y))
            count++;
        if (isMineAt(buttons, x + 1, y))
            count++;
        if (isMineAt(buttons, x - 1, y + 1))
            count++;
        if (isMineAt(buttons, x, y + 1))
            count++;
        if (isMineAt(buttons, x + 1, y + 1))
            count++;

        return count;
    }

    private static boolean isMineAt(MyButton[][] grid, int x, int y) {

        if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length) {
            if (grid[x][y].value == -1)
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);
        textview = (TextView) findViewById(R.id.score);
        bomb = (ImageButton) findViewById(R.id.bomb);
        setting = (ImageButton) findViewById(R.id.setting);
        smiley = (ImageButton) findViewById(R.id.smile);
        setUpBoard();
    }

    public void onBombClick() {
        if (BOMBCLICKED == 0) {
            BOMBCLICKED = 1;
            bomb.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.flag));
        } else {
            BOMBCLICKED = 0;
            bomb.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bomb));
        }
    }

    public void newBoard(View view) {
        if (view.getId() == R.id.smile)
            setUpBoard();
        if (view.getId() == R.id.bomb)
            onBombClick();
    }

    public void setUpBoard() {
        score = 0;
        button = new MyButton[WIDTH][HEIGHT];
        rowLayout = new LinearLayout[WIDTH];
        rootLayout.removeAllViews();

        bomb.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bomb));
        textview.setText("0");
        for (int i = 0; i < WIDTH; i++) {
            rowLayout[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            params.setMargins(0, 0, 0, 0);
            rowLayout[i].setLayoutParams(params);
            rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            rootLayout.addView(rowLayout[i]);
        }


        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                button[i][j] = new MyButton(this);
                button[i][j].pos_x = i;
                button[i][j].pos_y = j;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                params.setMargins(0, 0, 0, 0);
                button[i][j].setLayoutParams(params);
                button[i][j].setOnClickListener(this);
                button[i][j].isFlag = false;
                button[i][j].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                rowLayout[i].addView(button[i][j]);
            }
        }
        generate(button, 25);
    }

    @Override
    public void onClick(View v) {
        MyButton button = (MyButton) v;

        if (BOMBCLICKED == 1) {
            if (button.isFlag == false) {
                button.isFlag = true;
                button.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.flag));
            } else {
                button.isFlag = false;
                button.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.normal_button));
            }
            return;
        }
        if (button.isFlag == true)
            return;

        if (button.value == -1) {
            gameOver();
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show();
            return;
        }

        if (button.value == 0) {
            button.setText("0");
            revealNeighbours(button.pos_x, button.pos_y);
            textview.setText(score + " ");
        }

        if (button.value > 0 && button.value < 9) {

            button.setText(button.value + "");
            score = score + button.value;
            textview.setText(score + "");
            button.setEnabled(false);
            button.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.revealed_button));

        }

    }

    private void gameOver() {
        for (int i = 0; i < button.length; i++) {
            for (int j = 0; j < button[0].length; j++) {
                if (button[i][j].value == -1) {
                    button[i][j].setBackgroundResource(R.drawable.bombb);
//                    button[i][j].setBackgroundColor(Color.RED);
                }
                button[i][j].setEnabled(false);
            }
        }
    }

    private void revealNeighbours(int a, int b) {

        //reveals neighbour
        int[] x = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] y = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            if ((a + x[i]) >= 0 && (b + y[i]) >= 0 && (a + x[i]) < button.length && (b + y[i]) < button[0].length) {
                if (button[a + x[i]][b + y[i]].isFlag == true)
                    continue;

                if (button[a + x[i]][b + y[i]].value == 0 && button[a + x[i]][b + y[i]].isEnabled()) {
                    button[a + x[i]][b + y[i]].setText("");
                    button[a + x[i]][b + y[i]].setEnabled(false);
                    revealNeighbours(a + x[i], b + y[i]);
                    button[a + x[i]][b + y[i]].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.revealed_button));
                } else if (button[a + x[i]][b + y[i]].isEnabled()) {
                    button[a + x[i]][b + y[i]].setText(button[a + x[i]][b + y[i]].value + "");
                    score = score + button[a + x[i]][b + y[i]].value;
                    button[a + x[i]][b + y[i]].setEnabled(false);
                    button[a + x[i]][b + y[i]].setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.revealed_button));

                }
            }
        }

    }
}
