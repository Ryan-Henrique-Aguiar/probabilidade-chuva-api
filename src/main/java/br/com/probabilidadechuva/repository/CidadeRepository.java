package br.com.probabilidadechuva.repository;

import br.com.probabilidadechuva.util.CsvReader;
import br.com.probabilidadechuva.util.CsvTempReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CidadeRepository {

    private final CsvReader csvReader;
    private final CsvTempReader csvTempReader;

    @Value("${dados.caminho}")
    private String pastaDados;

    @Value("${dados2025.caminho}")
    private String pastaDados2025;


    public CidadeRepository(CsvReader csvReader, CsvTempReader csvTempReader) {
        this.csvReader = csvReader;
        this.csvTempReader = csvTempReader;
    }

    public Map<LocalDate, List<Double>> buscarDadosAno(
            String cidade, int ano) throws IOException {

        return csvReader.lerDadosAno(pastaDados, cidade, ano);
    }
    public Map<LocalDate, List<List<Double>>> buscarDadosAnoTemp(
            String cidade, int ano) throws  IOException{
        return csvTempReader.lerDadosAno(pastaDados,cidade,ano);
    }


}
