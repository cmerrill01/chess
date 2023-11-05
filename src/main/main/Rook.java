package main;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Rook implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.ROOK;

    public Rook(ChessGame.TeamColor teamColor) {
        team = teamColor;
    }

    @Override
    public String toString() {
        return (team == ChessGame.TeamColor.WHITE ? "R" : "r");
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
        // Create a hashset of valid moves to return once populated
        Collection<ChessMove> validMoves = new HashSet<>();

        // Get the row and column of the starting position
        ChessPositionImpl toPosition = new ChessPositionImpl(myPosition);
        // decrement the row
        toPosition.decrementRow();
        // while this position is on the board
        while (toPosition.onBoard(board)) {
            // create a move from the starting position to this position
            ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
            // if this position is occupied,
            if (toPosition.occupied(board)) {
                // if it is occupied by an enemy piece
                if (move.isCapture(board)) {
                    // add the move to the set of valid moves
                    validMoves.add(move);
                }
                // break out of the loop
                break;
            } else {
                // otherwise, add the move to the set of valid moves
                validMoves.add(move);
                // and decrement the row
                toPosition.decrementRow();
            }
        }

        // Get the start position
        toPosition.setToPosition(myPosition);
        // increment the row
        toPosition.incrementRow();
        // while this position is on the board
        while (toPosition.onBoard(board)) {
            // create a move from the starting position to this position
            ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
            // if this position is occupied,
            if (toPosition.occupied(board)) {
                // if it is occupied by an enemy piece
                if (move.isCapture(board)) {
                    // add the move to the set of valid moves
                    validMoves.add(move);
                }
                // break out of the loop
                break;
            } else {
                // otherwise, add the move to the set of valid moves
                validMoves.add(move);
                // and increment the row
                toPosition.incrementRow();
            }
        }

        // get the start position
        toPosition.setToPosition(myPosition);
        // decrement the column
        toPosition.decrementColumn();
        // while this position is on the board
        while (toPosition.onBoard(board)) {
            // create a move from the starting position to this position
            ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
            // if this position is occupied,
            if (toPosition.occupied(board)) {
                // if it is occupied by an enemy piece
                if (move.isCapture(board)) {
                    // add the move to the set of valid moves
                    validMoves.add(move);
                }
                // break out of the loop
                break;
            } else {
                // otherwise, add the move to the set of valid moves
                validMoves.add(move);
                // and decrement the row
                toPosition.decrementColumn();
            }
        }

        // get the start position
        toPosition.setToPosition(myPosition);
        // increment the column
        toPosition.incrementColumn();
        // while this position is on the board
        while (toPosition.onBoard(board)) {
            // create a move from the starting position to this position
            ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
            // if this position is occupied,
            if (toPosition.occupied(board)) {
                // if it is occupied by an enemy piece
                if (move.isCapture(board)) {
                    // add the move to the set of valid moves
                    validMoves.add(move);
                }
                // break out of the loop
                break;
            } else {
                // otherwise, add the move to the set of valid moves
                validMoves.add(move);
                // and increment the column
                toPosition.incrementColumn();
            }
        }

        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rook rook = (Rook) o;
        return team == rook.team && type == rook.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }
}
