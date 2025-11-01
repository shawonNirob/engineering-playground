package org.build.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class FileWebServer {
    private static final String WEB_ROOT = "/Users/shawon/builder/engineering-playground/www";

    public static void main(String[] args) {
        int port = 8099;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serving files from " + WEB_ROOT + " on http://localhost:" + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    String requestLine = in.readLine();
                    if(requestLine == null || requestLine.isEmpty()) continue;

                    System.out.println("Received: " + requestLine);

                    String[] parts = requestLine.split(" ");
                    String path = parts[1];
                    if(path.equals("/")) path = "/index.html";

                    File file = new File(WEB_ROOT, path);

                    System.out.println("Looking for file: " + file.getAbsolutePath());

                    if(file.exists() && !file.isDirectory()) {
                        String header = "HTTP/1.1 200 OK\r\n\r\n";
                        out.write(header.getBytes());
                        Files.copy(file.toPath(), out);
                    }else {
                        String error = "HTTP/1.1 404 Not Found\r\n\r\n<h1>404 Not Found</h1>";
                        out.write(error.getBytes());
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
