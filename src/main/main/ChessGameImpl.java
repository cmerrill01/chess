package main;

import chess.*;

import java.util.Collection;

public class ChessGameImpl implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGameImpl() {
        teamTurn = TeamColor.WHITE;
    }

    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) != null) {
            return board.getPiece(startPosition).pieceMoves(board, startPosition);
        }
        else return null;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        // start by assuming the king is not in check
        boolean inCheck = false;
        // iterate over all the positions on the board
        for (int i = 1; i <= board.getMaxRow(); i++) {
            for (int j = 1; j <= board.getMaxColumn(); j++) {
                ChessPosition position = new ChessPositionImpl(i, j);
                // if there is a piece of the opposite team at the current position,
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() != teamColor) {
                    // iterate through each move for that piece
                    for (ChessMove move : board.getPiece(position).pieceMoves(board, position)) {
                        // if one of those moves ends in capturing the king of the given color,
                        if (board.getPiece(move.getEndPosition()) != null &&
                            board.getPiece(move.getEndPosition()).getClass() == King.class &&
                            board.getPiece(move.getEndPosition()).getTeamColor() == teamColor) {
                            // the king is in check
                            inCheck = true;
                            break;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
