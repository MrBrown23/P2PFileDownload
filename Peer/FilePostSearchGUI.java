package Peer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FilePostSearchGUI extends JFrame implements ActionListener {

    private String host;
    private JButton postButton;
    private JButton searchButton;
    private JTextField searchField;
    private JTable fileTable;

    Object data = null;
    Object[][] dataArray = null;

    String[] columnNames = {"ID", "IP Address", "File Name", "File size", "Download"};

    public FilePostSearchGUI(String host) {
        this.host = host;
        setTitle("P2P JAVA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        postButton = new JButton("Post");
        postButton.addActionListener(this);

        postButton.setBackground(Color.GREEN);
        postButton.setForeground(Color.WHITE);
        topPanel.add(postButton);
        searchField = new JTextField(20);
        topPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setBackground(Color.GREEN);
        searchButton.setForeground(Color.WHITE);
        topPanel.add(searchButton);
        add(topPanel, BorderLayout.NORTH);

        create_connection("");
        TableCellRenderer fileNameRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                String fileName = (String) table.getModel().getValueAt(row, column);

                String displayedFileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                return super.getTableCellRendererComponent(table, displayedFileName, isSelected, hasFocus, row, column);
            }
        };

        TableCellEditor fileNameEditor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

                String fileName = (String) table.getModel().getValueAt(row, column);

                return super.getTableCellEditorComponent(table, fileName, isSelected, row, column);
            }
        };

        fileTable = new JTable(dataArray, columnNames);
        fileTable.getColumnModel().getColumn(2).setCellRenderer(fileNameRenderer);
        fileTable.getColumnModel().getColumn(2).setCellEditor(fileNameEditor);
        fileTable.setFont(new Font("Arial", Font.PLAIN, 16)); // set font size to 16
        ButtonColumn buttonColumn = new ButtonColumn(fileTable,  this);
        fileTable.getColumnModel().getColumn(4).setCellRenderer(buttonColumn);
        fileTable.getColumnModel().getColumn(4).setCellEditor(buttonColumn);

        JScrollPane scrollPane = new JScrollPane(fileTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == postButton) {
            FilePostGUI ex = new FilePostGUI(this.host);
            ex.setVisible(true);

        } else if (e.getSource() == searchButton) {

            String searchTerm = searchField.getText();
            System.out.println(searchTerm);
            create_connection(searchTerm);


            DefaultTableModel newTableModel = new DefaultTableModel(dataArray, columnNames);


            fileTable.setModel(newTableModel);


            ButtonColumn buttonColumn = new ButtonColumn(fileTable,  this);
            TableCellRenderer fileNameRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                    String fileName = (String) table.getModel().getValueAt(row, column);

                    String displayedFileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                    return super.getTableCellRendererComponent(table, displayedFileName, isSelected, hasFocus, row, column);
                }
            };

            TableCellEditor fileNameEditor = new DefaultCellEditor(new JTextField()) {
                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

                    String fileName = (String) table.getModel().getValueAt(row, column);


                    return super.getTableCellEditorComponent(table, fileName, isSelected, row, column);
                }
            };
            fileTable.getColumnModel().getColumn(2).setCellRenderer(fileNameRenderer);
            fileTable.getColumnModel().getColumn(2).setCellEditor(fileNameEditor);
            fileTable.getColumnModel().getColumn(4).setCellRenderer(buttonColumn);
            fileTable.getColumnModel().getColumn(4).setCellEditor(buttonColumn);

        }
    }

    private void create_connection(String message){
        try {

            Socket clientSocket = new Socket(this.host,5500);
            System.out.println("Client connected: " + clientSocket);


            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(message);

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            this.data = in.readObject();

            this.dataArray = (Object[][]) data;


            in.close();
            clientSocket.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
