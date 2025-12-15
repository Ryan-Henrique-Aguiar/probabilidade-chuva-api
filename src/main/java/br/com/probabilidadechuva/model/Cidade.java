package br.com.probabilidadechuva.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Cidade {
    private String nome;
    private Map<LocalDate, List<Double>> precipitacoes;

    public Cidade(String nome, Map<LocalDate, List<Double>> precipitacoes) {
        this.nome = nome;
        this.precipitacoes = precipitacoes;
    }

    public String getNome() {
        return nome;
    }

    public Map<LocalDate, List<Double>> getPrecipitacoes() {
        return precipitacoes;
    }
}
