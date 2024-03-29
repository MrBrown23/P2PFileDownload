package Peer;

import java.net.ServerSocket;
import java.net.SocketException;

public class MultiThreadPeer {
    public static final int PORT_NUMBER = 1234;

    public static void main(String[] args)
    {
        try
        {
            FilePostSearchGUI filePostSearchGUI = new FilePostSearchGUI(args[0]);
            filePostSearchGUI.setVisible(true);
            ServerSocket s = new ServerSocket(PORT_NUMBER);
            while(true)
            {
                Peer th = new Peer(s.accept());
                System.out.println("client connected");
                th.start();
            }
        } catch (SocketException se) {System.out.println("SocketException interrupt");}
        catch (Exception e) {System.out.println(e);
        e.printStackTrace();
        }
    }
}
