package br.com.rabbithole.economy.utils.sql;

import br.com.rabbithole.economy.Economy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private final HikariDataSource hikariDataSource;

    public Database(String url, String user, String password) {
        this.hikariDataSource = new HikariDataSource(createConfig(url, user, password));
    }

    private HikariConfig createConfig(String url, String user, String password) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("CachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("PrepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setMaximumPoolSize(120);
        hikariConfig.setMaxLifetime(30000);
        hikariConfig.setLeakDetectionThreshold(5000);
        hikariConfig.setMinimumIdle(3);
        return hikariConfig;
    }

    public Connection getConnection() throws SQLException{
        return this.hikariDataSource.getDataSource().getConnection();
    }

    public static Query executeQuery(String sql) throws SQLException {
        Connection connection = Economy.getDataBase().getConnection();
        return new Query(connection, connection.prepareStatement(sql));
    }
}
