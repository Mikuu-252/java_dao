package pl.edu.pwr.dg.jp.lab03.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDao implements Dao<Seller>{

    private Connection conn = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    public SellerDao() {
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
    public Seller get(int id) {
        String sql = "SELECT name FROM sellers WHERE id=?";
        Seller seller = null;
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                seller = new Seller(id, rs.getString("name"));
            }
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return seller;
    }

    @Override
    public List<Seller> getAll() {
        String sql = "SELECT name, id FROM sellers";
        List<Seller> list = new ArrayList<>();
        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next())
                list.add(new Seller(resultSet.getInt("id"), resultSet.getString("name")));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Seller seller) {
        String sql = "INSERT INTO sellers(name,id) VALUES (?,?);";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, seller.getName());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Seller seller) {
        String sql = "UPDATE sellers SET name = ? " + "WHERE id = ?";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, seller.getName());
            pstmt.setInt(2, seller.getId());
            pstmt.executeUpdate();
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Seller seller) {
        String sql = "DELETE FROM sellers WHERE id=?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, seller.getId());
            pstmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
