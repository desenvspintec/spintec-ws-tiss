package br.com.spintec.wstiss.utils;

public class CalculoHashResultado {

    private String hashEntrada;
    private String hashValido;
    private boolean valido;

    public CalculoHashResultado(String hashEntrada, String hashValido, boolean valido) {
        this.hashEntrada = hashEntrada;
        this.hashValido = hashValido;
        this.valido = valido;
    }

    public String getHashEntrada() {
        return hashEntrada;
    }

    public String getHashValido() {
        return hashValido;
    }

    public boolean isValido() {
        return valido;
    }
}
