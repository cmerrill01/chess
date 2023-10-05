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
        // set the current row and column equal to the start row and column
        // decrement the current row by two
        // decrement the current column by one


        return null;
    }
}
