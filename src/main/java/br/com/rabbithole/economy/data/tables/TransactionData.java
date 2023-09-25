package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.enums.TransactionsType;

public class TransactionData {
    public void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id INT(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    player_sender VARCHAR(16) NOT NULL,
                    player_receiver VARCHAR(16) NOT NULL,
                    amount DOUBLE NOT NULL,
                    transaction_type INT(1) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (player_sender) REFERENCES accounts(player_name) ON UPDATE CASCADE,
                    FOREIGN KEY (player_receiver) REFERENCES accounts(player_name) ON UPDATE CASCADE
                );
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void insertTransaction(String playerSender, String playerReceiver, double amount, TransactionsType type) {
        String sqlQuery = """
                INSERT INTO transactions (player_sender, player_receiver, amount, transaction_type) VALUE (?, ? ,?, ?);
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, playerSender);
            query.getStatement().setString(2, playerReceiver);
            query.getStatement().setDouble(3, amount);
            query.getStatement().setInt(4, type.getTypeID());
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
