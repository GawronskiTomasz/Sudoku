package com.example.sudoku;

import java.util.ArrayList;

public class Board {
    private int[][] gameCells = new int[9][9];

    public Board() {
    }

    public void setValue(int row, int column, int value) {
        gameCells[row][column] = value;
    }

    public int[][] getGameCells() {
        return gameCells;
    }

    //wprowadzanie wartosci z tablicy (w ktorej sa zapisane watosci z txt) do planszy
    public void copyValues(int[][] newGameCells) {
        for (int i = 0; i < newGameCells.length; i++) {
            for (int j = 0; j < newGameCells[i].length; j++) {
                gameCells[i][j] = newGameCells[i][j];
            }
        }
    }

    public boolean isBoardFull() {
        for (int i = 0; i < gameCells.length; i++) {
            for (int j = 0; j < gameCells[i].length; j++) {
                if (gameCells[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //funkcja sprawdzaja poprawnosc wypelnionej planszy
    public boolean isBoardCorrect() {
        // sprawdza wiersz po wierszu
        for (int i = 0; i < gameCells.length; i++) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < gameCells[i].length; j++) {
                int number = gameCells[i][j];
                if (numbers.contains(number)) {
                    return false;
                } else {
                    numbers.add(number);
                }
            }
        }

        // sprawdza kolumne po polumnie
        for (int i = 0; i < gameCells.length; i++) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int j = 0; j < gameCells[i].length; j++) {
                int number = gameCells[j][i];
                if (numbers.contains(number)) {
                    return false;
                } else {
                    numbers.add(number);
                }
            }
        }
        // zwraca true jezeli wiersze i kolumny beda sie zgadzac
        return true;
    }

    public int getValue(int row, int column) {
        return gameCells[row][column];
    }

}
