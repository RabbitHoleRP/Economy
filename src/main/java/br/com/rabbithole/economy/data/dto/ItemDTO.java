package br.com.rabbithole.economy.data.dto;

public record ItemDTO(int itemID, String itemName, String itemStruct, double basePrice, double minPrice, double maxPrice, int baseStock) {
}
