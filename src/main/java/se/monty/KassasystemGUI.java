package se.monty;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KassasystemGUI extends JFrame {
    private JPanel productPanel;
    private JTextArea receiptArea;
    private JTextField quantityField;
    private JButton addButton;
    private JButton payButton;
    private JButton dailyStatsButton;  // For the VG "dagsstatistik"

    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private List<ReceiptLine> currentReceipt = new ArrayList<>();
    private Product selectedProduct = null;

    public KassasystemGUI() {
        setTitle("Kassasystem");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2));
        List<Product> productList = productDAO.getAllProducts();
        for (Product p : productList) {
            JButton btn = new JButton(p.getName());
            btn.addActionListener(e -> selectProduct(p));
            productPanel.add(btn);
        }


        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        bottomPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField("1", 5);
        bottomPanel.add(quantityField);

        addButton = new JButton("Add");
        addButton.addActionListener(e -> addToReceipt());
        bottomPanel.add(addButton);

        payButton = new JButton("Pay");
        payButton.addActionListener(e -> payAndClear());
        bottomPanel.add(payButton);

        dailyStatsButton = new JButton("Dagsstatistik");
        dailyStatsButton.addActionListener(e -> showDailyStats());
        bottomPanel.add(dailyStatsButton);

        // Layout
        setLayout(new BorderLayout());
        add(productPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void selectProduct(Product p) {
        selectedProduct = p;
        receiptArea.append("Selected product: " + p.getName() + "\n");
    }

    private void addToReceipt() {
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "No product selected!");
            return;
        }
        try {
            int qty = Integer.parseInt(quantityField.getText());
            if (qty <= 0) {
                throw new NumberFormatException();
            }
            ReceiptLine line = new ReceiptLine(selectedProduct, qty);
            currentReceipt.add(line);

            double linePrice = line.getLinePriceInclVat();
            receiptArea.append(String.format("%s x %d = %.2f SEK\n",
                    selectedProduct.getName(),
                    qty,
                    linePrice));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive integer for quantity.");
        }
    }

    private void payAndClear() {
        if (currentReceipt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the receipt!");
            return;
        }
        // Calculate totals
        double totalInclVat = 0.0;
        double totalVat = 0.0;
        for (ReceiptLine line : currentReceipt) {
            totalInclVat += line.getLinePriceInclVat();
            totalVat += line.getLineVat();
        }


        int receiptId = orderDAO.insertReceipt(totalInclVat, totalVat);
        for (ReceiptLine line : currentReceipt) {
            orderDAO.insertReceiptLine(receiptId, line);
        }


        receiptArea.append(String.format("TOTAL: %.2f SEK\n", totalInclVat));
        receiptArea.append(String.format("VAT: %.2f SEK\n", totalVat));
        receiptArea.append("TACK FÖR DITT KÖP!\n\n");


        currentReceipt.clear();
        selectedProduct = null;
    }

    private void showDailyStats() {

        String dateStr = JOptionPane.showInputDialog(this, "Enter date (YYYY-MM-DD) for stats:", "2025-03-0");
        if (dateStr == null || dateStr.isBlank()) {
            return;
        }
        DailyStats stats = orderDAO.getDailyStats(dateStr);


        String xml = "<xml>\n" +
                "  <SaleStatistics>\n" +
                "    <FirstOrderDateTime>" + stats.getFirstOrderDateTime() + "</FirstOrderDateTime>\n" +
                "    <LastOrderDateTime>" + stats.getLastOrderDateTime() + "</LastOrderDateTime>\n" +
                "    <TotalSalesInclVat>" + stats.getTotalSalesInclVat() + "</TotalSalesInclVat>\n" +
                "    <TotalVat>" + stats.getTotalVat() + "</TotalVat>\n" +
                "    <TotalNumberOfReceipts>" + stats.getTotalNumberOfReceipts() + "</TotalNumberOfReceipts>\n" +
                "  </SaleStatistics>\n" +
                "</xml>";


        JOptionPane.showMessageDialog(this, xml, "Dagsstatistik XML", JOptionPane.INFORMATION_MESSAGE);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KassasystemGUI gui = new KassasystemGUI();
            gui.setVisible(true);
        });
    }
}
