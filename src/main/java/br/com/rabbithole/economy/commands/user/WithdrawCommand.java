package br.com.rabbithole.economy.commands.user;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.dto.CoinBagDTO;
import br.com.rabbithole.economy.data.tables.CoinBagData;
import br.com.rabbithole.economy.utils.InventoryUtils;
import br.com.rabbithole.economy.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class WithdrawCommand implements CommandExecutor {
    public WithdrawCommand() {
        PluginCommand command = Objects.requireNonNull(Bukkit.getPluginCommand("sacar"));
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            Economy.getCommon().getMessages().sendError(sender, "Apenas jogadores podem executar este Comando!");
            return false;
        }

        if (args.length != 1) {
            Economy.getCommon().getMessages().sendWarn(player, "Utilize: /sacar <Valor>.");
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[0]);
        } catch (Exception exception) {
            Economy.getCommon().getMessages().sendError(player, "Você deve inserir apenas Números!");
            return false;
        }

        if (amount <=0) {
            Economy.getCommon().getMessages().sendError(player, "Você deve inserir um valor maior que 0!");
            return false;
        }

        if (!Economy.getCache().hasRequiredBalance(player.getName(), amount)) {
            Economy.getCommon().getMessages().sendError(player, "Você não possuí saldo Suficiente!");
            return false;
        }

        if (!InventoryUtils.hasEmptySlot(player.getInventory())) {
            Economy.getCommon().getMessages().sendError(player, "Você precisa liberar espaço no seu Inventário!");
            return false;
        }

        CoinBagDTO coinBag = new CoinBagDTO(UUID.randomUUID().toString(), player.getName(), amount);
        CoinBagData coinBagData = new CoinBagData();
        if (!coinBagData.createCoinBag(coinBag.bag_id(), coinBag.playerName(), coinBag.amount())) {
            Economy.getCommon().getMessages().sendError(player, "Erro ao criar Bolsa de Moedas!");
            return false;
        }

        Economy.getCache().updatePlayerBalanceInCache(player.getName(), Economy.getCache().getPlayerBalanceInCache(player.getName()) - amount);
        player.getInventory().addItem(ItemUtils.createCoinBag(coinBag.bag_id(), coinBag.playerName(), coinBag.amount()));
        Economy.getCommon().getMessages().sendSuccess(player, "Bolsa de Moedas criada com Sucesso!");
        return true;
    }
}
