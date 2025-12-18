package br.com.probabilidadechuva.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class CsvNameReader {
    private static final Map<String, String> ESTADO_PARA_REGIAO = Map.ofEntries(
            Map.entry("AC", "Norte"),
            Map.entry("AP", "Norte"),
            Map.entry("AM", "Norte"),
            Map.entry("PA", "Norte"),
            Map.entry("RO", "Norte"),
            Map.entry("RR", "Norte"),
            Map.entry("TO", "Norte"),

            Map.entry("AL", "Nordeste"),
            Map.entry("BA", "Nordeste"),
            Map.entry("CE", "Nordeste"),
            Map.entry("MA", "Nordeste"),
            Map.entry("PB", "Nordeste"),
            Map.entry("PE", "Nordeste"),
            Map.entry("PI", "Nordeste"),
            Map.entry("RN", "Nordeste"),
            Map.entry("SE", "Nordeste"),

            Map.entry("SP", "Sudeste"),
            Map.entry("MG", "Sudeste"),
            Map.entry("RJ", "Sudeste"),
            Map.entry("ES", "Sudeste"),

            Map.entry("PR", "Sul"),
            Map.entry("RS", "Sul"),
            Map.entry("SC", "Sul"),

            Map.entry("GO", "Centro_Oeste"),
            Map.entry("MT", "Centro_Oeste"),
            Map.entry("MS", "Centro_Oeste"),
            Map.entry("DF", "Centro_Oeste")
    );
    public static Map<String, Map<String, List<String>>> organizarPorRegiao(Map<String, List<String>> cidadesPorEstado) {
        Map<String, Map<String, List<String>>> resultado = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : cidadesPorEstado.entrySet()) {
            String estado = entry.getKey();
            List<String> cidades = entry.getValue();

            String regiao = ESTADO_PARA_REGIAO.getOrDefault(estado, "Outros");

            // garante que exista o mapa da região
            Map<String, List<String>> estadosDaRegiao = resultado.get(regiao);
            if (estadosDaRegiao == null) {
                estadosDaRegiao = new HashMap<>();
                resultado.put(regiao, estadosDaRegiao);
            }

            // adiciona cidades ao estado
            estadosDaRegiao.put(estado, cidades);
        }

        return resultado;
    }

    public Map<String, List<String>> extrairCidadesPorEstado(String pastaDados) {

        Map<String, List<String>> cidadesPorEstado = new HashMap<>();
        File pasta = new File(pastaDados);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.err.println("Erro: Pasta de dados não encontrada ou não é um diretório: " + pastaDados);
            return cidadesPorEstado;
        }

        File[] arquivos = pasta.listFiles();
        if (arquivos == null) return cidadesPorEstado;

        for (File arquivo : arquivos) {
            if (arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".csv")) {
                String[] partes = arquivo.getName().split("_");
                if (partes.length > 4) {
                    String estado = partes[2];
                    String cidade = partes[4];

                    cidadesPorEstado.putIfAbsent(estado, new ArrayList<>());
                    List<String> lista = cidadesPorEstado.get(estado);
                    if (!lista.contains(cidade)) {
                        lista.add(cidade);
                    }
                }
            }
        }

        return cidadesPorEstado;
    }
    public ArrayList<String> extrairNomesDasCidades(String pastaDados) {

        // Usamos um Set para garantir que cada cidade apareça apenas uma vez
        Set<String> cidadesUnicas = new HashSet<>();

        File pasta = new File(pastaDados);

        // 1. Verificar se a pasta existe e é um diretório
        if (!pasta.exists() || !pasta.isDirectory()) {
            System.err.println("Erro: Pasta de dados não encontrada ou não é um diretório: " + pastaDados);
            return new ArrayList<>(cidadesUnicas); // Retorna lista vazia
        }

        // 2. Listar todos os arquivos no diretório
        File[] arquivos = pasta.listFiles();

        if (arquivos == null) {
            System.err.println("Erro: Não foi possível listar os arquivos na pasta: " + pastaDados);
            return new ArrayList<>(cidadesUnicas);
        }

        // 3. Iterar sobre os arquivos e extrair o nome da cidade
        for (File arquivo : arquivos) {

            // Ignorar subdiretórios, apenas processar arquivos
            if (arquivo.isFile()) {
                String nomeArquivo = arquivo.getName();

                // 4. Filtrar por arquivos CSV
                if (nomeArquivo.toLowerCase().endsWith(".csv")) {

                    // Exemplo: INMET_CO_DF_A001_BRASILIA_01-01-2025_A_30-11-2025.csv
                    // O separador é o underscore '_'
                    String[] partes = nomeArquivo.split("_");

                    // 5. Verificar se o arquivo tem o formato esperado
                    // O nome da cidade é o 5º campo, então precisamos de pelo menos 5 campos (índice 0 a 4)
                    if (partes.length > 4) {

                        String nomeCidade = partes[4];

                        // O nome da cidade geralmente está em maiúsculas (ex: BRASILIA)
                        // Você pode normalizar o nome, se necessário (ex: Capitalizar)
                        // Para manter a consistência com o nome do arquivo, vamos manter como está.

                        cidadesUnicas.add(nomeCidade);
                    }
                }
            }
        }

        // 6. Converter o Set (sem duplicatas) para uma Lista e retornar
        return new ArrayList<>(cidadesUnicas);
    }
}
