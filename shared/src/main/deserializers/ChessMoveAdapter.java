package deserializers;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove> {
    @Override
    public ChessMove deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        builder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        return builder.create().fromJson(jsonElement, ChessMoveImpl.class);
    }
}
