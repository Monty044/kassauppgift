package se.monty;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int vatRate; // e.g. 12 or 25

    public Product(int productId, String name, double price, int vatRate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.vatRate = vatRate;
    }

    public int getProductId() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getVatRate() {
        return vatRate;
    }
}
