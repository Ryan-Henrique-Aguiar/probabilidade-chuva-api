package br.com.probabilidadechuva.service;

import br.com.probabilidadechuva.dto.TemperaturaDiariaDto;
import br.com.probabilidadechuva.repository.CidadeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class TemperaturaService {
    private final CidadeRepository cidadeRepository;

    public TemperaturaService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public Map<LocalDate, List<List<Double>>> lerdadostemp(String cidade, int ano) throws IOException {
        return cidadeRepository.buscarDadosAnoTemp(cidade,ano);
    }
    public List<TemperaturaDiariaDto> mediasTemp(String cidade, int ano) throws IOException{
        Map<LocalDate,List<List<Double>>> dados = cidadeRepository.buscarDadosAnoTemp(cidade,ano);
        List<TemperaturaDiariaDto>medias = new ArrayList<>();

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

            medias.add(new TemperaturaDiariaDto(entry.getKey(),mediaTemp,mediaUmid));
        }
        medias.sort(Comparator.comparing(TemperaturaDiariaDto::getData));

        return medias;
    }
    private static class Acumuladortemp{
        double somatotaltemp;
        double somatotalumid;
        int quantidade;
    }

    public List<TemperaturaDiariaDto> mediaTotalTempUmid(String cidade){
        int anoAtual = LocalDate.now().getYear();

        Map<LocalDate, Acumuladortemp> mapa = new HashMap<>();

        for (int ano = anoAtual;ano>=2000;ano--){
            List<TemperaturaDiariaDto>lista = new ArrayList<>();
            try {
                lista = mediasTemp(cidade, ano);
            }catch (IOException e) {
                continue;
            }
            for (TemperaturaDiariaDto dto :lista){
                LocalDate dataOriginal = dto.getData();

                // data SEM ano (ano fixo)
                LocalDate dataSemAno = LocalDate.of(
                        2000,
                        dataOriginal.getMonth(),
                        dataOriginal.getDayOfMonth()
                );

                Acumuladortemp acc= mapa.getOrDefault(dataSemAno, new Acumuladortemp());

                acc.somatotaltemp += dto.getTempMedia();
                acc.somatotalumid += dto.getUmdMedia();
                acc.quantidade++;

                mapa.put(dataSemAno,acc);
            }
        }
        // CONVERTE PARA MÉDIAS FINAIS
        List<TemperaturaDiariaDto> resultado = new ArrayList<>();

        for (var entry : mapa.entrySet()) {
            Acumuladortemp acc = entry.getValue();

            double mediaTemp = acc.somatotaltemp / acc.quantidade;
            double mediaUmid = acc.somatotalumid / acc.quantidade;

            TemperaturaDiariaDto temperaturaDiariaDto = new TemperaturaDiariaDto();

            temperaturaDiariaDto.setData(entry.getKey());
            temperaturaDiariaDto.setTempMedia(mediaTemp);
            temperaturaDiariaDto.setUmdMedia(mediaUmid);

            resultado.add(temperaturaDiariaDto);

        }
        resultado.sort(Comparator.comparing(TemperaturaDiariaDto::getData));
        return resultado;
    }

}
