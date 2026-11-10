package client;

public class ServerFacade {

    private String serverAddress;

    public ServerFacade (int port) {
        this.serverAddress = "http://localhost:" + port;
    }
}
