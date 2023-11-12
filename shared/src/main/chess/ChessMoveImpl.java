package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;

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

    public boolean isCapture(ChessBoard board) {
        return (board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor());
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
            // check whether the promotion pieces are equal
            if (promotionPiecesEqual(other)) {
                return (this.startPosition.equals(other.getStartPosition()) &&
                        this.endPosition.equals(other.getEndPosition()));
            }
        }
        return false;
    }

    private boolean promotionPiecesEqual(ChessMove other) {
        boolean promotionPiecesEqual;
        // if this promotion piece is null
        if (this.promotionPiece == null) {
            // then if the other promotion piece is null, they're equal;
            // otherwise, they're not
            promotionPiecesEqual = other.getPromotionPiece() == null;
        } else {
            // if the promotion piece is not null,
            // then if the other is not null,
            if (other.getPromotionPiece() != null) {
                // check to see if they're the same
                promotionPiecesEqual = this.promotionPiece == other.getPromotionPiece();
                // if the other is null, return falls
            } else promotionPiecesEqual = false;
        }
        return promotionPiecesEqual;
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
