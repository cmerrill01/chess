package main;

import chess.*;

import java.util.Collection;

public class Pawn implements ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type = PieceType.PAWN;

    public Pawn(ChessGame.TeamColor teamColor) {
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
        return null;
    }
}
