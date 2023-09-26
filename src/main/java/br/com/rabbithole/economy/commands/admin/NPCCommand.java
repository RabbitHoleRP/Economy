package br.com.rabbithole.economy.commands.admin;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.npc.ShopKeeperNPC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NPCCommand implements CommandExecutor {

    public NPCCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("setnpc"));
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Economy.getCommon().getMessages().sendError(sender, "Apenas jogadores podem executar este Comando!");
            return false;
        }

        if (!player.hasPermission("economy.admin")) {
            Economy.getCommon().getMessages().sendError(sender, "Você não tem permissão para executar este Comando!");
            return false;
        }

        if (args.length != 1) {
            Economy.getCommon().getMessages().sendWarn(sender, "Utilize: /setnpc <shop/goals>");
            return false;
        }

        switch (args[0]) {
            case "shop" -> {
                new ShopKeeperNPC().createNPC(player);
                Economy.getCommon().getMessages().sendSuccess(sender, "Npc ShopKeeper definido com Sucesso!");
                return true;
            }
            case "goals" -> { //TODO: IMPLEMENTAR!
                Economy.getCommon().getMessages().sendSuccess(sender, "Npc Goals definido com Sucesso!");
                return true;
            }
            default -> {
                Economy.getCommon().getMessages().sendWarn(sender, "Utilize: /setnpc <shop/goals>");
                return false;
            }
        }
    }
}
