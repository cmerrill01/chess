package server;

import com.google.gson.Gson;
import daos.memoryDatabase;
import responses.ClearApplicationResponse;
import services.ClearApplicationService;
import spark.*;


public class Server {

    private final memoryDatabase db = new memoryDatabase();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {

        Spark.port(8080);

        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", this::clear);
    }

    private Object clear(Request req, Response res) {
        ClearApplicationService service = new ClearApplicationService();
        ClearApplicationResponse response = service.clearApplication(db);
        return new Gson().toJson(response, ClearApplicationResponse.class);
    }

}