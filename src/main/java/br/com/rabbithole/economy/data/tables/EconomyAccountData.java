package br.com.rabbithole.economy.data.tables;

import br.com.rabbithole.common.core.sql.Query;
import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EconomyAccountData {
    final String playerName;

    public EconomyAccountData(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static void createTable() {
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS economy_account (
                    player_name VARCHAR(16) NOT NULL,
                    balance double NOT NULL DEFAULT 0.0,
                    FOREIGN KEY (player_name) REFERENCES accounts(player_name) ON UPDATE CASCADE
                );
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean createAccount() {
        String sqlQuery = """
                INSERT INTO economy_account (player_name) VALUE (?);
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, this.playerName);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean hasAccount() {
        String sqlQuery = """
                SELECT player_name FROM economy_account WHERE player_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, this.playerName);
            return query.executeQuery().next();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount() {
        String sqlQuery = """
                DELETE FROM economy_account WHERE player_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, this.playerName);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public double getBalance() {
        String sqlQuery = """
                SELECT balance FROM economy_account WHERE player_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setString(1, this.playerName);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) return resultSet.getDouble("balance");
            return 0.0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0.0;
        }
    }

    public boolean updateBalance(double newBalance) {
        String sqlQuery = """
                UPDATE economy_account SET balance = ? WHERE player_name = ?;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            query.getStatement().setDouble(1, newBalance);
            query.getStatement().setString(2, this.playerName);
            return query.executeUpdate() > 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Map<String, Double> getTopBalance() {
        String sqlQuery = """
                SELECT * from economy_account ORDER BY balance DESC LIMIT 10;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            ResultSet resultSet = query.executeQuery();
            Map<String, Double> topBalance = new HashMap<>();
            while (resultSet.next()) {
                topBalance.put(
                        resultSet.getString("player_name"),
                        resultSet.getDouble("balance")
                );
            }
            return topBalance;
        } catch (Exception exception) {
            return new HashMap<>();
        }
    }

    @Nullable
    public Pair<String, Double> getTycoon() {
        String sqlQuery = """
                SELECT * from economy_account ORDER BY balance DESC LIMIT 1;
                """;
        try (Query query = Economy.getDatabase().executeQuery(sqlQuery)) {
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                return new Pair<>(
                        resultSet.getString("player_name"),
                        resultSet.getDouble("balance")
                );
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }
}
