package deserializers;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import chess.*;

import java.io.IOException;
import java.util.Objects;

public class ChessPieceAdapter extends TypeAdapter<ChessPiece> {
    @Override
    public void write(JsonWriter jsonWriter, ChessPiece chessPiece) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("team");
        jsonWriter.value(chessPiece.getTeamColor().toString());
        jsonWriter.name("type");
        jsonWriter.value(chessPiece.getPieceType().toString());
        jsonWriter.endObject();
    }

    @Override
    public ChessPiece read(JsonReader jsonReader) throws IOException {
        ChessPiece piece;
        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            jsonReader.beginObject();
            String fieldName = null;
            ChessPiece.PieceType type = null;
            ChessGame.TeamColor team = null;

            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();

                if (token.equals(JsonToken.NAME)) {
                    fieldName = jsonReader.nextName();
                } else {
                    jsonReader.skipValue();
                }

                if ("type".equals(fieldName)) {
                    token = jsonReader.peek();
                    String typeString = jsonReader.nextString();
                    switch (typeString.toLowerCase()) {
                        case "bishop" -> type = ChessPiece.PieceType.BISHOP;
                        case "king" -> type = ChessPiece.PieceType.KING;
                        case "knight" -> type = ChessPiece.PieceType.KNIGHT;
                        case "pawn" -> type = ChessPiece.PieceType.PAWN;
                        case "queen" -> type = ChessPiece.PieceType.QUEEN;
                        case "rook" -> type = ChessPiece.PieceType.ROOK;
                        default -> throw new IllegalStateException("Unexpected piece type: " + typeString.toLowerCase());
                    }
                } else if ("team".equals(fieldName)) {
                    token = jsonReader.peek();
                    String teamString = jsonReader.nextString();
                    switch (teamString.toLowerCase()) {
                        case "white" -> team = ChessGame.TeamColor.WHITE;
                        case "black" -> team = ChessGame.TeamColor.BLACK;
                        default -> throw new IllegalStateException("Unexpected team color: " + teamString.toLowerCase());
                    }
                }
                if (type != null & team != null) {
                    break;
                }
            }
            jsonReader.endObject();
            switch (Objects.requireNonNull(type)) {
                case BISHOP -> piece = new Bishop(team);
                case KING -> piece = new King(team);
                case KNIGHT -> piece = new Knight(team);
                case PAWN -> piece = new Pawn(team);
                case QUEEN -> piece = new Queen(team);
                case ROOK -> piece = new Rook(team);
                default -> throw new IllegalStateException("Unexpected piece type: " + type);
            }
        } else if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.skipValue();
            piece = null;
        } else {
            throw new JsonParseException("Error: Unexpected Json token");
        }
        return piece;
    }
}
