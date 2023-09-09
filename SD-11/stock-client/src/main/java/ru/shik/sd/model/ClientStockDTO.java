package ru.shik.sd.model;

public class ClientStockDTO {

    private int stockId;
    private String stockName;
    private String companyName;
    private long amount;
    private double price;

    public ClientStockDTO() {
    }

    public ClientStockDTO(int stockId, String stockName, String companyName, long amount, double price) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.companyName = companyName;
        this.amount = amount;
        this.price = price;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
