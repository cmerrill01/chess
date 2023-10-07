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
        // if the player attempts to move the other team's piece,
        if (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            // throw an invalid move exception
            throw new InvalidMoveException("INVALID MOVE: The selected piece belongs to the other team.");

        }
        // if the move is not in the set of valid moves starting at the given position,
        if (!validMoves(move.getStartPosition()).contains(move)) {
            // throw an invalid move exception
            throw new InvalidMoveException("INVALID MOVE: The selected piece cannot make this move.");
        }
        ChessPiece capturedPiece = null;
        if (board.getPiece(move.getEndPosition()) != null) {
            capturedPiece = board.getPiece(move.getEndPosition());
        }
        board.movePiece(move);
        // if the move puts this team in check,
        if (isInCheck(teamTurn)) {
            // reverse the move
            ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(), move.getStartPosition());
            board.movePiece(reverseMove);
            if (capturedPiece != null) {
                board.addPiece(move.getEndPosition(), capturedPiece);
            }
            // throw an invalid move exception
            throw new InvalidMoveException("INVALID MOVE: This move puts the current team's king in check.");
        }
        // switch to the other team's turn
        switch (teamTurn) {
            case WHITE -> teamTurn = TeamColor.BLACK;
            case BLACK -> teamTurn = TeamColor.WHITE;
        }
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
        // first, assume that this team is not in checkmate
        boolean inCheckmate = false;
        // if this team is in check,
        if (isInCheck(teamColor)) {
            // assume this team is in checkmate
            inCheckmate = true;
            // iterate through all positions on the board
            for (int i = 1; i <= board.getMaxRow(); i++) {
                for (int j = 1; j <= board.getMaxColumn(); j++) {
                    ChessPosition position = new ChessPositionImpl(i, j);
                    // if there is a piece at the current position that belongs to this team,
                    if (board.getPiece(position) != null &&
                            board.getPiece(position).getTeamColor() == teamColor) {
                        // iterate through all this piece's moves
                        for (ChessMove move : board.getPiece(position).pieceMoves(board, position)) {
                            // make the move
                            ChessPiece capturedPiece = null;
                            if (board.getPiece(move.getEndPosition()) != null) {
                                capturedPiece = board.getPiece(move.getEndPosition());
                            }
                            board.movePiece(move);
                            // if this team is no longer in check,
                            if (!isInCheck(teamColor)) {
                                // this team is not in checkmate
                                inCheckmate = false;
                                // reverse the move
                                ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                        move.getStartPosition());
                                board.movePiece(reverseMove);
                                if (capturedPiece != null) {
                                    board.addPiece(move.getEndPosition(), capturedPiece);
                                }
                                // break out of the loop
                                break;
                            } else {
                                // reverse the move
                                ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                        move.getStartPosition());
                                board.movePiece(reverseMove);
                                if (capturedPiece != null) {
                                    board.addPiece(move.getEndPosition(), capturedPiece);
                                }
                            }
                        }
                    }
                }
            }
        }
        return inCheckmate;
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
