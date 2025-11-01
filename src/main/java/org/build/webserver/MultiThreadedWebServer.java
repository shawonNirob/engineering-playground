package org.build.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

//Handle Multiple Clients (Concurrency)

public class MultiThreadedWebServer {
    private static final String WEB_ROOT = "/Users/shawon/builder/engineering-playground/www";
    private static final AtomicInteger clientCounter = new AtomicInteger(0);


    public static void main(String[] args) {
        int port = 8099;
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server running on http://localhost:" + port);

            while(true){
                Socket clientSocket = serverSocket.accept();
                int clientNumber = clientCounter.incrementAndGet();
                new Thread(new ClientHandler(clientSocket, clientNumber)).start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable{
        private final Socket clientSocket;
        private final int clientNumber;

        ClientHandler(Socket clientSocket, int clientNumber){
            this.clientSocket = clientSocket;
            this.clientNumber = clientNumber;
        }

        public void run(){
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()){

                String requestLine = in.readLine();
                if(requestLine == null || requestLine.isEmpty()) return;

                System.out.println("Received: " + requestLine);

                String[] parts = requestLine.split(" ");
                String path = parts[1];
                if(path.equals("/")) path = "/index.html";

                File file = new File(WEB_ROOT, path);
                System.out.println("Path: " + file.getPath() + " | Thread: " + Thread.currentThread().getName());
                System.out.println("Client #" + clientNumber + " handled by thread: " + Thread.currentThread().getName());

                if(file.exists() && !file.isDirectory()) {
                    String header = "HTTP/1.1 200 OK\r\n\r\n";
                    out.write(header.getBytes());
                    Files.copy(file.toPath(), out);
                }else {
                    String error = "HTTP/1.1 404 Not Found\r\n\r\n<h1>404 Not Found</h1>";
                    out.write(error.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                try {
                    clientSocket.close();
                }catch (IOException ignored){}
            }
        }
    }
}
