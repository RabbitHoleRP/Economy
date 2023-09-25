package br.com.rabbithole.economy.events;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.tables.EconomyAccountData;
import br.com.rabbithole.economy.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

public class OnAsyncPreLoginEvent implements Listener {
    final Plugin plugin;

    public OnAsyncPreLoginEvent(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        /*
        EconomyAccountData accountData = new EconomyAccountData(event.getName());
        if (!accountData.hasAccount()) {
            if (!accountData.createAccount()) {
                Economy.getCommon().getMessages().sendError(Bukkit.getConsoleSender(), "Erro ao adicionar Jogador %s ao sistema de Economia!".formatted(event.getName()));
                return;
            }
            Economy.getCache().addPlayerInCache(accountData.getPlayerName(), 0.0);
            return;
        }
        Economy.getCache().addPlayerInCache(accountData.getPlayerName(), accountData.getBalance());
         */
        if (Economy.getCache().hasPlayerInRetryCache(event.getName())) {
            Economy.getCache().addPlayerInCache(event.getName(), Economy.getCache().getPlayerBalanceInRetryCache(event.getName()));
            Economy.getCache().removePlayerFromRetryCache(event.getName());
            return;
        }

        EconomyAccountData accountData = new EconomyAccountData(event.getName());
        if (!accountData.hasAccount()) {
            if (!accountData.createAccount()) {
                Economy.getCommon().getMessages().sendError(
                        Bukkit.getConsoleSender(),
                        "Erro ao adicionar Jogador %s ao sistema de Economia!".formatted(event.getName()));
                event.kickMessage(StringUtils.formatString("<red>Por favor entre novamente!"));
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                return;
            }
            Economy.getCache().addPlayerInCache(accountData.getPlayerName(), 0.0);
        }

        Economy.getCache().addPlayerInCache(accountData.getPlayerName(), accountData.getBalance());
    }
}
