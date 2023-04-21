package Peer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FilePostGUI extends JFrame implements ActionListener {

    private JLabel fileLabel;
    private JTextField fileField;
    private JButton browseButton;
    private JButton postButton;
    private JButton resetButton;

    private String host;
    public FilePostGUI(String host) {
        this.host = host;
        initUI();
    }

    private void initUI() {

        setTitle("File Post GUI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        JPanel topPanel = new JPanel(new FlowLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        fileLabel = new JLabel("File:");
        topPanel.add(fileLabel);

        fileField = new JTextField(20);
        topPanel.add(fileField);

        browseButton = new JButton("Browse...");
        browseButton.setBackground(Color.GREEN);
        browseButton.setForeground(Color.WHITE);
        topPanel.add(browseButton);
        browseButton.addActionListener(this);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        postButton = new JButton("Post File");
        postButton.setBackground(Color.GREEN);
        postButton.setForeground(Color.WHITE);
        bottomPanel.add(postButton);
        postButton.addActionListener(this);

        resetButton = new JButton("Reset");
        resetButton.setBackground(Color.GREEN);
        resetButton.setForeground(Color.WHITE);
        bottomPanel.add(resetButton);
        resetButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                fileField.setText(file.getAbsolutePath());
            }
        } else if (e.getSource() == postButton) {

            String ip = null;
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface ni = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.isSiteLocalAddress()) {
                            System.out.println("Your private IP address is: " + address.getHostAddress());
                            ip = address.getHostAddress();
                        }
                    }
                }
            } catch (Exception exc) {
                System.err.println("Unable to find private IP address: " + exc.getMessage());
            }
            System.out.println(ip);
            String fileName = fileField.getText();
            System.out.println("File posted: " + fileName);

            File f = new File(fileName);
            long fileSize = f.length();
            fileField.setText("");

            System.out.format("The size of the file: %d bytes", fileSize);

            Object[] data = {ip, fileName, fileSize};

            System.out.println(data[0]);
            System.out.println(data[1]);
            System.out.println(data[2]);

            try {
                Socket socket = new Socket(this.host, 5500);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(data);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        } else if (e.getSource() == resetButton) {
            fileField.setText("");
        }
    }

}
