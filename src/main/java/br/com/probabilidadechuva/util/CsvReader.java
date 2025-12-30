package br.com.probabilidadechuva.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe responsável por ler arquivos CSV de dados climáticos
 * e agrupar valores por dia.
 */
@Component
public class CsvReader {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public CsvReader() {}

    /**
     * Lê os dados de um determinado ano e cidade,
     * agrupando os valores por data.
     * @throws IOException Caso a pasta ou arquivo não seja encontrado
     */
    public Map<LocalDate, List<Double>> lerDadosAno(
            String pastaDados, String cidade, int ano) throws IOException {

        // Mapa que armazenará os valores agrupados por dia
        Map<LocalDate, List<Double>> dadosPorDia = new HashMap<>();

        // Pasta correspondente ao ano
        File pastaAno = new File(pastaDados + "/" + ano);

        // Verifica se a pasta do ano existe
        if (!pastaAno.exists() || !pastaAno.isDirectory()) {
            throw new IOException(
                    "Pasta do ano não encontrada: " + pastaAno.getAbsolutePath()
            );
        }

        File arquivoCidade = null;

        // Procura o arquivo CSV que contém o nome da cidade
        for (File f : Objects.requireNonNull(pastaAno.listFiles())) {
            if (f.getName().toLowerCase()
                    .contains(cidade.toLowerCase())) {
                arquivoCidade = f;
                break;
            }
        }

        // Se não encontrar o arquivo, lança erro
        if (arquivoCidade == null) {
            throw new IOException(
                    "Arquivo da cidade não encontrado para "
                            + cidade + " em " + ano
            );
        }

        // Leitura do arquivo CSV
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(arquivoCidade),
                        StandardCharsets.UTF_8))) {

            String linha;
            boolean iniciouTabela = false;

            // Lê o arquivo linha por linha
            while ((linha = br.readLine()) != null) {

                // Identifica o início da tabela
                if (linha.startsWith("Data")) {
                    iniciouTabela = true;
                    continue;
                }

                // Ignora linhas antes da tabela
                if (!iniciouTabela) continue;

                // Divide a linha por TAB ou ponto e vírgula
                String[] partes = linha.split("\t|;");

                // Verifica se há colunas suficientes
                if (partes.length < 3) continue;

                // Converte a data
                LocalDate data =
                        LocalDate.parse(partes[0].trim(), formatter);

                // Converte o valor numérico
                double valor;
                try {
                    valor = Double.parseDouble(
                            partes[2].replace(",", ".").trim()
                    );
                } catch (NumberFormatException e) {
                    // Ignora valores inválidos
                    continue;
                }

                // Agrupa os valores por dia
                dadosPorDia.putIfAbsent(data, new ArrayList<>());
                dadosPorDia.get(data).add(valor);
            }
        }

        return dadosPorDia;
    }
}
