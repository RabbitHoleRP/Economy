package br.com.rabbithole.economy.commands.user;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.tables.EconomyAccountData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BalanceCommand implements CommandExecutor {
    public BalanceCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("coins"));
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Economy.getCommon().getMessages().sendError(sender, "Apenas jogadores podem executar este Comando!");
            return false;
        }

        if (args.length > 0) {
            showOtherPlayerBalance(player, args[0]);
            return true;
        }

        double balance = Economy.getCache().getPlayerBalanceInCache(player.getName());
        Economy.getCommon().getMessages().sendSuccess(player, "Você possuí %.2f¢".formatted(balance));
        return true;
    }

    private boolean hasPlayer(String targetName) {
        return new EconomyAccountData(targetName).hasAccount();
    }

    private void showOtherPlayerBalance(Player player, String targetName) {
        if (!hasPlayer(targetName)) {
            Economy.getCommon().getMessages().sendError(player, "Jogador não encontrado!");
            return;
        }

        double targetBalance;
        if (Economy.getCache().hasPlayerInCache(targetName)) {
            targetBalance = Economy.getCache().getPlayerBalanceInCache(targetName);
        } else {
            targetBalance = new EconomyAccountData(targetName).getBalance();
        }

        Economy.getCommon().getMessages().sendSuccess(player, "O Jogador %s possuí %.2f¢".formatted(targetName, targetBalance));
    }
}
