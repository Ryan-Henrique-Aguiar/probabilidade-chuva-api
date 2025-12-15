package br.com.probabilidadechuva.repository;

import br.com.probabilidadechuva.util.CsvReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class CidadeRepository {

    private final CsvReader csvReader;

    @Value("${dados.caminho}")
    private String pastaDados;

    public CidadeRepository(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    public Map<LocalDate, List<Double>> buscarDadosAno(
            String cidade, int ano) throws IOException {

        return csvReader.lerDadosAno(pastaDados, cidade, ano);
    }
}
