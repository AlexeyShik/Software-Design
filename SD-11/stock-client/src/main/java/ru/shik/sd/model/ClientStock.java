package ru.shik.sd.model;

public class ClientStock {

    private int id;
    private int clientId;
    private int stockId;
    private long amount;

    public ClientStock() {

    }

    public ClientStock(int id, int clientId, int stockId, long amount) {
        this.id = id;
        this.clientId = clientId;
        this.stockId = stockId;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
