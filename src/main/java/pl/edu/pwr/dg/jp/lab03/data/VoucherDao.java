package pl.edu.pwr.dg.jp.lab03.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDao implements Dao<Voucher>{

    private Connection conn = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());
        }
    }

    public VoucherDao() {
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
    public Voucher get(int id) {
        String sql = """
        SELECT vouchers.id, vouchers.info, vouchers.status,
               clients.name AS client_name,
               organizers.name AS organizer_name,
               offers.parameters AS offer_parameters
        FROM vouchers
        LEFT JOIN clients ON vouchers.id_client = clients.id
        LEFT JOIN organizers ON vouchers.id_organizer = organizers.id
        LEFT JOIN offers ON vouchers.id_offer = offers.id
        WHERE vouchers.id = ?
        """;

        Voucher voucher = null;
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String info = rs.getString("info");
                Voucher.Status status = Voucher.Status.valueOf(rs.getString("status"));
                String clientName = rs.getString("client_name");
                String organizerName = rs.getString("organizer_name");
                String offerParameters = rs.getString("offer_parameters");
                voucher = new Voucher(id, clientName, organizerName, offerParameters , info, status);
            }
            pstmt.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucher;
    }

    @Override
    public List<Voucher> getAll() {
        String sql = """
        SELECT vouchers.id, vouchers.info, vouchers.status,
               clients.name AS client_name,
               organizers.name AS organizer_name,
               offers.parameters AS offer_parameters
        FROM vouchers
        LEFT JOIN clients ON vouchers.id_client = clients.id
        LEFT JOIN organizers ON vouchers.id_organizer = organizers.id
        LEFT JOIN offers ON vouchers.id_offer = offers.id
        """;
        
        List<Voucher> list = new ArrayList<>();
        try {
            connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int id = rs.getInt("id");
                String info = rs.getString("info");
                Voucher.Status status = Voucher.Status.valueOf(rs.getString("status"));
                String clientName = rs.getString("client_name");
                String organizerName = rs.getString("organizer_name");
                String offerParameters = rs.getString("offer_parameters");
                list.add(new Voucher(id, clientName, organizerName, offerParameters , info, status));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void create(Voucher voucher) {
        try {
            connect();

            // 1. Client
            int idClient = 0;
            if (voucher.getClientName() != "Brak") {
                String clientSql = "SELECT id FROM clients WHERE name = ?";
                PreparedStatement clientStmt = conn.prepareStatement(clientSql);
                clientStmt.setString(1, voucher.getClientName());
                ResultSet clientRs = clientStmt.executeQuery();
                if (!clientRs.next()) {
                    throw new SQLException("Client with name " + voucher.getClientName() + " not found.");
                }
                idClient = clientRs.getInt("id");
                clientStmt.close();
            }

            // 2. Organizer
            int idOrganizer = 0;
            if (voucher.getOrganizerName() != "Brak") {
                String organizerSql = "SELECT id FROM organizers WHERE name = ?";
                PreparedStatement organizerStmt = conn.prepareStatement(organizerSql);
                organizerStmt.setString(1, voucher.getOrganizerName());
                ResultSet organizerRs = organizerStmt.executeQuery();
                if (!organizerRs.next()) {
                    throw new SQLException("Organizer with name " + voucher.getOrganizerName() + " not found.");
                }
                idOrganizer = organizerRs.getInt("id");
                organizerStmt.close();
            }
            // 3. Offer
            String offerSql = "SELECT id FROM offers WHERE parameters = ?";
            PreparedStatement offerStmt = conn.prepareStatement(offerSql);
            offerStmt.setString(1, voucher.getOfferParameters());
            ResultSet offerRs = offerStmt.executeQuery();
            if (!offerRs.next()) {
                throw new SQLException("Offer with description " + voucher.getOfferParameters() + " not found.");
            }
            int idOffer = offerRs.getInt("id");
            offerStmt.close();

            // 4. Wstaw nowy wpis do tabeli vouchers
            String sql = "INSERT INTO vouchers (id_client, id_organizer, id_offer, info, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idClient);
            pstmt.setInt(2, idOrganizer);
            pstmt.setInt(3, idOffer);
            pstmt.setString(4, voucher.getInfo());
            pstmt.setString(5, voucher.getStatus().name());
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Voucher successfully created.");

            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO do update function
    @Override
    public void update(Voucher voucher) {
        String sql = "UPDATE vouchers SET id_client = ?, id_organizer = ?, id_offer = ?, info = ?, status = ? WHERE id = ?";

        try {
            connect();

            // 1. Client
            int idClient = 0;
            if (voucher.getClientName() != null) {
                String clientSql = "SELECT id FROM clients WHERE name = ?";
                PreparedStatement clientStmt = conn.prepareStatement(clientSql);
                clientStmt.setString(1, voucher.getClientName());
                ResultSet clientRs = clientStmt.executeQuery();
                if (!clientRs.next()) {
                    throw new SQLException("Client with name " + voucher.getClientName() + " not found.");
                }
                idClient = clientRs.getInt("id");
                clientStmt.close();
            }

            // 2. Organizer
            int idOrganizer = 0;
            System.out.println(voucher.getOrganizerName());
            if (voucher.getOrganizerName() != null) {
                System.out.println(voucher.getOrganizerName());
                String organizerSql = "SELECT id FROM organizers WHERE name = ?";
                PreparedStatement organizerStmt = conn.prepareStatement(organizerSql);
                organizerStmt.setString(1, voucher.getOrganizerName());
                ResultSet organizerRs = organizerStmt.executeQuery();
                if (!organizerRs.next()) {
                    throw new SQLException("Organizer with name " + voucher.getOrganizerName() + " not found.");
                }
                idOrganizer = organizerRs.getInt("id");
                System.out.println(idOrganizer);
                organizerStmt.close();
            }

            // 3. Offer
            int idOffer = 0;
            String offerSql = "SELECT id FROM offers WHERE parameters = ?";
            PreparedStatement offerStmt = conn.prepareStatement(offerSql);
            offerStmt.setString(1, voucher.getOfferParameters());
            ResultSet offerRs = offerStmt.executeQuery();
            if (!offerRs.next()) {
                throw new SQLException("Offer with description " + voucher.getOfferParameters() + " not found.");
            }
            idOffer = offerRs.getInt("id");
            offerStmt.close();

            // 4. Update the voucher in the database
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idClient); // Set client ID
            pstmt.setInt(2, idOrganizer); // Set organizer ID
            pstmt.setInt(3, idOffer); // Set offer ID
            pstmt.setString(4, voucher.getInfo()); // Set updated info
            pstmt.setString(5, voucher.getStatus().name()); // Set updated status
            pstmt.setInt(6, voucher.getId()); // Voucher ID for where clause
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Voucher successfully updated.");

            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Voucher voucher) {
        String sql = "DELETE FROM vouchers WHERE id=?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, voucher.getId());
            pstmt.executeUpdate();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
