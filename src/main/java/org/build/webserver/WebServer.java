package org.build.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class WebServer {
    public static void main(String[] args) {
        int port = 8099;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on http://localhost:" + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    // Read first line - GET /path HTTP/1.1
                    String requestLine = in.readLine();
                    if (requestLine == null || requestLine.isEmpty()) continue;

                    System.out.println("Received: " + requestLine);

                    String[] parts = requestLine.split(" ");
                    String path = parts.length > 1 ? parts[1] : "/";

                    String response = "HTTP/1.1 200 OK\r\n\r\nRequested path: " + path + "\r\n";
                    out.write(response.getBytes());
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}