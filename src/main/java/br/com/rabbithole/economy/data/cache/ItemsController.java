package br.com.rabbithole.economy.data.cache;

import br.com.rabbithole.economy.Economy;
import br.com.rabbithole.economy.data.dto.ItemDTO;
import br.com.rabbithole.economy.data.tables.ItemsData;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsController {
    private final Map<Integer, ItemDTO> itemsAvailable;

    public ItemsController() {
        this.itemsAvailable = new HashMap<>();
        this.insertAllItems();
    }

    private void insertAllItems() {
        List<ItemDTO> items = new ItemsData().getAllItems();
        Economy.getCommon().getMessages().sendCustomMessage(
                Bukkit.getConsoleSender(), "<green>Iniciando rotina de carregamento de Itens.");
        for (ItemDTO item : items) {
            this.itemsAvailable.put(item.itemID(), item);
            Economy.getCommon().getMessages().sendCustomMessage(
                    Bukkit.getConsoleSender(), "<green> %s inserido com Sucesso!".formatted(item.itemName()));
        }
        Economy.getCommon().getMessages().sendCustomMessage(
                Bukkit.getConsoleSender(), "<green>Rotina de carregamento de Itens concluída com Sucesso!");
    }

    public Map<Integer, ItemDTO> getItemsAvailable() {
        return itemsAvailable;
    }

    public boolean hasItemAvailable(int itemID) {
        return this.itemsAvailable.containsKey(itemID);
    }

    public ItemDTO getItem(int itemID) {
        return this.itemsAvailable.get(itemID);
    }
}
