package br.com.rabbithole.economy;

import br.com.rabbithole.RedisLib;
import br.com.rabbithole.configurations.RedisConfig;
import br.com.rabbithole.economy.utils.StringUtils;
import br.com.rabbithole.economy.utils.sql.Database;
import br.com.rabbithole.economy.utils.sql.Query;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;

public final class Economy extends JavaPlugin {
    private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("<green>Iniciado com Sucesso!"));
        registers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("<red>Desativado com Sucesso!"));
        HandlerList.unregisterAll(this);
    }

    private void registers() {
        data();
        commands();
        events();
        tasks();
    }

    private void commands() {}

    private void events() {}

    private void data() {
        saveDefaultConfig();
        RedisLib.init(new RedisConfig("", false, "", 6379, "", "", 100));
        database = new Database("", "", "");
    }

    private void tasks() {}

    public static Database getDataBase() {
        return database;
    }
}
