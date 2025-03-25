package pl.edu.pwr.dg.jp.lab03.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfferDao implements Dao<Offer>{

    private Connection conn = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    public OfferDao() {
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
    public Offer get(int id) {
        String sql = "SELECT parameters FROM offers WHERE id=?";
        Offer offer = null;
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                offer = new Offer(id, rs.getString("parameters"));
            }
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return offer;
    }

    @Override
    public List<Offer> getAll() {
        String sql = "SELECT parameters, id FROM offers";
        List<Offer> list = new ArrayList<>();
        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next())
                list.add(new Offer(resultSet.getInt("id"), resultSet.getString("parameters")));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Offer offer) {
        String sql = "INSERT INTO offers(parameters,id) VALUES (?,?);";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, offer.getParameters());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Offer offer) {
        String sql = "UPDATE offers SET parameters = ? " + "WHERE id = ?";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, offer.getParameters());
            pstmt.setInt(2, offer.getId());
            pstmt.executeUpdate();
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Offer offer) {
        String sql = "DELETE FROM offers WHERE id=?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, offer.getId());
            pstmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
