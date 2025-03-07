package se.monty;

import java.util.List;
import java.util.Scanner;

public class KassasystemAPP {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        List<Product> products = productDAO.getAllProducts();
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Kassasystem (Console) =====");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. List Products");
            System.out.println("2. Make a Purchase");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Products:");
                    for (Product p : products) {
                        System.out.printf("ID: %d | %s | %.2f SEK | VAT %d%%\n",
                                p.getProductId(), p.getName(), p.getPrice(), p.getVatRate());
                    }
                    break;
                case "2":
                    makePurchase(products, orderDAO);
                    break;
                case "3":
                    System.out.println("Exiting Kassasystem. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void makePurchase(List<Product> products, OrderDAO orderDAO) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter product ID (or 'done' to finish): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            int productId;
            try {
                productId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
                continue;
            }

            Product selected = products.stream()
                    .filter(p -> p.getProductId() == productId)
                    .findFirst()
                    .orElse(null);
            if (selected == null) {
                System.out.println("Product not found. Try again.");
                continue;
            }

            System.out.print("Quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(scanner.nextLine());
                if (qty <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Try again.");
                continue;
            }


            double linePrice = selected.getPrice() * qty;
            System.out.printf("Added to cart: %s x %d = %.2f SEK\n", selected.getName(), qty, linePrice);
        }


        System.out.println("Pretending to do a pay-and-clear…");
        System.out.println("TACK FÖR DITT KÖP!");
    }
}
