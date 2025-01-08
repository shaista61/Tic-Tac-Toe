package com.tictactoe;

import java.util.Random;
import java.util.Scanner;

class TicTacToe {
    static char[][] board;
    static int size;

    public TicTacToe(int boardSize) {
        size = boardSize;
        board = new char[size][size];
        initBoard();
    }

    void initBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    void display() {
        System.out.println("-".repeat(size * 4 + 1));
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            for (int j = 0; j < size; j++) {
                System.out.print(" " + board[i][j] + " |");
            }
            System.out.println();
            System.out.println("-".repeat(size * 4 + 1));
        }
    }

    static void placeMark(int row, int col, char mark) {
        if (row >= 0 && row < size && col >= 0 && col < size && board[row][col] == ' ') {
            board[row][col] = mark;
        } else {
            System.out.println("Invalid Position!!");
        }
    }

    static boolean checkWin(char mark) {
        // Check rows and columns
        for (int i = 0; i < size; i++) {
            if (checkLine(mark, i, 0, 0, 1) || checkLine(mark, 0, i, 1, 0)) {
                return true;
            }
        }
        // Check diagonals
        return checkLine(mark, 0, 0, 1, 1) || checkLine(mark, 0, size - 1, 1, -1);
    }

    static boolean checkLine(char mark, int startRow, int startCol, int rowIncrement, int colIncrement) {
        for (int i = 0; i < size; i++) {
            if (board[startRow + i * rowIncrement][startCol + i * colIncrement] != mark) {
                return false;
            }
        }
        return true;
    }

    static boolean checkDraw() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}

abstract class Player {
    String name;
    char mark;

    abstract void makeMove();

    boolean isValidMove(int row, int col) {
        return row >= 0 && row < TicTacToe.size && col >= 0 && col < TicTacToe.size && TicTacToe.board[row][col] == ' ';
    }
}

class HumanPlayer extends Player {
    public HumanPlayer(String name, char mark) {
        this.name = name;
        this.mark = mark;
    }

    void makeMove() {
        Scanner sc = new Scanner(System.in);
        int row, col;
        do {
            System.out.println(name + ", enter row and column (0 to " + (TicTacToe.size - 1) + "):");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter integers only.");
                sc.next();
            }
            row = sc.nextInt();
            col = sc.nextInt();
        } while (!isValidMove(row, col));
        TicTacToe.placeMark(row, col, mark);
    }
}

class AIPlayer extends Player {
    public AIPlayer(String name, char mark) {
        this.name = name;
        this.mark = mark;
    }

    void makeMove() {
        int row, col;
        Random r = new Random();
        do {
            row = r.nextInt(TicTacToe.size);
            col = r.nextInt(TicTacToe.size);
        } while (!isValidMove(row, col));
        TicTacToe.placeMark(row, col, mark);
        System.out.println(name + " placed at (" + row + ", " + col + ")");
    }
}

public class Launch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Tic Tac Toe!");
        System.out.println("Enter board size (default is 3):");
        int boardSize = sc.nextInt();
        if (boardSize < 3) {
            System.out.println("Invalid size! Setting board size to 3.");
            boardSize = 3;
        }

        TicTacToe tt = new TicTacToe(boardSize);
        System.out.println("Enter Player 1 name:");
        String player1Name = sc.next();
        HumanPlayer p1 = new HumanPlayer(player1Name, 'X');

        AIPlayer p2 = new AIPlayer("AI", 'O');
        Player currentPlayer = p1;

        while (true) {
            System.out.println(currentPlayer.name + "'s Turn");
            currentPlayer.makeMove();
            tt.display();

            if (TicTacToe.checkWin(currentPlayer.mark)) {
                System.out.println(currentPlayer.name + " has Won!");
                break;
            } else if (TicTacToe.checkDraw()) {
                System.out.println("Game is a Draw!");
                break;
            }

            currentPlayer = (currentPlayer == p1) ? p2 : p1;
        }

        System.out.println("Game Over!");
    }
}
