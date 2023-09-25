package br.com.rabbithole.economy.events;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.tables.EconomyAccountData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class OnQuitEvent implements Listener {
    final Plugin plugin;

    public OnQuitEvent(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EconomyAccountData accountData = new EconomyAccountData(event.getPlayer().getName());
        double actualBalance = Economy.getCache().getPlayerBalanceInCache(event.getPlayer().getName());
        if (!accountData.updateBalance(actualBalance)) {
            Economy.getCommon().getMessages().sendError(Bukkit.getConsoleSender(), "Erro ao atualizar Banco de Dados!");
            Economy.getCache().addPlayerInRetryCache(event.getPlayer().getName(), actualBalance);
            return;
        }
        Economy.getCache().removePlayerFromCache(event.getPlayer().getName());
    }
}
