package main;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Queen implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.QUEEN;

    public Queen(ChessGame.TeamColor teamColor) {
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
                // and decrement both the row and column
                toPosition.decrementRow();
                toPosition.decrementColumn();
            }
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // increment both the row and column
        toPosition.incrementRow();
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
                // and increment both the row and column
                toPosition.incrementRow();
                toPosition.incrementColumn();
            }
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // increment the row and decrement the column
        toPosition.incrementRow();
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
                // and increment the row and decrement the column
                toPosition.incrementRow();
                toPosition.decrementColumn();
            }
        }

        // Get the row and column of the starting position
        toPosition.setToPosition(myPosition);
        // decrement the row and increment the column
        toPosition.decrementRow();
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
                // and decrement the row and increment column
                toPosition.decrementRow();
                toPosition.incrementColumn();
            }
        }

        // get the start position
        toPosition.setToPosition(myPosition);
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
}
