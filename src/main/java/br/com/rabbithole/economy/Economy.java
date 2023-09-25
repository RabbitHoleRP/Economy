package br.com.rabbithole.economy;

import br.com.rabbithole.common.Common;
import br.com.rabbithole.common.core.sql.Database;
import br.com.rabbithole.economy.commands.admin.ItemsCommand;
import br.com.rabbithole.economy.commands.admin.ItemsKeyVerifyCommand;
import br.com.rabbithole.economy.commands.user.BalanceCommand;
import br.com.rabbithole.economy.commands.user.WithdrawCommand;
import br.com.rabbithole.economy.data.cache.CacheController;
import br.com.rabbithole.economy.data.tables.*;
import br.com.rabbithole.economy.events.OnInteractEvent;
import br.com.rabbithole.economy.events.OnAsyncPreLoginEvent;
import br.com.rabbithole.economy.events.OnQuitEvent;
import br.com.rabbithole.economy.tasks.RetryUpdateBalanceTask;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {
    private static EconomyAPI API;
    private static CacheController cache;
    private static Database database;
    private static Common common;

    @Override
    public void onEnable() {
        // Plugin startup logic
        registers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(this);
    }

    private void registers() {
        data();
        commands();
        events();
        tasks();
        common = new Common();
        cache = new CacheController();
    }

    private void commands() {
        new WithdrawCommand();
        new BalanceCommand();
        new ItemsKeyVerifyCommand();
        new ItemsCommand();
    }

    private void data() {
        saveDefaultConfig();
        API = new EconomyAPI();
        database = new Database(
                getConfig().getString("MySQL.Host"),
                getConfig().getInt("MySQL.Port"),
                getConfig().getString("MySQL.Database"),
                getConfig().getString("MySQL.User"),
                getConfig().getString("MySQL.Password")
        );
        new CoinBagData().createTable();
        new TransactionData().createTable();
        new DuplicatedBagsData().createTable();
        new ItemsData().createTable();
        EconomyAccountData.createTable();
    }

    private void events() {
        new OnInteractEvent(this);
        new OnAsyncPreLoginEvent(this);
        new OnQuitEvent(this);
    }

    private void tasks() {
        RetryUpdateBalanceTask.init(this);
    }

    public static EconomyAPI getAPI() {
        return API;
    }

    public static CacheController getCache() {
        return cache;
    }

    public static Database getDatabase() {
        return database;
    }

    public static Common getCommon() {
        return common;
    }

    public static Economy getInstance() {
        return Economy.getPlugin(Economy.class);
    }
}
