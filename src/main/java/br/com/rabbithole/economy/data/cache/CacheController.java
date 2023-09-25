package br.com.rabbithole.economy.data.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheController {
    private final Map<String, Double> economyCache;
    private final Map<String, Double> retryUpdateEconomyCache;

    public CacheController() {
        this.economyCache = new HashMap<>();
        this.retryUpdateEconomyCache = new HashMap<>();
    }

    public void addPlayerInCache(String playerName, double balance) {
        this.economyCache.put(playerName, balance);
    }

    public void addPlayerInRetryCache(String playerName, double balance) {
        this.retryUpdateEconomyCache.put(playerName, balance);
    }

    public boolean hasPlayerInCache(String playerName) {
        return this.economyCache.containsKey(playerName);
    }

    public boolean hasPlayerInRetryCache(String playerName) {
        return this.retryUpdateEconomyCache.containsKey(playerName);
    }

    public boolean hasRequiredBalance(String playerName, double amount) {
        return amount <= this.economyCache.get(playerName);
    }

    public void removePlayerFromCache(String playerName) {
        this.economyCache.remove(playerName);
    }

    public void removePlayerFromRetryCache(String playerName) {
        this.retryUpdateEconomyCache.remove(playerName);
    }

    public void updatePlayerBalanceInCache(String playerName, double balance) {
        this.economyCache.replace(playerName, balance);
    }

    public Double getPlayerBalanceInCache(String playerName) {
        return this.economyCache.get(playerName);
    }

    public Double getPlayerBalanceInRetryCache(String playerName) {
        return this.retryUpdateEconomyCache.get(playerName);
    }

    public Map<String, Double> getRetryUpdateEconomyCache() {
        return retryUpdateEconomyCache;
    }
}
