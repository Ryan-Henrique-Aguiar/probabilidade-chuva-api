package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PrecipitacaoService {

    private final CidadeRepository repository;

    public PrecipitacaoService(CidadeRepository repository) {
        this.repository = repository;
    }
    public Map<LocalDate,List<Double>> lerdadosano(String cidade, int ano) throws IOException {
        return repository.buscarDadosAno(cidade,ano);
    }

    public Map<LocalDate, Double> mediasDiarias(
            String cidade, int ano) throws IOException {

        Map<LocalDate, List<Double>> dados =
                repository.buscarDadosAno(cidade, ano);

        Map<LocalDate, Double> medias = new TreeMap<>();

        for (var entry : dados.entrySet()) {
            double media = entry.getValue()
                    .stream()
                    .mapToDouble(x -> x)
                    .average()
                    .orElse(0);

            medias.put(entry.getKey(), media);
        }

        return medias;
    }
    public Map<LocalDate,String> classificarChuva(String cidade,int ano) throws IOException{
        Map<LocalDate,List<Double>> dados = repository.buscarDadosAno(cidade,ano);
        Map<LocalDate,String>diasClassificados = new TreeMap<>();

        for (var entry : dados.entrySet()) {

            double totalDia = entry.getValue()
                    .stream()
                    .mapToDouble(x -> x)
                    .sum();

            String classificacao;

            if (totalDia == 0) {
                classificacao = "Sem chuva";
            } else if (totalDia < 2.5) {
                classificacao = "Chuva fraca";
            } else if (totalDia < 10) {
                classificacao = "Chuva moderada";
            } else if (totalDia < 50) {
                classificacao = "Chuva forte";
            } else {
                classificacao = "Chuva muito forte";
            }
            diasClassificados.put(entry.getKey(), classificacao);
        }

        return diasClassificados;
    }
}
