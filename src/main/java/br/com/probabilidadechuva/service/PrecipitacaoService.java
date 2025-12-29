package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.dto.ChuvaDiariaDto;
import br.com.probabilidadechuva.dto.ClassficacaoChuvaDto;
import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class PrecipitacaoService {

    private final CidadeRepository repository;

    // Construtor injetando o repositório de dados de cidades
    public PrecipitacaoService(CidadeRepository repository) {
        this.repository = repository;
    }


    /**
     * Lê os dados de chuva de um determinado ano para uma cidade.
     * @param cidade Nome da cidade
     * @param ano Ano a ser buscado
     * @return Map<LocalDate, List<Double>> contendo listas de valores de chuva por dia
     */
    public Map<LocalDate,List<Double>> lerdadosano(String cidade, int ano) throws IOException {
        return repository.buscarDadosAno(cidade,ano);
    }

    /**
     * Calcula a média diária de chuva para cada dia de um ano específico.
     * @param cidade Nome da cidade
     * @param ano Ano a ser calculado
     * @return Map<LocalDate, Double> com média diária de chuva
     */
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

    /**
     * Gera uma lista de DTOs com as médias diárias de chuva,
     * pronta para envio ao frontend.
     */
    public List<ChuvaDiariaDto> mediasDiariasDTO(
            String cidade, int ano) throws IOException {

        Map<LocalDate, Double> medias =
                mediasDiarias(cidade, ano);

        List<ChuvaDiariaDto> lista = new ArrayList<>();

        medias.forEach((data, valor) -> {
            // Converte cada média em DTO com data e valor
            lista.add(new ChuvaDiariaDto(
                    data.toString(),
                    valor
            ));
        });

        return lista;
    }
    /**
     * Classifica a chuva de cada dia de um ano em categorias
     * (Sem chuva, fraca, moderada, forte, muito forte).
     */
    public List<ClassficacaoChuvaDto> classificarChuva(String cidade, int ano) throws IOException {

        Map<LocalDate, List<Double>> dados = repository.buscarDadosAno(cidade, ano);

        List<ClassficacaoChuvaDto> resultado = new ArrayList<>();

        dados.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())  // ordena por data
                .forEach(entry -> {

                    double totalDia = entry.getValue()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    double maxHorario = entry.getValue()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .max()
                            .orElse(0);

                    // Define a classificação da chuva
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
                });

        return resultado;}

    /**
     * Classe auxiliar para acumular valores de chuva durante
     * a média de vários anos.
     */
    public class Acumulador {

        double somaTotalDia = 0;
        double somaMaxHorario = 0;
        int quantidade = 0;
    }
    /**
     * Calcula a média da classificação de chuva para cada dia/mês
     * considerando todos os anos disponíveis (2000–ano atual).
     */

    public List<ClassficacaoChuvaDto> mediaClassificacaoDeChuva(String cidade) throws IOException {

        int anoAtual = LocalDate.now().getYear();

        Map<LocalDate, Acumulador> mapa = new HashMap<>();

        // Percorre todos os anos para acumular dados
        for (int ano = anoAtual; ano >= 2000; ano--) {

            List<ClassficacaoChuvaDto> lista = classificarChuva(cidade, ano);


            for (ClassficacaoChuvaDto dto : lista) {

                LocalDate dataOriginal = dto.getData();

                // data SEM ano (ano fixo)
                LocalDate dataSemAno = LocalDate.of(
                        2000,
                        dataOriginal.getMonth(),
                        dataOriginal.getDayOfMonth()
                );

                Acumulador acc = mapa.getOrDefault(dataSemAno, new Acumulador());

                acc.somaTotalDia += dto.getTotalDia();
                acc.somaMaxHorario += dto.getMaxHorario();
                acc.quantidade++;

                mapa.put(dataSemAno, acc);
            }
        }

                    /*
              Atualiza a lista de médias:
              - Calcula totalDia médio
              - Calcula maxHorario médio
              - Define classificação baseado no totalDia
            */
        // Criar lista FINAL com médias
        List<ClassficacaoChuvaDto> resultado = new ArrayList<>();

        for (Map.Entry<LocalDate, Acumulador> entry : mapa.entrySet()) {

            Acumulador acc = entry.getValue();

            // Atualiza o DTO com os valores médios
            ClassficacaoChuvaDto media = new ClassficacaoChuvaDto();
            media.setData(entry.getKey());
            double totaldia = acc.somaTotalDia/acc.quantidade;
            media.setTotalDia(totaldia);
            media.setMaxHorario(acc.somaMaxHorario / acc.quantidade);

            String classificacao;

            if (totaldia == 0) {
                classificacao = "Sem chuva";
            } else if (totaldia < 2.5) {
                classificacao = "Chuva fraca";
            } else if (totaldia < 10) {
                classificacao = "Chuva moderada";
            } else if (totaldia < 50) {
                classificacao = "Chuva forte";
            } else {
                classificacao = "Chuva muito forte";
            }
            media.setClassificacao(classificacao);

            resultado.add(media);
        }
        // Ordena a lista por data (dia/mês)
        resultado.sort(Comparator.comparing(ClassficacaoChuvaDto::getData));
        return resultado;
    }

    /**
     * Calcula a probabilidade de chuva em um período de 5 dias
     * a partir de uma data inicial (dia/mês) considerando todos os anos.
     */
    public double probabilidadeChuva5Dias(
            String cidade,
            String diaInicios,
            String mesInicios) throws IOException {

        int diasComChuva = 0;
        int totalDias = 0;

        int diaInicio = Integer.parseInt(diaInicios);
        int mesInicio = Integer.parseInt(mesInicios);


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

        // Calcula a probabilidade composta
        double p = (double) diasComChuva / totalDias;

        return 1 - Math.pow(1 - p, 5);
    }
}
