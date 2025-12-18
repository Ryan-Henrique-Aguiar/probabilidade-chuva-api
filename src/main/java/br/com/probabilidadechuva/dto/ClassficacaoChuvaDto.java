package br.com.probabilidadechuva.dto;

import java.time.LocalDate;

public class ClassficacaoChuvaDto {
    private LocalDate data;
    private double totalDia;
    private double maxHorario;
    private String classificacao;

    public ClassficacaoChuvaDto(LocalDate data, double totalDia, double maxHorario, String classificacao) {
        this.data = data;
        this.totalDia = totalDia;
        this.maxHorario = maxHorario;
        this.classificacao = classificacao;
    }

    public LocalDate getData() {
        return data;
    }

    public double getTotalDia() {
        return totalDia;
    }

    public double getMaxHorario() {
        return maxHorario;
    }

    public String getClassificacao() {
        return classificacao;
    }
}
