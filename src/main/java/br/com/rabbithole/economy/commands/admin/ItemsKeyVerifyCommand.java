package br.com.rabbithole.economy.commands.admin;

import br.com.rabbithole.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class ItemsKeyVerifyCommand implements CommandExecutor {

    public ItemsKeyVerifyCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("verificar"));
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Economy.getCommon().getMessages().sendError(sender, "Apenas jogadores podem executar este Comando!");
            return false;
        }

        if (!player.hasPermission("economy.admin")) {
            Economy.getCommon().getMessages().sendError(player, "Você não tem permissão para executar este Comando!");
            return false;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            Economy.getCommon().getMessages().sendError(player, "Você deve segurar um item na mão!");
            return false;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta()) {
            Economy.getCommon().getMessages().sendError(player, "Este item não possui um Meta data.");
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        Set<NamespacedKey> keys = itemMeta.getPersistentDataContainer().getKeys();

        Economy.getCommon().getMessages().sendSuccess(player, "Lista de Chaves encontradas no Item: ");

        for (NamespacedKey key : keys) {
            Economy.getCommon().getMessages().sendSuccess(player, "- " + key.getKey());
        }
        return true;
    }
}
