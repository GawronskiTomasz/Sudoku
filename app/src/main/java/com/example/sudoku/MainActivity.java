package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.startGame);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //przejscie do aktywosci game
                Intent intent = new Intent(MainActivity.this, Game.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });
    }

    //wybór poziomu
    public void chooseLevel(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if(checked) {
            switch (view.getId()) {
                case R.id.easy:
                    level = 0;
                    break;
                case R.id.normal:
                    level = 1;
                    break;
                case R.id.hard:
                    level = 2;
                    break;
            }
        }
    }

    //start nowej gry
    public void start(View view) {
        Intent intent = new Intent("com.example.sudoku.Game");
        intent.putExtra("level", level);
        startActivity(intent);
    }
}
