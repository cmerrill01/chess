import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardImpl implements ChessBoard {

    private ChessPiece[][] board;
    private final int numRows = 8;
    private final int numCols = 8;

    public ChessBoardImpl() {
        board = new ChessPiece[numRows][numCols];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return null;
    }

    @Override
    public void resetBoard() {

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
