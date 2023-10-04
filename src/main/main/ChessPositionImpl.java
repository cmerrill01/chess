package main;

import chess.ChessBoard;
import chess.ChessPosition;
import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {

    private int row;
    private int col;

    public ChessPositionImpl(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPositionImpl(ChessPosition other) {
        this.row = other.getRow();
        this.col = other.getColumn();
    }

    @Override
    public int getRow() {
        return row;
    }

    public void incrementRow() { row++; }

    public void decrementRow() { row--; }


    @Override
    public int getColumn() {
        return col;
    }

    public void incrementColumn() { col++; }

    public void decrementColumn() { col--; }

    public void setToPosition(ChessPosition other) {
        this.row = other.getRow();
        this.col = other.getColumn();
    }

    @Override
    public boolean onBoard(ChessBoard board) {
        return (row > 0 && row <= board.getMaxRow() && col > 0 && col <= board.getMaxColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj instanceof ChessPositionImpl) {
            ChessPosition other = (ChessPositionImpl) obj;
            return (this.row == other.getRow() && this.col == other.getColumn());
        }
        return false;
    }
}
