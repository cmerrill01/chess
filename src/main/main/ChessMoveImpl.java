package main;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;

    private ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition startPos, ChessPosition endPos) {
        startPosition = new ChessPositionImpl(startPos.getRow(), startPos.getColumn());
        endPosition = new ChessPositionImpl(endPos.getRow(), endPos.getColumn());
    }

    public ChessMoveImpl(ChessPosition startPos, ChessPosition endPos, ChessPiece.PieceType promoteTo) {
        startPosition = new ChessPositionImpl(startPos.getRow(), startPos.getColumn());
        endPosition = new ChessPositionImpl(endPos.getRow(), endPos.getColumn());
        promotionPiece = promoteTo;
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj instanceof ChessMoveImpl) {
            ChessMove other = (ChessMoveImpl) obj;
            return (this.startPosition == other.getStartPosition() &&
                    this.endPosition == other.getEndPosition() &&
                    this.promotionPiece == other.getPromotionPiece());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        char startRow = (char) (startPosition.getRow() - 1 + 'a');
        output.append(startRow);
        int startCol = startPosition.getColumn();
        output.append(startCol);
        char endRow = (char) (endPosition.getRow() - 1 + 'a');
        output.append(endRow);
        int endCol = endPosition.getColumn();
        output.append(endCol);
        return output.toString();
    }
}
