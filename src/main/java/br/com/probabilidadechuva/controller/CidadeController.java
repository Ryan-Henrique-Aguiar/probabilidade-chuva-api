package br.com.probabilidadechuva.controller;

import br.com.probabilidadechuva.service.PrecipitacaoService;
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

    public CidadeController(PrecipitacaoService service) {
        this.service = service;
    }

    @GetMapping("/medias")
    public Map<LocalDate, Double> mediasDiarias(
            @RequestParam String nome,
            @RequestParam int ano) throws IOException {

        return service.mediasDiarias(nome, ano);
    }
    @GetMapping("/classificar")
    public Map<LocalDate,String>classificarDiarias(
            @RequestParam String nome,
            @RequestParam int ano) throws IOException{
        return service.classificarChuva(nome, ano);
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
            @RequestParam int dia,
            @RequestParam int mes) throws IOException {

        return service.probabilidadeChuva5Dias(nome, dia, mes);
    }

}
