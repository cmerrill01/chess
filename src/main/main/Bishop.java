package main;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class Bishop implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.BISHOP;

    public Bishop(ChessGame.TeamColor teamColor) {
        team = teamColor;
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
        // decrement both the row and column
        toPosition.decrementRow();
        toPosition.decrementColumn();
        // while the new Position is on the board,
        while (toPosition.onBoard(board) && !positionOccupied(board, validMoves, myPosition, toPosition)) {
            // decrement both the row and column by one
            toPosition.decrementRow();
            toPosition.decrementColumn();
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // decrement the row and increment the column
        toPosition.decrementRow();
        toPosition.incrementColumn();
        // while the new Position is on the board,
        while (toPosition.onBoard(board) && !positionOccupied(board, validMoves, myPosition, toPosition)) {
            // decrement the row and increment the column
            toPosition.decrementRow();
            toPosition.incrementColumn();
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // increment the row and increment the column
        toPosition.incrementRow();
        toPosition.incrementColumn();
        // while the new Position is on the board,
        while (toPosition.onBoard(board) && !positionOccupied(board, validMoves, myPosition, toPosition)) {
            // increment the row and increment the column
            toPosition.incrementRow();
            toPosition.incrementColumn();
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // increment the row and decrement the column
        toPosition.incrementRow();
        toPosition.decrementColumn();
        // while the new position is on the board and the position is not occupied
        while (toPosition.onBoard(board) && !positionOccupied(board, validMoves, myPosition, toPosition)) {
            // increment the row and decrement the column
            toPosition.incrementRow();
            toPosition.decrementColumn();
        }

        return validMoves;
    }

    private boolean positionOccupied(ChessBoard board,
                              Collection<ChessMove> validMoves,
                              ChessPosition startPosition,
                              ChessPosition toPosition
                              ) {
        boolean occupied = false;
        // if there is a piece at this position
        if (board.getPiece(toPosition) != null) {
            // if the piece is not the same color as this piece,
            if (board.getPiece(toPosition).getTeamColor() != this.team) {
                // create a new move from the starting position to this position
                ChessMove validMove = new ChessMoveImpl(startPosition, toPosition);
                // add this move to the collection
                validMoves.add(validMove);
            }
            // indicate that the space is occupied
            occupied = true;
        } else {
            // create a new move from the starting position to this position
            ChessMove validMove = new ChessMoveImpl(startPosition, toPosition);
            // add this move to the collection
            validMoves.add(validMove);
        }
        return occupied;
    }

}
