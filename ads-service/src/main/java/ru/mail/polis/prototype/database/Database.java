package ru.mail.polis.prototype.database;

import ru.mail.polis.prototype.database.datasets.Advertisement;
import ru.mail.polis.prototype.database.datasets.Advertiser;

import java.sql.SQLException;
import java.util.List;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public interface Database {

    void initTables() throws SQLException;

    void dropTables() throws SQLException;

    void addAdvertisement(long action, long userId, long advertisementId, float price) throws SQLException;

    void addAdvertiser(long advertiserId, float cash) throws SQLException;

    List<Advertisement> getAdvertisementByUserId(long userId) throws SQLException;

    Advertiser getAdvertiserById(long id) throws SQLException;


}
