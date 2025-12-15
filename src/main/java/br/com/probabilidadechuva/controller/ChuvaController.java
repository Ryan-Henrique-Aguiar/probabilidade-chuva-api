package br.com.probabilidadechuva.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chuva")
public class ChuvaController {

    @GetMapping
    public void getAll(){

    }

}
