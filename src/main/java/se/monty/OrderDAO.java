package se.monty;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDAO {


    public int insertReceipt(double totalInclVat, double totalVat) {
        int generatedId = -1;
        String sql = "INSERT INTO receipt (receipt_datetime, total_incl_vat, total_vat) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            LocalDateTime now = LocalDateTime.now();

            ps.setString(1, now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            ps.setDouble(2, totalInclVat);

            ps.setDouble(3, totalVat);


            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }







    public void insertReceiptLine(int receiptId, ReceiptLine line) {
        String sql = "INSERT INTO receipt_line (receipt_id, product_id, quantity, line_price_incl_vat, line_vat) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, receiptId);

            ps.setInt(2, line.getProduct().getProductId());

            ps.setInt(3, line.getQuantity());

            ps.setDouble(4, line.getLinePriceInclVat());
            ps.setDouble(5, line.getLineVat());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public DailyStats getDailyStats(String dateString) {

        DailyStats ds = new DailyStats();

        String sql = "SELECT MIN(receipt_datetime) AS firstOrder," +
                "       MAX(receipt_datetime) AS lastOrder," +
                "       SUM(total_incl_vat) AS sumInclVat," +
                "       SUM(total_vat) AS sumVat," +
                "       COUNT(*) AS countReceipts " +
                "  FROM receipt " +
                " WHERE DATE(receipt_datetime) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dateString);





            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ds.setFirstOrderDateTime(rs.getString("firstOrder"));
                    ds.setLastOrderDateTime(rs.getString("lastOrder"));
                    ds.setTotalSalesInclVat(rs.getDouble("sumInclVat"));
                    ds.setTotalVat(rs.getDouble("sumVat"));
                    ds.setTotalNumberOfReceipts(rs.getInt("countReceipts"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }
}
