package main;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

public class Main {
    public static void main(String[] args) {
        ChessBoard myChessBoard = new ChessBoardImpl();
        myChessBoard.resetBoard();
        System.out.print(myChessBoard);
        ChessPosition startPosition = new ChessPositionImpl(2, 4);
        ChessPosition endPosition = new ChessPositionImpl(4, 4);
        ChessMove move = new ChessMoveImpl(startPosition, endPosition);
        myChessBoard.movePiece(move);
        System.out.print(myChessBoard);
    }
}