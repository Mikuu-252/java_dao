package pl.edu.pwr.dg.jp.lab03.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizerDao implements Dao<Organizer>{

    private Connection conn = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    public OrganizerDao() {
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
    public Organizer get(int id) {
        String sql = "SELECT name FROM organizers WHERE id=?";
        Organizer organizer = null;
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                organizer = new Organizer(id, rs.getString("name"));
            }
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return organizer;
    }

    @Override
    public List<Organizer> getAll() {
        String sql = "SELECT name, id FROM organizers";
        List<Organizer> list = new ArrayList<>();
        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next())
                list.add(new Organizer(resultSet.getInt("id"), resultSet.getString("name")));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Organizer organizer) {
        String sql = "INSERT INTO organizers(name,id) VALUES (?,?);";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, organizer.getName());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Organizer organizer) {
        String sql = "UPDATE organizers SET name = ? " + "WHERE id = ?";

        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, organizer.getName());
            pstmt.setInt(2, organizer.getId());
            pstmt.executeUpdate();
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Organizer organizer) {
        String sql = "DELETE FROM organizers WHERE id=?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, organizer.getId());
            pstmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
