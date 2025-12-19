package br.com.probabilidadechuva.dto;

public class ChuvaDiariaDto {
    private String data;
    private double valor;

    public ChuvaDiariaDto(String data, double valor) {
        this.data = data;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }
}
