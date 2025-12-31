package br.com.probabilidadechuva.dto;

import java.time.LocalDate;

public class TemperaturaDiariaDto {
    private LocalDate data;
    private double tempMedia;
    private double umdMedia;

    public TemperaturaDiariaDto(LocalDate data, double tempMedia, double umdMedia) {
        this.data = data;
        this.tempMedia = tempMedia;
        this.umdMedia = umdMedia;
    }

    public TemperaturaDiariaDto() {
    }

    public LocalDate getData() {
        return data;
    }

    public double getTempMedia() {
        return tempMedia;
    }

    public double getUmdMedia() {
        return umdMedia;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setTempMedia(double tempMedia) {
        this.tempMedia = tempMedia;
    }

    public void setUmdMedia(double umdMedia) {
        this.umdMedia = umdMedia;
    }
}
