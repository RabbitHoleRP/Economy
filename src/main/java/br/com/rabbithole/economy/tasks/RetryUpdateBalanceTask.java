package br.com.rabbithole.economy.tasks;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.tables.EconomyAccountData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class RetryUpdateBalanceTask extends BukkitRunnable {

    public static void init(Plugin plugin) {
        new RetryUpdateBalanceTask().runTaskTimerAsynchronously(plugin, 0L, 20*60*5L);
    }

    @Override
    public void run() {
        if (Economy.getCache().getRetryUpdateEconomyCache().isEmpty()) return;
        Map<String, Double> retryMap = Economy.getCache().getRetryUpdateEconomyCache();

        for (Map.Entry<String, Double> entry : retryMap.entrySet()) {
            if (new EconomyAccountData(entry.getKey()).updateBalance(entry.getValue()))
                Economy.getCache().removePlayerFromRetryCache(entry.getKey());
        }
    }
}
