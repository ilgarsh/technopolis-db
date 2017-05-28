package ru.mail.polis.prototype.database.datasets;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public class Advertisement {

    private int action;
    private long userId;
    private long advertisementId;
    private float price;

    public Advertisement(int action, long userId, long advertisementId, float price) {
        this.action = action;
        this.userId = userId;
        this.advertisementId = advertisementId;
        this.price = price;
    }

    public int getAction() {
        return action;
    }

    public long getUserId() {
        return userId;
    }

    public long getAdvertisementId() {
        return advertisementId;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "action=" + action +
                ", userId=" + userId +
                ", advertisementId=" + advertisementId +
                ", price=" + price +
                '}';
    }
}
