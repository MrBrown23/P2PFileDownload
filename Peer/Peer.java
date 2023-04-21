package Peer;

import java.io.*;
import java.net.*;

public class Peer extends Thread{
    Socket socket;
    public static final int BUFFER_SIZE = 4096;
    public Peer(Socket client){
        this.socket = client;
    }
    public  void run() {
        try {

            System.out.println("Client connected: " + socket.getInetAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream());

            String fileName = in.readUTF();
            System.out.println("Client requested file: " + fileName);


            FileInputStream fileIn = new FileInputStream(fileName);

            OutputStream out = socket.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }


            fileIn.close();
            out.close();
            in.close();
            socket.close();
            System.out.println("File sent to client.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
