package br.com.probabilidadechuva.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CsvReader {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public CsvReader() {}

    public Map<LocalDate, List<Double>> lerDadosAno(String pastaDados, String cidade, int ano) throws IOException {

        Map<LocalDate, List<Double>> dadosPorDia = new HashMap<>();

        File pastaAno = new File(pastaDados + "/" + ano);

        if (!pastaAno.exists() || !pastaAno.isDirectory()) {
            throw new IOException("Pasta do ano não encontrada: " + pastaAno.getAbsolutePath());
        }

        File arquivoCidade = null;

        // encontra o arquivo CSV da cidade
        for (File f : Objects.requireNonNull(pastaAno.listFiles())) {
            if (f.getName().toLowerCase().contains(cidade.toLowerCase())) {
                arquivoCidade = f;
                break;
            }
        }

        if (arquivoCidade == null) {
            throw new IOException("Arquivo da cidade não encontrado para " + cidade + " em " + ano);
        }

        // leitura do CSV
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivoCidade), StandardCharsets.UTF_8))) {

            String linha;
            boolean iniciouTabela = false;

            while ((linha = br.readLine()) != null) {

                if (linha.startsWith("Data")) {
                    iniciouTabela = true;
                    continue;
                }

                if (!iniciouTabela) continue;

                String[] partes = linha.split("\t|;");
                if (partes.length < 3) continue;

                // converte data
                LocalDate data = LocalDate.parse(partes[0].trim(), formatter);

                double valor;
                try {
                    valor = Double.parseDouble(partes[2].replace(",", ".").trim());
                } catch (NumberFormatException e) {
                    continue;
                }

                // adiciona valores horários
                dadosPorDia.putIfAbsent(data, new ArrayList<>());
                dadosPorDia.get(data).add(valor);
            }
        }

        return dadosPorDia;
    }


}
