package br.com.rabbithole.economy.enums;

public enum TransactionsType {
    PLAYER_TO_PLAYER(1),
    SELLER_TO_PLAYER(2),
    MARKET_TO_PLAYER(3),
    ADMIN_TO_PLAYER(4);

    private final int typeID;

    TransactionsType(int typeID) {
        this.typeID = typeID;
    }

    public int getTypeID() {
        return typeID;
    }
}
