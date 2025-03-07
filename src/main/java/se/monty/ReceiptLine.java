package se.monty;

public class ReceiptLine {
    private Product product;
    private int quantity;

    public ReceiptLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }



    public Product getProduct() {
        return product;
    }



    public int getQuantity() {
        return quantity;
    }





    public double getLinePriceInclVat() {
        double basePrice = product.getPrice();
        return basePrice * quantity;
    }




    public double getLineVat() {

        double rate = product.getVatRate() / 100.0;
        return getLinePriceInclVat() - (getLinePriceInclVat() / (1 + rate));
    }
}
