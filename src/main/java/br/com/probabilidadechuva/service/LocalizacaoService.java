package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.repository.CidadeRepository;
import br.com.probabilidadechuva.util.CsvNameReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocalizacaoService {
    private final CsvNameReader csvNameReader;

    @Value("${dados2025.caminho}")
    private String pastaDados2025;

    public LocalizacaoService(CsvNameReader csvNameReader) {
        this.csvNameReader = csvNameReader;

    }
    public ArrayList<String> listarCidades(){
        return csvNameReader.extrairNomesDasCidades(pastaDados2025);
    }

    public Map<String, List<String>> listarCidadesPorEstado() {
        return csvNameReader.extrairCidadesPorEstado(pastaDados2025);
    }
}
