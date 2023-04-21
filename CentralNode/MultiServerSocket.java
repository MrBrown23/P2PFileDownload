package CentralNode;

import java.net.ServerSocket;
import java.net.SocketException;

public class MultiServerSocket
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket s = new ServerSocket(5500);
            while(true)
            {
                Server th = new Server(s.accept());
                System.out.println("client connected");
                th.start();
            }
        } catch (SocketException se) {System.out.println("Interruption de type SocketException");}
        catch (Exception e) {System.out.println(e);}
    }
}
