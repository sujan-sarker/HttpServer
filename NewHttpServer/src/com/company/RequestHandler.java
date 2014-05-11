package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private RequestParser requestParser;

    public RequestHandler(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            requestParser = new RequestParser(inputStream, outputStream);
            requestParser.parseRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
