package br.com.rabbithole.economy.utils;

import br.com.rabbithole.common.utils.SkullUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    private static final String KEY_NAME = "economy";

    public static ItemStack createCoinBag(String bagID,String playerName, double amount) {
        ItemStack coinBag = SkullUtils.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVmZDY3ZDU2ZmZjNTNmYjM2MGExNzg3OWQ5YjUzMzhkNzMzMmQ4ZjEyOTQ5MWE1ZTE3ZThkNmU4YWVhNmMzYSJ9fX0=");
        ItemMeta coinBagMeta = coinBag.getItemMeta();
        coinBagMeta.displayName(StringUtils.formatString("<gold><bold>Bolsa de Moedas<reset>").decoration(TextDecoration.ITALIC, false));
        List<Component> coinBagLore = new ArrayList<>();
        coinBagLore.add(StringUtils.formatString(""));
        coinBagLore.add(StringUtils.formatString("<grey>Essa bolsa contém <green>%.2f¢</green>.".formatted(amount)).decoration(TextDecoration.ITALIC, false));
        coinBagLore.add(StringUtils.formatString(""));
        coinBagLore.add(StringUtils.formatString("<dark_grey>(Clique com botão direito para resgatar)").decoration(TextDecoration.ITALIC, false));
        coinBagMeta.lore(coinBagLore);

        PersistentDataContainer dataContainer = coinBagMeta.getPersistentDataContainer();
        dataContainer.set(new NamespacedKey(KEY_NAME, "bag_id"), PersistentDataType.STRING, bagID);
        dataContainer.set(new NamespacedKey(KEY_NAME, "player_name"), PersistentDataType.STRING, playerName);
        dataContainer.set(new NamespacedKey(KEY_NAME, "amount"), PersistentDataType.DOUBLE, amount);
        coinBag.setItemMeta(coinBagMeta);
        return coinBag;
    }
}
