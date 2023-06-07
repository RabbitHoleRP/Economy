package br.com.rabbithole.economy.utils.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query implements AutoCloseable {
    private final Connection connection;
    private final PreparedStatement statement;
    private ResultSet resultSet = null;

    public Query(Connection connection, PreparedStatement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public ResultSet executeQuery() throws SQLException {
        return this.resultSet = statement.executeQuery();
    }

    public void executeUpdate() throws SQLException {
        this.statement.executeUpdate();
    }

    @Override
    public void close() throws SQLException {
        if (this.resultSet != null) this.resultSet.close();
        if (this.statement != null) this.statement.close();
        if (this.connection != null) this.connection.close();
    }
}
