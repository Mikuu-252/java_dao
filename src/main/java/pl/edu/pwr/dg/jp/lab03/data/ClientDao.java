package pl.edu.pwr.dg.jp.lab03.data;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientDao implements Dao<Client>{

    private Connection conn = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    public ClientDao() {
    }

    private void connect() throws SQLException {
        if (conn != null)
            return;
        String url = "jdbc:sqlite:lab03db.db";
        conn = DriverManager.getConnection(url);
    }

    private void disconnect() throws SQLException {
        if (conn == null)
            return;
        conn.close();
        conn = null;
    }

    @Override
    public Client get(int id) {
        String sql = "SELECT name FROM clients WHERE id=?";
        Client client = null;
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                client = new Client(id, rs.getString("name"));
            }
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> getAll() {
        String sql = "SELECT name, id FROM clients";
        List<Client> list = new ArrayList<>();
        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next())
                list.add(new Client(resultSet.getInt("id"), resultSet.getString("name")));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Client client) {
        String sql = "INSERT INTO clients(name,id) VALUES (?,?);";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE clients SET name = ? " + "WHERE id = ?";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setInt(2, client.getId());
            pstmt.executeUpdate();
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Client client) {
        String sql = "DELETE FROM clients WHERE id=?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, client.getId());
            pstmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
