package ru.mail.polis.prototype.database.datasets;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public class Advertiser {
    private long id;
    private float cash;

    public Advertiser(long id, float cash) {
        this.id = id;
        this.cash = cash;
    }

    public long getId() {
        return id;
    }

    public float getCash() {
        return cash;
    }

    @Override
    public String toString() {
        return "Advertiser{" +
                "id=" + id +
                ", cash=" + cash +
                '}';
    }
}
