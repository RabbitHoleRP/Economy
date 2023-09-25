package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;

public class CoinBagData {
    public void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS coins_bag (
                    bag_id VARCHAR(36) NOT NULL PRIMARY KEY,
                    player_name VARCHAR(16) NOT NULL,
                    amount DOUBLE NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (player_name) REFERENCES accounts(player_name) ON UPDATE CASCADE
                );
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean createCoinBag(String bagID, String playerName, double amount) {
        String sqlQuery = """
                INSERT INTO coins_bag (bag_id, player_name, amount) VALUES (?, ?, ?);
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, bagID);
            query.getStatement().setString(2, playerName);
            query.getStatement().setDouble(3, amount);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean hasCoinBag(String bagID) {
        String sqlQuery = """
                SELECT bag_id FROM coins_bag WHERE bag_id = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, bagID);
            return query.executeQuery().next();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteCoinBag(String bagID) {
        String sqlQuery = """
                DELETE FROM coins_bag WHERE bag_id = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, bagID);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
