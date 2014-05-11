package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int NO_OF_THREAD = 60;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(NO_OF_THREAD);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void runServer() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new RequestHandler(clientSocket));
                executorService.execute(clientThread);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
