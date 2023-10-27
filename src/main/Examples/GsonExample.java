package Examples;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class GsonExample {
    public static void main(String[] args) {
        var obj = Map.of(
                "name", "perry",
                "year", 2264,
                "pets", List.of("cat", "dog", "fish")
        );

        var serializer = new Gson();

        var json = serializer.toJson(obj);
        System.out.println("JSON: " + json);

        var objFromJson = serializer.fromJson(json, Map.class);
        System.out.println("Object: " + objFromJson);
    }
}
