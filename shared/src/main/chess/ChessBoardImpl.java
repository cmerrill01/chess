package chess;

import java.util.Arrays;
import java.util.Objects;

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
    public void removePiece(ChessPosition position) {
        board[8 - position.getRow()][position.getColumn() - 1] = null;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[8 - position.getRow()][position.getColumn() - 1];
    }

    @Override
    public void movePiece(ChessMove move) {
        if (move.getPromotionPiece() != null) {
            ChessGame.TeamColor pieceTeam = board[8 - move.getStartPosition().getRow()][move.getStartPosition().getColumn() - 1].getTeamColor();
            board[8 - move.getStartPosition().getRow()][move.getStartPosition().getColumn() - 1] = null;
            switch (move.getPromotionPiece()) {
                case BISHOP -> board[8 - move.getEndPosition().getRow()][move.getEndPosition().getColumn() - 1] = new Bishop(pieceTeam);
                case KNIGHT -> board[8 - move.getEndPosition().getRow()][move.getEndPosition().getColumn() - 1] = new Knight(pieceTeam);
                case QUEEN -> board[8 - move.getEndPosition().getRow()][move.getEndPosition().getColumn() - 1] = new Queen(pieceTeam);
                case ROOK -> board[8 - move.getEndPosition().getRow()][move.getEndPosition().getColumn() - 1] = new Rook(pieceTeam);
            }
        } else {
            board[8 - move.getEndPosition().getRow()][move.getEndPosition().getColumn() - 1] =
                    board[8 - move.getStartPosition().getRow()][move.getStartPosition().getColumn() - 1];
            board[8 - move.getStartPosition().getRow()][move.getStartPosition().getColumn() - 1] = null;
        }
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
        for (ChessPiece[] rowPieces : board) {
            // for each column in the row
            for (ChessPiece chessPiece : rowPieces) {
                // print "|"
                output.append("|");
                // if there is a piece in this position
                if (chessPiece != null) {
                    // print the string representation of the piece
                    output.append(chessPiece);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoardImpl that = (ChessBoardImpl) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(numRows, numCols);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}