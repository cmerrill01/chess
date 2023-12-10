package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessGameImpl implements ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGameImpl() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoardImpl();
        board.resetBoard();
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
            Collection<ChessMove> possibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            Collection<ChessMove> validMoves = new HashSet<>();
            TeamColor myTeam = board.getPiece(startPosition).getTeamColor();
            for (ChessMove possibleMove : possibleMoves) {
                // store the piece this move would capture, so we can put it back
                ChessPiece capturedPiece = null;
                if (board.getPiece(possibleMove.getEndPosition()) != null) {
                    capturedPiece = board.getPiece(possibleMove.getEndPosition());
                }
                // make the move, so we can check whether it's legal
                board.movePiece(possibleMove);
                // if the move does not put this team in check,
                if (!isInCheck(myTeam)) {
                    // add it to the set of valid moves
                    validMoves.add(possibleMove);
                }
                // reverse the move
                if (possibleMove.getPromotionPiece() == null) {
                    ChessMove reverseMove = new ChessMoveImpl(possibleMove.getEndPosition(), possibleMove.getStartPosition());
                    board.movePiece(reverseMove);
                } else {
                    board.removePiece(possibleMove.getEndPosition());
                    board.addPiece(possibleMove.getStartPosition(), new Pawn(myTeam));
                }
                if (capturedPiece != null) {
                    board.addPiece(possibleMove.getEndPosition(), capturedPiece);
                }
            }
            return validMoves;
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

        board.movePiece(move);

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
                                if (move.getPromotionPiece() == null) {
                                    ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                            move.getStartPosition());
                                    board.movePiece(reverseMove);
                                } else {
                                    board.removePiece(move.getEndPosition());
                                    board.addPiece(move.getStartPosition(), new Pawn(teamColor));
                                }
                                if (capturedPiece != null) {
                                    board.addPiece(move.getEndPosition(), capturedPiece);
                                }
                                // break out of the loop
                                break;
                            } else {
                                // reverse the move
                                if (move.getPromotionPiece() == null) {
                                    ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                            move.getStartPosition());
                                    board.movePiece(reverseMove);
                                } else {
                                    board.removePiece(move.getEndPosition());
                                    board.addPiece(move.getStartPosition(), new Pawn(teamColor));
                                }
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
        // first, assume that this team is not in stalemate
        boolean inStalemate = false;
        // if this team is NOT in check,
        if (!isInCheck(teamColor)) {
            // assume this team is in stalemate
            inStalemate = true;
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
                            // if this team is still not in check,
                            if (!isInCheck(teamColor)) {
                                // this team is not in stalemate
                                inStalemate = false;
                                // reverse the move
                                if (move.getPromotionPiece() == null) {
                                    ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                            move.getStartPosition());
                                    board.movePiece(reverseMove);
                                } else {
                                    board.removePiece(move.getEndPosition());
                                    board.addPiece(move.getStartPosition(), new Pawn(teamColor));
                                }
                                if (capturedPiece != null) {
                                    board.addPiece(move.getEndPosition(), capturedPiece);
                                }
                                // break out of the loop
                                break;
                            } else {
                                // reverse the move
                                if (move.getPromotionPiece() == null) {
                                    ChessMove reverseMove = new ChessMoveImpl(move.getEndPosition(),
                                            move.getStartPosition());
                                    board.movePiece(reverseMove);
                                } else {
                                    board.removePiece(move.getEndPosition());
                                    board.addPiece(move.getStartPosition(), new Pawn(teamColor));
                                }
                                if (capturedPiece != null) {
                                    board.addPiece(move.getEndPosition(), capturedPiece);
                                }
                            }
                        }
                    }
                }
            }
        }
        return inStalemate;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGameImpl chessGame = (ChessGameImpl) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
