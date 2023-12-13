package ui;

import chess.ChessGame;

public class ChessBoardDisplay {
    private final ChessGame game;
    private final ChessGame.TeamColor playerColor;

    public ChessBoardDisplay(ChessGame game, ChessGame.TeamColor playerColor) {
        this.game = game;
        this.playerColor = playerColor;
    }

    public String display() {
        StringBuilder display = new StringBuilder();
        display.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
        display.append("\n");

        var rows = game.getBoard().toString().split("\n");

        switch (playerColor) {
            case WHITE -> {
                colLabelsWhite(game, display);
                for (int rowNum = 0; rowNum < rows.length; rowNum++) {
                    var row = rows[rowNum].toCharArray();
                    rowLabel(display, rowNum);
                    displayBoardRowWhite(row, rowNum, display);
                    rowLabel(display, rowNum);
                    display.append("\n");
                }
                colLabelsWhite(game, display);
            }
            case BLACK -> {
                colLabelsBlack(game, display);
                for (int rowNum = rows.length - 1; rowNum >= 0; rowNum--) {
                    var row = rows[rowNum].toCharArray();
                    rowLabel(display, rowNum);
                    displayBoardRowBlack(row, rowNum, display);
                    rowLabel(display, rowNum);
                    display.append("\n");
                }
                colLabelsBlack(game, display);
            }
        }

        display.append(EscapeSequences.RESET_BG_COLOR);

        return display.toString();
    }

    private static void displayBoardRowWhite(char[] row, int rowNum, StringBuilder display) {
        int colNum = 0;
        for (int i = row.length - 1; i >= 0; i--) {
            char token = row[i];

            if ((rowNum + colNum) % 2 == 1) display.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            else display.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            if (Character.isUpperCase(token)) display.append(EscapeSequences.SET_TEXT_COLOR_RED);
            else if (Character.isLowerCase(token)) display.append(EscapeSequences.SET_TEXT_COLOR_BLUE);

            switch (token) {
                case '|' -> {
                }
                case 'R' -> display.append(EscapeSequences.WHITE_ROOK);
                case 'r' -> display.append(EscapeSequences.BLACK_ROOK);
                case 'N' -> display.append(EscapeSequences.WHITE_KNIGHT);
                case 'n' -> display.append(EscapeSequences.BLACK_KNIGHT);
                case 'B' -> display.append(EscapeSequences.WHITE_BISHOP);
                case 'b' -> display.append(EscapeSequences.BLACK_BISHOP);
                case 'K' -> display.append(EscapeSequences.WHITE_KING);
                case 'k' -> display.append(EscapeSequences.BLACK_KING);
                case 'Q' -> display.append(EscapeSequences.WHITE_QUEEN);
                case 'q' -> display.append(EscapeSequences.BLACK_QUEEN);
                case 'P' -> display.append(EscapeSequences.WHITE_PAWN);
                case 'p' -> display.append(EscapeSequences.BLACK_PAWN);
                case ' ' -> display.append(EscapeSequences.EMPTY);
                default -> display.append("X");
            }
            display.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            if (token != '|') colNum++;
        }
    }

    private static void displayBoardRowBlack(char[] row, int rowNum, StringBuilder display) {
        int colNum = 0;
        for (char token : row) {
            if ((rowNum + colNum) % 2 == 0) display.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            else display.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            if (Character.isUpperCase(token)) display.append(EscapeSequences.SET_TEXT_COLOR_RED);
            else display.append(EscapeSequences.SET_TEXT_COLOR_BLUE);

            switch (token) {
                case '|' -> {
                }
                case 'R' -> display.append(EscapeSequences.WHITE_ROOK);
                case 'r' -> display.append(EscapeSequences.BLACK_ROOK);
                case 'N' -> display.append(EscapeSequences.WHITE_KNIGHT);
                case 'n' -> display.append(EscapeSequences.BLACK_KNIGHT);
                case 'B' -> display.append(EscapeSequences.WHITE_BISHOP);
                case 'b' -> display.append(EscapeSequences.BLACK_BISHOP);
                case 'K' -> display.append(EscapeSequences.WHITE_KING);
                case 'k' -> display.append(EscapeSequences.BLACK_KING);
                case 'Q' -> display.append(EscapeSequences.WHITE_QUEEN);
                case 'q' -> display.append(EscapeSequences.BLACK_QUEEN);
                case 'P' -> display.append(EscapeSequences.WHITE_PAWN);
                case 'p' -> display.append(EscapeSequences.BLACK_PAWN);
                case ' ' -> display.append(EscapeSequences.EMPTY);
                default -> display.append("X");
            }
            display.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            if (token != '|') colNum++;
        }
    }

    private static void rowLabel(StringBuilder display, int rowNum) {
        display.append(EscapeSequences.RESET_BG_COLOR);
        display.append(" ");
        display.append(rowNum + 1);
        display.append(" ");
    }

    private static void colLabelsWhite(ChessGame game, StringBuilder display) {
        display.append(EscapeSequences.RESET_BG_COLOR);
        display.append("   ");
        for (int i = 0; i < game.getBoard().getMaxColumn(); i++) {
            display.append(" ");
            display.append((char) ('a' + i));
            display.append("\u2003");
        }
        display.append("   ");
        display.append("\n");
    }

    private static void colLabelsBlack(ChessGame game, StringBuilder display) {
        display.append(EscapeSequences.RESET_BG_COLOR);
        display.append("   ");
        for (int i = game.getBoard().getMaxColumn() - 1; i >= 0; i--) {
            display.append(" ");
            display.append((char) ('a' + i));
            display.append("\u2003");
        }
        display.append("   ");
        display.append("\n");
    }
}
