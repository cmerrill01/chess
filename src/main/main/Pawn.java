package main;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Pawn implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.PAWN;

    public Pawn(ChessGame.TeamColor teamColor) {
        team = teamColor;
    }

    @Override
    public String toString() {
        return (team == ChessGame.TeamColor.WHITE ? "P" : "p");
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
        int direction = (team == ChessGame.TeamColor.WHITE ? 1 : -1);
        boolean firstMove = ((team == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                team == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7);
        // prepare the information to enable promotions
        int promotionRow = (team == ChessGame.TeamColor.WHITE ? 8 : 1);
        Collection<PieceType> promotionPieces = new HashSet<>();
        promotionPieces.add(PieceType.BISHOP);
        promotionPieces.add(PieceType.KNIGHT);
        promotionPieces.add(PieceType.QUEEN);
        promotionPieces.add(PieceType.ROOK);

        // find the position directly forward from the starting position
        ChessPositionImpl forward = new ChessPositionImpl(myPosition.getRow() + direction, myPosition.getColumn());
        // if this position is on the board,
        if (forward.onBoard(board)) {
            // if it's not occupied
            if (!forward.occupied(board)) {
                // if the move takes it to the promotion row,
                if (forward.getRow() == promotionRow) {
                    // for each possible promotion piece,
                    for (PieceType promotionPiece : promotionPieces) {
                        // add a move from the starting position to here, including the promotion
                        validMoves.add(new ChessMoveImpl(myPosition, forward, promotionPiece));
                    }
                } else {
                    // add a move from the starting position to here
                    validMoves.add(new ChessMoveImpl(myPosition, forward));
                    // if the pawn is in its starting position
                    if (firstMove) {
                        // find the position two spaces forward from the starting position
                        ChessPositionImpl twoForward = new ChessPositionImpl(forward.getRow() + direction, forward.getColumn());
                        // if this position is not occupied, add a move from the starting position to here
                        if (!twoForward.occupied(board)) validMoves.add(new ChessMoveImpl(myPosition, twoForward));
                    }
                }
            }
        }

        // create an array to hold the two potential capture positions
        ChessPositionImpl[] capturePositions = new ChessPositionImpl[2];
        capturePositions[0] = new ChessPositionImpl(myPosition.getRow() + direction, myPosition.getColumn() - 1);
        capturePositions[1] = new ChessPositionImpl(myPosition.getRow() + direction, myPosition.getColumn() + 1);

        for (ChessPositionImpl capturePosition : capturePositions) {
            // if the position is on the board,
            if (capturePosition.onBoard(board)) {
                // if it is occupied
                if (capturePosition.occupied(board)) {
                    // if the piece is an enemy piece,
                    if (board.getPiece(capturePosition).getTeamColor() != this.team) {
                        // if the move takes it to the promotion row,
                        if (capturePosition.getRow() == promotionRow) {
                            // for each possible promotion piece,
                            for (PieceType promotionPiece : promotionPieces) {
                                // add a move from the starting position to here, including the promotion
                                validMoves.add(new ChessMoveImpl(myPosition, capturePosition, promotionPiece));
                            }
                        } else {
                            // add the move to the set of valid moves
                            validMoves.add(new ChessMoveImpl(myPosition, capturePosition));
                        }
                    }
                }
            }
        }

        return validMoves;
    }
}
