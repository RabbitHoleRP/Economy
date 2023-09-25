package br.com.rabbithole.economy.data.dto;

public record TransactionDTO(String playerSender, String playerReceiver, double amount) {
}
