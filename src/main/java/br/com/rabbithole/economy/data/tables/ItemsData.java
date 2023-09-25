package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.dto.ItemDTO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemsData {
    public void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS items (
                    item_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    item_name VARCHAR(64) NOT NULL,
                    item_struct BLOB NOT NULL,
                    base_price DOUBLE NOT NULL,
                    min_price DOUBLE NOT NULL,
                    max_price DOUBLE NOT NULL,
                    base_stock INT(8) NOT NULL
                );
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean addItem(ItemDTO item) {
        String sqlQuery = """
                INSERT INTO items (item_name, item_struct, base_price, min_price, max_price, base_stock) VALUE (?, ?, ?, ?, ?, ?);
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, item.itemName());
            query.getStatement().setString(2, item.itemStruct());
            query.getStatement().setDouble(3, item.basePrice());
            query.getStatement().setDouble(4, item.minPrice());
            query.getStatement().setDouble(5, item.maxPrice());
            query.getStatement().setInt(6, item.baseStock());
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public List<ItemDTO> getAllItems() {
        String sqlQuery = """
                SELECT * FROM items;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            ResultSet resultSet = query.executeQuery();
            List<ItemDTO> items = new ArrayList<>();
            while (resultSet.next()) {
                items.add(new ItemDTO(
                    resultSet.getInt("item_id"),
                    resultSet.getString("item_name"),
                    resultSet.getString("item_struct"),
                    resultSet.getDouble("base_price"),
                    resultSet.getDouble("min_price"),
                    resultSet.getDouble("max_price"),
                    resultSet.getInt("base_stock")
                ));
            }
            return items;
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    public int getItemID(String itemName) {
        String sqlQuery = """
                SELECT item_id FROM items WHERE item_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, itemName);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) return resultSet.getInt("item_id");
            return -1;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    public boolean hasItem(int itemId) {
        String sqlQuery = """
                SELECT item_id FROM items WHERE item_id = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setInt(1, itemId);
            ResultSet resultSet = query.executeQuery();
            return resultSet.next();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean updatePrice(int itemId, double newPrice) {
        String sqlQuery = """
                UPDATE items SET base_price = ?, min_price = ?, max_price = ? WHERE item_id = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setDouble(1, newPrice);
            query.getStatement().setDouble(2, newPrice/10);
            query.getStatement().setDouble(3, newPrice*10);
            query.getStatement().setInt(4, itemId);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean updateStock(int itemId, int newStock) {
        String sqlQuery = """
                UPDATE items SET stock = ? WHERE item_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setInt(1, newStock);
            query.getStatement().setInt(2, itemId);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean removeItem(String itemName) {
        String sqlQuery = """
                DELETE FROM items WHERE item_id = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, itemName);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
