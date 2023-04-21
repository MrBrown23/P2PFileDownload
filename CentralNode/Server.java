package CentralNode;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Server extends Thread{

    public static final int portNumber = 5500;
    Socket socket;
    public Server(Socket client){
        this.socket = client;
    }
    public void run() {
        try{

            String condition;

            System.out.println("I'm here1");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("I'm here2");
            Object objects = in.readObject();


            if ((objects.getClass().getName().equals("java.lang.String")))  {
                condition = (String) objects;
                    System.out.println("collection.size() == 0 || collection.size() == 1");
                    System.out.println("Condition: "+condition);
                    PeerDB peerDB = new PeerDB();
                    peerDB.search(condition);

                    Object[][] data = {};
                    while (peerDB.getResultSet().next()){
                        Object[] newRow ={
                                peerDB.getResultSet().getString("id"),
                                peerDB.getResultSet().getString("ip_address"),
                                peerDB.getResultSet().getString("file_name"),
                                peerDB.getResultSet().getString("size"),
                                "Download"
                        };
                        Object[][] newData = new Object[data.length + 1][];
                        System.arraycopy(data, 0, newData, 0, data.length);
                        newData[data.length] = newRow;
                        data = newData;
                    }

                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                    out.writeObject(data);

                    out.flush();
                } else {
                    Object[] data = (Object[]) objects;
                System.out.println(data[0]+" "+data[1]+" "+data[2]);
                    System.out.println("hahaha");
                    PeerDB peerDB = new PeerDB();
                    int size = Integer.parseInt(data[2].toString());
                System.out.println(size);
                    peerDB.insertFile(data[0].toString(),"username",data[1].toString(),size);

                }
            System.out.println("Nothing did work!!!!");


        }catch (SocketException se) {System.out.println("Interruption de type SocketException\n");
            se.printStackTrace();}
        catch (Exception e) {System.out.println(e);
        }

    }


}
