package br.com.rabbithole.economy.commands.admin;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.dto.ItemDTO;
import br.com.rabbithole.economy.data.tables.ItemsData;
import br.com.rabbithole.economy.utils.SerializationUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ItemsCommand implements CommandExecutor {

    public ItemsCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("itens"));
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Economy.getCommon().getMessages().sendError(sender, "Apenas jogadores podem executar este Comando!");
            return false;
        }

        if (!player.hasPermission("economy.admin")) {
            Economy.getCommon().getMessages().sendError(player, "Você não tem permissão para executar este Comando!");
            return false;
        }

        if (args.length < 1) {
            sendHelpAction(player);
            return false;
        }

        switch (args[0]) {
            case "lista" -> sendListAction(player);
            case "adicionar" -> sendAddAction(player, args);
            case "editar" -> sendEditAction(player, args);
            case "remover" -> sendRemoveAction(player, args);
            default -> sendHelpAction(player);
        }
        return true;
    }

    private void sendHelpAction(Player player) {
        Economy.getCommon().getMessages().sendWarn(player, "Lista de Comandos: ");
        Economy.getCommon().getMessages().sendWarn(player, "");
        Economy.getCommon().getMessages().sendWarn(player, "/itens lista -> Mostra a lista de Itens registrados.");
        Economy.getCommon().getMessages().sendWarn(player, "/itens adicionar <preço inicial> <estoque>. (Você deve ter um item na Mão!)");
        Economy.getCommon().getMessages().sendWarn(player, "/itens editar <id do item> <preço/estoque> <novo valor>.");
        Economy.getCommon().getMessages().sendWarn(player, "/itens remover <id do item>.");
        Economy.getCommon().getMessages().sendWarn(player, "");
    }

    private void sendListAction(Player player) {
        List<ItemDTO> items = new ItemsData().getAllItems();
        if (items.isEmpty()) {
            Economy.getCommon().getMessages().sendWarn(player, "Nenhum item foi adicionado ao Sistema.");
            return;
        }

        Economy.getCommon().getMessages().sendSuccess(player, "Lista de Itens:");
        for (ItemDTO item : items) {
            Economy.getCommon().getMessages().sendSuccess(player, "%d: %s | V.Base: %.2f | V.Mínimo: %.2f | V.Máximo: %.2f  | Estoque: %d".formatted(
                    item.itemID(),
                    item.itemName(),
                    item.basePrice(),
                    item.minPrice(),
                    item.maxPrice(),
                    item.baseStock()
            ));
            Economy.getCommon().getMessages().sendSuccess(player, "");
        }
    }

    private void sendAddAction(Player player, String[] args) {
        if (args.length != 3) {
            Economy.getCommon().getMessages().sendWarn(player, "Utilize: /itens adicionar <preço inicial> <estoque>.");
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            Economy.getCommon().getMessages().sendError(player, "Você deve segurar o Item que deseja adicionar em sua Mão!");
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        double basePrice;
        int baseStock;
        try {
            basePrice = Double.parseDouble(args[1]);
            baseStock = Integer.parseInt(args[2]);
        } catch (Exception exception) {
            Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
            return;
        }
        String customName;
        if (itemStack.getItemMeta().hasDisplayName()) {
            customName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(itemStack.getItemMeta().displayName()));
        } else {
            customName = itemStack.getType().name();
        }

        String itemStruct = SerializationUtils.toBase64(itemStack);
        double minPrice = basePrice/10;
        double maxPrice = basePrice*10;

        if (!new ItemsData().addItem(
                new ItemDTO(
         0,
                customName,
                itemStruct,
                basePrice,
                minPrice,
                maxPrice,
                baseStock)
        )) {
            Economy.getCommon().getMessages().sendError(player, "Erro ao inserir novo Item!");
            return;
        }

        Economy.getCommon().getMessages().sendSuccess(player, "Item inserido com Sucesso!");
    }

    private void sendEditAction(Player player, String[] args) {
        if (args.length != 4) {
            Economy.getCommon().getMessages().sendWarn(player, "Utilize: /itens editar <id do item> <preço/estoque> <novo valor>.");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
            return;
        }

        String editOption = args[2];

        if (!new ItemsData().hasItem(itemId)) {
            Economy.getCommon().getMessages().sendError(player, "O Item não foi encontrado!");
            return;
        }

        if (editOption.equals("preço")) {
            try {
                double newPrice = Double.parseDouble(args[3]);
                if (!new ItemsData().updatePrice(itemId, newPrice)) {
                    Economy.getCommon().getMessages().sendError(player, "Erro ao atualizar Preço do Item!");
                    return;
                }
            } catch (Exception exception) {
                Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
                return;
            }
        } else if (editOption.equals("estoque")) {
            try {
                int newStock = Integer.parseInt(args[3]);
                if (!new ItemsData().updateStock(itemId, newStock)) {
                    Economy.getCommon().getMessages().sendError(player, "Erro ao atualizar Estoque do Item!");
                    return;
                }
            } catch (Exception exception) {
                Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
                return;
            }
        } else {
            Economy.getCommon().getMessages().sendError(player, "Você deve escolher uma Opção Válida! (preço/estoque)");
            return;
        }

        Economy.getCommon().getMessages().sendSuccess(player, "Item atualizado com Sucesso!");
    }

    private void sendRemoveAction(Player player, String[] args) {
        if (args.length != 2) {
            Economy.getCommon().getMessages().sendWarn(player, "Utilize: /itens remover <id do item>.");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(args[1]);
        } catch (Exception exception) {
            Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
            return;
        }

        if (!new ItemsData().hasItem(itemId)) {
            Economy.getCommon().getMessages().sendError(player, "O Item não foi encontrado!");
            return;
        }

        if (!new ItemsData().removeItem(args[1])) {
            Economy.getCommon().getMessages().sendError(player, "Erro ao remover Item!");
            return;
        }

        Economy.getCommon().getMessages().sendSuccess(player, "Item removido com Sucesso!");
    }
}
