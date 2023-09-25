package br.com.rabbithole.economy;

//TODO: IMPLEMENTAR!
public final class EconomyAPI {

    /**
     * Método para retornar saldo em conta de Jogador.
     *
     * @param playerName - Nome do Jogador.
     * @return saldo do Jogador.
     */
    public double getBalance(String playerName) {
        return 0.0;
    }

    /**
     * Método para adicionar saldo a conta de Jogador.
     *
     * @param playerName - Nome do Jogador.
     * @param amount - Valor para adicionar.
     * @return "true" caso a ação seja executada | "false" caso a ação não seja executada.
     */
    public boolean addBalance(String playerName, double amount) {
        return false;
    }

    /**
     * Método para remover saldo de Jogador.
     *
     * @param playerName - Nome do Jogador.
     * @param amount - Valor para retirar.
     * @return "true" caso a ação seja executada | "false" caso a ação não seja executada.
     */
    public boolean removeBalance(String playerName, double amount) {
        return false;
    }

    /**
     * Método para verificar se Jogador possui saldo.
     *
     * @param playerName - Nome do Jogador.
     * @param amount - Valor a ser verificado.
     * @return "true" caso jogador tenha o valor | "false" caso o jogador não tenha o valor.
     */
    public boolean hasBalance(String playerName, double amount) {
        return false;
    }

    /**
     * Método para retornar o Nome do Jogador magnata do servidor.
     *
     * @return nome do Jogador.
     */
    public String getTycoon() {
        return "";
    }
}
