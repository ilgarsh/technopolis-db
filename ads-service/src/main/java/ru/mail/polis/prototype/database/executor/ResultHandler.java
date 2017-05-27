package ru.mail.polis.prototype.database.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 28.05.17
 *
 * @author olerom
 */
@FunctionalInterface
public interface ResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}