import chess.ChessPosition;

public class ChessPositionImpl implements ChessPosition {

    private int row;
    private int col;

    public ChessPositionImpl(int startRow, int startCol) {
        row = startRow;
        col = startCol;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return col;
    }
}
