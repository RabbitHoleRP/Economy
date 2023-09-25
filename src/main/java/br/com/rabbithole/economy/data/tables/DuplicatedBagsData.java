package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;

public class DuplicatedBagsData {
    public void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS duplicated_bags (
                    id INT(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    bag_id VARCHAR(36) NOT NULL,
                    player_name VARCHAR(16) NOT NULL,
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

    public void insertDuplicatedBag(String bagId, String playerName) {
        String sqlQuery = """
                INSERT INTO duplicated_bags (bag_id, player_name) VALUE (?, ?);
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, bagId);
            query.getStatement().setString(2, playerName);
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
