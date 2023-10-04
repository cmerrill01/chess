package main;

import chess.ChessBoard;

public class Main {
    public static void main(String[] args) {
        ChessBoard myChessBoard = new ChessBoardImpl();
        myChessBoard.resetBoard();
        System.out.print(myChessBoard);
    }
}