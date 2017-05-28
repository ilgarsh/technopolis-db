package ru.mail.polis.prototype.database;

import ru.mail.polis.prototype.database.datasets.Advertisement;
import ru.mail.polis.prototype.database.datasets.Advertiser;
import ru.mail.polis.prototype.database.executor.Executor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.h2.jdbcx.JdbcDataSource;

import java.util.List;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
public class DatabaseImpl implements Database {

    private String user;
    private String password;
    private String fullPath;

    private Executor executor;

    public DatabaseImpl() {
        try (InputStream input = new FileInputStream("./ads-service/src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            this.user = properties.getProperty("dbuser");
            this.password = properties.getProperty("dbpassword");
            this.fullPath = properties.getProperty("databaseUrl") + properties.getProperty("database");

            this.executor = new Executor(getH2Connection());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection getH2Connection() {
        try {
            JdbcDataSource jdbcDataSource = new JdbcDataSource();
            jdbcDataSource.setURL(this.fullPath);
            jdbcDataSource.setUser(this.user);
            jdbcDataSource.setPassword(this.password);

            return DriverManager.getConnection(this.fullPath, this.user, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initTables() throws SQLException {
        executor.execUpdate("CREATE TABLE if not exists ADVERTISER (advertiser_id bigint, cash real, primary key (advertiser_id));");
        executor.execUpdate("CREATE TABLE if not exists CONNECTION (advertisement_id bigint, advertiser_id bigint, primary key (advertisement_id));");
        executor.execUpdate("CREATE TABLE if not exists ADVERTISEMENT (action bigint, user_id bigint, advertisement_id bigint, price real, primary key (action, advertisement_id, user_id));");

        executor.execUpdate("ALTER TABLE CONNECTION ADD FOREIGN KEY (advertiser_id) REFERENCES ADVERTISER(advertiser_id)");
        executor.execUpdate("ALTER TABLE ADVERTISEMENT ADD FOREIGN KEY (advertisement_id) REFERENCES CONNECTION(advertisement_id)");
    }

    @Override
    public void dropTables() throws SQLException {
        executor.execUpdate("DROP TABLE if exists ADVERTISER;");
        executor.execUpdate("DROP TABLE if exists CONNECTION;");
        executor.execUpdate("DROP TABLE if exists ADVERTISEMENT;");
    }

    @Override
    public void addAdvertiser(long advertiserId, float cash) throws SQLException {
        executor.execUpdate("INSERT INTO ADVERTISER (advertiser_id, cash) values ('"
                + advertiserId + "', '" + cash + "')");
    }

    @Override
    public List<Advertisement> getAdvertisementByUserId(long userId) throws SQLException {
        return executor.execQuery("SELECT * FROM ADVERTISEMENT WHERE user_id='" + userId + '\'', result -> {
            List<Advertisement> ads = new ArrayList<>();
            while (!result.isLast()) {
                result.next();
                ads.add(new Advertisement(result.getInt(1),
                        result.getLong(2), result.getLong(3), result.getFloat(4)));
            }
            return ads;
        });
    }

    @Override
    public Advertiser getAdvertiserById(long id) throws SQLException {
        return executor.execQuery("SELECT * FROM ADVERTISER WHERE advertiser_id='" + id + '\'', result -> {
            result.next();
            return new Advertiser(result.getLong(1),
                    result.getFloat(2));
        });
    }

    @Override
    public void addAdvertisement(long action, long userId, long advertisementId, float price) throws SQLException {
        executor.execUpdate("INSERT INTO ADVERTISEMENT (action, user_id, advertisement_id, price) values ('"
                + action + "', '" + userId + "', '" + advertisementId + "', '" + price + "')");
    }
}
