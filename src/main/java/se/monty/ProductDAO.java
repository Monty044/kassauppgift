package se.monty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_name, price, vat_rate FROM product";



        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {




            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                int vatRate = rs.getInt("vat_rate");

                Product p = new Product(productId, name, price, vatRate);
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
