package org.ta;

import java.sql.*;

/**
 * @author thwal
 */

public class Model {


    /**
     * Connect to a sample database or create it.
     */
    public Model() {

        connect();
        createNewTable();

    }

    /**
     * Used to connect to an existing database or create one if it doesn't exist.
     *
     * @return returns a connection to a specific db.
     */
    private Connection connect() {
        String url = "jdbc:sqlite:db/sample.db";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error: connect");
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Create the Basic Tables for this database
     * Tables TPVData (NodeID, Type, X, Y)
     * and EdgeData (NodeID, OutputID, ConnectedNodeID, InputID)
     */
    public void createNewTable() {


        // SQL statement for creating a new table
        String sql1 = "CREATE TABLE IF NOT EXISTS TPVData (\n" +
                "   NodeID INTEGER PRIMARY KEY,\n " +
                "   Type TEXT NOT NULL,\n " +
                "   X DOUBLE NOT NULL,\n" +
                "   Y DOUBLE NOT NULL\n" +
                ");";

        String sql2 = "CREATE TABLE IF NOT EXISTS EdgeData (\n" +
                "   NodeID INTEGER NOT NULL,\n " +
                "   OutputID INTEGER NOT NULL,\n " +
                "   ConnectedNodeID INTEGER NOT NULL,\n" +
                "   InputID INTEGER NOT NULL,\n" +
                "   PRIMARY KEY (NodeID, OutputID),\n" +
                "   FOREIGN KEY (NodeID) REFERENCES TPVData(NodeID)\n" +
                ");";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();


            //create a new table
            stmt.execute("BEGIN TRANSACTION");
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute("COMMIT");

        } catch (SQLException e) {
            System.out.println("Error: Table creation");
            System.out.println(e.getMessage());
        }
    }

    public void insertTPVData(int nodeID, String type, double x, double y) {
        String sql = "INSERT OR REPLACE INTO TPVData(NodeID, Type, X, Y) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nodeID);
            pstmt.setString(2, type);
            pstmt.setDouble(3, x);
            pstmt.setDouble(4, y);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: InsertTPVData");
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert a new row into EdgeData
     */
    public void insertEdgeData(int nodeID, int outputID, int connectedNodeID, int inputID) {
        String sql = "INSERT OR REPLACE INTO EdgeData(NodeID, OutputID, ConnectedNodeID, InputID) VALUES(?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nodeID);
            pstmt.setInt(2, outputID);
            pstmt.setInt(3, connectedNodeID);
            pstmt.setInt(4, inputID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in a table
     */
    public void selectAll() {
        String sql1 = "SELECT NodeID, Type, X, Y FROM TPVData";
        String sql2 = "SELECT NodeID, OutputID, ConnectedNodeID, InputID FROM EdgeData";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql1)) {

            while (rs.next()) {
                System.out.println(rs.getInt("NodeID") + "\t" +
                        rs.getString("Type") + "\t" +
                        rs.getDouble("X") + "\t" +
                        rs.getDouble("Y"));
            }

            ResultSet rs2 = stmt.executeQuery(sql2);
            while (rs2.next()) {
                System.out.println(rs.getInt("NodeID") + "\t" +
                        rs.getInt("OutputID") + "\t" +
                        rs.getInt("ConnectedNodeID") + "\t" +
                        rs.getInt("InputID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int id) {
        String sql = "delete from warehouses where id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void clear() {

        String sql1 = "DELETE FROM TPVData";
        String sql2 = "DELETE FROM EdgeData";

    }


    public static void main(String[] args) {
        Model model = new Model();
        model.insertTPVData(1, "IF", 2, 5);
        model.insertEdgeData(1, 0, 5, 3);
        model.selectAll();
    }

}