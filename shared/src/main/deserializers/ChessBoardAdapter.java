package deserializers;

import chess.ChessBoard;
import chess.ChessPiece;
import com.google.gson.*;
import chess.ChessBoardImpl;

import java.lang.reflect.Type;

public class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {
    @Override
    public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        return builder.create().fromJson(jsonElement, ChessBoardImpl.class);
    }
}