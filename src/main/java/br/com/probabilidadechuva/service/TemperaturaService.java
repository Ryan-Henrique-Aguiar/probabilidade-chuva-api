package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TemperaturaService {
    private final CidadeRepository cidadeRepository;

    public TemperaturaService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Map<LocalDate, List<List<Double>>> lerdadostemp() throws IOException {
        return cidadeRepository.buscarDadosAnoTemp("brasilia",2022);
    }
}
