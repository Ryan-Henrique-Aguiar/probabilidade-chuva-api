package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TemperaturaService {
    private final CidadeRepository cidadeRepository;

    public TemperaturaService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Map<LocalDate, List<List<Double>>> lerdadostemp(String cidade, int ano) throws IOException {
        return cidadeRepository.buscarDadosAnoTemp(cidade,ano);
    }
    public Map<LocalDate,List<Double>> mediasTemp(String cidade, int ano) throws IOException{
        Map<LocalDate,List<List<Double>>> dados = cidadeRepository.buscarDadosAnoTemp(cidade,ano);
        Map<LocalDate,List<Double>> medias = new TreeMap<>();

        for (var entry :dados.entrySet()){
            double mediaTemp = entry.getValue()
                    .stream()
                    .mapToDouble(x -> x.get(1))
                    .average()
                    .orElse(0);
            double mediaUmid = entry.getValue()
                            .stream()
                                    .mapToDouble(x -> x.get(2))
                                            .average()
                                                    .orElse(0);
            List<Double> listaDeMedias = Arrays.asList(mediaTemp, mediaUmid);
            medias.put(entry.getKey(),listaDeMedias);

        }
        return medias;
    }
}
