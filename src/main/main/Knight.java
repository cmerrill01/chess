package main;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Knight implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.KNIGHT;

    public Knight(ChessGame.TeamColor teamColor) {
        team = teamColor;
    }

    @Override
    public String toString() {
        return (team == ChessGame.TeamColor.WHITE ? "N" : "n");
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
        // create a set to hold possible moves
        Collection<ChessPosition> possiblePositions = new HashSet<>();

        // add all possible knight moves to the set
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() - 2));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn() - 1));
        possiblePositions.add(new ChessPositionImpl(myPosition.getRow() + 2, myPosition.getColumn() + 1));

        // create a separate set to store valid moves
        Collection<ChessMove> validMoves = new HashSet<>();

        // check each move to ensure it is valid
        for (ChessPosition toPosition : possiblePositions) {
            // if the end position of a move is off the board, it is not valid
            boolean valid = toPosition.onBoard(board);
            if (valid) {
                ChessMoveImpl move = new ChessMoveImpl(myPosition, toPosition);
                // if the end position of a move is occupied by a non-enemy piece, it is not valid
                if (board.getPiece(move.getEndPosition()) != null) {
                    if (!move.isCapture(board)) valid = false;
                }
                if (valid) validMoves.add(move);
            }
        }

        return validMoves;
    }
}
