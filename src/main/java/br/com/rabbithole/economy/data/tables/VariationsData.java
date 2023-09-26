package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;

public class VariationsData {
    public void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS variations (
                    item_name VARCHAR(64) NOT NULL PRIMARY KEY,
                    current_price DOUBLE NOT NULL,
                    current_stock INT(8) NOT NULL,
                    sold INT(8) NOT NULL,
                    last_variation INT(4) NOT NULL,
                    FOREIGN KEY (item_name) REFERENCES items(item_name) ON UPDATE CASCADE
                );
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
