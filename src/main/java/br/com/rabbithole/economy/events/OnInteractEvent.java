package br.com.rabbithole.economy.events;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.tables.CoinBagData;
import br.com.rabbithole.economy.data.tables.DuplicatedBagsData;
import br.com.rabbithole.economy.data.tables.TransactionData;
import br.com.rabbithole.economy.enums.TransactionsType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class OnInteractEvent implements Listener {
    final Plugin plugin;

    public OnInteractEvent(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (!event.getMaterial().equals(Material.PLAYER_HEAD)) return;
        if (!Objects.requireNonNull(event.getItem()).hasItemMeta()) return;
        Player player = event.getPlayer();

        ItemStack coinBag = event.getItem();
        PersistentDataContainer data = coinBag.getItemMeta().getPersistentDataContainer();
        if (!data.has(new NamespacedKey("economy", "bag_id"))) return;
        Double amount = data.get(new NamespacedKey("economy", "amount"), PersistentDataType.DOUBLE);
        String playerSender = data.get(new NamespacedKey("economy", "player_name"), PersistentDataType.STRING);

        if (amount == null) {
            Economy.getCommon().getMessages().sendError(player, "Bolsa de Moedas inválida!");
            return;
        }

        CoinBagData coinBagData = new CoinBagData();
        String bagId = data.get(new NamespacedKey("economy", "bag_id"), PersistentDataType.STRING);

        if (!coinBagData.hasCoinBag(bagId)) {
            Economy.getCommon().getMessages().sendError(player, "Sua Bolsa de Moedas é um Item Duplicado!");
            new DuplicatedBagsData().insertDuplicatedBag(bagId, player.getName());
            coinBag.subtract();
            return;
        }

        if (!new CoinBagData().deleteCoinBag(data.get(new NamespacedKey("economy", "bag_id"), PersistentDataType.STRING))) {
            Economy.getCommon().getMessages().sendError(player, "Erro ao resgatar sua Bolsa de Moedas!");
            return;
        }

        Economy.getCache().updatePlayerBalanceInCache(player.getName(), Economy.getCache().getPlayerBalanceInCache(player.getName()) + amount);
        new TransactionData().insertTransaction(playerSender, player.getName(), amount, TransactionsType.PLAYER_TO_PLAYER);
        Economy.getCommon().getMessages().sendSuccess(player, "Bolsa de Moedas resgatada com Sucesso!");
        coinBag.subtract();
    }
}
