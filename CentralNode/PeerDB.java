package CentralNode;

import java.sql.*;

public class PeerDB {
    ResultSet resultSet;

    Connection connection;

    Statement statement;
    PeerDB(){
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2p","root","");
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
            System.out.println("Condition 2 activated!!!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    void insertFile(String ip, String username, String fileName, int size){
        try {

            String query1 = "INSERT INTO suppliers (ip_address, username, availability)" +
                    " SELECT '"+ip+"', '"+username+"', 1 WHERE NOT EXISTS (SELECT * FROM suppliers WHERE ip_address = '"+ip+"');";

            System.out.println("Traing to execute 1st query");
            statement.execute(query1);
            System.out.println("Were here!");
            String query2 = "Select id from suppliers WHERE ip_address = '"+ip+"';";
            System.out.println("Traing to execute 2nd query");
            this.resultSet = statement.executeQuery(query2);
            String id = "";
            while (resultSet.next()) {
                id = this.resultSet.getString("id");

            }
            System.out.println(id);
            String query3 = "INSERT INTO `resources` (`file_name`, `supplier_id`, `size`) VALUES ('"+fileName+"', '"+id+"', '"+size+"');";
            System.out.println("Traing to execute 3rd query");
            statement.execute(query3);
            System.out.println("Table Inserted 2 activated!!!");

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

