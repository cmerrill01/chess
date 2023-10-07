package main;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardImpl implements ChessBoard {

    private final ChessPiece[][] board;
    private final int numRows = 8;
    private final int numCols = 8;

    public ChessBoardImpl() {
        board = new ChessPiece[numRows][numCols];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[8 - position.getRow()][position.getColumn() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[8 - position.getRow()][position.getColumn() - 1];
    }

    public int getMaxRow() {
        return numRows;
    }

    public int getMaxColumn() {
        return  numCols;
    }

    @Override
    public void resetBoard() {
        for (int i = 0; i < numCols; i++) {
            switch (i) {
                case 0, 7 -> {
                    board[0][i] = new Rook(ChessGame.TeamColor.BLACK);
                    board[7][i] = new Rook(ChessGame.TeamColor.WHITE);
                }
                case 1, 6 -> {
                    board[0][i] = new Knight(ChessGame.TeamColor.BLACK);
                    board[7][i] = new Knight(ChessGame.TeamColor.WHITE);
                }
                case 2, 5 -> {
                    board[0][i] = new Bishop(ChessGame.TeamColor.BLACK);
                    board[7][i] = new Bishop(ChessGame.TeamColor.WHITE);
                }
                case 3 -> {
                    board[0][i] = new Queen(ChessGame.TeamColor.BLACK);
                    board[7][i] = new Queen(ChessGame.TeamColor.WHITE);
                }
                case 4 -> {
                    board[0][i] = new King(ChessGame.TeamColor.BLACK);
                    board[7][i] = new King(ChessGame.TeamColor.WHITE);
                }
            }
            board[1][i] = new Pawn(ChessGame.TeamColor.BLACK);
            board[6][i] = new Pawn(ChessGame.TeamColor.WHITE);
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        // for each row of the chess board
        for (int row = 0; row < board.length; row++) {
            // for each column in the row
            for (int col = 0; col < board[row].length; col++) {
                // print "|"
                output.append("|");
                // if there is a piece in this position
                if (board[row][col] != null) {
                    // print the string representation of the piece
                    output.append(board[row][col]);
                } else {
                    // print " "
                    output.append(" ");
                }
            }
            // print "|" and a new line
            output.append("|\n");
        }
        return output.toString();
    }
}
