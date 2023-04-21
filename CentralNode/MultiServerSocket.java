package CentralNode;

import java.net.ServerSocket;
import java.net.SocketException;

public class MultiServerSocket
{
    public static final int PORT_NUMBER = 5500;
    public static void main(String[] args)
    {
        try
        {
            ServerSocket s = new ServerSocket(PORT_NUMBER);
            while(true)
            {
                Server th = new Server(s.accept());
                System.out.println("Client connected");
                th.start();
            }
        } catch (SocketException se) {System.out.println("SocketException interrupt");}
        catch (Exception e) {System.out.println(e);}
    }
}
