package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class King implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.KING;

    public King(ChessGame.TeamColor teamColor) {
        team = teamColor;
    }

    @Override
    public String toString() {
        return (team == ChessGame.TeamColor.WHITE ? "K" : "k");
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return team;
    }

    @Override
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        // for every position one or fewer spaces away from the current position,
        for (int i = myPosition.getRow() - 1; i <= myPosition.getRow() + 1; i++) {
            for (int j = myPosition.getColumn() - 1; j <= myPosition.getColumn() + 1; j++) {
                // if we are not at the start position,
                if (!(i == myPosition.getRow() && j == myPosition.getColumn())) {
                    ChessPositionImpl toPosition = new ChessPositionImpl(i, j);
                    // if the current position is on the board,
                    if (toPosition.onBoard(board)) {
                        // create a move from the start position to the current position
                        ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
                        // if the current position is occupied,
                        if (toPosition.occupied(board)) {
                            // if the current move is a capture,
                            if (move.isCapture(board)) {
                                // add the move to the list of valid moves
                                validMoves.add(move);
                            }
                        } else {
                            // else, add the move to the list of valid moves
                            validMoves.add(move);
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        King king = (King) o;
        return team == king.team && type == king.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }
}
