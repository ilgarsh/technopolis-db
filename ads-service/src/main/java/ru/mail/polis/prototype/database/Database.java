package ru.mail.polis.prototype.database;

import java.sql.SQLException;

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

}
