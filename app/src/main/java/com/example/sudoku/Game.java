package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Game extends AppCompatActivity implements ThreexThree.OnFragmentInteractionListener {
    private Board currentBoard;
    private Board startBoard;
    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private int selectedNumber = 1;
    private boolean checkBoxChecked = false;
    Button checkBoard;
    Spinner spinner;
    LinearLayout ll;
    CheckBox checkBox;
    int row;
    int column;
    int flag = 0;
    TextView beforeCallId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        int level = getIntent().getIntExtra("level", 0);
        ArrayList<Board> boards = readGameBoards(level);

        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());
        checkBoard = findViewById(R.id.checkBoard);
        checkBox = findViewById(R.id.checkBox);
        spinner = findViewById(R.id.spinner);
        ll = findViewById(R.id.ll);


        //implementacja pola w odpowiednie miejscec w planszy
        int cellGroupFragments[] = new int[]{R.id.cellGroup1, R.id.cellGroup2, R.id.cellGroup3, R.id.cellGroup4,
                R.id.cellGroup5, R.id.cellGroup6, R.id.cellGroup7, R.id.cellGroup8, R.id.cellGroup9};
        for (int i = 1; i < 10; i++) {
            ThreexThree thisCellGroupFragment = (ThreexThree) getSupportFragmentManager().findFragmentById(cellGroupFragments[i-1]);
            thisCellGroupFragment.setGroupId(i);
        }

        //wygeerowanie tablic zapisanych w raw
        ThreexThree tempCellGroupFragment;
        //rzedy
        for (int i = 0; i < 9; i++) {
            //kolumny
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (ThreexThree) getSupportFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    tempCellGroupFragment.setValue(groupPosition, currentValue);
                }
            }
        }
    }

    private ArrayList<Board> readGameBoards(int level) {
        ArrayList<Board> boards = new ArrayList<>();
        int fileId;
        if (level == 1) {
            fileId = R.raw.normal;
        } else if (level == 0) {
            fileId = R.raw.easy;
        } else {
            fileId = R.raw.hard;
        }

        //otwarcie planszy z raw
        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // czytanie wszystkich linii planszy
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = bufferedReader.readLine(); //przejscie do kolejnego wiersza
                }
                boards.add(board);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e("Game", e.getMessage());
        }

        return boards;
    }


    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    //sprawdza ustawione na sztywno warosci w danym polu, group to pole a cell to 1 kwadracik
    private boolean isStartPiece(int group, int cell) {
        int row = ((group-1)/3)*3 + (cell/3);
        int column = ((group-1)%3)*3 + ((cell)%3);
        return startBoard.getValue(row, column) != 0;
    }

    //sprawdza wszystkie komorki sa uzupelnione
    private boolean checkAllGroups() {
        int cellGroupFragments[] = new int[]{R.id.cellGroup1, R.id.cellGroup2, R.id.cellGroup3, R.id.cellGroup4,
                R.id.cellGroup5, R.id.cellGroup6, R.id.cellGroup7, R.id.cellGroup8, R.id.cellGroup9};
        for (int i = 0; i < 9; i++) {
            ThreexThree thisCellGroupFragment = (ThreexThree) getSupportFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }


    public void checkBoard(View view) {
        currentBoard.isBoardCorrect();
        if(checkAllGroups() && currentBoard.isBoardCorrect()) {
            Toast.makeText(this, "Board is correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Board is not correct", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFragmentInteraction(int groupId, int cellId, View view) {
        clickedGroup = groupId;
        clickedCell = (TextView) view;
        clickedCellId = cellId;

        //ustawianie pustego pola za kazdym razem (usuwanie probably)
        if(beforeCallId != clickedCell && beforeCallId != null && flag == 0)
            beforeCallId.setBackground(getResources().getDrawable(R.drawable.cell));

        beforeCallId = clickedCell;
        flag =0;

        if (!isStartPiece(groupId, cellId)) {
            spinner.setVisibility(View.VISIBLE);
            ll.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
            //pobieranie wspolrzednych kwadraciku
            row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
            column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

            //podświetlanie danego pola na szaro
            clickedCell.setBackground(getResources().getDrawable(R.drawable.cell_actuall));
            initializeSpinner();
        }else{
            spinner.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "can not change this place", Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseNumber(View view) {
        clickedCell.setText(String.valueOf(selectedNumber));
        currentBoard.setValue(row, column, selectedNumber);
        flag = 0;

        if(checkBoxChecked){
            clickedCell.setBackground(getResources().getDrawable(R.drawable.cell_unsure));
            flag = 1;
        } else {
            clickedCell.setBackground(getResources().getDrawable(R.drawable.cell));
        }

        if (currentBoard.isBoardFull()) {
            checkBoard.setVisibility(View.VISIBLE);
        } else {
            checkBoard.setVisibility(View.INVISIBLE);
        }
    }

    public void checkBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        checkBoxChecked = view.getId() == R.id.checkBox && checked;
    }


    private void initializeSpinner() {
        final Integer numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbers);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNumber = numbers[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void remove(View view) {
        clickedCell.setText("");
        clickedCell.setBackground(getResources().getDrawable(R.drawable.cell));
        currentBoard.setValue(row, column, 0);
        checkBoard.setVisibility(View.INVISIBLE);
    }
}
