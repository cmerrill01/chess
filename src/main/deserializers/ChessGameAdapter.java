package deserializers;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.*;
import main.ChessGameImpl;

import java.lang.reflect.Type;

public class ChessGameAdapter implements JsonDeserializer<ChessGame> {
    @Override
    public ChessGame deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        return builder.create().fromJson(jsonElement, ChessGameImpl.class);
    }
}