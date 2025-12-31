package br.com.probabilidadechuva.controller;

import br.com.probabilidadechuva.dto.ChuvaDiariaDto;
import br.com.probabilidadechuva.dto.ClassficacaoChuvaDto;
import br.com.probabilidadechuva.dto.TemperaturaDiariaDto;
import br.com.probabilidadechuva.service.PrecipitacaoService;
import br.com.probabilidadechuva.service.TemperaturaService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cidade")
@CrossOrigin(origins = "*")
public class CidadeController {
    private final PrecipitacaoService service;
    private final TemperaturaService temperaturaService;


    public CidadeController(PrecipitacaoService service, TemperaturaService temperaturaService) {
        this.service = service;
        this.temperaturaService = temperaturaService;
    }

    @GetMapping("/medias")
    public List<ChuvaDiariaDto> mediasDiarias(
            @RequestParam String nome,
            @RequestParam int ano) throws IOException {

        return service.mediasDiariasDTO(nome, ano);
    }
    @GetMapping("/classificacao")
    public List<ClassficacaoChuvaDto>classificarChuva(
            @RequestParam String nome,
            @RequestParam int ano) throws IOException{
        return service.classificarChuva(nome, ano);
    }
    @GetMapping("/classificacaomedia")
    public List<ClassficacaoChuvaDto>mediaclassificarChuva(
            @RequestParam String nome)
            throws IOException {
            return  service.mediaClassificacaoDeChuva(nome);
    }
    @GetMapping("/dadosano")
    public Map<LocalDate, List<Double>>dadosAno(
            @RequestParam String nome,
            @RequestParam int ano) throws IOException{
        return service.lerdadosano(nome,ano);
    }
    @GetMapping("/probabilidade5dias")
    public double probabilidadeDias(
            @RequestParam String nome,
            @RequestParam String dia,
            @RequestParam String mes)
            throws IOException {
        if (dia == null || mes == null) {
            return 0;
        }
        return service.probabilidadeChuva5Dias(nome, dia, mes);
    }

    @GetMapping("/dadosanotemp")
    public Map<LocalDate,List<List<Double>>>dadosanotemp(
            @RequestParam String nome,
            @RequestParam int ano
    ) throws IOException {
        return temperaturaService.lerdadostemp(nome,ano);
    }
    @GetMapping("/mediadadosanotemp")
    public List<TemperaturaDiariaDto> mediadadosanotemp(
            @RequestParam String nome,
            @RequestParam int ano
    ) throws IOException {
        return temperaturaService.mediasTemp(nome,ano);
    }
    @GetMapping("/mediatotaltempumid")
    public List<TemperaturaDiariaDto> mediatotaltempumid(
        @RequestParam String nome)throws IOException{
            return temperaturaService.mediaTotalTempUmid(nome);
        }



}
