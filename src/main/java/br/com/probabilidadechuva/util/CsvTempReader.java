package br.com.probabilidadechuva.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Leitor de CSV para dados de temperatura (bulbo seco)
 * e umidade relativa do ar, agrupados por dia.
 */
@Component
public class CsvTempReader {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public CsvTempReader() {}

    /**
     * Lê dados horários de temperatura e umidade
     * de uma cidade em um determinado ano.
     * @return Mapa por dia contendo listas com:
     *         [hora, temperatura, umidade]
     */
    public Map<LocalDate, List<List<Double>>> lerDadosAno(
            String pastaDados, String cidade, int ano)
            throws IOException {

        Map<LocalDate, List<List<Double>>> dadosPorDia =
                new HashMap<>();

        File pastaAno = new File(pastaDados + "/" + ano);

        if (!pastaAno.exists() || !pastaAno.isDirectory()) {
            throw new IOException(
                    "Pasta do ano não encontrada: "
                            + pastaAno.getAbsolutePath()
            );
        }

        File arquivoCidade = null;

        // Procura o arquivo da cidade
        for (File f : Objects.requireNonNull(pastaAno.listFiles())) {
            if (f.getName().toLowerCase()
                    .contains(cidade.toLowerCase())) {
                arquivoCidade = f;
                break;
            }
        }

        if (arquivoCidade == null) {
            throw new IOException(
                    "Arquivo da cidade não encontrado para "
                            + cidade + " em " + ano
            );
        }

        // Leitura do CSV
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(arquivoCidade),
                        StandardCharsets.UTF_8))) {

            String linha;
            boolean iniciouTabela = false;

            while ((linha = br.readLine()) != null) {

                // Detecta início da tabela
                if (linha.startsWith("Data")) {
                    iniciouTabela = true;
                    continue;
                }

                if (!iniciouTabela) continue;

                String[] partes = linha.split("\t|;");

                if (partes.length < 15) continue;

                // Data
                LocalDate data = LocalDate.parse(
                        partes[0].trim(), formatter);

                // Hora UTC → somente hora (00, 01, 02...)
                String horaStr = partes[1].substring(0, 2);
                double hora = Double.parseDouble(horaStr);

                double temperatura;
                double umidade;

                try {
                    temperatura = Double.parseDouble(
                            partes[7].replace(",", ".").trim());

                    umidade = Double.parseDouble(
                            partes[14].replace(",", ".").trim());
                } catch (NumberFormatException e) {
                    continue;
                }

                // Lista: [hora, temperatura, umidade]
                List<Double> registro = new ArrayList<>();
                registro.add(hora);
                registro.add(temperatura);
                registro.add(umidade);

                dadosPorDia.putIfAbsent(
                        data, new ArrayList<>());
                dadosPorDia.get(data).add(registro);
            }
        }

        return dadosPorDia;
    }
}
