package com.bancode.sangue.api.controller;

import com.bancode.sangue.api.models.Candidato;
import com.bancode.sangue.api.services.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @PostMapping
    public ResponseEntity<String> salvarCandidatos(@RequestBody List<Candidato> candidatos) {
        candidatoService.salvarCandidatos(candidatos);
        return ResponseEntity.ok("Candidatos salvos com sucesso!");
    }
}