package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RequestParser {
    private OutputWriter writer;
    private FileInputStream inFile = null;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader inputReader;
    private String statusLine = null;
    private String contentTypeLine = null;
    private String contentLength = null;
    private List<String> resMessage;

    RequestParser(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void parseRequest() {
        try {

            inputReader = new BufferedReader(new InputStreamReader(inputStream));
            writer = new OutputWriter();
            processHeaderLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                inputReader.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processHeaderLine() {
        String token = " ";
        String headerLine;
        String name;
        String ext;
        String url;
        String method = " ";

        StringTokenizer stringTokenizer;

        int dot;
        int slash;
        int containLength = 0;

        try {
            String fileName;
            headerLine = inputReader.readLine();
            stringTokenizer = new StringTokenizer(headerLine);
            method = stringTokenizer.nextToken();
            url = stringTokenizer.nextToken();

            while ((headerLine = inputReader.readLine()) != null) {
                System.out.println(headerLine);

                if (headerLine.length() == 0)
                    break;

                stringTokenizer = new StringTokenizer(headerLine);
                token = stringTokenizer.nextToken();

                if (token.equals("Content-Length:")) {
                    String length = stringTokenizer.nextToken();
                    containLength = Integer.parseInt(length);
                    System.out.println(containLength);
                }

            }

            if (method.equals("GET")) {

                if (url.equals("/")) {
                    fileName = "/var/www/sujan/default.html";
                    processHtmlFile(fileName);

                } else {
                    dot = url.lastIndexOf('.');
                    slash = url.lastIndexOf('/');
                    name = url.substring(slash + 1, dot);
                    ext = url.substring(dot, url.length());
                    fileName = "/var/www/sujan/" + name + ext;

                    processHtmlFile(fileName);

                }

            }

            if (method.equals("POST")) {
                printPostContent(inputReader, containLength);
                fileName = "/var/www/sujan/default.html";
                processHtmlFile(fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPostContent(BufferedReader inputReader, int containLength) {
        StringBuilder content = new StringBuilder();
        String contextText;

        int read;

        try {
            while (((read = inputReader.read()) != -1)) {
                char ch = (char) read;
                content.append(ch);
                containLength--;
                if (containLength == 0)
                    break;

            }
            contextText = content.toString();
            System.out.println(contextText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processHtmlFile(String fileName) {
        boolean isFileExists = true;

        try {
            inFile = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
            isFileExists = false;
        }
        if (isFileExists) {
            sendResponse(inFile);

        } else {
            sendError();
        }

    }

    private void sendError() {

        statusLine = "HTTP/1.0 404 Not Found\r\n";
        contentTypeLine = "Content-Type: text/html\r\n";
        String entityBody = "<HTML>" +
                "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
                "<BODY><H1>404 File Not Found</H1></BODY></HTML>";

        resMessage = new ArrayList<String>();
        resMessage.add(statusLine);
        resMessage.add(contentTypeLine);
        resMessage.add("\r\n");
        resMessage.add(entityBody);
        writer.write(resMessage, outputStream);
    }

    private void sendResponse(FileInputStream inFile) {
        statusLine = " HTTP/1.1 200 Ok\r\n";
        contentTypeLine = "Content-Type: text/html\r\n";
        String vendorLine = "Server: EXECUTER 1.1";

        try {
            contentLength = "Content-Length:" + (new Integer(inFile.available())).toString() + "\r\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        resMessage = new ArrayList<String>();
        resMessage.add(statusLine);
        resMessage.add(vendorLine);
        resMessage.add(contentLength);
        resMessage.add(contentTypeLine);
        resMessage.add("\r\n");

        writer.write(resMessage, outputStream);
        writer.writeFile(inFile, outputStream);

    }


}
