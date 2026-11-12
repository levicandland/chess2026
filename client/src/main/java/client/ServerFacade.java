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

    private String sendRequest (HttpRequest httpRequest) throws ResponseException {
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new ResponseException("Error: request failed");
            }
            return httpResponse.body();
        }
        catch (IOException | InterruptedException ex) {
            throw new ResponseException("Error: could not reach the server");
        }
    }

    public ServerFacade (int port) {
        this.serverAddress = "http://localhost:" + port;
    }

    public void clear () throws ResponseException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/db"))
                .DELETE().build();
        sendRequest(httpRequest);
    }

    public AuthData register (UserData user) throws ResponseException {
        String requestBody = new Gson().toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/user"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        String responseBody = sendRequest(httpRequest);
        return new Gson().fromJson(responseBody, AuthData.class);
    }

    public AuthData login (String username, String password) throws ResponseException {
        UserData user = new UserData(username, password, null);
        String requestBody = new Gson().toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/session"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        String responseBody = sendRequest(httpRequest);
        return new Gson().fromJson(responseBody, AuthData.class);
    }

    public void logout (AuthData auth) throws ResponseException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverAddress + "/session"))
                .header("authorization", auth.authToken())
                .DELETE().build();
        sendRequest(httpRequest);
    }

}
