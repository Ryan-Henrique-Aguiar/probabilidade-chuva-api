package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.dto.ClassficacaoChuvaDto;
import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<ClassficacaoChuvaDto> classificarChuva(String cidade,int ano) throws IOException{

        Map<LocalDate,List<Double>> dados = repository.buscarDadosAno(cidade,ano);

        List<ClassficacaoChuvaDto> resultado = new ArrayList<>();

        for (var entry : dados.entrySet()) {

            double totalDia = entry.getValue()
                    .stream()
                    .mapToDouble(x -> x)
                    .sum();

            double maxHorario = entry.getValue()
                    .stream()
                    .mapToDouble(x -> x)
                    .max()
                    .orElse(0);

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


            resultado.add(
                    new ClassficacaoChuvaDto(
                            entry.getKey(),
                            totalDia,
                            maxHorario,
                            classificacao
                    )
            );
        }
        return resultado;
    }
    public double probabilidadeChuva5Dias(
            String cidade,
            int diaInicio,
            int mesInicio) throws IOException {

        int diasComChuva = 0;
        int totalDias = 0;

        // varre todos os anos disponíveis (2000–2025)
        for (int ano = 2000; ano <= 2025; ano++) {

            Map<LocalDate, List<Double>> dadosAno;

            try {
                dadosAno = repository.buscarDadosAno(cidade, ano);
            } catch (IOException e) {
                // cidade não tem dados nesse ano → ignora
                continue;
            }

            LocalDate dataInicial = LocalDate.of(ano, mesInicio, diaInicio);

            for (int i = 0; i < 5; i++) {
                LocalDate data = dataInicial.plusDays(i);

                if (!dadosAno.containsKey(data)) continue;

                double totalDia = dadosAno.get(data)
                        .stream()
                        .mapToDouble(x -> x)
                        .sum();

                if (totalDia >= 1) {
                    diasComChuva++;
                }
                totalDias++;
            }
        }

        if (totalDias == 0) return 0;

        double p = (double) diasComChuva / totalDias;

        return 1 - Math.pow(1 - p, 5);
    }
}
