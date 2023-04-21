package Peer;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private JButton button;
    int columnIndex;
    public static final int BUFFER_SIZE = 4096;

    public static final int PORT_NUMBER = 1234;

    public ButtonColumn(JTable table,  JFrame frame) {
        button = new JButton("Download");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
         this.columnIndex = table.getColumnModel().getColumnIndex("IP Address");
        buttonClicked(button,table,frame);

    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object objValue, boolean isSelected, boolean hasFocus, int row, int column) {
        column = this.columnIndex;
        String ip = table.getModel().getValueAt(row, column).toString();
        if (isUp(ip, PORT_NUMBER)) {
            button.setBackground(Color.GREEN);
            button.setEnabled(true);
        } else {
            button.setBackground(Color.RED);
            button.setEnabled(false);
        }
        fireEditingStopped();
        return this.button;
    }

    private  void buttonClicked(JButton button, JTable table, JFrame frame){
        button.addActionListener(e -> {
            Color buttonColor = button.getBackground();
            if (buttonColor.equals(Color.GREEN)){
            int row = table.convertRowIndexToModel(table.getEditingRow());
            int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to download the file?", "Download", JOptionPane.YES_NO_OPTION);
            System.out.println("Button clicked in row " + row);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    String value = table.getValueAt(row, 2).toString();
                    System.out.println("The file name is: "+value);

                    String host = table.getValueAt(row, 1).toString();

                    Socket clientSocket = new Socket(host, PORT_NUMBER);
                    System.out.println("Connected to server.");


                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                    out.writeUTF(value);
                    String fileType = value.substring(value.lastIndexOf("."));

                    InputStream in = clientSocket.getInputStream();

                    FileOutputStream fileOut = new FileOutputStream(UUID.randomUUID()+fileType);

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        fileOut.write(buffer, 0, bytesRead);
                    }

                    fileOut.close();
                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("File downloaded.");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            fireEditingStopped();
        }});
    }

    public static boolean isUp(String ipAddress, int port) {
        Socket socket = new Socket();
        try  {
            socket.connect(new InetSocketAddress(ipAddress, port), 10);
            return true;
        } catch (IOException e) {
            return false;
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
