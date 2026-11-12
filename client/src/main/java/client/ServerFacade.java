package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private String serverAddress;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerFacade (int port) {
        this.serverAddress = "http://localhost:" + port;
    }

    public void clear () throws ResponseException {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverAddress + "/db"))
                    .DELETE().build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ResponseException("Error: request failed");
            }
        }
        catch (IOException | InterruptedException ex) {
            throw new ResponseException("Error: could not reach the server");
        }
    }

    public AuthData register (UserData user) throws ResponseException {
        try {
            String requestBody = new Gson().toJson(user);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverAddress + "/user"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ResponseException("Error: request failed");
            }
            return new Gson().fromJson(httpResponse.body(), AuthData.class);
        }
        catch (IOException | InterruptedException ex) {
            throw new ResponseException("Error: could not reach the server");
        }
    }

    public AuthData login (String username, String password) throws ResponseException {
        UserData user = new UserData(username, password, null);
        try {
            String requestBody = new Gson().toJson(user);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverAddress + "/session"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ResponseException("Error: request failed");
            }
            return new Gson().fromJson(httpResponse.body(), AuthData.class);
        }
        catch (IOException | InterruptedException ex) {
            throw new ResponseException("Error: could not reach the server");
        }
    }

}
