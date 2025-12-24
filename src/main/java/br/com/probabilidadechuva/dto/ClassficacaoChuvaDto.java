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
    public ClassficacaoChuvaDto(){

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

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setTotalDia(double totalDia) {
        this.totalDia = totalDia;
    }

    public void setMaxHorario(double maxHorario) {
        this.maxHorario = maxHorario;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }
}
