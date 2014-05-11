package com.company;

public class ServerRunner {
    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        new Server(SERVER_PORT).runServer();
    }
}
