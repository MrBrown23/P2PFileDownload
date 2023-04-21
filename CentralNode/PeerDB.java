package CentralNode;

import java.sql.*;

public class PeerDB {
    private ResultSet resultSet;

    private Connection connection;

    private Statement statement;

    private static final String URL = "jdbc:mysql://localhost:3306/p2p";
    private static final String USER = "root";

    PeerDB(){
        try {
            this.connection = DriverManager.getConnection(URL,USER,"");
            this.statement = connection.createStatement();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    void search(String condition){
        try {
            if(condition.equals(""))
                this.resultSet = statement.executeQuery("SELECT s.id, s.ip_address, r.file_name, r.size" +
                        " FROM suppliers s JOIN resources r ON s.id = r.supplier_id ORDER BY r.id DESC; ");
            else this.resultSet = statement.executeQuery("SELECT s.id, s.ip_address, r.file_name, r.size" +
                    " FROM suppliers s JOIN resources r ON s.id = r.supplier_id WHERE r.file_name like '%"+condition+"%' ORDER BY r.id DESC;");

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    void insertFile(String ip, String username, String fileName, int size){
        try {

            String query1 = "INSERT INTO suppliers (ip_address, username, availability)" +
                    " SELECT '"+ip+"', '"+username+"', 1 WHERE NOT EXISTS (SELECT * FROM suppliers WHERE ip_address = '"+ip+"');";

            statement.execute(query1);
            String query2 = "Select id from suppliers WHERE ip_address = '"+ip+"';";
            this.resultSet = statement.executeQuery(query2);
            String id = "";
            while (resultSet.next()) {
                id = this.resultSet.getString("id");

            }
            System.out.println(id);
            String query3 = "INSERT INTO `resources` (`file_name`, `supplier_id`, `size`) VALUES ('"+fileName+"', '"+id+"', '"+size+"');";
            statement.execute(query3);

        }
        catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
        ResultSet getResultSet(){
        return this.resultSet;
    }


}

