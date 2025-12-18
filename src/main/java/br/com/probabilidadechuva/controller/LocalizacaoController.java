package br.com.probabilidadechuva.controller;

import br.com.probabilidadechuva.service.LocalizacaoService;
import br.com.probabilidadechuva.util.CsvNameReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/localizacao")
@CrossOrigin(origins = "*")
public class LocalizacaoController {
    private final CsvNameReader csvNameReader;
    private final LocalizacaoService localizacaoService;

    public LocalizacaoController(CsvNameReader csvNameReader, LocalizacaoService localizacaoService) {
        this.csvNameReader = csvNameReader;
        this.localizacaoService = localizacaoService;
    }

    @GetMapping("/listarcidadesporestado")
    public Map<String, List<String>> getLocalizacoes() throws IOException {
        // Extrai cidades por estado
        Map<String, List<String>> cidadesPorEstado = localizacaoService.listarCidadesPorEstado();
        // Organiza por região
        return cidadesPorEstado;
    }
    @GetMapping("/listarcidades")
    public ArrayList<String> listarcidades(){
        return localizacaoService.listarCidades();
    }

}
