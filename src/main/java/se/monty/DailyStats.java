package se.monty;

public class DailyStats {
    private String firstOrderDateTime;
    private String lastOrderDateTime;
    private double totalSalesInclVat;
    private double totalVat;
    private int totalNumberOfReceipts;




    public String getFirstOrderDateTime() {
        return firstOrderDateTime;
    }




    public void setFirstOrderDateTime(String firstOrderDateTime) {
        this.firstOrderDateTime = firstOrderDateTime;
    }



    public String getLastOrderDateTime() {
        return lastOrderDateTime;
    }

    public void setLastOrderDateTime(String lastOrderDateTime) {
        this.lastOrderDateTime = lastOrderDateTime;
    }

    public double getTotalSalesInclVat() {
        return totalSalesInclVat;
    }

    public void setTotalSalesInclVat(double totalSalesInclVat) {
        this.totalSalesInclVat = totalSalesInclVat;
    }

    public double getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(double totalVat) {
        this.totalVat = totalVat;
    }

    public int getTotalNumberOfReceipts() {
        return totalNumberOfReceipts;
    }

    public void setTotalNumberOfReceipts(int totalNumberOfReceipts) {
        this.totalNumberOfReceipts = totalNumberOfReceipts;
    }
}
